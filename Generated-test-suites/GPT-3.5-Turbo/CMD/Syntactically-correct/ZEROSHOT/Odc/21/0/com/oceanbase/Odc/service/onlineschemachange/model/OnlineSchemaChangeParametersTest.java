package com.oceanbase.odc.service.onlineschemachange.model;

import com.oceanbase.odc.core.flow.model.TaskParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.core.shared.model.TableIdentity;
import com.oceanbase.odc.service.common.util.SqlUtils;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class OnlineSchemaChangeParametersTest {

    private OnlineSchemaChangeParameters testParameters;

    @Before
    public void setUp() {
        testParameters = new OnlineSchemaChangeParameters();
        testParameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        testParameters.setSqlContent("ALTER TABLE test ADD column1 VARCHAR(255);");
        testParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        testParameters.setLockTableTimeOutSeconds(10);
        testParameters.setSwapTableNameRetryTimes(3);
        testParameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.CLEAN);
        testParameters.setDelimiter(";");
        testParameters.setLockUsers(new ArrayList<>());
        testParameters.setSwapTableType(SwapTableType.LOCK);
    }

    @Test
    public void testIsContinueOnError() {
        assertTrue(testParameters.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("mysql");
        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = testParameters.generateSubTaskParameters(connectionConfig, "schema");
        assertEquals(1, subTaskParameters.size());
    }

}