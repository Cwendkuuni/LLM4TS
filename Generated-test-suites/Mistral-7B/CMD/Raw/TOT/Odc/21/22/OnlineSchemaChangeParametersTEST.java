import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;

import com.oceanbase.odc.core.connection.model.ConnectionConfig;
import com.oceanbase.odc.core.model.TableIdentity;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OnlineSchemaChangeParametersTest {

    @Test
    public void testValidOnlineSchemaChangeParameters() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters.setSqlContent("CREATE TABLE test_table (id INT PRIMARY KEY, name VARCHAR(50));");
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        parameters.setLockTableTimeOutSeconds(30);
        parameters.setSwapTableType(SwapTableType.SWAP_IN_PLACE);
        parameters.setDelimiter(";");

        ConnectionConfig connectionConfig = Mockito.mock(ConnectionConfig.class);
        SubTaskParameterFactory subTaskParameterFactory = Mockito.spy(new SubTaskParameterFactory(connectionConfig, "test_schema"));

        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameters.generateSubTaskParameters(connectionConfig, "test_schema");

        Assertions.assertEquals(1, subTaskParameters.size());

        OnlineSchemaChangeScheduleTaskParameters taskParameters = subTaskParameters.get(0);
        Assertions.assertEquals("test_schema", taskParameters.getDatabaseName());
        Assertions.assertEquals("test_table", taskParameters.getOriginTableName());
        Assertions.assertEquals(OnlineSchemaChangeSqlType.ALTER, taskParameters.getSqlType());
        Assertions.assertEquals("CREATE TABLE test_table (id INT PRIMARY KEY, name VARCHAR(50));", taskParameters.getSqlContent());
        Assertions.assertEquals(List.of("ALTER TABLE test_table ADD COLUMN new_column INT;"), taskParameters.getSqlsToBeExecuted());
    }

    @Test
    public void testEmptySqlContent() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters.setSqlContent("");
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);

        Assertions.assertThrows(RuntimeException.class, new Executable() {
            @Override
            public void execute() {
                parameters.generateSubTaskParameters(Mockito.mock(ConnectionConfig.class), "test_schema");
            }
        });
    }

    @Test
    public void testNullSqlContent() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters.setSqlContent(null);
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);

        Assertions.assertThrows(RuntimeException.class, new Executable() {
            @Override
            public void execute() {
                parameters.generateSubTaskParameters(Mockito.mock(ConnectionConfig.class), "test_schema");
            }
        });
    }

    @Test
    public void testInvalidDelimiter() {
        OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
        parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
        parameters.setSqlContent("CREATE TABLE test_table (id INT PRIMARY KEY, name VARCHAR(50));CREATE TABLE test_table2 (id INT PRIMARY KEY, name VARCHAR(50));");
        parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
        parameters.setDelimiter(";");
        parameters.setLockTableTimeOutSeconds(30);

        Assertions.assertThrows(RuntimeException.class, new Executable() {
            @Override
            public void execute() {
                parameters.generateSubTaskParameters(Mockito.mock(ConnectionConfig.class), "test_schema");
            }
        });

        parameters.setDelimiter("\r\n");

        List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameters.generateSubTaskParameters(Mockito.mock(ConnectionConfig.class), "test_schema");

        Assertions.assertEquals(2, subTaskParameters.size());

        OnlineSchemaChangeScheduleTaskParameters taskParameters1 = subTaskParameters.get(0);
        OnlineSchemaChangeScheduleTaskParameters taskParameters2 = subTaskParameters.get(1);

        Assertions.assertEquals("test_schema", taskParameters1.getDatabaseName());
        Assertions.assertEquals("test_table", taskParameters1.getOriginTableName());
        Assertions.assertEquals(OnlineSchemaChangeSqlType.ALTER, taskParameters1.getSqlType());
        Assertions.assertEquals("CREATE TABLE test_table (id INT PRIMARY KEY, name VARCHAR(50));", taskParameters1.getSqlContent());
        Assertions.assertEquals(List.of("ALTER TABLE test_table ADD COLUMN new_column INT;"), taskParameters1.getSqlsToBeExecuted());

        Assertions.assertEquals("test_schema", taskParameters2.getDatabaseName());
        Assertions.assertEquals("test_table2", taskParameters2.getOriginTableName());
        Assertions.assertEquals(OnlineSchemaChangeSqlType.ALTER, taskParameters2.getSqlType());
        Assertions.assertEquals("CREATE TABLE test_table2 (id INT PRIMARY KEY, name VARCHAR(50));", taskParameters2.getSqlContent());
        Assertions.assertEquals(List.of("ALTER TABLE test_table2 ADD COLUMN new_column INT;"), taskParameters2.getSqlsToBeExecuted());
    }
}