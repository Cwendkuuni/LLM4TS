import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.CreationFactory;
import org.mockito.internal.util.CreationValidator;
import org.mockito.internal.util.reflection.LenientCopyTool;
import net.sf.cglib.proxy.*;

import java.lang.reflect.Proxy;
import java.util.Arrays;

public class MockUtilTest {

 private CreationValidator creationValidator = new CreationValidator();
 private MockUtil mockUtil = new MockUtil(creationValidator);

 @Test
 public void testCreateMock() {
 Class<String> classToMock = String.class;
 MockSettingsImpl settings = new MockSettingsImpl();
 T mock = mockUtil.createMock(classToMock, settings);
 Assert.assertNotNull(mock);
 Assert.assertTrue(mockUtil.isMock(mock));
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockWithNullClass() {
 mockUtil.createMock(null, null);
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockWithInvalidClass() {
 Class<Integer> integerClass = Integer.class;
 mockUtil.createMock(integerClass, null);
 }

 @Test
 public void testCreateMockWithExtraInterfaces() {
 Class<String> classToMock = String.class;
 Class<?>[] extraInterfaces = {StringBuilder.class};
 MockSettingsImpl settings = new MockSettingsImpl().extraInterfaces(extraInterfaces);
 T mock = mockUtil.createMock(classToMock, settings);
 Assert.assertNotNull(mock);
 Assert.assertTrue(mockUtil.isMock(mock));
 Assert.assertArrayEquals(new Class<?>[]{String.class, StringBuilder.class}, mock.getClass().getInterfaces());
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockWithSpiedInstance() {
 String spiedInstance = "test";
 mockUtil.createMock(String.class, null, spiedInstance);
 }

 @Test
 public void testResetMock() {
 String mock = Mockito.mock(String.class);
 mockUtil.resetMock(mock);
 MockHandlerInterface<String> oldMockHandler = mockUtil.getMockHandler(mock);
 MockHandler<String> newMockHandler = new MockHandler<String>(oldMockHandler);
 MethodInterceptorFilter newFilter = new MethodInterceptorFilter(newMockHandler,
 withSettings().defaultAnswer(Mockito.RETURNS_DEFAULTS));
 Factory factory = (Factory) mock;
 factory.setCallback(0, newFilter);
 }

 @Test(expected = NotAMockException.class)
 public void testGetMockHandlerWithNullMock() {
 mockUtil.getMockHandler(null);
 }

 @Test(expected = NotAMockException.class)
 public void testGetMockHandlerWithNonMockitoMock() {
 Object nonMockitoMock = Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{String.class}, new InvocationHandler() {
 @Override
 public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
 return null;
 }
 });
 mockUtil.getMockHandler(nonMockitoMock);
 }

 @Test
 public void testIsMock() {
 String mock = Mockito.mock(String.class);
 Assert.assertTrue(mockUtil.isMock(mock));
 }

 @Test
 public void testIsMockWithNullMock() {
 Assert.assertFalse(mockUtil.isMock(null));
 }

 @Test
 public void testGetInterceptorWithMock() {
 String mock = Mockito.mock(String.class);
 MethodInterceptorFilter interceptor = mockUtil.getInterceptor(mock);
 Assert.assertNotNull(interceptor);
 }

 @Test(expected = NotAMockException.class)
 public void testGetInterceptorWithNonMockitoMock() {
 Object nonMockitoMock = Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{String.class}, new InvocationHandler() {
 @Override
 public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
 return null;
 }
 });
 mockUtil.getInterceptor(nonMockitoMock);
 }

 @Test
 public void testGetMockNameWithMock() {
 String mock = Mockito.mock(String.class);
 String mockName = mockUtil.getMockName(mock);
 Assert.assertNotNull(mockName);
 }

 @Test(expected = NotAMockException.class)
 public void testGetMockNameWithNonMockitoMock() {
 Object nonMockitoMock = Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{String.class}, new InvocationHandler() {
 @Override
 public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
 return null;
 }
 });
 mockUtil.getMockName(nonMockitoMock);
 }
}