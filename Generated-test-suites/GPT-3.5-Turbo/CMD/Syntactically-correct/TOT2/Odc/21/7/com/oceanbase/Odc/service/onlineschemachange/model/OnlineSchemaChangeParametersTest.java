package com.oceanbase.odc.service.onlineschemachange.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class OnlineSchemaChangeParametersTest {
    
    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        params.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(params.isContinueOnError());
    }
    
    @Test
    public void testGenerateSubTaskParameters() {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        params.setSqlContent("ALTER TABLE test ADD COLUMN id INT;");
        params.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParams = params.generateSubTaskParameters(new ConnectionConfig(), "testDB");

        assertEquals(1, subTaskParams.size());
        
        OnlineSchemaChangeScheduleTaskParameters subTaskParam = subTaskParams.get(0);
        assertEquals("testDB", subTaskParam.getDatabaseName());
        assertEquals("test", subTaskParam.getOriginTableName());
        assertEquals(1, subTaskParam.getSqlsToBeExecuted().size());
    }
    
    @Test
    public void testDefaultValues() {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        
        assertEquals(TaskErrorStrategy.STOP, params.getErrorStrategy());
        assertNull(params.getLockTableTimeOutSeconds());
        assertNull(params.getSwapTableNameRetryTimes());
        assertNull(params.getOriginTableCleanStrategy());
        assertEquals(";", params.getDelimiter());
        assertEquals(new ArrayList<String>(), params.getLockUsers());
        assertNull(params.getSwapTableType());
    }
}