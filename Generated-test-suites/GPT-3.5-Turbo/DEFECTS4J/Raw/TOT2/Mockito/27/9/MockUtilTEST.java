import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.MockCreationValidator;

public class MockUtilTest {

    @Test
    public void testCreateMock() {
        MockUtil mockUtil = new MockUtil(new MockCreationValidator());
        ClassToMock mock = mockUtil.createMock(ClassToMock.class, new MockSettingsImpl());
        assertNotNull(mock);
    }

    @Test
    public void testResetMock() {
        MockUtil mockUtil = new MockUtil(new MockCreationValidator());
        ClassToMock mock = mockUtil.createMock(ClassToMock.class, new MockSettingsImpl());
        mockUtil.resetMock(mock);
        // Verify the mock reset functionality
    }

    @Test
    public void testGetMethodInterceptorFilter() {
        MockUtil mockUtil = new MockUtil(new MockCreationValidator());
        MethodInterceptorFilter filter = mockUtil.newMethodInterceptorFilter(new MockSettingsImpl());
        assertNotNull(filter);
    }

    @Test
    public void testGetMockHandler() {
        MockUtil mockUtil = new MockUtil(new MockCreationValidator());
        ClassToMock mock = new ClassToMock();
        MockHandlerInterface<ClassToMock> handler = mockUtil.getMockHandler(mock);
        assertNotNull(handler);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerWithNull() {
        MockUtil mockUtil = new MockUtil(new MockCreationValidator());
        mockUtil.getMockHandler(null);
    }

    @Test
    public void testIsMock() {
        MockUtil mockUtil = new MockUtil(new MockCreationValidator());
        ClassToMock mock = mockUtil.createMock(ClassToMock.class, new MockSettingsImpl());
        assertTrue(mockUtil.isMock(mock));
    }

    @Test
    public void testIsMockWithNonMockitoMock() {
        MockUtil mockUtil = new MockUtil(new MockCreationValidator());
        ClassToMock mock = new ClassToMock();
        assertFalse(mockUtil.isMock(mock));
    }

    @Test
    public void testGetMockName() {
        MockUtil mockUtil = new MockUtil(new MockCreationValidator());
        ClassToMock mock = mockUtil.createMock(ClassToMock.class, new MockSettingsImpl());
        MockName mockName = mockUtil.getMockName(mock);
        assertNotNull(mockName);
    }

    @Test
    public void testGetMockNameWithNull() {
        MockUtil mockUtil = new MockUtil(new MockCreationValidator());
        ClassToMock mock = null;
        MockName mockName = mockUtil.getMockName(mock);
        assertNull(mockName);
    }

}