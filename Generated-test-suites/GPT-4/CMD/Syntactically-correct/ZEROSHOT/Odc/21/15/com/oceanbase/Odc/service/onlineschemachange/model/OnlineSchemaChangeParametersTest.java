package com.oceanbase.odc.service.onlineschemachange.model;

import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.model.*;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.core.shared.model.TableIdentity;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OnlineSchemaChangeParametersTest {

    private OnlineSchemaChangeParameters parameters;

    @Before
    public void setUp() {
        parameters = new OnlineSchemaChangeParameters();
    }

    @Test
    public void testIsContinueOnError() {
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(parameters.isContinueOnError());

        parameters.setErrorStrategy(TaskErrorStrategy.STOP);
        assertFalse(parameters.isContinueOnError());
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParameters() {
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        when(connectionConfig.getDialectType()).thenReturn("SQL");
        parameters.setSqlContent("TEST_SQL_CONTENT");
        parameters.setDelimiter(";");
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);

        // This call will throw an UnexpectedException because we are using mock objects
        parameters.generateSubTaskParameters(connectionConfig, "test_schema");
    }

    @Test
    public void testSettersAndGetters() {
        // Just for code coverage
        parameters.setLockTableTimeOutSeconds(10);
        parameters.setSwapTableNameRetryTimes(5);
        parameters.setDelimiter(",");
        parameters.setSqlContent("Test SQL");
        parameters.setSqlType(OnlineSchemaChangeSqlType.ADD);
        parameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.RENAME);
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        parameters.setLockUsers(new ArrayList<>());
        parameters.setSwapTableType(SwapTableType.BEFORE_TABLE);

        assertTrue(parameters.getLockTableTimeOutSeconds() == 10);
        assertTrue(parameters.getSwapTableNameRetryTimes() == 5);
        assertTrue(parameters.getDelimiter().equals(","));
        assertTrue(parameters.getSqlContent().equals("Test SQL"));
        assertTrue(parameters.getSqlType() == OnlineSchemaChangeSqlType.ADD);
        assertTrue(parameters.getOriginTableCleanStrategy() == OriginTableCleanStrategy.RENAME);
        assertTrue(parameters.getErrorStrategy() == TaskErrorStrategy.CONTINUE);
        assertTrue(parameters.getLockUsers() instanceof List);
        assertTrue(parameters.getSwapTableType() == SwapTableType.BEFORE_TABLE);
    }
}
