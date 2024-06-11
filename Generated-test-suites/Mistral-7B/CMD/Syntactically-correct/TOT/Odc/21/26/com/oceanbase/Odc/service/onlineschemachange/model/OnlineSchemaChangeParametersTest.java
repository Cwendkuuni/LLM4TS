import org.junit.Test;
import static org.junit.Assert.*;

import com.oceanbase.odc.core.shared.model.ConnectionConfig;
import com.oceanbase.odc.core.shared.model.TableIdentity;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;

public class OnlineSchemaChangeParametersTest {

 private ConnectionConfig connectionConfig = new ConnectionConfig();

 @Test
 public void testValidParameters() {
 OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
 parameters.setSqlType(OnlineSchemaChangeSqlType.CREATE);
 parameters.setSqlContent("CREATE TABLE IF NOT EXISTS test_table (id int PRIMARY KEY, name varchar(255));");
 parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
 parameters.setLockTableTimeOutSeconds(10);
 parameters.setSwapTableType(SwapTableType.SWAP_IN_PLACE);
 List<String> expectedUsers = new ArrayList<>();
 expectedUsers.add("user1");
 expectedUsers.add("user2");
 parameters.setLockUsers(expectedUsers);

 List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameters.generateSubTaskParameters(connectionConfig, "test_schema");
 assertNotNull(subTaskParameters);
 assertEquals(1, subTaskParameters.size());

 OnlineSchemaChangeScheduleTaskParameters taskParameter = subTaskParameters.get(0);
 TableIdentity key = new TableIdentity("test_schema", "test_table");
 assertTrue(taskParameter.getTaskParameters().containsKey(key));
 assertEquals(1, taskParameter.getSqlsToBeExecuted().size());
 assertTrue(taskParameter.getSqlsToBeExecuted().get(0).startsWith("CREATE TABLE IF NOT EXISTS test_table"));
 }

 @Test
 public void testEmptySqlContent() {
 OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
 parameters.setSqlType(OnlineSchemaChangeSqlType.CREATE);
 parameters.setSqlContent("");
 parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
 parameters.setLockTableTimeOutSeconds(10);
 parameters.setSwapTableType(SwapTableType.SWAP_IN_PLACE);
 List<String> expectedUsers = new ArrayList<>();
 expectedUsers.add("user1");
 expectedUsers.add("user2");
 parameters.setLockUsers(expectedUsers);

 try {
 parameters.generateSubTaskParameters(connectionConfig, "test_schema");
 fail("Exception expected");
 } catch (UnexpectedException e) {
 // Expected exception
 }
 }

 @Test
 public void testSqlWithMultipleStatements() {
 OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
 parameters.setSqlType(OnlineSchemaChangeSqlType.CREATE);
 parameters.setSqlContent("CREATE TABLE IF NOT EXISTS test_table1 (id int PRIMARY KEY, name varchar(255));" +
         "CREATE TABLE IF NOT EXISTS test_table2 (id int PRIMARY KEY, name varchar(255));");
 parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
 parameters.setLockTableTimeOutSeconds(10);
 parameters.setSwapTableType(SwapTableType.SWAP_IN_PLACE);
 List<String> expectedUsers = new ArrayList<>();
 expectedUsers.add("user1");
 expectedUsers.add("user2");
 parameters.setLockUsers(expectedUsers);

 List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameters.generateSubTaskParameters(connectionConfig, "test_schema");
 assertNotNull(subTaskParameters);
 assertEquals(2, subTaskParameters.size());

 for (OnlineSchemaChangeScheduleTaskParameters taskParameter : subTaskParameters) {
 TableIdentity key = new TableIdentity("test_schema", taskParameter.getOriginTableName());
 assertTrue(taskParameter.getTaskParameters().containsKey(key));
 assertEquals(1, taskParameter.getSqlsToBeExecuted().size());
 }
 }

 @Test
 public void testSqlWithInvalidDdlStatement() {
 OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
 parameters.setSqlType(OnlineSchemaChangeSqlType.CREATE);
 parameters.setSqlContent("INVALID STATEMENT;");
 parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
 parameters.setLockTableTimeOutSeconds(10);
 parameters.setSwapTableType(SwapTableType.SWAP_IN_PLACE);
 List<String> expectedUsers = new ArrayList<>();
 expectedUsers.add("user1");
 expectedUsers.add("user2");
 parameters.setLockUsers(expectedUsers);

 try {
 parameters.generateSubTaskParameters(connectionConfig, "test_schema");
 fail("Exception expected");
 } catch (UnexpectedException e) {
 // Expected exception
 }
 }

