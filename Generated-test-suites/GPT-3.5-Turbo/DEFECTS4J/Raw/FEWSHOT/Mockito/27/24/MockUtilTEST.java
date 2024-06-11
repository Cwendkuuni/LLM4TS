
package org.mockito.internal.util;

import static org.mockito.Mockito.*;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.util.reflection.LenientCopyTool;

import java.io.Serializable;

public class MockUtilTest {

    private MockUtil mockUtil;

    @Before
    public void setUp() {
        mockUtil = new MockUtil();
    }

    @Test
    public void testCreateMock() {
        ClassToMock classToMock = mockUtil.createMock(ClassToMock.class, new MockSettingsImpl());
        assertNotNull(classToMock);
        assertTrue(classToMock instanceof ClassToMock);

        classToMock = mockUtil.createMock(ClassToMock.class, new MockSettingsImpl().serializable());
        assertNotNull(classToMock);
        assertTrue(classToMock instanceof Serializable);

        classToMock = mockUtil.createMock(ClassToMock.class, new MockSettingsImpl().extraInterfaces(ExtraInterface.class));
        assertNotNull(classToMock);
        assertTrue(classToMock instanceof ExtraInterface);

        classToMock = mockUtil.createMock(ClassToMock.class, new MockSettingsImpl().extraInterfaces(ExtraInterface.class).serializable());
        assertNotNull(classToMock);
        assertTrue(classToMock instanceof ExtraInterface);
        assertTrue(classToMock instanceof Serializable);
    }

    @Test
    public void testCreateMockWithSpiedInstance() {
        ClassToMock spiedInstance = new ClassToMock();

        ClassToMock classToMock = mockUtil.createMock(ClassToMock.class, new MockSettingsImpl().spiedInstance(spiedInstance));
        assertNotNull(classToMock);
        assertTrue(classToMock instanceof ClassToMock);

        LenientCopyTool lenientCopyTool = mock(LenientCopyTool.class);
        when(lenientCopyTool.copyToMock(eq(spiedInstance), any())).thenReturn(null);
        mockUtil = new MockUtil(new MockCreationValidator(lenientCopyTool));

        classToMock = mockUtil.createMock(ClassToMock.class, new MockSettingsImpl().spiedInstance(spiedInstance));
        assertNotNull(classToMock);
        assertTrue(classToMock instanceof ClassToMock);
        verify(lenientCopyTool).copyToMock(eq(spiedInstance), any());
    }

    @Test
    public void testResetMock() {
        ClassToMock mock = mock(ClassToMock.class);

        mockUtil.resetMock(mock);

        assertNotNull(mock);
        assertTrue(mock instanceof ClassToMock);
    }

    @Test(expected = NotAMockException.class)
    public void testResetNonMock() {
        ClassToMock nonMock = new ClassToMock();

        mockUtil.resetMock(nonMock);
    }

    @Test
    public void testGetMockHandler() {
        ClassToMock mock = mock(ClassToMock.class);
        MockHandlerInterface<ClassToMock> mockHandler = mockUtil.getMockHandler(mock);

        assertNotNull(mockHandler);
        assertTrue(mockHandler instanceof MockHandler);

        Assert.assertThrows(NotAMockException.class, () -> {
            ClassToMock nonMock = new ClassToMock();
            mockUtil.getMockHandler(nonMock);
        });
    }

    @Test
    public void testIsMock() {
        ClassToMock mock = mock(ClassToMock.class);
        assertTrue(mockUtil.isMock(mock));

        assertTrue(mockUtil.isMock(null));
    }

    @Test
    public void testGetMockName() {
        ClassToMock mock = mock(ClassToMock.class);
        assertNull(mockUtil.getMockName(mock));
    }

    private static class ClassToMock {
    }

    private interface ExtraInterface {
    }
}
