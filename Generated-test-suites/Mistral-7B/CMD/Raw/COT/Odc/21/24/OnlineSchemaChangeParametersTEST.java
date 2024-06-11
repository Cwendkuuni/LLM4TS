import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.oceanbase.odc.core.connection.model.ConnectionConfig;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.model.TableIdentity;
import com.oceanbase.odc.service.common.util.SqlUtils;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;

public class OnlineSchemaChangeParametersTest {

    @Test
    public void testNewOnlineSchemaChangeParameters() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        assertNull(parameters.getSqlType());
        assertNull(parameters.getSqlContent());
        assertNull(parameters.getErrorStrategy());
        assertNull(parameters.getLockTableTimeOutSeconds());
        assertNull(parameters.getSwapTableNameRetryTimes());
        assertNull(parameters.getOriginTableCleanStrategy());
        assertNull(parameters.getDelimiter());
        assertNull(parameters.getLockUsers());
        assertNull(parameters.getSwapTableType());
    }

    @Test
    public void testSettersAndGetters() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();

        OnlineSchemaChangeSqlType sqlType = OnlineSchemaChangeSqlType.ALTER;
        parameters.setSqlType(sqlType);
        assertEquals(sqlType, parameters.getSqlType());

        String sqlContent = "ALTER TABLE test_table ADD COLUMN test_column INT";
        parameters.setSqlContent(sqlContent);
        assertEquals(sqlContent, parameters.getSqlContent());

        TaskErrorStrategy errorStrategy = TaskErrorStrategy.CONTINUE;
        parameters.setErrorStrategy(errorStrategy);
        assertEquals(errorStrategy, parameters.getErrorStrategy());

        parameters.setLockTableTimeOutSeconds(10);
        assertEquals(Integer.valueOf(10), parameters.getLockTableTimeOutSeconds());

        parameters.setSwapTableNameRetryTimes(5);
        assertEquals(Integer.valueOf(5), parameters.getSwapTableNameRetryTimes());

        OnlineTableCleanStrategy originTableCleanStrategy = OnlineTableCleanStrategy.TRUNCATE;
        parameters.setOriginTableCleanStrategy(originTableCleanStrategy);
        assertEquals(originTableCleanStrategy, parameters.getOriginTableCleanStrategy());

        String delimiter = ";";
        parameters.setDelimiter(delimiter);
        assertEquals(delimiter, parameters.getDelimiter());

        List<String> lockUsers = Arrays.asList("user1", "user2");
        parameters.setLockUsers(lockUsers);
        assertEquals(lockUsers, parameters.getLockUsers());

        SwapTableType swapTableType = SwapTableType.OLD_TABLE;
        parameters.setSwapTableType(swapTableType);
        assertEquals(swapTableType, parameters.getSwapTableType());

        assertTrue(parameters.isContinueOnError());
        parameters.setErrorStrategy(TaskErrorStrategy.FAIL);
        assertFalse(parameters.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        ConnectionConfig connectionConfig = new ConnectionConfig();
        String schema = "test_schema";

        String sqlContent = "ALTER TABLE test_table ADD COLUMN test_column INT";
        parameters.setSqlContent(sqlContent);
        OnlineSchemaChangeSqlType sqlType = OnlineSchemaChangeSqlType.ALTER;
        parameters.setSqlType(sqlType);

        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameters
                .generateSubTaskParameters(connectionConfig, schema);

        assertEquals(1, subTaskParameters.size());
        OnlineSchemaChangeScheduleTaskParameters taskParameters = subTaskParameters.get(0);
        TableIdentity key = new TableIdentity("test_schema", "test_table");
        assertEquals(key, taskParameters.getTableIdentity());
        assertEquals(sqlContent, taskParameters.getSqlsToBeExecuted().get(0));
        assertEquals(sqlType, taskParameters.getSqlType());

        String newAlterStmt = "ALTER TABLE test_table RENAME TO test_table_new";
        assertTrue(subTaskParameters.get(0).getSqlsToBeExecuted().contains(newAlterStmt));
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParametersException() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        ConnectionConfig connectionConfig = new ConnectionConfig();
        String schema = "test_schema";

        String sqlContent = "ALTER TABLE test_table ADD COLUMN test_column INT";
        parameters.setSqlContent(sqlContent);
        OnlineSchemaChangeSqlType sqlType = OnlineSchemaChangeSqlType.ALTER;
        parameters.setSqlType(sqlType);

        try (SubTaskParameterFactory subTaskParameterFactory = new SubTaskParameterFactory(connectionConfig,
                schema)) {
            Map<TableIdentity, OnlineSchemaChangeScheduleTaskParameters> taskParameters = new LinkedHashMap<>();
            List<String> sqls = SqlUtils.split(connectionConfig.getDialectType(), sqlContent,
                    parameters.getDelimiter());
            for (String sql : sqls) {
                OnlineSchemaChangeScheduleTaskParameters parameter = subTaskParameterFactory.generate(sql, sqlType);
                TableIdentity key = new TableIdentity(parameter.getDatabaseName(),
                        parameter.getOriginTableName());
                taskParameters.putIfAbsent(key, parameter);
                if (sqlType == OnlineSchemaChangeSqlType.ALTER) {
                    String newAlterStmt = DdlUtils.replaceTableName(sql,
                            parameter.getNewTableName(), connectionConfig.getDialectType(), sqlType);
                    taskParameters.get(key).getSqlsToBeExecuted().add(newAlterStmt);
                }
            }
            List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = new ArrayList<>(
                    taskParameters.values());
            parameters.setSubTaskParameters(subTaskParameters);
        } catch (Exception e) {
            throw new UnexpectedException("Failed to generate subtasks with parameter: " + parameters, e);
        }

        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameters
                .generateSubTaskParameters(connectionConfig, schema);
    }
}