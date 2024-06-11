
package com.oceanbase.odc.service.onlineschemachange.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.service.common.util.SqlUtils;

public class OnlineSchemaChangeParametersTest {

    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(parameters.isContinueOnError());

        parameters.setErrorStrategy(TaskErrorStrategy.STOP);
        assertFalse(parameters.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlContent("ALTER TABLE test ADD COLUMN id INT;");
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);

        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("mysql");

        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameters.generateSubTaskParameters(connectionConfig, "testSchema");
        assertEquals(1, subTaskParameters.size());
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParametersException() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlContent("ALTER TABLE test ADD COLUMN id INT;");
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);

        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("invalid_dialect");

        parameters.generateSubTaskParameters(connectionConfig, "testSchema");
    }

}

