import static org.junit.Assert.*;
import org.junit.Test;

public class MockUtilTest {
    
    // Test case for createMock method
    @Test
    public void testCreateMock() {
        MockUtil mockUtil = new MockUtil();

        // Typical use case
        MyInterface mock = mockUtil.createMock(MyInterface.class, new MockSettingsImpl());
        assertNotNull(mock);

        // Edge case - Serializable interface
        MyInterface serializableMock = mockUtil.createMock(MyInterface.class, new MockSettingsImpl().serializable());
        assertNotNull(serializableMock);

        // Edge case - Spied instance
        MyInterface spiedMock = new MyInterfaceImpl();
        MyInterface spiedMockInstance = mockUtil.createMock(MyInterface.class, new MockSettingsImpl().spiedInstance(spiedMock));
        assertNotNull(spiedMockInstance);
    }

    // Test case for resetMock method
    @Test
    public void testResetMock() {
        MockUtil mockUtil = new MockUtil();
        
        MyInterface mock = mockUtil.createMock(MyInterface.class, new MockSettingsImpl());
        
        // Typical use case
        mockUtil.resetMock(mock);
        // Reset mock logic should be tested in more detail
    }

    // Test case for getMockHandler method
    @Test
    public void testGetMockHandler() {
        MockUtil mockUtil = new MockUtil();
        
        MyInterface mock = mockUtil.createMock(MyInterface.class, new MockSettingsImpl());

        // Typical use case
        MockHandlerInterface<MyInterface> mockHandler = mockUtil.getMockHandler(mock);
        assertNotNull(mockHandler);
        
        // Error scenario - Null mock
        assertThrows(NotAMockException.class, () -> mockUtil.getMockHandler(null));
        
        // Error scenario - Invalid mock
        assertThrows(NotAMockException.class, () -> mockUtil.getMockHandler(new MyInterfaceImpl()));
    }

    // Test case for isMock method
    @Test
    public void testIsMock() {
        MockUtil mockUtil = new MockUtil();
        
        MyInterface mock = mockUtil.createMock(MyInterface.class, new MockSettingsImpl());

        // Typical use case
        assertTrue(mockUtil.isMock(mock));
        
        // Error scenario - Null mock
        assertFalse(mockUtil.isMock(null));

        // Error scenario - Invalid mock
        assertFalse(mockUtil.isMock(new MyInterfaceImpl()));
    }

    // Test case for getMockName method
    @Test
    public void testGetMockName() {
        MockUtil mockUtil = new MockUtil();
        
        MyInterface mock = mockUtil.createMock(MyInterface.class, new MockSettingsImpl());

        // Typical use case
        MockName mockName = mockUtil.getMockName(mock);
        assertNotNull(mockName);
    }
}