import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.oceanbase.odc.core.connection.model.ConnectionConfig;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.model.TableIdentity;
import com.oceanbase.odc.service.common.util.SqlUtils;
import com.oceanbase.odc.service.connection.model.ConnectionConfigFactory;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactoryMock;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class OnlineSchemaChangeParametersTest {

    private ConnectionConfig connectionConfig;
    private SubTaskParameterFactory subTaskParameterFactory;

    @Before
    public void setUp() {
        connectionConfig = ConnectionConfigFactory.newInstance();
        subTaskParameterFactory = new SubTaskParameterFactoryMock();
    }

    @After
    public void tearDown() {
        reset(subTaskParameterFactory);
    }

    @Test
    public void testInstance() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        assertNotNull(parameters);
    }

    @Test
    public void testGetSqlType() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        assertEquals(OnlineSchemaChangeSqlType.ALTER, parameters.getSqlType());
    }

    @Test
    public void testSetAndGetSqlContent() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        String sqlContent = "CREATE TABLE test_table (id INT PRIMARY KEY)";
        parameters.setSqlContent(sqlContent);
        assertEquals(sqlContent, parameters.getSqlContent());
    }

    @Test
    public void testGetErrorStrategy() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertEquals(TaskErrorStrategy.CONTINUE, parameters.getErrorStrategy());
    }

    @Test
    public void testSetAndGetLockTableTimeOutSeconds() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        int lockTableTimeOutSeconds = 10;
        parameters.setLockTableTimeOutSeconds(lockTableTimeOutSeconds);
        assertEquals(lockTableTimeOutSeconds, parameters.getLockTableTimeOutSeconds());
    }

    @Test
    public void testSetAndGetSwapTableNameRetryTimes() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        int swapTableNameRetryTimes = 3;
        parameters.setSwapTableNameRetryTimes(swapTableNameRetryTimes);
        assertEquals(swapTableNameRetryTimes, parameters.getSwapTableNameRetryTimes());
    }

    @Test
    public void testSetAndGetOriginTableCleanStrategy() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.DROP_AND_CREATE);
        assertEquals(OriginTableCleanStrategy.DROP_AND_CREATE, parameters.getOriginTableCleanStrategy());
    }

    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(parameters.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() {
        String schema = "test_schema";
        String sqlContent = "CREATE TABLE test_table (id INT PRIMARY KEY)";
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlType(OnlineSchemaChangeSqlType.CREATE);
        parameters.setSqlContent(sqlContent);
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);

        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameters.generateSubTaskParameters(connectionConfig, schema);
        assertEquals(1, subTaskParameters.size());

        OnlineSchemaChangeScheduleTaskParameters taskParameter = subTaskParameters.get(0);
        TableIdentity tableIdentity = new TableIdentity("test_db", "test_table");
        assertEquals(tableIdentity.getDatabaseName(), taskParameter.getDatabaseName());
        assertEquals(tableIdentity.getOriginTableName(), taskParameter.getOriginTableName());
        assertEquals(OnlineSchemaChangeSqlType.CREATE, taskParameter.getSqlType());

        List<String> sqlsToBeExecuted = taskParameter.getSqlsToBeExecuted();
        assertEquals(1, sqlsToBeExecuted.size());
        assertEquals(sqlContent, sqlsToBeExecuted.get(0));
    }
}

class SubTaskParameterFactoryMock extends SubTaskParameterFactory {

    @Override
    public OnlineSchemaChangeScheduleTaskParameters generate(String sql, OnlineSchemaChangeSqlType sqlType) {
        OnlineSchemaChangeScheduleTaskParameters taskParameter = mock(OnlineSchemaChangeScheduleTaskParameters.class);
        when(taskParameter.getDatabaseName()).thenReturn("test_db");
        when(taskParameter.getOriginTableName()).thenReturn("test_table");
        when(taskParameter.getSqlType()).thenReturn(sqlType);
        when(taskParameter.getNewTableName()).thenReturn("new_test_table");
        when(taskParameter.getSqlsToBeExecuted()).thenReturn(new ArrayList<>());
        return taskParameter;
    }
}