import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;

import com.oceanbase.odc.core.connection.model.ConnectionConfig;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.service.common.util.SqlUtils;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OnlineSchemaChangeParametersTest {

    @Test
    public void testValidParameters() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlType(OnlineSchemaChangeSqlType.CREATE);
        parameters.setSqlContent("CREATE TABLE test_table (id INT PRIMARY KEY, name VARCHAR(255));");
        parameters.setErrorStrategy(TaskErrorStrategy.CONFIRM);
        parameters.setLockTableTimeOutSeconds(30);
        parameters.setSwapTableType(SwapTableType.SWAP_IN_PLACE);
        assert parameters.isValid();
    }

    @Test
    public void testInvalidParameters_NullSqlContent() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlType(OnlineSchemaChangeSqlType.CREATE);
        parameters.setErrorStrategy(TaskErrorStrategy.CONFIRM);
        parameters.setLockTableTimeOutSeconds(30);
        parameters.setSwapTableType(SwapTableType.SWAP_IN_PLACE);
        parameters.setSqlContent(null);
        Assertions.assertFalse(parameters.isValid());
    }

    @Test
    public void testGenerateSubTaskParameters_ValidInput() {
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        String schema = "test_schema";
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlType(OnlineSchemaChangeSqlType.CREATE);
        parameters.setErrorStrategy(TaskErrorStrategy.CONFIRM);
        parameters.setLockTableTimeOutSeconds(30);
        parameters.setSwapTableType(SwapTableType.SWAP_IN_PLACE);
        parameters.setSqlContent("CREATE TABLE test_table (id INT PRIMARY KEY, name VARCHAR(255));");

        when(connectionConfig.getDialectType()).thenReturn("MySQL");

        try {
            SubTaskParameterFactory subTaskParameterFactory = Mockito.spy(new SubTaskParameterFactory(connectionConfig, schema));
            List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameters.generateSubTaskParameters(connectionConfig, schema);
            Assertions.assertEquals(1, subTaskParameters.size());
            OnlineSchemaChangeScheduleTaskParameters taskParameters = subTaskParameters.get(0);
            Assertions.assertEquals("test_schema", taskParameters.getDatabaseName());
            Assertions.assertEquals("test_table", taskParameters.getOriginTableName());
            Assertions.assertFalse(taskParameters.isDropTable());
            Assertions.assertEquals(1, taskParameters.getSqlsToBeExecuted().size());
            Assertions.assertTrue(taskParameters.getSqlsToBeExecuted().get(0).startsWith("CREATE TABLE test_table (id INT PRIMARY KEY, name VARCHAR(255));"));
        } catch (Exception e) {
            Assertions.fail("Exception should not be thrown: " + e.getMessage());
        }
    }

    @ParameterizedTest
    @CsvSource({
            "CREATE TABLE test_table (id INT PRIMARY KEY, name VARCHAR(255)), TEST_DB, test_table, true, MySQL",
            "ALTER TABLE test_table ADD COLUMN new_column INT, TEST_DB, test_table, false, MySQL",
            "DROP TABLE test_table, TEST_DB, test_table, false, MySQL",
            "CREATE TABLE test_table (id INT PRIMARY KEY, name VARCHAR(255)), TEST_DB, test_table, true, PostgreSQL"
    })
    public void testGenerateSubTaskParameters_MultipleQueries(String sqlContent, String databaseName, String tableName,
            boolean dropTable, String dialectType) {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlType(OnlineSchemaChangeSqlType.valueOf(sqlContent.substring(0, 10).toUpperCase()));
        parameters.setErrorStrategy(TaskErrorStrategy.CONFIRM);
        parameters.setLockTableTimeOutSeconds(30);
        parameters.setSwapTableType(SwapTableType.SWAP_IN_PLACE);
        parameters.setSqlContent(sqlContent);
        parameters.setDelimiter(";");

        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        when(connectionConfig.getDialectType()).thenReturn(dialectType);

        try {
            SubTaskParameterFactory subTaskParameterFactory = new SubTaskParameterFactory(connectionConfig, databaseName);
            List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameters.generateSubTaskParameters(connectionConfig, databaseName);
            OnlineSchemaChangeScheduleTaskParameters taskParameter = subTaskParameters.get(0);
            Assertions.assertEquals(databaseName, taskParameter.getDatabaseName());
            Assertions.assertEquals(tableName, taskParameter.getOriginTableName());
            Assertions.assertEquals(dropTable, taskParameter.isDropTable());
            List<String> sqlsToBeExecuted = taskParameter.getSqlsToBeExecuted();
            if (sqlContent.contains("CREATE")) {
                Assertions.assertEquals(2, sqlsToBeExecuted.size());
                Assertions.assertTrue(sqlsToBeExecuted.get(0).startsWith("CREATE TABLE test_table (id INT PRIMARY KEY, name VARCHAR(255));"));
                Assertions.assertTrue(sqlsToBeExecuted.get(1).startsWith("ALTER TABLE test_table ENGINE = InnoDB;"));
            } else {
                Assertions.assertEquals(1, sqlsToBeExecuted.size());
                Assertions.assertTrue(sqlsToBeExecuted.get(0).startsWith(sqlContent.substring(sqlContent.lastIndexOf(" "), sqlContent.length())));
            }
        } catch (Exception e) {
            Assertions.fail("Exception should not be thrown: " + e.getMessage());
        }
    }

    @Test
    public void testGenerateSubTaskParameters_EmptySqlContent() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlType(OnlineSchemaChangeSqlType.CREATE);
        parameters.setErrorStrategy(TaskErrorStrategy.CONFIRM);
        parameters.setLockTableTimeOutSeconds(30);
        parameters.setSwapTableType(SwapTableType.SWAP_IN_PLACE);
        parameters.setSqlContent("");

        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        String schema = "test_schema";

        try {
            SubTaskParameterFactory subTaskParameterFactory = new SubTaskParameterFactory(connectionConfig, schema);
            List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameters.generateSubTaskParameters(connectionConfig, schema);
            Assertions.assertEquals(0, subTaskParameters.size());
        } catch (Exception e) {
            Assertions.fail("Exception should not be thrown: " + e.getMessage());
        }
    }
}