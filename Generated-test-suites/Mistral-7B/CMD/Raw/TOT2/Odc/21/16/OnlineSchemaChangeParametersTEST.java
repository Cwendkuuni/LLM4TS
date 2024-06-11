import static org.junit.Assert.*;

import org.junit.Test;

public class OnlineSchemaChangeParametersTest {

    @Test
    public void testConstructorWithDefaultValues() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();

        assertEquals(OnlineSchemaChangeSqlType.ALTER, parameters.getSqlType());
        assertNull(parameters.getSqlContent());
        assertEquals(TaskErrorStrategy.RETHROW, parameters.getErrorStrategy());
        assertEquals(10, parameters.getLockTableTimeOutSeconds());
        assertEquals(3, parameters.getSwapTableNameRetryTimes());
        assertEquals(OriginTableCleanStrategy.DROP_AND_CREATE, parameters.getOriginTableCleanStrategy());
        assertEquals(";", parameters.getDelimiter());
        assertNull(parameters.getLockUsers());
        assertEquals(SwapTableType.MERGE_IN_PLACE, parameters.getSwapTableType());
        assertFalse(parameters.isContinueOnError());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithInvalidErrorStrategy() {
        new OnlineSchemaChangeParameters(null, OnlineSchemaChangeSqlType.CREATE);
    }

    @Test
    public void testConstructorWithSpecifiedValues() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters(
                OnlineSchemaChangeSqlType.CREATE, "CREATE TABLE", TaskErrorStrategy.CONTINUE, 5, 5,
                OriginTableCleanStrategy.TRUNCATE, null, SwapTableType.COPY_TO_NEW_TABLE);

        assertEquals(OnlineSchemaChangeSqlType.CREATE, parameters.getSqlType());
        assertEquals("CREATE TABLE", parameters.getSqlContent());
        assertEquals(TaskErrorStrategy.CONTINUE, parameters.getErrorStrategy());
        assertEquals(5, parameters.getLockTableTimeOutSeconds());
        assertEquals(5, parameters.getSwapTableNameRetryTimes());
        assertEquals(OriginTableCleanStrategy.TRUNCATE, parameters.getOriginTableCleanStrategy());
        assertNull(parameters.getLockUsers());
        assertEquals(SwapTableType.COPY_TO_NEW_TABLE, parameters.getSwapTableType());
        assertFalse(parameters.isContinueOnError());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNegativeLockTableTimeOutSeconds() {
        new OnlineSchemaChangeParameters(
                OnlineSchemaChangeSqlType.CREATE, null, TaskErrorStrategy.CONTINUE, -5, 5,
                OriginTableCleanStrategy.TRUNCATE, null, SwapTableType.COPY_TO_NEW_TABLE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNegativeSwapTableNameRetryTimes() {
        new OnlineSchemaChangeParameters(
                OnlineSchemaChangeSqlType.CREATE, null, TaskErrorStrategy.CONTINUE, 5, -3,
                OriginTableCleanStrategy.TRUNCATE, null, SwapTableType.COPY_TO_NEW_TABLE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullOrEmptyDelimiter() {
        new OnlineSchemaChangeParameters(
                OnlineSchemaChangeSqlType.CREATE, null, TaskErrorStrategy.CONTINUE, 5, 5,
                OriginTableCleanStrategy.TRUNCATE, null, SwapTableType.COPY_TO_NEW_TABLE, "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullOrEmptyLockUsers() {
        new OnlineSchemaChangeParameters(
                OnlineSchemaChangeSqlType.CREATE, null, TaskErrorStrategy.CONTINUE, 5, 5,
                OriginTableCleanStrategy.TRUNCATE, null, SwapTableType.COPY_TO_NEW_TABLE, null);
    }

    @Test
    public void testIsContinueOnErrorWithValidErrorStrategy() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters(
                OnlineSchemaChangeSqlType.CREATE, null, TaskErrorStrategy.CONTINUE, 5, 5,
                OriginTableCleanStrategy.TRUNCATE, null, SwapTableType.COPY_TO_NEW_TABLE);

        assertTrue(parameters.isContinueOnError());

        parameters.setErrorStrategy(TaskErrorStrategy.RETHROW);
        assertFalse(parameters.isContinueOnError());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsContinueOnErrorWithInvalidErrorStrategy() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters(
                OnlineSchemaChangeSqlType.CREATE, null, null, 5, 5,
                OriginTableCleanStrategy.TRUNCATE, null, SwapTableType.COPY_TO_NEW_TABLE);
    }

    @Test
    public void testGenerateSubTaskParameters() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        String schema = "test_schema";

        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters(
                OnlineSchemaChangeSqlType.CREATE, "CREATE TABLE my_table (id INT PRIMARY KEY)",
                TaskErrorStrategy.CONTINUE, 5, 5, OriginTableCleanStrategy.TRUNCATE, null,
                SwapTableType.MERGE_IN_PLACE);

        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameters
                .generateSubTaskParameters(connectionConfig, schema);

        assertEquals(1, subTaskParameters.size());
        OnlineSchemaChangeScheduleTaskParameters taskParameter = subTaskParameters.get(0);
        assertEquals("CREATE TABLE my_table (id INT PRIMARY KEY)", taskParameter.getSqlContent());
    }

    @Test
    public void testGenerateSubTaskParametersWithMultipleSqls() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        String schema = "test_schema";

        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters(
                OnlineSchemaChangeSqlType.ALTER, "ALTER TABLE my_table ADD COLUMN new_column INT",
                TaskErrorStrategy.CONTINUE, 5, 5, OriginTableCleanStrategy.TRUNCATE, null,
                SwapTableType.MERGE_IN_PLACE);

        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameters
                .generateSubTaskParameters(connectionConfig, schema);

        assertEquals(1, subTaskParameters.size());
        OnlineSchemaChangeScheduleTaskParameters taskParameter = subTaskParameters.get(0);
        List<String> sqlsToBeExecuted = taskParameter.getSqlsToBeExecuted();
        assertEquals(2, sqlsToBeExecuted.size());
        assertEquals("ALTER TABLE my_table ADD COLUMN new_column INT", sqlsToBeExecuted.get(0));
        assertEquals("ALTER TABLE my_table RENAME my_table TO my_table_temp, ADD TABLE my_table_new, RENAME my_table_temp TO my_table", sqlsToBeExecuted.get(1));
    }
}