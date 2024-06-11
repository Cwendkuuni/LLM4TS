
import org.junit.Assert;
import org.junit.Test;

public class OnlineSchemaChangeParametersTest {

    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        Assert.assertTrue(parameters.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters(){
        //.. Add code to setup connectionConfig, and schema
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        //.. Add respective setup for parameters object
        List<OnlineSchemaChangeScheduleTaskParameters> taskParameters = parameters.generateSubTaskParameters(connectionConfig, schema);
        //.. Add code to Assert necessary conditions. Here, actual conditions to assert is ambiguous
    }

    @Test
    public void testIsContinueOnErrorWhenErrorStrategyIsNotContinue() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setErrorStrategy(TaskErrorStrategy.SKIP);
        Assert.assertFalse(parameters.isContinueOnError());
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParametersExceptionThrown(){
        //.. Add code to setup connectionConfig, and schema
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        //.. Add respective setup for parameters object such that exception will be thrown
        parameters.generateSubTaskParameters(connectionConfig, schema);
    }        
}
