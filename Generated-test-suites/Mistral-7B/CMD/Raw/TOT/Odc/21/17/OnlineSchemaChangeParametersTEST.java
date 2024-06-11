import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OnlineSchemaChangeParametersTest {

 private OnlineSchemaChangeParameters parameters;

 @Before
 public void setUp() {
 String sqlContent = "CREATE TABLE IF NOT EXISTS test_table(id INT PRIMARY KEY, name VARCHAR(50), age INT);"
 + "ALTER TABLE test_table ADD COLUMN new_column VARCHAR(50);";
 parameters = new OnlineSchemaChangeParameters();
 parameters.setSqlType(OnlineSchemaChangeSqlType.CREATE_AND_ALTER);
 parameters.setSqlContent(sqlContent);
 }

 @Test
 public void testIsContinueOnError() {
 parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
 assertTrue(parameters.isContinueOnError());

 parameters.setErrorStrategy(TaskErrorStrategy.STOP);
 assertFalse(parameters.isContinueOnError());
 }

 @Test
 public void testGenerateSubTaskParameters() {
 List<OnlineSchemaChangeScheduleTaskParameters> subTaskParameters = parameters.generateSubTaskParameters(null, null);
 assertNotNull(subTaskParameters);
 assertEquals(2, subTaskParameters.size());

 OnlineSchemaChangeScheduleTaskParameters firstParam = subTaskParameters.get(0);
 assertEquals("CREATE TABLE IF NOT EXISTS test_table(id INT PRIMARY KEY, name VARCHAR(50), age INT)", firstParam.getSqlToBeExecuted());
 assertNull(firstParam.getNewTableName());
 assertNull(firstParam.getOriginTableName());
 assertNull(firstParam.getNewAlterStmt());

 OnlineSchemaChangeScheduleTaskParameters secondParam = subTaskParameters.get(1);
 assertEquals("ALTER TABLE test_table ADD COLUMN new_column VARCHAR(50)", secondParam.getSqlToBeExecuted());
 assertEquals("test_table", secondParam.getNewTableName());
 assertEquals("test_table", secondParam.getOriginTableName());
 assertEquals("ALTER TABLE test_table RENAME test_table TO test_table_renamed, ADD COLUMN new_column VARCHAR(50) test_table_renamed;\n"
 + "ALTER TABLE test_table RENAME test_table_renamed TO test_table;\n", secondParam.getNewAlterStmt());
 }
}