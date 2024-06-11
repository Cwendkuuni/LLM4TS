package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.MDC;

import com.oceanbase.odc.core.shared.constant.OdcConstants;
import com.oceanbase.odc.service.iam.util.SecurityContextUtils;

@RunWith(MockitoJUnitRunner.class)
public class OnlineSchemaChangeContextHolderTest {
    @Mock
    private Map<String, String> mockMDC;
    
    @Test
    public void testTrace_LongInputs() {
        Long userId = 1l;
        Long taskId = 2l;
        Long orgId = 3l;

        OnlineSchemaChangeContextHolder.trace(userId, taskId, orgId);

        assertEquals(MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE), userId.toString());
        assertEquals(MDC.get(OnlineSchemaChangeContextHolder.TASK_ID), taskId.toString());
        assertEquals(MDC.get(OdcConstants.ORGANIZATION_ID), orgId.toString());
    }

    @Test
    public void testTrace_StringInputs() {
        String userId = "1";
        String taskId = "2";
        String orgId = "3";

        OnlineSchemaChangeContextHolder.trace(userId, taskId, orgId);

        assertEquals(MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE), userId);
        assertEquals(MDC.get(OnlineSchemaChangeContextHolder.TASK_ID), taskId);
        assertEquals(MDC.get(OdcConstants.ORGANIZATION_ID), orgId);
    }

    @Test(expected = NumberFormatException.class)
    public void testTrace_StringInputs_NotANumber() {
        String userId = "notANumber";
        OnlineSchemaChangeContextHolder.trace(userId, "1", "1");
    }

    @Test
    public void testRetrace() {
        Map<String, String> context = new HashMap<>();
        context.put("key1", "value1");
        context.put("key2", "value2");

        OnlineSchemaChangeContextHolder.retrace(context);

        verify(mockMDC).setContextMap(context);
    }

    @Test
    public void testClear() {
        OnlineSchemaChangeContextHolder.clear();
        assertNull(MDC.getCopyOfContextMap());
    }

    @Test
    public void testGet() {
        String key = "key";
        String value = "value";
        when(mockMDC.get(key)).thenReturn(value);

        String result = OnlineSchemaChangeContextHolder.get(key);

        assertEquals(value, result);
    }
}