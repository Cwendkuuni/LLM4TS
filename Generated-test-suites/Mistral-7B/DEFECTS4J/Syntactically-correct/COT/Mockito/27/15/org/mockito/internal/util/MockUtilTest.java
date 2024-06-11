package org.mockito.internal.util.test;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;
import org.mockito.MockSettings;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.util.reflection.LenientCopyTool;
import org.mockito.internal.util.reflection.ClassImposterizer;
import org.mockito.internal.util.reflection.MethodInterceptorFilter;
import org.mockito.internal.util.reflection.AnswerInvocationOnMock;
import org.mockito.internal.util.reflection.MethodInterceptor;
import org.mockito.internal.util.MockName;
import org.mockito.internal.util.MockCreationValidator;
import org.mockito.internal.util.MockHandler;
import org.mockito.internal.util.MockHandlerInterface;
import org.mockito.internal.util.InvocationNotifierHandler;
import org.mockito.internal.util.ClassUtils;
import org.mockito.internal.util.ArrayUtils;
import org.mockito.internal.util.lenient.Lenient;
import org.powermock.api.mockito.internal.MockitoAnswer;
import org.powermock.api.mockito.internal.mockcreation.MockCreationSettings;
import org.powermock.api.mockito.internal.mockcreation.MockCreator;
import org.powermock.reflect.Whitebox;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;

@RunWith(PowerMockRunner.class)
public class MockUtilTest {

 private MockCreationValidator creationValidator;
 private ClassImposterizer imposterizer;
 private Lenient lenient;
 private MockHandlerInterface mockHandler;
 private Class<?> mockClass;
 private Object spiedInstance;
 private MockSettingsImpl settings;
 private MockName mockName;

 @SuppressWarnings("unchecked")
 @Before
 public void setup() {
 creationValidator = Mockito.spy(new MockCreationValidator());
 imposterizer = ClassImposterizer.INSTANCE;
 lenient = Lenient.on(this);
 }

 @Test
 public void testCreateMock_validArguments() {
 // Test creating a mock with valid arguments
 Class<MockUtil> mockUtilClass = Mockito.mock(MockUtil.class);
 MockSettings settings = new MockSettings();
 settings.initiateMockName(MockUtil.class);
 mockClass = MockUtil.class;
 spiedInstance = new Object();
 MockHandler mockHandler = new MockHandler(settings);
 mockHandlerInterface = mockHandler;
 MockName mockName = Mockito.mock(MockName.class);
 lenientCopyTool = lenient.mock(LenientCopyTool.class);
 ClassImposterizer classImposterizer = Mockito.mock(ClassImposterizer.class);
 Factory factory = Mockito.mock(Factory.class);
 Whitebox.setInternalState(factory, "handler", mockHandler);
 Whitebox.setInternalState(factory, "interceptor", Mockito.mock(MethodInterceptor.class));
 Whitebox.setInternalState(factory, "classImposterizer", classImposterizer);
 Whitebox.setInternalState(factory, "settings", settings);

 when(classImposterizer.imposterise(any(MethodInterceptorFilter.class), eq(mockClass), anyArray()))
 .thenReturn(mockClass);
 when(lenientCopyTool.copyToMock(anyObject(), anyObject()))
 .thenAnswer(new Answer<Void>() {
 @Override
 public Void answer(InvocationOnMock invocation) throws Throwable {
 Object source = invocation.getArgument(0);
 Object target = invocation.getMock();
 System.arraycopy(source.getClass().getDeclaredFields(), 0, target.getClass().getDeclaredFields(), 0, source.getClass().getDeclaredFields().length);
 return null;
 }
 });

 Mockito.doAnswer(new Answer<T>() {
 @Override
 public T answer(InvocationOnMock invocation) throws Throwable {
 T result = MockUtil.createMock(mockClass, settings);
 Whitebox.setInternalState(result, "handler", mockHandler);
 Whitebox.setInternalState(result, "classImposterizer", classImposterizer);
 Whitebox.setInternalState(result, "settings", settings);
 Whitebox.setInternalState(result, "lenientCopyTool", lenientCopyTool);
 return result;
 }
 }).when(mockUtilClass).createMock(eq(mockClass), eq(settings));

 T mock = MockUtil.createMock(mockClass, settings);

 Assert.assertNotNull(mock);
 Assert.assertSame(mockClass, mock.getClass());
 Assert.assertNotNull(mockHandler);
 Assert.assertSame(mockHandler, Whitebox.getInternalState(mock, "handler"));
 Assert.assertSame(settings, Whitebox.getInternalState(mock, "settings"));
 Assert.assertSame(mockName, Whitebox.getInternalState(mock, "mockName"));
 }

