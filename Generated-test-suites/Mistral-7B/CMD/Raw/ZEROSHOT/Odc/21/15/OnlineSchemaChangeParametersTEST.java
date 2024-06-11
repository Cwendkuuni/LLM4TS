package com.oceanbase.odc.service.onlineschemachange.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.oceanbase.odc.core.model.ConnectionConfig;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.onlineschemachange.model.SubTaskParameterFactory;
import com.oceanbase.odc.service.onlineschemachange.model.TableIdentity;
import com.oceanbase.odc.service.onlineschemachange.subtask.OnlineSchemaChangeScheduleTaskParameters;

public class OnlineSchemaChangeParametersTest {

    @Test
    public void testInstance() {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        assertNull(params.getSqlType());
        assertNull(params.getSqlContent());
        assertNull(params.getErrorStrategy());
        assertNull(params.getLockTableTimeOutSeconds());
        assertNull(params.getSwapTableNameRetryTimes());
        assertNull(params.getOriginTableCleanStrategy());
        assertNull(params.getDelimiter());
        assertNull(params.getLockUsers());
        assertNull(params.getSwapTableType());
    }

    @Test
    public void testSettersAndGetters() {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();

        OnlineSchemaChangeSqlType sqlType = OnlineSchemaChangeSqlType.CREATE;
        params.setSqlType(sqlType);
        assertEquals(sqlType, params.getSqlType());

        String sqlContent = "CREATE TABLE test (id INT PRIMARY KEY)";
        params.setSqlContent(sqlContent);
        assertEquals(sqlContent, params.getSqlContent());

        params.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(params.isContinueOnError());

        params.setErrorStrategy(TaskErrorStrategy.ABORT);
        assertFalse(params.isContinueOnError());

        params.setLockTableTimeOutSeconds(30);
        assertEquals(30, params.getLockTableTimeOutSeconds().intValue());

        params.setSwapTableNameRetryTimes(5);
        assertEquals(5, params.getSwapTableNameRetryTimes().intValue());

        params.setOriginTableCleanStrategy(OriginTableCleanStrategy.DROP);
        assertEquals(OriginTableCleanStrategy.DROP, params.getOriginTableCleanStrategy());

        String delimiter = ";";
        params.setDelimiter(delimiter);
        assertEquals(delimiter, params.getDelimiter());

        List<String> lockUsers = Arrays.asList("user1", "user2");
        params.setLockUsers(lockUsers);
        assertSame(lockUsers, params.getLockUsers());

        SwapTableType swapTableType = SwapTableType.SWAP_TABLE;
        params.setSwapTableType(swapTableType);
        assertEquals(swapTableType, params.getSwapTableType());
    }

    @Test
    public void testGenerateSubTaskParameters() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        String schema = "test_schema";

        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        params.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        params.setSqlContent("ALTER TABLE test_table ADD COLUMN new_column INT;");
        params.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        params.setLockTableTimeOutSeconds(10);
        params.setSwapTableNameRetryTimes(3);
        params.setOriginTableCleanStrategy(OriginTableCleanStrategy.TRUNCATE);
        params.setDelimiter(";");
        params.setLockUsers(Arrays.asList("user1", "user2"));
        params.setSwapTableType(SwapTableType.SWAP_TABLE);

        List<OnlineSchemaChangeScheduleTaskParameters> taskParameters = params.generateSubTaskParameters(connectionConfig, schema);
        assertEquals(2, taskParameters.size());

        OnlineSchemaChangeScheduleTaskParameters taskParameter1 = taskParameters.get(0);
        TableIdentity tableIdentity1 = new TableIdentity("test_database", "test_table");
        assertEquals(tableIdentity1, taskParameter1.getTableIdentity());
        assertEquals(OnlineSchemaChangeSqlType.ALTER, taskParameter1.getSqlType());
        assertEquals("ALTER TABLE test_table ADD COLUMN new_column INT;", taskParameter1.getSqlContent());
        assertEquals(TaskErrorStrategy.CONTINUE, taskParameter1.getErrorStrategy());
        assertEquals(10, taskParameter1.getLockTableTimeOutSeconds().intValue());
        assertEquals(3, taskParameter1.getSwapTableNameRetryTimes().intValue());
        assertEquals(OriginTableCleanStrategy.TRUNCATE, taskParameter1.getOriginTableCleanStrategy());
        assertEquals(";", taskParameter1.getDelimiter());
        assertEquals(Arrays.asList("user1", "user2"), taskParameter1.getLockUsers());
        assertEquals(SwapTableType.SWAP_TABLE, taskParameter1.getSwapTableType());
        assertEquals(Arrays.asList("ALTER TABLE test_table DROP COLUMN new_column;"), taskParameter1.getSqlsToBeExecuted());

        OnlineSchemaChangeScheduleTaskParameters taskParameter2 = taskParameters.get(1);
        TableIdentity tableIdentity2 = new TableIdentity("test_database", "test_table");
        assertEquals(tableIdentity2, taskParameter2.getTableIdentity());
        assertEquals(OnlineSchemaChangeSqlType.ALTER, taskParameter2.getSqlType());
        assertEquals("ALTER TABLE test_table ADD COLUMN new_column INT;", taskParameter2.getSqlContent());
        assertEquals(TaskErrorStrategy.CONTINUE, taskParameter2.getErrorStrategy());
        assertEquals(10, taskParameter2.getLockTableTimeOutSeconds().intValue());
        assertEquals(3, taskParameter2.getSwapTableNameRetryTimes().intValue());
        assertEquals(OriginTableCleanStrategy.TRUNCATE, taskParameter2.getOriginTableCleanStrategy());
        assertEquals(";", taskParameter2.getDelimiter());
        assertEquals(Arrays.asList("user1", "user2"), taskParameter2.getLockUsers());
        assertEquals(SwapTableType.SWAP_Table, taskParameter2.getSwapTableType());
        assertEquals(Arrays.asList("ALTER TABLE test_table DROP COLUMN new_column;"), taskParameter2.getSqlsToBeExecuted());
    }
}