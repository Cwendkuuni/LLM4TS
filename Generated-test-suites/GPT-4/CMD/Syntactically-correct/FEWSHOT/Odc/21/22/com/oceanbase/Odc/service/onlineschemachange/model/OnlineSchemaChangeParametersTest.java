package com.oceanbase.odc.service.onlineschemachange.model;

import static org.junit.Assert.*;

import java.util.List;
import java.util.ArrayList;

import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.model.TableIdentity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import org.junit.Test;

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
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("MySQL"); // set your desired Dialect type
        String schema = "testSchema";

        parameters.setSqlContent("CREATE TABLE TestTable (id INT PRIMARY KEY); DROP TABLE TestTable;");
        parameters.setDelimiter(";");
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);

        List<OnlineSchemaChangeScheduleTaskParameters> result = parameters.generateSubTaskParameters(connectionConfig, schema);

        assertEquals(2, result.size());
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParametersThrowsException() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("MySQL"); // set your desired Dialect type
        String schema = "testSchema";

        parameters.setSqlContent("INVALID SQL QUERY");
        parameters.setDelimiter(";");
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);

        parameters.generateSubTaskParameters(connectionConfig, schema);
    }
}