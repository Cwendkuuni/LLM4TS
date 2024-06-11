package com.oceanbase.odc.service.onlineschemachange.model;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;

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
        final OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        
        parameters.setSqlContent("CREATE TABLE...");
        parameters.setSqlType(OnlineSchemaChangeSqlType.CREATE);
        parameters.setDelimiter(";");
        
        final ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.MYSQL);

        try {
            final List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = 
                parameters.generateSubTaskParameters(connectionConfig, "schema");
            
            assertFalse(subTaskParameters.isEmpty());
        } catch (UnexpectedException e) {
            fail("UnexpectedException was not supposed to be thrown.");
        }
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParametersError() {
        final OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        
        parameters.setSqlContent("INVALID SQL...");
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters.setDelimiter(";");
        
        final ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.MYSQL);

        parameters.generateSubTaskParameters(connectionConfig, "schema");
    }
}