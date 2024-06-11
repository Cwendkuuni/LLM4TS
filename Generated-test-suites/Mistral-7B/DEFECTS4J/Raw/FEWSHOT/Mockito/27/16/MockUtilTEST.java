package org.mockito.internal.util;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.internal.MockCreationValidator;
import org.mockito.internal.MockHandler;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.MockSettingsImpl;
import org.mockito.internal.creation.MethodInterceptorFilter;
import org.mockito.internal.reflection.LenientCopyTool;

import java.lang.reflect.Proxy;
import java.util.Arrays;

public class MockUtilTest {

    private MockCreationValidator creationValidator = mock(MockCreationValidator.class);
    private MockUtil mockUtil = new MockUtil(creationValidator);

    @Test
    public void testCreateMock() {
        Class<?> clazzToMock = Mockito.class;
        MockSettingsImpl settings = mock(MockSettingsImpl.class);
        when(settings.getSpiedInstance()).thenReturn(this);
        when(settings.getExtraInterfaces()).thenReturn(new Class[0]);

        Object mock = mockUtil.createMock(clazzToMock, settings);
        assertNotNull(mock);
        assertTrue(isA(Proxy.class, mock));
        verify(creationValidator).validateType(clazzToMock);
        verify(creationValidator).validateExtraInterfaces(clazzToMock, Arrays.asList(new Class[0]));
        verify(creationValidator).validateMockedType(clazzToMock, this);

        reset(creationValidator);

        settings.getSpiedInstance().equals(this);
        settings.setMockName("mockName");
        settings.isSerializable();

        Class<?>[] ancillaryTypes = mockUtil.createMock(clazzToMock, settings).getClass().getInterfaces();
        assertTrue(Arrays.asList(ancillaryTypes).contains(Serializable.class));
    }

    @Test(expected = NotAMockException.class)
    public void testCreateMockWithNullClassToMock() {
        mockUtil.createMock(null, new MockSettingsImpl());
    }

    @Test(expected = NotAMockException.class)
    public void testCreateMockWithInvalidClassToMock() {
        MockCreationValidator creationValidatorSpy = spy(MockCreationValidator.class);
        doThrow(new RuntimeException("Invalid class to mock!")).when(creationValidatorSpy).validateType(Integer.class);

        MockUtil mockUtilSpy = spy(MockUtil.class);
        doCallRealMethod().when(mockUtilSpy).createMock(eq(Integer.class), any(MockSettingsImpl.class));

        mockUtilSpy.createMock(Integer.class, new MockSettingsImpl());
    }

    @Test(expected = NotAMockException.class)
    public void testCreateMockWithInvalidMockSettingsExtraInterfaces() {
        MockCreationValidator creationValidatorSpy = spy(MockCreationValidator.class);
        doThrow(new RuntimeException("Invalid extra interfaces!")).when(creationValidatorSpy).validateExtraInterfaces(eq(Integer.class), anyArray());

        MockUtil mockUtilSpy = spy(MockUtil.class);
        doCallRealMethod().when(mockUtilSpy).createMock(eq(Integer.class), any(MockSettingsImpl.class));

        mockUtilSpy.createMock(Integer.class, new MockSettingsImpl() {{
            setExtraInterfaces(new Class[0]);
        }});
    }

    @Test(expected = NotAMockException.class)
    public void testCreateMockWithInvalidSpiedInstance() {
        MockCreationValidator creationValidatorSpy = spy(MockCreationValidator.class);
        doThrow(new RuntimeException("Invalid spied instance!")).when(creationValidatorSpy).validateMockedType(eq(Integer.class), any());

        MockUtil mockUtilSpy = spy(MockUtil.class);
        doCallRealMethod().when(mockUtilSpy).createMock(eq(Integer.class), any(MockSettingsImpl.class));

        mockUtilSpy.createMock(Integer.class, new MockSettingsImpl() {{
            setSpiedInstance(new Object());
        }});
    }

    @Test
    public void testResetMock() {
        Object mock = mock(Object.class);
        mockUtil.resetMock(mock);
        verify(mock, times(1)).setCallback(eq(0), any());
    }

    @Test(expected = NotAMockException.class)
    public void testResetMockWithNullMock() {
        mockUtil.resetMock(null);
    }

    @Test
    public void testGetMockHandler() {
        Object mock = mock(Factory.class);
        when(mock.getClass().getInterfaces()).thenReturn(new Class[0]);
        MockHandlerInterface mockHandler = mockUtil.getMockHandler(mock);
        assertNotNull(mockHandler);

        Object mockWithInterceptor = proxy(Mockito.class.getInterfaces(), new MockHandler<Object>(mockHandler));
        MockHandlerInterface mockHandlerWithInterceptor = mockUtil.getMockHandler(mockWithInterceptor);
        assertNotNull(mockHandlerWithInterceptor);
        assertEquals(mockHandler, mockHandlerWithInterceptor);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerWithNullMock() {
        mockUtil.getMockHandler(null);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerWithNonMockitoMock() {
        Object nonMockitoMock = new Object();
        mockUtil.getMockHandler(nonMockitoMock);
    }

    @Test
    public void testIsMock() {
        Object mock = mockitoMock(Factory.class);
        assertTrue(mockUtil.isMock(mock));

        Object nonMockitoMock = new Object();
        assertFalse(mockUtil.isMock(nonMockitoMock));
    }

    @Test
    public void testGetInterceptor() {
        Object mock = mock(Factory.class);
        MethodInterceptorFilter interceptorFilter = mockUtil.getInterceptor(mock);
        assertNull(interceptorFilter);

        Object mockWithInterceptor = proxy(Mockito.class.getInterfaces(), new MethodInterceptorFilter(mockUtil, new MockSettingsImpl()));
        MethodInterceptorFilter interceptorFilterWithInterceptor = mockUtil.getInterceptor(mockWithInterceptor);
        assertNotNull(interceptorFilterWithInterceptor);
        assertEquals(mockUtil.getInterceptor(mockWithInterceptor), interceptorFilterWithInterceptor);
    }

    @Test
    public void testGetInterceptorWithNullMock() {
        mockUtil.getInterceptor(null);
    }

    @Test
    public void testGetMockName() {
        Object mock = mock(Object.class);
        MockHandlerInterface mockHandler = mockUtil.getMockHandler(mock);
        MockName mockName = mockUtil.getMockName(mock);
        assertEquals(mockHandler.getMockSettings().getMockName(), mockName);
    }

    @Test
    public void testGetMockNameWithNullMock() {
        mockUtil.getMockName(null);
    }
}