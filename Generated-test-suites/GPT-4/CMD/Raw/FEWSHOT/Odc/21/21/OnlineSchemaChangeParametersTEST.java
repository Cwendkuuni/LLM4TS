package com.oceanbase.odc.service.onlineschemachange.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import com.oceanbase.odc.core.flow.model.TaskParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;

import org.junit.Test;
import org.junit.Before;

import java.util.List;

public class OnlineSchemaChangeParametersTest {
    private OnlineSchemaChangeParameters parameters;

    @Before
    public void setUp() {
        parameters = new OnlineSchemaChangeParameters();
        // Adjust the parameter initialization as per your requirement.
    }

    @Test
    public void testIsContinueOnError() {
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(parameters.isContinueOnError());
        parameters.setErrorStrategy(TaskErrorStrategy.TERMINATE);
        assertFalse(parameters.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() {
        ConnectionConfig config = new ConnectionConfig();
        // Initialize connectionConfig parameters as per your need
        String schema = ""; // provide schema as per your need
        List<OnlineSchemaChangeScheduleTaskParameters> taskParameters = parameters.generateSubTaskParameters(config, schema);
        //assert the taskParameters based on initialized setup and schema
        assertNotNull(taskParameters);
    }

    // Similarly, write additional tests for setters and getters, ensure all edge cases are covered.
}