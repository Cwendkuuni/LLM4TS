
package com.oceanbase.odc.service.onlineschemachange.model;

import com.oceanbase.odc.core.flow.model.TaskParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.ddl.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.onlineschemachange.ddl.OriginTableCleanStrategy;
import com.oceanbase.odc.service.onlineschemachange.ddl.SwapTableType;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Collections;

public class OnlineSchemaChangeParametersTest {

    @Test
    public void isContinueOnErrorTest() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(parameters.isContinueOnError());

        parameters.setErrorStrategy(TaskErrorStrategy.STOP);
        assertFalse(parameters.isContinueOnError());
    }

    @Test
    public void generateSubTaskParametersTest() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("mysql");
        String schema = "db_schema";

        String testSqlContent = "CREATE TABLE `test` (`id` int not null);";

        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlContent(testSqlContent);
        parameters.setSqlType(OnlineSchemaChangeSqlType.CREATE);

        try {
            parameters.generateSubTaskParameters(connectionConfig, schema);
        } catch (Exception e) {
            fail("Failed to generate subTaskParameters: " + e.getMessage());
        }
    }
}
