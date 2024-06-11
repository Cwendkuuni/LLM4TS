import org.junit.Test;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.util.ArrayUtils;
import org.mockito.internal.util.MockCreationValidator;
import org.mockito.internal.util.MockName;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.LenientCopyTool;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MockUtilTest {

    @Test
    public void testCreateMock() {
        // Given
        MockUtil mockUtil = new MockUtil();
        Class<?> classToMock = TestClass.class;

        // When
        TestClass mock = mockUtil.createMock(classToMock, new MockSettingsImpl());

        // Then
        assertNotNull(mock);
        assertTrue(classToMock.isAssignableFrom(mock.getClass()));
        assertNotNull(getMockHandler(mock));
        assertNotNull(getInterceptor(mock));
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerWithNull() {
        // Given
        MockUtil mockUtil = new MockUtil();

        // When
        mockUtil.getMockHandler(null);

        // Then
        // NotAMockException should be thrown
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerWithNonMock() {
        // Given
        MockUtil mockUtil = new MockUtil();
        TestClass nonMock = new TestClass();

        // When
        mockUtil.getMockHandler(nonMock);

        // Then
        // NotAMockException should be thrown
    }

    @Test
    public void testIsMockWithMock() {
        // Given
        MockUtil mockUtil = new MockUtil();
        TestClass mock = mock(TestClass.class);

        // When
        boolean result = mockUtil.isMock(mock);

        // Then
        assertTrue(result);
    }

    @Test
    public void testIsMockWithNonMock() {
        // Given
        MockUtil mockUtil = new MockUtil();
        TestClass nonMock = new TestClass();

        // When
        boolean result = mockUtil.isMock(nonMock);

        // Then
        assertFalse(result);
    }

    @Test
    public void testResetMock() {
        // Given
        MockUtil mockUtil = new MockUtil();
        TestClass mock = mock(TestClass.class);
        MethodInterceptorFilter oldInterceptor = getInterceptor(mock);
        MockHandlerInterface oldHandler = getMockHandler(mock);

        // When
        mockUtil.resetMock(mock);
        MethodInterceptorFilter newInterceptor = getInterceptor(mock);
        MockHandlerInterface newHandler = getMockHandler(mock);

        // Then
        assertNotSame(oldHandler, newHandler);
        assertNotSame(oldInterceptor, newInterceptor);
        assertEquals(MockSettingsImpl.class, newInterceptor.getMockSettings().getClass());
        assertEquals(oldHandler.getInvocationContainer().toString(), newHandler.getInvocationContainer().toString());
    }

    @Test
    public void testGetMockName() {
        // Given
        MockUtil mockUtil = new MockUtil();
        TestClass mock = mock(TestClass.class);
        MockName mockName = new MockName();
        mockUtil.getMockHandler(mock).getMockSettings().setMockName(mockName);

        // When
        MockName result = mockUtil.getMockName(mock);

        // Then
        assertEquals(mockName, result);
    }

    // Helper methods to access private fields

    private MethodInterceptorFilter getInterceptor(Object mock) {
        try {
            Field factoryField = mock.getClass().getDeclaredField("CGLIB$CALLBACK_0");
            factoryField.setAccessible(true);
            Callback callback = (Callback) factoryField.get(mock);
            if (callback instanceof MethodInterceptorFilter) {
                return (MethodInterceptorFilter) callback;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private MockHandlerInterface getMockHandler(Object mock) {
        try {
            Method getMockHandlerMethod = mock.getClass().getMethod("getMockHandler");
            getMockHandlerMethod.setAccessible(true);
            return (MockHandlerInterface) getMockHandlerMethod.invoke(mock);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Mock class for testing

    private static class TestClass {
    }
}