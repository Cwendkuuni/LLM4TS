package com.oceanbase.odc.service.onlineschemachange.model;

import com.oceanbase.odc.core.flow.model.TaskParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class OnlineSchemaChangeParametersTest {

    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters param = new OnlineSchemaChangeParameters();
        param.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(param.isContinueOnError());

        param.setErrorStrategy(TaskErrorStrategy.STOP);
        assertFalse(param.isContinueOnError());

        param.setErrorStrategy(null);
        assertFalse(param.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() {
        // prepare test data
        OnlineSchemaChangeParameters param = new OnlineSchemaChangeParameters();
        param.setSqlType(OnlineSchemaChangeSqlType.CREATE);
        param.setSqlContent("CREATE TABLE test (id INT PRIMARY KEY, name VARCHAR(20));");
        param.setDelimiter(";");
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("MySQL");
        String schema = "test_schema";

        // test function
        List<OnlineSchemaChangeScheduleTaskParameters> list = param.generateSubTaskParameters(connectionConfig, schema);
        
        // verify the return value
        assertEquals(1, list.size());
        OnlineSchemaChangeScheduleTaskParameters result = list.get(0);
        assertEquals(schema, result.getDatabaseName());
        assertEquals("test", result.getOriginTableName());
        assertEquals("CREATE", result.getSqlsToBeExecuted().get(0));
    }

    //TODO: Add other test cases here based on the properties and methods of the OnlineSchemaChangeParameters class
}