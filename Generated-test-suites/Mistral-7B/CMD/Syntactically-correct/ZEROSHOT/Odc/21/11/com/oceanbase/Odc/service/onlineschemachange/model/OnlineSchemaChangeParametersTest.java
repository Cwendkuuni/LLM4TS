import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.oceanbase.odc.core.connection.ConnectionConfig;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.core.shared.model.TableIdentity;
import com.oceanbase.odc.service.common.util.SqlUtils;
import com.oceanbase.odc.service.connection.model.ConnectionConfigBuilder;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import org.junit.Test;
import org.mockito.ArgumentMatchers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

public class OnlineSchemaChangeParametersTest {

    @Test
    public void testInstance() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        assertNotNull(parameters);
    }

    @Test
    public void testGettersAndSetters() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters.setSqlContent("create table test (id int primary key) engine=innodb;");
        parameters.setErrorStrategy(TaskErrorStrategy.IGNORE);
        parameters.setLockTableTimeOutSeconds(10);
        parameters.setSwapTableType(SwapTableType.TABLE_SWAP);
        parameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.CLEAN);
        parameters.setDelimiter(";");
        parameters.setLockUsers(new ArrayList<>());

        assertEquals(OnlineSchemaChangeSqlType.ALTER, parameters.getSqlType());
        assertEquals("create table test (id int primary key) engine=innodb;", parameters.getSqlContent());
        assertEquals(TaskErrorStrategy.IGNORE, parameters.getErrorStrategy());
        assertEquals(10, parameters.getLockTableTimeOutSeconds());
        assertEquals(SwapTableType.TABLE_SWAP, parameters.getSwapTableType());
        assertEquals(OriginTableCleanStrategy.CLEAN, parameters.getOriginTableCleanStrategy());
        assertEquals(";", parameters.getDelimiter());
        assertTrue(parameters.getLockUsers().isEmpty());

        parameters.setLockUsers(List.of("user1", "user2"));
        assertEquals(2, parameters.getLockUsers().size());
        assertTrue(parameters.getLockUsers().contains("user1"));
        assertTrue(parameters.getLockUsers().contains("user2"));
    }

    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(parameters.isContinueOnError());

        parameters.setErrorStrategy(TaskErrorStrategy.IGNORE);
        assertFalse(parameters.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() throws Exception {
        String schema = "test_schema";
        ConnectionConfig connectionConfig = new ConnectionConfigBuilder()
                .setDatabaseName("test_db")
                .setDialectType("MySQL")
                .setAddress("localhost:1521")
                .setUsername("username")
                .setPassword("password")
                .setPort(1521)
                .build();

        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters.setSqlContent("create table test (id int primary key) engine=innodb;");
        parameters.setErrorStrategy(TaskErrorStrategy.IGNORE);
        parameters.setLockTableTimeOutSeconds(10);
        parameters.setSwapTableType(SwapTableType.TABLE_SWAP);
        parameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.CLEAN);
        parameters.setDelimiter(";");

        SubTaskParameterFactory subTaskParameterFactory = mock(SubTaskParameterFactory.class);
        when(subTaskParameterFactory.generate(ArgumentMatchers.anyString(), ArgumentMatchers.any(OnlineSchemaChangeSqlType.class)))
                .thenReturn(new OnlineSchemaChangeScheduleTaskParameters());

        try (ConnectionConfig mockedConnectionConfig = mock(ConnectionConfig.class);
             SubTaskParameterFactory mockedSubTaskParameterFactory = mock(SubTaskParameterFactory.class)) {

            when(mockedConnectionConfig.getDialectType()).thenReturn(connectionConfig.getDialectType());
            when(mockedConnectionConfig.getDialectName()).thenReturn(connectionConfig.getDialectType());

            doReturn(mockedSubTaskParameterFactory).when(parameters).newSubTaskParameterFactory(connectionConfig, schema);

            List<OnlineSchemaChangeScheduleTaskParameters> taskParameters = parameters.generateSubTaskParameters(connectionConfig, schema);
            assertEquals(1, taskParameters.size());
            verify(subTaskParameterFactory, times(1)).generate(ArgumentMatchers.anyString(), ArgumentMatchers.any(OnlineSchemaChangeSqlType.class));
        }
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParametersWithException() throws Exception {
        String schema = "test_schema";
        ConnectionConfig connectionConfig = new ConnectionConfigBuilder()
                .setDatabaseName("test_db")
                .setDialectType("MySQL")
                .setAddress("localhost:1521")
                .setUsername("username")
                .setPassword("password")
                .setPort(1521)
                .build();

        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters.setSqlContent("create table test (id int primary key) engine=innodb;");
        parameters.setErrorStrategy(TaskErrorStrategy.IGNORE);
        parameters.setLockTableTimeOutSeconds(10);
        parameters.setSwapTableType(SwapTableType.TABLE_SWAP);
        parameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.CLEAN);
        parameters.setDelimiter(";");

        SubTaskParameterFactory subTaskParameterFactory = mock(SubTaskParameterFactory.class);
        when(subTaskParameterFactory.generate(ArgumentMatchers.anyString(), ArgumentMatchers.any(OnlineSchemaChangeSqlType.class)))
                .throwException(new Exception("Failed to generate subtasks with parameter: " + parameters));

        try (ConnectionConfig mockedConnectionConfig = mock(ConnectionConfig.class);
             SubTaskParameterFactory mockedSubTaskParameterFactory = mock(SubTaskParameterFactory.class)) {

            when(mockedConnectionConfig.getDialectType()).thenReturn(connectionConfig.getDialectType());
            when(mockedConnectionConfig.getDialectName()).thenReturn(connectionConfig.getDialectType());

            doReturn(mockedSubTaskParameterFactory).when(parameters).newSubTaskParameterFactory(connectionConfig, schema);

            List<OnlineSchemaChangeScheduleTaskParameters> taskParameters = parameters.generateSubTaskParameters(connectionConfig, schema);
            fail("Unexpected exception should be thrown");
        }
    }

    @Test
    public void testGenerateAlterStatement() throws Exception {
        String schema = "test_schema";
        ConnectionConfig connectionConfig = new ConnectionConfigBuilder()
                .setDatabaseName("test_db")
                .setDialectType("MySQL")
                .setAddress("localhost:1521")
                .setUsername("username")
                .setPassword("password")
                .setPort(1521)
                .build();

        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters.setSqlContent("create table test (id int primary key) engine=innodb; alter table test add column col int;");
        parameters.setErrorStrategy(TaskErrorStrategy.IGNORE);
        parameters.setLockTableTimeOutSeconds(10);
        parameters.setSwapTableType(SwapTableType.TABLE_SWAP);
        parameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.CLEAN);
        parameters.setDelimiter(";");

        DdlUtils ddlUtils = mock(DdlUtils.class);
        when(ddlUtils.replaceTableName(ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.any(OnlineSchemaChangeSqlType.class)))
                .thenReturn("alter table test add column col int;");

        try (ConnectionConfig mockedConnectionConfig = mock(ConnectionConfig.class);
             SubTaskParameterFactory mockedSubTaskParameterFactory = mock(SubTaskParameterFactory.class);
             DdlUtils mockedDdlUtils = mock(DdlUtils.class)) {

            when(mockedConnectionConfig.getDialectType()).thenReturn(connectionConfig.getDialectType());
            when(mockedConnectionConfig.getDialectName()).thenReturn(connectionConfig.getDialectType());
            when(mockedSubTaskParameterFactory.generate(ArgumentMatchers.anyString(), ArgumentMatchers.any(OnlineSchemaChangeSqlType.class))).thenReturn(new OnlineSchemaChangeScheduleTaskParameters());
            when(mockedConnectionConfig.getDdlUtils()).thenReturn(mockedDdlUtils);

            OnlineSchemaChangeScheduleTaskParameters taskParameter = parameters.generateSubTaskParameters(connectionConfig, schema).get(0);
            List<String> sqlsToBeExecuted = taskParameter.getSqlsToBeExecuted();

            assertEquals(2, sqlsToBeExecuted.size());
            assertEquals("create table test (id int primary key) engine=innodb;", sqlsToBeExecuted.get(0));
            assertEquals("alter table test add column col int;", sqlsToBeExecuted.get(1));
        }
    }
}