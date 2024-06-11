package com.oceanbase.odc.service.onlineschemachange.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import java.util.List;
import java.util.Map;

import com.oceanbase.odc.core.flow.model.TaskParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;

import org.junit.Test;

public class OnlineSchemaChangeParametersTest {

    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(parameters.isContinueOnError());

        parameters.setErrorStrategy(TaskErrorStrategy.STOP);
        assertFalse(parameters.isContinueOnError());
    }
    
    @Test
    public void testGenerateSubTaskParameters(){
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters.setSqlContent("SELECT * FROM test_table;DROP test_table;");
        parameters.setDelimiter(";");
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        parameters.setLockTableTimeOutSeconds(10);
        parameters.setSwapTableNameRetryTimes(5);
        parameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.DELETE_DATA);
        parameters.setSwapTableType(SwapTableType.TABLE_NAME);

        ConnectionConfig config = new ConnectionConfig();
        config.setDialectType("mysql");

        try {
            List<OnlineSchemaChangeScheduleTaskParameters> subTasks = parameters.generateSubTaskParameters(config, "test_schema");
            assertEquals(2, subTasks.size());
        } catch (UnexpectedException ue) {
            ue.printStackTrace();
        }

        parameters.setSqlContent(null);
        try {
            List<OnlineSchemaChangeScheduleTaskParameters> subTasks = parameters.generateSubTaskParameters(config, "test_schema");
            assertNull(subTasks);
        } catch (UnexpectedException ue) {
            ue.printStackTrace();
        }
    }
}