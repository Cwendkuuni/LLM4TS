import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.MockHandler;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.LenientCopyTool;
import org.mockito.cglib.proxy.Factory;
import org.mockito.cglib.proxy.MethodInterceptorFilter;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MockUtilTest {

    private MockUtil mockUtil;
    private MockSettingsImpl mockSettings;

    @Before
    public void setUp() {
        mockUtil = new MockUtil();
    }

    @Test
    public void testCreateMock_validClass_validSettings_mockCreated() {
        // Setup
        ClassToMock classToMock = new ClassToMock();
        mockSettings = new MockSettingsImpl();

        // Execute
        Object mock = mockUtil.createMock(classToMock.getClass(), mockSettings);

        // Verify
        assertNotNull(mock);
        assertTrue(mock instanceof ClassToMock);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandler_nullMock_exceptionThrown() {
        // Setup

        // Execute
        mockUtil.getMockHandler(null);

        // Verify
        // Exception expected
    }

    @SuppressWarnings("unchecked")
    @Test(expected = NotAMockException.class)
    public void testGetMockHandler_nonMockitoMock_exceptionThrown() {
        // Setup
        Object nonMockitoMock = new Object();

        // Execute
        mockUtil.getMockHandler(nonMockitoMock);

        // Verify
        // Exception expected
    }

    @Test
    public void testIsMock_validMock_true() {
        // Setup
        Object mock = mock(ClassToMock.class);

        // Execute
        boolean isMock = mockUtil.isMock(mock);

        // Verify
        assertTrue(isMock);
    }

    @Test
    public void testIsMock_nonMock_false() {
        // Setup
        Object nonMock = new Object();

        // Execute
        boolean isMock = mockUtil.isMock(nonMock);

        // Verify
        assertFalse(isMock);
    }

    @Test
    public void testResetMock_validMock_mockReset() {
        // Setup
        ClassToMock mock = mock(ClassToMock.class);
        MockHandlerInterface<ClassToMock> oldMockHandler = mockUtil.getMockHandler(mock);

        // Execute
        mockUtil.resetMock(mock);
        MockHandlerInterface<ClassToMock> newMockHandler = mockUtil.getMockHandler(mock);

        // Verify
        assertNotSame(oldMockHandler, newMockHandler);
    }

    @Test
    public void testGetMockName_validMock_mockNameReturned() {
        // Setup
        ClassToMock mock = mock(ClassToMock.class);
        MockHandlerInterface<ClassToMock> mockHandler = mockUtil.getMockHandler(mock);

        // Execute
        MockName mockName = mockUtil.getMockName(mock);
        mockHandler.getMockSettings().setMockName(mockName);

        // Verify
        assertNotNull(mockName);
        assertSame(mockName, mockHandler.getMockSettings().getMockName());
    }
}

class ClassToMock {
    // Class for testing purposes only
}