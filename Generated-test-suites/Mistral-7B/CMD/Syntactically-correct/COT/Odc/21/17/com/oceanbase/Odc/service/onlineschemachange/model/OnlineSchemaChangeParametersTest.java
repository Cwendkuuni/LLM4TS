import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.core.connection.model.ConnectionConfig;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.core.shared.model.TableIdentity;
import com.oceanbase.odc.service.common.util.SqlUtils;
import com.oceanbase.odc.service.connection.model.ConnectionConfigMock;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtilsMock;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactoryMock;

public class OnlineSchemaChangeParametersTest {

    private ConnectionConfig connectionConfig;
    private DdlUtils ddlUtils;
    private SubTaskParameterFactory subTaskParameterFactory;

    @Before
    public void setup() {
        connectionConfig = new ConnectionConfigMock();
        ddlUtils = Mockito.mock(DdlUtils.class);
        subTaskParameterFactory = new SubTaskParameterFactoryMock();
        when(ddlUtils.replaceTableName(anyString(), anyString(), anyString(), anyInt()))
                .thenReturn("replaced table name");
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
    public void testGenerateSubTaskParameters() throws Exception {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlContent("test sql");
        parameters.setSqlType(OnlineSchemaChangeSqlType.CREATE);
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        parameters.setLockTableTimeOutSeconds(5);
        parameters.setSwapTableType(SwapTableType.SWAP);
        parameters.setDelimiter(";");

        List<String> sqls = SqlUtils.split(connectionConfig.getDialectType(), parameters.getSqlContent(),
                parameters.getDelimiter());

        when(subTaskParameterFactory.generate(anyString(), anyInt()))
                .thenReturn(new OnlineSchemaChangeScheduleTaskParameters());

        List<OnlineSchemaChangeScheduleTaskParameters> result = parameters.generateSubTaskParameters(connectionConfig,
                "schema");

        assertEquals(sqls.size(), result.size());

        for (int i = 0; i < sqls.size(); i++) {
            OnlineSchemaChangeScheduleTaskParameters taskParameters = result.get(i);
            String sql = sqls.get(i);
            String replacedSql = "replaced table name";
            TableIdentity tableIdentity = new TableIdentity("databaseName", "originTableName");

            verify(subTaskParameterFactory, times(1)).generate(sql, OnlineSchemaChangeSqlType.CREATE);
            assertEquals(tableIdentity.getDatabaseName(), taskParameters.getDatabaseName());
            assertEquals(tableIdentity.getOriginTableName(), taskParameters.getOriginTableName());
            assertEquals(sql, taskParameters.getSqlContent());
            assertEquals(replacedSql, taskParameters.getSqlsToBeExecuted().get(0));
        }
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParametersWithException() throws Exception {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlContent("test sql");
        parameters.setSqlType(OnlineSchemaChangeSqlType.CREATE);
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        parameters.setLockTableTimeOutSeconds(5);
        parameters.setSwapTableType(SwapTableType.SWAP);
        parameters.setDelimiter(";");

        when(subTaskParameterFactory.generate(anyString(), anyInt()))
                .thenThrow(new Exception("test exception"));

        List<OnlineSchemaChangeScheduleTaskParameters> result = parameters.generateSubTaskParameters(connectionConfig,
                "schema");

        fail("Exception should be thrown");
    }
}

class ConnectionConfigMock implements ConnectionConfig {
    @Override
    public String getDialectType() {
        return "dialectType";
    }
}

class DdlUtilsMock implements DdlUtils {
    @Override
    public String replaceTableName(String sql, String newTableName, String dialectType, OnlineSchemaChangeSqlType sqlType) {
        return "replaced table name";
    }
}

class SubTaskParameterFactoryMock implements SubTaskParameterFactory {
    @Override
    public OnlineSchemaChangeScheduleTaskParameters generate(String sql, OnlineSchemaChangeSqlType sqlType) {
        OnlineSchemaChangeScheduleTaskParameters parameters = new OnlineSchemaChangeScheduleTaskParameters();
        parameters.setSqlContent(sql);
        parameters.setSqlsToBeExecuted(new ArrayList<>());
        return parameters;
    }
}