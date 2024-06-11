package com.oceanbase.odc.service.onlineschemachange.model;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.service.onlineschemachange.model.*;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

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
        parameters.setSqlContent("content");
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters.setDelimiter(";");

        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("type");

        try(SubTaskParameterFactory subTaskParameterFactory = new SubTaskParameterFactory(connectionConfig, "schema")) {
            List<OnlineSchemaChangeScheduleTaskParameters> onlineSchemaChangeScheduleTaskParameters = parameters.generateSubTaskParameters(connectionConfig, "schema");
            
            for(OnlineSchemaChangeScheduleTaskParameters taskParameters : onlineSchemaChangeScheduleTaskParameters){
                assertNotNull(taskParameters.getDatabaseName());
                assertNotNull(taskParameters.getOriginTableName());
                assertTrue(taskParameters.getSqlsToBeExecuted() != null && !taskParameters.getSqlsToBeExecuted().isEmpty());
            }
        } catch (Exception e) {
            fail("Test case failed: " + e.getMessage());
        }
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParametersWithException() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlContent(null);
        parameters.setDelimiter(null);

        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(null);

        parameters.generateSubTaskParameters(connectionConfig, "schema");
    }

    // More methods to test setters and getters of all parameters can be put here following the same pattern
}