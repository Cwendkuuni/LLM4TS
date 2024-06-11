import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.onlineschemachange.model.OriginTableCleanStrategy;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;

public class OnlineSchemaChangeParametersTest {

 private OnlineSchemaChangeParameters parameters;

 @Before
 public void setup() {
 parameters = new OnlineSchemaChangeParameters();
 parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
 parameters.setSqlContent("ALTER TABLE test_table ADD COLUMN new_column INT;");
 parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
 parameters.setLockTableTimeOutSeconds(30);
 parameters.setSwapTableType(SwapTableType.SWAP_AND_RENAME);
 parameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.DROP);
 }

 @Test
 public void testValidOnlineSchemaChangeParameters() {
 assertNotNull(parameters);
 assertEquals(OnlineSchemaChangeSqlType.ALTER, parameters.getSqlType());
 assertNotNull(parameters.getSqlContent());
 assertEquals(TaskErrorStrategy.CONTINUE, parameters.getErrorStrategy());
 assertEquals(30, parameters.getLockTableTimeOutSeconds());
 assertEquals(SwapTableType.SWAP_AND_RENAME, parameters.getSwapTableType());
 assertEquals(OriginTableCleanStrategy.DROP, parameters.getOriginTableCleanStrategy());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidOnlineSchemaChangeParametersNullSqlContent() {
 parameters.setSqlContent(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidOnlineSchemaChangeParametersEmptySqlContent() {
 parameters.setSqlContent("");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidOnlineSchemaChangeParametersNullErrorStrategy() {
 parameters.setErrorStrategy(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidOnlineSchemaChangeParametersNegativeLockTableTimeOutSeconds() {
 parameters.setLockTableTimeOutSeconds(-1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidOnlineSchemaChangeParametersNullSwapTableType() {
 parameters.setSwapTableType(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidOnlineSchemaChangeParametersNullOriginTableCleanStrategy() {
 parameters.setOriginTableCleanStrategy(null);
 }

 @Test
 public void testGenerateSubTaskParameters() {
 List<String> expectedSqls = new ArrayList<>();
 expectedSqls.add("ALTER TABLE test_table ADD COLUMN new_column INT;");
 String newAlterStmt = "ALTER TABLE test_table RENAME TO test_table_new;";
 expectedSqls.add(newAlterStmt);
 List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameters.generateSubTaskParameters(new ConnectionConfig(), "test_schema");
 assertNotNull(subTaskParameters);
 assertEquals(1, subTaskParameters.size());
 OnlineSchemaChangeScheduleTaskParameters taskParameter = subTaskParameters.get(0);
 assertNotNull(taskParameter);
 assertEquals("test_schema", taskParameter.getDatabaseName());
 assertEquals("test_table", taskParameter.getOriginTableName());
 assertEquals(expectedSqls, taskParameter.getSqlsToBeExecuted());
 }

 @Test(expected = UnexpectedException.class)
 public void testGenerateSubTaskParametersException() {
 List<String> sqls = new ArrayList<>();
 sqls.add("ALTER TABLE test_table ADD COLUMN new_column INT;");
 try (SubTaskParameterFactory subTaskParameterFactory = new SubTaskParameterFactory(new ConnectionConfig(), "test_schema")) {
 List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameters.generateSubTaskParameters(new ConnectionConfig(), "test_schema");
 assertThrows(UnexpectedException.class, () -> subTaskParameters.get(0).getSqlsToBeExecuted().get(0));
 }
 }
}