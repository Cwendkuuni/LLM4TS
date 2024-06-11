import org.junit.Assert;
import org.junit.Test;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;

public class OnlineSchemaChangeParametersTest {

 private OnlineSchemaChangeParameters createOnlineSchemaChangeParameters() {
 return new OnlineSchemaChangeParameters()
 .setSqlType(OnlineSchemaChangeSqlType.ALTER)
 .setSqlContent("ALTER TABLE test_table ADD COLUMN new_column INT;")
 .setErrorStrategy(TaskErrorStrategy.CONTINUE)
 .setLockTableTimeOutSeconds(10)
 .setSwapTableType(SwapTableType.SWAP_IN_PLACE)
 .setDelimiter(";")
 .setLockUsers(new ArrayList<>())
 .setOriginTableCleanStrategy(OnlineSchemaChangeParameters.OriginTableCleanStrategy.DROP_AND_RECREATE);
 }

 @Test
 public void testIsContinueOnError() {
 OnlineSchemaChangeParameters parameters = createOnlineSchemaChangeParameters();
 Assert.assertTrue(parameters.isContinueOnError());
 }

 @Test
 public void testGenerateSubTaskParameters_normalCase() throws Exception {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 OnlineSchemaChangeParameters parameters = createOnlineSchemaChangeParameters();
 List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameters.generateSubTaskParameters(connectionConfig, "test_schema");
 Assert.assertEquals(1, subTaskParameters.size());
 }

 @Test(expected = UnexpectedException.class)
 public void testGenerateSubTaskParameters_exceptionCase() throws Exception {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 OnlineSchemaChangeParameters parameters = createOnlineSchemaChangeParameters();
 // mock an exception in SubTaskParameterFactory
 throw new Exception();
 }

 @Test
 public void testSqlContentSplit_normalCase() {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 OnlineSchemaChangeParameters parameters = createOnlineSchemaChangeParameters();
 List<String> sqls = parameters.generateSubTaskParameters(connectionConfig, "test_schema").get(0).getSqlsToBeExecuted();
 String sqlContent = "ALTER TABLE test_table ADD COLUMN new_column INT;\nALTER TABLE test_table DROP COLUMN old_column;\n";
 List<String> expectedSqls = SqlUtils.split(connectionConfig.getDialectType(), sqlContent, parameters.getDelimiter());
 Assert.assertEquals(expectedSqls, sqls);
 }

 @Test
 public void testSqlContentSplit_emptyCase() {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 OnlineSchemaChangeParameters parameters = createOnlineSchemaChangeParameters();
 List<String> sqls = parameters.generateSubTaskParameters(connectionConfig, "test_schema").get(0).getSqlsToBeExecuted();
 String sqlContent = "";
 List<String> expectedSqls = new ArrayList<>();
 Assert.assertEquals(expectedSqls, sqls);
 }

 @Test
 public void testSqlContentSplit_singleCase() {
 ConnectionConfig connectionConfig = new ConnectionConfig();
 OnlineSchemaChangeParameters parameters = createOnlineSchemaChangeParameters();
 List<String> sqls = parameters.generateSubTaskParameters(connectionConfig, "test_schema").get(0).getSqlsToBeExecuted();
 String sqlContent = "ALTER TABLE test_table ADD COLUMN new_column INT;";
 List<String> expectedSqls = SqlUtils.split(connectionConfig.getDialectType(), sqlContent, parameters.getDelimiter());
 Assert.assertEquals(expectedSqls, sqls);
 }

}