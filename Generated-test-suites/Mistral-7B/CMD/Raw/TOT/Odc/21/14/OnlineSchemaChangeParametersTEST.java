import org.junit.Test;
import static org.junit.Assert.*;

import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.onlineschemachange.model.OriginTableCleanStrategy;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;

public class OnlineSchemaChangeParametersTest {

 @Test
 public void testValidParameters() {
 OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
 params.setSqlType(OnlineSchemaChangeSqlType.CREATE);
 params.setSqlContent("CREATE TABLE my_table (id INT PRIMARY KEY, name VARCHAR(50))");
 params.setErrorStrategy(TaskErrorStrategy.CONTINUE);
 params.setLockTableTimeOutSeconds(30);
 params.setSwapTableType(SwapTableType.SWAP_TABLE);
 params.setOriginTableCleanStrategy(OriginTableCleanStrategy.KEEP_DATA);
 List<String> lockUsers = new ArrayList<>();
 lockUsers.add("user1");
 params.setLockUsers(lockUsers);

 // Check if all fields are set correctly
 assertEquals(OnlineSchemaChangeSqlType.CREATE, params.getSqlType());
 assertEquals("CREATE TABLE my_table (id INT PRIMARY KEY, name VARCHAR(50))", params.getSqlContent());
 assertEquals(TaskErrorStrategy.CONTINUE, params.getErrorStrategy());
 assertEquals(30, params.getLockTableTimeOutSeconds());
 assertEquals(SwapTableType.SWAP_TABLE, params.getSwapTableType());
 assertEquals(OriginTableCleanStrategy.KEEP_DATA, params.getOriginTableCleanStrategy());
 assertEquals(lockUsers, params.getLockUsers());
 }

 @Test(expected = AssertionError.class)
 public void testInvalidErrorStrategy() {
 OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
 params.setSqlType(OnlineSchemaChangeSqlType.CREATE);
 params.setSqlContent("CREATE TABLE my_table (id INT PRIMARY KEY, name VARCHAR(50))");
 params.setErrorStrategy(-1);
 params.setLockTableTimeOutSeconds(30);
 params.setSwapTableType(SwapTableType.SWAP_TABLE);
 params.setOriginTableCleanStrategy(OriginTableCleanStrategy.KEEP_DATA);
 List<String> lockUsers = new ArrayList<>();
 lockUsers.add("user1");
 params.setLockUsers(lockUsers);
 }

 @Test
 public void testGenerateSubTaskParameters() {
 OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
 params.setSqlType(OnlineSchemaChangeSqlType.ALTER);
 params.setErrorStrategy(TaskErrorStrategy.CONTINUE);
 params.setLockTableTimeOutSeconds(30);
 params.setSwapTableType(SwapTableType.SWAP_TABLE);
 params.setOriginTableCleanStrategy(OriginTableCleanStrategy.KEEP_DATA);
 params.setDelimiter(";");
 params.setSqlContent("ALTER TABLE my_table ADD COLUMN new_column INT; ALTER TABLE my_table RENAME TO my_table_new;");
 ConnectionConfig connectionConfig = new ConnectionConfig();
 String schema = "test_schema";
 List<OnlineSchemaChangeScheduleTaskParameters> taskParameters = params.generateSubTaskParameters(connectionConfig, schema);
 assertEquals(2, taskParameters.size());
 for (OnlineSchemaChangeScheduleTaskParameters param : taskParameters) {
 // Check if table identity and sqls are set correctly
 TableIdentity tableIdentity = param.getTableIdentity();
 assertEquals("test_schema", tableIdentity.getDatabaseName());
 assertEquals("my_table", tableIdentity.getOriginTableName());
 List<String> sqlsToBeExecuted = param.getSqlsToBeExecuted();
 assertEquals(2, sqlsToBeExecuted.size());
 assertEquals("ALTER TABLE my_table ADD COLUMN new_column INT", sqlsToBeExecuted.get(0));
 assertEquals("ALTER TABLE my_table RENAME TO my_table_new", sqlsToBeExecuted.get(1));
 }
 }
}