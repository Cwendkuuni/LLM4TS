import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.exceptions.Reporter;
import org.mockito.stubbing.Answer;
import org.mockito.internal.util.MockName;

import static org.junit.Assert.*;

public class MockSettingsImplTest {

    private MockSettingsImpl mockSettings;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockSettings = new MockSettingsImpl();
    }

    @Test
    public void testSerializable() {
        MockSettingsImpl result = mockSettings.serializable();
        assertTrue(result.isSerializable());
    }

    @Test(expected = Reporter.class)
    public void testExtraInterfacesWithNoInterface() {
        mockSettings.extraInterfaces();
        // Reporter should be thrown
    }

    @Test(expected = Reporter.class)
    public void testExtraInterfacesWithNullInterface() {
        mockSettings.extraInterfaces(null);
        // Reporter should be thrown
    }

    @Test(expected = Reporter.class)
    public void testExtraInterfacesWithNonInterface() {
        mockSettings.extraInterfaces(Object.class);
        // Reporter should be thrown
    }

    @Test
    public void testExtraInterfaces() {
        Class<?>[] interfaces = {Serializable.class, Cloneable.class};
        MockSettingsImpl result = mockSettings.extraInterfaces(interfaces);
        assertArrayEquals(interfaces, result.getExtraInterfaces());
    }

    @Test
    public void testGetMockName() {
        assertNull(mockSettings.getMockName());
        mockSettings.initiateMockName(MockSettingsImpl.class);
        assertEquals(MockName.class, mockSettings.getMockName().getClass());
    }

    @Test
    public void testGetName() {
        assertNull(mockSettings.getMockName());
        mockSettings.name("MockName");
        assertEquals("MockName", mockSettings.getMockName().getMockName());
    }

    @Test
    public void testGetSpiedInstance() {
        assertNull(mockSettings.getSpiedInstance());
        Object spiedInstance = new Object();
        mockSettings.spiedInstance(spiedInstance);
        assertEquals(spiedInstance, mockSettings.getSpiedInstance());
    }

    @Test
    public void testDefaultAnswer() {
        assertNull(mockSettings.getDefaultAnswer());
        Answer<Object> defaultAnswer = Mockito.mock(Answer.class);
        mockSettings.defaultAnswer(defaultAnswer);
        assertEquals(defaultAnswer, mockSettings.getDefaultAnswer());
    }

    @Test
    public void testIsSerializableWithoutInterfaces() {
        assertFalse(mockSettings.isSerializable());
    }

    @Test
    public void testIsSerializableWithInterfaces() {
        Class<?>[] interfaces = {Serializable.class};
        mockSettings.extraInterfaces(interfaces);
        assertTrue(mockSettings.isSerializable());
    }
}