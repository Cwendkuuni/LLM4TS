package com.oceanbase.odc.service.onlineschemachange.model;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.oceanbase.odc.core.flow.model.TaskParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.core.shared.model.TableIdentity;
import com.oceanbase.odc.service.common.util.SqlUtils;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;

public class OnlineSchemaChangeParametersTest {

    @Test
    public void testInstance() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        assertNotNull(parameters);
    }

    @Test
    public void testGettersAndSetters() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters.setSqlContent("ALTER TABLE test_table ADD COLUMN new_column INT;");
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        parameters.setLockTableTimeOutSeconds(30);
        parameters.setSwapTableType(SwapTableType.REPLACE);
        parameters.setSwapTableNameRetryTimes(2);
        parameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.DELETE);
        parameters.setDelimiter(";");
        parameters.setLockUsers(new ArrayList<>());

        assertEquals(OnlineSchemaChangeSqlType.ALTER, parameters.getSqlType());
        assertEquals("ALTER TABLE test_table ADD COLUMN new_column INT;", parameters.getSqlContent());
        assertEquals(TaskErrorStrategy.CONTINUE, parameters.getErrorStrategy());
        assertEquals(30, parameters.getLockTableTimeOutSeconds());
        assertEquals(SwapTableType.REPLACE, parameters.getSwapTableType());
        assertEquals(2, parameters.getSwapTableNameRetryTimes());
        assertEquals(OriginTableCleanStrategy.DELETE, parameters.getOriginTableCleanStrategy());
        assertEquals(";", parameters.getDelimiter());
        assertNull(parameters.getLockUsers());

        parameters.setLockUsers(new ArrayList<>());
        parameters.getLockUsers().add("user1");
        assertEquals(1, parameters.getLockUsers().size());
        assertEquals("user1", parameters.getLockUsers().get(0));
    }

    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(parameters.isContinueOnError());

        parameters.setErrorStrategy(TaskErrorStrategy.HALT);
        assertFalse(parameters.isContinueOnError());

        parameters.setErrorStrategy(null);
        assertFalse(parameters.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() throws Exception {
        String schema = "test_schema";
        String databaseName = "test_database";
        String originTableName = "test_table";
        String newTableName = "new_test_table";
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("mysql");

        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters.setSqlContent("ALTER TABLE " + originTableName + " ADD COLUMN new_column INT;");
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        parameters.setLockTableTimeOutSeconds(30);
        parameters.setSwapTableType(SwapTableType.REPLACE);
        parameters.setSwapTableNameRetryTimes(2);
        parameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.DELETE);
        parameters.setDelimiter(";");

        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameters.generateSubTaskParameters(connectionConfig, schema);

        assertEquals(1, subTaskParameters.size());
        OnlineSchemaChangeScheduleTaskParameters taskParameters = subTaskParameters.get(0);
        TableIdentity key = new TableIdentity(databaseName, originTableName);
        assertEquals(databaseName, taskParameters.getDatabaseName());
        assertEquals(originTableName, taskParameters.getOriginTableName());
        assertEquals(newTableName, taskParameters.getNewTableName());
        assertNotNull(taskParameters.getSqlsToBeExecuted());
        assertEquals(1, taskParameters.getSqlsToBeExecuted().size());
        assertEquals("ALTER TABLE " + originTableName + " ADD COLUMN new_column INT;", taskParameters.getSqlsToBeExecuted().get(0));

        assertEquals(1, parameters.getLockUsers().size());
        assertEquals("user1", parameters.getLockUsers().get(0));

        List<String> sqls = SqlUtils.split(connectionConfig.getDialectType(), parameters.getSqlContent(),
                parameters.getDelimiter());
        assertEquals(1, sqls.size());
        assertEquals("ALTER TABLE " + originTableName + " ADD COLUMN new_column INT;", sqls.get(0));

        assertThrows(UnexpectedException.class, () -> {
            parameters.generateSubTaskParameters(null, schema);
        });
    }
}