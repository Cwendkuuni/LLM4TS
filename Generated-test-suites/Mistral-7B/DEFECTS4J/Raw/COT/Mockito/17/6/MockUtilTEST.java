import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.LenientCopyTool;
import org.mockito.invocation.MethodInterceptor;
import org.mockito.stub.Answer;
import org.mockito.stub.Callback;

import static org.junit.Assert.*;

public class MockUtilTest {

 private MockUtil mockUtil;
 private MockSettingsImpl settings;
 private Object mock;

 @Before
 public void setup() {
 mockUtil = new MockUtil();
 settings = new MockSettingsImpl();
 }

 @Test
 public void testCreateMock() {
 // Test creating a mock with default settings
 mock = mockUtil.createMock(String.class, settings);
 assertNotNull(mock);
 assertEquals(String.class, mock.getClass());

 // Test creating a mock with extra interfaces
 settings.setExtraInterfaces(new Class[] { Printable.class });
 mock = mockUtil.createMock(String.class, settings);
 assertInstanceOf(Printable.class, mock);

 // Test creating a mock with spied instance
 settings.setSpiedInstance("spied instance");
 mock = mockUtil.createMock(String.class, settings);
 assertEquals("spied instance", mockUtil.getMockName(mock).getName());
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockWithNullClass() {
 mockUtil.createMock(null, settings);
 fail("Should throw NotAMockException");
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockWithInvalidClass() {
 settings.setSpiedInstance(Mockito.mock(String.class));
 mockUtil.createMock((Class) null, settings);
 fail("Should throw NotAMockException");
 }

 @Test
 public void testResetMock() {
 // Test resetting a mock
 mockUtil.resetMock(mock);
 MockHandlerInterface<String> handler = mockUtil.getMockHandler(mock);
 assertNull(handler.getInvocations().getInvocation(0));

 // Test resetting a mock with spied instance
 settings.setSpiedInstance("spied instance");
 mock = mockUtil.createMock(String.class, settings);
 mockUtil.resetMock(mock);
 assertNull(mockUtil.getMockHandler(mock).getInvocations().getInvocation(0));
 }

 @Test(expected = NotAMockException.class)
 public void testResetMockWithNullMock() {
 mockUtil.resetMock(null);
 fail("Should throw NotAMockException");
 }

 @Test(expected = NotAMockException.class)
 public void testResetMockWithNonMock() {
 Object nonMock = new Object();
 mockUtil.resetMock(nonMock);
 fail("Should throw NotAMockException");
 }

 @Test
 public void testGetMockHandler() {
 // Test getting mock handler for a mock
 mock = mockUtil.createMock(String.class, settings);
 MockHandlerInterface<String> handler = mockUtil.getMockHandler(mock);
 assertNotNull(handler);

 // Test getting mock handler for a non-mock
 Object nonMock = new Object();
 assertNull(mockUtil.getMockHandler(nonMock));
 }

 @Test(expected = NotAMockException.class)
 public void testGetMockHandlerWithNullMock() {
 mockUtil.getMockHandler(null);
 fail("Should throw NotAMockException");
 }

 @Test
 public void testIsMockitoMock() {
 // Test isMockitoMock returns true for a mock
 mock = mockUtil.createMock(String.class, settings);
 assertTrue(mockUtil.isMockitoMock(mock));

 // Test isMockitoMock returns false for a non-mock
 Object nonMock = new Object();
 assertFalse(mockUtil.isMockitoMock(nonMock));
 }

 @Test
 public void testIsMock() {
 // Test isMock returns true for a mock
 mock = mockUtil.createMock(String.class, settings);
 assertTrue(mockUtil.isMock(mock));

 // Test isMock returns false for a non-mock
 Object nonMock = new Object();
 assertFalse(mockUtil.isMock(nonMock));
 }

 @Test
 public void testGetInterceptor() {
 // Test getting interceptor for a mock
 mock = mockUtil.createMock(String.class, settings);
 MethodInterceptor interceptor = mockUtil.getInterceptor(mock);
 assertNotNull(interceptor);

 // Test getting interceptor for a non-mock
 Object nonMock = new Object();
 assertNull(mockUtil.getInterceptor(nonMock));
 }

 @Test(expected = NotAMockException.class)
 public void testGetInterceptorWithNullMock() {
 mockUtil.getInterceptor(null);
 fail("Should throw NotAMockException");
 }

 @Test
 public void testGetMockName() {
 // Test getting mock name for a mock
 mock = mockUtil.createMock(String.class, settings);
 MockName mockName = mockUtil.getMockName(mock);
 assertNotNull(mockName);
 assertEquals("MockitoMock_<String>", mockName.getName());
 }

 @Test
 public void testGetMockNameWithSpiedInstance() {
 // Test getting mock name for a mock with spied instance
 settings.setSpiedInstance("spied instance");
 mock = mockUtil.createMock(String.class, settings);
 MockName mockName = mockUtil.getMockName(mock);
 assertEquals("MockitoMock_<String_spied instance>", mockName.getName());
 }
}