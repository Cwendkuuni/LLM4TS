import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.oceanbase.odc.core.connection.model.ConnectionConfig;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.core.shared.model.TableIdentity;
import com.oceanbase.odc.service.common.util.SqlUtils;
import com.oceanbase.odc.service.connection.model.ConnectionConfigBuilder;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;

public class OnlineSchemaChangeParametersTest {

    @Test
    public void testValidInstance() {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        assertNull(params.getErrorStrategy());
        assertNull(params.getLockTableTimeOutSeconds());
        assertNull(params.getSwapTableType());
        assertNull(params.getOriginTableCleanStrategy());
        assertNull(params.getDelimiter());
        assertNull(params.getLockUsers());
        assertFalse(params.isContinueOnError());
        assertNull(params.getSqlContent());
        assertNull(params.getSqlType());
    }

    @Test
    public void testSettersAndGetters() {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        params.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        params.setLockTableTimeOutSeconds(10);
        params.setSwapTableType(SwapTableType.SWAP_TO_NEW);
        params.setOriginTableCleanStrategy(OriginTableCleanStrategy.DROP);
        params.setDelimiter("|");
        params.setLockUsers(Arrays.asList("user1", "user2"));
        params.setSqlContent("create table t1 (id int primary key);");
        params.setSqlType(OnlineSchemaChangeSqlType.CREATE);

        assertEquals(TaskErrorStrategy.CONTINUE, params.getErrorStrategy());
        assertEquals(10, params.getLockTableTimeOutSeconds());
        assertEquals(SwapTableType.SWAP_TO_NEW, params.getSwapTableType());
        assertEquals(OriginTableCleanStrategy.DROP, params.getOriginTableCleanStrategy());
        assertEquals("|", params.getDelimiter());
        assertEquals(Arrays.asList("user1", "user2"), params.getLockUsers());
        assertEquals("create table t1 (id int primary key);", params.getSqlContent());
        assertEquals(OnlineSchemaChangeSqlType.CREATE, params.getSqlType());
        assertTrue(params.isContinueOnError());
    }

    @Test
    public void testInvalidErrorStrategy() {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        assertThrows(IllegalArgumentException.class, () -> params.setErrorStrategy(null));
        assertThrows(IllegalArgumentException.class, () -> params.setErrorStrategy("INVALID"));
    }

    @Test
    public void testInvalidLockTableTimeOutSeconds() {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        assertThrows(IllegalArgumentException.class, () -> params.setLockTableTimeOutSeconds(-1));
    }

    @Test
    public void testInvalidSwapTableType() {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        assertThrows(IllegalArgumentException.class, () -> params.setSwapTableType(null));
        assertThrows(IllegalArgumentException.class, () -> params.setSwapTableType("INVALID"));
    }

    @Test
    public void testInvalidOriginTableCleanStrategy() {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        assertThrows(IllegalArgumentException.class, () -> params.setOriginTableCleanStrategy(null));
        assertThrows(IllegalArgumentException.class, () -> params.setOriginTableCleanStrategy("INVALID"));
    }

    @Test
    public void testInvalidDelimiter() {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        assertThrows(IllegalArgumentException.class, () -> params.setDelimiter(null));
        assertThrows(IllegalArgumentException.class, () -> params.setDelimiter(""));
        assertThrows(IllegalArgumentException.class, () -> params.setDelimiter(" "));
        assertThrows(IllegalArgumentException.class, () -> params.setDelimiter(";")); // should not pass because it is a valid delimiter
    }

    @Test
    public void testInvalidLockUsers() {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        assertThrows(IllegalArgumentException.class, () -> params.setLockUsers(null));
        assertThrows(IllegalArgumentException.class, () -> params.setLockUsers(new String[]{}));
        assertThrows(IllegalArgumentException.class, () -> params.setLockUsers(Arrays.asList("", "user2")));
    }

