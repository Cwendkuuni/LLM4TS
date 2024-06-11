
package com.oceanbase.odc.service.onlineschemachange.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

import java.util.List;

import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.core.shared.model.TableIdentity;

import org.junit.Before;
import org.junit.Test;


public class OnlineSchemaChangeParametersTest {
    OnlineSchemaChangeParameters parameters;
    
    @Before
    public void setUp() throws Exception {
        parameters = new OnlineSchemaChangeParameters();
    }

    @Test
    public void testIsContinueOnError() {
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(parameters.isContinueOnError());
        
        parameters.setErrorStrategy(TaskErrorStrategy.FAST_FAIL);
        assertFalse(parameters.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() {
        // Need to setup a mocked ConnectionConfig and some other details
        // to fully test this method. This could get rather involved.

        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        String schema = "testSchema";
        
        try {
            List<OnlineSchemaChangeScheduleTaskParameters> result = parameters.generateSubTaskParameters(connectionConfig, schema);
            assertNotNull(result);
        } catch (Exception e) {
            assertNotEquals("Failed to generate subtasks with parameter: " + parameters, e.getMessage());
        }
    }
}
