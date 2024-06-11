
package com.oceanbase.odc.service.onlineschemachange.model;

import com.oceanbase.odc.core.flow.model.TaskParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.model.TableIdentity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.List;

public class OnlineSchemaChangeParametersTest {

    private OnlineSchemaChangeParameters instance;
    private ConnectionConfig connectionConfig;

    @Before
    public void setUp() {
        instance = new OnlineSchemaChangeParameters();
        connectionConfig = new ConnectionConfig();
    }

    @Test
    public void testIsContinueOnError() {
        instance.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(instance.isContinueOnError());

        instance.setErrorStrategy(TaskErrorStrategy.WARNING);
        assertFalse(instance.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() {
        // Provide some setup such as connectionConfig settings and sqlContent
       
        instance.setSqlType(OnlineSchemaChangeSqlType.MODIFY);
        instance.setSqlContent("REPLACE TABLE TEST");

        List<OnlineSchemaChangeScheduleTaskParameters> result 
            = instance.generateSubTaskParameters(connectionConfig, "TEST SCHEMA");

        assertNotEquals(0, result.size());

        // Check TaskParameters, we could further test if task parameters match given condition.
        // Here assuming  first task parameter generate database name and table name as expected.
        TableIdentity expectedIdentity
                = new TableIdentity("EXPECTED_DATABASE_NAME", "EXPECTED_TABLE_NAME");
        assertEquals(expectedIdentity, result.get(0).getTableIdentity());
    }

   // Similarly write tests for all other setter getter methods, 
   // and more complex methods if there are
}
