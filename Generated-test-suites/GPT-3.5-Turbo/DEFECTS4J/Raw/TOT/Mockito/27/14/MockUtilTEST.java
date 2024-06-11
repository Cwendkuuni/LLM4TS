import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.util.*;

public class MockUtilTest {

    private MockUtil mockUtil;

    @Before
    public void setUp() {
        mockUtil = new MockUtil();
    }
    
    @Test
    public void testCreateMock_typical() {
        // Typical use case
        ClassToMock mock = mockUtil.createMock(ClassToMock.class, new MockSettingsImpl());
        
        assertNotNull(mock);
    }
    
    @Test
    public void testCreateMock_extraInterfaces() {
        // Test with extra interfaces
        ClassToMock mock = mockUtil.createMock(ClassToMock.class, new MockSettingsImpl().extraInterfaces(Serializable.class));
        
        assertNotNull(mock);
        assertTrue(mock instanceof Serializable);
    }
    
    @Test
    public void testCreateMock_spiedInstance() {
        // Test with spied instance
        ClassToMock spiedInstance = new ClassToMock();
        ClassToMock mock = mockUtil.createMock(ClassToMock.class, new MockSettingsImpl().spiedInstance(spiedInstance));
        
        assertNotNull(mock);
        assertSame(spiedInstance, mock.getSpiedInstance());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateMock_invalidType() {
        // Test with invalid type
        mockUtil.createMock(Void.class, new MockSettingsImpl());
    }
    
    @Test
    public void testResetMock_typical() {
        // Typical use case
        ClassToMock mock = mock(ClassToMock.class);
        when(mock.someMethod()).thenReturn("Mocked");
        
        mockUtil.resetMock(mock);
        
        assertEquals("Original", mock.someMethod());
    }
    
    @Test(expected = NotAMockException.class)
    public void testResetMock_notAMock() {
        // Test with not a mock
        ClassToMock notAMock = new ClassToMock();
        
        mockUtil.resetMock(notAMock);
    }
    
    @Test
    public void testGetMethodInterceptorFilter_typical() {
        // Typical use case
        ClassToMock mock = mock(ClassToMock.class);
        
        MethodInterceptorFilter filter = mockUtil.getMethodInterceptorFilter(mock);
        
        assertNotNull(filter);
    }
    
    @Test(expected = NotAMockException.class)
    public void testGetMethodInterceptorFilter_notAMock() {
        // Test with not a mock
        ClassToMock notAMock = new ClassToMock();
        
        mockUtil.getMethodInterceptorFilter(notAMock);
    }
    
    @Test
    public void testIsMock_true() {
        // Check if it is a mock (true)
        ClassToMock mock = mock(ClassToMock.class);
        
        assertTrue(mockUtil.isMock(mock));
    }
    
    @Test
    public void testIsMock_false() {
        // Check if it is a mock (false)
        ClassToMock notAMock = new ClassToMock();
        
        assertFalse(mockUtil.isMock(notAMock));
    }
    
    @Test
    public void testGetMockName_typical() {
        // Typical use case
        ClassToMock mock = mock(ClassToMock.class, withSettings().name("Mock"));
        
        MockName mockName = mockUtil.getMockName(mock);
        
        assertNotNull(mockName);
        assertEquals("Mock", mockName.toString());
    }
    
    @Test(expected = NotAMockException.class)
    public void testGetMockName_notAMock() {
        // Test with not a mock
        ClassToMock notAMock = new ClassToMock();
        
        mockUtil.getMockName(notAMock);
    }
    
    private static class ClassToMock {
        
        public ClassToMock() {
        }
        
        public ClassToMock getSpiedInstance() {
            return this;
        }
        
        public String someMethod() {
            return "Original";
        }
    }
}