package org.mockito.internal.util;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.LenientCopyTool;
import org.mockito.mockito3.MockitoCore;
import org.mockito.plugins.MockMaker;
import org.mockito.plugins.MockMakerImpl;
import org.mockito.plugins.MockMakerUtils;
import org.mockito.plugins.MockMakers;
import org.powermock.api.mockito.internal.mockcreator.MockCreator;
import org.powermock.api.mockito.internal.mockcreator.MockCreationSettings;
import org.powermock.core.classloader.MockClassLoader;
import org.powermock.reflect.Whitebox;

import java.lang.reflect.Method;
import java.util.Arrays;

public class MockUtilTest {

    @Test
    public void testCreateMockWithValidArguments() {
        MockMaker mockMaker = new MockMakerImpl(MockitoCore.getMockMakerSettings());
        Class<Object> classToMock = Object.class;
        MockSettingsImpl settings = new MockSettingsImpl();

        MockUtil mockUtil = new MockUtil();
        Object mock = mockUtil.createMock(classToMock, settings);

        assertNotNull(mock);
        verify(mock).mockObject();
    }

    @Test
    public void testCreateMockWithValidArgumentsAndExtraInterfaces() {
        MockMaker mockMaker = new MockMakerImpl(MockitoCore.getMockMakerSettings());
        Class<Foo> classToMock = Foo.class;
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.getExtraInterfaces().add(Bar.class);

        MockUtil mockUtil = new MockUtil();
        Object mock = mockUtil.createMock(classToMock, settings);

        assertNotNull(mock);
        verify(mock).mockObject();
        assertMockHasExpectedInterfaces(mock, Bar.class);
    }

    @Test
    public void testCreateMockWithValidArgumentsAndSpiedInstance() {
        MockMaker mockMaker = new MockMakerImpl(MockitoCore.getMockMakerSettings());
        Class<Foo> classToMock = Foo.class;
        Object spiedInstance = new Foo();
        MockSettingsImpl settings = new MockSettingsImpl();

        MockUtil mockUtil = new MockUtil();
        Object mock = mockUtil.createMock(classToMock, settings, spiedInstance);

        assertNotNull(mock);
        verify(mock).mockObject();
        assertNotNull(Whitebox.getInternalState(mock, "handler"));
        assertEquals(spiedInstance, Whitebox.getInternalState(mock, "spiedInstance"));
    }

    @Test(expected = NotAMockException.class)
    public void testCreateMockWithInvalidArgumentsType() {
        MockUtil mockUtil = new MockUtil();
        mockUtil.createMock(String.class, null);
    }

    @Test(expected = NotAMockException.class)
    public void testCreateMockWithInvalidArgumentsExtraInterfaces() {
        MockMaker mockMaker = new MockMakerImpl(MockitoCore.getMockMakerSettings());
        Class<Foo> classToMock = Foo.class;
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.getExtraInterfaces().add(List.class);

        MockUtil mockUtil = new MockUtil();
        mockUtil.createMock(classToMock, settings);
    }

    @Test(expected = NotAMockException.class)
    public void testCreateMockWithInvalidArgumentsSpiedInstance() {
        MockMaker mockMaker = new MockMakerImpl(MockitoCore.getMockMakerSettings());
        Class<Foo> classToMock = Foo.class;
        MockSettingsImpl settings = new MockSettingsImpl();
        Object spiedInstance = new Object();

        MockUtil mockUtil = new MockUtil();
        mockUtil.createMock(classToMock, settings, spiedInstance);
    }

    @Test
    public void testResetMock() {
        MockMaker mockMaker = new MockMakerImpl(MockitoCore.getMockMakerSettings());
        Class<Foo> classToMock = Foo.class;
        Object mock = mock(classToMock);

        MockUtil mockUtil = new MockUtil();
        mockUtil.resetMock(mock);

        verify(mock).reset();
    }

