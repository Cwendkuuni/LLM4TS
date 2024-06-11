import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.util.MockName;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.*;

public class MockSettingsImplTest {

    private MockSettingsImpl mockSettings;

    @Before
    public void setUp() {
        mockSettings = new MockSettingsImpl();
    }

    @Test
    public void testSerializable() {
        mockSettings.serializable();
        assertTrue(mockSettings.isSerializable());
    }

    @Test
    public void testExtraInterfaces() {
        Class<?>[] extraInterfaces = new Class<?>[]{Runnable.class, Cloneable.class};
        mockSettings.extraInterfaces(extraInterfaces);
        assertArrayEquals(extraInterfaces, mockSettings.getExtraInterfaces());
    }

    @Test(expected = Reporter.class)
    public void testExtraInterfacesWithNull() {
        Class<?>[] extraInterfaces = new Class<?>[]{Runnable.class, null};
        mockSettings.extraInterfaces(extraInterfaces);
    }

    @Test(expected = Reporter.class)
    public void testExtraInterfacesWithNonInterface() {
        Class<?>[] extraInterfaces = new Class<?>[]{Runnable.class, String.class};
        mockSettings.extraInterfaces(extraInterfaces);
    }

    @Test
    public void testGetMockName() {
        assertNull(mockSettings.getMockName());
    }

    @Test
    public void testGetName() {
        String name = "mockObject";
        mockSettings.name(name);
        assertEquals(name, mockSettings.getMockName().getMockName());
    }

    @Test
    public void testGetExtraInterfaces() {
        Class<?>[] extraInterfaces = new Class<?>[]{Runnable.class, Cloneable.class};
        mockSettings.extraInterfaces(extraInterfaces);
        assertArrayEquals(extraInterfaces, mockSettings.getExtraInterfaces());
    }

    @Test
    public void testGetSpiedInstance() {
        assertNull(mockSettings.getSpiedInstance());

        Object spiedInstance = new Object();
        mockSettings.spiedInstance(spiedInstance);
        assertEquals(spiedInstance, mockSettings.getSpiedInstance());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testDefaultAnswer() {
        Answer<Object> defaultAnswer = (Answer<Object>) invocation -> "Default answer";
        mockSettings.defaultAnswer(defaultAnswer);
        assertEquals(defaultAnswer, mockSettings.getDefaultAnswer());
    }

    @Test
    public void testIsSerializableWithExtraInterfaces() {
        Class<?>[] extraInterfaces = new Class<?>[]{Runnable.class, Cloneable.class, java.io.Serializable.class};
        mockSettings.extraInterfaces(extraInterfaces);
        assertTrue(mockSettings.isSerializable());
    }

    @Test
    public void testIsSerializableWithoutExtraInterfaces() {
        assertFalse(mockSettings.isSerializable());
    }

    @Test
    public void testInitiateMockName() {
        String name = "mockObject";
        Class<?> classToMock = String.class;
        mockSettings.name(name);

        mockSettings.initiateMockName(classToMock);
        MockName mockName = mockSettings.getMockName();

        assertEquals(name, mockName.getMockName());
        assertEquals(classToMock, mockName.getClassToMock());
    }
}