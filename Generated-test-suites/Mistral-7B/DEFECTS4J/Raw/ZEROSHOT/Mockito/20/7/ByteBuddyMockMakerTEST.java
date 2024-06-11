package org.mockito.internal.creation.bytebuddy;

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.invocation.MockHandler;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.internal.creation.instance.ClassInstantiator;
import org.mockito.internal.creation.instance.CachingMockBytecodeGenerator;
import org.mockito.internal.creation.instance.MockMethodInterceptor;
import org.mockito.plugins.MockMaker;
import org.mockito.plugins.MockMaker.MockCreationSettings;

public class ByteBuddyMockMakerTest {

    private ByteBuddyMockMaker byteBuddyMockMaker;
    private CachingMockBytecodeGenerator cachingMockBytecodeGeneratorSpy;
    private ClassInstantiator classInstantiatorSpy;
    private InternalMockHandler mockHandler;

    @Before
    public void setup() {
        cachingMockBytecodeGeneratorSpy = mock(CachingMockBytecodeGenerator.class);
        classInstantiatorSpy = mock(ClassInstantiator.class);
        mockHandler = mock(InternalMockHandler.class);
        byteBuddyMockMaker = new ByteBuddyMockMaker() {
            {
                this.classInstantiator = classInstantiatorSpy;
                this.cachingMockBytecodeGenerator = cachingMockBytecodeGeneratorSpy;
            }
        };
    }

    @Test
    public void testCreateMockWithValidArgs() {
        // Given
        MockCreationSettings<String> settings = MockCreationSettings.newBuilder().withTypeToMock(String.class).build();

        // When
        String mock = byteBuddyMockMaker.createMock(settings, mockHandler);

        // Then
        verify(cachingMockBytecodeGeneratorSpy).get(eq(String.class), eq(new Class[0]));
        verifyZeroInteractions(classInstantiatorSpy);
        verify(classInstantiatorSpy).instantiate(eq(String.class));
        verify(mockHandler).getMockHandler();
        verify(mockHandler).mockUsing(any());
    }

    @Test(expected = MockitoException.class)
    public void testCreateMockWithSerializableModeAcrossClassLoaders() {
        // Given
        MockCreationSettings<String> settings = MockCreationSettings.newBuilder()
                .withTypeToMock(String.class)
                .withSerializableMode(SerializableMode.ACROSS_CLASSLOADERS)
                .build();

        // When
        byteBuddyMockMaker.createMock(settings, mockHandler);

        // Then
        // Exception should be thrown
    }

    @Test
    public void testGetHandler() {
        // Given
        Object mock = mock(String.class);

        // When
        MockHandler handler = byteBuddyMockMaker.getHandler(mock);

        // Then
        assertNull(handler);
    }

    @Test
    public void testResetMock() {
        // Given
        Object mock = mock(String.class);
        MockHandler newHandler = mock(InternalMockHandler.class);
        MockCreationSettings<String> settings = MockCreationSettings.newBuilder().withTypeToMock(String.class).build();

        // When
        byteBuddyMockMaker.resetMock(mock, newHandler, settings);

        // Then
        MockMethodInterceptor mockAccess = (MockMethodInterceptor) mock;
        MockMethodInterceptor newMockAccess = (MockMethodInterceptor) mock;
        MockHandler oldHandler = mockAccess.getMockitoInterceptor().getMockHandler();
        MockHandler newHandler1 = mockAccess.getMockitoInterceptor().getMockHandler();
        MockHandler newHandler2 = newMockAccess.getMockitoInterceptor().getMockHandler();

        assertSame(oldHandler, newHandler1);
        assertNotSame(oldHandler, newHandler2);
        assertSame(newHandler, newHandler2);
    }

    @Test(expected = ClassCastException.class)
    public void testEnsureMockIsAssignableToMockedTypeWithInvalidCast() throws ClassCastException, InstantiationException {
        // Given
        MockCreationSettings<Object> settings = MockCreationSettings.newBuilder().withTypeToMock(String.class).build();
        Object mock = byteBuddyMockMaker.createMock(settings, mockHandler);

        // When, Then
        byteBuddyMockMaker.ensureMockIsAssignableToMockedType(settings, mock);
    }

    @Test
    public void testInitializeClassInstantiator() {
        // Given

        // When
        ClassInstantiator classInstantiator = byteBuddyMockMaker.initializeClassInstantiator();

        // Then
        // Objectenesis classloader should be used
        Class<?> objenesisClassLoader = classInstantiator.getClass().getClassLoader();
        assertTrue(objenesisClassLoader.getName().contains("org.mockito.internal.creation.bytebuddy.ClassInstantiator$UsingObjenesis"));
    }

    @Test(expected = MockitoException.class)
    public void testInitializeClassInstantiatorWithObjenesisMissing() {
        // Given

        // When
        byteBuddyMockMaker.initializeClassInstantiator();

        // Then
        // Exception should be thrown
    }

    @Test
    public void testDescribeClass() {
        // Given
        Class<?> type1 = Object.class;
        Class<?> type2 = String.class;
        Object instance1 = null;
        Object instance2 = new String("test");

        // When
        String result1 = ByteBuddyMockMaker.describeClass(type1);
        String result2 = ByteBuddyMockMaker.describeClass(type2);
        String result3 = ByteBuddyMockMaker.describeClass(instance1);
        String result4 = ByteBuddyMockMaker.describeClass(instance2);

        // Then
        assertEquals("'null', loaded by classloader : 'null'", result3);
        assertEquals("'java.lang.String', loaded by classloader : 'sun.misc.Launcher$AppClassLoader@7852e922'", result4);
        assertEquals("'java.lang.Object', loaded by classloader : 'sun.misc.Launcher$AppClassLoader@7852e922'", result1);
        assertEquals("'java.lang.String', loaded by classloader : 'sun.misc.Launcher$AppClassLoader@7852e922'", result2);
    }
}