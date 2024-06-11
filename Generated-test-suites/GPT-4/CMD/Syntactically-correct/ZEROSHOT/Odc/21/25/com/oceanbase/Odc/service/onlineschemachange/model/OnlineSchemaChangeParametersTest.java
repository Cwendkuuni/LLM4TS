
package com.oceanbase.odc.service.onlineschemachange.model;

import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mockito;

import java.util.List;

public class OnlineSchemaChangeParametersTest {
    @Test
    public void testIsContinueOnError(){
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        params.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(params.isContinueOnError());

        params.setErrorStrategy(TaskErrorStrategy.FAST_FAIL);
        assertFalse(params.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters(){
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        ConnectionConfig mockConfig = Mockito.mock(ConnectionConfig.class);
        Mockito.when(mockConfig.getDialectType()).thenReturn("MySQL");
        
        params.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        params.setSqlContent("ALTER TABLE Test ADD id INT; ALTER TABLE Test CHANGE name new_name VARCHAR(255);");

        try {
            List<OnlineSchemaChangeScheduleTaskParameters> subTasks = params.generateSubTaskParameters(mockConfig, "testSchema");
            assertEquals(2, subTasks.size());
            
            OnlineSchemaChangeScheduleTaskParameters subtask1 = subTasks.get(0);
            assertEquals("Test", subtask1.getOriginTableName());
            assertEquals("ADD id INT", subtask1.getSqlsToBeExecuted().get(0));

            OnlineSchemaChangeScheduleTaskParameters subtask2 = subTasks.get(1);
            assertEquals("Test", subtask2.getOriginTableName());
            assertEquals("CHANGE name new_name VARCHAR(255)", subtask2.getSqlsToBeExecuted().get(0));
        } catch (UnexpectedException e) {
            fail("No exception should be thrown");
        }
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParametersException(){
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        ConnectionConfig mockConfig = Mockito.mock(ConnectionConfig.class);
        Mockito.when(mockConfig.getDialectType()).thenReturn("MySQL");

        params.setSqlType(null); // force generate error
        params.setSqlContent("ALTER TABLE Test ADD id INT;");

        params.generateSubTaskParameters(mockConfig, "testSchema");
    }
}
