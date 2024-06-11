import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import com.oceanbase.odc.core.shared.constant.ErrorCodes;
import com.oceanbase.odc.service.onlineschemachange.OscService;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeParameters;
import com.oceanbase.odc.service.onlineschemachange.model.OnlineSchemaChangeScheduleTaskResult;
import com.oceanbase.odc.service.onlineschemachange.model.SwapTableType;
import com.oceanbase.odc.service.schedule.model.JobType;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OscServiceTest {

 private OscService oscService;

 @Autowired
 private DatabaseRepository databaseRepository;
 @Autowired
 private ConnectionService connectionService;
 @Autowired
 private ScheduleTaskRepository scheduleTaskRepository;
 @Autowired
 private ScheduleRepository scheduleRepository;

 @InjectMocks
 private OscService systemUnderTest;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 ReflectionTestUtils.setFieldValue(systemUnderTest, "databaseRepository", databaseRepository);
 ReflectionTestUtils.setFieldValue(systemUnderTest, "connectionService", connectionService);
 ReflectionTestUtils.setFieldValue(systemUnderTest, "scheduleTaskRepository", scheduleTaskRepository);
 ReflectionTestUtils.setFieldValue(systemUnderTest, "scheduleRepository", scheduleRepository);
 }

 @Test
 public void testGetOscDatabaseInfo_InvalidId() {
 // Given
 long id = 0;
 // When
 OscLockDatabaseUserInfo result = systemUnderTest.getOscDatabaseInfo(id);
 // Then
 assertNull(result);
 }

 @Test
 public void testGetOscDatabaseInfo_ValidId() {
 // Given
 DatabaseEntity database = mock(DatabaseEntity.class);
 when(databaseRepository.findById(1L)).thenReturn(Optional.of(database));
 OscLockDatabaseUserInfo expectedResult = new OscLockDatabaseUserInfo();
 expectedResult.setDatabaseId(1L);
 expectedResult.setLockDatabaseUserRequired(true);
 // When
 OscLockDatabaseUserInfo result = systemUnderTest.getOscDatabaseInfo(1L);
 // Then
 assertEquals(expectedResult, result);
 }

 @Test
 public void testSwapTable_InvalidScheduleTaskId() {
 // Given
 long scheduleTaskId = 0;
 // When, Then
 assertThrows(IllegalArgumentException.class, () -> systemUnderTest.swapTable(scheduleTaskId));
 }

 @Test
 public void testSwapTable_ValidScheduleTaskId() {
 // Given
 ScheduleTaskEntity scheduleTask = mock(ScheduleTaskEntity.class);
 when(scheduleTaskRepository.findById(1L)).thenReturn(Optional.of(scheduleTask));
 ScheduleEntity schedule = mock(ScheduleEntity.class);
 when(scheduleRepository.findById(1L)).thenReturn(Optional.of(schedule));
 OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
 oscParameters.setSwapTableType(SwapTableType.MANUAL);
 JsonUtils jsonUtils = mock(JsonUtils.class);
 when(scheduleEntity.getJobParametersJson()).thenReturn(JsonUtils.toJson(oscParameters));
 OnlineSchemaChangeScheduleTaskResult expectedResult = new OnlineSchemaChangeScheduleTaskResult();
 expectedResult.setManualSwapTableEnabled(false);
 expectedResult.setManualSwapTableStarted(true);
 JsonUtils.setJsonFieldValue(expectedResult, "resultJson", JsonUtils.toJson(expectedResult));
 when(scheduleTask.getResultJson()).thenReturn("{}");
 // When
 OscSwapTableVO result = systemUnderTest.swapTable(1L);
 // Then
 assertEquals(new OscSwapTableVO(), result);
 }

 @Test
 public void testSwapTable_InvalidJobType() {
 // Given
 ScheduleEntity schedule = mock(ScheduleEntity.class);
 when(scheduleRepository.findById(1L)).thenReturn(Optional.of(schedule));
 OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
 oscParameters.setSwapTableType(SwapTableType.MANUAL);
 JsonUtils jsonUtils = mock(JsonUtils.class);
 when(scheduleEntity.getJobParametersJson()).thenReturn(JsonUtils.toJson(oscParameters));
 // When, Then
 assertThrows(IllegalArgumentException.class, () -> systemUnderTest.swapTable(1L));
 }

 @Test
 public void testSwapTable_InvalidSwapTableType() {
 // Given
 ScheduleEntity schedule = mock(ScheduleEntity.class);
 when(scheduleRepository.findById(1L)).thenReturn(Optional.of(schedule));
 OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
 oscParameters.setSwapTableType(SwapTableType.AUTOMATIC);
 JsonUtils jsonUtils = mock(JsonUtils.class);
 when(scheduleEntity.getJobParametersJson()).thenReturn(JsonUtils.toJson(oscParameters));
 // When, Then
 assertThrows(IllegalArgumentException.class, () -> systemUnderTest.swapTable(1L));
 }

 @Test
 public void testSwapTable_InvalidManualSwapTableEnabled() {
 // Given
 ScheduleEntity schedule = mock(ScheduleEntity.class);
 when(scheduleRepository.findById(1L)).thenReturn(Optional.of(schedule));
 OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
 oscParameters.setSwapTableType(SwapTableType.MANUAL);
 JsonUtils jsonUtils = mock(JsonUtils.class);
 when(scheduleEntity.getJobParametersJson()).thenReturn(JsonUtils.toJson(oscParameters));
 OnlineSchemaChangeScheduleTaskResult expectedResult = new OnlineSchemaChangeScheduleTaskResult();
 JsonUtils.setJsonFieldValue(expectedResult, "manualSwapTableEnabled", false);
 JsonUtils.setJsonFieldValue(expectedResult, "manualSwapTableStarted", true);
 JsonUtils.setJsonFieldValue(expectedResult, "resultJson", JsonUtils.toJson(expectedResult));
 when(scheduleTask.getResultJson()).thenReturn(JsonUtils.toJson(expectedResult));
 // When
 OscSwapTableVO result = systemUnderTest.swapTable(1L);
 // Then
 assertFalse(result.isManualSwapTableEnabled());
 }

 @Test
 public void testSwapTable_ValidManualSwapTableEnabled() {
 // Given
 ScheduleEntity schedule = mock(ScheduleEntity.class);
 when(scheduleRepository.findById(1L)).thenReturn(Optional.of(schedule));
 OnlineSchemaChangeParameters oscParameters = new OnlineSchemaChangeParameters();
 oscParameters.setSwapTableType(SwapTableType.MANUAL);
 JsonUtils jsonUtils = mock(JsonUtils.class);
 when(scheduleEntity.getJobParametersJson()).thenReturn(JsonUtils.toJson(oscParameters));
 OnlineSchemaChangeScheduleTaskResult expectedResult = new OnlineSchemaChangeScheduleTaskResult();
 expectedResult.setManualSwapTableEnabled(true);
 expectedResult.setManualSwapTableStarted(false);
 JsonUtils.setJsonFieldValue(expectedResult, "resultJson", JsonUtils.toJson(expectedResult));
 when(scheduleTask.getResultJson()).thenReturn("{}");
 // When
 OscSwapTableVO result = systemUnderTest.swapTable(1L);
 // Then
 assertFalse(result.isManualSwapTableEnabled());
 }
}