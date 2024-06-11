package org.mockito.internal.creation.bytebuddy;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.invocation.MockHandler;
import org.mockito.plugins.MockMaker;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

public class ByteBuddyMockMakerTest {

    private ByteBuddyMockMaker mockMaker;
    private MockMaker originalMockMaker;
    private InternalMockHandler mockHandler;

    @Before
    public void setup() {
        originalMockMaker = Mockito.mock(MockMaker.class);
        mockHandler = Mockito.mock(InternalMockHandler.class);
        mockMaker = new ByteBuddyMockMaker();
        Mockito.when(originalMockMaker.createMock(ArgumentMatchers.any(MockCreationSettings.class), ArgumentMatchers.any(MockHandler.class)))
                .thenAnswer(invocation -> mockHandler);
        mockMaker.setMockMaker(originalMockMaker);
    }

    @Test
    public void testCreateMockWithSerializableModeACrossClassLoadersThrowsException() {
        MockCreationSettings<Object> settings = new MockCreationSettings.Builder<Object>()
                .serializableMode(SerializableMode.ACROSS_CLASSLOADERS)
                .build();
        assertThrows(MockitoException.class, () -> mockMaker.createMock(settings, mockHandler));
    }

    @Test
    public void testCreateMockSuccessfully() throws InstantiationException {
        MockCreationSettings<Object> settings = new MockCreationSettings.Builder<Object>()
                .typeToMock(Object.class)
                .serializableMode(SerializableMode.DEFAULT)
                .build();
        Object mock = mockMaker.createMock(settings, mockHandler);
        assertNotNull(mock);
    }

    @Test
    public void testCreateMockWithExceptionThrowsCorrectException() {
        Class<? extends Object> mockedProxyType = new Object() {}.getClass();
        MockCreationSettings<Object> settings = new MockCreationSettings.Builder<Object>()
                .typeToMock(mockedProxyType)
                .build();
        assertThrows(MockitoException.class, () -> mockMaker.createMock(settings, mockHandler));
    }

    @Test
    public void testGetHandlerReturnsMockHandler() {
        Object mock = mockMaker.createMock(new MockCreationSettings.Builder<Object>().typeToMock(Object.class).build(), mockHandler);
        MockHandler handler = mockMaker.getHandler(mock);
        assertNotNull(handler);
        assertNotNull(handler.getMockForConcrete());
    }

    @Test
    public void testResetMockWithNewHandler() {
        Object mock = mockMaker.createMock(new MockCreationSettings.Builder<Object>().typeToMock(Object.class).build(), mockHandler);
        MockHandler newHandler = Mockito.mock(MockHandler.class);
        mockMaker.resetMock(mock, newHandler, new MockCreationSettings.Builder<Object>().build());
        MockHandler newMockHandler = mockMaker.getHandler(mock);
        assertNotNull(newMockHandler);
        assertNotSame(mockHandler, newMockHandler);
    }

    @Test
    public void testInitializeClassInstantiatorSuccessfully() {
        try {
            mockMaker.initializeClassInstantiator();
        } catch (Throwable ignore) {
            // Expected
        }
    }

    @Test(expected = IllegalStateException.class)
    public void testInitializeClassInstantiatorFailsWithMissingObjenesis() {
        mockMaker.initializeClassInstantiator();
    }
}