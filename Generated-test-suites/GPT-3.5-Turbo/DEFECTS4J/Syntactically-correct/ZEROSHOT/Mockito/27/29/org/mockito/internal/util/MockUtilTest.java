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

public class MockUtilTest {
    private MockUtil mockUtil;

    @Before
    public void setUp() {
        mockUtil = new MockUtil();
    }

    @Test
    public void testCreateMock() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        mockSettings.setSpiedInstance(new Object());
        mockSettings.setSerializable(true);

        Object mock = mockUtil.createMock(Object.class, mockSettings);

        Assert.assertNotNull(mock);
    }

    @Test(expected = NotAMockException.class)
    public void testResetMock_NotAMock() {
        mockUtil.resetMock("not_a_mock");
    }

    @Test
    public void testResetMock() {
        Factory mock = createMockFactory();
        mockUtil.resetMock(mock);

        Assert.assertNotNull(mock);
    }

    @Test
    public void testNewMethodInterceptorFilter() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        MethodInterceptorFilter filter = mockUtil.newMethodInterceptorFilter(mockSettings);

        Assert.assertNotNull(filter);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandler_NotAMock() {
        mockUtil.getMockHandler("not_a_mock");
    }

    @Test
    public void testGetMockHandler_IsMock() {
        Factory mock = createMockFactory();
        MockHandlerInterface handler = mockUtil.getMockHandler(mock);

        Assert.assertNotNull(handler);
    }

    @Test
    public void testIsMock_NotMock() {
        boolean isMock = mockUtil.isMock("not_a_mock");

        Assert.assertFalse(isMock);
    }

    @Test
    public void testIsMock_IsMock() {
        Factory mock = createMockFactory();
        boolean isMock = mockUtil.isMock(mock);

        Assert.assertTrue(isMock);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockName_NotAMock() {
        mockUtil.getMockName("not_a_mock");
    }

    @Test
    public void testGetMockName() {
        Factory mock = createMockFactory();
        MockName mockName = mockUtil.getMockName(mock);

        Assert.assertNotNull(mockName);
    }

    private Factory createMockFactory() {
        return (Factory) ClassImposterizer.INSTANCE.imposterise(new MethodInterceptorFilter(new InvocationNotifierHandler<>(new MockHandler<>(new MockSettingsImpl()), new MockSettingsImpl()), new MockSettingsImpl()), Factory.class);
    }
}