import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.hamcrest.CoreMatchers.instanceOf;

import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;

public class OnlineSchemaChangeParametersTest {

    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertEquals(parameters.isContinueOnError(), true);
        parameters.setErrorStrategy(TaskErrorStrategy.OTHER);
        assertEquals(parameters.isContinueOnError(), false);
    }

    @Test
    public void testGenerateSubTaskParameters() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlContent("sql content;;;");
        parameters.setDelimiter(";");
        ConnectionConfig config = new ConnectionConfig();
        config.setDialectType(DialectType.MYSQL);
        try {
            parameters.generateSubTaskParameters(config, "schema");
        } catch(Exception e) {
            fail("Should not have thrown any exception");
        }
    }

    @Test
    public void testGenerateSubTaskParametersWithInvalidSqlContent() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlContent("");
        parameters.setDelimiter(";");
        ConnectionConfig config = new ConnectionConfig();
        config.setDialectType(DialectType.MYSQL);
        try {
            parameters.generateSubTaskParameters(config, "schema");
            fail("Expected an UnexpectedException to be thrown");
        } catch (Exception e) {
            assertThat(e, instanceOf(UnexpectedException.class));
        }
    }
}