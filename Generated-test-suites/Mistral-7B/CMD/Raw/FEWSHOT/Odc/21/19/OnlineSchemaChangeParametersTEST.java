package com.oceanbase.odc.service.onlineschemachange.model;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.oceanbase.odc.core.connection.model.ConnectionConfig;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.model.TableIdentity;
import com.oceanbase.odc.service.common.util.SqlUtils;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

/**
 * Test class for OnlineSchemaChangeParameters
 *
 * @author yaobin
 * @since 4.2.0
 */
public class OnlineSchemaChangeParametersTest {

    private SubTaskParameterFactory subTaskParameterFactory;
    private ConnectionConfig connectionConfig;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        connectionConfig = mock(ConnectionConfig.class);
        subTaskParameterFactory = new SubTaskParameterFactory(connectionConfig, "test_schema");
    }

    @Test
    public void testNewInstance() {
        OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
        assertNull(onlineSchemaChangeParameters.getSqlType());
        assertNull(onlineSchemaChangeParameters.getSqlContent());
        assertNull(onlineSchemaChangeParameters.getErrorStrategy());
        assertNull(onlineSchemaChangeParameters.getLockTableTimeOutSeconds());
        assertNull(onlineSchemaChangeParameters.getSwapTableNameRetryTimes());
        assertNull(onlineSchemaChangeParameters.getOriginTableCleanStrategy());
        assertNull(onlineSchemaChangeParameters.getDelimiter());
        assertNull(onlineSchemaChangeParameters.getLockUsers());
        assertNull(onlineSchemaChangeParameters.getSwapTableType());
    }

    @Test
    public void testSettersAndGetters() {
        OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
        OnlineSchemaChangeSqlType sqlType = OnlineSchemaChangeSqlType.ALTER;
        String sqlContent = "ALTER TABLE test_table ADD column test_column INT";
        TaskErrorStrategy errorStrategy = TaskErrorStrategy.CONTINUE;
        OnlineSchemaChangeScheduleTaskParameters taskParameters = mock(OnlineSchemaChangeScheduleTaskParameters.class);
        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = new ArrayList<>();
        subTaskParameters.add(taskParameters);
        onlineSchemaChangeParameters.setSqlType(sqlType);
        onlineSchemaChangeParameters.setSqlContent(sqlContent);
        onlineSchemaChangeParameters.setErrorStrategy(errorStrategy);
        onlineSchemaChangeParameters.setLockTableTimeOutSeconds(10);
        onlineSchemaChangeParameters.setSwapTableNameRetryTimes(3);
        onlineSchemaChangeParameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.DROP_AND_RECREATE);
        onlineSchemaChangeParameters.setDelimiter(";");
        onlineSchemaChangeParameters.setLockUsers(new ArrayList<>());
        onlineSchemaChangeParameters.setSwapTableType(SwapTableType.EXTERNAL);

        assertEquals(sqlType, onlineSchemaChangeParameters.getSqlType());
        assertEquals(sqlContent, onlineSchemaChangeParameters.getSqlContent());
        assertEquals(errorStrategy, onlineSchemaChangeParameters.getErrorStrategy());
        assertEquals(10, onlineSchemaChangeParameters.getLockTableTimeOutSeconds().intValue());
        assertEquals(3, onlineSchemaChangeParameters.getSwapTableNameRetryTimes().intValue());
        assertEquals(OriginTableCleanStrategy.DROP_AND_RECREATE, onlineSchemaChangeParameters.getOriginTableCleanStrategy());
        assertEquals(";", onlineSchemaChangeParameters.getDelimiter());
        assertTrue(onlineSchemaChangeParameters.getLockUsers().isEmpty());
        assertEquals(SwapTableType.EXTERNAL, onlineSchemaChangeParameters.getSwapTableType());

        OnlineSchemaChangeParameters clone = new OnlineSchemaChangeParameters(onlineSchemaChangeParameters);
        assertEquals(onlineSchemaChangeParameters, clone);
    }

    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
        OnlineSchemaChangeParameters onlineSchemaChangeParametersWithErrorStrategy = new OnlineSchemaChangeParameters();
        onlineSchemaChangeParametersWithErrorStrategy.setErrorStrategy(TaskErrorStrategy.CONTINUE);

        assertFalse(onlineSchemaChangeParameters.isContinueOnError());
        assertTrue(onlineSchemaChangeParametersWithErrorStrategy.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() {
        String sqlContent = "ALTER TABLE test_table ADD column test_column INT";
        OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
        onlineSchemaChangeParameters.setSqlContent(sqlContent);
        onlineSchemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);

        List<String> sqls = SqlUtils.split(connectionConfig.getDialectType(), sqlContent, onlineSchemaChangeParameters.getDelimiter());
        assertEquals(1, sqls.size());

        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, "test_schema");
        assertEquals(1, subTaskParameters.size());

        OnlineSchemaChangeScheduleTaskParameters taskParameters = subTaskParameters.get(0);
        assertEquals("test_schema", taskParameters.getDatabaseName());
        assertEquals("test_table", taskParameters.getOriginTableName());
        assertEquals(OnlineSchemaChangeSqlType.ALTER, taskParameters.getSqlType());
        assertTrue(taskParameters.getSqlsToBeExecuted().isEmpty());

        OnlineSchemaChangeSqlType newSqlType = OnlineSchemaChangeSqlType.RENAME;
        String newAlterStmt = DdlUtils.replaceTableName(sqlContent, "new_test_table", connectionConfig.getDialectType(), OnlineSchemaChangeSqlType.ALTER);
        onlineSchemaChangeParameters.setSqlType(newSqlType);

        subTaskParameters = onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, "test_schema");
        assertEquals(1, subTaskParameters.size());

        taskParameters = subTaskParameters.get(0);
        assertEquals("test_schema", taskParameters.getDatabaseName());
        assertEquals("test_table", taskParameters.getOriginTableName());
        assertEquals(newSqlType, taskParameters.getSqlType());
        assertTrue(taskParameters.getSqlsToBeExecuted().size() > 1);
        assertEquals(newAlterStmt, taskParameters.getSqlsToBeExecuted().get(1));
    }
}