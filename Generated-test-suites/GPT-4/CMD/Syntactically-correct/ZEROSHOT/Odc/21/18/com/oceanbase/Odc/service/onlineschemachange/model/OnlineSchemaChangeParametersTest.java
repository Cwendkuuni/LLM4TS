package com.oceanbase.odc.service.onlineschemachange.model;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import org.junit.Test;
import org.junit.Assert;

import java.util.List;

public class OnlineSchemaChangeParametersTest {

    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        Assert.assertTrue(parameters.isContinueOnError());
        parameters.setErrorStrategy(TaskErrorStrategy.EXIT);
        Assert.assertFalse(parameters.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        ConnectionConfig config = new ConnectionConfig();
        config.setDialectType("sql");
        parameters.setSqlContent("CREATE TABLE testTable (id INTEGER PRIMARY KEY, name TEXT);");
        List<?> subTasksParameters = parameters.generateSubTaskParameters(config,"test");
        Assert.assertNotEmpty(subTasksParameters);
    }

    @Test(expected = NullPointerException.class)
    public void testGenerateSubTaskParametersException() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        ConnectionConfig config = null;
        parameters.setSqlContent("CREATE TABLE testTable (id INTEGER PRIMARY KEY, name TEXT);");
        List<?> subTasksParameters = parameters.generateSubTaskParameters(config,"test");
    }

    @Test
    public void testSetterGetter() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlType(null);
        parameters.setSqlContent(null);
        parameters.setErrorStrategy(null);
        parameters.setLockTableTimeOutSeconds(null);
        parameters.setSwapTableNameRetryTimes(null);
        parameters.setOriginTableCleanStrategy(null);
        parameters.setDelimiter(null);
        parameters.setLockUsers(null);
        parameters.setSwapTableType(null);

        Assert.assertNull(parameters.getSqlType());
        Assert.assertNull(parameters.getSqlContent());
        Assert.assertNull(parameters.getErrorStrategy());
        Assert.assertNull(parameters.getLockTableTimeOutSeconds());
        Assert.assertNull(parameters.getSwapTableNameRetryTimes());
        Assert.assertNull(parameters.getOriginTableCleanStrategy());
        Assert.assertNull(parameters.getDelimiter());
        Assert.assertNull(parameters.getLockUsers());
        Assert.assertNull(parameters.getSwapTableType());
    }
}