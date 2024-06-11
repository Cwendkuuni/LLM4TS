import static org.junit.Assert.*;
import org.junit.Test;

public class OnlineSchemaChangeParametersTest {

    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(parameters.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlContent("ALTER TABLE test_table ADD column_name INT;");
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters.setDelimiter(";");
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);

        ConnectionConfig connectionConfig = new ConnectionConfig(); // mock
        String schema = "test_schema";

        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameters.generateSubTaskParameters(connectionConfig, schema);

        assertNotNull(subTaskParameters);
        assertEquals(1, subTaskParameters.size());
        assertNotNull(subTaskParameters.get(0).getSqlsToBeExecuted());
        assertFalse(subTaskParameters.get(0).getSqlsToBeExecuted().isEmpty());
    }

    // Additional test cases can be added for other methods in OnlineSchemaChangeParameters class

}