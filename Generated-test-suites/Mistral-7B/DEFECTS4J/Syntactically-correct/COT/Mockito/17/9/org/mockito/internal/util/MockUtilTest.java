import org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.MockitoCore;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.LenientCopyTool;
import org.mockito.internal.util.validation.CreationValidator;
import org.mockito.internal.util.validation.MockSettingsImpl;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Proxy;
import java.util.Arrays;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class MockUtilTest {

    private MockUtil mockUtil;
    private CreationValidator creationValidator;

    @Before
    public void setup() {
        creationValidator = MockitoCore.createMock(CreationValidator.class);
        mockUtil = new MockUtil(creationValidator);
    }

    @Test
    public void testCreateMock_ValidScenario() {
        Class<MockitoJUnitRunner> mockitoJUnitRunnerType = MockitoJUnitRunner.class;
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.setExtraInterfaces(new Class[]{MockitoJUnitRunner.class});
        Object spiedInstance = spy(new Object());
        Object mock = mockUtil.createMock(mockitoJUnitRunnerType, settings);
        assertTrue(mock instanceof MockitoJUnitRunner);
        assertEquals(settings.getMockName(mockitoJUnitRunnerType), mock.getClass().getName());
        assertEquals(spiedInstance, mockUtil.getMockHandler(mock).getMockNameProvider().getOriginalMock());
        assertNotNull(mockUtil.getInterceptor(mock));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateMock_NullClassToMock() {
        mockUtil.createMock(null, new MockSettingsImpl());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateMock_InvalidClassToMock() {
        Class<String> stringClass = String.class;
        mockUtil.createMock(stringClass, new MockSettingsImpl());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateMock_NullSettings() {
        mockUtil.createMock(MockitoJUnitRunner.class, null);
    }

    @Test
    public void testCreateMock_EmptyExtraInterfaces() {
        Class<MockitoJUnitRunner> mockitoJUnitRunnerType = MockitoJUnitRunner.class;
        MockSettingsImpl settings = new MockSettingsImpl();
        Object spiedInstance = spy(new Object());
        MockHandler<MockitoJUnitRunner> mockHandler = mockUtil.createMock(mockitoJUnitRunnerType, settings);
        assertNull(settings.getExtraInterfaces());
        assertEquals(mockitoJUnitRunnerType, mockHandler.getMockedType());
        assertEquals(spiedInstance, mockHandler.getMockSettings().getSpiedInstance());
    }

    @Test
    public void testCreateMock_ExtraInterfaces() {
        Class<MockitoJUnitRunner> mockitoJUnitRunnerType = MockitoJUnitRunner.class;
        Class<LenientCopyTool> lenientCopyToolType = LenientCopyTool.class;
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.setExtraInterfaces(new Class[]{LenientCopyTool.class});
        Object spiedInstance = spy(new Object());
        Object mock = mockUtil.createMock(mockitoJUnitRunnerType, settings);
        assertEquals(Arrays.asList(mockitoJUnitRunnerType, lenientCopyToolType), Arrays.asList(mock.getClass().getInterfaces()));
        assertEquals(spiedInstance, mockUtil.getMockHandler(mock).getMockNameProvider().getOriginalMock());
        assertNotNull(mockUtil.getInterceptor(mock));
    }

    @Test
    public void testCreateMock_SpiedInstanceIsNotNull() {
        Class<MockitoJUnitRunner> mockitoJUnitRunnerType = MockitoJUnitRunner.class;
        MockSettingsImpl settings = new MockSettingsImpl();
        Object spiedInstance = new Object();
        Object mock = mockUtil.createMock(mockitoJUnitRunnerType, settings, spiedInstance);
        assertNotNull(spiedInstance);
        assertEquals(spiedInstance, mockUtil.getMockHandler(mock).getMockNameProvider().getOriginalMock());
        assertNotNull(mockUtil.getInterceptor(mock));
    }

    @Test(expected = NotAMockException.class)
    public void testCreateMock_InvalidMock() {
        Object invalidMock = new Object();
        mockUtil.createMock(MockitoJUnitRunner.class, new MockSettingsImpl(), invalidMock);
    }

    @Test(expected = NotAMockException.class)
    public void testCreateMock_NonMockitoMock() {
        Object nonMockitoMock = Proxy.newProxyInstance(MockUtilTest.class.getClassLoader(), new Class[]{}, new ObjectHandler());
        mockUtil.createMock(MockitoJUnitRunner.class, new MockSettingsImpl(), nonMockitoMock);
    }

    @Test
    public void testResetMock() {
        Class<MockitoJUnitRunner> mockitoJUnitRunnerType = MockitoJUnitRunner.class;
        Object mock = mockUtil.createMock(mockitoJUnitRunnerType, new MockSettingsImpl());
        MockHandler<MockitoJUnitRunner> mockHandler = mockUtil.getMockHandler(mock);
        MethodInterceptorFilter filter = new MethodInterceptorFilter(mockHandler, withSettings().defaultAnswer(RETURNS_DEFAULTS));
        ((Factory) mock).setCallback(0, filter);
        resetMock(mock);
        assertNotNull(mockUtil.getInterceptor(mock));
    }

    @Test(expected = NullPointerException.class)
    public void testResetMock_NullMock() {
        resetMock(null);
    }

    @Test(expected = NotAMockException.class)
    public void testResetMock_InvalidMock() {
        Object invalidMock = new Object();
        resetMock(invalidMock);
    }

    @Test
    public void testIsMock() {
        Class<MockitoJUnitRunner> mockitoJUnitRunnerType = MockitoJUnitRunner.class;
        Object mockitoJUnitRunnerMock = mockUtil.createMock(mockitoJUnitRunnerType, new MockSettingsImpl());
        Object emptyObject = new Object();
        Object nonMockitoMock = Proxy.newProxyInstance(MockUtilTest.class.getClassLoader(), new Class[]{}, new ObjectHandler());
        assertTrue(mockUtil.isMock(mockitoJUnitRunnerMock));
        assertFalse(mockUtil.isMock(emptyObject));
        assertFalse(mockUtil.isMock(nonMockitoMock));
    }

    @Test(expected = NullPointerException.class)
    public void testIsMock_NullMock() {
        isMock(null);
    }

    private static class ObjectHandler implements InvocationHandler {
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            return null;
        }
    }
}