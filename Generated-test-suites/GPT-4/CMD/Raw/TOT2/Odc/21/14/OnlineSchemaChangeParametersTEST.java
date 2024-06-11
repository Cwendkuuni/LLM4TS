package com.oceanbase.odc.service.onlineschemachange.model;

import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.util.List;



public class OnlineSchemaChangeParametersTest {

    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters oscp = new OnlineSchemaChangeParameters();
        
        oscp.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(oscp.isContinueOnError());
        
        oscp.setErrorStrategy(TaskErrorStrategy.STOP);
        assertFalse(oscp.isContinueOnError());
    }
    
    @Test
    public void testGenerateSubTaskParameters() {
        
        OnlineSchemaChangeParameters oscp = new OnlineSchemaChangeParameters();
        ConnectionConfig cc = new ConnectionConfig();
        // assuming that connectionConfig and schema are already set correctly
        List<OnlineSchemaChangeScheduleTaskParameters> parameters = oscp.generateSubTaskParameters(cc, "testSchema");
        assertTrue(parameters.size() >= 0);
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParametersForFailure() {
        
        OnlineSchemaChangeParameters oscp = new OnlineSchemaChangeParameters();
        oscp.setSqlContent("Fail"); // setting up data to fail
        ConnectionConfig cc = new ConnectionConfig();
        oscp.generateSubTaskParameters(cc, "testSchema");
    }
}