    @Test
    public void testGenerateSubTaskParameters_ValidScenario() {
        ConnectionConfig connectionConfig = new ConnectionConfigBuilder()
                .setHostname("localhost")
                .setPort(6101)
                .setUsername("user")
                .setPassword("password")
                .setDialectType("mysql")
                .setDatabaseName("testdb")
                .build();

        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        params.setSqlType(OnlineSchemaChangeSqlType.CREATE);
        params.setSqlContent("create table t1 (id int primary key);");
        params.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        params.setLockTableTimeOutSeconds(10);
        params.setSwapTableType(SwapTableType.SWAP_TO_NEW);
        params.setOriginTableCleanStrategy(OriginTableCleanStrategy.RENAME);
        params.setDelimiter("|");
        params.setLockUsers(Arrays.asList("user1", "user2"));

        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = params.generateSubTaskParameters(connectionConfig,
                "testdb");

        assertEquals(1, subTaskParameters.size());
        OnlineSchemaChangeScheduleTaskParameters taskParameter = subTaskParameters.get(0);
        assertEquals("testdb", taskParameter.getDatabaseName());
        assertEquals("t1", taskParameter.getOriginTableName());
        assertEquals(OnlineSchemaChangeSqlType.CREATE, taskParameter.getSqlType());
        assertEquals("create table t1 (id int primary key);", taskParameter.getSqlContent());
        assertEquals(Arrays.asList("create table t1 (id int primary key);"), taskParameter.getSqlsToBeExecuted());
        assertEquals("t1_renamed", taskParameter.getNewTableName());
        assertEquals(TaskErrorStrategy.CONTINUE, taskParameter.getErrorStrategy());
        assertEquals(10, taskParameter.getLockTableTimeOutSeconds());
        assertEquals(SwapTableType.SWAP_TO_NEW, taskParameter.getSwapTableType());
        assertEquals(OriginTableCleanStrategy.RENAME, taskParameter.getOriginTableCleanStrategy());
        assertEquals("|", taskParameter.getDelimiter());
        assertEquals(Arrays.asList("user1", "user2"), taskParameter.getLockUsers());
    }

    @Test
    public void testGenerateSubTaskParameters_WithAlterSql() {
        ConnectionConfig connectionConfig = new ConnectionConfigBuilder()
                .setHostname("localhost")
                .setPort(6101)
                .setUsername("user")
                .setPassword("password")
                .setDialectType("mysql")
                .setDatabaseName("testdb")
                .build();

        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        params.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        params.setSqlContent("alter table t1 add column c1 int;");
        params.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        params.setLockTableTimeOutSeconds(10);
        params.setSwapTableType(SwapTableType.SWAP_TO_NEW);
        params.setOriginTableCleanStrategy(OriginTableCleanStrategy.DROP);
        params.setDelimiter("|");
        params.setLockUsers(Arrays.asList("user1", "user2"));

        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = params.generateSubTaskParameters(connectionConfig,
                "testdb");

        assertEquals(1, subTaskParameters.size());
        OnlineSchemaChangeScheduleTaskParameters taskParameter = subTaskParameters.get(0);
        assertEquals("testdb", taskParameter.getDatabaseName());
        assertEquals("t1", taskParameter.getOriginTableName());
        assertEquals(OnlineSchemaChangeSqlType.ALTER, taskParameter.getSqlType());
        assertEquals("alter table t1 add column c1 int;", taskParameter.getSqlContent());
        assertEquals(Arrays.asList("alter table t1 add column c1 int;"), taskParameter.getSqlsToBeExecuted());
        assertNull(taskParameter.getNewTableName());
        assertEquals(TaskErrorStrategy.CONTINUE, taskParameter.getErrorStrategy());
        assertEquals(10, taskParameter.getLockTableTimeOutSeconds());
        assertEquals(SwapTableType.SWAP_TO_NEW, taskParameter.getSwapTableType());
        assertEquals(OriginTableCleanStrategy.DROP, taskParameter.getOriginTableCleanStrategy());
        assertEquals("|", taskParameter.getDelimiter());
        assertEquals(Arrays.asList("user1", "user2"), taskParameter.getLockUsers());

        String newAlterStmt = DdlUtils.replaceTableName(taskParameter.getSqlContent(), taskParameter.getNewTableName(),
                connectionConfig.getDialectType(), OnlineSchemaChangeSqlType.ALTER);
        assertTrue(subTaskParameters.get(0).getSqlsToBeExecuted().contains(newAlterStmt));
    }

    @Test
    public void testGenerateSubTaskParameters_WithEmptySqlContent() {
        ConnectionConfig connectionConfig = new ConnectionConfigBuilder()
                .setHostname("localhost")
                .setPort(6101)
                .setUsername("user")
                .setPassword("password")
                .setDialectType("mysql")
                .setDatabaseName("testdb")
                .build();

        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        params.setSqlType(OnlineSchemaChangeSqlType.CREATE);
        params.setSqlContent("");
        params.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        params.setLockTableTimeOutSeconds(10);
        params.setSwapTableType(SwapTableType.SWAP_TO_NEW);
        params.setOriginTableCleanStrategy(OriginTableCleanStrategy.DROP);
        params.setDelimiter("|");
        params.setLockUsers(Arrays.asList("user1", "user2"));

        assertThrows(UnexpectedException.class, () -> params.generateSubTaskParameters(connectionConfig, "testdb"));
    }

}