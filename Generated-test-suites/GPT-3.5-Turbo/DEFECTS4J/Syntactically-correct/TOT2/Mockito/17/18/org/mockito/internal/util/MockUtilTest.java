import org.junit.Test;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.MockHandler;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.CreationValidator;
import org.mockito.internal.creation.MethodInterceptorFilter;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.util.MockName;
import org.mockito.internal.util.MockUtil;
import org.mockito.Mockito;

import static org.junit.Assert.*;
import static org.mockito.Mockito.withSettings;

public class MockUtilTest {

    @Test
    public void testCreateMock() {
        MockUtil mockUtil = new MockUtil();
        MockSettingsImpl settings = new MockSettingsImpl();

        Object mock = mockUtil.createMock(String.class, settings);

        assertNotNull(mock);
    }

    @Test
    public void testResetMock() {
        MockUtil mockUtil = new MockUtil();
        String mock = Mockito.mock(String.class);

        mockUtil.resetMock(mock);

        assertNotNull(mock);
    }

    @Test
    public void testGetMockHandler() {
        MockUtil mockUtil = new MockUtil();
        String mock = Mockito.mock(String.class);

        MockHandlerInterface<String> mockHandler = mockUtil.getMockHandler(mock);

        assertNotNull(mockHandler);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerWithNullArgument() {
        MockUtil mockUtil = new MockUtil();

        mockUtil.getMockHandler(null);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerWithNonMockObject() {
        MockUtil mockUtil = new MockUtil();

        mockUtil.getMockHandler("not a mock");
    }

    @Test
    public void testIsMock() {
        MockUtil mockUtil = new MockUtil();
        String mock = Mockito.mock(String.class);

        boolean result = mockUtil.isMock(mock);

        assertTrue(result);
    }

    @Test(expected = NotAMockException.class)
    public void testIsMockWithNonMockObject() {
        MockUtil mockUtil = new MockUtil();

        boolean result = mockUtil.isMock("not a mock");

        assertFalse(result);
    }

    @Test
    public void testGetMockName() {
        MockUtil mockUtil = new MockUtil();
        String mock = Mockito.mock(String.class);

        MockName mockName = mockUtil.getMockName(mock);

        assertNotNull(mockName);
    }
}