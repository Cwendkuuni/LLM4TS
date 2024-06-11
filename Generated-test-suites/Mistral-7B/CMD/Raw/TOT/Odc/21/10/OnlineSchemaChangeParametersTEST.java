import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

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
        parameters.setSqlContent("CREATE TABLE IF NOT EXISTS test_table (id INT PRIMARY KEY);");
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        parameters.setLockTableTimeOutSeconds(30);
        parameters.setSwapTableType(SwapTableType.SWAP);
        parameters.setSwapTableNameRetryTimes(3);
        parameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.DROP);

        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        String schema = "test_schema";

        List<OnlineSchemaChangeScheduleTaskParameters> actual = parameters.generateSubTaskParameters(connectionConfig, schema);

        Assertions.assertEquals(1, actual.size());
        OnlineSchemaChangeScheduleTaskParameters taskParameters = actual.get(0);
        Assertions.assertEquals("test_schema", taskParameters.getDatabaseName());
        Assertions.assertEquals("test_table", taskParameters.getOriginTableName());
        Assertions.assertEquals(OnlineSchemaChangeSqlType.ALTER, taskParameters.getSqlType());
        Assertions.assertEquals(List.of("CREATE TABLE IF NOT EXISTS test_table (id INT PRIMARY KEY);"), taskParameters.getSqlsToBeExecuted());
    }

    @Test
    public void testNullSqlContent() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        parameters.setLockTableTimeOutSeconds(30);
        parameters.setSwapTableType(SwapTableType.SWAP);
        parameters.setSwapTableNameRetryTimes(3);
        parameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.DROP);
        parameters.setSqlContent(null);

        Assertions.assertThrows(UnexpectedException.class, () -> parameters.generateSubTaskParameters(Mockito.mock(ConnectionConfig.class), "test_schema"));
    }

    @Test
    public void testEmptySqlContent() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        parameters.setLockTableTimeOutSeconds(30);
        parameters.setSwapTableType(SwapTableType.SWAP);
        parameters.setSwapTableNameRetryTimes(3);
        parameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.DROP);
        parameters.setSqlContent("");

        Assertions.assertThrows(UnexpectedException.class, () -> parameters.generateSubTaskParameters(Mockito.mock(ConnectionConfig.class), "test_schema"));
    }

    @Test
    public void testInvalidSqlContent() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        parameters.setLockTableTimeOutSeconds(30);
        parameters.setSwapTableType(SwapTableType.SWAP);
        parameters.setSwapTableNameRetryTimes(3);
        parameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.DROP);
        parameters.setSqlContent("invalid_sql_content");

        Assertions.assertThrows(UnexpectedException.class, () -> parameters.generateSubTaskParameters(Mockito.mock(ConnectionConfig.class), "test_schema"));
    }
}