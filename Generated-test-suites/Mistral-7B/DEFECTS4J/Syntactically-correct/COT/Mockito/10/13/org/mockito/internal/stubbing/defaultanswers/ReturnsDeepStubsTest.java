import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.internal.stubbing.defaultanswers.ReturnsDeepStubs;
import org.mockito.internal.util.reflection.GenericMetadataSupport;

import java.lang.reflect.Type;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.withSettings;
import static org.mockito.internal.util.reflection.MockUtil.getMockHandler;
import static org.mockito.internal.stubbing.InvocationContainerImpl.class$;
import static org.mockito.internal.stubbing.InvocationContainerImpl.getInvocationForStubbing;
import static org.mockito.internal.stubbing.ReturnsDeepStubs.deepStub;
import static org.mockito.internal.stubbing.ReturnsDeepStubs.recordDeepStubAnswer;
import static org.mockito.internal.stubbing.ReturnsDeepStubs.newDeepStubMock;
import static org.mockito.internal.stubbing.ReturnsDeepStubs.withSettingsUsing;
import static org.mockito.internal.stubbing.StubbedInvocationMatcher.answer;
import static org.mockito.internal.stubbing.StubbedInvocationMatcher.matches;
import static org.mockito.internal.util.reflection.GenericMetadataSupport.inferFrom;
import static org.mockito.internal.util.reflection.GenericMetadataSupport.resolveGenericReturnType;

@RunWith(MockitoJUnitRunner.class)
public class ReturnsDeepStubsTest {

 private ReturnsDeepStubs returnsDeepStubs;
 private Object mock1;
 private Object mock2;
 private Object mock3;
 private Object mock4;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 returnsDeepStubs = new ReturnsDeepStubs();
 mock1 = mock(Object.class, new ReturnsDeepStubs());
 mock2 = mock(Object.class, withSettings().defaultAnswer(Answers.RETURNS_DEEP_STUBS));
 mock3 = mock(Object[].class, withSettings().defaultAnswer(Answers.RETURNS_DEEP_STUBS));
 mock4 = mock(Object.class, withSettings().defaultAnswer(ReturnsDeepStubs.class));
 }

 @Test
 public void testAnswerMethod_whenInvokedWithNonMockableType_thenReturnsDelegateAnswer() throws Exception {
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 Type returnType = resolveGenericReturnType(invocation.getMethod(), Object.class);
 verify(returnsDeepStubs).delegate().returnValueFor(returnType.getType());
 }

 @Test
 public void testAnswerMethod_whenInvokedWithMockableType_thenReturnsDeepStub() throws Exception {
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 GenericMetadataSupport returnTypeGenericMetadata = inferFrom(invocation.getMock().getClass());
 Object deepStub = deepStub(invocation, returnTypeGenericMetadata);
 verify(returnsDeepStubs).deepStub(eq(invocation), eq(returnTypeGenericMetadata));
 verify(getMockHandler(invocation.getMock())).getInvocationContainer().addAnswer(eq(answer(deepStub)), anyBoolean());
 assertEquals(deepStub, returnsDeepStubs.answer(invocation));
 }

 @Test
 public void testNewDeepStubMockMethod_whenInvoked_thenCreatesNewMockWithReturnTypeMetadata() throws Exception {
 Object returnTypeMetadata = mock(GenericMetadataSupport.class);
 Object newMock = newDeepStubMock(returnTypeMetadata);
 verify(mockitoCore()).mock(returnTypeMetadata.rawType(), withSettingsUsing(returnTypeMetadata));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNewDeepStubMockMethod_whenInvokedWithNullMetadata_thenThrowsIllegalArgumentException() throws Exception {
 newDeepStubMock(null);
 }

 @Test
 public void testRecordDeepStubAnswerMethod_whenInvoked_thenRecordsDeepStubAnswer() throws Exception {
 Object deepStub = mock(Object.class);
 InvocationContainerImpl container = (InvocationContainerImpl) getMockHandler(mock1).getInvocationContainer();
 verify(container).addAnswer(eq(answer(deepStub)), anyBoolean());
 assertEquals(deepStub, recordDeepStubAnswer(mock1, container));
 }

 @Test
 public void testActualParameterizedTypeMethod_whenInvokedWithMockObject_thenReturnsTypeMetadata() throws Exception {
 Object mockObject = mock(Object.class);
 GenericMetadataSupport typeMetadata = actualParameterizedType(mockObject);
 assertNotNull(typeMetadata);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testActualParameterizedTypeMethod_whenInvokedWithNullMockObject_thenThrowsIllegalArgumentException() throws Exception {
 actualParameterizedType(null);
 }

 @Test(expected = IOException.class)
 public void testWriteReplaceMethod_whenInvoked_thenThrowsIOException() throws Exception {
 ReturnsDeepStubsReturnsDeepStubsSerializationFallback deepStubsSerializationFallback = new ReturnsDeepStubsReturnsDeepStubsSerializationFallback(mock(GenericMetadataSupport.class));
 deepStubsSerializationFallback.writeReplace();
 }

 @Test
 public void testDeeplyStubbedAnswer_whenInvoked_thenReturnsMockObject() throws Exception {
 Object deeplyStubbedAnswer = new DeeplyStubbedAnswer(mock1);
 assertEquals(mock1, deeplyStubbedAnswer.answer(mock(InvocationOnMock.class)));
 }
}