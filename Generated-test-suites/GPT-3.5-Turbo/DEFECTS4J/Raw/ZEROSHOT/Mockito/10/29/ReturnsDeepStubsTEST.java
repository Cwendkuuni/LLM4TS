import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class ReturnsDeepStubsTest {
  
  private ReturnsDeepStubs returnsDeepStubs;
 
  @Before
  public void setup() {
    returnsDeepStubs = new ReturnsDeepStubs();
  }
 
  @Test
  public void testAnswer() throws Throwable {
    // Mockito.mock(InvocationOnMock invocation)
    InvocationOnMock invocation = mock(InvocationOnMock.class);
    Object result = returnsDeepStubs.answer(invocation);
    // assert result
    
  }
  
  @Test
  public void testDeepStub() throws Throwable {
    // Mockito.mock(InvocationOnMock invocation)
    InvocationOnMock invocation = mock(InvocationOnMock.class);
    GenericMetadataSupport returnTypeGenericMetadata = new GenericMetadataSupport();
    Object result = returnsDeepStubs.deepStub(invocation, returnTypeGenericMetadata);
    // assert result
    
  }
  
  @Test
  public void testNewDeepStubMock() {
    GenericMetadataSupport returnTypeGenericMetadata = new GenericMetadataSupport();
    Object result = returnsDeepStubs.newDeepStubMock(returnTypeGenericMetadata);
    // assert result
    
  }
  
  @Test
  public void testWithSettingsUsing() {
    GenericMetadataSupport returnTypeGenericMetadata = new GenericMetadataSupport();
    MockSettings mockSettings = returnsDeepStubs.withSettingsUsing(returnTypeGenericMetadata);
    // assert mockSettings
    
  }
  
  @Test
  public void testReturnsDeepStubsAnswerUsing() {
    GenericMetadataSupport returnTypeGenericMetadata = new GenericMetadataSupport();
    ReturnsDeepStubs returnsDeepStubsAnswer = returnsDeepStubs.returnsDeepStubsAnswerUsing(returnTypeGenericMetadata);
    // assert returnsDeepStubsAnswer
    
  }
  
  @Test
  public void testRecordDeepStubAnswer() throws Throwable {
    Object mock = new Object();
    InvocationContainerImpl container = new InvocationContainerImpl();
    Object result = returnsDeepStubs.recordDeepStubAnswer(mock, container);
    // assert result
    
  }
  
  @Test
  public void testActualParameterizedType() {
    Object mock = new Object();
    GenericMetadataSupport result = returnsDeepStubs.actualParameterizedType(mock);
    // assert result
    
  }
  
  @Test
  public void testReturnsDeepStubsSerializationFallback() {
    GenericMetadataSupport returnTypeGenericMetadata = new GenericMetadataSupport();
    ReturnsDeepStubsSerializationFallback result = new ReturnsDeepStubsSerializationFallback(returnTypeGenericMetadata);
    // assert result
    
  }
  
  @Test
  public void testDeeplyStubbedAnswer() {
    Object mock = new Object();
    DeeplyStubbedAnswer deeplyStubbedAnswer = new DeeplyStubbedAnswer(mock);
    // assert deeplyStubbedAnswer
    
  }
  
  @Test
  public void testMockitoCore() {
    MockitoCore result = ReturnsDeepStubs.mockitoCore();
    // assert result
    
  }
  
  @Test
  public void testDelegate() {
    ReturnsEmptyValues result = ReturnsDeepStubs.delegate();
    // assert result
    
  }
}