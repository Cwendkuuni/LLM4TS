import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.oceanbase.odc.core.connection.ConnectionConfig;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;

public class OnlineSchemaChangeParametersTest {

    @Test
    public void testCreateOnlineSchemaChangeParameters() {
        String delimiter = ";";
        String sqlContent = "ALTER TABLE table1 ADD COLUMN col1 INT; \nCREATE TABLE table2 (id INT PRIMARY KEY);";
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        params.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        params.setSqlContent(sqlContent);
        params.setDelimiter(delimiter);

        List<OnlineSchemaChangeScheduleTaskParameters> expected = createExpectedTaskParameters();
        List<OnlineSchemaChangeScheduleTaskParameters> actual = params.generateSubTaskParameters(getConnectionConfig(), "test_schema");

        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); ++i) {
            OnlineSchemaChangeScheduleTaskParameters expectedTaskParam = expected.get(i);
            OnlineSchemaChangeScheduleTaskParameters actualTaskParam = actual.get(i);
            assertEquals(expectedTaskParam.getSqlsToBeExecuted().size(), actualTaskParam.getSqlsToBeExecuted().size());
            for (int j = 0; j < expectedTaskParam.getSqlsToBeExecuted().size(); ++j) {
                String expectedSql = expectedTaskParam.getSqlsToBeExecuted().get(j);
                String actualSql = actualTaskParam.getSqlsToBeExecuted().get(j);
                assertEquals(expectedSql, actualSql);
            }
        }
    }

    @Test
    public void testCreateOnlineSchemaChangeParametersWithSwapTable() {
        String delimiter = ";";
        String sqlContent = "ALTER TABLE table1 RENAME TO table1_old; \nCREATE TABLE table1 (id INT PRIMARY KEY);";
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        params.setSqlType(OnlineSchemaChangeSqlType.RENAME);
        params.setSwapTableType(SwapTableType.SWAP);
        params.setSqlContent(sqlContent);
        params.setDelimiter(delimiter);

        List<OnlineSchemaChangeScheduleTaskParameters> expected = createExpectedSwapTableTaskParameters();
        List<OnlineSchemaChangeScheduleTaskParameters> actual = params.generateSubTaskParameters(getConnectionConfig(), "test_schema");

        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); ++i) {
            OnlineSchemaChangeScheduleTaskParameters expectedTaskParam = expected.get(i);
            OnlineSchemaChangeScheduleTaskParameters actualTaskParam = actual.get(i);
            assertEquals(expectedTaskParam.getSwapTableName(), actualTaskParam.getSwapTableName());
            assertEquals(expectedTaskParam.getSqlsToBeExecuted().size(), actualTaskParam.getSqlsToBeExecuted().size());
            for (int j = 0; j < expectedTaskParam.getSqlsToBeExecuted().size(); ++j) {
                String expectedSql = expectedTaskParam.getSqlsToBeExecuted().get(j);
                String actualSql = actualTaskParam.getSqlsToBeExecuted().get(j);
                assertEquals(expectedSql, actualSql);
            }
        }
    }

    @Test
    public void testCreateOnlineSchemaChangeParametersWithoutSwapTable() {
        String delimiter = ";";
        String sqlContent = "ALTER TABLE table1 ADD COLUMN col1 INT; \nCREATE TABLE table2 (id INT PRIMARY KEY);";
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        params.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        params.setSqlContent(sqlContent);
        params.setDelimiter(delimiter);

        List<OnlineSchemaChangeScheduleTaskParameters> expected = createExpectedTaskParameters();
        List<OnlineSchemaChangeScheduleTaskParameters> actual = params.generateSubTaskParameters(getConnectionConfig(), "test_schema");

        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); ++i) {
            OnlineSchemaChangeScheduleTaskParameters expectedTaskParam = expected.get(i);
            OnlineSchemaChangeScheduleTaskParameters actualTaskParam = actual.get(i);
            assertEquals(expectedTaskParam.getSwapTableName(), null);
            assertEquals(expectedTaskParam.getSqlsToBeExecuted().size(), actualTaskParam.getSqlsToBeExecuted().size());
            for (int j = 0; j < expectedTaskParam.getSqlsToBeExecuted().size(); ++j) {
                String expectedSql = expectedTaskParam.getSqlsToBeExecuted().get(j);
                String actualSql = actualTaskParam.getSqlsToBeExecuted().get(j);
                assertEquals(expectedSql, actualSql);
            }
        }
    }

    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        params.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(params.isContinueOnError());

        params.setErrorStrategy(TaskErrorStrategy.FAIL);
        assertFalse(params.isContinueOnError());
    }

    @Test
    public void testGetNullValues() {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        assertNull(params.getSqlContent());
        assertNull(params.getSwapTableName());
        assertNull(params.getLockUsers());
        assertNull(params.getLockTableTimeOutSeconds());
        assertNull(params.getSwapTableType());
        assertNull(params.getOriginTableCleanStrategy());
    }

    @Test
    public void testGetNonNullValues() {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        params.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        params.setSqlContent("ALTER TABLE table1 ADD COLUMN col1 INT; \nCREATE TABLE table2 (id INT PRIMARY KEY);");
        params.setDelimiter(";");
        params.setSwapTableType(SwapTableType.NO_SWAP);
        params.setLockTableTimeOutSeconds(123);
        params.setSwapTableName("swap_table");
        params.setLockUsers(Arrays.asList("user1", "user2"));
        params.setOriginTableCleanStrategy(OriginTableCleanStrategy.KEEP);

        assertNotNull(params.getSqlType());
        assertNotNull(params.getSqlContent());
        assertNotNull(params.getSwapTableName());
        assertNotNull(params.getLockUsers());
        assertNotNull(params.getLockTableTimeOutSeconds());
        assertNotNull(params.getSwapTableType());
        assertNotNull(params.getOriginTableCleanStrategy());
    }

    private ConnectionConfig getConnectionConfig() {
        return new ConnectionConfig();
    }

    private List<OnlineSchemaChangeScheduleTaskParameters> createExpectedTaskParameters() {
        List<String> sqlsToBeExecuted_1 = Arrays.asList("ALTER TABLE table1 ADD COLUMN col1 INT");
        List<String> sqlsToBeExecuted_2 = Arrays.asList("CREATE TABLE table2 (id INT PRIMARY KEY)");
        OnlineSchemaChangeScheduleTaskParameters taskParam_1 = new OnlineSchemaChangeScheduleTaskParameters();
        taskParam_1.setSwapTableName(null);
        taskParam_1.setSqlsToBeExecuted(sqlsToBeExecuted_1);

        OnlineSchemaChangeScheduleTaskParameters taskParam_2 = new OnlineSchemaChangeScheduleTaskParameters();
        taskParam_2.setSwapTableName(null);
        taskParam_2.setSqlsToBeExecuted(sqlsToBeExecuted_2);

        return Arrays.asList(taskParam_1, taskParam_2);
    }

    private List<OnlineSchemaChangeScheduleTaskParameters> createExpectedSwapTableTaskParameters() {
        List<String> sqlsToBeExecuted_1 = Arrays.asList("ALTER TABLE table1 RENAME TO table1_old");
        List<String> sqlsToBeExecuted_2 = Arrays.asList("CREATE TABLE table1 (id INT PRIMARY KEY)");
        String swapTableName = "swap_table_table1";

        OnlineSchemaChangeScheduleTaskParameters taskParam_1 = new OnlineSchemaChangeScheduleTaskParameters();
        taskParam_1.setSwapTableName(swapTableName);
        taskParam_1.setSqlsToBeExecuted(sqlsToBeExecuted_1);

        OnlineSchemaChangeScheduleTaskParameters taskParam_2 = new OnlineSchemaChangeScheduleTaskParameters();
        taskParam_2.setSwapTableName(swapTableName);
        taskParam_2.setSqlsToBeExecuted(sqlsToBeExecuted_2);

        return Arrays.asList(taskParam_1, taskParam_2);
    }
}