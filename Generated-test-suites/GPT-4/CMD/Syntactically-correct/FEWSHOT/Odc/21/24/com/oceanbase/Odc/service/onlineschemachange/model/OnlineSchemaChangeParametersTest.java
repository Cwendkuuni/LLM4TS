package com.oceanbase.odc.service.onlineschemachange.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class OnlineSchemaChangeParametersTest {
    
    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();

        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(parameters.isContinueOnError());

        parameters.setErrorStrategy(TaskErrorStrategy.STOP);
        assertFalse(parameters.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters.setSqlContent("ALTER TABLE test_table ADD COLUMN column_test int;");
        parameters.setDelimiter(";");

        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.ORACLE);

        List<OnlineSchemaChangeScheduleTaskParameters> subTask = parameters.generateSubTaskParameters(connectionConfig, "test_schema");

        assertEquals(1, subTask.size());
        assertEquals(OnlineSchemaChangeSqlType.ALTER, subTask.get(0).getSqlType());
        assertEquals("test_schema", subTask.get(0).getDatabaseName());
        assertEquals("test_table", subTask.get(0).getOriginTableName());
        assertEquals("column_test int;", subTask.get(0).getSqlsToBeExecuted().get(0));
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParametersException() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlContent("INVALID SQL");
        ConnectionConfig connectionConfig = new ConnectionConfig();
        parameters.generateSubTaskParameters(connectionConfig, "test_schema");
    }
    
    @Test
    public void testSwapTableType() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSwapTableType(SwapTableType.TEMP);

        assertEquals(SwapTableType.TEMP, parameters.getSwapTableType());
    }

    @Test
    public void testLockUsers() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        List<String> lockUsers = Arrays.asList("user1", "user2").stream().collect(Collectors.toList());

        parameters.setLockUsers(lockUsers);
        List<String> result = parameters.getLockUsers();

        assertEquals(2, result.size());
        assertTrue(result.containsAll(Arrays.asList("user1", "user2")));
    }

    @Test
    public void testErrorStrategy() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setErrorStrategy(TaskErrorStrategy.STOP);
        assertEquals(TaskErrorStrategy.STOP, parameters.getErrorStrategy());
    }

}