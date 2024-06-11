package org.mockito.internal.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;

import org.junit.Before;
import org.junit.Test;

public class MockUtilTest {
    private MockUtil mockUtil;

    @Before
    public void setUp() {
        mockUtil = new MockUtil();
    }

    @Test
    public void testCreateMock() {
        MockUtil.MockCreationValidator creationValidator = mock(MockUtil.MockCreationValidator.class);
        MockUtil.MockSettingsImpl settings = mock(MockUtil.MockSettingsImpl.class);
        Class clazzToMock = Object.class;
        Object mock = mockUtil.createMock(clazzToMock, settings);
        assertNotNull(mock);
        assertEquals(clazzToMock, mock.getClass());
    }

    @Test
    public void testResetMock() {
        Object mock = mock(Object.class);
        reset(mock);
    }

    @Test
    public void testGetMethodInterceptorFilter() {
        Object mock = mock(Object.class);
        MockUtil.MethodInterceptorFilter interceptor = mockUtil.getInterceptor(mock);
        assertNotNull(interceptor);
    }

    @Test
    public void testIsMock() {
        Object mock = mock(Object.class);
        boolean isMock = mockUtil.isMock(mock);
        assertEquals(true, isMock);
    }

    @Test
    public void testGetMockName() {
        Object mock = mock(Object.class);
        MockUtil.MockHandlerInterface handler = mockUtil.getMockHandler(mock);
        assertNotNull(handler);
        MockUtil.MockName mockName = mockUtil.getMockName(mock);
        assertNotNull(mockName);
    }
}