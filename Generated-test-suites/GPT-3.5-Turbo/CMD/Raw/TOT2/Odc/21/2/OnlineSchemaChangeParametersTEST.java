import org.junit.Test;
import static org.junit.Assert.*;
import com.oceanbase.odc.service.onlineschemachange.model.*;

public class OnlineSchemaChangeParametersTest {

    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        params.setErrorStrategy(TaskErrorStrategy.CONTINUE);

        assertTrue(params.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        params.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        params.setSqlContent("ALTER TABLE test_table ADD COLUMN new_column INT;");

        // Mock connectionConfig and schema for testing
        ConnectionConfig connectionConfig = new ConnectionConfig();
        String schema = "schema";

        List<OnlineSchemaChangeScheduleTaskParameters> result = params.generateSubTaskParameters(connectionConfig, schema);

        assertEquals(1, result.size);
    }
}