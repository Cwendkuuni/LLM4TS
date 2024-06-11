import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class MockSettingsImplTest {

    // Test for serializable() method
    @Test
    public void testSerializable() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();

        MockSettings result = mockSettings.serializable();

        assertTrue(result.isSerializable());
    }

    // Test for extraInterfaces() method with valid input
    @Test
    public void testExtraInterfacesValid() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();

        MockSettings result = mockSettings.extraInterfaces(Runnable.class, Cloneable.class);

        Class<?>[] extraInterfaces = result.getExtraInterfaces();
        assertNotNull(extraInterfaces);
        assertEquals(2, extraInterfaces.length);
        assertEquals(Runnable.class, extraInterfaces[0]);
        assertEquals(Cloneable.class, extraInterfaces[1]);
    }

    // Test for extraInterfaces() method with null input
    @Test(expected = RuntimeException.class)
    public void testExtraInterfacesNull() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();

        MockSettings result = mockSettings.extraInterfaces((Class<?>[]) null);
    }

    // Test for extraInterfaces() method with empty input
    @Test(expected = RuntimeException.class)
    public void testExtraInterfacesEmpty() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();

        MockSettings result = mockSettings.extraInterfaces();
    }

    // Test for extraInterfaces() method with non-interface input
    @Test(expected = RuntimeException.class)
    public void testExtraInterfacesNonInterface() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();

        MockSettings result = mockSettings.extraInterfaces(String.class);
    }

    // Test for getMockName() method
    @Test
    public void testGetMockName() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();

        assertNull(mockSettings.getMockName());
    }

    // Test for getName() method
    @Test
    public void testGetName() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();

        MockSettings result = mockSettings.name("mockName");

        assertEquals("mockName", result.getMockName().toString());
    }

    // Test for getExtraInterfaces() method
    @Test
    public void testGetExtraInterfaces() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();

        MockSettings result = mockSettings.extraInterfaces(Runnable.class, Cloneable.class);

        Class<?>[] extraInterfaces = result.getExtraInterfaces();
        assertNotNull(extraInterfaces);
        assertEquals(2, extraInterfaces.length);
        assertEquals(Runnable.class, extraInterfaces[0]);
        assertEquals(Cloneable.class, extraInterfaces[1]);
    }

    // Test for getSpiedInstance() method
    @Test
    public void testGetSpiedInstance() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();

        Object spiedInstance = new Object();
        MockSettings result = mockSettings.spiedInstance(spiedInstance);

        assertEquals(spiedInstance, result.getSpiedInstance());
    }

    // Test for defaultAnswer() method
    @Test
    public void testDefaultAnswer() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Answer<Object> defaultAnswer = mock(Answer.class);

        MockSettings result = mockSettings.defaultAnswer(defaultAnswer);

        assertEquals(defaultAnswer, result.getDefaultAnswer());
    }

    // Test for isSerializable() method when extraInterfaces is null
    @Test
    public void testIsSerializableNullExtraInterfaces() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();

        assertFalse(mockSettings.isSerializable());
    }

    // Test for isSerializable() method with serializable extraInterface
    @Test
    public void testIsSerializableWithSerializableExtraInterface() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        mockSettings.extraInterfaces(java.io.Serializable.class);

        assertTrue(mockSettings.isSerializable());
    }

    // Test for initiateMockName() method
    @Test
    public void testInitiateMockName() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();

        mockSettings.initiateMockName(MockSettingsImpl.class);

        assertNotNull(mockSettings.getMockName());
        assertEquals("MockSettingsImpl", mockSettings.getMockName().toString());
    }

}