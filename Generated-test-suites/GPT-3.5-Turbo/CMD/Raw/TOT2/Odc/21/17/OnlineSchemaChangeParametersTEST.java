import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.service.common.util.SqlUtils;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.onlineschemachange.model.OriginTableCleanStrategy;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;

public class OnlineSchemaChangeParametersTest {

    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(parameters.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() {
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        when(connectionConfig.getDialectType()).thenReturn("MySQL");
        
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlContent("ALTER TABLE test ADD COLUMN new_column INT;");
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters.setDelimiter(";");

        SubTaskParameterFactory subTaskParameterFactory = mock(SubTaskParameterFactory.class);
        when(subTaskParameterFactory.generate("ALTER TABLE test ADD COLUMN new_column INT;", OnlineSchemaChangeSqlType.ALTER))
                .thenReturn(new OnlineSchemaChangeScheduleTaskParameters("test", "new_table"));
        
        try {
            List<OnlineSchemaChangeScheduleTaskParameters> taskParameters = parameters.generateSubTaskParameters(connectionConfig, "schema");
            assertEquals(1, taskParameters.size());
            assertEquals("test", taskParameters.get(0).getDatabaseName());
            assertEquals("new_table", taskParameters.get(0).getOriginTableName());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParameters_Exception() {
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        when(connectionConfig.getDialectType()).thenReturn("MySQL");

        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlContent("ALTER TABLE test ADD COLUMN new_column INT;");
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters.setDelimiter(";");
        
        SubTaskParameterFactory subTaskParameterFactory = mock(SubTaskParameterFactory.class);
        when(subTaskParameterFactory.generate("ALTER TABLE test ADD COLUMN new_column INT;", OnlineSchemaChangeSqlType.ALTER))
                .thenReturn(new OnlineSchemaChangeScheduleTaskParameters("test", "new_table"));
        
        SqlUtils sqlUtils = mock(SqlUtils.class);
        when(sqlUtils.split("MySQL", "ALTER TABLE test ADD COLUMN new_column INT;", ";"))
                .thenThrow(new UnexpectedException("Error splitting SQL statements"));
        
        parameters.generateSubTaskParameters(connectionConfig, "schema");
    }
}