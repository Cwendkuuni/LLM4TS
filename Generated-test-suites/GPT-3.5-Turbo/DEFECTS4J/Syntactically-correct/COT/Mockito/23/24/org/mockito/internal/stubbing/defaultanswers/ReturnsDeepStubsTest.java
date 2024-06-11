
import static org.mockito.Mockito.*;
import org.mockito.internal.stubbing.defaultanswers.ReturnsDeepStubs;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.junit.Test;
import static org.junit.Assert.*;

public class ReturnsDeepStubsTest {

  @Test
  public void testAnswer() throws Throwable {
    ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
    InvocationOnMock invocation = mock(InvocationOnMock.class);
    Object result = returnsDeepStubs.answer(invocation);
    assertNull(result);
  }

  @Test
  public void testGetMock() throws Throwable {
    ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
    InvocationOnMock invocation = mock(InvocationOnMock.class);
    Object result = returnsDeepStubs.getMock(invocation, null);
    assertNotNull(result);
  }

  @Test
  public void testCreateNewDeepStubMock() {
    ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
    Object result = returnsDeepStubs.createNewDeepStubMock(null);
    assertNotNull(result);
  }

  @Test
  public void testWithSettingsUsing() {
    ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
    Object result = returnsDeepStubs.withSettingsUsing(null);
    assertNotNull(result);
  }

  @Test
  public void testReturnsDeepStubsAnswerUsing() {
    ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
    Object result = returnsDeepStubs.returnsDeepStubsAnswerUsing(null);
    assertNotNull(result);
  }

  @Test
  public void testRecordDeepStubMock() throws Throwable {
    ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
    Object mock = new Object();
    InvocationContainerImpl container = mock(InvocationContainerImpl.class);
    Object result = returnsDeepStubs.recordDeepStubMock(mock, container);
    assertEquals(mock, result);
  }

  @Test
  public void testActualParameterizedType() {
    ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
    Object mock = new Object();
    Object result = returnsDeepStubs.actualParameterizedType(mock);
    assertNotNull(result);
  }
}
