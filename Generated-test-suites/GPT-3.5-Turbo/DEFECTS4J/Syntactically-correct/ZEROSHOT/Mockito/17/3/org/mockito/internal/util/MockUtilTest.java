import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.MockHandler;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.creation.MethodInterceptorFilter;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.util.reflection.LenientCopyTool;
import org.mockito.Mockito;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MockUtilTest {

    private MockUtil mockUtil;

    @Before
    public void setUp() {
        mockUtil = new MockUtil();
    }

    @Test
    public void testCreateMock() {
        Class<?> classToMock = String.class;
        MockSettingsImpl settings = new MockSettingsImpl();
        Object spiedInstance = "spiedInstance";

        MockHandler mockHandler = mock(MockHandler.class);
        MethodInterceptorFilter filter = Mockito.mock(MethodInterceptorFilter.class);
        Class<?>[] interfaces = null;

        when(mockUtil.createMock(classToMock, settings)).thenReturn(spiedInstance);

        Object mock = mockUtil.createMock(classToMock, settings);

        assertNotNull(mock);
        assertTrue(mock instanceof String);

        verify(mockUtil.creationValidator).validateType(classToMock);
        verify(mockUtil.creationValidator).validateExtraInterfaces(classToMock, settings.getExtraInterfaces());
        verify(mockUtil.creationValidator).validateMockedType(classToMock, settings.getSpiedInstance());
        verify(settings).initiateMockName(classToMock);
        verifyNew(MockHandler.class).withArguments(settings);
        verifyNew(MethodInterceptorFilter.class).withArguments(mockHandler, settings);
        verify(settings).getExtraInterfaces();
        verify(mockUtil.creationValidator).validateExtraInterfaces(classToMock, interfaces);
        verify(settings).getSpiedInstance();
        verify(ClassImposterizer.INSTANCE).imposterise(filter, classToMock, ancillaryTypes);
        verifyNew(LenientCopyTool.class).withNoArguments();
        verify(mockUtil).copyToMock(spiedInstance, mock);
        verify(filter).getHandler();
    }

    @Test
    public void testResetMock() {
        Object mock = mock(Object.class);
        MockHandlerInterface mockHandler = mock(MockHandlerInterface.class);
        MethodInterceptorFilter newFilter = Mockito.mock(MethodInterceptorFilter.class);
        MockSettingsImpl mockSettings = Mockito.mock(MockSettingsImpl.class);
        Factory factoryMock = mock(Factory.class);

        when(mockUtil.getMockHandler(mock)).thenReturn(mockHandler);
        when(withSettings().defaultAnswer(RETURNS_DEFAULTS)).thenReturn(mockSettings);
        when(mockHandler.getMockSettings()).thenReturn(mockSettings);
        when(factoryMock.getCallback(0)).thenReturn(newFilter);

        mockUtil.resetMock(mock);

        verify(mockUtil).getMockHandler(mock);
        verify(mockHandler).getMockSettings();
        verify(mockSettings).getMockName();
        verify(withSettings()).defaultAnswer(RETURNS_DEFAULTS);
        verify(newFilter).getHandler();
        verify(factoryMock).setCallback(0, newFilter);
    }

    @Test
    public void testResetMockNullMock() {
        Object mock = null;

        try {
            mockUtil.resetMock(mock);
            fail("Should throw NotAMockException when mock is null");
        } catch (NotAMockException e) {
            assertEquals("Argument should be a mock, but is null!", e.getMessage());
        }

        verify(mockUtil).resetMock(mock);
        verify(mockUtil).getMockHandler(mock);
        verifyNoMoreInteractions(mockUtil);
    }

    @Test
    public void testResetMockNotMockObject() {
        Object mock = new Object();

        try {
            mockUtil.resetMock(mock);
            fail("Should throw NotAMockException when mock is not a mock object");
        } catch (NotAMockException e) {
            assertEquals("Argument should be a mock, but is: java.lang.Object", e.getMessage());
        }

        verify(mockUtil).resetMock(mock);
        verify(mockUtil).getMockHandler(mock);
        verifyNoMoreInteractions(mockUtil);
    }

    @Test
    public void testIsMock() {
        Object mock = mock(Object.class);

        assertTrue(mockUtil.isMock(mock));

        verify(mockUtil).isMock(mock);
    }

    @Test
    public void testIsMockNotMock() {
        Object notMock = new Object();

        assertFalse(mockUtil.isMock(notMock));

        verify(mockUtil).isMock(notMock);
    }

    @Test
    public void testGetMockHandler() {
        Object mock = mock(Object.class);
        MockHandlerInterface mockHandler = mock(MockHandlerInterface.class);
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);

        when(mockUtil.isMockitoMock(mock)).thenReturn(true);
        when(mockUtil.getInterceptor(mock)).thenReturn(new MethodInterceptorFilter(mockHandler, mockSettings));

        MockHandlerInterface result = mockUtil.getMockHandler(mock);

        assertEquals(mockHandler, result);

        verify(mockUtil).getMockHandler(mock);
        verify(mockUtil).isMockitoMock(mock);
        verify(mockUtil).getInterceptor(mock);
    }

    @Test
    public void testGetMockHandlerNullMock() {
        Object mock = null;

        try {
            mockUtil.getMockHandler(mock);
            fail("Should throw NotAMockException when mock is null");
        } catch (NotAMockException e) {
            assertEquals("Argument should be a mock, but is null!", e.getMessage());
        }

        verify(mockUtil).getMockHandler(mock);
    }

    @Test
    public void testGetMockHandlerNotMockObject() {
        Object mock = new Object();

        try {
            mockUtil.getMockHandler(mock);
            fail("Should throw NotAMockException when mock is not a mock object");
        } catch (NotAMockException e) {
            assertEquals("Argument should be a mock, but is: java.lang.Object", e.getMessage());
        }

        verify(mockUtil).getMockHandler(mock);
    }

    @Test
    public void testMockitoMock() {
        Object mock = mock(Object.class);
        Factory factoryMock = mock(Factory.class);
        MethodInterceptorFilter filter = Mockito.mock(MethodInterceptorFilter.class);
        when(factoryMock.getCallback(0)).thenReturn(filter);
        when(mockUtil.isMockitoMock(mock)).thenReturn(true);
        when(mockUtil.getInterceptor(mock)).thenReturn(filter);

        assertTrue(mockUtil.isMockitoMock(mock));

        verify(mockUtil).isMockitoMock(mock);
        verify(mockUtil).getInterceptor(mock);
        verify(filter).getHandler();
    }

    @Test
    public void testMockitoMockNotEnhanced() {
        Object notMockitoMock = new Object();
        when(mockUtil.isMockitoMock(notMockitoMock)).thenReturn(false);

        assertFalse(mockUtil.isMockitoMock(notMockitoMock));

        verify(mockUtil).isMockitoMock(notMockitoMock);
    }

    @Test
    public void testCopyToMock() {
        Object spiedInstance = mock(Object.class);
        Object mock = mock(Object.class);
        LenientCopyTool copyTool = mock(LenientCopyTool.class);

        whenNew(LenientCopyTool.class).withNoArguments().thenReturn(copyTool);

        mockUtil.copyToMock(spiedInstance, mock);

        verifyNew(LenientCopyTool.class).withNoArguments();
        verify(copyTool).copyToMock(spiedInstance, mock);
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

        MockName result = mockUtil.getMockName(mock);

        assertEquals(mockName, result);

        verify(mockUtil).getMockHandler(mock);
        verify(mockHandler).getMockSettings();
        verify(mockSettings).getMockName();
    }
}