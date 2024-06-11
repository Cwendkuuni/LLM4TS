
package com.oceanbase.odc.service.onlineschemachange;

import org.junit.Test;
import org.junit.Before;
import org.slf4j.MDC;
import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import com.oceanbase.odc.service.iam.util.SecurityContextUtils;
import com.oceanbase.odc.core.shared.constant.OdcConstants;

public class OnlineSchemaChangeContextHolderTest {

@Before
public void setUp() throws Exception {
SecurityContextUtils.setCurrentUser(null, null, null);
MDC.clear();
}

@Test
public void testTraceLongs() {
    OnlineSchemaChangeContextHolder.trace(123L, 456L, 789L);

    assertEquals("123", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
    assertEquals("456", MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
    assertEquals("789", MDC.get(OdcConstants.ORGANIZATION_ID));
    // validate if the current user was properly set
    assertEquals(new Long(123), SecurityContextUtils.getCurrentUser().getUserId());
    assertEquals(new Long(789), SecurityContextUtils.getCurrentUser().getOrgId());
}

@Test
public void testTraceStrings() {
    OnlineSchemaChangeContextHolder.trace("123", "456", "789");

    assertEquals("123", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
    assertEquals("456", MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
    assertEquals("789", MDC.get(OdcConstants.ORGANIZATION_ID));
    // validate if the current user was properly set
    assertEquals(new Long(123), SecurityContextUtils.getCurrentUser().getUserId());
    assertEquals(new Long(789), SecurityContextUtils.getCurrentUser().getOrgId());
}

@Test
public void testRetrace() {
    Map<String, String> context = new HashMap<>();
    context.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "123");
    context.put(OnlineSchemaChangeContextHolder.TASK_ID, "456");
    context.put(OdcConstants.ORGANIZATION_ID, "789");
    
    OnlineSchemaChangeContextHolder.retrace(context);

    assertEquals("123", MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
    assertEquals("456", MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
    assertEquals("789", MDC.get(OdcConstants.ORGANIZATION_ID));
}

@Test
public void testClear() {
    MDC.put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, "123");
    MDC.put(OnlineSchemaChangeContextHolder.TASK_ID, "456");
    MDC.put(OdcConstants.ORGANIZATION_ID, "789");

    OnlineSchemaChangeContextHolder.clear();

    assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
    assertNull(MDC.get(OnlineSchemaChangeContextHolder.TASK_ID));
    assertNull(MDC.get(OdcConstants.ORGANIZATION_ID));
}

@Test
public void testGet() {
    MDC.put("key", "value");
    assertEquals("value", OnlineSchemaChangeContextHolder.get("key"));
    MDC.remove("key");
}

}
