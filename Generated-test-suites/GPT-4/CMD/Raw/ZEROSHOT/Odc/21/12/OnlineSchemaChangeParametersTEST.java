
import org.junit.Test;
import org.junit.Assert;
import java.util.*;

import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;

public class OnlineSchemaChangeParametersTest {
    private OnlineSchemaChangeParameters setupTestParameters() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlType(OnlineSchemaChangeSqlType.CREATE);
        parameters.setSqlContent("test SQL");
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        parameters.setLockTableTimeOutSeconds(10);
        parameters.setSwapTableNameRetryTimes(5);
        parameters.setDelimiter(";");
        List<String> lockUsers = Arrays.asList("user1","user2");
        parameters.setLockUsers(lockUsers);
        parameters.setSwapTableType(SwapTableType.INPLACE);

        return parameters;
    }

    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters parameters = setupTestParameters();
        Assert.assertTrue(parameters.isContinueOnError());
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParametersWithException() {
        OnlineSchemaChangeParameters parameters = setupTestParameters();
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("MySQL");
        parameters.generateSubTaskParameters(connectionConfig, "test-schema");
    }

    @Test
    public void testSqlsToBeExecuted() {
        OnlineSchemaChangeParameters parameters = setupTestParameters();
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("MySQL");
        parameters.generateSubTaskParameters(connectionConfig, "test-schema");
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        String sql = "ALTER TABLE test";
        Assert.assertEquals(DdlUtils.replaceTableName(sql, "newTest", "MySQL", OnlineSchemaChangeSqlType.ALTER),
                parameters.generateSubTaskParameters(connectionConfig, "test-schema").get(0).getSqlsToBeExecuted().get(0));
    }
}
