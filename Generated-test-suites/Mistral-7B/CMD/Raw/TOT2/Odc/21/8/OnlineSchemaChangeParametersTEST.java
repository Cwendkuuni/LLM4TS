import org.junit.Test;
import org.junit.Assert.*;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class OnlineSchemaChangeParametersTest {

 // Test case for constructor
 @Test
 public void testConstructor() {
 OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
 Assert.assertNull(parameters.getSqlType());
 Assert.assertNull(parameters.getSqlContent());
 Assert.assertNull(parameters.getErrorStrategy());
 Assert.assertNull(parameters.getLockTableTimeOutSeconds());
 Assert.assertNull(parameters.getSwapTableNameRetryTimes());
 Assert.assertNull(parameters.getOriginTableCleanStrategy());
 Assert.assertNull(parameters.getDelimiter());
 Assert.assertNull(parameters.getLockUsers());
 Assert.assertNull(parameters.getSwapTableType());
 }

 // Test case for setters and getters
 @Test
 public void testSettersAndGetters() {
 OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
 OnlineSchemaChangeSqlType sqlType = OnlineSchemaChangeSqlType.ALTER;
 String sqlContent = "ALTER TABLE table_name ADD column_name INT";
 TaskErrorStrategy errorStrategy = TaskErrorStrategy.CONTINUE;
 Integer lockTableTimeOutSeconds = 30;
 Integer swapTableNameRetryTimes = 5;
 OriginTableCleanStrategy originTableCleanStrategy = OriginTableCleanStrategy.CLEAN;
 String delimiter = ";";
 List<String> lockUsers = Arrays.asList("user1", "user2");
 SwapTableType swapTableType = SwapTableType.SWAP_TO_TEMP_TABLE;

 parameters.setSqlType(sqlType);
 parameters.setSqlContent(sqlContent);
 parameters.setErrorStrategy(errorStrategy);
 parameters.setLockTableTimeOutSeconds(lockTableTimeOutSeconds);
 parameters.setSwapTableNameRetryTimes(swapTableNameRetryTimes);
 parameters.setOriginTableCleanStrategy(originTableCleanStrategy);
 parameters.setDelimiter(delimiter);
 parameters.setLockUsers(lockUsers);
 parameters.setSwapTableType(swapTableType);

 Assert.assertEquals(sqlType, parameters.getSqlType());
 Assert.assertEquals(sqlContent, parameters.getSqlContent());
 Assert.assertEquals(errorStrategy, parameters.getErrorStrategy());
 Assert.assertEquals(lockTableTimeOutSeconds, parameters.getLockTableTimeOutSeconds());
 Assert.assertEquals(swapTableNameRetryTimes, parameters.getSwapTableNameRetryTimes());
 Assert.assertEquals(originTableCleanStrategy, parameters.getOriginTableCleanStrategy());
 Assert.assertEquals(delimiter, parameters.getDelimiter());
 Assert.assertEquals(lockUsers, parameters.getLockUsers());
 Assert.assertEquals(swapTableType, parameters.getSwapTableType());
 }

 // Test case for isContinueOnError()
 @Test
 public void testIsContinueOnError() {
 OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
 parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
 assertTrue(parameters.isContinueOnError());

 parameters.setErrorStrategy(TaskErrorStrategy.ABORT);
 assertFalse(parameters.isContinueOnError());
 }

 // Test case for generateSubTaskParameters() with valid input
 @Test
 public void testGenerateSubTaskParametersValidInput() {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 String schema = "test_schema";
 OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
 parameters.setSqlType(OnlineSchemaChangeSqlType.CREATE);
 parameters.setSqlContent("CREATE TABLE table_name (id INT PRIMARY KEY)");
 parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
 parameters.setLockTableTimeOutSeconds(30);
 parameters.setSwapTableNameRetryTimes(5);
 parameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.CLEAN);
 parameters.setDelimiter(";");
 List<String> lockUsers = Arrays.asList("user1", "user2");
 parameters.setSwapTableType(SwapTableType.SWAP_TO_TEMP_TABLE);

 List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameters.generateSubTaskParameters(connectionConfig, schema);
 assertFalse(subTaskParameters.isEmpty());
 }

 // Test case for generateSubTaskParameters() with empty sqlContent
 @Test(expected = UnexpectedException.class)
 public void testGenerateSubTaskParametersEmptySqlContent() {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 String schema = "test_schema";
 OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
 parameters.setSqlType(OnlineSchemaChangeSqlType.CREATE);
 parameters.setSqlContent("");
 parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
 parameters.setLockTableTimeOutSeconds(30);
 parameters.setSwapTableNameRetryTimes(5);
 parameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.CLEAN);
 parameters.setDelimiter(";");
 List<String> lockUsers = Arrays.asList("user1", "user2");
 parameters.setSwapTableType(SwapTableType.SWAP_TO_TEMP_TABLE);

 List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameters.generateSubTaskParameters(connectionConfig, schema);
 fail("generateSubTaskParameters() should throw an UnexpectedException with empty sqlContent");
 }

 // Test case for generateSubTaskParameters() with invalid sqlContent
 @Test(expected = UnexpectedException.class)
 public void testGenerateSubTaskParametersInvalidSqlContent() {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 String schema = "test_schema";
 OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
 parameters.setSqlType(OnlineSchemaChangeSqlType.CREATE);
 parameters.setSqlContent("ALTER TABLE table_name column_name INT; ALTER TABLE table_name column_name INT");
 parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
 parameters.setLockTableTimeOutSeconds(30);
 parameters.setSwapTableNameRetryTimes(5);
 parameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.CLEAN);
 parameters.setDelimiter(";");
 List<String> lockUsers = Arrays.asList("user1", "user2");
 parameters.setSwapTableType(SwapTableType.SWAP_TO_TEMP_TABLE);

 List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameters.generateSubTaskParameters(connectionConfig, schema);
 fail("generateSubTaskParameters() should throw an UnexpectedException with invalid sqlContent");
 }
}