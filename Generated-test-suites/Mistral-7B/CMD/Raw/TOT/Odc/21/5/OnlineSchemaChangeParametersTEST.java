import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;

public class OnlineSchemaChangeParametersTest {

 private OnlineSchemaChangeParameters parameters;

 @Before
 public void setup() {
 // Set up a sample OnlineSchemaChangeParameters object
 parameters = new OnlineSchemaChangeParameters();
 parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
 parameters.setSqlContent("ALTER TABLE my_table ADD COLUMN new_column INT;");
 parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
 parameters.setLockTableTimeOutSeconds(10);
 parameters.setSwapTableType(SwapTableType.SWAP_IN_PLACE);
 parameters.setDelimiter(";");
 parameters.setLockUsers(new ArrayList<>());
 }

 @Test
 public void testIsContinueOnError() {
 // Test isContinueOnError method
 assertTrue(parameters.isContinueOnError());
 // Test with a different error strategy
 parameters.setErrorStrategy(TaskErrorStrategy.TERMINATE);
 assertFalse(parameters.isContinueOnError());
 }

 @Test
 public void testGenerateSubTaskParameters() {
 // Test generateSubTaskParameters method
 // (Note: This test assumes that SubTaskParameterFactory and DdlUtils classes are properly mocked or stubbed)
 List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameters.generateSubTaskParameters(new ConnectionConfig(), "test_schema");
 // Assert the number of generated subtask parameters
 assertEquals(1, subTaskParameters.size());
 // Assert the generated subtask parameter's properties
 OnlineSchemaChangeScheduleTaskParameters subTaskParameter = subTaskParameters.get(0);
 assertEquals("test_schema", subTaskParameter.getDatabaseName());
 assertEquals("my_table", subTaskParameter.getOriginTableName());
 assertEquals(OnlineSchemaChangeSqlType.ALTER, subTaskParameter.getSqlType());
 assertFalse(subTaskParameter.isRollback());
 assertFalse(subTaskParameter.isDropTable());
 }

}