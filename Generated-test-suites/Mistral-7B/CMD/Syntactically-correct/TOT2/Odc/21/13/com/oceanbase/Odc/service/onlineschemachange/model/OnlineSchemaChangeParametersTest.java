import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.oceanbase.odc.core.connection.model.ConnectionConfig;
import com.oceanbase.odc.core.shared.constant.TaskErrorStrategy;
import com.oceanbase.odc.service.onlineschemachange.ddl.DdlUtils;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskParameters;
import com.oceanbase.odc.service.onlineschemachange.subtask.SubTaskParameterFactory;

public class OnlineSchemaChangeParametersTest {

 private static final String DATABASE_NAME = "testDB";
 private static final String TABLE_NAME = "testTable";
 private static final String NEW_TABLE_NAME = "newTestTable";
 private static final String SQL_CONTENT = "ALTER TABLE testTable ADD COLUMN newColumn INT;";
 private static final OnlineSchemaChangeSqlType SQL_TYPE = OnlineSchemaChangeSqlType.ALTER;
 private static final String DELIMITER = ";";
 private static final TaskErrorStrategy ERROR_STRATEGY = TaskErrorStrategy.CONTINUE;
 private static final Integer LOCK_TABLE_TIMEOUT_SECONDS = 30;
 private static final Integer SWAP_TABLE_NAME_RETRY_TIMES = 3;
 private static final OriginTableCleanStrategy ORIGIN_TABLE_CLEAN_STRATEGY = OriginTableCleanStrategy.DROP;
 private static final List<String> LOCK_USERS = Arrays.asList("user1", "user2");
 private static final SwapTableType SWAP_TABLE_TYPE = SwapTableType.OLD_TO_NEW;

 private OnlineSchemaChangeParameters parameters;
 private ConnectionConfig connectionConfig;
 private SubTaskParameterFactory subTaskParameterFactory;

 @Before
 public void setup() {
 connectionConfig = mock(ConnectionConfig.class);
 subTaskParameterFactory = mock(SubTaskParameterFactory.class);
 parameters = new OnlineSchemaChangeParameters(SQL_TYPE, SQL_CONTENT, ERROR_STRATEGY, LOCK_TABLE_TIMEOUT_SECONDS,
 SWAP_TABLE_NAME_RETRY_TIMES, ORIGIN_TABLE_CLEAN_STRATEGY, DELIMITER, LOCK_USERS, SWAP_TABLE_TYPE);
 }

 @Test
 public void testIsContinueOnError() {
 assertTrue(parameters.isContinueOnError());
 }

 @Test
 public void testGenerateSubTaskParametersHappyPath() {
 when(subTaskParameterFactory.generate(anyString(), eq(SQL_TYPE)))
 .thenReturn(new OnlineSchemaChangeScheduleTaskParameters(DATABASE_NAME, TABLE_NAME, SQL_TYPE, null));

 List<OnlineSchemaChangeScheduleTaskParameters> result = parameters.generateSubTaskParameters(connectionConfig, DATABASE_NAME);
 assertEquals(1, result.size());
 }

 @Test(expected = UnexpectedException.class)
 public void testGenerateSubTaskParametersException() {
 when(subTaskParameterFactory.generate(anyString(), eq(SQL_TYPE)))
 .thenThrow(new RuntimeException("Error generating task parameters."));

 parameters.generateSubTaskParameters(connectionConfig, DATABASE_NAME);
 fail("Exception not thrown.");
 }

 @Test
 public void testGenerateSubTaskParametersWithMultipleSqls() {
 String multipleSqls = "ALTER TABLE testTable ADD COLUMN newColumn INT;\nALTER TABLE testTable ADD COLUMN newColumn2 INT;";
 when(subTaskParameterFactory.generate(anyString(), eq(SQL_TYPE)))
 .thenAnswer(i -> new OnlineSchemaChangeScheduleTaskParameters(DATABASE_NAME, TABLE_NAME, SQL_TYPE, Arrays.asList(SQL_CONTENT)));

 List<OnlineSchemaChangeScheduleTaskParameters> result = parameters.generateSubTaskParameters(connectionConfig, DATABASE_NAME);
 assertEquals(1, result.size());
 assertEquals(2, result.get(0).getSqlsToBeExecuted().size());
 }

}