 @Test
 public void testCreateMock_nullClassArgument() {
 // Test creating a mock with null class argument
 try {
 MockUtil.createMock(null, settings);
 FailureHandler.fail("Expected NotAMockException to be thrown!");
 } catch (NotAMockException e) {
 // Expected
 }
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMock_notAValidClassArgument() {
 // Test creating a mock with not a valid class argument
 Class<String> stringClass = String.class;
 try {
 MockUtil.createMock(stringClass, settings);
 } catch (NotAMockException e) {
 // Expected
 }
 }

 @Test
 public void testCreateMock_validExtraInterfaces() {
 // Test creating a mock with valid extra interfaces
 Class<?>[] extraInterfaces = new Class[] {Serializable.class};
 settings.setExtraInterfaces(extraInterfaces);

 when(classImposterizer.imposterise(any(MethodInterceptorFilter.class), eq(mockClass), anyArray()))
 .thenReturn(mockClass);

 Mockito.doAnswer(new Answer<T>() {
 @Override
 public T answer(InvocationOnMock invocation) throws Throwable {
 T result = MockUtil.createMock(mockClass, settings);
 Whitebox.setInternalState(result, "handler", mockHandler);
 Whitebox.setInternalState(result, "classImposterizer", classImposterizer);
 Whitebox.setInternalState(result, "settings", settings);
 Whitebox.setInternalState(result, "lenientCopyTool", lenientCopyTool);
 return result;
 }
 }).when(mockUtilClass).createMock(eq(mockClass), eq(settings));

 T mock = MockUtil.createMock(mockClass, settings);

 Assert.assertSame(Arrays.asList(mockClass), Arrays.asList(Whitebox.getInternalState(mock, "interfaces")));
 }

 @Test
 public void testCreateMock_emptyExtraInterfaces() {
 // Test creating a mock with empty extra interfaces
 settings.setExtraInterfaces(Collections.emptyArray());

 when(classImposterizer.imposterise(any(MethodInterceptorFilter.class), eq(mockClass), anyArray()))
 .thenReturn(mockClass);

 Mockito.doAnswer(new Answer<T>() {
 @Override
 public T answer(InvocationOnMock invocation) throws Throwable {
 T result = MockUtil.createMock(mockClass, settings);
 Whitebox.setInternalState(result, "handler", mockHandler);
 Whitebox.setInternalState(result, "classImposterizer", classImposterizer);
 Whitebox.setInternalState(result, "settings", settings);
 Whitebox.setInternalState(result, "lenientCopyTool", lenientCopyTool);
 return result;
 }
 }).when(mockUtilClass).createMock(eq(mockClass), eq(settings));

 T mock = MockUtil.createMock(mockClass, settings);

 Assert.assertArrayEquals(new Class[0], Whitebox.getInternalState(mock, "interfaces"));
 }

 @Test
 public void testCreateMock_nullSpiedInstance() {
 // Test creating a mock with null spied instance
 try {
 MockUtil.createMock(mockClass, settings);
 FailureHandler.fail("Expected NotAMockException to be thrown!");
 } catch (NotAMockException e) {
 // Expected
 }
 }

 @Test
 public void testResetMock() {
 // Test resetting a mock
 T mock = MockUtil.createMock(mockClass, settings);

 Mockito.doAnswer(new Answer<Void>() {
 @Override
 public Void answer(InvocationOnMock invocation) throws Throwable {
 return MockUtil.resetMock(mock);
 }
 }).when(mockUtilClass).resetMock(eq(mock));

 MockUtil.resetMock(mock);

 verify(mock, times(1)).reset();
 }

 @Test(expected = NotAMockException.class)
 public void testResetMock_notAValidMock() {
 // Test resetting a non-mock object
 MockUtil.resetMock(new Object());
 }

 @Test
 public void testGetMockHandler() {
 // Test getting the mock handler
 T mock = MockUtil.createMock(mockClass, settings);

 Mockito.doAnswer(new Answer<MockHandlerInterface<T>>() {
 @Override
 public MockHandlerInterface<T> answer(InvocationOnMock invocation) throws Throwable {
 return mockHandler;
 }
 }).when(mockUtilClass).getMockHandler(eq(mock));

 MockHandlerInterface<T> mockHandler = MockUtil.getMockHandler(mock);

 Assert.assertSame(mockHandler, Whitebox.getInternalState(mock, "handler"));
 }

 @Test(expected = NotAMockException.class)
 public void testGetMockHandler_notAValidMock() {
 // Test getting the mock handler for a non-mock object
 MockUtil.getMockHandler(new Object());
 }

 @Test
 public void testIsMock() {
 // Test checking if an object is a mock or not
 Object mock = MockUtil.createMock(Mockito.mock(Object.class), settings);
 Object nonMock = new Object();

 Assert.assertTrue(MockUtil.isMock(mock));
 Assert.assertFalse(MockUtil.isMock(nonMock));
 }

 @Test(expected = NotAMockException.class)
 public void testIsMock_notAValidMock() {
 // Test checking if a non-mock object is a mock
 MockUtil.isMock(new Object());
 }

 @Test
 public void testGetInterceptor() {
 // Test getting the interceptor of a mock
 T mock = MockUtil.createMock(mockClass, settings);

 Mockito.doAnswer(new Answer<MethodInterceptorFilter>() {
 @Override
 public MethodInterceptorFilter answer(InvocationOnMock invocation) throws Throwable {
 return new MethodInterceptorFilter(mockHandler, settings);
 }
 }).when(mockUtilClass).getInterceptor(eq(mock));

 MethodInterceptorFilter interceptor = MockUtil.getInterceptor(mock);

 Assert.assertSame(Whitebox.getInternalState(mock, "handler"), interceptor.getHandler());
 Assert.assertSame(settings, interceptor.getSettings());
 }

 @Test(expected = NotAMockException.class)
 public void testGetInterceptor_notAValidMock() {
 // Test getting the interceptor for a non-mock object
 MockUtil.getInterceptor(new Object());
 }

 @Test
 public void testGetMockName() {
 // Test getting the mock name
 T mock = MockUtil.createMock(mockClass, settings);
 MockName mockName = Mockito.mock(MockName.class);

 Mockito.doAnswer(new Answer<MockName>() {
 @Override
 public MockName answer(InvocationOnMock invocation) throws Throwable {
 return mockName;
 }
 }).when(mockUtilClass).getMockName(eq(mock));

 MockName result = MockUtil.getMockName(mock);

 Assert.assertSame(mockName, result);
 }

 @Test(expected = NotAMockException.class)
 public void testGetMockName_notAValidMock() {
 // Test getting the mock name for a non-mock object
 MockUtil.getMockName(new Object());
 }
}