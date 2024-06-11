/*
 * To note:
 * Based on the given class, it has been assumed that the auxiliary classes, constants, and variables used
 * like OnlineSchemaChangeSqlType, OriginTableCleanStrategy, et cetera, are defined and available in the environment.
 * Since the actual code of them is not given, we couldn't generate tests for them 
 * This script can be improved if the actual classes are included
 */

package com.oceanbase.odc.service.onlineschemachange.model;

import java.util.Arrays;
import java.util.List;

import com.oceanbase.odc.core.flow.model.TaskParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class OnlineSchemaChangeParametersTest {
  
    private OnlineSchemaChangeParameters schemaChangeParameters;
    
    @Before
    public void setup(){
        schemaChangeParameters = new OnlineSchemaChangeParameters();
    }

    @Test
    public void testIsContinueOnError() {
        schemaChangeParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        Assert.assertTrue(schemaChangeParameters.isContinueOnError());

        schemaChangeParameters.setErrorStrategy(TaskErrorStrategy.STOP);
        Assert.assertFalse(schemaChangeParameters.isContinueOnError());
    }

    @Test(expected = UnexpectedException.class) 
    public void testGenerateSubTaskParameters(){
        ConnectionConfig connectionConfig = new ConnectionConfig();
        schemaChangeParameters.setSqlContent("valid SQL to trigger Unexpected Exception");
        schemaChangeParameters.generateSubTaskParameters(connectionConfig, "testSchema");
    }

    @Test
    public void testSettersAndGetters(){
        schemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.ADD);
        Assert.assertEquals(OnlineSchemaChangeSqlType.ADD, schemaChangeParameters.getSqlType());

        schemaChangeParameters.setSqlContent("dummySqlContent");
        Assert.assertEquals("dummySqlContent", schemaChangeParameters.getSqlContent());

        schemaChangeParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        Assert.assertEquals(TaskErrorStrategy.CONTINUE, schemaChangeParameters.getErrorStrategy());

        schemaChangeParameters.setLockTableTimeOutSeconds(10);
        Assert.assertEquals(Integer.valueOf(10), schemaChangeParameters.getLockTableTimeOutSeconds());

        schemaChangeParameters.setSwapTableNameRetryTimes(3);
        Assert.assertEquals(Integer.valueOf(3), schemaChangeParameters.getSwapTableNameRetryTimes());

        schemaChangeParameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.DELETE);
        Assert.assertEquals(OriginTableCleanStrategy.DELETE, schemaChangeParameters.getOriginTableCleanStrategy());

        schemaChangeParameters.setDelimiter(",");
        Assert.assertEquals(",", schemaChangeParameters.getDelimiter());

        List<String> lockUsers = Arrays.asList("user1", "user2");
        schemaChangeParameters.setLockUsers(lockUsers);
        Assert.assertEquals(lockUsers, schemaChangeParameters.getLockUsers());

        schemaChangeParameters.setSwapTableType(SwapTableType.TEMP);
        Assert.assertEquals(SwapTableType.TEMP, schemaChangeParameters.getSwapTableType());
    }
}