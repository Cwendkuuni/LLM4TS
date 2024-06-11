package com.oceanbase.odc.service.onlineschemachange.model;

import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
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
    public void isContinueOnErrorTest() {
        parameters.setErrorStrategy(TaskErrorStrategy.SKIP);
        assertFalse(parameters.isContinueOnError());

        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(parameters.isContinueOnError());
    }

    @Test(expected = UnexpectedException.class)
    public void generateSubTaskParametersExceptionTest() {

        ConnectionConfig connectionConfig = new ConnectionConfig();
        // set faulty parameters to force exception
        connectionConfig.setDialectType(null);
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters.setSqlContent(null);
        
        parameters.generateSubTaskParameters(connectionConfig, "testSchema");
    }

    @Test
    public void generateSubTaskParametersTest() {
        // set required parameters for successful execution
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("someDialect");
        
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters.setSqlContent("someSqlContent");

        List<OnlineSchemaChangeScheduleTaskParameters> taskParameters = parameters.generateSubTaskParameters(connectionConfig, "testSchema");

        assertNotNull(taskParameters);
        assertFalse(taskParameters.isEmpty());
    }
}