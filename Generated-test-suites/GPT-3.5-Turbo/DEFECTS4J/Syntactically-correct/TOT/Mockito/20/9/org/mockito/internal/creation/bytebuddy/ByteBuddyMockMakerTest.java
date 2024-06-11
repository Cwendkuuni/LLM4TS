import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.SerializableMode;
import org.mockito.plugins.MockMaker;

public class ByteBuddyMockMakerTest {

    private ByteBuddyMockMaker byteBuddyMockMaker;

    @Before
    public void setUp() {
        byteBuddyMockMaker = new ByteBuddyMockMaker();
    }

    @Test
    public void createMock_NoSerializableMode_SerializableModeNotSupportedExceptionThrown() {
        MockCreationSettings<String> settings = mock(MockCreationSettings.class);
        MockHandler handler = mock(MockHandler.class);

        when(settings.getSerializableMode()).thenReturn(SerializableMode.NONE);

        try {
            byteBuddyMockMaker.createMock(settings, handler);
            fail("Expected MockitoException to be thrown");
        } catch (MockitoException e) {
            assertEquals("Serialization across classloaders not yet supported with ByteBuddyMockMaker", e.getMessage());
        }
    }

    @Test
    public void createMock_SerializableModeACrossClassloaders_ClassCastExceptionThrown() {
        MockCreationSettings<Integer> settings = mock(MockCreationSettings.class);
        MockHandler handler = mock(MockHandler.class);

        when(settings.getSerializableMode()).thenReturn(SerializableMode.ACROSS_CLASSLOADERS);
        when(settings.getTypeToMock()).thenReturn(Integer.class);

        try {
            byteBuddyMockMaker.createMock(settings, handler);
            fail("Expected MockitoException to be thrown");
        } catch (MockitoException e) {
            assertEquals("ClassCastException occurred while creating the mockito mock :\n" +
                    "  class to mock : 'java.lang.Integer', loaded by classloader : 'null'\n" +
                    "  created class : 'null', loaded by classloader : 'null'\n" +
                    "  proxy instance class : 'java.lang.Integer', loaded by classloader : 'null'\n" +
                    "  instance creation by : 'ClassInstantiator', loaded by classloader : 'null'\n" +
                    "\n" +
                    "You might experience classloading issues, please ask the mockito mailing-list.\n" +
                    "", e.getMessage());
        }
    }

    @Test
    public void createMock_MockInstanceCreationFailed_InstantiationExceptionThrown() {
        MockCreationSettings<Double> settings = mock(MockCreationSettings.class);
        MockHandler handler = mock(MockHandler.class);

        when(settings.getSerializableMode()).thenReturn(SerializableMode.NEW_SERIALIZABLE);
        when(settings.getTypeToMock()).thenReturn(Double.class);

        try {
            byteBuddyMockMaker.createMock(settings, handler);
            fail("Expected MockitoException to be thrown");
        } catch (MockitoException e) {
            assertEquals("Unable to create mock instance of type 'Number'", e.getMessage());
        }
    }

    @Test
    public void ensureMockIsAssignableToMockedType_MockIsAssignableToMockedType_ReturnsMock() {
        MockCreationSettings<String> settings = mock(MockCreationSettings.class);
        String mock = "mock";

        when(settings.getTypeToMock()).thenReturn(String.class);

        String result = byteBuddyMockMaker.ensureMockIsAssignableToMockedType(settings, mock);

        assertEquals(mock, result);
    }

    @Test
    public void ensureMockIsAssignableToMockedType_MockIsNotAssignableToMockedType_ClassCastExceptionThrown() {
        MockCreationSettings<Number> settings = mock(MockCreationSettings.class);
        String mock = "mock";

        when(settings.getTypeToMock()).thenReturn(Number.class);

        try {
            byteBuddyMockMaker.ensureMockIsAssignableToMockedType(settings, mock);
            fail("Expected ClassCastException to be thrown");
        } catch (ClassCastException e) {
            // Success
        }
    }

