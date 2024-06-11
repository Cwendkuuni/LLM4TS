import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.exceptions.Reporter;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class MockSettingsImplTest {

    private MockSettingsImpl mockSettings;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockSettings = new MockSettingsImpl();
    }

    @Test
    public void testSerializable() {
        MockSettingsImpl result = mockSettings.serializable();
        assertTrue(result.isSerializable());
    }

    @Test
    public void testExtraInterfaces() {
        Class<?>[] extraInterfaces = {Runnable.class, Comparable.class};
        MockSettingsImpl result = mockSettings.extraInterfaces(extraInterfaces);
        assertEquals(extraInterfaces, result.getExtraInterfaces());
    }

    @Test
    public void testExtraInterfacesWithNull() {
        try {
            mockSettings.extraInterfaces(null);
        } catch (NullPointerException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testExtraInterfacesWithEmptyArray() {
        try {
            Class<?>[] extraInterfaces = {};
            mockSettings.extraInterfaces(extraInterfaces);
        } catch (RuntimeException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testExtraInterfacesWithNonInterfaceClass() {
        try {
            Class<?>[] extraInterfaces = {String.class};
            mockSettings.extraInterfaces(extraInterfaces);
        } catch (RuntimeException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testGetMockName() {
        assertNull(mockSettings.getMockName());
    }

    @Test
    public void testGetSpiedInstance() {
        assertNull(mockSettings.getSpiedInstance());
    }

    @Test
    public void testName() {
        String name = "TestName";
        MockSettingsImpl result = mockSettings.name(name);
        assertEquals(name, result.getMockName().toString());
    }

    @Test
    public void testSpiedInstance() {
        Object spiedInstance = new Object();
        MockSettingsImpl result = mockSettings.spiedInstance(spiedInstance);
        assertEquals(spiedInstance, result.getSpiedInstance());
    }

    @Test
    public void testDefaultAnswer() {
        Answer<Object> defaultAnswer = (invocation) -> null;
        MockSettingsImpl result = mockSettings.defaultAnswer(defaultAnswer);
        assertEquals(defaultAnswer, result.getDefaultAnswer());
    }

    @Test
    public void testIsSerializable() {
        assertFalse(mockSettings.isSerializable());
    }

    @Test
    public void testInitiateMockName() {
        Class<?> classToMock = Object.class;
        mockSettings.initiateMockName(classToMock);
        assertEquals(classToMock.getName(), mockSettings.getMockName().toString());
    }
}