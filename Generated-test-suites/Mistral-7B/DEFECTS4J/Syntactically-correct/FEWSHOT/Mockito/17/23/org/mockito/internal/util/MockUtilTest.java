package org.mockito.internal.util;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.mockito.Mockito;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.util.MockName;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.LenientCopyTool;
import org.mockito.mockito3.MockitoCore;
import org.mockito.plugins.MockMaker;
import org.mockito.plugins.MockMakerImpl;

import java.lang.reflect.Method;
import java.util.Arrays;

public class MockUtilTest {

    private static final MockMaker mockMaker = new MockMakerImpl();
    private static final MockHandler<Object> MOCK_HANDLER = mock(MockHandler.class);

    @Test
    public void testCreateMock() {
        MockUtil mockUtil = new MockUtil();
        Class<?> mockableClass = MockitoCore.class;
        MockSettingsImpl settings = MockitoCore.settings();

        Object mock = mockUtil.createMock(mockableClass, settings);
        assertNotNull(mock);
        assertTrue(isMockitoMock(mock));
        assertEquals(mockableClass.getName(), getMockName(mock).getName());
    }

    @Test(expected = NotAMockException.class)
    public void testCreateMockWithNullClass() {
        MockUtil mockUtil = new MockUtil();
        mockUtil.createMock(null, new MockSettingsImpl());
    }

    @Test(expected = NotAMockException.class)
    public void testCreateMockWithInvalidClass() {
        MockUtil mockUtil = new MockUtil();
        Class<?> invalidClass = String.class;
        MockSettingsImpl settings = MockitoCore.settings();

        mockUtil.createMock(invalidClass, settings);
    }

    @Test(expected = NotAMockException.class)
    public void testCreateMockWithExtraInterfaces() {
        MockUtil mockUtil = new MockUtil();
        Class<?> mockableClass = MockitoCore.class;
        Class<?> extraInterface = String.class;
        MockSettingsImpl settings = MockitoCore.settings().extraInterfaces(extraInterface);

        mockUtil.createMock(mockableClass, settings);
    }

    @Test(expected = NotAMockException.class)
    public void testCreateMockWithSpiedInstance() {
        MockUtil mockUtil = new MockUtil();
        Class<?> mockableClass = MockitoCore.class;
        Object spiedInstance = mock(MockitoCore.class);
        MockSettingsImpl settings = MockitoCore.settings().spiedInstance(spiedInstance);

        mockUtil.createMock(mockableClass, settings);
    }

    @Test
    public void testCreateMockWithSpiedInstanceAndExtraInterfaces() {
        MockUtil mockUtil = new MockUtil();
        Class<?> mockableClass = MockitoCore.class;
        Object spiedInstance = mock(MockitoCore.class);
        Class<?> extraInterface = String.class;
        MockSettingsImpl settings = MockitoCore.settings().spiedInstance(spiedInstance).extraInterfaces(extraInterface);

        Object mock = mockUtil.createMock(mockableClass, settings);
        assertNotNull(mock);
        assertTrue(isMockitoMock(mock));
        assertEquals(mockableClass.getName(), getMockName(mock).getName());
        assertEquals(extraInterface.getName(), getMockName(mock, "extraInterfaces").getName());

        verify(spiedInstance, never()).hashCode();
        verifyNoMoreInteractions(spiedInstance);
    }

    @Test
    public void testResetMock() {
        MockUtil mockUtil = new MockUtil();
        Object mock = mock(Object.class);
        MockHandlerInterface<Object> oldMockHandler = mockUtil.getMockHandler(mock);

        mockUtil.resetMock(mock);

        MockHandler<Object> newMockHandler = mockUtil.getMockHandler(mock);
        assertEquals(oldMockHandler, newMockHandler);
        assertSame(oldMockHandler, getInterceptor(mock).getHandler());
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerWithNullMock() {
        MockUtil mockUtil = new MockUtil();
        mockUtil.getMockHandler(null);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerWithNonMockitoMock() {
        Object nonMockitoMock = new Object() {};
        MockUtil mockUtil = new MockUtil();
        mockUtil.getMockHandler(nonMockitoMock);
    }

    @Test
    public void testIsMock() {
        Object mock = mock(Object.class);
        assertTrue(mockUtil.isMock(mock));
    }

    @Test
    public void testIsMockFalse() {
        Object nonMock = new Object() {};
        assertFalse(mockUtil.isMock(nonMock));
    }

    @Test
    public void testGetInterceptor() {
        Object mock = mock(Object.class);
        MethodInterceptorFilter interceptorFilter = mockUtil.getInterceptor(mock);
        assertNotNull(interceptorFilter);
    }

    @Test(expected = NotAMockException.class)
    public void testGetInterceptorWithNullMock() {
        MockUtil mockUtil = new MockUtil();
        mockUtil.getInterceptor(null);
    }

    @Test(expected = NotAMockException.class)
    public void testGetInterceptorWithNonMockitoMock() {
        Object nonMockitoMock = new Object() {};
        MockUtil mockUtil = new MockUtil();
        mockUtil.getInterceptor(nonMockitoMock);
    }

    @Test
    public void testGetMockName() {
        Object mock = mock(Object.class);
        MockName mockName = mockUtil.getMockName(mock);
        assertNotNull(mockName);
        assertEquals("mock", mockName.getName());
    }

    @Test
    public void testGetMockNameWithCustomName() {
        Object mock = mock(Object.class, "CustomName");
        MockName mockName = mockUtil.getMockName(mock);
        assertNotNull(mockName);
        assertEquals("CustomName", mockName.getName());
    }

    @Test
    public void testGetMockNameWithCustomNameAndExtraInterfaces() {
        Object mock = mock(Object.class, "CustomName");
        Class<?> extraInterface = String.class;
        MockSettingsImpl settings = MockitoCore.settings().extraInterfaces(extraInterface);
        Object mockWithExtraInterfaces = mockUtil.createMock(mock.getClass(), settings);
        MockName mockName = mockUtil.getMockName(mockWithExtraInterfaces);
        assertNotNull(mockName);
        assertEquals("CustomName", mockName.getName());
        assertEquals("mock", mockName.getName("extraInterfaces"));
    }

    private boolean isMockitoMock(Object mock) {
        return mockUtil.isMockitoMock(mock);
    }

    private MethodInterceptorFilter getInterceptor(Object mock) {
        return mockUtil.getInterceptor(mock);
    }

    private MockName getMockName(Object mock) {
        return mockUtil.getMockName(mock);
    }

    private MockName getMockName(Object mock, String name) {
        return mockUtil.getMockName(mock, name);
    }
}