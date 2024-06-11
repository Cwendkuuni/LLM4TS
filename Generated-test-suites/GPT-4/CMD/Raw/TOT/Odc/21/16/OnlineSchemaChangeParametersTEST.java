import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import com.oceanbase.odc.service.onlineschemachange.model.*;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import static org.junit.jupiter.api.Assertions.*;

public class OnlineSchemaChangeParametersTest {
    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        params.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(params.isContinueOnError());

        params.setErrorStrategy(TaskErrorStrategy.STOP);
        assertFalse(params.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        ConnectionConfig config = Mockito.mock(ConnectionConfig.class);
        String schema = "schema";
        
        params.setSqlContent("SELECT * FROM table;");
        params.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        
        List<OnlineSchemaChangeScheduleTaskParameters> result = params.generateSubTaskParameters(config, schema);
        assertEquals(1, result.size());
    }
    
    @Test
    public void testSwapTableNameRetryTimes() {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        params.setSwapTableNameRetryTimes(7);
        assertEquals(7, params.getSwapTableNameRetryTimes().intValue());
        
        params.setSwapTableNameRetryTimes(-4);
        assertEquals(-4, params.getSwapTableNameRetryTimes().intValue());
    }
    
    @Test
    public void testLockTableTimeOutSeconds() {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        params.setLockTableTimeOutSeconds(13);
        assertEquals(13, params.getLockTableTimeOutSeconds().intValue());
        
        params.setLockTableTimeOutSeconds(-7);
        assertEquals(-7, params.getLockTableTimeOutSeconds().intValue());
    }
}