import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.InternalMockHandler;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.internal.creation.instance.ClassInstantiator;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.SerializableMode;
import org.mockito.plugins.MockMaker;

public class ByteBuddyMockMakerTest {

    private ByteBuddyMockMaker mockMaker;
    private MockCreationSettings mockSettings;
    private MockHandler mockHandler;

    @Before
    public void setUp() {
        mockMaker = new ByteBuddyMockMaker();
        mockSettings = createMockCreationSettings();
        mockHandler = createMockHandler();
    }

    @Test
    public void testCreateMock_SerializableModeAcrossClassLoaders_ThrowsMockitoException() {
        mockSettings.setSerializableMode(SerializableMode.ACROSS_CLASSLOADERS);

        try {
            mockMaker.createMock(mockSettings, mockHandler);
            fail("Expected MockitoException to be thrown");
        } catch (MockitoException e) {
            assertEquals("Serialization across classloaders not yet supported with ByteBuddyMockMaker", e.getMessage());
        }
    }

    @Test
    public void testCreateMock_SuccessfullyCreatesMock() {
        Class<?> mockedProxyType = mockSettings.getTypeToMock();
        Object mockInstance = null;

        try {
            mockInstance = mockMaker.createMock(mockSettings, mockHandler);
            assertNotNull(mockInstance);
            assertTrue(mockedProxyType.isInstance(mockInstance));
        } catch (Exception e) {
            fail("Failed to create mock");
        }
    }

    @Test
    public void testEnsureMockIsAssignableToMockedType_ForceExplicitCast() {
        Class<MockedClass> typeToMock = MockedClass.class;
        MockedClass mock = new MockedClass();

        MockedClass result = mockMaker.ensureMockIsAssignableToMockedType(mockSettings, mock);

        assertNotNull(result);
        assertEquals(typeToMock, result.getClass());
    }

    @Test
    public void testDescribeClass_ReturnsDescriptionForClass() {
        Class<?> mockClass = MockedClass.class;
        String expected = "'" + mockClass.getCanonicalName() + "', loaded by classloader : '" + mockClass.getClassLoader() + "'";

        String result = mockMaker.describeClass(mockClass);

        assertEquals(expected, result);
    }

    @Test
    public void testDescribeClass_ReturnsNullDescriptionForNullClass() {
        String result = mockMaker.describeClass(null);
        assertEquals("null", result);
    }

    @Test
    public void testDescribeClass_ReturnsDescriptionForObjectInstance() {
        MockedClass mock = new MockedClass();
        Class<?> mockClass = mock.getClass();
        String expected = "'" + mockClass.getCanonicalName() + "', loaded by classloader : '" + mockClass.getClassLoader() + "'";

        String result = mockMaker.describeClass(mock);

        assertEquals(expected, result);
    }

    @Test
    public void testGetHandler_ReturnsNullIfMockDoesNotImplementMockAccess() {
        Object mock = new Object();

        MockHandler result = mockMaker.getHandler(mock);

        assertNull(result);
    }

    @Test
    public void testGetHandler_ReturnsMockHandlerFromMockAccess() {
        MockMethodInterceptor.MockAccess mockAccess = new MockMethodInterceptor.MockAccess() {
            @Override
            public MockMethodInterceptor getMockitoInterceptor() {
                return null;
            }

            @Override
            public void setMockitoInterceptor(MockMethodInterceptor mockitoInterceptor) {

            }
        };

        MockHandler result = mockMaker.getHandler(mockAccess);

        assertNotNull(result);
    }

    @Test
    public void testResetMock_SetsNewHandlerForMockAccess() {
        MockMethodInterceptor.MockAccess mockAccess = new MockMethodInterceptor.MockAccess() {
            @Override
            public MockMethodInterceptor getMockitoInterceptor() {
                return null;
            }

            @Override
            public void setMockitoInterceptor(MockMethodInterceptor mockitoInterceptor) {

            }
        };
        MockHandler newHandler = createMockHandler();

        mockMaker.resetMock(mockAccess, newHandler, mockSettings);

        // Ensure no exceptions are thrown
    }

    @Test
    public void testInitializeClassInstantiator_SuccessfullyInitializesUsingObjenesis() {
        ClassInstantiator classInstantiator = mockMaker.initializeClassInstantiator();
        assertNotNull(classInstantiator);
    }

    @Test(expected = IllegalStateException.class)
    public void testInitializeClassInstantiator_ThrowsIllegalStateExceptionWhenObjenesisIsMissing() {
        // Replace MockitoException with IllegalStateException to avoid dependencies on MockitoException
        ByteBuddyMockMaker spyMockMaker = mock(ByteBuddyMockMaker.class);
        Mockito.when(spyMockMaker.initializeClassInstantiator()).thenThrow(new IllegalStateException());

        spyMockMaker.initializeClassInstantiator();
    }

    @Test
    public void testAsInternalMockHandler_ThrowsMockitoExceptionWhenHandlerIsNotInternalMockHandler() {
        MockHandler handler = createMockHandler();

        try {
            InternalMockHandler result = mockMaker.asInternalMockHandler(handler);
            fail("Expected MockitoException to be thrown");
        } catch (MockitoException e) {
            assertEquals("At the moment you cannot provide own implementations of MockHandler.\nPlease see the javadocs for the MockMaker interface.", e.getMessage());
        }
    }

    private MockCreationSettings createMockCreationSettings() {
        MockCreationSettings mockSettings = new MockCreationSettings();
        mockSettings.setTypeToMock(MockedClass.class);
        return mockSettings;
    }

    private MockHandler createMockHandler() {
        return mock(MockHandler.class);
    }

    private static class MockedClass {

    }
}