package com.oceanbase.odc.service.onlineschemachange.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.core.flow.model.TaskParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;

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

    @Test
    public void testGenerateSubTaskParameters() {
        ConnectionConfig config = new ConnectionConfig();
        config.setDialectType(ConnectionConfig.DialectType.MYSQL);
        onlineSchemaChangeParameters.setSqlContent("CREATE TABLE test_table (ID INT);");
        onlineSchemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.CREATE);
        
        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = onlineSchemaChangeParameters.generateSubTaskParameters(config, "test_db");
        assertEquals(1, subTaskParameters.size());
        
        // More assertions can be added here to verify the content of subTaskParameters
    }

    // you can add test cases for the setters and getters as well
}