import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.MockHandler;
import org.mockito.internal.creation.MockSettingsImpl;

import static org.junit.Assert.*;
import static org.mockito.Mockito.RETURNS_DEFAULTS;
import static org.mockito.Mockito.withSettings;

public class MockUtilTest {
    
    private MockUtil mockUtil;
    
    @Before
    public void setUp() {
        mockUtil = new MockUtil();
    }
    
    @Test
    public void testCreateMock() {
        MockSettingsImpl settings = withSettings().defaultAnswer(RETURNS_DEFAULTS);
        Object mock = mockUtil.createMock(String.class, settings);
        assertNotNull(mock);
    }
    
    @Test
    public void testResetMock() {
        MockHandler<String> mockHandler = new MockHandler<>(new MockSettingsImpl());
        String mock = mockHandler.getMock();
        
        mockUtil.resetMock(mock);
        assertNotNull(mock);
    }
    
    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerWithNullMock() {
        mockUtil.getMockHandler(null);
    }
    
    @Test
    public void testIsMockWithMockitoMock() {
        MockHandler<String> mockHandler = new MockHandler<>(new MockSettingsImpl());
        String mock = mockHandler.getMock();
        
        boolean result = mockUtil.isMock(mock);
        assertTrue(result);
    }
    
    @Test
    public void testIsMockWithNonMockitoMock() {
        String nonMock = "test";
        
        boolean result = mockUtil.isMock(nonMock);
        assertFalse(result);
    }
    
    @Test
    public void testGetMockName() {
        MockHandler<String> mockHandler = new MockHandler<>(new MockSettingsImpl());
        String mock = mockHandler.getMock();
        
        assertNotNull(mockUtil.getMockName(mock));
    }
}