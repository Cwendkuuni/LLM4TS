
package com.oceanbase.odc.service.onlineschemachange;

import org.junit.*;
import org.slf4j.MDC;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.oceanbase.odc.service.iam.util.SecurityContextUtils;
import java.util.HashMap;
import java.util.Map;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class OnlineSchemaChangeContextHolderTest {

    @InjectMocks
    OnlineSchemaChangeContextHolder onlineSchemaChangeContextHolder;

    @Mock
    MDC mdc;

    @Mock
    SecurityContextUtils securityContextUtils;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testTraceLongInputs() {
        Long userId = 10L;
        Long taskId = 20L;
        Long organizationId = 30L;
        onlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);
        verify(mdc).put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "10");
        verify(mdc).put(OnlineSchemaChangeContextHolder.TASK_ID, "20");
        verify(mdc).put(OdcConstants.ORGANIZATION_ID, "30");
    }

    @Test
    public void testTraceStringInputs() {
        String userId = "10";
        String taskId = "20";
        String organizationId = "30";
        onlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);
        verify(mdc).put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, userId);
        verify(mdc).put(OnlineSchemaChangeContextHolder.TASK_ID, taskId);
        verify(mdc).put(OdcConstants.ORGANIZATION_ID, organizationId);
    }

    @Test
    public void testRetrace() {
        Map<String, String> context = new HashMap<>();
        context.put("key1", "value1");
        onlineSchemaChangeContextHolder.retrace(context);
        verify(mdc).setContextMap(context);
    }

    @Test
    public void testClear() {
        onlineSchemaChangeContextHolder.clear();
        verify(mdc).clear();
    }

    @Test
    public void testGet() {
        String key = "key";
        onlineSchemaChangeContextHolder.get(key);
        verify(mdc).get(key);
    }

    @Test(expected = NumberFormatException.class)
    public void testTraceStringInputException() {
        String userId = "notANumber";
        String taskId = "20";
        String organizationId = "30";
        onlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);
    }
}
