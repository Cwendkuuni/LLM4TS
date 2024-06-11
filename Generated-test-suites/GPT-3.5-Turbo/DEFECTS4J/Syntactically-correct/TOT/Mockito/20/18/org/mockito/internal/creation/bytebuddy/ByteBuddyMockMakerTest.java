import org.junit.*;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.InternalMockHandler;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.SerializableMode;
import org.mockito.plugins.MockMaker;

import static org.junit.Assert.*;

public class ByteBuddyMockMakerTest {

    private ByteBuddyMockMaker byteBuddyMockMaker;

    @Before
    public void setUp() {
        byteBuddyMockMaker = new ByteBuddyMockMaker();
    }

    @Test
    public void testCreateMock() {
        MockCreationSettings<Object> mockCreationSettings = new MockCreationSettings<>();
        mockCreationSettings.setTypeToMock(Object.class);
        mockCreationSettings.setSerializableMode(SerializableMode.ACROSS_CLASSLOADERS);

        MockHandler mockHandler = new InternalMockHandler();

        try {
            byteBuddyMockMaker.createMock(mockCreationSettings, mockHandler);
            fail("Expected MockitoException to be thrown");
        } catch (MockitoException e) {
            assertEquals("Serialization across classloaders not yet supported with ByteBuddyMockMaker", e.getMessage());
        }
    }

    @Test
    public void testCreateMock_ClassCastException() {
        MockCreationSettings<Object> mockCreationSettings = new MockCreationSettings<>();
        mockCreationSettings.setTypeToMock(Integer.class);
        mockCreationSettings.setSerializableMode(SerializableMode.NONE);

        MockHandler mockHandler = new InternalMockHandler();

        try {
            byteBuddyMockMaker.createMock(mockCreationSettings, mockHandler);
            fail("Expected MockitoException to be thrown");
        } catch (MockitoException e) {
            assertTrue(e.getMessage().contains("ClassCastException occurred while creating the mockito mock"));
        }
    }

    @Test
    public void testCreateMock_InstantiationException() {
        MockCreationSettings<Object> mockCreationSettings = new MockCreationSettings<>();
        mockCreationSettings.setTypeToMock(AbstractClass.class);
        mockCreationSettings.setSerializableMode(SerializableMode.NONE);

        MockHandler mockHandler = new InternalMockHandler();

        try {
            byteBuddyMockMaker.createMock(mockCreationSettings, mockHandler);
            fail("Expected MockitoException to be thrown");
        } catch (MockitoException e) {
            assertTrue(e.getMessage().contains("Unable to create mock instance of type 'AbstractClass'"));
        }
    }

    @Test
    public void testEnsureMockIsAssignableToMockedType() {
        MockCreationSettings<Object> mockCreationSettings = new MockCreationSettings<>();
        mockCreationSettings.setTypeToMock(Object.class);

        Object mock = new Object();
        Object result = byteBuddyMockMaker.ensureMockIsAssignableToMockedType(mockCreationSettings, mock);

        assertSame(Object.class, result.getClass());
    }

    @Test
    public void testGetHandler() {
        Object mock = new Object();
        MockHandler result = byteBuddyMockMaker.getHandler(mock);

        assertNull(result);
    }

    @Test
    public void testResetMock() {
        Object mock = new Object();
        MockHandler newHandler = new InternalMockHandler();
        MockCreationSettings<Object> settings = new MockCreationSettings<>();

        try {
            byteBuddyMockMaker.resetMock(mock, newHandler, settings);
            fail("Expected ClassCastException to be thrown");
        } catch (ClassCastException e) {
            assertEquals(MockMethodInterceptor.MockAccess.class, ((MockMethodInterceptor.MockAccess) mock).getMockitoInterceptor().getClass());
        }
    }

    @Ignore
    @Test
    public void testInitializeClassInstantiator() {
        // TODO: Implement test for initializeClassInstantiator()
    }

    @Test
    public void testAsInternalMockHandler() {
        MockHandler mockHandler = new InternalMockHandler();
        InternalMockHandler result = byteBuddyMockMaker.asInternalMockHandler(mockHandler);

        assertSame(InternalMockHandler.class, result.getClass());
    }

}

class AbstractClass {}