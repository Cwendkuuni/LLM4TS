import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.withSettings;

@RunWith(org.mockito.runners.JUnit4.class)
public class ReturnsDeepStubsTest {

 private ReturnsDeepStubs deepStubs;
 private Object mock;

 @Before
 public void setUp() {
 deepStubs = new ReturnsDeepStubs();
 mock = mock(Map.class, withSettings().answerings(Answers.RETURNS_DEEP_STUBS));
 }

 @Test
 public void testAnswer_whenInvocationMatchesStubbedInvocation_thenReturnStub() throws Exception {
 // given
 StubbedInvocationMatcher stubbedInvocationMatcher = Mockito.mock(StubbedInvocationMatcher.class);
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 Container container = new Container();
 container.addStubbedInvocation(stubbedInvocationMatcher);
 InternalMockHandler<Object> handler = new MockUtil().getMockHandler(mock);
 handler.setInvocationContainer(container);

 // when
 Object answer = deepStubs.answer(invocation);

 // then
 verify(stubbedInvocationMatcher).answer(invocation);
 Assert.assertSame(mock, answer);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAnswer_whenInvocationDoesNotMatchAnyStubbedInvocation_thenThrowException() throws Exception {
 // given
 Container container = new Container();
 InternalMockHandler<Object> handler = new MockUtil().getMockHandler(mock);
 handler.setInvocationContainer(container);

 // when, then
 deepStubs.answer(Mockito.mock(InvocationOnMock.class));
 }

 @Test
 public void testNewDeepStubMock_whenCreateMockUsingGenericMetadata_thenReturnNewMock() throws Exception {
 // given
 GenericMetadataSupport returnTypeGenericMetadata = Mockito.mock(GenericMetadataSupport.class);

 // when
 Object newMock = deepStubs.newDeepStubMock(returnTypeGenericMetadata);

 // then
 verify(returnTypeGenericMetadata).hasRawExtraInterfaces();
 if (returnTypeGenericMetadata.hasRawExtraInterfaces()) {
 verify(returnTypeGenericMetadata).extraInterfaces(ArgumentMatchers.isA(List.class));
 }
 verify(returnTypeGenericMetadata).serializable();
 verify(returnTypeGenericMetadata).defaultAnswer(deepStubs);
 verify(returnTypeGenericMetadata).rawType(Mockito.eq(returnTypeGenericMetadata.getType()));
 Assert.assertNotNull(newMock);
 }

 @Test
 public void testRecordDeepStubAnswer_whenRecordAnswerForInvocationContainer_thenReturnMock() throws Exception {
 // given
 Container container = new Container();
 Object mock = mock(Map.class, withSettings().answerings(Answers.RETURNS_DEEP_STUBS));

 // when
 Object answer = deepStubs.recordDeepStubAnswer(mock, container);

 // then
 Assert.assertSame(mock, answer);
 }

 @Test
 public void testDeepStub_whenInvocationMatchesStubbedInvocation_thenReturnStubAnswer() throws Exception {
 // given
 Container container = new Container();
 Object mock = mock(Map.class, withSettings().answerings(Answers.RETURNS_DEEP_STUBS));
 StubbedInvocationMatcher stubbedInvocationMatcher = Mockito.mock(StubbedInvocationMatcher.class);
 container.addStubbedInvocation(stubbedInvocationMatcher);
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 handler.setInvocationContainer(container);

 // when
 Object answer = deepStubs.deepStub(invocation, Mockito.mock(GenericMetadataSupport.class));

 // then
 verify(stubbedInvocationMatcher).answer(invocation);
 Assert.assertSame(mock, answer);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testDeepStub_whenInvocationDoesNotMatchAnyStubbedInvocation_thenThrowException() throws Exception {
 // given
 Container container = new Container();
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 handler.setInvocationContainer(container);

 // when, then
 deepStubs.deepStub(invocation, Mockito.mock(GenericMetadataSupport.class));
 }

 @Test
 public void testAnswer_whenInvokeWithANonMockableType_thenDelegateToDefaultEmptyValues() throws Exception {
 // given
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 GenericDeclaration genericDeclaration = Mockito.mock(GenericDeclaration.class);
 Type type = Mockito.mock(Type.class);
 when(genericDeclaration.getTypeArguments()).thenReturn(new Type[] { type });
 when(genericDeclaration.getGenericSuperclass()).thenReturn(type);
 when(type.getRawType()).thenReturn(int[].class);
 when(MockitoCore.isTypeMockable(int[].class)).thenReturn(false);

 // when
 Object answer = deepStubs.answer(invocation);

 // then
 verifyNoMoreInteractions(MockitoCore.class);
 Assert.assertNotNull(answer);
 }
}

 class Container implements InvocationContainerImpl {
 private final List<StubbedInvocationMatcher> stubbedInvocations = Arrays.asList(Mockito.mock(StubbedInvocationMatcher.class), Mockito.mock(StubbedInvocationMatcher.class));

 public void addAnswer(Answer<?> answer, boolean serializable) {
 // no-op
 }

 public void addStubbedInvocation(StubbedInvocationMatcher stubbedInvocationMatcher) {
 addAnswer(stubbedInvocationMatcher, false);
 }

 public List<StubbedInvocationMatcher> getStubbedInvocations() {
 return stubbedInvocations;
 }
}