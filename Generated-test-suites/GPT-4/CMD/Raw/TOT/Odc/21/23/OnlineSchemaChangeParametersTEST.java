import static org.junit.Assert.*;

import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

public class OnlineSchemaChangeParametersTest {

    @Mock
    private ConnectionConfig mockConnectionConfig;
    
    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(parameters.isContinueOnError());

        parameters.setErrorStrategy(TaskErrorStrategy.INTERRUPT);
        assertFalse(parameters.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlContent("CREATE TABLE IF NOT EXISTS tableTest;");
        parameters.setSqlType(OnlineSchemaChangeSqlType.CREATE);

        MockitoAnnotations.openMocks(this);

        List<OnlineSchemaChangeScheduleTaskParameters> result = 
                              parameters.generateSubTaskParameters(mockConnectionConfig, "TestScheme");
        assertEquals(1, result.size());

        // Add more assertions to validate the returned OnlineSchemaChangeScheduleTaskParameters objects.
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParameters_ExceptionFlow() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlContent("INVALID SQL STATEMENT;"); // This should cause UnexpectedException.
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);

        MockitoAnnotations.openMocks(this);

        parameters.generateSubTaskParameters(mockConnectionConfig, "TestScheme");
        // Assertion is not needed because we expect an exception to be thrown.
    }
}