package com.oceanbase.odc.service.onlineschemachange.model;

import org.junit.Test;
import static org.junit.Assert.*;

public class OnlineSchemaChangeParametersTest {
    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        params.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(params.isContinueOnError());

        params.setErrorStrategy(TaskErrorStrategy.STOP);
        assertFalse(params.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() {
        // Test method has dependencies and needs to be mocked
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParametersException() {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        params.generateSubTaskParameters(null, null);
    }

    @Test
    public void testGenerateSubTaskParametersEmptySqlContent() {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();

        params.setSqlContent("");
        params.setConnectionConfig(new ConnectionConfig());

        List<OnlineSchemaChangeScheduleTaskParameters> returnedParams = params.generateSubTaskParameters(params.getConnectionConfig(), "schema");
        assertTrue(returnedParams.isEmpty());
    }
}