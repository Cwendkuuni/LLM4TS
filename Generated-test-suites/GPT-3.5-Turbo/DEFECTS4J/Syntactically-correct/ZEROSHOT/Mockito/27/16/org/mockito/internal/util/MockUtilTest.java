import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.MockCreationValidator;
import org.mockito.internal.util.MockUtil;

import static org.mockito.Mockito.*;

public class MockUtilTest {

    private MockUtil mockUtil;

    @Before
    public void setUp() {
        mockUtil = new MockUtil();
    }

    @Test
    public void testCreateMock() {
        MockCreationValidator creationValidator = mock(MockCreationValidator.class);
        MockUtil mockUtil = new MockUtil(creationValidator);
        Class<TestObject> classToMock = TestObject.class;
        MockSettingsImpl settings = mock(MockSettingsImpl.class);

        TestObject mock = mockUtil.createMock(classToMock, settings);

        assertNotNull(mock);
        verify(creationValidator).validateType(classToMock);
        verify(creationValidator).validateExtraInterfaces(classToMock, settings.getExtraInterfaces());
        verify(creationValidator).validateMockedType(classToMock, settings.getSpiedInstance());
        verify(settings).initiateMockName(classToMock);
    }

    @Test
    public void testResetMock() {
        TestObject mock = mock(TestObject.class);

        mockUtil.resetMock(mock);

        verify(mock).setCallback(0, any(MethodInterceptorFilter.class));
    }

    @Test
    public void testGetMockHandler() {
        TestObject mock = mock(TestObject.class);

        mockUtil.getMockHandler(mock);

        verify(mock, atLeastOnce()).getClass();
    }

    @Test
    public void testIsMock() {
        TestObject mock = mock(TestObject.class);

        boolean result = mockUtil.isMock(mock);

        assertTrue(result);
    }

    @Test
    public void testIsMockWithNull() {
        boolean result = mockUtil.isMock(null);

        assertFalse(result);
    }

    @Test
    public void testGetMockName() {
        TestObject mock = mock(TestObject.class);

        mockUtil.getMockName(mock);

        verify(mock).getMockSettings();
    }
}

class TestObject {
  // Test class for mocking purposes
}