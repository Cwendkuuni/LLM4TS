
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.MockHandler;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.creation.MethodInterceptorFilter;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.util.reflection.LenientCopyTool;

public class MockUtilTest {

    private MockUtil mockUtil;
    
    @Before
    public void setup() {
        mockUtil = new MockUtil();
    }
    
    @Test
    public void testCreateMock() {
        // Given
        Class mockClass = MyClass.class;
        MockSettingsImpl settings = new MockSettingsImpl();
        
        // When
        Object mock = mockUtil.createMock(mockClass, settings);
        
        // Then
        assertNotNull(mock);
        assertTrue(mockClass.isInstance(mock));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateMock_InvalidType() {
        // Given
        Class mockClass = String.class;
        MockSettingsImpl settings = new MockSettingsImpl();
        
        // When
        Object mock = mockUtil.createMock(mockClass, settings);
        
        // Then
        // Expecting IllegalArgumentException
    }
    
    @Test
    public void testResetMock() {
        // Given
        MyClass mock = mock(MyClass.class);
        
        // When
        mockUtil.resetMock(mock);
        
        // Then
        verify(mock).setCallback(eq(0), any(MethodInterceptorFilter.class));
    }
    
    @Test(expected = NotAMockException.class)
    public void testResetMock_NotAMock() {
        // Given
        MyClass nonMock = new MyClass();
        
        // When
        mockUtil.resetMock(nonMock);
        
        // Then
        // Expecting NotAMockException
    }
    
    @Test
    public void testGetMockHandler() {
        // Given
        MyClass mock = mock(MyClass.class);
        
        // When
        MockHandlerInterface<MyClass> mockHandler = mockUtil.getMockHandler(mock);
        
        // Then
        assertNotNull(mockHandler);
        assertEquals(mock, mockHandler.getHandler());
    }
    
    @Test(expected = NotAMockException.class)
    public void testGetMockHandler_NotAMock() {
        // Given
        MyClass nonMock = new MyClass();
        
        // When
        mockUtil.getMockHandler(nonMock);
        
        // Then
        // Expecting NotAMockException
    }
    
    @Test
    public void testIsMock() {
        // Given
        MyClass mock = mock(MyClass.class);
        
        // When
        boolean isMock = mockUtil.isMock(mock);
        
        // Then
        assertTrue(isMock);
    }
    
    @Test
    public void testIsMock_NotAMock() {
        // Given
        MyClass nonMock = new MyClass();
        
        // When
        boolean isMock = mockUtil.isMock(nonMock);
        
        // Then
        assertFalse(isMock);
    }
    
    @Test
    public void testGetMockName() {
        // Given
        MyClass mock = mock(MyClass.class);
        
        // When
        MockName mockName = mockUtil.getMockName(mock);
        
        // Then
        assertNotNull(mockName);
        assertNull(mockName.get());
    }
    
    private static class MyClass {
        // Test class
    }
}
