
import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.util.reflection.LenientCopyTool;

import java.io.Serializable;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MockUtilTest {

    private MockUtil mockUtil;

    @Before
    public void setUp() {
        mockUtil = new MockUtil();
    }

    @Test
    public void testCreateMock() {
        // Test basic functionality of createMock
        Class<String> classToMock = String.class;
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.setExtraInterfaces(new Class<?>[] {Serializable.class});
        Object mock = mockUtil.createMock(classToMock, settings);

        assertNotNull(mock);
        assertTrue(classToMock.isInstance(mock));
    }

    @Test
    public void testCreateMockWithSpiedInstance() {
        // Test createMock with spiedInstance
        Class<String> classToMock = String.class;
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.setSpiedInstance("Hello");
        Object mock = mockUtil.createMock(classToMock, settings);

        assertNotNull(mock);
        assertTrue(classToMock.isInstance(mock));
        assertEquals("Hello", mock);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerWithNullMock() {
        // Test getMockHandler with null mock
        Object mock = null;
        mockUtil.getMockHandler(mock);
    }

    @Test(expected = NotAMockException.class)
    public void testGetMockHandlerWithInvalidMock() {
        // Test getMockHandler with invalid mock
        Object mock = "Hello";
        mockUtil.getMockHandler(mock);
    }

    @Test
    public void testGetMockHandlerWithValidMock() {
        // Test getMockHandler with valid mock
        Class<String> classToMock = String.class;
        MockSettingsImpl settings = new MockSettingsImpl();
        Object mock = mockUtil.createMock(classToMock, settings);

        assertNotNull(mock);
        assertEquals(MockHandler.class, mockUtil.getMockHandler(mock).getClass());
    }

    @Test
    public void testIsMockWithNullMock() {
        // Test isMock with null mock
        Object mock = null;
        assertFalse(mockUtil.isMock(mock));
    }

    @Test
    public void testIsMockWithInvalidMock() {
        // Test isMock with invalid mock
        Object mock = "Hello";
        assertFalse(mockUtil.isMock(mock));
    }

    @Test
    public void testIsMockWithValidMock() {
        // Test isMock with valid mock
        Class<String> classToMock = String.class;
        MockSettingsImpl settings = new MockSettingsImpl();
        Object mock = mockUtil.createMock(classToMock, settings);

        assertTrue(mockUtil.isMock(mock));
    }

    @Test
    public void testResetMock() {
        // Test basic functionality of resetMock
        Class<String> classToMock = String.class;
        MockSettingsImpl settings = new MockSettingsImpl();
        Object mock = mockUtil.createMock(classToMock, settings);

        mockUtil.resetMock(mock);

        verify(mock, times(1)).toString();
    }

    @Test
    public void testGetMockName() {
        // Test basic functionality of getMockName
        Class<String> classToMock = String.class;
        MockSettingsImpl settings = new MockSettingsImpl();
        Object mock = mockUtil.createMock(classToMock, settings);

        assertNotNull(mockUtil.getMockName(mock));
    }

    @Test
    public void testGetMockNameWithNullMock() {
        // Test getMockName with null mock
        Object mock = null;
        assertNull(mockUtil.getMockName(mock));
    }

    @Test
    public void testGetMockNameWithInvalidMock() {
        // Test getMockName with invalid mock
        Object mock = "Hello";
        assertNull(mockUtil.getMockName(mock));
    }
}
