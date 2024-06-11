
import org.junit.Test;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.util.MockCreationValidator;
import org.mockito.internal.MockHandler;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.creation.MethodInterceptorFilter;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.util.reflection.LenientCopyTool;

import java.io.Serializable;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MockUtilTest {

    @Test
    public void testCreateMock() {
        // Given
        MockCreationValidator creationValidator = mock(MockCreationValidator.class);
        MockUtil mockUtil = new MockUtil(creationValidator);
        Class<T> classToMock = T.class;
        MockSettingsImpl settings = new MockSettingsImpl();
        
        // When
        T mock = mockUtil.createMock(classToMock, settings);
        
        // Then
        assertNotNull(mock);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandler_Null() {
        // Given
        MockUtil mockUtil = new MockUtil();
        
        // When
        mockUtil.getMockHandler(null);
        
        // Then
        // Expecting NotAMockException
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandler_NotAMock() {
        // Given
        MockUtil mockUtil = new MockUtil();
        Object notAMock = new Object();
        
        // When
        mockUtil.getMockHandler(notAMock);
        
        // Then
        // Expecting NotAMockException
    }

    @Test
    public void testGetMockHandler_MockitoMock() {
        // Given
        MockUtil mockUtil = new MockUtil();
        Object mock = mock(Object.class);
        
        // When
        MockHandlerInterface<Object> handler = mockUtil.getMockHandler(mock);
        
        // Then
        assertNotNull(handler);
    }

    @Test
    public void testIsMock_True() {
        // Given
        MockUtil mockUtil = new MockUtil();
        Object mock = mock(Object.class);
        
        // When
        boolean isMock = mockUtil.isMock(mock);
        
        // Then
        assertTrue(isMock);
    }

    @Test
    public void testIsMock_False() {
        // Given
        MockUtil mockUtil = new MockUtil();
        Object notAMock = new Object();
        
        // When
        boolean isMock = mockUtil.isMock(notAMock);
        
        // Then
        assertFalse(isMock);
    }

    @Test
    public void testResetMock() {
        // Given
        MockUtil mockUtil = new MockUtil();
        T mock = mock(T.class);
        
        // When
        mockUtil.resetMock(mock);
        
        // Then
        verify(mock).setCallback(eq(0), any(MethodInterceptorFilter.class));
    }

    @Test
    public void testGetMockName() {
        // Given
        MockUtil mockUtil = new MockUtil();
        T mock = mock(T.class);
        
        // When
        MockName mockName = mockUtil.getMockName(mock);
        
        // Then
        assertNotNull(mockName);
    }
}
