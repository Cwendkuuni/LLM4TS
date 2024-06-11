import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.oceanbase.odc.core.connection.ConnectionConfig;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.model.TableIdentity;
import com.oceanbase.odc.service.common.util.SqlUtils;
import com.oceanbase.odc.service.connection.model.ConnectionConfigFactory;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

/**
 * Test class for OnlineSchemaChangeParameters
 *
 * @author yaobin
 */
public class OnlineSchemaChangeParametersTest {

    @Test
    public void testOnlineSchemaChangeParameters() {
        // Given
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        ConnectionConfig connectionConfig = ConnectionConfigFactory.newConnectionConfig();
        String schema = "test_schema";

        // When
        parameters.setSqlType(OnlineSchemaChangeSqlType.CREATE);
        parameters.setSqlContent("create table test_table (id int primary key, name varchar(10));");
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        parameters.setLockTableTimeOutSeconds(10);
        parameters.setSwapTableType(SwapTableType.SWAP_WITH_ANOTHER_TABLE);
        parameters.setSwapTableNameRetryTimes(3);
        parameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.DROP);

        // Then
        assertEquals(OnlineSchemaChangeSqlType.CREATE, parameters.getSqlType());
        assertEquals("create table test_table (id int primary key, name varchar(10));", parameters.getSqlContent());
        assertEquals(TaskErrorStrategy.CONTINUE, parameters.getErrorStrategy());
        assertEquals(10, parameters.getLockTableTimeOutSeconds());
        assertEquals(SwapTableType.SWAP_WITH_ANOTHER_TABLE, parameters.getSwapTableType());
        assertEquals(3, parameters.getSwapTableNameRetryTimes());
        assertEquals(OriginTableCleanStrategy.DROP, parameters.getOriginTableCleanStrategy());

        // When generating sub task parameters
        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameters.generateSubTaskParameters(connectionConfig, schema);

        // Then
        assertEquals(1, subTaskParameters.size());
        OnlineSchemaChangeScheduleTaskParameters taskParameters = subTaskParameters.get(0);
        assertEquals(connectionConfig.getDatabaseName(), taskParameters.getDatabaseName());
        assertEquals("test_table", taskParameters.getOriginTableName());
        assertTrue(taskParameters.getSqlsToBeExecuted().size() > 0);
        assertEquals("create table test_table (id int primary key, name varchar(10));", taskParameters.getSqlsToBeExecuted().get(0));

        // When generating sub task parameters with ALTER statement
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters.setSqlContent("alter table test_table add column new_column varchar(10);");
        subTaskParameters = parameters.generateSubTaskParameters(connectionConfig, schema);

        // Then
        assertEquals(1, subTaskParameters.size());
        taskParameters = subTaskParameters.get(0);
        assertEquals("test_table", taskParameters.getOriginTableName());
        assertTrue(taskParameters.getSqlsToBeExecuted().size() > 1);
        assertEquals("alter table test_table add column new_column varchar(10);", taskParameters.getSqlsToBeExecuted().get(0));
        assertEquals("alter table test_table rename to test_table_new;", taskParameters.getSqlsToBeExecuted().get(1));

        // When checking isContinueOnError
        assertTrue(parameters.isContinueOnError());

        // When setting errorStrategy to ABORT
        parameters.setErrorStrategy(TaskErrorStrategy.ABORT);
        assertFalse(parameters.isContinueOnError());

        // When setting lockUsers
        List<String> lockUsers = new ArrayList<>();
        lockUsers.add("user1");
        lockUsers.add("user2");
        parameters.setLockUsers(lockUsers);

        // Then
        assertEquals(lockUsers, parameters.getLockUsers());
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParametersWithException() {
        // Given
        ConnectionConfig connectionConfig = ConnectionConfigFactory.newConnectionConfig();
        String schema = "test_schema";
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlType(OnlineSchemaChangeSqlType.CREATE);
        parameters.setSqlContent("create table test_table (id int primary key, name varchar(10));");

        // When
        try {
            parameters.generateSubTaskParameters(null, schema);
        } catch (Exception e) {
            throw new UnexpectedException("Failed to generate subtasks with parameter: " + parameters, e);
        }

        // Then - this should not reach here
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParametersWithNullDdlUtils() {
        // Given
        ConnectionConfig connectionConfig = ConnectionConfigFactory.newConnectionConfig();
        String schema = "test_schema";
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlType(OnlineSchemaChangeSqlType.CREATE);
        parameters.setSqlContent("create table test_table (id int primary key, name varchar(10));");
        SubTaskParameterFactory subTaskParameterFactory = mock(SubTaskParameterFactory.class);
        when(subTaskParameterFactory.generate(anyString(), any(OnlineSchemaChangeSqlType.class)))
                .thenThrow(new RuntimeException("Failed to generate subtask parameter"));

        try {
            parameters.generateSubTaskParameters(connectionConfig, schema, null);
        } catch (Exception e) {
            throw new UnexpectedException("Failed to generate subtasks with parameter: " + parameters, e);
        }

        // Then - this should not reach here
    }
}