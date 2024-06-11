
import org.junit.Test;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.MockHandler;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.util.MockCreationValidator;
import org.mockito.internal.util.MockName;
import org.mockito.internal.util.reflection.LenientCopyTool;

import java.io.Serializable;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

public class MockUtilTest {

    @Test
    public void createMockTest() {
        // Given
        MockCreationValidator validator = mock(MockCreationValidator.class);
        MockUtil mockUtil = new MockUtil(validator);
        Class<TestInterface> classToMock = TestInterface.class;
        MockSettingsImpl settings = mock(MockSettingsImpl.class);
        
        // When
        TestInterface mock = mockUtil.createMock(classToMock, settings);
        
        // Then
        assertNotNull(mock);
        verify(validator).validateType(classToMock);
        verify(validator).validateExtraInterfaces(classToMock, settings.getExtraInterfaces());
        verify(validator).validateMockedType(classToMock, settings.getSpiedInstance());
        verify(settings).initiateMockName(classToMock);
        verify(settings).getExtraInterfaces();
        verify(settings).isSerializable();
        verify(settings).getSpiedInstance();
        verify(settings).getMockName();
        verify(settings).getMockedType();
    }

    @Test
    public void resetMockTest() {
        // Given
        MockUtil mockUtil = new MockUtil();
        TestInterface mock = mock(TestInterface.class);
        
        // When
        mockUtil.resetMock(mock);
        
        // Then
        verify(mock).setCallback(eq(0), any());
    }

    @Test(expected = NotAMockException.class)
    public void getMockHandlerWithNullMockTest() {
        // Given
        MockUtil mockUtil = new MockUtil();
        
        // When
        mockUtil.getMockHandler(null);
        
        // Then
        // Expect NotAMockException
    }
    
    @Test(expected = NotAMockException.class)
    public void getMockHandlerWithNonMockitoMockTest() {
        // Given
        MockUtil mockUtil = new MockUtil();
        TestInterface mock = new TestInterfaceImpl();
        
        // When
        mockUtil.getMockHandler(mock);
        
        // Then
        // Expect NotAMockException
    }
    
    @Test
    public void isMockTest() {
        // Given
        MockUtil mockUtil = new MockUtil();
        TestInterface mock = mock(TestInterface.class);
        
        // When
        boolean result = mockUtil.isMock(mock);
        
        // Then
        assertTrue(result);
    }
    
    @Test
    public void isMockWithNullTest() {
        // Given
        MockUtil mockUtil = new MockUtil();
        
        // When
        boolean result = mockUtil.isMock(null);
        
        // Then
        assertFalse(result);
    }
    
    @Test
    public void getMockNameTest() {
        // Given
        MockUtil mockUtil = new MockUtil();
        TestInterface mock = mock(TestInterface.class);
        MockHandlerInterface mockHandler = mock(MockHandlerInterface.class);
        when(mockUtil.getMockHandler(mock)).thenReturn(mockHandler);
        MockName mockName = mock(MockName.class);
        when(mockHandler.getMockSettings().getMockName()).thenReturn(mockName);
        
        // When
        MockName result = mockUtil.getMockName(mock);
        
        // Then
        assertEquals(mockName, result);
    }
}

interface TestInterface {}

class TestInterfaceImpl implements TestInterface {}
