
package com.oceanbase.odc.service.onlineschemachange.model;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.oceanbase.odc.core.flow.model.TaskParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.service.common.util.SqlUtils;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import java.util.List;
import java.util.Arrays;

public class OnlineSchemaChangeParametersTest {

    private OnlineSchemaChangeParameters onlineSchemaChangeParams;
    private ConnectionConfig connectionConfig;

    @Before
    public void setup() {
        onlineSchemaChangeParams = new OnlineSchemaChangeParameters();
        connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("SQL");
    }

    @Test
    public void testIsContinueOnError() {
        onlineSchemaChangeParams.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(onlineSchemaChangeParams.isContinueOnError());
    }

    @Test
    public void testIsNotContinueOnError() {
        onlineSchemaChangeParams.setErrorStrategy(TaskErrorStrategy.IMMEDIATE_EXIT);
        assertFalse(onlineSchemaChangeParams.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() {
        onlineSchemaChangeParams.setSqlContent("CREATE TABLE test (id int PRIMARY KEY, name varchar(255));");
        onlineSchemaChangeParams.setSqlType(OnlineSchemaChangeSqlType.CREATE);
        
        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = onlineSchemaChangeParams.generateSubTaskParameters(connectionConfig, "schema");

        assertNotNull(subTaskParameters);
        assertFalse(subTaskParameters.isEmpty());
        assertEquals(1, subTaskParameters.size());
        assertEquals("test", subTaskParameters.get(0).getOriginTableName());
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParametersUnsuccessful() {
        onlineSchemaChangeParams.setSqlContent(null);
        onlineSchemaChangeParams.generateSubTaskParameters(connectionConfig, "schema");
    }

}
