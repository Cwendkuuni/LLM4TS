import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.MockSettingsImpl;
import org.mockito.internal.util.reflection.lenient.LenientMockito;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.expectThrows;

public class MockUtilTest {

    @Test
    public void testCreateMock() {
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.setExtraInterfaces(new Class[]{String.class});
        settings.setSpiedInstance("mock instance");
        MockUtil mockUtil = new MockUtil();

        String mock = mockUtil.createMock(String.class, settings);

        verify(mock).toString(); // Verify that the method "toString" is called on the mock object
        verifyZeroInteractions(mock).otherCalls(); // Verify that other methods are not called on the mock object
    }

    @Test(expected = NullPointerException.class)
    public void testCreateMockWithNullClassToMock() {
        MockUtil mockUtil = new MockUtil();
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.setSpiedInstance("mock instance");
        mockUtil.createMock(null, settings);
    }

    @Test
    public void testCreateMockWithNullSettings() {
        MockUtil mockUtil = new MockUtil();
        Class<String> classToMock = String.class;
        String spiedInstance = "mock instance";
        MockSettingsImpl settings = null;

        try {
            mockUtil.createMock(classToMock, settings);
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(expected = NotAMockException.class)
    public void testCreateMockWithUnsupportedClass() {
        MockUtil mockUtil = new MockUtil();
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.setSpiedInstance(new Object());

        mockUtil.createMock(Object.class, settings);
    }

    @Test
    public void testResetMock() {
        String mock = mock(String.class);
        MockUtil mockUtil = new MockUtil();
        MockHandler<String> mockHandler = mockUtil.getMockHandler(mock);

        mockUtil.resetMock(mock);
        mockUtil.getMockName(mock);

        verify(mockHandler).reset();
        verify(mockHandler).resetInvocations();
    }

    @Test
    public void testIsMock() {
        Object mock = mock(Object.class);
        MockUtil mockUtil = new MockUtil();

        assertNotNull(mockUtil.isMock(mock));
    }

    @Test
    public void testIsMockitoMock() {
        Object mock = mock(Object.class);
        Factory factory = (Factory) mock;
        Callback callback = factory.getCallback(0);
        MethodInterceptorFilter interceptorFilter = (MethodInterceptorFilter) callback;

        MockHandler<Object> mockHandler = interceptorFilter.getHandler();
        MockUtil mockUtil = new MockUtil();

        assertNotNull(mockUtil.isMockitoMock(mock));
        assertTrue(mockUtil.isMock(mock));
    }

    @Test(expected = NotAMockException.class)
    public void testIsMockitoMockWithNullMock() {
        MockUtil mockUtil = new MockUtil();
        mockUtil.isMockitoMock(null);
    }

    @Test
    public void testGetInterceptor() {
        String mock = mock(String.class);
        Factory factory = (Factory) mock;
        Callback callback = factory.getCallback(0);
        MethodInterceptorFilter interceptorFilter = (MethodInterceptorFilter) callback;

        MockUtil mockUtil = new MockUtil();
        MethodInterceptorFilter actualInterceptorFilter = mockUtil.getInterceptor(mock);

        assertEquals(interceptorFilter, actualInterceptorFilter);
    }

    @Test(expected = NotAMockException.class)
    public void testGetInterceptorWithNullMock() {
        MockUtil mockUtil = new MockUtil();
        mockUtil.getInterceptor(null);
    }

    @Test
    public void testGetMockName() {
        Object mock = mock(Object.class);
        MockUtil mockUtil = new MockUtil();
        MockHandler<Object> mockHandler = mockUtil.getMockHandler(mock);

        String mockName = mockUtil.getMockName(mock);

        assertEquals(mockHandler.getMockSettings().getMockName(), mockName);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockNameWithNullMock() {
        MockUtil mockUtil = new MockUtil();
        mockUtil.getMockName(null);
    }
}