import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.MockSettings;
import org.mockito.internal.util.MockName;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class MockSettingsImplTest {

    @Test
    public void testSerializable() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        MockSettings result = mockSettings.serializable();

        assertTrue(result.isSerializable());
    }

    @Test(expected = Reporter.class)
    public void testExtraInterfacesWithNullValue() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        mockSettings.extraInterfaces(null);
    }

    @Test(expected = Reporter.class)
    public void testExtraInterfacesWithNoInterfaces() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        mockSettings.extraInterfaces();
    }

    @Test(expected = Reporter.class)
    public void testExtraInterfacesWithNonInterface() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        mockSettings.extraInterfaces(String.class);
    }

    @Test
    public void testExtraInterfaces() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Class<?>[] extraInterfaces = {Serializable.class, Cloneable.class};
        MockSettings result = mockSettings.extraInterfaces(extraInterfaces);

        assertArrayEquals(extraInterfaces, result.getExtraInterfaces());
    }

    @Test
    public void testGetMockName() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        MockName mockName = mock(MockName.class);
        mockSettings.mockName = mockName;

        assertEquals(mockName, mockSettings.getMockName());
    }

    @Test
    public void testGetExtraInterfaces() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Class<?>[] extraInterfaces = {Serializable.class, Cloneable.class};
        mockSettings.extraInterfaces = extraInterfaces;

        assertArrayEquals(extraInterfaces, mockSettings.getExtraInterfaces());
    }

    @Test
    public void testGetSpiedInstance() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Object spiedInstance = new Object();
        mockSettings.spiedInstance = spiedInstance;

        assertEquals(spiedInstance, mockSettings.getSpiedInstance());
    }

    @Test
    public void testName() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        String name = "mockName";
        MockSettings result = mockSettings.name(name);

        assertEquals(name, mockSettings.getMockName().toString());
        assertEquals(result, mockSettings);
    }

    @Test
    public void testSpiedInstance() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Object spiedInstance = new Object();
        MockSettings result = mockSettings.spiedInstance(spiedInstance);

        assertEquals(spiedInstance, mockSettings.getSpiedInstance());
        assertEquals(result, mockSettings);
    }

    @Test
    public void testDefaultAnswer() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Answer<Object> defaultAnswer = mock(Answer.class);
        MockSettings result = mockSettings.defaultAnswer(defaultAnswer);

        assertEquals(defaultAnswer, mockSettings.getDefaultAnswer());
        assertEquals(result, mockSettings);
    }

    @Test
    public void testIsSerializableWithExtraInterfaces() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Class<?>[] extraInterfaces = {Serializable.class, Cloneable.class};
        mockSettings.extraInterfaces = extraInterfaces;

        assertTrue(mockSettings.isSerializable());
    }

    @Test
    public void testIsSerializableWithoutExtraInterfaces() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();

        assertFalse(mockSettings.isSerializable());
    }

    @Test
    public void testInitiateMockName() {
        MockName mockName = mock(MockName.class);
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Class classToMock = Object.class;

        mockSettings.initiateMockName(classToMock);

        assertEquals(mockName.toString(), mockSettings.getMockName().toString());
    }
}