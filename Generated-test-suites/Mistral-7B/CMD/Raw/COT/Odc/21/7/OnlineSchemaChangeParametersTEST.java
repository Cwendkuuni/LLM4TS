import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.oceanbase.odc.core.connection.model.ConnectionConfig;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.core.shared.model.TableIdentity;
import com.oceanbase.odc.service.common.util.SqlUtils;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import org.junit.Test;

public class OnlineSchemaChangeParametersTest {

    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
       assertTrue(parameters.isContinueOnError());

        parameters.setErrorStrategy(TaskErrorStrategy.ABORT);
        assertFalse(parameters.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters_normal() {
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        String schema = "testSchema";
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlContent("create table t1(id int primary key); create table t2(id int primary key);");
        parameters.setSqlType(OnlineSchemaChangeSqlType.CREATE);

        List<OnlineSchemaChangeScheduleTaskParameters> expected = new ArrayList<>();
        OnlineSchemaChangeScheduleTaskParameters taskParams1 = new OnlineSchemaChangeScheduleTaskParameters();
        taskParams1.setDatabaseName("testDb");
        taskParams1.setOriginTableName("t1");
        OnlineSchemaChangeScheduleTaskParameters taskParams2 = new OnlineSchemaChangeScheduleTaskParameters();
        taskParams2.setDatabaseName("testDb");
        taskParams2.setOriginTableName("t2");
        expected.add(taskParams1);
        expected.add(taskParams2);

        when(connectionConfig.getDialectType()).thenReturn("MySQL");
        when(SubTaskParameterFactory.newInstance(connectionConfig, schema)).thenReturn(mock(SubTaskParameterFactory.class));

        List<OnlineSchemaChangeScheduleTaskParameters> actual = parameters.generateSubTaskParameters(connectionConfig, schema);
        assertEquals(expected, actual);

        verify(connectionConfig).getDialectType();
        verify(SubTaskParameterFactory.newInstance(connectionConfig, schema)).generate(anyString(), eq(OnlineSchemaChangeSqlType.CREATE));
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParameters_exception() {
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        String schema = "testSchema";
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlContent("create table t1(id int primary key); create table t2(id int primary key);");
        parameters.setSqlType(OnlineSchemaChangeSqlType.CREATE);

        when(connectionConfig.getDialectType()).thenThrow(new RuntimeException());

        List<OnlineSchemaChangeScheduleTaskParameters> actual = parameters.generateSubTaskParameters(connectionConfig, schema);
        fail("Should throw UnexpectedException");
    }

    @Test
    public void testGenerateSubTaskParameters_DDL_type() {
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        String schema = "testSchema";
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlContent("ALTER TABLE t1 ADD COLUMN c1 INT; ALTER TABLE t2 ADD COLUMN c2 INT;");
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);

        List<OnlineSchemaChangeScheduleTaskParameters> expected = new ArrayList<>();
        OnlineSchemaChangeScheduleTaskParameters taskParams1 = new OnlineSchemaChangeScheduleTaskParameters();
        taskParams1.setDatabaseName("testDb");
        taskParams1.setOriginTableName("t1");
        OnlineSchemaChangeScheduleTaskParameters taskParams2 = new OnlineSchemaChangeScheduleTaskParameters();
        taskParams2.setDatabaseName("testDb");
        taskParams2.setOriginTableName("t2");
        expected.add(taskParams1);
        expected.add(taskParams2);

        when(connectionConfig.getDialectType()).thenReturn("MySQL");
        when(SubTaskParameterFactory.newInstance(connectionConfig, schema)).thenReturn(mock(SubTaskParameterFactory.class));

        List<OnlineSchemaChangeScheduleTaskParameters> actual = parameters.generateSubTaskParameters(connectionConfig, schema);
        assertEquals(expected, actual);

        verify(connectionConfig).getDialectType();
        verify(SubTaskParameterFactory.newInstance(connectionConfig, schema)).generate(anyString(), eq(OnlineSchemaChangeSqlType.ALTER));

        OnlineSchemaChangeScheduleTaskParameters taskParams1_new = new OnlineSchemaChangeScheduleTaskParameters();
        taskParams1_new.setDatabaseName("testDb");
        taskParams1_new.setOriginTableName("t1");
        String newAlterStmt = "ALTER TABLE t1 RENAME TO t1_new;";
        taskParams1_new.getSqlsToBeExecuted().add(newAlterStmt);
        expected.set(0, taskParams1_new);

        doAnswer(invocation -> {
            OnlineSchemaChangeScheduleTaskParameters taskParams = invocation.getArgument(0);
            taskParams.getSqlsToBeExecuted().add(newAlterStmt);
            return null;
        }).when(SubTaskParameterFactory.newInstance(connectionConfig, schema)).generate(eq(parameters.getSqlContent()), eq(OnlineSchemaChangeSqlType.ALTER));

        actual = parameters.generateSubTaskParameters(connectionConfig, schema);
        assertEquals(expected, actual);

        verify(connectionConfig).getDialectType();
        verify(SubTaskParameterFactory.newInstance(connectionConfig, schema)).generate(eq(parameters.getSqlContent()), eq(OnlineSchemaChangeSqlType.ALTER));
    }
}