package com.oceanbase.odc.service.onlineschemachange.model;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;

public class OnlineSchemaChangeParametersTest {

    private OnlineSchemaChangeParameters onlineSchemaChangeParameters;

    @Before
    public void setup() {
        onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
    }

    @Test
    public void testIsContinueOnError() {
        onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(onlineSchemaChangeParameters.isContinueOnError());

        onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.FAIL);
        assertFalse(onlineSchemaChangeParameters.isContinueOnError());
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParametersException() {
        ConnectionConfig mockConnectionConfig = mock(ConnectionConfig.class);
        String mockSchema = "mockSchema";
       
        //assuming an exception is thrown from the method
        onlineSchemaChangeParameters.setSqlContent(null);
        onlineSchemaChangeParameters.generateSubTaskParameters(mockConnectionConfig, mockSchema);
    }
    
    // A normal test case where the generation of subtask parameters is successful can only be written if we have the required 
    // data to set in the ConnectionConfig instance. The main idea is to set the required 
    // attributes in the OnlineSchemaChangeParameters and ConnectionConfig instance and then call the generateSubTaskParameters method. 
    // Then verify the returned list for expected outcome.
}
