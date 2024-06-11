import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OnlineSchemaChangeParametersTest {

 private OnlineSchemaChangeParameters createValidOnlineSchemaChangeParameters() {
 return new OnlineSchemaChangeParameters()
 .setSqlType(OnlineSchemaChangeSqlType.CREATE)
 .setSqlContent("CREATE TABLE test_table (id INT PRIMARY KEY)")
 .setErrorStrategy(TaskErrorStrategy.CONTINUE)
 .setLockTableTimeOutSeconds(10)
 .setSwapTableType(SwapTableType.SWAP_IN_PLACE)
 .setOriginTableCleanStrategy(OriginTableCleanStrategy.KEEP_ORIGIN_TABLE)
 .setDelimiter(";")
 .setLockUsers(new ArrayList<>())
 .setSwapTableNameRetryTimes(3);
 }

 @Test
 public void testValidOnlineSchemaChangeParameters() {
 OnlineSchemaChangeParameters onlineSchemaChangeParameters = createValidOnlineSchemaChangeParameters();
 Assertions.assertNull(onlineSchemaChangeParameters.getNewTableName());
 Assertions.assertEquals(OnlineSchemaChangeSqlType.CREATE, onlineSchemaChangeParameters.getSqlType());
 Assertions.assertEquals(TaskErrorStrategy.CONTINUE, onlineSchemaChangeParameters.getErrorStrategy());
 Assertions.assertEquals(10, onlineSchemaChangeParameters.getLockTableTimeOutSeconds());
 Assertions.assertEquals(SwapTableType.SWAP_IN_PLACE, onlineSchemaChangeParameters.getSwapTableType());
 Assertions.assertEquals(OriginTableCleanStrategy.KEEP_ORIGIN_TABLE, onlineSchemaChangeParameters.getOriginTableCleanStrategy());
 Assertions.assertEquals(";", onlineSchemaChangeParameters.getDelimiter());
 Assertions.assertFalse(onlineSchemaChangeParameters.isContinueOnError());
 Assertions.assertTrue(onlineSchemaChangeParameters.getLockUsers().isEmpty());
 Assertions.assertEquals(3, onlineSchemaChangeParameters.getSwapTableNameRetryTimes());
 }

 @Test
 public void testNullSqlType() {
 Assertions.assertThrows(NullPointerException.class, () -> new OnlineSchemaChangeParameters().setSqlType(null));
 }

 @Test
 public void testEmptySqlContent() {
 OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
 Assertions.assertThrows(AssertionError.class, () -> onlineSchemaChangeParameters.setSqlContent(""));
 }

 @Test
 public void testNullErrorStrategy() {
 Assertions.assertThrows(NullPointerException.class, () -> new OnlineSchemaChangeParameters().setErrorStrategy(null));
 }

 @Test
 public void testNegativeLockTableTimeOutSeconds() {
 OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
 Assertions.assertThrows(IllegalArgumentException.class, () -> onlineSchemaChangeParameters.setLockTableTimeOutSeconds(-1));
 }

 @Test
 public void testNullLockUsers() {
 OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
 onlineSchemaChangeParameters.setLockUsers(null);
 Assertions.assertThrows(NullPointerException.class, () -> onlineSchemaChangeParameters.getLockUsers());
 }

 @Test
 public void testEmptyLockUsers() {
 OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
 Assertions.assertEquals(0, onlineSchemaChangeParameters.getLockUsers().size());
 }

 @Test
 public void testLockUsersWithNullElement() {
 OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
 List<String> lockUsers = Arrays.asList("user1", null, "user3");
 onlineSchemaChangeParameters.setLockUsers(lockUsers);
 Assertions.assertEquals(2, onlineSchemaChangeParameters.getLockUsers().size());
 Assertions.assertEquals("user1", onlineSchemaChangeParameters.getLockUsers().get(0));
 Assertions.assertEquals("user3", onlineSchemaChangeParameters.getLockUsers().get(1));
 }

 @Test
 public void testInvalidSwapTableType() {
 OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
 Assertions.assertThrows(IllegalArgumentException.class, () -> onlineSchemaChangeParameters.setSwapTableType(null));
 }

 @Test
 public void testInvalidOriginTableCleanStrategy() {
 OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
 Assertions.assertThrows(IllegalArgumentException.class, () -> onlineSchemaChangeParameters.setOriginTableCleanStrategy(null));
 }

 @Test
 public void testInvalidDelimiter() {
 OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
 Assertions.assertThrows(IllegalArgumentException.class, () -> onlineSchemaChangeParameters.setDelimiter("\u0000"));
 }

 @Test
 public void testNegativeSwapTableNameRetryTimes() {
 OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
 Assertions.assertThrows(IllegalArgumentException.class, () -> onlineSchemaChangeParameters.setSwapTableNameRetryTimes(-1));
 }

 @Test
 public void testGenerateSubTaskParametersWithEmptySqls() {
 OnlineSchemaChangeParameters onlineSchemaChangeParameters = createValidOnlineSchemaChangeParameters();
 ConnectionConfig connectionConfig = new ConnectionConfig();
 String schema = "test_schema";
 List<OnlineSchemaChangeScheduleTaskParameters> taskParameters = onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, schema);
 Assertions.assertEquals(0, taskParameters.size());
 }

 @Test
 public void testGenerateSubTaskParametersWithValidSqls() {
 OnlineSchemaChangeParameters onlineSchemaChangeParameters = createValidOnlineSchemaChangeParameters();
 ConnectionConfig connectionConfig = new ConnectionConfig();
 String schema = "test_schema";
 String sqlContent = "CREATE TABLE test_table1 (id INT PRIMARY KEY),\nCREATE TABLE test_table2 (id INT PRIMARY KEY)";
 List<OnlineSchemaChangeScheduleTaskParameters> taskParameters = onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, schema);
 List<OnlineSchemaChangeScheduleTaskParameters> expectedTaskParameters = Arrays.asList(
 new OnlineSchemaChangeScheduleTaskParameters("test_schema", "test_table1", Arrays.asList("CREATE TABLE test_table1 (id INT PRIMARY KEY)")),
 new OnlineSchemaChangeScheduleTaskParameters("test_schema", "test_table2", Arrays.asList("CREATE TABLE test_table2 (id INT PRIMARY KEY)"))
 );
 Assertions.assertEquals(expectedTaskParameters, taskParameters);
 }

}