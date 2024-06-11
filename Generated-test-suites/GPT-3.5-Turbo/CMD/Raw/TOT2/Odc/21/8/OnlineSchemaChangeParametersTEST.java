import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

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
        parameters.setSqlContent("ALTER TABLE test ADD COLUMN id INT;");

        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameters
                .generateSubTaskParameters(new ConnectionConfig(), "schema");

        assertNotNull(subTaskParameters);
        assertEquals(1, subTaskParameters.size());

        OnlineSchemaChangeScheduleTaskParameters subTaskParameter = subTaskParameters.get(0);
        assertNotNull(subTaskParameter);
        assertEquals("test", subTaskParameter.getOriginTableName());
        assertEquals("schema", subTaskParameter.getDatabaseName());
    }

    @Test
    public void testGenerateSubTaskParametersWithAlterType() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlContent("ALTER TABLE test RENAME TO new_test;");

        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameters
                .generateSubTaskParameters(new ConnectionConfig(), "schema");

        assertNotNull(subTaskParameters);
        assertEquals(1, subTaskParameters.size());

        OnlineSchemaChangeScheduleTaskParameters subTaskParameter = subTaskParameters.get(0);
        assertNotNull(subTaskParameter);
        assertEquals("test", subTaskParameter.getOriginTableName());
        assertEquals("schema", subTaskParameter.getDatabaseName());
        assertEquals(1, subTaskParameter.getSqlsToBeExecuted().size());
    }

    @Test
    public void testGenerateSubTaskParametersException() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlContent("INVALID SQL");

        try {
            parameters.generateSubTaskParameters(new ConnectionConfig(), "schema");
            fail("Exception expected");
        } catch (Exception e) {
            assertNotNull(e);
        }
    }
}