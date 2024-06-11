import org.junit.Before;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ReturnsDeepStubsTest {

 private ReturnsDeepStubs returnsDeepStubs;
 private Object mockGenericsNest;
 private Object nestedMock;

 @Before
 public void setUp() {
 mockGenericsNest = mock(GenericsNest.class, new ReturnsDeepStubs());
 nestedMock = mock(Number.class);
 }

 @Test
 public void testAnswerMethodWithNonMockableType() {
 // given
 Class<String> stringClass = String.class;

 // when
 Object answer = returnsDeepStubs.answer(ArgumentMatchers.argument(0), ArgumentMatchers.any(InvocationOnMock.class), stringClass, null);

 // then
 verify(returnsDeepStubs.delegate()).returnValueFor(stringClass);
 assertEquals(returnsDeepStubs.delegate().returnValueFor(stringClass), answer);
 }

 @Test
 public void testAnswerMethodWithMockableType() throws Exception {
 // given
 Type genericReturnType = Map.class.getTypeParameters()[0].getType();
 Type genericKeyType = Map.Entry.class.getGenericInterfaces()[0];
 Type genericValueType = Number[].class;
 Type[] genericArguments = {genericKeyType, genericValueType};
 Class<?> mapClass = (Class<?>) genericReturnType.getRawType();
 Class<GenericsNest> genericsNestClass = (Class<GenericsNest>) MockUtil.getRealClass(mockGenericsNest.getClass());
 GenericMetadataSupport returnTypeGenericMetadata = GenericMetadataSupport.fromRawType(mapClass);

 // when
 Object answer = returnsDeepStubs.answer(ArgumentMatchers.argument(0), ArgumentMatchers.any(InvocationOnMock.class), returnTypeGenericMetadata);

 // then
 Object deepStubMock = returnsDeepStubs.newDeepStubMock(returnTypeGenericMetadata);
 verify(mockitoCore()).mock(mapClass, withSettings().extraInterfaces(genericsNestClass.getGenericInterfaces()));
 verify(deepStubMock).iterator().next().getValue().iterator().next();
 assertEquals(deepStubMock, answer);
 }

 @Test
 public void testDeepStub() throws Exception {
 // given
 InvocationOnMock invocation = Mockito.mock(Mockito.method(GenericsNest.class, "iterator")).addAnswer(new Answer<Object>() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 return Arrays.asList(nestedMock).iterator();
 }
 });
 mockitoCore().mock(GenericsNest.class).answer(invocation);

 // when
 Object answer = returnsDeepStubs.answer(ArgumentMatchers.argument(0), ArgumentMatchers.any(InvocationOnMock.class), GenericMetadataSupport.forClass(GenericsNest.class));

 // then
 verify(mockitoCore()).mock(Number.class);
 verify(nestedMock).iterator().next();
 verify(mockitoCore()).mock(Number[].class);
 assertEquals(nestedMock, answer);
 }

 @Test(expected = AssertionError.class)
 public void testDeepStubWithDuplicateInvocations() throws Exception {
 // given
 InvocationOnMock firstInvocation = Mockito.mock(Mockito.method(GenericsNest.class, "iterator")).addAnswer(new Answer<Object>() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 return Arrays.asList(nestedMock).iterator();
 }
 });
 mockitoCore().mock(GenericsNest.class).answer(firstInvocation);

 InvocationOnMock secondInvocation = Mockito.mock(Mockito.method(GenericsNest.class, "iterator")).addAnswer(new Answer<Object>() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 throw new AssertionError("Unexpected invocation.");
 }
 });
 mockitoCore().mock(GenericsNest.class).answer(secondInvocation);

 // when, then
 Object answer = returnsDeepStubs.answer(ArgumentMatchers.argument(0), ArgumentMatchers.any(InvocationOnMock.class), GenericMetadataSupport.forClass(GenericsNest.class));

 // Verify that the first invocation was used
 verify(nestedMock).iterator().next();

 // Verify that the second invocation threw an AssertionError
 verify(mockitoCore()).mockError(ArgumentMatchers.eq(AssertionError.class), ArgumentMatchers.anyString(), ArgumentMatchers.eq(secondInvocation));
 }

 @Test
 public void testRecordDeepStubAnswer() throws Exception {
 // given
 InvocationContainerImpl container = Mockito.mock(InvocationContainerImpl.class);
 Object deepStubMock = mockitoCore().mock(Number.class);
 when(deepStubMock.iterator()).thenReturn(Arrays.asList(nestedMock).iterator());
 mockitoCore().mock(GenericsNest.class).answer(ArgumentMatchers.any(InvocationOnMock.class), ArgumentMatchers.eq(container));

 // when
 Object answer = returnsDeepStubs.answer(ArgumentMatchers.argument(0), ArgumentMatchers.any(InvocationOnMock.class), GenericMetadataSupport.forClass(GenericsNest.class));

 // then
 verify(container).addAnswer(ArgumentMatchers.instanceOf(DeeplyStubbedAnswer.class), false);
 verify(deepStubMock).iterator().next();
 verify(deepStubMock).iterator().next();
 assertEquals(deepStubMock, answer);
 }
}