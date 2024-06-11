package com.oceanbase.odc.service.onlineschemachange.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import com.oceanbase.odc.core.flow.model.TaskParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;

public class OnlineSchemaChangeParametersTest {

    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(parameters.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlContent("CREATE TABLE test_table (id INT);");
        parameters.setSqlType(OnlineSchemaChangeSqlType.CREATE);

        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("oracle");
        String schema = "test_schema";

        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameters.generateSubTaskParameters(connectionConfig, schema);

        assertEquals(1, subTaskParameters.size());
        assertEquals("test_schema", subTaskParameters.get(0).getDatabaseName());
        assertEquals("test_table", subTaskParameters.get(0).getOriginTableName());
        assertEquals(1, subTaskParameters.get(0).getSqlsToBeExecuted().size());
        assertEquals("CREATE TABLE test_table (id INT);", subTaskParameters.get(0).getSqlsToBeExecuted().get(0));
    }

}