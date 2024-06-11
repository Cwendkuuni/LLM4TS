import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.core.connection.model.ConnectionConfig;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.core.shared.model.TableIdentity;
import com.oceanbase.odc.service.common.util.SqlUtils;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;
import com.oceanbase.odc.service.onlineschemachange.util.OnlineSchemaChangeScheduleTaskParameters;

/**
 * Test class for OnlineSchemaChangeParameters
 */
public class OnlineSchemaChangeParametersTest {

    private ConnectionConfig connectionConfig;

    @Before
    public void setup() {
        connectionConfig = mock(ConnectionConfig.class);
    }

    @Test
    public void testGettersAndSetters() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();

        parameters.setSqlType(OnlineSchemaChangeSqlType.ADD);
        parameters.setSqlContent("test sql content");
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        parameters.setLockTableTimeOutSeconds(10);
        parameters.setSwapTableType(SwapTableType.BACKUP);
        parameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.DROP);
        parameters.setDelimiter(";");
        parameters.setLockUsers(Arrays.asList("user1", "user2"));

        assertEquals(OnlineSchemaChangeSqlType.ADD, parameters.getSqlType());
        assertEquals("test sql content", parameters.getSqlContent());
        assertEquals(TaskErrorStrategy.CONTINUE, parameters.getErrorStrategy());
        assertEquals(10, parameters.getLockTableTimeOutSeconds());
        assertEquals(SwapTableType.BACKUP, parameters.getSwapTableType());
        assertEquals(OriginTableCleanStrategy.DROP, parameters.getOriginTableCleanStrategy());
        assertEquals(";", parameters.getDelimiter());
        assertEquals(Arrays.asList("user1", "user2"), parameters.getLockUsers());
    }

    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        assertFalse(parameters.isContinueOnError());

        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(parameters.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters_normal_scenario() throws Exception {
        // Given
        String databaseName = "test_database";
        String schemaName = "test_schema";
        String sqlContent = "CREATE TABLE test_table (id INT PRIMARY KEY);";
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlType(OnlineSchemaChangeSqlType.CREATE);
        parameters.setSqlContent(sqlContent);
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        parameters.setLockTableTimeOutSeconds(10);
        parameters.setSwapTableType(SwapTableType.BACKUP);
        parameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.DROP_TABLE);
        parameters.setDelimiter(";");
        parameters.setLockUsers(Arrays.asList("user1", "user2"));

        when(connectionConfig.getDialectType()).thenReturn("mysql");

        // When
        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameters.generateSubTaskParameters(connectionConfig,
                schemaName);

        // Then
        assertEquals(1, subTaskParameters.size());
        OnlineSchemaChangeScheduleTaskParameters taskParameter = subTaskParameters.get(0);
        assertEquals(databaseName, taskParameter.getDatabaseName());
        assertEquals(schemaName, taskParameter.getSchemaName());
        assertEquals("test_table", taskParameter.getOriginTableName());
        assertEquals("CREATE TABLE test_table (id INT PRIMARY KEY);", taskParameter.getSqlContent());
        assertEquals(OnlineSchemaChangeSqlType.CREATE, taskParameter.getSqlType());
        assertEquals(Arrays.asList("CREATE TABLE test_table (id INT PRIMARY KEY);"), taskParameter.getSqlsToBeExecuted());

        // Test with multiple SQLs
        sqlContent = "CREATE TABLE test_table1 (id INT PRIMARY KEY);;\nCREATE TABLE test_table2 (id INT PRIMARY KEY);";
        subTaskParameters = parameters.generateSubTaskParameters(connectionConfig, schemaName);
        assertEquals(2, subTaskParameters.size());
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParameters_exception_scenario() throws Exception {
        // Given
        String databaseName = "test_database";
        String schemaName = "test_schema";
        String sqlContent = "test_invalid_sql";
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlType(OnlineSchemaChangeSqlType.CREATE);
        parameters.setSqlContent(sqlContent);
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        parameters.setLockTableTimeOutSeconds(10);
        parameters.setSwapTableType(SwapTableType.BACKUP);
        parameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.DROP_TABLE);
        parameters.setDelimiter(";");
        parameters.setLockUsers(Arrays.asList("user1", "user2"));

        when(connectionConfig.getDialectType()).thenReturn("mysql");

        // When & Then
        try {
            parameters.generateSubTaskParameters(connectionConfig, schemaName);
            fail("Exception expected");
        } catch (UnexpectedException e) {
            // Expected
        }
    }
}