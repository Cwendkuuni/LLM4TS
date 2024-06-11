import org.junit.Assert;
import org.junit.Test;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.util.reflection.LenientCopyTool;
import org.mockito.internal.invocation.InvocationNotifierHandler;
import org.mockito.internal.MockHandler;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.creation.MethodInterceptorFilter;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.util.MockCreationValidator;
import org.mockito.internal.util.MockName;
import org.mockito.internal.util.MockUtil;

import java.io.Serializable;

public class MockUtilTest {

    @Test
    public void testCreateMock() {
        MockUtil mockUtil = new MockUtil();
        ClassToMock mock = mockUtil.createMock(ClassToMock.class, new MockSettingsImpl());
        Assert.assertNotNull(mock);
    }

    @Test
    public void testResetMock() {
        MockUtil mockUtil = new MockUtil();
        ClassToMock mock = mockUtil.createMock(ClassToMock.class, new MockSettingsImpl());
        mockUtil.resetMock(mock);

        // Verify that resetMock does not throw any exception
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerWithNull() {
        MockUtil mockUtil = new MockUtil();
        mockUtil.getMockHandler(null);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerWithNonMockObject() {
        MockUtil mockUtil = new MockUtil();
        mockUtil.getMockHandler(new Object());
    }

    @Test
    public void testGetMockHandlerWithMockitoMock() {
        MockUtil mockUtil = new MockUtil();
        ClassToMock mock = mockUtil.createMock(ClassToMock.class, new MockSettingsImpl());
        MockHandlerInterface<ClassToMock> mockHandler = mockUtil.getMockHandler(mock);
        Assert.assertNotNull(mockHandler);
    }

    @Test
    public void testIsMockWithMockObject() {
        MockUtil mockUtil = new MockUtil();
        ClassToMock mock = mockUtil.createMock(ClassToMock.class, new MockSettingsImpl());
        boolean isMock = mockUtil.isMock(mock);
        Assert.assertTrue(isMock);
    }

    @Test
    public void testIsMockWithNonMockObject() {
        MockUtil mockUtil = new MockUtil();
        boolean isMock = mockUtil.isMock(new Object());
        Assert.assertFalse(isMock);
    }

    @Test
    public void testGetMockName() {
        MockUtil mockUtil = new MockUtil();
        ClassToMock mock = mockUtil.createMock(ClassToMock.class, new MockSettingsImpl());
        MockName mockName = mockUtil.getMockName(mock);
        Assert.assertNotNull(mockName);
    }

    // Additional test cases for edge cases and exception handling can be added here

    // 
    // Test cases go here
    // 
}