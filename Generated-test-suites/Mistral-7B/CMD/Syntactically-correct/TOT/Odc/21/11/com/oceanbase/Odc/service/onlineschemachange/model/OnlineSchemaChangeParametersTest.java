import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.model.TableIdentity;
import com.oceanbase.odc.service.common.util.SqlUtils;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;

public class OnlineSchemaChangeParametersTest {

    private OnlineSchemaChangeParameters parameters;

    @Before
    public void setUp() {
        parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters.setSqlContent("CREATE TABLE IF NOT EXISTS test_table(id INT PRIMARY KEY) ; ALTER TABLE test_table ADD COLUMN col INT ;");
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        parameters.setLockTableTimeOutSeconds(10);
        parameters.setSwapTableType(SwapTableType.SEQUENTIAL);
        parameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.DROP_AND_RECREATE);
    }

    @Test
    public void testGetSqlType() {
        assertEquals(OnlineSchemaChangeSqlType.ALTER, parameters.getSqlType());
    }

    @Test
    public void testIsContinueOnError() {
        assertTrue(parameters.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        String schema = "test_schema";

        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameters.generateSubTaskParameters(connectionConfig, schema);
        assertEquals(2, subTaskParameters.size());

        OnlineSchemaChangeScheduleTaskParameters firstTaskParameters = subTaskParameters.get(0);
        TableIdentity key = new TableIdentity("test_schema", "test_table");
        assertEquals(key, firstTaskParameters.getKey());
        assertEquals("CREATE TABLE IF NOT EXISTS test_table(id INT PRIMARY KEY)", firstTaskParameters.getSqlsToBeExecuted().get(0));
        assertEquals("ALTER TABLE test_table ADD COLUMN col INT", firstTaskParameters.getSqlsToBeExecuted().get(1));

        OnlineSchemaChangeScheduleTaskParameters secondTaskParameters = subTaskParameters.get(1);
        key = new TableIdentity("test_schema", "test_table");
        assertEquals(key, secondTaskParameters.getKey());
        assertNull(secondTaskParameters.getSqlsToBeExecuted());

        String newAlterStmt = DdlUtils.replaceTableName("ALTER TABLE test_table ADD COLUMN col INT", "new_test_table",
                connectionConfig.getDialectType(), OnlineSchemaChangeSqlType.ALTER);
        assertEquals(newAlterStmt, secondTaskParameters.getSqlsToBeExecuted().get(0));
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParametersWithException() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        String schema = null;
        parameters.generateSubTaskParameters(connectionConfig, schema);
    }

    @Test
    public void testGetErrorStrategy() {
        assertEquals(TaskErrorStrategy.CONTINUE, parameters.getErrorStrategy());
    }

    @Test
    public void testSetErrorStrategy() {
        parameters.setErrorStrategy(TaskErrorStrategy.ABORT);
        assertEquals(TaskErrorStrategy.ABORT, parameters.getErrorStrategy());
    }

    @Test
    public void testGetLockTableTimeOutSeconds() {
        assertEquals(10, parameters.getLockTableTimeOutSeconds());
    }

    @Test
    public void testSetLockTableTimeOutSeconds() {
        parameters.setLockTableTimeOutSeconds(20);
        assertEquals(20, parameters.getLockTableTimeOutSeconds());
    }

    @Test
    public void testGetSwapTableType() {
        assertEquals(SwapTableType.SEQUENTIAL, parameters.getSwapTableType());
    }

    @Test
    public void testSetSwapTableType() {
        parameters.setSwapTableType(SwapTableType.PARALLEL);
        assertEquals(SwapTableType.PARALLEL, parameters.getSwapTableType());
    }

    @Test
    public void testGetOriginTableCleanStrategy() {
        assertEquals(OriginTableCleanStrategy.DROP_AND_RECREATE, parameters.getOriginTableCleanStrategy());
    }

    @Test
    public void testSetOriginTableCleanStrategy() {
        parameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.TRUNCATE);
        assertEquals(OriginTableCleanStrategy.TRUNCATE, parameters.getOriginTableCleanStrategy());
    }

    @Test
    public void testSetSqlContentWithEmptyString() {
        parameters.setSqlContent("");
        assertEquals("", parameters.getSqlContent());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSqlContentWithNull() {
        parameters.setSqlContent(null);
    }

    @Test
    public void testSetSqlContentWithValidContent() {
        String sqlContent = "CREATE TABLE IF NOT EXISTS test_table(id INT PRIMARY KEY) ; ALTER TABLE test_table ADD COLUMN col INT ;";
        parameters.setSqlContent(sqlContent);
        List<String> sqls = SqlUtils.split(connectionConfig.getDialectType(), sqlContent, parameters.getDelimiter());
        assertEquals(2, sqls.size());
        assertEquals("CREATE TABLE IF NOT EXISTS test_table(id INT PRIMARY KEY)", sqls.get(0));
        assertEquals("ALTER TABLE test_table ADD COLUMN col INT", sqls.get(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSqlContentWithInvalidDelimiter() {
        String sqlContent = "CREATE TABLE IF NOT EXISTS test_table(id INT PRIMARY KEY) ; ALTER TABLE test_table ADD COLUMN col INT _";
        parameters.setSqlContent(sqlContent);
    }

    @Test
    public void testSetLockUsers() {
        List<String> lockUsers = Arrays.asList("user1", "user2");
        parameters.setLockUsers(lockUsers);
        assertEquals(lockUsers, parameters.getLockUsers());
    }

    @Test
    public void testSetLockUsersWithEmptyList() {
        parameters.setLockUsers(Collections.emptyList());
        assertNull(parameters.getLockUsers());
    }
}