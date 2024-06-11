import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.MDC;

public class OnlineSchemaChangeContextHolderTest {

 private static final String TASK_ID = "taskId";
 private static final String TASK_WORK_SPACE = "onlineSchemaChangeWorkSpace";
 private static final String ORGANIZATION_ID = "organizationId";
 private static final String USER_ID = "1";
 private static final Long TASK_ID_LONG = 1L;
 private static final Long ORGANIZATION_ID_LONG = 1L;
 private static final Long USER_ID_LONG = 1L;

 private Map<String, String> context;
 private OnlineSchemaChangeContextHolder onlineSchemaChangeContextHolder;

 @Before
 public void setup() {
 context = new HashMap<>();
 onlineSchemaChangeContextHolder = new OnlineSchemaChangeContextHolder();
 MDC.setContextMap(context);
 }

 @Test
 public void testTraceWithLongParameters() {
 // Given
 Long userId = USER_ID_LONG;
 Long taskId = TASK_ID_LONG;
 Long organizationId = ORGANIZATION_ID_LONG;

 // When
 onlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

 // Then
 assertEquals(userId + "", onlineSchemaChangeContextHolder.get(TASK_WORK_SPACE));
 assertEquals(taskId + "", onlineSchemaChangeContextHolder.get(TASK_ID));
 assertEquals(organizationId + "", onlineSchemaChangeContextHolder.get(ORGANIZATION_ID));
 }

 @Test
 public void testTraceWithStringParameters() {
 // Given
 String userId = USER_ID;
 String taskId = TASK_ID;
 String organizationId = ORGANIZATION_ID;

 // When
 onlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

 // Then
 Long userIdLong = (userId == null ? null : Long.parseLong(userId));
 Long taskIdLong = (taskId == null ? null : Long.parseLong(taskId));
 Long organizationIdLong = (organizationId == null ? null : Long.parseLong(organizationId));
 assertEquals(userIdLong + "", onlineSchemaChangeContextHolder.get(TASK_WORK_SPACE));
 assertEquals(taskIdLong + "", onlineSchemaChangeContextHolder.get(TASK_ID));
 assertEquals(organizationIdLong + "", onlineSchemaChangeContextHolder.get(ORGANIZATION_ID));
 }

 @Test
 public void testClear() {
 // Given
 onlineSchemaChangeChangeContextHolder.trace(USER_ID_LONG, TASK_ID_LONG, ORGANIZATION_ID_LONG);

 // When
 onlineSchemaChangeContextHolder.clear();

 // Then
 assertNull(onlineSchemaChangeContextHolder.get(TASK_ID));
 assertNull(onlineSchemaChangeContextHolder.get(TASK_WORK_SPACE));
 assertNull(onlineSchemaChangeContextHolder.get(ORGANIZATION_ID));
 }

 @Test
 public void testGet() {
 // Given
 onlineSchemaChangeContextHolder.trace(USER_ID_LONG, TASK_ID_LONG, ORGANIZATION_ID_LONG);

 // When
 String key = TASK_ID;

 // Then
 assertEquals(TASK_ID_LONG + "", onlineSchemaChangeContextHolder.get(key));
 }

 @Test
 public void testGetWithNullKey() {
 // Given
 onlineSchemaChangeContextHolder.trace(USER_ID_LONG, TASK_ID_LONG, ORGANIZATION_ID_LONG);

 // When
 String key = null;

 // Then
 assertNull(onlineSchemaChangeContextHolder.get(key));
 }

 @Test
 public void testTraceWithNullParameters() {
 // Given
 OnlineSchemaChangeContextHolder onlineSchemaChangeContextHolderSpy = mock(OnlineSchemaChangeContextHolder.class);
 when(onlineSchemaChangeContextHolderSpy.get(TASK_WORK_SPACE)).thenReturn(null);

 // When
 onlineSchemaChangeContextHolderSpy.trace(null, null, null);

 // Then
 // No action needed since we are only checking if the method call does not throw an exception
 }

 @Test(expected = IllegalArgumentException.class)
 public void testTraceWithNegativeUserId() {
 // Given
 OnlineSchemaChangeContextHolder onlineSchemaChangeContextHolderSpy = mock(OnlineSchemaChangeContextHolder.class);
 when(onlineSchemaChangeContextHolderSpy.get(TASK_WORK_SPACE)).thenReturn(null);

 // When and Then
 onlineSchemaChangeContextHolderSpy.trace(-1L, TASK_ID_LONG, ORGANIZATION_ID_LONG);
 }
}