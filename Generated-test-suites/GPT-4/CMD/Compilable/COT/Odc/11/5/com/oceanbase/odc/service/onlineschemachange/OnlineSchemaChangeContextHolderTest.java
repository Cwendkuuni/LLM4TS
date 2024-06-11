package com.oceanbase.odc.service.onlineschemachange;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.MDC;

public class OnlineSchemaChangeContextHolderTest {

@Before
public void setUp(){
    // Set up code before each test
    Map<String, String> map = new HashMap<>();
    map.put("TEST", "VALUE");
    MDC.setContextMap(map);
}

@Test
public void testTraceWithStrings() {
    OnlineSchemaChangeContextHolder.trace("1", "2", "3");
    assertEquals("1", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
    assertEquals("2", MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
}

@Test
public void testTraceWithLongs() {
    OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
    assertEquals("1", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
    assertEquals("2", MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
}

@Test
public void testRetrace() {
    Map<String, String> map = new HashMap<>();
    map.put("TEST", "VALUE");
    OnlineSchemaChangeContextHolder.retrace(map);
    assertEquals("VALUE", MDC.get("TEST"));
}

@Test
public void testClear() {
    OnlineSchemaChangeContextHolder.clear();
    assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
    assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
}

@Test
public void testGet() {
    String key = OnlineSchemaChangeContextHolder.TASK_WORK_SPACE;
    assertEquals("1", OnlineSchemaChangeContextHolder.get(key));
}

@After
public void tearDown() {
    // Clean up after each test
    MDC.clear();
}
}
