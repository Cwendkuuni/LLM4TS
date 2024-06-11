import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.util.MockName;
import org.mockito.stubbing.Answer;

public class MockSettingsImplTest {

    @Test
    public void testSerializable() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        MockSettings result = mockSettings.serializable();
        assertTrue(result.isSerializable());
    }

    @Test
    public void testExtraInterfaces() {
        Class<?> interface1 = Interface1.class;
        Class<?> interface2 = Interface2.class;

        MockSettingsImpl mockSettings = new MockSettingsImpl();
        MockSettings result = mockSettings.extraInterfaces(interface1, interface2);

        Class<?>[] extraInterfaces = result.getExtraInterfaces();
        assertEquals(2, extraInterfaces.length);
        assertEquals(interface1, extraInterfaces[0]);
        assertEquals(interface2, extraInterfaces[1]);
    }

    @Test(expected = Reporter.class)
    public void testExtraInterfacesWithNullInput() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        mockSettings.extraInterfaces((Class<?>[]) null);
    }

    @Test(expected = Reporter.class)
    public void testExtraInterfacesWithEmptyInput() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        mockSettings.extraInterfaces();
    }

    @Test(expected = Reporter.class)
    public void testExtraInterfacesWithNonInterfaceInput() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        mockSettings.extraInterfaces(String.class);
    }

    @Test
    public void testGetMockName() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        MockName result = mockSettings.getMockName();
        assertNull(result);
    }

    @Test
    public void testSpiedInstance() {
        Object spiedInstance = new Object();

        MockSettingsImpl mockSettings = new MockSettingsImpl();
        MockSettings result = mockSettings.spiedInstance(spiedInstance);

        Object resultInstance = result.getSpiedInstance();
        assertEquals(spiedInstance, resultInstance);
    }

    @Test
    public void testDefaultAnswer() {
        Answer<Object> defaultAnswer = new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return null;
            }
        };

        MockSettingsImpl mockSettings = new MockSettingsImpl();
        MockSettings result = mockSettings.defaultAnswer(defaultAnswer);

        Answer<Object> resultAnswer = result.getDefaultAnswer();
        assertEquals(defaultAnswer, resultAnswer);
    }

    @Test
    public void testIsSerializableWithExtraInterfaces() {
        Class<?> serializableInterface = java.io.Serializable.class;

        MockSettingsImpl mockSettings = new MockSettingsImpl();
        mockSettings.extraInterfaces(serializableInterface);

        assertTrue(mockSettings.isSerializable());
    }

    @Test
    public void testIsSerializableWithoutExtraInterfaces() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();

        assertFalse(mockSettings.isSerializable());
    }

    @Test
    public void testInitiateMockName() {
        Class<?> classToMock = MyClass.class;

        MockSettingsImpl mockSettings = new MockSettingsImpl();
        mockSettings.initiateMockName(classToMock);

        MockName result = mockSettings.getMockName();

        assertNotNull(result);
        assertEquals(classToMock, result.getTypeToMock());
    }

    private interface Interface1 {}

    private interface Interface2 {}

    private class MyClass {}
}