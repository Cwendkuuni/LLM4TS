
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.MockCreationValidator;
import org.mockito.internal.util.reflection.LenientCopyTool;

import static org.mockito.Mockito.*;

public class MockUtilTest {

    private MockUtil mockUtil;
    private MockCreationValidator mockCreationValidator;
    private LenientCopyTool lenientCopyTool;

    @Before
    public void setUp() {
        mockCreationValidator = mock(MockCreationValidator.class);
        lenientCopyTool = mock(LenientCopyTool.class);
        mockUtil = new MockUtil(mockCreationValidator);
    }

    @Test
    public void testCreateMock() {
        Class<?> classToMock = Object.class;
        MockSettingsImpl settings = mock(MockSettingsImpl.class);

        mockUtil.createMock(classToMock, settings);

        verify(mockCreationValidator).validateType(classToMock);
        verify(mockCreationValidator).validateExtraInterfaces(classToMock, settings.getExtraInterfaces());
        verify(mockCreationValidator).validateMockedType(classToMock, settings.getSpiedInstance());
        verify(settings).initiateMockName(classToMock);
        verify(lenientCopyTool).copyToMock(settings.getSpiedInstance(), mock);
    }

    @Test
    public void testResetMock() {
        Object mock = mock(Object.class);
        MockHandlerInterface oldMockHandler = mock(MockHandlerInterface.class);
        MockHandler newMockHandler = mock(MockHandler.class);
        MethodInterceptorFilter newFilter = mock(MethodInterceptorFilter.class);

        when(mockUtil.getMockHandler(mock)).thenReturn(oldMockHandler);
        when(mockUtil.newMethodInterceptorFilter(any(MockSettingsImpl.class))).thenReturn(newFilter);

        mockUtil.resetMock(mock);

        verify(oldMockHandler).getMockSettings();
        verify(mockUtil).newMethodInterceptorFilter(any(MockSettingsImpl.class));
        verify(mock).setCallback(0, newFilter);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerWithNull() {
        Object mock = null;

        mockUtil.getMockHandler(mock);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerWithNonMockitoMock() {
        Object mock = new Object();

        mockUtil.getMockHandler(mock);
    }

    @Test
    public void testGetMockHandlerWithMockitoMock() {
        Object mock = mock(Object.class);
        MockHandlerInterface mockHandler = mock(MockHandlerInterface.class);
        MethodInterceptorFilter interceptor = mock(MethodInterceptorFilter.class);

        when(mockUtil.isMockitoMock(mock)).thenReturn(true);
        when(mockUtil.getInterceptor(mock)).thenReturn(interceptor);
        when(interceptor.getHandler()).thenReturn(mockHandler);

        mockUtil.getMockHandler(mock);

        verify(mockUtil).isMockitoMock(mock);
        verify(mockUtil).getInterceptor(mock);
        verify(interceptor).getHandler();
    }

    @Test
    public void testIsMockWithNull() {
        Object mock = null;

        boolean result = mockUtil.isMock(mock);

        assertFalse(result);
    }

    @Test
    public void testIsMockWithNonMockitoMock() {
        Object mock = new Object();

        boolean result = mockUtil.isMock(mock);

        assertFalse(result);
    }

    @Test
    public void testIsMockWithMockitoMock() {
        Object mock = mock(Object.class);

        when(mockUtil.isMockitoMock(mock)).thenReturn(true);

        boolean result = mockUtil.isMock(mock);

        assertTrue(result);
    }

    @Test
    public void testGetMockName() {
        Object mock = mock(Object.class);
        MockHandlerInterface mockHandler = mock(MockHandlerInterface.class);
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        MockName mockName = mock(MockName.class);

        when(mockUtil.getMockHandler(mock)).thenReturn(mockHandler);
        when(mockHandler.getMockSettings()).thenReturn(mockSettings);
        when(mockSettings.getMockName()).thenReturn(mockName);

        mockUtil.getMockName(mock);

        verify(mockUtil).getMockHandler(mock);
        verify(mockHandler).getMockSettings();
        verify(mockSettings).getMockName();
    }
}
