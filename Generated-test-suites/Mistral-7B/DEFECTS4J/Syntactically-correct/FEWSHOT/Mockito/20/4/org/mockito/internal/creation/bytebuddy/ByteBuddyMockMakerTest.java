package org.mockito.internal.creation.bytebuddy;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
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
    public void testCreateMockWithNullHandler() {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        MockHandler nullHandler = null;
        try {
            byteBuddyMockMaker.createMock(null, nullHandler);
            assert false : "Expected MockitoException";
        } catch (MockitoException e) {
            // Expected
        }
    }

    @Test
    public void testCreateMockWithNullTypeToMock() {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        MockHandler handler = mock(MockHandler.class);
        try {
            byteBuddyMockMaker.createMock(null, handler);
            assert false : "Expected MockitoException";
        } catch (MockitoException e) {
            // Expected
        }
    }

    @Test
    public void testCreateMockWithNullSettings() {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        Class<?> mockType = Object.class;
        MockHandler handler = mock(MockHandler.class);
        try {
            byteBuddyMockMaker.createMock(new MockCreationSettings<Object>() {}, handler);
            assert false : "Expected MockitoException";
        } catch (MockitoException e) {
            // Expected
        }
    }

    @Test
    public void testCreateMockWithNullSerializerMode() {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        Class<?> mockType = Object.class;
        MockHandler handler = mock(MockHandler.class);
        MockCreationSettings<Object> settings = new MockCreationSettings<Object>() {{
            setSerializableMode(null);
        }};
        try {
            byteBuddyMockMaker.createMock(settings, handler);
            assert false : "Expected MockitoException";
        } catch (MockitoException e) {
            // Expected
        }
    }

    @Test
    public void testCreateMockWithEmptyExtraInterfaces() {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        Class<?> mockType = Object.class;
        MockHandler handler = mock(MockHandler.class);
        MockCreationSettings<Object> settings = new MockCreationSettings<Object>() {{
            setTypeToMock(mockType);
            setExtraInterfaces(new Class[0]);
        }};
        try {
            byteBuddyMockMaker.createMock(settings, handler);
            Object mock = byteBuddyMockMaker.getHandler(byteBuddyMockMaker.createMock(settings, handler));
            assertNotNull(mock);
        } catch (MockitoException e) {
            assert false : "Unexpected MockitoException";
        }
    }

    @Test
    public void testCreateMockWithExtraInterfaces() {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        Class<?> mockType = Object.class;
        Class<String> extraInterface = String.class;
        MockHandler handler = mock(MockHandler.class);
        MockCreationSettings<Object> settings = new MockCreationSettings<Object>() {{
            setTypeToMock(mockType);
            setExtraInterfaces(new Class[] { extraInterface });
        }};
        try {
            Object mock = byteBuddyMockMaker.createMock(settings, handler);
            assertNotNull(mock);
            MockHandler handler1 = byteBuddyMockMaker.getHandler(mock);
            assertNotNull(handler1);
            handler1.handle(extraInterface, "test");
        } catch (MockitoException e) {
            assert false : "Unexpected MockitoException";
        }
    }

    @Test
    public void testCreateMockWithInvalidSerializableMode() {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        Class<?> mockType = Object.class;
        MockHandler handler = mock(MockHandler.class);
        MockCreationSettings<Object> settings = new MockCreationSettings<Object>() {{
            setTypeToMock(mockType);
            setSerializableMode(SerializableMode.ACROSS_CLASSLOADERS);
        }};
        try {
            byteBuddyMockMaker.createMock(settings, handler);
            assert false : "Expected MockitoException";
        } catch (MockitoException e) {
            String expectedMessage = join("Serialization across classloaders not yet supported with ByteBuddyMockMaker",
                    "",
                    "Type to mock : " + describeClass(mockType),
                    "");
            assertEquals(expectedMessage, e.getMessage());
        }
    }

    @Test
    public void testCreateMockWithNonSerializableType() {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        Class<Object> mockType = Object.class;
        Class<String> nonSerializableType = String.class;
        MockHandler handler = mock(MockHandler.class);
        MockCreationSettings<Object> settings = new MockCreationSettings<Object>() {{
            setTypeToMock(mockType);
            setSerializableMode(SerializableMode.SERIALIZABLE);
            setTypeToMock(nonSerializableType);
        }};
        try {
            byteBuddyMockMaker.createMock(settings, handler);
            assert false : "Expected MockitoException";
        } catch (MockitoException e) {
            String expectedMessage = join("Unable to create mock instance of type '", nonSerializableType.getSimpleName(), "',",
                    "you should use 'MockitoJUnitRunner' or 'MockitoAnnotations.initMocks()' to properly setup the mockito context.",
                    "");
            assertEquals(expectedMessage, e.getMessage());
        }
    }

    @Test
    public void testDescribeClass() {
        Class<String> stringClass = String.class;
        Class<Object> objectClass = Object.class;
        assertEquals("'java.lang.String'", describeClass(stringClass));
        assertEquals("'java.lang.Object', loaded by classloader : 'sun.misc.Launcher$AppClassLoader@15407172'", describeClass(objectClass));
        assertEquals("null", describeClass(null));
    }

    @Test
    public void testGetHandler() {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        Class<Object> mockType = Object.class;
        MockHandler handler = mock(MockHandler.class);
        Object mock = byteBuddyMockMaker.createMock(new MockCreationSettings<Object>() {{
            setTypeToMock(mockType);
        }}, handler);
        MockHandler handler1 = byteBuddyMockMaker.getHandler(mock);
        assertNotNull(handler1);
        assertEquals(handler, handler1);
    }

    @Test
    public void testResetMock() {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        Class<Object> mockType = Object.class;
        MockHandler handler = mock(MockHandler.class);
        Object mock = byteBuddyMockMaker.createMock(new MockCreationSettings<Object>() {{
            setTypeToMock(mockType);
        }}, handler);
        MockHandler newHandler = mock(MockHandler.class);
        byteBuddyMockMaker.resetMock(mock, newHandler, new MockCreationSettings<Object>() {{
            setTypeToMock(mockType);
        }});
        MockHandler handler1 = byteBuddyMockMaker.getHandler(mock);
        assertNotNull(handler1);
        assertEquals(newHandler, handler1);
    }

    @Test
    public void testInitializeClassInstantiator() {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        try {
            byteBuddyMockMaker.initializeClassInstantiator();
        } catch (Throwable e) {
            assert false : "Unexpected exception";
        }
    }

    @Test(expected = MockitoException.class)
    public void testInitializeClassInstantiatorWithMissingObjenesis() {
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();
        byteBuddyMockMaker.initializeClassInstantiator();
    }
}