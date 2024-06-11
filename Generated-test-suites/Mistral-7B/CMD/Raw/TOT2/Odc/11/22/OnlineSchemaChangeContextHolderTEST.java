package com.oceanbase.odc.service.onlineschemachange.test;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.MDC;

import com.oceanbase.odc.core.shared.constant.OdcConstants;
import com.oceanbase.odc.service.iam.util.SecurityContextUtils;

public class OnlineSchemaChangeContextHolderTest {

 @Test
 public void testTraceWithValidInput() {
 Long userId = 1L;
 Long taskId = 2L;
 Long organizationId = 3L;
 OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);
 String taskIdValue = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID);
 String organizationIdValue = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.ODC_CONSTANTS_ORGANIZATION_ID);
 Assert.assertEquals(taskId.toString(), taskIdValue);
 Assert.assertEquals(organizationId.toString(), organizationIdValue);
 }

 @Test
 public void testTraceWithStringInput() {
 String userId = "1";
 String taskId = "2";
 String organizationId = "3";
 OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);
 String taskIdValue = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID);
 String organizationIdValue = OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.ODC_CONSTANTS_ORGANIZATION_ID);
 Assert.assertEquals(taskId, taskIdValue);
 Assert.assertEquals(organizationId, organizationIdValue);
 }

 @Test
 public void testClear() {
 OnlineSchemaChangeContextHolder.clear();
 Assert.assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.TASK_ID));
 Assert.assertNull(OnlineSchemaChangeContextHolder.get(OnlineSchemaChangeContextHolder.ODC_CONSTANTS_ORGANIZATION_ID));
 }

 @Test
 public void testGetWithValidKey() {
 String key = OnlineSchemaChangeContextHolder.TASK_ID;
 String value = OnlineSchemaChangeContextHolder.get(key);
 Assert.assertNotNull(value);
 }

 @Test(expected = NullPointerException.class)
 public void testGetWithInvalidKey() {
 String key = "invalidKey";
 OnlineSchemaChangeContextHolder.get(key);
 }

 @Test(expected = NumberFormatException.class)
 public void testTraceWithNullInputForLong() {
 Long userId = null;
 Long taskId = null;
 Long organizationId = null;
 OnlineSchemaChangeContextHolder.trace(userId, taskId, organizationId);
 }

 @Test
 public void testTraceWithValidInputButNullSecurityContextUtils() {
 doNothing().when(SecurityContextUtils.class).setCurrentUser(anyLong(), anyLong(), any());
 OnlineSchemaChangeChangeContextHolder.trace(1L, 2L, 3L);
 verify(SecurityContextUtils, times(0)).setCurrentUser(anyLong(), anyLong(), any());
 }

 @Test
 public void testTraceWithValidInputButNullMDC() {
 doThrow(NullPointerException.class).when(MDC.class).put(anyString(), any());
 OnlineSchemaChangeContextHolder.trace(1L, 2L, 3L);
 verify(MDC, times(0)).put(OnlineSchemaChangeContextHolder.TASK_WORK_SPACE, anyString());
 verify(MDC, times(0)).put(OnlineSchemaChangeContextHolder.TASK_ID, anyString());
 verify(MDC, times(0)).put(OdcConstants.ORGANIZATION_ID, anyString());
 }
}