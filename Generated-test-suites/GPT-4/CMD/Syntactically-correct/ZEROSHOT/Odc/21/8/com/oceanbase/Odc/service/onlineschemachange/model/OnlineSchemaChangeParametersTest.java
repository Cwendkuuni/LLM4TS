
package com.oceanbase.odc.service.onlineschemachange.model;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.oceanbase.odc.core.flow.model.TaskParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.model.TableIdentity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;

import java.util.List;


public class OnlineSchemaChangeParametersTest {
    private OnlineSchemaChangeParameters parameters;
    
    @Before
    public void setUp() {
        parameters = new OnlineSchemaChangeParameters();
    }

    @Test
    public void testIsContinueOnError() {
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(parameters.isContinueOnError());

        parameters.setErrorStrategy(TaskErrorStrategy.EXIT);
        assertFalse(parameters.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() {
        ConnectionConfig config = new ConnectionConfig();
        config.setDialectType("MySQL");
        String schema = "testSchema";
        parameters.setSqlContent("SELECT * FROM TEST");
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);

        try {
            List<OnlineSchemaChangeScheduleTaskParameters> taskParameters = parameters.generateSubTaskParameters(config, schema);
            assertNotNull(taskParameters);
            assertTrue(taskParameters.size() > 0);
        } catch(UnexpectedException e) {
            fail("Unexpected exception: "+ e.getMessage());
        }
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParametersException() {
        ConnectionConfig config = new ConnectionConfig();
        config.setDialectType("InvalidDialectType"); // This will impose UnexpectedException.
        String schema = "testSchema";
        parameters.setSqlContent("SELECT * FROM TEST");
        parameters.setSqlType(OnlineSchemaChangeSqlType.SELECT);

        parameters.generateSubTaskParameters(config, schema); // Except UnexpectedException to be thrown here.
    }
    
}
