import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.util.Map;
import java.util.HashMap;

import org.junit.Test;

public class OnlineSchemaChangeContextHolderTest {

   @Test
   public void testTrace() {
      Long userId = 1L;
      Long taskId = 2L;
      Long organizationId = 3L;

      Map<String, String> context = mock(Map.class);

      OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);

      assertEquals("" + userId, OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
      assertEquals("" + organizationId, OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));

      OnlineSchemaChangeContextHolder.retrace(context);
      assertEquals("" + userId, context.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
      assertEquals("" + taskId, context.get(OnlineSchemaChangeContextHolder.TASK_ID));
      assertEquals("" + organizationId, context.get(OdcConstants.ORGANIZATION_ID));
   }

   @Test
   public void testTrace_NullInput() {
      OnlineSchemaChangeContextHolder.trace(null, null, null);
      assertEquals(null, OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
      assertEquals(null, OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
   }

   @Test
   public void testTrace_EmptyStringInput() {
      OnlineSchemaChangeContextHolder.trace("", "", "");
      assertEquals("", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
      assertEquals("", OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
   }

   @Test(expected = NumberFormatException.class)
   public void testTrace_InvalidLongInput() {
      OnlineSchemaChangeContextHolder.trace(-1, null, null);
   }

   @Test
   public void testTrace_EmptyMapInRetrace() {
      Map<String, String> context = new HashMap<>();
      OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
      OnlineSchemaChangeContextHolder.retrace(context);
      assertEquals(null, context.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
      assertEquals(null, context.get(OnlineSchemaChangeContextHolder.TASK_ID));
      assertEquals(null, context.get(OdcConstants.ORGANIZATION_ID));
   }

   @Test
   public void testClear() {
      OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
      OnlineSchemaChangeContextHolder.clear();
      assertEquals(null, OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
      assertEquals(null, OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
   }

   @Test
   public void testGet() {
      OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
      assertEquals("" + 1, OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
      assertEquals("" + 2, OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
      assertEquals("" + 3, OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
   }
}