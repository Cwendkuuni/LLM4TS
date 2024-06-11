
package com.oceanbase.odc.service.onlineschemachange;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.MDC;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;

import com.oceanbase.odc.core.shared.constant.OdcConstants;
import com.oceanbase.odc.service.iam.util.SecurityContextUtils;


@RunWith(MockitoJUnitRunner.class)
public class OnlineSchemaChangeContextHolderTest {

    @Mock
    SecurityContextUtils securityContextUtils;

    @Before
    public void setUp() {
        MDC.clear();
    }

    @Test
    public void testTraceLong() {
        Long userId = 1L;
        Long taskId = 2L;
        Long organizationId = 3L;
        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);
        assert (MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE)).equals(userId.toString());
        assert (MDC.get(OnlineSchemaChangeContextHolder.TASK_ID)).equals(taskId.toString());
        assert (MDC.get(OdcConstants.ORGANIZATION_ID)).equals(organizationId.toString());
    }

    @Test
    public void testTraceString() {
        String userId = "1";
        String taskId = "2";
        String organizationId = "3";
        OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);
        assert (MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE)).equals(userId);
        assert (MDC.get(OnlineSchemaChangeContextHolder.TASK_ID)).equals(taskId);
        assert (MDC.get(OdcConstants.ORGANIZATION_ID)).equals(organizationId);
    }

    @Test
    public void testClear() {
        MDC.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "1");
        OnlineSchemaChangeContextHolder.clear();
        assert(MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE)) == null;
    }

    @Test
    public void testRetrace() {
        Map<String, String> context = new HashMap<>();
        context.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "1");
        OnlineSchemaChangeContextHolder.retrace(context);
        assert(MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE)).equals("1");
    }

    @Test
    public void testGet() {
        MDC.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "1");
        assert(MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE)).equals("1");
    }
}
