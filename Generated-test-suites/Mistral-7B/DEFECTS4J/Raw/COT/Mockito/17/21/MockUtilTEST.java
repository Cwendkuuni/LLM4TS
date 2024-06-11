import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.Mockito;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.CreationValidator;
import org.mockito.internal.creation.MethodInterceptor;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.util.reflection.LenientCopyTool;
import org.mockito.exceptions.misusing.NotAMockException;

import java.lang.reflect.Proxy;
import java.util.Arrays;

public class MockUtilTest {

 private CreationValidator creationValidator;
 private MockUtil mockUtil;

 @SuppressWarnings("unchecked")
 @Before
 public void setup() {
 creationValidator = Mockito.spy(CreationValidator.class);
 mockUtil = new MockUtil(creationValidator);
 }

 @Test
 public void testCreateMock_ValidScenario() {
 Class<MockitoMock> mockitoMockClass = MockitoMock.class;
 MockSettingsImpl settings = new MockSettingsImpl(mockitoMockClass, null, null, null, null);

 MockHandler<MockitoMock> mockHandler = mockUtil.createMock(mockitoMockClass, settings);

 assertTrue(mockHandler instanceof MockHandler);
 assertNotNull(mockHandler.getMockSettings());
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMock_NullClass() {
 Class<?> nullClass = null;
 MockSettingsImpl settings = new MockSettingsImpl(nullClass, null, null, null, null);

 mockUtil.createMock(nullClass, settings);
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMock_InvalidClass() {
 Class<?> invalidClass = Object.class;
 MockSettingsImpl settings = new MockSettingsImpl(invalidClass, null, null, null, null);

 mockUtil.createMock(invalidClass, settings);
 }

 @Test
 public void testCreateMock_ExtraInterfaces() {
 Class<MockitoMock> mockitoMockClass = MockitoMock.class;
 Class<AdditionalInterface> additionalInterfaceClass = AdditionalInterface.class;
 MockSettingsImpl settings = new MockSettingsImpl(mockitoMockClass, new Class[] { additionalInterfaceClass }, null, null, null);

 MockHandler<MockitoMock> mockHandler = mockUtil.createMock(mockitoMockClass, settings);

 assertTrue(mockHandler instanceof MockHandler);
 assertNotNull(mockHandler.getMockSettings());
 assertArrayEquals(new Class[] { additionalInterfaceClass }, mockHandler.getMockSettings().getExtraInterfaces());
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMock_NullExtraInterface() {
 Class<?> nullClass = null;
 Class<AdditionalInterface> additionalInterfaceClass = AdditionalInterface.class;
 MockSettingsImpl settings = new MockSettingsImpl(MockitoMock.class, new Class[] { null }, null, null, null);

 mockUtil.createMock(MockitoMock.class, settings);
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMock_InvalidExtraInterface() {
 Class<MockitoMock> mockitoMockClass = MockitoMock.class;
 Class<?> invalidClass = Object.class;
 MockSettingsImpl settings = new MockSettingsImpl(mockitoMockClass, new Class[] { invalidClass }, null, null, null);

 mockUtil.createMock(mockitoMockClass, settings);
 }

 @Test
 public void testCreateMock_SpiedInstance() {
 Class<MockitoMock> mockitoMockClass = MockitoMock.class;
 Object spiedInstance = new Object();
 MockSettingsImpl settings = new MockSettingsImpl(mockitoMockClass, null, null, null, spiedInstance);

 MockHandler<MockitoMock> mockHandler = mockUtil.createMock(mockitoMockClass, settings);

 assertTrue(mockHandler instanceof MockHandler);
 assertNotNull(mockHandler.getMockSettings());
 assertNotNull(mockHandler.getMockSettings().getSpiedInstance());
 assertSame(spiedInstance, mockHandler.getMockSettings().getSpiedInstance());
 }

 @Test
 public void testCreateMock_NoSpiedInstance() {
 Class<MockitoMock> mockitoMockClass = MockitoMock.class;
 MockSettingsImpl settings = new MockSettingsImpl(mockitoMockClass, null, null, null, null);

 MockHandler<MockitoMock> mockHandler = mockUtil.createMock(mockitoMockClass, settings);

 assertTrue(mockHandler instanceof MockHandler);
 assertNotNull(mockHandler.getMockSettings());
 assertNull(mockHandler.getMockSettings().getSpiedInstance());
 }

 @Test
 public void testResetMock() {
 MockitoMock mockitoMock = MockitoMock.mock(Object.class);
 MockHandler<MockitoMock> oldMockHandler = mockUtil.getMockHandler(mockitoMock);
 MockHandler<MockitoMock> newMockHandler = MockUtil.createMockHandler(mockitoMock.getClass(), new MockSettingsImpl(mockitoMock.getClass()));

 mockUtil.resetMock(mockitoMock);

 MethodInterceptorFilter newFilter = (MethodInterceptorFilter) mockitoMock.getCallback(0);
 assertEquals(newMockHandler, newFilter.getMockHandler());
 }

 @Test
 public void testGetMockHandler_NullMock() {
 Object nullMock = null;
 assertNull(mockUtil.getMockHandler(nullMock));
 }

 @Test
 public void testGetMockHandler_NotAMockitoMock() {
 Object notAMockitoMock = new Object();
 assertNull(mockUtil.getMockHandler(notAMockitoMock));
 }

 @Test
 public void testIsMock_NullMock() {
 Object nullMock = null;
 assertFalse(mockUtil.isMock(nullMock));
 }

 @Test
 public void testIsMock_NotAMockitoMock() {
 Object notAMockitoMock = new Object();
 assertFalse(mockUtil.isMock(notAMockitoMock));
 }

 @Test
 public void testIsMock_MockitoMock() {
 MockitoMock mockitoMock = MockitoMock.mock(Object.class);
 assertTrue(mockUtil.isMock(mockitoMock));
 }

 @Test
 public void testGetInterceptor_NullMock() {
 Object nullMock = null;
 assertNull(mockUtil.getInterceptor(nullMock));
 }

 @Test
 public void testGetInterceptor_NotAMockitoMock() {
 Object notAMockitoMock = new Object();
 assertNull(mockUtil.getInterceptor(notAMockitoMock));
 }

 @Test
 public void testGetInterceptor_MockitoMock() {
 MockitoMock mockitoMock = MockitoMock.mock(Object.class);
 MethodInterceptor interceptor = (MethodInterceptor) Proxy.getInvocationHandler(mockitoMock);
 MethodInterceptorFilter filter = mockUtil.getInterceptor(mockitoMock);
 assertEquals(interceptor.getHandler(), filter.getMockHandler());
 }

 @Test
 public void testGetMockName_NullMock() {
 Object nullMock = null;
 assertNull(mockUtil.getMockName(nullMock));
 }

 @Test
 public void testGetMockName_MockitoMock() {
 MockitoMock mockitoMock = MockitoMock.mock(Object.class);
 MockName mockName = mockUtil.getMockName(mockitoMock);
 assertEquals("MockitoMock_<MockName>", mockName.getMockName());
 }

}