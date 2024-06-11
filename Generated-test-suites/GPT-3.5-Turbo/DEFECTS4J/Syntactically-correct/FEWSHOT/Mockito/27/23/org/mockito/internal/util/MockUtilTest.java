package org.mockito.internal.util;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.MockHandler;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.util.reflection.LenientCopyTool;
import org.mockito.cglib.proxy.Factory;
import org.mockito.cglib.proxy.Callback;
import org.mockito.cglib.proxy.MethodInterceptorFilter;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.creation.MockSettingsImpl;
import java.io.Serializable;
import org.mockito.MockCreationValidator;
import org.mockito.internal.InvocationNotifierHandler;
import org.mockito.MockName;
public class MockUtilTest {
    private MockUtil mockUtil; 
    private MockCreationValidator mockCreationValidator;
    
    @Before
    public void setUp() {
        mockCreationValidator = new MockCreationValidator();
        mockUtil = new MockUtil(mockCreationValidator);
    }
    
    @Test
    public void testCreateMock() {
        MockSettingsImpl settings = new MockSettingsImpl();
        Class<?> classToMock = Object.class;
        Class<?>[] extraInterfaces = new Class[0];
        Object spiedInstance = null;
        settings.setExtraInterfaces(extraInterfaces);
        settings.setSpiedInstance(spiedInstance);
        settings.setSerializable(true);
        mockCreationValidator.validateType(classToMock);
        mockCreationValidator.validateExtraInterfaces(classToMock, settings.getExtraInterfaces());
        mockCreationValidator.validateMockedType(classToMock, settings.getSpiedInstance());
        settings.initiateMockName(classToMock);
        MethodInterceptorFilter filter = newMethodInterceptorFilter(settings);
        
        Class<?>[] ancillaryTypes;
        if (settings.isSerializable()) {
            ancillaryTypes = extraInterfaces == null ? new Class<?>[]{Serializable.class} : ArrayUtils.concat(extraInterfaces, Serializable.class);
        } else {
            ancillaryTypes = extraInterfaces == null ? new Class<?>[0] : extraInterfaces;
        }
        
        Object spiedInstance = settings.getSpiedInstance();
        Object mock = ClassImposterizer.INSTANCE.imposterise(filter, classToMock, ancillaryTypes);
        
        if (spiedInstance != null) {
            new LenientCopyTool().copyToMock(spiedInstance, mock);
        }
        
        assertNotNull(mock);
        assertTrue(mock instanceof classToMock);
    }
    
    @Test
    public void testResetMock() {
        T mock = Mockito.mock(T.class);
        MockHandlerInterface<T> oldMockHandler = getMockHandler(mock);
        MockHandler<T> newMockHandler = new MockHandler<T>(oldMockHandler);
        MethodInterceptorFilter newFilter = new MethodInterceptorFilter(newMockHandler, (MockSettingsImpl) org.mockito.Mockito.withSettings().defaultAnswer(org.mockito.Mockito.RETURNS_DEFAULTS));
        ((Factory) mock).setCallback(0, newFilter);
    }
    
    @Test
    public void testGetMethodInterceptorFilter() {
        T mock = Mockito.mock(T.class);
        if (!(mock instanceof Factory)) {
            assertNull(getInterceptor(mock));
        } else {
            Factory factory = (Factory) mock;
            Callback callback = factory.getCallback(0);
            if (callback instanceof MethodInterceptorFilter) {
                assertNotNull(getInterceptor(mock));
                assertTrue(getInterceptor(mock) instanceof MethodInterceptorFilter);
            }
        }
    }
    
    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerWithNullMock() {
        T mock = null;
        try {
            getMockHandler(mock);
        } catch (NotAMockException e) {
            assertEquals("Argument should be a mock, but is null!", e.getMessage());
            throw e;
        }
    }
    
    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerWithNonMockitoMock() {
        T mock = new T();
        try {
            getMockHandler(mock);
        } catch (NotAMockException e) {
            assertEquals("Argument should be a mock, but is: " + mock.getClass(), e.getMessage());
            throw e;
        }
    }
    
    @Test
    public void testIsMock() {
        T mock = Mockito.mock(T.class);
        assertTrue(isMock(mock));
    }
    
    @Test
    public void testIsNotMock() {
        T nonMock = new T();
        assertFalse(isMock(nonMock));
    }
    
    @Test
    public void testGetMockName() {
        T mock = Mockito.mock(T.class);
        assertNotNull(getMockName(mock));
    }
    
    private <T> MethodInterceptorFilter newMethodInterceptorFilter(MockSettingsImpl settings) {
        MockHandler<T> mockHandler = new MockHandler<T>(settings);
        InvocationNotifierHandler<T> invocationNotifierHandler = new InvocationNotifierHandler<T>(mockHandler, settings);
        return new MethodInterceptorFilter(invocationNotifierHandler, settings);
    }
    
    private <T> MockHandlerInterface<T> getMockHandler(T mock) {
        if (mock == null) {
            throw new NotAMockException("Argument should be a mock, but is null!");
        }
        if (isMockitoMock(mock)) {
            return (MockHandlerInterface<T>) getInterceptor(mock).getHandler();
        } else {
            throw new NotAMockException("Argument should be a mock, but is: " + mock.getClass());
        }
    }
    
    private <T> boolean isMockitoMock(T mock) {
        return getInterceptor(mock) != null;
    }
    
    private <T> MethodInterceptorFilter getInterceptor(T mock) {
        if (!(mock instanceof Factory)) {
            return null;
        }
        Factory factory = (Factory) mock;
        Callback callback = factory.getCallback(0);
        if (callback instanceof MethodInterceptorFilter) {
            return (MethodInterceptorFilter) callback;
        }
        return null;
    }
}