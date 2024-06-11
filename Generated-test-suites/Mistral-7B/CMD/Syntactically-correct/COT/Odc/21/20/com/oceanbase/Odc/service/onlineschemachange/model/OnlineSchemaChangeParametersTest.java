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
import com.oceanbase.odc.service.connection.model.MockConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;

public class OnlineSchemaChangeParametersTest {

    private ConnectionConfig connectionConfig;
    private SubTaskParameterFactory subTaskParameterFactory;

    @Before
    public void setUp() {
        connectionConfig = new MockConnectionConfig();
        subTaskParameterFactory = mock(SubTaskParameterFactory.class);
    }

    @After
    public void tearDown() {
        reset(subTaskParameterFactory);
    }

    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(parameters.isContinueOnError());

        parameters.setErrorStrategy(TaskErrorStrategy.FAIL);
        assertFalse(parameters.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() {
        when(subTaskParameterFactory.generate(anyString(), any(OnlineSchemaChangeSqlType.class)))
                .thenReturn(new OnlineSchemaChangeScheduleTaskParameters());

        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlType(OnlineSchemaChangeSqlType.CREATE);
        parameters.setSqlContent("CREATE TABLE test (id INT PRIMARY KEY);");

        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameters.generateSubTaskParameters(connectionConfig,
                "test_db");

        assertEquals(1, subTaskParameters.size());
        TableIdentity key = new TableIdentity("test_db", "test");

        OnlineSchemaChangeScheduleTaskParameters taskParameter = subTaskParameters.get(0);
        assertEquals(key.getDatabaseName(), taskParameter.getDatabaseName());
        assertEquals(key.getTableName(), taskParameter.getOriginTableName());

        verify(subTaskParameterFactory, times(1)).generate(anyString(), any(OnlineSchemaChangeSqlType.class));
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParametersWithException() {
        when(subTaskParameterFactory.generate(anyString(), any(OnlineSchemaChangeSqlType.class)))
                .thenThrow(new RuntimeException("Test Exception"));

        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlType(OnlineSchemaChangeSqlType.CREATE);
        parameters.setSqlContent("CREATE TABLE test (id INT PRIMARY KEY);");

        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameters.generateSubTaskParameters(connectionConfig,
                "test_db");
    }

    @Test
    public void testDelimiter() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setDelimiter(";");
        assertEquals(";", parameters.getDelimiter());

        parameters.setDelimiter(",");
        assertEquals(",", parameters.getDelimiter());
    }

    @Test
    public void testLockUsers() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        List<String> users = new ArrayList<>();
        users.add("user1");
        users.add("user2");
        parameters.setLockUsers(users);
        assertEquals(users, parameters.getLockUsers());
    }

    @Test
    public void testSwapTableType() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSwapTableType(SwapTableType.OLD_TO_NEW);
        assertEquals(SwapTableType.OLD_TO_NEW, parameters.getSwapTableType());

        parameters.setSwapTableType(SwapTableType.NEW_TO_OLD);
        assertEquals(SwapTableType.NEW_TO_OLD, parameters.getSwapTableType());
    }

    @Test
    public void testOriginTableCleanStrategy() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setOriginTableCleanStrategy(OnlineSchemaChangeParameters.OriginTableCleanStrategy.DROP_AND_CREATE);
        assertEquals(OnlineSchemaChangeParameters.OriginTableCleanStrategy.DROP_AND_CREATE, parameters.getOriginTableCleanStrategy());

        parameters.setOriginTableCleanStrategy(OnlineSchemaChangeParameters.OriginTableCleanStrategy.TRUNCATE);
        assertEquals(OnlineSchemaChangeParameters.OriginTableCleanStrategy.TRUNCATE, parameters.getOriginTableCleanStrategy());
    }

    @Test
    public void testLockTableTimeOutSeconds() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setLockTableTimeOutSeconds(10);
        assertEquals(10, parameters.getLockTableTimeOutSeconds());
    }

    @Test
    public void testSwapTableNameRetryTimes() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSwapTableNameRetryTimes(5);
        assertEquals(5, parameters.getSwapTableNameRetryTimes());
    }

    @Test
    public void testSqlType() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        assertEquals(OnlineSchemaChangeSqlType.ALTER, parameters.getSqlType());

        parameters.setSqlType(OnlineSchemaChangeSqlType.CREATE);
        assertEquals(OnlineSchemaChangeSqlType.CREATE, parameters.getSqlType());
    }

    @Test
    public void testErrorStrategy() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setErrorStrategy(TaskErrorStrategy.FAIL);
        assertEquals(TaskErrorStrategy.FAIL, parameters.getErrorStrategy());

        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertEquals(TaskErrorStrategy.CONTINUE, parameters.getErrorStrategy());
    }
}