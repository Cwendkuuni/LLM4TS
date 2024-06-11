package org.mockito.internal.util;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.LenientCopyTool;
import org.mockito.invocation.MethodInterceptor;
import org.mockito.internal.util.reflection.Whitebox;

public class MockUtilTest {

    @Test
    public void testCreateMock() {
        Class<String> stringClass = String.class;
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.setExtraInterfaces(new Class[] { MyClass.class });
        settings.setSpiedInstance("Initial value");

        MockUtil mockUtil = new MockUtil();
        String mock = mockUtil.createMock(stringClass, settings);

        assertNotNull(mock);
        assertEquals("Initial value", Whitebox.getInternalState(mock, "name"));
        assertNull(Whitebox.getInternalState(mock, "handler"));

        settings.setExtraInterfaces(new Class[] { MyClass.class, AnotherClass.class });
        mock = mockUtil.createMock(stringClass, settings);

        assertNotNull(mock);
        assertEquals("Initial value [MyClass, AnotherClass]", Whitebox.getInternalState(mock, "name"));
        assertNotNull(Whitebox.getInternalState(mock, "handler"));
        verify(Whitebox.getInternalState(mock, "handler"), Mockito.times(1)).answer(Mockito.eq("Initial value"), Mockito.anyInt());
    }

    @Test
    public void testResetMock() {
        Class<String> stringClass = String.class;
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.setExtraInterfaces(new Class[] { MyClass.class });
        settings.setSpiedInstance("Initial value");

        MockUtil mockUtil = new MockUtil();
        String mock = mockUtil.createMock(stringClass, settings);
        MethodInterceptorFilter filter = Whitebox.getInternalState(mock, "filter");

        mockUtil.resetMock(mock);
        assertNotNull(Whitebox.getInternalState(mock, "handler"));
        assertNull(Whitebox.getInternalState(mock, "filter"));
        verify(filter, Mockito.times(1)).answer(Mockito.eq("Initial value"), Mockito.anyInt());
    }

    @Test
    public void testGetMockHandler() {
        Class<String> stringClass = String.class;
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.setExtraInterfaces(new Class[] { MyClass.class });
        settings.setSpiedInstance("Initial value");
        String mock = mockUtil.createMock(stringClass, settings);

        MockUtil mockUtil1 = new MockUtil();
        assertNotNull(mockUtil1.getMockHandler(mock));
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerNull() {
        String mock = new String("Initial value");
        MockUtil mockUtil = new MockUtil();
        mockUtil.getMockHandler(mock);
    }

    @Test
    public void testIsMockitoMock() {
        Class<String> stringClass = String.class;
        String mock = mockUtil.createMock(stringClass, new MockSettingsImpl());
        Class<?> mockClass = mock.getClass();

        assertFalse(mockUtil.isMockitoMock(new Object()));
        assertTrue(mockUtil.isMockitoMock(mock));
        assertTrue(mockUtil.isMockitoMock(mockClass.getEnclosingClass()));
        assertFalse(mockUtil.isMockitoMock(mockClass));
    }

    @Test
    public void testIsMock() {
        String mock = mockUtil.createMock(String.class, new MockSettingsImpl());
        assertFalse(mockUtil.isMock(new Object()));
        assertTrue(mockUtil.isMock(mock));
    }

    @Test
    public void testGetInterceptor() {
        Class<String> stringClass = String.class;
        String mock = mockUtil.createMock(stringClass, new MockSettingsImpl());

        MethodInterceptorFilter filter = Whitebox.getInternalState(mock, "filter");
        MethodInterceptor interceptor = Whitebox.getInternalState(mock, "interceptor");

        assertEquals(filter, mockUtil.getInterceptor(mock));
    }

    @Test
    public void testGetMockName() {
        String mockName = "mockName";
        Class<String> stringClass = String.class;
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.setMockName(mockName);

        MockUtil mockUtil = new MockUtil();
        String mock = mockUtil.createMock(stringClass, settings);

        assertEquals(mockName, mockUtil.getMockName(mock));
    }

    @Test
    public void testGetMockNameNull() {
        String mock = new String("Initial value");
        MockUtil mockUtil = new MockUtil();
        mockUtil.getMockName(mock);
    }

    @Test
    public void testValidateType() {
        MockUtil mockUtil = new MockUtil();
        try {
            mockUtil.validateType(new Object());
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        mockUtil.validateType(String.class);
    }

    @Test
    public void testValidateExtraInterfaces() {
        MockUtil mockUtil = new MockUtil();
        try {
            mockUtil.validateExtraInterfaces(String.class, new Class[0]);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        mockUtil.validateExtraInterfaces(String.class, new Class[] { MyClass.class });
    }

    @Test
    public void testValidateMockedType() {
        MockUtil mockUtil = new MockUtil();
        try {
            mockUtil.validateMockedType(int.class, new Object());
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            mockUtil.validateMockedType(String.class, null);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }

        mockUtil.validateMockedType(String.class, "Initial value");
    }

    static class MyClass {}
    static class AnotherClass {}

}