
package com.oceanbase.odc.service.onlineschemachange.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;

public class OnlineSchemaChangeParametersTest {

    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        params.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(params.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        params.setSqlContent("ALTER TABLE table1 ...");
        params.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        params.setDelimiter(";");
        params.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("mysql");
        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParams = params.generateSubTaskParameters(connectionConfig, "db1");

        assertEquals(1, subTaskParams.size());
        assertEquals("db1", subTaskParams.get(0).getDatabaseName());
    }
}

