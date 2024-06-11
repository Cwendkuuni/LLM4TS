import org.junit.Test;
import org.junit.Assert.*;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.core.flow.model.TaskErrorStrategy;
import java.util.ArrayList;
import java.util.List;

public class OnlineSchemaChangeParametersTest {

 // Test case for constructor
 @Test
 public void testConstructor() {
 OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
 // Check default values
 assertEquals(OnlineSchemaChangeSqlType.CREATE, params.getSqlType());
 assertEquals(TaskErrorStrategy.ABORT, params.getErrorStrategy());
 assertEquals(0, params.getLockTableTimeOutSeconds());
 assertEquals(0, params.getSwapTableNameRetryTimes());
 assertEquals(OnlineSchemaChangeScheduleTaskParameters.OriginTableCleanStrategy.DROP, params.getOriginTableCleanStrategy());
 assertEquals(";", params.getDelimiter());
 Assert.assertNull(params.getLockUsers());
 assertEquals(SwapTableType.DISABLE_WRITE, params.getSwapTableType());
 }

 // Test case for getters and setters
 @Test
 public void testGettersAndSetters() {
 OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
 params.setSqlType(OnlineSchemaChangeSqlType.ALTER);
 params.setErrorStrategy(TaskErrorStrategy.CONTINUE);
 params.setLockTableTimeOutSeconds(10);
 params.setSwapTableNameRetryTimes(5);
 params.setOriginTableCleanStrategy(OnlineSchemaChangeScheduleTaskParameters.OriginTableCleanStrategy.RENAME);
 params.setDelimiter(":");
 List<String> lockUsers = new ArrayList<>();
 lockUsers.add("user1");
 params.setLockUsers(lockUsers);
 params.setSwapTableType(SwapTableType.ENABLE_WRITE);

 assertEquals(OnlineSchemaChangeSqlType.ALTER, params.getSqlType());
 assertEquals(TaskErrorStrategy.CONTINUE, params.getErrorStrategy());
 assertEquals(10, params.getLockTableTimeOutSeconds());
 assertEquals(5, params.getSwapTableNameRetryTimes());
 assertEquals(OnlineSchemaChangeScheduleTaskParameters.OriginTableCleanStrategy.RENAME, params.getOriginTableCleanStrategy());
 assertEquals(":", params.getDelimiter());
 assertEquals(lockUsers, params.getLockUsers());
 assertEquals(SwapTableType.ENABLE_WRITE, params.getSwapTableType());
 }

 // Test case for isContinueOnError()
 @Test
 public void testIsContinueOnError() {
 OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
 params.setErrorStrategy(TaskErrorStrategy.CONTINUE);
 assertTrue(params.isContinueOnError());

 params.setErrorStrategy(TaskErrorStrategy.ABORT);
 assertFalse(params.isContinueOnError());
 }

 // Test case for generateSubTaskParameters()
 @Test
 public void testGenerateSubTaskParameters() {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
 String schema = "test_schema";
 List<String> sqls = new ArrayList<>();
 sqls.add("CREATE TABLE test_table (id INT);");
 sqls.add("ALTER TABLE test_table ADD COLUMN new_column INT;");
 params.setSqlContent(String.join(params.getDelimiter(), sqls));
 params.setSqlType(OnlineSchemaChangeSqlType.CREATE_AND_ALTER);
 params.setErrorStrategy(TaskErrorStrategy.CONTINUE);
 params.setLockTableTimeOutSeconds(10);
 params.setSwapTableNameRetryTimes(5);
 params.setOriginTableCleanStrategy(OnlineSchemaChangeScheduleTaskParameters.OriginTableCleanStrategy.DROP);
 params.setDelimiter(";");
 params.setLockUsers(new ArrayList<>());
 params.setSwapTableType(SwapTableType.DISABLE_WRITE);

 try {
 List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = params.generateSubTaskParameters(connectionConfig, schema);
 assertEquals(2, subTaskParameters.size());
 for (OnlineSchemaChangeScheduleTaskParameters taskParameter : subTaskParameters) {
 assertEquals("test_schema", taskParameter.getDatabaseName());
 assertEquals("test_table", taskParameter.getOriginTableName());
 }
 } catch (Exception e) {
 fail("Exception should not be thrown: " + e.getMessage());
 }
 }

 // Test case for edge cases: empty sqlContent
 @Test(expected = UnexpectedException.class)
 public void testGenerateSubTaskParametersEmptySqlContent() {
 OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
 params.setSqlContent("");
 ConnectionConfig connectionConfig = new ConnectionConfig();
 String schema = "test_schema";
 try {
 params.generateSubTaskParameters(connectionConfig, schema);
 fail("Exception should be thrown with empty sqlContent");
 } catch (UnexpectedException e) {
 // expected
 }
 }

 // Test case for exception handling scenarios: unexpected exception
 @Test(expected = UnexpectedException.class)
 public void testGenerateSubTaskParametersUnexpectedException() {
 OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
 ConnectionConfig connectionConfig = new ConnectionConfig();
 String schema = "test_schema";
 try {
 throw new RuntimeException("Unexpected exception");
 } catch (Exception e) {
 params.setSqlContent(e.getMessage());
 }
 try {
 params.generateSubTaskParameters(connectionConfig, schema);
 fail("Exception should be thrown with unexpected exception");
 } catch (UnexpectedException e) {
 // expected
 }
 }

}