import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.internal.stubbing.answers.ReturnsDeepStubs;
import org.mockito.internal.stubbing.answers.ReturnsEmptyValues;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.util.reflection.GenericMetadataSupport;
import org.mockito.stubbing.Answer;

import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

public class ReturnsDeepStubsTest {

 private ReturnsDeepStubs returnsDeepStubs;
 private ReturnsEmptyValues returnsEmptyValues;
 private Object mockObject;
 private Object deepStub;

 @Before
 public void setup() {
 returnsDeepStubs = new ReturnsDeepStubs();
 returnsEmptyValues = new ReturnsEmptyValues();
 }

 @Test
 public void testAnswerMethodReturnsExistingMock() {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 GenericMetadataSupport returnTypeGenericMetadata = Mockito.mock(GenericMetadataSupport.class);
 when(returnTypeGenericMetadata.rawType()).thenReturn(String.class);
 when(returnTypeGenericMetadata.resolveGenericReturnType(ArgumentMatchers.any(Method.class))).thenReturn(String.class);

 when(mockitoCore.isTypeMockable(String.class)).thenReturn(true);

 mockObject = mock(String.class, withSettings().defaultAnswer(returnsDeepStubs));

 InvocationOnMock invocationWithMock = Mockito.mock(InvocationOnMock.class);
 when(invocationWithMock.getMock()).thenReturn(mockObject);

 InvocationOnMock invocationToTest = Mockito.mock(InvocationOnMock.class);
 when(invocationToTest.getMethod()).thenReturn(new Method(String.class.getDeclaredMethod("length")));
 when(invocationToTest.getMock()).thenReturn(invocationWithMock);

 Object result = returnsDeepStubs.answer(invocationToTest);

 assertEquals(mockObject, result);

 verify(returnsDeepStubs).recordDeepStubMock(eq(mockObject), ArgumentMatchers.isInstanceOf(InvocationContainerImpl.class));
 }

 @Test(expected = Throwable.class)
 public void testAnswerMethodReturnsEmptyStubForNonMockableType() {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 GenericMetadataSupport returnTypeGenericMetadata = Mockito.mock(GenericMetadataSupport.class);
 when(returnTypeGenericMetadata.rawType()).thenReturn(List.class);
 when(returnTypeGenericMetadata.resolveGenericReturnType(ArgumentMatchers.any(Method.class))).thenReturn(List.class);
 when(mockitoCore.isTypeMockable(List.class)).thenReturn(false);

 returnsDeepStubs.answer(invocation);
 }

 @Test
 public void testGetMockCreatesNewMockForDeepStubbing() {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 GenericMetadataSupport returnTypeGenericMetadata = Mockito.mock(GenericMetadataSupport.class);
 when(returnTypeGenericMetadata.rawType()).thenReturn(String[].class);
 when(returnTypeGenericMetadata.resolveGenericReturnType(ArgumentMatchers.any(Method.class))).thenReturn(String[].class);

 when(mockitoCore.isTypeMockable(String[].class)).thenReturn(true);

 deepStub = returnsDeepStubs.getMock(invocation, returnTypeGenericMetadata);

 verifyZeroInteractions(deepStub);

 InvocationContainerImpl container = Mockito.mock(InvocationContainerImpl.class);
 InvocationOnMock invocationWithContainer = Mockito.mock(InvocationOnMock.class);
 when(invocationWithContainer.getInvocationContainer()).thenReturn(container);
 when(container.getStubbedInvocations()).thenReturn(Arrays.asList(Mockito.mock(StubbedInvocationMatcher.class)));

 verify(container).addAnswer(argThat(answer -> answer.getClass().equals(ReturnsDeepStubs.class)), eq(true));
 }

 @Test
 public void testGetMockReturnsExistingStubForPreviousStubbing() {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 GenericMetadataSupport returnTypeGenericMetadata = Mockito.mock(GenericMetadataSupport.class);
 when(returnTypeGenericMetadata.rawType()).thenReturn(String[].class);
 when(returnTypeGenericMetadata.resolveGenericReturnType(ArgumentMatchers.any(Method.class))).thenReturn(String[].class);
 when(mockitoCore.isTypeMockable(String[].class)).thenReturn(true);

 mockObject = mock(String[].class, withSettings().defaultAnswer(returnsDeepStubs));
 deepStub = returnsDeepStubs.getMock(invocation, returnTypeGenericMetadata);

 InvocationOnMock invocationWithMock = Mockito.mock(InvocationOnMock.class);
 when(invocationWithMock.getMock()).thenReturn(mockObject);

 InvocationOnMock invocationToTest = Mockito.mock(InvocationOnMock.class);
 when(invocationToTest.getMethod()).thenReturn(new Method(String[].class.getDeclaredMethod("length")));
 when(invocationToTest.getMock()).thenReturn(invocationWithMock);

 Object result = returnsDeepStubs.getMock(invocationToTest, returnTypeGenericMetadata);

 assertEquals(mockObject, result);
 }

 @Test
 public void testRecordDeepStubMock() {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 InvocationContainerImpl container = Mockito.mock(InvocationContainerImpl.class);

 ReturnsDeepStubs returnsDeepStubsSpy = Mockito.spy(returnsDeepStubs);

 when(mockitoCore.mock(String[].class, withSettings().defaultAnswer(returnsDeepStubsSpy))).thenReturn(deepStub);
 when(container.getInvocationForStubbing()).thenReturn(invocation);

 returnsDeepStubsSpy.recordDeepStubMock(deepStub, container);

 verify(container).addAnswer(any(), eq(false));
 }
}