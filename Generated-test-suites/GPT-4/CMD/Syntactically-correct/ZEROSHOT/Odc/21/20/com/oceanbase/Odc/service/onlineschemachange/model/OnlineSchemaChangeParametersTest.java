package com.oceanbase.odc.service.onlineschemachange.model;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;
import com.oceanbase.odc.service.onlineschemachange.model.*;

import java.util.ArrayList;
import java.util.List;

public class OnlineSchemaChangeParametersTest {
    private OnlineSchemaChangeParameters parameters;
    private ConnectionConfig connectionConfig;
    private SubTaskParameterFactory subTaskParameterFactory;

    @Before
    public void setUp() {
        parameters = new OnlineSchemaChangeParameters();
        connectionConfig = mock(ConnectionConfig.class);
        subTaskParameterFactory = mock(SubTaskParameterFactory.class);
    }

    @Test
    public void isContinueOnErrorTest() {
        // Set errorStrategy to CONTINUE, should return true
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(parameters.isContinueOnError());

        // Set errorStrategy to FAIL, should return false
        parameters.setErrorStrategy(TaskErrorStrategy.FAIL);
        assertFalse(parameters.isContinueOnError());
    }

    @Test
    public void generateSubTaskParametersTest() {
        String schema = "testSchema";
        String sqlContent = "CREATE TABLE test_table;";
        parameters.setSqlContent(sqlContent);
        parameters.setSqlType(OnlineSchemaChangeSqlType.CREATE);

        when(connectionConfig.getDialectType()).thenReturn(DialectType.MYSQL);
        
        List<OnlineSchemaChangeScheduleTaskParameters> generatedParameters =
            parameters.generateSubTaskParameters(connectionConfig, schema);

        assertTrue(generatedParameters.size() > 0);
    }

    @Test(expected = UnexpectedException.class)
    public void generateSubTaskParametersFailedTest() {
        String schema = "testSchema";
        parameters.setSqlContent(null); // Set an invalid sqlContent
        parameters.generateSubTaskParameters(connectionConfig, schema);
    }
}
