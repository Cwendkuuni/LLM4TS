package com.oceanbase.odc.service.onlineschemachange.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.oceanbase.odc.core.flow.model.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;

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
        oscp.setSqlContent("CREATE TABLE Students (ID INT, NAME VARCHAR);");
        
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("OCEANBASE");
        
        List<OnlineSchemaChangeScheduleTaskParameters> taskParameters = oscp.generateSubTaskParameters(connectionConfig, "school");
        
        assertNotNull(taskParameters);
        assertEquals(1, taskParameters.size());

        OnlineSchemaChangeScheduleTaskParameters firstTaskParam = taskParameters.get(0);
        assertEquals("school", firstTaskParam.getDatabaseName());
        assertEquals("Students", firstTaskParam.getOriginTableName());
    }
    
    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParametersException() {
        OnlineSchemaChangeParameters oscp = new OnlineSchemaChangeParameters();
        oscp.setSqlContent(null);
        
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("OCEANBASE");

        oscp.generateSubTaskParameters(connectionConfig, "school"); //should throw UnexpectedException
    }
}