package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.slf4j.MDC;

import com.oceanbase.odc.core.shared.constant.OdcConstants;
import com.oceanbase.odc.service.iam.util.SecurityContextUtils;

@RunWith(MockitoJUnitRunner.class)
public class OnlineSchemaChangeContextHolderTest {

 private static final String USER_ID = "1";
 private static final String TASK_ID = "2";
 private static final String ORGANIZATION_ID = "3";

 @Test
 public void testTraceWithLongArgs() {
 OnlineSchemaChangeContextHolder.trace(Long.parseLong(USER_ID), Long.parseLong(TASK_ID), Long.parseLong(ORGANIZATION_ID));

 Map<String, String> context = MDC.getCopyOfContextMap();
 assertEquals(USER_ID, context.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
 assertEquals(TASK_ID, context.get(OnlineSchemaChangeContextHolder.TASK_ID));
 assertEquals(ORGANIZATION_ID, context.get(OdcConstants.ORGANIZATION_ID));

 SecurityContextUtils securityContextUtils = Mockito.mock(SecurityContextUtils.class);
 BDDMockito.given(securityContextUtils.getCurrentUser()).willReturn(null);
 OnlineSchemaChangeContextHolder.trace(Long.parseLong(USER_ID), Long.parseLong(TASK_ID), Long.parseLong(ORGANIZATION_ID), securityContextUtils);

 Map<String, String> contextAfterTrace = MDC.getCopyOfContextMap();
 assertEquals(USER_ID, contextAfterTrace.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
 assertEquals(TASK_ID, contextAfterTrace.get(OnlineSchemaChangeContextHolder.TASK_ID));
 assertEquals(ORGANIZATION_ID, contextAfterTrace.get(OdcConstants.ORGANIZATION_ID));
 }

 @Test
 public void testTraceWithStringArgs() {
 OnlineSchemaChangeContextHolder.trace(USER_ID, TASK_ID, ORGANIZATION_ID);

 Map<String, String> context = MDC.getCopyOfContextMap();
 assertEquals(USER_ID, context.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
 assertEquals(TASK_ID, context.get(OnlineSchemaChangeContextHolder.TASK_ID));
 assertEquals(ORGANIZATION_ID, context.get(OdcConstants.ORGANIZATION_ID));
 }

 @Test
 public void testRetrace() {
 OnlineSchemaChangeContextHolder.trace(USER_ID, TASK_ID, ORGANIZATION_ID);
 Map<String, String> context = MDC.getCopyOfContextMap();
 OnlineSchemaChangeContextHolder.retrace(context);

 Map<String, String> contextAfterRetrace = MDC.getCopyOfContextMap();
 assertNull(contextAfterRetrace.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
 assertNull(contextAfterRetrace.get(OnlineSchemaChangeContextHolder.TASK_ID));
 assertNull(contextAfterRetrace.get(OdcConstants.ORGANIZATION_ID));
 }

 @Test
 public void testClear() {
 OnlineSchemaChangeContextHolder.trace(USER_ID, TASK_ID, ORGANIZATION_ID);
 OnlineSchemaChangeContextHolder.clear();

 Map<String, String> contextAfterClear = MDC.getCopyOfContextMap();
 assertNull(contextAfterClear.get(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE));
 assertNull(contextAfterClear.get(OnlineSchemaChangeContextHolder.TASK_ID));
 assertNull(contextAfterClear.get(OdcConstants.ORGANIZATION_ID));
 }

 @Test
 public void testGet() {
 OnlineSchemaChangeContextHolder.trace(USER_ID, TASK_ID, ORGANIZATION_ID);
 assertEquals(ORGANIZATION_ID, OnlineSchemaChangeContextHolder.get(OdcConstants.ORGANIZATION_ID));
 }
}