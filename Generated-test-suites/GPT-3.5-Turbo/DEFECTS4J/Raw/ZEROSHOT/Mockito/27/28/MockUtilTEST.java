import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.cglib.proxy.Factory;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.InvocationNotifierHandler;
import org.mockito.internal.MockHandler;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.creation.MethodInterceptorFilter;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.util.reflection.LenientCopyTool;

import java.io.Serializable;

@SuppressWarnings("unchecked")
public class MockUtilTest {

    private MockUtil mockUtil;

    @Before
    public void setup() {
        mockUtil = new MockUtil();
    }

    @Test
    public void testCreateMock_ValidClass_ValidSettings() {
        Class<TestClass> classToMock = TestClass.class;
        MockSettingsImpl settings = new MockSettingsImpl();
        TestClass mock = mockUtil.createMock(classToMock, settings);
        
        Assert.assertNotNull(mock);
        Assert.assertTrue(mock instanceof TestClass);
    }

    @Test(expected = NotAMockException.class)
    public void testCreateMock_NullClass_InvalidSettings() {
        Class<TestInterface> classToMock = null;
        MockSettingsImpl settings = new MockSettingsImpl();
        TestInterface mock = mockUtil.createMock(classToMock, settings);
        
        Assert.fail("Expected NotAMockException");
    }

    @Test
    public void testResetMock_ValidMock() {
        TestClass mock = mockUtil.createMock(TestClass.class, new MockSettingsImpl());
        
        mockUtil.resetMock(mock);

        Assert.assertTrue(mockUtil.isMock(mock));
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandler_NullMock() {
        TestClass mock = null;
        
        mockUtil.getMockHandler(mock);
        
        Assert.fail("Expected NotAMockException");
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandler_NonMockMock() {
        TestClass mock = new TestClass();
        
        mockUtil.getMockHandler(mock);
        
        Assert.fail("Expected NotAMockException");
    }

    @Test
    public void testIsMock_ValidMock() {
        TestClass mock = mockUtil.createMock(TestClass.class, new MockSettingsImpl());

        boolean isMock = mockUtil.isMock(mock);

        Assert.assertTrue(isMock);
    }

    @Test
    public void testIsMock_NullMock() {
        TestClass mock = null;

        boolean isMock = mockUtil.isMock(mock);

        Assert.assertFalse(isMock);
    }

    @Test
    public void testGetMockName_ValidMock() {
        TestClass mock = mockUtil.createMock(TestClass.class, new MockSettingsImpl());

        MockName mockName = mockUtil.getMockName(mock);

        Assert.assertNotNull(mockName);
    }

    private static class TestClass {}

    private interface TestInterface {}
}