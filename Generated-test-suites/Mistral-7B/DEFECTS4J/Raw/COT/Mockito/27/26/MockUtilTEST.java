import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.MockitoCore;
import org.mockito.internal.util.MockCreationValidator;
import org.mockito.internal.util.MockHandler;
import org.mockito.internal.util.MockHandlerInterface;
import org.mockito.internal.util.reflection.LenientCopyTool;
import org.mockito.internal.util.reflection.MethodInterceptorFilter;
import org.mockito.internal.util.reflection.MethodInterceptor;
import org.mockito.internal.util.reflection.MockSettingsImpl;
import org.mockito.internal.util.reflection.annotations.Mock;
import org.mockito.internal.util.reflection.annotations.MockType;
import org.mockito.internal.util.reflection.annotations.ParametersAnnotations;
import org.mockito.internal.util.reflection.annotations.SuperCall;
import org.mockito.internal.util.reflection.annotations.Verity;
import org.mockito.internal.util.reflection.annotations.Invocation;
import org.mockito.internal.util.reflection.annotations.Arguments;
import org.mockito.internal.util.reflection.annotations.Answer;
import org.mockito.internal.util.reflection.annotations.MockitoAnnotations;
import org.mockito.internal.util.reflection.annotations.MockitoAnnotations.MockAnnotation;
import org.mockito.internal.util.reflection.annotations.MockitoAnnotations.MockAnnotationType;

import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

public class MockUtilTest {

 private MockCreationValidator creationValidator;
 private MockUtil mockUtil;

 @Before
 public void setup() {
 creationValidator = new MockCreationValidator();
 mockUtil = new MockUtil(creationValidator);
 }

 @Test
 public void testCreateMockWithValidArguments() {
 Class<?> mockClass = Mockito.class;
 Object spiedInstance = new Object();
 MockSettingsImpl settings = new MockSettingsImpl();
 settings.setName("MockitoTest");
 MockitoAnnotations.initMocks(this); // Initialize mocks

 T mock = (T) mockUtil.createMock(mockClass, settings);
 Assert.assertNotNull(mock);
 Assert.assertFalse(isMockitoMock(spiedInstance));
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockWithNullClass() {
 mockUtil.createMock(null, new MockSettingsImpl());
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockWithInvalidClass() {
 mockUtil.createMock(new Object() {}, new MockSettingsImpl());
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockWithNullSettings() {
 mockUtil.createMock(Mockito.class, null);
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockWithExtraInterface() {
 Class<?> mockClass = Mockito.class;
 Class<?> extraInterface = String.class;
 MockSettingsImpl settings = new MockSettingsImpl();
 settings.setExtraInterfaces(new Class<?>[] {extraInterface});
 mockUtil.createMock(mockClass, settings);
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockWithSpiedInstanceOfDifferentClass() {
 Object spiedInstance = new Object();
 Class<?> mockClass = Mockito.class;
 MockSettingsImpl settings = new MockSettingsImpl();
 settings.setSpiedInstance(new Object());
 mockUtil.createMock(mockClass, settings);
 }

 @Test
 public void testResetMock() {
 T mock = Mockito.mock(Mockito.class);
 mockUtil.resetMock(mock);
 }

 @Test(expected = NotAMockException.class)
 public void testResetMockWithNullMock() {
 mockUtil.resetMock(null);
 }

 @Test(expected = NotAMockException.class)
 public void testResetMockWithNonMockObject() {
 Object nonMockObject = new Object();
 mockUtil.resetMock(nonMockObject);
 }

 @Test
 public void testGetMockHandlerWithMock() {
 T mock = Mockito.mock(Mockito.class);
 Object handler = mockUtil.getMockHandler(mock);
 Assert.assertNotNull(handler);
 Assert.assertInstanceOf(MockHandler.class, handler);
 }

 @Test(expected = NotAMockException.class)
 public void testGetMockHandlerWithNullMock() {
 mockUtil.getMockHandler(null);
 }

 @Test(expected = NotAMockException.class)
 public void testGetMockHandlerWithNonMockObject() {
 Object nonMockObject = new Object();
 mockUtil.getMockHandler(nonMockObject);
 }

 @Test
 public void testIsMockitoMockWithMock() {
 T mock = Mockito.mock(Mockito.class);
 Assert.assertTrue(mockUtil.isMockitoMock(mock));
 }

 @Test
 public void testIsMockitoMockWithNonMockObject() {
 Object nonMockObject = new Object();
 Assert.assertFalse(mockUtil.isMockitoMock(nonMockObject));
 }

 @Test
 public void testGetInterceptorWithMock() {
 T mock = Mockito.mock(Mockito.class);
 Object interceptor = mockUtil.getInterceptor(mock);
 Assert.assertNotNull(interceptor);
 Assert.assertInstanceOf(MethodInterceptor.class, interceptor);
 }

 @Test
 public void testGetInterceptorWithNonMockObject() {
 Object nonMockObject = new Object();
 Assert.assertNull(mockUtil.getInterceptor(nonMockObject));
 }

 @Test
 public void testGetMockNameWithMock() {
 T mock = Mockito.mock(Mockito.class);
 Object mockName = mockUtil.getMockName(mock);
 Assert.assertNotNull(mockName);
 }

 @Test
 public void testGetMockNameWithNullMock() {
 Assert.assertNull(mockUtil.getMockName(null));
 }
}