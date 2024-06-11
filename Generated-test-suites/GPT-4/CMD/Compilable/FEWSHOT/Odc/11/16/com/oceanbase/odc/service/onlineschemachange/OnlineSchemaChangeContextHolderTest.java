
package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.MDC;

import com.oceanbase.odc.core.shared.constant.OdcConstants;

@RunWith(MockitoJUnitRunner.class)
public class OnlineSchemaChangeContextHolderTest {

    @Mock
    private MDC mdc;

    @Test
    public void testTraceLong() {
        Long userId = 1L;
        Long taskId = 2L;
        Long organizationId = 3L;

        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);
        
        verify(mdc, times(1)).put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, userId + "");
        verify(mdc, times(1)).put(OnlineSchemaChangeContextHolder.TASK_ID, taskId + "");
        verify(mdc, times(1)).put(OdcConstants.ORGANIZATION_ID, organizationId + "");
    }

    @Test
    public void testTraceString() {
        String userId = "1";
        String taskId = "2";
        String organizationId = "3";

        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);
        
        verify(mdc, times(1)).put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, userId);
        verify(mdc, times(1)).put(OnlineSchemaChangeContextHolder.TASK_ID, taskId);
        verify(mdc, times(1)).put(OdcConstants.ORGANIZATION_ID, organizationId);
    }

    @Test
    public void testRetrace() {
        Map<String, String> context = new HashMap<>();
        context.put("key1", "value1");
        context.put("key2", "value2");

        OnlineSchemaChangeContextHolder.retrace(context);
        verify(mdc, times(1)).setContextMap(context);
    }

    @Test
    public void testClear() {
        OnlineSchemaChangeContextHolder.clear();
        verify(mdc, times(1)).clear();
    }

    @Test
    public void testGet() {
        String key = "sampleKey";
        String value = "sampleValue";
        when(mdc.get(key)).thenReturn(value);
        
        String returnValue = OnlineSchemaChangeContextHolder.get(key);
        assertEquals(value, returnValue);
    }
}
