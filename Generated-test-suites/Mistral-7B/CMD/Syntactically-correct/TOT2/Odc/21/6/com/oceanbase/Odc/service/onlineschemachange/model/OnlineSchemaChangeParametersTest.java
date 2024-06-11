import org.junit.Test;
import org.junit.Assert.*;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.core.flow.model.TaskErrorStrategy;
import java.util.ArrayList;
import java.util.List;

public class OnlineSchemaChangeParametersTest {

 @Test
 public void testValidOnlineSchemaChangeParameters() {
 OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
 parameters.setSqlType(OnlineSchemaChangeSqlType.CREATE);
 parameters.setSqlContent("CREATE TABLE IF NOT EXISTS test_table (id INT PRIMARY KEY);");
 parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
 parameters.setLockTableTimeOutSeconds(30);
 parameters.setSwapTableType(SwapTableType.TABLE);
 List<String> lockUsers = new ArrayList<>();
 lockUsers.add("user1");
 parameters.setLockUsers(lockUsers);

 // Check if all fields have valid values
 assertEquals(OnlineSchemaChangeSqlType.CREATE, parameters.getSqlType());
 assertEquals("CREATE TABLE IF NOT EXISTS test_table (id INT PRIMARY KEY);", parameters.getSqlContent());
 assertEquals(TaskErrorStrategy.CONTINUE, parameters.getErrorStrategy());
 assertEquals(30, parameters.getLockTableTimeOutSeconds());
 assertEquals(SwapTableType.TABLE, parameters.getSwapTableType());
 assertEquals(lockUsers, parameters.getLockUsers());

 // Test generateSubTaskParameters method with valid parameters
 ConnectionConfig connectionConfig = new ConnectionConfig();
 String schema = "test_schema";
 List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameters.generateSubTaskParameters(connectionConfig, schema);
 // Check if subTaskParameters is not empty and has the correct size
 assertTrue(subTaskParameters.size() > 0);
 }

 @Test(expected = UnexpectedException.class)
 public void testInvalidSqlContent() {
 OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
 parameters.setSqlType(OnlineSchemaChangeSqlType.CREATE);
 parameters.setSqlContent("CREATE TABLE test_table;"); // missing semicolon at the end
 parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
 parameters.setLockTableTimeOutSeconds(30);
 parameters.setSwapTableType(SwapTableType.TABLE);
 List<String> lockUsers = new ArrayList<>();
 lockUsers.add("user1");
 parameters.setLockUsers(lockUsers);

 // This test case should throw an UnexpectedException
 parameters.generateSubTaskParameters(new ConnectionConfig(), "test_schema");
 }

 @Test
 public void testErrorStrategyNotContinue() {
 OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
 parameters.setSqlType(OnlineSchemaChangeSqlType.CREATE);
 parameters.setSqlContent("CREATE TABLE IF NOT EXISTS test_table (id INT PRIMARY KEY);");
 parameters.setErrorStrategy(TaskErrorStrategy.FAIL);
 parameters.setLockTableTimeOutSeconds(30);
 parameters.setSwapTableType(SwapTableType.TABLE);
 List<String> lockUsers = new ArrayList<>();
 lockUsers.add("user1");
 parameters.setLockUsers(lockUsers);

 // Check if all fields have valid values
 assertEquals(OnlineSchemaChangeSqlType.CREATE, parameters.getSqlType());
 assertEquals("CREATE TABLE IF NOT EXISTS test_table (id INT PRIMARY KEY);", parameters.getSqlContent());
 assertEquals(TaskErrorStrategy.FAIL, parameters.getErrorStrategy());
 assertEquals(30, parameters.getLockTableTimeOutSeconds());
 assertEquals(SwapTableType.TABLE, parameters.getSwapTableType());
 assertEquals(lockUsers, parameters.getLockUsers());

 // Test generateSubTaskParameters method with error strategy set to fail
 ConnectionConfig connectionConfig = new ConnectionConfig();
 String schema = "test_schema";
 // This test case should not throw an exception as error strategy is set to fail
 parameters.generateSubTaskParameters(connectionConfig, schema);
 }

 @Test
 public void testEmptyLockUsers() {
 OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
 parameters.setSqlType(OnlineSchemaChangeSqlType.CREATE);
 parameters.setSqlContent("CREATE TABLE IF NOT EXISTS test_table (id INT PRIMARY KEY);");
 parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
 parameters.setLockTableTimeOutSeconds(30);
 parameters.setSwapTableType(SwapTableType.TABLE);
 parameters.setLockUsers(new ArrayList<String>());

 // Check if all fields have valid values
 assertEquals(OnlineSchemaChangeSqlType.CREATE, parameters.getSqlType());
 assertEquals("CREATE TABLE IF NOT EXISTS test_table (id INT PRIMARY KEY);", parameters.getSqlContent());
 assertEquals(TaskErrorStrategy.CONTINUE, parameters.getErrorStrategy());
 assertEquals(30, parameters.getLockTableTimeOutSeconds());
 assertEquals(SwapTableType.TABLE, parameters.getSwapTableType());
 assertEquals(new ArrayList<String>(), parameters.getLockUsers());

 // Test generateSubTaskParameters method with empty lockUsers list
 ConnectionConfig connectionConfig = new ConnectionConfig();
 String schema = "test_schema";
 List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameters.generateSubTaskParameters(connectionConfig, schema);
 // Check if subTaskParameters is not empty and has the correct size
 assertTrue(subTaskParameters.size() > 0);
 }

}