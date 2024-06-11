package org.mockito.internal.util;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.cglib.proxy.MethodInterceptor;
import org.mockito.internal.MockHandler;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.MockSettingsImpl;
import org.mockito.internal.creation.ClassImposterizer;
import org.mockito.internal.util.reflection.LenientCopyTool;

public class MockUtilTest {

 private MockUtil mockUtil;
 private CreationValidator creationValidatorMock;

 @Before
 public void setup() {
 creationValidatorMock = mock(CreationValidator.class);
 mockUtil = new MockUtil(creationValidatorMock);
 }

 @Test
 public void testCreateMock() {
 Class<String> stringClass = String.class;
 MockSettingsImpl settings = new MockSettingsImpl();
 String mock = mockUtil.createMock(stringClass, settings);
 assertNotNull(mock);
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockWithNullClass() {
 mockUtil.createMock(null, new MockSettingsImpl());
 }

 @Test
 public void testCreateMockWithNullSettings() {
 Class<String> stringClass = String.class;
 MockSettingsImpl settings = null;
 String mock = mockUtil.createMock(stringClass, settings);
 assertNull(mock);
 }

 @Test
 public void testCreateMockWithNullMock() {
 Class<String> stringClass = String.class;
 String mock = mockUtil.createMock(stringClass, null);
 assertNull(mock);
 }

 @Test
 public void testCreateMockWithExtraInterfaces() {
 Class<String> stringClass = String.class;
 Class<MyInterface> myInterfaceClass = MyInterface.class;
 MockSettingsImpl settings = new MockSettingsImpl();
 settings.setExtraInterfaces(new Class<?>[] { myInterfaceClass });
 String mock = mockUtil.createMock(stringClass, settings);
 assertNotNull(mock);
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockWithInvalidType() {
 Class<Integer> integerClass = Integer.class;
 MockSettingsImpl settings = new MockSettingsImpl();
 mockUtil.createMock(String.class, settings);
 }

 @Test
 public void testResetMock() {
 MockHandlerInterface<String> handler = mock(MockHandlerInterface.class);
 String mock = mockUtil.createMock(String.class, new MockSettingsImpl());
 mockUtil.resetMock(mock);
 assertNotNull(handler);
 MethodInterceptorFilter newFilter = getNewFilter(handler);
 verifyNoMoreInteractions(newFilter);
 }

 @Test
 public void testGetMockHandler() {
 String mock = mockUtil.createMock(String.class, new MockSettingsImpl());
 MockHandlerInterface<String> handler = mockUtil.getMockHandler(mock);
 assertNotNull(handler);
 }

 @Test(expected = NotAMockException.class)
 public void testGetMockHandlerWithNullMock() {
 mockUtil.getMockHandler(null);
 }

 @Test(expected = NotAMockException.class)
 public void testGetMockHandlerWithInvalidMock() {
 Object invalidMock = new Object();
 mockUtil.getMockHandler(invalidMock);
 }

 @Test
 public void testIsMock() {
 String mock = mockUtil.createMock(String.class, new MockSettingsImpl());
 assertNotNull(mockUtil.isMock(mock));
 }

 @Test
 public void testIsMockWithNullMock() {
 assertFalse(mockUtil.isMock(null));
 }

 @Test
 public void testIsMockWithNonMockitoMock() {
 Object nonMockitoMock = mock(Object.class);
 assertFalse(mockUtil.isMock(nonMockitoMock));
 }

 @Test
 public void testGetInterceptor() {
 String mock = mockUtil.createMock(String.class, new MockSettingsImpl());
 MethodInterceptorFilter interceptor = mockUtil.getInterceptor(mock);
 assertNotNull(interceptor);
 }

 @Test
 public void testGetInterceptorWithNullMock() {
 MethodInterceptorFilter interceptor = mockUtil.getInterceptor(null);
 assertNull(interceptor);
 }

 @Test
 public void testGetMockName() {
 String mock = mockUtil.createMock(String.class, new MockSettingsImpl());
 MockName mockName = mockUtil.getMockName(mock);
 assertNotNull(mockName);
 }

 @Test
 public void testGetMockNameWithNullMock() {
 MockName mockName = mockUtil.getMockName(null);
 assertNull(mockName);
 }

 private MethodInterceptorFilter getNewFilter(MockHandlerInterface handler) {
 MethodInterceptorFilter filter = Mockito.spy(new MethodInterceptorFilter(handler, new MockSettingsImpl()));
 return filter;
}

}