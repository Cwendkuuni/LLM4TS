import static org.junit.Assert.*;
import org.junit.Test;

public class MockUtilTest {
  
  // Test createMock method
  @Test
  public void testCreateMock() {
    MockUtil mockUtil = new MockUtil();
    MockSettingsImpl settings = new MockSettingsImpl();
    
    // Test creation of mock object
    MyObject mockObj = mockUtil.createMock(MyObject.class, settings);
    
    assertNotNull(mockObj);
  }
  
  // Test resetMock method
  @Test
  public void testResetMock() {
    MockUtil mockUtil = new MockUtil();
    MyObject mockObj = mock(MyObject.class);
    
    // Test resetting the mock object
    mockUtil.resetMock(mockObj);
    
    // Verify that the mock object has been reset
    verify(mockObj).reset();
  }
  
  // Test getMockHandler method
  @Test
  public void testGetMockHandler() {
    MockUtil mockUtil = new MockUtil();
    MyObject mockObj = mock(MyObject.class);
    
    // Test getting the mock handler
    MockHandlerInterface<MyObject> handler = mockUtil.getMockHandler(mockObj);
    
    assertNotNull(handler);
  }
  
  // Test isMock method
  @Test
  public void testIsMock() {
    MockUtil mockUtil = new MockUtil();
    MyObject mockObj = mock(MyObject.class);
    
    // Test checking if object is a mock
    boolean isMock = mockUtil.isMock(mockObj);
    
    assertTrue(isMock);
  }
  
  // Test getMockName method
  @Test
  public void testGetMockName() {
    MockUtil mockUtil = new MockUtil();
    MyObject mockObj = mock(MyObject.class);
    
    // Test getting the mock name
    MockName mockName = mockUtil.getMockName(mockObj);
    
    assertNotNull(mockName);
  }
}