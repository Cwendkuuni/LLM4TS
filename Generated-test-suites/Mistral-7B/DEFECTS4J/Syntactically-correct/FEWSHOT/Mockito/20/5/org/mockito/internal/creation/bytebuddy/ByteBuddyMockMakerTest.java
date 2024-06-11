package org.mockito.internal.creation.bytebuddy;

import static org.junit.Assert.*;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.internal.creation.instance.*;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.SerializableMode;
import org.mockito.plugins.MockMaker;
import org.mockito.Mockito;

public class ByteBuddyMockMakerTest {

    private final MockMaker mockMaker = new ByteBuddyMockMaker();

    @Test
    public void testCreateMockWithSerializableMode() {
        try {
            mockMaker.createMock(new MockCreationSettings<Object>() {{
                setSerializableMode(SerializableMode.SERIALIZABLE);
            }}, Mockito.mock(MockHandler.class));
            fail("Expected MockitoException as serialization is not supported with ByteBuddyMockMaker");
        } catch (MockitoException e) {
            // expected
        }
    }

    @Test
    public void testCreateMockWithAcrossClassloadersSerializableMode() {
        try {
            mockMaker.createMock(new MockCreationSettings<Object>() {{
                setSerializableMode(SerializableMode.ACROSS_CLASSLOADERS);
            }}, Mockito.mock(MockHandler.class));
            fail("Expected MockitoException as serialization across classloaders is not supported with ByteBuddyMockMaker");
        } catch (MockitoException e) {
            // expected
        }
    }

    @Test
    public void testCreateMockWithSerializableModeSupported() {
        try {
            Class<?> mockedType = Mockito.mock(Class.class);
            MockHandler mockHandler = Mockito.mock(MockHandler.class);
            Object mock = mockMaker.createMock(new MockCreationSettings<Object>() {{
                setSerializableMode(SerializableMode.SERIALIZE);
                setTypeToMock(mockedType);
            }}, mockHandler);
            assertNotNull(mock);
            assertInstanceOf(mockedType, mock);
            Mockito.verify(mockHandler).handle(Mockito.any(MockMethodCall.class));
        } catch (MockitoException e) {
            fail("Unexpected MockitoException: " + e.getMessage());
        }
    }

    @Test
    public void testCreateMockWithAcrossClassloaderSupported() {
        try {
            Class<?> mockedType = Mockito.mock(Class.class);
            MockHandler mockHandler = Mockito.mock(MockHandler.class);
            ClassLoader classLoader = getClass().getClassLoader();
            ClassLoader anotherClassLoader = ClassLoader.getSystemClassLoader();
            Object mock = mockMaker.createMock(new MockCreationSettings<Object>() {{
                setSerializableMode(SerializableMode.ACROSS_CLASSLOADERS);
                setTypeToMock(mockedType);
                setClassLoader(anotherClassLoader);
            }}, mockHandler);
            assertNotNull(mock);
            assertInstanceOf(mockedType, mock);
            Mockito.verify(mockHandler).handle(Mockito.any(MockMethodCall.class));
        } catch (MockitoException e) {
            fail("Unexpected MockitoException: " + e.getMessage());
        }
    }

    @Test
    public void testCreateMockWithNullTypeToMock() {
        try {
            MockHandler mockHandler = Mockito.mock(MockHandler.class);
            mockMaker.createMock(new MockCreationSettings<>(), mockHandler);
            fail("Expected MockitoException as typeToMock cannot be null");
        } catch (MockitoException e) {
            // expected
        }
    }

    @Test
    public void testCreateMockWithNullMockHandler() {
        try {
            Class<?> mockedType = Mockito.mock(Class.class);
            mockMaker.createMock(new MockCreationSettings<Object>() {{
                setTypeToMock(mockedType);
            }}, null);
            fail("Expected MockitoException as mockHandler cannot be null");
        } catch (MockitoException e) {
            // expected
        }
    }

    @Test
    public void testGetHandler() {
        Class<?> mockedType = Mockito.mock(Class.class);
        Object mock = mockMaker.createMock(new MockCreationSettings<Object>() {{
            setTypeToMock(mockedType);
        }}, Mockito.mock(MockHandler.class));
        MockHandler handler = mockMaker.getHandler(mock);
        assertNotNull(handler);
    }

    @Test
    public void testResetMock() {
        Class<?> mockedType = Mockito.mock(Class.class);
        MockHandler mockHandler = Mockito.mock(MockHandler.class);
        Object mock = mockMaker.createMock(new MockCreationSettings<Object>() {{
            setTypeToMock(mockedType);
        }}, mockHandler);
        mockMaker.resetMock(mock, Mockito.mock(MockHandler.class), new MockCreationSettings<Object>());
        MockHandler newMockHandler = mockMaker.getHandler(mock);
        assertNotEquals(mockHandler, newMockHandler);
    }

    @Test
    public void testInitializeClassInstantiator() {
        try {
            mockMaker.initializeClassInstantiator();
        } catch (Throwable e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = IllegalStateException.class)
    public void testInitializeClassInstantiatorWithObjenesisMissing() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        mockMaker.initializeClassInstantiator();
    }

    @Test
    public void testAsInternalMockHandler() {
        MockHandler mockHandler = Mockito.mock(MockHandler.class);
        InternalMockHandler internalMockHandler = mockMaker.asInternalMockHandler(mockHandler);
        assertInstanceOf(InternalMockHandler.class, internalMockHandler);
        assertEquals(mockHandler, internalMockHandler.getMockHandler());
    }

    @Test(expected = MockitoException.class)
    public void testAsInternalMockHandlerWithNonInternalMockHandler() {
        MockHandler mockHandler = Mockito.mock(MockHandler.class);
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        mockMaker.asInternalMockHandler(mockHandler);
    }
}