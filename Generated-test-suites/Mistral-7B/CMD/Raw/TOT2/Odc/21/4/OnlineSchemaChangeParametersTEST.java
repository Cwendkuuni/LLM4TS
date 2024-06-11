import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import java.util.Arrays;
import java.util.List;

@DisplayName("OnlineSchemaChangeParameters Test")
class OnlineSchemaChangeParametersTest {

 @Test
 @DisplayName("Test OnlineSchemaChangeParameters constructor with valid input")
 void testValidConstructor() {
 OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
 parameters.setSqlType(OnlineSchemaChangeSqlType.CREATE);
 parameters.setSqlContent("CREATE TABLE test (id INT PRIMARY KEY);");
 parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
 parameters.setLockTableTimeOutSeconds(30);
 parameters.setSwapTableType(SwapTableType.IMMEDIATE);
 parameters.setDelimiter(";");
 parameters.setLockUsers(Arrays.asList("user1", "user2"));
 parameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.DROP_AND_CREATE);

 assertEquals(OnlineSchemaChangeSqlType.CREATE, parameters.getSqlType());
 assertEquals("CREATE TABLE test (id INT PRIMARY KEY);", parameters.getSqlContent());
 assertEquals(TaskErrorStrategy.CONTINUE, parameters.getErrorStrategy());
 assertEquals(30, parameters.getLockTableTimeOutSeconds());
 assertEquals(SwapTableType.IMMEDIATE, parameters.getSwapTableType());
 assertEquals(";", parameters.getDelimiter());
 assertEquals(Arrays.asList("user1", "user2"), parameters.getLockUsers());
 assertEquals(OriginTableCleanStrategy.DROP_AND_CREATE, parameters.getOriginTableCleanStrategy());
 }

 @Test
 @DisplayName("Test isContinueOnError method with valid input")
 void testIsContinueOnError() {
 OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
 parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
 assertTrue(parameters.isContinueOnError());
 }

 @Test
 @DisplayName("Test isContinueOnError method with invalid input")
 void testIsContinueOnErrorInvalidInput() {
 OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
 parameters.setErrorStrategy(TaskErrorStrategy.RETHROW);
 assertFalse(parameters.isContinueOnError());
 }

 @Test
 @DisplayName("Test generateSubTaskParameters method with valid input")
 void testGenerateSubTaskParametersValidInput() {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
 parameters.setSqlType(OnlineSchemaChangeSqlType.CREATE);
 parameters.setSqlContent("CREATE TABLE test (id INT PRIMARY KEY);");
 parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
 parameters.setLockTableTimeOutSeconds(30);
 parameters.setSwapTableType(SwapTableType.IMMEDIATE);
 parameters.setDelimiter(";");
 parameters.setLockUsers(Arrays.asList("user1", "user2"));
 parameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.DROP_AND_CREATE);

 List<OnlineSchemaChangeScheduleTaskParameters> expectedSubTasks = Arrays.asList(
 new OnlineSchemaChangeScheduleTaskParameters("test", "CREATE TABLE test (id INT PRIMARY KEY);", "test", OnlineSchemaChangeSqlType.CREATE),
 new OnlineSchemaChangeScheduleTaskParameters("test", "CREATE TABLE test (id INT PRIMARY KEY);", "test", OnlineSchemaChangeSqlType.CREATE)
 );

 List<OnlineSchemaChangeScheduleTaskParameters> actualSubTasks = parameters.generateSubTaskParameters(connectionConfig, "test");
 assertEquals(expectedSubTasks, actualSubTasks);
 }

 @Test
 @DisplayName("Test generateSubTaskParameters method with empty SQL content")
 void testGenerateSubTaskParametersEmptySqlContent() {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
 parameters.setSqlType(OnlineSchemaChangeSqlType.CREATE);
 parameters.setSqlContent("");
 parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
 parameters.setLockTableTimeOutSeconds(30);
 parameters.setSwapTableType(SwapTableType.IMMEDIATE);
 parameters.setDelimiter(";");
 parameters.setLockUsers(Arrays.asList("user1", "user2"));
 parameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.DROP_AND_CREATE);

 List<OnlineSchemaChangeScheduleTaskParameters> actualSubTasks = parameters.generateSubTaskParameters(connectionConfig, "test");
 assertTrue(actualSubTasks.isEmpty());
 }

 @Test
 @DisplayName("Test generateSubTaskParameters method with null ConnectionConfig")
 void testGenerateSubTaskParametersNullConnectionConfig() {
 OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
 parameters.setSqlType(OnlineSchemaChangeSqlType.CREATE);
 parameters.setSqlContent("CREATE TABLE test (id INT PRIMARY KEY);");
 parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
 parameters.setLockTableTimeOutSeconds(30);
 parameters.setSwapTableType(SwapTableType.IMMEDIATE);
 parameters.setDelimiter(";");
 parameters.setLockUsers(Arrays.asList("user1", "user2"));
 parameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.DROP_AND_CREATE);
 ConnectionConfig connectionConfig = null;

 assertThrows(NullPointerException.class, () -> {
 parameters.generateSubTaskParameters(connectionConfig, "test");
 });
 }

 @Test
 @DisplayName("Test generateSubTaskParameters method with invalid SQL content")
 void testGenerateSubTaskParametersInvalidSqlContent() {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
 parameters.setSqlType(OnlineSchemaChangeSqlType.CREATE);
 parameters.setSqlContent("CREATE NOT A TABLE test (id INT PRIMARY KEY);"); // invalid SQL statement
 parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
 parameters.setLockTableTimeOutSeconds(30);
 parameters.setSwapTableType(SwapTableType.IMMEDIATE);
 parameters.setDelimiter(";");
 parameters.setLockUsers(Arrays.asList("user1", "user2"));
 parameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.DROP_AND_CREATE);

 List<OnlineSchemaChangeScheduleTaskParameters> actualSubTasks = parameters.generateSubTaskParameters(connectionConfig, "test");
 assertTrue(actualSubTasks.isEmpty());
 }
}