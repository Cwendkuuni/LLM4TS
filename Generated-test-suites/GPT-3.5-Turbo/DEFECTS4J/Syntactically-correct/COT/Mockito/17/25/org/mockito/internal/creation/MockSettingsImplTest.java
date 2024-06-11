import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.util.MockName;
import org.mockito.stubbing.Answer;

import java.util.Arrays;

import static org.junit.Assert.*;

public class MockSettingsImplTest {

    private MockSettingsImpl mockSettings;

    @Before
    public void setUp() {
        mockSettings = new MockSettingsImpl();
    }

    @Test
    public void testSerializable() {
        MockSettingsImpl result = mockSettings.serializable();
        assertTrue(result.isSerializable());
    }

    @Test
    public void testExtraInterfaces() {
        Class<?>[] extraInterfaces = {Runnable.class};
        MockSettingsImpl result = mockSettings.extraInterfaces(extraInterfaces);
        assertArrayEquals(extraInterfaces, result.getExtraInterfaces());
    }

    @Test
    public void testExtraInterfacesNull() {
        try {
            mockSettings.extraInterfaces(null);
            fail("Expected Reporter.extraInterfacesRequiresAtLeastOneInterface() to be called");
        } catch (Reporter reporter) {
            // Test passed
        }
    }

    @Test
    public void testExtraInterfacesEmpty() {
        try {
            mockSettings.extraInterfaces();
            fail("Expected Reporter.extraInterfacesRequiresAtLeastOneInterface() to be called");
        } catch (Reporter reporter) {
            // Test passed
        }
    }

    @Test
    public void testExtraInterfacesAcceptsOnlyInterfaces() {
        Class<?>[] extraInterfaces = {String.class};
        try {
            mockSettings.extraInterfaces(extraInterfaces);
            fail("Expected Reporter.extraInterfacesAcceptsOnlyInterfaces() to be called");
        } catch (Reporter reporter) {
            assertEquals(extraInterfaces[0], reporter.getArgument());
        }
    }

    @Test
    public void testGetMockName() {
        MockName mockName = new MockName("testName", MockSettingsImpl.class);
        mockSettings.name("testName");
        assertEquals(mockName, mockSettings.getMockName());
    }

    @Test
    public void testGetName() {
        String name = "testName";
        mockSettings.name(name);
        assertEquals(name, mockSettings.getMockName().toString());
    }

    @Test
    public void testGetExtraInterfaces() {
        Class<?>[] extraInterfaces = {Runnable.class};
        mockSettings.extraInterfaces(extraInterfaces);
        assertArrayEquals(extraInterfaces, mockSettings.getExtraInterfaces());
    }

    @Test
    public void testGetSpiedInstance() {
        Object spiedInstance = new Object();
        mockSettings.spiedInstance(spiedInstance);
        assertEquals(spiedInstance, mockSettings.getSpiedInstance());
    }

    @Test
    public void testDefaultAnswer() {
        Answer<Object> defaultAnswer = invocation -> null;
        mockSettings.defaultAnswer(defaultAnswer);
        assertEquals(defaultAnswer, mockSettings.getDefaultAnswer());
    }

    @Test
    public void testIsSerializable() {
        assertFalse(mockSettings.isSerializable());

        Class<?>[] extraInterfaces = {Runnable.class};
        mockSettings.extraInterfaces(extraInterfaces);
        assertTrue(mockSettings.isSerializable());
    }

    @Test
    public void testInitiateMockName() {
        Class<?> classToMock = MockSettingsImpl.class;
        mockSettings.initiateMockName(classToMock);
        assertEquals(new MockName(null, classToMock), mockSettings.getMockName());
    }
}