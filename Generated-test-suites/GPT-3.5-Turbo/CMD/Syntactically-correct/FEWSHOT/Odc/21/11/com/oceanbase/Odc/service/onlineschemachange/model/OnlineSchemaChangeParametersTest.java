package com.oceanbase.odc.service.onlineschemachange.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.oceanbase.odc.core.flow.model.TaskParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.core.shared.model.TableIdentity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;

public class OnlineSchemaChangeParametersTest {

    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        params.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(params.isContinueOnError());

        params.setErrorStrategy(TaskErrorStrategy.STOP);
        assertFalse(params.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        params.setSqlContent("ALTER TABLE test ADD COLUMN col INT;");
        params.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        params.setDelimiter(";");

        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("mysql");

        String schema = "test_schema";

        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParams = params.generateSubTaskParameters(connectionConfig, schema);

        assertNotNull(subTaskParams);
        assertEquals(1, subTaskParams.size());

        OnlineSchemaChangeScheduleTaskParameters subTaskParam = subTaskParams.get(0);
        assertEquals("test_schema", subTaskParam.getDatabaseName());
        assertEquals("test", subTaskParam.getOriginTableName());
        assertEquals(1, subTaskParam.getSqlsToBeExecuted().size());

        // Test with invalid SQL content
        params.setSqlContent("INVALID SQL");
        UnexpectedException exception = assertThrows(UnexpectedException.class, () -> {
            params.generateSubTaskParameters(connectionConfig, schema);
        });
        assertNotNull(exception);
    }
}