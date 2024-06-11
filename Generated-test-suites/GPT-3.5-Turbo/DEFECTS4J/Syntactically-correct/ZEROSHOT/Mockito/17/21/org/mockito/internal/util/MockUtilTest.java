import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class MockUtilTest {
    
    private MockUtil mockUtil;
    private CreationValidator creationValidator;
    
    @Before
    public void setup() {
        creationValidator = mock(CreationValidator.class);
        mockUtil = new MockUtil(creationValidator);
    }
    
    @Test
    public void testCreateMock() {
        Class<TestObject> classToMock = TestObject.class;
        MockSettingsImpl settings = new MockSettingsImpl();
        
        TestObject mock = mockUtil.createMock(classToMock, settings);
        
        Assert.assertNotNull(mock);
        verify(creationValidator).validateType(classToMock);
        verify(creationValidator).validateExtraInterfaces(classToMock, settings.getExtraInterfaces());
        verify(creationValidator).validateMockedType(classToMock, settings.getSpiedInstance());
    }
    
    @Test
    public void testResetMock() {
        TestObject mock = mock(TestObject.class);
        
        mockUtil.resetMock(mock);
        
        verify(mock, times(1)).setCallback(eq(0), any(MethodInterceptorFilter.class));
    }
    
    @Test
    public void testGetMockHandler() {
        TestObject mock = mock(TestObject.class);
        MockHandlerInterface<TestObject> mockHandler = mock(MockHandlerInterface.class);
        when(mockUtil.getInterceptor(mock)).thenReturn(mockHandler);
        
        MockHandlerInterface<TestObject> result = mockUtil.getMockHandler(mock);
        
        Assert.assertEquals(mockHandler, result);
        verify(mockUtil).isMockitoMock(mock);
        verify(mockUtil).getInterceptor(mock);
    }
    
    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerWithNullMock() {
        TestObject mock = null;
        
        mockUtil.getMockHandler(mock);
    }
    
    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerWithNonMockitoMock() {
        TestObject mock = new TestObject();
        
        mockUtil.getMockHandler(mock);
    }
    
    @Test
    public void testIsMock() {
        TestObject mock = mock(TestObject.class);
        
        boolean result = mockUtil.isMock(mock);
        
        Assert.assertTrue(result);
        verify(mockUtil).isMockitoMock(mock);
    }
    
    @Test
    public void testIsMockWithNullMock() {
        TestObject mock = null;
        
        boolean result = mockUtil.isMock(mock);
        
        Assert.assertFalse(result);
        verify(mockUtil, never()).isMockitoMock(mock);
    }
    
    @Test
    public void testGetMockName() {
        TestObject mock = mock(TestObject.class);
        MockHandlerInterface<TestObject> mockHandler = mock(MockHandlerInterface.class);
        MockSettingsImpl settings = mock(MockSettingsImpl.class);
        MockName mockName = mock(MockName.class);
        when(mockUtil.getMockHandler(mock)).thenReturn(mockHandler);
        when(mockHandler.getMockSettings()).thenReturn(settings);
        when(settings.getMockName()).thenReturn(mockName);
        
        MockName result = mockUtil.getMockName(mock);
        
        Assert.assertEquals(mockName, result);
        verify(mockUtil).getMockHandler(mock);
        verify(mockHandler).getMockSettings();
        verify(settings).getMockName();
    }
}

class TestObject {}