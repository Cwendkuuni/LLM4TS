import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.core.connection.model.ConnectionConfig;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.model.TableIdentity;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;

public class OnlineSchemaChangeParametersTest {

 private ConnectionConfig connectionConfig;
 private OnlineSchemaChangeParameters onlineSchemaChangeParameters;

 @Before
 public void setup() {
 connectionConfig = Mockito.mock(ConnectionConfig.class);
 onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
 }

 @Test
 public void testIsContinueOnError() {
 OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
 parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
 assertTrue(parameters.isContinueOnError());

 parameters.setErrorStrategy(TaskErrorStrategy.RETRY);
 assertFalse(parameters.isContinueOnError());
 }

 @Test
 public void testGenerateSubTaskParameters() throws Exception {
 List<String> sqls = Arrays.asList("CREATE TABLE IF NOT EXISTS test_table(id INT PRIMARY KEY)",
 "ALTER TABLE test_table ADD COLUMN column1 INT");

 when(connectionConfig.getDialectType()).thenReturn("MySQL");
 doReturn(new ArrayList<>()).when(onlineSchemaChangeParameters).new ArrayList<>();

 onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
 onlineSchemaChangeParameters.setSwapTableType(SwapTableType.SWAP_IN_PLACE);
 onlineSchemaChangeParameters.setLockUsers(new ArrayList<>());
 onlineSchemaChangeParameters.setSqlContent(String.join(onlineSchemaChangeParameters.getDelimiter(), sqls));
 onlineSchemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.CREATE_AND_ALTER);

 List<OnlineSchemaChangeScheduleTaskParameters> result = onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, "test_schema");

 assertEquals(2, result.size());

 for (OnlineSchemaChangeScheduleTaskParameters taskParameters : result) {
 assertEquals("test_schema", taskParameters.getDatabaseName());
 assertEquals("test_table", taskParameters.getOriginTableName());
 }
 }

 @Test(expected = UnexpectedException.class)
 public void testGenerateSubTaskParametersException() throws Exception {
 when(connectionConfig.getDialectType()).thenThrow(new Exception("Mock Exception"));

 onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
 onlineSchemaChangeParameters.setSwapTableType(SwapTableType.SWAP_IN_PLACE);
 onlineSchemaChangeParameters.setLockUsers(new ArrayList<>());
 onlineSchemaChangeParameters.setSqlContent("CREATE TABLE IF NOT EXISTS test_table(id INT PRIMARY KEY)");
 onlineSchemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.CREATE);

 onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, "test_schema");
 fail("Exception should be thrown");
 }

 @Test
 public void testSplitSqls() {
 String sqlContent = "CREATE TABLE IF NOT EXISTS test_table1(id INT PRIMARY KEY); CREATE TABLE IF NOT EXISTS test_table2(id INT PRIMARY KEY);";

 List<String> sqls = OnlineSchemaChangeParameters.splitSqls(connectionConfig.getDialectType(), sqlContent,
 OnlineSchemaChangeParameters.DELIMITER);

 assertEquals(2, sqls.size());
 assertEquals("CREATE TABLE IF NOT EXISTS test_table1(id INT PRIMARY KEY)", sqls.get(0));
 assertEquals("CREATE TABLE IF NOT EXISTS test_table2(id INT PRIMARY KEY)", sqls.get(1));
 }

 @Test
 public void testReplaceTableName() {
 String sql = "ALTER TABLE test_table ADD COLUMN column1 INT";
 String databaseName = "test_database";
 String originTableName = "test_table";
 String newTableName = "new_test_table";
 String dialectType = "MySQL";

 String expected = "ALTER TABLE new_test_table ADD COLUMN column1 INT";

 assertEquals(expected, OnlineSchemaChangeParameters.replaceTableName(sql, newTableName, dialectType, OnlineSchemaChangeSqlType.ALTER));
 }
}