import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OnlineSchemaChangeParametersTest {

 private OnlineSchemaChangeParameters parameters;

 @BeforeAll
 public void setup() {
 // Basic test case for constructor
 parameters = new OnlineSchemaChangeParameters();

 // Basic test case for getters and setters
 parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
 parameters.setSqlContent("CREATE TABLE test_table (id INT PRIMARY KEY);");
 parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
 parameters.setLockTableTimeOutSeconds(30);
 parameters.setSwapTableType(SwapTableType.ASYNC);
 parameters.setDelimiter(";");
 parameters.setLockUsers(new ArrayList<>());
 }

 @Test
 public void testIsContinueOnError() {
 // Basic test case for isContinueOnError method
 assertTrue(parameters.isContinueOnError());

 // Edge case test for isContinueOnError method with errorStrategy as STOP
 parameters.setErrorStrategy(TaskErrorStrategy.STOP);
 assertFalse(parameters.isContinueOnError());
 }

 @Test
 public void testGenerateSubTaskParameters() {
 // Basic test case for generateSubTaskParameters method
 ConnectionConfig connectionConfig = new ConnectionConfig();
 String schema = "test_schema";
 List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameters.generateSubTaskParameters(connectionConfig, schema);
 assertEquals(1, subTaskParameters.size());

 // Edge case test for generateSubTaskParameters method with empty sqlContent
 parameters.setSqlContent("");
 subTaskParameters = parameters.generateSubTaskParameters(connectionConfig, schema);
 assertEquals(0, subTaskParameters.size());
 }

 @Test
 public void testGenerateSubTaskParametersWithException() {
 // Exception test case for generateSubTaskParameters method
 try {
 parameters.generateSubTaskParameters(null, "test_schema");
 fail();
 } catch (Exception e) {
 // Expected exception
 assertTrue(e instanceof IllegalArgumentException);
 }
 }
}