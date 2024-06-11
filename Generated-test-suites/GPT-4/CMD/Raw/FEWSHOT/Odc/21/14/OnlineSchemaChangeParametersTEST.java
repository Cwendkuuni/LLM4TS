package com.oceanbase.odc.service.onlineschemachange.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import com.oceanbase.odc.core.flow.model.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.service.onlineschemachange.model.OriginTableCleanStrategy;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;

import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

public class OnlineSchemaChangeParametersTest {
    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
        onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(onlineSchemaChangeParameters.isContinueOnError());

        onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.STOP);
        assertFalse(onlineSchemaChangeParameters.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() {
        OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
        onlineSchemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        onlineSchemaChangeParameters.setSqlContent("ALTER TABLE myTable ADD myColumn VARCHAR(255)");
        onlineSchemaChangeParameters.setDelimiter(";");
        onlineSchemaChangeParameters.setLockTableTimeOutSeconds(30);
        onlineSchemaChangeParameters.setSwapTableNameRetryTimes(3);
        onlineSchemaChangeParameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.DROP);
        List<String> users = new ArrayList<>();
        users.add("user1");
        users.add("user2");
        onlineSchemaChangeParameters.setLockUsers(users);
        onlineSchemaChangeParameters.setSwapTableType(SwapTableType.NEW);

        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.MYSQL);

        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = onlineSchemaChangeParameters
                .generateSubTaskParameters(connectionConfig, "mySchema");

        assertEquals(1, subTaskParameters.size());
        assertEquals("ALTER TABLE newTable ADD myColumn VARCHAR(255)",
                subTaskParameters.get(0).getSqlsToBeExecuted().get(0));
    }
}