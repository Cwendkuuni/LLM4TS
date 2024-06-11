package org.mockito.internal.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.MockHandler;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.creation.MethodInterceptorFilter;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.util.reflection.LenientCopyTool;

import java.io.Serializable;

public class MockUtilTest {

    @Test
    public void testCreateMock() {
        MockUtil mockUtil = new MockUtil();
        ClassToMock mock = mockUtil.createMock(ClassToMock.class, new MockSettingsImpl());

        assertNotNull(mock);
        assertTrue(mock instanceof ClassToMock);
    }

    @Test
    public void testResetMock() {
        MockUtil mockUtil = new MockUtil();
        ClassToMock mock = mockUtil.createMock(ClassToMock.class, new MockSettingsImpl());

        mockUtil.resetMock(mock);
        assertTrue(mock instanceof ClassToMock);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerWithNull() {
        MockUtil mockUtil = new MockUtil();
        mockUtil.getMockHandler(null);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerWithNonMockObject() {
        MockUtil mockUtil = new MockUtil();
        mockUtil.getMockHandler(new Object());
    }

    @Test
    public void testGetMockHandlerWithMockitoMock() {
        MockUtil mockUtil = new MockUtil();
        ClassToMock mock = mockUtil.createMock(ClassToMock.class, new MockSettingsImpl());

        MockHandlerInterface<ClassToMock> handler = mockUtil.getMockHandler(mock);
        assertNotNull(handler);
        assertTrue(handler instanceof MockHandler);
    }

    @Test
    public void testIsMockWithMockObject() {
        MockUtil mockUtil = new MockUtil();
        ClassToMock mock = mockUtil.createMock(ClassToMock.class, new MockSettingsImpl());

        assertTrue(mockUtil.isMock(mock));
    }

    @Test
    public void testIsMockWithNonMockObject() {
        MockUtil mockUtil = new MockUtil();
        assertFalse(mockUtil.isMock(new Object()));
    }

    @Test
    public void testGetMockName() {
        MockUtil mockUtil = new MockUtil();
        ClassToMock mock = mockUtil.createMock(ClassToMock.class, new MockSettingsImpl());

        MockName mockName = mockUtil.getMockName(mock);
        assertNotNull(mockName);
    }
}

class ClassToMock {}