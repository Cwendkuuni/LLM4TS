import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.mockito.Mockito;

import com.oceanbase.odc.core.connection.model.ConnectionConfig;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.model.TableIdentity;
import com.oceanbase.odc.service.common.util.SqlUtils;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;

public class OnlineSchemaChangeParametersTest {

    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
        onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(onlineSchemaChangeParameters.isContinueOnError());

        onlineSchemaChangeParameters.setErrorStrategy(TaskErrorStrategy.ABORT);
        assertFalse(onlineSchemaChangeParameters.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParametersHappyPath() throws Exception {
        String delimiter = ";";
        OnlineSchemaChangeSqlType sqlType = OnlineSchemaChangeSqlType.CREATE;
        String schema = "testSchema";
        String sqlContent = "create table if not exists testTable (id int primary key);";
        ConnectionConfig connectionConfig = Mockito.mock(ConnectionConfig.class);
        OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
        onlineSchemaChangeParameters.setSqlType(sqlType);
        onlineSchemaChangeParameters.setSqlContent(sqlContent);
        onlineSchemaChangeParameters.setDelimiter(delimiter);

        List<String> sqls = SqlUtils.split(Mockito.mock(String.class), onlineSchemaChangeParameters.getSqlContent(), delimiter);
        List<OnlineSchemaChangeScheduleTaskParameters> expectedSubTaskParameters = new ArrayList<>();
        OnlineSchemaChangeScheduleTaskParameters expectedParameter = new OnlineSchemaChangeScheduleTaskParameters();
        expectedParameter.setDatabaseName("testSchema");
        expectedParameter.setOriginTableName("testTable");
        expectedParameter.setNewTableName("testTable");
        expectedSubTaskParameters.add(expectedParameter);

        when(connectionConfig.getDialectType()).thenReturn(Mockito.mock(String.class));
        when(connectionConfig.getDialectType()).thenReturn("mysql");
        SubTaskParameterFactory subTaskParameterFactory = Mockito.spy(new SubTaskParameterFactory(connectionConfig, schema));

        List<OnlineSchemaChangeScheduleTaskParameters> actualSubTaskParameters = onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, schema);
        assertEquals(expectedSubTaskParameters, actualSubTaskParameters);
        verify(subTaskParameterFactory, times(1)).generate(eq(sqls.get(0)), eq(sqlType));
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParametersException() throws Exception {
        String delimiter = ";";
        OnlineSchemaChangeSqlType sqlType = OnlineSchemaChangeSqlType.CREATE;
        String schema = "testSchema";
        String sqlContent = "create table if not exists testTable (id int primary key);";
        ConnectionConfig connectionConfig = Mockito.mock(ConnectionConfig.class);
        OnlineSchemaChangeParameters onlineSchemaChangeParameters = new OnlineSchemaChangeParameters();
        onlineSchemaChangeParameters.setSqlType(sqlType);
        onlineSchemaChangeParameters.setSqlContent(sqlContent);
        onlineSchemaChangeParameters.setDelimiter(delimiter);

        when(connectionConfig.getDialectType()).thenThrow(new Exception());

        List<OnlineSchemaChangeScheduleTaskParameters> actualSubTaskParameters = onlineSchemaChangeParameters.generateSubTaskParameters(connectionConfig, schema);
        fail("Exception should be thrown");
    }
}