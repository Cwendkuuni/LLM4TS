import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

public class MockUtilTest {

    private MockUtil mockUtil;

    @Before
    public void setUp() {
        mockUtil = new MockUtil();
    }

    @Test
    public void testCreateMock() {
        // Prepare
        Class<?> classToMock = MyClass.class;
        MockSettingsImpl settings = new MockSettingsImpl();
        
        // Execute
        MyClass mock = mockUtil.createMock(classToMock, settings);
        
        // Verify
        assertNotNull(mock);
    }

    @Test
    public void testResetMock() {
        // Prepare
        MyClass mock = Mockito.mock(MyClass.class);
        
        // Execute
        mockUtil.resetMock(mock);
        
        // Verify
        verify(mock).setCallback(eq(0), any(MethodInterceptorFilter.class));
    }

    @Test
    public void testGetMockHandler() {
        // Prepare
        MyClass mock = Mockito.mock(MyClass.class);
        
        // Execute
        MockHandlerInterface<MyClass> handler = mockUtil.getMockHandler(mock);
        
        // Verify
        assertNotNull(handler);
    }

    @Test
    public void testIsMock() {
        // Prepare
        MyClass mock = Mockito.mock(MyClass.class);
        
        // Execute
        boolean isMock = mockUtil.isMock(mock);
        
        // Verify
        assertTrue(isMock);
    }

    @Test
    public void testGetMockName() {
        // Prepare
        MyClass mock = Mockito.mock(MyClass.class);
        
        // Execute
        MockName mockName = mockUtil.getMockName(mock);
        
        // Verify
        assertNotNull(mockName);
    }
}

class MyClass {
    // Test class
}