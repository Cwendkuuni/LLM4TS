package com.oceanbase.odc.service.onlineschemachange.model;

import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.model.*;
import com.oceanbase.odc.service.common.util.SqlUtils;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;


public class OnlineSchemaChangeParametersTest {

    private OnlineSchemaChangeParameters parameters;
    private ConnectionConfig connectionConfig;

    @Before
    public void setUp() {
        parameters = new OnlineSchemaChangeParameters();
        connectionConfig = new ConnectionConfig();
    }

    @Test
    public void testIsContinueOnError() {
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        Assert.assertTrue(parameters.isContinueOnError());
        parameters.setErrorStrategy(TaskErrorStrategy.ABORT);
        Assert.assertFalse(parameters.isContinueOnError());
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParametersException() {
        parameters.setSqlContent("INVALID SQL CONTENT");
        parameters.generateSubTaskParameters(connectionConfig, "testSchema");
    }

    @Test
    public void testGenerateSubTaskParameters() {
        parameters.setSqlContent("VALID SQL CONTENT");
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters.generateSubTaskParameters(connectionConfig, "testSchema");
    }

    @Test
    public void testSetSwapTableNameRetryTimes() {
        parameters.setSwapTableNameRetryTimes(5);
        Assert.assertEquals(5, parameters.getSwapTableNameRetryTimes().intValue());
    }

    @Test
    public void testLockUsers() {
        List<String> users = new ArrayList<>();
        users.add("user1");
        users.add("user2");
        parameters.setLockUsers(users);
        Assert.assertEquals(users, parameters.getLockUsers());
    }

    @Test
    public void testSwapTableType() {
        parameters.setSwapTableType(SwapTableType.SWAP);
        Assert.assertEquals(SwapTableType.SWAP, parameters.getSwapTableType());
    }

    @Test
    public void testSetDelimiter() {
        parameters.setDelimiter("#");
        Assert.assertEquals("#", parameters.getDelimiter());
    }

}
