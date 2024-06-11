import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.oceanbase.odc.core.connection.model.ConnectionConfig;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.core.shared.model.TableIdentity;
import com.oceanbase.odc.service.common.util.SqlUtils;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.onlineschemachange.model.OriginTableCleanStrategy;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;

public class OnlineSchemaChangeParametersTest {

    @Test
    public void testOnlineSchemaChangeParameters() {
        // Given
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        ConnectionConfig connectionConfig = new ConnectionConfig();
        String schema = "test_schema";

        // When setting properties
        parameters.setSqlType(OnlineSchemaChangeSqlType.CREATE);
        parameters.setSqlContent("CREATE TABLE test (id INT PRIMARY KEY);");
        parameters.setErrorStrategy(TaskErrorStrategy.ERROR);
        parameters.setLockTableTimeOutSeconds(30);
        parameters.setSwapTableType(SwapTableType.ASYNC);
        parameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.KEEP);
        parameters.setSwapTableNameRetryTimes(3);
        parameters.setDelimiter(";");
        parameters.setLockUsers(new ArrayList<>());

        // Then check properties
        assertEquals(OnlineSchemaChangeSqlType.CREATE, parameters.getSqlType());
        assertEquals("CREATE TABLE test (id INT PRIMARY KEY);", parameters.getSqlContent());
        assertEquals(TaskErrorStrategy.ERROR, parameters.getErrorStrategy());
        assertEquals(30, parameters.getLockTableTimeOutSeconds());
        assertEquals(SwapTableType.ASYNC, parameters.getSwapTableType());
        assertEquals(OriginTableCleanStrategy.KEEP, parameters.getOriginTableCleanStrategy());
        assertEquals(3, parameters.getSwapTableNameRetryTimes());
        assertEquals(";", parameters.getDelimiter());
        assertNotNull(parameters.getLockUsers());
        assertNull(parameters.getSqlContent().trim().isEmpty());
        assertFalse(parameters.isContinueOnError());

        // When generating subtasks
        List<OnlineSchemaChangeScheduleTaskParameters> subTasks = parameters.generateSubTaskParameters(connectionConfig, schema);
        assertEquals(1, subTasks.size());
        OnlineSchemaChangeScheduleTaskParameters subTask = subTasks.get(0);
        assertEquals("test_schema", subTask.getDatabaseName());
        assertEquals("test", subTask.getOriginTableName());
        assertEquals(OnlineSchemaChangeSqlType.CREATE, subTask.getSqlType());
        assertEquals("CREATE TABLE test (id INT PRIMARY KEY);", subTask.getSqlContent());
        assertNull(subTask.getNewTableName());
        assertNotNull(subTask.getSqlsToBeExecuted());
        assertEquals(1, subTask.getSqlsToBeExecuted().size());
        assertEquals("CREATE TABLE test (id INT PRIMARY KEY);", subTask.getSqlsToBeExecuted().get(0));

        // When generating altered subtasks
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters.setSqlContent("ALTER TABLE test ADD COLUMN col INT;");
        subTasks = parameters.generateSubTaskParameters(connectionConfig, schema);
        assertEquals(1, subTasks.size());
        subTask = subTasks.get(0);
        assertEquals("test_schema", subTask.getDatabaseName());
        assertEquals("test", subTask.getOriginTableName());
        assertEquals(OnlineSchemaChangeSqlType.ALTER, subTask.getSqlType());
        assertEquals("ALTER TABLE test ADD COLUMN col INT;", subTask.getSqlContent());
        assertEquals("test", subTask.getNewTableName());
        assertNotNull(subTask.getSqlsToBeExecuted());
        assertEquals(2, subTask.getSqlsToBeExecuted().size());
        String newAlterStmt = DdlUtils.replaceTableName(subTask.getSqlContent(), subTask.getNewTableName(),
                connectionConfig.getDialectType(), OnlineSchemaChangeSqlType.ALTER);
        assertEquals("ALTER TABLE test RENAME TO test_new;", subTask.getSqlsToBeExecuted().get(0));
        assertEquals(newAlterStmt, subTask.getSqlsToBeExecuted().get(1));
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTasksWithException() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        ConnectionConfig connectionConfig = new ConnectionConfig();
        String schema = "test_schema";

        // Given
        parameters.setSqlType(OnlineSchemaChangeSqlType.CREATE);
        parameters.setSqlContent("CREATE TABLE test (id INT PRIMARY KEY);");
        parameters.setErrorStrategy(TaskErrorStrategy.ERROR);
        parameters.setLockTableTimeOutSeconds(30);
        parameters.setSwapTableType(SwapTableType.ASYNC);
        parameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.KEEP);
        parameters.setSwapTableNameRetryTimes(3);
        parameters.setDelimiter(";");
        parameters.setLockUsers(new ArrayList<>());

        // When generating subtasks with exception
        try {
            List<OnlineSchemaChangeScheduleTaskParameters> subTasks = parameters.generateSubTaskParameters(connectionConfig, schema);
            fail("Unexpected subtask generation");
        } catch (UnexpectedException e) {
            // Expected exception
        }
    }
}