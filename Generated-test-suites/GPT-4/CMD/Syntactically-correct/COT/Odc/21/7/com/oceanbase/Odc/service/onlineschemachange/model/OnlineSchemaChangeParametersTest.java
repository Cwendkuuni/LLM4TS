package com.oceanbase.odc.service.onlineschemachange.model;

import com.oceanbase.odc.service.onlineschemachange.model.*;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.List;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class OnlineSchemaChangeParametersTest {

    private OnlineSchemaChangeParameters onlineSchemaChangeParameters;

    @Before
    public void setUp() throws Exception {
        onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
        onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
    }

    @Test
    public void testIsContinueOnError() {
        assertTrue(onlineSchemaChangeParameters.isContinueOnError());
        onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.STOP);
        assertFalse(onlineSchemaChangeParameters.isContinueOnError());
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParametersException() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("INVALID");
        onlineSchemaChangeParameters.setSqlContent("INVALID_SQL_CONTENT");
        onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, "schema");
    }

    @Test
    public void testGenerateSubTaskParameters() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("MYSQL");
        onlineSchemaChangeParameters.setSqlContent("CREATE TABLE Test (column1 INT, column2 VARCHAR(255));");
        onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, "schema");

        onlineSchemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        onlineSchemaChangeParameters.setSqlContent("ALTER TABLE Test ADD column3 DATE;");
        List<OnlineSchemaChangeScheduleTaskParameters> taskParameters = onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, "schema");
        assertEquals(1, taskParameters.size());

        OnlineSchemaChangeScheduleTaskParameters parameter = taskParameters.get(0);
        assertEquals("schema", parameter.getDatabaseName());
        assertEquals("Test", parameter.getOriginTableName());
        assertEquals(2, parameter.getSqlsToBeExecuted().size());
    }

    // The method "generateSubTaskParameters()" uses an exception handling scenario for a failed sub-task generation. 
    // A test case testing this scenario should be created but it may require substantial setup of a particular environment. 
    //
    // Each method call with a different SQL type is an edge case of the method "generateSubTaskParameters()". 
    // For example, different SQL types may produce different numbers of subtasks or different exceptions. 
    // The amount of setup required to create these tests may be significant and is beyond the scope of this example.
}
