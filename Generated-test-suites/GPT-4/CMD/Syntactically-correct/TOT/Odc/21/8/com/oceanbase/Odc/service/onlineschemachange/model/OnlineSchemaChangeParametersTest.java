package com.oceanbase.odc.service.onlineschemachange.model;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;

import java.util.List;

public class OnlineSchemaChangeParametersTest {

    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters oscp = new OnlineSchemaChangeParameters();
        oscp.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(oscp.isContinueOnError());
        oscp.setErrorStrategy(TaskErrorStrategy.STOP);
        assertFalse(oscp.isContinueOnError());
    }
    
    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParametersException() {
        OnlineSchemaChangeParameters oscp = new OnlineSchemaChangeParameters();
        oscp.setSqlContent("INVALID SQL");
        oscp.setDelimiter(";");
        oscp.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        ConnectionConfig config = new ConnectionConfig();
        config.setDialectType(DialectType.MYSQL);
        
        oscp.generateSubTaskParameters(config, "schema");
    }

    @Test
    public void testGenerateSubTaskParameters() {
        OnlineSchemaChangeParameters oscp = new OnlineSchemaChangeParameters();
        oscp.setSqlContent("VALID SQL");
        oscp.setDelimiter(";");
        oscp.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        ConnectionConfig config = new ConnectionConfig();
        config.setDialectType(DialectType.MYSQL);
        
        List<OnlineSchemaChangeScheduleTaskParameters> parameters = oscp.generateSubTaskParameters(config, "schema");
        assertEquals(1, parameters.size());
    }

}