package com.oceanbase.odc.service.onlineschemachange;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.After;
import org.junit.Test;
import org.slf4j.MDC;

public class OnlineSchemaChangeContextHolderTest {

 private static final String TASK_ID = OnlineSchemaChangeContextHolder.TASK_ID;
 private static final String TASK_WORK_SPACE = OnlineSchemaChangeContextHolder.TASK_WORK_SPACE;
 private static final String ORGANIZATION_ID = OdcConstants.ORGANIZATION_ID;

 @After
 public void cleanUp() {
 OnlineSchemaChangeContextHolder.clear();
 }

 /**
 * Test method for {@link OnlineSchemaChangeContextHolder#trace(Long, Long, Long)}
 */
 @Test
 public void testTraceLongLongLong() {
 OnlineSchemaChangeContextHolder.trace(1L, 1L, 1L);

 assertEquals("1", OnlineSchemaChangeContextHolder.get(TASK_WORK_SPACE));
 assertEquals("1", OnlineSchemaChangeContextHolder.get(TASK_ID));
 assertEquals("1", OnlineSchemaChangeContextHolder.get(ORGANIZATION_ID));
 }

 /**
 * Test method for {@link OnlineSchemaChangeContextHolder#trace(String, String, String)}
 */
 @Test
 public void testTraceStringStringString() {
 OnlineSchemaChangeContextHolder.trace("1", "1", "1");

 assertEquals("1", OnlineSchemaChangeContextHolder.get(TASK_WORK_SPACE));
 assertEquals("1", OnlineSchemaChangeContextHolder.get(TASK_ID));
 assertEquals("1", OnlineSchemaChangeContextHolder.get(ORGANIZATION_ID));
 }

 /**
 * Test method for {@link OnlineSchemaChangeContextHolder#trace(Long, Long, Long)} with null parameters
 */
 @Test(expected = NumberFormatException.class)
 public void testTraceNullLongLongLong() {
 OnlineSchemaChangeContextHolder.trace(null, 1L, 1L);
 }

 /**
 * Test method for {@link OnlineSchemaChangeContextHolder#trace(String, String, String)} with null parameters
 */
 @Test(expected = NumberFormatException.class)
 public void testTraceNullStringStringString() {
 OnlineSchemaChangeContextHolder.trace(null, "1", "1");
 }

 /**
 * Test method for {@link OnlineSchemaChangeContextHolder#trace(Long, Long, Long)} with invalid Long values
 */
 @Test(expected = NumberFormatException.class)
 public void testTraceInvalidLongLongLong() {
 OnlineSchemaChangeContextHolder.trace("abc", 1L, 1L);
 }

 /**
 * Test method for {@link OnlineSchemaChangeContextHolder#trace(String, String, String)} with invalid String values
 */
 @Test(expected = NumberFormatException.class)
 public void testTraceInvalidStringStringString() {
 OnlineSchemaChangeContextHolder.trace("1", "abc", "1");
 }

 /**
 * Test method for {@link OnlineSchemaChangeContextHolder#retrace(Map<String, String>)}
 */
 @Test
 public void testRetrace() {
 Map<String, String> context = MDC.getCopyOfContextMap();
 OnlineSchemaChangeContextHolder.retrace(context);

 assertNull(OnlineSchemaChangeContextHolder.get(TASK_WORK_SPACE));
 assertNull(OnlineSchemaChangeContextHolder.get(TASK_ID));
 assertNull(OnlineSchemaChangeContextHolder.get(ORGANIZATION_ID));
 }

 /**
 * Test method for {@link OnlineSchemaChangeContextHolder#clear()}
 */
 @Test
 public void testClear() {
 OnlineSchemaChangeContextHolder.trace(1L, 1L, 1L);
 OnlineSchemaChangeContextHolder.clear();

 assertNull(OnlineSchemaChangeContextHolder.get(TASK_WORK_SPACE));
 assertNull(OnlineSchemaChangeContextHolder.get(TASK_ID));
 assertNull(OnlineSchemaChangeContextHolder.get(ORGANIZATION_ID));
 }

 /**
 * Test method for {@link OnlineSchemaChangeContextHolder#get(String)} with existing key
 */
 @Test
 public void testGetExistingKey() {
 OnlineSchemaChangeContextHolder.trace(1L, 1L, 1L);

 assertEquals("1", OnlineSchemaChangeContextHolder.get(TASK_WORK_SPACE));
 }

 /**
 * Test method for {@link OnlineSchemaChangeContextHolder#get(String)} with non-existing key
 */
 @Test
 public void testGetNonExistingKey() {
 OnlineSchemaChangeContextHolder.trace(1L, 1L, 1L);

 assertNull(OnlineSchemaChangeContextHolder.get("nonExistingKey"));
 }
}