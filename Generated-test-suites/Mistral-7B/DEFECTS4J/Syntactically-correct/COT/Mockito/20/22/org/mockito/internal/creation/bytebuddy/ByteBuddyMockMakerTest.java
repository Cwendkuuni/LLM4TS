package org.mockito.internal.creation.bytebuddy;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.internal.creation.instance.ClassInstantiator;
import org.mockito.internal.creation.instance.CachingMockBytecodeGenerator;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.SerializableMode;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;

import static org.mockito.internal.util.StringJoiner.join;
import static org.mockito.internal.util.StringUtil.describeClass;

public class ByteBuddyMockMakerTest {

    @Test
    public void testCreateMock_NormalCase() {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        MockHandler mockHandler = new MockHandler() {
            @Override
            public Object handle(Object target, MethodInvocation invocation) throws Throwable {
                // Handle mock invocations here
                return null;
            }
        };
        MockCreationSettings settings = new MockCreationSettings.Builder()
                .typeToMock(String.class)
                .serializableMode(SerializableMode.DEFAULT)
                .build();

        Object mock = byteBuddyMockMaker.createMock(settings, mockHandler);
        Assert.assertNotNull(mock);
    }

    @Test(expected = MockitoException.class)
    public void testCreateMock_SerializationAcrossClassLoaders() {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        MockHandler mockHandler = new MockHandler() {
            @Override
            public Object handle(Object target, MethodInvocation invocation) throws Throwable {
                // Handle mock invocations here
                return null;
            }
        };
        MockCreationSettings settings = new MockCreationSettings.Builder()
                .typeToMock(String.class)
                .serializableMode(SerializableMode.ACROSS_CLASSLOADERS)
                .build();

        byteBuddyMockMaker.createMock(settings, mockHandler);
    }

    @Test
    public void testCreateMock_ClassCastException() {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        MockHandler mockHandler = new MockHandler() {
            @Override
            public Object handle(Object target, MethodInvocation invocation) throws Throwable {
                // Handle mock invocations here
                return null;
            }
        };
        MockCreationSettings settings = new MockCreationSettings.Builder()
                .typeToMock(Object.class)
                .serializableMode(SerializableMode.DEFAULT)
                .build();

        try {
            byteBuddyMockMaker.createMock(settings, mockHandler);
            Assert.fail("ClassCastException should have been thrown");
        } catch (ClassCastException e) {
            // Expected exception
        }
    }

    @Test(expected = MockitoException.class)
    public void testCreateMock_InstantiationException() {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        MockHandler mockHandler = new MockHandler() {
            @Override
            public Object handle(Object target, MethodInvocation invocation) throws Throwable {
                // Handle mock invocations here
                return null;
            }
        };
        MockCreationSettings settings = new MockCreationSettings.Builder()
                .typeToMock(String[].class)
                .serializableMode(SerializableMode.DEFAULT)
                .build();

        byteBuddyMockMaker.createMock(settings, mockHandler);
    }

    @Test
    public void testGetHandler_MockAccess() {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        Object mock = new Object() {
            MockMethodInterceptor mockAccess = new MockMethodInterceptor(new MockitoStub(), new MockCreationSettings.Builder().build());
        };

        MockHandler handler = byteBuddyMockMaker.getHandler(mock);
        Assert.assertNotNull(handler);
    }

    @Test
    public void testResetMock() {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        Object mock = new Object();
        MockHandler newHandler = new MockHandler() {
            @Override
            public Object handle(Object target, MethodInvocation invocation) throws Throwable {
                // Handle mock invocations here
                return null;
            }
        };
        MockCreationSettings settings = new MockCreationSettings.Builder()
                .typeToMock(Object.class)
                .serializableMode(SerializableMode.DEFAULT)
                .build();

        byteBuddyMockMaker.resetMock(mock, newHandler, settings);
    }

    @Test(expected = MockitoException.class)
    public void testResetMock_NonMockAccess() {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        Object mock = new Object();
        MockHandler newHandler = new MockHandler() {
            @Override
            public Object handle(Object target, MethodInvocation invocation) throws Throwable {
                // Handle mock invocations here
                return null;
            }
        };
        MockCreationSettings settings = new MockCreationSettings.Builder()
                .typeToMock(Object.class)
                .serializableMode(SerializableMode.DEFAULT)
                .build();

        byteBuddyMockMaker.resetMock(new Object(), newHandler, settings);
    }

    @Test(expected = MockitoException.class)
    public void testInitializeClassInstantiator_ObjenesisMissing() {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        MockHandler mockHandler = new MockHandler() {
            @Override
            public Object handle(Object target, MethodInvocation invocation) throws Throwable {
                // Handle mock invocations here
                return null;
            }
        };
        MockCreationSettings settings = new MockCreationSettings.Builder()
                .typeToMock(String.class)
                .serializableMode(SerializableMode.DEFAULT)
                .build();

        byteBuddyMockMaker.createMock(settings, mockHandler);
    }

    @Test
    public void testDescribeClass() {
        Class<?> stringClass = String.class;
        Object stringInstance = new String("Test String");

        Assert.assertEquals("class java.lang.String, loaded by classloader : sun.misc.Launcher$AppClassLoader@18b4aac2", describeClass(stringClass));
        Assert.assertEquals("class java.lang.String, loaded by classloader : sun.misc.Launcher$AppClassLoader@18b4aac2", describeClass((Class<?>) stringClass.getClass()));
        Assert.assertEquals("class 'java.lang.String', loaded by classloader : sun.misc.Launcher$AppClassLoader@18b4aac2", describeClass(stringClass));
        Assert.assertEquals("class 'java.lang.String', loaded by classloader : sun.misc.Launcher$AppClassLoader@18b4aac2", describeClass(stringInstance));
    }
}