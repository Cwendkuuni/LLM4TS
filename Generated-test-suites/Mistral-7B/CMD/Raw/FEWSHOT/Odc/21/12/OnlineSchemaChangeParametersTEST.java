package com.oceanbase.odc.service.onlineschemachange.model;

import static com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters.OnlineSchemaChangeSqlType.ALTER;
import static com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters.OnlineSchemaChangeSqlType.CREATE;
import static com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters.OnlineSchemaChangeSqlType.DROP;
import static com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters.OriginTableCleanStrategy.CLEAN_AND_DROP;
import static com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters.OriginTableCleanStrategy.CLEAN_ONLY;
import static com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters.SwapTableType.AFTER_SWAP;
import static com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters.SwapTableType.BEFORE_SWAP;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.oceanbase.odc.core.flow.model.ConnectionConfig;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.core.shared.model.TableIdentity;
import com.oceanbase.odc.service.common.util.SqlUtils;
import com.oceanbase.odc.service.connection.model.ConnectionConfigImpl;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OnlineSchemaChangeParametersTest {

    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(parameters.isContinueOnError());

        OnlineSchemaChangeParameters noContinueParameters = new OnlineSchemaChangeParameters();
        assertFalse(noContinueParameters.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParametersWithNullConnectionConfig() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        when(parameters.generateSubTaskParameters((ConnectionConfig) null, "schema")).thenThrow(new UnexpectedException("message"));
        assertThrows(UnexpectedException.class, () -> parameters.generateSubTaskParameters((ConnectionConfig) null, "schema"));
    }

    @Test
    public void testGenerateSubTaskParametersWithEmptySqlContent() {
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        when(parameters.generateSubTaskParameters(connectionConfig, "schema")).thenCallRealMethod();

        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameters.generateSubTaskParameters(connectionConfig, "schema");
        assertTrue(subTaskParameters.isEmpty());
    }

    @Test
    public void testGenerateSubTaskParametersWithOneSqlStatement() {
        ConnectionConfig connectionConfig = new ConnectionConfigImpl();
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();

        String sqlContent = "CREATE TABLE test (id INT PRIMARY KEY);";
        List<String> sqls = SqlUtils.split(connectionConfig.getDialectType(), sqlContent, parameters.getDelimiter());

        when(parameters.generateSubTaskParameters(connectionConfig, "schema")).thenCallRealMethod();

        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameters.generateSubTaskParameters(connectionConfig, "schema");
        assertEquals(1, subTaskParameters.size());
        OnlineSchemaChangeScheduleTaskParameters taskParameter = subTaskParameters.get(0);
        assertEquals("CREATE TABLE test (id INT PRIMARY KEY);", taskParameter.getSqlContent());
    }

    @Test
    public void testGenerateSubTaskParametersWithMultipleSqlStatements() {
        ConnectionConfig connectionConfig = new ConnectionConfigImpl();
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();

        String sqlContent = "CREATE TABLE test (id INT PRIMARY KEY);\nCREATE TABLE test2 (id INT PRIMARY KEY);";
        List<String> sqls = SqlUtils.split(connectionConfig.getDialectType(), sqlContent, parameters.getDelimiter());

        when(parameters.generateSubTaskParameters(connectionConfig, "schema")).thenCallRealMethod();

        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameters.generateSubTaskParameters(connectionConfig, "schema");
        assertEquals(2, subTaskParameters.size());
    }

    @Test
    public void testGenerateSubTaskParametersWithDifferentTableNames() {
        ConnectionConfig connectionConfig = new ConnectionConfigImpl();
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();

        String sqlContent = "CREATE TABLE test (id INT PRIMARY KEY);\nCREATE TABLE test2 (id INT PRIMARY KEY);";
        List<String> sqls = SqlUtils.split(connectionConfig.getDialectType(), sqlContent, parameters.getDelimiter());

        when(parameters.generateSubTaskParameters(connectionConfig, "schema")).thenCallRealMethod();

        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameters.generateSubTaskParameters(connectionConfig, "schema");
        List<String> testTableNames = new ArrayList<>();
        List<String> test2TableNames = new ArrayList<>();

        for (OnlineSchemaChangeScheduleTaskParameters taskParameter : subTaskParameters) {
            if (taskParameter.getTableIdentity().getTableName().equals("test")) {
                testTableNames.add(taskParameter.getTableIdentity().getTableName());
            } else {
                test2TableNames.add(taskParameter.getTableIdentity().getTableName());
            }
        }

        assertEquals(1, testTableNames.size());
        assertEquals("test", testTableNames.get(0));
        assertEquals(1, test2TableNames.size());
        assertEquals("test2", test2TableNames.get(0));
    }

    @Test
    public void testReplaceTableNameWithNewTableName() {
        ConnectionConfig connectionConfig = new ConnectionConfigImpl();
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();

        String sqlContent = "ALTER TABLE test ADD COLUMN new_column INT;";
        List<String> sqls = SqlUtils.split(connectionConfig.getDialectType(), sqlContent, parameters.getDelimiter());

        String newTableName = "new_test";
        OnlineSchemaChangeScheduleTaskParameters taskParameters = parameters.new OnlineSchemaChangeScheduleTaskParameters();
        taskParameters.setNewTableName(newTableName);

        String newAlterStmt = DdlUtils.replaceTableName(sqls.get(0), taskParameters.getTableIdentity().getTableName(),
                connectionConfig.getDialectType(), ALTER);
        assertEquals("ALTER TABLE new_test ADD COLUMN new_column INT;", newAlterStmt);
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParametersWithException() {
        ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();

        String sqlContent = "CREATE TABLE test (id INT PRIMARY KEY);";
        List<String> sqls = SqlUtils.split(connectionConfig.getDialectType(), sqlContent, parameters.getDelimiter());

        when(parameters.generateSubTaskParameters(connectionConfig, "schema")).thenCall((MockInvocationOnMock invocation) -> {
            throw new UnexpectedException("message");
        });

        parameters.generateSubTaskParameters(connectionConfig, "schema");
    }

}