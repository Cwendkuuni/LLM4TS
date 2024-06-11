import org.mockito.Mockito;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.exceptions.ExceptionIncludingMockitoWarnings;
import org.mockito.internal.util.MockCreationValidator;
import org.mockito.internal.util.reflection.LenientCopyTool;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.quality.Strictness;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.handler.MethodInvocationReport;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class MockUtilTest {

    private MockUtil mockUtil;

    @Before
    public void setUp() {
        mockUtil = new MockUtil();
    }
    
    @Test
    public void testCreateMock_ValidClassAndSettings_CreatesMock() {
        // Arrange
        MockCreationValidator creationValidatorMock = Mockito.mock(MockCreationValidator.class);
        MockSettingsImpl settingsMock = Mockito.mock(MockSettingsImpl.class);
        Class<?> classToMock = List.class;
        
        when(settingsMock.getExtraInterfaces()).thenReturn(new Class<?>[] {Serializable.class});
        when(settingsMock.getSpiedInstance()).thenReturn(null);
        
        // Act
        List<?> mock = mockUtil.createMock(classToMock, settingsMock);
        
        // Assert
        assertNotNull(mock);
        assertTrue(mock instanceof List);
        
        verify(creationValidatorMock).validateType(classToMock);
        verify(creationValidatorMock).validateExtraInterfaces(classToMock, settingsMock.getExtraInterfaces());
        verify(creationValidatorMock).validateMockedType(classToMock, settingsMock.getSpiedInstance());
    }
    
    @Test
    public void testCreateMock_InvalidClass_ThrowsException() {
        // Arrange
        MockCreationValidator creationValidatorMock = Mockito.mock(MockCreationValidator.class);
        MockSettingsImpl settingsMock = Mockito.mock(MockSettingsImpl.class);
        Class<?> classToMock = String.class;
        
        when(settingsMock.getExtraInterfaces()).thenReturn(null);
        when(settingsMock.getSpiedInstance()).thenReturn(null);
        doThrow(NotAMockException.class).when(creationValidatorMock).validateType(classToMock);
        
        // Act
        try {
            mockUtil.createMock(classToMock, settingsMock);
            fail("Should throw NotAMockException");
        } catch (NotAMockException e) {
            // Assert
            assertTrue(e.getMessage().contains("Argument should be a mock, but is: java.lang.String"));
        }
        
        verify(creationValidatorMock).validateType(classToMock);
        verifyZeroInteractions(creationValidatorMock);
    }

    @Test
    public void testResetMock_ValidMock_ResetsMock() {
        // Arrange
        List<?> mock = Mockito.mock(List.class);
        mockUtil.resetMock(mock);
        
        // Act
        mock.add("test");
        
        // Assert
        assertTrue(mock.isEmpty());
    }
    
    @Test
    public void testResetMock_NullMock_ThrowsException() {
        // Arrange
        List<?> mock = null;
        
        // Act & Assert
        try {
            mockUtil.resetMock(mock);
            fail("Should throw NotAMockException");
        } catch (NotAMockException e) {
            // Assert
            assertTrue(e.getMessage().contains("Argument should be a mock, but is null!"));
        }
    }
    
    @Test
    public void testGetMethodInterceptorFilter_ValidMock_ReturnsMethodInterceptorFilter() {
        // Arrange
        List<?> mock = Mockito.mock(List.class);
        
        // Act
        MethodInterceptorFilter filter = mockUtil.getInterceptor(mock);
        
        // Assert
        assertNotNull(filter);
    }
    
    @Test
    public void testGetMethodInterceptorFilter_NotMock_ReturnsNull() {
        // Arrange
        List<?> notMock = new ArrayList<>();
        
        // Act
        MethodInterceptorFilter filter = mockUtil.getInterceptor(notMock);
        
        // Assert
        assertNull(filter);
    }
    
    @Test
    public void testIsMock_ValidMock_ReturnsTrue() {
        // Arrange
        List<?> mock = Mockito.mock(List.class);
        
        // Act
        boolean isMock = mockUtil.isMock(mock);
        
        // Assert
        assertTrue(isMock);
    }
    
    @Test
    public void testIsMock_NotMock_ReturnsFalse() {
        // Arrange
        List<?> notMock = new ArrayList<>();
        
        // Act
        boolean isMock = mockUtil.isMock(notMock);
        
        // Assert
        assertFalse(isMock);
    }
    
    @Test
    public void testGetMockName_ValidMock_ReturnsMockName() {
        // Arrange
        List<?> mock = Mockito.mock(List.class);
        
        // Act
        MockName mockName = mockUtil.getMockName(mock);
        
        // Assert
        assertNotNull(mockName);
    }
    
    @Test
    public void testGetMockName_NullMock_ThrowsException() {
        // Arrange
        List<?> mock = null;
        
        // Act & Assert
        try {
            mockUtil.getMockName(mock);
            fail("Should throw NotAMockException");
        } catch (NotAMockException e) {
            // Assert
            assertTrue(e.getMessage().contains("Argument should be a mock, but is null!"));
        }
    }
    
    @Test
    public void testIsMock_ValidMock_ReturnsTrue() {
        // Arrange
        List<?> mock = Mockito.mock(List.class);
        
        // Act
        boolean isMock = mockUtil.isMock(mock);
        
        // Assert
        assertTrue(isMock);
    }
    
    @Test
    public void testIsMock_NotMock_ReturnsFalse() {
        // Arrange
        List<?> notMock = new ArrayList<>();
        
        // Act
        boolean isMock = mockUtil.isMock(notMock);
        
        // Assert
        assertFalse(isMock);
    }
    
    @Test
    public void testCreateMock_ValidClassAndSettingsWithSpiedInstance_CreatesMockAndCopyValues() {
        // Arrange
        MockCreationValidator creationValidatorMock = Mockito.mock(MockCreationValidator.class);
        MockSettingsImpl settingsMock = Mockito.mock(MockSettingsImpl.class);
        Class<?> classToMock = List.class;
        List<String> spiedInstance = new ArrayList<>();
        spiedInstance.add("test");
        
        when(settingsMock.getExtraInterfaces()).thenReturn(new Class<?>[] {Serializable.class});
        when(settingsMock.getSpiedInstance()).thenReturn(spiedInstance);
        
        // Act
        List<?> mock = mockUtil.createMock(classToMock, settingsMock);
        
        // Assert
        assertNotNull(mock);
        assertTrue(mock instanceof List);
        assertEquals(1, mock.size());
        
        verify(creationValidatorMock).validateType(classToMock);
        verify(creationValidatorMock).validateExtraInterfaces(classToMock, settingsMock.getExtraInterfaces());
        verify(creationValidatorMock).validateMockedType(classToMock, settingsMock.getSpiedInstance());
    }
    
    @Test
    public void testCreateMock_ValidClassAndSettingsWithSerializable_CreatesMockAndImplementsSerializable() {
        // Arrange
        MockCreationValidator creationValidatorMock = Mockito.mock(MockCreationValidator.class);
        MockSettingsImpl settingsMock = Mockito.mock(MockSettingsImpl.class);
        Class<?> classToMock = List.class;
        
        when(settingsMock.getExtraInterfaces()).thenReturn(null);
        when(settingsMock.getSpiedInstance()).thenReturn(null);
        when(settingsMock.isSerializable()).thenReturn(true);
        
        // Act
        List<?> mock = mockUtil.createMock(classToMock, settingsMock);
        
        // Assert
        assertNotNull(mock);
        assertTrue(mock instanceof List);
        assertTrue(mock instanceof Serializable);
        
        verify(creationValidatorMock).validateType(classToMock);
        verify(creationValidatorMock).validateExtraInterfaces(classToMock, settingsMock.getExtraInterfaces());
        verify(creationValidatorMock).validateMockedType(classToMock, settingsMock.getSpiedInstance());
    }
    
}