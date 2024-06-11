import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.*;

import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.onlineschemachange.model.TableIdentity;
import com.oceanbase.odc.service.onlineschemachange.model.TaskErrorStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OnlineSchemaChangeParametersTest {

    @Test
    public void testConstructor() {
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
    public void testGettersAndSetters() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();

        OnlineSchemaChangeSqlType sqlType = OnlineSchemaChangeSqlType.ALTER;
        parameters.setSqlType(sqlType);
        assertEquals(sqlType, parameters.getSqlType());

        String sqlContent = "test_sql_content";
        parameters.setSqlContent(sqlContent);
        assertEquals(sqlContent, parameters.getSqlContent());

        TaskErrorStrategy errorStrategy = TaskErrorStrategy.CONTINUE;
        parameters.setErrorStrategy(errorStrategy);
        assertEquals(errorStrategy, parameters.getErrorStrategy());

        parameters.setLockTableTimeOutSeconds(5);
        assertEquals(5, parameters.getLockTableTimeOutSeconds().intValue());

        parameters.setSwapTableNameRetryTimes(3);
        assertEquals(3, parameters.getSwapTableNameRetryTimes().intValue());

        OnlineSchemaChangeParameters.OriginTableCleanStrategy originTableCleanStrategy = OnlineSchemaChangeParameters.OriginTableCleanStrategy.DROP_TABLE;
        parameters.setOriginTableCleanStrategy(originTableCleanStrategy);
        assertEquals(originTableCleanStrategy, parameters.getOriginTableCleanStrategy());

        String delimiter = ";";
        parameters.setDelimiter(delimiter);
        assertEquals(delimiter, parameters.getDelimiter());

        List<String> lockUsers = new ArrayList<>();
        lockUsers.add("test_user_1");
        parameters.setLockUsers(lockUsers);
        assertEquals(lockUsers, parameters.getLockUsers());

        SwapTableType swapTableType = SwapTableType.REPLACE;
        parameters.setSwapTableType(swapTableType);
        assertEquals(swapTableType, parameters.getSwapTableType());
    }

    @Test(expected = AssertionError.class)
    public void testIsContinueOnErrorWithUnexpectedErrorStrategy() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setErrorStrategy(TaskErrorStrategy.UNEXPECTED);
        assertFalse(parameters.isContinueOnError());
    }

    @Test
    public void testIsContinueOnErrorWithExpectedErrorStrategy() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(parameters.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParametersWithValidInput() throws Exception {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters.setSqlContent("ALTER TABLE test_table ADD COLUMN test_column INT;");
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        parameters.setLockTableTimeOutSeconds(5);
        parameters.setSwapTableNameRetryTimes(3);
        parameters.setOriginTableCleanStrategy(OnlineSchemaChangeParameters.OriginTableCleanStrategy.DROP_TABLE);
        parameters.setDelimiter(";");
        parameters.setLockUsers(new ArrayList<>());
        parameters.setSwapTableType(SwapTableType.REPLACE);

        String dbName = "test_database";;
        String schema = "test_schema";
        String newTableName = "test_new_table";

        List<OnlineSchemaChangeScheduleTaskParameters> taskParameters = parameters.generateSubTaskParameters(connectionConfig, schema);
        assertEquals(1, taskParameters.size());

        OnlineSchemaChangeScheduleTaskParameters taskParameter = taskParameters.get(0);
        assertEquals(dbName, taskParameter.getDatabaseName());
        assertEquals("test_table", taskParameter.getOriginTableName());
        assertEquals(OnlineSchemaChangeSqlType.ALTER, taskParameter.getSqlType());
        assertEquals("ALTER TABLE test_table ADD COLUMN test_column INT;", taskParameter.getSqlContent());
        assertEquals("test_database", taskParameter.getNewDatabaseName());
        assertEquals("test_new_table", taskParameter.getNewTableName());
        assertTrue(taskParameter.getSqlsToBeExecuted().size() > 1);
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParametersWithException() throws Exception {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters.setSqlContent("ALTER TABLE test_table ADD COLUMN test_column INT;");
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        parameters.setLockTableTimeOutSeconds(5);
        parameters.setSwapTableNameRetryTimes(3);
        parameters.setOriginTableCleanStrategy(OnlineSchemaChangeParameters.OriginTableCleanStrategy.DROP_TABLE);
        parameters.setDelimiter(";");
        parameters.setLockUsers(new ArrayList<>());
        parameters.setSwapTableType(SwapTableType.REPLACE);

        String dbName = "test_database";;
        String schema = "test_schema";
        String newTableName = null;

        List<OnlineSchemaChangeScheduleTaskParameters> taskParameters = parameters.generateSubTaskParameters(connectionConfig, schema);
        fail("Expected UnexpectedException");
    }

}