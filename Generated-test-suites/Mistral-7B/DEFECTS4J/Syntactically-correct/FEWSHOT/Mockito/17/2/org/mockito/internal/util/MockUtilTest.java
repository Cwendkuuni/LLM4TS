package org.mockito.internal.util;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.internal.MockHandler;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.MockSettingsImpl;
import org.mockito.internal.creation.MethodInterceptorFilter;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.util.reflection.LenientCopyTool;
import org.mockito.invocation.MethodInterceptor;
import org.mockito.stub.Answer;
import org.mockito.stub.Stubber;

import java.util.Arrays;

public class MockUtilTest {
    @Test
    public void testCreateMock() {
        Class<String> classToMock = String.class;
        MockSettingsImpl settings = new MockSettingsImpl();

        MockUtil mockUtil = new MockUtil();

        String mock = mockUtil.createMock(classToMock, settings);
        assertNotNull(mock);

        // Test with null settings
        mock = mockUtil.createMock(classToMock, null);
        assertNotNull(mock);

        // Test with empty extra interfaces
        settings = new MockSettingsImpl();
        settings.setExtraInterfaces(new Class[0]);
        mock = mockUtil.createMock(classToMock, settings);
        assertNotNull(mock);

        // Test with extra interfaces
        settings.setExtraInterfaces(new Class[]{Integer.class, Long.class});
        mock = mockUtil.createMock(classToMock, settings);
        assertNotNull(mock);

        // Test with spied instance
        String spiedInstance = "mockito";
        settings.setSpiedInstance(spiedInstance);
        mock = mockUtil.createMock(classToMock, settings);
        assertNotNull(mock);
        verify(spiedInstance).getClass();

        // Test with LenientCopyTool
        settings = new MockSettingsImpl();
        LenientCopyTool lenientCopyTool = new LenientCopyTool();
        mockUtil = new MockUtil(new CreationValidator() {
            @Override
            public void validateType(Class<?> clazz) {
                // do nothing
            }

            @Override
            public void validateExtraInterfaces(Class<?> clazz, Class<?>[] extraInterfaces) {
                // do nothing
            }

            @Override
            public void validateMockedType(Class<?> clazz, Object spiedInstance) {
                // do nothing
            }

            @Override
            public void initiateMockName(Class<?> clazz) {
                // do nothing
            }

            @Override
            public void copyToMock(Object spiedInstance, Object mock) {
                lenientCopyTool.copyToMock(spiedInstance, mock);
            }
        });
        mock = mockUtil.createMock(classToMock, settings);
        assertNotNull(mock);
        verify(spiedInstance).getClass();
        verifyZeroInteractions(spiedInstance);
    }

    @Test
    public void testResetMock() {
        String mock = mock(String.class);
        MockUtil mockUtil = new MockUtil();

        MockHandlerInterface<String> oldMockHandler = mockUtil.getMockHandler(mock);
        MockHandler<String> newMockHandler = new MockHandler<>(oldMockHandler);
        MethodInterceptorFilter newFilter = new MethodInterceptorFilter(newMockHandler, new MockSettingsImpl());

        mockUtil.resetMock(mock);

        verify(mock).setCallback(0, newFilter);
    }

    @Test
    public void testGetMockHandler() {
        String mock = mock(String.class);

        MockUtil mockUtil = new MockUtil();

        MockHandlerInterface<String> mockHandler = mockUtil.getMockHandler(mock);
        assertNotNull(mockHandler);

        // Test with null mock
        mockHandler = mockUtil.getMockHandler(null);
        assertNull(mockHandler);

        // Test with non-mock object
        Object nonMockObject = new Object();
        mockHandler = mockUtil.getMockHandler(nonMockObject);
        assertNull(mockHandler);
    }

    @Test
    public void testIsMockitoMock() {
        String mock = mock(String.class);

        MockUtil mockUtil = new MockUtil();

        assertTrue(mockUtil.isMockitoMock(mock));

        // Test with null mock
        assertFalse(mockUtil.isMockitoMock(null));

        // Test with non-mock object
        Object nonMockObject = new Object();
        assertFalse(mockUtil.isMockitoMock(nonMockObject));
    }

    @Test
    public void testIsMock() {
        String mock = mock(String.class);

        MockUtil mockUtil = new MockUtil();

        assertTrue(mockUtil.isMock(mock));

        // Test with null mock
        assertFalse(mockUtil.isMock(null));

        // Test with non-mock object
        Object nonMockObject = new Object();
        assertFalse(mockUtil.isMock(nonMockObject));
    }

    @Test
    public void testGetInterceptor() {
        String mock = mock(String.class);

        MockUtil mockUtil = new MockUtil();

        MethodInterceptorFilter interceptorFilter = mockUtil.getInterceptor(mock);
        assertNull(interceptorFilter);

        // Test with null mock
        interceptorFilter = mockUtil.getInterceptor(null);
        assertNull(interceptorFilter);

        // Test with non-mock object
        Object nonMockObject = new Object();
        interceptorFilter = mockUtil.getInterceptor(nonMockObject);
        assertNull(interceptorFilter);
    }

    @Test
    public void testGetMockName() {
        Object mock = mock(String.class);

        MockUtil mockUtil = new MockUtil();

        MockName mockName = mockUtil.getMockName(mock);
        assertNotNull(mockName);

        // Test with null mock
        mockName = mockUtil.getMockName(null);
        assertNull(mockName);
    }
}