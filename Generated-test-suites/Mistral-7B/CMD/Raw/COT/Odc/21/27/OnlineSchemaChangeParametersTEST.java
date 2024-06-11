import org.junit.Assert;
import org.junit.Test;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;

public class OnlineSchemaChangeParametersTest {

 private OnlineSchemaChangeParameters createTestObject() {
 return new OnlineSchemaChangeParameters()
 // Set the desired values for test
 .setSqlType(OnlineSchemaChangeSqlType.ALTER)
 .setSqlContent("CREATE TABLE test_table (id INT PRIMARY KEY)")
 .setErrorStrategy(TaskErrorStrategy.CONTINUE)
 .setLockTableTimeOutSeconds(30)
 .setSwapTableType(SwapTableType.SWAP_IN_PLACE)
 .setLockUsers(new ArrayList<>())
 .setOriginTableCleanStrategy(OriginTableCleanStrategy.RENAME);
 }

 @Test
 public void testIsContinueOnError() {
 OnlineSchemaChangeParameters params = createTestObject();
 Assert.assertTrue(params.isContinueOnError());
 }

 @Test(expected = IllegalStateException.class)
 public void testEmptyConstructor() {
 new OnlineSchemaChangeParameters();
 }

 @Test
 public void testGenerateSubTaskParameters_normal() {
 OnlineSchemaChangeParameters params = createTestObject();
 // Given
 ConnectionConfig connectionConfig = new ConnectionConfig();
 String schema = "test_schema";
 // When
 List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = params.generateSubTaskParameters(connectionConfig, schema);
 // Then
 Assert.assertEquals(1, subTaskParameters.size());
 }

 @Test(expected = UnexpectedException.class)
 public void testGenerateSubTaskParameters_exception() {
 OnlineSchemaChangeParameters params = createTestObject();
 // Given
 ConnectionConfig connectionConfig = new ConnectionConfig();
 String schema = "test_schema";
 // When & Then
 params.generateSubTaskParameters(connectionConfig, schema);
 }

 @Test
 public void testGenerateSubTaskParameters_emptySqlContent() {
 OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
 params.setSqlType(OnlineSchemaChangeSqlType.ALTER);
 params.setSqlContent("");
 // Given
 ConnectionConfig connectionConfig = new ConnectionConfig();
 String schema = "test_schema";
 // When
 List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = params.generateSubTaskParameters(connectionConfig, schema);
 // Then
 Assert.assertEquals(0, subTaskParameters.size());
 }

 @Test
 public void testGenerateSubTaskParameters_invalidSqlContent() {
 OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
 params.setSqlType(OnlineSchemaChangeSqlType.ALTER);
 params.setSqlContent("INVALID_SQL");
 // Given
 ConnectionConfig connectionConfig = new ConnectionConfig();
 String schema = "test_schema";
 // When
 List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = params.generateSubTaskParameters(connectionConfig, schema);
 // Then
 Assert.assertEquals(0, subTaskParameters.size());
 }

 @Test
 public void testGenerateSubTaskParameters_sqlsSplitFailure() {
 OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
 params.setSqlType(OnlineSchemaChangeSqlType.ALTER);
 params.setSqlContent("INVALID_SQL;INVALID_SQL;INVALID_SQL");
 // Given
 ConnectionConfig connectionConfig = new ConnectionConfig();
 String schema = "test_schema";
 // When
 List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = params.generateSubTaskParameters(connectionConfig, schema);
 // Then
 Assert.assertEquals(0, subTaskParameters.size());
 }

}