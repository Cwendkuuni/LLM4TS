import static org.junit.Assert.*;
import org.junit.Test;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.core.shared.model.TableIdentity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;

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
        params.setSqlContent("ALTER TABLE test_table ADD column1 INT;");
        params.setErrorStrategy(TaskErrorStrategy.STOP);

        ConnectionConfig connectionConfig = new ConnectionConfig("localhost", "user", "password", "dbName", "mysql");
        String schema = "test_schema";

        assertEquals(1, params.generateSubTaskParameters(connectionConfig, schema).size());
    }

    @Test
    public void testGenerateSubTaskParametersWithInvalidSQL() {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        params.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        params.setSqlContent("INVALID SQL STATEMENT");
        params.setErrorStrategy(TaskErrorStrategy.CONTINUE);

        ConnectionConfig connectionConfig = new ConnectionConfig("localhost", "user", "password", "dbName", "mysql");
        String schema = "test_schema";

        assertTrue(params.generateSubTaskParameters(connectionConfig, schema).isEmpty());
    }

    // Add more test cases for edge scenarios and error scenarios

}