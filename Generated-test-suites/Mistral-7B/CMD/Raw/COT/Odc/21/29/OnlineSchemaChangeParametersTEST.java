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
import com.oceanbase.odc.service.common.util.SqlUtils;
import com.oceanbase.odc.service.connection.model.ConnectionConfigBuilder;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactoryImpl;

public class OnlineSchemaChangeParametersTest {

    private ConnectionConfig connectionConfig;
    private SubTaskParameterFactory subTaskParameterFactory;

    @Before
    public void setUp() {
        connectionConfig = new ConnectionConfigBuilder().build();
        subTaskParameterFactory = new SubTaskParameterFactoryImpl(connectionConfig, "schema");
    }

    @Test
    public void testIsContinueOnError() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        assertTrue(parameters.isContinueOnError());

        parameters.setErrorStrategy(TaskErrorStrategy.FAIL);
        assertFalse(parameters.isContinueOnError());
    }

    @Test
    public void testGenerateSubTaskParameters_emptySqlContent() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        List<OnlineSchemaChangeScheduleTaskParameters> actual = parameters.generateSubTaskParameters(connectionConfig, "schema");
        assertEquals(0, actual.size());
    }

    @Test
    public void testGenerateSubTaskParameters_valid() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters.setSqlContent("CREATE TABLE IF NOT EXISTS test_table (id INT PRIMARY KEY); ALTER TABLE test_table ADD COLUMN column1 INT;");
        parameters.setDelimiter(";");
        parameters.setLockTableTimeOutSeconds(10);
        parameters.setSwapTableType(SwapTableType.MERGE);

        List<OnlineSchemaChangeScheduleTaskParameters> actual = parameters.generateSubTaskParameters(connectionConfig, "schema");
        List<OnlineSchemaChangeScheduleTaskParameters> expected = new ArrayList<>();
        OnlineSchemaChangeScheduleTaskParameters task1 = new OnlineSchemaChangeScheduleTaskParameters();
        task1.setDatabaseName("schema");
        task1.setOriginTableName("test_table");
        task1.setNewTableName("test_table_new");
        task1.setSqlsToBeExecuted(new ArrayList<>());
        OnlineSchemaChangeScheduleTaskParameters task2 = new OnlineSchemaChangeScheduleTaskParameters();
        task2.setDatabaseName("schema");
        task2.setOriginTableName("test_table");
        task2.setNewTableName("test_table_new");
        String alterStmt = "ALTER TABLE test_table_new ADD COLUMN column1 INT;";
        task2.getSqlsToBeExecuted().add(alterStmt);
        expected.add(task1);
        expected.add(task2);
        assertEquals(expected, actual);
    }

    @Test(expected = UnexpectedException.class)
    public void testGenerateSubTaskParameters_exception() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlContent("CREATE TABLE IF NOT EXISTS test_table (id INT PRIMARY KEY); ALTER TABLE test_table ADD COLUMN column1 INT;");
        parameters.setDelimiter(";");
        when(subTaskParameterFactory.generate("CREATE TABLE IF NOT EXISTS test_table (id INT PRIMARY KEY);", OnlineSchemaChangeSqlType.CREATE))
                .thenThrow(new RuntimeException("mock exception"));

        List<OnlineSchemaChangeScheduleTaskParameters> actual = parameters.generateSubTaskParameters(connectionConfig, "schema");
    }

}