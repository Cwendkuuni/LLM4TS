package org.mockito.internal.creation.bytebuddy;

import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.internal.creation.instance.ClassInstantiator;
import org.mockito.internal.creation.instance.CachingMockBytecodeGenerator;
import org.mockito.internal.creation.instance.MockMethodInterceptor;
import org.mockito.internal.util.MockitoConfiguration;
import org.mockito.plugins.MockMaker;
import org.mockito.plugins.MockMakerReader;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ByteBuddyMockMakerTest {

    private ByteBuddyMockMaker byteBuddyMockMaker;
    private ClassInstantiator classInstantiator;
    private CachingMockBytecodeGenerator cachingMockBytecodeGenerator;

    @Rule
    public MockMaker mockMaker = new MockMakerReader().getMockMaker();

    @Before
    public void setup() {
        classInstantiator = mock(ClassInstantiator.class);
        cachingMockBytecodeGenerator = mock(CachingMockBytecodeGenerator.class);
        byteBuddyMockMaker = new ByteBuddyMockMaker() {
            @Override
            protected ClassInstantiator initializeClassInstantiator() {
                return classInstantiator;
            }

            @Override
            protected CachingMockBytecodeGenerator newCachingMockBytecodeGenerator() {
                return cachingMockBytecodeGenerator;
            }
        };
    }

    @org.junit.Test
    public void testCreateMockWithDefaultSettings() throws Exception {
        Type mockType = mock(String.class);
        MockHandler mockHandler = mock(MockHandler.class);

        when(cachingMockBytecodeGenerator.get(any(Type.class), any(Class[].class)))
                .thenReturn(mockType);

        byteBuddyMockMaker.createMock(new MockCreationSettings<>(), mockHandler);

        verify(mockHandler).handle(any(), any());

        MockMethodInterceptor mockAccess = (MockMethodInterceptor) byteBuddyMockMaker.getHandler(mockType);
        verify(mockAccess).getMockitoInterceptor().getMockHandler().handle(any(), any());
    }

    @org.junit.Test
    public void testCreateMockWithCustomSerializableMode() throws Exception {
        Type mockType = mock(String.class);
        SerializableMode serializableMode = SerializableMode.ACROSS_CLASSLOADERS;
        MockHandler mockHandler = mock(MockHandler.class);

        when(cachingMockBytecodeGenerator.get(any(Type.class), any(Class[].class)))
                .thenReturn(mockType);

        try {
            byteBuddyMockMaker.createMock(new MockCreationSettings<>().serializableMode(serializableMode), mockHandler);
            fail();
        } catch (MockitoException expected) {
            verify(mockHandler).handle(any(), any());
        }

        Mockito.verifyNoMoreInteractions(mockHandler);
    }

    @org.junit.Test
    public void testCreateMockWithClassCastException() throws Exception {
        Type mockType = mock(String[].class);
        MockHandler mockHandler = mock(MockHandler.class);

        when(cachingMockBytecodeGenerator.get(any(Type.class), any(Class[].class)))
                .thenReturn((Class<?>) mockType.getComponentType());

        try {
            byteBuddyMockMaker.createMock(new MockCreationSettings<>(), mockHandler);
            fail();
        } catch (ClassCastException expected) {
            verify(mockHandler).handle(any(), any());
        }

        Mockito.verifyNoMoreInteractions(mockHandler);
    }

    @org.junit.Test
    public void testCreateMockWithInstantiationException() throws Exception {
        Type mockType = mock(String.class);
        MockHandler mockHandler = mock(MockHandler.class);

        when(cachingMockBytecodeGenerator.get(any(Type.class), any(Class[].class)))
                .thenThrow(new InstantiationException("Unable to create mock instance"));

        try {
            byteBuddyMockMaker.createMock(new MockCreationSettings<>(), mockHandler);
            fail();
        } catch (MockitoException expected) {
            verify(mockHandler).handle(any(), any());
        }

        Mockito.verifyNoMoreInteractions(mockHandler);
    }

    @org.junit.Test
    public void testGetHandler() throws Exception {
        Type mockType = mock(String.class);
        MockHandler mockHandler = mock(MockHandler.class);
        byteBuddyMockMaker.createMock(new MockCreationSettings<>(), mockHandler);

        MockMethodInterceptor mockAccess = (MockMethodInterceptor) byteBuddyMockMaker.getHandler(mockType);
        verify(mockAccess).getMockitoInterceptor().getMockHandler().handle(any(), any());
    }

    @org.junit.Test
    public void testResetMock() throws Exception {
        Type mockType = mock(String.class);
        MockHandler mockHandler = mock(MockHandler.class);
        byteBuddyMockMaker.createMock(new MockCreationSettings<>(), mockHandler);

        MockMethodInterceptor oldMockAccess = (MockMethodInterceptor) mockType.cast(byteBuddyMockMaker.getHandler(mockType));
        MockMethodInterceptor newMockAccess = spy(oldMockAccess);
        byteBuddyMockMaker.resetMock(mockType, newMockAccess, new MockCreationSettings<>());

        verify(mockHandler).handle(any(), any());
        verify(newMockAccess).getMockitoInterceptor().getMockHandler().handle(any(), any());
    }

    @org.junit.Test
    public void testInitializeClassInstantiator() throws Exception {
        try {
            byteBuddyMockMaker.initializeClassInstantiator();
            fail();
        } catch (MockitoException expected) {
            // expected
        }

        verify(classInstantiator).instantiate(any(Class.class));
    }

    @org.junit.Test
    public void testAsInternalMockHandler() throws Exception {
        MockHandler mockHandler = mock(MockHandler.class);

        try {
            byteBuddyMockMaker.asInternalMockHandler(mockHandler);
            fail();
        } catch (MockitoException expected) {
            // expected
        }

        verify(mockHandler).handle(any(), any());
    }
}