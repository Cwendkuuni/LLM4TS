import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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
    public void testCreateMock() {
        Class<Object> classToMock = Object.class;
        MockSettingsImpl settings = new MockSettingsImpl();
        
        Object mock = mockUtil.createMock(classToMock, settings);
        
        assertEquals(classToMock, mock.getClass());
    }
    
    @Test
    public void testResetMock() {
        Object mock = ClassImposterizer.INSTANCE.imposterise(new MethodInterceptorFilter(new InvocationNotifierHandler(), new MockSettingsImpl()), Object.class, new Class<?>[0]);
        
        mockUtil.resetMock(mock);
        
        MockHandlerInterface<Object> mockHandler = mockUtil.getMockHandler(mock);
        MockHandler<Object> newMockHandler = new MockHandler<Object>(mockHandler);
        MethodInterceptorFilter newFilter = new MethodInterceptorFilter(newMockHandler, (MockSettingsImpl) org.mockito.Mockito.withSettings().defaultAnswer(org.mockito.Mockito.RETURNS_DEFAULTS));
        assertEquals(newFilter, ((Factory) mock).getCallback(0));
    }
    
    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerWithNullMock() {
        mockUtil.getMockHandler(null);
    }
    
    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerWithNonMockitoMock() {
        mockUtil.getMockHandler(new Object());
    }
    
    @Test
    public void testGetMockHandlerWithMockitoMock() {
        Object mock = ClassImposterizer.INSTANCE.imposterise(new MethodInterceptorFilter(new InvocationNotifierHandler(), new MockSettingsImpl()), Object.class, new Class<?>[0]);
        
        MockHandlerInterface<Object> mockHandler = mockUtil.getMockHandler(mock);
        
        assertEquals(mockHandler, ((MethodInterceptorFilter) ((Factory) mock).getCallback(0)).getHandler());
    }
    
    @Test
    public void testIsMockWithNullMock() {
        boolean isMock = mockUtil.isMock(null);
        
        assertFalse(isMock);
    }
    
    @Test
    public void testIsMockWithNonMockitoMock() {
        boolean isMock = mockUtil.isMock(new Object());
        
        assertFalse(isMock);
    }
    
    @Test
    public void testIsMockWithMockitoMock() {
        Object mock = ClassImposterizer.INSTANCE.imposterise(new MethodInterceptorFilter(new InvocationNotifierHandler(), new MockSettingsImpl()), Object.class, new Class<?>[0]);
        
        boolean isMock = mockUtil.isMock(mock);
        
        assertTrue(isMock);
    }
    
    @Test
    public void testGetMockName() {
        Object mock = ClassImposterizer.INSTANCE.imposterise(new MethodInterceptorFilter(new InvocationNotifierHandler(), new MockSettingsImpl()), Object.class, new Class<?>[0]);
        
        MockName mockName = mockUtil.getMockName(mock);
        
        assertNull(mockName);
    }
}