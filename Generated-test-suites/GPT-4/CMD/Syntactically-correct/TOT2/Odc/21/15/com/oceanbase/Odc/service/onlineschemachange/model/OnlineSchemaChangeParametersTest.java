
package com.oceanbase.odc.service.onlineschemachange.model;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;

import java.util.List;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;

public class OnlineSchemaChangeParametersTest {

    OnlineSchemaChangeParameters onlineSchemaChangeParameters;
    ConnectionConfig connectionConfig;

    @Before
    public void setUp() {
        onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
        connectionConfig = new ConnectionConfig();
    }

    @Test
    public void testIsContinueOnError() {
        onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(onlineSchemaChangeParameters.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() {
        // Assuming schema name is "test"
        String schema = "test";

        // Assuming sqlContent is a valid and correct SQL statement
        onlineSchemaChangeParameters.setSqlContent("INSERT INTO test (col1, col2) VALUES (1, 2);");

        List<OnlineSchemaChangeScheduleTaskParameters> parameters = onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, schema);

        // Check if parameters are generated correctly.
        // Perform assertions like assertNotNull(parameters);
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParametersException() {
        // Assuming schema name is empty or null
        String schema = "";

        onlineSchemaChangeParameters.setSqlContent("INSERT INTO test (col1, col2) VALUES (1, 2);");

        onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, schema);
    }
}
