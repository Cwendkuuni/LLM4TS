package com.oceanbase.odc.service.onlineschemachange.model;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.oceanbase.odc.service.onlineschemachange.model.*;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;

public class OnlineSchemaChangeParametersTest {
    private OnlineSchemaChangeParameters parameters;

    @Before
    public void setUp() {
        parameters = new OnlineSchemaChangeParameters();
    }

    @Test
    public void testIsContinueOnError() {
        // when the error strategy is CONTINUE
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(parameters.isContinueOnError());

        // when the error strategy is not CONTINUE
        parameters.setErrorStrategy(TaskErrorStrategy.STOP);
        assertFalse(parameters.isContinueOnError());
    }

    // Just an example of how to test the method which depends on other objects
    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParameters_ThrowException() {
        ConnectionConfig mockConfig = mock(ConnectionConfig.class);
        parameters.setSqlContent(""); // this would lead to an exception as the SQL will be empty

        parameters.generateSubTaskParameters(mockConfig, "some_schema");
    }

    // Add more tests for generateSubTaskParameters method for normal cases, edge cases, and other exception scenarios here

}