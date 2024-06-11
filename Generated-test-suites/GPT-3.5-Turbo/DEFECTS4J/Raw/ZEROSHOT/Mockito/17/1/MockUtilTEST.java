import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.MockHandler;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.util.reflection.LenientCopyTool;

import static org.junit.Assert.*;
import static org.mockito.Mockito.RETURNS_DEFAULTS;
import static org.mockito.Mockito.withSettings;

@SuppressWarnings("unchecked")
public class MockUtilTest {

    MockUtil mockUtil;

    @Before
    public void setUp() {
        mockUtil = new MockUtil();
    }

    @Test
    public void testCreateMock() {
        Class<TestObj> classToMock = TestObj.class;
        MockSettingsImpl settings = new MockSettingsImpl();
        TestObj mock = mockUtil.createMock(classToMock, settings);

        assertNotNull(mock);
        assertTrue(mock instanceof TestObj);
    }

    @Test(expected = NotAMockException.class)
    public void testCreateMockNullClass() {
        Class<TestObj> classToMock = null;
        MockSettingsImpl settings = new MockSettingsImpl();
        mockUtil.createMock(classToMock, settings);
    }

    @Test
    public void testResetMock() {
        TestObj mock = Mockito.mock(TestObj.class);

        mockUtil.resetMock(mock);

        assertTrue(true); // No exception thrown
    }

    @Test
    public void testResetMockNullMock() {
        TestObj mock = null;

        try {
            mockUtil.resetMock(mock);
        } catch (Exception e) {
            fail("Exception thrown for null mock");
        }
    }

    @Test
    public void testGetMockHandler() {
        TestObj mock = Mockito.mock(TestObj.class);

        MockHandlerInterface<TestObj> mockHandler = mockUtil.getMockHandler(mock);

        assertNotNull(mockHandler);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerNullMock() {
        TestObj mock = null;

        mockUtil.getMockHandler(mock);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerInvalidMock() {
        TestObj mock = new TestObj();

        mockUtil.getMockHandler(mock);
    }

    @Test
    public void testIsMock() {
        TestObj mock = Mockito.mock(TestObj.class);

        boolean result = mockUtil.isMock(mock);

        assertTrue(result);
    }

    @Test
    public void testIsMockNullMock() {
        TestObj mock = null;

        boolean result = mockUtil.isMock(mock);

        assertFalse(result);
    }

    @Test
    public void testIsMockInvalidMock() {
        TestObj mock = new TestObj();

        boolean result = mockUtil.isMock(mock);

        assertFalse(result);
    }

    @Test
    public void testGetMockName() {
        TestObj mock = Mockito.mock(TestObj.class);

        MockName mockName = mockUtil.getMockName(mock);

        assertNotNull(mockName);
    }

    private static class TestObj {}

}