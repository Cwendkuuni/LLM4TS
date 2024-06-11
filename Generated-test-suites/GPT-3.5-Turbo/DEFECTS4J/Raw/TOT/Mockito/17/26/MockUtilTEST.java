import static org.mockito.Mockito.*;

import org.junit.Test;

public class MockUtilTest {

    @Test
    public void createMock_shouldCreateMock() {
        MockUtil mockUtil = new MockUtil();
        ClassToMock classToMock = mockUtil.createMock(ClassToMock.class, new MockSettingsImpl());
        assertNotNull(classToMock);
    }
    
    @Test(expected = AssertionError.class)
    public void createMock_withInvalidClass_shouldThrowAssertionError() {
        MockUtil mockUtil = new MockUtil();
        mockUtil.createMock(InvalidClassToMock.class, new MockSettingsImpl());
    }
    
    @Test
    public void createMock_withExtraInterfaces_shouldCreateMock() {
        MockUtil mockUtil = new MockUtil();
        ClassToMock classToMock = mockUtil.createMock(ClassToMock.class, new MockSettingsImpl().extraInterfaces(ExtraInterface.class));
        assertNotNull(classToMock);
    }
    
    @Test
    public void createMock_withSpiedInstance_shouldCreateMock() {
        MockUtil mockUtil = new MockUtil();
        ClassToMock spiedInstance = new ClassToMock();
        ClassToMock classToMock = mockUtil.createMock(ClassToMock.class, new MockSettingsImpl().spiedInstance(spiedInstance));
        assertNotNull(classToMock);
    }
    
    @Test
    public void resetMock_shouldResetMock() {
        MockUtil mockUtil = new MockUtil();
        ClassToMock classToMock = mock(ClassToMock.class);
        mockUtil.resetMock(classToMock);
        verify(classToMock).reset();
    }
    
    @Test(expected = NotAMockException.class)
    public void resetMock_withNullMock_shouldThrowNotAMockException() {
        MockUtil mockUtil = new MockUtil();
        mockUtil.resetMock(null);
    }
    
    @Test
    public void getMockHandler_shouldReturnMockHandlerInterface() {
        MockUtil mockUtil = new MockUtil();
        ClassToMock classToMock = mock(ClassToMock.class);
        MockHandlerInterface<ClassToMock> mockHandler = mockUtil.getMockHandler(classToMock);
        assertNotNull(mockHandler);
    }
    
    @Test(expected = NotAMockException.class)
    public void getMockHandler_withInvalidMock_shouldThrowNotAMockException() {
        MockUtil mockUtil = new MockUtil();
        ClassToMock classToMock = new ClassToMock();
        mockUtil.getMockHandler(classToMock);
    }
    
    @Test
    public void isMock_withMockitoMock_shouldReturnTrue() {
        MockUtil mockUtil = new MockUtil();
        ClassToMock classToMock = mock(ClassToMock.class);
        boolean isMock = mockUtil.isMock(classToMock);
        assertTrue(isMock);
    }
    
    @Test
    public void isMock_withNonMockitoMock_shouldReturnFalse() {
        MockUtil mockUtil = new MockUtil();
        ClassToMock classToMock = new ClassToMock();
        boolean isMock = mockUtil.isMock(classToMock);
        assertFalse(isMock);
    }
    
    @Test
    public void getMockName_shouldReturnMockName() {
        MockUtil mockUtil = new MockUtil();
        ClassToMock classToMock = mock(ClassToMock.class);
        MockName mockName = mockUtil.getMockName(classToMock);
        assertNotNull(mockName);
    }
}