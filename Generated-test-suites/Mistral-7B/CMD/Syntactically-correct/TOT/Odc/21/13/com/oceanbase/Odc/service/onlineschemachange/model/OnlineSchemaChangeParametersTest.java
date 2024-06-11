import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OnlineSchemaChangeParametersTest {

    @Test
    void testEmptyConstructor() {
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
    void testConstructorWithAllArguments() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters(
                OnlineSchemaChangeSqlType.ALTER,
                "CREATE TABLE new_table (id INT); CREATE TABLE old_table (id INT)",
                TaskErrorStrategy.CONTINUE,
                30,
                3,
                OriginTableCleanStrategy.DROP,
                ";",
                Arrays.asList("user1", "user2"),
                SwapTableType.IN_PLACE);

        assertEquals(OnlineSchemaChangeSqlType.ALTER, parameters.getSqlType());
        assertEquals("CREATE TABLE new_table (id INT); CREATE TABLE old_table (id INT)", parameters.getSqlContent());
        assertEquals(TaskErrorStrategy.CONTINUE, parameters.getErrorStrategy());
        assertEquals(30, parameters.getLockTableTimeOutSeconds());
        assertEquals(3, parameters.getSwapTableNameRetryTimes());
        assertEquals(OriginTableCleanStrategy.DROP, parameters.getOriginTableCleanStrategy());
        assertEquals(";", parameters.getDelimiter());
        assertEquals(Arrays.asList("user1", "user2"), parameters.getLockUsers());
        assertEquals(SwapTableType.IN_PLACE, parameters.getSwapTableType());
    }

    @ParameterizedTest(name = "isContinueOnError with errorStrategy={0}")
    @ValueSource(TaskErrorStrategy.class)
    void testIsContinueOnError(TaskErrorStrategy errorStrategy) {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters(
                OnlineSchemaChangeSqlType.ALTER,
                "CREATE TABLE new_table (id INT); CREATE TABLE old_table (id INT)",
                errorStrategy,
                30,
                3,
                OriginTableCleanStrategy.DROP,
                ";",
                Arrays.asList("user1", "user2"),
                SwapTableType.IN_PLACE);

        assertEquals(errorStrategy, parameters.getErrorStrategy());
        assertTrue(parameters.isContinueOnError());
    }

    @Test
    void testInvalidErrorStrategy() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new OnlineSchemaChangeParameters(
                    OnlineSchemaChangeSqlType.ALTER,
                    "CREATE TABLE new_table (id INT); CREATE TABLE old_table (id INT)",
                    TaskErrorStrategy.VALUE_NOT_FOUND,
                    30,
                    3,
                    OriginTableCleanStrategy.DROP,
                    ";",
                    Arrays.asList("user1", "user2"),
                    SwapTableType.IN_PLACE);
        });
    }

    @Test
    void testGenerateSubTaskParametersHappyPath() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        String schema = "test_schema";

        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters(
                OnlineSchemaChangeSqlType.ALTER,
                "CREATE TABLE new_table (id INT); ALTER TABLE old_table ADD COLUMN new_col INT",
                TaskErrorStrategy.CONTINUE,
                30,
                3,
                OriginTableCleanStrategy.DROP,
                ";",
                Arrays.asList("user1", "user2"),
                SwapTableType.IN_PLACE);

        List<OnlineSchemaChangeScheduleTaskParameters> taskParameters = parameters.generateSubTaskParameters(connectionConfig, schema);
        assertEquals(2, taskParameters.size());

        OnlineSchemaChangeScheduleTaskParameters taskParam1 = taskParameters.get(0);
        assertEquals("test_schema", taskParam1.getDatabaseName());
        assertEquals("old_table", taskParam1.getOriginTableName());
        assertEquals(Arrays.asList("CREATE TABLE new_table (id INT)", "ALTER TABLE old_table ADD COLUMN new_col INT"), taskParam1.getSqlsToBeExecuted());

        OnlineSchemaChangeScheduleTaskParameters taskParam2 = taskParameters.get(1);
        assertEquals("test_schema", taskParam2.getDatabaseName());
        assertEquals("old_table", taskParam2.getOriginTableName());
        assertEquals(Arrays.asList("ALTER TABLE old_table ADD COLUMN new_col INT"), taskParam2.getSqlsToBeExecuted());
    }

    @Test
    void testGenerateSubTaskParametersEmptySqlContent() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        String schema = "test_schema";

        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters(
                OnlineSchemaChangeSqlType.ALTER,
                "",
                TaskErrorStrategy.CONTINUE,
                30,
                3,
                OriginTableCleanStrategy.DROP,
                ";",
                Arrays.asList("user1", "user2"),
                SwapTableType.IN_PLACE);

        List<OnlineSchemaChangeScheduleTaskParameters> taskParameters = parameters.generateSubTaskParameters(connectionConfig, schema);
        assertEquals(0, taskParameters.size());
    }

    @Test
    void testGenerateSubTaskParametersNullSqlContent() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        String schema = "test_schema";

        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters(
                OnlineSchemaChangeSqlType.ALTER,
                null,
                TaskErrorStrategy.CONTINUE,
                30,
                3,
                OriginTableCleanStrategy.DROP,
                ";",
                Arrays.asList("user1", "user2"),
                SwapTableType.IN_PLACE);

        List<OnlineSchemaChangeScheduleTaskParameters> taskParameters = parameters.generateSubTaskParameters(connectionConfig, schema);
        assertEquals(0, taskParameters.size());
    }

    @Test
    void testGenerateSubTaskParametersLockUsersEmptyList() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        String schema = "test_schema";

        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters(
                OnlineSchemaChangeSqlType.ALTER,
                "CREATE TABLE new_table (id INT); ALTER TABLE old_table ADD COLUMN new_col INT",
                TaskErrorStrategy.CONTINUE,
                30,
                3,
                OriginTableCleanStrategy.DROP,
                ";",
                null,
                SwapTableType.IN_PLACE);

        List<OnlineSchemaChangeScheduleTaskParameters> taskParameters = parameters.generateSubTaskParameters(connectionConfig, schema);
        assertEquals(2, taskParameters.size());

        OnlineSchemaChangeScheduleTaskParameters taskParam1 = taskParameters.get(0);
        assertEquals("test_schema", taskParam1.getDatabaseName());
        assertEquals("old_table", taskParam1.getOriginTableName());
        assertNull(taskParam1.getLockUsers());

        OnlineSchemaChangeScheduleTaskParameters taskParam2 = taskParameters.get(1);
        assertEquals("test_schema", taskParam2.getDatabaseName());
        assertEquals("old_table", taskParam2.getOriginTableName());
        assertNull(taskParam2.getLockUsers());
    }
}