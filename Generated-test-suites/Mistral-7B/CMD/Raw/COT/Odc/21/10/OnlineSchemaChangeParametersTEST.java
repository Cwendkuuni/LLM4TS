import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.core.connection.model.ConnectionConfig;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.model.TableIdentity;
import com.oceanbase.odc.service.common.util.SqlUtils;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;

public class OnlineSchemaChangeParametersTest {

    private ConnectionConfig connectionConfig;
    private OnlineSchemaChangeParameters parameters;

    @Before
    public void setUp() {
        connectionConfig = mock(ConnectionConfig.class);
        parameters = new OnlineSchemaChangeParameters();
    }

    @Test
    public void testIsContinueOnError() {
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(parameters.isContinueOnError());

        parameters.setErrorStrategy(TaskErrorStrategy.STOP);
        assertFalse(parameters.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters_normal() {
        String sqlContent = "CREATE TABLE test_table(id INT PRIMARY KEY, name VARCHAR(50));";
        parameters.setSqlType(OnlineSchemaChangeSqlType.CREATE);
        parameters.setSqlContent(sqlContent);

        when(connectionConfig.getDialectType()).thenReturn("mysql");

        List<OnlineSchemaChangeScheduleTaskParameters> result = parameters.generateSubTaskParameters(connectionConfig, "test_schema");
        assertEquals(1, result.size());

        OnlineSchemaChangeScheduleTaskParameters taskParameter = result.get(0);
        assertEquals("test_schema", taskParameter.getSchema());
        assertEquals("test_schema.test_table", taskParameter.getTableName());
        assertEquals(1, taskParameter.getSqlsToBeExecuted().size());
        assertEquals(sqlContent, taskParameter.getSqlsToBeExecuted().get(0));
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParameters_exception() {
        String sqlContent = "CREATE TABLE test_table(id INT PRIMARY KEY, name VARCHAR(50));";
        parameters.setSqlType(OnlineSchemaChangeSqlType.CREATE);
        parameters.setSqlContent(sqlContent);

        when(connectionConfig.getDialectType()).thenThrow(new RuntimeException());

        parameters.generateSubTaskParameters(connectionConfig, "test_schema");
    }

    @Test
    public void testSplitSqls_normal() {
        String sqlContent = "CREATE TABLE test_table1(id INT PRIMARY KEY, name VARCHAR(50));CREATE TABLE test_table2(id INT PRIMARY KEY, name VARCHAR(50));";
        String delimiter = ";";

        List<String> result = SqlUtils.split(connectionConfig.getDialectType(), sqlContent, delimiter);

        assertEquals(2, result.size());
        assertEquals("CREATE TABLE test_table1(id INT PRIMARY KEY, name VARCHAR(50));", result.get(0));
        assertEquals("CREATE TABLE test_table2(id INT PRIMARY KEY, name VARCHAR(50));", result.get(1));
    }

    @Test
    public void testReplaceTableName_normal() {
        String sqlContent = "ALTER TABLE test_table RENAME TO new_test_table;";
        String newTableName = "new_test_table_new";

        String result = DdlUtils.replaceTableName(sqlContent, newTableName, connectionConfig.getDialectType(), OnlineSchemaChangeSqlType.ALTER);

        assertEquals("ALTER TABLE test_table RENAME TO new_test_table_new;", result);
    }

    @Test
    public void testGenerateSubTaskParameters_alter() {
        String sqlContent = "CREATE TABLE test_table(id INT PRIMARY KEY, name VARCHAR(50));ALTER TABLE test_table RENAME TO new_test_table;";
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters.setSqlContent(sqlContent);

        when(connectionConfig.getDialectType()).thenReturn("mysql");

        List<OnlineSchemaChangeScheduleTaskParameters> result = parameters.generateSubTaskParameters(connectionConfig, "test_schema");
        assertEquals(2, result.size());

        OnlineSchemaChangeScheduleTaskParameters testTableTaskParameter = result.get(0);
        assertEquals("test_schema", testTableTaskParameter.getSchema());
        assertEquals("test_schema.test_table", testTableTaskParameter.getTableName());
        assertEquals(1, testTableTaskParameter.getSqlsToBeExecuted().size());
        assertEquals("CREATE TABLE test_table(id INT PRIMARY KEY, name VARCHAR(50));", testTableTaskParameter.getSqlsToBeExecuted().get(0));

        OnlineSchemaChangeScheduleTaskParameters newTestTableTaskParameter = result.get(1);
        assertEquals("test_schema", newTestTableTaskParameter.getSchema());
        assertEquals("test_schema.new_test_table", newTestTableTaskParameter.getTableName());
        assertEquals(1, newTestTableTaskParameter.getSqlsToBeExecuted().size());
        assertEquals("ALTER TABLE test_table RENAME TO new_test_table;", newTestTableTaskParameter.getSqlsToBeExecuted().get(0));
    }

}