    @Test
    public void testGetMockHandler() {
        MockMaker mockMaker = new MockMakerImpl(MockitoCore.getMockMakerSettings());
        Class<Foo> classToMock = Foo.class;
        Object mock = mock(classToMock);

        MockUtil mockUtil = new MockUtil();
        MockHandlerInterface<Foo> mockHandler = mockUtil.getMockHandler(mock);

        assertNotNull(mockHandler);
        assertNull(mockHandler.getMockSettings().getMockName());
    }

    @Test
    public void testIsMockitoMock() {
        MockMaker mockMaker = new MockMakerImpl(MockitoCore.getMockMakerSettings());
        Class<Foo> classToMock = Foo.class;
        Object mock = mock(classToMock);

        MockUtil mockUtil = new MockUtil();
        boolean result = mockUtil.isMockitoMock(mock);

        assertTrue(result);
    }

    @Test
    public void testIsMock() {
        MockMaker mockMaker = new MockMakerImpl(MockitoCore.getMockMakerSettings());
        Class<Foo> classToMock = Foo.class;
        Object mock = mock(classToMock);

        MockUtil mockUtil = new MockUtil();
        boolean result = mockUtil.isMock(mock);

        assertTrue(result);
    }

    @Test
    public void testGetInterceptor() {
        Class<Foo> classToMock = Foo.class;
        Object mock = mock(classToMock);

        MockUtil mockUtil = new MockUtil();
        MethodInterceptorFilter result = mockUtil.getInterceptor(mock);

        assertNotNull(result);
        assertNull(result.getMockSettings());
    }

    @Test(expected = NotAMockException.class)
    public void testGetInterceptorWithInvalidArgument() {
        MockUtil mockUtil = new MockUtil();
        MethodInterceptorFilter result = mockUtil.getInterceptor(new Object());
    }

    @Test
    public void testGetMockName() {
        MockMaker mockMaker = new MockMakerImpl(MockitoCore.getMockMakerSettings());
        Class<Foo> classToMock = Foo.class;
        Object mock = mock(classToMock);

        MockUtil mockUtil = new MockUtil();
        MockName result = mockUtil.getMockName(mock);

        assertNotNull(result);
        assertEquals("mock", result.getName());
    }

    @Test
    public void testCreateMockWithCustomName() {
        MockMaker mockMaker = new MockMakerImpl(MockitoCore.getMockMakerSettings());
        Class<Foo> classToMock = Foo.class;
        String customName = "myCustomName";
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.getMockName().setName(customName);

        MockUtil mockUtil = new MockUtil();
        Object mock = mockUtil.createMock(classToMock, settings);

        assertEquals(customName, mockUtil.getMockName(mock).getName());
    }

    @Test(expected = NotAMockException.class)
    public void testCreateMockWithNullSettings() {
        MockUtil mockUtil = new MockUtil();
        mockUtil.createMock(Object.class, null);
    }

    @Test
    public void testCreateMockWithCustomSettings() {
        MockMaker mockMaker = new MockMakerImpl(MockitoCore.getMockMakerSettings());
        Class<Foo> classToMock = Foo.class;
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.getMockName().setName("myCustomName");
        settings.getExtraInterfaces().add(Bar.class);

        MockUtil mockUtil = new MockUtil();
        Object mock = mockUtil.createMock(classToMock, settings);

        assertEquals("myCustomName", mockUtil.getMockName(mock).getName());
        assertMockHasExpectedInterfaces(mock, Bar.class);
    }

    @Test
    public void testCreateMockWithReflectionBasedMockMaker() throws Exception {
        MockMaker mockMaker = MockMakers.getMockMaker();
        Class<Foo> classToMock = Foo.class;
        MockSettingsImpl settings = new MockSettingsImpl();

        MockUtil mockUtil = new MockUtil();
        Object mock = mockUtil.createMock(classToMock, settings);

        assertNotNull(mock);
        verify(mock).mockObject();
    }

