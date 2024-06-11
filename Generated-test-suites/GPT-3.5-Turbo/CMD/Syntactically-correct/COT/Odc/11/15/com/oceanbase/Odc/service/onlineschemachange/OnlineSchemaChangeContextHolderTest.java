import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.util.Map;
import org.junit.Test;

public class OnlineSchemaChangeContextHolderTest {

  @Test
  public void testTrace_Long() {
    OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
    assertEquals("2", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
    assertEquals("1", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
    assertEquals("3", OnlineSchemaChangeContextHolder.get("OrganizationId"));
  }

  @Test
  public void testTrace_String() {
    OnlineSchemaChangeContextHolder.trace("1", "2", "3");
    assertEquals("2", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
    assertEquals("1", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
    assertEquals("3", OnlineSchemaChangeContextHolder.get("OrganizationId"));
  }

  @Test
  public void testRetrace() {
    OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
    Map<String, String> context = MDC.getCopyOfContextMap();
    OnlineSchemaChangeContextHolder.clear();
    OnlineSchemaChangeContextHolder.retrace(context);
    assertEquals("2", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
    assertEquals("1", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
    assertEquals("3", OnlineSchemaChangeContextHolder.get("OrganizationId"));
  }

  @Test
  public void testClear() {
    OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
    OnlineSchemaChangeContextHolder.clear();
    assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
    assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
    assertNull(OnlineSchemaChangeContextHolder.get("OrganizationId"));
  }

  @Test
  public void testGet() {
    OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
    assertEquals("2", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
    assertNull(OnlineSchemaChangeContextHolder.get("NonExistingKey"));
  }
}