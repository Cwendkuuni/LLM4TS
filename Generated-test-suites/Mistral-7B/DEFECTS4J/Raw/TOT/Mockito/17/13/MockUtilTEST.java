import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.internal.util.MockUtil;
import org.mockito.invocation.MethodInterceptor;
import org.mockito.stub.Answer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class MockUtilTest {

 private MockUtil mockUtil;
 private MockHandler<Object> mockHandler;
 private Object mock;

 @Before
 public void setUp() {
 mockUtil = new MockUtil(new CreationValidator());
 }

 @Test
 public void testCreateMock() {
 Class<?> mockClass = Mockito.mock(String.class);
 MockSettingsImpl settings = new MockSettingsImpl();
 settings.setSpiedInstance("Hello World");
 mockHandler = new MockHandler<Object>(settings);
 MethodInterceptorFilter filter = new MethodInterceptorFilter(mockHandler, settings);
 mock = mockUtil.createMock(String.class, settings);

 assertEquals(String.class, mock.getClass());
 assertNotNull(mockHandler);
 assertNotNull(getInterceptor(mock));
 assertEquals("Hello World", getMockHandler(mock).getMockSettings().getSpiedInstance());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateMockWithNullClass() {
 mockUtil.createMock(null, new MockSettingsImpl());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateMockWithInvalidClass() {
 mockUtil.createMock(int.class, new MockSettingsImpl());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateMockWithNullSettings() {
 mockUtil.createMock(String.class, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateMockWithSpiedInstanceOfDifferentType() {
 mockUtil.createMock(String.class, new MockSettingsImpl().setSpiedInstance(new Object()));
 }

 @Test
 public void testResetMock() throws IllegalAccessException, InvocationTargetException {
 mock = mockUtil.createMock(Object.class, new MockSettingsImpl());
 MethodInterceptorFilter oldFilter = (MethodInterceptorFilter) getInterceptor(mock);
 MethodInterceptor oldInterceptor = oldFilter.getInterceptor();
 Object oldHandler = oldInterceptor.getHandler();
 assertNotNull(oldInterceptor);
 assertNotNull(oldHandler);

 mockUtil.resetMock(mock);
 MethodInterceptorFilter newFilter = (MethodInterceptorFilter) getInterceptor(mock);
 MethodInterceptor newInterceptor = newFilter.getInterceptor();
 Object newHandler = newInterceptor.getHandler();
 assertNotNull(newInterceptor);
 assertNotNull(newHandler);
 assertNotSame(oldHandler, newHandler);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testResetMockWithNullMock() {
 mockUtil.resetMock(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testResetMockWithNonMockObject() {
 mockUtil.resetMock(new Object());
 }

 @Test
 public void testGetMockHandler() {
 mock = mockUtil.createMock(String.class, new MockSettingsImpl());
 assertNotNull(getMockHandler(mock));
 }

 @Test(expected = NotAMockException.class)
 public void testGetMockHandlerWithNullMock() {
 mockUtil.getMockHandler(null);
 }

 @Test(expected = NotAMockException.class)
 public void testGetMockHandlerWithNonMockObject() {
 mockUtil.getMockHandler(new Object());
 }

 @Test
 public void testIsMockitoMock() {
 mock = mockUtil.createMock(Object.class, new MockSettingsImpl());
 assertTrue(mockUtil.isMockitoMock(mock));
 }

 @Test
 public void testIsMockitoMockWithNullObject() {
 assertFalse(mockUtil.isMockitoMock(null));
 }

 @Test
 public void testIsMockitoMockWithNonMockitoMock() {
 assertFalse(mockUtil.isMockitoMock(new Object()));
 }

 @Test
 public void testGetInterceptor() {
 mock = mockUtil.createMock(Object.class, new MockSettingsImpl());
 assertNotNull(getInterceptor(mock));
 }

 @Test(expected = NotAMockException.class)
 public void testGetInterceptorWithNullMock() {
 mockUtil.getInterceptor(null);
 }

 @Test(expected = NotAMockException.class)
 public void testGetInterceptorWithNonMockObject() {
 mockUtil.getInterceptor(new Object());
 }

 @Test
 public void testGetMockName() {
 mock = mockUtil.createMock(Object.class, new MockSettingsImpl());
 assertNotNull(mockUtil.getMockName(mock));
 }

 @Test(expected = NotAMockException.class)
 public void testGetMockNameWithNullMock() {
 mockUtil.getMockName(null);
 }

 @Test(expected = NotAMockException.class)
 public void testGetMockNameWithNonMockObject() {
 mockUtil.getMockName(new Object());
 }

 @Test
 public void testCreateMockWithExtraInterfaces() {
 Class<?> mockClass = Mockito.mock(String.class);
 MockSettingsImpl settings = new MockSettingsImpl();
 settings.setSpiedInstance("Hello World");
 Class<?>[] extraInterfaces = {Number.class};
 settings.setExtraInterfaces(extraInterfaces);
 mockHandler = new MockHandler<Object>(settings);
 filter = new MethodInterceptorFilter(mockHandler, settings);
 mock = mockUtil.createMock(mockClass, settings);

 assertEquals(String.class, mock.getClass());
 assertNotNull(mockHandler);
 assertNotNull(getInterceptor(mock));
 assertEquals("Hello World", getMockHandler(mock).getMockSettings().getSpiedInstance());
 assertArrayEquals(extraInterfaces, getMockHandler(mock).getMockSettings().getExtraInterfaces());
 }
}