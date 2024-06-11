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
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.service.common.util.SqlUtils;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeSqlType;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.model.TableIdentity;

public class OnlineSchemaChangeParametersTest {

 private OnlineSchemaChangeParameters parameters;
 private ConnectionConfig connectionConfig;
 private SubTaskParameterFactory subTaskParameterFactory;

 @Before
 public void setup() {
 connectionConfig = mock(ConnectionConfig.class);
 subTaskParameterFactory = mock(SubTaskParameterFactory.class);
 parameters = new OnlineSchemaChangeParameters();
 }

 @Test
 public void testIsContinueOnError() {
 parameters.setErrorStrategy(TaskErrorStrategy.CONTINUE);
 assertTrue(parameters.isContinueOnError());
 }

 @Test
 public void testIsContinueOnErrorWithFail() {
 parameters.setErrorStrategy(TaskErrorStrategy.FAIL);
 assertFalse(parameters.isContinueOnError());
 }

 @Test
 public void testGenerateSubTaskParameters_EmptySqlContent() {
 String schema = "testSchema";
 when(subTaskParameterFactory.generate(anyString(), any(OnlineSchemaChangeSqlType.class))).thenReturn(null);
 parameters.setSqlContent("");
 parameters.setSchema(schema);

 List<OnlineSchemaChangeScheduleTaskParameters> result = parameters.generateSubTaskParameters(connectionConfig, schema);
 assertTrue(result.isEmpty());
 }

 @Test
 public void testGenerateSubTaskParameters_NonEmptySqlContent() {
 String schema = "testSchema";
 String sqlContent = "create table test(id int); create table test2(id int);";
 when(subTaskParameterFactory.generate(anyString(), any(OnlineSchemaChangeSqlType.class))).thenReturn(mock(OnlineSchemaChangeScheduleTaskParameters.class));
 parameters.setSqlContent(sqlContent);
 parameters.setSchema(schema);

 List<OnlineSchemaChangeScheduleTaskParameters> result = parameters.generateSubTaskParameters(connectionConfig, schema);
 assertEquals(2, result.size());
 }

 @Test(expected = UnexpectedException.class)
 public void testGenerateSubTaskParameters_UnexpectedException() {
 ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
 when(subTaskParameterFactory.generate(anyString(), any(OnlineSchemaChangeSqlType.class))).thenThrow(new RuntimeException("Error"));
 parameters.setSqlContent("create table test(id int);");
 parameters.setSchema("testSchema");

 List<OnlineSchemaChangeScheduleTaskParameters> result = parameters.generateSubTaskParameters(connectionConfig, "testSchema");
 fail("The method should throw UnexpectedException.");
 }

 @Test
 public void testGenerateSubTaskParameters_SplitSqls() {
 ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
 String schema = "testSchema";
 String sqlContent = "create table test1(id int); create table test2(id int);";
 when(subTaskParameterFactory.generate(anyString(), any(OnlineSchemaChangeSqlType.class))).thenReturn(mock(OnlineSchemaChangeScheduleTaskParameters.class));

 List<OnlineSchemaChangeScheduleTaskParameters> result = parameters.generateSubTaskParameters(connectionConfig, schema);
 assertEquals(2, result.size());
 List<String> sqls = SqlUtils.split(connectionConfig.getDialectType(), sqlContent, ";");
 for (int i = 0; i < sqls.size(); i++) {
 OnlineSchemaChangeScheduleTaskParameters parameter = result.get(i);
 String sql = sqls.get(i);
 assertEquals(sql, parameter.getSqlContent());
 }
 }

 @Test
 public void testGenerateSubTaskParameters_ReplaceTableName() {
 ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
 String schema = "testSchema";
 String sqlContent = "alter table test rename to test2;";
 when(subTaskParameterFactory.generate(anyString(), any(OnlineSchemaChangeSqlType.class))).thenReturn(mock(OnlineSchemaChangeScheduleTaskParameters.class));

 List<OnlineSchemaChangeScheduleTaskParameters> result = parameters.generateSubTaskParameters(connectionConfig, schema);
 assertEquals(1, result.size());
 OnlineSchemaChangeScheduleTaskParameters parameter = result.get(0);
 String newAlterStmt = DdlUtils.replaceTableName(sqlContent, "test2", connectionConfig.getDialectType(), OnlineSchemaChangeSqlType.ALTER);
 assertEquals(newAlterStmt, parameter.getSqlsToBeExecuted().get(0));
 }
}