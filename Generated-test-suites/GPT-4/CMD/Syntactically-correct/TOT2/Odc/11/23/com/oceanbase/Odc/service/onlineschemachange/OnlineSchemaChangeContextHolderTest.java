package com.oceanbase.odc.service.onlineschemachange;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;


public class OnlineSchemaChangeContextHolderTest {

    @Before
    public void cleanContext() {
         OnlineSchemaChangeContextHolder.clear();
    }

    @Test
    public void testTraceLong() {
        OnlineSchemaChangeContextHolder.trace(
           1L,
           2L,
           3L
        );

        String expected = "1";
        String actual = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE);
        assertEquals(expected, actual);
    }

    @Test
    public void testTraceString() {
        OnlineSchemaChangeContextHolder.trace(
           "1",
           "2",
           "3"
        );

        String expected = "1";
        String actual = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE);
        assertEquals(expected, actual);
    }

    @Test
    public void testRetrace() {
        Map<String, String> context = new HashMap<>();
        context.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "test");

        OnlineSchemaChangeContextHolder.retrace(context);

        String expected = "test";
        String actual = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE);
        assertEquals(expected, actual);
    }

    @Test
    public void testClear() {
        OnlineSchemaChangeContextHolder.trace(
           1L,
           2L,
           3L
        );

        OnlineSchemaChangeContextHolder.clear();

        String actual = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE);
        assertEquals(null, actual);
    }

    // more test case s...
}
