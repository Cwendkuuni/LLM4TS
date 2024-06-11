import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.oceanbase.odc.core.flow.model.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;

public class OnlineSchemaChangeParametersTest {

 // Test case for the constructor and getter methods
 @Test
 public void testConstructorAndGetters() {
 OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
 // Test default values
 assertEquals(OnlineSchemaChangeSqlType.ALTER, parameters.getSqlType());
 assertNull(parameters.getSqlContent());
 assertEquals(TaskErrorStrategy.CONTINUE, parameters.getErrorStrategy());
 assertEquals(0, parameters.getLockTableTimeOutSeconds());
 assertEquals(0, parameters.getSwapTableNameRetryTimes());
 assertEquals(OnlineSchemaChangeScheduleTaskParameters.OriginTableCleanStrategy.DROP_AND_RECREATE, parameters.getOriginTableCleanStrategy());
 assertEquals(";", parameters.getDelimiter());
 assertNull(parameters.getLockUsers());
 assertEquals(SwapTableType.SWAP_IN_PLACE, parameters.getSwapTableType());

 // Test setter methods and getters
 parameters.setSqlType(OnlineSchemaChangeSqlType.CREATE);
 assertEquals(OnlineSchemaChangeSqlType.CREATE, parameters.getSqlType());
 parameters.setSqlContent("CREATE TABLE test (id INT PRIMARY KEY);");
 assertEquals("CREATE TABLE test (id INT PRIMARY KEY);", parameters.getSqlContent());
 parameters.setErrorStrategy(TaskErrorStrategy.ABORT);
 assertEquals(TaskErrorStrategy.ABORT, parameters.getErrorStrategy());
 parameters.setLockTableTimeOutSeconds(10);
 assertEquals(10, parameters.getLockTableTimeOutSeconds());
 parameters.setSwapTableNameRetryTimes(5);
 assertEquals(5, parameters.getSwapTableNameRetryTimes());
 parameters.setOriginTableCleanStrategy(OnlineSchemaChangeScheduleTaskParameters.OriginTableCleanStrategy.RENAME);
 assertEquals(OnlineSchemaChangeScheduleTaskParameters.OriginTableCleanStrategy.RENAME, parameters.getOriginTableCleanStrategy());
 parameters.setDelimiter("--");
 assertEquals("--", parameters.getDelimiter());
 parameters.setLockUsers(new ArrayList<>());
 parameters.getLockUsers().add("user1");
 assertEquals("user1", parameters.getLockUsers().get(0));
 parameters.setSwapTableType(SwapTableType.SWAP_TO_TEMP);
 assertEquals(SwapTableType.SWAP_TO_TEMP, parameters.getSwapTableType());
 }

 // Test method generateSubTaskParameters() with valid input
 @Test
 public void testGenerateSubTaskParameters() {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
 String schema = "test_schema";
 parameters.setSqlType(OnlineSchemaChangeSqlType.CREATE);
 parameters.setSqlContent("CREATE TABLE test (id INT PRIMARY KEY);");
 parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
 parameters.setLockTableTimeOutSeconds(10);
 parameters.setSwapTableNameRetryTimes(5);
 parameters.setOriginTableCleanStrategy(OnlineSchemaChangeScheduleTaskParameters.OriginTableCleanStrategy.DROP_AND_RECREATE);
 parameters.setDelimiter(";");
 List<String> lockUsers = new ArrayList<>();
 lockUsers.add("user1");
 parameters.setLockUsers(lockUsers);
 parameters.setSwapTableType(SwapTableType.SWAP_IN_PLACE);

 List<OnlineSchemaChangeScheduleTaskParameters> expectedSubTaskParameters = new ArrayList<>();
 OnlineSchemaChangeScheduleTaskParameters expectedParameter1 = new OnlineSchemaChangeScheduleTaskParameters();
 expectedParameter1.setDatabaseName("test_schema");
 expectedParameter1.setOriginTableName("test");
 expectedParameter1.setNewTableName("test_new");
 expectedSubTaskParameters.add(expectedParameter1);

 try {
 List<OnlineSchemaChangeScheduleTaskParameters> actualSubTaskParameters = parameters.generateSubTaskParameters(connectionConfig, schema);
 assertEquals(expectedSubTaskParameters, actualSubTaskParameters);
 } catch (UnexpectedException e) {
 fail("UnexpectedException was thrown");
 }
 }

 // Test method generateSubTaskParameters() with empty sqlContent
 @Test(expected = UnexpectedException.class)
 public void testGenerateSubTaskParametersEmptySqlContent() {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
 String schema = "test_schema";
 parameters.setSqlType(OnlineSchemaChangeSqlType.CREATE);
 parameters.setSqlContent("");
 parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
 parameters.setLockTableTimeOutSeconds(10);
 parameters.setSwapTableNameRetryTimes(5);
 parameters.setOriginTableCleanStrategy(OnlineSchemaChangeScheduleTaskParameters.OriginTableCleanStrategy.DROP_AND_RECREATE);
 parameters.setDelimiter(";");
 List<String> lockUsers = new ArrayList<>();
 lockUsers.add("user1");
 parameters.setLockUsers(lockUsers);
 parameters.setSwapTableType(SwapTableType.SWAP_IN_PLACE);

 try {
 List<OnlineSchemaChangeScheduleTaskParameters> actualSubTaskParameters = parameters.generateSubTaskParameters(connectionConfig, schema);
 fail("Unexpectedly generated subtask parameters with empty sqlContent");
 } catch (UnexpectedException e) {
 // Expected
 }
 }

 // Test method generateSubTaskParameters() with invalid dialect type
 @Test(expected = UnexpectedException.class)
 public void testGenerateSubTaskParametersInvalidDialectType() {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
 String schema = "test_schema";
 parameters.setSqlType(OnlineSchemaChangeSqlType.CREATE);
 parameters.setSqlContent("CREATE TABLE test (id INT PRIMARY KEY);");
 parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
 parameters.setLockTableTimeOutSeconds(10);
 parameters.setSwapTableNameRetryTimes(5);
 parameters.setOriginTableCleanStrategy(OnlineSchemaChangeScheduleTaskParameters.OriginTableCleanStrategy.DROP_AND_RECREATE);
 parameters.setDelimiter(";");
 List<String> lockUsers = new ArrayList<>();
 lockUsers.add("user1");
 parameters.setLockUsers(lockUsers);
 parameters.setSwapTableType(SwapTableType.SWAP_IN_PLACE);
 connectionConfig.setDialectType("InvalidDialectType");

 try {
 List<OnlineSchemaChangeScheduleTaskParameters> actualSubTaskParameters = parameters.generateSubTaskParameters(connectionConfig, schema);
 fail("Unexpectedly generated subtask parameters with invalid dialect type");
 } catch (UnexpectedException e) {
 // Expected
 }
 }

}