package org.mockito.internal.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.internal.MockHandler;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.MockSettingsImpl;
import org.mockito.internal.creation.MethodInterceptorFilter;
import org.mockito.internal.creation.MockSettingsImpl.MockName;
import org.mockito.internal.util.reflection.LenientCopyTool;

public class MockUtilTest {

 MockUtil util = new MockUtil();

 @Test
 public void testCreateMock() {
 MockSettingsImpl settings = new MockSettingsImpl(MockName.MOCKITO_1, null);
 Class<String> stringClass = String.class;
 String mock = util.createMock(stringClass, settings);
 assertNotNull(mock);
 assertSame(stringClass, mock.getClass());

 MockHandler<String> handler = util.getMockHandler(mock);
 verify(handler).getMockName();
 }

 @Test
 public void testCreateMockWithExtraInterfaces() {
 Class<?> extraInterface1 = MockitoStub.class;
 Class<?> extraInterface2 = MockitoAnswer.class;
 MockSettingsImpl settings = new MockSettingsImpl(MockName.MOCKITO_1, extraInterface1, extraInterface2);
 Class<String> stringClass = String.class;
 String mock = util.createMock(stringClass, settings);
 assertNotNull(mock);
 assertSame(stringClass, mock.getClass());

 MockHandler<String> handler = util.getMockHandler(mock);
 verify(handler).getMockName();
 verify(handler).getExtraInterfaces()[0].equals(extraInterface1);
 verify(handler).getExtraInterfaces()[1].equals(extraInterface2);
 }

 @Test
 public void testCreateMockWithSpiedInstance() {
 String spiedInstance = "hello";
 MockSettingsImpl settings = new MockSettingsImpl(MockName.MOCKITO_1, null, spiedInstance);
 Class<String> stringClass = String.class;
 String mock = util.createMock(stringClass, settings);
 assertNotNull(mock);
 assertSame(stringClass, mock.getClass());

 assertEquals(spiedInstance, util.getMockHandler(mock).getMockSettings().getSpiedInstance());
 }

 @Test
 public void testResetMock() {
 String mock = mock(String.class);
 util.resetMock(mock);
 assertNull(util.getInterceptor(mock));
 }

 @Test
 public void testGetMockHandler() {
 String mock = spy(String.class);
 assertNotNull(util.getMockHandler(mock));
 }

 @Test(expected = NotAMockException.class)
 public void testGetMockHandlerWithNullMock() {
 util.getMockHandler(null);
 }

 @Test(expected = NotAMockException.class)
 public void testGetMockHandlerWithNonMockitoMock() {
 Object nonMockitoMock = new Object();
 util.getMockHandler(nonMockitoMock);
 }

 @Test
 public void testIsMockitoMock() {
 Factory mockitoMockFactory = Mockito.mock(Factory.class);
 assertTrue(util.isMockitoMock(mockitoMockFactory));
 }

 @Test
 public void testIsMockitoMockWithNullMock() {
 assertFalse(util.isMock(null));
 }

 @Test
 public void testIsMockitoMockWithNonMockitoMock() {
 Object nonMockitoMock = new Object();
 assertFalse(util.isMock(nonMockitoMock));
 }

 @Test
 public void testGetInterceptor() {
 String mock = mock(String.class);
 MethodInterceptorFilter interceptor = util.getInterceptor(mock);
 assertNotNull(interceptor);
 }

 @Test
 public void testGetInterceptorWithNullMock() {
 assertNull(util.getInterceptor(null));
 }

 @Test
 public void testGetMockName() {
 String mock = mock(String.class);
 MockName name = util.getMockName(mock);
 assertNotNull(name);
 }

 @Test
 public void testGetMockNameWithNullMock() {
 assertNull(util.getMockName(null));
 }
}