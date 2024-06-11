import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Arrays;
import java.util.List;
import com.oceanbase.odc.core.flow.model.TaskErrorStrategy;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;

public class OnlineSchemaChangeParametersTest {

    OnlineSchemaChangeParameters parameters;

    @Test
    public void testIsContinueOnError() {
        parameters = new OnlineSchemaChangeParameters();
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(parameters.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("MySQL");
        parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters.setSqlContent("ALTER TABLE tableName ADD COLUMN column1 INT;");
        parameters.setDelimiter(";");
        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameters.generateSubTaskParameters(connectionConfig, "schema");
        assertEquals(1, subTaskParameters.size());
    }

    // Additional test cases for edge cases and exceptions can be added here

}