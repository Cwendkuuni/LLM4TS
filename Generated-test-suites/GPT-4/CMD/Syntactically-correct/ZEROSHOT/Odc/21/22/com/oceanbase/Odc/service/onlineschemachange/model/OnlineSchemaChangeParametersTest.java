
package com.oceanbase.odc.service.onlineschemachange.model;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import org.junit.Test;
import org.junit.Before;

public class OnlineSchemaChangeParametersTest {

    private OnlineSchemaChangeParameters onlineSchemaChangeParameters;

    @Before
    public void setup() {
        onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
    }

    @Test
    public void isContinueOnErrorTest() {
        onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(onlineSchemaChangeParameters.isContinueOnError());

        onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.DEFAULT);
        assertFalse(onlineSchemaChangeParameters.isContinueOnError());
    }

    @Test
    public void generateSubTaskParametersTest() {
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);

        onlineSchemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        onlineSchemaChangeParameters.setSqlContent("Test SQL content");
        onlineSchemaChangeParameters.setDelimiter(";");

        List<OnlineSchemaChangeScheduleTaskParameters> taskParameters 
            = onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, "TestSchema");

        assertFalse(taskParameters.isEmpty());

        //Here, you can perform additional assertions to verify the contents of the returned `taskParameters` list.
        //This depends on the logic of your `SubTaskParameterFactory.generate` method, 
        //`DdlUtils.replaceTableName` method and any other method that affects the structure 
        //and content of `taskParameters`.
    }

    // create other unit tests for remaining getters and setters
}
