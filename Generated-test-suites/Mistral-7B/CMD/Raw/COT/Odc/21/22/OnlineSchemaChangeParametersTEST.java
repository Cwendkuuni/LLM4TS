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
import com.oceanbase.odc.service.connection.model.ConnectionConfigFactory;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactoryMock;

public class OnlineSchemaChangeParametersTest {

 private ConnectionConfig connectionConfig = ConnectionConfigFactory.create();

 @Test
 public void testIsContinueOnError() {
 OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
 parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
 assertTrue(parameters.isContinueOnError());
 }

 @Test
 public void testIsContinueOnError_False() {
 OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
 parameters.setErrorStrategy(TaskErrorStrategy.STOP);
 assertFalse(parameters.isContinueOnError());
 }

 @Test
 public void testGenerateSubTaskParameters_Normal() throws Exception {
 OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
 parameters.setSqlType(OnlineSchemaChangeSqlType.CREATE);
 parameters.setSqlContent("CREATE TABLE test (id INT PRIMARY KEY)");
 parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
 parameters.setLockTableTimeOutSeconds(10);
 parameters.setSwapTableType(SwapTableType.IN_PLACE);

 SubTaskParameterFactory subTaskParameterFactory = mock(SubTaskParameterFactory.class);
 when(subTaskParameterFactory.generate("CREATE TABLE test (id INT PRIMARY KEY)", OnlineSchemaChangeSqlType.CREATE))
 .thenReturn(new OnlineSchemaChangeScheduleTaskParameters("test", "test", null, null, null, null, null, null));

 OnlineSchemaChangeParameters actual = parameters.generateSubTaskParameters(connectionConfig, "test");

 List<OnlineSchemaChangeScheduleTaskParameters> expected = new ArrayList<>();
 OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters("test", "test", null, null, null, null, null, null);
 expected.add(taskParameter);

 assertEquals(expected, actual);
 }

 @Test(expected = UnexpectedException.class)
 public void testGenerateSubTaskParameters_Exception() throws Exception {
 OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
 parameters.setSqlType(OnlineSchemaChangeSqlType.CREATE);
 parameters.setSqlContent("CREATE TABLE test (id INT PRIMARY KEY) X"); // Extra character X added to cause exception
 parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
 parameters.setLockTableTimeOutSeconds(10);
 parameters.setSwapTableType(SwapTableType.IN_PLACE);

 SubTaskParameterFactory subTaskParameterFactory = new SubTaskParameterFactoryMock();
 OnlineSchemaChangeParameters actual = parameters.generateSubTaskParameters(connectionConfig, "test");
 fail("Exception expected but not thrown");
 }

 @Test
 public void testGenerateSubTaskParameters_SwapTableTypeDdl() throws Exception {
 OnlineSchemaChangeParameters parameters = new OnlineSchemaChangeParameters();
 parameters.setSqlType(OnlineSchemaChangeSqlType.ALTER);
 parameters.setSqlContent("ALTER TABLE test ADD COLUMN new_column INT");
 parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
 parameters.setLockTableTimeOutSeconds(10);
 parameters.setSwapTableType(SwapTableType.SWAP);

 SubTaskParameterFactory subTaskParameterFactory = new SubTaskParameterFactoryMock();
 when(subTaskParameterFactory.generate("ALTER TABLE test ADD COLUMN new_column INT", OnlineSchemaChangeSqlType.ALTER))
 .thenReturn(new OnlineSchemaChangeScheduleTaskParameters("test", "test", null, null, null, null, null, new ArrayList<>()));

 String newAlterStmt = "ALTER TABLE test RENAME TO test_new";
 OnlineSchemaChangeScheduleTaskParameters actualTaskParameter = parameters.generateSubTaskParameters(connectionConfig, "test")
 .get(0);

 List<String> expectedSqlsToBeExecuted = new ArrayList<>();
 expectedSqlsToBeExecuted.add("ALTER TABLE test ADD COLUMN new_column INT");
 expectedSqlsToBeExecuted.add(newAlterStmt);
 List<String> actualSqlsToBeExecuted = actualTaskParameter.getSqlsToBeExecuted();

 assertEquals(expectedSqlsToBeExecuted, actualSqlsToBeExecuted);
 }

}

class SubTaskParameterFactoryMock extends SubTaskParameterFactory {
 @Override
 OnlineSchemaChangeScheduleTaskParameters generate(String sql, OnlineSchemaChangeSqlType sqlType) throws Exception {
 OnlineSchemaChangeScheduleTaskParameters taskParameter = new OnlineSchemaChangeScheduleTaskParameters("test", "test", null, null, null, null, null, null);
 taskParameter.setSqlsToBeExecuted(new ArrayList<>());
 addSqlToBeExecuted(taskParameter, sql, sqlType);
 return taskParameter;
 }
}