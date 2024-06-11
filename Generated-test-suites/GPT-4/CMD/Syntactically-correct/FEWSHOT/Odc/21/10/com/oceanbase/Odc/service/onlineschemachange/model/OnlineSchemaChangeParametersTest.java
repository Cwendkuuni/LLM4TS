package com.oceanbase.odc.service.onlineschemachange.model;

import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;
import org.junit.Before;
import org.junit.Test;
import java.util.*;

import static org.junit.Assert.*;

public class OnlineSchemaChangeParametersTest {
    private OnlineSchemaChangeParameters onlineSchemaChangeParameters;
    private ConnectionConfig connectionConfig;

    @Before
    public void setUp() {
        onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
        connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType("MySQL");
    }

    @Test
    public void testIsContinueOnError() {
        onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(onlineSchemaChangeParameters.isContinueOnError());

        onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.TERMINATE);
        assertFalse(onlineSchemaChangeParameters.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() {
        List<String> sqls = new ArrayList<>(Collections.singletonList("SELECT * FROM tableName"));
        onlineSchemaChangeParameters.setSqlContent(String.join(onlineSchemaChangeParameters.getDelimiter(), sqls));
        onlineSchemaChangeParameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        List<OnlineSchemaChangeScheduleTaskParameters> parametersList = onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, "test_schema");

        assertNotNull(parametersList);
        assertFalse(parametersList.isEmpty());

        // etc: Continue testing the received parameters for different SQL types and inputs
    }

    // Similar tests for other fields in the class as per your requirements.
}