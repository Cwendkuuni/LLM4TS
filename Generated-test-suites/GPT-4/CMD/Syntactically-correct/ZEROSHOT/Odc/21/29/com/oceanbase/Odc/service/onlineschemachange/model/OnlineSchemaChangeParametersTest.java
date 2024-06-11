package com.oceanbase.odc.service.onlineschemachange.model;

import com.oceanbase.odc.service.onlineschemachange.model.*;
import com.oceanbase.odc.core.flow.model.*;
import com.oceanbase.odc.core.shared.constant.*;
import com.oceanbase.odc.service.connection.model.*;
import com.oceanbase.odc.service.onlineschemachange.model.*;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class OnlineSchemaChangeParametersTest {

    @Test
    public void continueOnErrorTest() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(parameters.isContinueOnError());

        parameters.setErrorStrategy(TaskErrorStrategy.STOP);
        assertFalse(parameters.isContinueOnError());
    }

    @Test
    public void sqlsToBeExecutedTest() {
        String sql1 = "SELECT * FROM Table1";
        String sql2 = "DELETE FROM Table2 WHERE ID > 10";
        String sql3 = "UPDATE Table3 SET Salary = 6000 WHERE ID > 20";
        
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlContent(sql1 + ";" + sql2 + ";" + sql3);
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        ConnectionConfig connectionConfig = new ConnectionConfig();
        
        try {
            List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameters.generateSubTaskParameters(connectionConfig, "mySchema");
            assertEquals(3, subTaskParameters.size());

            for(OnlineSchemaChangeScheduleTaskParameters param : subTaskParameters) {
                assertTrue(param.getSqlsToBeExecuted().contains(sql1) || param.getSqlsToBeExecuted().contains(sql2) || param.getSqlsToBeExecuted().contains(sql3));
            }
            
        } catch(UnexpectedException e){
            fail("Unexpected Exception: " + e.getMessage());
        }
    }
    
    @Test(expected=UnexpectedException.class)
    public void failedSqlsToBeExecutedTest() {
        String sql = "SELECT * FROM Table1";
        
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlContent(sql);
        ConnectionConfig connectionConfig = new ConnectionConfig();
        
        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameters.generateSubTaskParameters(connectionConfig, "mySchema");
    }
}
