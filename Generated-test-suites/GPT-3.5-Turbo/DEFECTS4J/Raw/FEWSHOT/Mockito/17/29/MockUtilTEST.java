
package org.mockito.internal.util;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.RETURNS_DEFAULTS;
import static org.mockito.Mockito.withSettings;

import org.junit.Before;
import org.junit.Test;
import org.mockito.cglib.proxy.Callback;
import org.mockito.cglib.proxy.Enhancer;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.MockHandler;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.creation.MethodInterceptorFilter;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.util.reflection.LenientCopyTool;

@SuppressWarnings("unchecked")
public class MockUtilTest {
    private MockUtil mockUtil;

    @Before
    public void setUp() {
        mockUtil = new MockUtil();
    }

    @Test
    public void testCreateMock() {
        ClassToMock classToMock = new ClassToMock();
        MockSettingsImpl settings = new MockSettingsImpl();
        Object mock = mockUtil.createMock(classToMock.getClass(), settings);

        // Verify that mock is of correct type
        assertEquals(classToMock.getClass(), mock.getClass());

        // Verify that mock handler is set correctly
        MockHandlerInterface mockHandler = mockUtil.getMockHandler(mock);
        assertEquals(MockHandler.class, mockHandler.getClass());

        // Verify that callback is set correctly
        Callback callback = ((Factory) mock).getCallback(0);
        assertEquals(MethodInterceptorFilter.class, callback.getClass());
    }

    @Test
    public void testResetMock() {
        ClassToMock classToMock = new ClassToMock();
        Object mock = Enhancer.create(classToMock.getClass());

        // Reset the mock
        mockUtil.resetMock(mock);

        // Verify that mock handler is replaced with a new one
        MockHandlerInterface mockHandler = mockUtil.getMockHandler(mock);
        assertEquals(MockHandler.class, mockHandler.getClass());

        // Verify that callback is replaced with a new one
        Callback callback = ((Factory) mock).getCallback(0);
        assertEquals(MethodInterceptorFilter.class, callback.getClass());
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerWithNullMock() {
        mockUtil.getMockHandler(null);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerWithNonMockitoMock() {
        ClassToMock classToMock = new ClassToMock();
        Object mock = classToMock;
        mockUtil.getMockHandler(mock);
    }

    @Test
    public void testGetMockHandlerWithMockitoMock() {
        ClassToMock classToMock = new ClassToMock();
        Object mock = Enhancer.create(classToMock.getClass());

        // Get the mock handler
        MockHandlerInterface mockHandler = mockUtil.getMockHandler(mock);

        // Verify that mock handler is set correctly
        assertEquals(MockHandler.class, mockHandler.getClass());
    }

    @Test
    public void testIsMockWithMockitoMock() {
        ClassToMock classToMock = new ClassToMock();
        Object mock = Enhancer.create(classToMock.getClass());

        // Check if the object is a mock
        boolean isMock = mockUtil.isMock(mock);

        // Verify that isMock returns true
        assertEquals(true, isMock);
    }

    @Test
    public void testIsMockWithNonMockitoMock() {
        ClassToMock classToMock = new ClassToMock();
        Object mock = classToMock;

        // Check if the object is a mock
        boolean isMock = mockUtil.isMock(mock);

        // Verify that isMock returns false
        assertEquals(false, isMock);
    }

    @Test
    public void testGetMockName() {
        ClassToMock classToMock = new ClassToMock();
        Object mock = Enhancer.create(classToMock.getClass());

        // Get the mock name
        MockName mockName = mockUtil.getMockName(mock);

        // Verify that the mock name is not null
        assertNotNull(mockName);
    }
}

class ClassToMock {}
