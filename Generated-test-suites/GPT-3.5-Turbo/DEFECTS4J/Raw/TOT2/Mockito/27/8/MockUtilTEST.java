import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.exceptions.misusing.NotAMockException;

import static org.junit.Assert.*;

public class MockUtilTest {

    private MockUtil mockUtil;

    @Before
    public void setUp() {
        mockUtil = new MockUtil();
    }

    @Test
    public void testCreateMock() {
        ClassToMock mock = mockUtil.createMock(ClassToMock.class, new MockSettingsImpl());
        assertNotNull(mock);
    }

    @Test
    public void testCreateMockWithSpiedInstance() {
        ClassToMock spiedInstance = new ClassToMock();
        ClassToMock mock = mockUtil.createMock(ClassToMock.class, new MockSettingsImpl().spiedInstance(spiedInstance));
        assertNotEquals(spiedInstance, mock);
    }

    @Test
    public void testResetMock() {
        ClassToMock mock = mockUtil.createMock(ClassToMock.class, new MockSettingsImpl());
        mockUtil.resetMock(mock);
        // No exception should be thrown, resetMock should be successful
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerWithNullMock() {
        mockUtil.getMockHandler(null);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerWithNonMockitoMock() {
        Object notMock = new Object();
        mockUtil.getMockHandler(notMock);
    }

    @Test
    public void testIsMockWithMock() {
        ClassToMock mock = mockUtil.createMock(ClassToMock.class, new MockSettingsImpl());
        assertTrue(mockUtil.isMock(mock));
    }

    @Test
    public void testIsMockWithNonMock() {
        Object notMock = new Object();
        assertFalse(mockUtil.isMock(notMock));
    }

    @Test
    public void testGetMockName() {
        ClassToMock mock = mockUtil.createMock(ClassToMock.class, new MockSettingsImpl().name("mockName"));
        MockName mockName = mockUtil.getMockName(mock);
        assertEquals("mockName", mockName.toString());
    }

    class ClassToMock {
        // Dummy class for testing purposes
    }

}