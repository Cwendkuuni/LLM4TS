import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class OnlineSchemaChangeParametersTest {

    @Test
    public void testDefaultConstructor() {
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
    public void testConstructorWithValues() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters(OnlineSchemaChangeSqlType.ADD, "test",
                TaskErrorStrategy.CONTINUE, 30, 5, OriginTableCleanStrategy.DROP_OLD, ";", Arrays.asList("user1"),
                SwapTableType.SWAP);
        assertEquals(OnlineSchemaChangeSqlType.ADD, parameters.getSqlType());
        assertEquals("test", parameters.getSqlContent());
        assertEquals(TaskErrorStrategy.CONTINUE, parameters.getErrorStrategy());
        assertEquals(30, parameters.getLockTableTimeOutSeconds());
        assertEquals(5, parameters.getSwapTableNameRetryTimes());
        assertEquals(OriginTableCleanStrategy.DROP_OLD, parameters.getOriginTableCleanStrategy());
        assertEquals(";", parameters.getDelimiter());
        assertEquals(Arrays.asList("user1"), parameters.getLockUsers());
        assertEquals(SwapTableType.SWAP, parameters.getSwapTableType());
    }

    @Test
    @ParameterizedTest(name = "[errorStrategy={0}] test isContinueOnError()")
    @ValueSource(enumClass = TaskErrorStrategy.class, labels = {"CONTINUE", "FAIL"})
    public void testIsContinueOnError(TaskErrorStrategy errorStrategy) {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setErrorStrategy(errorStrategy);
        boolean expectedResult = (errorStrategy == TaskErrorStrategy.CONTINUE);
        boolean result = parameters.isContinueOnError();
        assertTrue(expectedResult == result);
    }

    @Test
    public void testGenerateSubTaskParametersHappyPath() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        String schema = "testSchema";
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters(OnlineSchemaChangeSqlType.ADD, "test",
                TaskErrorStrategy.CONTINUE, 30, 5, OriginTableCleanStrategy.DROP_OLD, ";", Arrays.asList("user1"),
                SwapTableType.SWAP);

        List<OnlineSchemaChangeScheduleTaskParameters> expectedResult = Arrays.asList(
                new OnlineSchemaChangeScheduleTaskParameters("testSchema", "oldTableName1", "newTableName1",
                        Arrays.asList("ALTER TABLE oldTableName1 ADD newColumn INT;",
                                "ALTER TABLE newTableName1 DROP COLUMN oldColumn;")),
                new OnlineSchemaChangeScheduleTaskParameters("testSchema", "oldTableName2", "newTableName2",
                        Arrays.asList("ALTER TABLE oldTableName2 ADD newColumn INT;",
                                "ALTER TABLE newTableName2 DROP COLUMN oldColumn;")));

        List<OnlineSchemaChangeScheduleTaskParameters> result = parameters.generateSubTaskParameters(connectionConfig,
                schema);
        assertEquals(expectedResult.size(), result.size());
        for (int i = 0; i < expectedResult.size(); i++) {
            OnlineSchemaChangeScheduleTaskParameters expected = expectedResult.get(i);
            OnlineSchemaChangeScheduleTaskParameters actual = result.get(i);
            assertEquals(expected.getDatabaseName(), actual.getDatabaseName());
            assertEquals(expected.getOriginTableName(), actual.getOriginTableName());
            assertEquals(expected.getNewTableName(), actual.getNewTableName());
            assertEquals(expected.getSqlsToBeExecuted().size(), actual.getSqlsToBeExecuted().size());
            for (int j = 0; j < expected.getSqlsToBeExecuted().size(); j++) {
                String expectedSql = expected.getSqlsToBeExecuted().get(j);
                String actualSql = actual.getSqlsToBeExecuted().get(j);
                assertEquals(expectedSql, actualSql);
            }
        }
    }

    @Test
    public void testGenerateSubTaskParametersNullSqlContent() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        String schema = "testSchema";
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters(OnlineSchemaChangeSqlType.ADD, null,
                TaskErrorStrategy.CONTINUE, 30, 5, OriginTableCleanStrategy.DROP_OLD, ";", Arrays.asList("user1"),
                SwapTableType.SWAP);

        List<OnlineSchemaChangeScheduleTaskParameters> result = parameters.generateSubTaskParameters(connectionConfig,
                schema);
        assertThrows(UnexpectedException.class, () -> result);
    }
}