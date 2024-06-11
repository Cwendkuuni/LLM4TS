package com.oceanbase.odc.service.onlineschemachange.model;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.oceanbase.odc.core.connection.model.ConnectionConfig;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.model.TableIdentity;
import com.oceanbase.odc.service.common.util.SqlUtils;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class OnlineSchemaChangeParametersTest {

    private ConnectionConfig connectionConfig;
    private SubTaskParameterFactory subTaskParameterFactory;

    @Before
    public void initMocks() {
        connectionConfig = mock(ConnectionConfig.class);
        subTaskParameterFactory = mock(SubTaskParameterFactory.class);
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testNewOnlineSchemaChangeParameters() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        assertEquals(OnlineSchemaChangeSqlType.UNDEFINED, parameters.getSqlType());
        assertNull(parameters.getSqlContent());
        assertEquals(TaskErrorStrategy.IGNORE, parameters.getErrorStrategy());
        assertEquals(0, parameters.getLockTableTimeOutSeconds());
        assertEquals(0, parameters.getSwapTableNameRetryTimes());
        assertEquals(OriginTableCleanStrategy.DROP_AND_CREATE, parameters.getOriginTableCleanStrategy());
        assertEquals(";", parameters.getDelimiter());
        assertNull(parameters.getLockUsers());
        assertEquals(SwapTableType.SWAP_TABLE, parameters.getSwapTableType());
    }

    @Test
    public void testNewOnlineSchemaChangeParametersWithValues() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters.setSqlContent("ALTER TABLE mytable ADD COLUMN new_column INT");
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        parameters.setLockTableTimeOutSeconds(10);
        parameters.setSwapTableNameRetryTimes(3);
        parameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.TRUNCATE);
        parameters.setDelimiter(":");
        parameters.setLockUsers(new ArrayList<>());
        parameters.setSwapTableType(SwapTableType.SWAP_IN_PLACE);

        assertEquals(OnlineSchemaChangeSqlType.ALTER, parameters.getSqlType());
        assertEquals("ALTER TABLE mytable ADD COLUMN new_column INT", parameters.getSqlContent());
        assertEquals(TaskErrorStrategy.CONTINUE, parameters.getErrorStrategy());
        assertEquals(10, parameters.getLockTableTimeOutSeconds());
        assertEquals(3, parameters.getSwapTableNameRetryTimes());
        assertEquals(OriginTableCleanStrategy.TRUNCATE, parameters.getOriginTableCleanStrategy());
        assertEquals(":", parameters.getDelimiter());
        assertNotNull(parameters.getLockUsers());
        assertEquals(SwapTableType.SWAP_IN_PLACE, parameters.getSwapTableType());
    }

    @Test
    public void testGenerateSubTaskParameters() {
        when(subTaskParameterFactory.generate(anyString(), any(OnlineSchemaChangeSqlType.class))).thenReturn(mock(OnlineSchemaChangeScheduleTaskParameters.class));

        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters.setSqlContent("ALTER TABLE mytable ADD COLUMN new_column INT");
        parameters.setDelimiter(":");
        parameters.setLockUsers(new ArrayList<>());

        List<OnlineSchemaChangeScheduleTaskParameters> result = parameters.generateSubTaskParameters(connectionConfig, "test_schema");

        assertEquals(1, result.size());
        verify(subTaskParameterFactory, times(1)).generate(eq("ALTER TABLE mytable ADD COLUMN new_column INT"), any(OnlineSchemaChangeSqlType.class));
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParameters_Exception() {
        when(subTaskParameterFactory.generate(anyString(), any(OnlineSchemaChangeSqlType.class))).thenThrow(new RuntimeException());

        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters.setSqlContent("ALTER TABLE mytable ADD COLUMN new_column INT");
        parameters.setDelimiter(":");
        parameters.setLockUsers(new ArrayList<>());

        parameters.generateSubTaskParameters(connectionConfig, "test_schema");
    }
}