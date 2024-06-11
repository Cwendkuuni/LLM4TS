import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.core.connection.model.ConnectionConfig;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.model.TableIdentity;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.onlineschemachange.model.SubTaskParameterFactory;
import com.oceanbase.odc.service.onlineschemachange.subtask.SwapTableType;

public class OnlineSchemaChangeParametersTest {

 private OnlineSchemaChangeParameters parameters;
 private ConnectionConfig connectionConfig;

 @Before
 public void setup() {
 connectionConfig = new ConnectionConfig();
 connectionConfig.setDialectType("MySQL");
 parameters = new OnlineSchemaChangeParameters();
 }

 @Test
 public void testBasic() {
 // test default values
 assertNull(parameters.getSqlType());
 assertNull(parameters.getSqlContent());
 assertNull(parameters.getErrorStrategy());
 assertNull(parameters.getLockTableTimeOutSeconds());
 assertNull(parameters.getSwapTableType());
 assertSame(TaskErrorStrategy.IGNORE, parameters.getErrorStrategy());
 assertNull(parameters.getOriginTableCleanStrategy());
 assertNull(parameters.getDelimiter());
 assertNull(parameters.getLockUsers());

 // test setters and getters
 OnlineSchemaChangeSqlType sqlType = OnlineSchemaChangeSqlType.CREATE;
 String sqlContent = "CREATE TABLE test (id INT PRIMARY KEY)";
 parameters.setSqlType(sqlType);
 parameters.setSqlContent(sqlContent);
 assertEquals(sqlType, parameters.getSqlType());
 assertEquals(sqlContent, parameters.getSqlContent());

 parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
 assertEquals(TaskErrorStrategy.CONTINUE, parameters.getErrorStrategy());

 parameters.setLockTableTimeOutSeconds(10);
 assertEquals(10, parameters.getLockTableTimeOutSeconds().intValue());

 parameters.setSwapTableType(SwapTableType.SWAP_AFTER);
 assertEquals(SwapTableType.SWAP_AFTER, parameters.getSwapTableType());

 parameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.KEEP_OLD);
 assertEquals(OriginTableCleanStrategy.KEEP_OLD, parameters.getOriginTableCleanStrategy());

 parameters.setDelimiter("|");
 assertEquals("|", parameters.getDelimiter());

 List<String> lockUsers = Arrays.asList("user1", "user2");
 parameters.setLockUsers(lockUsers);
 assertEquals(lockUsers, parameters.getLockUsers());
 }

 @Test
 public void testGenerateSubTaskParameters() {
 parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
 parameters.setSqlContent("ALTER TABLE test ADD COLUMN col INT");
 parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
 parameters.setLockTableTimeOutSeconds(5);
 parameters.setSwapTableType(SwapTableType.SWAP_BEFORE);
 parameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.TRUNCATE);
 parameters.setDelimiter(";");
 parameters.setLockUsers(Arrays.asList("user1", "user2"));

 List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameters.generateSubTaskParameters(connectionConfig, "testdb");
 assertNotNull(subTaskParameters);
 assertFalse(subTaskParameters.isEmpty());

 OnlineSchemaChangeScheduleTaskParameters taskParameter = subTaskParameters.get(0);
 TableIdentity key = new TableIdentity("testdb", "test");
 assertSame(taskParameter, subTaskParameters.get(subTaskParameters.size() - 1));
 assertEquals(key, taskParameter.getTableIdentity());
 assertEquals("ALTER TABLE test ADD COLUMN col INT", taskParameter.getSqlContent());
 assertEquals(OnlineSchemaChangeSqlType.ALTER, taskParameter.getSqlType());
 assertFalse(taskParameter.getSqlsToBeExecuted().isEmpty());
 assertEquals(1, taskParameter.getSqlsToBeExecuted().size());
 assertEquals("ALTER TABLE test ADD COLUMN col INT", taskParameter.getSqlsToBeExecuted().get(0));
 }

 @Test(expected = UnexpectedException.class)
 public void testGenerateSubTaskParametersWithException() {
 parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
 parameters.setSqlContent("ALTER TABLE test ADD COLUMN col INT");

 try {
 parameters.generateSubTaskParameters(null, "testdb");
 } catch (UnexpectedException e) {
 // expected
 assertEquals("Failed to generate subtasks with parameter: null", e.getMessage());
 throw e;
 }
 }

 @Test
 public void testIsContinueOnError() {
 assertFalse(parameters.isContinueOnError());
 parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
 assertTrue(parameters.isContinueOnError());
 }

 @Test
 public void testReplaceTableName() {
 String newSql = DdlUtils.replaceTableName("ALTER TABLE test RENAME TO new_test", "new_test", "MySQL", OnlineSchemaChangeSqlType.ALTER);
 assertEquals("ALTER TABLE new_test RENAME TO new_test", newSql);
 }
}