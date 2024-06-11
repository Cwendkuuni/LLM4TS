
package com.oceanbase.odc.service.onlineschemachange;

import java.util.HashMap;
import java.util.Map;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class OnlineSchemaChangeContextHolderTest {

    @Before
    public void setUp() {
        OnlineSchemaChangeContextHolder.clear();
    }

    @After
    public void tearDown() {
        OnlineSchemaChangeContextHolder.clear();
    }

    @Test
    public void testTrace_LongParameters() {
        Long userId = 123L;
        Long taskId = 456L;
        Long orgId = 789L;

        OnlineSchemaChangeContextHolder.trace(userId, taskId, orgId);

        assertEquals(userId.toString(), OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals(taskId.toString(), OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals(orgId.toString(), OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test(expected=NumberFormatException.class)
    public void testTrace_StringParameters() {
        String userId = "abc";
        String taskId = "def";
        String orgId = "ghi";

        OnlineSchemaChangeContextHolder.trace(userId, taskId, orgId);

    }

    @Test
    public void testRetrace() {
        Map<String, String> context = new HashMap<String, String>();
        context.put("Sample Key", "Sample Value");

        OnlineSchemaChangeContextHolder.retrace(context);

        assertEquals("Sample Value", OnlineSchemaChangeContextHolder.get("Sample Key"));
    }

    @Test
    public void testClear() {
        OnlineSchemaChangeContextHolder.trace(123L, 456L, 789L);
        OnlineSchemaChangeContextHolder.clear();

        assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertNull(OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void testGet() {
        String key = "Key";
        assertNull(OnlineSchemaChangeContextHolder.get(key));

        String value = "Value";
        OnlineSchemaChangeContextHolder.trace(value, value, value);
        assertEquals(value, OnlineSchemaChangeContextHolder.get(key));
    }
}
