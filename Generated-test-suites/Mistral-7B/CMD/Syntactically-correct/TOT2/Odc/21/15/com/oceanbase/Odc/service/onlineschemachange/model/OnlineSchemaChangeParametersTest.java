package com.oceanbase.odc.service.onlineschemachange.model.test;

import com.oceanbase.odc.core.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class OnlineSchemaChangeParametersTest {

    @Test
    public void testConstructor() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        assertNotNull(parameters);
    }

    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(parameters.isContinueOnError());
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorNullSqlContent() {
        new OnlineSchemaChangeParameters(null);
    }

    @Test
    public void testConstructorEmptySqlContent() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlContent("");
        assertEquals(0, parameters.getSqlsToBeExecuted().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorInvalidSqlStatement() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlContent("INVALID_SQL_STATEMENT");
    }

    @Test(expected = NullPointerException.class)
    public void testGenerateSubTaskParametersNullConnectionConfig() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        assertThrows(NullPointerException.class, () -> parameters.generateSubTaskParameters(null, "test_schema"));
    }

    @Test(expected = NullPointerException.class)
    public void testGenerateSubTaskParametersNullSchema() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        assertThrows(NullPointerException.class, () -> parameters.generateSubTaskParameters(new ConnectionConfig(), null));
    }

    @Test
    public void testGenerateSubTaskParametersEmptySqlContent() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        ConnectionConfig connectionConfig = new ConnectionConfig();
        List<OnlineSchemaChangeScheduleTaskParameters> taskParameters = parameters.generateSubTaskParameters(connectionConfig, "test_schema");
        assertEquals(0, taskParameters.size());
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParametersWithError() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        ConnectionConfig connectionConfig = new ConnectionConfig();
        parameters.setSqlContent("INVALID_SQL_STATEMENT;");
        assertThrows(UnexpectedException.class, () -> parameters.generateSubTaskParameters(connectionConfig, "test_schema"));
    }
}