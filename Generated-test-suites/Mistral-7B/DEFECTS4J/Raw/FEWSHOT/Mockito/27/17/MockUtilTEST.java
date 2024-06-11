package org.mockito.internal.util;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class MockUtilTest {

    @Test
    public void testCreateMock() {
        Class<String> classToMock = String.class;
        MockSettingsImpl settings = new MockSettingsImpl();

        MockUtil mockUtil = new MockUtil();

        T mock = mockUtil.createMock(classToMock, settings);
        Assert.assertNotNull(mock);
        Assert.assertInstanceOf(String.class, mock);
    }

    @Test(expected = NotAMockException.class)
    public void testCreateMockWithNullClass() {
        MockUtil mockUtil = new MockUtil();
        mockUtil.createMock(null, new MockSettingsImpl());
    }

    @Test(expected = NotAMockException.class)
    public void testCreateMockWithInvalidClass() {
        Class<Integer> invalidClass = new Object() {}.getClass();
        MockUtil mockUtil = new MockUtil();
        mockUtil.createMock(invalidClass, new MockSettingsImpl());
    }

    @Test(expected = NotAMockException.class)
    public void testCreateMockWithExtraInterface() {
        Class<String> classToMock = String.class;
        Class<?> extraInterface = Integer.class;
        MockSettingsImpl settings = new MockSettingsImpl().withExtraInterfaces(extraInterface);

        MockUtil mockUtil = new MockUtil();

        mockUtil.createMock(classToMock, settings);
    }

    @Test(expected = NotAMockException.class)
    public void testCreateMockWithSpiedInstance() {
        Class<String> classToMock = String.class;
        Object spiedInstance = new Object();
        MockSettingsImpl settings = new MockSettingsImpl().withSpiedInstance(spiedInstance);

        MockUtil mockUtil = new MockUtil();

        mockUtil.createMock(classToMock, settings);
    }

    @Test
    public void testCreateMockWithExtraInterfaceAndSpiedInstance() {
        Class<String> classToMock = String.class;
        Class<?> extraInterface = Integer.class;
        Object spiedInstance = new Object();
        MockSettingsImpl settings = new MockSettingsImpl()
                .withExtraInterfaces(extraInterface)
                .withSpiedInstance(spiedInstance);

        MockUtil mockUtil = new MockUtil();

        T mock = mockUtil.createMock(classToMock, settings);
        Assert.assertNotNull(mock);
        Assert.assertInstanceOf(String.class, mock);
        verify(spiedInstance).toString();
    }

    @Test
    public void testResetMock() {
        Object mock = Mockito.mock(String.class);
        MockUtil mockUtil = new MockUtil();

        mockUtil.resetMock(mock);
        verify(mock, times(0)).toString();

        reset(mock);
        verify(mock).toString();
    }

    @Test
    public void testGetMockHandler() {
        Object mock = Mockito.mock(String.class);

        MockUtil mockUtil = new MockUtil();
        MockHandlerInterface<String> handler = mockUtil.getMockHandler(mock);

        Assert.assertNotNull(handler);
        Assert.assertInstanceOf(MockHandler.class, handler);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerWithNullMock() {
        MockUtil mockUtil = new MockUtil();
        mockUtil.getMockHandler(null);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerWithNonMockitoMock() {
        Object nonMockitoMock = new Object();
        MockUtil mockUtil = new MockUtil();

        mockUtil.getMockHandler(nonMockitoMock);
    }

    @Test
    public void testIsMockitoMock() {
        Object mock = Mockito.mock(String.class);
        Object nonMockitoMock = new Object();

        MockUtil mockUtil = new MockUtil();

        Assert.assertTrue(mockUtil.isMockitoMock(mock));
        Assert.assertFalse(mockUtil.isMockitoMock(nonMockitoMock));
    }

    @Test
    public void testIsMock() {
        Object mock = Mockito.mock(String.class);
        Object nonMock = new Object();

        MockUtil mockUtil = new MockUtil();

        Assert.assertTrue(mockUtil.isMock(mock));
        Assert.assertFalse(mockUtil.isMock(nonMock));
    }
}