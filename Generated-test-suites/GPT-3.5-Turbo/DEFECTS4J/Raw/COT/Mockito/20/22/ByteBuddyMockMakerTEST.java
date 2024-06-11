
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.SerializableMode;
import org.mockito.plugins.MockMaker;

public class ByteBuddyMockMakerTest {

    @Test(expected=MockitoException.class)
    public void testCreateMock_SerializationAcrossClassloaders() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockCreationSettings settings = new MockCreationSettings();
        settings.setSerializableMode(SerializableMode.ACROSS_CLASSLOADERS);
        MockHandler handler = null;
        
        mockMaker.createMock(settings, handler);
    }

    @Test
    public void testCreateMock_Success() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockCreationSettings settings = new MockCreationSettings();
        settings.setSerializableMode(SerializableMode.NONE);
        MockHandler handler = new MockHandler() {
            public Object handle(MockHandler.MockInvocation invocation) throws Throwable {
                return null;
            }
        };
        
        mockMaker.createMock(settings, handler);
    }

    @Test(expected=MockitoException.class)
    public void testCreateMock_ClassCastException() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockCreationSettings settings = new MockCreationSettings();
        settings.setTypeToMock(String.class);
        MockHandler handler = null;
        
        mockMaker.createMock(settings, handler);
    }

    @Test(expected=MockitoException.class)
    public void testCreateMock_InstantiationException() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockCreationSettings settings = new MockCreationSettings();
        MockHandler handler = null;
        
        mockMaker.createMock(settings, handler);
    }

    @Test
    public void testEnsureMockIsAssignableToMockedType() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockCreationSettings settings = new MockCreationSettings();
        settings.setTypeToMock(String.class);
        String mock = "mock";
        
        mockMaker.ensureMockIsAssignableToMockedType(settings, mock);
    }

    @Test
    public void testEnsureMockIsAssignableToMockedType_NullMock() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockCreationSettings settings = new MockCreationSettings();
        settings.setTypeToMock(String.class);
        String mock = null;
        
        mockMaker.ensureMockIsAssignableToMockedType(settings, mock);
    }

    @Test
    public void testDescribeClass() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        Class type = String.class;
        
        mockMaker.describeClass(type);
    }

    @Test
    public void testDescribeClass_NullType() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        Class type = null;
        
        mockMaker.describeClass(type);
    }

    @Test
    public void testDescribeClass_Instance() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        Object instance = new Object();
        
        mockMaker.describeClass(instance);
    }

    @Test
    public void testDescribeClass_NullInstance() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        Object instance = null;
        
        mockMaker.describeClass(instance);
    }

    @Test
    public void testGetHandler() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        Object mock = new Object();
        
        mockMaker.getHandler(mock);
    }

    @Test
    public void testGetHandler_NonMockObject() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        Object mock = new Object();
        
        mockMaker.getHandler(mock);
    }

    @Test
    public void testResetMock() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        Object mock = new Object();
        MockHandler newHandler = null;
        MockCreationSettings settings = null;
        
        mockMaker.resetMock(mock, newHandler, settings);
    }

    @Test(expected=MockitoException.class)
    public void testResetMock_NonInternalMockHandler() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        Object mock = new Object();
        MockHandler newHandler = null;
        MockCreationSettings settings = null;
        
        mockMaker.resetMock(mock, newHandler, settings);
    }

    @Test
    public void testInitializeClassInstantiator() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        
        mockMaker.initializeClassInstantiator();
    }

    @Test
    public void testAsInternalMockHandler() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockHandler handler = null;
        
        mockMaker.asInternalMockHandler(handler);
    }

    @Test(expected=MockitoException.class)
    public void testAsInternalMockHandler_NonInternalMockHandler() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockHandler handler = null;
        
        mockMaker.asInternalMockHandler(handler);
    }

}
