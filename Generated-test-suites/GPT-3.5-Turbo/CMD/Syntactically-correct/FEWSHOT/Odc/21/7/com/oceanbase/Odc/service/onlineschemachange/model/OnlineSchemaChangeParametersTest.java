package com.oceanbase.odc.service.onlineschemachange.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.subtask.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;

public class OnlineSchemaChangeParametersTest {

    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(parameters.isContinueOnError());

        parameters.setErrorStrategy(TaskErrorStrategy.ABORT);
        assertFalse(parameters.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlContent("ALTER TABLE test ADD COLUMN id INT;");
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters.setDelimiter(";");

        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("mysql");

        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameters
                .generateSubTaskParameters(connectionConfig, "test_schema");

        assertEquals(1, subTaskParameters.size());
        OnlineSchemaChangeScheduleTaskParameters subTaskParameter = subTaskParameters.get(0);
        assertEquals("test_schema", subTaskParameter.getDatabaseName());
        assertEquals("test", subTaskParameter.getOriginTableName());
        assertEquals(1, subTaskParameter.getSqlsToBeExecuted().size());
        assertEquals("ALTER TABLE test ADD COLUMN id INT;", subTaskParameter.getSqlsToBeExecuted().get(0));

        parameters.setSqlType(OnlineSchemaChangeSqlType.CREATE);
        
        subTaskParameters = parameters.generateSubTaskParameters(connectionConfig, "test_schema");
        assertEquals(1, subTaskParameters.size());
        subTaskParameter = subTaskParameters.get(0);
        assertEquals(0, subTaskParameter.getSqlsToBeExecuted().size());
    }

}