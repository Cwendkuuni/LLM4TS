import org.junit.Test;
import org.junit.Assert.*;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;

public class OnlineSchemaChangeParametersTest {

 @Test
 public void testValidOnlineSchemaChangeParameters() {
 OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
 params.setSqlType(OnlineSchemaChangeSqlType.CREATE);
 params.setSqlContent("CREATE TABLE test_table (id INT PRIMARY KEY);");
 params.setErrorStrategy(TaskErrorStrategy.CONCURRENT_ERROR_RETRY);
 params.setLockTableTimeOutSeconds(30);
 params.setSwapTableType(SwapTableType.SWAP_IN_PLACE);
 params.setOriginTableCleanStrategy(OriginTableCleanStrategy.DROP_TABLE);
 params.setDelimiter(";");
 params.setLockUsers(new ArrayList<>());

 // Test valid values
 assertTrue(params.isValid());
 }

 @Test(expected = AssertionError.class)
 public void testInvalidOnlineSchemaChangeParameters() {
 OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
 params.setSqlType(null);
 params.setErrorStrategy(null);
 params.setLockTableTimeOutSeconds(-1);
 params.setSwapTableType(null);
 params.setOriginTableCleanStrategy(null);

 // Test invalid values
 assertFalse(params.isValid());
 }

 @Test
 public void testGenerateSubTaskParameters() {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setDialectType("MySQL");

 OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
 params.setSqlType(OnlineSchemaChangeSqlType.ALTER);
 params.setSqlContent("ALTER TABLE test_table ADD COLUMN new_column INT;");
 params.setErrorStrategy(TaskErrorStrategy.CONCURRENT_ERROR_RETRY);
 params.setLockTableTimeOutSeconds(30);
 params.setSwapTableType(SwapTableType.SWAP_IN_PLACE);
 params.setOriginTableCleanStrategy(OriginTableCleanStrategy.DROP_TABLE);
 params.setDelimiter(";");
 params.setLockUsers(new ArrayList<>());

 List<OnlineSchemaChangeScheduleTaskParameters> taskParameters = params.generateSubTaskParameters(connectionConfig, "test_schema");
 assertEquals(1, taskParameters.size());

 OnlineSchemaChangeScheduleTaskParameters taskParam = taskParameters.get(0);
 assertEquals("ALTER TABLE test_schema.test_table ADD COLUMN new_column INT;", taskParam.getSqlsToBeExecuted().get(0));
 assertEquals("test_schema.test_table", taskParam.getOriginTableName());
 }

}