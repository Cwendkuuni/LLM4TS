
import org.junit.Test;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.util.reflection.LenientCopyTool;

import java.io.Serializable;

import static org.mockito.Mockito.*;

public class MockUtilTest {
    
    @Test
    public void testCreateMock() {
        MockCreationValidator creationValidator = mock(MockCreationValidator.class);
        MockUtil mockUtil = new MockUtil(creationValidator);
        
        Class<MyClass> classToMock = MyClass.class;
        MockSettingsImpl settings = new MockSettingsImpl();
        
        MyClass mock = mockUtil.createMock(classToMock, settings);
        
        // Verify creationValidator.validateType is called
        verify(creationValidator).validateType(classToMock);
        
        // Verify creationValidator.validateExtraInterfaces is called
        verify(creationValidator).validateExtraInterfaces(classToMock, settings.getExtraInterfaces());
        
        // Verify creationValidator.validateMockedType is called
        verify(creationValidator).validateMockedType(classToMock, settings.getSpiedInstance());
        
        // Verify settings.initiateMockName is called
        verify(settings).initiateMockName(classToMock);
        
        // Verify ClassImposterizer.INSTANCE.imposterise is called
        verify(ClassImposterizer.INSTANCE).imposterise(any(), eq(classToMock), any(Class[].class));
        
        // Verify call to new LenientCopyTool().copyToMock if spiedInstance is not null
        if (settings.getSpiedInstance() != null) {
            verify(new LenientCopyTool()).copyToMock(settings.getSpiedInstance(), mock);
        }
    }
    
    @Test
    public void testResetMock() {
        // Create a mock
        MyClass mock = mock(MyClass.class);
        
        // Reset the mock
        MockUtil mockUtil = new MockUtil();
        mockUtil.resetMock(mock);
        
        // Verify that the callback is set to a new MethodInterceptorFilter instance
        verify(((Factory) mock)).setCallback(eq(0), any(MethodInterceptorFilter.class));
    }
    
    @Test
    public void testGetMockHandler() {
        // Create a mock
        MyClass mock = mock(MyClass.class);
        
        // Get the mock handler
        MockUtil mockUtil = new MockUtil();
        MockHandlerInterface<MyClass> mockHandler = mockUtil.getMockHandler(mock);
        
        // Verify that mockHandler is returned, and getHandler() is called on the Mockito mock interceptor
        verify(mockHandler).getMockSettings();
        verify(mock).getCallback(eq(0));
    }
    
    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerWithNull() {
        // Call getMockHandler with null argument
        MockUtil mockUtil = new MockUtil();
        mockUtil.getMockHandler(null);
    }
    
    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerWithNonMockObject() {
        // Create a non-mock object
        MyClass nonMockObject = new MyClass();
        
        // Call getMockHandler with non-mock object
        MockUtil mockUtil = new MockUtil();
        mockUtil.getMockHandler(nonMockObject);
    }
    
    @Test
    public void testIsMock() {
        // Create a mock
        MyClass mock = mock(MyClass.class);
        
        // Check if the object is a mock
        MockUtil mockUtil = new MockUtil();
        boolean isMock = mockUtil.isMock(mock);
        
        // Verify that isMockitoMock is called
        verify(mockUtil).isMockitoMock(mock);
    }
    
    @Test
    public void testIsMockWithNull() {
        // Check if null is a mock
        MockUtil mockUtil = new MockUtil();
        boolean isMock = mockUtil.isMock(null);
        
        // Verify that isMockitoMock is not called
        verify(mockUtil, never()).isMockitoMock(null);
    }
    
    @Test
    public void testGetInterceptor() {
        // Create a mock
        MyClass mock = mock(MyClass.class);
        
        // Get the interceptor
        MockUtil mockUtil = new MockUtil();
        MethodInterceptorFilter interceptor = mockUtil.getInterceptor(mock);
        
        // Verify that getCallback is called on the Mockito mock object
        verify(mock).getCallback(eq(0));
    }
    
    @Test
    public void testGetInterceptorWithNonMockObject() {
        // Create a non-mock object
        MyClass nonMockObject = new MyClass();
        
        // Get the interceptor
        MockUtil mockUtil = new MockUtil();
        MethodInterceptorFilter interceptor = mockUtil.getInterceptor(nonMockObject);
        
        // Verify that getCallback is not called on the non-mock object
        verify(nonMockObject, never()).getCallback(eq(0));
    }
    
    @Test
    public void testGetMockName() {
        // Create a mock
        MyClass mock = mock(MyClass.class);
        
        // Get the mock name
        MockUtil mockUtil = new MockUtil();
        MockName mockName = mockUtil.getMockName(mock);
        
        // Verify that getMockSettings is called on the mock handler
        verify(mockUtil.getMockHandler(mock)).getMockSettings();
    }
    
    class MyClass {}
}
