package org.mockito.internal.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;

import org.junit.Test;

public class MockUtilTest {
    @Test
    public void testCreateMock() {
        MockCreationValidator creationValidatorMock = mock(MockCreationValidator.class);
        MockUtil mockUtil = new MockUtil(creationValidatorMock);
        Class<Foo> classToMock = Foo.class;
        MockSettingsImpl settings = new MockSettingsImpl();

        Foo mock = mockUtil.createMock(classToMock, settings);

        assertEquals(classToMock, mock.getClass());
        verify(creationValidatorMock).validateType(classToMock);
        verify(creationValidatorMock).validateExtraInterfaces(classToMock, settings.getExtraInterfaces());
        verify(creationValidatorMock).validateMockedType(classToMock, settings.getSpiedInstance());
    }

    @Test
    public void testResetMock() {
        Foo mock = mock(Foo.class);

        mockUtil.resetMock(mock);

        verify(((Factory) mock).getCallback(0)).setCallback(0, newFilter);
    }

    @Test
    public void testGetMockHandler() {
        Foo mock = mock(Foo.class);

        MockHandlerInterface<Foo> handler = mockUtil.getMockHandler(mock);

        assertEquals(((Factory) mock).getCallback(0).getHandler(), handler);
    }

    @Test
    public void testIsMock() {
        Foo mock = mock(Foo.class);

        boolean isMock = mockUtil.isMock(mock);

        assertTrue(isMock);
    }

    @Test
    public void testGetMockName() {
        Foo mock = mock(Foo.class);

        MockName mockName = mockUtil.getMockName(mock);

        assertEquals(mock.getMockSettings().getMockName(), mockName);
    }
}