import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.RETURNS_DEFAULTS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

import org.junit.Test;
import org.mockito.cglib.proxy.Enhancer;
import org.mockito.cglib.proxy.Factory;
import org.mockito.cglib.proxy.MethodInterceptor;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.MockHandler;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.creation.MethodInterceptorFilter;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.util.MockName;
import org.mockito.internal.util.reflection.LenientCopyTool;

public class MockUtilTest {

    private MockUtil mockUtil;

    @Before
    public void setUp() {
        mockUtil = new MockUtil();
    }

    @Test
    public void testCreateMock() {
        Class<?> classToMock = MyClass.class;
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.addExtraInterface(MyInterface.class);

        MyClass mock = mockUtil.createMock(classToMock, settings);

        assertTrue(Enhancer.isEnhanced(mock.getClass()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateMock_InvalidClass() {
        Class<?> classToMock = null;
        MockSettingsImpl settings = new MockSettingsImpl();

        mockUtil.createMock(classToMock, settings);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateMock_InvalidExtraInterface() {
        Class<?> classToMock = MyClass.class;
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.addExtraInterface(null);

        mockUtil.createMock(classToMock, settings);
    }

    @Test
    public void testCreateMock_SpiedInstance() {
        Class<?> classToMock = MyClass.class;
        MockSettingsImpl settings = new MockSettingsImpl();
        MyInterface spiedInstance = new MyImplementation();
        settings.setSpiedInstance(spiedInstance);

        MyClass mock = mockUtil.createMock(classToMock, settings);
        
        assertTrue(Enhancer.isEnhanced(mock.getClass()));
        assertEquals(spiedInstance, mock.getSpiedInstance());
    }

    @Test
    public void testResetMock() {
        MyClass mock = mock(MyClass.class);

        mockUtil.resetMock(mock);

        verify(mock).reset();
    }

    @Test(expected = NotAMockException.class)
    public void testResetMock_NullMock() {
        MyClass mock = null;

        mockUtil.resetMock(mock);
    }

    @Test(expected = NotAMockException.class)
    public void testResetMock_NotAMock() {
        MyClass mock = new MyClass();

        mockUtil.resetMock(mock);
    }

    @Test
    public void testGetMockHandler() {
        MyClass mock = mock(MyClass.class);
        MockHandlerInterface<MyClass> expectedHandler = (MockHandlerInterface<MyClass>) getInterceptor(mock).getHandler();

        MockHandlerInterface<MyClass> result = mockUtil.getMockHandler(mock);

        assertEquals(expectedHandler, result);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandler_NullMock() {
        MyClass mock = null;

        mockUtil.getMockHandler(mock);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandler_NotAMock() {
        MyClass mock = new MyClass();

        mockUtil.getMockHandler(mock);
    }

    @Test
    public void testIsMock() {
        MyClass mock = mock(MyClass.class);

        boolean result = mockUtil.isMock(mock);

        assertTrue(result);
    }

    @Test
    public void testIsMock_NotAMock() {
        MyClass mock = new MyClass();

        boolean result = mockUtil.isMock(mock);

        assertFalse(result);
    }

    @Test
    public void testGetMockName() {
        MyClass mock = mock(MyClass.class);
        MockName expectedName = mockUtil.getMockHandler(mock).getMockSettings().getMockName();

        MockName result = mockUtil.getMockName(mock);

        assertEquals(expectedName, result);
    }

    private <T> MethodInterceptorFilter getInterceptor(T mock) {
        Factory factory = (Factory) mock;
        Callback callback = factory.getCallback(0);
        if (callback instanceof MethodInterceptorFilter) {
            return (MethodInterceptorFilter) callback;
        }
        return null;
    }
}

class MyClass implements MyInterface {
    private MyInterface spiedInstance;
    
    public void reset() {
        // Reset mock implementation
    }
    
    public void setSpiedInstance(MyInterface spiedInstance) {
        this.spiedInstance = spiedInstance;
    }
    
    public MyInterface getSpiedInstance() {
        return spiedInstance;
    }
}

interface MyInterface {
    // Interface methods
}

class MyImplementation implements MyInterface {
    // Implementation class
}