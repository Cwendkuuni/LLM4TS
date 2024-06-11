import com.oceanbase.odc.core.flow.model.TaskParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.model.TableIdentity;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.model.*;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;
import com.oceanbase.odc.service.common.util.SqlUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import java.util.List;
import java.util.Map;

public class OnlineSchemaChangeParametersTest {

    private OnlineSchemaChangeParameters params;
    private ConnectionConfig connectionConfig;
    private final String SCHEMA_NAME = "testSchema";

    @Before
    public void setup() {
        params = new OnlineSchemaChangeParameters();
        connectionConfig = mock(ConnectionConfig.class);
    }

    @Test
    public void testIsContinueOnError() {
        params.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(params.isContinueOnError());

        params.setErrorStrategy(TaskErrorStrategy.STOP);
        assertFalse(params.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() {
        params.setSqlContent("CREATE TABLE `test` ( `id` INT NOT NULL );");
        params.setSqlType(OnlineSchemaChangeSqlType.CREATE);

        Mockito.when(connectionConfig.getDialectType()).thenReturn(DialectType.MYSQL);

        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParams;
        try {
            subTaskParams = params.generateSubTaskParameters(connectionConfig, SCHEMA_NAME);
            assertNotNull(subTaskParams);
            assertFalse(subTaskParams.isEmpty());

            // Validate first sub-task parameter
            OnlineSchemaChangeScheduleTaskParameters subTaskParam = subTaskParams.get(0);
            assertEquals(SCHEMA_NAME, subTaskParam.getDatabaseName());
            assertEquals("test", subTaskParam.getOriginTableName());

        } catch (Exception e) {
            fail("Exception should not have been thrown.");
        }
    }

    @Test(expected = Exception.class) 
    public void testGenerateSubTaskParameters_throwsException() {
        params.setSqlContent(null);
        params.setSqlType(OnlineSchemaChangeSqlType.CREATE);
        params.generateSubTaskParameters(connectionConfig, SCHEMA_NAME);
    }
}