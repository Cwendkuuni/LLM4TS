package com.oceanbase.odc.service.onlineschemachange.model;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.hamcrest.MatcherAssert.assertThat; 
import static org.hamcrest.Matchers.*;

public class OnlineSchemaChangeParametersTest {

    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters param = new OnlineSchemaChangeParameters();
        param.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(param.isContinueOnError());
        
        param.setErrorStrategy(TaskErrorStrategy.STOP);
        assertFalse(param.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() {
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        SubTaskParameterFactory subTaskParameterFactory = mock(SubTaskParameterFactory.class);

        OnlineSchemaChangeParameters param = new OnlineSchemaChangeParameters();
        param.setSqlContent("CREATE TABLE...");
        param.setSqlType(OnlineSchemaChangeSqlType.CREATE);
        assertThat(param.generateSubTaskParameters(connectionConfig, "schema"), is(not(empty())));
    }

    @Test
    public void testIsContinueOnErrorEdgeCase() {
        OnlineSchemaChangeParameters param = new OnlineSchemaChangeParameters();
        param.setErrorStrategy(null);
        assertFalse(param.isContinueOnError());
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParametersEdgeCase1() {
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        SubTaskParameterFactory subTaskParameterFactory = mock(SubTaskParameterFactory.class);

        OnlineSchemaChangeParameters param = new OnlineSchemaChangeParameters();
        param.setSqlContent(null);
        param.setSqlType(OnlineSchemaChangeSqlType.CREATE);
        param.generateSubTaskParameters(connectionConfig, "schema");
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParametersEdgeCase2() {
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        SubTaskParameterFactory subTaskParameterFactory = mock(SubTaskParameterFactory.class);

        OnlineSchemaChangeParameters param = new OnlineSchemaChangeParameters();
        param.setSqlContent("CREATE TABLE...");
        param.setSqlType(null);
        param.generateSubTaskParameters(connectionConfig, "schema"); 
    }
}