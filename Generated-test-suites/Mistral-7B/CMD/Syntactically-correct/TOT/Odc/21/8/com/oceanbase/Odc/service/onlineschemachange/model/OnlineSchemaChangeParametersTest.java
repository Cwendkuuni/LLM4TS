package com.oceanbase.odc.service.onlineschemachange.model;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.core.connection.model.ConnectionConfig;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.core.shared.model.TableIdentity;
import com.oceanbase.odc.service.common.util.SqlUtils;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;

public class OnlineSchemaChangeParametersTest {

    private ConnectionConfig connectionConfig;
    private SubTaskParameterFactory subTaskParameterFactory;

    @Before
    public void setup() {
        connectionConfig = Mockito.mock(ConnectionConfig.class);
        subTaskParameterFactory = Mockito.spy(new SubTaskParameterFactory(connectionConfig, null));
    }

    @After
    public void tearDown() {
        reset(connectionConfig, subTaskParameterFactory);
    }

    @Test
    public void testInstance() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        assertNotNull(parameters);
    }

    @Test
    public void testGetIsContinueOnError() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        assertFalse(parameters.isContinueOnError());

        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(parameters.isContinueOnError());

        parameters.setErrorStrategy(null);
        assertFalse(parameters.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters_ValidParameters() throws UnexpectedException {
        String schema = "test_schema";
        String sqlContent = "CREATE TABLE test_table (id INT PRIMARY KEY); ALTER TABLE test_table ADD COLUMN test_column INT;";
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlType(OnlineSchemaChangeSqlType.CREATE_AND_ALTER);
        parameters.setSqlContent(sqlContent);
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        parameters.setLockTableTimeOutSeconds(10);
        parameters.setSwapTableType(SwapTableType.SWAP_TABLE_WITH_BACKUP);

        List<OnlineSchemaChangeScheduleTaskParameters> expectedTaskParameters = new ArrayList<>();
        OnlineSchemaChangeScheduleTaskParameters expectedTaskParameter1 = subTaskParameterFactory.generate("CREATE TABLE test_table (id INT PRIMARY KEY)",
                OnlineSchemaChangeSqlType.CREATE);
        OnlineSchemaChangeScheduleTaskParameters expectedTaskParameter2 = subTaskParameterFactory.generate("ALTER TABLE test_table ADD COLUMN test_column INT",
                OnlineSchemaChangeSqlType.ALTER);
        TableIdentity key1 = new TableIdentity("test_schema", "test_table");
        TableIdentity key2 = new TableIdentity("test_schema", "test_table");
        expectedTaskParameter1.setNewTableName("test_table_tmp");
        expectedTaskParameter2.setNewTableName("test_table_new");
        expectedTaskParameters.add(expectedTaskParameter1);
        expectedTaskParameters.add(expectedTaskParameter2);

        List<OnlineSchemaChangeScheduleTaskParameters> actualTaskParameters = parameters.generateSubTaskParameters(connectionConfig, schema);
        assertEquals(expectedTaskParameters, actualTaskParameters);
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParameters_InvalidSqlContent() throws UnexpectedException {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlType(OnlineSchemaChangeSqlType.CREATE_AND_ALTER);
        parameters.setSqlContent("CREATE TABLE test_table; ALTER TABLE test_table ADD COLUMN test_column INT ;");
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        parameters.setLockTableTimeOutSeconds(10);
        parameters.setSwapTableType(SwapTableType.SWAP_TABLE_WITH_BACKUP);

        parameters.generateSubTaskParameters(connectionConfig, "test_schema");
    }
}