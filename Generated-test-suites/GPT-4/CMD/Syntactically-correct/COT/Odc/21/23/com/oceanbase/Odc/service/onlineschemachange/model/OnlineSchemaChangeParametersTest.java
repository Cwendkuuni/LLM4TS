package com.oceanbase.odc.service.onlineschemachange.model;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import java.util.List;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;

public class OnlineSchemaChangeParametersTest {
    private OnlineSchemaChangeParameters parameters;

    @Before
    public void setUp() {
        parameters = new OnlineSchemaChangeParameters();
    }

    @Test
    public void isContinueOnErrorTest() {
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(parameters.isContinueOnError());

        parameters.setErrorStrategy(TaskErrorStrategy.STOP);
        assertFalse(parameters.isContinueOnError());
    }

    @Test
    public void generateSubTaskParametersTest() {
        parameters.setSqlContent("CREATE TABLE test_table (id INT PRIMARY KEY, name VARCHAR(64));");
        parameters.setSqlType(OnlineSchemaChangeSqlType.CREATE);
        parameters.setDelimiter(";");

        ConnectionConfig connectionConfig = Mockito.mock(ConnectionConfig.class);
        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters
            = parameters.generateSubTaskParameters(connectionConfig, "test_schema");

        assertEquals(1, subTaskParameters.size());
    }

    @Test(expected = UnexpectedException.class)
    public void generateSubTaskParametersErrorTest() {
        parameters.setSqlContent(null);
        parameters.setSqlType(OnlineSchemaChangeSqlType.CREATE);
        parameters.setDelimiter(null);

        ConnectionConfig connectionConfig = Mockito.mock(ConnectionConfig.class);
        parameters.generateSubTaskParameters(connectionConfig, "test_schema");
    }
}
