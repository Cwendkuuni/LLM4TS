package com.oceanbase.odc.service.onlineschemachange.model;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.oceanbase.odc.core.connection.model.ConnectionConfig;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.model.TableIdentity;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;

@SuppressWarnings("javasqlno rs")
public class OnlineSchemaChangeParametersTest {

    @Test
    public void testOnlineSchemaChangeParameters() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        assertEquals("Default value for OnlineSchemaChangeParameters", OnlineSchemaChangeSqlType.DEFAULT,
                parameters.getSqlType());
        assertTrue("Default value for lockTableTimeOutSeconds is zero", parameters.getLockTableTimeOutSeconds() == 0);
        assertTrue("Default value for swapTableNameRetryTimes is zero", parameters.getSwapTableNameRetryTimes() == 0);
        assertEquals("Default value for errorStrategy is 'STOP'", TaskErrorStrategy.STOP, parameters.getErrorStrategy());
        assertNull("Default value for delimiter is null", parameters.getDelimiter());
        assertTrue("Default value for list of lockUsers is empty", parameters.getLockUsers().isEmpty());
        assertEquals("Default value for swapTableType is 'SWAP_IN_PLACE'", SwapTableType.SWAP_IN_PLACE,
                parameters.getSwapTableType());

        OnlineSchemaChangeParameters newParameters = new OnlineSchemaChangeParameters();
        OnlineSchemaChangeSqlType sqlType = OnlineSchemaChangeSqlType.ALTER;
        String sqlContent = "ALTER TABLE test_table ADD COLUMN new_column INT";
        TaskErrorStrategy errorStrategy = TaskErrorStrategy.CONTINUE;
        Integer lockTableTimeOutSeconds = 10;
        Integer swapTableNameRetryTimes = 3;
        OriginTableCleanStrategy originTableCleanStrategy = OriginTableCleanStrategy.RENAME;
        String delimiter = ";";
        List<String> lockUsers = new ArrayList<>();
        lockUsers.add("user1");
        lockUsers.add("user2");
        SwapTableType swapTableType = SwapTableType.SWAP_TO_NEW_TABLE;

        newParameters.setSqlType(sqlType);
        newParameters.setSqlContent(sqlContent);
        newParameters.setErrorStrategy(errorStrategy);
        newParameters.setLockTableTimeOutSeconds(lockTableTimeOutSeconds);
        newParameters.setSwapTableNameRetryTimes(swapTableNameRetryTimes);
        newParameters.setOriginTableCleanStrategy(originTableCleanStrategy);
        newParameters.setDelimiter(delimiter);
        newParameters.setLockUsers(lockUsers);
        newParameters.setSwapTableType(swapTableType);

        assertEquals("Test setSqlType method", sqlType, newParameters.getSqlType());
        assertEquals("Test getSqlContent method", sqlContent, newParameters.getSqlContent());
        assertEquals("Test getErrorStrategy method", errorStrategy, newParameters.getErrorStrategy());
        assertEquals("Test getLockTableTimeOutSeconds method", lockTableTimeOutSeconds,
                newParameters.getLockTableTimeOutSeconds());
        assertEquals("Test getSwapTableNameRetryTimes method", swapTableNameRetryTimes,
                newParameters.getSwapTableNameRetryTimes());
        assertEquals("Test getOriginTableCleanStrategy method", originTableCleanStrategy,
                newParameters.getOriginTableCleanStrategy());
        assertEquals("Test getDelimiter method", delimiter, newParameters.getDelimiter());
        assertEquals("Test getLockUsers method", lockUsers, newParameters.getLockUsers());
        assertEquals("Test getSwapTableType method", swapTableType, newParameters.getSwapTableType());

        ConnectionConfig connectionConfig = new ConnectionConfig();
        String schema = "test_schema";
        List<OnlineSchemaChangeScheduleTaskParameters> taskParameters = newParameters.generateSubTaskParameters(connectionConfig, schema);
        assertEquals("Test generateSubTaskParameters method, size of list", 1, taskParameters.size());
        OnlineSchemaChangeScheduleTaskParameters taskParameter = taskParameters.get(0);
        TableIdentity key = new TableIdentity("test_database", "test_table");
        assertEquals("Test generateSubTaskParameters method, key matches", key, taskParameter.getKey());
        assertEquals("Test generateSubTaskParameters method, sqlsToBeExecuted size", 1, taskParameter.getSqlsToBeExecuted().size());
        String sql = "ALTER TABLE test_table ADD COLUMN new_column INT";
        String newAlterStmt = DdlUtils.replaceTableName(sql, "new_test_table", connectionConfig.getDialectType(), sqlType);
        assertTrue("Test generateSubTaskParameters method, sql in sqlsToBeExecuted matches", sql.equals(taskParameter.getSqlsToBeExecuted().get(0))
                || newAlterStmt.equals(taskParameter.getSqlsToBeExecuted().get(0)));
    }

    @Test(expected = UnexpectedException.class)
    public void testUnexpectedException() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        ConnectionConfig connectionConfig = new ConnectionConfig();
        String schema = "test_schema";
        List<OnlineSchemaChangeScheduleTaskParameters> taskParameters = parameters.generateSubTaskParameters(connectionConfig, schema);

        // This test case should throw an UnexpectedException
    }

    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        assertFalse("Default value for isContinueOnError method is false", parameters.isContinueOnError());

        TaskErrorStrategy errorStrategy = TaskErrorStrategy.CONTINUE;
        parameters.setErrorStrategy(errorStrategy);
        assertTrue("Test isContinueOnError method, continueOnError set to true", parameters.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParametersWithEmptySqls() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        ConnectionConfig connectionConfig = new ConnectionConfig();
        String schema = "test_schema";

        List<OnlineSchemaChangeScheduleTaskParameters> taskParameters = parameters.generateSubTaskParameters(connectionConfig, schema);

        assertEquals("Test generateSubTaskParameters method with empty sqls, size of list", 0, taskParameters.size());
    }
}