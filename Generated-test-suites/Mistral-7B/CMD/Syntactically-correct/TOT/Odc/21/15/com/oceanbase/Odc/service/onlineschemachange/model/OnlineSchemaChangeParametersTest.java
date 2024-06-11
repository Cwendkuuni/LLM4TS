import org.junit.jupiter.api.Assertions;// for Assertions
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;

import com.oceanbase.odc.core.connection.model.ConnectionConfig;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.service.common.util.SqlUtils;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class OnlineSchemaChangeParametersTest {

    @Test
    public void testValidParameters() {
        // GIVEN
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters.setSqlContent("CREATE TABLE test_table (id INT PRIMARY KEY); ALTER TABLE test_table ADD COLUMN new_column INT;");
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        parameters.setLockTableTimeOutSeconds(10);
        parameters.setSwapTableType(SwapTableType.ASYNC);
        parameters.setDelimiter(";");

        // WHEN
        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameters.generateSubTaskParameters(
                Mockito.mock(ConnectionConfig.class), "test_schema");

        // THEN
        Assertions.assertEquals(2, subTaskParameters.size());
        // Add assertions for checking correctness of subTaskParameters based on given sqlContent

        // Verify that generateSubTaskParameters method does not throw an exception
        Assertions.assertDoesNotThrow(new Executable() {
            @Override
            public void execute() {
                parameters.generateSubTaskParameters(Mockito.mock(ConnectionConfig.class), "test_schema");
            }
        });
    }

    @Test
    public void testGenerateSubTaskParametersWithEmptySqlContent() {
        // GIVEN
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters.setSqlContent("");
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        parameters.setLockTableTimeOutSeconds(10);
        parameters.setSwapTableType(SwapTableType.ASYNC);
        parameters.setDelimiter(";");

        // WHEN & THEN
        Assertions.assertThrows(UnexpectedException.class, () -> {
            parameters.generateSubTaskParameters(Mockito.mock(ConnectionConfig.class), "test_schema");
        });
    }

    @Test
    public void testGenerateSubTaskParametersWithInvalidDelimiter() {
        // GIVEN
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters.setSqlContent("CREATE TABLE test_table (id INT PRIMARY KEY); ALTER TABLE test_table ADD COLUMN new_column INT;");
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        parameters.setLockTableTimeOutSeconds(10);
        parameters.setSwapTableType(SwapTableType.ASYNC);
        parameters.setDelimiter("x");

        // WHEN & THEN
        Assertions.assertThrows(UnexpectedException.class, () -> {
            parameters.generateSubTaskParameters(Mockito.mock(ConnectionConfig.class), "test_schema");
        });
    }

    @Test
    public void testGenerateSubTaskParametersWithNullConnectionConfig() {
        // GIVEN
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters.setSqlContent("CREATE TABLE test_table (id INT PRIMARY KEY); ALTER TABLE test_table ADD COLUMN new_column INT;");
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        parameters.setLockTableTimeOutSeconds(10);
        parameters.setSwapTableType(SwapTableType.ASYNC);
        parameters.setDelimiter(";");

        // WHEN & THEN
        Assertions.assertThrows(NullPointerException.class, () -> {
            parameters.generateSubTaskParameters(null, "test_schema");
        });
    }

    @Test
    public void testGenerateSubTaskParametersWithNullSchema() {
        // GIVEN
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters.setSqlContent("CREATE TABLE test_table (id INT PRIMARY KEY); ALTER TABLE test_table ADD COLUMN new_column INT;");
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        parameters.setLockTableTimeOutSeconds(10);
        parameters.setSwapTableType(SwapTableType.ASYNC);
        parameters.setDelimiter(";");

        // WHEN & THEN
        Assertions.assertThrows(NullPointerException.class, () -> {
            parameters.generateSubTaskParameters(Mockito.mock(ConnectionConfig.class), null);
        });
    }

    @Test
    public void testIsContinueOnError() {
        // GIVEN
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();

        // WHEN
        boolean result = parameters.isContinueOnError();

        // THEN
        Assertions.assertTrue(result); // assuming TaskErrorStrategy.CONTINUE is set by default

        // Verify that setting errorStrategy to other values changes the result accordingly
        parameters.setErrorStrategy(TaskErrorStrategy.FAIL);
        result = parameters.isContinueOnError();
        Assertions.assertFalse(result);
    }
}