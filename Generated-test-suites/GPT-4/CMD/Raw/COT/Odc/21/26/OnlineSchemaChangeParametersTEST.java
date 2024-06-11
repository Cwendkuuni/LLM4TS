
package com.oceanbase.odc.service.onlineschemachange.model;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.junit.Assert;
import static org.junit.Assert.*;

import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import java.util.List;
import java.util.ArrayList;

public class OnlineSchemaChangeParametersTest {
    @Mock
    private ConnectionConfig connectionConfig;

    private OnlineSchemaChangeParameters parameters;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        parameters = new OnlineSchemaChangeParameters();
    }

    @Test
    public void testIsContinueOnError() {
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(parameters.isContinueOnError());

        parameters.setErrorStrategy(TaskErrorStrategy.STOP_ON_ERROR);
        assertFalse(parameters.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() {
        parameters.setDelimiter(";");
        parameters.setSqlContent("Create Table;");
        parameters.setSqlType(OnlineSchemaChangeSqlType.ADD_COLUMN);

        try{
            parameters.generateSubTaskParameters(connectionConfig, "test");
        }
        catch(Exception e){
            fail("Exception should not be thrown");
        }
        

        parameters.setSqlContent(null);
        try{
            parameters.generateSubTaskParameters(connectionConfig, "test");
            fail("Exception should be thrown for null SQL content");
        }
        catch(Exception e){
            assertEquals("Failed to generate subtasks with parameter: " + parameters, e.getMessage());
        }

        parameters.setSqlContent("Create Table;");
        parameters.setSqlType(null);
        try{
            parameters.generateSubTaskParameters(connectionConfig, "test");
            fail("Exception should be thrown for null SQL type");
        }
        catch(Exception e){
            assertEquals("Failed to generate subtasks with parameter: " + parameters, e.getMessage());
        }
    }

}

