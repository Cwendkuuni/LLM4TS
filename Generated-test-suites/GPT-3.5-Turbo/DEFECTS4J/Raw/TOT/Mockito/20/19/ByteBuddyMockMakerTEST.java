import org.junit.Test;
import org.mockito.MockCreationSettings;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.InternalMockHandler;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.SerializableMode;
import org.mockito.plugins.MockMaker;

import static org.junit.Assert.*;

public class ByteBuddyMockMakerTest {

    @Test
    public void createMock_SerializableModeACROSS_CLASSLOADERS_ThrowsException() {
        // Arrange
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockCreationSettings settings = new MockCreationSettings();
        settings.setSerializableMode(SerializableMode.ACROSS_CLASSLOADERS);
        MockHandler handler = new InternalMockHandler();

        try {
            // Act
            mockMaker.createMock(settings, handler);

            // Assert
            fail("Should have thrown MockitoException");
        } catch (MockitoException ex) {
            // Assert
            assertEquals("Serialization across classloaders not yet supported with ByteBuddyMockMaker", ex.getMessage());
        }
    }

    @Test
    public void createMock_SuccessfulMockCreation_ReturnsMockObjectAssignableToMockedType() {
        // Arrange
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockCreationSettings settings = new MockCreationSettings();
        settings.setTypeToMock(MockCreationSettings.class);
        MockHandler handler = new InternalMockHandler();

        // Act
        MockCreationSettings result = mockMaker.createMock(settings, handler);

        // Assert
        assertNotNull(result);
        assertTrue(MockCreationSettings.class.isAssignableFrom(result.getClass()));
    }

    @Test
    public void createMock_FailedMockCreationDueToClassCastException_ThrowsException() {
        // Arrange
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockCreationSettings settings = new MockCreationSettings();
        settings.setTypeToMock(Integer.class);
        MockHandler handler = new InternalMockHandler();

        try {
            // Act
            mockMaker.createMock(settings, handler);

            // Assert
            fail("Should have thrown MockitoException");
        } catch (MockitoException ex) {
            // Assert
            assertTrue(ex.getMessage().contains("ClassCastException occurred while creating the mockito mock"));
        }
    }

    @Test
    public void createMock_FailedMockCreationDueToInstantiationException_ThrowsException() {
        // Arrange
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockCreationSettings settings = new MockCreationSettings();
        settings.setTypeToMock(Void.class);
        MockHandler handler = new InternalMockHandler();

        try {
            // Act
            mockMaker.createMock(settings, handler);

            // Assert
            fail("Should have thrown MockitoException");
        } catch (MockitoException ex) {
            // Assert
            assertTrue(ex.getMessage().contains("Unable to create mock instance"));
        }
    }

    @Test
    public void ensureMockIsAssignableToMockedType_MockIsAssignable_ReturnsMockOfType() {
        // Arrange
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockCreationSettings settings = new MockCreationSettings();
        settings.setTypeToMock(MockCreationSettings.class);
        Object mock = new MockCreationSettings();

        // Act
        MockCreationSettings result = mockMaker.ensureMockIsAssignableToMockedType(settings, mock);

        // Assert
        assertNotNull(result);
        assertTrue(MockCreationSettings.class.isAssignableFrom(result.getClass()));
    }

    @Test
    public void ensureMockIsAssignableToMockedType_MockIsNotAssignable_ThrowsClassCastException() {
        // Arrange
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockCreationSettings settings = new MockCreationSettings();
        settings.setTypeToMock(Integer.class);
        Object mock = "not a mock object";

        try {
            // Act
            mockMaker.ensureMockIsAssignableToMockedType(settings, mock);

            // Assert
            fail("Should have thrown ClassCastException");
        } catch (ClassCastException ex) {
            // Assert
            assertTrue(ex.getMessage().contains("cannot be cast to"));
        }
    }

    @Test
    public void describeClass_ClassIsNull_ReturnsNullDescription() {
        // Arrange
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        Class type = null;

        // Act
        String result = mockMaker.describeClass(type);

        // Assert
        assertEquals("null", result);
    }

