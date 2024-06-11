import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.core.connection.model.ConnectionConfig;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.model.TableIdentity;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;
import com.oceanbase.odc.service.onlineschemachange.util.SqlUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.mock;

public class OnlineSchemaChangeParametersTest {

    @Test
    public void testValidOnlineSchemaChangeParameters() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters.setsqlContent("ALTER TABLE test_table ADD COLUMN new_column INT;");
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        parameters.setLockTableTimeOutSeconds(30);
        parameters.setSwapTableType(SwapTableType.IMMEDIATE);
        parameters.setDelimiter(";");

        List<String> expectedSqls = new ArrayList<>();
        expectedSqls.add("ALTER TABLE test_table ADD COLUMN new_column INT;");

        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        SubTaskParameterFactory subTaskParameterFactory = mock(SubTaskParameterFactory.class);
        Map<TableIdentity, OnlineSchemaChangeScheduleTaskParameters> expectedTaskParameters = new LinkedHashMap<>();

        try {
            List<OnlineSchemaChangeScheduleTaskParameters> actualTaskParameters = parameters.generateSubTaskParameters(connectionConfig, "test_schema");
            Assertions.assertEquals(expectedTaskParameters.size(), actualTaskParameters.size());
            for (OnlineSchemaChangeScheduleTaskParameters actualTaskParameter : actualTaskParameters) {
                TableIdentity key = new TableIdentity("test_schema", "test_table");
                OnlineSchemaChangeScheduleTaskParameters expectedTaskParameter = expectedTaskParameters.get(key);
                Assertions.assertEquals(expectedTaskParameter, actualTaskParameter);
                Assertions.assertEquals(expectedSqls, actualTaskParameter.getSqlsToBeExecuted());
            }
        } catch (Exception e) {
            Assertions.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testEmptySqlContent() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters.setsqlContent("");
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        parameters.setLockTableTimeOutSeconds(30);
        parameters.setSwapTableType(SwapTableType.IMMEDIATE);
        parameters.setDelimiter(";");

        Assertions.assertThrows(Exception.class, () -> parameters.generateSubTaskParameters(Mockito.mock(ConnectionConfig.class), "test_schema"));
    }

    @Test
    public void testInvalidSqlContent() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters.setsqlContent("ALTER TABLE test_table ADD COLUMN new_column INT; ALTER TABLE test_table ADD COLUMN new_column INT;");
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        parameters.setLockTableTimeOutSeconds(30);
        parameters.setSwapTableType(SwapTableType.IMMEDIATE);
        parameters.setDelimiter(";");

        List<String> expectedSqls = new ArrayList<>();
        expectedSqls.add("ALTER TABLE test_table ADD COLUMN new_column INT;");

        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        SubTaskParameterFactory subTaskParameterFactory = mock(SubTaskParameterFactory.class);
        Map<TableIdentity, OnlineSchemaChangeScheduleTaskParameters> expectedTaskParameters = new LinkedHashMap<>();

        try {
            List<OnlineSchemaChangeScheduleTaskParameters> actualTaskParameters = parameters.generateSubTaskParameters(connectionConfig, "test_schema");
            Assertions.assertEquals(expectedTaskParameters.size(), actualTaskParameters.size());
            for (OnlineSchemaChangeScheduleTaskParameters actualTaskParameter : actualTaskParameters) {
                TableIdentity key = new TableIdentity("test_schema", "test_table");
                OnlineSchemaChangeScheduleTaskParameters expectedTaskParameter = expectedTaskParameters.get(key);
                Assertions.assertEquals(expectedTaskParameter, actualTaskParameter);
                Assertions.assertEquals(expectedSqls, actualTaskParameter.getSqlsToBeExecuted());
            }
        } catch (Exception e) {
            Assertions.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testNullErrorStrategy() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters.setsqlContent("ALTER TABLE test_table ADD COLUMN new_column INT;");
        parameters.setErrorStrategy(null);
        parameters.setLockTableTimeOutSeconds(30);
        parameters.setSwapTableType(SwapTableType.IMMEDIATE);
        parameters.setDelimiter(";");

        Assertions.assertThrows(NullPointerException.class, () -> parameters.generateSubTaskParameters(Mockito.mock(ConnectionConfig.class), "test_schema"));
    }

    @Test
    public void testInvalidErrorStrategy() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters.setsqlContent("ALTER TABLE test_table ADD COLUMN new_column INT;");
        parameters.setErrorStrategy("INVALID");
        parameters.setLockTableTimeOutSeconds(30);
        parameters.setSwapTableType(SwapTableType.IMMEDIATE);
        parameters.setDelimiter(";");

        Assertions.assertThrows(IllegalArgumentException.class, () -> parameters.generateSubTaskParameters(Mockito.mock(ConnectionConfig.class), "test_schema"));
    }

    @Test
    public void testNegativeLockTableTimeOutSeconds() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters.setsqlContent("ALTER TABLE test_table ADD COLUMN new_column INT;");
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        parameters.setLockTableTimeOutSeconds(-10);
        parameters.setSwapTableType(SwapTableType.IMMEDIATE);
        parameters.setDelimiter(";");

        Assertions.assertThrows(IllegalArgumentException.class, () -> parameters.generateSubTaskParameters(Mockito.mock(ConnectionConfig.class), "test_schema"));
    }

    @Test
    public void testNullDelimiter() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters.setsqlContent("ALTER TABLE test_table ADD COLUMN new_column INT;");
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        parameters.setLockTableTimeOutSeconds(30);
        parameters.setSwapTableType(SwapTableType.IMMEDIATE);
        parameters.setsDelimiter(null);

        Assertions.assertThrows(NullPointerException.class, () -> parameters.generateSubTaskParameters(Mockito.mock(ConnectionConfig.class), "test_schema"));
    }
}