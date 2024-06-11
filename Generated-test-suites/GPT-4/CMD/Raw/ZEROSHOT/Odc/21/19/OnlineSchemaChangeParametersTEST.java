
package com.oceanbase.odc.service.onlineschemachange.model;

import com.oceanbase.odc.core.flow.model.TaskParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.core.shared.model.TableIdentity;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

public class OnlineSchemaChangeParametersTest {
    private OnlineSchemaChangeParameters onlineSchemaChangeParams;
    private final ConnectionConfig connectionConfig = new ConnectionConfig();

    @Before
    public void setUp() {
        onlineSchemaChangeParams = new OnlineSchemaChangeParameters();
        onlineSchemaChangeParams.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        onlineSchemaChangeParams.setSqlContent("testSqlContent");
        onlineSchemaChangeParams.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        onlineSchemaChangeParams.setLockTableTimeOutSeconds(30);
        onlineSchemaChangeParams.setSwapTableNameRetryTimes(3);
        onlineSchemaChangeParams.setOriginTableCleanStrategy(new OriginTableCleanStrategy());
        onlineSchemaChangeParams.setDelimiter(";");
        onlineSchemaChangeParams.setLockUsers(Arrays.asList("User1", "User2"));
        onlineSchemaChangeParams.setSwapTableType(new SwapTableType());
        connectionConfig.setDialectType("MySQL");
    }

    @Test
    public void isContinueOnErrorTest() {
        Assert.assertEquals(TaskErrorStrategy.CONTINUE, onlineSchemaChangeParams.getErrorStrategy());
    }

    @Test
    public void generateSubTaskParametersTest() {
        List<OnlineSchemaChangeScheduleTaskParameters> taskParameters =
                onlineSchemaChangeParams.generateSubTaskParameters(connectionConfig, "testSchema");

        Assert.assertNotNull(taskParameters);
        Assert.assertNotEquals(0, taskParameters.size());
        
        for (OnlineSchemaChangeScheduleTaskParameters param : taskParameters){
            Assert.assertNotNull(param.getDatabaseName());
            Assert.assertNotNull(param.getOriginTableName());
            Assert.assertNotEquals(0, param.getSqlsToBeExecuted().size());
        }
    }
}
