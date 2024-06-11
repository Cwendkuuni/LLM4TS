import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;

public class MockUtilTest {

    private MockUtil mockUtil = new MockUtil();

    @Test
    public void testCreateMock() {
        // typical use case
        MyInterface mock = mockUtil.createMock(MyInterface.class, new MockSettingsImpl());
        assertNotNull(mock);
        
        // edge case: interfaces are null
        MyInterface mock1 = mockUtil.createMock(MyInterface.class, new MockSettingsImpl(null));
        assertNotNull(mock1);
        
        // edge case: spied instance is not null
        MyInterface spiedInstance = new MyInterfaceImpl();
        MyInterface mock2 = mockUtil.createMock(MyInterface.class, new MockSettingsImpl().spiedInstance(spiedInstance));
        assertNotNull(mock2);
    }

    @Test
    public void testResetMock() {
        MyInterface mock = mock(MyInterface.class);
        
        // typical use case
        mockUtil.resetMock(mock);
        assertNotNull(mock);
        
        // edge case: mock is null
        assertThrows(NotAMockException.class, () -> mockUtil.resetMock(null));
    }

    @Test
    public void testGetMockHandler() {
        MyInterface mock = mock(MyInterface.class);
        
        // typical use case
        MockHandlerInterface<MyInterface> mockHandler = mockUtil.getMockHandler(mock);
        assertNotNull(mockHandler);
        
        // edge case: mock is null
        assertThrows(NotAMockException.class, () -> mockUtil.getMockHandler(null));
        
        // edge case: mock is not a Mockito mock
        assertThrows(NotAMockException.class, () -> mockUtil.getMockHandler(new MyInterfaceImpl()));
    }

    @Test
    public void testIsMock() {
        MyInterface mock = mock(MyInterface.class);
        
        // typical use case
        assertTrue(mockUtil.isMock(mock));
        
        // edge case: mock is null
        assertFalse(mockUtil.isMock(null));
        
        // edge case: mock is not a Mockito mock
        assertFalse(mockUtil.isMock(new MyInterfaceImpl()));
    }

    @Test
    public void testGetMockName() {
        MyInterface mock = mock(MyInterface.class);
        
        // typical use case
        MockName mockName = mockUtil.getMockName(mock);
        assertNotNull(mockName);
    }

    private interface MyInterface {
        // add methods here
    }

    private class MyInterfaceImpl implements MyInterface {
        // implementation here
    }

}