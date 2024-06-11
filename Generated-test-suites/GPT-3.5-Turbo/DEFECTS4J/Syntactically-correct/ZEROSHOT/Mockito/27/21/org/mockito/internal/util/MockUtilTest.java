import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.util.MockCreationValidator;
import org.mockito.internal.util.MockName;
import org.mockito.internal.util.MockUtil;
import java.io.Serializable;

public class MockUtilTest {
    
    @Test
    public void testCreateMock() {
        // Initialize MockUtil and MockCreationValidator
        MockCreationValidator creationValidator = new MockCreationValidator();
        MockUtil mockUtil = new MockUtil(creationValidator);
        
        // MockSettingsImpl for createMock method
        MockSettingsImpl settings = Mockito.mock(MockSettingsImpl.class);
        Class<Object> classToMock = Object.class;
        
        // Test createMock method
        Object mock = mockUtil.createMock(classToMock, settings);
        
        // Verify that mock is not null
        assertNotNull(mock);
    }
    
    @Test
    public void testResetMock() {
        // Initialize MockUtil
        MockUtil mockUtil = new MockUtil();
        
        // Create mock object
        Object mock = Mockito.mock(Object.class);
        
        // Test resetMock method
        mockUtil.resetMock(mock);
        
        // Verify that resetMock method was called successfully
    }
    
    @Test
    public void testGetMockHandler() {
        // Initialize MockUtil
        MockUtil mockUtil = new MockUtil();
        
        // Create mock object
        Object mock = Mockito.mock(Object.class);
        
        // Test getMockHandler method
        MockHandlerInterface<Object> mockHandler = mockUtil.getMockHandler(mock);
        
        // Verify that mockHandler is not null
        assertNotNull(mockHandler);
    }
    
    @Test(expected = NotAMockException.class)
    public void testGetMockHandler_NotAMockException() {
        // Initialize MockUtil
        MockUtil mockUtil = new MockUtil();
        
        // Create non-mock object
        Object nonMock = new Object();
        
        // Test getMockHandler method with non-mock object
        mockUtil.getMockHandler(nonMock);
    }
    
    @Test
    public void testIsMock() {
        // Initialize MockUtil
        MockUtil mockUtil = new MockUtil();
        
        // Create mock object
        Object mock = Mockito.mock(Object.class);
        
        // Test isMock method with mock object
        boolean isMock = mockUtil.isMock(mock);
        
        // Verify that isMock is true
        assertTrue(isMock);
    }
    
    @Test
    public void testIsMock_NullObject() {
        // Initialize MockUtil
        MockUtil mockUtil = new MockUtil();
        
        // Test isMock method with null object
        boolean isMock = mockUtil.isMock(null);
        
        // Verify that isMock is false
        assertFalse(isMock);
    }
    
    @Test
    public void testIsMock_NotAMock() {
        // Initialize MockUtil
        MockUtil mockUtil = new MockUtil();
        
        // Create non-mock object
        Object nonMock = new Object();
        
        // Test isMock method with non-mock object
        boolean isMock = mockUtil.isMock(nonMock);
        
        // Verify that isMock is false
        assertFalse(isMock);
    }
    
    @Test
    public void testGetMockName() {
        // Initialize MockUtil
        MockUtil mockUtil = new MockUtil();
        
        // Create mock object
        Object mock = Mockito.mock(Object.class);
        
        // Test getMockName method
        MockName mockName = mockUtil.getMockName(mock);
        
        // Verify that mockName is not null
        assertNotNull(mockName);
    }
    
}