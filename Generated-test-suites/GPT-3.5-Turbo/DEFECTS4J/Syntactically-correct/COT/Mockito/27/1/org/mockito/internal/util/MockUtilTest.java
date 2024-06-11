
import org.junit.Test;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.util.MockCreationValidator;
import org.mockito.internal.util.MockHandlerInterface;
import org.mockito.internal.util.MockName;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.creation.jmock.ClassImposterizer;

import java.io.Serializable;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MockUtilTest {

    @Test
    public void testCreateMock() {
        MockUtil mockUtil = new MockUtil();
        MockSettingsImpl settings = new MockSettingsImpl();
        Class<?> classToMock = Object.class;
        Object mock = mockUtil.createMock(classToMock, settings);
        
        assertNotNull(mock);
        assertTrue(mock instanceof Object);
    }
    
    @Test
    public void testResetMock() {
        MockUtil mockUtil = new MockUtil();
        Object mock = mock(Object.class);
        
        mockUtil.resetMock(mock);
        
        MockHandlerInterface<?> handler = mockUtil.getMockHandler(mock);
        assertNotNull(handler);
        assertEquals(mock, handler.getMockSettings().getMock());
    }
    
    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerShouldThrowExceptionWhenArgumentIsNull() {
        MockUtil mockUtil = new MockUtil();
        mockUtil.getMockHandler(null);
    }
    
    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerShouldThrowExceptionWhenArgumentIsNotAMock() {
        MockUtil mockUtil = new MockUtil();
        Object object = new Object();
        
        mockUtil.getMockHandler(object);
    }
    
    @Test
    public void testIsMockShouldReturnFalseWhenArgumentIsNull() {
        MockUtil mockUtil = new MockUtil();
        
        assertFalse(mockUtil.isMock(null));
    }
    
    @Test
    public void testIsMockShouldReturnFalseWhenArgumentIsNotAMock() {
        MockUtil mockUtil = new MockUtil();
        Object object = new Object();
        
        assertFalse(mockUtil.isMock(object));
    }

    @Test
    public void testGetMockName() {
        MockUtil mockUtil = new MockUtil();
        Object mock = mock(Object.class);
        MockName mockName = new MockName("testMock");
        when(mockUtil.getMockHandler(mock).getMockSettings().getMockName()).thenReturn(mockName);
        
        MockName result = mockUtil.getMockName(mock);
        
        assertEquals(mockName, result);
    }
    
    @Test
    public void testCreateMockWithSerializableSettings() {
        MockUtil mockUtil = new MockUtil();
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.serializable(true);
        Class<?> classToMock = Object.class;
        Object mock = mockUtil.createMock(classToMock, settings);
        
        assertNotNull(mock);
        assertTrue(mock instanceof Object);
        assertTrue(mock instanceof Serializable);
    }

    @Test
    public void testCreateMockWithSpiedInstanceSettings() {
        MockUtil mockUtil = new MockUtil();
        MockSettingsImpl settings = new MockSettingsImpl();
        Object spiedInstance = new Object();
        settings.spiedInstance(spiedInstance);
        Class<?> classToMock = Object.class;
        Object mock = mockUtil.createMock(classToMock, settings);

        assertNotNull(mock);
        assertTrue(mock instanceof Object);
        assertSame(spiedInstance, mockUtil.getMockHandler(mock).getMockSettings().getSpiedInstance());
    }

    @Test
    public void testCreateMockWithExtraInterfacesSettings() {
        MockUtil mockUtil = new MockUtil();
        MockSettingsImpl settings = new MockSettingsImpl();
        Class<?>[] extraInterfaces = {Serializable.class};
        settings.extraInterfaces(extraInterfaces);
        Class<?> classToMock = Object.class;
        Object mock = mockUtil.createMock(classToMock, settings);

        assertNotNull(mock);
        assertTrue(mock instanceof Object);
        assertTrue(mock instanceof Serializable);
    }
    
    @Test(expected = NotAMockException.class)
    public void testResetMockShouldThrowExceptionWhenArgumentIsNotAMock() {
        MockUtil mockUtil = new MockUtil();
        Object object = new Object();
        
        mockUtil.resetMock(object);
    }
    
    @Test
    public void testGetInterceptorShouldReturnNullWhenArgumentIsNotAMock() {
        MockUtil mockUtil = new MockUtil();
        Object object = new Object();
        
        assertNull(mockUtil.getInterceptor(object));
    }
    
    @Test
    public void testGetInterceptorShouldReturnMethodInterceptorFilterWhenArgumentIsAMock() {
        MockUtil mockUtil = new MockUtil();
        Object mock = mock(Object.class);
        
        assertNotNull(mockUtil.getInterceptor(mock));
    }
}