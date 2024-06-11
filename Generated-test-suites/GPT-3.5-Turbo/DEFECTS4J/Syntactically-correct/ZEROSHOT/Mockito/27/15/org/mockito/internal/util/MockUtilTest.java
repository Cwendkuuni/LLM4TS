import org.junit.Test;
import org.mockito.cglib.proxy.Callback;
import org.mockito.cglib.proxy.Factory;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.InvocationNotifierHandler;
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
        MockCreationValidator validator = mock(MockCreationValidator.class);
        MockUtil mockUtil = new MockUtil(validator);

        Class<?> classToMock = Object.class;
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.setExtraInterfaces(new Class<?>[]{Serializable.class});
        settings.setSpiedInstance(new Object());

        Object mock = mockUtil.createMock(classToMock, settings);

        assertNotNull(mock);
        verify(validator).validateType(classToMock);
        verify(validator).validateExtraInterfaces(classToMock, settings.getExtraInterfaces());
        verify(validator).validateMockedType(classToMock, settings.getSpiedInstance());
    }

    @Test
    public void testResetMock() {
        MockUtil mockUtil = new MockUtil();

        Object mock = mock(Object.class);
        MockHandlerInterface<Object> oldMockHandler = mock(MockHandlerInterface.class);
        when(mockUtil.getMockHandler(mock)).thenReturn(oldMockHandler);

        mockUtil.resetMock(mock);

        ArgumentCaptor<MethodInterceptorFilter> newFilterCaptor = ArgumentCaptor.forClass(MethodInterceptorFilter.class);
        verify(mock, times(1)).setCallback(eq(0), newFilterCaptor.capture());

        MethodInterceptorFilter newFilter = newFilterCaptor.getValue();
        assertNotNull(newFilter);

        MockHandler<Object> newMockHandler = new MockHandler<>(oldMockHandler);
        MethodInterceptorFilter expectedFilter = new MethodInterceptorFilter(newMockHandler, (MockSettingsImpl) org.mockito.Mockito.withSettings().defaultAnswer(org.mockito.Mockito.RETURNS_DEFAULTS));

        assertEquals(expectedFilter, newFilter);
    }

    @Test
    public void testNewMethodInterceptorFilter() {
        MockSettingsImpl settings = new MockSettingsImpl();
        MockHandler<Object> mockHandler = mock(MockHandler.class);
        when(mockHandler.getMockSettings()).thenReturn(settings);

        InvocationNotifierHandler<Object> invocationNotifierHandler = mock(InvocationNotifierHandler.class);
        when(invocationNotifierHandler.getMockHandler()).thenReturn(mockHandler);
        when(invocationNotifierHandler.getSettings()).thenReturn(settings);

        MethodInterceptorFilter filter = new MethodInterceptorFilter(invocationNotifierHandler, settings);

        assertSame(invocationNotifierHandler, filter.getInvocationHandler());
        assertSame(settings, filter.getSettings());
    }

    @Test
    public void testGetMockHandler() {
        MockUtil mockUtil = new MockUtil();

        Object mock = mock(Object.class);
        MockHandlerInterface<Object> handler = mock(MockHandlerInterface.class);
        when(mockUtil.isMockitoMock(mock)).thenReturn(true);
        when(mockUtil.getInterceptor(mock)).thenReturn(new MethodInterceptorFilter(handler, null));

        MockHandlerInterface<Object> result = mockUtil.getMockHandler(mock);

        assertSame(handler, result);
        verify(mockUtil).isMockitoMock(mock);
        verify(mockUtil).getInterceptor(mock);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerWithNullMock() {
        MockUtil mockUtil = new MockUtil();

        mockUtil.getMockHandler(null);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerWithNonMockitoMock() {
        MockUtil mockUtil = new MockUtil();

        Object mock = new Object();
        mockUtil.getMockHandler(mock);
    }

    @Test
    public void testIsMock() {
        MockUtil mockUtil = new MockUtil();

        Object mock = mock(Object.class);
        when(mockUtil.isMockitoMock(mock)).thenReturn(true);

        boolean result = mockUtil.isMock(mock);

        assertTrue(result);
        verify(mockUtil).isMockitoMock(mock);

        mock = new Object();
        result = mockUtil.isMock(mock);

        assertFalse(result);
        verify(mockUtil, times(2)).isMockitoMock(mock);
    }

    @Test
    public void testGetInterceptor() {
        MockUtil mockUtil = new MockUtil();

        Object mock = mock(Object.class);
        Factory factory = mock(Factory.class);
        Callback callback = mock(MethodInterceptorFilter.class);
        when(factory.getCallback(0)).thenReturn(callback);
        when(mockUtil.isMockitoMock(mock)).thenReturn(true);
        when(mockUtil.getInterceptor(mock)).thenCallRealMethod();

        MethodInterceptorFilter result = mockUtil.getInterceptor(mock);

        assertSame(callback, result);
        verify(mockUtil).isMockitoMock(mock);
        verify(factory).getCallback(0);
    }

    @Test
    public void testGetInterceptorWithNonFactoryMock() {
        MockUtil mockUtil = new MockUtil();

        Object mock = new Object();
        mockUtil.getInterceptor(mock);

        verify(mockUtil).isMockitoMock(mock);
        verify(mockUtil, never()).isMockitoMock(any());
    }

    @Test
    public void testGetMockName() {
        MockUtil mockUtil = new MockUtil();

        Object mock = mock(Object.class);
        MockHandlerInterface<Object> handler = mock(MockHandlerInterface.class);
        MockSettingsImpl settings = mock(MockSettingsImpl.class);
        when(handler.getMockSettings()).thenReturn(settings);
        when(mockUtil.getMockHandler(mock)).thenReturn(handler);
        when(settings.getMockName()).thenReturn(new MockName("mockName"));

        MockName result = mockUtil.getMockName(mock);

        assertEquals(new MockName("mockName"), result);
        verify(mockUtil).getMockHandler(mock);
        verify(mockUtil, never()).getMockHandler(null);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockNameWithNullMock() {
        MockUtil mockUtil = new MockUtil();

        mockUtil.getMockName(null);
    }
}