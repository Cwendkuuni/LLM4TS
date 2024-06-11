package org.mockito.internal.creation.bytebuddy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.InternalMockHandler;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.internal.creation.instance.ClassInstantiator;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.SerializableMode;
import org.mockito.plugins.MockMaker;

public class ByteBuddyMockMakerTest {

    private ByteBuddyMockMaker mockMaker;
    private MockCreationSettings<?> settings;
    private MockHandler handler;

    @Before
    public void setUp() {
        mockMaker = new ByteBuddyMockMaker();
        settings = mock(MockCreationSettings.class);
        handler = mock(MockHandler.class);
    }

    @Test
    public void testCreateMock() throws Throwable {
        when(settings.getSerializableMode()).thenReturn(SerializableMode.ACROSS_CLASSLOADERS);
        try {
            mockMaker.createMock(settings, handler);
        } catch (MockitoException e) {
            assertEquals("Serialization across classloaders not yet supported with ByteBuddyMockMaker", e.getMessage());
        }
    }

    @Test
    public void testEnsureMockIsAssignableToMockedType() {
        String mock = "mock";
        Class<String> typeToMock = String.class;
        assertEquals(mock, mockMaker.ensureMockIsAssignableToMockedType(settings, mock));
    }

    @Test
    public void testDescribeClassWithClass() {
        Class<?> type = String.class;
        assertEquals("'java.lang.String', loaded by classloader : 'null'", mockMaker.describeClass(type));
    }

    @Test
    public void testDescribeClassWithNull() {
        Class<?> type = null;
        assertEquals("null", mockMaker.describeClass(type));
    }

    @Test
    public void testDescribeClassWithInstance() {
        String instance = "instance";
        assertEquals("'java.lang.String', loaded by classloader : 'null'", mockMaker.describeClass(instance));
    }

    @Test
    public void testGetHandler() {
        Object mock = mock(MockMethodInterceptor.MockAccess.class);
        when(mock instanceof MockMethodInterceptor.MockAccess).thenReturn(true);
        when(((MockMethodInterceptor.MockAccess) mock).getMockitoInterceptor().getMockHandler()).thenReturn(handler);
        assertEquals(handler, mockMaker.getHandler(mock));
    }

    @Test
    public void testGetHandlerWithNotImplementingMockAccess() {
        Object mock = mock(Object.class);
        assertNull(mockMaker.getHandler(mock));
    }

    @Test
    public void testResetMock() {
        Object mock = mock(MockMethodInterceptor.MockAccess.class);
        Object newHandler = mock(InternalMockHandler.class);
        mockMaker.resetMock(mock, newHandler, settings);
        verify(((MockMethodInterceptor.MockAccess) mock), times(1)).setMockitoInterceptor(any(MockMethodInterceptor.class));
    }

    @Test(expected = MockitoException.class)
    public void testResetMockWithNotImplementingMockAccess() {
        Object mock = mock(Object.class);
        Object newHandler = mock(InternalMockHandler.class);
        mockMaker.resetMock(mock, newHandler, settings);
    }

    @Test
    public void testInitializeClassInstantiator() throws Throwable {
        GlobalConfiguration globalConfiguration = mock(GlobalConfiguration.class);
        Class<?> objenesisClassLoader = mock(Class.class);
        Constructor<?> usingClassCacheConstructor = mock(Constructor.class);
        ClassInstantiator classInstantiator = mock(ClassInstantiator.class);

        when(Class.forName("org.mockito.internal.creation.bytebuddy.ClassInstantiator$UsingObjenesis")).thenReturn(objenesisClassLoader);
        when(objenesisClassLoader.getDeclaredConstructor(boolean.class)).thenReturn(usingClassCacheConstructor);
        when(usingClassCacheConstructor.newInstance(globalConfiguration.enableClassCache())).thenReturn(classInstantiator);

        ClassInstantiator initializedClassInstantiator = ByteBuddyMockMaker.initializeClassInstantiator();

        assertEquals(classInstantiator, initializedClassInstantiator);
    }

    @Test(expected = IllegalStateException.class)
    public void testInitializeClassInstantiatorWithThrowable() throws Throwable {
        when(Class.forName("org.mockito.internal.creation.bytebuddy.ClassInstantiator$UsingObjenesis")).thenThrow(new ClassNotFoundException());

        ByteBuddyMockMaker.initializeClassInstantiator();
    }

    @Test
    public void testAsInternalMockHandler() {
        InternalMockHandler internalMockHandler = mock(InternalMockHandler.class);
        MockHandler mockHandler = mock(MockHandler.class);

        when(mockHandler instanceof InternalMockHandler).thenReturn(true);
        when((InternalMockHandler) mockHandler).thenReturn(internalMockHandler);

        InternalMockHandler convertedInternalMockHandler = ByteBuddyMockMaker.asInternalMockHandler(mockHandler);

        assertEquals(internalMockHandler, convertedInternalMockHandler);
    }

    @Test(expected = MockitoException.class)
    public void testAsInternalMockHandlerWithNotImplementingInternalMockHandler() {
        MockHandler mockHandler = mock(MockHandler.class);

        when(mockHandler instanceof InternalMockHandler).thenReturn(false);

        ByteBuddyMockMaker.asInternalMockHandler(mockHandler);
    }
}