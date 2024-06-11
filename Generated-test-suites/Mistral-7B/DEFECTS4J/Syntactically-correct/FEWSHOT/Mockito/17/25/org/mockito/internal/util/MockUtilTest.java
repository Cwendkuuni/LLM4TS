package org.mockito.internal.util;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.mockito.Mockito;
import org.mockito.internal.MockHandler;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.creation.MethodInterceptorFilter;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.util.reflection.LenientCopyTool;
import org.mockito.MockitoSession;
import org.mockito.exceptions.misusing.NotAMockException;

public class MockUtilTest {

 private MockitoSession session = Mockito.session();
 private CreationValidator creationValidator = mock(CreationValidator.class);
 private MockUtil mockUtil = new MockUtil(creationValidator);

 @SuppressWarnings("unchecked")
 @Test
 public void testCreateMock() {
 MockSettingsImpl settings = new MockSettingsImpl(session, creationValidator);
 Class<?> mockType = Mockito.class;
 Object mock = mockUtil.createMock(mockType, settings);
 assertEquals(mockType, mock.getClass());
 assertNotNull(mock);
 }

 @SuppressWarnings("unchecked")
 @Test(expected = NotAMockException.class)
 public void testCreateMockNull() {
 mockUtil.createMock(null, null);
 }

 @SuppressWarnings("unchecked")
 @Test(expected = NotAMockException.class)
 public void testCreateMockInvalidType() {
 Class<?> invalidType = String.class;
 mockUtil.createMock(invalidType, null);
 }

 @SuppressWarnings("unchecked")
 @Test(expected = NotAMockException.class)
 public void testCreateMockMultipleInterfaces() {
 Class<?> interface1 = Mockito.class;
 Class<?> interface2 = String.class;
 Class<?> mockType = Object.class;
 MockSettingsImpl settings = new MockSettingsImpl(session, creationValidator);
 settings.setExtraInterfaces(new Class<?>[]{interface1, interface2});
 mockUtil.createMock(mockType, settings);
 }

 @SuppressWarnings("unchecked")
 @Test(expected = NotAMockException.class)
 public void testCreateMockSpiedInstanceNull() {
 Class<?> mockType = Mockito.class;
 mockUtil.createMock(mockType, null);
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testResetMock() {
 MockSettingsImpl settings = new MockSettingsImpl(session, creationValidator);
 Object mock = mock(Mockito.class);
 mockUtil.resetMock(mock);
 verify(mock, times(0)).someMethod();
 }

 @Test
 public void testGetMockHandlerNull() {
 assertNull(mockUtil.getMockHandler(null));
 }

 @Test
 public void testIsMockNull() {
 assertFalse(mockUtil.isMock(null));
 }

 @Test
 public void testIsMockFalse() {
 Object notAMock = new Object();
 assertFalse(mockUtil.isMock(notAMock));
 }

 @Test
 public void testIsMockTrue() {
 Object mock = mock(Mockito.class);
 assertTrue(mockUtil.isMock(mock));
 }

 @Test
 public void testGetInterceptorNull() {
 assertNull(mockUtil.getInterceptor(null));
 }

 @Test
 public void testGetInterceptorMock() {
 Object mock = mock(Mockito.class);
 MockHandlerInterface mockHandler = mockUtil.getMockHandler(mock);
 MethodInterceptorFilter interceptor = mockUtil.getInterceptor(mock);
 assertEquals(mockHandler, interceptor.getHandler());
 }

 @Test
 public void testGetMockNameNull() {
 assertNull(mockUtil.getMockName(null));
 }

 @Test
 public void testGetMockNameMock() {
 Object mock = mock(Mockito.class);
 MockName mockName = mockUtil.getMockName(mock);
 assertEquals("mock", mockName.getName());
 }

 @Test
 public void testCreateMockWithSettings() {
 Class<?> mockType = Mockito.class;
 MockSettingsImpl settings = new MockSettingsImpl(session, creationValidator);
 settings.setName("testMock");
 Object mock = mockUtil.createMock(mockType, settings);
 assertEquals("testMock", getMockUtil().getMockName(mock).getName());
 }
}