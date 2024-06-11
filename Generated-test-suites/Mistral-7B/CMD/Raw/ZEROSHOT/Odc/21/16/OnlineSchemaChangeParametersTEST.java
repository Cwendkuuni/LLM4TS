import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.oceanbase.odc.core.connection.model.ConnectionConfig;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.model.TableIdentity;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;

public class OnlineSchemaChangeParametersTest {

    @Test
    public void testInstance() {
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

        OnlineSchemaChangeSqlType sqlType = OnlineSchemaChangeSqlType.ADD_COLUMN;
        parameters.setSqlType(sqlType);
        assertEquals(sqlType, parameters.getSqlType());

        String sqlContent = "CREATE TABLE test_table (id INT PRIMARY KEY)";
        parameters.setSqlContent(sqlContent);
        assertEquals(sqlContent, parameters.getSqlContent());

        TaskErrorStrategy errorStrategy = TaskErrorStrategy.RETRY;
        parameters.setErrorStrategy(errorStrategy);
        assertEquals(errorStrategy, parameters.getErrorStrategy());

        parameters.setLockTableTimeOutSeconds(3);
        assertEquals(3, parameters.getLockTableTimeOutSeconds().intValue());

        parameters.setSwapTableNameRetryTimes(5);
        assertEquals(5, parameters.getSwapTableNameRetryTimes().intValue());

        OriginTableCleanStrategy originTableCleanStrategy = OriginTableCleanStrategy.DROP;
        parameters.setOriginTableCleanStrategy(originTableCleanStrategy);
        assertEquals(originTableCleanStrategy, parameters.getOriginTableCleanStrategy());

        String delimiter = "|";
        parameters.setDelimiter(delimiter);
        assertEquals(delimiter, parameters.getDelimiter());

        List<String> lockUsers = Arrays.asList("user1", "user2");
        parameters.setLockUsers(lockUsers);
        assertEquals(lockUsers, parameters.getLockUsers());

        SwapTableType swapTableType = SwapTableType.MERGE;
        parameters.setSwapTableType(swapTableType);
        assertEquals(swapTableType, parameters.getSwapTableType());
    }

    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        assertFalse(parameters.isContinueOnError());

        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(parameters.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() throws Exception {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        String schema = "test_schema";
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlType(OnlineSchemaChangeSqlType.ADD_COLUMN);
        parameters.setSqlContent("CREATE TABLE test_table (id INT PRIMARY KEY, new_column INT)");
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        parameters.setLockTableTimeOutSeconds(5);
        parameters.setSwapTableNameRetryTimes(3);
        parameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.DROP);
        parameters.setDelimiter("|");
        parameters.setLockUsers(Collections.singletonList("user1"));
        parameters.setSwapTableType(SwapTableType.MERGE);

        List<OnlineSchemaChangeScheduleTaskParameters> taskParameters = parameters.generateSubTaskParameters(connectionConfig, schema);
        assertEquals(1, taskParameters.size());
        OnlineSchemaChangeScheduleTaskParameters taskParameter = taskParameters.get(0);
        TableIdentity tableIdentity = taskParameter.getTableIdentity();
        assertEquals("test_schema", tableIdentity.getDatabaseName());
        assertEquals("test_table", tableIdentity.getTableName());
        assertEquals(OnlineSchemaChangeSqlType.ADD_COLUMN, taskParameter.getSqlType());
        assertSame("CREATE TABLE test_table (id INT PRIMARY KEY, new_column INT)", taskParameter.getSqlContent());
        assertEquals(TaskErrorStrategy.CONTINUE, taskParameter.getErrorStrategy());
        assertEquals(5, taskParameter.getLockTableTimeOutSeconds().intValue());
        assertEquals(3, taskParameter.getSwapTableNameRetryTimes().intValue());
        assertEquals(OriginTableCleanStrategy.DROP, taskParameter.getOriginTableCleanStrategy());
        assertEquals("|", taskParameter.getDelimiter());
        assertEquals("user1", taskParameter.getLockUsers().get(0));
        assertEquals(SwapTableType.MERGE, taskParameter.getSwapTableType());
        List<String> sqlsToBeExecuted = taskParameter.getSqlsToBeExecuted();
        assertEquals(1, sqlsToBeExecuted.size());
        String expectedNewAlterStmt = "ALTER TABLE test_table ADD COLUMN new_column INT";
        assertEquals(expectedNewAlterStmt, sqlsToBeExecuted.get(0));

        // Test error scenario
        parameters.setSqlContent(null);
        try {
            taskParameters = parameters.generateSubTaskParameters(connectionConfig, schema);
            fail("Unexpected exception not thrown");
        } catch (UnexpectedException e) {
            // Expected
        }
    }

    @Test
    public void testReplaceTableName() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        String schema = "test_schema";
        String sqlContent = "ALTER TABLE test_table ADD COLUMN new_column INT";
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlContent(sqlContent);
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);

        String newDatabaseName = "new_database";
        String newTableName = "new_table";
        String expectedNewAlterStmt = "ALTER TABLE " + newDatabaseName + "." + newTableName + " ADD COLUMN new_column INT";

        List<OnlineSchemaChangeScheduleTaskParameters> taskParameters = parameters.generateSubTaskParameters(connectionConfig, schema);
        OnlineSchemaChangeScheduleTaskParameters taskParameter = taskParameters.get(0);
        TableIdentity tableIdentity = taskParameter.getTableIdentity();
        String databaseName = tableIdentity.getDatabaseName();
        String tableName = tableIdentity.getTableName();

        String newAlterStmt = DdlUtils.replaceTableName(sqlContent, newTableName, connectionConfig.getDialectType(), OnlineSchemaChangeSqlType.ALTER);
        assertEquals(expectedNewAlterStmt, newAlterStmt);
    }
}