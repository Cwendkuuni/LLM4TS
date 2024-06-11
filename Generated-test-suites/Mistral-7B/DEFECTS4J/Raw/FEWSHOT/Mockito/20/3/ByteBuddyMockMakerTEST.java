package org.mockito.internal.creation.bytebuddy;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.internal.creation.instance.*;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.SerializableMode;
import org.mockito.plugins.MockMaker;

public class ByteBuddyMockMakerTest {

    private ByteBuddyMockMaker mockMaker;

    @Before
    public void setup() {
        mockMaker = new ByteBuddyMockMaker();
    }

    @Test
    public void testCreateMockWithSerializableModeAcrossClassloaders() {
        MockCreationSettings<Object> settings = new MockCreationSettings.Builder().withTypeToMock(Object.class).withSerializableMode(SerializableMode.ACROSS_CLASSLOADERS).build();
        assertThrows(MockitoException.class, () -> mockMaker.createMock(settings, mockitoMock()));
    }

    @Test
    public void testCreateMockWithValidSerializableMode() {
        MockCreationSettings<Object> settings = new MockCreationSettings.Builder().withTypeToMock(Object.class).withSerializableMode(SerializableMode.SERIALIZABLE).build();
        Object mock = mockMaker.createMock(settings, mockitoMock());
        assertNotNull(mock);
    }

    @Test
    public void testCreateMockWithNullHandler() {
        MockCreationSettings<Object> settings = new MockCreationSettings.Builder().withTypeToMock(Object.class).build();
        assertThrows(NullPointerException.class, () -> mockMaker.createMock(settings, null));
    }

    @Test
    public void testCreateMockWithInvalidType() {
        MockCreationSettings<String> settings = new MockCreationSettings.Builder().withTypeToMock(int.class).withSerializableMode(SerializableMode.SERIALIZABLE).build();
        assertThrows(ClassCastException.class, () -> mockMaker.createMock(settings, mockitoMock()));
    }

    @Test
    public void testCreateMockWithClassCastException() {
        MockCreationSettings<Object> settings = new MockCreationSettings.Builder().withTypeToMock(String.class).withSerializableMode(SerializableMode.SERIALIZABLE).build();
        assertThrows(ClassCastException.class, () -> mockMaker.createMock(settings, mockitoMock()));
    }

    @Test
    public void testGetHandlerWithMockInstance() {
        Object mock = mockMaker.createMock(settingsForType(String.class), mockitoMock());
        MockHandler handler = mockMaker.getHandler(mock);
        assertNotNull(handler);
    }

    @Test
    public void testGetHandlerWithNullMockInstance() {
        assertNull(mockMaker.getHandler(null));
    }

    @Test
    public void testResetMock() {
        Object mock = mockMaker.createMock(settingsForType(String.class), mockitoMock());
        MockHandler handler = mockitoMock();
        mockMaker.resetMock(mock, handler, new MockCreationSettings.Builder().build());
        assertEquals(handler, mockMaker.getHandler(mock));
    }

    @Test
    public void testDescribeClass() {
        assertEquals("class java.lang.String, loaded by classloader : sun.misc.Launcher$AppClassLoader@1b6d1d92", describeClass(String.class));
        assertEquals("class java.lang.String, loaded by classloader : sun.misc.Launcher$AppClassLoader@1b6d1d92", describeClass(new String("hello")));
        assertEquals("null", describeClass(null));
    }

    @Test
    public void testInitializeClassInstantiator() {
        assertEquals("class org.mockito.internal.creation.bytebuddy.ClassInstantiator$UsingObjenesis, loaded by classloader : sun.misc.Launcher$AppClassLoader@1b6d1d92", describeClass(ClassInstantiator.class));
    }

    private MockHandler mockitoMock() {
        return mock(MockHandler.class);
    }

    private MockCreationSettings<Object> settingsForType(Class<?> type) {
        return new MockCreationSettings.Builder().withTypeToMock(type).build();
    }

}