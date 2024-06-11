package org.mockito.internal.creation.bytebuddy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.SerializableMode;


public class ByteBuddyMockMakerTest {

    private ByteBuddyMockMaker mockMaker;

    @Before
    public void setUp() {
        mockMaker = new ByteBuddyMockMaker();
    }

    @Test
    public void testCreateMock() {
        MockCreationSettings<Object> settings = mock(MockCreationSettings.class);
        MockHandler handler = mock(MockHandler.class);

        Object mockInstance = mockMaker.createMock(settings, handler);

        assertNotNull(mockInstance);
    }

    @Test(expected = MockitoException.class)
    public void testCreateMockWithSerializableModeAcrossClassLoaders() {
        MockCreationSettings<Object> settings = mock(MockCreationSettings.class);
        MockHandler handler = mock(MockHandler.class);

        settings.serializable(SerializableMode.ACROSS_CLASSLOADERS);

        mockMaker.createMock(settings, handler);
    }

    @Test
    public void testEnsureMockIsAssignableToMockedType() {
        MockCreationSettings<Object> settings = mock(MockCreationSettings.class);
        Object mock = new Object();

        Object result = mockMaker.ensureMockIsAssignableToMockedType(settings, mock);

        assertNotNull(result);
    }

    @Test
    public void testEnsureMockIsAssignableToMockedTypeWithNullMock() {
        MockCreationSettings<Object> settings = mock(MockCreationSettings.class);
        Object mock = null;

        Object result = mockMaker.ensureMockIsAssignableToMockedType(settings, mock);

        assertNull(result);
    }

    @Test
    public void testGetHandler() {
        Object mock = mock(Object.class);

        MockHandler handler = mockMaker.getHandler(mock);

        assertNotNull(handler);
    }

    @Test
    public void testGetHandlerWithNonMockMethodInterceptorMock() {
        Object mock = new Object();

        MockHandler handler = mockMaker.getHandler(mock);

        assertNull(handler);
    }

    @Test
    public void testResetMock() {
        Object mock = mock(Object.class);
        MockHandler newHandler = mock(MockHandler.class);
        MockCreationSettings<Object> settings = mock(MockCreationSettings.class);

        mockMaker.resetMock(mock, newHandler, settings);

        MockHandler handler = mockMaker.getHandler(mock);
        assertNotNull(handler);
    }

    @Test(expected = MockitoException.class)
    public void testResetMockWithNonInternalMockHandler() {
        Object mock = mock(Object.class);
        MockHandler newHandler = mock(MockHandler.class);
        MockCreationSettings<Object> settings = mock(MockCreationSettings.class);

        mockMaker.resetMock(mock, newHandler, settings);
    }

}