package org.mockito.internal.util;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.internal.util.MockUtil.createMock;
import static org.mockito.internal.util.MockUtil.getMockHandler;
import static org.mockito.internal.util.MockUtil.isMock;
import static org.mockito.internal.util.MockUtil.resetMock;
import static org.mockito.internal.util.MockUtil.stripLeadingAndTrailingQuotes;

import org.junit.Before;
import org.junit.Test;
import org.mockito.cglib.proxy.Factory;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.MockHandler;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.MockSettingsImpl;
import org.mockito.internal.InvocationNotifierHandler;
import org.mockito.internal.util.reflection.LenientCopyTool;
import org.mockito.internal.util.reflection.ClassImposterizer;
import org.mockito.internal.util.reflection.ClassUtils;
import org.mockito.internal.util.reflection.MethodInterceptorFilter;

import java.io.Serializable;
import java.lang.reflect.Proxy;

public class MockUtilTest {
    private MockUtil mockUtil;
    private Class<Object> mockClass;
    private MockSettingsImpl mockSettings;
    private Factory mockFactory;

    @Before
    public void setUp() {
        mockUtil = new MockUtil();
        mockClass = Object.class;
        mockSettings = new MockSettingsImpl();
        mockFactory = (Factory) Proxy.newProxyInstance(Object.class.getClassLoader(), new Class[] {Factory.class}, new MockInterceptor());
    }

    @Test
    public void testCreateMock() {
        Object mockedObject = new Object() {};
        Object mock = createMock(mockClass, mockSettings);
        assertNotNull(mock);
        assertNull(getMockHandler(mockedObject).getMockName());
        assertEquals(mockClass, getMockHandler(mock).getMockSettings().getMockType());
        assertEquals(mockClass, getMockHandler(mock).getMockSettings().getMockType());
        assertNull(getMockHandler(mock).getMockSettings().getExtraInterfaces());
    }

    @Test
    public void testCreateMockWithExtraInterfaces() {
        Object mockedObject = new Object() {};
        Class<Serializable> serializableClass = Serializable.class;
        mockSettings.setExtraInterfaces(new Class<?>[]{serializableClass});
        Object mock = createMock(mockClass, mockSettings);
        assertNotNull(mock);
        assertEquals(mockClass, getMockHandler(mock).getMockSettings().getMockType());
        assertArrayEquals(new Class[]{serializableClass}, getMockHandler(mock).getMockSettings().getExtraInterfaces());
    }

    @Test(expected = NotAMockException.class)
    public void testCreateMockWithNonMockInstance() {
        Object mockedObject = new Object() {};
        MockHandlerInterface mockHandler = getMockHandler(mockedObject);
        Object mock = mockUtil.createMock(mockClass, mockSettings);
        assertNotNull(mock);
        mockUtil.resetMock(mock);
        mockHandler = getMockHandler(mockedObject);
        mockHandler.stubs().stubReturnValue("value").when(mockedObject).someMethod();
        mockHandler.verify(mockedObject).someMethod();
    }

    @Test(expected = NotAMockException.class)
    public void testCreateMockWithNullClassToMock() {
        mockUtil.createMock(null, mockSettings);
    }

    @Test(expected = NotAMockException.class)
    public void testCreateMockWithNullMockSettings() {
        mockUtil.createMock(mockClass, null);
    }

    @Test(expected = NotAMockException.class)
    public void testCreateMockWithNullSpiedInstance() {
        mockSettings.setSpiedInstance(null);
        mockUtil.createMock(mockClass, mockSettings);
    }

    @Test
    public void testResetMock() {
        Object mockedObject = new Object() {};
        Object mock = createMock(mockClass, mockSettings);
        resetMock(mock);
        MockHandlerInterface mockHandler = getMockHandler(mockedObject);
        assertNull(mockHandler.getMockSettings().getMockName());
        mockHandler.reset();
        mockHandler.verifyNoInteractions();
    }

    @Test
    public void testIsMock() {
        Object mockedObject = new Object() {};
        Object mock = createMock(mockClass, mockSettings);
        assertTrue(isMock(mock));
        assertFalse(isMock(mockedObject));
    }

    @Test
    public void testGetMockName() {
        Object mockedObject = new Object() {};
        Object mock = createMock(mockClass, mockSettings);
        assertEquals("MockitoMock_<" + mockClass.getName() + ">", getMockName(mock));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        assertEquals("foo", stripLeadingAndTrailingQuotes("\"foo\""));
        assertEquals("foo ", stripLeadingAndTrailingQuotes("foo \"bar\""));
        assertEquals("\"foo\" bar", stripLeadingAndTrailingQuotes("\"foo\" bar"));
        assertEquals("\"foo\" and \"bar\"", stripLeadingAndTrailingQuotes("\"foo\" and \"bar\""));
        assertEquals("", stripLeadingAndTrailingQuotes("\"\""));
    }

    private MockName getMockName(Object mock) {
        return getMockHandler(mock).getMockSettings().getMockName();
    }

    private static class MockInterceptor implements Callback {
        @Override
        public int callback(MethodInterceptor arg0, Object arg1, Object[] arg2, int arg3) {
            return 0;
        }

        @Override
        public Object getHandler() {
            return new MockHandler<Object>(mockSettings);
        }
    }
}