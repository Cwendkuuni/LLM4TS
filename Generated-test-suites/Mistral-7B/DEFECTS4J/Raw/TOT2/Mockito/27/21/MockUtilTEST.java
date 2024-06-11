import org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.cglib.proxy.MethodInterceptor;
import org.mockito.internal.util.MockCreationValidator;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.lenient.LenientCopyTool;
import org.mockito.internal.util.reflection.LenientReflection;
import org.mockito.internal.util.reflection.util.ArrayUtils;
import org.mockito.internal.util.reflection.util.ClassImposterizer;
import org.mockito.internal.util.reflection.util.MethodInterceptorFilter;
import org.mockito.internal.util.reflection.util.MethodInterceptorWrapper;
import org.mockito.internal.util.reflection.util.MockSettingsImpl;
import org.mockito.internal.util.reflection.util.MockHandler;
import org.mockito.internal.util.reflection.util.MockHandlerInterface;
import org.mockito.internal.util.reflection.util.MockName;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.util.Arrays;

public class MockUtilTest {
    private MockUtil mockUtil;
    private MockCreationValidator creationValidator;

    @Before
    public void setUp() {
        creationValidator = new MockCreationValidator();
        mockUtil = new MockUtil(creationValidator);
    }

    @Test
    public void testConstructorWithMockCreationValidator() {
        MockUtil mockUtil = new MockUtil(creationValidator);
        assertNotNull(mockUtil);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullMockCreationValidator() {
        MockUtil mockUtil = new MockUtil(null);
    }

    @Test
    public <T> void testCreateMockWithValidArguments() {
        Class<TestClass> classToMock = TestClass.class;
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.setName("TestMock");
        settings.setSerializable(true);
        settings.setSpiedInstance(new TestClass());
        T mock = mockUtil.createMock(classToMock, settings);

        assertTrue(mock instanceof TestClass);
        assertEquals("TestMock", mockUtil.getMockName(mock));
    }

    @Test(expected = NullPointerException.class)
    public void testCreateMockWithNullClassToMock() {
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.setName("TestMock");
        mockUtil.createMock(null, settings);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateMockWithNullMockSettings() {
        Class<TestClass> classToMock = TestClass.class;
        mockUtil.createMock(classToMock, null);
    }

    @Test(expected = NotAMockException.class)
    public void testCreateMockWithInstanceBeingAMock() {
        Class<TestClass> classToMock = TestClass.class;
        TestClass spiedInstance = new TestClass();
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.setName("TestMock");
        settings.setSpiedInstance(spiedInstance);
        mockUtil.createMock(classToMock, settings);
    }

    @Test(expected = NotAMockException.class)
    public void testCreateMockWithClassAlreadyAMock() {
        Class<TestClass> classToMock = TestClass.class;
        TestClass mock = (TestClass) Proxy.getProxyClass(classToMock.getClassLoader(), new Class[]{TestClass.class, Serializable.class}).newInstance();
        mockUtil.createMock(classToMock, new MockSettingsImpl());
    }

    @Test(expected = MockCreationException.class)
    public void testCreateMockWithInvalidClass() {
        Class<Object> classToMock = Object.class;
        mockUtil.createMock(classToMock, new MockSettingsImpl());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateMockWithNullSpiedInstance() {
        Class<TestClass> classToMock = TestClass.class;
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.setName("TestMock");
        mockUtil.createMock(classToMock, settings);
    }

    @Test
    public void testResetMock() throws Exception {
        Class<TestClass> classToMock = TestClass.class;
        TestClass mock = createMock(classToMock, new MockSettingsImpl());
        mockUtil.resetMock(mock);
        assertFalse(mockUtil.getMockHandler(mock).getNumberOfInvocations());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testResetMockWithNullMock() {
        mockUtil.resetMock(null);
    }

    @Test
    public void testNewMethodInterceptorFilter() {
        MockSettingsImpl settings = new MockSettingsImpl();
        MethodInterceptorFilter filter = mockUtil.newMethodInterceptorFilter(settings);
        assertNotNull(filter);
    }

    @Test(expected = NullPointerException.class)
    public void testGetMockHandlerWithNullMock() {
        TestClass mock = new TestClass();
        mockUtil.getMockHandler(null);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerWithNonMockitoMock() {
        Object nonMockitoMock = Proxy.getProxyClass(TestClass.class.getClassLoader(), new Class[]{TestClass.class, Serializable.class}).newInstance();
        mockUtil.getMockHandler(nonMockitoMock);
    }

    @Test
    public void testIsMockitoMockWithMockitoMock() {
        TestClass mock = createMock(TestClass.class, new MockSettingsImpl());
        assertTrue(mockUtil.isMockitoMock(mock));
    }

    @Test
    public void testIsMockitoMockWithNonMockitoMock() {
        Object nonMockitoMock = Proxy.getProxyClass(TestClass.class.getClassLoader(), new Class[]{TestClass.class, Serializable.class}).newInstance();
        assertFalse(mockUtil.isMockitoMock(nonMockitoMock));
    }

    @Test
    public void testIsMockWithMockitoMock() {
        TestClass mock = createMock(TestClass.class, new MockSettingsImpl());
        assertTrue(mockUtil.isMock(mock));
    }

    @Test
    public void testIsMockWithNonMockitoMock() {
        Object nonMockitoMock = Proxy.getProxyClass(TestClass.class.getClassLoader(), new Class[]{TestClass.class, Serializable.class}).newInstance();
        assertFalse(mockUtil.isMock(nonMockitoMock));
    }

    @Test
    public void testGetMockNameWithMockitoMock() {
        TestClass mock = createMock(TestClass.class, new MockSettingsImpl());
        MockName mockName = mockUtil.getMockName(mock);
        assertEquals("TestMock", mockName.getName());
    }

    private <T> T createMock(Class<T> classToMock, MockSettingsImpl settings) {
        return (T) ClassImposterizer.INSTANCE.imposterise(mockUtil.newMethodInterceptorFilter(settings), classToMock, new Class[]{Serializable.class});
    }
}

class TestClass implements Serializable {
}