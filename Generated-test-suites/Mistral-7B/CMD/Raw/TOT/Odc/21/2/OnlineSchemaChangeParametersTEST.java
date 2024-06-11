import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class OnlineSchemaChangeParametersTest {

    @Test
    void testValidOnlineSchemaChangeParameters() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters.setSqlContent("CREATE TABLE IF NOT EXISTS test_table (id INT PRIMARY KEY, name VARCHAR(50))");
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        parameters.setLockTableTimeOutSeconds(30);
        parameters.setSwapTableType(SwapTableType.SWAP_IN_PLACE);
        parameters.setDelimiter(";");
        parameters.setLockUsers(Collections.singletonList("test_user"));

        Assertions.assertEquals(OnlineSchemaChangeSqlType.ALTER, parameters.getSqlType());
        Assertions.assertEquals("CREATE TABLE IF NOT EXISTS test_table (id INT PRIMARY KEY, name VARCHAR(50))", parameters.getSqlContent());
        Assertions.assertEquals(TaskErrorStrategy.CONTINUE, parameters.getErrorStrategy());
        Assertions.assertEquals(30, parameters.getLockTableTimeOutSeconds());
        Assertions.assertEquals(SwapTableType.SWAP_IN_PLACE, parameters.getSwapTableType());
        Assertions.assertEquals(";", parameters.getDelimiter());
        Assertions.assertEquals(Collections.singletonList("test_user"), parameters.getLockUsers());
        Assertions.assertFalse(parameters.isContinueOnError());
    }

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    void testContinueOnError(boolean continueOnError) {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setErrorStrategy(continueOnError ? TaskErrorStrategy.CONTINUE : TaskErrorStrategy.FAIL);

        Assertions.assertEquals(continueOnError, parameters.isContinueOnError());
    }

    @Test
    void testEmptySqlContent() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        parameters.setSqlContent("");

        Assertions.assertThrows(IllegalArgumentException.class, () -> parameters.generateSubTaskParameters(mock(ConnectionConfig.class), "test_schema"));
    }

    @Test
    void testNullSqlContent() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        parameters.setSqlContent(null);

        Assertions.assertThrows(IllegalArgumentException.class, () -> parameters.generateSubTaskParameters(mock(ConnectionConfig.class), "test_schema"));
    }

    @Test
    void testEmptyLockUsers() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        parameters.setLockUsers(Collections.emptyList());

        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameters.generateSubTaskParameters(mock(ConnectionConfig.class), "test_schema");
        Assertions.assertTrue(subTaskParameters.isEmpty());
    }

    @Test
    void testNullLockUsers() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        parameters.setLockUsers(null);

        Assertions.assertThrows(IllegalArgumentException.class, () -> parameters.generateSubTaskParameters(mock(ConnectionConfig.class), "test_schema"));
    }

    @Test
    void testSqlContentWithMultipleSqls() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        parameters.setSqlContent("CREATE TABLE test_table1 (id INT PRIMARY KEY, name VARCHAR(50)), CREATE TABLE test_table2 (id INT PRIMARY KEY, name VARCHAR(50))");

        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameters.generateSubTaskParameters(mock(ConnectionConfig.class), "test_schema");
        Assertions.assertEquals(2, subTaskParameters.size());
    }

    @Test
    void testSwapTableTypeNotSupported() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        parameters.setSwapTableType(SwapTableType.SWAP_TO_NEW_TABLE);

        Assertions.assertThrows(IllegalArgumentException.class, () -> parameters.generateSubTaskParameters(mock(ConnectionConfig.class), "test_schema"));
    }
}