    @Test
    public void describeClass_ClassIsNotNull_ReturnsClassDescription() {
        // Arrange
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        Class type = MockCreationSettings.class;

        // Act
        String result = mockMaker.describeClass(type);

        // Assert
        assertEquals("'org.mockito.mock.MockCreationSettings', loaded by classloader : 'null'", result);
    }

    @Test
    public void describeClass_InstanceIsNull_ReturnsNullDescription() {
        // Arrange
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        Object instance = null;

        // Act
        String result = mockMaker.describeClass(instance);

        // Assert
        assertEquals("null", result);
    }

    @Test
    public void describeClass_InstanceIsNotNull_ReturnsClassDescription() {
        // Arrange
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        Object instance = new MockCreationSettings();

        // Act
        String result = mockMaker.describeClass(instance);

        // Assert
        assertEquals("'org.mockito.mock.MockCreationSettings', loaded by classloader : 'null'", result);
    }

    @Test
    public void getHandler_InvalidMockObject_ReturnsNull() {
        // Arrange
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        Object mock = new Object();

        // Act
        MockHandler result = mockMaker.getHandler(mock);

        // Assert
        assertNull(result);
    }

    @Test
    public void getHandler_ValidMockObject_ReturnsMockHandler() {
        // Arrange
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        Object mock = new MockMethodInterceptor.MockAccess();

        // Act
        MockHandler result = mockMaker.getHandler(mock);

        // Assert
        assertNotNull(result);
        assertTrue(InternalMockHandler.class.isAssignableFrom(result.getClass()));
    }

    @Test
    public void resetMock_ValidMockObject_ResetsMockHandler() {
        // Arrange
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        Object mock = new MockMethodInterceptor.MockAccess();
        MockHandler newHandler = new InternalMockHandler();
        MockCreationSettings settings = new MockCreationSettings();

        // Act
        mockMaker.resetMock(mock, newHandler, settings);

        // Assert
        MockMethodInterceptor.MockAccess mockAccess = (MockMethodInterceptor.MockAccess) mock;
        InternalMockHandler mockitoInterceptor = mockAccess.getMockitoInterceptor();
        assertNotNull(mockitoInterceptor);
        assertTrue(InternalMockHandler.class.isAssignableFrom(mockitoInterceptor.getClass()));
        assertEquals(newHandler, mockitoInterceptor.getMockHandler());
    }

    @Test(expected = MockitoException.class)
    public void resetMock_InvalidMockObject_ThrowsException() {
        // Arrange
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        Object mock = new Object();
        MockHandler newHandler = new InternalMockHandler();
        MockCreationSettings settings = new MockCreationSettings();

        // Act
        mockMaker.resetMock(mock, newHandler, settings);
    }

    @Test
    public void initializeClassInstantiator_ObjenesisIsNotAvailable_ThrowsException() {
        // Arrange
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();

        try {
            // Act
            ClassInstantiator result = mockMaker.initializeClassInstantiator();

            // Assert
            fail("Should have thrown IllegalStateException");
        } catch (IllegalStateException ex) {
            // Assert
            assertTrue(ex.getMessage().contains("Mockito could not create mock: Objenesis is missing on the classpath."));
        }
    }

    @Test
    public void asInternalMockHandler_SupportedMockHandler_ReturnsInternalMockHandler() {
        // Arrange
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockHandler handler = new InternalMockHandler();

        // Act
        InternalMockHandler result = mockMaker.asInternalMockHandler(handler);

        // Assert
        assertNotNull(result);
        assertTrue(InternalMockHandler.class.isAssignableFrom(result.getClass()));
    }

    @Test(expected = MockitoException.class)
    public void asInternalMockHandler_UnsupportedMockHandler_ThrowsException() {
        // Arrange
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockHandler handler = new MockHandler() {
            @Override
            public Object handle(MockInvocation invocation) throws Throwable {
                return null;
            }
        };

        // Act
        mockMaker.asInternalMockHandler(handler);
    }

}