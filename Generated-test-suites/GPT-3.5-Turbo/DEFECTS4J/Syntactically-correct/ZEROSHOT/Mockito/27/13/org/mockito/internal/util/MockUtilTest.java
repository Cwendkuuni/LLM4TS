import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.creation.MockSettingsImpl;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MockUtilTest {

    private MockUtil mockUtil;

    @Before
    public void setUp() {
        mockUtil = new MockUtil();
    }

    @Test
    public void testCreateMock() {
        Class<TestObject> classToMock = TestObject.class;
        MockSettingsImpl settings = new MockSettingsImpl();

        TestObject mock = mockUtil.createMock(classToMock, settings);

        assertNotNull(mock);
    }

    @Test(expected = NotAMockException.class)
    public void testResetMock_notAMock() {
        TestObject mock = new TestObject();

        mockUtil.resetMock(mock);
    }

    @Test
    public void testResetMock() {
        TestObject mock = mock(TestObject.class);

        mockUtil.resetMock(mock);

        verify(mock, times(1)).reset();
    }

    @Test
    public void testGetMethodInterceptorFilter() {
        TestObject mock = mock(TestObject.class);

        MethodInterceptorFilter interceptor = mockUtil.getInterceptor(mock);

        assertNotNull(interceptor);
    }

    @Test
    public void testGetMethodInterceptorFilter_notAMock() {
        TestObject mock = new TestObject();

        MethodInterceptorFilter interceptor = mockUtil.getInterceptor(mock);

        assertNull(interceptor);
    }

    @Test
    public void testGetMockHandler() {
        TestObject mock = mock(TestObject.class);

        MockHandlerInterface<TestObject> handler = mockUtil.getMockHandler(mock);

        assertNotNull(handler);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandler_notAMock() {
        TestObject mock = new TestObject();

        mockUtil.getMockHandler(mock);
    }

    @Test
    public void testIsMock_true() {
        TestObject mock = mock(TestObject.class);

        boolean result = mockUtil.isMock(mock);

        assertTrue(result);
    }

    @Test
    public void testIsMock_false() {
        TestObject mock = new TestObject();

        boolean result = mockUtil.isMock(mock);

        assertFalse(result);
    }

    @Test
    public void testGetMockName() {
        TestObject mock = mock(TestObject.class);

        MockName mockName = mockUtil.getMockName(mock);

        assertNotNull(mockName);
    }

    class TestObject {
        public void reset() {}
    }
}