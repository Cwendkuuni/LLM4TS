import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class OnlineSchemaChangeParametersTest {

    private OnlineSchemaChangeParameters parameters;
    private ConnectionConfig connectionConfig;
    private String schema;

    @Before
    public void setUp() {
        parameters = new OnlineSchemaChangeParameters();
        connectionConfig = Mockito.mock(ConnectionConfig.class);
        schema = "testSchema";
    }

    @Test
    public void testIsContinueOnError() {
        // Expert 1:
        // Given
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);

        // When
        boolean result = parameters.isContinueOnError();

        // Then
        assertTrue(result);
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParametersShouldThrowException() {
        // Expert 2:
        // Given
        Mockito.when(connectionConfig.getDialectType()).thenReturn(DialectType.ORACLE);
        parameters.setSqlContent("wrong-sql");

        // When
        parameters.generateSubTaskParameters(connectionConfig, schema);
    }

}