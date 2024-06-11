import static org.junit.Assert.*;
import org.junit.Test;
import com.oceanbase.odc.service.onlineschemachange.model.*;

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
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters.setSqlContent("ALTER TABLE test ADD COLUMN id INT;");
        parameters.setDelimeter(";");

        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("MySQL");
        String schema = "public";

        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameters.generateSubTaskParameters(connectionConfig, schema);

        assertNotNull(subTaskParameters);
        assertEquals(1, subTaskParameters.size());
    }

    // Add more test cases for other methods as needed

}