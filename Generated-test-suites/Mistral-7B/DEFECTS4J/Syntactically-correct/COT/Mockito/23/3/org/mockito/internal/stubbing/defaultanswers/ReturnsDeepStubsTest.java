import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.internal.stubbing.answers.Answer;
import org.mockito.internal. stubbing.defaultanswers.ReturnsDeepStubs;
import org.mockito.internal.util.reflection.GenericMetadataSupport;
import org.mockito.invocation.InvocationOnMock;

public class ReturnsDeepStubsTest {

 private ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

 @Test
 public void testAnswerMethodWithEmptyInvocation() {
 // Test case for method answer(InvocationOnMock invocation) with empty invocation
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 Object result = returnsDeepStubs.answer(invocation);
 Assert.assertNull(result);
 }

 @Test
 public void testAnswerMethodWithMockableType() throws Exception {
 // Test case for method answer(InvocationOnMock invocation) with mockable type
 InterfaceMock mock = Mockito.mock(InterfaceMock.class, new ReturnsDeepStubs());
 Object returnTypeGenericMetadata = new GenericMetadataSupport(InterfaceMock.class);
 Object result = returnsDeepStubs.answer(Mockito.argThat(i -> i.getMock() == mock));
 Assert.assertEquals(mock, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAnswerMethodWithNonMockableType() throws Exception {
 // Test case for method answer(InvocationOnMock invocation) with non-mockable type
 Object nonMockableType = new Object() {};
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 invocation.setTarget(nonMockableType);
 Assert.assertThrows(IllegalArgumentException.class, () -> returnsDeepStubs.answer(invocation));
 }

 @Test
 public void testGetMockMethodWithEmptyInvocationContainer() throws Exception {
 // Test case for method getMock(InvocationOnMock invocation, GenericMetadataSupport returnTypeGenericMetadata) with empty InvocationContainerImpl
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 Object result = returnsDeepStubs.getMock(invocation, Mockito.mock(GenericMetadataSupport.class));
 Assert.assertNull(result);
 }

 @Test
 public void testGetMockMethodWithStubbedInvocationMatcher() throws Exception {
 // Test case for method getMock(InvocationOnMock invocation, GenericMetadataSupport returnTypeGenericMetadata) with StubbedInvocationMatcher
 StubbedInvocationMatcher stubbedInvocationMatcher = Mockito.mock(StubbedInvocationMatcher.class);
 InvocationContainerImpl container = Mockito.mock(InvocationContainerImpl.class);
 when(container.getStubbedInvocations()).thenReturn(new StubbedInvocationMatcher[]{stubbedInvocationMatcher});
 Object returnTypeGenericMetadata = new GenericMetadataSupport(String.class);
 Object stubbedReturnValue = Mockito.mock(Object.class);
 when(stubbedInvocationMatcher.answer(ArgumentMatchers.any(InvocationOnMock.class))).thenReturn(stubbedReturnValue);
 Object result = returnsDeepStubs.getMock(Mockito.argThat(i -> i.getMock() == container), returnTypeGenericMetadata);
 Assert.assertEquals(stubbedReturnValue, result);
 }

 @Test(expected = IllegalStateException.class)
 public void testGetMockMethodWithNoMatchingStubbedInvocationMatcher() throws Exception {
 // Test case for method getMock(InvocationOnMock invocation, GenericMetadataSupport returnTypeGenericMetadata) with no matching StubbedInvocationMatcher
 InvocationContainerImpl container = Mockito.mock(InvocationContainerImpl.class);
 Object returnTypeGenericMetadata = new GenericMetadataSupport(String.class);
 Object result = returnsDeepStubs.getMock(Mockito.argThat(i -> i.getMock() == container), returnTypeGenericMetadata);
 Assert.assertNull(result);
 }

 @Test
 public void testCreateNewDeepStubMockMethod() throws Exception {
 // Test case for method createNewDeepStubMock(GenericMetadataSupport returnTypeGenericMetadata)
 InterfaceMock deepStub = Mockito.mock(InterfaceMock.class, new ReturnsDeepStubs());
 Object returnTypeGenericMetadata = new GenericMetadataSupport(InterfaceMock.class);
 Object result = returnsDeepStubs.createNewDeepStubMock(returnTypeGenericMetadata);
 Assert.assertEquals(deepStub, result);
 }

 @Test(expected = IllegalStateException.class)
 public void testCreateNewDeepStubMockMethodWithNonMockableType() throws Exception {
 // Test case for method createNewDeepStubMock(GenericMetadataSupport returnTypeGenericMetadata) with non-mockable type
 Object nonMockableType = new Object() {};
 Object result = returnsDeepStubs.createNewDeepStubMock(new GenericMetadataSupport(nonMockableType.getClass()));
 Assert.assertThrows(IllegalStateException.class, () -> returnsDeepStubs.createNewDeepStubMock(new GenericMetadataSupport(nonMockableType.getClass())));
 }

 @Test
 public void testRecordDeepStubMockMethodWithEmptyContainer() throws Exception {
 // Test case for method recordDeepStubMock(Object mock, InvocationContainerImpl container) with empty InvocationContainerImpl
 InvocationContainerImpl container = Mockito.mock(InvocationContainerImpl.class);
 Object deepStub = Mockito.mock(Object.class);
 Assert.assertDoesNotAnswer(invocation -> returnsDeepStubs.recordDeepStubMock(deepStub, container));
 }

 @Test
 public void testRecordDeepStubMockMethod() throws Exception {
 // Test case for method recordDeepStubMock(Object mock, InvocationContainerImpl container)
 InvocationContainerImpl container = Mockito.mock(InvocationContainerImpl.class);
 Object deepStub = Mockito.mock(Object.class);
 when(container.addAnswer(ArgumentMatchers.any(Answer.class), ArgumentMatchers.booleanValue(false))).thenAnswer(i -> i.answersTo(deepStub));
 Object result = returnsDeepStubs.recordDeepStubMock(deepStub, container);
 Assert.assertEquals(deepStub, result);
 }
}