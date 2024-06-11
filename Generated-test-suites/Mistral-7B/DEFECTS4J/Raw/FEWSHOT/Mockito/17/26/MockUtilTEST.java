package org.mockito.internal.util;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.RETURNS_DEFAULTS;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.withSettings;

import org.junit.Test;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.util.MockHandler;
import org.mockito.internal.util.MockName;
import org.mockito.internal.util.MethodInterceptorFilter;
import org.mockito.internal.util.reflection.LenientCopyTool;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.junit.Runners;
import org.mockito.mock.MockitoMockFactory;
import org.mockito.plugins.MockMaker;
import org.mockito.plugins.MockMakers;
import org.mockito.plugins.Plugin;
import org.mockito.plugins.PluginLoader;
import org.powermock.reflect.whitebox.WhiteboxImpl;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unchecked")
public class MockUtilTest {

    private CreationValidator creationValidator;
    private MockMaker mockMaker;

    public MockUtilTest() {
        this(new CreationValidator(), new MockMakers.DefaultMockMaker());
    }

    public MockUtilTest(CreationValidator creationValidator, MockMaker mockMaker) {
        this.creationValidator = creationValidator;
        this.mockMaker = mockMaker;
    }

    @Test
    public void testCreateMock() {
        Class<SomeClass> classToMock = SomeClass.class;
        MockSettingsImpl settings = new MockSettingsImpl();

        MockHandler mockHandler = new MockHandler(settings);
        MethodInterceptorFilter filter = new MethodInterceptorFilter(mockHandler, settings);
        Class<?>[] ancillaryTypes = new Class[0];

        Object spiedInstance = spy(classToMock);

        MockitoMockFactory factory = this.mockMaker.createMock(classToMock, settings);
        SomeClass mock = (SomeClass) factory.getMock();

        creationValidator.validateType(classToMock);
        creationValidator.validateExtraInterfaces(classToMock, settings.getExtraInterfaces());
        creationValidator.validateMockedType(classToMock, spiedInstance);

        assertNotNull(mock);
        assertNotNull(mockHandler);
        assertNotNull(filter);

        assertEquals(settings, mockHandler.getMockSettings());
        assertEquals(spiedInstance, mockHandler.getMockSettings().getSpiedInstance());

        creationValidator.initiateMockName(classToMock, settings);
        assertNotNull(settings.getMockName());
        assertEquals(settings.getMockName().toString(), mock.getClass().getSimpleName() + "$MockitoMock$");

        creationValidator.setMockName(settings, "MyTestMockName");
        assertEquals("MyTestMockName", mock.getClass().getSimpleName());

        settings.initiateMockName(classToMock);
        assertNotNull(settings.getMockName());
        assertEquals(settings.getMockName().toString(), mock.getClass().getSimpleName() + "$MockitoMock$");

        creationValidator.setMockName(settings, null);
        assertNull(mock.getClass().getSimpleName());

        MockHandlerInterface mockHandlerInterface = getMockHandler(mock);
        assertNotNull(mockHandlerInterface);
        assertEquals(mockHandler, mockHandlerInterface);
    }

    @Test
    public void testCreateMockWithExtraInterfaces() {
        Class<SomeInterface> interfaceToMock = SomeInterface.class;
        Class<SomeClass> classToMock = SomeClass.class;
        MockSettingsImpl settings = new MockSettingsImpl();

        settingExtraInterfaces(settings, interfaceToMock);

        MockHandler mockHandler = new MockHandler(settings);
        MethodInterceptorFilter filter = new MethodInterceptorFilter(mockHandler, settings);
        Class<?>[] ancillaryTypes = new Class[]{interfaceToMock};

        Object spiedInstance = spy(classToMock);

        MockitoMockFactory factory = this.mockMaker.createMock(classToMock, settings);
        SomeClass mock = (SomeClass) factory.getMock();

        creationValidator.validateType(classToMock);
        creationValidator.validateExtraInterfaces(classToMock, settings.getExtraInterfaces());
        creationValidator.validateMockedType(classToMock, spiedInstance);

        assertNotNull(mock);
        assertNotNull(mockHandler);
        assertNotNull(filter);

        assertEquals(settings, mockHandler.getMockSettings());
        assertEquals(spiedInstance, mockHandler.getMockSettings().getSpiedInstance());

        creationValidator.initiateMockName(classToMock, settings);
        assertNotNull(settings.getMockName());
        assertEquals(settings.getMockName().toString(), mock.getClass().getSimpleName() + "$MockitoMock$");

        creationValidator.setMockName(settings, "MyTestMockName");
        assertEquals("MyTestMockName", mock.getClass().getSimpleName());

        assertInstanceOf(interfaceToMock, mock);
    }

