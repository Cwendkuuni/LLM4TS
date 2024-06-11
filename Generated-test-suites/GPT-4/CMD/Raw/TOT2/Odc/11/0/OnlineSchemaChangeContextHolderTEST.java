import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import org.slf4j.MDC;

import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeContextHolder;

import static org.junit.Assert.*;

public class OnlineSchemaChangeContextHolderTest {

  @Before
  public void setUp() {
    // Clear any context information before each test
    OnlineSchemaChangeContextHolder.clear();
  }

  @Test
  public void testTraceWithLongs() {
    OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
    assertEquals("1", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
    assertEquals("2", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
  }

  @Test
  public void testTraceWithStrings() {
    OnlineSchemaChangeContextHolder.trace("4", "5", "6");
    assertEquals("4", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
    assertEquals("5", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
  }

  @Test
  public void testRetrace() {
    Map<String, String> context = new HashMap<>();
    context.put("key", "value");
    OnlineSchemaChangeContextHolder.retrace(context);
    assertEquals("value", OnlineSchemaChangeContextHolder.get("key"));
  }

  @Test
  public void testClear() {
    OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
    OnlineSchemaChangeContextHolder.clear();
    assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
    assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
  }

  @Test
  public void testGet() {
    OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
    assertEquals("1", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
    assertNull(OnlineSchemaChangeContextHolder.get("noKey"));
  }

  @Test(expected = NumberFormatException.class)
  public void testTraceWithNullStrings() {
    OnlineSchemaChangeContextHolder.trace(null, null, null);
  }

  @Test(expected = NullPointerException.class)
  public void testRetraceNullContext() {
    OnlineSchemaChangeContextHolder.retrace(null);
  }

  @After
  public void tearDown() {
    // Clear context after each test to avoid test cross-contamination
    OnlineSchemaChangeContextHolder.clear();
  }
}