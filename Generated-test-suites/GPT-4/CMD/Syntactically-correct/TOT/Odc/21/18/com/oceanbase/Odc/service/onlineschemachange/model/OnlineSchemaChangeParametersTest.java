package com.oceanbase.odc.service.onlineschemachange.model;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.util.*;
import org.junit.*;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;

public class OnlineSchemaChangeParametersTest {

    private OnlineSchemaChangeParameters onlineSchemaChangeParameters;
    private ConnectionConfig connectionConfig;

    @Before
    public void setUp() {
         onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
         connectionConfig = mock(ConnectionConfig.class);
         when(connectionConfig.getDialectType()).thenReturn("Some Dialect Type");
    }
  
    @Test
    public void testIsContinueOnError() {

        //Test with error strategy set to CONTINUE
        onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        boolean result = onlineSchemaChangeParameters.isContinueOnError();
        assertEquals(true, result);

        //Test with error strategy set to other than CONTINUE
        onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.FAIL);
        result = onlineSchemaChangeParameters.isContinueOnError();
        assertEquals(false, result);
    }

    @Test
    public void testGenerateSubTaskParameters() {
        //TO DO: Mock the needed classes
        //Pass more complex parameters into generateSubTaskParameters
        //Check the returned value correctness in different scenarios
        
        String schema = "test_schema";
        onlineSchemaChangeParameters.setSqlContent("Some SQL Content");
        
        try {
            onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, schema);
            //Test passes if no exception is thrown
        } catch (UnexpectedException e) {
            //Test fails if an exception is thrown
            Assert.fail();
        }
    }

    //Lots of missing test case...
}