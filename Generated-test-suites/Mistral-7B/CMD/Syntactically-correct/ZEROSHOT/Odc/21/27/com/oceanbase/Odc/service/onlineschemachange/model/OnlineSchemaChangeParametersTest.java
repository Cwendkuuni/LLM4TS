import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.core.connection.model.ConnectionConfig;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.core.shared.model.TableIdentity;
import com.oceanbase.odc.service.common.util.SqlUtils;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;

public class OnlineSchemaChangeParametersTest {

    @Test
    public void testOnlineSchemaChangeParameters() {
        // Given
        OnlineSchemaChangeSqlType sqlType = OnlineSchemaChangeSqlType.CREATE;
        String sqlContent = "CREATE TABLE test_table (id INT PRIMARY KEY)";
        TaskErrorStrategy errorStrategy = TaskErrorStrategy.CONTINUE;
        Integer lockTableTimeOutSeconds = 30;
        Integer swapTableNameRetryTimes = 3;
        OriginTableCleanStrategy originTableCleanStrategy = OriginTableCleanStrategy.TRUNCATE;
        String delimiter = ";";
        List<String> lockUsers = new ArrayList<>();
        SwapTableType swapTableType = SwapTableType.SWAP;

        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters(sqlType, sqlContent, errorStrategy,
                lockTableTimeOutSeconds, swapTableNameRetryTimes, originTableCleanStrategy, delimiter, lockUsers, swapTableType);

        // When
        ConnectionConfig connectionConfig = Mockito.mock(ConnectionConfig.class);
        String schema = "test_schema";
        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameters.generateSubTaskParameters(connectionConfig, schema);

        // Then
        assertEquals(1, subTaskParameters.size());
        OnlineSchemaChangeScheduleTaskParameters taskParameters = subTaskParameters.get(0);
        assertEquals(sqlType, taskParameters.getSqlType());
        assertEquals(sqlContent, taskParameters.getSqlContent());
        assertTrue(taskParameters.getSqlsToBeExecuted().contains("CREATE TABLE test_table (id INT PRIMARY KEY)"));
        assertTrue(taskParameters.getSqlsToBeExecuted().contains("ALTER TABLE test_table RENAME TO test_table_renamed"));
    }

    @Test(expected = UnexpectedException.class)
    public void testFailToGenerateSubTaskParameters() {
        // Given
        String sqlContent = "CREATE TABLE test_table (id INT PRIMARY KEY)";
        TaskErrorStrategy errorStrategy = TaskErrorStrategy.CONTINUE;
        OnlineSchemaChangeSqlType sqlType = OnlineSchemaChangeSqlType.CREATE;
        ConnectionConfig connectionConfig = Mockito.mock(ConnectionConfig.class);
        String schema = "test_schema";

        doThrow(new RuntimeException("Test Failure")).when(connectionConfig).getDialectType();

        // When
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters(sqlType, sqlContent, errorStrategy, null, null, null, null, null, null);
        parameters.generateSubTaskParameters(connectionConfig, schema);

        // Then
        fail("Should have thrown UnexpectedException");
    }
}