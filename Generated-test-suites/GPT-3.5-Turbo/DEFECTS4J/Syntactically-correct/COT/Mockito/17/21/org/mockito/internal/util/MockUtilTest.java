import org.junit.Test;
import org.junit.Assert;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.LenientCopyTool;

public class MockUtilTest {

    @Test
    public void testCreateMock() {
        MockUtil mockUtil = new MockUtil();
        ClassToMock mock = mockUtil.createMock(ClassToMock.class, new MockSettingsImpl());
        Assert.assertNotNull(mock);
    }

    @Test(expected = NotAMockException.class)
    public void testResetMock() {
        MockUtil mockUtil = new MockUtil();
        ClassToMock mock = new ClassToMock();
        mockUtil.resetMock(mock);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandler() {
        MockUtil mockUtil = new MockUtil();
        ClassToMock mock = new ClassToMock();
        mockUtil.getMockHandler(mock);
    }

    @Test
    public void testIsMock() {
        MockUtil mockUtil = new MockUtil();
        ClassToMock mock = mockUtil.createMock(ClassToMock.class, new MockSettingsImpl());
        boolean isMock = mockUtil.isMock(mock);
        Assert.assertTrue(isMock);
    }

    @Test
    public void testGetMockName() {
        MockUtil mockUtil = new MockUtil();
        ClassToMock mock = mockUtil.createMock(ClassToMock.class, new MockSettingsImpl());
        MockName name = mockUtil.getMockName(mock);
        Assert.assertNotNull(name);
    }

    private class ClassToMock {

    }

}