import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.core.connection.model.ConnectionConfig;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;

class OnlineSchemaChangeParametersTest {

    @Test
    void testValidOnlineSchemaChangeParameters() {
        // Given
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters.setSqlContent("CREATE TABLE IF NOT EXISTS test_table (id INT PRIMARY KEY);");
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        parameters.setLockTableTimeOutSeconds(30);
        parameters.setSwapTableType(SwapTableType.SWAP_AND_CREATE_NEW);
        parameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.KEEP_ORIGIN);

        ConnectionConfig connectionConfig = Mockito.mock(ConnectionConfig.class);
        SubTaskParameterFactory subTaskParameterFactory = Mockito.mock(SubTaskParameterFactory.class);
        when(subTaskParameterFactory.generate(anyString(), eq(OnlineSchemaChangeSqlType.ALTER)))
                .thenReturn(new OnlineSchemaChangeScheduleTaskParameters("test_database", "test_table", "test_new_table"));

        // When
        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameters.generateSubTaskParameters(connectionConfig, "test_database");

        // Then
        Assertions.assertEquals(1, subTaskParameters.size());
        OnlineSchemaChangeScheduleTaskParameters subTaskParameter = subTaskParameters.get(0);
        Assertions.assertEquals("test_database", subTaskParameter.getDatabaseName());
        Assertions.assertEquals("test_table", subTaskParameter.getOriginTableName());
        Assertions.assertEquals("test_new_table", subTaskParameter.getNewTableName());

        List<String> sqlsToBeExecuted = subTaskParameter.getSqlsToBeExecuted();
        Assertions.assertEquals(2, sqlsToBeExecuted.size());
        String createTableSql = sqlsToBeExecuted.get(0);
        String alterTableSql = sqlsToBeExecuted.get(1);
        Assertions.assertTrue(createTableSql.startsWith("CREATE TABLE IF NOT EXISTS test_table (id INT PRIMARY KEY);"));
        Assertions.assertTrue(alterTableSql.startsWith("ALTER TABLE test_table RENAME TO test_new_table;"));
    }

    @Test
    void testInvalidOnlineSchemaChangeParameters() {
        // Given
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();

        // When & Then
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            parameters.generateSubTaskParameters(null, "test_database");
        });
    }

    @Test
    void testEmptySqlContent() {
        // Given
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters.setSqlContent("");

        // When & Then
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            parameters.generateSubTaskParameters(Mockito.mock(ConnectionConfig.class), "test_database");
        });
    }

    @Test
    void testEmptySqlWithSemicolon() {
        // Given
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters.setSqlContent(";");

        // When & Then
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            parameters.generateSubTaskParameters(Mockito.mock(ConnectionConfig.class), "test_database");
        });
    }

    @Test
    void testEmptySqlWithMultipleSemicolons() {
        // Given
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters.setSqlContent(";;");

        // When & Then
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            parameters.generateSubTaskParameters(Mockito.mock(ConnectionConfig.class), "test_database");
        });
    }

    @Test
    void testInvalidSql() {
        // Given
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters.setSqlContent("CREATE TEST TABLE IF NOT EXISTS test_table (id INT PRIMARY KEY); TEST;");

        // When & Then
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            parameters.generateSubTaskParameters(Mockito.mock(ConnectionConfig.class), "test_database");
        });
    }
}