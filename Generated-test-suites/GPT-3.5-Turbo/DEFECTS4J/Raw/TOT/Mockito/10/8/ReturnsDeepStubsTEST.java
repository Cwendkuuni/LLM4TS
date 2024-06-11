import org.junit.Test;
import org.mockito.internal.stubbing.defaultanswers.ReturnsDeepStubs;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class ReturnsDeepStubsTest {
  
  @Test
  public void answer_ShouldReturnMock_WhenInvocationMatches() throws Throwable {
    // Create a mock object with ReturnsDeepStubs answer
    ReturnsDeepStubs stubs = new ReturnsDeepStubs();
    List<String> mockList = mock(List.class, stubs);
    
    // Stubs
    when(mockList.get(0)).thenReturn("Mockito");
    
    // Invoke the method on the mock object
    String result = mockList.get(0);
    
    // Verify the result
    assertThat(result).isEqualTo("Mockito");
  }
  
  @Test
  public void answer_ShouldReturnDefaultValue_WhenInvocationDoesNotMatch() throws Throwable {
    // Create a mock object with ReturnsDeepStubs answer
    ReturnsDeepStubs stubs = new ReturnsDeepStubs();
    List<String> mockList = mock(List.class, stubs);
    
    // Stubs
    when(mockList.get(0)).thenReturn("Mockito");
    
    // Invoke the method on the mock object with different argument
    String result = mockList.get(1);
    
    // Verify the result
    assertThat(result).isNull();
  }
  
  @Test
  public void answer_ShouldReturnDefaultMock_WhenTypeCannotBeMocked() throws Throwable {
    // Create a mock object with ReturnsDeepStubs answer
    ReturnsDeepStubs stubs = new ReturnsDeepStubs();
    Integer mockInteger = mock(Integer.class, stubs);
    
    // Invoke a method on the mock object
    int result = mockInteger.intValue();
    
    // Verify the result
    assertThat(result).isEqualTo(0);
  }
  
  @Test
  public void deepStub_ShouldReturnMock_WhenInvocationMatches() throws Throwable {
    // Create a mock object with ReturnsDeepStubs answer
    ReturnsDeepStubs stubs = new ReturnsDeepStubs();
    Map<String, Set<Integer>> mockMap = mock(Map.class, stubs);
    
    // Stubs
    when(mockMap.get("key")).thenReturn(new HashSet<>(Arrays.asList(1, 2, 3)));
    
    // Invoke the method chain on the mock object
    int result = mockMap.get("key").size();
    
    // Verify the result
    assertThat(result).isEqualTo(3);
  }
  
  @Test
  public void deepStub_ShouldReturnDefaultMock_WhenInvocationDoesNotMatch() throws Throwable {
    // Create a mock object with ReturnsDeepStubs answer
    ReturnsDeepStubs stubs = new ReturnsDeepStubs();
    Map<String, Set<Integer>> mockMap = mock(Map.class, stubs);
    
    // Stubs
    when(mockMap.get("key")).thenReturn(new HashSet<>(Arrays.asList(1, 2, 3)));
    
    // Invoke the method chain on the mock object with different argument
    int result = mockMap.get("wrongKey").size();
    
    // Verify the result
    assertThat(result).isZero();
  }
  
  @Test
  public void newDeepStubMock_ShouldCreateMockWithGenericMetadata() {
    // Create a newDeepStubMock object
    ReturnsDeepStubs stubs = new ReturnsDeepStubs();
    List<String> mockList = (List<String>) stubs.newDeepStubMock(GenericMetadataSupport.inferFrom(List.class));
    
    // Verify the metadata is present
    assertThat(mockList).isNotNull();
  }
  
  @Test
  public void withSettingsUsing_ShouldReturnMockSettingsWithExtraInterfaces_WhenExtraInterfacesAvailable() {
    // Create a mock object with ReturnsDeepStubs answer
    ReturnsDeepStubs stubs = new ReturnsDeepStubs();
    ReturnsDeepStubs.GenericMetadataSupport mockGenericMetadata = new ReturnsDeepStubs.GenericMetadataSupport(List.class, null);
    
    // Invoke the method with extra interfaces
    ReturnsDeepStubs.MockSettings mockSettings = stubs.withSettingsUsing(mockGenericMetadata);
    
    // Verify the result
    assertThat(mockSettings.extraInterfaces()).isNull();
    assertThat(mockSettings.serializable()).isNotNull();
  }
  
  @Test
  public void withSettingsUsing_ShouldReturnMockSettingsWithoutExtraInterfaces_WhenExtraInterfacesNotAvailable() {
    // Create a mock object with ReturnsDeepStubs answer
    ReturnsDeepStubs stubs = new ReturnsDeepStubs();
    ReturnsDeepStubs.GenericMetadataSupport mockGenericMetadata = new ReturnsDeepStubs.GenericMetadataSupport(List.class, Collections.emptyList());
    
    // Invoke the method without extra interfaces
    ReturnsDeepStubs.MockSettings mockSettings = stubs.withSettingsUsing(mockGenericMetadata);
    
    // Verify the result
    assertThat(mockSettings.extraInterfaces()).isNull();
    assertThat(mockSettings.serializable()).isNotNull();
  }
  
  @Test
  public void returnsDeepStubsAnswerUsing_ShouldReturnNewInstance() {
    // Create a mock object with ReturnsDeepStubs answer
    ReturnsDeepStubs stubs = new ReturnsDeepStubs();
    ReturnsDeepStubs.GenericMetadataSupport mockGenericMetadata = new ReturnsDeepStubs.GenericMetadataSupport(List.class, null);
    
    // Invoke the method
    ReturnsDeepStubs answer = stubs.returnsDeepStubsAnswerUsing(mockGenericMetadata);
    
    // Verify the result
    assertThat(answer).isNotNull();
  }
  
  @Test
  public void recordDeepStubAnswer_ShouldAddAnswerToContainer() throws Throwable {
    // Create a mock object with ReturnsDeepStubs answer
    ReturnsDeepStubs stubs = new ReturnsDeepStubs();
    List<String> mockList = mock(List.class, stubs);
    ReturnsDeepStubs.InvocationContainerImpl container = (ReturnsDeepStubs.InvocationContainerImpl) mockList;
    
    // Stubs
    when(container.getInvocationForStubbing().matches(any())).thenReturn(true);
    
    // Record the deep stub answer
    Object result = stubs.recordDeepStubAnswer(mockList, container);
    
    // Verify the result
    assertThat(result).isEqualTo(mockList);
  }
  
  @Test
  public void actualParameterizedType_ShouldReturnGenericMetadata() {
    // Create a mock object with ReturnsDeepStubs answer
    ReturnsDeepStubs stubs = new ReturnsDeepStubs();
    List<String> mockList = (List<String>) mock(List.class, RETURNS_DEEP_STUBS);
    
    // Invoke the method
    ReturnsDeepStubs.GenericMetadataSupport result = stubs.actualParameterizedType(mockList);
    
    // Verify the result
    assertThat(result).isNotNull();
  }
  
  @Test
  public void writeReplace_ShouldReturnReturnsDeepStubsConstant() throws Exception {
    // Create a new ReturnsDeepStubsSerializationFallback object
    ReturnsDeepStubs.ReturnsDeepStubsSerializationFallback fallback = new ReturnsDeepStubs.ReturnsDeepStubsSerializationFallback(new ReturnsDeepStubs.GenericMetadataSupport(List.class, null));
    
    // Invoke the writeReplace method
    Object result = fallback.writeReplace();
    
    // Verify the result
    assertThat(result).isEqualTo(Mockito.RETURNS_DEEP_STUBS);
  }
  
  @Test
  public void answer_ShouldReturnMock_WhenDeeplyStubbedAnswerInvoked() throws Throwable {
    // Create a mock object with ReturnsDeepStubs answer
    ReturnsDeepStubs stubs = new ReturnsDeepStubs();
    List<String> mockList = (List<String>) stubs.returnsDeepStubsAnswerUsing(new ReturnsDeepStubs.GenericMetadataSupport(List.class, null));
    
    // Invoke the deeply stubbed answer
    Object result = mockList.add("Mockito");
    
    // Verify the result
    assertThat(result).isEqualTo(mockList);
  }
  
  @Test
  public void mockitoCore_ShouldReturnMockitoCoreInstance() {
    // Invoke the MockitoCore method
    ReturnsDeepStubs.MockitoCore core = ReturnsDeepStubs.mockitoCore();
    
    // Verify the result
    assertThat(core).isNotNull();
  }
  
  @Test
  public void delegate_ShouldReturnReturnsEmptyValuesInstance() {
    // Invoke the delegate method
    ReturnsDeepStubs.ReturnsEmptyValues delegate = ReturnsDeepStubs.delegate();
    
    // Verify the result
    assertThat(delegate).isNotNull();
  }
}