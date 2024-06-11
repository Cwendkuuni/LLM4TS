import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.oceanbase.odc.core.flow.model.TaskParameters;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.core.shared.exception.UnexpectedException;
import com.oceanbase.odc.core.shared.model.TableIdentity;
import com.oceanbase.odc.service.common.util.SqlUtils;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OriginTableCleanStrategy;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactoryImpl;
import lombok.SneakyThrows;

public class OnlineSchemaChangeParametersTest {

 private ConnectionConfig connectionConfig;
 private SubTaskParameterFactory subTaskParameterFactory;

 @Before
 public void setUp() {
 initMocks(this);
 connectionConfig = new ConnectionConfig();
 subTaskParameterFactory = new SubTaskParameterFactoryImpl(connectionConfig);
 }

 @Test
 public void testOnlineSchemaChangeParameters() {
 // Test case for constructor and getters and setters
 OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
 assertNull(params.getSqlType());
 assertNull(params.getSqlContent());
 assertNull(params.getErrorStrategy());
 // set values and check getters
 params.setSqlType(OnlineSchemaChangeSqlType.ALTER);
 params.setSqlContent("test sql content");
 params.setErrorStrategy(TaskErrorStrategy.CONTINUE);
 assertEquals(OnlineSchemaChangeSqlType.ALTER, params.getSqlType());
 assertEquals("test sql content", params.getSqlContent());
 assertEquals(TaskErrorStrategy.CONTINUE, params.getErrorStrategy());
 }

 @Test
 public void testIsContinueOnError() {
 OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
 assertFalse(params.isContinueOnError());
 params.setErrorStrategy(TaskErrorStrategy.CONTINUE);
 assertTrue(params.isContinueOnError());
 params.setErrorStrategy(TaskErrorStrategy.ABORT);
 assertFalse(params.isContinueOnError());
 }

 @Test
 public void testMethodsWithValidInput() {
 OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
 params.setSqlType(OnlineSchemaChangeSqlType.ALTER);
 params.setSqlContent("test sql content");
 params.setErrorStrategy(TaskErrorStrategy.CONTINUE);
 params.setLockTableTimeOutSeconds(30);
 params.setSwapTableNameRetryTimes(5);
 params.setOriginTableCleanStrategy(OriginTableCleanStrategy.DELETE_OLD);
 params.setDelimiter(";");
 params.setLockUsers(new ArrayList<>());
 params.setSwapTableType(SwapTableType.SWAP_TABLE);
 verifyNoMoreInteractions();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMethodsWithInvalidInput() {
 OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
 params.setSqlType(null);
 }

 @Test
 public void testGenerateSubTaskParameters() {
 // Test case for generateSubTaskParameters method with valid input
 ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
 OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
 when(subTaskParameterFactory.generate(anyString(), eq(OnlineSchemaChangeSqlType.ALTER)))
 .thenReturn(mock(OnlineSchemaChangeScheduleTaskParameters.class));
 params.setSqlContent("test sql content; test sql content2");
 params.setSqlType(OnlineSchemaChangeSqlType.ALTER);
 List<OnlineSchemaChangeScheduleTaskParameters> expectedTaskParameters = new ArrayList<>();
 OnlineSchemaChangeScheduleTaskParameters taskParameter = mock(OnlineSchemaChangeScheduleTaskParameters.class);
 when(taskParameter.getDatabaseName()).thenReturn("testDB");
 when(taskParameter.getOriginTableName()).thenReturn("testTable");
 expectedTaskParameters.add(taskParameter);
 params.generateSubTaskParameters(connectionConfig, "testSchema");
 verify(subTaskParameterFactory, atLeast(2)).generate(anyString(), eq(OnlineSchemaChangeSqlType.ALTER));
 }

 @Test(expected = UnexpectedException.class)
 public void testGenerateSubTaskParametersWithException() {
 ConnectionConfig connectionConfig = mock(ConnectionConfig.class);
 OnlineSchemaChangeParameters params = new OnlineSchemaChangeParameters();
 when(subTaskParameterFactory.generate(anyString(), eq(OnlineSchemaChangeSqlType.ALTER)))
 .thenThrow(new UnexpectedException("test exception"));
 params.setSqlContent("test sql content");
 params.setSqlType(OnlineSchemaChangeSqlType.ALTER);
 List<OnlineSchemaChangeScheduleTaskParameters> expectedTaskParameters = new ArrayList<>();
 OnlineSchemaChangeScheduleTaskParameters taskParameter = mock(OnlineSchemaChangeScheduleTaskParameters.class);
 expectedTaskParameters.add(taskParameter);
 try {
 params.generateSubTaskParameters(connectionConfig, "testSchema");
 } catch (UnexpectedException e) {
 // do nothing
 }
 verify(subTaskParameterFactory, never()).generate(anyString(), eq(OnlineSchemaChangeSqlType.ALTER));
 }
}