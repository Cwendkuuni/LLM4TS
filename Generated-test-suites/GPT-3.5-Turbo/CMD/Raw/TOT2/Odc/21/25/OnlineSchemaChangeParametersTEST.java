import static org.junit.Assert.*;
import org.junit.Test;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;

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
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("MySQL");
        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = 
                parameters.generateSubTaskParameters(connectionConfig, "schema");
        assertEquals(1, subTaskParameters.size());
    }

}