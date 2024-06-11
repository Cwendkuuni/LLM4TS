package com.oceanbase.odc.service.onlineschemachange.model;

import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.List;

public class OnlineSchemaChangeParametersTest{

    private OnlineSchemaChangeParameters parameters;

    @Before
    public void setUp(){
        parameters = new OnlineSchemaChangeParameters();
    }

    @Test
    public void testIsContinueOnError(){
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(parameters.isContinueOnError());
        parameters.setErrorStrategy(TaskErrorStrategy.OTHER_STRATEGY);  // You should change "OTHER_STRATEGY" to a valid value
        assertFalse(parameters.isContinueOnError());
    }

    @Test
    public void testIsContinueOnError_Null(){
        parameters.setErrorStrategy(null);
        try{
            parameters.isContinueOnError();
            fail("Expected UnexpectedException");
        } catch (UnexpectedException e){
            // expected
        }
    }

    // To test generateSubTaskParameters, a ConnectionConfig object and schema string need to be set up
    @Test
    public void testGenerateSubTaskParameters(){
        ConnectionConfig config = new ConnectionConfig();
        //config.set... You should configure the ConnectionConfig object accordingly 

        String schema = "...";  // Provide a valid schema

        try{
            List<OnlineSchemaChangeScheduleTaskParameters> result = parameters.generateSubTaskParameters(config, schema);
            // Assertions follow based on what you expect the result to be 
        } catch (UnexpectedException e){
            // Handle exception 
        }
    }
}