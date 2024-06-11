package com.oceanbase.odc.service.onlineschemachange.model;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;

public class OnlineSchemaChangeParametersTest {
    private OnlineSchemaChangeParameters onlineSchemaChangeParameters;
    private ConnectionConfig connectionConfig;

    @Before
    public void setUp() {
        onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
        onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        onlineSchemaChangeParameters.setSqlContent("CREATE TABLE Customers (CustomerName varchar(255), ContactName varchar(255), Country varchar(255));");

        connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("mysql");
    }

    @Test
    public void testIsContinueOnError() {
        assertTrue(onlineSchemaChangeParameters.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() {
        List<OnlineSchemaChangeScheduleTaskParameters> generatedParameters = onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, "database1");
       
        assertNotNull(generatedParameters);
        assertEquals(1, generatedParameters.size());
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParametersException() {
        OnlineSchemaChangeParameters exceptionParameters = new OnlineSchemaChangeParameters();
        exceptionParameters.generateSubTaskParameters(null, null);
    }
}