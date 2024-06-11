package com.oceanbase.odc.service.onlineschemachange.model;

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import com.oceanbase.odc.core.flow.model.TaskParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.model.TableIdentity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import java.util.List;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.anyString;

public class OnlineSchemaChangeParametersTest{
    
    private OnlineSchemaChangeParameters parameters;
    private ConnectionConfig connectionConfigMock;
    private static final String TEST_SQL_CONTENT = "Test Sql Content";

    @Before
    public void setUp(){
        parameters = new OnlineSchemaChangeParameters();
        connectionConfigMock = mock(ConnectionConfig.class);
    }

    @Test
    public void isContinueOnErrorTest(){
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        Assert.assertTrue(parameters.isContinueOnError());
        
        parameters.setErrorStrategy(TaskErrorStrategy.On_ERROR_ANY);
        Assert.assertFalse(parameters.isContinueOnError());
    }

    @Test(expected = UnexpectedException.class)
    public void generateSubTaskParametersExceptionTest(){
        when(SqlUtils.split(anyString(), anyString(), anyString())).thenThrow(new Exception());
        parameters.setSqlContent(TEST_SQL_CONTENT);
        parameters.generateSubTaskParameters(connectionConfigMock, "testSchema");
    }

    @Test
    public void generateSubTaskParametersTest(){
        when(connectionConfigMock.getDialectType()).thenReturn("MySQL");
        parameters.setSqlContent(TEST_SQL_CONTENT);
        List<OnlineSchemaChangeScheduleTaskParameters> result = parameters.generateSubTaskParameters(connectionConfigMock, "testSchema");
        Assert.assertNotNull(result);
        Assert.assertFalse(result.isEmpty());
    }
}
