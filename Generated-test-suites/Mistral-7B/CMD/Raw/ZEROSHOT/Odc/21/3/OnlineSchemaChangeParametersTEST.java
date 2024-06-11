import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.oceanbase.odc.core.connection.model.ConnectionConfig;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.model.TableIdentity;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OnlineSchemaChangeParametersTest {

 private ConnectionConfig connectionConfig = new ConnectionConfig();

 @Test
 public void testOnlineSchemaChangeParameters() {
 OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
 // test default values
 assertEquals(OnlineSchemaChangeSqlType.DROP, parameters.getSqlType());
 assertNull(parameters.getSqlContent());
 assertEquals(TaskErrorStrategy.ABORT, parameters.getErrorStrategy());
 assertEquals(10, parameters.getLockTableTimeOutSeconds());
 assertEquals(3, parameters.getSwapTableNameRetryTimes());
 assertEquals(OnlineSchemaChangeParameters.OriginTableCleanStrategy.DROP_AND_CREATE,
 parameters.getOriginTableCleanStrategy());
 assertNull(parameters.getDelimiter());
 assertNull(parameters.getLockUsers());
 assertEquals(SwapTableType.SWAP_IN_PLACE, parameters.getSwapTableType());
 assertFalse(parameters.isContinueOnError());

 // test setter methods
 parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
 assertEquals(OnlineSchemaChangeSqlType.ALTER, parameters.getSqlType());

 parameters.setSqlContent("ALTER TABLE test ALTER COLUMN col1 INT;");
 assertEquals("ALTER TABLE test ALTER COLUMN col1 INT;", parameters.getSqlContent());

 parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
 assertTrue(parameters.isContinueOnError());

 parameters.setLockTableTimeOutSeconds(15);
 assertEquals(15, parameters.getLockTableTimeOutSeconds());

 parameters.setSwapTableNameRetryTimes(5);
 assertEquals(5, parameters.getSwapTableNameRetryTimes());

 parameters.setOriginTableCleanStrategy(OnlineSchemaChangeParameters.OriginTableCleanStrategy.RENAME);
 assertEquals(OnlineSchemaChangeParameters.OriginTableCleanStrategy.RENAME, parameters.getOriginTableCleanStrategy());

 parameters.setDelimiter("--");
 assertEquals("--", parameters.getDelimiter());

 List<String> lockUsers = Arrays.asList("user1", "user2");
 parameters.setLockUsers(lockUsers);
 assertEquals(lockUsers, parameters.getLockUsers());

 parameters.setSwapTableType(SwapTableType.CREATE_A_NEW_TABLE);
 assertEquals(SwapTableType.CREATE_A_NEW_TABLE, parameters.getSwapTableType());
 }

 @Test
 public void testGenerateSubTaskParameters() throws Exception {
 // given
 String schema = "test_schema";
 String sqlContent = "CREATE TABLE test1 (col1 INT); CREATE TABLE test2 (col2 INT); ALTER TABLE test1 ADD COLUMN col3 INT;";
 OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
 parameters.setSqlType(OnlineSchemaChangeSqlType.CREATE_AND_ALTER);
 parameters.setSqlContent(sqlContent);
 parameters.setLockTableTimeOutSeconds(15);
 parameters.setSwapTableNameRetryTimes(5);
 parameters.setOriginTableCleanStrategy(OnlineSchemaChangeParameters.OriginTableCleanStrategy.RENAME);
 parameters.setDelimiter("--");
 parameters.setLockUsers(Arrays.asList("user1", "user2"));
 parameters.setSwapTableType(SwapTableType.CREATE_A_NEW_TABLE);

 // when
 List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameters.generateSubTaskParameters(connectionConfig, schema);

 // then
 assertEquals(3, subTaskParameters.size());
 List<OnlineSchemaChangeScheduleTaskParameters> expectedParameters = Arrays.asList(
 new OnlineSchemaChangeScheduleTaskParameters("test_schema", "test1", "test_schema", "test1", Arrays.asList(
 "CREATE TABLE test1 (col1 INT);"), OnlineSchemaChangeSqlType.CREATE),
 new OnlineSchemaChangeScheduleTaskParameters("test_schema", "test1", "test_schema", "test1", Arrays.asList(
 "ALTER TABLE test1 ADD COLUMN col3 INT;"), OnlineSchemaChangeSqlType.ALTER),
 new OnlineSchemaChangeScheduleTaskParameters("test_schema", "test2", "test_schema", "test2", Collections.emptyList(),
 OnlineSchemaChangeSqlType.CREATE)
 );
 for (int i = 0; i < subTaskParameters.size(); i++) {
 OnlineSchemaChangeScheduleTaskParameters actual = subTaskParameters.get(i);
 OnlineSchemaChangeScheduleTaskParameters expected = expectedParameters.get(i);
 assertSame(expected, actual);
 }
 }

 @Test(expected = UnexpectedException.class)
 public void testGenerateSubTaskParametersFailure() throws Exception {
 // given
 String schema = "test_schema";
 String sqlContent = "CREATE TABLE test1 (col1 INT); ALTER TABLE test1 ADD COLUMN col3 INT;";
 OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
 parameters.setSqlType(OnlineSchemaChangeSqlType.CREATE_AND_ALTER);
 parameters.setSqlContent(sqlContent);
 parameters.setLockTableTimeOutSeconds(15);
 parameters.setSwapTableNameRetryTimes(5);
 parameters.setOriginTableCleanStrategy(OnlineSchemaChangeParameters.OriginTableCleanStrategy.RENAME);
 parameters.setDelimiter("--");
 parameters.setLockUsers(Arrays.asList("user1", "user2"));
 parameters.setSwapTableType(SwapTableType.CREATE_A_NEW_TABLE);

 // when, then
 try {
 parameters.generateSubTaskParameters(connectionConfig, schema);
 } catch (Exception e) {
 log.error("Expected exception: {}", e.getMessage());
 assertTrue(true);
 }
 }
}