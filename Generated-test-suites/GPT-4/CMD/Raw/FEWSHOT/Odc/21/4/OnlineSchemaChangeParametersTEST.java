package com.oceanbase.odc.service.onlineschemachange.model;

import com.oceanbase.odc.core.flow.model.TaskParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import org.junit.Test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OnlineSchemaChangeParametersTest{

    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(parameters.isContinueOnError());

        parameters.setErrorStrategy(TaskErrorStrategy.FAIL);
        assertFalse(parameters.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlContent("CREATE; DROP");
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters.setDelimiter(";");

        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.MYSQL);

        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameters.generateSubTaskParameters(connectionConfig, "schema");
        assertNotNull(subTaskParameters);
        assertEquals(2, subTaskParameters.size());
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParametersWithException() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlContent("INVALID SQL");
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters.setDelimiter(";");

        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.MYSQL);

        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameters.generateSubTaskParameters(connectionConfig, "schema");
    }

    @Test
    public void testGettersAndSetters() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setLockTableTimeOutSeconds(100);
        parameters.setSwapTableNameRetryTimes(5);
        parameters.setErrorStrategy(TaskErrorStrategy.FAIL);
        parameters.setLockUsers(new ArrayList<>(Arrays.asList("user1", "user2")));
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters.setSwapTableType(SwapTableType.TEMP);
        parameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.DROP);
        parameters.setDelimiter(";");

        assertEquals(Integer.valueOf(100), parameters.getLockTableTimeOutSeconds());
        assertEquals(Integer.valueOf(5), parameters.getSwapTableNameRetryTimes());
        assertEquals(TaskErrorStrategy.FAIL, parameters.getErrorStrategy());
        assertEquals(Arrays.asList("user1", "user2"), parameters.getLockUsers());
        assertEquals(OnlineSchemaChangeSqlType.ALTER, parameters.getSqlType());
        assertEquals(SwapTableType.TEMP, parameters.getSwapTableType());
        assertEquals(OriginTableCleanStrategy.DROP, parameters.getOriginTableCleanStrategy());
        assertEquals(";", parameters.getDelimiter());
    }
}