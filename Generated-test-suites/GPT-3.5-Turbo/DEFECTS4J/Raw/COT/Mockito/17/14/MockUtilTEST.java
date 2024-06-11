
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.MockUtil;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class MockUtilTest {
  
  private MockUtil mockUtil;
  
  @Before
  public void setup() {
    mockUtil = new MockUtil();
  }
  
  @Test
  public void testCreateMock() {
    ClassA mock = mockUtil.createMock(ClassA.class, new MockSettingsImpl());
    assertNotNull(mock);
  }
  
  @Test
  public void testResetMock() {
    ClassA mock = mockUtil.createMock(ClassA.class, new MockSettingsImpl());
    mockUtil.resetMock(mock);
    // test resetted mock
  }
  
  @Test(expected = NotAMockException.class)
  public void testGetMockHandlerNull() {
    mockUtil.getMockHandler(null);
  }
  
  @Test(expected = NotAMockException.class)
  public void testGetMockHandlerNonMock() {
    mockUtil.getMockHandler(new ClassA());
  }
  
  @Test
  public void testIsMock() {
    ClassA mock = mockUtil.createMock(ClassA.class, new MockSettingsImpl());
    assertTrue(mockUtil.isMock(mock));
  }
  
  @Test
  public void testGetMockName() {
    ClassA mock = mockUtil.createMock(ClassA.class, new MockSettingsImpl());
    MockName mockName = mockUtil.getMockName(mock);
    assertNotNull(mockName);
  }
}