    @Test(expected = NotAMockException.class)
    public void testCreateMockWithReflectionBasedMockMakerAndSpiedInstance() throws Exception {
        MockMaker mockMaker = MockMakers.getMockMaker();
        Class<Foo> classToMock = Foo.class;
        Object spiedInstance = new Foo();
        MockSettingsImpl settings = new MockSettingsImpl();

        MockUtil mockUtil = new MockUtil();
        mockUtil.createMock(classToMock, settings, spiedInstance);
    }

    @Test
    public void testCreateMockWithJMockBasedMockMaker() {
        MockClassLoader mockClassLoader = new MockClassLoader();
        Class<Foo> classToMock = Foo.class;
        MockSettingsImpl settings = new MockSettingsImpl();

        MockUtil mockUtil = new MockUtil();
        Object mock = mockUtil.createMock(classToMock, settings);

        assertNotNull(mock);
        verify(mock).mockObject();
    }

    @Test
    public void testCreateMockWithJMockBasedMockMakerAndSpiedInstance() {
        MockClassLoader mockClassLoader = new MockClassLoader();
        Class<Foo> classToMock = Foo.class;
        Object spiedInstance = new Foo();
        MockSettingsImpl settings = new MockSettingsImpl();

        MockUtil mockUtil = new MockUtil();
        Object mock = mockUtil.createMock(classToMock, settings, spiedInstance);

        assertNotNull(mock);
        verify(mock).mockObject();
        assertEquals(spiedInstance, mockUtil.getMockName(mock).getSpiedInstance());
    }

    @Test
    public void testCreateMockWithPowerMockBasedMockMaker() {
        MockMaker mockMaker = MockMaker.class;
        Class<Foo> classToMock = Foo.class;
        MockCreationSettings creationSettings = MockMakerUtils.createNiceMockSettings();
        creationSettings.mockName = "myCustomName";

        MockCreator<Foo> mockCreator = mockMaker.createMock(classToMock, creationSettings);
        Object mock = mockCreator.getMock();

        MockUtil mockUtil = new MockUtil();
        assertNotNull(mockUtil.getMockHandler(mock));
        assertEquals("myCustomName", mockUtil.getMockName(mock).getName());
    }

    @Test
    public void testCreateMockWithPowerMockBasedMockMakerAndExtraInterfaces() {
        MockMaker mockMaker = MockMaker.class;
        Class<Foo> classToMock = Foo.class;
        Class<Bar> extraInterface = Bar.class;
        MockCreationSettings creationSettings = MockMakerUtils.createNiceMockSettings();
        creationSettings.mockName = "myCustomName";
        creationSettings.extraInterfaces = new Class[]{extraInterface};

        MockCreator<Foo> mockCreator = mockMaker.createMock(classToMock, creationSettings);
        Object mock = mockCreator.getMock();

        MockUtil mockUtil = new MockUtil();
        assertMockHasExpectedInterfaces(mock, extraInterface);
        assertEquals("myCustomName", mockUtil.getMockName(mock).getName());
    }

    private void assertMockHasExpectedInterfaces(Object mock, Class<?> expectedInterface) {
        Class<?>[] interfaces = Whitebox.getInternalState(mock, "handler").getClass()
                .getInterfaces();
        assertArrayEquals(new Class[]{expectedInterface}, interfaces);
    }

    private static Class<?>[] assertArrayEquals(Class<?>[] expected, Class<?>[] actual) {
        if (!expected.length == actual.length) {
            throw new AssertionError("Size mismatch: expected=" + expected.length + ", actual=" + actual.length);
        }
        for (int i = 0; i < expected.length; i++) {
            if (!expected[i].equals(actual[i])) {
                throw new AssertionError("Mismatch at index " + i + ". Expected: " + expected[i] + ", Actual: " + actual[i]);
            }
        }
    }
}