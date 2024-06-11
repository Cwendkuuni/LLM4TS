import org.junit.Test;
import org.junit.Assert;
import static org.mockito.Mockito.*;

public class OnlineSchemaChangeParametersTest {

    @Test
    public void isContinueOnErrorTest() {
        OnlineSchemaChangeParameters param = new OnlineSchemaChangeParameters();
        param.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        Assert.assertTrue(param.isContinueOnError());

        param.setErrorStrategy(TaskErrorStrategy.STOP);
        Assert.assertFalse(param.isContinueOnError());
    }

    @Test(expected=UnexpectedException.class)
    public void generateSubTaskParametersExceptionTest() throws Exception {
        OnlineSchemaChangeParameters param = new OnlineSchemaChangeParameters();
        ConnectionConfig connConfig = mock(ConnectionConfig.class);

        param.generateSubTaskParameters(connConfig, null);
    }

    @Test
    public void generateSubTaskParametersTest() throws Exception {
        OnlineSchemaChangeParameters param = new OnlineSchemaChangeParameters();
        ConnectionConfig connConfig = mock(ConnectionConfig.class);
        when(connConfig.getDialectType()).thenReturn(DialectType.MYSQL);
        param.setSqlContent("sql1;sql2;sql3");
        param.setDelimiter(";");
        param.setSqlType(OnlineSchemaChangeSqlType.ALTER);

        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = param.generateSubTaskParameters(connConfig, "schema1");
        Assert.assertEquals(3, subTaskParameters.size());
    }
}