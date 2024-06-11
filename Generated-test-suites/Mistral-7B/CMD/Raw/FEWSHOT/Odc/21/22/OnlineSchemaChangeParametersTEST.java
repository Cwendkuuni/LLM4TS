package com.oceanbase.odc.service.onlineschemachange.model;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.core.flow.model.ConnectionConfig;
import com.oceanbase.odc.core.flow.model.TaskParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.core.shared.model.TableIdentity;
import com.oceanbase.odc.service.common.util.SqlUtils;
import com.oceanbase.odc.service.connection.model.ConnectionConfigImpl;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactoryImpl;

@SuppressWarnings("unchecked")
public class OnlineSchemaChangeParametersTest {

    private ConnectionConfig connectionConfig;
    private SubTaskParameterFactory subTaskParameterFactory;

    @Before
    public void setUp() {
        connectionConfig = new ConnectionConfigImpl();
        subTaskParameterFactory = new SubTaskParameterFactoryImpl(connectionConfig, "testSchema");
    }

    @Test
    public void testOnlineSchemaChangeParameters() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        assertNull(parameters.getSqlType());
        assertNull(parameters.getSqlContent());
        assertNull(parameters.getErrorStrategy());
        assertNull(parameters.getLockTableTimeOutSeconds());
        assertNull(parameters.getSwapTableNameRetryTimes());
        assertNull(parameters.getOriginTableCleanStrategy());
        assertNull(parameters.getDelimiter());
        assertNull(parameters.getLockUsers());
        assertNull(parameters.getSwapTableType());
    }

    @Test
    public void testSettersAndGetters() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        OnlineSchemaChangeSqlType sqlType = OnlineSchemaChangeSqlType.ALTER;
        String sqlContent = "ALTER TABLE table1 ADD column1 INT";
        TaskErrorStrategy errorStrategy = TaskErrorStrategy.CONTINUE;
        Integer lockTableTimeOutSeconds = 10;
        Integer swapTableNameRetryTimes = 3;
        OriginTableCleanStrategy originTableCleanStrategy = OriginTableCleanStrategy.DROP_AND_CREATE;
        String delimiter = ";";
        List<String> lockUsers = new ArrayList<>();
        SwapTableType swapTableType = SwapTableType.SWAP_TO_HOT;

        parameters.setSqlType(sqlType);
        assertEquals(sqlType, parameters.getSqlType());

        parameters.setSqlContent(sqlContent);
        assertEquals(sqlContent, parameters.getSqlContent());

        parameters.setErrorStrategy(errorStrategy);
        assertEquals(errorStrategy, parameters.getErrorStrategy());

        parameters.setLockTableTimeOutSeconds(lockTableTimeOutSeconds);
        assertEquals(lockTableTimeOutSeconds, parameters.getLockTableTimeOutSeconds());

        parameters.setSwapTableNameRetryTimes(swapTableNameRetryTimes);
        assertEquals(swapTableNameRetryTimes, parameters.getSwapTableNameRetryTimes());

        parameters.setOriginTableCleanStrategy(originTableCleanStrategy);
        assertEquals(originTableCleanStrategy, parameters.getOriginTableCleanStrategy());

        parameters.setDelimiter(delimiter);
        assertEquals(delimiter, parameters.getDelimiter());

        parameters.setLockUsers(lockUsers);
        assertEquals(lockUsers, parameters.getLockUsers());

        parameters.setSwapTableType(swapTableType);
        assertEquals(swapTableType, parameters.getSwapTableType());
    }

    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        assertFalse(parameters.isContinueOnError());

        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(parameters.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() throws Exception {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters.setSqlContent("ALTER TABLE table1 ADD column1 INT");
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        parameters.setLockTableTimeOutSeconds(10);
        parameters.setSwapTableNameRetryTimes(3);
        parameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.DROP_AND_CREATE);
        parameters.setDelimiter(";");
        parameters.setLockUsers(new ArrayList<>());
        parameters.setSwapTableType(SwapTableType.SWAP_TO_HOT);

        when(subTaskParameterFactory.generate("ALTER TABLE table1 ADD column1 INT", OnlineSchemaChangeSqlType.ALTER))
                .thenReturn(mock(OnlineSchemaChangeScheduleTaskParameters.class));

        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameters.generateSubTaskParameters(connectionConfig,
                "testSchema");

        assertEquals(1, subTaskParameters.size());
        OnlineSchemaChangeScheduleTaskParameters taskParameter = subTaskParameters.get(0);
        assertEquals("testSchema", taskParameter.getSchema());
        assertEquals("table1", taskParameter.getOriginTableName());
        assertTrue(taskParameter.getSqlsToBeExecuted().contains("ALTER TABLE table1 ADD column1 INT"));
        assertTrue(taskParameter.getSqlsToBeExecuted().contains(containsString("ALTER TABLE testSchema.table1")));
    }
}