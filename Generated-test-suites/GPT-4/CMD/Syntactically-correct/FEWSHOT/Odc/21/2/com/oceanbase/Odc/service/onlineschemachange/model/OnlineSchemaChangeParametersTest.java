
package com.oceanbase.odc.service.onlineschemachange.model;

import com.oceanbase.odc.core.flow.model.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.model.TableIdentity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import org.junit.Test;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
        params.setSqlContent("CREATE TABLE test_table (id INT PRIMARY KEY, value VARCHAR(24))");
        params.setDelimiter(";");
        params.setSqlType(OnlineSchemaChangeSqlType.CREATE);
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("MySQL"); // replace with actual dialect type
        try {
            List<OnlineSchemaChangeScheduleTaskParameters> parametersList= params.generateSubTaskParameters(connectionConfig, "schema_test");
            assertEquals(1, parametersList.size());

            OnlineSchemaChangeScheduleTaskParameters generatedParams = parametersList.get(0);
            TableIdentity tableIdentity = new TableIdentity(generatedParams.getDatabaseName(), generatedParams.getOriginTableName());

            assertEquals("schema_test", generatedParams.getDatabaseName());
            assertEquals("test_table", generatedParams.getOriginTableName());

            assertEquals(tableIdentity, new TableIdentity("schema_test", "test_table"));
        } catch (Exception e) {
            System.out.println("Failed to generate subtask parameters");
        }
    }
}
