package org.mockito.internal.creation.bytebuddy;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.verify;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.creation.instance.ClassInstantiator;
import org.mockito.internal.creation.instance.MockMethodInterceptor;
import org.mockito.internal.creation.instance.MockMethodInterceptor.MockAccess;
import org.mockito.invocation.MockHandler;
import org.mockito.internal.util.StringJoiner;
import org.mockito.plugins.MockMaker;
import org.mockito.plugins.MockMaker.MockCreationSettings;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;

public class ByteBuddyMockMakerTest {

    private ByteBuddyMockMaker byteBuddyMockMaker;
    private ClassInstantiator classInstantiatorMock;
    private CachingMockBytecodeGenerator cachingMockBytecodeGeneratorMock;

    @Before
    public void setup() {
        classInstantiatorMock = mock(ClassInstantiator.class);
        cachingMockBytecodeGeneratorMock = mock(CachingMockBytecodeGenerator.class);
        byteBuddyMockMaker = new ByteBuddyMockMaker() {
            {
                this.classInstantiator = classInstantiatorMock;
                this.cachingMockBytecodeGenerator = cachingMockBytecodeGeneratorMock;
            }
        };
    }

    @Test
    public void testCreateMock_ValidSettingsAndHandler() {
        // Given
        MockCreationSettings<Object> settings = new MockCreationSettings.Builder(Object.class).build();
        MockHandler mockHandler = mock(MockHandler.class);

        // When
        Object mock = byteBuddyMockMaker.createMock(settings, mockHandler);

        // Then
        verify(cachingMockBytecodeGeneratorMock).get(eq(Object.class), any(Class[].class));
        verify(classInstantiatorMock).instantiate(eq(Object.class));
        MockAccess mockAccess = (MockAccess) mock;
        verify(mockAccess).setMockitoInterceptor(any(MockMethodInterceptor.class));
        MockMethodInterceptor interceptor = mockAccess.getMockitoInterceptor();
        verify(interceptor).getMockHandler();
        assertEquals(mockHandler, interceptor.getMockHandler());
    }

    @Test(expected = MockitoException.class)
    public void testCreateMock_SerializationAcrossClassloaders() {
        // Given
        MockCreationSettings<Object> settings = new MockCreationSettings.Builder(Object.class).serializableMode(SerializableMode.ACROSS_CLASSLOADERS).build();
        MockHandler mockHandler = mock(MockHandler.class);

        // When
        byteBuddyMockMaker.createMock(settings, mockHandler);

        // Then
        // Exception is expected to be thrown
    }

    @Test(expected = ClassCastException.class)
    public void testCreateMock_ClassCastException() {
        // Given
        MockCreationSettings<Object> settings = new MockCreationSettings.Builder(String.class).build();
        MockHandler mockHandler = mock(MockHandler.class);
        Class<Object> mockedProxyType = new Class<Object>() {};
        Object mockInstance = new Object() {};

        // When
        byteBuddyMockMaker.createMock(settings, mockHandler);
        byteBuddyMockMaker.ensureMockIsAssignableToMockedType(settings, mockInstance);

        // Then
        // ClassCastException is expected to be thrown
    }

    @Test(expected = InstantiationException.class)
    public void testCreateMock_InstantiationException() {
        // Given
        MockCreationSettings<Object> settings = new MockCreationSettings.Builder(Object.class).build();
        MockHandler mockHandler = mock(MockHandler.class);

        // When
        byteBuddyMockMaker.createMock(settings, mockHandler);

        // Then
        // InstantiationException is expected to be thrown
    }

    @Test
    public void testGetHandler() {
        // Given
        Object mock = mock(Object.class);

        // When
        MockHandler handler = byteBuddyMockMaker.getHandler(mock);

        // Then
        assertNull(handler);
    }

    @Test
    public void testGetHandler_MockAccess() {
        // Given
        Object mock = spy(new Object() {});

        // When
        MockHandler handler = byteBuddyMockMaker.getHandler(mock);

        // Then
        assertNotNull(handler);
    }

    @Test
    public void testResetMock() {
        // Given
        Object mock = mock(Object.class);
        MockHandler newHandler = mock(MockHandler.class);
        MockCreationSettings settings = new MockCreationSettings.Builder(Object.class).build();

        // When
        byteBuddyMockMaker.resetMock(mock, newHandler, settings);

        // Then
        MockMethodInterceptor oldInterceptor = ((MockAccess) mock).getMockitoInterceptor();
        MockMethodInterceptor newInterceptor = ((MockAccess) mock).getMockitoInterceptor();
        assertNotSame(oldInterceptor, newInterceptor);
        assertEquals(newHandler, newInterceptor.getMockHandler());
    }

    @Test
    public void testResetMock_NullHandler() {
        // Given
        Object mock = mock(Object.class);
        MockCreationSettings settings = new MockCreationSettings.Builder(Object.class).build();

        // When
        byteBuddyMockMaker.resetMock(mock, null, settings);

        // Then
        // Nothing happens
    }

    @Test
    public void testDescribeClass() {
        // Given
        Class<String> stringClass = String.class;
        Object obj = new Object() {};

        // When
        String result1 = ByteBuddyMockMaker.describeClass(stringClass);
        String result2 = ByteBuddyMockMaker.describeClass(obj);

        // Then
        assertEquals("'java.lang.String', loaded by classloader : 'sun.misc.Launcher$AppClassLoader@18b4aac2, name : 'org.mockito.internal.creation.bytebuddy.ByteBuddyMockMaker'", result1);
        assertEquals("'class java.lang.Object', loaded by classloader : 'sun.misc.Launcher$AppClassLoader@18b4aac2, name : 'org.mockito.internal.creation.bytebuddy.ByteBuddyMockMaker'", result2);
    }
}