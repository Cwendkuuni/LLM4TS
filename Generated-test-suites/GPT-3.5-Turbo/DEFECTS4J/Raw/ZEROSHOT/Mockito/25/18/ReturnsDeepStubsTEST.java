import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mockito.*;

public class ReturnsDeepStubsTest {

  @Test
  public void testAnswer() throws Throwable {
    ReturnsDeepStubs stub = new ReturnsDeepStubs();
    InvocationOnMock invocation = mock(InvocationOnMock.class);
    Object result = stub.answer(invocation);
    // assertion here
  }

  @Test
  public void testGetMock() throws Throwable {
    ReturnsDeepStubs stub = new ReturnsDeepStubs();
    InvocationOnMock invocation = mock(InvocationOnMock.class);
    Object result = stub.getMock(invocation);
    // assertion here
  }

  @Test
  public void testRecordDeepStubMock() {
    ReturnsDeepStubs stub = new ReturnsDeepStubs();
    InvocationOnMock invocation = mock(InvocationOnMock.class);
    InvocationContainerImpl container = new InvocationContainerImpl();
    Object result = stub.recordDeepStubMock(invocation, container);
    // assertion here
  }
}