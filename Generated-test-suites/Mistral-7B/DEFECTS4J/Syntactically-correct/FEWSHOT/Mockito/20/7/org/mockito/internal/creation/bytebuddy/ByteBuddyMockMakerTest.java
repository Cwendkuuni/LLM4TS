package org.mockito.internal.creation.bytebuddy;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertEquals;
import static org.mockito.internal.util.StringJoiner.join;
import static org.powermock.reflect.internal.Whitebox.getInternalState;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.internal.creation.instance.ClassInstantiator;
import org.mockito.internal.creation.instance.CachingMockBytecodeGenerator;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.SerializableMode;
import org.mockito.plugins.MockMaker;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.rule.PowerMockRule;

public class ByteBuddyMockMakerTest {

    private ByteBuddyMockMaker mockMaker;
    private PowerMockRule powerMockRule;

    @Before
    public void setup() {
        powerMockRule.enablePrePreInitialization();
        mockMaker = new ByteBuddyMockMaker();
    }

    @After
    public void reset() {
        powerMockRule.clearLoadedClasses();
    }

    @Test
    public void testCreateMockWithSerializableModeAcrossClassLoaders() {
        MockCreationSettings settings = new MockCreationSettings.Builder().serializableMode(SerializableMode.ACROSS_CLASSLOADERS).build();
        assertThrows(MockitoException.class, () -> mockMaker.createMock(settings, mock(MockHandler.class)));
    }

    @Test
    public void testCreateMock() {
        MockCreationSettings<String> settings = new MockCreationSettings.Builder()
                .typeToMock(String.class)
                .serializableMode(SerializableMode.DEFAULT)
                .build();
        String mock = mockMaker.createMock(settings, mock(MockHandler.class));
        assertEquals(String.class, mock.getClass());
    }

    @Test
    public void testEnsureMockIsAssignableToMockedType() {
        Class<?> mockType = mock(Class.class);
        MockCreationSettings<Object> settings = new MockCreationSettings.Builder()
                .typeToMock(mockType)
                .serializableMode(SerializableMode.DEFAULT)
                .build();
        Object mock = mockMaker.createMock(settings, mock(MockHandler.class));
        assertEquals(mockType, mock.getClass().cast(mock));
    }

    @Test
    public void testGetHandler() {
        Class<?> mockType = mock(Class.class);
        MockCreationSettings<Object> settings = new MockCreationSettings.Builder()
                .typeToMock(mockType)
                .serializableMode(SerializableMode.DEFAULT)
                .build();
        Object mock = mockMaker.createMock(settings, mock(MockHandler.class));
        MockHandler handler = mockMaker.getHandler(mock);
        assertSame(mock(MockHandler.class).mocking(eq(mock)), handler);
    }

    @Test
    public void testResetMock() {
        Class<?> mockType = mock(Class.class);
        MockCreationSettings<Object> settings = new MockCreationSettings.Builder()
                .typeToMock(mockType)
                .serializableMode(SerializableMode.DEFAULT)
                .build();
        Object mock = mockMaker.createMock(settings, mock(MockHandler.class));
        InternalMockHandler handler = mock(InternalMockHandler.class);
        mockMaker.resetMock(mock, handler, settings);
        assertSame(handler, getInternalState(mock, "mockitoInterceptor"));
    }

    @Test
    public void testDescribeClassNull() {
        assertNull(mockMaker.describeClass(null));
    }

    @Test
    public void testDescribeClass() {
        Class<String> type = String.class;
        assertEquals("'" + type.getCanonicalName() + "', loaded by classloader : '" + type.getClassLoader() + "'", mockMaker.describeClass(type));
    }

    @Test
    public void testDescribeClassObject() {
        Object instance = new Object();
        assertEquals("null", mockMaker.describeClass(instance));
    }

    @Test
    public void testInitializeClassInstantiator() {
        assertFalse(Boolean.parseBoolean(System.getProperty("org.mockito.internal.creation.bytebuddy.ClassInstantiator.useObjenesis", "false")));
        assertFalse(mockMaker.initializeClassInstantiator().useObjenesis());
        System.setProperty("org.mockito.internal.creation.bytebuddy.ClassInstantiator.useObjenesis", "true");
        assertTrue(mockMaker.initializeClassInstantiator().useObjenesis());
    }
}