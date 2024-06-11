import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MockUtilTest {

    private MockUtil mockUtil;
    
    @Before
    public void setUp() {
        mockUtil = new MockUtil();
    }
    
    @Test
    public void testCreateMock_typicalUseCase() {
        // Arrange
        ClassToMock classToMock = mockUtil.createMock(ClassToMock.class, new MockSettingsImpl());
        
        // Act
        
        // Assert
        assertNotNull(classToMock);
        assertTrue(classToMock instanceof ClassToMock);
    }
    
    @Test
    public void testCreateMock_extraInterfaces() {
        // Arrange
        ClassToMock classToMock = mockUtil.createMock(ClassToMock.class, new MockSettingsImpl().extraInterfaces(ExtraInterface1.class, ExtraInterface2.class));
        
        // Act
        
        // Assert
        assertNotNull(classToMock);
        assertTrue(classToMock instanceof ClassToMock);
        assertTrue(classToMock instanceof ExtraInterface1);
        assertTrue(classToMock instanceof ExtraInterface2);
    }
    
    @Test
    public void testCreateMock_spiedInstance() {
        // Arrange
        ClassToMock spiedInstance = new ClassToMock();
        ClassToMock classToMock = mockUtil.createMock(ClassToMock.class, new MockSettingsImpl().spiedInstance(spiedInstance));
        
        // Act
        
        // Assert
        assertNotNull(classToMock);
        assertTrue(classToMock instanceof ClassToMock);
        assertEquals(spiedInstance, ((ClassToMock)classToMock).getSpiedInstance());
    }
    
    @Test
    public void testResetMock_mock() {
        // Arrange
        ClassToMock classToMock = mockUtil.createMock(ClassToMock.class, new MockSettingsImpl());
        classToMock.someMethod();
        
        // Act
        mockUtil.resetMock(classToMock);
        
        // Assert
        verifyZeroInteractions(classToMock);
    }
    
    @Test(expected = NotAMockException.class)
    public void testResetMock_nonMockArgument() {
        // Arrange
        ClassToMock classToMock = new ClassToMock();
        
        // Act
        mockUtil.resetMock(classToMock);
        
        // Assert
        // NotAMockException is expected
    }
    
    @Test
    public void testGetMockHandler_mock() {
        // Arrange
        ClassToMock classToMock = mockUtil.createMock(ClassToMock.class, new MockSettingsImpl());
        
        // Act
        MockHandlerInterface<ClassToMock> mockHandler = mockUtil.getMockHandler(classToMock);
        
        // Assert
        assertNotNull(mockHandler);
        assertTrue(mockHandler instanceof MockHandler);
    }
    
    @Test(expected = NotAMockException.class)
    public void testGetMockHandler_nullArgument() {
        // Arrange
        ClassToMock classToMock = null;
        
        // Act
        mockUtil.getMockHandler(classToMock);
        
        // Assert
        // NotAMockException is expected
    }
    
    @Test(expected = NotAMockException.class)
    public void testGetMockHandler_nonMockArgument() {
        // Arrange
        ClassToMock classToMock = new ClassToMock();
        
        // Act
        mockUtil.getMockHandler(classToMock);
        
        // Assert
        // NotAMockException is expected
    }
    
    @Test
    public void testIsMock_mock() {
        // Arrange
        ClassToMock classToMock = mockUtil.createMock(ClassToMock.class, new MockSettingsImpl());
        
        // Act
        boolean isMock = mockUtil.isMock(classToMock);
        
        // Assert
        assertTrue(isMock);
    }
    
    @Test
    public void testIsMock_nonMockArgument() {
        // Arrange
        ClassToMock classToMock = new ClassToMock();
        
        // Act
        boolean isMock = mockUtil.isMock(classToMock);
        
        // Assert
        assertFalse(isMock);
    }
    
    @Test
    public void testGetMockName_mock() {
        // Arrange
        ClassToMock classToMock = mockUtil.createMock(ClassToMock.class, new MockSettingsImpl().name("MockName"));
        
        // Act
        MockName mockName = mockUtil.getMockName(classToMock);
        
        // Assert
        assertNotNull(mockName);
        assertEquals("MockName", mockName.toString());
    }
    
    @Test(expected = NotAMockException.class)
    public void testGetMockName_nullArgument() {
        // Arrange
        ClassToMock classToMock = null;
        
        // Act
        mockUtil.getMockName(classToMock);
        
        // Assert
        // NotAMockException is expected
    }
    
    @Test(expected = NotAMockException.class)
    public void testGetMockName_nonMockArgument() {
        // Arrange
        ClassToMock classToMock = new ClassToMock();
        
        // Act
        mockUtil.getMockName(classToMock);
        
        // Assert
        // NotAMockException is expected
    }
}