import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.service.common.util.SqlUtils;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;

/**
 * Test class for OnlineSchemaChangeParameters
 *
 * @author yaobin
 * @since 4.2.0
 */
public class OnlineSchemaChangeParametersTest {

    /**
     * Test constructor with default values
     */
    @Test
    public void testCreateDefaultOnlineSchemaChangeParameters() {
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

    /**
     * Test constructor with all parameters
     */
    @Test
    public void testCreateOnlineSchemaChangeParameters() {
        OnlineSchemaChangeSqlType sqlType = OnlineSchemaChangeSqlType.ALTER;
        String sqlContent = "CREATE TABLE new_table (id INT PRIMARY KEY);";
        TaskErrorStrategy errorStrategy = TaskErrorStrategy.IGNORE;
        Integer lockTableTimeOutSeconds = 30;
        Integer swapTableNameRetryTimes = 5;
        OriginTableCleanStrategy originTableCleanStrategy = OriginTableCleanStrategy.DROP;
        String delimiter = ";";
        List<String> lockUsers = Arrays.asList("user1", "user2");
        SwapTableType swapTableType = SwapTableType.PRE_SWAP;

        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters(sqlType, sqlContent, errorStrategy,
                lockTableTimeOutSeconds, swapTableNameRetryTimes, originTableCleanStrategy, delimiter, lockUsers, swapTableType);

        assertEquals(sqlType, parameters.getSqlType());
        assertEquals(sqlContent, parameters.getSqlContent());
        assertEquals(errorStrategy, parameters.getErrorStrategy());
        assertEquals(lockTableTimeOutSeconds, parameters.getLockTableTimeOutSeconds());
        assertEquals(swapTableNameRetryTimes, parameters.getSwapTableNameRetryTimes());
        assertEquals(originTableCleanStrategy, parameters.getOriginTableCleanStrategy());
        assertEquals(delimiter, parameters.getDelimiter());
        assertEquals(lockUsers, parameters.getLockUsers());
        assertEquals(swapTableType, parameters.getSwapTableType());

        assertFalse(parameters.isContinueOnError());
    }

    /**
     * Test isContinueOnError method
     */
    @Test
    public void testIsContinueOnErrorWithContinueErrorStrategy() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(parameters.isContinueOnError());
    }

    /**
     * Test isContinueOnError method
     */
    @Test
    public void testIsContinueOnErrorWithIgnoreErrorStrategy() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setErrorStrategy(TaskErrorStrategy.IGNORE);
        assertFalse(parameters.isContinueOnError());
    }

    /**
     * Test generateSubTaskParameters method
     */
    @Test
    public void testGenerateSubTaskParameters() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        String schema = "test_schema";

        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters.setSqlContent("ALTER TABLE old_table ADD COLUMN new_column INT;");
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        parameters.setLockTableTimeOutSeconds(30);
        parameters.setSwapTableNameRetryTimes(5);
        parameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.DROP);
        parameters.setDelimiter(";");
        parameters.setLockUsers(Arrays.asList("user1", "user2"));
        parameters.setSwapTableType(SwapTableType.PRE_SWAP);

        List<OnlineSchemaChangeScheduleTaskParameters> taskParameters = parameters.generateSubTaskParameters(connectionConfig, schema);

        assertEquals(1, taskParameters.size());
        OnlineSchemaChangeScheduleTaskParameters taskParameter = taskParameters.get(0);

        assertEquals("test_schema", taskParameter.getDatabaseName());
        assertEquals("old_table", taskParameter.getOriginTableName());
        assertEquals(OnlineSchemaChangeSqlType.ALTER, taskParameter.getSqlType());
        assertEquals("ALTER TABLE old_table ADD COLUMN new_column INT;", taskParameter.getSqlContent());
        assertEquals(TaskErrorStrategy.CONTINUE, taskParameter.getErrorStrategy());
        assertEquals(30, taskParameter.getLockTableTimeOutSeconds());
        assertEquals(5, taskParameter.getSwapTableNameRetryTimes());
        assertEquals(OriginTableCleanStrategy.DROP, taskParameter.getOriginTableCleanStrategy());
        assertEquals(";", taskParameter.getDelimiter());
        assertEquals(Arrays.asList("user1", "user2"), taskParameter.getLockUsers());
        assertEquals(SwapTableType.PRE_SWAP, taskParameter.getSwapTableType());
        assertEquals(1, taskParameter.getSqlsToBeExecuted().size());
        assertEquals("ALTER TABLE old_table ADD COLUMN new_column INT;", taskParameter.getSqlsToBeExecuted().get(0));

        String newAlterStmt = DdlUtils.replaceTableName("ALTER TABLE old_table ADD COLUMN new_column INT;", "new_table",
                connectionConfig.getDialectType(), OnlineSchemaChangeSqlType.ALTER);
        assertEquals("ALTER TABLE new_table ADD COLUMN new_column INT;", newAlterStmt);
        assertEquals(1, taskParameter.getSqlsToBeExecuted().size());
        assertEquals(newAlterStmt, taskParameter.getSqlsToBeExecuted().get(0));
    }
}