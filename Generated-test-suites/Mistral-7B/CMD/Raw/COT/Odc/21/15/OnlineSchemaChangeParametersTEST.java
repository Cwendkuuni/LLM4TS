import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.oceanbase.odc.core.connection.model.ConnectionConfig;
import com.oceanbase.odc.core.model.TableIdentity;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class OnlineSchemaChangeParametersTest {

    @Mock
    private SubTaskParameterFactory subTaskParameterFactory;

    @InjectMocks
    private OnlineSchemaChangeParameters onlineSchemaChangeParameters;

    @Test
    public void testGetIsContinueOnError() {
        // Given
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);

        // When
        boolean actual = parameters.isContinueOnError();

        // Then
        assertTrue(actual);
    }

    @Test
    public void testGenerateSubTaskParameters_success() throws Exception {
        // Given
        String schema = "test_schema";
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        String sqlContent = "create table test_table (id int primary key); ALTER TABLE test_table ADD COLUMN new_column int;";
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlType(OnlineSchemaChangeSqlType.CREATE_AND_ALTER);
        parameters.setSqlContent(sqlContent);

        when(subTaskParameterFactory.generate(anyString(), any(OnlineSchemaChangeSqlType.class)))
                .thenAnswer(invocation -> new OnlineSchemaChangeScheduleTaskParameters());

        // When
        List<OnlineSchemaChangeScheduleTaskParameters> result = parameters.generateSubTaskParameters(connectionConfig, schema);

        // Then
        assertEquals(2, result.size());
        verify(subTaskParameterFactory, times(2)).generate(anyString(), any(OnlineSchemaChangeSqlType.class));
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParameters_exception() throws Exception {
        // Given
        String schema = "test_schema";
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        String sqlContent = "create table test_table (id int primary key); ALTER TABLE test_table ADD COLUMN new_column int;";
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlType(OnlineSchemaChangeSqlType.CREATE_AND_ALTER);
        parameters.setSqlContent(sqlContent);

        doThrow(new Exception("Generate subtasks failed")).when(subTaskParameterFactory)
                .generate(anyString(), any(OnlineSchemaChangeSqlType.class));

        // When & Then
        parameters.generateSubTaskParameters(connectionConfig, schema);
    }

    @Test
    public void testDelimiter() {
        // Given
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setDelimiter(";");

        // When
        String actual = parameters.getDelimiter();

        // Then
        assertEquals(";", actual);
    }

    @Test
    public void testLockUsers() {
        // Given
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        List<String> lockUsers = new ArrayList<>();
        lockUsers.add("user1");
        lockUsers.add("user2");
        parameters.setLockUsers(lockUsers);

        // When
        List<String> actual = parameters.getLockUsers();

        // Then
        assertEquals(2, actual.size());
        assertEquals("user1", actual.get(0));
        assertEquals("user2", actual.get(1));
    }

    @Test
    public void testSwapTableType() {
        // Given
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        SwapTableType swapTableType = SwapTableType.SWAP;
        parameters.setSwapTableType(swapTableType);

        // When
        SwapTableType actual = parameters.getSwapTableType();

        // Then
        assertEquals(swapTableType, actual);
    }

    @Test
    public void testOriginTableCleanStrategy() {
        // Given
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        OriginTableCleanStrategy originTableCleanStrategy = OriginTableCleanStrategy.DROP_AND_RECREATE;
        parameters.setOriginTableCleanStrategy(originTableCleanStrategy);

        // When
        OriginTableCleanStrategy actual = parameters.getOriginTableCleanStrategy();

        // Then
        assertEquals(originTableCleanStrategy, actual);
    }

    @Test
    public void testGenerateSubTaskParameters_sqlsToBeExecuted() throws Exception {
        // Given
        String schema = "test_schema";
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        String sqlContent = "create table test_table (id int primary key); ALTER TABLE test_table ADD COLUMN new_column int;";
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlType(OnlineSchemaChangeSqlType.CREATE_AND_ALTER);
        parameters.setSqlContent(sqlContent);

        when(subTaskParameterFactory.generate(anyString(), any(OnlineSchemaChangeSqlType.class)))
                .thenAnswer(invocation -> new OnlineSchemaChangeScheduleTaskParameters());

        // When
        List<OnlineSchemaChangeScheduleTaskParameters> result = parameters.generateSubTaskParameters(connectionConfig, schema);
        OnlineSchemaChangeScheduleTaskParameters taskParameters = result.get(0);

        // Then
        assertEquals(2, taskParameters.getSqlsToBeExecuted().size());
        assertTrue(taskParameters.getSqlsToBeExecuted().contains("create table test_table (id int primary key);"));
        assertTrue(taskParameters.getSqlsToBeExecuted().contains("ALTER TABLE test_table ADD COLUMN new_column int;"));
    }

}