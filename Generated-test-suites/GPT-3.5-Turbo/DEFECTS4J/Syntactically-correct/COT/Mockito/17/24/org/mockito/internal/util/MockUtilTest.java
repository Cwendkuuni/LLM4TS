
import static org.mockito.Mockito.*;
import org.junit.Test;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.util.MockUtil;

public class MockUtilTest {
    @Test
    public void testCreateMock() {
        MockUtil mockUtil = new MockUtil();
        // Test creating mock for a class
        MyClass mockClass = mockUtil.createMock(MyClass.class, new MockSettingsImpl());
        assertNotNull(mockClass);
        
        // Test creating mock for a class with extra interfaces
        Class<?>[] extraInterfaces = { MyInterface.class };
        AnotherClass mockClassWithInterfaces = mockUtil.createMock(AnotherClass.class, new MockSettingsImpl().extraInterfaces(extraInterfaces));
        assertNotNull(mockClassWithInterfaces);
        
        // Test creating mock for a spied instance
        MyClass spiedInstance = new MyClass();
        MyClass mockClassWithSpiedInstance = mockUtil.createMock(MyClass.class, new MockSettingsImpl().spiedInstance(spiedInstance));
        assertNotNull(mockClassWithSpiedInstance);
        
        // Test creating mock with initiateMockName
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.initiateMockName(MyClass.class);
        MyClass mockClassWithMockName = mockUtil.createMock(MyClass.class, settings);
        assertNotNull(mockClassWithMockName);
    }

    @Test
    public void testResetMock() {
        MockUtil mockUtil = new MockUtil();
        MyClass mock = mock(MyClass.class);
        
        // Test resetting mock
        mockUtil.resetMock(mock);
        
        // Verify that mock has been reset
        verify(mock, times(1)).getClass();
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandler_WhenArgumentIsNull() {
        MockUtil mockUtil = new MockUtil();
        mockUtil.getMockHandler(null);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandler_WhenArgumentIsNotMock() {
        MockUtil mockUtil = new MockUtil();
        Object nonMock = new Object();
        mockUtil.getMockHandler(nonMock);
    }

    @Test
    public void testIsMock() {
        MockUtil mockUtil = new MockUtil();
        MyClass mockClass = mock(MyClass.class);
        
        // Test isMock for a mock object
        assertTrue(mockUtil.isMock(mockClass));
        
        // Test isMock for a non-mock object
        assertFalse(mockUtil.isMock(new Object()));
    }

    @Test
    public void testGetMockName() {
        MockUtil mockUtil = new MockUtil();
        MyClass mockClass = mock(MyClass.class);
        
        // Test getting mock name
        MockName mockName = mockUtil.getMockName(mockClass);
        assertNotNull(mockName);
    }
}
