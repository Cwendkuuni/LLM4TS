package org.mockito.internal.creation.bytebuddy;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.lang.reflect.Constructor;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.internal.creation.instance.ClassInstantiator;
import org.mockito.internal.creation.instance.CachingMockBytecodeGenerator;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.SerializableMode;
import org.mockito.plugins.MockMaker;

public class ByteBuddyMockMakerTest {

    @Test
    public void testCreateMockWithNullSettingsAndHandler() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        assertNull(mockMaker.createMock(null, null));
    }

    @Test
    public void testCreateMockWithInvalidSerializableMode() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockCreationSettings<Object> settings = new MockCreationSettings.Builder().withTypeToMock(Object.class).withSerializableMode(SerializableMode.ACROSS_CLASSLOADERS).build();
        assertThrows(MockitoException.class, () -> mockMaker.createMock(settings, null));
    }

    @Test
    public void testCreateMockWithValidSettingsAndHandler() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockCreationSettings<Object> settings = new MockCreationSettings.Builder().withTypeToMock(Object.class).build();
        MockHandler handler = mock(MockHandler.class);
        Object mock = mockMaker.createMock(settings, handler);
        assertNotNull(mock);
    }

    @Test
    public void testCreateMockWithInvalidMockHandler() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        Object mock = mockMaker.createMock(null, "invalidMockHandler");
        assertNull(mock);
    }

    @Test
    public void testGetHandler() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        Object mock = mock(Object.class);
        MockHandler handler = mockMaker.getHandler(mock);
        assertNotNull(handler);
    }

    @Test
    public void testResetMock() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        Object mock = mock(Object.class);
        MockHandler handler = mock(MockHandler.class);
        mockMaker.resetMock(mock, handler, null);
        assertNull(mockMaker.getHandler(mock));
        MockMethodInterceptor interceptor = (MockMethodInterceptor) mock;
        assertNotNull(interceptor.getMockitoInterceptor());
        mockMaker.resetMock(mock, handler, null);
        assertNull(interceptor.getMockitoInterceptor());
    }

    @Test
    public void testInitializeClassInstantiator() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        assertNotNull(mockMaker.initializeClassInstantiator());
    }

    @Test
    public void testAsInternalMockHandler() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockHandler handler = mock(MockHandler.class);
        InternalMockHandler internalMockHandler = mock(InternalMockHandler.class);
        internalMockHandler.setMockitoInterceptor(handler);
        assertEquals(handler, mockMaker.asInternalMockHandler(internalMockHandler));
    }

    private <T> void assertThrows(Class<T> exceptionClass, Runnable runnable) {
        try {
            runnable.run();
        } catch (Throwable e) {
            if (exceptionClass.isInstance(e)) {
                return;
            }
            throw new AssertionError("Expected " + exceptionClass.getName() + " but was " + e.getClass().getName());
        }
        throw new AssertionError("Expected " + exceptionClass.getName() + " but was not thrown");
    }
}