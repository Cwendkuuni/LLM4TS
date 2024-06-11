package com.oceanbase.odc.service.onlineschemachange.model.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.oceanbase.odc.core.flow.model.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;

public class OnlineSchemaChangeParametersTest {

    @Test
    public void testOnlineSchemaChangeParameters() {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        // Test default values
        assertNull(params.getSqlType());
        assertNull(params.getSqlContent());
        assertNull(params.getErrorStrategy());
        assertNull(params.getLockTableTimeOutSeconds());
        assertNull(params.getSwapTableNameRetryTimes());
        assertNull(params.getOriginTableCleanStrategy());
        assertNull(params.getDelimiter());
        assertNull(params.getLockUsers());
        assertNull(params.getSwapTableType());

        // Test setter and getter
        OnlineSchemaChangeSqlType sqlType = OnlineSchemaChangeSqlType.ALTER;
        params.setSqlType(sqlType);
        assertSame(sqlType, params.getSqlType());

        String sqlContent = "create table test (id int primary key)";
        params.setSqlContent(sqlContent);
        assertSame(sqlContent, params.getSqlContent());

        params.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(params.isContinueOnError());

        params.setLockTableTimeOutSeconds(10);
        assertEquals(10, params.getLockTableTimeOutSeconds().intValue());

        params.setSwapTableNameRetryTimes(3);
        assertEquals(3, params.getSwapTableNameRetryTimes().intValue());

        params.setOriginTableCleanStrategy(OriginTableCleanStrategy.DROP_AND_CREATE);
        assertSame(OriginTableCleanStrategy.DROP_AND_CREATE, params.getOriginTableCleanStrategy());

        params.setDelimiter(";");
        assertSame(";", params.getDelimiter());

        List<String> lockUsers = Arrays.asList("user1", "user2");
        params.setLockUsers(lockUsers);
        assertSame(lockUsers, params.getLockUsers());

        params.setSwapTableType(SwapTableType.OLD_TO_NEW);
        assertSame(SwapTableType.OLD_TO_NEW, params.getSwapTableType());
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParametersException() {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        ConnectionConfig connectionConfig = new ConnectionConfig();
        String schema = "test";

        try {
            params.generateSubTaskParameters(connectionConfig, schema);
        } catch (Exception e) {
            throw new UnexpectedException("Expected exception not thrown", e);
        }
    }

    @Test
    public void testGenerateSubTaskParameters() {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        ConnectionConfig connectionConfig = new ConnectionConfig();
        String schema = "test";

        // Test empty sqlContent
        params.setSqlContent("");
        List<OnlineSchemaChangeScheduleTaskParameters> expected = new ArrayList<>();
        List<OnlineSchemaChangeScheduleTaskParameters> actual = params.generateSubTaskParameters(connectionConfig, schema);
        assertSame(expected, actual);

        // Test invalid sqlType
        params.setSqlType(null);
        try {
            params.generateSubTaskParameters(connectionConfig, schema);
        } catch (UnexpectedException e) {
            // Expected exception
        }

        // Test valid sqlContent with one sql statement
        params.setSqlContent("create table test (id int primary key)");
        String databaseName = "db1";
        String originTableName = "table1";
        String newTableName = "table2";
        OnlineSchemaChangeSqlType sqlType = OnlineSchemaChangeSqlType.CREATE;

        List<OnlineSchemaChangeScheduleTaskParameters> expectedTaskParameters = Arrays.asList(
                new OnlineSchemaChangeScheduleTaskParameters(databaseName, originTableName, sqlType, Arrays.asList("create table test (id int primary key)"))
        );
        List<OnlineSchemaChangeScheduleTaskParameters> actualTaskParameters = params.generateSubTaskParameters(connectionConfig, schema);
        assertSame(expectedTaskParameters, actualTaskParameters);

        // Test valid sqlContent with multiple sql statements
        params.setSqlContent("create table test1 (id int primary key); create table test2 (id int primary key)");
        List<String> sqls = Arrays.asList("create table test1 (id int primary key)", "create table test2 (id int primary key)");
        List<OnlineSchemaChangeScheduleTaskParameters> expectedTaskParameters2 = Arrays.asList(
                new OnlineSchemaChangeScheduleTaskParameters(databaseName, originTableName, sqlType, Arrays.asList("create table test1 (id int primary key)", "create table test2 (id int primary key)")),
                new OnlineSchemaChangeScheduleTaskParameters(databaseName, originTableName, sqlType, Arrays.asList("create table test2 (id int primary key)"))
        );
        List<OnlineSchemaChangeScheduleTaskParameters> actualTaskParameters2 = params.generateSubTaskParameters(connectionConfig, schema);
        assertSame(expectedTaskParameters2, actualTaskParameters2);
    }

}