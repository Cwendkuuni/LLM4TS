import org.junit.*;
import static org.mockito.Mockito.*;

public class MockUtilTest {

   // CreateMock Test Case
   @Test
   public void testCreateMock() {
      MockUtil mockUtil = new MockUtil();
      
      // Test for basic functionality
      SomeClass mock = mockUtil.createMock(SomeClass.class, new MockSettingsImpl());
      assertNotNull(mock);
   }

   // ResetMock Test Case
   @Test
   public void testResetMock() {
      MockUtil mockUtil = new MockUtil();
      
      // Test for resetting a mock
      SomeClass mock = mock(SomeClass.class);
      mockUtil.resetMock(mock);
      // Verify that the mock has been reset
      verify(mock, times(1)).reset();
   }

   // GetMockHandler Test Case
   @Test
   public void testGetMockHandler() {
      MockUtil mockUtil = new MockUtil();
      
      // Test for getting the mock handler of a mock
      SomeClass mock = mock(SomeClass.class);
      MockHandlerInterface<SomeClass> handler = mockUtil.getMockHandler(mock);
      assertNotNull(handler);
   }

   // IsMock Test Case
   @Test
   public void testIsMock() {
      MockUtil mockUtil = new MockUtil();
      
      // Test for checking if an object is a mock
      SomeClass mock = mock(SomeClass.class);
      assertTrue(mockUtil.isMock(mock));
   }

   // GetMockName Test Case
   @Test
   public void testGetMockName() {
      MockUtil mockUtil = new MockUtil();
      
      // Test for getting the mock name of a mock
      SomeClass mock = mock(SomeClass.class);
      MockName mockName = mockUtil.getMockName(mock);
      assertNotNull(mockName);
   }
}