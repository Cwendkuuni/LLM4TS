package com.oceanbase.odc.service.onlineschemachange.model;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class OnlineSchemaChangeParametersTest extends TestCase {
    private OnlineSchemaChangeParameters online;

    @Before
    public void setUp() {
        online = new OnlineSchemaChangeParameters();
    }

    @Test
    public void testIsContinueOnError() {
        online.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertEquals(true, online.isContinueOnError());

        online.setErrorStrategy(TaskErrorStrategy.STOP);
        assertEquals(false, online.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.MYSQL);

        online.setSqlContent("SELECT * FROM emp;INSERT INTO emp (id,name) VALUES (1001, 'Steve');");
        online.setDelimiter(";");
        online.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        try {
            List<OnlineSchemaChangeScheduleTaskParameters> params = online.generateSubTaskParameters(connectionConfig, "myschema");
            assertEquals(2, params.size());
        } catch (Exception e) {
            fail("Test failed with exception: " + e.getMessage());
        }
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParametersWithException() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.MYSQL);

        online.setSqlContent("DROP DATABASE myschema");
        online.setDelimiter(";");
        online.setSqlType(OnlineSchemaChangeSqlType.ALTER);

        online.generateSubTaskParameters(connectionConfig, "myschema");
    }
}