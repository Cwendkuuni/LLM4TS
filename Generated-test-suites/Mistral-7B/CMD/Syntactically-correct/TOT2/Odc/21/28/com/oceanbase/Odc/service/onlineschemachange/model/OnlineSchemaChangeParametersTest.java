
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.core.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.common.exception.UnexpectedException;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;

public class OnlineSchemaChangeParametersTest {

    private ConnectionConfig connectionConfig;
    private SubTaskParameterFactory subTaskParameterFactory;
    private OnlineSchemaChangeParameters onlineSchemaChangeParameters;

    @Before
    public void setup() {
        connectionConfig = mock(ConnectionConfig.class);
        subTaskParameterFactory = mock(SubTaskParameterFactory.class);
        when(subTaskParameterFactory.generate(anyString(), any(OnlineSchemaChangeSqlType.class)))
                .thenReturn(new OnlineSchemaChangeScheduleTaskParameters());
        onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
        onlineSchemaChangeParameters.setSubTaskParameterFactory(subTaskParameterFactory);
    }

    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertEquals(true, parameters.isContinueOnError());

        parameters.setErrorStrategy(TaskErrorStrategy.ABORT);
        assertEquals(false, parameters.isContinueOnError());

        OnlineSchemaChangeParameters invalidParameters = new OnlineSchemaChangeParameters();
        invalidParameters.setErrorStrategy(null);
        assertEquals(false, invalidParameters.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() {
        when(subTaskParameterFactory.generate(anyString(), any(OnlineSchemaChangeSqlType.class)))
                .thenReturn(new OnlineSchemaChangeScheduleTaskParameters());

        List<OnlineSchemaChangeScheduleTaskParameters> taskParameters = onlineSchemaChangeParameters
                .generateSubTaskParameters(connectionConfig, "schema");

        assertEquals(1, taskParameters.size());

        OnlineSchemaChangeParameters parametersWithException = new OnlineSchemaChangeParameters();
        parametersWithException.setSubTaskParameterFactory(subTaskParameterFactory);
        when(subTaskParameterFactory.generate(anyString(), any(OnlineSchemaChangeSqlType.class)))
                .thenThrow(new UnexpectedException("Exception message"));

        try {
            parametersWithException.generateSubTaskParameters(connectionConfig, "schema");
            fail("An unexpected exception should have been thrown.");
        } catch (UnexpectedException e) {
            // Expected
        }
    }

    @Test(expected = NullPointerException.class)
    public void testIsContinueOnErrorWithNullErrorStrategy() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setErrorStrategy(null);
        assertEquals(false, parameters.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParametersWithNullConnectionConfig() {
        when(subTaskParameterFactory.generate(anyString(), any(OnlineSchemaChangeSqlType.class)))
                .thenThrow(new NullPointerException());

        try {
            OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
            parameters.setSubTaskParameterFactory(subTaskParameterFactory);
            parameters.generateSubTaskParameters(null, "schema");
            fail("An exception should have been thrown.");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGenerateSubTaskParametersWithEmptySchema() {
        when(subTaskParameterFactory.generate(anyString(), any(OnlineSchemaChangeSqlType.class)))
                .thenReturn(new OnlineSchemaChangeScheduleTaskParameters());

        try {
            OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
            parameters.setSubTaskParameterFactory(subTaskParameterFactory);
            parameters.generateSubTaskParameters(connectionConfig, "");
            fail("An exception should have been thrown.");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGenerateSubTaskParametersWithEmptySqlContent() {
        when(subTaskParameterFactory.generate(anyString(), any(OnlineSchemaChangeSqlType.class)))
                .thenReturn(new OnlineSchemaChangeScheduleTaskParameters());

        try {
            OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
            parameters.setSubTaskParameterFactory(subTaskParameterFactory);
            parameters.generateSubTaskParameters(connectionConfig, "schema");
            fail("An exception should have been thrown.");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParametersWithException() {
        when(subTaskParameterFactory.generate(anyString(), any(OnlineSchemaChangeSqlType.class)))
                .thenThrow(new UnexpectedException("Exception message"));

        try {
            OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
            parameters.setSubTaskParameterFactory(subTaskParameterFactory);
            parameters.generateSubTaskParameters(connectionConfig, "schema");
            fail("An unexpected exception should have been thrown.");
        } catch (UnexpectedException e) {
            // Expected
        }
    }
}

//