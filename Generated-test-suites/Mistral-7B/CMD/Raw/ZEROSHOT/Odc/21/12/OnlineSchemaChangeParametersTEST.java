package com.oceanbase.odc.service.onlineschemachange.model.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.oceanbase.odc.core.connection.model.ConnectionConfig;
import com.oceanbase.odc.core.flow.model.TaskParameters;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;

public class OnlineSchemaChangeParametersTest {

    @Test
    public void testOnlineSchemaChangeParameters() {
        // Test with default values
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        assertEquals(OnlineSchemaChangeSqlType.CREATE, parameters.getSqlType());
        assertNull(parameters.getSqlContent());
        assertEquals(TaskErrorStrategy.FAIL, parameters.getErrorStrategy());
        assertEquals(300, parameters.getLockTableTimeOutSeconds());
        assertEquals(3, parameters.getSwapTableNameRetryTimes());
        assertEquals(OriginTableCleanStrategy.DELETE, parameters.getOriginTableCleanStrategy());
        assertEquals(";", parameters.getDelimiter());
        assertNull(parameters.getLockUsers());
        assertEquals(SwapTableType.SWAP, parameters.getSwapTableType());
        assertFalse(parameters.isContinueOnError());

        // Test with custom values
        OnlineSchemaChangeParameters customParameters = new OnlineSchemaChangeParameters();
        customParameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        customParameters.setSqlContent("ALTER TABLE test_table ADD COLUMN new_column INT;");
        customParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        customParameters.setLockTableTimeOutSeconds(600);
        customParameters.setSwapTableNameRetryTimes(5);
        customParameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.TRUNCATE);
        customParameters.setDelimiter(":");
        customParameters.setLockUsers(new ArrayList<>());
        customParameters.setSwapTableType(SwapTableType.CLONE);
        assertTrue(customParameters.isContinueOnError());

        // Test generateSubTaskParameters method
        ConnectionConfig connectionConfig = new ConnectionConfig();
        String schema = "test_schema";
        OnlineSchemaChangeParameters testParameters = new OnlineSchemaChangeParameters();
        testParameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        testParameters.setSqlContent("ALTER TABLE test_table ADD COLUMN new_column INT;");
        testParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        testParameters.setLockTableTimeOutSeconds(600);
        testParameters.setSwapTableNameRetryTimes(5);
        testParameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.TRUNCATE);
        testParameters.setDelimiter(":");
        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = testParameters.generateSubTaskParameters(connectionConfig, schema);
        assertEquals(1, subTaskParameters.size());
        OnlineSchemaChangeScheduleTaskParameters taskParameter = subTaskParameters.get(0);
        assertEquals("test_schema", taskParameter.getDatabaseName());
        assertEquals("test_table", taskParameter.getOriginTableName());
        assertEquals("ALTER TABLE test_table ADD COLUMN new_column INT;", taskParameter.getSqlContent());
        assertEquals(OnlineSchemaChangeSqlType.ALTER, taskParameter.getSqlType());
        assertNotNull(taskParameter.getNewTableName());
        List<String> sqlsToBeExecuted = taskParameter.getSqlsToBeExecuted();
        assertEquals(2, sqlsToBeExecuted.size());
        assertEquals("ALTER TABLE test_table ADD COLUMN new_column INT;", sqlsToBeExecuted.get(0));
        String newAlterStmt = "ALTER TABLE test_table RENAME TO test_table_new;";
        assertEquals(newAlterStmt, sqlsToBeExecuted.get(1));
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParametersWithException() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        ConnectionConfig connectionConfig = new ConnectionConfig();
        String schema = "test_schema";
        List<String> sqls = new ArrayList<>();
        sqls.add("ALTER TABLE test_table ADD COLUMN new_column INT;");
        try (SubTaskParameterFactory subTaskParameterFactory = new SubTaskParameterFactory(connectionConfig, schema)) {
            Map<TableIdentity, OnlineSchemaChangeScheduleTaskParameters> taskParameters = new LinkedHashMap<>();
            for (String sql : sqls) {
                OnlineSchemaChangeScheduleTaskParameters parameter = subTaskParameterFactory.generate(sql, OnlineSchemaChangeSqlType.ALTER);
                TableIdentity key = new TableIdentity(parameter.getDatabaseName(), parameter.getOriginTableName());
                taskParameters.putIfAbsent(key, parameter);
            }
            List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = new ArrayList<>(taskParameters.values());
            parameters.setSubTaskParameters(subTaskParameters);
        } catch (Exception e) {
            throw new UnexpectedException("Failed to generate subtasks with parameter: " + parameters, e);
        }

        // Test generateSubTaskParameters method
        List<OnlineSchemaChangeScheduleTaskParameters> generatedSubTaskParameters = parameters.generateSubTaskParameters(connectionConfig, schema);
        fail("Unexpected method execution");
    }
}