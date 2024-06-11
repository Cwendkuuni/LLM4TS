import org.junit.Test;
import static org.junit.Assert.*;

import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;

public class OnlineSchemaChangeParametersTest {

 // Test case for the constructor
 @Test
 public void testOnlineSchemaChangeParametersConstructor() {
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

 // Test case for setters and getters
 @Test
 public void testSettersAndGetters() {
 OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
 OnlineSchemaChangeSqlType sqlType = OnlineSchemaChangeSqlType.ALTER;
 String sqlContent = "ALTER TABLE test_table ADD COLUMN new_column INT";
 TaskErrorStrategy errorStrategy = TaskErrorStrategy.CONTINUE;
 int lockTableTimeOutSeconds = 30;
 int swapTableNameRetryTimes = 3;
 OriginTableCleanStrategy originTableCleanStrategy = OriginTableCleanStrategy.DELETE_OLD;
 String delimiter = ";";
 List<String> lockUsers = new ArrayList<>();
 lockUsers.add("user1");
 SwapTableType swapTableType = SwapTableType.SWAP_TO_NEW;

 parameters.setSqlType(sqlType);
 parameters.setSqlContent(sqlContent);
 parameters.setErrorStrategy(errorStrategy);
 parameters.setLockTableTimeOutSeconds(lockTableTimeOutSeconds);
 parameters.setSwapTableNameRetryTimes(swapTableNameRetryTimes);
 parameters.setOriginTableCleanStrategy(originTableCleanStrategy);
 parameters.setDelimiter(delimiter);
 parameters.setLockUsers(lockUsers);
 parameters.setSwapTableType(swapTableType);

 assertEquals(sqlType, parameters.getSqlType());
 assertEquals(sqlContent, parameters.getSqlContent());
 assertEquals(errorStrategy, parameters.getErrorStrategy());
 assertEquals(lockTableTimeOutSeconds, parameters.getLockTableTimeOutSeconds());
 assertEquals(swapTableNameRetryTimes, parameters.getSwapTableNameRetryTimes());
 assertEquals(originTableCleanStrategy, parameters.getOriginTableCleanStrategy());
 assertEquals(delimiter, parameters.getDelimiter());
 assertEquals(lockUsers, parameters.getLockUsers());
 assertEquals(swapTableType, parameters.getSwapTableType());
 }

 // Test case for isContinueOnError() method
 @Test
 public void testIsContinueOnError() {
 OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
 parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
 assertTrue(parameters.isContinueOnError());

 parameters.setErrorStrategy(TaskErrorStrategy.ABORT);
 assertFalse(parameters.isContinueOnError());
 }

 // Test case for generateSubTaskParameters() method with valid input
 @Test
 public void testGenerateSubTaskParametersWithValidInput() {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 String schema = "test_schema";
 OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
 OnlineSchemaChangeSqlType sqlType = OnlineSchemaChangeSqlType.ALTER;
 String sqlContent = "ALTER TABLE test_table ADD COLUMN new_column INT";
 parameters.setSqlType(sqlType);
 parameters.setSqlContent(sqlContent);
 List<String> expectedSqls = new ArrayList<>();
 expected.add("ALTER TABLE test_schema.test_table ADD COLUMN new_column INT");
 ConnectionConfig connectionConfigSpy = mock(ConnectionConfig.class);
 when(connectionConfigSpy.getDialectType()).thenReturn("mysql");
 try {
 List<OnlineSchemaChangeScheduleTaskParameters> result = parameters.generateSubTaskParameters(connectionConfigSpy, schema);
 List<String> actualSqls = new ArrayList<>();
 for (OnlineSchemaChangeScheduleTaskParameters taskParameter : result) {
 actualSqls.addAll(taskParameter.getSqlsToBeExecuted());
 }
 assertEquals(expectedSqls, actualSqls);
 } catch (Exception e) {
 fail("Exception should not be thrown");
 }
 }

 // Test case for generateSubTaskParameters() method with invalid input (empty sqlContent)
 @Test(expected = UnexpectedException.class)
 public void testGenerateSubTaskParametersWithEmptySqlContent() {
 OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
 parameters.setSqlContent("");
 ConnectionConfig connectionConfig = new ConnectionConfig();
 String schema = "test_schema";
 try {
 parameters.generateSubTaskParameters(connectionConfig, schema);
 fail("Exception should be thrown");
 } catch (Exception e) {
 verify(connectionConfig).getDialectType();
 }
 }
}