import org.junit.Test;
import org.mockito.Mockito;

public class MockitoCoreTest {

  @Test
  public void testMock() {
    Class<MockitoCore> mockitoCoreClass = Mockito.mock(MockitoCore.class);
    MockSettings mockSettings = new MockSettings();
    Mockito.verify(mockitoCoreClass).mock(Mockito.eq(MockitoCore.class), mockSettings);
  }
}