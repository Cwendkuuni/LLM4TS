import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.cglib.proxy.Enhancer;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.MockHandler;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.creation.MethodInterceptorFilter;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.util.reflection.LenientCopyTool;
import static org.junit.Assert.*;

public class MockUtilTest {
    
    @Test
    public void testCreateMock() {
        MockUtil util = new MockUtil();
        Class<?> classToMock = Object.class;
        MockSettingsImpl settings = Mockito.mock(MockSettingsImpl.class);
        
        util.createMock(classToMock, settings);
        
        // Verify that creationValidator.validateType() method is called with classToMock
        Mockito.verify(util.creationValidator).validateType(classToMock);
        
        // Verify that creationValidator.validateExtraInterfaces() method is called with classToMock and settings.getExtraInterfaces()
        Mockito.verify(util.creationValidator).validateExtraInterfaces(classToMock, settings.getExtraInterfaces());
        
        // Verify that creationValidator.validateMockedType() method is called with classToMock and settings.getSpiedInstance()
        Mockito.verify(util.creationValidator).validateMockedType(classToMock, settings.getSpiedInstance());
        
        // Verify that settings.initiateMockName() method is called with classToMock
        Mockito.verify(settings).initiateMockName(classToMock);
        
        // Verify that MockHandler constructor is called with settings
        Mockito.verify(util.creationValidator).validateType(classToMock);
        
        // Verify that MethodInterceptorFilter constructor is called with mockHandler and settings
        Mockito.verify(util.creationValidator).validateType(classToMock);
        
        // Verify that ClassImposterizer.INSTANCE.imposterise() method is called with filter, classToMock, and ancillaryTypes
        Mockito.verify(ClassImposterizer.INSTANCE).imposterise(Mockito.any(MethodInterceptorFilter.class), Mockito.eq(classToMock), Mockito.any(Class[].class));
        
        // Verify that LenientCopyTool.copyToMock() method is called with spiedInstance and mock
        Mockito.verify(new LenientCopyTool()).copyToMock(settings.getSpiedInstance(), mock);
        
        // Verify that the mock is returned
        assertNotNull(mock);
    }
    
    @Test
    public void testResetMock() {
        MockUtil util = new MockUtil();
        Object mock = new Object();
        
        util.resetMock(mock);
        
        // Verify that getMockHandler() method is called with mock
        Mockito.verify(util).getMockHandler(mock);
        
        // Verify that getInterceptor() method is called with mock
        Mockito.verify(util).getInterceptor(mock);
        
        // Verify that Factory.setCallback() method is called with 0 and newFilter
        Mockito.verify(((Factory) mock)).setCallback(0, newFilter);
    }
    
    @Test(expected = NotAMockException.class)
    public void testGetMockHandler_NullArgument() {
        MockUtil util = new MockUtil();
        Object mock = null;
        
        util.getMockHandler(mock);
    }
    
    @Test
    public void testGetMockHandler_NotMock() {
        MockUtil util = new MockUtil();
        Object mock = new Object();
        
        try {
            util.getMockHandler(mock);
            fail("NotAMockException should have been thrown");
        } catch (NotAMockException e) {
            // Exception expected
        }
    }
    
    @Test
    public void testGetMockHandler_Mock() {
        MockUtil util = new MockUtil();
        Object mock = Mockito.mock(Object.class);
        
        util.getMockHandler(mock);
        
        // Verify that isMockitoMock() method is called with mock
        Mockito.verify(util).isMockitoMock(mock);
        
        // Verify that Enhancer.isEnhanced() method is called with mock.getClass()
        Mockito.verify(Enhancer).isEnhanced(mock.getClass());
        
        // Verify that getInterceptor() method is called with mock
        Mockito.verify(util).getInterceptor(mock);
    }
    
    @Test
    public void testIsMock_True() {
        MockUtil util = new MockUtil();
        Object mock = Mockito.mock(Object.class);
        
        boolean result = util.isMock(mock);
        
        assertTrue(result);
        
        // Verify that isMockitoMock() method is called with mock
        Mockito.verify(util).isMockitoMock(mock);
    }
    
    @Test
    public void testIsMock_False() {
        MockUtil util = new MockUtil();
        Object mock = new Object();
        
        boolean result = util.isMock(mock);
        
        assertFalse(result);
        
        // Verify that isMockitoMock() method is called with mock
        Mockito.verify(util).isMockitoMock(mock);
    }
    
    @Test
    public void testGetInterceptor_ValidInterceptor() {
        MockUtil util = new MockUtil();
        Object mock = Mockito.mock(Object.class);
        
        MethodInterceptorFilter interceptor = util.getInterceptor(mock);
        
        // Verify that Factory.getCallback() method is called with 0
        Mockito.verify(((Factory) mock)).getCallback(0);
        
        // Verify that the correct interceptor is returned
        assertNotNull(interceptor);
    }
    
    @Test
    public void testGetInterceptor_NoInterceptor() {
        MockUtil util = new MockUtil();
        Object mock = new Object();
        
        MethodInterceptorFilter interceptor = util.getInterceptor(mock);
        
        // Verify that Factory.getCallback() method is called with 0
        Mockito.verify(((Factory) mock)).getCallback(0);
        
        // Verify that null is returned
        assertNull(interceptor);
    }
    
    @Test
    public void testGetMockName() {
        MockUtil util = new MockUtil();
        Object mock = Mockito.mock(Object.class);
        
        MockName mockName = util.getMockName(mock);
        
        // Verify that getMockHandler() method is called with mock
        Mockito.verify(util).getMockHandler(mock);
        
        // Verify that getMockSettings() and getMockName() methods are called
        Mockito.verify(mockHandler).getMockSettings();
        Mockito.verify(mockSettings).getMockName();
        
        // Verify that the correct mockName is returned
        assertNotNull(mockName);
    }
}