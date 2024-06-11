import org.junit.Before;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.ArgumentMatchers;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;
import org.mockito.internal.stubbing.defaultanswers.ReturnsDeepStubs;
import org.mockito.stubbing.Answer;

import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ReturnsDeepStubsTest {

 private ReturnsDeepStubs returnsDeepStubs;
 private Object mock1;
 private Object mock2;
 private Object mock3;
 private Object mock4;

 @Before
 public void setUp() {
 returnsDeepStubs = new ReturnsDeepStubs();
 mock1 = mock(Map.class, Answers.RETURNS_DEEP_STUBS);
 mock2 = mock(Map.Entry.class, Answers.RETURNS_DEEP_STUBS);
 mock3 = mock(Number.class, Answers.RETURNS_DEEP_STUBS);
 mock4 = mock(Number[].class, Answers.RETURNS_DEEP_STUBS);
 }

 @Test
 public void testAnswerMethodWithMockableType() throws Exception {
 // Basic test for answer method with mockable type
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 Type returnType = invocation.getMethod().getReturnType();
 Object result = returnsDeepStubs.answer(invocation);
 assertNotNull(result);
 assertEquals(returnType, result.getClass());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAnswerMethodWithNonMockableType() throws Exception {
 // Test for answer method with non-mockable type
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 Type returnType = int[].class;
 returnsDeepStubs.answer(invocation);
 }

 @Test
 public void testGetMockMethodWithMatchingInvocation() throws Exception {
 // Basic test for getMock method with matching invocation
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 Object mock = returnsDeepStubs.getMock(invocation);
 assertNotNull(mock);
 }

 @Test
 public void testRecordDeepStubMockMethod() throws Exception {
 // Test for recordDeepStubMock method
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 InvocationContainerImpl container = new InvocationContainerImpl();
 Object mock = mock(Map.class, Answers.RETURNS_DEEP_STUBS);
 ReturnsDeepStubs deepStubs = new ReturnsDeepStubs();
 deepStubs.recordDeepStubMock(invocation, container);
 container.addAnswer(Mockito.answer(mock), false);
 InvocationOnMock invocation2 = Mockito.mock(InvocationOnMock.class);
 Object result = deepStubs.getMock(invocation2);
 assertEquals(mock, result);
 }

 @Test
 public void testGetMockMethodWithUnmatchedInvocation() throws Exception {
 // Test for getMock method with unmatched invocation
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 Object mock = mock(Map.class, Answers.RETURNS_DEEP_STUBS);
 InvocationContainerImpl container = new InvocationContainerImpl();
 container.addAnswer(Mockito.answer(mock), false);
 Object result = returnsDeepStubs.getMock(invocation);
 assertNull(result);
 }

 @Test
 public void testAnswerMethodWithNestedGenericTypes() throws Exception {
 // Test for answer method with nested generic types
 Class<Map> MapClass = Map.class;
 Class<Set> SetClass = Set.class;
 Class<Number> NumberClass = Number.class;
 Type mapType = new LinkedHashMap<Object, Object>().getType();
 Type setType = new LinkedHashSet<Number>().getType();
 Type numberArrayType = Number[].class.getType();
 Type genericType = ParameterizedType.uncheckedCast(MapType.getGenericSuperclass());
 Type mapEntryType = new Type[] {MapClass, SetClass}.getClass();
 Type mapEntryValueType = new Type[] {NumberClass}.getClass();
 Type mapEntryTypeWithGenericReturnType = new Type[] {MapClass, Set.class}.getClass();
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 Object result = returnsDeepStubs.answer(invocation);
 Type resultType = result.getClass().getGenericSuperclass();
 assertEquals(MapEntryTypeWithGenericReturnType.getActualTypeArguments()[1], resultType.getActualTypeArguments()[0]);
 }
}