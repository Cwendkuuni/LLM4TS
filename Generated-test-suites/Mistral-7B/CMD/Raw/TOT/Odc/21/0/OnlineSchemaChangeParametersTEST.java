import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OnlineSchemaChangeParametersTest {

 private OnlineSchemaChangeParameters parameters;

 @BeforeAll
 public void setUp() {
 // Set up the test object with valid values
 this.parameters = new OnlineSchemaChangeParameters();
 this.parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
 this.parameters.setSqlContent("ALTER TABLE my_table ADD COLUMN new_column INT;");
 this.parameters.setErrorStrategy(TaskErrorStrategy.ABORT);
 this.parameters.setLockTableTimeOutSeconds(30);
 this.parameters.setSwapTableType(SwapTableType.SAVEPOINT);
 this.parameters.setDelimiter(";");
 this.parameters.setLockUsers(new ArrayList<>());
 }

 @Test
 public void testValidOnlineSchemaChangeParameters() {
 // Test valid OnlineSchemaChangeParameters with all required fields set
 assertTrue(this.parameters.isTaskValid());
 assertEquals(OnlineSchemaChangeSqlType.ALTER, this.parameters.getSqlType());
 assertNotNull(this.parameters.getSqlContent());
 assertNotNull(this.parameters.getErrorStrategy());
 assertNotNull(this.parameters.getLockTableTimeOutSeconds());
 assertNotNull(this.parameters.getSwapTableType());
 assertNotNull(this.parameters.getDelimiter());
 assertEmpty(this.parameters.getLockUsers());
 }

 @Test
 public void testEmptySqlContent() {
 // Test OnlineSchemaChangeParameters with empty sql content
 this.parameters.setSqlContent("");
 assertFalse(this.parameters.isTaskValid());
 assertNull(this.parameters.getSqlContent());
 }

 @Test
 public void testNullSqlType() {
 // Test OnlineSchemaChangeParameters with null sql type
 this.parameters.setSqlType(null);
 assertFalse(this.parameters.isTaskValid());
 assertNull(this.parameters.getSqlType());
 }

 @Test
 public void testInvalidErrorStrategy() {
 // Test OnlineSchemaChangeParameters with invalid error strategy
 this.parameters.setErrorStrategy(null);
 assertFalse(this.parameters.isTaskValid());
 assertNull(this.parameters.getErrorStrategy());
 }

 @Test
 public void testNegativeLockTableTimeOutSeconds() {
 // Test OnlineSchemaChangeParameters with negative lock table time out seconds
 this.parameters.setLockTableTimeOutSeconds(-1);
 assertFalse(this.parameters.isTaskValid());
 assertNotNull(this.parameters.getLockTableTimeOutSeconds());
 }

 @Test
 public void testEmptyLockUsers() {
 // Test OnlineSchemaChangeParameters with empty lock users list
 this.parameters.setLockUsers(null);
 assertEmpty(this.parameters.getLockUsers());
 }

 @Test
 public void testNullSwapTableType() {
 // Test OnlineSchemaChangeParameters with null swap table type
 this.parameters.setSwapTableType(null);
 assertFalse(this.parameters.isTaskValid());
 assertNull(this.parameters.getSwapTableType());
 }

 @Test(expected = UnexpectedException.class)
 public void testGenerateSubTaskParametersWithException() {
 // Test generating subtask parameters with an exception
 ConnectionConfig connectionConfig = new ConnectionConfig();
 String schema = "test_schema";
 try {
 this.parameters.generateSubTaskParameters(connectionConfig, schema);
 fail("Exception expected");
 } catch (UnexpectedException e) {
 // Expected exception
 }
 }

}