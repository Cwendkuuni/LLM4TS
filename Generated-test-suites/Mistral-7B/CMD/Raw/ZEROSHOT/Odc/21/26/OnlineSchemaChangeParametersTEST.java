package com.oceanbase.odc.service.onlineschemachange.model;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import com.oceanbase.odc.core.connection.model.ConnectionConfig;
import com.oceanbase.odc.core.flow.model.TaskParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.core.shared.model.TableIdentity;
import com.oceanbase.odc.service.common.util.SqlUtils;
import com.oceanbase.odc.service.connection.model.ConnectionConfigBuilder;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactoryBuilder;

@RunWith(org.mockito.junit.MockitoJUnitRunner.class)
public class OnlineSchemaChangeParametersTest {

    @Test
    public void testInstance() {
        OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
        assertNotNull(onlineSchemaChangeParameters);
    }

    @Test
    public void testGettersAndSetters() {
        OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
        OnlineSchemaChangeSqlType sqlType = OnlineSchemaChangeSqlType.ALTER;
        String sqlContent = "ALTER TABLE table_name SET TABLECOMMENT = 'new_comment'";
        onlineSchemaChangeParameters.setSqlType(sqlType);
        onlineSchemaChangeParameters.setSqlContent(sqlContent);

        assertEquals(OnlineSchemaChangeSqlType.ALTER, onlineSchemaChangeParameters.getSqlType());
        assertEquals("ALTER TABLE table_name SET TABLECOMMENT = 'new_comment'", onlineSchemaChangeParameters.getSqlContent());

        // Set and get other fields
        Mockito.when(connectionConfig.getDialectType()).thenReturn("mysql");
        ConnectionConfig connectionConfig = Mockito.spy(new ConnectionConfigBuilder().build());
        String schema = "test_schema";
        List<OnlineSchemaChangeScheduleTaskParameters> expectedTaskParameters = new ArrayList<>();
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        taskParameter.setDatabaseName("test_database");
        taskParameter.setOriginTableName("test_table");
        taskParameter.setNewTableName("test_new_table");
        expectedTaskParameters.add(taskParameter);
        OnlineSchemaChangeParameters actualOnlineSchemaChangeParameters = onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, schema);
        assertEquals(expectedTaskParameters, actualOnlineSchemaChangeParameters.getSubTaskParameters());
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParametersWithException() {
        ConnectionConfig connectionConfig = new ConnectionConfigBuilder().build();
        OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
        String schema = "test_schema";
        List<OnlineSchemaChangeScheduleTaskParameters> expectedException = null;
        try {
            onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, schema);
        } catch (Exception e) {
            expectedException = (List<OnlineSchemaChangeScheduleTaskParameters>) e.getCause().getCause();
        }
        assertNotNull(expectedException);
        throw new UnexpectedException("Failed to generate subtasks with parameter: " + onlineSchemaChangeParameters, e);
    }

    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
        assertEquals(false, onlineSchemaChangeParameters.isContinueOnError());
        OnlineSchemaChangeParameters onlineSchemaChangeParametersContinue = new OnlineSchemaChangeParameters();
        onlineSchemaChangeParametersContinue.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertEquals(true, onlineSchemaChangeParametersContinue.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() {
        ConnectionConfig connectionConfig = new ConnectionConfigBuilder().build();
        String schema = "test_schema";
        OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
        List<String> sqls = Arrays.asList("ALTER TABLE table_name SET TABLECOMMENT = 'new_comment'",
                "ALTER TABLE table_name ADD COLUMN new_column INT");
        OnlineSchemaChangeSqlType sqlType = OnlineSchemaChangeSqlType.ALTER;
        List<OnlineSchemaChangeScheduleTaskParameters> expectedTaskParameters = new ArrayList<>();
        OnlineSchemaChangeScheduleTaskParameters taskParameter1 = new OnlineSchemaChangeScheduleTaskParameters();
        OnlineSchemaChangeScheduleTaskParameters taskParameter2 = new OnlineSchemaChangeScheduleTaskParameters();
        taskParameter1.setDatabaseName("test_database");
        taskParameter1.setOriginTableName("test_table");
        taskParameter1.setNewTableName("test_new_table_1");
        taskParameter1.setSqlsToBeExecuted(Arrays.asList("ALTER TABLE table_name SET TABLECOMMENT = 'new_comment'",
                "ALTER TABLE test_new_table_1 ADD COLUMN new_column INT"));
        taskParameter2.setDatabaseName("test_database");
        taskParameter2.setOriginTableName("test_table");
        taskParameter2.setNewTableName("test_new_table_2");
        taskParameter2.setSqlsToBeExecuted(Arrays.asList("ALTER TABLE table_name ADD COLUMN new_column INT"));
        expectedTaskParameters.add(taskParameter1);
        expectedTaskParameters.add(taskParameter2);
        try (SubTaskParameterFactory subTaskParameterFactory = new SubTaskParameterFactoryBuilder().build()) {
            List<OnlineSchemaChangeScheduleTaskParameters> actualTaskParameters = onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, schema);
            assertEquals(expectedTaskParameters, actualTaskParameters);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
}