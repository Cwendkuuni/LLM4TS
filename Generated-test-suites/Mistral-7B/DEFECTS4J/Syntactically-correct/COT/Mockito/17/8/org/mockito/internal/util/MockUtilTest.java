import org.junit.Assert.*;
import org.junit.Test;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.MockName;
import org.mockito.internal.util.CreationValidator;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.creation.MethodInterceptorFilter;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.util.reflection.LenientCopyTool;
import org.mockito.exceptions.misusing.NotAMockException;
import net.mockito.invocation.MethodInterceptor;

import java.lang.reflect.Proxy;
import java.util.Arrays;

public class MockUtilTest {

 private MockUtil mockUtil;

 @SuppressWarnings("unchecked")
 @Before
 public void setup() {
 mockUtil = new MockUtil(new CreationValidator());
 }

 @Test
 public void testCreateMock_ValidArguments() {
 Class<?> classToMock = MockitoMockClass.class;
 MockSettingsImpl settings = new MockSettingsImpl();
 T mock = (T) mockUtil.createMock(classToMock, settings);
 assertTrue("Mock should not be null", mock != null);
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMock_NullClassToMock() {
 Class<?> classToMock = null;
 MockSettingsImpl settings = new MockSettingsImpl();
 mockUtil.createMock(classToMock, settings);
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMock_InvalidClassToMock() {
 Class<?> classToMock = int.class;
 MockSettingsImpl settings = new MockSettingsImpl();
 mockUtil.createMock(classToMock, settings);
 }

 @Test
 public void testCreateMock_ExtraInterfaces() {
 Class<?> classToMock = MockitoMockClass.class;
 MockSettingsImpl settings = new MockSettingsImpl();
 settings.setExtraInterfaces(new Class[]{SomeExtraInterface.class});
 T mock = (T) mockUtil.createMock(classToMock, settings);
 assertNotNull("Mock should not be null", mock);
 verify(mock).someMethod(); // assuming SomeExtraInterface has a method named 'someMethod'
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMock_NullExtraInterfaces() {
 Class<?> classToMock = MockitoMockClass.class;
 MockSettingsImpl settings = new MockSettingsImpl();
 settings.setExtraInterfaces(null);
 mockUtil.createMock(classToMock, settings);
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMock_InvalidExtraInterface() {
 Class<?> classToMock = MockitoMockClass.class;
 MockSettingsImpl settings = new MockSettingsImpl();
 settings.setExtraInterfaces(new Class[]{String.class});
 mockUtil.createMock(classToMock, settings);
 }

 @Test
 public void testCreateMock_SpiedInstance() {
 Class<?> classToMock = MockitoMockClass.class;
 Object spiedInstance = new MockitoMockClass();
 MockSettingsImpl settings = new MockSettingsImpl();
 settings.setSpiedInstance(spiedInstance);
 T mock = (T) mockUtil.createMock(classToMock, settings);
 assertNotNull("Mock should not be null", mock);
 assertSame("Spied instance and mock should be the same", spiedInstance, mock);
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMock_InvalidSpiedInstance() {
 Class<?> classToMock = MockitoMockClass.class;
 Object spiedInstance = new Object();
 MockSettingsImpl settings = new MockSettingsImpl();
 settings.setSpiedInstance(spiedInstance);
 mockUtil.createMock(classToMock, settings);
 }

 @Test
 public void testResetMock() {
 T mock = createMock(MockitoMockClass.class, new MockSettingsImpl());
 resetMock(mock);
 assertFalse("Mock should not be mocked anymore", mockUtil.isMock(mock));
 }

 @Test
 public void testGetMockHandler() {
 T mock = createMock(MockitoMockClass.class, new MockSettingsImpl());
 MockHandlerInterface<T> mockHandler = mockUtil.getMockHandler(mock);
 assertNotNull("MockHandler should not be null", mockHandler);
 }

 @Test(expected = NotAMockException.class)
 public void testGetMockHandler_NullMock() {
 T mock = null;
 mockUtil.getMockHandler(mock);
 }

 @Test(expected = NotAMockException.class)
 public void testGetMockHandler_NotAMockitoMock() {
 Object notAMockitoMock = new Object();
 mockUtil.getMockHandler(notAMockitoMock);
 }

 @Test
 public void testIsMock() {
 T mock = createMock(MockitoMockClass.class, new MockSettingsImpl());
 assertTrue("Mock should be a mock", mockUtil.isMock(mock));
 }

 @Test
 public void testIsMock_NullMock() {
 Object mock = null;
 assertFalse("Mock should not be a mock", mockUtil.isMock(mock));
 }

 @Test
 public void testIsMock_NotAMockitoMock() {
 Object notAMockitoMock = new Object();
 assertFalse("Not a mockito mock", mockUtil.isMock(notAMockitoMock));
 }

 @Test
 public void testGetInterceptor() {
 T mock = createMock(MockitoMockClass.class, new MockSettingsImpl());
 assertNotNull("Interceptor should not be null", mockUtil.getInterceptor(mock));
 }

 @Test(expected = NotAMockException.class)
 public void testGetInterceptor_NullMock() {
 T mock = null;
 mockUtil.getInterceptor(mock);
 }

 @Test(expected = NotAMockException.class)
 public void testGetInterceptor_NotAMockitoMock() {
 Object notAMockitoMock = new Object();
 mockUtil.getInterceptor(notAMockitoMock);
 }

 @Test
 public void testGetMockName() {
 T mock = createMock(MockitoMockClass.class, new MockSettingsImpl());
 MockName mockName = mockUtil.getMockName(mock);
 assertNotNull("Mock name should not be null", mockName);
 }

 @Test(expected = NotAMockException.class)
 public void testGetMockName_NullMock() {
 T mock = null;
 mockUtil.getMockName(mock);
 }

 @Test(expected = NotAMockException.class)
 public void testGetMockName_NotAMockitoMock() {
 Object notAMockitoMock = new Object();
 mockUtil.getMockName(notAMockitoMock);
 }

 @Test
 public void testCreateMock_ProxyClassLoading() throws Exception {
 Class<?> classToMock = ProxyClass.class;
 MockSettingsImpl settings = new MockSettingsImpl();
 Object proxy = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[]{classToMock}, (Proxy.InvocationHandler) (obj, method, args) -> method.invoke(mockUtil.createMock(classToMock, settings), args));
 T mock = (T) mockUtil.createMock(classToMock, settings);
 assertSame("Proxy and mock should be the same", proxy, mock);
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMock_ClassLoaderException() throws Exception {
 Class<?> classToMock = ProxyClass.class;
 MockSettingsImpl settings = new MockSettingsImpl();
 Thread.currentThread().getContextClassLoader().getClassLoader().getResource("non-existing.class"); // throw an IOException
 T mock = (T) mockUtil.createMock(classToMock, settings);
 }

 @Test
 public void testCreateMock_CglibClassLoading() throws Exception {
 Class<?> classToMock = CglibMockClass.class;
 MockSettingsImpl settings = new MockSettingsImpl();
 Class<?> mockClass = Class.forName("org.mockito.internal.creation.jmock.ClassImposterizer$MockitoMockito$$EnhancerByMockito$$188392917"); // the generated CGLIB class
 Object spiedInstance = new CglibMockClass();
 Object mock = ClassImposterizer.INSTANCE.createMock(classToMock, mockClass, spiedInstance);
 T mockitoMock = (T) mockUtil.createMock(classToMock, settings);
 assertSame("CGLIB mock and mockito mock should be the same", mock, mockitoMock);
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMock_CglibClassLoadingException() throws Exception {
 Class<?> classToMock = CglibMockClass.class;
 MockSettingsImpl settings = new MockSettingsImpl();
 Class<?> mockClass = Class.forName("org.mockito.exceptions.misusing.NotAMockException"); // throw a ClassNotFoundException
 T mockitoMock = (T) mockUtil.createMock(classToMock, settings);
 }

 @Test
 public void testCreateMock_DeepHierarchy() {
 Class<?> classToMock = DeepHierarchyMockClass.class;
 MockSettingsImpl settings = new MockSettingsImpl();
 T mock = (T) mockUtil.createMock(classToMock, settings);
 assertNotNull("Mock should not be null", mock);
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMock_CircularDependency() {
 Class<?> classToMock = CircularDependencyMockClass.class;
 MockSettingsImpl settings = new MockSettingsImpl();
 mockUtil.createMock(classToMock, settings);
 }

 @Test
 public void testCreateMock_PrivateConstructor() {
 Class<?> classToMock = PrivateConstructorMockClass.class;
 MockSettingsImpl settings = new MockSettingsImpl();
 T mock = (T) mockUtil.createMock(classToMock, settings);
 assertNotNull("Mock should not be null", mock);
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMock_AbstractClass() {
 Class<?> classToMock = AbstractClassMock.class;
 MockSettingsImpl settings = new MockSettingsImpl();
 mockUtil.createMock(classToMock, settings);
 }

 @Test
 public void testCreateMock_FinalClass() {
 Class<?> classToMock = FinalClassMock.class;
 MockSettingsImpl settings = new MockSettingsImpl();
 try {
 mockUtil.createMock(classToMock, settings);
 fail("Should have thrown NotAMockException since the class is final");
 } catch (NotAMockException e) {
 // expected
 }
 }
}

 class MockitoMockClass {
}

 class ProxyClass {
}

 interface SomeExtraInterface {
 void someMethod();
}

 class CglibMockClass {
}

 interface DeepHierarchyMockInterface {
 DeepHierarchyMockInterface deepHierarchyMockInterface();
}

 interface CircularDependencyMockInterface {
 CircularDependencyMockInterface circularDependencyMockInterface();
 CircularDependencyMockInterface circularDependencyMockInterface2();
}

 interface PrivateConstructorMockInterface {
 private PrivateConstructorMockInterface privateConstructorMockInterface();
}

 abstract class AbstractClassMock {
}

 final class FinalClassMock {
}