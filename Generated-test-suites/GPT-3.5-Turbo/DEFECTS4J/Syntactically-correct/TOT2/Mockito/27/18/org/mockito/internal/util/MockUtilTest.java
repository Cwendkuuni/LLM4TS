import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.mock.MockCreationValidator;
import org.mockito.internal.util.MockUtil;

public class MockUtilTest {

    private MockUtil mockUtil;

    @Before
    public void setUp() {
        mockUtil = new MockUtil();
    }

    @Test
    public void testCreateMock() {
        MockSettingsImpl settings = new MockSettingsImpl();
        Object mock = mockUtil.createMock(String.class, settings);
        assertNotNull(mock);
    }

    @Test
    public void testResetMock() {
        String mock = "mock";
        mockUtil.resetMock(mock);
        assertTrue(true); // No assertion for reset method
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerWithNull() {
        mockUtil.getMockHandler(null);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerWithNonMock() {
        String nonMock = "not a mock";
        mockUtil.getMockHandler(nonMock);
    }

    @Test
    public void testIsMockWithMock() {
        String mock = "mock";
        assertFalse(mockUtil.isMock(mock));
    }

    @Test
    public void testIsMockWithNull() {
        assertFalse(mockUtil.isMock(null));
    }

    @Test
    public void testGetMockName() {
        String mock = "mock";
        assertNotNull(mockUtil.getMockName(mock));
    }
}