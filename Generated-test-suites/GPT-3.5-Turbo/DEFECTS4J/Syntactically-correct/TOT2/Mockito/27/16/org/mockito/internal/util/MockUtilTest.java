import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MockUtilTest {

    @Test
    public void testCreateMock() {
        // Typical scenario
        MockUtil mockUtil = new MockUtil();
        Object mock = mockUtil.createMock(Object.class, new MockSettingsImpl());
        assertNotNull(mock);
        
        // Edge case
        Object spiedInstance = new Object();
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.setSpiedInstance(spiedInstance);
        Object mockWithSpy = mockUtil.createMock(Object.class, settings);
        assertNotNull(mockWithSpy);
    }

    @Test
    public void testResetMock() {
        // Typical scenario
        MockUtil mockUtil = new MockUtil();
        Object mock = mockUtil.createMock(Object.class, new MockSettingsImpl());
        mockUtil.resetMock(mock);
        
        // Edge case
        Object notAMock = new Object();
        assertThrows(NotAMockException.class, () -> mockUtil.resetMock(notAMock));
    }

    @Test
    public void testGetMockHandler() {
        // Typical scenario
        MockUtil mockUtil = new MockUtil();
        Object mock = mockUtil.createMock(Object.class, new MockSettingsImpl());
        assertNotNull(mockUtil.getMockHandler(mock));
        
        // Edge case
        Object notAMock = new Object();
        assertThrows(NotAMockException.class, () -> mockUtil.getMockHandler(notAMock));
    }

    @Test
    public void testIsMock() {
        // Typical scenario
        MockUtil mockUtil = new MockUtil();
        Object mock = mockUtil.createMock(Object.class, new MockSettingsImpl());
        assertTrue(mockUtil.isMock(mock));
        
        // Edge case
        assertFalse(mockUtil.isMock(null));
    }

    @Test
    public void testGetMockName() {
        // Typical scenario
        MockUtil mockUtil = new MockUtil();
        Object mock = mockUtil.createMock(Object.class, new MockSettingsImpl());
        assertNotNull(mockUtil.getMockName(mock));
        
        // Error scenario
        Object notAMock = new Object();
        assertThrows(NotAMockException.class, () -> mockUtil.getMockName(notAMock));
    }

}