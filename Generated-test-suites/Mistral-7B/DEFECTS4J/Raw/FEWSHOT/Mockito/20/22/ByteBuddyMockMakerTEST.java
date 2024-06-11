package org.mockito.internal.creation.bytebuddy;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.internal.util.StringJoiner.join;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.MockHandler;
import org.mockito.plugins.MockMaker;
import org.mockito.plugins.MockMakerRunner;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.SerializableMode;

@RunWith(MockMakerRunner.class)
public class ByteBuddyMockMakerTest {

    private final MockMaker mockMaker = new ByteBuddyMockMaker();

    @Test
    public void testCreateMock() {
        Class<?> mockableType = String[].class;
        MockHandler mockHandler = Mockito.mock(MockHandler.class);
        MockCreationSettings<Object> settings = new MockCreationSettings.Builder()
                .forType(mockableType)
                .serializable(SerializableMode.IN_VM)
                .build();

        Object mock = mockMaker.createMock(settings, mockHandler);

        assertNotNull(mock);
        assertNotNull(mockMaker.getHandler(mock));
    }

    @Test(expected = MockitoException.class)
    public void testCreateMockWithSerializableAcrossClassloaders() {
        Class<?> mockableType = String[].class;
        MockHandler mockHandler = Mockito.mock(MockHandler.class);
        MockCreationSettings<Object> settings = new MockCreationSettings.Builder()
                .forType(mockableType)
                .serializable(SerializableMode.ACROSS_CLASSLOADERS)
                .build();

        mockMaker.createMock(settings, mockHandler);
    }

    @Test
    public void testResetMock() {
        Class<?> mockableType = String[].class;
        MockHandler mockHandler = Mockito.mock(MockHandler.class);
        MockCreationSettings<Object> settings = new MockCreationSettings.Builder()
                .forType(mockableType)
                .serializable(SerializableMode.IN_VM)
                .build();

        Object mock = mockMaker.createMock(settings, mockHandler);
        mockMaker.resetMock(mock, mockHandler, settings);

        assertNotNull(mock);
        MockHandler newMockHandler = Mockito.mock(MockHandler.class);
        mockMaker.resetMock(mock, newMockHandler, settings);
        assertNull(mockMaker.getHandler(mock));
    }

    @Test
    public void testDescribeClass() {
        Class<?> mockableType1 = String[].class;
        Class<?> mockableType2 = Integer.class;

        String description1 = ByteBuddyMockMaker.describeClass(mockableType1);
        String description2 = ByteBuddyMockMaker.describeClass(mockableType2);

        assertEquals(description1, "class [Ljava.lang.String;, loaded by classloader : sun.misc.Launcher$AppClassLoader@18b4aac2");
        assertEquals(description2, "class java.lang.Integer, loaded by classloader : sun.misc.Launcher$AppClassLoader@18b4aac2");
    }

    @Test
    public void testInitializeClassInstantiator() {
        Class<?> classInstantiatorType = ByteBuddyMockMaker.class.getPackage().getName() + ".ClassInstantiator";

        try {
            Class<?> classInstantiator = Class.forName(classInstantiatorType);
            assertNotNull(classInstantiator);
        } catch (ClassNotFoundException e) {
            throw new MockitoException("Unexpected error", e);
        }
    }
}