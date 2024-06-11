package com.oceanbase.odc.service.onlineschemachange.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.core.connection.model.ConnectionConfig;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.model.TableIdentity;
import com.oceanbase.odc.core.shared.util.SqlUtils;
import com.oceanbase.odc.service.common.util.DdlUtils;
import com.oceanbase.odc.service.common.util.SubTaskParameterFactory;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtilsMock;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactoryMock;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeSqlType;

public class OnlineSchemaChangeParametersTest {

    private ConnectionConfig connectionConfig;
    private OnLineSchemaChangeParameters parameters;
    private DdlUtils ddlUtils;
    private SubTaskParameterFactory subTaskParameterFactory;

    @Before
    public void setUp() {
        connectionConfig = mock(ConnectionConfig.class);
        ddlUtils = new DdlUtilsMock();
        subTaskParameterFactory = new SubTaskParameterFactoryMock();
        when(connectionConfig.getDialectType()).thenReturn("dialectType");
        when(connectionConfig.getDialectType()).thenReturn("dialectType");
        ddlUtils.replaceTableName = (sql, newTableName, dialectType, sqlType) -> "newSql";
        subTaskParameterFactory.generate = (sql, sqlType) -> new OnlineSchemaChangeScheduleTaskParameters();

        parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters.setSqlContent("content;sql2;sql3");
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        parameters.setLockTableTimeOutSeconds(10);
        parameters.setSwapTableType(SwapTableType.SWAP_PRIMARY);
        parameters.setLockUsers(Arrays.asList("user1", "user2"));
    }

    @Test
    public void testProperties() {
        assertEquals(OnlineSchemaChangeSqlType.ALTER, parameters.getSqlType());
        assertNull(parameters.getSqlContent());
        assertFalse(parameters.isContinueOnError());
        assertNull(parameters.getLockTableTimeOutSeconds());
        assertSame(SwapTableType.SWAP_PRIMARY, parameters.getSwapTableType());
        assertSame(Arrays.asList("user1", "user2"), parameters.getLockUsers());
    }

    @Test
    public void testGenerateSubTaskParameters() {
        List<OnlineSchemaChangeScheduleTaskParameters> expected = Arrays.asList(new OnlineSchemaChangeScheduleTaskParameters(),
                new OnlineSchemaChangeScheduleTaskParameters(), new OnlineSchemaChangeScheduleTaskParameters());

        List<OnlineSchemaChangeScheduleTaskParameters> actual = parameters.generateSubTaskParameters(connectionConfig, "schema");

        assertEquals(expected, actual);
    }
}

class DdlUtilsMock {
    public String replaceTableName = (sql, newTableName, dialectType, sqlType) -> "newSql";
}

class SubTaskParameterFactoryMock {
    public OnlineSchemaChangeScheduleTaskParameters generate = (sql, sqlType) -> new OnlineSchemaChangeScheduleTaskParameters();
}

class OnlineSchemaChangeScheduleTaskParameters {
}