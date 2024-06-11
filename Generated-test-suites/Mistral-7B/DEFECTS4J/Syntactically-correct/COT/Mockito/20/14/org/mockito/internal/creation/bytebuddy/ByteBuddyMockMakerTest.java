import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.creation.bytebuddy.ByteBuddyMockMaker;
import org.mockito.internal.creation.bytebuddy.CachingMockBytecodeGenerator;
import org.mockito.internal.creation.bytebuddy.ClassInstantiator;
import org.mockito.internal.creation.bytebuddy.MockHandler;
import org.mockito.internal.creation.bytebuddy.MockMethodInterceptor;
import org.mockito.internal.creation.instance.InstantiationException;
import org.mockito.plugins.MockMaker;

public class ByteBuddyMockMakerTest {

    @Test
    public void testCreateMock() {
        MockMaker mockMaker = new ByteBuddyMockMaker();
        MockHandler handler = Mockito.mock(MockHandler.class);

        try {
            Object mock = mockMaker.createMock(new MockCreationSettings<>(), handler);
            MockMethodInterceptor mockAccess = (MockMethodInterceptor) mock;
            assertEquals(handler, mockAccess.getMockitoInterceptor().getMockHandler());
        } catch (MockitoException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = MockitoException.class)
    public void testCreateMockWithSerializableModeAcrossClassLoaders() {
        MockMaker mockMaker = new ByteBuddyMockMaker();
        MockHandler handler = Mockito.mock(MockHandler.class);
        MockCreationSettings settings = new MockCreationSettings<>().serializableMode(SerializableMode.ACROSS_CLASSLOADERS);

        try {
            mockMaker.createMock(settings, handler);
            fail("Expected MockitoException");
        } catch (MockitoException e) {
            // Expected
        }
    }

    @Test(expected = ClassCastException.class)
    public void testEnsureMockIsAssignableToMockedTypeWithClassCastException() {
        MockMaker mockMaker = new ByteBuddyMockMaker();
        MockHandler handler = Mockito.mock(MockHandler.class);
        Class<?> wrongType = Object.class;

        try {
            MockCreationSettings settings = new MockCreationSettings<>().typeToMock(wrongType);
            Object mock = mockMaker.createMock(settings, handler);
            mockMaker.ensureMockIsAssignableToMockedType(settings, mock);
            fail("Expected ClassCastException");
        } catch (ClassCastException e) {
            // Expected
        }
    }

    @Test
    public void testDescribeClass() {
        Class<?> mockType = Mockito.mock(String.class);
        assertEquals("'" + mockType.getCanonicalName() + "', loaded by classloader : '" + mockType.getClassLoader() + "'", ByteBuddyMockMaker.describeClass(mockType));
    }

    @Test
    public void testGetHandler() {
        Object mock = Mockito.mock(MockMethodInterceptor.MockAccess.class);
        MockHandler handler = Mockito.mock(MockHandler.class);
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockMethodInterceptor mockAccess = (MockMethodInterceptor) mock;

        mockAccess.setMockitoInterceptor(new MockMethodInterceptor(Mockito.mock(InternalMockHandler.class), new MockCreationSettings<>()));
        assertEquals(handler, mockMaker.getHandler(mock));
    }

    @Test
    public void testResetMock() {
        Object mock = Mockito.mock(MockMethodInterceptor.MockAccess.class);
        MockHandler newHandler = Mockito.mock(MockHandler.class);
        MockCreationSettings settings = new MockCreationSettings<>();
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();

        mockMaker.resetMock(mock, newHandler, settings);
        MockMethodInterceptor mockAccess = (MockMethodInterceptor) mock;
        assertEquals(newHandler, mockAccess.getMockitoInterceptor().getMockHandler());
    }

    @Test(expected = IllegalStateException.class)
    public void testInitializeClassInstantiatorWithMissingObjenesis() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        mockMaker.initializeClassInstantiator();
        fail("Expected IllegalStateException");
    }

    @Test
    public void testAsInternalMockHandler() {
        MockHandler handler = Mockito.mock(MockHandler.class);
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();

        assertEquals(handler, mockMaker.asInternalMockHandler(handler));
    }
}