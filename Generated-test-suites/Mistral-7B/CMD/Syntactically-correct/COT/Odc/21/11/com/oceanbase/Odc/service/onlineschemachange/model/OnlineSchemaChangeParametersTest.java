import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.oceanbase.odc.core.connection.model.ConnectionConfig;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.model.TableIdentity;
import com.oceanbase.odc.service.common.util.SqlUtils;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class OnlineSchemaChangeParametersTest {

    @Mock
    private ConnectionConfig connectionConfig;

    @Mock
    private SubTaskParameterFactory subTaskParameterFactory;

    @InjectMocks
    private OnlineSchemaChangeParameters onlineSchemaChangeParameters;

    @Test
    public void testGetSqlType() {
        OnlineSchemaChangeSqlType expected = OnlineSchemaChangeSqlType.ALTER;
        OnlineSchemaChangeSqlType actual = onlineSchemaChangeParameters.getSqlType();
        assertEquals(expected, actual);
    }

    @Test
    public void testSetGetSqlContent() {
        String expected = "test sqlContent";
        String actual = onlineSchemaChangeParameters.getSqlContent();
        onlineSchemaChangeParameters.setSqlContent(expected);
        assertEquals(expected, actual);
    }

    @Test
    public void testIsContinueOnError() {
        TaskErrorStrategy expected = TaskErrorStrategy.CONTINUE;
        taskStrategies: {
            when(onlineSchemaChangeParameters.getErrorStrategy()).thenReturn(expected);
            boolean actual = onlineSchemaChangeParameters.isContinueOnError();
            assertEquals(expected, actual);
        }

        when(onlineSchemaChangeParameters.getErrorStrategy()).thenReturn(TaskErrorStrategy.ABORT);
        assertFalse(onlineSchemaChangeParameters.isContinueOnError());
    }

    @Test
    public void testGetLockTableTimeOutSeconds() {
        int expected = 10;
        int actual = onlineSchemaChangeParameters.getLockTableTimeOutSeconds();
        assertEquals(expected, actual);
    }

    @Test
    public void testSetGetSwapTableNameRetryTimes() {
        int expected = 3;
        int actual = onlineSchemaChangeParameters.getSwapTableNameRetryTimes();
        onlineSchemaChangeParameters.setSwapTableNameRetryTimes(expected);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetOriginTableCleanStrategy() {
        OnlineSchemaChangeParameters.OriginTableCleanStrategy expected = OnlineSchemaChangeParameters.OriginTableCleanStrategy.TRUNCATE;
        OnlineSchemaChangeParameters.OriginTableCleanStrategy actual = onlineSchemaChangeParameters.getOriginTableCleanStrategy();
        assertEquals(expected, actual);
    }

    @Test
    public void testGenerateSubTaskParameters_emptySqls() {
        when(subTaskParameterFactory.generate(anyString(), any(OnlineSchemaChangeSqlType.class))).thenReturn(
                new OnlineSchemaChangeScheduleTaskParameters());
        List<OnlineSchemaChangeScheduleTaskParameters> expected = new ArrayList<>();
        List<OnlineSchemaChangeScheduleTaskParameters> actual = onlineSchemaChangeParameters
                .generateSubTaskParameters(connectionConfig, "testSchema");
        assertEquals(expected, actual);
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParameters_failedToGenerateSubtasks() {
        String errorMessage = "test error message";
        doThrow(new UnexpectedException(errorMessage)).when(subTaskParameterFactory).generate(anyString(), any(OnlineSchemaChangeSqlType.class));
        onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, "testSchema");
    }

    @Test
    public void testGenerateSubTaskParameters_sqlsWithNewTableName() {
        String databaseName = "testDatabase";
        String originTableName = "testOriginTable";
        String newTableName = "testNewTable";
        String sql = "test sql";
        OnlineSchemaChangeSqlType sqlType = OnlineSchemaChangeSqlType.ALTER;

        when(subTaskParameterFactory.generate(sql, sqlType)).thenReturn(new OnlineSchemaChangeScheduleTaskParameters(databaseName, originTableName));
        when(connectionConfig.getDialectType()).thenReturn("testDialectType");

        OnlineSchemaChangeScheduleTaskParameters expected = new OnlineSchemaChangeScheduleTaskParameters(databaseName, originTableName);
        OnlineSchemaChangeScheduleTaskParameters actual = onlineSchemaChangeParameters
                .generateSubTaskParameters(connectionConfig, "testSchema")
                .get(0);

        assertEquals(expected, actual);

        String newAlterStmt = "test newAlterStmt";
        expected.getSqlsToBeExecuted().add(newAlterStmt);
        actual = onlineSchemaChangeParameters
                .generateSubTaskParameters(connectionConfig, "testSchema")
                .get(0);

        assertEquals(expected.getSqlsToBeExecuted().size(), actual.getSqlsToBeExecuted().size());
        assertEquals(newAlterStmt, actual.getSqlsToBeExecuted().get(1));
    }

    @Test
    public void testIsContinueOnError_whenErrorStrategyIsAbort() {
        TaskErrorStrategy errorStrategy = TaskErrorStrategy.ABORT;
        OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
        onlineSchemaChangeParameters.setErrorStrategy(errorStrategy);
        assertFalse(onlineSchemaChangeParameters.isContinueOnError());
    }
}