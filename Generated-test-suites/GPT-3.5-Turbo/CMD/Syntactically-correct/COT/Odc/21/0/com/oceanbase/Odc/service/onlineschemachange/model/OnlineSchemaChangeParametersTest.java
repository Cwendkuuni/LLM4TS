
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.Test;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.model.TableIdentity;
import com.oceanbase.odc.core.flow.model.TaskParameters;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;

public class OnlineSchemaChangeParametersTest {
    
    private ConnectionConfig connectionConfig;
    private OnlineSchemaChangeParameters parameters;
    
    @Test
    public void testIsContinueOnError() {
        parameters = new OnlineSchemaChangeParameters();
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(parameters.isContinueOnError());
        
        parameters.setErrorStrategy(TaskErrorStrategy.FAIL_FAST);
        assertFalse(parameters.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() {
        parameters = new OnlineSchemaChangeParameters();
        
        connectionConfig = mock(ConnectionConfig.class);
        when(connectionConfig.getDialectType()).thenReturn("MySQL");
        
        parameters.setSqlContent("ALTER TABLE test_table ADD COLUMN new_col INT;");
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters.setDelimiter(";");
        
        List<String> sqls = DdlUtils.split(connectionConfig.getDialectType(), parameters.getSqlContent(), parameters.getDelimiter());
        TaskParameters taskParameters = parameters.generateSubTaskParameters(connectionConfig, "test_schema").get(0);
        
        TableIdentity expectedTableIdentity = new TableIdentity("test_schema", "test_table");
        assertEquals(expectedTableIdentity, new TableIdentity(taskParameters.getDatabaseName(), taskParameters.getOriginTableName()));
        
        List<String> expectedSqls = Collections.singletonList("ALTER TABLE test_table ADD COLUMN new_col INT;");
        assertEquals(expectedSqls, taskParameters.getSqlsToBeExecuted());
    }
}
