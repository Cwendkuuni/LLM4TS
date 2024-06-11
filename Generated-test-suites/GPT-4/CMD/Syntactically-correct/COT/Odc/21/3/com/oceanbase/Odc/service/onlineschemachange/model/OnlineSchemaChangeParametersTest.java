package com.oceanbase.odc.service.onlineschemachange.model;
import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mockito;
import com.oceanbase.odc.service.onlineschemachange.model.*;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;

public class OnlineSchemaChangeParametersTest {

    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        params.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(params.isContinueOnError());

        params.setErrorStrategy(TaskErrorStrategy.ABORT);
        assertFalse(params.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        ConnectionConfig mockConfig = Mockito.mock(ConnectionConfig.class);

        try {
            params.generateSubTaskParameters(mockConfig, "testSchema");
        } catch (UnexpectedException e) {
            assertTrue(e.getMessage().contains("Failed to generate subtasks with parameter: "));
        }
    }
}