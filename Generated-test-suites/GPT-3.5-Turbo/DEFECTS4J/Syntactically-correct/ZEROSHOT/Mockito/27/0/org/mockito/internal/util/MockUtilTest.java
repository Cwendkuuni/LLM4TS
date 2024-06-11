import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.InternalMockHandler;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.util.reflection.LenientCopyTool;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class MockUtilTest {
    
    private MockUtil mockUtil;
    
    @Before
    public void setup() {
        mockUtil = new MockUtil();
    }
    
    @Test
    public void testCreateMock() {
        Class<String> classToMock = String.class;
        MockSettingsImpl settings = new MockSettingsImpl();
        String mock = mockUtil.createMock(classToMock, settings);
        
        assertNotNull(mock);
        assertTrue(mock instanceof String);
    }
    
    @Test(expected = NotAMockException.class)
    public void testResetMock_NotAMockException() {
        String mock = "not a mock";
        mockUtil.resetMock(mock);
    }
    
    @Test
    public void testResetMock() {
        Class<String> classToMock = String.class;
        MockSettingsImpl settings = new MockSettingsImpl();
        String mock = mockUtil.createMock(classToMock, settings);
        
        assertNotNull(mock);
        assertTrue(mock instanceof String);
        
        mockUtil.resetMock(mock);
        
        assertTrue(mock instanceof String);
    }
    
    @Test
    public void testGetMethodInterceptorFilter() {
        Class<String> classToMock = String.class;
        MockSettingsImpl settings = new MockSettingsImpl();
        String mock = mockUtil.createMock(classToMock, settings);
        
        assertNotNull(mock);
        assertTrue(mock instanceof String);
        
        MethodInterceptorFilter filter = mockUtil.getMethodInterceptorFilter(mock);
        
        assertNotNull(filter);
        assertTrue(filter instanceof MethodInterceptorFilter);
    }
    
    @Test
    public void testGetMockHandler() {
        Class<String> classToMock = String.class;
        MockSettingsImpl settings = new MockSettingsImpl();
        String mock = mockUtil.createMock(classToMock, settings);
        
        assertNotNull(mock);
        assertTrue(mock instanceof String);
        
        MockHandlerInterface<String> handler = mockUtil.getMockHandler(mock);
        
        assertNotNull(handler);
        assertTrue(handler instanceof InternalMockHandler);
    }
    
    @Test
    public void testIsMock() {
        Class<String> classToMock = String.class;
        MockSettingsImpl settings = new MockSettingsImpl();
        String mock = mockUtil.createMock(classToMock, settings);
        
        assertNotNull(mock);
        assertTrue(mock instanceof String);
        
        assertTrue(mockUtil.isMock(mock));
        assertFalse(mockUtil.isMock("not a mock"));
    }
    
    @Test
    public void testGetMockName() {
        Class<String> classToMock = String.class;
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.setMockName("myMock");
        String mock = mockUtil.createMock(classToMock, settings);
        
        assertNotNull(mock);
        assertTrue(mock instanceof String);
        
        MockName mockName = mockUtil.getMockName(mock);
        
        assertNotNull(mockName);
        assertEquals("myMock", mockName.toString());
    }
}