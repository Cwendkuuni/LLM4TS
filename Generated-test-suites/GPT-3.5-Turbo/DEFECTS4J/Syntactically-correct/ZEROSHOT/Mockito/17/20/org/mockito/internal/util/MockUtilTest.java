import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.mockito.cglib.proxy.Factory;

public class MockUtilTest {

    private MockUtil mockUtil;
    
    @Before
    public void setUp() {
        mockUtil = new MockUtil();
    }
    
    @Test
    public void testCreateMock_ValidClass_ValidSettings_ReturnsMock() {
        // Arrange
        Class<?> classToMock = Object.class;
        MockSettingsImpl settings = Mockito.mock(MockSettingsImpl.class);
        
        // Act
        Object result = mockUtil.createMock(classToMock, settings);
        
        // Assert
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof Factory);
    }
    
    @Test(expected = NotAMockException.class)
    public void testCreateMock_InvalidClass_ThrowsNotAMockException() {
        // Arrange
        Class<?> classToMock = null;
        MockSettingsImpl settings = Mockito.mock(MockSettingsImpl.class);
        
        // Act
        mockUtil.createMock(classToMock, settings);
        
        // Assert - Exception expected
    }
    
    @Test
    public void testResetMock_ValidMock_ResetsMock() {
        // Arrange
        Object mock = Mockito.mock(Object.class);
        
        // Act
        mockUtil.resetMock(mock);
        
        // Assert
        MockHandlerInterface mockHandler = mockUtil.getMockHandler(mock);
        Assert.assertNotNull(mockHandler);
    }
    
    @Test(expected = NotAMockException.class)
    public void testResetMock_InvalidMock_ThrowsNotAMockException() {
        // Arrange
        Object mock = new Object();
        
        // Act
        mockUtil.resetMock(mock);
        
        // Assert - Exception expected
    }
    
    @Test
    public void testGetMockHandler_ValidMock_ReturnsMockHandler() {
        // Arrange
        Object mock = Mockito.mock(Object.class);
        
        // Act
        MockHandlerInterface result = mockUtil.getMockHandler(mock);
        
        // Assert
        Assert.assertNotNull(result);
    }
    
    @Test(expected = NotAMockException.class)
    public void testGetMockHandler_NullMock_ThrowsNotAMockException() {
        // Arrange
        Object mock = null;
        
        // Act
        mockUtil.getMockHandler(mock);
        
        // Assert - Exception expected
    }
    
    @Test(expected = NotAMockException.class)
    public void testGetMockHandler_NonMockObject_ThrowsNotAMockException() {
        // Arrange
        Object mock = new Object();
        
        // Act
        mockUtil.getMockHandler(mock);
        
        // Assert - Exception expected
    }
    
    @Test
    public void testIsMock_ValidMock_ReturnsTrue() {
        // Arrange
        Object mock = Mockito.mock(Object.class);
        
        // Act
        boolean result = mockUtil.isMock(mock);
        
        // Assert
        Assert.assertTrue(result);
    }
    
    @Test
    public void testIsMock_NullValue_ReturnsFalse() {
        // Arrange
        Object mock = null;
        
        // Act
        boolean result = mockUtil.isMock(mock);
        
        // Assert
        Assert.assertFalse(result);
    }
    
    @Test
    public void testIsMock_NonMockObject_ReturnsFalse() {
        // Arrange
        Object mock = new Object();
        
        // Act
        boolean result = mockUtil.isMock(mock);
        
        // Assert
        Assert.assertFalse(result);
    }
    
    @Test
    public void testGetMockName_ValidMock_ReturnsMockName() {
        // Arrange
        Object mock = Mockito.mock(Object.class);
        
        // Act
        MockName result = mockUtil.getMockName(mock);
        
        // Assert
        Assert.assertNotNull(result);
    }
    
    @Test(expected = NotAMockException.class)
    public void testGetMockName_NullMock_ThrowsNotAMockException() {
        // Arrange
        Object mock = null;
        
        // Act
        mockUtil.getMockName(mock);
        
        // Assert - Exception expected
    }
}