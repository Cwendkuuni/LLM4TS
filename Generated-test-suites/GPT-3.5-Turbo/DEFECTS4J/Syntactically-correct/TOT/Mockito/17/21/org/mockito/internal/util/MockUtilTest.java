import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.creation.MethodInterceptorFilter;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.LenientCopyTool;

import java.lang.reflect.Field;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MockUtilTest {
    
    private MockUtil mockUtil;
    
    @Mock
    private CreationValidator creationValidatorMock;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockUtil = new MockUtil(creationValidatorMock);
    }

    @Test
    public void testCreateMock_validInput() {
        ClassToMockImpl classToMockImpl = mockUtil.createMock(ClassToMockImpl.class, new MockSettingsImpl());

        assertNotNull(classToMockImpl);
        assertTrue(classToMockImpl instanceof ClassToMockImpl);
        assertTrue(classToMockImpl instanceof ClassToMock);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateMock_invalidInput() {
        mockUtil.createMock(InvalidClassToMock.class, new MockSettingsImpl());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateMock_invalidExtraInterface() {
        ClassToMockImpl classToMockImpl = mock(ClassToMockImpl.class);
        mockUtil.createMock(ClassToMockImpl.class, new MockSettingsImpl().extraInterfaces(InvalidExtraInterface.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateMock_invalidSpiedInstance() {
        ClassToMockImpl classToMockImpl = mock(ClassToMockImpl.class);
        mockUtil.createMock(ClassToMockImpl.class, new MockSettingsImpl().spiedInstance(classToMockImpl));
    }

    @Test
    public void testResetMock_validMock() {
        ClassToMockImpl classToMockImpl = mock(ClassToMockImpl.class);
        MockHandlerInterface<ClassToMockImpl> oldMockHandler = mockUtil.getMockHandler(classToMockImpl);

        mockUtil.resetMock(classToMockImpl);

        MockHandlerInterface<ClassToMockImpl> newMockHandler = mockUtil.getMockHandler(classToMockImpl);
        assertNotSame(oldMockHandler, newMockHandler);
    }

    @Test(expected = NotAMockException.class)
    public void testResetMock_notAMock() {
        ClassToMockImpl classToMockImpl = new ClassToMockImpl();
        mockUtil.resetMock(classToMockImpl);
    }

    @Test
    public void testGetMockHandler_validMock() {
        ClassToMockImpl classToMockImpl = mock(ClassToMockImpl.class);
        MockHandlerInterface<ClassToMockImpl> mockHandler = mockUtil.getMockHandler(classToMockImpl);

        assertNotNull(mockHandler);
        assertTrue(mockHandler instanceof MockHandlerInterface);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandler_nullMock() {
        mockUtil.getMockHandler(null);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandler_notAMock() {
        ClassToMockImpl classToMockImpl = new ClassToMockImpl();
        mockUtil.getMockHandler(classToMockImpl);
    }

    @Test
    public void testIsMock_true() {
        ClassToMockImpl classToMockImpl = mock(ClassToMockImpl.class);
        assertTrue(mockUtil.isMock(classToMockImpl));
    }

    @Test
    public void testIsMock_false() {
        ClassToMockImpl classToMockImpl = new ClassToMockImpl();
        assertFalse(mockUtil.isMock(classToMockImpl));
    }

    @Test
    public void testGetMockName_validMock() {
        ClassToMockImpl classToMockImpl = mock(ClassToMockImpl.class);
        MockName mockName = mockUtil.getMockName(classToMockImpl);

        assertNotNull(mockName);
        assertEquals("", mockName.toString());
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockName_nullMock() {
        mockUtil.getMockName(null);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockName_notAMock() {
        ClassToMockImpl classToMockImpl = new ClassToMockImpl();
        mockUtil.getMockName(classToMockImpl);
    }
    
    private interface ClassToMock {}
    
    private class ClassToMockImpl implements ClassToMock {}
    
    private class InvalidClassToMock {}
    
    private interface InvalidExtraInterface {}

}