    @Test
    public void testResetMock() {
        Class<SomeClass> classToMock = SomeClass.class;
        MockSettingsImpl settings = new MockSettingsImpl();

        Object spiedInstance = new SomeClass();
        SomeClass mock = createMock(classToMock, settings);

        MockHandlerInterface oldMockHandler = getMockHandler(mock);

        resetMock(mock);

        MockHandler<SomeClass> newMockHandler = new MockHandler<>(oldMockHandler);
        MethodInterceptorFilter newFilter = new MethodInterceptorFilter(newMockHandler, withSettings().defaultAnswer(RETURNS_DEFAULTS));
        ((Factory) mock).setCallback(0, newFilter);

        MockHandlerInterface newMockHandlerInterface = getMockHandler(mock);
        assertNotNull(newMockHandlerInterface);
        assertNotSame(oldMockHandler, newMockHandlerInterface);
    }

    @Test(expected = NotAMockException.class)
    public void testResetMockOnNullMock() {
        resetMock(null);
    }

    @Test(expected = NotAMockException.class)
    public void testResetMockOnNonMockitoMock() {
        Object nonMockitoMock = new Object();
        resetMock(nonMockitoMock);
    }

    @Test
    public void testIsMock() {
        Object mock = createMock(SomeClass.class, new MockSettingsImpl());
        assertTrue(isMock(mock));

        Object nonMock = new Object();
        assertFalse(isMock(nonMock));

        Object nonMockitoMock = new SomeClass();
        assertFalse(isMock(nonMockitoMock));
    }

    @Test
    public void testIsMockitoMock() {
        Object mockitoMock = createMock(SomeClass.class, new MockSettingsImpl());
        assertTrue(isMockitoMock(mockitoMock));

        Object nonMockitoMock = new SomeClass();
        assertFalse(isMockitoMock(nonMockitoMock));
    }

    @Test
    public void testGetInterceptor() {
        Object mockitoMock = createMock(SomeClass.class, new MockSettingsImpl());

        MethodInterceptorFilter interceptor = getInterceptor(mockitoMock);
        assertNotNull(interceptor);
    }

    @Test(expected = NotAMockException.class)
    public void testGetInterceptorOnNullMock() {
        getInterceptor(null);
    }

    @Test(expected = NotAMockException.class)
    public void testGetInterceptorOnNonMockitoMock() {
        Object nonMockitoMock = new SomeClass();
        getInterceptor(nonMockitoMock);
    }

