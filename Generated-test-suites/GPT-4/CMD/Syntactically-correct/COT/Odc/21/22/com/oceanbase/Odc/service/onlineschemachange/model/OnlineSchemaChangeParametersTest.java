
package com.oceanbase.odc.service.onlineschemachange.model;

import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class OnlineSchemaChangeParametersTest {

    private OnlineSchemaChangeParameters onlineSchemaChangeParameters;

    @Before
    public void setUp() {
        onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
    }

    @Test
    public void testIsContinueOnError() {
        onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(onlineSchemaChangeParameters.isContinueOnError());

        onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.STOP);
        assertFalse(onlineSchemaChangeParameters.isContinueOnError());
    }


    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParametersException() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        String schema = "test";
        onlineSchemaChangeParameters.setSqlContent("INVALID SQL");
        onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, schema);
    }

    @Test
    public void testGenerateSubTaskParameters() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        String schema = "test";
        onlineSchemaChangeParameters.setSqlContent("VALID SQL");
        List<OnlineSchemaChangeScheduleTaskParameters> result = onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, schema);

        assertNotNull(result);
        assertEquals(1, result.size());
    }   
}
