import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.mockito.cglib.proxy.Factory;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.util.MockHandler;
import org.mockito.internal.util.reflection.LenientCopyTool;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MockUtilTest {

    private MockHandlerInterface mockHandler;
    private MockUtil mockUtil;

    @Before
    public void setUp() {
        mockHandler = mock(MockHandlerInterface.class);
        mockUtil = new MockUtil();
    }

    @Test
    public void testCreateMock_withValidArguments_shouldReturnMockObject() {
        ClassToMock classToMock = mock(ClassToMock.class);
        MockSettingsImpl settings = mock(MockSettingsImpl.class);
        
        when(settings.getExtraInterfaces()).thenReturn(new Class<?>[]{});
        when(settings.getSpiedInstance()).thenReturn(null);
        
        ClassImposterizer imposterizer = mock(ClassImposterizer.class);
        when(imposterizer.imposterise(any(), eq(classToMock), any())).thenReturn(classToMock);
        
        MockName mockName = mock(MockName.class);
        when(mockName.toString()).thenReturn("mockName");

        assertEquals(classToMock, mockUtil.createMock(classToMock.getClass(), settings));
    }

    @Test(expected = NotAMockException.class)
    public void testCreateMock_withNullClassToMock_shouldThrowNotAMockException() {
        MockSettingsImpl settings = mock(MockSettingsImpl.class);
        
        when(settings.getExtraInterfaces()).thenReturn(new Class<?>[]{});
        when(settings.getSpiedInstance()).thenReturn(null);

        mockUtil.createMock(null, settings);
    }

    @Test
    public void testResetMock_withValidMock_shouldResetMock() {
        ClassToMock mock = mock(ClassToMock.class);
        Factory factoryMock = mock(Factory.class);
        MethodInterceptorFilter newFilterMock = mock(MethodInterceptorFilter.class);
        
        when(mock.getCallback(0)).thenReturn(newFilterMock);
        doNothing().when(factoryMock).setCallback(0, newFilterMock);
        
        mockUtil.resetMock(mock);
        
        verify(factoryMock).setCallback(0, newFilterMock);
    }

    @Test(expected = NotAMockException.class)
    public void testResetMock_withNullMock_shouldThrowNotAMockException() {
        mockUtil.resetMock(null);
    }

    @Test
    public void testGetMockHandler_withValidMock_shouldReturnMockHandlerInterface() {
        ClassToMock mock = mock(ClassToMock.class);

        when(mockUtil.isMockitoMock(mock)).thenReturn(true);
        when(mockUtil.getInterceptor(mock)).thenReturn(mock(MockHandlerInterface.class));

        MockHandlerInterface result = mockUtil.getMockHandler(mock);

        assertNotNull(result);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandler_withNullMock_shouldThrowNotAMockException() {
        mockUtil.getMockHandler(null);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandler_withNonMockitoMock_shouldThrowNotAMockException() {
        ClassToMock mock = mock(ClassToMock.class);

        when(mockUtil.isMockitoMock(mock)).thenReturn(false);

        mockUtil.getMockHandler(mock);
    }

    @Test
    public void testIsMock_withMockitoMock_shouldReturnTrue() {
        ClassToMock mock = mock(ClassToMock.class);

        when(mockUtil.isMockitoMock(mock)).thenReturn(true);

        assertTrue(mockUtil.isMock(mock));
    }

    @Test
    public void testIsMock_withNonMockitoMock_shouldReturnFalse() {
        ClassToMock mock = mock(ClassToMock.class);

        when(mockUtil.isMockitoMock(mock)).thenReturn(false);

        assertFalse(mockUtil.isMock(mock));
    }

    @Test
    public void testIsMock_withNullMock_shouldReturnFalse() {
        assertFalse(mockUtil.isMock(null));
    }

    @Test
    public void testGetMockName_withValidMock_shouldReturnMockName() {
        ClassToMock mock = mock(ClassToMock.class);
        MockHandlerInterface mockHandler = mock(MockHandlerInterface.class);
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        MockName mockName = mock(MockName.class);

        when(mockUtil.getMockHandler(mock)).thenReturn(mockHandler);
        when(mockHandler.getMockSettings()).thenReturn(mockSettings);
        when(mockSettings.getMockName()).thenReturn(mockName);

        assertEquals(mockName, mockUtil.getMockName(mock));
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockName_withNullMock_shouldThrowNotAMockException() {
        mockUtil.getMockName(null);
    }

    // Helper class for testing
    public static class ClassToMock {
    }
}