 @Test
 public void testSqlWithMissingSemicolonAtTheEnd() {
 OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
 parameters.setSqlType(OnlineSchemaChangeSqlType.CREATE);
 parameters.setSqlContent("CREATE TABLE IF NOT EXISTS test_table (id int PRIMARY KEY, name varchar(255) ");
 parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
 parameters.setLockTableTimeOutSeconds(10);
 parameters.setSwapTableType(SwapTableType.SWAP_IN_PLACE);
 List<String> expectedUsers = new ArrayList<>();
 expectedUsers.add("user1");
 expectedUsers.add("user2");
 parameters.setLockUsers(expectedUsers);

 try {
 parameters.generateSubTaskParameters(connectionConfig, "test_schema");
 fail("Exception expected");
 } catch (UnexpectedException e) {
 // Expected exception
 }
 }

 @Test
 public void testSqlWithComment() {
 OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
 parameters.setSqlType(OnlineSchemaChangeSqlType.CREATE);
 parameters.setSqlContent("-- Comment" +
         "CREATE TABLE IF NOT EXISTS test_table (id int PRIMARY KEY, name varchar(255));");
 parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
 parameters.setLockTableTimeOutSeconds(10);
 parameters.setSwapTableType(SwapTableType.SWAP_IN_PLACE);
 List<String> expectedUsers = new ArrayList<>();
 expectedUsers.add("user1");
 expectedUsers.add("user2");
 parameters.setLockUsers(expectedUsers);

 List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameters.generateSubTaskParameters(connectionConfig, "test_schema");
 assertNotNull(subTaskParameters);
 assertEquals(1, subTaskParameters.size());

 OnlineSchemaChangeScheduleTaskParameters taskParameter = subTaskParameters.get(0);
 TableIdentity key = new TableIdentity("test_schema", "test_table");
 assertTrue(taskParameter.getTaskParameters().containsKey(key));
 assertEquals(1, taskParameter.getSqlsToBeExecuted().size());
 assertTrue(taskParameter.getSqlsToBeExecuted().get(0).startsWith("CREATE TABLE IF NOT EXISTS test_table"));
 }

 @Test
 public void testSwapTableTypeIsDropAndTableDoesNotExist() {
 OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
 parameters.setSqlType(OnlineSchemaChangeSqlType.DROP);
 parameters.setSqlContent("DROP TABLE IF EXISTS test_table;");
 parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
 parameters.setLockTableTimeOutSeconds(10);
 parameters.setSwapTableType(SwapTableType.DROP);
 List<String> expectedUsers = new ArrayList<>();
 expectedUsers.add("user1");
 expectedUsers.add("user2");
 parameters.setLockUsers(expectedUsers);

 List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameters.generateSubTaskParameters(connectionConfig, "test_schema");
 assertNotNull(subTaskParameters);
 assertEquals(0, subTaskParameters.size());
 }

 @Test
 public void testSwapTableTypeIsDropAndTableExists() {
 OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
 parameters.setSqlType(OnlineSchemaChangeSqlType.DROP);
 parameters.setSqlContent("DROP TABLE IF EXISTS test_table;");
 parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
 parameters.setLockTableTimeOutSeconds(10);
 parameters.setSwapTableType(SwapTableType.DROP);
 connectionConfig.setDialectType("MySQL");
 parameters.setLockUsers(new ArrayList<>());

 try {
 List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameters.generateSubTaskParameters(connectionConfig, "test_schema");
 fail("Exception expected");
 } catch (UnexpectedException e) {
 // Expected exception
 }
 }

 @Test
 public void testSwapTableTypeIsSwapAndTableDoesNotExist() {
 OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
 parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
 parameters.setSqlContent("ALTER TABLE test_table ADD COLUMN new_column INT;");
 parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
 parameters.setLockTableTimeOutSeconds(10);
 parameters.setSwapTableType(SwapTableType.SWAP);
 List<String> expectedUsers = new ArrayList<>();
 expectedUsers.add("user1");
 expectedUsers.add("user2");
 parameters.setLockUsers(expectedUsers);

 List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameters.generateSubTaskParameters(connectionConfig, "test_schema");
 assertNotNull(subTaskParameters);
 assertEquals(1, subTaskParameters.size());

 OnlineSchemaChangeScheduleTaskParameters taskParameter = subTaskParameters.get(0);
 TableIdentity key = new TableIdentity("test_schema", "test_table");
 assertTrue(taskParameter.getTaskParameters().containsKey(key));
 assertEquals(1, taskParameter.getSqlsToBeExecuted().size());
 assertTrue(taskParameter.getSqlsToBeExecuted().get(0).startsWith("ALTER TABLE test_table " +
         "SWAP test_schema.test_table test_schema.test_table_temp;"));
 }

 @Test
 public void testSwapTableTypeIsSwapAndTableExists() {
 OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
 parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
 parameters.setSqlContent("ALTER TABLE test_table ADD COLUMN new_column INT;");
 parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
 parameters.setLockTableTimeOutSeconds(10);
 parameters.setSwapTableType(SwapTableType.SWAP);
 connectionConfig.setDialectType("MySQL");
 parameters.setLockUsers(new ArrayList<>());

 try {
 List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameters.generateSubTaskParameters(connectionConfig, "test_schema");
 fail("Exception expected");
 } catch (UnexpectedException e) {
 // Expected exception
 }
 }
}