import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.internal.stubbing.defaultanswers.ReturnsDeepStubs;
import org.mockito.internal.stubbing.defaultanswers.ReturnsDeepStubsSerializationFallback;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.withSettings;

@RunWith(org.mockito.runners.JUnit4.class)
public class ReturnsDeepStubsTest {

 // Test method for answer(InvocationOnMock)
 @Test
 public void testAnswer() throws Throwable {
 // Given
 Object mock = mock(Object.class, new ReturnsDeepStubs());
 // When
 Object result = mock.answer(new InvocationOnMock(mock, new Object()[0], new Object()[0], new Object()[0]));
 // Then
 // Since the method returns the mock itself, we verify no method calls on the mock
 verify(mock).answer(Mockito.any(InvocationOnMock.class));
 // Assert that the returned object is the same as the given mock
 assertEquals(mock, result);
 }

 // Test method for deepStub(InvocationOnMock, GenericMetadataSupport)
 @Test
 public void testDeepStub_ReturnTypeIsMockable() throws Throwable {
 // Given
 Object mock = mock(Object.class, new ReturnsDeepStubs());
 // When
 Object deepStub = new ReturnsDeepStubs().deepStub(new InvocationOnMock(mock, new Object()[0], new Object()[0], new Object()[0]), Mockito.mock(GenericMetadataSupport.class));
 // Then
 // Since the method returns the deep stub, we verify no method calls on the deep stub
 verify(deepStub).answer(Mockito.any(InvocationOnMock.class));
 // Assert that the deep stub is the same as the given mock
 assertEquals(mock, deepStub);
 }

 // Test method for deepStub(InvocationOnMock, GenericMetadataSupport) when return type is not mockable
 @Test(expected = java.lang.IllegalArgumentException.class)
 public void testDeepStub_ReturnTypeIsNotMockable() throws Throwable {
 // Given
 Object mock = mock(Object.class, new ReturnsDeepStubs());
 // When & Then
 new ReturnsDeepStubs().deepStub(new InvocationOnMock(mock, new Object()[0], new Object()[0], new Object()[0]), Mockito.mock(Class.class));
 }

 // Test method for newDeepStubMock(GenericMetadataSupport)
 @Test
 public void testNewDeepStubMock() throws Throwable {
 // Given
 Object mock = mock(Object.class, new ReturnsDeepStubs());
 GenericMetadataSupport returnTypeGenericMetadata = Mockito.mock(GenericMetadataSupport.class);
 // When
 Object deepStub = new ReturnsDeepStubs().newDeepStubMock(returnTypeGenericMetadata);
 // Then
 // Since the method returns the mock, we verify no method calls on the mock
 verify(deepStub).answer(Mockito.any(InvocationOnMock.class));
 // Assert that the deep stub is a new instance of the given type
 assertEquals(returnTypeGenericMetadata.getType(), deepStub.getClass());
 }

 // Test method for recordDeepStubAnswer(Object, InvocationContainerImpl)
 @Test
 public void testRecordDeepStubAnswer() throws Throwable {
 // Given
 Object mock = mock(Object.class, new ReturnsDeepStubs());
 InvocationContainerImpl container = new InvocationContainerImpl();
 // When
 Object deepStub = new ReturnsDeepStubs().recordDeepStubAnswer(mock, container);
 // Then
 // Since the method returns the deep stub, we verify no method calls on the deep stub
 verify(deepStub).answer(Mockito.any(InvocationOnMock.class));
 // Assert that the deep stub is added to the container
 assertEquals(deepStub, container.getAnswerFor(Mockito.eq(new InvocationOnMock(mock, new Object()[0], new Object()[0], new Object()[0]))).getAnswer());
 }

 // Test method for actualParameterizedType(Object) when mock is a generic type with raw type Object
 @Test
 public void testActualParameterizedType_MockIsGenericTypeWithRawTypeObject() throws Throwable {
 // Given
 Object mock = mock(List.class);
 // When
 GenericMetadataSupport returnTypeGenericMetadata = new ReturnsDeepStubs().actualParameterizedType(mock);
 // Then
 // Assert that the return type generic metadata is correct
 assertEquals(List.class, returnTypeGenericMetadata.getType());
 assertEquals(java.util.List.class, returnTypeGenericMetadata.getRawType());
 assertEquals(java.util.List.class, returnTypeGenericMetadata.getSuperclass());
 assertTrue(returnTypeGenericMetadata.hasRawExtraInterfaces());
 assertArrayEquals(new Class[0], returnTypeGenericMetadata.getRawExtraInterfaces());
 }

 // Test method for actualParameterizedType(Object) when mock is a generic type with generic types
 @Test
 public void testActualParameterizedType_MockIsGenericTypeWithGenericTypes() throws Throwable {
 // Given
 Object mock = mock(Map.class, new WithSettings().extraInterfaces(Map.Entry.class));
 // When
 GenericMetadataSupport returnTypeGenericMetadata = new ReturnsDeepStubs().actualParameterizedType(mock);
 // Then
 // Assert that the return type generic metadata is correct
 assertEquals(Map.class, returnTypeGenericMetadata.getType());
 assertEquals(java.util.Map.class, returnTypeGenericMetadata.getRawType());
 assertEquals(java.util.Map.class, returnTypeGenericMetadata.getSuperclass());
 assertFalse(returnTypeGenericMetadata.hasRawExtraInterfaces());
 }
}