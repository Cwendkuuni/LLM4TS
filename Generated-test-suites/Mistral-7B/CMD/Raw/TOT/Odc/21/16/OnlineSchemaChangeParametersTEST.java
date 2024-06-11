package com.oceanbase.odc.service.onlineschemachange.model;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.oceanbase.odc.core.flow.model.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.service.common.util.SqlUtils;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;

import org.junit.Test;

public class OnlineSchemaChangeParametersTest {

    @Test
    public void testValidOnlineSchemaChangeParameters() {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        params.setSqlType(OnlineSchemaChangeSqlType.CREATE);
        params.setSqlContent("CREATE TABLE test (id INT PRIMARY KEY, name VARCHAR(50));");
        params.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        params.setLockTableTimeOutSeconds(30);
        params.setSwapTableType(SwapTableType.ONLINE);
        List<String> lockUsers = new ArrayList<>();
        lockUsers.add("user1");
        params.setLockUsers(lockUsers);

        ConnectionConfig connectionConfig = new ConnectionConfig("testDb", "localhost", "user", "password");
        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = params.generateSubTaskParameters(connectionConfig, "testSchema");
        assertEquals(1, subTaskParameters.size());
        OnlineSchemaChangeScheduleTaskParameters taskParameter = subTaskParameters.get(0);
        assertEquals("testSchema", taskParameter.getDatabaseName());
        assertEquals("test", taskParameter.getOriginTableName());
        assertEquals(OnlineSchemaChangeSqlType.CREATE, taskParameter.getSqlType());
        assertEquals("CREATE TABLE test (id INT PRIMARY KEY, name VARCHAR(50));", taskParameter.getSqlContent());
        assertEquals("testSchema", taskParameter.getNewDatabaseName());
        assertEquals("test_swap", taskParameter.getNewTableName());
        assertEquals(30, taskParameter.getLockTableTimeOutSeconds());
        assertEquals(1, taskParameter.getSqlsToBeExecuted().size());
        assertEquals("CREATE TABLE test (id INT PRIMARY KEY, name VARCHAR(50));", taskParameter.getSqlsToBeExecuted().get(0));
    }

    @Test
    public void testEmptySqlContent() {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        params.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        params.setSqlContent("");
        try {
            params.generateSubTaskParameters(new ConnectionConfig("testDb", "localhost", "user", "password"), "testSchema");
            fail("Expected UnexpectedException");
        } catch (UnexpectedException e) {
            // Expected
        }
    }

    @Test
    public void testSplitSqlContentWithDelimiter() {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        params.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        params.setSqlContent("CREATE TABLE test1 (id INT PRIMARY KEY, name VARCHAR(50));CREATE TABLE test2 (id INT PRIMARY KEY, name VARCHAR(50));");
        params.setDelimiter(";");
        ConnectionConfig connectionConfig = new ConnectionConfig("testDb", "localhost", "user", "password");
        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = params.generateSubTaskParameters(connectionConfig, "testSchema");
        assertEquals(2, subTaskParameters.size());
    }

    @Test
    public void testSplitSqlContentWithoutDelimiter() {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        params.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        params.setSqlContent("CREATE TABLE test1 (id INT PRIMARY KEY, name VARCHAR(50))CREATE TABLE test2 (id INT PRIMARY KEY, name VARCHAR(50));");
        params.setDelimiter(" ");
        ConnectionConfig connectionConfig = new ConnectionConfig("testDb", "localhost", "user", "password");
        try {
            params.generateSubTaskParameters(connectionConfig, "testSchema");
            fail("Expected UnexpectedException");
        } catch (UnexpectedException e) {
            // Expected
        }
    }
}