
package org.mockito.internal.util;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.InvocationNotifierHandler;
import org.mockito.internal.MockHandler;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.creation.MethodInterceptorFilter;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.util.reflection.LenientCopyTool;

import java.io.Serializable;

import static org.mockito.Mockito.*;

public class MockUtilTest {
    
    @Test
    public void testCreateMock() {
        MockCreationValidator creationValidator = mock(MockCreationValidator.class);
        MockUtil util = new MockUtil(creationValidator);

        Class<String> classToMock = String.class;
        MockSettingsImpl settings = mock(MockSettingsImpl.class);
        Class<?>[] extraInterfaces = new Class<?>[]{Comparable.class};
        Object spiedInstance = "spiedInstance";
        settings.setExtraInterfaces(extraInterfaces);
        settings.setSpiedInstance(spiedInstance);
        settings.setSerializable(true);

        MethodInterceptorFilter filter = mock(MethodInterceptorFilter.class);
        Class<String>[] ancillaryTypes = new Class[]{String.class, Serializable.class};

        when(settings.getExtraInterfaces()).thenReturn(extraInterfaces);
        when(settings.isSerializable()).thenReturn(true);
        when(settings.getSpiedInstance()).thenReturn(spiedInstance);
        when(filter.filter(null)).thenReturn(filter);
        when(settings.getSpiedInstance()).thenReturn(spiedInstance);
        when(creationValidator.validateType(classToMock)).thenReturn(true);
        when(creationValidator.validateExtraInterfaces(classToMock, extraInterfaces)).thenReturn(true);
        when(creationValidator.validateMockedType(classToMock, spiedInstance)).thenReturn(true);
        when(ClassImposterizer.INSTANCE.imposterise(filter, classToMock, ancillaryTypes)).thenReturn(mock(classToMock));

        String mock = util.createMock(classToMock, settings);

        assertNotNull(mock);
        verify(creationValidator).validateType(classToMock);
        verify(creationValidator).validateExtraInterfaces(classToMock, extraInterfaces);
        verify(creationValidator).validateMockedType(classToMock, spiedInstance);
        verify(settings).initiateMockName(classToMock);
        verify(settings).getExtraInterfaces();
        verify(settings).isSerializable();
        verify(settings).getSpiedInstance();
        verify(filter).filter(null);
        verify(ClassImposterizer.INSTANCE).imposterise(filter, classToMock, ancillaryTypes);
        verifyZeroInteractions(extraInterfaces);
    }

    @Test
    public void testResetMock() {
        MockUtil util = new MockUtil();

        MockHandlerInterface mockHandler = mock(MockHandlerInterface.class);
        MockHandler newMockHandler = mock(MockHandler.class);
        MethodInterceptorFilter newFilter = mock(MethodInterceptorFilter.class);
        Factory mock = mock(Factory.class);

        when(util.getMockHandler(mock)).thenReturn(mockHandler);
        when(mockHandler.getHandler()).thenReturn(newMockHandler);
        when(org.mockito.Mockito.withSettings().defaultAnswer(org.mockito.Mockito.RETURNS_DEFAULTS)).thenReturn(mock(org.mockito.configuration.ReturnsDefaults.class));

        util.resetMock(mock);

        verify(util).getMockHandler(mock);
        verify(newMockHandler).setDefaultAnswer(org.mockito.Mockito.RETURNS_DEFAULTS);
        verify(mock).setCallback(0, newFilter);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerWithNullMock() {
        MockUtil util = new MockUtil();

        util.getMockHandler(null);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerWithNonMockitoMock() {
        MockUtil util = new MockUtil();

        Object mock = new Object();
        util.getMockHandler(mock);
    }

    @Test
    public void testIsMock() {
        MockUtil util = new MockUtil();

        Object mock = mock(Object.class);
        boolean isMock = util.isMock(mock);

        assertTrue(isMock);
        verify(util).isMockitoMock(mock);
    }

    @Test
    public void testIsMockWithNullMock() {
        MockUtil util = new MockUtil();

        boolean isMock = util.isMock(null);

        assertFalse(isMock);
        verify(util, never()).isMockitoMock(null);
    }

    @Test
    public void testGetInterceptor() {
        MockUtil util = new MockUtil();

        MethodInterceptorFilter interceptor = mock(MethodInterceptorFilter.class);
        Factory mock = mock(Factory.class);
        when(mock.getCallback(0)).thenReturn(interceptor);

        MethodInterceptorFilter result = util.getInterceptor(mock);

        assertEquals(interceptor, result);
    }

    @Test
    public void testGetInterceptorWithoutInterceptor() {
        MockUtil util = new MockUtil();

        Factory mock = mock(Factory.class);
        when(mock.getCallback(0)).thenReturn(null);

        MethodInterceptorFilter result = util.getInterceptor(mock);

        assertNull(result);
    }

    @Test
    public void testGetMockName() {
        MockUtil util = new MockUtil();

        MockHandlerInterface mockHandler = mock(MockHandlerInterface.class);
        MockName mockName = mock(MockName.class);
        Object mock = mock(Object.class);

        when(util.getMockHandler(mock)).thenReturn(mockHandler);
        when(mockHandler.getMockSettings()).thenReturn(mockSettings);
        when(mockSettings.getMockName()).thenReturn(mockName);

        MockName result = util.getMockName(mock);

        assertEquals(mockName, result);
        verify(util).getMockHandler(mock);
        verify(mockHandler).getMockSettings();
        verify(mockSettings).getMockName();
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockNameWithNonMockitoMock() {
        MockUtil util = new MockUtil();

        Object mock = new Object();
        util.getMockName(mock);
    }
}
