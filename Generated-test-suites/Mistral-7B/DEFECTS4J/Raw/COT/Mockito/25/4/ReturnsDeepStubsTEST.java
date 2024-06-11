import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.internal.stubbing.defaultanswers.ReturnsDeepStubs;
import org.mockito.internal.stubbing.defaultanswers.ReturnsEmptyValues;
import org.mockito.mockito3.Mockito;
import org.mockito.stubbing.Answer;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class ReturnsDeepStubsTest {

 private ReturnsDeepStubs returnsDeepStubs;
 private ReturnsEmptyValues returnsEmptyValues;
 private Object mockGenericsNest;
 private Object mockNumber;

 @Before
 public void setup() {
 returnsDeepStubs = new ReturnsDeepStubs();
 returnsEmptyValues = new ReturnsEmptyValues();

 mockGenericsNest = Mockito.mock(Map.class, new ReturnsDeepStubs());
 mockNumber = Mockito.mock(Number.class, new ReturnsDeepStubs());
 }

 @Test
 public void testAnswer_NonMockableType() throws Exception {
 // Test for the case when the return type is not mockable
 InvocationOnMock invocation = new InvocationOnMock(this, new Method(getClass().getName(), "testAnswer_NonMockableType"), null);
 Object result = returnsDeepStubs.answer(invocation);
 Assert.assertEquals(returnsEmptyValues, result);
 }

 @Test
 public void testAnswer_MockableType() throws Exception {
 // Test for the case when the return type is mockable
 InvocationOnMock invocation = new InvocationOnMock(mockGenericsNest, new Method(Map.class, "entrySet"), null);
 Object result = returnsDeepStubs.answer(invocation);
 Assert.assertNotNull(result);
 Assert.assertTrue(result instanceof Map);

 // Test the returned mock's behavior
 Result deepStub = ((Map) result).entrySet().iterator().next();
 Assert.assertNotNull(deepStub);
 Object value = deepStub.getValue();
 Assert.assertNotNull(value);
 Result valueDeepStub = ((Result) value).iterator().next();
 Assert.assertNotNull(valueDeepStub);
 Assert.assertTrue(valueDeepStub instanceof Number);
 }

 @Test(expected = Throwable.class)
 public void testAnswer_ThrowsException() throws Throwable {
 // Test for the case when the method throws an exception
 InvocationOnMock invocation = new InvocationOnMock(this, new Method(getClass().getName(), "testAnswer_ThrowsException"), null);
 ReturnsDeepStubs.answer(invocation); // This will throw an exception
 }

 @Test
 public void testGetMock_NoMatchingStubbedInvocation() throws Exception {
 // Test for the case when there is no matching stubbed invocation
 InvocationOnMock invocation = new InvocationOnMock(mockGenericsNest, new Method(Map.class, "iterator"), null);
 Object result = returnsDeepStubs.getMock(invocation);
 Assert.assertEquals(returnsEmptyValues, result);
 }

 @Test
 public void testGetMock_MatchingStubbedInvocation() throws Exception {
 // Test for the case when there is a matching stubbed invocation
 InvocationOnMock invocation = new InvocationOnMock(mockNumber, new Method(Number.class, "intValue"), null);
 StubbedInvocationMatcher stubbedInvocationMatcher = Mockito.mock(StubbedInvocationMatcher.class);
 Mockito.when(stubbedInvocationMatcher.getInvocation()).thenReturn(invocation);
 InvocationContainerImpl container = Mockito.mock(InvocationContainerImpl.class);
 Mockito.when(container.getStubbedInvocations()).thenReturn(Arrays.asList(stubbedInvocationMatcher));
 InternalMockHandler handler = Mockito.mock(InternalMockHandler.class);
 Mockito.when(handler.getInvocationContainer()).thenReturn(container);
 Mockito.when(handler.getMockHandler(mockNumber)).thenReturn(handler);
 Object result = returnsDeepStubs.getMock(invocation);
 Assert.assertEquals(mockNumber, result);
 }

 @Test
 public void testRecordDeepStubMock() throws Exception {
 // Test for the case when recordDeepStubMock is called
 InvocationOnMock invocation = new InvocationOnMock(mockNumber, new Method(Number.class, "intValue"), null);
 Object mock = Mockito.mock(Number.class, new ReturnsDeepStubs());
 InvocationContainerImpl container = Mockito.mock(InvocationContainerImpl.class);
 Mockito.when(container.addAnswer(ArgumentMatchers.any(Answer.class), ArgumentMatchers.eq(false))).thenReturn(container);
 Object result = returnsDeepStubs.recordDeepStubMock(invocation, container);
 Assert.assertEquals(mock, result);
 }

 @Test
 public void testActualParameterizedType() throws Exception {
 // Test for the actualParameterizedType method
 Object mock = Mockito.mock(Map.class, new ReturnsDeepStubs());
 GenericMetadataSupport genericMetadataSupport = returnsDeepStubs.actualParameterizedType(mock);
 Assert.assertEquals(Map.class, genericMetadataSupport.getRawType());
 Assert.assertEquals(Map.class, genericMetadataSupport.getType());
 }

}