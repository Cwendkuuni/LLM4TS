
import org.junit.Test;
import org.mockito.cglib.proxy.Factory;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.util.MockCreationValidator;
import org.mockito.internal.util.MockUtil;

import java.io.Serializable;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MockUtilTest {

    @Test
    public void testCreateMock() {
        MockCreationValidator creationValidator = mock(MockCreationValidator.class);
        MockUtil mockUtil = new MockUtil(creationValidator);

        // Typical use case
        assertNotNull(mockUtil.createMock(MockUtil.class, new MockSettingsImpl()));

        // Edge case - classToMock is an interface
        assertNotNull(mockUtil.createMock(Serializable.class, new MockSettingsImpl()));

        // Error scenario - classToMock is null
        try {
            mockUtil.createMock(null, new MockSettingsImpl());
            fail("Expected NotAMockException");
        } catch (NotAMockException e) {
            assertEquals("Argument should be a mock, but is null!", e.getMessage());
        }
    }

    @Test
    public void testResetMock() {
        MockUtil mockUtil = new MockUtil();

        // Typical use case
        MockUtil mock = mock(MockUtil.class);
        when(mockUtil.isMock(mock)).thenReturn(true);
        mockUtil.resetMock(mock);
        verify(mock).setCallback(0, mockUtil.newMethodInterceptorFilter(new MockSettingsImpl()));

        // Error scenario - mock is null
        try {
            mockUtil.resetMock(null);
            fail("Expected NotAMockException");
        } catch (NotAMockException e) {
            assertEquals("Argument should be a mock, but is null!", e.getMessage());
        }
    }

    @Test
    public void testGetMethodInterceptorFilter() {
        MockUtil mockUtil = new MockUtil();

        // Typical use case
        Factory mock = mock(Factory.class);
        assertNotNull(mockUtil.getInterceptor(mock));

        // Error scenario - mock is not a Factory instance
        Object notMock = new Object();
        assertNull(mockUtil.getInterceptor(notMock));
    }

    @Test
    public void testIsMock() {
        MockUtil mockUtil = new MockUtil();

        // Typical use case
        Factory mock = mock(Factory.class);
        assertTrue(mockUtil.isMock(mock));

        // Error scenario - mock is null
        assertFalse(mockUtil.isMock(null));
    }

    @Test
    public void testGetMockName() {
        MockUtil mockUtil = new MockUtil();

        // Typical use case
        MockHandlerInterface mockHandler = mock(MockHandlerInterface.class);
        when(mockUtil.getMockHandler(mockHandler)).thenReturn(mockHandler);
        assertNotNull(mockUtil.getMockName(mockHandler));

        // Error scenario - mock is null
        try {
            mockUtil.getMockName(null);
            fail("Expected NotAMockException");
        } catch (NotAMockException e) {
            assertEquals("Argument should be a mock, but is null!", e.getMessage());
        }

        // Error scenario - mock is not a Mockito mock
        Object notMock = new Object();
        try {
            mockUtil.getMockName(notMock);
            fail("Expected NotAMockException");
        } catch (NotAMockException e) {
            assertEquals("Argument should be a mock, but is: " + Object.class, e.getMessage());
        }
    }
}
