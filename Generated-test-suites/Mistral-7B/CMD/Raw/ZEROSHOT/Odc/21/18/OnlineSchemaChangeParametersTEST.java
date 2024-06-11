import org.junit.Test;
import static org.junit.Assert.*;

import com.oceanbase.odc.core.connection.model.ConnectionConfig;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;
import com.oceanbase.odc.service.onlineschemachange.util.SqlUtils;

public class OnlineSchemaChangeParametersTest {

    @Test
    public void testInstance() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        assertNotNull(parameters);
    }

    @Test
    public void testGetIsContinueOnError() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        assertFalse(parameters.isContinueOnError());

        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(parameters.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters() throws Exception {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();

        String sqlContent = "ALTER TABLE test_table ALTER COLUMN test_column INT;";
        parameters.setSqlContent(sqlContent);
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);

        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameters.generateSubTaskParameters(connectionConfig, "test_schema");
        assertEquals(1, subTaskParameters.size());

        OnlineSchemaChangeScheduleTaskParameters taskParameter = subTaskParameters.get(0);
        assertEquals("test_schema", taskParameter.getDatabaseName());
        assertEquals("test_table", taskParameter.getOriginTableName());
        assertEquals(OnlineSchemaChangeSqlType.ALTER, taskParameter.getSqlType());
        assertEquals(1, taskParameter.getSqlsToBeExecuted().size());
        assertEquals("ALTER TABLE test_schema.test_table ALTER COLUMN test_column INT;", taskParameter.getSqlsToBeExecuted().get(0));
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParametersWithException() throws Exception {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();

        String sqlContent = "ALTER TABLE test_table ALTER COLUMN test_column INT;";
        parameters.setSqlContent(sqlContent);
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);

        try (SubTaskParameterFactory subTaskParameterFactory = new SubTaskParameterFactory(connectionConfig, "test_schema")) {
            List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameters.generateSubTaskParameters(connectionConfig, "test_schema");
            fail("Generate subtask parameters should throw an exception");
        } catch (Exception e) {
            // expected
        }
    }

    @Test
    public void testSplitSqls() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();

        String sqlContent = "ALTER TABLE test_table1 ALTER COLUMN test_column1 INT;\nALTER TABLE test_table2 ALTER COLUMN test_column2 INT;";
        parameters.setSqlContent(sqlContent);
        parameters.setDelimiter("\\n");

        List<String> sqls = SqlUtils.split(connectionConfig.getDialectType(), sqlContent, parameters.getDelimiter());
        assertEquals(2, sqls.size());
        assertEquals("ALTER TABLE test_table1 ALTER COLUMN test_column1 INT;", sqls.get(0));
        assertEquals("ALTER TABLE test_table2 ALTER COLUMN test_column2 INT;", sqls.get(1));
    }

    @Test
    public void testReplaceTableName() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();

        String sqlContent = "ALTER TABLE test_table ALTER COLUMN test_column INT;";
        String newTableName = "new_test_table";

        String newAlterStmt = DdlUtils.replaceTableName(sqlContent, newTableName, connectionConfig.getDialectType(), OnlineSchemaChangeSqlType.ALTER);
        assertEquals("ALTER TABLE new_test_schema.new_test_table ALTER COLUMN test_column INT;", newAlterStmt);
    }
}