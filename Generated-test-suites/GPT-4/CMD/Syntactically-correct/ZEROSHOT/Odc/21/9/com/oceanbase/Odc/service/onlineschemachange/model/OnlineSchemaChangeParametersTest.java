
package com.oceanbase.odc.service.onlineschemachange.model;

import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.List;

public class OnlineSchemaChangeParametersTest {
    
    private OnlineSchemaChangeParameters parameters;
    private ConnectionConfig connectionConfig;

    @Before
    public void setup() {
        parameters = new OnlineSchemaChangeParameters();
        connectionConfig = mock(ConnectionConfig.class);
    }

    @Test
    public void testIsContinueOnError() {
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(parameters.isContinueOnError());

        parameters.setErrorStrategy(TaskErrorStrategy.STOP);
        assertFalse(parameters.isContinueOnError());
    }
    
    @Test
    public void testDelimiter(){
        parameters.setDelimiter(",");
        assertEquals(",",
                parameters.getDelimiter());
    }

    @Test
    public void testGenerateSubTaskParameters() {
        when(connectionConfig.getDialectType()).thenReturn("mysql");
        parameters.setSqlContent("CREATE TABLE table1 (column1 INT);");
        parameters.setSqlType(OnlineSchemaChangeSqlType.ADD_COLUMN);
        
        try {
            List<OnlineSchemaChangeScheduleTaskParameters> result = parameters.generateSubTaskParameters(connectionConfig, "database1");
            assertEquals(1, result.size());
            assertEquals("database1", result.get(0).getDatabaseName());
            assertEquals("table1", result.get(0).getOriginTableName());
        } catch (UnexpectedException e) {
            fail("Unexpected Exception thrown: " + e.getMessage());
        }
    }
    
    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParametersException() {
        when(connectionConfig.getDialectType()).thenReturn("invalid");
        parameters.setSqlContent("CREATE TABLE table1 (column1 INT);");
        parameters.generateSubTaskParameters(connectionConfig, "database1");
    }
}
