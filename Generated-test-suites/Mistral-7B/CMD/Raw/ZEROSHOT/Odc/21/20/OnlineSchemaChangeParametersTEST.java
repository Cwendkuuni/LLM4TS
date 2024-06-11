import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.core.connection.ConnectionConfig;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.model.TableIdentity;
import com.oceanbase.odc.service.common.util.SqlUtils;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;

import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;

public class OnlineSchemaChangeParametersTest {

    @Test
    public void testInstance() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        assertNotNull(parameters);
        assertEquals(OnlineSchemaChangeSqlType.CREATE, parameters.getSqlType());
        assertEquals(TaskErrorStrategy.CONTINUE, parameters.getErrorStrategy());
        assertEquals(5, parameters.getLockTableTimeOutSeconds());
        assertEquals(3, parameters.getSwapTableNameRetryTimes());
        assertEquals(OnlineSchemaChangeParameters.OriginTableCleanStrategy.TRUNCATE, parameters.getOriginTableCleanStrategy());
        assertEquals(";", parameters.getDelimiter());
        assertNull(parameters.getLockUsers());
        assertEquals(SwapTableType.COLUMNS_FIRST, parameters.getSwapTableType());
    }

    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(parameters.isContinueOnError());

        parameters.setErrorStrategy(TaskErrorStrategy.ABORT);
        assertFalse(parameters.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() {
        ConnectionConfig connectionConfig = Mockito.mock(ConnectionConfig.class);
        String schema = "test_schema";
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters.setSqlContent("ALTER TABLE test_table DROP COLUMN col1; ALTER TABLE test_table ADD COLUMN col2 INT;");
        parameters.setLockTableTimeOutSeconds(5);
        parameters.setSwapTableNameRetryTimes(3);
        parameters.setOriginTableCleanStrategy(OnlineSchemaChangeParameters.OriginTableCleanStrategy.TRUNCATE);
        parameters.setSwapTableType(SwapTableType.COLUMNS_FIRST);

        List<OnlineSchemaChangeScheduleTaskParameters> expected = new ArrayList<>();

        OnlineSchemaChangeScheduleTaskParameters task1 = Mockito.mock(OnlineSchemaChangeScheduleTaskParameters.class);
        OnlineSchemaChangeScheduleTaskParameters task2 = Mockito.mock(OnlineSchemaChangeScheduleTaskParameters.class);

        TableIdentity key1 = new TableIdentity("test_database", "test_table");
        TableIdentity key2 = new TableIdentity("test_database", "test_table");

        when(task1.getDatabaseName()).thenReturn("test_database");
        when(task1.getOriginTableName()).thenReturn("test_table");
        when(task1.getNewTableName()).thenReturn("test_table_new");
        when(task1.getSqlsToBeExecuted()).thenReturn(new ArrayList<>());

        when(task2.getDatabaseName()).thenReturn("test_database");
        when(task2.getOriginTableName()).thenReturn("test_table");
        when(task2.getNewTableName()).thenReturn("test_table_new");
        when(task2.getSqlsToBeExecuted()).thenReturn(new ArrayList<>());

        expected.add(task1);
        expected.add(task2);

        try {
            List<OnlineSchemaChangeScheduleTaskParameters> result = parameters.generateSubTaskParameters(connectionConfig, schema);
            assertEquals(expected.size(), result.size());
            for (int i = 0; i < expected.size(); i++) {
                OnlineSchemaChangeScheduleTaskParameters expectedTask = expected.get(i);
                OnlineSchemaChangeScheduleTaskParameters actualTask = result.get(i);
                assertEquals(expectedTask.getDatabaseName(), actualTask.getDatabaseName());
                assertEquals(expectedTask.getOriginTableName(), actualTask.getOriginTableName());
                assertEquals(expectedTask.getNewTableName(), actualTask.getNewTableName());
                assertEquals(expectedTask.getSqlsToBeExecuted().size(), actualTask.getSqlsToBeExecuted().size());
                for (int j = 0; j < expectedTask.getSqlsToBeExecuted().size(); j++) {
                    String expectedSql = expectedTask.getSqlsToBeExecuted().get(j);
                    String actualSql = actualTask.getSqlsToBeExecuted().get(j);
                    assertEquals(expectedSql, actualSql);
                }
            }
        } catch (Exception e) {
            fail("Failed to generate subtasks: " + e.getMessage());
        }
    }
}