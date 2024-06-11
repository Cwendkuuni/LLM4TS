package com.oceanbase.odc.service.onlineschemachange.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.core.flow.model.ConnectionConfig;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.core.shared.model.TableIdentity;
import com.oceanbase.odc.service.common.util.SqlUtils;
import com.oceanbase.odc.service.connection.model.ConnectionConfigMock;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtilsMock;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactoryMock;

/**
 * Tests for OnlineSchemaChangeParameters class
 *
 * @author yaobin
 * @since 4.2.0
 */
public class OnlineSchemaChangeParametersTest {

    private OnlineSchemaChangeParameters parameters;
    private ConnectionConfig connectionConfig;
    private DdlUtils ddlUtils;
    private SubTaskParameterFactory subTaskParameterFactory;

    @Before
    public void setup() {
        connectionConfig = new ConnectionConfigMock();
        ddlUtils = new DdlUtilsMock();
        subTaskParameterFactory = new SubTaskParameterFactoryMock();
        parameters = new OnlineSchemaChangeParameters();
    }

    @Test
    public void testGetSqlType() {
        assertNull(parameters.getSqlType());

        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        assertEquals(OnlineSchemaChangeSqlType.ALTER, parameters.getSqlType());
    }

    @Test
    public void testGetSqlContent() {
        assertNull(parameters.getSqlContent());

        String sqlContent = "ALTER TABLE test_table ADD COLUMN new_column INT";
        parameters.setSqlContent(sqlContent);
        assertEquals(sqlContent, parameters.getSqlContent());
    }

    @Test
    public void testGetErrorStrategy() {
        assertNull(parameters.getErrorStrategy());

        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertEquals(TaskErrorStrategy.CONTINUE, parameters.getErrorStrategy());
    }

    @Test
    public void testGetLockTableTimeOutSeconds() {
        assertEquals(0, parameters.getLockTableTimeOutSeconds());

        parameters.setLockTableTimeOutSeconds(10);
        assertEquals(10, parameters.getLockTableTimeOutSeconds());
    }

    @Test
    public void testGetSwapTableNameRetryTimes() {
        assertEquals(0, parameters.getSwapTableNameRetryTimes());

        parameters.setSwapTableNameRetryTimes(5);
        assertEquals(5, parameters.getSwapTableNameRetryTimes());
    }

    @Test
    public void testGetOriginTableCleanStrategy() {
        assertNull(parameters.getOriginTableCleanStrategy());

        parameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.DELETE);
        assertEquals(OriginTableCleanStrategy.DELETE, parameters.getOriginTableCleanStrategy());
    }

    @Test
    public void testIsContinueOnError() {
        assertFalse(parameters.isContinueOnError());

        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(parameters.isContinueOnError());

        parameters.setErrorStrategy(TaskErrorStrategy.STOP);
        assertFalse(parameters.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() throws Exception {
        String schema = "test_schema";

        String sqlContent = "ALTER TABLE test_table ADD COLUMN new_column INT";
        parameters.setSqlContent(sqlContent);
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);

        when(ddlUtils.replaceTableName(sqlContent, "new_table_name", connectionConfig.getDialectType(), OnlineSchemaChangeSqlType.ALTER))
                .thenReturn("ALTER TABLE new_table_name ADD COLUMN new_column INT");

        when(subTaskParameterFactory.generate(sqlContent, OnlineSchemaChangeSqlType.ALTER))
                .thenReturn(new OnlineSchemaChangeScheduleTaskParametersMock());

        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameters.generateSubTaskParameters(connectionConfig, schema);
        assertEquals(1, subTaskParameters.size());

        OnlineSchemaChangeScheduleTaskParameters taskParameters = subTaskParameters.get(0);
        assertEquals(schema, taskParameters.getDatabaseName());
        assertEquals("test_table", taskParameters.getOriginTableName());
        assertEquals("new_table_name", taskParameters.getNewTableName());
        assertEquals(Collections.singletonList("ALTER TABLE new_table_name ADD COLUMN new_column INT"), taskParameters.getSqlsToBeExecuted());
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParameters_exception() throws Exception {
        String schema = "test_schema";

        String sqlContent = "ALTER TABLE test_table ADD COLUMN new_column INT";
        parameters.setSqlContent(sqlContent);
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);

        when(subTaskParameterFactory.generate(sqlContent, OnlineSchemaChangeSqlType.ALTER))
                .thenThrow(new Exception("Failed to generate subtask"));

        parameters.generateSubTaskParameters(connectionConfig, schema);
    }

}

class ConnectionConfigMock implements ConnectionConfig {
    @Override
    public String getDialectType() {
        return "mock_dialect_type";
    }
}

class DdlUtilsMock implements DdlUtils {
    @Override
    public String replaceTableName(String sql, String newTableName, String dialectType, OnlineSchemaChangeSqlType sqlType) {
        return "replaced_sql";
    }
}

class SubTaskParameterFactoryMock implements SubTaskParameterFactory {
    @Override
    public OnlineSchemaChangeScheduleTaskParameters generate(String sql, OnlineSchemaChangeSqlType sqlType) {
        return new OnlineSchemaChangeScheduleTaskParametersMock();
    }
}

class OnlineSchemaChangeScheduleTaskParametersMock implements OnlineSchemaChangeScheduleTaskParameters {
    @Override
    public String getDatabaseName() {
        return "test_database";
    }

    @Override
    public String getOriginTableName() {
        return "test_table";
    }

    @Override
    public String getNewTableName() {
        return "new_table_name";
    }

    @Override
    public List<String> getSqlsToBeExecuted() {
        return new ArrayList<>();
    }
}