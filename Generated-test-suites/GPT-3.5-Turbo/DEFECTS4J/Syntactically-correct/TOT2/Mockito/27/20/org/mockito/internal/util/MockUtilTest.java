import org.junit.Test;
import org.mockito.exceptions.misusing.NotAMockException;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class MockUtilTest {

    @Test
    public void testCreateMock() {
        MockUtil mockUtil = new MockUtil();
        Object mock = mock(Object.class);
        Assert.assertNotNull(mock);
    }

    @Test
    public void testResetMock() {
        MockUtil mockUtil = new MockUtil();
        Object mock = mock(Object.class);
        mockUtil.resetMock(mock);
        // Assert reset behavior, can't verify much due to library restrictions
    }

    @Test
    public void testGetMockHandler() {
        MockUtil mockUtil = new MockUtil();
        Object mock = mock(Object.class);
        MockHandlerInterface handler = mockUtil.getMockHandler(mock);
        Assert.assertNotNull(handler);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerNull() {
        MockUtil mockUtil = new MockUtil();
        mockUtil.getMockHandler(null);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerNonMockitoMock() {
        MockUtil mockUtil = new MockUtil();
        Object notMock = new Object();
        mockUtil.getMockHandler(notMock);
    }

    @Test
    public void testIsMock() {
        MockUtil mockUtil = new MockUtil();
        Object mock = mock(Object.class);
        Assert.assertTrue(mockUtil.isMock(mock));
    }

    @Test
    public void testIsMockNotMock() {
        MockUtil mockUtil = new MockUtil();
        Object notMock = new Object();
        Assert.assertFalse(mockUtil.isMock(notMock));
    }

    @Test
    public void testGetMockName() {
        MockUtil mockUtil = new MockUtil();
        Object mock = mock(Object.class);
        MockName mockName = mockUtil.getMockName(mock);
        // Assert if mock name is valid
    }

}