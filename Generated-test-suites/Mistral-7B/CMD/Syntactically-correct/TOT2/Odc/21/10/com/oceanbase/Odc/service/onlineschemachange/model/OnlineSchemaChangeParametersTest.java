import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.oceanbase.odc.core.connection.model.ConnectionConfig;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.model.TableIdentity;
import com.oceanbase.odc.service.common.util.SqlUtils;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;

public class OnlineSchemaChangeParametersTest {

 private OnlineSchemaChangeParameters parameters;
 private ConnectionConfig connectionConfig;

 @Before
 public void setup() {
 connectionConfig = new ConnectionConfig();
 parameters = new OnlineSchemaChangeParameters();
 }

 @Test
 public void testIsContinueOnError() {
 // Test case for isContinueOnError method with errorStrategy as CONTINUE
 parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
 assertTrue(parameters.isContinueOnError());

 // Test case for isContinueOnError method with errorStrategy as ABORT
 parameters.setErrorStrategy(TaskErrorStrategy.ABORT);
 assertFalse(parameters.isContinueOnError());
 }

 @Test
 public void testGenerateSubTaskParameters() {
 // Test case for generateSubTaskParameters with valid parameters
 parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
 parameters.setSqlContent("ALTER TABLE test_table ADD COLUMN new_column INT");
 parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
 parameters.setLockTableTimeOutSeconds(10);
 parameters.setSwapTableType(SwapTableType.SWAP_AND_REPLACE);

 List<OnlineSchemaChangeScheduleTaskParameters> taskParameters = parameters.generateSubTaskParameters(connectionConfig, "test_schema");
 assertEquals(1, taskParameters.size());

 // Check if the generated task parameters have the expected values
 OnlineSchemaChangeScheduleTaskParameters taskParameter = taskParameters.get(0);
 assertEquals("test_schema", taskParameter.getDatabaseName());
 assertEquals("test_table", taskParameter.getOriginTableName());
 assertEquals(OnlineSchemaChangeSqlType.ALTER, taskParameter.getSqlType());
 assertNotNull(taskParameter.getSqlsToBeExecuted());
 assertEquals(1, taskParameter.getSqlsToBeExecuted().size());

 String expectedAlterStmt = "ALTER TABLE test_table ADD COLUMN new_column INT";
 String actualAlterStmt = taskParameter.getSqlsToBeExecuted().get(0);
 assertEquals(expectedAlterStmt, actualAlterStmt);
 }

 @Test(expected = UnexpectedException.class)
 public void testGenerateSubTaskParametersWithException() {
 // Test case for generateSubTaskParameters with an exception
 parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
 parameters.setSqlContent("ALTER TABLE test_table ADD COLUMN new_column INT; -- Malformed SQL statement");
 parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
 parameters.setLockTableTimeOutSeconds(10);
 parameters.setSwapTableType(SwapTableType.SWAP_AND_REPLACE);

 List<String> sqls = SqlUtils.split(connectionConfig.getDialectType(), parameters.getSqlContent(), parameters.getDelimiter());
 try (SubTaskParameterFactory subTaskParameterFactory = new SubTaskParameterFactory(connectionConfig, "test_schema")) {
 parameters.generateSubTaskParameters(connectionConfig, "test_schema");
 fail("Expected UnexpectedException to be thrown");
 } catch (UnexpectedException e) {
 // Expected exception, do nothing
 }
 }
}