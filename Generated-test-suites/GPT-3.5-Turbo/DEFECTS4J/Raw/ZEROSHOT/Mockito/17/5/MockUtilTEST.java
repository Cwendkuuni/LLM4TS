import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.cglib.proxy.Factory;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.util.reflection.LenientCopyTool;

public class MockUtilTest {
    
    private MockUtil mockUtil;
    
    @Before
    public void setUp() {
        mockUtil = new MockUtil();
    }
    
    @Test
    public void testCreateMock() {
        Class<TestObject> classToMock = TestObject.class;
        MockSettingsImpl settings = new MockSettingsImpl();
        
        TestObject mock = mockUtil.createMock(classToMock, settings);
        
        assertNotNull(mock);
        assertTrue(mockUtil.isMock(mock));
        assertTrue(mockUtil.getMockHandler(mock) instanceof MockHandlerInterface);
        assertTrue(mockUtil.getMockHandler(mock).getMockSettings() instanceof MockSettingsImpl);
        assertEquals(classToMock, mock.getClass());
    }
    
    @Test(expected = NotAMockException.class)
    public void testCreateMockWithNullClassToMock() {
        Class<TestObject> classToMock = null;
        MockSettingsImpl settings = new MockSettingsImpl();
        
        mockUtil.createMock(classToMock, settings);
    }
    
    @Test
    public void testCreateMockWithExtraInterfaces() {
        Class<TestObject> classToMock = TestObject.class;
        Class<?>[] extraInterfaces = {ExtraInterface.class};
        MockSettingsImpl settings = new MockSettingsImpl().extraInterfaces(extraInterfaces);
        
        TestObject mock = mockUtil.createMock(classToMock, settings);
        
        assertNotNull(mock);
        assertTrue(mockUtil.isMock(mock));
        assertTrue(mockUtil.getMockHandler(mock) instanceof MockHandlerInterface);
        assertTrue(mockUtil.getMockHandler(mock).getMockSettings() instanceof MockSettingsImpl);
        assertEquals(classToMock, mock.getClass());
    }
    
    @Test
    public void testCreateMockWithSpiedInstance() {
        Class<TestObject> classToMock = TestObject.class;
        TestObject spiedInstance = new TestObject();
        MockSettingsImpl settings = new MockSettingsImpl().spiedInstance(spiedInstance);
        
        TestObject mock = mockUtil.createMock(classToMock, settings);
        
        assertNotNull(mock);
        assertTrue(mockUtil.isMock(mock));
        assertTrue(mockUtil.getMockHandler(mock) instanceof MockHandlerInterface);
        assertTrue(mockUtil.getMockHandler(mock).getMockSettings() instanceof MockSettingsImpl);
        assertEquals(classToMock, mock.getClass());
    }
    
    @Test
    public void testResetMock() {
        TestObject mock = mock(TestObject.class);
        
        mockUtil.resetMock(mock);
        
        assertNotNull(mock);
        assertTrue(mockUtil.isMock(mock));
        assertTrue(mockUtil.getMockHandler(mock) instanceof MockHandlerInterface);
        assertTrue(mockUtil.getMockHandler(mock).getMockSettings() instanceof MockSettingsImpl);
    }
    
    @Test(expected = NotAMockException.class)
    public void testResetMockWithNullMock() {
        TestObject mock = null;
        
        mockUtil.resetMock(mock);
    }
    
    @Test
    public void testGetMockHandler() {
        TestObject mock = mock(TestObject.class);
        
        MockHandlerInterface<TestObject> handler = mockUtil.getMockHandler(mock);
        
        assertNotNull(handler);
        assertEquals(mockUtil.getMockHandler(mock), handler);
    }
    
    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerWithNullMock() {
        TestObject mock = null;
        
        mockUtil.getMockHandler(mock);
    }
    
    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerWithNonMockClass() {
        TestObject mock = new TestObject();
        
        mockUtil.getMockHandler(mock);
    }
    
    @Test
    public void testIsMock() {
        TestObject mock = mock(TestObject.class);
        
        assertTrue(mockUtil.isMock(mock));
    }
    
    @Test
    public void testIsMockWithNonMockObject() {
        Object nonMock = new Object();
        
        assertFalse(mockUtil.isMock(nonMock));
    }
    
    @Test
    public void testGetMockName() {
        TestObject mock = mock(TestObject.class);
        
        MockName mockName = mockUtil.getMockName(mock);
        
        assertNotNull(mockName);
        assertEquals(mockSettings.getMockName(), mockName);
    }
}