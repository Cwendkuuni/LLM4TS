import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.core.connection.model.ConnectionConfig;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.core.shared.model.TableIdentity;
import com.oceanbase.odc.service.common.util.SqlUtils;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;

public class OnlineSchemaChangeParametersTest {

    private ConnectionConfig connectionConfig;
    private OnlineSchemaChangeParameters onlineSchemaChangeParameters;

    @Before
    public void setUp() {
        connectionConfig = Mockito.mock(ConnectionConfig.class);
        onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
        onlineSchemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        onlineSchemaChangeParameters.setSqlContent("CREATE TABLE TEST_TABLE (id INT PRIMARY KEY);");
        onlineSchemaChangeParameters.setLockTableTimeOutSeconds(20);
        onlineSchemaChangeParameters.setSwapTableNameRetryTimes(3);
    }

    @Test
    public void testIsContinueOnError() {
        assertTrue(onlineSchemaChangeParameters.isContinueOnError());
        onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.ABORT);
        assertFalse(onlineSchemaChangeParameters.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters_normal() throws Exception {
        List<OnlineSchemaChangeScheduleTaskParameters> expectedTaskParameters = new ArrayList<>();
        OnlineSchemaChangeScheduleTaskParameters expectedTaskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        expectedTaskParameter.setDatabaseName("testdb");
        expectedTaskParameter.setOriginTableName("test_table");
        expectedTaskParameter.setNewTableName("new_test_table");
        expectedTaskParameter.setSqlsToBeExecuted(new ArrayList<>());
        expectedTaskParameters.add(expectedTaskParameter);

        when(connectionConfig.getDialectType()).thenReturn("mysql");
        when(connectionConfig.getDialectType()).thenReturn("mysql"); // verify dialect type is consistent

        List<OnlineSchemaChangeScheduleTaskParameters> actualTaskParameters = onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, "testdb");

        assertEquals(expectedTaskParameters, actualTaskParameters);
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParameters_exception() throws Exception {
        when(connectionConfig.getDialectType()).thenThrow(new Exception("Test exception"));

        onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, "testdb");
    }

    @Test
    public void testGenerateSubTaskParameters_splitSqls() throws Exception {
        when(connectionConfig.getDialectType()).thenReturn("mysql");
        String sqlContent = "CREATE TABLE TABLE1 (id INT PRIMARY KEY); CREATE TABLE TABLE2 (id INT PRIMARY KEY);";
        onlineSchemaChangeParameters.setSqlContent(sqlContent);

        List<String> sqls = SqlUtils.split(connectionConfig.getDialectType(), onlineSchemaChangeParameters.getSqlContent(), onlineSchemaChangeParameters.getDelimiter());
        assertEquals(2, sqls.size());
        assertEquals("CREATE TABLE TABLE1 (id INT PRIMARY KEY);", sqls.get(0));
        assertEquals("CREATE TABLE TABLE2 (id INT PRIMARY KEY);", sqls.get(1));

        List<OnlineSchemaChangeScheduleTaskParameters> actualTaskParameters = onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, "testdb");
        assertEquals(2, actualTaskParameters.size());
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParameters_invalidSqlType() {
        onlineSchemaChangeParameters.setSqlType(null);
        onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, "testdb");
    }

}