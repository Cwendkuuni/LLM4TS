import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class OnlineSchemaChangeParametersTest {

    private ConnectionConfig connectionConfig;
    private OnlineSchemaChangeParameters parameters;

    @Before
    public void setup() {
        connectionConfig = Mockito.mock(ConnectionConfig.class);
        parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters.setSqlContent("ALTER TABLE table1 ADD COLUMN col1 INT; ALTER TABLE table1 RENAME COLUMN col2 TO col3;");
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        parameters.setLockTableTimeOutSeconds(30);
        parameters.setSwapTableType(SwapTableType.IN_PLACE);
        parameters.setDelimiter(";");
        parameters.setSwapTableNameRetryTimes(3);
        parameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.DROP_AND_RECREATE);
        parameters.setLockUsers(new ArrayList<>());
    }

    @Test
    public void testIsContinueOnError() {
        assertTrue(parameters.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() {
        Mockito.when(connectionConfig.getDialectType()).thenReturn("mysql");

        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameters.generateSubTaskParameters(connectionConfig, "test_schema");

        assertEquals(2, subTaskParameters.size());

        OnlineSchemaChangeScheduleTaskParameters firstTaskParameters = subTaskParameters.get(0);
        assertEquals("test_schema", firstTaskParameters.getDatabaseName());
        assertEquals("table1", firstTaskParameters.getOriginTableName());
        assertEquals(OnlineSchemaChangeSqlType.ALTER, firstTaskParameters.getSqlType());
        assertFalse(firstTaskParameters.isDropTable());
        assertFalse(firstTaskParameters.isRenameTable());
        assertEquals(30, firstTaskParameters.getLockTableTimeOutSeconds());
        assertEquals(SwapTableType.IN_PLACE, firstTaskParameters.getSwapTableType());
        assertEquals("ALTER TABLE table1 ADD COLUMN col1 INT", firstTaskParameters.getSqlsToBeExecuted().get(0));

        OnlineSchemaChangeScheduleTaskParameters secondTaskParameters = subTaskParameters.get(1);
        assertEquals("test_schema", secondTaskParameters.getDatabaseName());
        assertEquals("table1", secondTaskParameters.getOriginTableName());
        assertEquals(OnlineSchemaChangeSqlType.ALTER, secondTaskParameters.getSqlType());
        assertTrue(secondTaskParameters.isDropTable());
        assertFalse(secondTaskParameters.isRenameTable());
        assertEquals(30, secondTaskParameters.getLockTableTimeOutSeconds());
        assertEquals(SwapTableType.IN_PLACE, secondTaskParameters.getSwapTableType());
        assertEquals("ALTER TABLE table1 RENAME COLUMN col2 TO col3", secondTaskParameters.getSqlsToBeExecuted().get(0));

        Mockito.verify(connectionConfig, Mockito.times(1)).getDialectType();
        Mockito.verifyNoMoreInteractions(connectionConfig);
    }
}