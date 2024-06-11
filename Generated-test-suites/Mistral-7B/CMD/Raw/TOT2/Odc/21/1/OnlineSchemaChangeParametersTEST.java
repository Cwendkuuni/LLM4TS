import static org.junit.Assert.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.oceanbase.odc.core.connection.model.ConnectionConfig;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.core.shared.model.TableIdentity;
import com.oceanbase.odc.service.common.util.SqlUtils;
import com.oceanbase.odc.service.connection.model.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.ddl.SubTaskParameterFactory;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.subtask.SwapTableType;
import com.oceanbase.odc.service.onlineschemachange.util.OriginTableCleanStrategy;
import com.oceanbase.odc.service.onlineschemachange.util.OnlineSchemaChangeSqlType;

public class OnlineSchemaChangeParametersTest {

    @Test
    public void testConstructor() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        assertNull(parameters.getSqlType());
        assertNull(parameters.getSqlContent());
        assertNull(parameters.getErrorStrategy());
        assertNull(parameters.getLockTableTimeOutSeconds());
        assertNull(parameters.getSwapTableNameRetryTimes());
        assertNull(parameters.getOriginTableCleanStrategy());
        assertNull(parameters.getDelimiter());
        assertNull(parameters.getLockUsers());
        assertNull(parameters.getSwapTableType());
        assertFalse(parameters.isContinueOnError());
        assertNull(parameters.generateSubTaskParameters(new ConnectionConfig(), "schema"));
    }

    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(parameters.isContinueOnError());

        parameters.setErrorStrategy(TaskErrorStrategy.ABORT);
        assertFalse(parameters.isContinueOnError());
    }

    @Test
    public void testDefaultConstructor() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        assertEquals(OnlineSchemaChangeSqlType.UNDEFINED, parameters.getSqlType());
        assertNull(parameters.getSqlContent());
        assertEquals(TaskErrorStrategy.UNDEFINED, parameters.getErrorStrategy());
        assertNull(parameters.getLockTableTimeOutSeconds());
        assertNull(parameters.getSwapTableNameRetryTimes());
        assertEquals(OriginTableCleanStrategy.UNDEFINED, parameters.getOriginTableCleanStrategy());
        assertEquals(";", parameters.getDelimiter());
        assertNull(parameters.getLockUsers());
        assertEquals(SwapTableType.UNDEFINED, parameters.getSwapTableType());
        assertFalse(parameters.isContinueOnError());
        assertNull(parameters.generateSubTaskParameters(new ConnectionConfig(), "schema"));
    }

    @Test
    public void testGenerateSubTaskParameters() throws Exception {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters.setSqlContent("ALTER TABLE mytable ALTER COLUMN col new_coltype");
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        parameters.setLockTableTimeOutSeconds(10);
        parameters.setSwapTableNameRetryTimes(5);
        parameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.TRUNCATE);
        parameters.setDelimiter("|");
        parameters.setLockUsers(new ArrayList<>());
        parameters.setSwapTableType(SwapTableType.IN_PLACE);

        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameters.generateSubTaskParameters(connectionConfig, "schema");
        assertNotNull(subTaskParameters);
        assertFalse(subTaskParameters.isEmpty());

        OnlineSchemaChangeScheduleTaskParameters taskParameter = subTaskParameters.get(0);
        assertEquals("schema", taskParameter.getDatabaseName());
        assertEquals("mytable", taskParameter.getOriginTableName());
        assertEquals("ALTER TABLE mytable ALTER COLUMN col new_coltype", taskParameter.getSqlsToBeExecuted().get(0));

        // Test with empty SQL content
        OnlineSchemaChangeParameters parametersEmpty = new OnlineSchemaChangeParameters();
        parametersEmpty.setSqlContent("");
        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParametersEmpty = parametersEmpty.generateSubTaskParameters(connectionConfig, "schema");
        assertTrue(subTaskParametersEmpty.isEmpty());

        // Test with null SQL content
        OnlineSchemaChangeParameters parametersNull = new OnlineSchemaChangeParameters();
        parametersNull.setSqlContent(null);
        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParametersNull = parametersNull.generateSubTaskParameters(connectionConfig, "schema");
        assertTrue(subTaskParametersNull.isEmpty());

        // Test with invalid SQL content
        OnlineSchemaChangeParameters parametersInvalid = new OnlineSchemaChangeParameters();
        parametersInvalid.setSqlContent("INVALID SQL CONTENT");
        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParametersInvalid = parametersInvalid.generateSubTaskParameters(connectionConfig, "schema");
        assertTrue(subTaskParametersInvalid.isEmpty());

        // Test with invalid error strategy
        OnlineSchemaChangeParameters parametersInvalidErrorStrategy = new OnlineSchemaChangeParameters();
        parametersInvalidErrorStrategy.setErrorStrategy(TaskErrorStrategy.INVALID);
        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParametersInvalidErrorStrategy = parametersInvalidErrorStrategy.generateSubTaskParameters(connectionConfig, "schema");
        assertTrue(subTaskParametersInvalidErrorStrategy.isEmpty());

        // Test with negative lockTableTimeOutSeconds and swapTableNameRetryTimes
        OnlineSchemaChangeParameters parametersNegative = new OnlineSchemaChangeParameters();
        parametersNegative.setLockTableTimeOutSeconds(-1);
        parametersNegative.setSwapTableNameRetryTimes(-1);
        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParametersNegative = parametersNegative.generateSubTaskParameters(connectionConfig, "schema");
        assertTrue(subTaskParametersNegative.isEmpty());
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParametersWithException() throws Exception {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters.setSqlContent("ALTER TABLE mytable ALTER COLUMN col new_coltype");
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        parameters.setLockTableTimeOutSeconds(10);
        parameters.setSwapTableNameRetryTimes(5);
        parameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.TRUNCATE);
        parameters.setDelimiter("|");
        parameters.setLockUsers(new ArrayList<>());
        parameters.setSwapTableType(SwapTableType.IN_PLACE);

        // Simulate exception in SubTaskParameterFactory
        SubTaskParameterFactory subTaskParameterFactory = new SubTaskParameterFactory() {
            @Override
            public OnlineSchemaChangeScheduleTaskParameters generate(String sql, OnlineSchemaChangeSqlType sqlType) throws Exception {
                throw new Exception("Failed to generate subtasks with parameter: " + parameters);
            }
        };
        try (SubTaskParameterFactory subTaskParameterFactoryMock = subTaskParameterFactory) {
            List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameters.generateSubTaskParameters(connectionConfig, "schema");
            fail("Expected UnexpectedException but test passed");
        }
    }

}