package org.mockito.internal.creation.bytebuddy;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.creation.instance.ClassInstantiator;
import org.mockito.internal.creation.instance.CachingMockBytecodeGenerator;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.SerializableMode;
import org.mockito.plugins.MockMaker;

public class ByteBuddyMockMakerTest {

    private ByteBuddyMockMaker byteBuddyMockMaker;
    private ClassInstantiator classInstantiator;
    private CachingMockBytecodeGenerator cachingMockBytecodeGenerator;

    @Before
    public void setup() {
        byteBuddyMockMaker = new ByteBuddyMockMaker();
        classInstantiator = byteBuddyMockMaker.classInstantiator;
        cachingMockBytecodeGenerator = byteBuddyMockMaker.cachingMockBytecodeGenerator;
    }

    @Test
    public void testCreateMockWithNullSettingsAndHandler() {
        assertNull(byteBuddyMockMaker.createMock(null, null));
    }

    @Test
    public void testCreateMockWithInvalidSerializableMode() {
        MockCreationSettings<Object> settings = new MockCreationSettings.Builder().serializableMode(SerializableMode.ACROSS_CLASSLOADERS).build();
        assertThrows(MockitoException.class, () -> byteBuddyMockMaker.createMock(settings, null));
    }

    @Test
    public void testCreateMockWithValidSerializableMode() {
        MockCreationSettings<Object> settings = new MockCreationSettings.Builder().serializableMode(SerializableMode.SERIAL).build();
        Object mock = byteBuddyMockMaker.createMock(settings, mock(MockHandler.class));
        assertInstanceOf(Object.class, mock);
    }

    @Test
    public void testCreateMockWithMockedClassAndHandler() {
        MockCreationSettings<Object> settings = new MockCreationSettings.Builder().typeToMock(String.class).serializableMode(SerializableMode.SERIAL).build();
        MockHandler handler = mock(MockHandler.class);
        Object mock = byteBuddyMockMaker.createMock(settings, handler);
        assertInstanceOf(String.class, mock);
    }

    @Test
    public void testCreateMockWithMockedClassThatThrowsInstantiationException() {
        Class<Throwable> exceptionClass = Throwable.class;
        MockCreationSettings<Throwable> settings = new MockCreationSettings.Builder().typeToMock(exceptionClass).serializableMode(SerializableMode.SERIAL).build();
        assertThrows(MockitoException.class, () -> byteBuddyMockMaker.createMock(settings, null));
    }

    @Test
    public void testCreateMockWithMockedClassThatIsNotAssignableToMockedType() {
        Class<?> notAssignableToMockedTypeClass = Object.class;
        MockCreationSettings<Object> settings = new MockCreationSettings.Builder().typeToMock(String.class).serializableMode(SerializableMode.SERIAL).build();
        Object mock = byteBuddyMockMaker.createMock(settings, spy(notAssignableToMockedTypeClass));
        assertInstanceOf(String.class, mock);
    }

    @Test
    public void testGetHandler() {
        Object mock = byteBuddyMockMaker.createMock(new MockCreationSettings.Builder().typeToMock(String.class).serializableMode(SerializableMode.SERIAL).build(), mock(MockHandler.class));
        MockHandler handler = byteBuddyMockMaker.getHandler(mock);
        assertInstanceOf(MockHandler.class, handler);
    }

    @Test
    public void testResetMock() {
        Object mock = byteBuddyMockMaker.createMock(new MockCreationSettings.Builder().typeToMock(String.class).serializableMode(SerializableMode.SERIAL).build(), mock(MockHandler.class));
        MockHandler newHandler = mock(MockHandler.class);
        byteBuddyMockMaker.resetMock(mock, newHandler, new MockCreationSettings.Builder().typeToMock(String.class).serializableMode(SerializableMode.SERIAL).build());
        verify(newHandler).handle(eq(eq(isA(String.class))), eq(any()));
        verifyZeroInteractions(mock);
    }

    private void assertThrows(Class<? extends Throwable> expectedExceptionClass, Runnable runnable) {
        try {
            runnable.run();
        } catch (Throwable e) {
            if (expectedExceptionClass.isInstance(e)) {
                throw (ExpectedException) e;
            }
            throw new AssertionError("Unexpected exception: " + e.getClass().getName());
        }
    }
}