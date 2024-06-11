
import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.util.reflection.LenientCopyTool;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.MockHandler;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.cglib.proxy.*;
import org.mockito.Mockito;

public class MockUtilTest {

    @Test
    public void testCreateMock() {
        MockUtil mockUtil = new MockUtil();
        Class<String> classToMock = String.class;
        MockSettingsImpl settings = new MockSettingsImpl();
        String mock = mockUtil.createMock(classToMock, settings);
        assertNotNull(mock);
        assertTrue(mock instanceof String);
    }

    @Test(expected = NotAMockException.class)
    public void testResetMock() {
        MockUtil mockUtil = new MockUtil();
        String mock = "mockString";
        mockUtil.resetMock(mock);
    }

    @Test
    public void testGetMockHandler() {
        MockUtil mockUtil = new MockUtil();
        String mock = Mockito.mock(String.class);
        MockHandlerInterface<String> mockHandler = mockUtil.getMockHandler(mock);
        assertNotNull(mockHandler);
    }

    @Test
    public void testIsMock() {
        MockUtil mockUtil = new MockUtil();
        String mock = Mockito.mock(String.class);
        assertTrue(mockUtil.isMock(mock));
    }

    @Test
    public void testGetMockName() {
        MockUtil mockUtil = new MockUtil();
        String mock = Mockito.mock(String.class);
        MockName mockName = mockUtil.getMockName(mock);
        assertNotNull(mockName);
    }
}
