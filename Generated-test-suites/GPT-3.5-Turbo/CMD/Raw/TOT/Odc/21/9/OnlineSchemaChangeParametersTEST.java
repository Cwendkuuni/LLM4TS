import static org.junit.Assert.*;
import org.junit.Test;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.subtask.OriginTableCleanStrategy;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;

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
        parameters.setDelimiter(";");
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        parameters.setLockTableTimeOutSeconds(30);
        parameters.setSwapTableNameRetryTimes(3);
        parameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.DROP);

        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("mysql");
        String schema = "test_schema";

        try {
            parameters.generateSubTaskParameters(connectionConfig, schema);
        } catch (UnexpectedException e) {
            fail("UnexpectedException thrown while generating subtask parameters");
        }
    }

}