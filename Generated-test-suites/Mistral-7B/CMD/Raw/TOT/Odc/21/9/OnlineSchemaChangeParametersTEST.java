import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;

public class OnlineSchemaChangeParametersTest {

    @Test
    public void testOnlineSchemaChangeParameters() {
        // Test valid parameters
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlType(OnlineSchemaChangeSqlType.CREATE);
        parameters.setSqlContent("CREATE TABLE test_table (id INT PRIMARY KEY, name VARCHAR(50));");
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        parameters.setLockTableTimeOutSeconds(10);
        parameters.setSwapTableType(SwapTableType.NOT_SWAP);
        Assertions.assertAll(() -> Assertions.assertEquals(OnlineSchemaChangeSqlType.CREATE, parameters.getSqlType()),
                () -> Assertions.assertEquals("CREATE TABLE test_table (id INT PRIMARY KEY, name VARCHAR(50));", parameters.getSqlContent()),
                () -> Assertions.assertEquals(TaskErrorStrategy.CONTINUE, parameters.getErrorStrategy()),
                () -> Assertions.assertEquals(10, parameters.getLockTableTimeOutSeconds()),
                () -> Assertions.assertEquals(SwapTableType.NOT_SWAP, parameters.getSwapTableType()));

        // Test invalid parameters
        try {
            OnlineSchemaChangeParameters invalidParameters = new OnlineSchemaChangeParameters();
            invalidParameters.setSqlType(null);
            Assertions.assertThrows(NullPointerException.class, () -> invalidParameters.getSqlType());
        } catch (NullPointerException e) {
            Assertions.assertEquals("sqlType is marked as final and should not be null", e.getMessage());
        }

        try {
            OnlineSchemaChangeParameters invalidParameters = new OnlineSchemaChangeParameters();
            invalidParameters.setSqlContent(null);
            Assertions.assertThrows(NullPointerException.class, () -> invalidParameters.getSqlContent());
        } catch (NullPointerException e) {
            Assertions.assertEquals("sqlContent is marked as final and should not be null", e.getMessage());
        }

        try {
            OnlineSchemaChangeParameters invalidParameters = new OnlineSchemaChangeParameters();
            invalidParameters.setErrorStrategy(null);
            Assertions.assertThrows(NullPointerException.class, () -> invalidParameters.getErrorStrategy());
        } catch (NullPointerException e) {
            Assertions.assertEquals("errorStrategy is marked as final and should not be null", e.getMessage());
        }

        try {
            OnlineSchemaChangeParameters invalidParameters = new OnlineSchemaChangeParameters();
            invalidParameters.setLockTableTimeOutSeconds(-1);
            Assertions.assertThrows(IllegalArgumentException.class, () -> invalidParameters.getLockTableTimeOutSeconds());
        } catch (IllegalArgumentException e) {
            Assertions.assertEquals("lockTableTimeOutSeconds should be non-negative", e.getMessage());
        }

        try {
            OnlineSchemaChangeParameters invalidParameters = new OnlineSchemaChangeParameters();
            invalidParameters.setSwapTableType(null);
            Assertions.assertThrows(NullPointerException.class, () -> invalidParameters.getSwapTableType());
        } catch (NullPointerException e) {
            Assertions.assertEquals("swapTableType is marked as final and should not be null", e.getMessage());
        }

        // Test generateSubTaskParameters
        ConnectionConfig connectionConfig = new ConnectionConfig();
        String schema = "test_schema";
        OnlineSchemaChangeParameters validParameters = new OnlineSchemaChangeParameters();
        validParameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        validParameters.setSqlContent("ALTER TABLE test_table ADD COLUMN new_column INT;");
        validParameters.setErrorStrategy(TaskErrorStrategy.FAILURE);
        List<OnlineSchemaChangeScheduleTaskParameters> taskParameters = validParameters.generateSubTaskParameters(connectionConfig, schema);
        Assertions.assertAll(() -> Assertions.assertEquals(1, taskParameters.size()),
                taskParameters -> Assertions.assertAll(() -> Assertions.assertEquals("test_schema", taskParameters.get(0).getDatabaseName()),
                        () -> Assertions.assertEquals("test_table", taskParameters.get(0).getOriginTableName()),
                        () -> Assertions.assertEquals(OnlineSchemaChangeSqlType.ALTER, taskParameters.get(0).getSqlType()),
                        () -> Assertions.assertEquals("ALTER TABLE test_table ADD COLUMN new_column INT;", taskParameters.get(0).getSqlContent()),
                        () -> Assertions.assertFalse(taskParameters.get(0).isContinueOnError()),
                        sqls -> Assertions.assertAll(() -> Assertions.assertEquals(1, sqls.size()),
                                sql -> Assertions.assertEquals("ALTER TABLE test_table ADD COLUMN new_column INT;", sql))));

        // Test isContinueOnError
        OnlineSchemaChangeParameters validParametersContinue = new OnlineSchemaChangeParameters();
        validParametersContinue.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        validParametersContinue.setSqlContent("ALTER TABLE test_table ADD COLUMN new_column INT;");
        validParametersContinue.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        Assertions.assertTrue(validParametersContinue.isContinueOnError());

        OnlineSchemaChangeParameters validParametersFailure = new OnlineSchemaChangeParameters();
        validParametersFailure.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        validParametersFailure.setSqlContent("ALTER TABLE test_table ADD COLUMN new_column INT;");
        validParametersFailure.setErrorStrategy(TaskErrorStrategy.FAILURE);
        Assertions.assertFalse(validParametersFailure.isContinueOnError());
    }

    @Test
    @EnumSource(OnlineSchemaChangeSqlType.class)
    public void testSqlType(OnlineSchemaChangeSqlType sqlType) {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlType(sqlType);
        Assertions.assertEquals(sqlType, parameters.getSqlType());
    }

    @Test
    @ValueSource(ints = {-1, 0, 1, 2})
    public void testLockTableTimeOutSeconds(int lockTableTimeOutSeconds) {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setLockTableTimeOutSeconds(lockTableTimeOutSeconds);
        if (lockTableTimeOutSeconds < 0) {
            Assertions.assertThrows(IllegalArgumentException.class, () -> parameters.getLockTableTimeOutSeconds());
        } else {
            Assertions.assertEquals(lockTableTimeOutSeconds, parameters.getLockTableTimeOutSeconds());
        }
    }
}