    @Test
    public void testGetMockHandler() {
        Object mockitoMock = createMock(SomeClass.class, new MockSettingsImpl());

        MockHandlerInterface mockHandler = getMockHandler(mockitoMock);
        assertNotNull(mockHandler);

        MockHandler mockHandlerFromInterceptor = getMockHandlerFromInterceptor(mockHandler);
        assertSame(mockHandler, mockHandlerFromInterceptor);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerOnNullMock() {
        getMockHandler(null);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerOnNonMockitoMock() {
        Object nonMockitoMock = new SomeClass();
        getMockHandler(nonMockitoMock);
    }

    @Test
    public void testGetMockHandlerFromInterceptor() {
        Object mockitoMock = createMock(SomeClass.class, new MockSettingsImpl());

        MockHandlerInterface mockHandler = getMockHandler(mockitoMock);
        MockHandler mockHandlerFromInterceptor = getMockHandlerFromInterceptor(mockHandler);
        assertSame(mockHandler, mockHandlerFromInterceptor);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerFromInterceptorOnNullMockHandler() {
        Object mockitoMock = createMock(SomeClass.class, new MockSettingsImpl());
        MockHandlerInterface nullMockHandler = null;
        getMockHandlerFromInterceptor(nullMockHandler);
    }

    @Test
    public void testGetMockName() {
        Object mockitoMock = createMock(SomeClass.class, new MockSettingsImpl());
        MockName mockName = getMockName(mockitoMock);
        assertNotNull(mockName);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockNameOnNullMock() {
        getMockName(null);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockNameOnNonMockitoMock() {
        Object nonMockitoMock = new SomeClass();
        getMockName(nonMockitoMock);
    }

    private <T> T createMock(Class<T> classToMock, MockSettingsImpl settings) {
        creationValidator.validateType(classToMock);
        creationValidator.validateExtraInterfaces(classToMock, settings.getExtraInterfaces());
        creationValidator.validateMockedType(classToMock, settings.getSpiedInstance());

        settings.initiateMockName(classToMock);

        MockHandler<T> mockHandler = new MockHandler<>(settings);
        MethodInterceptorFilter filter = new MethodInterceptorFilter(mockHandler, settings);
        Class<?>[] ancillaryTypes = settings.getExtraInterfaces() == null ? new Class<?>[0] : settings.getExtraInterfaces();

        Object spiedInstance = settings.getSpiedInstance();

        T mock = ClassImposterizer.INSTANCE.imposterise(filter, classToMock, ancillaryTypes);
        if (spiedInstance != null) {
            new LenientCopyTool().copyToMock(spiedInstance, mock);
        }

        return mock;
    }

    private void settingExtraInterfaces(MockSettingsImpl settings, Class<?> interfaceToMock) {
        List<Class<?>> extraInterfaces = new ArrayList<>();
        extraInterfaces.add(interfaceToMock);
        settings.setExtraInterfaces(extraInterfaces.toArray(new Class[0]));
    }

    private <T> MethodInterceptorFilter getInterceptor(T mock) {
        Factory factory = (Factory) mock;
        Callback callback = factory.getCallback(0);

        if (callback instanceof MethodInterceptorFilter) {
            return (MethodInterceptorFilter) callback;
        }

        return null;
    }

    private <T> MockHandlerInterface<T> getMockHandler(T mock) {
        if (mock == null) {
            throw new NotAMockException("Argument should be a mock, but is null!");
        }

        if (isMockitoMock(mock)) {
            return (MockHandlerInterface<T>) getInterceptor(mock).getHandler();
        } else {
            throw new NotAMockException("Argument should be a mock, but is: " + mock.getClass());
        }
    }

    private <T> MockHandlerFromInterceptor getMockHandlerFromInterceptor(MockHandlerInterface<T> mockHandler) {
        return (MockHandlerFromInterceptor) Whitebox.getInternalState(mockHandler, "imposterisedObject");
    }

    private boolean isMock(Object mock) {
        return mock != null && isMockitoMock(mock);
    }

    private <T> boolean isMockitoMock(T mock) {
        return Enhancer.isEnhanced(mock.getClass()) && getInterceptor(mock) != null;
    }

    private MockName getMockName(Object mock) {
        return getMockHandler(mock).getMockSettings().getMockName();
    }
}

class Factory {
    private Object callback;

    public void setCallback(int index, Callback callback) {
        this.callback = callback;
    }

    public Callback getCallback(int index) {
        return (Callback) this.callback;
    }
}

class MockHandler<T> {
    private MockSettingsImpl settings;

    public MockHandler(MockSettingsImpl settings) {
        this.settings = settings;
    }

    public MockSettingsImpl getMockSettings() {
        return this.settings;
    }
}

class MockHandlerFromInterceptor<T> {
    private T imposterisedObject;

    public T getHandler() {
        return this.imposterisedObject;
    }

    public void setImposterisedObject(T imposterisedObject) {
        this.imposterisedObject = imposterisedObject;
    }
}

class SomeClass {
}

interface SomeInterface {
}

class SomeInterfaceImpl implements SomeInterface {
}