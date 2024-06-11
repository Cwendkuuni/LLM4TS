package org.mockito.internal.creation.bytebuddy;

import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.plugins.MockMaker;
import org.mockito.plugins.MockMakerAnnotations;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ByteBuddyMockMakerTest {

    private ByteBuddyMockMaker byteBuddyMockMaker;

    @Mock
    private InternalMockHandler internalMockHandler;

    @Mock
    private ClassInstantiator classInstantiator;

    @Mock
    private CachingMockBytecodeGenerator cachingMockBytecodeGenerator;

    @Before
    public void setUp() {
        byteBuddyMockMaker = new ByteBuddyMockMaker();
        Mockito.initMocks(this);

        when(classInstantiator.instantiate(ArgumentMatchers.any(Class.class)))
                .then(invocation -> spy(invocation.getArgument(0)));

        doThrow(new IllegalStateException("Test exception"))
                .when(classInstantiator)
                .instantiate(ArgumentMatchers.any(Class.class));

        when(cachingMockBytecodeGenerator.get(ArgumentMatchers.any(Class.class), ArgumentMatchers.anyArray(Class.class)))
                .thenReturn(Mockito.mock(Class.class));
    }

    @org.junit.Test
    public void testCreateMock() {
        Class<?> typeToMock = Mockito.mock(Class.class);
        MockHandler handler = internalMockHandler;

        MockCreationSettings<Object> settings = Mockito.mock(MockCreationSettings.class);
        SerializableMode serializableMode = Mockito.mock(SerializableMode.class);
        when(settings.getSerializableMode()).thenReturn(serializableMode);
        when(serializableMode.getValue()).thenReturn(SerializableMode.NONE);

        try {
            byteBuddyMockMaker.createMock(settings, handler);
            verify(classInstantiator).instantiate(isA(Class.class));
        } catch (MockitoException e) {
            verify(internalMockHandler).handleMockCreationError(ArgumentMatchers.eq(settings), ArgumentMatchers.eq(e));
        }
    }

    @org.junit.Test
    public void testCreateMockWithSerialization() {
        Class<?> typeToMock = Mockito.mock(Class.class);
        MockHandler handler = internalMockHandler;

        MockCreationSettings<Object> settings = Mockito.mock(MockCreationSettings.class);
        SerializableMode serializableMode = Mockito.mock(SerializableMode.class);
        when(settings.getSerializableMode()).thenReturn(serializableMode);
        when(serializableMode.getValue()).thenReturn(SerializableMode.ACROSS_CLASSLOADERS);

        try {
            byteBuddyMockMaker.createMock(settings, handler);
            verify(internalMockHandler).handleMockCreationError(ArgumentMatchers.eq(settings), ArgumentMatchers.isInstanceOf(MockitoException.class));
        } catch (MockitoException e) {
            verify(internalMockHandler).handleMockCreationError(ArgumentMatchers.eq(settings), ArgumentMatchers.eq(e.getMessage()));
        }
    }

    @org.junit.Test(expected = MockitoException.class)
    public void testCreateMockWithSerializationClassCastException() {
        Class<?> mockedProxyType = Mockito.mock(Class.class);
        MockHandler handler = internalMockHandler;

        MockCreationSettings<Object> settings = Mockito.mock(MockCreationSettings.class);
        SerializableMode serializableMode = Mockito.mock(SerializableMode.class);
        when(settings.getSerializableMode()).thenReturn(serializableMode);
        when(serializableMode.getValue()).thenReturn(SerializableMode.ACROSS_CLASSLOADERS);

        ByteBuddyMockMaker byteBuddyMockMakerSpy = spy(byteBuddyMockMaker);
        when(byteBuddyMockMakerSpy.cachingMockBytecodeGenerator).thenReturn(cachingMockBytecodeGenerator);

        try {
            byteBuddyMockMakerSpy.createMock(settings, handler);
            verify(internalMockHandler).handleMockCreationError(ArgumentMatchers.eq(settings), ArgumentMatchers.isInstanceOf(MockitoException.class));
        } catch (MockitoException e) {
            // expected
        }

        Class<?> createdMock = byteBuddyMockMakerSpy.createMock(settings, handler);
        Class<?> typeToMock = settings.getTypeToMock();

        verify(byteBuddyMockMakerSpy).createMock(settings, handler);
        verify(cachingMockBytecodeGenerator).get(ArgumentMatchers.eq(typeToMock), ArgumentMatchers.anyArray(Class.class));
        verify(classInstantiator).instantiate(isA(Class.class));
        verify(internalMockHandler).handleMockCreated(ArgumentMatchers.eq(createdMock), ArgumentMatchers.eq(settings));
    }

    @org.junit.Test(expected = MockitoException.class)
    public void testCreateMockWithSerializationClassCastException2() {
        Class<?> mockedProxyType = Mockito.mock(Class.class);
        MockHandler handler = internalMockHandler;

        MockCreationSettings<Object> settings = Mockito.mock(MockCreationSettings.class);
        SerializableMode serializableMode = Mockito.mock(SerializableMode.class);
        when(settings.getSerializableMode()).thenReturn(serializableMode);
        when(serializableMode.getValue()).thenReturn(SerialiableMode.ACROSS_CLASSLOADERS); // typo in SerialialeMode

        ByteBuddyMockMaker byteBuddyMockMakerSpy = spy(byteBuddyMockMaker);
        when(byteBuddyMockMakerSpy.cachingMockBytecodeGenerator).thenReturn(cachingMockBytecodeGenerator);

        try {
            byteBuddyMockMakerSpy.createMock(settings, handler);
        } catch (MockitoException e) {
            throw new AssertionError("Unexpected error", e);
        }

        Class<?> createdMock = byteBuddyMockMakerSpy.createMock(settings, handler);
        Class<?> typeToMock = settings.getTypeToMock();

        verify(byteBuddyMockMakerSpy).createMock(settings, handler);
        verify(cachingMockBytecodeGenerator).get(ArgumentMatchers.eq(typeToMock), ArgumentMatchers.anyArray(Class.class));
        verify(classInstantiator).instantiate(isA(Class.class));
        verify(internalMockHandler).handleMockCreated(ArgumentMatchers.eq(createdMock), ArgumentMatchers.eq(settings));

        verify(internalMockHandler).handleMockCreationError(ArgumentMatchers.eq(settings), isA(ClassCastException.class));
    }

    @org.junit.Test
    public void testGetHandler() {
        Object mock = Mockito.mock(Object.class);

        when(byteBuddyMockMaker.getHandler(mock)).thenReturn(internalMockHandler);
        verify(internalMockHandler).getMockHandler();
    }

    @org.junit.Test
    public void testResetMock() {
        Object mock = mock(Object.class);
        InternalMockHandler newHandler = mock(InternalMockHandler.class);

        byteBuddyMockMaker.resetMock(mock, newHandler, Mockito.mock(MockCreationSettings.class));

        MockMethodInterceptor mockAccess = (MockMethodInterceptor) mock;
        MockMethodInterceptor newMockAccess = (MockMethodInterceptor) mock;

        verify(mockAccess).setMockitoInterceptor(ArgumentMatchers.any());
        verify(newMockAccess).setMockitoInterceptor(ArgumentMatchers.eq(newMockHandler));
    }

    @org.junit.Test(expected = IllegalStateException.class)
    public void testInitializeClassInstantiatorFailure() {
        when(classInstantiator).instantiate(ArgumentMatchers.any(Class.class))
                .thenThrow(new InstantiationException("Class Instantiation failed"));

        byteBuddyMockMaker = new ByteBuddyMockMaker();
    }
}