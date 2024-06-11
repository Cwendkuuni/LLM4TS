package com.oceanbase.odc.service.onlineschemachange.model;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;
import org.junit.Before;
import org.junit.Test;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;

public class OnlineSchemaChangeParametersTest {

    private OnlineSchemaChangeParameters parameters;
    private ConnectionConfig connectionConfig;

    @Before
    public void setUp() {
        parameters = new OnlineSchemaChangeParameters();
        connectionConfig = mock(ConnectionConfig.class);
    }

    @Test
    public void testIsContinueOnErrorWhenErrorStrategyIsContinue() {
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertEquals(true, parameters.isContinueOnError());
    }

    @Test
    public void testIsContinueOnErrorWhenErrorStrategyIsNotContinue() {
        parameters.setErrorStrategy(TaskErrorStrategy.ABORT);
        assertEquals(false, parameters.isContinueOnError());
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParametersWithException() {
        parameters.setSqlContent("DROP TABLE nonExistTable");
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters.generateSubTaskParameters(connectionConfig, "mySchema");
    }

    // other test cases such as edge cases and error scenarios here...
}