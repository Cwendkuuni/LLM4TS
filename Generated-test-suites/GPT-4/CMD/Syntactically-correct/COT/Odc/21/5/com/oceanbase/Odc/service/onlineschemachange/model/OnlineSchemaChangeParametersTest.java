package com.oceanbase.odc.service.onlineschemachange.model;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class OnlineSchemaChangeParametersTest {

    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        params.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(params.isContinueOnError());
        
        params.setErrorStrategy(TaskErrorStrategy.STOP);
        assertFalse(params.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() {
        //We need to mock the ConnectionConfig and SubTaskParameterFactory classes, as they are dependencies
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        SubTaskParameterFactory factory = mock(SubTaskParameterFactory.class);
        
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        params.setSqlContent("SQL_CONTENT");
        params.setSqlType(OnlineSchemaChangeSqlType.ALTER);

        //mock the behavior of split method
        when(SqlUtils.split(any(), anyString(), anyString())).thenReturn(Collections.singletonList("SQL_CONTENT"));

        //mock the behavior of the generate method of the factory
        OnlineSchemaChangeScheduleTaskParameters param = new OnlineSchemaChangeScheduleTaskParameters();
        param.setDatabaseName("DB");
        param.setOriginTableName("ORIGINAL");
        param.setNewTableName("NEW");
        when(factory.generate(anyString(), any())).thenReturn(param);
        
        List<OnlineSchemaChangeScheduleTaskParameters> output = params.generateSubTaskParameters(connectionConfig, "SCHEMA");

        assertFalse(output.isEmpty());
        assertEquals("DB", output.get(0).getDatabaseName());
        assertEquals("NEW", output.get(0).getNewTableName());
    }
}
