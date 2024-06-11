package org.mockito.internal.util;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.RETURNS_DEFAULTS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.withSettings;

import org.junit.Test;
import org.mockito.internal.MockHandler;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.util.reflection.LenientCopyTool;
import org.mockito.mockito3.MockitoCore;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.OngoingStubbing;

public class MockUtilTest {

 private CreationValidator creationValidator;
 private MockSettingsImpl settings;

 @Test
 public void testCreateMock() {
 MockUtil mockUtil = new MockUtil();
 settings = new MockSettingsImpl();
 settings.setExtraInterfaces(new Class[0]);
 settings.setSpiedInstance(null);

 MockHandler<Object> mockHandler = mock(MockHandler.class);
 MethodInterceptorFilter filter = mock(MethodInterceptorFilter.class);
 ClassImposterizer classImposterizer = mock(ClassImposterizer.class);
 LenientCopyTool lenientCopyTool = mock(LenientCopyTool.class);

 when(classImposterizer.imposterise(filter, anyClass(), anyArray()))
 .thenReturn(mock(Object.class));
 when(lenientCopyTool.copyToMock(any(), any()))
 .thenReturn(mock(Object.class));

 when(mockUtil.creationValidator.validateType(anyClass()))
 .thenReturn(null);
 when(mockUtil.creationValidator.validateExtraInterfaces(anyClass(), anyArray()))
 .thenReturn(null);
 when(mockUtil.creationValidator.validateMockedType(anyClass(), any()))
 .thenReturn(null);

 MockHandler<Object> result = mockUtil.createMock(Object.class, settings);

 assertNotNull(result);
 assertNotNull(getMockHandler(result));
 }

 @Test
 public void testCreateMockWithExtraInterfaces() {
 MockUtil mockUtil = new MockUtil();
 settings = new MockSettingsImpl();
 settings.setExtraInterfaces(new Class[] {String.class});
 settings.setSpiedInstance(null);

 MockHandler<Object> mockHandler = mock(MockHandler.class);
 MethodInterceptorFilter filter = mock(MethodInterceptorFilter.class);
 ClassImposterizer classImposterizer = mock(ClassImposterizer.class);
 LenientCopyTool lenientCopyTool = mock(LenientCopyTool.class);

 when(classImposterizer.imposterise(filter, anyClass(), anyArray()))
 .thenReturn(mock(Object.class));
 when(lenientCopyTool.copyToMock(any(), any()))
 .thenReturn(mock(Object.class));

 when(mockUtil.creationValidator.validateType(anyClass()))
 .thenReturn(null);
 when(mockUtil.creationValidator.validateExtraInterfaces(anyClass(), anyArray()))
 .thenReturn(null);
 when(mockUtil.creationValidator.validateMockedType(anyClass(), any()))
 .thenReturn(null);

 MockHandler<Object> result = mockUtil.createMock(Object.class, settings);

 assertNotNull(result);
 assertNotNull(getMockHandler(result));
 assertNotNull(getMockHandler(result).getMockSettings().getExtraInterfaces()[0]);
 }

 @Test
 public void testCreateMockWithSpiedInstance() {
 MockUtil mockUtil = new MockUtil();
 settings = new MockSettingsImpl();
 settings.setExtraInterfaces(new Class[0]);
 settings.setSpiedInstance("test");

 MockHandler<Object> mockHandler = mock(MockHandler.class);
 MethodInterceptorFilter filter = mock(MethodInterceptorFilter.class);
 ClassImposterizer classImposterizer = mock(ClassImposterizer.class);
 LenientCopyTool lenientCopyTool = mock(LenientCopyTool.class);

 when(classImposterizer.imposterise(filter, anyClass(), anyArray()))
 .thenReturn(mock(Object.class));
 when(lenientCopyTool.copyToMock(any(), any()))
 .thenReturn(mock(Object.class));

 when(mockUtil.creationValidator.validateType(anyClass()))
 .thenReturn(null);
 when(mockUtil.creationValidator.validateExtraInterfaces(anyClass(), anyArray()))
 .thenReturn(null);
 when(mockUtil.creationValidator.validateMockedType(anyClass(), any()))
 .thenReturn(null);

 MockHandler<Object> result = mockUtil.createMock(Object.class, settings);

 assertNotNull(result);
 assertNotNull(getMockHandler(result));
 assertNull(getMockHandler(result).getMockSettings().getSpiedInstance());
 assertNotNull(getMockHandler(result).getMockSettings().getMockName());
 assertNotNull(getMockHandler(result).getMockSettings().getMockName().getValue());
 assertEquals("test", getMockHandler(result).getMockSettings().getSpiedInstance());
 }

 @Test
 public void testResetMock() {
 MockUtil mockUtil = new MockUtil();
 Object mock = mock(Object.class);

 MockHandler<Object> oldMockHandler = mockUtil.getMockHandler(mock);
 MockHandler<Object> newMockHandler = mock(MockHandler.class);
 MethodInterceptorFilter newFilter = mock(MethodInterceptorFilter.class, new Answer<Object>() {
 @Override
 public Object answer(InvocationOnMock invocation)
 throws Throwable {
 return null;
 }
 });

 mockUtil.resetMock(mock);

 OngoingStubbing<Object> result = mockito3().doReturn(null).when(mock);
 assertNull(result.getMockHandler().getHandler());
 assertSame(newMockHandler, getMockHandler(mock));
 }

 @Test(expected = NotAMockException.class)
 public void testResetMockWithNullMock() {
 MockUtil mockUtil = new MockUtil();
 mockUtil.resetMock(null);
 }

 @Test(expected = NotAMockException.class)
 public void testGetMockHandlerWithNullMock() {
 MockUtil mockUtil = new MockUtil();
 mockUtil.getMockHandler(null);
 }

 @Test(expected = NotAMockException.class)
 public void testIsMockWithNullMock() {
 MockUtil mockUtil = new MockUtil();
 assertFalse(mockUtil.isMock(null));
 }

 @Test
 public void testIsMockWithMockitoMock() {
 Object mock = mock(Object.class);
 MockUtil mockUtil = new MockUtil();
 assertTrue(mockUtil.isMock(mock));
 }

 @Test
 public void testIsMockWithNonMockitoMock() {
 Object mock = mock(Object.class, new MockitoCore());
 MockUtil mockUtil = new MockUtil();
 assertFalse(mockUtil.isMock(mock));
 }

 @Test
 public void testGetInterceptorWithMockitoMock() {
 Object mock = mock(Object.class);
 MockUtil mockUtil = new MockUtil();
 assertNotNull(mockUtil.getInterceptor(mock));
 }

 @Test(expected = NotAMockException.class)
 public void testGetInterceptorWithNonMockitoMock() {
 Object mock = mock(Object.class, new MockitoCore());
 MockUtil mockUtil = new MockUtil();
 mockUtil.getInterceptor(mock);
 }

 @Test
 public void testGetMockNameWithMockitoMock() {
 Object mock = mock(Object.class);
 MockUtil mockUtil = new MockUtil();
 assertNotNull(mockUtil.getMockName(mock));
 }

 @Test(expected = NotAMockException.class)
 public void testGetMockNameWithNonMockitoMock() {
 Object mock = mock(Object.class, new MockitoCore());
 MockUtil mockUtil = new MockUtil();
 mockUtil.getMockName(mock);
 }
}