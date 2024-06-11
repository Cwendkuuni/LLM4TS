package org.mockito.internal.creation.bytebuddy;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.internal.util.StringJoiner.join;

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
    public void testCreateMockWithNullType() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        assertNull(mockMaker.createMock(null, null));
    }

    @Test
    public void testCreateMockWithNullHandler() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        assertNull(mockMaker.createMock(new MockCreationSettings<>(String.class), null));
    }

    @Test
    public void testCreateMockWithNullSettings() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        assertNull(mockMaker.createMock(null, new MockHandler() {
            @Override
            public void handle(Object target, MethodInvocation invocation) {
            }
        }));
    }

    @Test
    public void testCreateMockWithSerializableModeACrossClassloaders() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        try {
            mockMaker.createMock(new MockCreationSettings<>(String.class, SerializableMode.ACROSS_CLASSLOADERS), null);
            fail("Expected MockitoException");
        } catch (MockitoException e) {
            assertEquals("Serialization across classloaders not yet supported with ByteBuddyMockMaker", e.getMessage());
        }
    }

    @Test
    public void testCreateMock() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockCreationSettings<String> settings = new MockCreationSettings<>(String.class);
        MockHandler handler = new MockHandler() {
            @Override
            public void handle(Object target, MethodInvocation invocation) {
            }
        };
        String mockedProxyType = mockMaker.createMock(settings, handler).getClass().getSuperclass();
        assertNotNull(mockedProxyType);
        assertEquals("org.mockito.plugins.internal.bytesource.bytebuddy.TypeMockBytecodeGenerator$MockType$1", mockedProxyType.getName());
    }

    @Test
    public void testDescribeClass() {
        Class<?> mockClass = String[].class;
        assertEquals("class [Ljava.lang.String;, loaded by classloader : sun.misc.Launcher$AppClassLoader@18b4aac2", describeClass(mockClass));

        Object mockObject = new Object();
        assertEquals("class java.lang.Object, loaded by classloader : sun.misc.Launcher$AppClassLoader@18b4aac2", describeClass(mockObject));
    }

    @Test
    public void testGetHandler() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        Object mock = mockMaker.createMock(new MockCreationSettings<>(String.class), new MockHandler() {
            @Override
            public void handle(Object target, MethodInvocation invocation) {
            }
        });
        MockHandler handler = mockMaker.getHandler(mock);
        assertNotNull(handler);
    }

    @Test
    public void testResetMock() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        Object mock = mockMaker.createMock(new MockCreationSettings<>(String.class), new MockHandler() {
            @Override
            public void handle(Object target, MethodInvocation invocation) {
            }
        });
        MockHandler newHandler = new MockHandler() {
            @Override
            public void handle(Object target, MethodInvocation invocation) {
            }
        };
        mockMaker.resetMock(mock, newHandler, new MockCreationSettings<>(String.class));
        MockHandler resetHandler = mockMaker.getHandler(mock);
        assertNotNull(resetHandler);
        assertEquals(newHandler, resetHandler);
    }

    @Test
    public void testInitializeClassInstantiator() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        assertNotNull(mockMaker.initializeClassInstantiator());
    }

    @Test
    public void testAsInternalMockHandler() {
        MockHandler handler = new MockHandler() {
            @Override
            public void handle(Object target, MethodInvocation invocation) {
            }
        };
        assertNotNull(ByteBuddyMockMaker.asInternalMockHandler(handler));
    }

}