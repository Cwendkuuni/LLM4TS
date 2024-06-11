package com.oceanbase.odc.service.onlineschemachange.model;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.oceanbase.odc.core.flow.model.ConnectionConfig;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.model.TableIdentity;
import com.oceanbase.odc.service.common.util.SqlUtils;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;

/**
 * Test class for OnlineSchemaChangeParameters
 *
 * @author yaobin
 */
public class OnlineSchemaChangeParametersTest {

    @Test
    public void testOnlineSchemaChangeParametersInstance() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
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
    public void testSettersAndGetters() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();

        String sqlContent = "create table test_table(id int, name varchar(50))";
        parameters.setSqlContent(sqlContent);
        assertEquals(sqlContent, parameters.getSqlContent());

        TaskErrorStrategy errorStrategy = TaskErrorStrategy.CONTINUE;
        parameters.setErrorStrategy(errorStrategy);
        assertEquals(errorStrategy, parameters.getErrorStrategy());

        parameters.setLockTableTimeOutSeconds(10);
        assertEquals(Integer.valueOf(10), parameters.getLockTableTimeOutSeconds());

        parameters.setSwapTableNameRetryTimes(5);
        assertEquals(Integer.valueOf(5), parameters.getSwapTableNameRetryTimes());

        parameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.TRUNCATE);
        assertEquals(OriginTableCleanStrategy.TRUNCATE, parameters.getOriginTableCleanStrategy());

        parameters.setDelimiter("//");
        assertEquals("//", parameters.getDelimiter());

        List<String> lockUsers = Arrays.asList("user1", "user2", "user3");
        parameters.setLockUsers(lockUsers);
        assertEquals(lockUsers, parameters.getLockUsers());

        SwapTableType swapTableType = SwapTableType.BACKUP_TABLE;
        parameters.setSwapTableType(swapTableType);
        assertEquals(swapTableType, parameters.getSwapTableType());
    }

    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        assertFalse(parameters.isContinueOnError());

        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(parameters.isContinueOnError());

        parameters.setErrorStrategy(TaskErrorStrategy.FAIL);
        assertFalse(parameters.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() throws Exception {
        String schema = "test_schema";
        ConnectionConfig connectionConfig = new ConnectionConfig();

        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlContent("create table test_table1(id int, name varchar(50)); create table test_table2(id int, name varchar(50));");
        parameters.setSqlType(OnlineSchemaChangeSqlType.CREATE);
        parameters.setSchema(schema);
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        parameters.setLockTableTimeOutSeconds(10);
        parameters.setSwapTableNameRetryTimes(5);
        parameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.TRUNCATE);
        parameters.setDelimiter("//");
        parameters.setLockUsers(Arrays.asList("user1", "user2", "user3"));
        parameters.setSwapTableType(SwapTableType.BACKUP_TABLE);

        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameters.generateSubTaskParameters(connectionConfig, schema);
        assertEquals(2, subTaskParameters.size());

        OnlineSchemaChangeScheduleTaskParameters taskParameter1 = subTaskParameters.get(0);
        TableIdentity key1 = new TableIdentity("test_schema", "test_table1");
        OnlineSchemaChangeScheduleTaskParameters expectedTaskParameter1 = new OnlineSchemaChangeScheduleTaskParameters();
        expectedTaskParameter1.setDatabaseName("test_schema");
        expectedTaskParameter1.setOriginTableName("test_table1");
        expectedTaskParameter1.setNewTableName("test_table1_new");
        expectedTaskParameter1.setSqlsToBeExecuted(Arrays.asList(
                "create table test_table1(id int, name varchar(50));",
                "alter table test_table1 rename to test_table1_old;"
        ));
        assertEquals(expectedTaskParameter1, taskParameter1);

        OnlineSchemaChangeScheduleTaskParameters taskParameter2 = subTaskParameters.get(1);
        TableIdentity key2 = new TableIdentity("test_schema", "test_table2");
        OnlineSchemaChangeScheduleTaskParameters expectedTaskParameter2 = new OnlineSchemaChangeScheduleTaskParameters();
        expectedTaskParameter2.setDatabaseName("test_schema");
        expectedTaskParameter2.setOriginTableName("test_table2");
        expectedTaskParameter2.setNewTableName("test_table2_new");
        expectedTaskParameter2.setSqlsToBeExecuted(Arrays.asList(
                "create table test_table2(id int, name varchar(50));",
                "alter table test_table2 rename to test_table2_old;"
        ));
        assertEquals(expectedTaskParameter2, taskParameter2);
    }
}