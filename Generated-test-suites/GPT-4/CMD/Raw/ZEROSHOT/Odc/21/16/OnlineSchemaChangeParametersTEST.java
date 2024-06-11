
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.onlineschemachange.model.OriginTableCleanStrategy;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import org.junit.Before;
import org.junit.Test;
import java.util.List;
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
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("MySQL");
        parameters.setSqlContent("CREATE TABLE test_table(id INT);");
        parameters.setSqlType(OnlineSchemaChangeSqlType.CREATE); 
        parameters.generateSubTaskParameters(connectionConfig, "schema");
    }

    @Test
    public void testAccessors() {
        parameters.setSqlType(OnlineSchemaChangeSqlType.CREATE);
        assertEquals(OnlineSchemaChangeSqlType.CREATE, parameters.getSqlType());

        parameters.setDelimiter(";");
        assertEquals(";", parameters.getDelimiter());

        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertEquals(TaskErrorStrategy.CONTINUE, parameters.getErrorStrategy());

        parameters.setLockTableTimeOutSeconds(10);
        assertEquals((Integer) 10, parameters.getLockTableTimeOutSeconds());

        parameters.setSqlContent("CREATE TABLE test_table(id INT);");
        assertEquals("CREATE TABLE test_table(id INT);", parameters.getSqlContent());

        parameters.setSwapTableNameRetryTimes(3);
        assertEquals((Integer) 3, parameters.getSwapTableNameRetryTimes());

        parameters.setSwapTableType(SwapTableType.SWAP_TABLE);
        assertEquals(SwapTableType.SWAP_TABLE, parameters.getSwapTableType());

        parameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.DROP_TABLE);
        assertEquals(OriginTableCleanStrategy.DROP_TABLE, parameters.getOriginTableCleanStrategy());
    }

}
