package com.oceanbase.odc.service.onlineschemachange.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class OnlineSchemaChangeParametersTest {

    private ConnectionConfig connectionConfig;
    private OnlineSchemaChangeParameters parameters;

    @Before
    public void setup() {
        connectionConfig = mock(ConnectionConfig.class);
        parameters = new OnlineSchemaChangeParameters();
    }

    @Test
    public void testGetSqlType() {
        assertEquals(OnlineSchemaChangeSqlType.CREATE, OnlineSchemaChangeParameters.OnlineSchemaChangeSqlType.CREATE);
        assertEquals(OnlineSchemaChangeSqlType.ALTER, OnlineSchemaChangeParameters.OnlineSchemaChangeSqlType.ALTER);
        assertEquals(OnlineSchemaChangeSqlType.DROP, OnlineSchemaChangeParameters.OnlineSchemaChangeSqlType.DROP);
    }

    @Test
    public void testIsContinueOnError() {
        assertFalse(parameters.isContinueOnError());
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(parameters.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() throws Exception {
        when(connectionConfig.getDialectType()).thenReturn(SqlUtils.DialectType.MYSQL);
        parameters.setSqlContent("CREATE TABLE t (id INT PRIMARY KEY) ENGINE=INNODB;");
        parameters.setSqlType(OnlineSchemaChangeSqlType.CREATE);

        List<OnlineSchemaChangeScheduleTaskParameters> taskParameters = parameters.generateSubTaskParameters(connectionConfig, "test_schema");
        assertEquals(1, taskParameters.size());
        OnlineSchemaChangeScheduleTaskParameters taskParameter = taskParameters.get(0);
        assertEquals("CREATE TABLE t (id INT PRIMARY KEY) ENGINE=INNODB", taskParameter.getSqlContent());
        assertEquals("test_schema.t", taskParameter.getTableIdentity().getTableName());
        assertEquals("test_schema", taskParameter.getTableIdentity().getDatabaseName());
        assertEquals(List.of("CREATE TABLE t (id INT PRIMARY KEY) ENGINE=INNODB"), taskParameter.getSqlsToBeExecuted());

        parameters.setSqlContent("ALTER TABLE t ADD COLUMN c INT;");
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        taskParameters = parameters.generateSubTaskParameters(connectionConfig, "test_schema");
        assertEquals(1, taskParameters.size());
        OnlineSchemaChangeScheduleTaskParameters alterTaskParameter = taskParameters.get(0);
        assertEquals("ALTER TABLE t ADD COLUMN c INT", alterTaskParameter.getSqlContent());
        assertEquals("test_schema.t", alterTaskParameter.getTableIdentity().getTableName());
        assertEquals("test_schema", alterTaskParameter.getTableIdentity().getDatabaseName());
        List<String> alterSqlsToBeExecuted = alterTaskParameter.getSqlsToBeExecuted();
        assertEquals(2, alterSqlsToBeExecuted.size());
        assertEquals("ALTER TABLE t ADD COLUMN c INT", alterSqlsToBeExecuted.get(0));
        String newAlterStmt = "ALTER TABLE test_schema.t CHANGE COLUMN c c INT;";
        assertEquals(newAlterStmt, alterSqlsToBeExecuted.get(1));
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParametersException() throws Exception {
        when(connectionConfig.getDialectType()).thenThrow(new RuntimeException());
        parameters.setSqlContent("CREATE TABLE t (id INT PRIMARY KEY) ENGINE=INNODB;");
        parameters.setSqlType(OnlineSchemaChangeSqlType.CREATE);
        parameters.generateSubTaskParameters(connectionConfig, "test_schema");
    }
}