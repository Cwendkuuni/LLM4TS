import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import com.oceanbase.odc.core.connection.model.ConnectionConfig;
import com.oceanbase.odc.core.model.TableIdentity;
import com.oceanbase.odc.service.common.exception.UnexpectedException;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;

public class OnlineSchemaChangeParametersTest {

    @Test
    public void testOnlineSchemaChangeParameters() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters.setSqlContent("CREATE TABLE test_table (id INT PRIMARY KEY); ALTER TABLE test_table ADD COLUMN column_name INT;");
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        parameters.setLockTableTimeOutSeconds(10);
        parameters.setSwapTableType(SwapTableType.OLD_TO_NEW);
        parameters.setSwapTableNameRetryTimes(3);
        parameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.NONE);
        parameters.setDelimiter(";");
        List<String> lockUsers = new ArrayList<>();
        lockUsers.add("user1");
        parameters.setLockUsers(lockUsers);

        ConnectionConfig connectionConfig = Mockito.mock(ConnectionConfig.class);
        String schema = "test_schema";
        List<OnlineSchemaChangeScheduleTaskParameters> expectedSubTaskParameters = createSubTaskParameters();

        when(connectionConfig.getDialectType()).thenReturn("mysql");
        when(connectionConfig.getDatabaseName()).thenReturn("test_database");

        List<OnlineSchemaChangeScheduleTaskParameters> actualSubTaskParameters = parameters.generateSubTaskParameters(connectionConfig, schema);

        assertEquals(expectedSubTaskParameters.size(), actualSubTaskParameters.size());
        for (int i = 0; i < expectedSubTaskParameters.size(); i++) {
            OnlineSchemaChangeScheduleTaskParameters expectedParameter = expectedSubTaskParameters.get(i);
            OnlineSchemaChangeScheduleTaskParameters actualParameter = actualSubTaskParameters.get(i);
            assertEquals(expectedParameter.getDatabaseName(), actualParameter.getDatabaseName());
            assertEquals(expectedParameter.getOriginTableName(), actualParameter.getOriginTableName());
            assertEquals(expectedParameter.getNewTableName(), actualParameter.getNewTableName());
            assertEquals(expectedParameter.getSqlsToBeExecuted().size(), actualParameter.getSqlsToBeExecuted().size());
            for (int j = 0; j < expectedParameter.getSqlsToBeExecuted().size(); j++) {
                String expectedSql = expectedParameter.getSqlsToBeExecuted().get(j);
                String actualSql = actualParameter.getSqlsToBeExecuted().get(j);
                assertEquals(expectedSql, actualSql);
            }
        }

        verify(connectionConfig, times(1)).getDialectType();
        verify(connectionConfig, times(1)).getDatabaseName();
    }

    @Test(expected = UnexpectedException.class)
    public void testExceptionWhenGeneratingSubTaskParameters() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters.setSqlContent("CREATE TABLE test_table (id INT PRIMARY KEY); ALTER TABLE test_table ADD COLUMN column_name INT;");
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        parameters.setLockTableTimeOutSeconds(10);
        parameters.setSwapTableType(SwapTableType.OLD_TO_NEW);
        parameters.setSwapTableNameRetryTimes(3);
        parameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.NONE);
        parameters.setDelimiter(";");
        List<String> lockUsers = new ArrayList<>();
        lockUsers.add("user1");
        parameters.setLockUsers(lockUsers);

        ConnectionConfig connectionConfig = Mockito.mock(ConnectionConfig.class);
        String schema = "test_schema";

        doThrow(new RuntimeException("Test exception")).when(connectionConfig).getDialectType();

        parameters.generateSubTaskParameters(connectionConfig, schema);
    }

    private List<OnlineSchemaChangeScheduleTaskParameters> createSubTaskParameters() {
        ConnectionConfig connectionConfig = Mockito.mock(ConnectionConfig.class);
        String databaseName = "test_database";
        String schemaName = "test_schema";
        String originTableName1 = "test_table1";
        String newTableName1 = "test_new_table1";
        String originTableName2 = "test_table2";
        String newTableName2 = "test_new_table2";

        Mockito.when(connectionConfig.getDatabaseName()).thenReturn(databaseName);

        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = new ArrayList<>();

        OnlineSchemaChangeScheduleTaskParameters taskParameter1 = Mockito.spy(new OnlineSchemaChangeScheduleTaskParameters());
        taskParameter1.setDatabaseName(databaseName);
        taskParameter1.setOriginTableName(originTableName1);
        taskParameter1.setNewTableName(newTableName1);
        taskParameter1.setSqlsToBeExecuted(new ArrayList<>());
        OnlineSchemaChangeScheduleTaskParameters taskParameter2 = new OnlineSchemaChangeScheduleTaskParameters();
        taskParameter2.setDatabaseName(databaseName);
        taskParameter2.setOriginTableName(originTableName2);
        taskParameter2.setNewTableName(newTableName2);
        taskParameter2.setSqlsToBeExecuted(new ArrayList<>());

        subTaskParameters.add(taskParameter1);
        subTaskParameters.add(taskParameter2);

        taskParameter1.getSqlsToBeExecuted().add("CREATE TABLE test_table1 (id INT PRIMARY KEY);");
        taskParameter1.getSqlsToBeExecuted().add("ALTER TABLE test_table1 ADD COLUMN column_name INT;");
        taskParameter1.getSqlsToBeExecuted().add("ALTER TABLE test_table1 RENAME TO test_new_table1;");

        taskParameter2.getSqlsToBeExecuted().add("CREATE TABLE test_table2 (id INT PRIMARY KEY);");
        taskParameter2.getSqlsToBeExecuted().add("ALTER TABLE test_table2 ADD COLUMN column_name INT;");
        taskParameter2.getSqlsToBeExecuted().add("ALTER TABLE test_table2 RENAME TO test_new_table2;");

        OnlineSchemaChangeSqlType sqlType = OnlineSchemaChangeSqlType.ALTER;
        DdlUtils ddlUtils = Mockito.mock(DdlUtils.class);
        SubTaskParameterFactory subTaskParameterFactory = new SubTaskParameterFactory(connectionConfig, schemaName, ddlUtils);
        OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
        onlineSchemaChangeParameters.setSqlType(sqlType);
        onlineSchemaChangeParameters.setSqlContent("CREATE TABLE test_table1 (id INT PRIMARY KEY); ALTER TABLE test_table1 ADD COLUMN column_name INT; ALTER TABLE test_table2 ADD COLUMN column_name INT;");
        onlineSchemaChangeParameters.setLockTableTimeOutSeconds(10);
        onlineSchemaChangeParameters.setSwapTableType(SwapTableType.OLD_TO_NEW);
        onlineSchemaChangeParameters.setSwapTableNameRetryTimes(3);
        onlineSchemaChangeParameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.NONE);
        onlineSchemaChangeParameters.setDelimiter(";");
        onlineSchemaChangeParameters.setLockUsers(new ArrayList<>());

        when(ddlUtils.replaceTableName(ArgumentMatchers.anyString(), ArgumentMatchers.eq(newTableName1),
                ArgumentMatchers.eq("mysql"), ArgumentMatchers.eq(sqlType)))
                .thenReturn("ALTER TABLE test_new_table1 RENAME TO test_original_table1;");

        return onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, schemaName);
    }
}