import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.util.MockName;

public class MockSettingsImplTest {

    private MockSettingsImpl mockSettings;

    @Before
    public void setUp() {
        mockSettings = new MockSettingsImpl();
    }

    @Test
    public void testSerializable() {
        MockSettings result = mockSettings.serializable();
        assertTrue(java.util.Arrays.asList(result.getExtraInterfaces()).contains(java.io.Serializable.class));
    }

    @Test
    public void testExtraInterfaces_NullInput() {
        Reporter reporter = new Reporter();
        try {
            mockSettings.extraInterfaces(null);
        } catch (MockitoException e) {
            assertEquals("Extra interfaces require at least one interface", e.getMessage());
        }
    }

    @Test
    public void testExtraInterfaces_NullElement() {
        Reporter reporter = new Reporter();
        try {
            mockSettings.extraInterfaces(String.class, null);
        } catch (MockitoException e) {
            assertEquals("Extra interfaces does not accept null parameters", e.getMessage());
        }
    }

    @Test
    public void testExtraInterfaces_NonInterfaceElement() {
        Reporter reporter = new Reporter();
        try {
            mockSettings.extraInterfaces(String.class);
        } catch (MockitoException e) {
            assertEquals("Extra interfaces accepts only interfaces", e.getMessage());
        }
    }

    @Test
    public void testGetName() {
        String name = "mockName";
        mockSettings.name(name);
        assertEquals(name, mockSettings.getMockName());
    }

    @Test
    public void testGetSpiedInstance() {
        Object spiedInstance = new Object();
        mockSettings.spiedInstance(spiedInstance);
        assertEquals(spiedInstance, mockSettings.getSpiedInstance());
    }

    @Test
    public void testDefaultAnswer() {
        Answer<Object> defaultAnswer = (invocation) -> null;
        mockSettings.defaultAnswer(defaultAnswer);
        assertEquals(defaultAnswer, mockSettings.getDefaultAnswer());
    }

    @Test
    public void testIsSerializable_True() {
        mockSettings.extraInterfaces(java.io.Serializable.class);
        assertTrue(mockSettings.isSerializable());
    }

    @Test
    public void testIsSerializable_False() {
        assertFalse(mockSettings.isSerializable());
    }

    @Test
    public void testInitiateMockName() {
        String name = "mockName";
        Class classToMock = String.class;
        mockSettings.name(name);
        mockSettings.initiateMockName(classToMock);
        assertEquals(new MockName(name, classToMock), mockSettings.getMockName());
    }

}