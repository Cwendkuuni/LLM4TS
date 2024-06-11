package com.oceanbase.odc.service.onlineschemachange.model;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class OnlineSchemaChangeParametersTest {

    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        params.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(params.isContinueOnError());
        params.setErrorStrategy(TaskErrorStrategy.FAIL);
        assertFalse(params.isContinueOnError());
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParametersException() {
        ConnectionConfig config = new ConnectionConfig();
        config.setDialectType("InvalidDialect");
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        params.setSqlContent("SELECT * FROM test_table");
        params.generateSubTaskParameters(config, "test_schema");
    }

    @Test
    public void testGenerateSubTaskParameters() {
        ConnectionConfig config = new ConnectionConfig();
        config.setDialectType("MySQL");
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        params.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        params.setSqlContent("ALTER TABLE test_table ADD COLUMN test_column varchar(255)");
        List<OnlineSchemaChangeScheduleTaskParameters> parameters = params.generateSubTaskParameters(config, "test_schema");
        
        // Asserts to be written here based on the expected parameter values and tasks created.
    }
}