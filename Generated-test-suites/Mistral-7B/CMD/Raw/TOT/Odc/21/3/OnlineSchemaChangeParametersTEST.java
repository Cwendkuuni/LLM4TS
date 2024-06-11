import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.oceanbase.odc.core.connection.ConnectionConfig;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.service.common.util.SqlUtils;
import com.oceanbase.odc.service.connection.model.ConnectionConfigImpl;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactoryImpl;

public class OnlineSchemaChangeParametersTest {

    @Test
    public void testValidParameters() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlType(OnlineSchemaChangeSqlType.CREATE);
        parameters.setSqlContent("CREATE TABLE test (id INT PRIMARY KEY) ;");
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        parameters.setLockTableTimeOutSeconds(10);
        parameters.setSwapTableType(SwapTableType.SWAP_NAME);

        Assertions.assertNotNull(parameters);
        Assertions.assertEquals(OnlineSchemaChangeSqlType.CREATE, parameters.getSqlType());
        Assertions.assertNotNull(parameters.getSqlContent());
        Assertions.assertEquals(TaskErrorStrategy.CONTINUE, parameters.getErrorStrategy());
        Assertions.assertNotNull(parameters.getLockTableTimeOutSeconds());
        Assertions.assertEquals(SwapTableType.SWAP_NAME, parameters.getSwapTableType());
    }

    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);

        Assertions.assertTrue(parameters.isContinueOnError());

        OnlineSchemaChangeParameters parametersContinue = new OnlineSchemaChangeParameters();
        parametersContinue.setErrorStrategy(TaskErrorStrategy.FAIL);

        Assertions.assertFalse(parametersContinue.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParametersHappyPath() {
        ConnectionConfig connectionConfig = mock(ConnectionConfigImpl.class);
        when(connectionConfig.getDialectType()).thenReturn("mysql");

        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlType(OnlineSchemaChangeSqlType.CREATE);
        parameters.setSqlContent("CREATE TABLE test (id INT PRIMARY KEY) ;");
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        parameters.setLockTableTimeOutSeconds(10);
        parameters.setSwapTableType(SwapTableType.SWAP_NAME);

        SubTaskParameterFactory subTaskParameterFactory = new SubTaskParameterFactoryImpl();
        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameters.generateSubTaskParameters(connectionConfig, "test");

        Assertions.assertNotNull(subTaskParameters);
        Assertions.assertEquals(1, subTaskParameters.size());

        OnlineSchemaChangeScheduleTaskParameters taskParameter = subTaskParameters.get(0);
        Assertions.assertNotNull(taskParameter);
        Assertions.assertEquals("test", taskParameter.getDatabaseName());
        Assertions.assertEquals("test", taskParameter.getOriginTableName());
        Assertions.assertEquals(OnlineSchemaChangeSqlType.CREATE, taskParameter.getSqlType());
        Assertions.assertNotNull(taskParameter.getSqlsToBeExecuted());
        Assertions.assertEquals(1, taskParameter.getSqlsToBeExecuted().size());
        Assertions.assertEquals("CREATE TABLE test (id INT PRIMARY KEY) ;", taskParameter.getSqlsToBeExecuted().get(0));
    }

    @ParameterizedTest(name = "{index}: testGenerateSubTaskParametersWithDdlUtils- {0}")
    @MethodSource("provideSqlContentWithDdlUtils")
    public void testGenerateSubTaskParametersWithDdlUtils(String sqlContent) {
        ConnectionConfig connectionConfig = mock(ConnectionConfigImpl.class);
        when(connectionConfig.getDialectType()).thenReturn("mysql");

        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters.setSqlContent(sqlContent);
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        parameters.setLockTableTimeOutSeconds(10);
        parameters.setSwapTableType(SwapTableType.SWAP_NAME);

        SubTaskParameterFactory subTaskParameterFactory = new SubTaskParameterFactoryImpl();
        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameters.generateSubTaskParameters(connectionConfig, "test");

        Assertions.assertNotNull(subTaskParameters);
        Assertions.assertEquals(1, subTaskParameters.size());

        OnlineSchemaChangeScheduleTaskParameters taskParameter = subTaskParameters.get(0);
        Assertions.assertNotNull(taskParameter);
        Assertions.assertEquals("test", taskParameter.getDatabaseName());
        Assertions.assertEquals("test", taskParameter.getOriginTableName());
        Assertions.assertEquals(OnlineSchemaChangeSqlType.ALTER, taskParameter.getSqlType());
        Assertions.assertNotNull(taskParameter.getSqlsToBeExecuted());
        Assertions.assertEquals(2, taskParameter.getSqlsToBeExecuted().size());
        Assertions.assertEquals("ALTER TABLE test ADD new_column INT ;", taskParameter.getSqlsToBeExecuted().get(0));
        Assertions.assertEquals("ALTER TABLE test RENAME TO test_renamed ;", taskParameter.getSqlsToBeExecuted().get(1));
    }

    private static List<String> provideSqlContentWithDdlUtils() {
        return Arrays.asList("ALTER TABLE test ADD new_column INT ;; ALTER TABLE test RENAME TO test_renamed ;");
    }
}