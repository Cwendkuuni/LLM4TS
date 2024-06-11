
package com.oceanbase.odc.service.onlineschemachange.model;

import com.oceanbase.odc.core.flow.model.TaskParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.model.TableIdentity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class OnlineSchemaChangeParametersTest {
    OnlineSchemaChangeParameters onlineSchemaChangeParameters;

    @Before
    public void setUp() throws Exception {
        onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
    }

    @Test
    public void testIsContinueOnError() {
        onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(onlineSchemaChangeParameters.isContinueOnError());

        onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.STOP);
        assertFalse(onlineSchemaChangeParameters.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() {
        // Mock objects
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("MySQL");

        // Set mock values
        onlineSchemaChangeParameters.setSqlContent("CREATE TABLE test_table (id INT PRIMARY KEY);");
        onlineSchemaChangeParameters.setDelimiter(";");

        List<String> mockResult = new ArrayList<>();
        mockResult.add("CREATE TABLE test_table (id INT PRIMARY KEY);");

        assertEquals(mockResult, onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, "test_db"));
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParametersException() {
        onlineSchemaChangeParameters.generateSubTaskParameters(null, null);
    }
}
