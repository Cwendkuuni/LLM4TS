import org.junit.Test;
import static org.junit.Assert.*;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;

public class OnlineSchemaChangeParametersTest {

 // Test case 1: Basic constructor test
 @Test
 public void testBasicConstructor() {
 OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
 assertNull(parameters.getSqlType());
 assertNull(parameters.getSqlContent());
 assertNull(parameters.getErrorStrategy());
 assertNull(parameters.getLockTableTimeOutSeconds());
 assertNull(parameters.getSwapTableNameRetryTimes());
 assertNull(parameters.getOriginTableCleanStrategy());
 assertNull(parameters.getDelimiter());
 assertNull(parameters.getLockUsers());
 assertNull(parameters.getSwapTableType());
 }

 // Test case 2: Constructor with valid arguments
 @Test
 public void testValidConstructor() {
 OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters()
 .setSqlType(OnlineSchemaChangeSqlType.CREATE)
 .setSqlContent("CREATE TABLE test (id INT PRIMARY KEY);")
 .setErrorStrategy(TaskErrorStrategy.ABORT)
 .setLockTableTimeOutSeconds(30)
 .setSwapTableNameRetryTimes(5)
 .setOriginTableCleanStrategy(OriginTableCleanStrategy.DROP)
 .setDelimiter("|")
 .setLockUsers(new ArrayList<>())
 .setSwapTableType(SwapTableType.IN_PLACE);

 assertEquals(OnlineSchemaChangeSqlType.CREATE, parameters.getSqlType());
 assertEquals("CREATE TABLE test (id INT PRIMARY KEY);", parameters.getSqlContent());
 assertEquals(TaskErrorStrategy.ABORT, parameters.getErrorStrategy());
 assertEquals(30, parameters.getLockTableTimeOutSeconds());
 assertEquals(5, parameters.getSwapTableNameRetryTimes());
 assertEquals(OriginTableCleanStrategy.DROP, parameters.getOriginTableCleanStrategy());
 assertEquals("|", parameters.getDelimiter());
 assertFalse(parameters.getLockUsers().isEmpty());
 assertEquals(SwapTableType.IN_PLACE, parameters.getSwapTableType());
 }

 // Test case 3: Check isContinueOnError method
 @Test
 public void testIsContinueOnError() {
 OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters()
 .setErrorStrategy(TaskErrorStrategy.CONTINUE);
 assertTrue(parameters.isContinueOnError());

 parameters.setErrorStrategy(TaskErrorStrategy.ABORT);
 assertFalse(parameters.isContinueOnError());
 }

 // Test case 4: Check generateSubTaskParameters method
 @Test
 public void testGenerateSubTaskParameters() {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters()
 .setSqlType(OnlineSchemaChangeSqlType.CREATE)
 .setSqlContent("CREATE TABLE test (id INT PRIMARY KEY);")
 .setErrorStrategy(TaskErrorStrategy.CONTINUE)
 .setLockTableTimeOutSeconds(30)
 .setSwapTableNameRetryTimes(5)
 .setOriginTableCleanStrategy(OriginTableCleanStrategy.DROP)
 .setDelimiter("|")
 .setLockUsers(new ArrayList<>())
 .setSwapTableType(SwapTableType.IN_PLACE);

 List<String> sqls = new ArrayList<>();
 sqls.add("CREATE TABLE test1 (id INT PRIMARY KEY);");
 sqls.add("CREATE TABLE test2 (id INT PRIMARY KEY);");

 Map<TableIdentity, OnlineSchemaChangeScheduleTaskParameters> expectedTaskParameters = new LinkedHashMap<>();

 TableIdentity tableIdentity1 = new TableIdentity("test_database", "test1");
 OnlineSchemaChangeScheduleTaskParameters taskParameters1 = new OnlineSchemaChangeScheduleTaskParameters()
 .setDatabaseName("test_database")
 .setOriginTableName("test1")
 .setNewTableName("test1_new")
 .setSqlsToBeExecuted(new ArrayList<>());
 expectedTaskParameters.put(tableIdentity1, taskParameters1);

 TableIdentity tableIdentity2 = new TableIdentity("test_database", "test2");
 OnlineSchemaChangeScheduleTaskParameters taskParameters2 = new OnlineSchemaChangeScheduleTaskParameters()
 .setDatabaseName("test_database")
 .setOriginTableName("test2")
 .setNewTableName("test2_new")
 .setSqlsToBeExecuted(new ArrayList<>());
 expectedTaskParameters.put(tableIdentity2, taskParameters2);

 try {
 List<OnlineSchemaChangeScheduleTaskParameters> actualTaskParameters = parameters.generateSubTaskParameters(connectionConfig, "test_database");
 assertEquals(expectedTaskParameters.size(), actualTaskParameters.size());
 for (OnlineSchemaChangeScheduleTaskParameters actualTaskParameter : actualTaskParameters) {
 TableIdentity actualTableIdentity = actualTaskParameter.getTableIdentity();
 assertEquals(expectedTaskParameters.get(actualTableIdentity).getDatabaseName(), actualTableIdentity.getDatabaseName());
 assertEquals(expectedTaskParameters.get(actualTableIdentity).getOriginTableName(), actualTableIdentity.getOriginTableName());
 }
 } catch (UnexpectedException e) {
 fail("Unexpected exception: " + e.getMessage());
 }
 }
}