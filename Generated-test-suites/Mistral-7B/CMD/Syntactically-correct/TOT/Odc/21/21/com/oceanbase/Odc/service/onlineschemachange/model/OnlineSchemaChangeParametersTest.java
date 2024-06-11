import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.oceanbase.odc.core.connection.model.ConnectionConfig;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeSqlType;

/**
 * Test class for OnlineSchemaChangeParameters
 *
 * @author yaobin
 * @since 4.2.0
 */
public class OnlineSchemaChangeParametersTest {

 private OnlineSchemaChangeParameters parameters;

 @Before
 public void setUp() {
 // Initialize test data
 ConnectionConfig connectionConfig = new ConnectionConfig();
 parameters = new OnlineSchemaChangeParameters();
 parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
 parameters.setSqlContent("ALTER TABLE test_table RENAME TO new_test_table;");
 parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
 parameters.setLockTableTimeOutSeconds(30);
 parameters.setSwapTableType(SwapTableType.MASTER_MASTER);
 }

 @Test
 public void testGenerateSubTaskParameters() {
 // Test generating subtask parameters for ALTER statement
 List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameters.generateSubTaskParameters(connectionConfig, "test_schema");
 assertEquals(1, subTaskParameters.size());
 OnlineSchemaChangeScheduleTaskParameters taskParameter = subTaskParameters.get(0);
 assertEquals("ALTER TABLE test_table RENAME TO new_test_table;", taskParameter.getSqlsToBeExecuted().get(0));
 assertEquals(OnlineSchemaChangeSqlType.ALTER, taskParameter.getSqlType());

 // Test generating subtask parameters for CREATE statement
 parameters.setSqlContent("CREATE TABLE test_table (id INT PRIMARY KEY, name VARCHAR(10));");
 subTaskParameters = parameters.generateSubTaskParameters(connectionConfig, "test_schema");
 assertEquals(1, subTaskParameters.size());
 taskParameter = subTaskParameters.get(0);
 assertEquals("CREATE TABLE test_table (id INT PRIMARY KEY, name VARCHAR(10));", taskParameter.getSqlsToBeExecuted().get(0));
 assertEquals(OnlineSchemaChangeSqlType.CREATE, taskParameter.getSqlType());
 }

 @Test(expected = UnexpectedException.class)
 public void testGenerateSubTaskParametersWithException() {
 // Test generating subtask parameters with exception
 parameters.setSqlContent("CREATE TABLE test_table (id INT PRIMARY KEY, name VARCHAR(10)); -- comment");
 try {
 parameters.generateSubTaskParameters(connectionConfig, "test_schema");
 fail();
 } catch (UnexpectedException e) {
 // Expected exception
 }
 }

 @Test
 public void testIsContinueOnError() {
 // Test continue on error strategy
 assertTrue(parameters.isContinueOnError());

 // Test stop on error strategy
 parameters.setErrorStrategy(TaskErrorStrategy.STOP);
 assertFalse(parameters.isContinueOnError());
 }
}