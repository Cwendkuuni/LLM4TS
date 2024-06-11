import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.oceanbase.odc.core.connection.Configuration;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.service.common.util.SqlUtils;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.model.TableIdentity;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

/**
 * Test class for OnlineSchemaChangeParameters
 *
 * @author yaobin
 * @since 4.2.0
 */
public class OnlineSchemaChangeParametersTest {

    private ConnectionConfig connectionConfig;
    private Configuration config;
    private SubTaskParameterFactory subTaskParameterFactory;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.config = mock(Configuration.class);
        this.connectionConfig = new ConnectionConfig(this.config);
        this.subTaskParameterFactory = mock(SubTaskParameterFactory.class);
    }

    @Test
    public void testGetInstance() {
        OnlineSchemaChangeParameters params = OnlineSchemaChangeParameters.getInstance();
        assertNull(params.getSqlType());
        assertNull(params.getSqlContent());
        assertNull(params.getErrorStrategy());
        assertNull(params.getLockTableTimeOutSeconds());
        assertNull(params.getSwapTableNameRetryTimes());
        assertNull(params.getOriginTableCleanStrategy());
        assertNull(params.getDelimiter());
        assertNull(params.getLockUsers());
        assertNull(params.getSwapTableType());
    }

    @Test
    public void testNewOnlineSchemaChangeParameters() {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        OnlineSchemaChangeSqlType sqlType = OnlineSchemaChangeSqlType.ALTER;
        String sqlContent = "test sql content";
        TaskErrorStrategy errorStrategy = TaskErrorStrategy.CONTINUE;
        Integer lockTableTimeOutSeconds = 10;
        Integer swapTableNameRetryTimes = 3;
        OriginTableCleanStrategy originTableCleanStrategy = OriginTableCleanStrategy.DROP;
        String delimiter = ";";
        List<String> lockUsers = Arrays.asList("user1", "user2");
        SwapTableType swapTableType = SwapTableType.SWAP_TO_TEMP;

        params.setSqlType(sqlType);
        params.setSqlContent(sqlContent);
        params.setErrorStrategy(errorStrategy);
        params.setLockTableTimeOutSeconds(lockTableTimeOutSeconds);
        params.setSwapTableNameRetryTimes(swapTableNameRetryTimes);
        params.setOriginTableCleanStrategy(originTableCleanStrategy);
        params.setDelimiter(delimiter);
        params.setLockUsers(lockUsers);
        params.setSwapTableType(swapTableType);

        assertEquals(sqlType, params.getSqlType());
        assertEquals(sqlContent, params.getSqlContent());
        assertEquals(errorStrategy, params.getErrorStrategy());
        assertEquals(lockTableTimeOutSeconds, params.getLockTableTimeOutSeconds());
        assertEquals(swapTableNameRetryTimes, params.getSwapTableNameRetryTimes());
        assertEquals(originTableCleanStrategy, params.getOriginTableCleanStrategy());
        assertEquals(delimiter, params.getDelimiter());
        assertEquals(lockUsers, params.getLockUsers());
        assertEquals(swapTableType, params.getSwapTableType());
    }

    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        params.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(params.isContinueOnError());

        params.setErrorStrategy(TaskErrorStrategy.ABORT);
        assertFalse(params.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() throws Exception {
        String databaseName = "testDb";
        String schema = "testSchema";
        OnlineSchemaChangeSqlType sqlType = OnlineSchemaChangeSqlType.ALTER;
        String sqlContent = "alter table testTable add column newColumn int;";
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        params.setSqlType(sqlType);
        params.setSqlContent(sqlContent);

        List<OnlineSchemaChangeScheduleTaskParameters> expectedTaskParameters = Arrays.asList(
                new OnlineSchemaChangeScheduleTaskParameters(databaseName, "testTable", Arrays.asList("alter table testTable add column newColumn int;"), sqlType)
        );

        when(subTaskParameterFactory.generate(eq("alter table testTable add column newColumn int;"), eq(sqlType)))
                .thenReturn(new OnlineSchemaChangeScheduleTaskParameters(databaseName, "testTable", Arrays.asList("alter table testTable add column newColumn int;"), sqlType));

        List<OnlineSchemaChangeScheduleTaskParameters> actualTaskParameters = params.generateSubTaskParameters(connectionConfig, schema);
        assertEquals(expectedTaskParameters, actualTaskParameters);
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParametersWithException() throws Exception {
        String databaseName = "testDb";
        String schema = "testSchema";
        OnlineSchemaChangeSqlType sqlType = OnlineSchemaChangeSqlType.ALTER;
        String sqlContent = "alter table testTable add column newColumn int;";
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        params.setSqlType(sqlType);
        params.setSqlContent(sqlContent);

        when(subTaskParameterFactory.generate(anyString(), eq(sqlType)))
                .thenThrow(new Exception("test exception"));

        params.generateSubTaskParameters(connectionConfig, schema);
    }

}