import org.junit.Before;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.internal.stubbing.defaultanswers.ReturnsDeepStubs;

import java.io.Serializable;
import java.lang.reflect.GenericMetadataSupport;
import java.lang.reflect.Type;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.withSettings;

public class ReturnsDeepStubsTest {

 private ReturnsDeepStubs returnsDeepStubs;
 private Object mock;
 private GenericMetadataSupport returnTypeGenericMetadata;

 @Before
 public void setup() {
 mock = mock(Object.class, new ReturnsDeepStubs());
 returnTypeGenericMetadata = (GenericMetadataSupport) Mockito.type(mock.getClass())
 .getGenericInterfaces()[0];
 }

 @Test
 public void testAnswer() throws Throwable {
 // Test basic functionality
 Object result = returnsDeepStubs.answer(Mockito.mock(InvocationOnMock.class));
 assertNotNull(result);
 // Test with non-mockable type
 Type rawType = returnTypeGenericMetadata.rawType();
 if (!Mockito.isTypeMockable(rawType)) {
 assertNotNull(returnsDeepStubs.delegate().returnValueFor(rawType));
 } else {
 // Test deep stub
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 Object deepStub = returnsDeepStubs.deepStub(invocation, returnTypeGenericMetadata);
 assertNotNull(deepStub);
 verify(deepStub).toString(); // Verify that the deep stub can be used in verification
 }
 }

 @Test(expected = Throwable.class)
 public void testExceptionHandling() throws Throwable {
 // Test exception handling
 throwsException(IllegalArgumentException.class, () -> returnsDeepStubs.answer(null));
 }

 @Test
 public void testSerialization() throws Exception {
 // Test serialization
 ReturnsDeepStubsSerializationFallback deepStubsSerializationFallback = new ReturnsDeepStubsSerializationFallback(returnTypeGenericMetadata);
 Object serialized = Mockito.serialize(deepStubsSerializationFallback);
 assertNotNull(serialized);
 Object deserialized = Mockito.deserialize(serialized);
 assertTrue(deserialized instanceof ReturnsDeepStubsSerializationFallback);
 }
}