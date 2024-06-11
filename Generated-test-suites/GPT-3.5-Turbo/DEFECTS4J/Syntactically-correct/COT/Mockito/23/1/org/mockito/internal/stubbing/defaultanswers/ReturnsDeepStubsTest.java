
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReturnsDeepStubsTest {

  private ReturnsDeepStubs returnsDeepStubs;

  @Before
  public void setUp() {
    returnsDeepStubs = new ReturnsDeepStubs();
  }

  @Test
  public void testAnswer() throws Throwable {
    InvocationOnMock invocation = mock(InvocationOnMock.class);
    Object result = returnsDeepStubs.answer(invocation);
    assertNotNull(result);
  }

  @Test
  public void testCreateNewDeepStubMock() {
    Object mock = returnsDeepStubs.createNewDeepStubMock(null);
    assertNotNull(mock);
  }

  @Test
  public void testWithSettingsUsing() {
    GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
    MockSettings mockSettings = returnsDeepStubs.withSettingsUsing(returnTypeGenericMetadata);
    assertNotNull(mockSettings);
  }

  @Test
  public void testReturnsDeepStubsAnswerUsing() {
    GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
    ReturnsDeepStubs innerReturnsDeepStubs = returnsDeepStubs.returnsDeepStubsAnswerUsing(returnTypeGenericMetadata);
    assertNotNull(innerReturnsDeepStubs);
  }

  @Test
  public void testRecordDeepStubMock() throws Throwable {
    Object mock = new Object();
    InvocationContainerImpl container = mock(InvocationContainerImpl.class);
    Object result = returnsDeepStubs.recordDeepStubMock(mock, container);
    assertNotNull(result);
  }

  @Test
  public void testActualParameterizedType() {
    Object mock = new Object();
    GenericMetadataSupport genericType = returnsDeepStubs.actualParameterizedType(mock);
    assertNotNull(genericType);
  }

}
