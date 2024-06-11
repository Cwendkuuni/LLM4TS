import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;
import java.util.concurrent.TimeUnit;

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
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory.SubTaskParameterType;

/**
 * Test class for OnlineSchemaChangeParameters
 *
 * @author yaobin
 * @since 4.2.0
 */
public class OnlineSchemaChangeParametersTest {

    private ConnectionConfig connectionConfig;
    private SubTaskParameterFactory subTaskParameterFactory;

    @Before
    public void setup() {
        connectionConfig = Mockito.mock(ConnectionConfig.class);
        subTaskParameterFactory = Mockito.spy(new SubTaskParameterFactory(connectionConfig, "testSchema"));
    }

    @After
    public void cleanup() {
        reset(subTaskParameterFactory);
    }

    @Test
    public void testNewOnlineSchemaChangeParameters() {
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
    public void testNewOnlineSchemaChangeParametersWithValues() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlType(OnlineSchemaChangeSqlType.CREATE);
        parameters.setSqlContent("sqlContent");
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        parameters.setLockTableTimeOutSeconds(30);
        parameters.setSwapTableNameRetryTimes(5);
        parameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.DROP_AND_RECREATE);
        parameters.setDelimiter(";");
        parameters.setLockUsers(Arrays.asList("user1", "user2"));
        parameters.setSwapTableType(SwapTableType.DATA_NO_INDEX);

        assertEquals(OnlineSchemaChangeSqlType.CREATE, parameters.getSqlType());
        assertEquals("sqlContent", parameters.getSqlContent());
        assertEquals(TaskErrorStrategy.CONTINUE, parameters.getErrorStrategy());
        assertEquals(30, parameters.getLockTableTimeOutSeconds());
        assertEquals(5, parameters.getSwapTableNameRetryTimes());
        assertEquals(OriginTableCleanStrategy.DROP_AND_RECREATE, parameters.getOriginTableCleanStrategy());
        assertEquals(";", parameters.getDelimiter());
        assertEquals(Arrays.asList("user1", "user2"), parameters.getLockUsers());
        assertEquals(SwapTableType.DATA_NO_INDEX, parameters.getSwapTableType());
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParametersThrowsException() {
        doThrow(new UnexpectedException("Test exception")).when(subTaskParameterFactory).generate(anyString(),
                any(OnlineSchemaChangeSqlType.class));

        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        List<OnlineSchemaChangeScheduleTaskParameters> result = parameters.generateSubTaskParameters(connectionConfig,
                "testSchema");
        fail("Exception should be thrown");
    }

    @Test
    public void testGenerateSubTaskParameters() {
        when(subTaskParameterFactory.generate(anyString(), any(OnlineSchemaChangeSqlType.class)))
                .thenReturn(Mockito.mock(OnlineSchemaChangeScheduleTaskParameters.class));

        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        String sqlContent = "sqlContent";
        OnlineSchemaChangeSqlType sqlType = OnlineSchemaChangeSqlType.CREATE;
        parameters.setSqlType(sqlType);
        parameters.setSqlContent(sqlContent);

        List<OnlineSchemaChangeScheduleTaskParameters> result = parameters.generateSubTaskParameters(connectionConfig,
                "testSchema");
        assertEquals(1, result.size());
        verify(subTaskParameterFactory, times(1)).generate(eq(sqlContent), eq(sqlType));
    }

}