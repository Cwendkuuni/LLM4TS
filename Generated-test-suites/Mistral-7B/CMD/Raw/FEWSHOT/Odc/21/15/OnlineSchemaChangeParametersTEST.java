package com.oceanbase.odc.service.onlineschemachange.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import com.oceanbase.odc.core.connection.model.ConnectionConfig;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.model.TableIdentity;
import com.oceanbase.odc.service.common.util.SqlUtils;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;

@RunWith(org.junit.runner.JUnit4.class)
public class OnlineSchemaChangeParametersTest {

    @Test
    public void testNewInstance() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        assertNull(parameters.getSqlType());
        assertNull(parameters.getSqlContent());
        assertSame(TaskErrorStrategy.IGNORE, parameters.getErrorStrategy());
        assertNull(parameters.getLockTableTimeOutSeconds());
        assertNull(parameters.getSwapTableNameRetryTimes());
        assertSame(OnlineSchemaChangeParameters.OriginTableCleanStrategy.DROP_AND_CREATE,
                parameters.getOriginTableCleanStrategy());
        assertNull(parameters.getDelimiter());
        assertNull(parameters.getLockUsers());
        assertSame(OnlineSchemaChangeParameters.SwapTableType.SWAP, parameters.getSwapTableType());
        assertFalse(parameters.isContinueOnError());
        assertNull(parameters.generateSubTaskParameters(null, null));
    }

    @Test
    public void testNewInstance_WithAllFields() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters.setSqlContent("alter table t1 add column c1 int;");
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        parameters.setLockTableTimeOutSeconds(123);
        parameters.setSwapTableNameRetryTimes(1234);
        parameters.setOriginTableCleanStrategy(OnlineSchemaChangeParameters.OriginTableCleanStrategy.BACKUP);
        parameters.setDelimiter(";");
        List<String> lockUsers = Arrays.asList("user1", "user2");
        parameters.setLockUsers(lockUsers);
        parameters.setSwapTableType(OnlineSchemaChangeParameters.SwapTableType.SWAP_AND_RENAME);
        assertTrue(parameters.isContinueOnError());

        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameters.generateSubTaskParameters(
                getConnectionConfig(), "test_schema");
        assertNotNull(subTaskParameters);
        assertEquals(1, subTaskParameters.size());
        OnlineSchemaChangeScheduleTaskParameters taskParameter = subTaskParameters.get(0);
        assertEquals(OnlineSchemaChangeSqlType.ALTER, taskParameter.getSqlType());
        assertSame("alter table t1 add column c1 int;", taskParameter.getSqlContent());
        assertEquals(TaskErrorStrategy.CONTINUE, taskParameter.getErrorStrategy());
        assertSame(123, taskParameter.getLockTableTimeOutSeconds());
        assertSame(1234, taskParameter.getSwapTableNameRetryTimes());
        assertEquals(OnlineSchemaChangeParameters.OriginTableCleanStrategy.BACKUP,
                taskParameter.getOriginTableCleanStrategy());
        assertEquals(";", taskParameter.getDelimiter());
        assertSame(lockUsers, taskParameter.getLockUsers());
        assertSame(OnlineSchemaChangeParameters.SwapTableType.SWAP_AND_RENAME, taskParameter.getSwapTableType());
    }

    @Test
    public void testSplitSqlsUsingSemicolonDelimiter() {
        ConnectionConfig connectionConfig = Mockito.mock(ConnectionConfig.class);
        String sqlContent = "create table t1 (id int primary key, name varchar(10));"
                + "create index idx1 on t1 (id);"
                + "alter table t1 add column c1 int;";
        List<String> expectedSqls = Arrays.asList("create table t1 (id int primary key, name varchar(10));",
                "create index idx1 on t1 (id);", "alter table t1 add column c1 int;");
        List<String> sqls = SqlUtils.split(connectionConfig.getDialectType(), sqlContent, ";");
        assertEquals(expectedSqls, sqls);
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParameters_FailedToGenerateSubTasks() {
        ConnectionConfig connectionConfig = Mockito.mock(ConnectionConfig.class);
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlContent("drop table t1;");
        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameters.generateSubTaskParameters(
                connectionConfig, "test_schema");
    }

    private ConnectionConfig getConnectionConfig() {
        ConnectionConfig connectionConfig = Mockito.mock(ConnectionConfig.class);
        when(connectionConfig.getDialectType()).thenReturn("MySQL");
        return connectionConfig;
    }
}