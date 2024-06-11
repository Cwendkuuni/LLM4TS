package org.mockito.internal.creation.bytebuddy;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.internal.util.StringJoiner.join;
import static org.mockito.internal.util.StringJoiner.separator;

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

public class ByteBuddyMockMakerTest {

    private ByteBuddyMockMaker mockMaker;

    @Before
    public void setUp() {
        mockMaker = new ByteBuddyMockMaker();
    }

    @Test
    public void testCreateMock_SerializableMode_SerializedWithinSameClassloader() {
        MockCreationSettings<Object> settings = new MockCreationSettings.Builder()
                .forClass(String.class)
                .serializable()
                .build();

        MockHandler handler = mock(MockHandler.class);

        Object mock = mockMaker.createMock(settings, handler);
        assertNotNull(mock);

        MockHandler handlerFromMock = mockMaker.getHandler(mock);
        assertNotNull(handlerFromMock);
        assertEquals(handler, handlerFromMock);
    }

    @Test(expected = MockitoException.class)
    public void testCreateMock_SerializableMode_AcrossClassloaders() {
        MockCreationSettings<Object> settings = new MockCreationSettings.Builder()
                .forClass(String.class)
                .serializableAcrossClassloaders()
                .build();

        MockHandler handler = mock(MockHandler.class);

        mockMaker.createMock(settings, handler);
    }

    @Test
    public void testCreateMock_Descriptions() {
        MockCreationSettings<Object> settings = new MockCreationSettings.Builder()
                .forClass(String.class)
                .build();

        MockHandler handler = mock(MockHandler.class);

        Object mock = mockMaker.createMock(settings, handler);
        Class<?> mockedProxyType = mockMaker.getClassInstantiator().instantiate(settings.getTypeToMock().getClass());
        String mockDescription = describeClass(mock);
        String mockedTypeDescription = describeClass(mockedProxyType);

        assertEquals(join("Class to mock : ", mockedTypeDescription, ", Created class : ", mockDescription),
                join("Created mock for class: ", describeClass(mockedProxyType), ", using class: ", mockDescription));
    }

    @Test(expected = MockitoException.class)
    public void testCreateMock_ClassCastException() {
        MockCreationSettings<Object> settings = new MockCreationSettings.Builder()
                .forClass(String[].class)
                .build();

        MockHandler handler = mock(MockHandler.class);

        Object mock = mockMaker.createMock(settings, handler);
        Class<Object> typeToMock = settings.getTypeToMock();
        throw new AssertionError("Expected ClassCastException but got:\n" + describeClass(mock));
    }

    @Test
    public void testCreateMock_InstantiationException() {
        MockCreationSettings<Object> settings = new MockCreationSettings.Builder()
                .forClass(Throwable.class)
                .build();

        MockHandler handler = mock(MockHandler.class);

        try {
            mockMaker.createMock(settings, handler);
            fail();
        } catch (InstantiationException e) {
            // Expected
        }
    }

    @Test
    public void testResetMock() {
        MockCreationSettings<Object> settings = new MockCreationSettings.Builder()
                .forClass(String.class)
                .build();

        Object mock = mockMaker.createMock(settings, mock(MockHandler.class));
        MockHandler handler = mockMaker.getHandler(mock);

        MockHandler newHandler = mock(MockHandler.class);
        mockMaker.resetMock(mock, newHandler, settings);

        MockHandler handlerFromMock = mockMaker.getHandler(mock);

        assertEquals(newHandler, handlerFromMock);
    }

    private static String describeClass(Object instance) {
        return mockMaker.describeClass(instance);
    }

    private static String describeClass(Class<?> type) {
        return mockMaker.describeClass(type);
    }
}