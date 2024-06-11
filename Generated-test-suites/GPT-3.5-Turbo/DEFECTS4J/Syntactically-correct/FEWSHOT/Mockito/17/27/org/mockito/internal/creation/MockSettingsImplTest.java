import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.util.MockName;

public class MockSettingsImplTest {
    private MockSettingsImpl mockSettings;

    @Before
    public void setup() {
        mockSettings = new MockSettingsImpl();
    }

    @Test
    public void testSerializable() {
        MockSettingsImpl result = mockSettings.serializable();
        assertEquals(java.io.Serializable.class, result.getExtraInterfaces()[0]);
    }

    @Test
    public void testExtraInterfaces() {
        Class<?>[] extraInterfaces = {java.lang.Runnable.class, java.io.Closeable.class};
        MockSettingsImpl result = mockSettings.extraInterfaces(extraInterfaces);
        assertEquals(extraInterfaces, result.getExtraInterfaces());
    }

    @Test
    public void testExtraInterfacesWithNull() {
        Class<?>[] extraInterfaces = {null};
        MockSettingsImpl result = mockSettings.extraInterfaces(extraInterfaces);
        assertNull(result.getExtraInterfaces());
    }

    @Test
    public void testExtraInterfacesWithNonInterface() {
        Class<?>[] extraInterfaces = {java.lang.String.class};
        MockSettingsImpl result = mockSettings.extraInterfaces(extraInterfaces);
        assertNull(result.getExtraInterfaces());
    }

    @Test
    public void testName() {
        String name = "mockName";
        MockSettingsImpl result = mockSettings.name(name);
        MockName mockName = result.getMockName();
        assertEquals(name, mockName.toString());
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
    public void testIsSerializableWithExtraInterfaces() {
        Class<?>[] extraInterfaces = {java.io.Serializable.class};
        mockSettings.extraInterfaces(extraInterfaces);
        boolean result = mockSettings.isSerializable();
        assertEquals(true, result);
    }

    @Test
    public void testIsSerializableWithoutExtraInterfaces() {
        boolean result = mockSettings.isSerializable();
        assertEquals(false, result);
    }

    @Test
    public void testInitiateMockName() {
        Class classToMock = Object.class;
        mockSettings.initiateMockName(classToMock);
        MockName mockName = mockSettings.getMockName();
        assertEquals(classToMock, mockName.getMockType());
    }
}