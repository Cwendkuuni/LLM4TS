import org.junit.Test;
import org.mockito.*;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.util.reflection.LenientCopyTool;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MockUtilTest {

    @Test
    public void testCreateMock_typicalCase() {
        MockCreationValidator mockCreationValidator = mock(MockCreationValidator.class);
        MockUtil mockUtil = new MockUtil(mockCreationValidator);

        Class<Object> classToMock = Object.class;
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.setExtraInterfaces(new Class[]{Serializable.class});

        Object mock = mockUtil.createMock(classToMock, settings);

        assertNotNull(mock);
        verify(mockCreationValidator).validateType(classToMock);
        verify(mockCreationValidator).validateExtraInterfaces(classToMock, settings.getExtraInterfaces());
        verify(mockCreationValidator).validateMockedType(classToMock, settings.getSpiedInstance());
        verify(settings).initiateMockName(classToMock);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandler_argumentIsNull() {
        MockUtil mockUtil = new MockUtil();

        mockUtil.getMockHandler(null);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandler_argumentIsNotMock() {
        MockUtil mockUtil = new MockUtil();

        Object nonMockObject = new Object();
        mockUtil.getMockHandler(nonMockObject);
    }

    @Test
    public void testGetMockHandler_argumentIsMock() {
        MockUtil mockUtil = new MockUtil();

        Factory mockFactory = mock(Factory.class);
        Callback mockCallback = mock(MethodInterceptorFilter.class);
        
        when(mockFactory.getCallback(0)).thenReturn(mockCallback);

        mockUtil.getMockHandler(mockFactory);

        verify(mockFactory).getCallback(0);
    }

    @Test
    public void testIsMock_argumentIsNull() {
        MockUtil mockUtil = new MockUtil();

        boolean isMock = mockUtil.isMock(null);

        assertFalse(isMock);
    }

    @Test
    public void testIsMock_argumentIsMock() {
        MockUtil mockUtil = new MockUtil();

        Factory mockFactory = mock(Factory.class);
        Callback mockCallback = mock(MethodInterceptorFilter.class);
        
        when(mockFactory.getCallback(0)).thenReturn(mockCallback);

        boolean isMock = mockUtil.isMock(mockFactory);

        assertTrue(isMock);
    }

    @Test
    public void testIsMock_argumentIsNotMock() {
        MockUtil mockUtil = new MockUtil();

        Object nonMockObject = new Object();

        boolean isMock = mockUtil.isMock(nonMockObject);

        assertFalse(isMock);
    }

    @Test
    public void testResetMock() {
        MockUtil mockUtil = new MockUtil();

        Factory mockFactory = mock(Factory.class);
        Callback mockCallback = mock(MethodInterceptorFilter.class);
        
        when(mockFactory.getCallback(0)).thenReturn(mockCallback);

        mockUtil.resetMock(mockFactory);

        verify(mockFactory).setCallback(0, mockCallback);
    }

    @Test
    public void testGetMockName() {
        MockUtil mockUtil = new MockUtil();

        MockHandlerInterface<Object> mockHandlerInterface = mock(MockHandlerInterface.class);
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        MockName mockName = mock(MockName.class);
        
        when(mockHandlerInterface.getMockSettings()).thenReturn(mockSettings);
        when(mockSettings.getMockName()).thenReturn(mockName);

        Object mock = mock(Object.class);
        when(mockUtil.getMockHandler(mock)).thenReturn(mockHandlerInterface);

        MockName retrievedMockName = mockUtil.getMockName(mock);

        assertSame(mockName, retrievedMockName);
    }
}