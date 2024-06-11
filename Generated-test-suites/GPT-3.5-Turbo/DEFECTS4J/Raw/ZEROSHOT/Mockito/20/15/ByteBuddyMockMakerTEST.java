import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.InternalMockHandler;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.internal.creation.bytebuddy.ByteBuddyMockMaker;
import org.mockito.internal.creation.instance.ClassInstantiator;
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
    public void testCreateMock() {
        // Create mock settings
        MockCreationSettings<T> settings = mock(MockCreationSettings.class);
        MockHandler handler = mock(MockHandler.class);

        // Call the method under test
        T mock = byteBuddyMockMaker.createMock(settings, handler);

        // Verify the result
        assertNotNull(mock);
        verify(handler, times(1)).getMockitoInterceptor();
    }

    @Test(expected = MockitoException.class)
    public void testCreateMock_SerializationAcrossClassloaders() {
        // Create mock settings
        MockCreationSettings<T> settings = mock(MockCreationSettings.class);
        MockHandler handler = mock(MockHandler.class);

        // Set the serialization mode to ACROSS_CLASSLOADERS
        when(settings.getSerializableMode()).thenReturn(SerializableMode.ACROSS_CLASSLOADERS);

        // Call the method under test
        T mock = byteBuddyMockMaker.createMock(settings, handler);
    }

    @Test
    public void testEnsureMockIsAssignableToMockedType() {
        // Create mock settings
        MockCreationSettings<T> settings = mock(MockCreationSettings.class);
        T mock = mock(T.class);

        // Call the method under test
        T result = byteBuddyMockMaker.ensureMockIsAssignableToMockedType(settings, mock);

        // Verify the result
        assertNotNull(result);
        assertEquals(mock, result);
    }

    @Test
    public void testDescribeClass_TypeIsNull() {
        // Call the method under test
        String result = byteBuddyMockMaker.describeClass(null);

        // Verify the result
        assertEquals("null", result);
    }

    @Test
    public void testDescribeClass_TypeIsNotNull() {
        // Create a test class
        Class<?> testClass = String.class;

        // Call the method under test
        String result = byteBuddyMockMaker.describeClass(testClass);

        // Verify the result
        assertNotNull(result);
        assertEquals("java.lang.String, loaded by classloader : " + testClass.getClassLoader(), result);
    }

    @Test
    public void testDescribeClass_InstanceIsNull() {
        // Call the method under test
        String result = byteBuddyMockMaker.describeClass(null);

        // Verify the result
        assertEquals("null", result);
    }

    @Test
    public void testDescribeClass_InstanceIsNotNull() {
        // Create a test instance
        Object testInstance = new Object();

        // Call the method under test
        String result = byteBuddyMockMaker.describeClass(testInstance);

        // Verify the result
        assertNotNull(result);
        assertEquals("java.lang.Object, loaded by classloader : " + testInstance.getClass().getClassLoader(), result);
    }

    @Test
    public void testGetHandler() {
        // Create a mock object
        Object mock = mock(Object.class);

        // Call the method under test
        MockHandler handler = byteBuddyMockMaker.getHandler(mock);

        // Verify the result
        assertNull(handler);
    }

    @Test
    public void testResetMock() {
        // Create a mock object
        Object mock = mock(Object.class);
        MockHandler newHandler = mock(MockHandler.class);
        MockCreationSettings settings = mock(MockCreationSettings.class);

        // Call the method under test
        byteBuddyMockMaker.resetMock(mock, newHandler, settings);

        // Verify the result
        verify(mock, times(1)).setMockitoInterceptor(any(MockMethodInterceptor.class));
    }

    @Test(expected = MockitoException.class)
    public void testResetMock_InvalidMockHandler() {
        // Create a mock object
        Object mock = mock(Object.class);
        MockHandler newHandler = mock(MockHandler.class);
        MockCreationSettings settings = mock(MockCreationSettings.class);

        // Set the mock handler to be an instance of InternalMockHandler
        when(mock instanceof InternalMockHandler).thenReturn(false);

        // Call the method under test
        byteBuddyMockMaker.resetMock(mock, newHandler, settings);
    }

    @Test
    public void testInitializeClassInstantiator() {
        // Call the method under test
        ClassInstantiator result = byteBuddyMockMaker.initializeClassInstantiator();

        // Verify the result
        assertNotNull(result);
    }

    @Test(expected = IllegalStateException.class)
    public void testInitializeClassInstantiator_ObjenesisMissing() throws Exception {
        // Create a mock GlobalConfiguration object
        GlobalConfiguration configuration = mock(GlobalConfiguration.class);
        whenNew(GlobalConfiguration.class).withNoArguments().thenReturn(configuration);

        // Call the method under test
        ClassInstantiator result = byteBuddyMockMaker.initializeClassInstantiator();

        // Verify the result
        assertNotNull(result);
    }

    @Test
    public void testAsInternalMockHandler() {
        // Create a mock MockHandler object
        MockHandler handler = mock(MockHandler.class);

        // Call the method under test
        InternalMockHandler internalHandler = byteBuddyMockMaker.asInternalMockHandler(handler);

        // Verify the result
        assertNotNull(internalHandler);
    }

    @Test(expected = MockitoException.class)
    public void testAsInternalMockHandler_InvalidMockHandler() {
        // Create a mock MockHandler object
        MockHandler handler = mock(MockHandler.class);

        // Call the method under test
        InternalMockHandler internalHandler = byteBuddyMockMaker.asInternalMockHandler(handler);
    }
}