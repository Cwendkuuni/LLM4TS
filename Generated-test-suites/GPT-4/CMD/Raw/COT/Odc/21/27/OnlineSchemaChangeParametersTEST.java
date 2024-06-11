import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class OnlineSchemaChangeParametersTest {
    private OnlineSchemaChangeParameters parameters;

    @Before
    public void setUp() {
        parameters = new OnlineSchemaChangeParameters();
    }

    @Test
    public void testIsContinueOnError() {
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(parameters.isContinueOnError());

        parameters.setErrorStrategy(TaskErrorStrategy.STOP);
        assertFalse(parameters.isContinueOnError());
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParameters() {
        parameters.setSqlContent("invalid");
        ConnectionConfig cc = new ConnectionConfig();
        cc.setDialectType("mysql");

        parameters.generateSubTaskParameters(cc, "schema");
    }
}