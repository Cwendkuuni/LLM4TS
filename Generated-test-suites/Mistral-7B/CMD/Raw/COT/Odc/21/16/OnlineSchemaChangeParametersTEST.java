import static org.junit.Assert.*;
import static com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType.*;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class OnlineSchemaChangeParametersTest {

 @Test
 public void testOnlineSchemaChangeParameters_DefaultConstructor() {
 OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
 assertEquals(OnlineSchemaChangeSqlType.UNKNOWN, parameters.getSqlType());
 assertNull(parameters.getSqlContent());
 assertNull(parameters.getErrorStrategy());
 assertEquals(0, parameters.getLockTableTimeOutSeconds());
 assertEquals(0, parameters.getSwapTableNameRetryTimes());
 assertNull(parameters.getOriginTableCleanStrategy());
 assertEquals(";", parameters.getDelimiter());
 assertNull(parameters.getLockUsers());
 assertEquals(SwapTableType.REPLACE, parameters.getSwapTableType());
 }

 @Test
 public void testOnlineSchemaChangeParameters_Constructor() {
 OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters(ALTER, "test", TaskErrorStrategy.CONTINUE, 5, 3,
 SwapTableType.APPEND, Arrays.asList("user1", "user2"));
 assertEquals(ALTER, parameters.getSqlType());
 assertEquals("test", parameters.getSqlContent());
 assertEquals(TaskErrorStrategy.CONTINUE, parameters.getErrorStrategy());
 assertEquals(5, parameters.getLockTableTimeOutSeconds());
 assertEquals(3, parameters.getSwapTableNameRetryTimes());
 assertEquals(SwapTableType.APPEND, parameters.getSwapTableType());
 assertEquals(Arrays.asList("user1", "user2"), parameters.getLockUsers());
 }

 @Test
 public void testIsContinueOnError() {
 OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
 assertFalse(parameters.isContinueOnError());

 parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
 assertTrue(parameters.isContinueOnError());
 }

 @Test
 public void testGenerateSubTaskParameters_EmptySqlContent() {
 OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
 ConnectionConfig connectionConfig = new ConnectionConfig();
 String schema = "testSchema";
 List<OnlineSchemaChangeScheduleTaskParameters> expected = new ArrayList<>();
 try {
 List<OnlineSchemaChangeScheduleTaskParameters> actual = parameters.generateSubTaskParameters(connectionConfig, schema);
 assertEquals(expected.size(), actual.size());
 } catch (Exception e) {
 fail("generateSubTaskParameters should not throw any exception with empty sqlContent");
 }
 }

 @Test
 public void testGenerateSubTaskParameters_ValidParameters() {
 OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters(ALTER, "test", TaskErrorStrategy.CONTINUE, 5, 3,
 SwapTableType.APPEND, Arrays.asList("user1", "user2"));
 ConnectionConfig connectionConfig = new ConnectionConfig();
 String schema = "testSchema";
 List<OnlineSchemaChangeScheduleTaskParameters> expected = Arrays.asList(
 new OnlineSchemaChangeScheduleTaskParameters("testSchema", "table1", "test", Arrays.asList("ALTER TABLE table1 ADD COLUMN new_column INT;"), ALTER),
 new OnlineSchemaChangeScheduleTaskParameters("testSchema", "table1", "test", Arrays.asList("ALTER TABLE table1 DROP COLUMN old_column;"), ALTER)
 );
 try {
 List<OnlineSchemaChangeScheduleTaskParameters> actual = parameters.generateSubTaskParameters(connectionConfig, schema);
 assertEquals(expected.size(), actual.size());
 for (int i = 0; i < expected.size(); i++) {
 OnlineSchemaChangeScheduleTaskParameters actualTaskParameters = actual.get(i);
 OnlineSchemaChangeScheduleTaskParameters expectedTaskParameters = expected.get(i);
 assertEquals(expectedTaskParameters.getDatabaseName(), actualTaskParameters.getDatabaseName());
 assertEquals(expectedTaskParameters.getOriginTableName(), actualTaskParameters.getOriginTableName());
 assertEquals(expectedTaskParameters.getSqlsToBeExecuted().size(), actualTaskParameters.getSqlsToBeExecuted().size());
 for (int j = 0; j < expectedTaskParameters.getSqlsToBeExecuted().size(); j++) {
 assertEquals(expectedTaskParameters.getSqlsToBeExecuted().get(j), actualTaskParameters.getSqlsToBeExecuted().get(j));
 }
 }
 } catch (Exception e) {
 fail("generateSubTaskParameters should not throw any exception with valid parameters");
 }
 }

 @Test
 public void testGenerateSubTaskParameters_InvalidParameters() {
 OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
 ConnectionConfig connectionConfig = new ConnectionConfig();
 String schema = "testSchema";
 List<OnlineSchemaChangeScheduleTaskParameters> expected = new ArrayList<>();
 try {
 List<OnlineSchemaChangeScheduleTaskParameters> actual = parameters.generateSubTaskParameters(connectionConfig, schema);
 fail("generateSubTaskParameters should throw an UnexpectedException with empty sqlContent");
 } catch (Exception e) {
 }

 parameters.setSqlContent("test");
 try {
 List<OnlineSchemaChangeScheduleTaskParameters> actual = parameters.generateSubTaskParameters(connectionConfig, schema);
 fail("generateSubTaskParameters should throw an UnexpectedException with invalid sqlContent format");
 } catch (Exception e) {
 }

 parameters.setSqlContent("test;ALTER TABLE test.table1 ADD COLUMN new_column INT;ALTER TABLE test.table1 ADD COLUMN new_column2 INT;");
 List<String> sqls = Arrays.asList("ALTER TABLE test.table1 ADD COLUMN new_column INT;", "ALTER TABLE test.table1 ADD COLUMN new_column2 INT;");
 try {
 List<OnlineSchemaChangeScheduleTaskParameters> actual = parameters.generateSubTaskParameters(connectionConfig, schema);
 assertEquals(sqls.size(), actual.size());
 for (int i = 0; i < sqls.size(); i++) {
 OnlineSchemaChangeScheduleTaskParameters actualTaskParameters = actual.get(i);
 String expectedSql = sqls.get(i);
 String actualSql = actualTaskParameters.getSqlsToBeExecuted().get(0);
 assertEquals(expectedSql, actualSql);
 }
 } catch (Exception e) {
 fail("generateSubTaskParameters should not throw any exception with valid sqlContent");
 }
 }

}