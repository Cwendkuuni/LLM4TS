import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.creation.MockSettingsImpl;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MockUtilTest {

    private MockUtil mockUtil;
    
    @Before
    public void setUp() {
        mockUtil = new MockUtil();
    }
    
    @Test
    public void testCreateMock_validClassAndSettings_mockCreatedSuccessfully() {
        // Arrange
        Class<Object> classToMock = Object.class;
        MockSettingsImpl settings = new MockSettingsImpl();
        
        // Act
        Object mock = mockUtil.createMock(classToMock, settings);
        
        // Assert
        assertNotNull(mock);
        assertTrue(classToMock.isInstance(mock));
        assertTrue(mockUtil.isMock(mock));
    }
    
    @Test
    public void testCreateMock_typeValidationFailure_throwIllegalArgumentException() {
        // Arrange
        Class<Object> classToMock = null;
        MockSettingsImpl settings = new MockSettingsImpl();
        
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            mockUtil.createMock(classToMock, settings);
        });
    }
    
    @Test
    public void testCreateMock_extraInterfacesValidationFailure_throwIllegalArgumentException() {
        // Arrange
        Class<Object> classToMock = Object.class;
        Class<?>[] extraInterfaces = { Serializable.class };
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.extraInterfaces(extraInterfaces);
        
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            mockUtil.createMock(classToMock, settings);
        });
    }
    
    @Test
    public void testCreateMock_mockedTypeValidationFailure_throwIllegalArgumentException() {
        // Arrange
        Class<Object> classToMock = Object.class;
        Object spiedInstance = new Object();
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.spiedInstance(spiedInstance);
        
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            mockUtil.createMock(classToMock, settings);
        });
    }
    
    @Test
    public void testCreateMock_serializableSettings_serializeMockSuccessfully() {
        // Arrange
        Class<Object> classToMock = Object.class;
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.serializable(true);
        
        // Act
        Object mock = mockUtil.createMock(classToMock, settings);
        
        // Assert
        assertTrue(mock instanceof Serializable);
    }
    
    @Test
    public void testResetMock_mockHandlerReset_mockResetSuccessfully() {
        // Arrange
        Object mock = mock(Object.class);
        
        // Act
        mockUtil.resetMock(mock);
        
        // Assert
        MockHandlerInterface<Object> mockHandler = mockUtil.getMockHandler(mock);
        assertNotNull(mockHandler);
        assertFalse(mockHandler.isInitialized());
    }
    
    @Test
    public void testResetMock_invalidMock_throwNotAMockException() {
        // Arrange
        Object notAMock = new Object();
        
        // Act & Assert
        assertThrows(NotAMockException.class, () -> {
            mockUtil.resetMock(notAMock);
        });
    }
    
    @Test
    public void testGetMockHandler_mockIsMock_returnMockHandler() {
        // Arrange
        Object mock = mock(Object.class);
        
        // Act
        MockHandlerInterface<Object> mockHandler = mockUtil.getMockHandler(mock);
        
        // Assert
        assertNotNull(mockHandler);
    }
    
    @Test
    public void testGetMockHandler_nonMockObject_throwNotAMockException() {
        // Arrange
        Object notAMock = new Object();
        
        // Act & Assert
        assertThrows(NotAMockException.class, () -> {
            mockUtil.getMockHandler(notAMock);
        });
    }
    
    @Test
    public void testIsMock_mockIsMock_returnTrue() {
        // Arrange
        Object mock = mock(Object.class);
        
        // Act
        boolean isMock = mockUtil.isMock(mock);
        
        // Assert
        assertTrue(isMock);
    }
    
    @Test
    public void testIsMock_nonMockObject_returnFalse() {
        // Arrange
        Object notAMock = new Object();
        
        // Act
        boolean isMock = mockUtil.isMock(notAMock);
        
        // Assert
        assertFalse(isMock);
    }
    
    @Test
    public void testGetInterceptor_mockIsMock_returnInterceptor() {
        // Arrange
        Object mock = mock(Object.class);
        
        // Act
        MethodInterceptorFilter interceptor = mockUtil.getInterceptor(mock);
        
        // Assert
        assertNotNull(interceptor);
    }
    
    @Test
    public void testGetInterceptor_nonMockObject_returnNull() {
        // Arrange
        Object notAMock = new Object();
        
        // Act
        MethodInterceptorFilter interceptor = mockUtil.getInterceptor(notAMock);
        
        // Assert
        assertNull(interceptor);
    }
    
    @Test
    public void testGetMockName_mockWithMockHandler_returnMockName() {
        // Arrange
        Object mock = mock(Object.class);
        
        // Act
        MockName mockName = mockUtil.getMockName(mock);
        
        // Assert
        assertNotNull(mockName);
    }
    
    @Test
    public void testGetMockName_nonMockObject_throwNotAMockException() {
        // Arrange
        Object notAMock = new Object();
        
        // Act & Assert
        assertThrows(NotAMockException.class, () -> {
            mockUtil.getMockName(notAMock);
        });
    }
}