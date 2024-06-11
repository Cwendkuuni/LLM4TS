import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.core.connection.model.ConnectionConfig;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.core.shared.model.TableIdentity;
import com.oceanbase.odc.service.common.util.SqlUtils;
import com.oceanbase.odc.service.connection.model.ConnectionConfigFactory;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.onlineschemachange.model.OriginTableCleanStrategy;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;

class OnlineSchemaChangeParametersTest {

    private ConnectionConfig connectionConfig;
    private SubTaskParameterFactory subTaskParameterFactory;

    @BeforeEach
    public void setup() {
        connectionConfig = ConnectionConfigFactory.create();
        subTaskParameterFactory = Mockito.spy(new SubTaskParameterFactory(connectionConfig, "schema"));
    }

    @Test
    public void testInstance() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        assertNotNull(parameters);
    }

    @Test
    public void testGetSqlType() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        assertEquals(OnlineSchemaChangeSqlType.CREATE, parameters.getSqlType());

        OnlineSchemaChangeParameters parametersWithValue = new OnlineSchemaChangeParameters();
        parametersWithValue.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        assertEquals(OnlineSchemaChangeSqlType.ALTER, parametersWithValue.getSqlType());
    }

    @Test
    public void testGetSqlContent() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        assertNull(parameters.getSqlContent());

        OnlineSchemaChangeParameters parametersWithValue = new OnlineSchemaChangeParameters();
        parametersWithValue.setSqlContent("CREATE TABLE test (id INT PRIMARY KEY)");
        assertEquals("CREATE TABLE test (id INT PRIMARY KEY)", parametersWithValue.getSqlContent());
    }

    @Test
    public void testGetErrorStrategy() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        assertEquals(TaskErrorStrategy.ABORT, parameters.getErrorStrategy());

        OnlineSchemaChangeParameters parametersWithValue = new OnlineSchemaChangeParameters();
        parametersWithValue.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertEquals(TaskErrorStrategy.CONTINUE, parametersWithValue.getErrorStrategy());
    }

    @Test
    public void testGetLockTableTimeOutSeconds() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        assertEquals(0, parameters.getLockTableTimeOutSeconds().intValue());

        OnlineSchemaChangeParameters parametersWithValue = new OnlineSchemaChangeParameters();
        parametersWithValue.setLockTableTimeOutSeconds(10);
        assertEquals(10, parametersWithValue.getLockTableTimeOutSeconds().intValue());
    }

    @Test
    public void testGetSwapTableNameRetryTimes() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        assertEquals(0, parameters.getSwapTableNameRetryTimes().intValue());

        OnlineSchemaChangeParameters parametersWithValue = new OnlineSchemaChangeParameters();
        parametersWithValue.setSwapTableNameRetryTimes(10);
        assertEquals(10, parametersWithValue.getSwapTableNameRetryTimes().intValue());
    }

    @Test
    public void testGetOriginTableCleanStrategy() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        assertEquals(OnlineSchemaChangeParameters.OriginTableCleanStrategy.DROP_AND_CREATE, parameters.getOriginTableCleanStrategy());

        OnlineSchemaChangeParameters parametersWithValue = new OnlineSchemaChangeParameters();
        parametersWithValue.setOriginTableCleanStrategy(OnlineSchemaChangeParameters.OriginTableCleanStrategy.TRUNCATE);
        assertEquals(OnlineSchemaChangeParameters.OriginTableCleanStrategy.TRUNCATE, parametersWithValue.getOriginTableCleanStrategy());
    }

    @Test
    public void testGetDelimiter() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        assertEquals(";", parameters.getDelimiter());

        OnlineSchemaChangeParameters parametersWithValue = new OnlineSchemaChangeParameters();
        parametersWithValue.setDelimiter(":");
        assertEquals(":", parametersWithValue.getDelimiter());
    }

    @Test
    public void testGetLockUsers() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        assertNull(parameters.getLockUsers());

        OnlineSchemaChangeParameters parametersWithValue = new OnlineSchemaChangeParameters();
        parametersWithValue.setLockUsers(new ArrayList<>());
        assertEquals(0, parametersWithValue.getLockUsers().size());

        OnlineSchemaChangeParameters parametersWithUsers = new OnlineSchemaChangeParameters();
        parametersWithUsers.setLockUsers(new ArrayList<>());
        parametersWithUsers.getLockUsers().add("user1");
        parametersWithUsers.getLockUsers().add("user2");
        assertEquals(2, parametersWithUsers.getLockUsers().size());
    }

    @Test
    public void testGetSwapTableType() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        assertEquals(OnlineSchemaChangeParameters.SwapTableType.SWAP_IN_PLACE, parameters.getSwapTableType());

        OnlineSchemaChangeParameters parametersWithValue = new OnlineSchemaChangeParameters();
        parametersWithValue.setSwapTableType(OnlineSchemaChangeParameters.SwapTableType.SWAP_TO_NEW_TABLE);
        assertEquals(OnlineSchemaChangeParameters.SwapTableType.SWAP_TO_NEW_TABLE, parametersWithValue.getSwapTableType());
    }

    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        assertFalse(parameters.isContinueOnError());

        OnlineSchemaChangeParameters parametersWithValue = new OnlineSchemaChangeParameters();
        parametersWithValue.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(parametersWithValue.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() throws Exception {
        when(subTaskParameterFactory.generate(Mockito.anyString(), Mockito.eq(OnlineSchemaChangeSqlType.CREATE)))
                .thenReturn(Mockito.mock(OnlineSchemaChangeScheduleTaskParameters.class));

        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlContent("CREATE TABLE test (id INT PRIMARY KEY)");
        List<OnlineSchemaChangeScheduleTaskParameters> result = parameters.generateSubTaskParameters(connectionConfig, "schema");
        assertEquals(1, result.size());

        verify(subTaskParameterFactory, times(1)).generate(Mockito.eq("CREATE TABLE test (id INT PRIMARY KEY)"),
                Mockito.eq(OnlineSchemaChangeSqlType.CREATE));
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParametersWithException() throws Exception {
        when(subTaskParameterFactory.generate(Mockito.anyString(), Mockito.eq(OnlineSchemaChangeSqlType.CREATE)))
                .thenThrow(new UnexpectedException("Test exception"));

        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlContent("CREATE TABLE test (id INT PRIMARY KEY)");
        parameters.generateSubTaskParameters(connectionConfig, "schema");

        fail("UnexpectedException should be thrown but not caught");
    }
}