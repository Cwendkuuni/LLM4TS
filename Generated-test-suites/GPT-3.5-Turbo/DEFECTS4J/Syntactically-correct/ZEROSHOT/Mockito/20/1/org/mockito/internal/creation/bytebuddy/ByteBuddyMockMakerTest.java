import org.junit.*;
import org.mockito.*;
import org.mockito.exceptions.*;
import org.mockito.internal.configuration.*;
import org.mockito.internal.creation.bytebuddy.*;
import org.mockito.internal.creation.instance.*;
import org.mockito.invocation.*;
import org.mockito.mock.*;
import org.mockito.plugins.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.internal.util.StringJoiner.*;

public class ByteBuddyMockMakerTest {

    @Test
    public void testCreateMock() {
        // Create mock objects for testing
        MockCreationSettings mockCreationSettings = mock(MockCreationSettings.class);
        MockHandler mockHandler = mock(MockHandler.class);
        SerializableMode serializableMode = mock(SerializableMode.class);
        ClassInstantiator classInstantiator = mock(ClassInstantiator.class);
        CachingMockBytecodeGenerator cachingMockBytecodeGenerator = mock(CachingMockBytecodeGenerator.class);

        // Create instance of ByteBuddyMockMaker
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();

        // Provide necessary mocks for method invocation
        when(mockCreationSettings.getSerializableMode()).thenReturn(serializableMode);
        when(serializableMode == SerializableMode.ACROSS_CLASSLOADERS).thenReturn(false);
        when(mockCreationSettings.getTypeToMock()).thenReturn(Object.class);
        when(mockCreationSettings.getExtraInterfaces()).thenReturn(new Class<?>[]{});

        // Mock behavior of cachingMockBytecodeGenerator
        when(cachingMockBytecodeGenerator.get(Object.class, new Class<?>[]{})).thenReturn(Object.class);

        // Mock behavior of classInstantiator
        when(classInstantiator.instantiate(Object.class)).thenReturn(mock(Object.class));
        MockMethodInterceptor.MockAccess mockAccess = (MockMethodInterceptor.MockAccess)mock(Object.class);
        when(mockAccess.getMockitoInterceptor()).thenReturn(mock(MockMethodInterceptor.class));

        // Perform the test
        Object mockInstance = byteBuddyMockMaker.createMock(mockCreationSettings, mockHandler);

        // Verify the results
        assertNotNull(mockInstance);
        verify(mockAccess, times(1)).setMockitoInterceptor(any(MockMethodInterceptor.class));
        verify(mockCreationSettings, times(1)).getTypeToMock();
        verify(mockCreationSettings, times(1)).getTypeToMock().cast(mockInstance);
    }

    @Test
    public void testEnsureMockIsAssignableToMockedType() {
        // Create mock objects for testing
        MockCreationSettings mockCreationSettings = mock(MockCreationSettings.class);
        Object mock = mock(Object.class);

        // Create instance of ByteBuddyMockMaker
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();

        // Provide necessary mocks for method invocation
        when(mockCreationSettings.getTypeToMock()).thenReturn(Object.class);

        // Perform the test
        Object result = byteBuddyMockMaker.ensureMockIsAssignableToMockedType(mockCreationSettings, mock);

        // Verify the results
        assertNotNull(result);
        assertSame(mock, result);
        verify(mockCreationSettings, times(1)).getTypeToMock();
    }

    @Test
    public void testDescribeClass() {
        // Create test objects
        Class type = Object.class;
        Object instance = new Object();

        // Create instance of ByteBuddyMockMaker
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();

        // Perform the test
        String result1 = byteBuddyMockMaker.describeClass(type);
        String result2 = byteBuddyMockMaker.describeClass(instance);

        // Verify the results
        assertEquals("'java.lang.Object', loaded by classloader : 'null'", result1);
        assertEquals("'java.lang.Object', loaded by classloader : 'java.net.URLClassLoader@1bc93013'", result2);
    }

    @Test
    public void testGetHandler() {
        // Create mock objects for testing
        Object mock = mock(Object.class);
        MockMethodInterceptor.MockAccess mockAccess = (MockMethodInterceptor.MockAccess)mock;
        MockHandler mockHandler = mock(MockHandler.class);

        // Create instance of ByteBuddyMockMaker
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();

        // Mock behavior of mockAccess
        when(mockAccess.getMockitoInterceptor()).thenReturn(mock(MockMethodInterceptor.class));
        when(mockAccess.getMockitoInterceptor().getMockHandler()).thenReturn(mockHandler);

        // Perform the test
        MockHandler result = byteBuddyMockMaker.getHandler(mock);

        // Verify the results
        assertNotNull(result);
        assertSame(mockHandler, result);
        verify(mockAccess, times(1)).getMockitoInterceptor();
        verify(mockAccess, times(1)).getMockitoInterceptor().getMockHandler();
    }

    @Test
    public void testResetMock() {
        // Create mock objects for testing
        Object mock = mock(Object.class);
        MockHandler mockHandler = mock(MockHandler.class);
        MockCreationSettings settings = mock(MockCreationSettings.class);
        MockMethodInterceptor.MockAccess mockAccess = (MockMethodInterceptor.MockAccess)mock;

        // Create instance of ByteBuddyMockMaker
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();

        // Mock behavior of mockAccess
        when(mockAccess.getMockitoInterceptor()).thenReturn(mock(MockMethodInterceptor.class));

        // Perform the test
        byteBuddyMockMaker.resetMock(mock, mockHandler, settings);

        // Verify the results
        verify(mockAccess, times(1)).setMockitoInterceptor(any(MockMethodInterceptor.class));
        verify(mockAccess, times(1)).getMockitoInterceptor();
    }

    @Test
    public void testInitializeClassInstantiator() {
        // Create instance of ByteBuddyMockMaker
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();

        // Perform the test
        ClassInstantiator result = byteBuddyMockMaker.initializeClassInstantiator();

        // Verify the results
        assertNotNull(result);
    }

    @Test(expected = MockitoException.class)
    public void testAsInternalMockHandler() {
        // Create test object
        MockHandler mockHandler = mock(MockHandler.class);

        // Create instance of ByteBuddyMockMaker
        ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();

        // Perform the test
        InternalMockHandler result = byteBuddyMockMaker.asInternalMockHandler(mockHandler);

        // Verify the results
        assertNotNull(result);
    }
}