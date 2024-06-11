import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.InternalMockHandler;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.internal.creation.instance.*;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.SerializableMode;
import org.mockito.plugins.MockMaker;

import static org.junit.Assert.*;
import static org.mockito.internal.util.StringJoiner.join;

public class ByteBuddyMockMakerTest {

    private ByteBuddyMockMaker byteBuddyMockMaker = new ByteBuddyMockMaker();

    @Test
    public void testCreateMock() {
        // Test case 1: Test basic functionality
        MockCreationSettings<String> settings = new MockCreationSettings<>();
        String mock = byteBuddyMockMaker.createMock(settings, new InternalMockHandler());
        assertNotNull(mock);
    }

    @Test(expected = MockitoException.class)
    public void testCreateMock_serializationAcrossClassloaders() {
        // Test case 2: Test exception handling for not supported SerializableMode
        MockCreationSettings<Boolean> settings = new MockCreationSettings<>();
        settings.setSerializableMode(SerializableMode.ACROSS_CLASSLOADERS);
        byteBuddyMockMaker.createMock(settings, new InternalMockHandler());
    }

    @Test(expected = IllegalStateException.class)
    public void testInitializeClassInstantiator_missingObjenesis() {
        // Test case 3: Test exception handling for missing Objenesis on the classpath
        ByteBuddyMockMaker.initializeClassInstantiator();
    }

    @Test
    public void testGetHandler() {
        // Test case 4: Test basic functionality
        InternalMockHandler mockHandler = new InternalMockHandler();
        Object mock = new Object();
        assertNull(byteBuddyMockMaker.getHandler(mock));
        mock = new MockMethodInterceptor.MockAccess(mockHandler);
        assertSame(mockHandler, byteBuddyMockMaker.getHandler(mock));
    }

    @Test
    public void testResetMock() {
        // Test case 5: Test basic functionality
        InternalMockHandler mockHandler = new InternalMockHandler();
        Object mock = new MockMethodInterceptor.MockAccess(mockHandler);
        MockCreationSettings<Boolean> settings = new MockCreationSettings<>();
        byteBuddyMockMaker.resetMock(mock, new InternalMockHandler(), settings);
        assertSame(mockHandler, ((MockMethodInterceptor.MockAccess) mock).getMockitoInterceptor().getMockHandler());
    }
}