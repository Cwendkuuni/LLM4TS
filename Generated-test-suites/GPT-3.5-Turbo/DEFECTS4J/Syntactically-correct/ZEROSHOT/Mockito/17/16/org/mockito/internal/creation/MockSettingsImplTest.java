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
    public void setUp() {
        mockSettings = new MockSettingsImpl();
    }

    @Test
    public void testSerializable() {
        MockSettingsImpl result = mockSettings.serializable();
        assertTrue(result.isSerializable());
    }

    @Test
    public void testExtraInterfacesWithValidInterfaces() {
        Class<?>[] extraInterfaces = {Runnable.class, Comparable.class};
        MockSettingsImpl result = mockSettings.extraInterfaces(extraInterfaces);
        assertArrayEquals(extraInterfaces, result.getExtraInterfaces());
    }

    @Test(expected = Reporter.class)
    public void testExtraInterfacesWithNoInterfaces() {
        Class<?>[] extraInterfaces = {};
        mockSettings.extraInterfaces(extraInterfaces);
    }

    @Test(expected = Reporter.class)
    public void testExtraInterfacesWithNullInterfaces() {
        mockSettings.extraInterfaces((Class<?>[]) null);
    }

    @Test(expected = Reporter.class)
    public void testExtraInterfacesWithNonInterfaces() {
        Class<?>[] extraInterfaces = {String.class};
        mockSettings.extraInterfaces(extraInterfaces);
    }

    @Test
    public void testGetName() {
        String name = "mockName";
        mockSettings.name(name);
        assertEquals(name, mockSettings.getMockName().toString());
    }

    @Test
    public void testGetExtraInterfaces() {
        Class<?>[] extraInterfaces = {Runnable.class, Comparable.class};
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
        Answer<Object> defaultAnswer = mock(Answer.class);
        mockSettings.defaultAnswer(defaultAnswer);
        assertEquals(defaultAnswer, mockSettings.getDefaultAnswer());
    }

    @Test
    public void testIsSerializableWithSerializableExtraInterfaces() {
        Class<?>[] extraInterfaces = {Runnable.class, java.io.Serializable.class};
        mockSettings.extraInterfaces(extraInterfaces);
        assertTrue(mockSettings.isSerializable());
    }

    @Test
    public void testIsSerializableWithNonSerializableExtraInterfaces() {
        Class<?>[] extraInterfaces = {Runnable.class, Comparable.class};
        mockSettings.extraInterfaces(extraInterfaces);
        assertFalse(mockSettings.isSerializable());
    }

    @Test
    public void testInitiateMockName() {
        String name = "mockName";
        Class<?> classToMock = Object.class;
        mockSettings.name(name);
        mockSettings.initiateMockName(classToMock);
        assertEquals(name, mockSettings.getMockName().toString());
        assertEquals(classToMock, mockSettings.getMockName().getSuperType());
    }

}