package com.oceanbase.odc.service.onlineschemachange;
import com.oceanbase.odc.service.onlineschemachange.OnlineSchemaChangeContextHolder;
import org.junit.Test;
import org.slf4j.MDC;

import java.util.Map;

import static org.junit.Assert.*;

public class OnlineSchemaChangeContextHolderTest {

    @Test
    public void traceShouldChangeContextForNumbers() {
        OnlineSchemaChangeContextHolder.trace(123L, 321L, 213L);
        assertEquals("123", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("321", MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("213", MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void traceShouldChangeContextForStrings() {
        OnlineSchemaChangeContextHolder.trace("123", "321", "213");
        assertEquals("123", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertEquals("321", MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
        assertEquals("213", MDC.get(OdcConstants.ORGANIZATION_ID));
    }

    @Test
    public void retraceShouldSetNewContext() {
        Map<String, String> newContext = new HashMap<>();
        newContext.put("newKey", "newValue");
        OnlineSchemaChangeContextHolder.retrace(newContext);
        assertEquals("newValue", MDC.get("newKey"));
    }

    @Test
    public void clearShouldEmptyContext() {
        OnlineSchemaChangeContextHolder.trace(123L, 321L, 213L);
        OnlineSchemaChangeContextHolder.clear();
        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
        assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
    }

    @Test
    public void getShouldRetrieveCorrectValue() {
        OnlineSchemaChangeContextHolder.trace(123L, 321L, 213L);
        assertEquals("321", OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
    }

    @Test(expected = NumberFormatException.class)
    public void traceShouldThrowExceptionForInvalidStrings() {
        OnlineSchemaChangeContextHolder.trace("invalid", "321", "213");
    }
}