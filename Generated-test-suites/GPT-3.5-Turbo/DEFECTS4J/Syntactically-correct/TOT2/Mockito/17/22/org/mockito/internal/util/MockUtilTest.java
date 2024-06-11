package org.mockito.internal.util;

import org.junit.*;
import static org.junit.Assert.*;
import org.mockito.*;
import org.mockito.exceptions.misusing.NotAMockException;

public class MockUtilTest {

    MockUtil mockUtil;

    @Before
    public void setUp() {
        mockUtil = new MockUtil();
    }

    @Test
    public void testCreateMock() {
        // Typical case
        SomeClass mock = mockUtil.createMock(SomeClass.class, new MockSettingsImpl());
        assertNotNull(mock);

        // Edge cases
        // Testing with extra interfaces
        SomeClass mockWithInterface = mockUtil.createMock(SomeClass.class, new MockSettingsImpl().extraInterfaces(SomeInterface.class));
        assertNotNull(mockWithInterface);

        // Testing with spied instance
        SomeClass spiedInstance = new SomeClass();
        SomeClass spiedMock = mockUtil.createMock(SomeClass.class, new MockSettingsImpl().spiedInstance(spiedInstance));
        assertNotNull(spiedMock);
        assertSame(spiedInstance, spiedMock);
    }

    @Test
    public void testResetMock() {
        SomeClass mock = mockUtil.createMock(SomeClass.class, new MockSettingsImpl());
        mockUtil.resetMock(mock);
        assertNotNull(mock);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandler_NullMock() {
        mockUtil.getMockHandler(null);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandler_NonMock() {
        SomeClass nonMock = new SomeClass();
        mockUtil.getMockHandler(nonMock);
    }

    @Test
    public void testIsMock() {
        SomeClass mock = mockUtil.createMock(SomeClass.class, new MockSettingsImpl());
        assertTrue(mockUtil.isMock(mock));

        SomeClass nonMock = new SomeClass();
        assertFalse(mockUtil.isMock(nonMock));
    }

    @Test
    public void testGetMockName() {
        
        SomeClass mock = mockUtil.createMock(SomeClass.class, new MockSettingsImpl());
        MockName mockName = mockUtil.getMockName(mock);
        assertNotNull(mockName);
    }

}