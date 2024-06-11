import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

public class OnlineSchemaChangeParametersTest {

    @Test
    public void testValidOnlineSchemaChangeParameters() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlType(OnlineSchemaChangeSqlType.CREATE);
        parameters.setsqlContent("CREATE TABLE test_table (id INT PRIMARY KEY);");
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        parameters.setLockTableTimeOutSeconds(30);
        parameters.setSwapTableType(SwapTableType.IN_PLACE);

        Assertions.assertDoesNotThrow(() -> parameters.validate());
    }

    @Test
    public void testInvalidOnlineSchemaChangeParameters_missingSqlType() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setsqlContent("CREATE TABLE test_table (id INT PRIMARY KEY);");

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            parameters.validate();
        });
    }

    @Test
    public void testInvalidOnlineSchemaChangeParameters_missingSqlContent() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlType(OnlineSchemaChangeSqlType.CREATE);

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            parameters.validate();
        });
    }

    @ParameterizedTest
    @CsvSource({
            "CREATE TABLE test_table (id INT PRIMARY KEY),TaskErrorStrategy.CONTINUE",
            "CREATE TABLE test_table (id INT PRIMARY KEY),TaskErrorStrategy.ABORT",
            "CREATE TABLE test_table (id INT PRIMARY KEY),null",
            "CREATE TABLE test_table (id INT PRIMARY KEY),INVALID_STRATEGY"
    })
    public void testInvalidOnlineSchemaChangeParameters_invalidErrorStrategy(String sqlContent, String errorStrategy) {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlType(OnlineSchemaChangeSqlType.CREATE);
        parameters.setsqlContent(sqlContent);
        parameters.setErrorStrategy(errorStrategy);

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            parameters.validate();
        });
    }

    @Test
    public void testGenerateSubTaskParameters_validParameters() {
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlType(OnlineSchemaChangeSqlType.CREATE);
        parameters.setsqlContent("CREATE TABLE test_table (id INT PRIMARY KEY);");
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        parameters.setLockTableTimeOutSeconds(30);
        parameters.setSwapTableType(SwapTableType.IN_PLACE);

        List<OnlineSchemaChangeScheduleTaskParameters> actual = parameters.generateSubTaskParameters(connectionConfig, "test_schema");

        Assertions.assertNotNull(actual);
        Assertions.assertFalse(actual.isEmpty());
    }

    @Test
    public void testGenerateSubTaskParameters_invalidLockTableTimeOutSeconds() {
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlType(OnlineSchemaChangeSqlType.CREATE);
        parameters.setsqlContent("CREATE TABLE test_table (id INT PRIMARY KEY);");
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        parameters.setLockTableTimeOutSeconds(-1);
        parameters.setSwapTableType(SwapTableType.IN_PLACE);

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            parameters.generateSubTaskParameters(connectionConfig, "test_schema");
        });

        Mockito.verify(connectionConfig, Mockito.times(0)).getDialectType();
    }

    @Test
    public void testValidate_validParameters() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlType(OnlineSchemaChangeSqlType.CREATE);
        parameters.setsqlContent("CREATE TABLE test_table (id INT PRIMARY KEY);");
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        parameters.setLockTableTimeOutSeconds(30);
        parameters.setSwapTableType(SwapTableType.IN_PLACE);

        parameters.validate();
    }

    @Test
    public void testValidate_missingSqlType() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setsqlContent("CREATE TABLE test_table (id INT PRIMARY KEY);");

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            parameters.validate();
        });
    }

    @Test
    public void testValidate_missingSqlContent() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlType(OnlineSchemaChangeSqlType.CREATE);

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            parameters.validate();
        });
    }
}