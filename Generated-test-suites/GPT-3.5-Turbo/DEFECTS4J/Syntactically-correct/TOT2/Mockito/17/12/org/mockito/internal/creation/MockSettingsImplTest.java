import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.util.MockName;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MockSettingsImplTest {

    private MockSettingsImpl mockSettings;

    @Before
    public void setup() {
        mockSettings = new MockSettingsImpl();
    }

    @Test
    public void testSerializable() {
        MockSettingsImpl result = mockSettings.serializable();
        assertNotNull(result);
    }

    @Test
    public void testExtraInterfaces_NormalCase() {
        Class<?> interface1 = Runnable.class;
        Class<?> interface2 = Cloneable.class;

        MockSettingsImpl result = mockSettings.extraInterfaces(interface1, interface2);
        assertArrayEquals(new Class[]{interface1, interface2}, result.getExtraInterfaces());
    }

    @Test(expected = Reporter.class)
    public void testExtraInterfaces_NoInterfaces() {
        mockSettings.extraInterfaces();
    }

    @Test(expected = Reporter.class)
    public void testExtraInterfaces_NullInterface() {
        mockSettings.extraInterfaces(null);
    }

    @Test(expected = Reporter.class)
    public void testExtraInterfaces_NonInterface() {
        mockSettings.extraInterfaces(MockSettingsImplTest.class);
    }

    @Test
    public void testGetName() {
        assertNull(mockSettings.getMockName());
    }

    @Test
    public void testGetExtraInterfaces() {
        assertNull(mockSettings.getExtraInterfaces());
    }

    @Test
    public void testGetSpiedInstance() {
        assertNull(mockSettings.getSpiedInstance());
    }

    @Test
    public void testName() {
        String name = "mockName";

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
        Answer<Object> defaultAnswer = mock(Answer.class);

        MockSettingsImpl result = mockSettings.defaultAnswer(defaultAnswer);
        assertEquals(defaultAnswer, result.getDefaultAnswer());
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
        mockSettings.name("mockName");
        mockSettings.initiateMockName(MockSettingsImpl.class);

        MockName mockName = mockSettings.getMockName();
        assertEquals("mockName", mockName.toString());
    }

}