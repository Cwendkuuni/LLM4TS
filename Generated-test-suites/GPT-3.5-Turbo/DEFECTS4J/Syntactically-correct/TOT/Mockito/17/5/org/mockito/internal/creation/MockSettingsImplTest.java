import org.junit.Before;
import org.junit.Test;

import org.mockito.exceptions.Reporter;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class MockSettingsImplTest {

    private MockSettingsImpl mockSettings;

    @Before
    public void setUp() {
        mockSettings = new MockSettingsImpl();
    }

    @Test
    public void testSerializable() {
        MockSettingsImpl result = mockSettings.serializable();
        Class<?>[] extraInterfaces = result.getExtraInterfaces();

        assertTrue(result.isSerializable());
        assertNotNull(extraInterfaces);
        assertEquals(1, extraInterfaces.length);
        assertEquals(java.io.Serializable.class, extraInterfaces[0]);
    }

    @Test
    public void testExtraInterfaces() {
        Class<?>[] extraInterfaces = new Class<?>[]{Comparable.class, Cloneable.class};
        MockSettingsImpl result = mockSettings.extraInterfaces(extraInterfaces);

        assertEquals(extraInterfaces, result.getExtraInterfaces());
    }

    @Test(expected = Reporter.class)
    public void testExtraInterfacesWithNull() {
        Class<?>[] extraInterfaces = new Class<?>[]{Comparable.class, null};
        mockSettings.extraInterfaces(extraInterfaces);
    }

    @Test(expected = Reporter.class)
    public void testExtraInterfacesWithoutInterface() {
        Class<?>[] extraInterfaces = new Class<?>[]{Integer.class};
        mockSettings.extraInterfaces(extraInterfaces);
    }

    @Test
    public void testGetMockName() {
        mockSettings.name("mockName");
        MockName mockName = new MockName("mockName", null);
        assertEquals(mockName, mockSettings.getMockName());
    }

    @Test
    public void testGetName() {
        String name = "mockName";
        MockSettingsImpl result = mockSettings.name(name);
        assertEquals(name, result.getMockName().getMockName());
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
    public void testisSerializable() {
        assertFalse(mockSettings.isSerializable());

        Class<?>[] extraInterfaces = new Class<?>[]{java.io.Serializable.class};
        mockSettings.extraInterfaces(extraInterfaces);
        assertTrue(mockSettings.isSerializable());
    }

    @Test
    public void testInitiateMockName() {
        Class<?> classToMock = Object.class;
        mockSettings.initiateMockName(classToMock);
        assertEquals(new MockName(null, classToMock), mockSettings.getMockName());
    }
}