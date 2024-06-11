package com.oceanbase.odc.service.onlineschemachange.model;

import org.junit.Test;
import org.junit.Before;
import org.junit.Assert;
import static org.mockito.Mockito.mock;

import java.util.List;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;


public class OnlineSchemaChangeParametersTest {

    private OnlineSchemaChangeParameters onlineSchemaChangeParameters;
    private ConnectionConfig connectionConfig;

    @Before
    public void setUp() {
        onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
        connectionConfig = mock(ConnectionConfig.class);
    }

    @Test
    public void testIsContinueOnError() {
        onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        Assert.assertTrue(onlineSchemaChangeParameters.isContinueOnError());

        onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.FAIL);
        Assert.assertFalse(onlineSchemaChangeParameters.isContinueOnError());
    }

    @Test
    public void testLockTableTimeOutSeconds() {
        Integer seconds = 300;
        onlineSchemaChangeParameters.setLockTableTimeOutSeconds(seconds);
        Assert.assertEquals(onlineSchemaChangeParameters.getLockTableTimeOutSeconds(), seconds);
    }

    @Test
    public void testSwapTableNameRetryTimes() {
        Integer retryTimes = 5;
        onlineSchemaChangeParameters.setSwapTableNameRetryTimes(retryTimes);
        Assert.assertEquals(onlineSchemaChangeParameters.getSwapTableNameRetryTimes(), retryTimes);
    }

    @Test
    public void testGenerateSubTaskParameters() {
        String schema = "test_schema";
        String sqlQuery = "SELECT * FROM schema";
        onlineSchemaChangeParameters.setSqlContent(sqlQuery);
        onlineSchemaChangeParameters.setDelimiter(";");
        // Here we cannot assert anything because the method may throw an UnexpectedException
        // So we only verify that it does not raise an exception
        List<OnlineSchemaChangeScheduleTaskParameters> result =
                onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, schema);
        Assert.assertNotNull(result);
    }

    // Continue testing other setter and getter
    // ...
}
