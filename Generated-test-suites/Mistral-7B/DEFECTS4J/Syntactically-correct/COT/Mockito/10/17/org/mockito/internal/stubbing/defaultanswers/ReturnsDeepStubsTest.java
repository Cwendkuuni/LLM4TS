import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.internal.stubbing.defaultanswers.ReturnsDeepStubs;
import org.mockito.internal.stubbing.defaultanswers.ReturnsDeepStubsSerializationFallback;
import org.mockito.internal.stubbing.defaultanswers.DeeplyStubbedAnswer;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.MockSettings;
import org.mockito.internal.util.GenericMetadataSupport;
import org.mockito.internal.util.reflection.GenericType;
import org.mockito.internal.util.reflection.Reflections;
import org.mockito.internal.util.reflection.Whitebox;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.withSettings;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;

@RunWith(MockitoJUnitRunner.class)
public class ReturnsDeepStubsTest {

 private static final Class<ReturnsDeepStubs> RETURNS_DEEP_STUBS_CLASS = ReturnsDeepStubs.class;

 @Test
 public void testAnswerMethod() {
 InvocationOnMock invocationMock = Mockito.mock(InvocationOnMock.class);
 GenericMetadataSupport returnTypeMetadata = Mockito.mock(GenericMetadataSupport.class);

 ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
 Object answer = returnsDeepStubs.answer(invocationMock);

 assertNotNull(answer);
 }

 @Test
 public void testAnswerMethodWithNonMockableType() {
 InvocationOnMock invocationMock = Mockito.mock(InvocationOnMock.class);
 GenericMetadataSupport returnTypeMetadata = Mockito.mock(GenericMetadataSupport.class);

 when(returnTypeMetadata.rawType()).thenReturn(int[].class);
 ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
 Object answer = returnsDeepStubs.answer(invocationMock);

 assertEquals(Reflections.newInstance(ReturnsEmptyValues.class), answer);
 }

 @Test
 public void testDeepStubMethod() throws Exception {
 InvocationOnMock invocationMock = Mockito.mock(InvocationOnMock.class);
 GenericMetadataSupport returnTypeMetadata = Mockito.mock(GenericMetadataSupport.class);
 when(returnTypeMetadata.rawType()).thenReturn(String[].class);

 MockitoCore mockitoCore = Mockito.mock(MockitoCore.class);
 Whitebox.setInternalState(ReturnsDeepStubs.class, "mockitoCore", mockitoCore);

 InternalMockHandler mockHandler = MockUtil.getMockHandler(invocationMock.getMock());
 InvocationContainerImpl container = (InvocationContainerImpl) mockHandler.getInvocationContainer();

 when(mockitoCore.mock(String[].class, withSettings())).thenReturn(Mockito.mock(String[].class));

 Object deepStubAnswer = new ReturnsDeepStubs().deepStub(invocationMock, returnTypeMetadata);
 Object deepStubMock = Whitebox.getInternalState(container, "deepStubs").get(0);

 assertSame(deepStubMock, deepStubAnswer);
 }

 @Test(expected = IllegalStateException.class)
 public void testDeepStubMethodWithFailedVerification() throws Exception {
 InvocationOnMock invocationMock = Mockito.mock(InvocationOnMock.class);
 GenericMetadataSupport returnTypeMetadata = Mockito.mock(GenericMetadataSupport.class);
 when(returnTypeMetadata.rawType()).thenReturn(String[].class);

 MockitoCore mockitoCore = Mockito.mock(MockitoCore.class);
 Whitebox.setInternalState(ReturnsDeepStubs.class, "mockitoCore", mockitoCore);

 InternalMockHandler mockHandler = MockUtil.getMockHandler(invocationMock.getMock());
 InvocationContainerImpl container = (InvocationContainerImpl) mockHandler.getInvocationContainer();

 when(mockitoCore.mock(String[].class, withSettings())).thenReturn(Mockito.mock(String[].class));

 StubbedInvocationMatcher stubbedInvocationMatcher = Mockito.mock(StubbedInvocationMatcher.class);
 container.addStubbedInvocation(stubbedInvocationMatcher);

 when(stubbedInvocationMatcher.getInvocation()).thenReturn(invocationMock);
 when(stubbedInvocationMatcher.answer(invocationMock)).thenAnswer(i -> "Stubbed Answer");

 ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
 Object deepStubAnswer = returnsDeepStubs.deepStub(invocationMock, returnTypeMetadata);

 verifyZeroInteractions(stubbedInvocationMatcher);
 }

 @Test
 public void testNewDeepStubMockMethod() throws Exception {
 GenericMetadataSupport returnTypeMetadata = Mockito.mock(GenericMetadataSupport.class);
 when(returnTypeMetadata.rawType()).thenReturn(List.class);
 when(returnTypeMetadata.hasRawExtraInterfaces()).thenReturn(false);
 when(returnTypeMetadata.resolveGenericReturnType(Mockito.any(Method.class))).thenReturn(List.class);

 MockSettings mockSettings = withSettings().serializable();
 Object newDeepStubMock = new ReturnsDeepStubs().newDeepStubMock(returnTypeMetadata);

 assertNotNull(newDeepStubMock);
 assertEquals(List.class, newDeepStubMock.getClass());
 assertEquals(mockSettings, Whitebox.getInternalState(newDeepStubMock, "mockSettings"));
 }

 @Test
 public void testNewDeepStubMockMethodWithExtraInterfaces() throws Exception {
 GenericMetadataSupport returnTypeMetadata = Mockito.mock(GenericMetadataSupport.class);
 when(returnTypeMetadata.rawType()).thenReturn(List.class);
 when(returnTypeMetadata.hasRawExtraInterfaces()).thenReturn(true);
 when(returnTypeMetadata.rawExtraInterfaces()).thenReturn(new Class[]{Collection.class});
 when(returnTypeMetadata.resolveGenericReturnType(Mockito.any(Method.class))).thenReturn(List.class);

 MockSettings mockSettings = withSettings().serializable().extraInterfaces(Collection.class);
 Object newDeepStubMock = new ReturnsDeepStubs().newDeepStubMock(returnTypeMetadata);

 assertNotNull(newDeepStubMock);
 assertEquals(List.class, newDeepStubMock.getClass());
 assertEquals(mockSettings, Whitebox.getInternalState(newDeepStubMock, "mockSettings"));
 assertEquals(Collection.class, Whitebox.getInternalState(newDeepStubMock, "mockSettings.extraInterfaces"));
 }

 @Test
 public void testRecordDeepStubAnswerMethod() throws Exception {
 InvocationOnMock invocationMock = Mockito.mock(InvocationOnMock.class);
 InvocationContainerImpl container = Mockito.mock(InvocationContainerImpl.class);

 Object mock = mock(Object.class, withSettings().defaultAnswer(Answers.RETURNS_DEEP_STUBS));
 when(container.getInvocationForStubbing()).thenReturn(invocationMock);

 ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
 Object recordDeepStubAnswer = returnsDeepStubs.recordDeepStubAnswer(mock, container);

 assertEquals(mock, recordDeepStubAnswer);
 }

 @Test
 public void testReturnsDeepStubsAnswerUsingMethod() throws Exception {
 GenericMetadataSupport returnTypeMetadata = Mockito.mock(GenericMetadataSupport.class);
 when(returnTypeMetadata.rawType()).thenReturn(List.class);

 Object returnsDeepStubsAnswer = new ReturnsDeepStubs().returnsDeepStubsAnswerUsing(returnTypeMetadata);
 assertSame(new ReturnsDeepStubsSerializationFallback(returnTypeMetadata), returnsDeepStubsAnswer);
 }
}