
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.exceptions.base.MockitoException;
import org.mockito.internal.util.MockName;

import static org.junit.Assert.*;

public class MockUtilTest {

    @Test
    public void testCreateMock_ValidClass_ValidSettings_ReturnsMockObject() {
        MockUtil mockUtil = new MockUtil();
        
        // Test case 1: Typical use case
        MyInterface mock = mockUtil.createMock(MyInterface.class, withSettings());
        assertNotNull(mock);
        assertTrue(Mockito.mockingDetails(mock).isMock());
        
        // Test case 2: edge case with extra interfaces
        MyInterface mockWithExtraInterfaces = mockUtil.createMock(MyInterface.class, withSettings().extraInterfaces(ExtraInterface.class));
        assertNotNull(mockWithExtraInterfaces);
        assertTrue(Mockito.mockingDetails(mockWithExtraInterfaces).isMock());
        assertTrue(mockWithExtraInterfaces instanceof ExtraInterface);
        
        // Test case 3: edge case with spied instance
        MyInterface spiedInstance = Mockito.spy(new MyInterfaceImpl());
        MyInterface mockedSpiedInstance = mockUtil.createMock(MyInterface.class, withSettings().spiedInstance(spiedInstance));
        assertNotNull(mockedSpiedInstance);
        assertTrue(Mockito.mockingDetails(mockedSpiedInstance).isMock());
        assertTrue(Mockito.mockingDetails(mockedSpiedInstance).isSpy());
        assertSame(spiedInstance, Mockito.mockingDetails(mockedSpiedInstance).getMockedInstance());
    }

    @Test(expected = MockitoException.class)
    public void testCreateMock_InvalidClass_ThrowsMockitoException() {
        MockUtil mockUtil = new MockUtil();
        
        // Test case 1: Invalid class to mock
        mockUtil.createMock(Object.class, withSettings());
        fail("Should have thrown MockitoException");
    }

    @Test(expected = MockitoException.class)
    public void testCreateMock_InvalidExtraInterfaces_ThrowsMockitoException() {
        MockUtil mockUtil = new MockUtil();
        
        // Test case 1: Invalid extra interfaces
        mockUtil.createMock(MyInterface.class, withSettings().extraInterfaces(Class.class));
        fail("Should have thrown MockitoException");
    }

    @Test(expected = MockitoException.class)
    public void testCreateMock_InvalidSpiedInstance_ThrowsMockitoException() {
        MockUtil mockUtil = new MockUtil();
        
        // Test case 1: Invalid spied instance
        mockUtil.createMock(MyInterface.class, withSettings().spiedInstance(new Object()));
        fail("Should have thrown MockitoException");
    }

    @Test
    public void testResetMock_ValidMock_MockReset() {
        MockUtil mockUtil = new MockUtil();
        
        // Test case 1: Valid mock object
        MyInterface mock = Mockito.mock(MyInterface.class);
        assertNotNull(mock);
        assertTrue(Mockito.mockingDetails(mock).isMock());
        mockUtil.resetMock(mock);
        assertFalse(Mockito.mockingDetails(mock).isMock());
    }

    @Test(expected = NotAMockException.class)
    public void testResetMock_NullMock_ThrowsNotAMockException() {
        MockUtil mockUtil = new MockUtil();
        
        // Test case 1: Null mock object
        mockUtil.resetMock(null);
        fail("Should have thrown NotAMockException");
    }

    @Test
    public void testGetMockHandler_ValidMock_ReturnsMockHandlerInterface() {
        MockUtil mockUtil = new MockUtil();
        
        // Test case 1: Valid mock object
        MyInterface mock = Mockito.mock(MyInterface.class);
        assertNotNull(mock);
        assertTrue(Mockito.mockingDetails(mock).isMock());
        MockHandlerInterface<MyInterface> mockHandler = mockUtil.getMockHandler(mock);
        assertNotNull(mockHandler);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandler_NullMock_ThrowsNotAMockException() {
        MockUtil mockUtil = new MockUtil();
        
        // Test case 1: Null mock object
        mockUtil.getMockHandler(null);
        fail("Should have thrown NotAMockException");
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandler_NonMockObject_ThrowsNotAMockException() {
        MockUtil mockUtil = new MockUtil();
        
        // Test case 1: Non-mock object
        mockUtil.getMockHandler(new Object());
        fail("Should have thrown NotAMockException");
    }

    @Test
    public void testIsMock_ValidMock_ReturnsTrue() {
        MockUtil mockUtil = new MockUtil();
        
        // Test case 1: Valid mock object
        MyInterface mock = Mockito.mock(MyInterface.class);
        assertNotNull(mock);
        assertTrue(Mockito.mockingDetails(mock).isMock());
        assertTrue(mockUtil.isMock(mock));
    }

    @Test
    public void testIsMock_NullMock_ReturnsFalse() {
        MockUtil mockUtil = new MockUtil();
        
        // Test case 1: Null mock object
        assertFalse(mockUtil.isMock(null));
    }

    @Test
    public void testIsMock_NonMockObject_ReturnsFalse() {
        MockUtil mockUtil = new MockUtil();
        
        // Test case 1: Non-mock object
        assertFalse(mockUtil.isMock(new Object()));
    }

    @Test
    public void testGetMockName_ValidMock_ReturnsMockName() {
        MockUtil mockUtil = new MockUtil();
        
        // Test case 1: Valid mock object
        MyInterface mock = Mockito.mock(MyInterface.class, withSettings().name("mockName"));
        assertNotNull(mock);
        assertTrue(Mockito.mockingDetails(mock).isMock());
        MockName mockName = mockUtil.getMockName(mock);
        assertNotNull(mockName);
        assertEquals("mockName", mockName.toString());
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockName_NullMock_ThrowsNotAMockException() {
        MockUtil mockUtil = new MockUtil();
        
        // Test case 1: Null mock object
        mockUtil.getMockName(null);
        fail("Should have thrown NotAMockException");
    }
    
    interface MyInterface {}
    interface ExtraInterface {}
    class MyInterfaceImpl implements MyInterface {}
}
