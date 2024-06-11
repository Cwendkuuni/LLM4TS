import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.core.connection.model.ConnectionConfig;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.model.TableIdentity;
import com.oceanbase.odc.service.common.util.SqlUtils;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;

public class OnlineSchemaChangeParametersTest {

    private ConnectionConfig connectionConfig;
    private SubTaskParameterFactory subTaskParameterFactory;

    @Before
    public void setup() {
        connectionConfig = mock(ConnectionConfig.class);
        subTaskParameterFactory = mock(SubTaskParameterFactory.class);
    }

    @Test
    public void testConstructor() {
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
    public void testGettersAndSetters() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();

        OnlineSchemaChangeSqlType sqlType = OnlineSchemaChangeSqlType.ALTER;
        String sqlContent = "ALTER TABLE TEST TABLE ADD COLUMN COLUMN_NAME INT";
        TaskErrorStrategy errorStrategy = TaskErrorStrategy.CONTINUE;
        Integer lockTableTimeOutSeconds = 30;
        Integer swapTableNameRetryTimes = 3;
        OriginTableCleanStrategy originTableCleanStrategy = OriginTableCleanStrategy.TRUNCATE;
        String delimiter = "|";
        List<String> lockUsers = Arrays.asList("user1", "user2");
        SwapTableType swapTableType = SwapTableType.SWAP_TABLE_TO_TEMP_TABLE;

        parameters.setSqlType(sqlType);
        parameters.setSqlContent(sqlContent);
        parameters.setErrorStrategy(errorStrategy);
        parameters.setLockTableTimeOutSeconds(lockTableTimeOutSeconds);
        parameters.setSwapTableNameRetryTimes(swapTableNameRetryTimes);
        parameters.setOriginTableCleanStrategy(originTableCleanStrategy);
        parameters.setDelimiter(delimiter);
        parameters.setLockUsers(lockUsers);
        parameters.setSwapTableType(swapTableType);

        assertEquals(sqlType, parameters.getSqlType());
        assertEquals(sqlContent, parameters.getSqlContent());
        assertEquals(errorStrategy, parameters.getErrorStrategy());
        assertEquals(lockTableTimeOutSeconds, parameters.getLockTableTimeOutSeconds());
        assertEquals(swapTableNameRetryTimes, parameters.getSwapTableNameRetryTimes());
        assertEquals(originTableCleanStrategy, parameters.getOriginTableCleanStrategy());
        assertEquals(delimiter, parameters.getDelimiter());
        assertEquals(lockUsers, parameters.getLockUsers());
        assertEquals(swapTableType, parameters.getSwapTableType());
    }

    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(parameters.isContinueOnError());

        parameters.setErrorStrategy(TaskErrorStrategy.FAIL_ON_ERROR);
        assertFalse(parameters.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() {
        when(subTaskParameterFactory.generate(anyString(), any(OnlineSchemaChangeSqlType.class)))
                .thenReturn(new OnlineSchemaChangeScheduleTaskParameters("", "", Arrays.asList(""), null, null));

        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters.setSqlContent("ALTER TABLE TEST TABLE ADD COLUMN COLUMN_NAME INT");

        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameters.generateSubTaskParameters(connectionConfig,
                "test_schema");

        assertEquals(1, subTaskParameters.size());
        OnlineSchemaChangeScheduleTaskParameters taskParameter = subTaskParameters.get(0);
        assertEquals("test_schema", taskParameter.getDatabaseName());
        assertEquals("test_table", taskParameter.getOriginTableName());
        assertEquals(1, taskParameter.getSqlsToBeExecuted().size());
        assertEquals("ALTER TABLE TEST TABLE ADD COLUMN COLUMN_NAME INT",
                taskParameter.getSqlsToBeExecuted().get(0));

        verify(subTaskParameterFactory, times(1)).generate(eq("ALTER TABLE TEST TABLE ADD COLUMN COLUMN_NAME INT"),
                eq(OnlineSchemaChangeSqlType.ALTER));
    }

}