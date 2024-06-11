package org.mockito.internal.creation.bytebuddy;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.mockito.internal.util.StringJoiner.join;
import static org.mockito.mockito3.Mockito.mock;
import static org.mockito.mockito3.Mockito.spy;
import static org.mockito.mockito3.Mockito.verify;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.internal.creation.instance.ClassInstantiator;
import org.mockito.internal.creation.instance.CachingMockBytecodeGenerator;
import org.mockito.internal.creation.instance.MockMethodInterceptor;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.SerializableMode;
import org.mockito.plugins.MockMaker;

public class ByteBuddyMockMakerTest {

    private ByteBuddyMockMaker mockMaker;
    private ClassInstantiator classInstantiator;
    private CachingMockBytecodeGenerator cachingMockBytecodeGenerator;

    @Before
    public void setUp() {
        this.mockMaker = new ByteBuddyMockMaker();
        this.classInstantiator = mockMaker.classInstantiator;
        this.cachingMockBytecodeGenerator = mockMaker.cachingMockBytecodeGenerator;
    }

    @Test
    public void testCreateMockWithNullTypeToMock() {
        assertThrows(MockitoException.class, () -> {
            mockMaker.createMock(new MockCreationSettings<>(null), mock(MockHandler.class));
        });
    }

    @Test
    public void testCreateMockWithNullHandler() {
        assertThrows(MockitoException.class, () -> {
            mockMaker.createMock(new MockCreationSettings<>(String.class), null);
        });
    }

    @Test
    public void testCreateMockWithNullSettings() {
        assertThrows(NullPointerException.class, () -> {
            mockMaker.createMock(null, mock(MockHandler.class), String.class);
        });
    }

    @Test
    public void testCreateMockWithNullExtraInterfaces() {
        MockCreationSettings<String> settings = new MockCreationSettings<>(String.class);
        Class<? extends String> mockedProxyType = cachingMockBytecodeGenerator.get(String.class, (Class[]) null);
        try {
            mockMaker.createMock(settings, mock(MockHandler.class));
            fail();
        } catch (MockitoException e) {
            // Expected
        }
        assertNull(mockedProxyType.newInstance());
    }

    @Test
    public void testCreateMockWithEmptyExtraInterfaces() {
        Class<? extends String> mockedProxyType = cachingMockBytecodeGenerator.get(String.class, new Class[0]);
        T mockInstance = mockMaker.createMock(new MockCreationSettings<>(String.class), mock(MockHandler.class));
        assertSame(mockedProxyType, mockInstance.getClass());
    }

    @Test
    public void testCreateMockWithExtraInterfaces() {
        Class<? extends String> mockedProxyType = cachingMockBytecodeGenerator.get(String.class, String[].class);
        Class<?> extraInterface = Integer.class;
        T mockInstance = mockMaker.createMock(new MockCreationSettings<>(String.class, extraInterface), mock(MockHandler.class));
        assertSame(mockedProxyType, mockInstance.getClass());
        assert(mockInstance instanceof MockMethodInterceptor);
        MockMethodInterceptor mockAccess = (MockMethodInterfaceInterceptor) mockInstance;
        assertSame(mockHandler(), mockAccess.getMockitoInterceptor().getMockHandler());
        verify(mockAccess.getMockitoInterceptor().getMockHandler()).mock(eq(extraInterface));
    }

    @Test
    public void testCreateMockWithException() {
        try {
            mockMaker.createMock(new MockCreationSettings<>(String[].class), mock(MockHandler.class));
            fail();
        } catch (MockitoException e) {
            String expectedMessage = join(
                "Unable to create mock instance of type 'java.lang.String[]'",
                "",
                "You might experience classloading issues, please ask the mockito mailing-list."
            );
            assertEquals(expectedMessage, e.getMessage());
        }
    }

    @Test
    public void testGetHandler() {
        Object mock = mock(MockHandler.class);
        assertNull(mockMaker.getHandler(null));
        assertSame(mock, mockMaker.getHandler(mock));
    }

    @Test
    public void testResetMock() {
        Object mock = mock(MockMethodInterceptor.class);
        MockHandler mockHandler = mock(MockHandler.class);
        mockMaker.resetMock(mock, mockHandler, new MockCreationSettings<>(MockMethodInterceptor.class));
        assertSame(mockHandler, mockMaker.getHandler(mock));
    }

    @Test
    public void testInitializeClassInstantiator() {
        Class<?> objenesisClassLoader = null;
        try {
            objenesisClassLoader = Class.forName("org.mockito.internal.creation.bytebuddy.ClassInstantiator$UsingObjenesis");
        } catch (ClassNotFoundException e) {
            // Expected
        }
        assertNotNull(mockMaker.initializeClassInstantiator());
        assertNotNull(objenesisClassLoader);
    }
}