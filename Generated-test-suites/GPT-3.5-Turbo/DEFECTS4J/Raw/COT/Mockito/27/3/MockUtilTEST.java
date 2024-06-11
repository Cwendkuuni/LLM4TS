
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.util.MockCreationValidator;
import org.mockito.internal.util.MockName;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.creation.MockSettingsImpl;

public class MockUtilTest {
    
    private MockUtil mockUtil;
    private MockCreationValidator creationValidator;

    @Before
    public void setUp() {
        creationValidator = mock(MockCreationValidator.class);
        mockUtil = new MockUtil(creationValidator);
    }

    @Test
    public void testCreateMock() {
        Class<?> classToMock = SomeTestClass.class;
        MockSettingsImpl settings = new MockSettingsImpl();
        mockUtil.createMock(classToMock, settings);

        verify(creationValidator).validateType(classToMock);
        verify(creationValidator).validateExtraInterfaces(classToMock, settings.getExtraInterfaces());
        verify(creationValidator).validateMockedType(classToMock, settings.getSpiedInstance());
    }

    @Test
    public void testResetMock() {
        SomeTestClass mock = mock(SomeTestClass.class);

        mockUtil.resetMock(mock);

        verify(mock).setCallback(eq(0), any(MethodInterceptorFilter.class));
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerWithNullMock() {
        Object mock = null;

        mockUtil.getMockHandler(mock);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerWithNonMockitoMock() {
        SomeTestClass mock = new SomeTestClass();

        mockUtil.getMockHandler(mock);
    }

    @Test
    public void testIsMock() {
        SomeTestClass mock = mock(SomeTestClass.class);

        boolean isMock = mockUtil.isMock(mock);

        assertTrue(isMock);
    }

    @Test
    public void testIsMockWithNonMockitoMock() {
        SomeTestClass mock = new SomeTestClass();

        boolean isMock = mockUtil.isMock(mock);

        assertFalse(isMock);
    }

    @Test
    public void testGetMockName() {
        SomeTestClass mock = mock(SomeTestClass.class);
        MockName mockName = mock(MockName.class);
        when(mockUtil.getMockHandler(mock).getMockSettings().getMockName()).thenReturn(mockName);

        MockName result = mockUtil.getMockName(mock);

        assertEquals(mockName, result);
    }
}

class SomeTestClass {}
