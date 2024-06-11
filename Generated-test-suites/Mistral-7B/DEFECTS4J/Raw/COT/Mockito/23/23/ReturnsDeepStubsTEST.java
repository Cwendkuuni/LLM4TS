import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.lang.reflect.GenericMetadataSupport;
import java.lang.reflect.Type;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(org.mockito.runners.JUnit4.class)
public class ReturnsDeepStubsTest {

 private ReturnsDeepStubs returnsDeepStubs;
 private Object mock1;
 private Object mock2;
 private Object mock3;

 @Before
 public void setup() {
 returnsDeepStubs = new ReturnsDeepStubs();
 mock1 = mock(Type1.class, Answers.RETURNS_DEEP_STUBS);
 mock2 = mock(Type2.class, Answers.RETURNS_DEEP_STUBS);
 mock3 = mock(Type3.class, Answers.RETURNS_DEEP_STUBS);
 }

 @Test
 public void testAnswerMethod() {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 when(mock1.answerMethod(ArgumentMatchers.eq(invocation))).thenReturn("return value");

 Object result = returnsDeepStubs.answer(invocation);

 assertEquals("return value", result);
 verify(mock1).answerMethod(ArgumentMatchers.eq(invocation));
 }

 @Test
 public void testGetMockMethod() throws Exception {
 InvocationOnMock invocation1 = Mockito.mock(InvocationOnMock.class);
 InvocationOnMock invocation2 = Mockito.mock(InvocationOnMock.class);

 when(mock1.getMock()).thenReturn(mock2);
 when(mock2.getMock()).thenReturn(mock3);

 GenericMetadataSupport returnTypeGenericMetadata = new GenericMetadataSupport() {
 @Override
 public Type getRawType() {
 return Type3.class;
 }
 };

 Object mockResult = returnsDeepStubs.getMock(invocation1, returnTypeGenericMetadata);

 assertEquals(mock3, mockResult);
 verify(mock1).getMock();
 verify(mock2).getMock();
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testUnmockableType() throws Exception {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);

 when(returnsDeepStubs.answer(ArgumentMatchers.eq(invocation))).thenReturn(new Integer(1));

 GenericMetadataSupport returnTypeGenericMetadata = new GenericMetadataSupport() {
 @Override
 public Type getRawType() {
 throw new UnsupportedOperationException();
 }
 };

 returnsDeepStubs.answer(invocation);
 }

 @Test
 public void testRecordDeepStubMockMethod() throws Exception {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 Object mock = mock(Type1.class, Answers.RETURNS_DEEP_STUBS);
 InvocationContainerImpl container = new InvocationContainerImpl();
 container.setInvocationForStubbing(invocation);

 when(mock.recordDeepStubMock(ArgumentMatchers.eq(container))).thenReturn(mock2);

 Object result = returnsDeepStubs.recordDeepStubMock(mock, container);

 assertEquals(mock2, result);
 verify(mock).answer(ArgumentMatchers.eq(invocation));
 }

 @Test
 public void testAnswerMethodWithExistingStub() throws Exception {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 Object mock = mock(Type1.class, Answers.RETURNS_DEEP_STUBS);
 InvocationContainerImpl container = new InvocationContainerImpl();
 container.addAnswer(Mockito.answer(new Foo()), false);

 when(mock.answer(ArgumentMatchers.eq(invocation))).thenReturn(new Bar());

 Object result = returnsDeepStubs.answer(invocation);

 assertEquals(new Bar(), result);
 verify(mock).answer(ArgumentMatchers.eq(invocation));
 }

 @Test
 public void testGetMockMethodWithExistingStub() throws Exception {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 Object mock = mock(Type1.class, Answers.RETURNS_DEEP_STUBS);
 InvocationContainerImpl container = new InvocationContainerImpl();
 container.addAnswer(Mockito.answer(new Foo()), false);

 GenericMetadataSupport returnTypeGenericMetadata = new GenericMetadataSupport() {
 @Override
 public Type getRawType() {
 return Type3.class;
 }
 };

 when(mock.getMock()).thenReturn(mock2);
 when(mock2.getMock()).thenReturn(mock3);
 when(mock.answer(ArgumentMatchers.eq(invocation))).thenReturn(new Bar());

 Object result = returnsDeepStubs.getMock(invocation, returnTypeGenericMetadata);

 assertEquals(mock3, result);
 verify(mock).answer(ArgumentMatchers.eq(invocation));
 }

}