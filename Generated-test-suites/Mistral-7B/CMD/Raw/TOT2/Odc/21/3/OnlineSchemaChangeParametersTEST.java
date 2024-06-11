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

import com.oceanbase.odc.core.connection.model.ConnectionConfig;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.service.common.util.SqlUtils;
import com.oceanbase.odc.service.connection.model.SubTaskParameterFactory;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.subtask.SwapTableType;

public class OnlineSchemaChangeParametersTest {

    private ConnectionConfig connectionConfig;
    private OnlineSchemaChangeParameters onlineSchemaChangeParameters;

    @Before
    public void setUp() {
        connectionConfig = Mockito.mock(ConnectionConfig.class);
        onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
    }

    @Test
    public void testIsContinueOnError() {
        onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(onlineSchemaChangeParameters.isContinueOnError());

        onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.ABORT);
        assertFalse(onlineSchemaChangeParameters.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParametersHappyPath() {
        String schema = "testSchema";
        String sqlContent = "CREATE TABLE testTable (id INT PRIMARY KEY);";
        OnlineSchemaChangeSqlType sqlType = OnlineSchemaChangeSqlType.CREATE;

        when(connectionConfig.getDialectType()).thenReturn("mysql");

        onlineSchemaChangeParameters.setSqlType(sqlType);
        onlineSchemaChangeParameters.setSqlContent(sqlContent);
        onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        onlineSchemaChangeParameters.setLockTableTimeOutSeconds(30);
        onlineSchemaChangeParameters.setSwapTableType(SwapTableType.SWAP);

        List<OnlineSchemaChangeScheduleTaskParameters> expectedTaskParameters = new ArrayList<>();
        OnlineSchemaChangeScheduleTaskParameters expectedTaskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        expectedTaskParameter.setDatabaseName("testSchema");
        expectedTaskParameter.setOriginTableName("testTable");
        expectedTaskParameter.setNewTableName("testTable_new");
        expectedTaskParameter.setSqlsToBeExecuted(Arrays.asList(sqlContent));
        expectedTaskParameters.add(expectedTaskParameter);

        List<OnlineSchemaChangeScheduleTaskParameters> actualTaskParameters = onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, schema);

        assertEquals(expectedTaskParameters, actualTaskParameters);
        verify(connectionConfig).getDialectType();
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParametersException() {
        when(connectionConfig.getDialectType()).thenThrow(new RuntimeException("ConnectionError"));

        OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
        onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        onlineSchemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.CREATE);
        onlineSchemaChangeParameters.setSqlContent("CREATE TABLE testTable (id INT PRIMARY KEY);");
        onlineSchemaChangeParameters.setLockTableTimeOutSeconds(30);

        onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, "testSchema");
    }
}