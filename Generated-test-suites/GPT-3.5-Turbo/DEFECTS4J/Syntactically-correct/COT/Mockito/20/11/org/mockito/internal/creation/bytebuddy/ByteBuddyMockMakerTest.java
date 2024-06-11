package org.mockito.internal.creation.bytebuddy;

import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.InternalMockHandler;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.internal.creation.instance.*;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.SerializableMode;
import org.mockito.plugins.MockMaker;

public class ByteBuddyMockMakerTest {

    @Test
    public void testCreateMock() {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        MockCreationSettings mockCreationSettings = new MockCreationSettings();
        MockHandler mockHandler = new MockHandler() {
            @Override
            public Object handle(Object[] args) throws Throwable {
                return null;
            }
        };
        try {
            Object result = byteBuddyMockMaker.createMock(mockCreationSettings, mockHandler);
            assertNull(result);
        } catch (MockitoException e) {
            fail("Failed to create mock");
        }
    }

    @Test(expected = MockitoException.class)
    public void testCreateMockWithSerializableModeAcrossClassloaders() {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        MockCreationSettings mockCreationSettings = new MockCreationSettings();
        mockCreationSettings.setSerializableMode(SerializableMode.ACROSS_CLASSLOADERS);
        MockHandler mockHandler = new MockHandler() {
            @Override
            public Object handle(Object[] args) throws Throwable {
                return null;
            }
        };
        byteBuddyMockMaker.createMock(mockCreationSettings, mockHandler);
    }

    @Test
    public void testGetHandler() {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        Object mock = new Object();
        assertNull(byteBuddyMockMaker.getHandler(mock));
    }

    @Test
    public void testResetMock() {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        Object mock = new Object();
        MockHandler newHandler = new MockHandler() {
            @Override
            public Object handle(Object[] args) throws Throwable {
                return null;
            }
        };
        MockCreationSettings settings = new MockCreationSettings();
        try {
            byteBuddyMockMaker.resetMock(mock, newHandler, settings);
        } catch (MockitoException e) {
            fail("Failed to reset mock");
        }
    }

    @Test
    public void testDescribeClassWithNonNullType() {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        Class type = String.class;
        String result = byteBuddyMockMaker.describeClass(type);
        assertEquals("'java.lang.String', loaded by classloader : 'null'", result);
    }

    @Test
    public void testDescribeClassWithNullType() {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        Class type = null;
        String result = byteBuddyMockMaker.describeClass(type);
        assertEquals("null", result);
    }

    @Test
    public void testDescribeClassWithNonNullInstance() {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        Object instance = new Object();
        String result = byteBuddyMockMaker.describeClass(instance);
        assertEquals("'java.lang.Object', loaded by classloader : 'null'", result);
    }

    @Test
    public void testDescribeClassWithNullInstance() {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        Object instance = null;
        String result = byteBuddyMockMaker.describeClass(instance);
        assertEquals("null", result);
    }

    @Test
    public void testInitializeClassInstantiator() {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        ClassInstantiator result = byteBuddyMockMaker.initializeClassInstantiator();
        assertNotNull(result);
    }

    @Test(expected = MockitoException.class)
    public void testAsInternalMockHandlerWithNonInternalMockHandler() {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        MockHandler mockHandler = new MockHandler() {
            @Override
            public Object handle(Object[] args) throws Throwable {
                return null;
            }
        };
        byteBuddyMockMaker.asInternalMockHandler(mockHandler);
    }

    @Test
    public void testAsInternalMockHandlerWithInternalMockHandler() {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        InternalMockHandler internalMockHandler = new InternalMockHandler() {
            @Override
            public Object handle(Object[] args) throws Throwable {
                return null;
            }

            @Override
            public MockHandler getDelegatedMockHandler() {
                return null;
            }
        };
        InternalMockHandler result = byteBuddyMockMaker.asInternalMockHandler(internalMockHandler);
        assertNotNull(result);
    }

}