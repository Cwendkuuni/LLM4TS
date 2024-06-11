import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.model.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.model.OriginTableCleanStrategy;
import com.oceanbase.odc.model.SwapTableType;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;

public class OnlineSchemaChangeParametersTest {

    @Test
    public void testNewOnlineSchemaChangeParameters() {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        assertNull(params.getSqlType());
        assertNull(params.getSqlContent());
        assertNull(params.getErrorStrategy());
        assertNull(params.getLockTableTimeOutSeconds());
        assertNull(params.getSwapTableNameRetryTimes());
        assertNull(params.getOriginTableCleanStrategy());
        assertNull(params.getDelimiter());
        assertNull(params.getLockUsers());
        assertNull(params.getSwapTableType());
        assertFalse(params.isContinueOnError());
        assertNull(params.generateSubTaskParameters(null, null));
    }

    @Test
    public void testNewOnlineSchemaChangeParametersWithValues() {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        params.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        params.setSqlContent("sqlContent");
        params.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        params.setLockTableTimeOutSeconds(123);
        params.setSwapTableNameRetryTimes(456);
        params.setOriginTableCleanStrategy(OriginTableCleanStrategy.TRUNCATE);
        params.setDelimiter(";");
        params.setLockUsers(new ArrayList<>(Arrays.asList("user1", "user2")));
        params.setSwapTableType(SwapTableType.READ_WRITE);
        assertTrue(params.isContinueOnError());

        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = params.generateSubTaskParameters(null, null);
        assertNull(subTaskParameters);
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParametersWithException() {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        params.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        params.setSqlContent("sqlContent");
        params.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        params.setLockTableTimeOutSeconds(123);
        params.setSwapTableNameRetryTimes(456);
        params.setOriginTableCleanStrategy(OriginTableCleanStrategy.TRUNCATE);
        params.setDelimiter(";");
        params.setLockUsers(new ArrayList<>(Arrays.asList("user1", "user2")));
        params.setSwapTableType(SwapTableType.READ_WRITE);

        Map<String, String> connectionConfig = new HashMap<>();
        Map<String, OnlineSchemaChangeScheduleTaskParameters> expectedTaskParameters = new HashMap<>();
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters();
        taskParameter.setDatabaseName("dbName");
        taskParameter.setOriginTableName("originTableName");
        taskParameter.setNewTableName("newTableName");
        expectedTaskParameters.put("originTableName", taskParameter);

        try (SubTaskParameterFactory subTaskParameterFactory = new SubTaskParameterFactory(null, null)) {
            List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = params.generateSubTaskParameters(null, null);
            assertEquals(expectedTaskParameters, subTaskParameters);
        } catch (Exception e) {
            throw new UnexpectedException("Failed to generate subtasks with parameter: " + params, e);
        }
    }

    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters params1 = new OnlineSchemaChangeParameters();
        params1.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(params1.isContinueOnError());

        OnlineSchemaChangeParameters params2 = new OnlineSchemaChangeParameters();
        params2.setErrorStrategy(TaskErrorStrategy.FAIL);
        assertFalse(params2.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParametersWithNullConnectionConfigAndSchema() {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        params.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        params.setSqlContent("sqlContent");
        params.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        params.setLockTableTimeOutSeconds(123);
        params.setSwapTableNameRetryTimes(456);
        params.setOriginTableCleanStrategy(OriginTableCleanStrategy.TRUNCATE);
        params.setDelimiter(";");
        params.setLockUsers(new ArrayList<>(Arrays.asList("user1", "user2")));
        params.setSwapTableType(SwapTableType.READ_WRITE);

        assertThrows(UnexpectedException.class, () -> params.generateSubTaskParameters(null, null));
    }

    @Test
    public void testGenerateSubTaskParametersWithEmptySqlContent() {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        params.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        params.setSqlContent("");
        params.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        params.setLockTableTimeOutSeconds(123);
        params.setSwapTableNameRetryTimes(456);
        params.setOriginTableCleanStrategy(OriginTableCleanStrategy.TRUNCATE);
        params.setDelimiter(";");
        params.setLockUsers(new ArrayList<>(Arrays.asList("user1", "user2")));
        params.setSwapTableType(SwapTableType.READ_WRITE);

        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = params.generateSubTaskParameters(null, null);
        assertNull(subTaskParameters);
    }

    @Test
    public void testGenerateSubTaskParametersWithEmptySqls() {
        OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
        params.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        params.setSqlContent("sqlContent");
        params.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        params.setLockTableTimeOutSeconds(123);
        params.setSwapTableNameRetryTimes(456);
        params.setOriginTableCleanStrategy(OriginTableCleanStrategy.TRUNCATE);
        params.setDelimiter(";");
        params.setLockUsers(new ArrayList<>(Arrays.asList("user1", "user2")));
        params.setSwapTableType(SwapTableType.READ_WRITE);

        String sqlContent = "sqlContent";
        String[] sqls = SqlUtils.split(null, sqlContent, params.getDelimiter()).toArray(new String[0]);

        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = params.generateSubTaskParameters(null, null);
        assertNull(subTaskParameters);
    }
}