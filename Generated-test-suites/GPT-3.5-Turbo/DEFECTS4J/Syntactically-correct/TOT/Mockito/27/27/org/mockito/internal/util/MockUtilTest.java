import org.junit.Test;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class MockUtilTest {

    private MockUtil mockUtil;

    @Before
    public void setUp() {
        mockUtil = new MockUtil();
    }

    @Test
    public void testCreateMock_typical() {
        // Typical case: Create a mock of a class with no extra interfaces or spied instance
        ClassToMock mock = mockUtil.createMock(ClassToMock.class, new MockSettingsImpl());
        
        assertNotNull(mock);
        assertTrue(mock instanceof ClassToMock);
        assertFalse(mock instanceof Serializable);
    }
    
    @Test
    public void testCreateMock_extraInterfaces() {
        // Case with extra interfaces: Create a mock of a class with extra interfaces
        ClassToMock mock = mockUtil.createMock(ClassToMock.class, new MockSettingsImpl().extraInterfaces(ExtraInterface.class));
        
        assertNotNull(mock);
        assertTrue(mock instanceof ClassToMock);
        assertTrue(mock instanceof ExtraInterface);
        assertFalse(mock instanceof Serializable);
    }
    
    @Test
    public void testCreateMock_spiedInstance() {
        // Case with spied instance: Create a mock of a class with a spied instance
        SpiedInstance spiedInstance = new SpiedInstance();
        ClassToMock mock = mockUtil.createMock(ClassToMock.class, new MockSettingsImpl().spiedInstance(spiedInstance));
        
        assertNotNull(mock);
        assertTrue(mock instanceof ClassToMock);
        assertFalse(mock instanceof Serializable);
        
        verify(spiedInstance, atLeastOnce()).someMethod();
    }
    
    @Test
    public void testCreateMock_serializable() {
        // Case with serializable: Create a serializable mock of a class
        ClassToMock mock = mockUtil.createMock(ClassToMock.class, new MockSettingsImpl().serializable());
        
        assertNotNull(mock);
        assertTrue(mock instanceof ClassToMock);
        assertTrue(mock instanceof Serializable);
    }
    
    @Test(expected = NotAMockException.class)
    public void testGetMockHandler_nullMock() {
        // Error scenario: Get mock handler for a null mock
        mockUtil.getMockHandler(null);
    }
    
    @Test(expected = NotAMockException.class)
    public void testGetMockHandler_nonMockitoMock() {
        // Error scenario: Get mock handler for a non-Mockito mock
        NonMockitoMock mock = new NonMockitoMock();
        mockUtil.getMockHandler(mock);
    }
    
    @Test
    public void testGetMockHandler_mockitoMock() {
        // Typical case: Get mock handler for a Mockito mock
        ClassToMock mock = mockUtil.createMock(ClassToMock.class, new MockSettingsImpl());
        MockHandlerInterface handler = mockUtil.getMockHandler(mock);
        
        assertNotNull(handler);
        assertTrue(handler instanceof MockHandlerInterface);
    }
    
    @Test
    public void testIsMock_notMock() {
        // Typical case: Check if an object is a mock (not a mock)
        ClassToMock object = new ClassToMock();
        boolean isMock = mockUtil.isMock(object);
        
        assertFalse(isMock);
    }
    
    @Test
    public void testIsMock_mockitoMock() {
        // Typical case: Check if an object is a mock (Mockito mock)
        ClassToMock mock = mockUtil.createMock(ClassToMock.class, new MockSettingsImpl());
        boolean isMock = mockUtil.isMock(mock);
        
        assertTrue(isMock);
    }
    
    @Test
    public void testResetMock() {
        // Typical case: Reset a mock
        ClassToMock mock = mockUtil.createMock(ClassToMock.class, new MockSettingsImpl());
        mockUtil.resetMock(mock);
        
        verify(mock, times(0)).someMethod();
    }

    @Test
    public void testGetMockName() {
        // Typical case: Get mock name
        ClassToMock mock = mockUtil.createMock(ClassToMock.class, new MockSettingsImpl().name("mockName"));
        MockName name = mockUtil.getMockName(mock);
        
        assertEquals("mockName", name.toString());
    }

}