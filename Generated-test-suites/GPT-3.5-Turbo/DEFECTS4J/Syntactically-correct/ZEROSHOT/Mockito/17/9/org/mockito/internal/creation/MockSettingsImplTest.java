import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.MockName;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class MockSettingsImplTest {
    private MockSettingsImpl mockSettings;

    @Before
    public void setUp() {
        mockSettings = new MockSettingsImpl();
    }

    @Test
    public void testSerializable() {
        MockSettingsImpl mockSettingsSerializable = mockSettings.serializable();

        assertEquals(java.io.Serializable.class, mockSettingsSerializable.getExtraInterfaces()[0]);
    }

    @Test(expected = Reporter.class)
    public void testExtraInterfacesNull() {
        mockSettings.extraInterfaces(null);
    }

    @Test(expected = Reporter.class)
    public void testExtraInterfacesEmpty() {
        mockSettings.extraInterfaces();
    }

    @Test(expected = Reporter.class)
    public void testExtraInterfacesNotInterface() {
        mockSettings.extraInterfaces(String.class);
    }

    @Test
    public void testExtraInterfaces() {
        Class<?>[] extraInterfaces = {Runnable.class, Comparable.class};
        MockSettingsImpl mockSettingsExtraInterfaces = mockSettings.extraInterfaces(extraInterfaces);

        assertEquals(extraInterfaces, mockSettingsExtraInterfaces.getExtraInterfaces());
    }

    @Test
    public void testGetName() {
        String name = "mockName";
        MockSettingsImpl mockSettingsName = mockSettings.name(name);

        assertEquals(name, mockSettingsName.getMockName().toString());
    }

    @Test
    public void testGetSpiedInstance() {
        Object spiedInstance = mock(Object.class);
        MockSettingsImpl mockSettingsSpiedInstance = mockSettings.spiedInstance(spiedInstance);

        assertEquals(spiedInstance, mockSettingsSpiedInstance.getSpiedInstance());
    }

    @Test
    public void testDefaultAnswer() {
        Answer<Object> defaultAnswer = mock(Answer.class);
        MockSettingsImpl mockSettingsDefaultAnswer = mockSettings.defaultAnswer(defaultAnswer);

        assertEquals(defaultAnswer, mockSettingsDefaultAnswer.getDefaultAnswer());
    }

    @Test
    public void testIsSerializableTrue() {
        Class<?>[] extraInterfaces = {java.io.Serializable.class};
        MockSettingsImpl mockSettingsSerializable = mockSettings.extraInterfaces(extraInterfaces);

        assertEquals(true, mockSettingsSerializable.isSerializable());
    }

    @Test
    public void testIsSerializableFalse() {
        Class<?>[] extraInterfaces = {Runnable.class, Comparable.class};
        MockSettingsImpl mockSettingsNotSerializable = mockSettings.extraInterfaces(extraInterfaces);

        assertEquals(false, mockSettingsNotSerializable.isSerializable());
    }

    @Test
    public void testInitiateMockName() {
        Class<?> classToMock = Object.class;
        mockSettings.initiateMockName(classToMock);

        assertEquals(classToMock, mockSettings.getMockName().getType());
    }
}