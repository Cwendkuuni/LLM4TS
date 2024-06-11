
package org.mockito.internal.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.MockHandlerInterface;

public class MockUtilTest {

    private MockCreationValidator creationValidator;

    @Before
    public void setup() {
        creationValidator = mock(MockCreationValidator.class);
    }

    @Test
    public void testCreateMock() {
        MockSettingsImpl settings = mock(MockSettingsImpl.class);
        Class<?> classToMock = Object.class;

        MockUtil mockUtil = new MockUtil(creationValidator);
        mockUtil.createMock(classToMock, settings);

        verify(creationValidator).validateType(classToMock);
        verify(creationValidator).validateExtraInterfaces(classToMock, settings.getExtraInterfaces());
        verify(creationValidator).validateMockedType(classToMock, settings.getSpiedInstance());
        verify(settings).initiateMockName(classToMock);
        // Validate other method calls in createMock()
    }

    @Test
    public void testResetMock() {
        Object mock = mock(Object.class);

        MockUtil mockUtil = new MockUtil();
        mockUtil.resetMock(mock);

        // Validate reset operation
    }

    @Test
    public void testGetMockHandlerWithValidMock() {
        Object mock = mock(Object.class);
        MockHandlerInterface<Object> mockHandler = mock(MockHandlerInterface.class);
        when(mockHandler.getHandler()).thenReturn(mockHandler);

        MockUtil mockUtil = new MockUtil();
        MockHandlerInterface<Object> result = mockUtil.getMockHandler(mock);

        assertEquals(mockHandler, result);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerWithNullMock() {
        Object mock = null;

        MockUtil mockUtil = new MockUtil();
        mockUtil.getMockHandler(mock);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerWithInvalidMock() {
        Object mock = new Object();

        MockUtil mockUtil = new MockUtil();
        mockUtil.getMockHandler(mock);
    }

    @Test
    public void testIsMockWithValidMock() {
        Object mock = mock(Object.class);

        MockUtil mockUtil = new MockUtil();
        boolean result = mockUtil.isMock(mock);

        assertEquals(true, result);
    }

    @Test
    public void testIsMockWithNullMock() {
        Object mock = null;

        MockUtil mockUtil = new MockUtil();
        boolean result = mockUtil.isMock(mock);

        assertEquals(false, result);
    }

    @Test
    public void testIsMockWithInvalidMock() {
        Object mock = new Object();

        MockUtil mockUtil = new MockUtil();
        boolean result = mockUtil.isMock(mock);

        assertEquals(false, result);
    }

    @Test
    public void testGetMockName() {
        Object mock = mock(Object.class);
        MockHandlerInterface<Object> mockHandler = mock(MockHandlerInterface.class);
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        MockName mockName = new MockName();

        when(mockHandler.getMockSettings()).thenReturn(mockSettings);
        when(mockSettings.getMockName()).thenReturn(mockName);

        MockUtil mockUtil = new MockUtil();
        MockName result = mockUtil.getMockName(mock);

        assertEquals(mockName, result);
    }
}
