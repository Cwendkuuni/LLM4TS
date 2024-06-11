import static org.mockito.Mockito.*;
import org.mockito.Answers;
import org.mockito.ArgumentMatchers;
import org.mockito.internal.stubbing.answers.ReturnsDeepStubs;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReturnsDeepStubsTest {

 private ReturnsDeepStubs returnsDeepStubs;

 @Before
 public void setUp() {
 returnsDeepStubs = new ReturnsDeepStubs();
 }

 // Test for answer method with empty InvocationOnMock
 @Test
 public void testAnswerEmptyInvocation() {
 Object result = returnsDeepStubs.answer(mock(Object.class));
 assertNull(result);
 }

 // Test for answer method with non-mockable type
 @Test(expected = UnsupportedOperationException.class)
 public void testAnswerNonMockableType() {
 Object result = returnsDeepStubs.answer(mock(String[].class));
 }

 // Test for getMock method with no matching stubbed invocation
 @Test
 public void testGetMockNoMatchingStub() throws Throwable {
 Object mock = mock(Object.class);
 returnsDeepStubs.answer(new InvocationOnMock(mock, null));
 Object result = returnsDeepStubs.getMock(new InvocationOnMock(mock, null), mock(GenericMetadataSupport.class));
 assertNull(result);
 }

 // Test for getMock method with matching stubbed invocation
 @Test
 public void testGetMockMatchingStub() throws Throwable {
 Object mock = mock(Object.class);
 when(mock.method()).thenReturn(1);
 Object result = returnsDeepStubs.getMock(new InvocationOnMock(mock, ArgumentMatchers.method(Object.class, "method")), mock(GenericMetadataSupport.class));
 assertEquals(1, result);
 }

 // Test for createNewDeepStubMock method with empty list of extra interfaces
 @Test
 public void testCreateNewDeepStubMockEmptyExtraInterfaces() {
 Object mock = returnsDeepStubs.createNewDeepStubMock(mock(GenericMetadataSupport.class));
 assertNotNull(mock);
 }

 // Test for createNewDeepStubMock method with extra interfaces
 @Test
 public void testCreateNewDeepStubMockExtraInterfaces() {
 Object mock = mock(Object.class);
 when(mock.method()).thenReturn(1);
 GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
 returnTypeGenericMetadata.addRawExtraInterface(String[].class);
 Object newMock = returnsDeepStubs.createNewDeepStubMock(returnTypeGenericMetadata);
 assertNotNull(newMock);
 verify(newMock).method();
 }

 // Test for recordDeepStubMock method
 @Test(expected = NullPointerException.class)
 public void testRecordDeepStubMockNullMock() throws Throwable {
 InvocationContainerImpl container = mock(InvocationContainerImpl.class);
 ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
 returnsDeepStubs.recordDeepStubMock(null, container);
 }

 // Test for recordDeepStubMock method with null answer
 @Test(expected = NullPointerException.class)
 public void testRecordDeepStubMockNullAnswer() throws Throwable {
 Object mock = mock(Object.class);
 InvocationContainerImpl container = mock(InvocationContainerImpl.class);
 ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
 returnsDeepStubs.recordDeepStubMock(mock, container, null);
 }

 // Test for recordDeepStubMock method with existing answer
 @Test
 public void testRecordDeepStubMockExistingAnswer() throws Throwable {
 Object mock = mock(Object.class);
 InvocationContainerImpl container = mock(InvocationContainerImpl.class);
 ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
 Answer<Object> existingAnswer = mock(Answer.class);
 container.addAnswer(existingAnswer, false);
 returnsDeepStubs.recordDeepStubMock(mock, container, existingAnswer);
 assertEquals(existingAnswer, container.getAnswer(invocation(anyObject(), anyMethod())));
 }
}