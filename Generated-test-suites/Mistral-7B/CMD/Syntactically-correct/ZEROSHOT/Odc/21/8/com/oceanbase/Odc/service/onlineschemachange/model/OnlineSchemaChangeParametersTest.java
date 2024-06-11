import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.oceanbase.odc.core.connection.model.ConnectionConfig;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.core.shared.model.TableIdentity;
import com.oceanbase.odc.service.common.util.SqlUtils;
import com.oceanbase.odc.service.connection.model.ConnectionConfigMock;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtilsMock;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactoryMock;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class OnlineSchemaChangeParametersTest {

    private ConnectionConfig connectionConfig;
    private DdlUtils ddlUtils;
    private SubTaskParameterFactory subTaskParameterFactory;

    @Before
    public void setUp() {
        connectionConfig = new ConnectionConfigMock();
        ddlUtils = new DdlUtilsMock();
        subTaskParameterFactory = new SubTaskParameterFactoryMock();
    }

    @Test
    public void testCreateOnlineSchemaChangeParameters() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        assertEquals(OnlineSchemaChangeSqlType.CREATE, parameters.getSqlType());
        assertNull(parameters.getSqlContent());
        assertEquals(TaskErrorStrategy.CONTINUE, parameters.getErrorStrategy());
        assertEquals(0, parameters.getLockTableTimeOutSeconds());
        assertEquals(0, parameters.getSwapTableNameRetryTimes());
        assertNull(parameters.getOriginTableCleanStrategy());
        assertEquals(";", parameters.getDelimiter());
        assertNull(parameters.getLockUsers());
        assertEquals(SwapTableType.NO_SWAP, parameters.getSwapTableType());

        OnlineSchemaChangeParameters newParameters = new OnlineSchemaChangeParameters();
        newParameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        newParameters.setSqlContent("ALTER TABLE test_table ALTER COLUMN id INT2");
        newParameters.setErrorStrategy(TaskErrorStrategy.FAIL);
        newParameters.setLockTableTimeOutSeconds(10);
        newParameters.setSwapTableNameRetryTimes(3);
        newParameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.DELETE_DROP);
        newParameters.setDelimiter("\\|");
        newParameters.setLockUsers(Arrays.asList("user1", "user2"));
        newParameters.setSwapTableType(SwapTableType.SWAP);

        OnlineSchemaChangeParameters cloneParameters = new OnlineSchemaChangeParameters(newParameters);
        assertEquals(newParameters.getSqlType(), cloneParameters.getSqlType());
        assertEquals(newParameters.getSqlContent(), cloneParameters.getSqlContent());
        assertEquals(newParameters.getErrorStrategy(), cloneParameters.getErrorStrategy());
        assertEquals(newParameters.getLockTableTimeOutSeconds(), cloneParameters.getLockTableTimeOutSeconds());
        assertEquals(newParameters.getSwapTableNameRetryTimes(), cloneParameters.getSwapTableNameRetryTimes());
        assertEquals(newParameters.getOriginTableCleanStrategy(), cloneParameters.getOriginTableCleanStrategy());
        assertEquals(newParameters.getDelimiter(), cloneParameters.getDelimiter());
        assertEquals(newParameters.getLockUsers(), cloneParameters.getLockUsers());
        assertEquals(newParameters.getSwapTableType(), cloneParameters.getSwapTableType());

        OnlineSchemaChangeParameters nullParameters = new OnlineSchemaChangeParameters(null);
        assertNull(nullParameters.getSqlType());
        assertNull(nullParameters.getSqlContent());
        assertNull(nullParameters.getErrorStrategy());
        assertNull(nullParameters.getLockTableTimeOutSeconds());
        assertNull(nullParameters.getSwapTableNameRetryTimes());
        assertNull(nullParameters.getOriginTableCleanStrategy());
        assertNull(nullParameters.getDelimiter());
        assertNull(nullParameters.getLockUsers());
        assertNull(nullParameters.getSwapTableType());

        OnlineSchemaChangeParameters deepCloneParameters = new OnlineSchemaChangeParameters(new OnlineSchemaChangeParameters(newParameters()));
        assertEquals(newParameters.getSqlType(), deepCloneParameters.getSqlType());
        assertEquals(newParameters.getSqlContent(), deepCloneParameters.getSqlContent());
        assertEquals(newParameters.getErrorStrategy(), deepCloneParameters.getErrorStrategy());
        assertEquals(newParameters.getLockTableTimeOutSeconds(), deepCloneParameters.getLockTableTimeOutSeconds());
        assertEquals(newParameters.getSwapTableNameRetryTimes(), deepCloneParameters.getSwapTableNameRetryTimes());
        assertEquals(newParameters.getOriginTableCleanStrategy(), deepCloneParameters.getOriginTableCleanStrategy());
        assertEquals(newParameters.getDelimiter(), deepCloneParameters.getDelimiter());
        assertEquals(newParameters.getLockUsers(), deepCloneParameters.getLockUsers());
        assertEquals(newParameters.getSwapTableType(), deepCloneParameters.getSwapTableType());
    }

    @Test
    public void testGenerateSubTaskParameters() {
        String schema = "test_schema";
        when(ddlUtils.replaceTableName(anyString(), anyString(), anyString(), OnlineSchemaChangeSqlType.ALTER))
                .thenReturn("ALTER TABLE test_table ALTER COLUMN id INT2");

        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlType(OnlineSchemaChangeSqlType.CREATE);
        parameters.setSqlContent("CREATE TABLE test_table (id INT PRIMARY KEY)");
        parameters.setErrorStrategy(TaskErrorStrategy.FAIL);
        parameters.setLockTableTimeOutSeconds(10);
        parameters.setSwapTableNameRetryTimes(3);
        parameters.setOriginTableCleanStrategy(OriginTableCleanStrategy.DELETE_DROP);
        parameters.setDelimiter(";");
        parameters.setLockUsers(Arrays.asList("user1", "user2"));
        parameters.setSwapTableType(SwapTableType.SWAP);

        List<OnlineSchemaChangeScheduleTaskParameters> expectedTaskParameters = new ArrayList<>();
        expectedTaskParameters.add(new OnlineSchemaChangeScheduleTaskParameters(
                "test_schema", "test_table", "CREATE TABLE test_table (id INT PRIMARY KEY)", OnlineSchemaChangeSqlType.CREATE));
        when(subTaskParameterFactory.generate(anyString(), OnlineSchemaChangeSqlType.CREATE)).thenReturn(
                new OnlineSchemaChangeScheduleTaskParameters("test_schema", "test_table", null, OnlineSchemaChangeSqlType.CREATE));

        List<OnlineSchemaChangeScheduleTaskParameters> actualTaskParameters = parameters.generateSubTaskParameters(connectionConfig, schema);
        assertEquals(1, actualTaskParameters.size());
        OnlineSchemaChangeScheduleTaskParameters actualTaskParameter = actualTaskParameters.get(0);
        assertEquals(expectedTaskParameters.get(0).getDatabaseName(), actualTaskParameter.getDatabaseName());
        assertEquals(expectedTaskParameters.get(0).getOriginTableName(), actualTaskParameter.getOriginTableName());
        assertEquals(expectedTaskParameters.get(0).getSqlsToBeExecuted().get(0), actualTaskParameter.getSqlsToBeExecuted().get(0));
        assertEquals(expectedTaskParameters.get(0).getSqlType(), actualTaskParameter.getSqlType());

        verify(ddlUtils, times(1)).replaceTableName(anyString(), "test_table", anyString(), OnlineSchemaChangeSqlType.ALTER);
        verifyNoMoreInteractions(ddlUtils);
        verifyNoMoreInteractions(subTaskParameterFactory);

        OnlineSchemaChangeParameters alterParameters = new OnlineSchemaChangeParameters();
        alterParameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        alterParameters.setSqlContent("ALTER TABLE test_table ALTER COLUMN id INT2");

        expectedTaskParameters.clear();
        expectedTaskParameters.add(new OnlineSchemaChangeScheduleTaskParameters("test_schema", "test_table", null, OnlineSchemaChangeSqlType.CREATE));
        expectedTaskParameters.add(new OnlineSchemaChangeScheduleTaskParameters("test_schema", "test_table", "ALTER TABLE test_table ALTER COLUMN id INT2", OnlineSchemaChangeSqlType.ALTER));
        when(subTaskParameterFactory.generate(anyString(), OnlineSchemaChangeSqlType.CREATE)).thenReturn(
                new OnlineSchemaChangeScheduleTaskParameters("test_schema", "test_table", null, OnlineSchemaChangeSqlType.CREATE));
        when(subTaskParameterFactory.generate(anyString(), OnlineSchemaChangeSqlType.ALTER)).thenAnswer(new Function<String, OnlineSchemaChangeScheduleTaskParameters>() {
            @Override
            public OnlineSchemaChangeScheduleTaskParameters apply(String sql) {
                String newAlterStmt = "ALTER TABLE test_table ALTER COLUMN id INT2";
                return new OnlineSchemaChangeScheduleTaskParameters("test_schema", "test_table", newAlterStmt, OnlineSchemaChangeSqlType.ALTER);
            }
        });

        actualTaskParameters = alterParameters.generateSubTaskParameters(connectionConfig, schema);
        assertEquals(2, actualTaskParameters.size());
        OnlineSchemaChangeScheduleTaskParameters alterTaskParameter = actualTaskParameters.get(1);
        assertEquals(expectedTaskParameters.get(1).getDatabaseName(), alterTaskParameter.getDatabaseName());
        assertEquals(expectedTaskParameters.get(1).getOriginTableName(), alterTaskParameter.getOriginTableName());
        assertEquals(expectedTaskParameters.get(1).getSqlsToBeExecuted().get(0), alterTaskParameter.getSqlsToBeExecuted().get(0));
        assertEquals(expectedTaskParameters.get(1).getSqlType(), alterTaskParameter.getSqlType());

        verify(ddlUtils, times(1)).replaceTableName(anyString(), "test_table", anyString(), OnlineSchemaChangeSqlType.ALTER);
        verify(subTaskParameterFactory, times(2)).generate(anyString(), OnlineSchemaChangeSqlType.CREATE);
        verify(subTaskParameterFactory, times(1)).generate(anyString(), OnlineSchemaChangeSqlType.ALTER);
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParametersException() {
        String schema = "test_schema";
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlContent("CREATE TABLE test_table (id INT PRIMARY KEY)");

        when(subTaskParameterFactory.generate(anyString(), OnlineSchemaChangeSqlType.CREATE)).thenThrow(new UnexpectedException("Failed to generate subtask"));

        try {
            parameters.generateSubTaskParameters(connectionConfig, schema);
            fail("Should throw UnexpectedException");
        } catch (UnexpectedException e) {
            assertEquals("Failed to generate subtasks with parameter: OnlineSchemaChangeParameters{sqlType=CREATE, sqlContent=CREATE TABLE test_table (id INT PRIMARY KEY), errorStrategy=FAIL, lockTableTimeOutSeconds=10, swapTableNameRetryTimes=3, originTableCleanStrategy=DELETE_DROP, delimiter=;, lockUsers=[], swapTableType=SWAP}", e.getMessage());
        }

        verify(subTaskParameterFactory, times(1)).generate(anyString(), OnlineSchemaChangeSqlType.CREATE);
    }
}

class ConnectionConfigMock implements ConnectionConfig {
    @Override
    public String getDialectType() {
        return "test_dialect";
    }
}

class DdlUtilsMock implements DdlUtils {
    @Override
    public String replaceTableName(String sql, String oldTableName, String dialectType, OnlineSchemaChangeSqlType sqlType) {
        return "ALTER TABLE test_table ALTER COLUMN id INT2";
    }
}

class SubTaskParameterFactoryMock implements SubTaskParameterFactory {
    private ConnectionConfig connectionConfig;
    private String schema;

    public SubTaskParameterFactoryMock(ConnectionConfig connectionConfig, String schema) {
        this.connectionConfig = connectionConfig;
        this.schema = schema;
    }

    @Override
    public OnlineSchemaChangeScheduleTaskParameters generate(String sql, OnlineSchemaChangeSqlType sqlType) {
        OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters(
                schema, "test_table", null, sqlType);
        return taskParameter;
    }
}