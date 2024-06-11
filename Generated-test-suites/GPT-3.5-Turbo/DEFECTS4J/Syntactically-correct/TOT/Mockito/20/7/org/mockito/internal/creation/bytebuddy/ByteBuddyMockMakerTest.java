import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.creation.bytebuddy.ByteBuddyMockMaker;
import org.mockito.internal.creation.instance.MockMethodInterceptor;

public class ByteBuddyMockMakerTest {

    // Test createMock() method
    
    @Test
    public void testCreateMock_validSettings_success() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockCreationSettings<TestClass> settings = Mockito.mock(MockCreationSettings.class);
        settings.setupTypeToMock(TestClass.class);
        settings.setupExtraInterfaces(new Class[] {Serializable.class});
        MockHandler handler = Mockito.mock(MockHandler.class);
        
        TestClass mock = mockMaker.createMock(settings, handler);
        
        assertNotNull(mock);
        assertTrue(mock instanceof TestClass);
        assertTrue(mock instanceof Serializable);
        assertNotNull(((MockMethodInterceptor.MockAccess)mock).getMockitoInterceptor().getMockHandler());
    }
    
    @Test(expected = MockitoException.class)
    public void testCreateMock_invalidSettings_throwException() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockCreationSettings<TestClass> settings = Mockito.mock(MockCreationSettings.class);
        settings.setupTypeToMock(TestClass.class);
        settings.setupSerializableMode(SerializableMode.ACROSS_CLASSLOADERS);
        MockHandler handler = Mockito.mock(MockHandler.class);
        
        mockMaker.createMock(settings, handler);
    }
    
    // Test ensureMockIsAssignableToMockedType() method
    
    @Test
    public void testEnsureMockIsAssignableToMockedType_validSettings_success() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockCreationSettings<TestClass> settings = Mockito.mock(MockCreationSettings.class);
        settings.setupTypeToMock(TestClass.class);
        TestClass mock = Mockito.mock(TestClass.class);
        
        TestClass result = mockMaker.ensureMockIsAssignableToMockedType(settings, mock);
        
        assertNotNull(mock);
        assertTrue(result instanceof TestClass);
    }
    
    @Test(expected = ClassCastException.class)
    public void testEnsureMockIsAssignableToMockedType_invalidSettings_throwException() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockCreationSettings<SubClass> settings = Mockito.mock(MockCreationSettings.class);
        settings.setupTypeToMock(SubClass.class);
        TestClass mock = Mockito.mock(TestClass.class);
        
        TestClass result = mockMaker.ensureMockIsAssignableToMockedType(settings, mock);
    }
    
    // Test getHandler() method
    
    @Test
    public void testGetHandler_validMock_success() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        TestClass mock = Mockito.mock(TestClass.class);
        
        MockHandler handler = mockMaker.getHandler(mock);
        
        assertNotNull(handler);
    }
    
    @Test
    public void testGetHandler_invalidMock_returnNull() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        Object mock = new Object();
        
        MockHandler handler = mockMaker.getHandler(mock);
        
        assertNull(handler);
    }
    
    // Test resetMock() method
    
    @Test
    public void testResetMock_validMock_success() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        TestClass mock = Mockito.mock(TestClass.class);
        MockHandler newHandler = Mockito.mock(MockHandler.class);
        MockCreationSettings<TestClass> settings = Mockito.mock(MockCreationSettings.class);
        settings.setupTypeToMock(TestClass.class);

        mockMaker.resetMock(mock, newHandler, settings);

        assertNotNull(((MockMethodInterceptor.MockAccess)mock).getMockitoInterceptor().getMockHandler());
        assertSame(newHandler, ((MockMethodInterceptor.MockAccess)mock).getMockitoInterceptor().getMockHandler());
    }
    
    // Additional Test Cases
    
    @Test
    public void testConstructor_initializedFields_success() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        
        assertNotNull(mockMaker.classInstantiator);
        assertNotNull(mockMaker.cachingMockBytecodeGenerator);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testInitializeClassInstantiator_missingObjenesis_throwException() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        
        mockMaker.initializeClassInstantiator();
    }
    
    @Test(expected = MockitoException.class)
    public void testAsInternalMockHandler_notInternalMockHandler_throwException() {
        ByteBuddyMockMaker mockMaker = new ByteBuddyMockMaker();
        MockHandler handler = Mockito.mock(MockHandler.class);
        
        mockMaker.asInternalMockHandler(handler);
    }

}