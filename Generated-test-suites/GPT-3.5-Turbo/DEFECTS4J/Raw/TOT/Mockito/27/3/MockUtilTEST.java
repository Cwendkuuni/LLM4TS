import org.junit.Test;
import org.mockito.internal.util.MockCreationValidator;
import org.mockito.internal.util.MockHandler;
import org.mockito.internal.util.MockHandlerInterface;
import org.mockito.internal.util.reflection.LenientCopyTool;
import org.mockito.mock.MockSettingsImpl;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MockUtilTest {

    @Test
    public void testCreateMock_typicalCase() {
        // Setup
        MockHandlerInterface mockHandlerInterface = mock(MockHandlerInterface.class);
        MockCreationValidator creationValidator = mock(MockCreationValidator.class);
        when(creationValidator.validateType(any(Class.class))).thenReturn(true);
        when(creationValidator.validateExtraInterfaces(any(Class.class), any(Class[].class))).thenReturn(true);
        when(creationValidator.validateMockedType(any(Class.class), any())).thenReturn(true);
        whenNew(MockCreationValidator.class).withNoArguments().thenReturn(creationValidator);
        MockUtil mockUtil = new MockUtil();

        // Run
        Object result = mockUtil.createMock(String.class, new MockSettingsImpl());

        // Verify
        assertNotNull(result);
    }

    @Test(expected = NotAMockException.class)
    public void testResetMock_errorScenario() {
        // Setup
        MockHandlerInterface mockHandlerInterface = mock(MockHandlerInterface.class);
        Object mock = mock(Object.class);
        when(mockHandlerInterface.getMockSettings().getMockName()).thenReturn(new MockName());
        MockUtil mockUtil = new MockUtil();

        // Run
        mockUtil.resetMock(mock);
    }

    @Test
    public void testGetMockHandler_typicalCase() {
        // Setup
        MockHandlerInterface mockHandlerInterface = mock(MockHandlerInterface.class);
        MockUtil mockUtil = new MockUtil();

        // Run
        MockHandlerInterface result = mockUtil.getMockHandler(mockHandlerInterface);

        // Verify
        assertEquals(mockHandlerInterface, result);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandler_errorScenario() {
        // Setup
        MockHandlerInterface mockHandlerInterface = mock(MockHandlerInterface.class);
        Object notAMock = new Object();
        MockUtil mockUtil = new MockUtil();

        // Run
        MockHandlerInterface result = mockUtil.getMockHandler(notAMock);
    }

    @Test
    public void testIsMock_typicalCase() {
        // Setup
        MockHandlerInterface mockHandlerInterface = mock(MockHandlerInterface.class);
        MockUtil mockUtil = new MockUtil();

        // Run
        boolean result = mockUtil.isMock(mockHandlerInterface);

        // Verify
        assertTrue(result);
    }

    @Test
    public void testIsMock_notAMock() {
        // Setup
        Object notAMock = new Object();
        MockUtil mockUtil = new MockUtil();

        // Run
        boolean result = mockUtil.isMock(notAMock);

        // Verify
        assertFalse(result);
    }

    @Test
    public void testGetMockName_typicalCase() {
        // Setup
        MockName mockName = mock(MockName.class);
        MockHandlerInterface mockHandlerInterface = mock(MockHandlerInterface.class);
        when(mockHandlerInterface.getMockSettings().getMockName()).thenReturn(mockName);
        MockUtil mockUtil = new MockUtil();

        // Run
        MockName result = mockUtil.getMockName(mockHandlerInterface);

        // Verify
        assertEquals(mockName, result);
    }

}