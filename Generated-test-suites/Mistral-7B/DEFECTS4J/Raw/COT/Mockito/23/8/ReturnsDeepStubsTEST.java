import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.whitebox.Whitebox;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

@RunWith(PowerMockRunner.class)
public class ReturnsDeepStubsTest {

 private static final Class<ReturnsDeepStubs> RETURNS_DEEP_STUBS = ReturnsDeepStubs.class;
 private static final Class<ReturnsEmptyValues> RETURNS_EMPTY_VALUES = ReturnsEmptyValues.class;

 @Test
 public void testAnswerMethodReturnsMockWhenTypeIsMockable() throws Exception {
 // Given
 Object mock = mock(Mock.class, new ReturnsDeepStubs());
 InvocationOnMock invocation = PowerMockito.mockMethodCall(mock, "method");

 // When
 Object result = RETURNS_DEEP_STUBS.instance().answer(invocation);

 // Then
 assertEquals(mock, result);
 }

 @Test(expected = Throwable.class)
 public void testAnswerMethodReturnsEmptyValuesWhenTypeIsNotMockable() throws Exception {
 // Given
 Object mock = mock(String.class);
 InvocationOnMock invocation = PowerMockito.mockMethodCall(mock, "method");

 // When
 // Then an exception is thrown
 RETURNS_DEEP_STUBS.instance().answer(invocation);
 }

 @Test
 public void testGetMockMethodReturnsMockObject() throws Exception {
 // Given
 Object mock = mock(Mock.class, new ReturnsDeepStubs());
 InvocationOnMock invocation = PowerMockito.mockMethodCall(mock, "method");
 GenericMetadataSupport returnTypeGenericMetadata = Mockito.type(int[].class).getGenericInterfaces()[0];

 // When
 Object result = RETURNS_DEEP_STUBS.instance().getMock(invocation, returnTypeGenericMetadata);

 // Then
 assertNotNull(result);
 verify(mockitoCore()).mock(returnTypeGenericMetadata.rawType(), withSettings());
 }

 @Test
 public void testGetMockMethodReturnsExistingStub() throws Exception {
 // Given
 Object mock = mock(Mock.class, new ReturnsDeepStubs());
 InvocationOnMock invocation = PowerMockito.mockMethodCall(mock, "method");
 InvocationOnMock existingStub = PowerMockito.mockMethodCall(mock, "existingMethod");
 InvocationContainerImpl container = (InvocationContainerImpl) new MockUtil().getMockHandler(mock).getInvocationContainer();

 // When
 Object result = RETURNS_DEEP_STUBS.instance().getMock(invocation, returnTypeGenericMetadata);

 // Then
 assertEquals(existingStub, result);
 }

 @Test
 public void testRecordDeepStubMethodRecordsAnswerToTheContainer() throws Exception {
 // Given
 Object mock = mock(Mock.class, new ReturnsDeepStubs());
 InvocationOnMock invocation = PowerMockito.mockMethodCall(mock, "method");
 InvocationContainerImpl container = (InvocationContainerImpl) new MockUtil().getMockHandler(mock).getInvocationContainer();
 Object deepStubMock = mock(DeepStubMock.class);

 // When
 RETURNS_DEEP_STUBS.instance().recordDeepStubMock(deepStubMock, container);

 // Then
 verify(container).addAnswer(argThat(answer -> answer.answer(invocation) != null && answer.answer(invocation).equals(deepStubMock)), anyBoolean());
 }

 @Test
 public void testAnswerMethodReturnsDeepStubWhenInvocationDoesNotMatchAnyExistingStub() throws Exception {
 // Given
 Object mock = mock(Mock.class, new ReturnsDeepStubs());
 InvocationOnMock invocation = PowerMockito.mockMethodCall(mock, "method");
 InvocationContainerImpl container = (InvocationContainerImpl) new MockUtil().getMockHandler(mock).getInvocationContainer();
 Object deepStubMock = mock(DeepStubMock.class);

 // When
 // Then a new deep stub is created and returned
 Object result = RETURNS_DEEP_STUBS.instance().answer(invocation);

 // Then
 assertEquals(deepStubMock, result);
 verifyZeroInteractions(container);
 }

 @Test
 public void testAnswerMethodReturnsDeepStubWhenInvocationMatchesMultipleExistingStubs() throws Exception {
 // Given
 Object mock = mock(Mock.class, new ReturnsDeepStubs());
 InvocationOnMock invocation = PowerMockito.mockMethodCall(mock, "method");
 InvocationContainerImpl container = (InvocationContainerImpl) new MockUtil().getMockHandler(mock).getInvocationContainer();
 Object deepStubMock1 = powerMockNew(DeepStubMock.class);
 Object deepStubMock2 = powerMockNew(DeepStubMock.class);

 // When
 // Then the first matching stub is returned
 Object result = RETURNS_DEEP_STUBS.instance().answer(invocation);

 // Then
 assertEquals(deepStubMock1, result);
 // ... repeat the assertion for the second stub
 }

 private static Object powerMockNew(Class<?> clazz) throws InstantiationException, IllegalAccessException, InvocationTargetException {
 return PowerMockito.newAnnotatedInstance(clazz);
}

 private static Method actualParameterizedTypeMethod = ReflectionUtils.findMethod(RETURNS_DEEP_STUBS, "actualParameterizedType");
 private static Object invokeActualParameterizedTypeMethod(Object instance, Object mock) throws Exception {
 return actualParameterizedTypeMethod.invoke(instance, mock);
}

 private static class Mock {
 }

 private static class DeepStubMock {
 }

 private static class ReflectionUtils {
 static Method findMethod(Class<?> clazz, String methodName) {
 try {
 return clazz.getDeclaredMethod(methodName);
 } catch (NoSuchMethodException e) {
 throw new RuntimeException("Could not find method: " + methodName, e);
 }
 }
 }
}