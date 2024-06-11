package com.oceanbase.odc.service.onlineschemachange.model;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.mockito.Mock;

import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;

public class OnlineSchemaChangeParametersTest {
    @Mock
    ConnectionConfig connectionConfig;

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParameters() {
        OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();

        onlineSchemaChangeParameters.setSqlContent("");
        onlineSchemaChangeParameters.setDelimiter(";");
        onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        
        onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, "");
    }
    
    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
        onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(onlineSchemaChangeParameters.isContinueOnError());

        onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.STOP);
        assertFalse(onlineSchemaChangeParameters.isContinueOnError());
    }

    @Test
    public void testLockTableTimeOutSeconds() {
        OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
        onlineSchemaChangeParameters.setLockTableTimeOutSeconds(5);
        assertEquals((Integer)5, onlineSchemaChangeParameters.getLockTableTimeOutSeconds());
    }

    @Test
    public void testSwapTableNameRetryTimes() {
        OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
        onlineSchemaChangeParameters.setSwapTableNameRetryTimes(10);
        assertEquals((Integer)10, onlineSchemaChangeParameters.getSwapTableNameRetryTimes());
    }

    @Test
    public void testLockUsers() {
        List<String> users = Arrays.asList("user1", "user2");

        OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
        onlineSchemaChangeParameters.setLockUsers(users);
        assertEquals(users, onlineSchemaChangeParameters.getLockUsers());
    }
    
    @Test
    public void testSwapTableType() {
        OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
        onlineSchemaChangeParameters.setSwapTableType(SwapTableType.NEW);
        assertEquals(SwapTableType.NEW, onlineSchemaChangeParameters.getSwapTableType());
    }
}