    @Test
    public void describeClass_TypeIsNull_ReturnsNull() {
        assertNull(byteBuddyMockMaker.describeClass(null));
    }

    @Test
    public void describeClass_TypeIsNotNull_ReturnsClassDescription() {
        Class<?> type = Double.class;

        String result = byteBuddyMockMaker.describeClass(type);

        assertEquals("'java.lang.Double', loaded by classloader : 'null'", result);
    }

    @Test
    public void describeClass_InstanceIsNull_ReturnsNull() {
        assertNull(byteBuddyMockMaker.describeClass(null));
    }

    @Test
    public void describeClass_InstanceIsNotNull_ReturnsClassDescription() {
        String instance = "instance";

        String result = byteBuddyMockMaker.describeClass(instance);

        assertEquals("'java.lang.String', loaded by classloader : 'sun.misc.Launcher$AppClassLoader'", result);
    }

    @Test
    public void getHandler_MockIsNotInstanceOfMockAccess_ReturnsNull() {
        Object mock = "mock";

        MockHandler result = byteBuddyMockMaker.getHandler(mock);

        assertNull(result);
    }

    @Test
    public void getHandler_MockIsInstanceOfMockAccess_ReturnsMockHandler() {
        MockMethodInterceptor.MockAccess mock = mock(MockMethodInterceptor.MockAccess.class);
        MockHandler handler = mock(MockHandler.class);

        when(mock.getMockitoInterceptor()).thenReturn(new MockMethodInterceptor(handler));
        
        MockHandler result = byteBuddyMockMaker.getHandler(mock);
        
        assertEquals(handler, result);
    }

    @Test
    public void resetMock_MockIsInstanceOfMockAccess_MockitoInterceptorSetWithNewHandler() {
        MockMethodInterceptor.MockAccess mock = mock(MockMethodInterceptor.MockAccess.class);
        MockHandler handler = mock(MockMethodHandler.class);
        MockCreationSettings settings = mock(MockCreationSettings.class);

        byteBuddyMockMaker.resetMock(mock, handler, settings);

        verify(mock).setMockitoInterceptor(any(MockMethodInterceptor.class));
    }

    @Test(expected = MockitoException.class)
    public void resetMock_MockIsNotInstanceOfMockAccess_MockitoExceptionThrown() {
        Object mock = "mock";
        MockHandler handler = mock(MockHandler.class);
        MockCreationSettings settings = mock(MockCreationSettings.class);

        byteBuddyMockMaker.resetMock(mock, handler, settings);
    }

    @Test
    public void initializeClassInstantiator_ObjenesisClassFound_ClassInstantiatorCreatedAndReturned() {
        ClassInstantiator result = byteBuddyMockMaker.initializeClassInstantiator();

        assertNotNull(result);
    }

    @Test(expected = IllegalStateException.class)
    public void initializeClassInstantiator_ObjenesisClassNotFound_IllegalStateExceptionThrown() {
        try {
            GlobalConfiguration globalConfiguration = new GlobalConfiguration();
            globalConfiguration.setEnableClassCache(false);
            new GlobalConfiguration();
        } catch (Throwable throwable) {
            throw new IllegalStateException("Mockito could not create mock: Objenesis is missing on the classpath.\n" +
                    "Please add Objenesis on the classpath.");
        }
    }

    @Test
    public void asInternalMockHandler_HandlerIsInstanceOfInternalMockHandler_ReturnsInternalMockHandler() {
        InternalMockHandler handler = mock(InternalMockHandler.class);

        InternalMockHandler result = byteBuddyMockMaker.asInternalMockHandler(handler);

        assertEquals(handler, result);
    }

    @Test(expected = MockitoException.class)
    public void asInternalMockHandler_HandlerIsNotInstanceOfInternalMockHandler_MockitoExceptionThrown() {
        Object handler = mock(Object.class);

        byteBuddyMockMaker.asInternalMockHandler(handler);
    }

}