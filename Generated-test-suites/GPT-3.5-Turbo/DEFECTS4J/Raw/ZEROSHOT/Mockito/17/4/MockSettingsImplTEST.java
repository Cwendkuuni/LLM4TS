import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
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
    public void testExtraInterfaces() {
        Class<?>[] extraInterfaces = {Comparable.class};
        MockSettingsImpl result = mockSettings.extraInterfaces(extraInterfaces);
        assertArrayEquals(extraInterfaces, result.getExtraInterfaces());
    }

    @Test(expected = Reporter.class)
    public void testExtraInterfacesWithNoInterfaces() {
        mockSettings.extraInterfaces();
    }

    @Test(expected = Reporter.class)
    public void testExtraInterfacesWithNullParameter() {
        mockSettings.extraInterfaces((Class<?>) null);
    }

    @Test(expected = Reporter.class)
    public void testExtraInterfacesWithNonInterfaceParameter() {
        Class<?> nonInterface = String.class;
        mockSettings.extraInterfaces(nonInterface);
    }

    @Test
    public void testGetMockName() {
        MockName mockName = mock(MockName.class);
        mockSettings.initiateMockName(String.class);
        mockSettings.name("mock");
        when(mockName.withMockName("mock")).thenReturn(mockName);

        assertEquals(mockName, mockSettings.getMockName());
    }

    @Test
    public void testGetExtraInterfaces() {
        Class<?>[] extraInterfaces = {Comparable.class, Cloneable.class};
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
    public void testName() {
        String name = "mock";
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
    public void testIsSerializable() {
        Class<?>[] extraInterfaces = {java.io.Serializable.class};
        mockSettings.extraInterfaces(extraInterfaces);

        assertTrue(mockSettings.isSerializable());
    }
}