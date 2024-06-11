package org.mockito.internal.creation.bytebuddy;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.exceptions.base.MockitoException;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ByteBuddyMockMaker.class, ClassInstantiator.class})
public class ByteBuddyMockMakerTest {

    @Test(expected = MockitoException.class)
    public void testCreateMockWithSerializableModeAcrossClassLoaders() {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        MockCreationSettings<Object> settings = new MockCreationSettings.Builder().withType(Object.class).withSerializableMode(SerializableMode.ACROSS_CLASSLOADERS).build();
        MockHandler handler = Mockito.mock(MockHandler.class);
        byteBuddyMockMaker.createMock(settings, handler);
    }

    @Test
    public void testCreateMock() {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        MockCreationSettings<DummyClass> settings = new MockCreationSettings.Builder().withType(DummyClass.class).build();
        MockHandler handler = Mockito.mock(MockHandler.class);
        DummyClass mock = byteBuddyMockMaker.createMock(settings, handler);
        assertNotNull(mock);
    }

    @Test(expected = ClassCastException.class)
    public void testCreateMockClassCastException() {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        MockCreationSettings<Object> settings = new MockCreationSettings.Builder().withType(String.class).build();
        MockHandler handler = Mockito.mock(MockHandler.class);
        Object mock = byteBuddyMockMaker.createMock(settings, handler);
        byteBuddyMockMaker.ensureMockIsAssignableToMockedType(settings, mock);
    }

    @Test(expected = InstantiationException.class)
    public void testCreateMockInstantiationException() {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        MockCreationSettings<DummyClassWithInstantiationException> settings = new MockCreationSettings.Builder().withType(DummyClassWithInstantiationException.class).build();
        MockHandler handler = Mockito.mock(MockHandler.class);
        byteBuddyMockMaker.createMock(settings, handler);
    }

    @Test
    public void testGetHandler() {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        DummyClass mock = mock(DummyClass.class);
        MockHandler handler = Mockito.mock(MockHandler.class);
        byteBuddyMockMaker.createMock(new MockCreationSettings.Builder().withType(DummyClass.class).build(), handler);
        MockHandler returnedHandler = byteBuddyMockMaker.getHandler(mock);
        assertNotNull(returnedHandler);
        assert(returnedHandler == handler);
    }

    @Test
    public void testResetMock() {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        DummyClass mock = mock(DummyClass.class);
        MockHandler handler = Mockito.mock(MockHandler.class);
        byteBuddyMockMaker.createMock(new MockCreationSettings.Builder().withType(DummyClass.class).build(), handler);
        byteBuddyMockMaker.resetMock(mock, Mockito.mock(MockHandler.class),
                new MockCreationSettings.Builder().withType(DummyClass.class).build());
        MockHandler newHandler = byteBuddyMockMaker.getHandler(mock);
        assertNull(newHandler);
    }

    @Test
    public void testInitializeClassInstantiator() throws Exception {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        PowerMockito.mockStatic(ClassInstantiator.class);
        when(ClassInstantiator.class.getDeclaredConstructor(boolean.class)).thenReturn(mock(Constructor.class));
        byteBuddyMockMaker.initializeClassInstantiator();
        verify(ClassInstantiator.class).getDeclaredConstructor(any(boolean.class));
    }

    @Test(expected = IllegalStateException.class)
    public void testInitializeClassInstantiatorWithObjenesisMissing() throws Exception {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        PowerMockito.mockStatic(ClassInstantiator.class);
        when(ClassInstantiator.class.getDeclaredConstructor(boolean.class)).thenThrow(new InstantiationException("Objenesis is missing on the classpath."));
        byteBuddyMockMaker.initializeClassInstantiator();
    }
}

class DummyClass {
}

class DummyClassWithInstantiationException {
    public DummyClassWithInstantiationException() throws InstantiationException {
        throw new InstantiationException();
    }
}