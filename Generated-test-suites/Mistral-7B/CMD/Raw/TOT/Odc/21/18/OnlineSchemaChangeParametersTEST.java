import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.core.connection.ConnectionConfig;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.core.shared.model.TableIdentity;
import com.oceanbase.odc.service.common.util.SqlUtils;
import com.oceanbase.odc.service.connection.model.ConnectionConfigFactory;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.onlineschemachange.SubTaskParameterFactory;

public class OnlineSchemaChangeParametersTest {

    private ConnectionConfig connectionConfig;
    private SubTaskParameterFactory subTaskParameterFactory;

    @Before
    public void setup() {
        connectionConfig = ConnectionConfigFactory.getInstance();
        subTaskParameterFactory = mock(SubTaskParameterFactory.class);
    }

    @Test
    public void testValidParameters() {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        params.setSqlType(OnlineSchemaChangeSqlType.CREATE);
        params.setSqlContent("CREATE TABLE IF NOT EXISTS test_table(id INT PRIMARY KEY);");
        params.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        params.setLockTableTimeOutSeconds(5);
        params.setSwapTableType(SwapTableType.SWAP_TABLE);

        List<OnlineSchemaChangeScheduleTaskParameters> expectedTaskParameters = new ArrayList<>();
        TableIdentity key = new TableIdentity("test_db", "test_table");
        OnlineSchemaChangeScheduleTaskParameters taskParameters = new OnlineSchemaChangeScheduleTaskParameters();
        taskParameters.setDatabaseName("test_db");
        taskParameters.setOriginTableName("test_table");
        taskParameters.setSqlsToBeExecuted(Arrays.asList("CREATE TABLE IF NOT EXISTS test_table(id INT PRIMARY KEY)",
                "ALTER TABLE test_table ADD COLUMN new_column INT;"));
        expectedTaskParameters.add(taskParameters);
        when(subTaskParameterFactory.generate(anyString(), OnlineSchemaChangeSqlType.CREATE)).thenReturn(taskParameters);

        List<OnlineSchemaChangeScheduleTaskParameters> actualTaskParameters = params.generateSubTaskParameters(connectionConfig, "test_db");

        assertEquals(expectedTaskParameters, actualTaskParameters);
        verify(subTaskParameterFactory, times(1)).generate(anyString(), OnlineSchemaChangeSqlType.CREATE);
    }

    @Test
    public void testEmptySqlContent() {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        params.setSqlType(OnlineSchemaChangeSqlType.CREATE);
        params.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        params.setLockTableTimeOutSeconds(5);
        params.setSwapTableType(SwapTableType.SWAP_TABLE);
        params.setSqlContent("");

        try {
            params.generateSubTaskParameters(connectionConfig, "test_db");
            fail("Expected UnexpectedException");
        } catch (UnexpectedException e) {
            // Expected
        }

        verifyNoMoreInteractions(subTaskParameterFactory);
    }

    @Test
    public void testInvalidSqlContent() {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        params.setSqlType(OnlineSchemaChangeSqlType.CREATE);
        params.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        params.setLockTableTimeOutSeconds(5);
        params.setSwapTableType(SwapTableType.SWAP_TABLE);
        params.setSqlContent("INVALID_SQL");

        try {
            params.generateSubTaskParameters(connectionConfig, "test_db");
            fail("Expected UnexpectedException");
        } catch (UnexpectedException e) {
            // Expected
        }

        verifyNoMoreInteractions(subTaskParameterFactory);
    }
}