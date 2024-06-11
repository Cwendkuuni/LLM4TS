import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockCreationSettings;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.InternalMockHandler;
import org.mockito.internal.configuration.GlobalConfiguration;
import org.mockito.internal.creation.instance.ClassInstantiator;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.SerializableMode;
import org.mockito.plugins.MockMaker;
import java.lang.reflect.Constructor;
import java.lang.Class;
import java.lang.Object;

public class ByteBuddyMockMakerTest {

    private ByteBuddyMockMaker byteBuddyMockMaker;
    
    @Before
    public void setup() {
        byteBuddyMockMaker = new ByteBuddyMockMaker();
    }
    
    @Test(expected = MockitoException.class)
    public void testCreateMock_SerializationAcrossClassloaders() {
        MockCreationSettings<Object> settings = createMockCreationSettings(Object.class);
        MockHandler mockHandler = createMockHandler();
        settings.serializableMode(SerializableMode.ACROSS_CLASSLOADERS);
        
        byteBuddyMockMaker.createMock(settings, mockHandler);
    }
    
    @Test
    public void testCreateMock_ValidSettings() {
        MockCreationSettings<String> settings = createMockCreationSettings(String.class);
        MockHandler mockHandler = createMockHandler();
        
        String mock = byteBuddyMockMaker.createMock(settings, mockHandler);
        
        assertNotNull(mock);
    }
    
    @Test(expected = MockitoException.class)
    public void testCreateMock_ClassCastException() {
        MockCreationSettings<Integer> settings = createMockCreationSettings(Integer.class);
        MockHandler mockHandler = createMockHandler();
        
        byteBuddyMockMaker.createMock(settings, mockHandler);
    }
    
    @Test(expected = org.mockito.internal.creation.instance.InstantiationException.class)
    public void testCreateMock_InstantiationException() {
        MockCreationSettings<Object> settings = createMockCreationSettings(Object.class);
        MockHandler mockHandler = createMockHandler();
        
        byteBuddyMockMaker.createMock(settings, mockHandler);
    }
    
    @Test
    public void testEnsureMockIsAssignableToMockedType() {
        MockCreationSettings<String> settings = createMockCreationSettings(String.class);
        String mock = "mock";
        
        String result = byteBuddyMockMaker.ensureMockIsAssignableToMockedType(settings, mock);
        
        assertEquals(mock, result);
    }
    
    @Test
    public void testDescribeClass_TypeNull() {
        Class type = null;
        
        String result = byteBuddyMockMaker.describeClass(type);
        
        assertEquals("null", result);
    }
    
    @Test
    public void testDescribeClass_TypeNotNull() {
        Class type = String.class;
        
        String result = byteBuddyMockMaker.describeClass(type);
        
        assertEquals("'java.lang.String', loaded by classloader : 'java.lang.bootstrap.ClassLoader'", result);
    }
    
    @Test
    public void testDescribeClass_InstanceNull() {
        Object instance = null;
        
        String result = byteBuddyMockMaker.describeClass(instance);
        
        assertEquals("null", result);
    }
    
    @Test
    public void testDescribeClass_InstanceNotNull() {
        Object instance = "instance";
        
        String result = byteBuddyMockMaker.describeClass(instance);
        
        assertEquals("'java.lang.String', loaded by classloader : 'java.lang.bootstrap.ClassLoader'", result);
    }
    
    @Test
    public void testGetHandler() {
        Object mock = createMock(MockMethodInterceptor.MockAccess.class);
        
        MockHandler result = byteBuddyMockMaker.getHandler(mock);
        
        assertTrue(result instanceof MockMethodInterceptor.MockAccess);
    }
    
    @Test
    public void testResetMock() {
        Object mock = createMock(MockMethodInterceptor.MockAccess.class);
        MockHandler newHandler = createMockHandler();
        MockCreationSettings settings = createMockCreationSettings(Object.class);
        
        byteBuddyMockMaker.resetMock(mock, newHandler, settings);
        
        MockMethodInterceptor.MockAccess mockAccess = (MockMethodInterceptor.MockAccess) mock;
        MockMethodInterceptor mockitoInterceptor = mockAccess.getMockitoInterceptor();
        assertEquals(newHandler, mockitoInterceptor.getMockHandler());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testInitializeClassInstantiator_ObjenesisMissing() throws Exception {
        byteBuddyMockMaker.initializeClassInstantiator();
    }
    
    @Test(expected = MockitoException.class)
    public void testAsInternalMockHandler_NotInternalMockHandler() {
        MockHandler handler = createMock(MockHandler.class);
        
        byteBuddyMockMaker.asInternalMockHandler(handler);
    }
    
    private <T> MockCreationSettings<T> createMockCreationSettings(Class<T> type) {
        MockCreationSettings<T> settings = createMock(MockCreationSettings.class);
        settings.typeToMock(type);
        return settings;
    }
    
    private MockHandler createMockHandler() {
        return createMock(InternalMockHandler.class);
    }
    
    private <T> T createMock(Class<T> clazz) {
        return org.mockito.Mockito.mock(clazz);
    }
}