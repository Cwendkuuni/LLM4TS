import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.core.connection.ConnectionConfig;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.core.shared.model.TableIdentity;
import com.oceanbase.odc.service.common.util.SqlUtils;
import com.oceanbase.odc.service.connection.model.ConnectionConfigMock;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtilsMock;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactoryMock;

public class OnlineSchemaChangeParametersTest {

    private ConnectionConfig connectionConfig;
    private SubTaskParameterFactory subTaskParameterFactory;
    private DdlUtils ddlUtils;

    @Before
    public void setup() {
        connectionConfig = new ConnectionConfigMock();
        subTaskParameterFactory = new SubTaskParameterFactoryMock();
        ddlUtils = new DdlUtilsMock();
    }

    @Test
    public void testGetSqlType() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        assertEquals(OnlineSchemaChangeSqlType.ALTER, parameters.getSqlType());
    }

    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(parameters.isContinueOnError());

        parameters.setErrorStrategy(TaskErrorStrategy.STOP);
        assertFalse(parameters.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() throws Exception {
        String schema = "test_schema";
        String sqlContent = "ALTER TABLE test_table ADD COLUMN new_column INT;";
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlContent(sqlContent);
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        parameters.setLockTableTimeOutSeconds(10);
        parameters.setSwapTableType(SwapTableType.ASYNC);

        List<OnlineSchemaChangeScheduleTaskParameters> expectedTaskParameters = new ArrayList<>();
        OnlineSchemaChangeScheduleTaskParameters taskParameter1 = new OnlineSchemaChangeScheduleTaskParameters();
        TableIdentity key1 = new TableIdentity("test_db", "test_table");
        taskParameter1.setDatabaseName("test_db");
        taskParameter1.setOriginTableName("test_table");
        taskParameter1.setOldTableName("test_table");
        taskParameter1.setNewTableName("test_table_new");
        taskParameter1.setSqlsToBeExecuted(new ArrayList<>());
        expectedTaskParameters.add(taskParameter1);

        when(subTaskParameterFactory.generate(anyString(), eq(OnlineSchemaChangeSqlType.ALTER)))
                .thenReturn(taskParameter1);

        List<OnlineSchemaChangeScheduleTaskParameters> actualTaskParameters = parameters.generateSubTaskParameters(connectionConfig, schema);
        assertEquals(expectedTaskParameters, actualTaskParameters);

        String newAlterStmt = "ALTER TABLE test_table_new ADD COLUMN new_column INT;";
        taskParameter1.getSqlsToBeExecuted().add(newAlterStmt);
        assertTrue(ddlUtils.isReplaceTableName(newAlterStmt, "test_table_new", connectionConfig.getDialectType(), OnlineSchemaChangeSqlType.ALTER));
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParametersWithException() throws Exception {
        String sqlContent = "ALTER TABLE test_table ADD COLUMN new_column INT;";
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlContent(sqlContent);
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);

        when(subTaskParameterFactory.generate(anyString(), eq(OnlineSchemaChangeSqlType.ALTER)))
                .thenThrow(new Exception("Failed to generate subtasks"));

        parameters.generateSubTaskParameters(connectionConfig, "test_schema");
    }
}

class ConnectionConfigMock implements ConnectionConfig {
    // mock implementation
}

class SubTaskParameterFactoryMock implements SubTaskParameterFactory {
    // mock implementation
}

class DdlUtilsMock implements DdlUtils {
    // mock implementation
}