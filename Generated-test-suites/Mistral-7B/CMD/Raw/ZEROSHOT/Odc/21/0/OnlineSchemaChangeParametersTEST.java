import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.oceanbase.odc.core.model.ConnectionConfig;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.service.common.util.SqlUtils;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;

public class OnlineSchemaChangeParametersTest {

    @Test
    public void testInstantiateOnlineSchemaChangeParameters() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        assertNull(parameters.getSqlType());
        assertNull(parameters.getSqlContent());
        assertNull(parameters.getErrorStrategy());
        assertNull(parameters.getLockTableTimeOutSeconds());
        assertNull(parameters.getSwapTableNameRetryTimes());
        assertNull(parameters.getOriginTableCleanStrategy());
        assertNull(parameters.getDelimiter());
        assertNull(parameters.getLockUsers());
        assertNull(parameters.getSwapTableType());
    }

    @Test
    public void testSetGetSqlType() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        OnlineSchemaChangeSqlType sqlType = OnlineSchemaChangeSqlType.ALTER;
        parameters.setSqlType(sqlType);
        assertEquals(sqlType, parameters.getSqlType());
    }

    @Test
    public void testSetGetSqlContent() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        String sqlContent = "CREATE TABLE test_table (id INT PRIMARY KEY, name VARCHAR)";
        parameters.setSqlContent(sqlContent);
        assertEquals(sqlContent, parameters.getSqlContent());
    }

    @Test
    public void testSetGetErrorStrategy() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(parameters.isContinueOnError());
        parameters.setErrorStrategy(TaskErrorStrategy.IGNORE);
        assertFalse(parameters.isContinueOnError());
    }

    @Test
    public void testSetGetLockTableTimeOutSeconds() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setLockTableTimeOutSeconds(10);
        assertEquals(10, parameters.getLockTableTimeOutSeconds().intValue());
    }

    @Test
    public void testSetGetSwapTableNameRetryTimes() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSwapTableNameRetryTimes(3);
        assertEquals(3, parameters.getSwapTableNameRetryTimes().intValue());
    }

    @Test
    public void testSetGetOriginTableCleanStrategy() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.DROP_AND_CREATE);
        assertEquals(OriginTableCleanStrategy.DROP_AND_CREATE, parameters.getOriginTableCleanStrategy());
    }

    @Test
    public void testSetGetDelimiter() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setDelimiter(";");
        assertEquals(";", parameters.getDelimiter());
    }

    @Test
    public void testSetGetLockUsers() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        List<String> lockUsers = Arrays.asList("user1", "user2");
        parameters.setLockUsers(lockUsers);
        assertEquals(lockUsers, parameters.getLockUsers());
    }

    @Test
    public void testSetGetSwapTableType() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSwapTableType(SwapTableType.ALL_IN_ONE);
        assertEquals(SwapTableType.ALL_IN_ONE, parameters.getSwapTableType());
    }

    @Test
    public void testGenerateSubTaskParametersSuccess() throws Exception {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        String schema = "test_schema";
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters.setSqlContent("ALTER TABLE test_table ADD COLUMN new_column INT");
        parameters.setErrorStrategy(TaskErrorStrategy.IGNORE);
        parameters.setLockTableTimeOutSeconds(10);
        parameters.setSwapTableNameRetryTimes(3);
        parameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.TRUNCATE);
        parameters.setDelimiter(";");
        parameters.setLockUsers(Arrays.asList("user1", "user2"));
        parameters.setSwapTableType(SwapTableType.SWAP);

        List<String> sqls = SqlUtils.split(connectionConfig.getDialectType(), parameters.getSqlContent(),
                parameters.getDelimiter());

        Map<TableIdentity, OnlineSchemaChangeScheduleTaskParameters> taskParameters = new LinkedHashMap<>();
        for (String sql : sqls) {
            OnlineSchemaChangeScheduleTaskParameters parameter = new SubTaskParameterFactory(connectionConfig, schema)
                    .generate(sql, parameters.getSqlType());
            TableIdentity key = new TableIdentity(parameter.getDatabaseName(), parameter.getOriginTableName());
            taskParameters.putIfAbsent(key, parameter);
        }

        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameters.generateSubTaskParameters(connectionConfig, schema);
        assertEquals(1, subTaskParameters.size());
        OnlineSchemaChangeScheduleTaskParameters subTaskParameter = subTaskParameters.get(0);
        TableIdentity tableIdentity = subTaskParameter.getTableIdentity();
        assertEquals("test_schema", tableIdentity.getSchema());
        assertEquals("test_table", tableIdentity.getTableName());
        assertEquals("ALTER TABLE test_table ADD COLUMN new_column INT", subTaskParameter.getSqlContent());
        assertEquals(OnlineSchemaChangeSqlType.ALTER, subTaskParameter.getSqlType());
        assertEquals(TaskErrorStrategy.IGNORE, subTaskParameter.getErrorStrategy());
        assertEquals(10, subTaskParameter.getLockTableTimeOutSeconds().intValue());
        assertEquals(3, subTaskParameter.getSwapTableNameRetryTimes().intValue());
        assertEquals(OriginTableCleanStrategy.TRUNCATE, subTaskParameter.getOriginTableCleanStrategy());
        assertEquals(";", subTaskParameter.getDelimiter());
        assertEquals(Arrays.asList("user1", "user2"), subTaskParameter.getLockUsers());
        assertEquals(SwapTableType.SWAP, subTaskParameter.getSwapTableType());
        List<String> sqlsToBeExecuted = subTaskParameter.getSqlsToBeExecuted();
        String alterStmt = DdlUtils.replaceTableName("ALTER TABLE test_table ADD COLUMN new_column INT", "test_new_table",
                connectionConfig.getDialectType(), OnlineSchemaChangeSqlType.ALTER);
        assertEquals(1, sqlsToBeExecuted.size());
        assertEquals(alterStmt, sqlsToBeExecuted.get(0));
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParametersFailure() throws Exception {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        String schema = "test_schema";
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters.setSqlContent("ALTER TABLE test_table ADD COLUMN new_column INT");
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        parameters.setLockTableTimeOutSeconds(10);
        parameters.setSwapTableNameRetryTimes(3);
        parameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.DROP_AND_CREATE);
        parameters.setDelimiter(";");
        parameters.setLockUsers(Arrays.asList("user1", "user2"));
        parameters.setSwapTableType(SwapTableType.SWAP);

        List<String> sqls = SqlUtils.split(connectionConfig.getDialectType(), parameters.getSqlContent(),
                parameters.getDelimiter());

        Map<TableIdentity, OnlineSchemaChangeScheduleTaskParameters> taskParameters = new LinkedHashMap<>();
        for (String sql : sqls) {
            OnlineSchemaChangeScheduleTaskParameters parameter = new SubTaskParameterFactory(connectionConfig, schema)
                    .generate(sql, parameters.getSqlType());
            TableIdentity key = new TableIdentity(parameter.getDatabaseName(), parameter.getOriginTableName());
            taskParameters.putIfAbsent(key, parameter);
        }

        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameters.generateSubTaskParameters(connectionConfig, schema);
        assertEquals(1, subTaskParameters.size());
        OnlineSchemaChangeScheduleTaskParameters subTaskParameter = subTaskParameters.get(0);
        // This line should throw an UnexpectedException
        subTaskParameter.getSqlsToBeExecuted().get(0);
    }
}