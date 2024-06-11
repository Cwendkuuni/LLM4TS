
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.Matchers.anyString;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.model.TableIdentity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.model.*;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

public class OnlineSchemaChangeParametersTest {
   
    @Test
    public void testIsContinueOnErrorTrue() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        Assert.assertTrue(parameters.isContinueOnError());
    }

    @Test
    public void testIsContinueOnErrorFalse() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setErrorStrategy(TaskErrorStrategy.STOP);
        Assert.assertFalse(parameters.isContinueOnError());
    }

    @Test(expected=UnexpectedException.class)
    public void testGenerateSubTaskParametersFailure() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.generateSubTaskParameters(null, "test");
    }

    @Test
    public void testGenerateSubTaskParametersSuccess() {
        ConnectionConfig connectionConfig = Mockito.mock(ConnectionConfig.class);
        Mockito.when(connectionConfig.getDialectType()).thenReturn("test");
        
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlContent("test");
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);

        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameters.generateSubTaskParameters(connectionConfig, "test");
        Assert.assertNotNull(subTaskParameters);
        for(OnlineSchemaChangeScheduleTaskParameters param: subTaskParameters) {
            Assert.assertNotNull(param.getDatabaseName());
            Assert.assertEquals("test", param.getOriginTableName());
        }
    }
}
