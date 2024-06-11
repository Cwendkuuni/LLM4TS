
import com.oceanbase.odc.service.onlineschemachange.model.*;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.model.TableIdentity;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class OnlineSchemaChangeParametersTest {

    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        
        //Test default case
        Assert.assertFalse(parameters.isContinueOnError());
        
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        Assert.assertTrue(parameters.isContinueOnError());
        
        parameters.setErrorStrategy(TaskErrorStrategy.ABORT);
        Assert.assertFalse(parameters.isContinueOnError());
    }
    
    @Test
    public void testGenerateSubTaskParameters() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        ConnectionConfig connectionConfig = new ConnectionConfig();
        String schema = "test_schema";

        connectionConfig.setDialectType(ConnectionConfig.DialectType.MYSQL);
        parameters.setSqlContent("SELECT * FROM TABLE_NAME");
        
        try {
            List<OnlineSchemaChangeScheduleTaskParameters> result = parameters.generateSubTaskParameters(connectionConfig, schema);
            Assert.assertNotNull(result);
        } catch(Exception e) {
           Assert.fail(e.getMessage());
        }
    }
    
    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParametersWithException() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        ConnectionConfig connectionConfig = null; //As per your given code, not providing ConnectionConfig should through an UnexpectedException.
        String schema = "test_schema";

        parameters.setSqlContent("SELECT * FROM TABLE_NAME");
        parameters.generateSubTaskParameters(connectionConfig, schema); // This line should throw UnexpectedException
    }
    
    @Test
    public void testGetLockTableTimeOutSeconds() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        Integer timeOut = 300;
        parameters.setLockTableTimeOutSeconds(timeOut);
        Assert.assertEquals(timeOut, parameters.getLockTableTimeOutSeconds());
    }
    
    // Similar tests can be written for other getters and setters
}
