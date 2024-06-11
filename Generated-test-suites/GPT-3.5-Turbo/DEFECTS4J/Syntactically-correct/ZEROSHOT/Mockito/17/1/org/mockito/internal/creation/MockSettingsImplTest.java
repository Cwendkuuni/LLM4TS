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
        MockSettingsImpl settings = mockSettings.serializable();
        assertTrue(settings.isSerializable());
    }

    @Test
    public void testExtraInterfaces() {
        Class<?>[] extraInterfaces = new Class<?>[]{Runnable.class, Comparable.class};
        MockSettingsImpl settings = mockSettings.extraInterfaces(extraInterfaces);
        assertArrayEquals(extraInterfaces, settings.getExtraInterfaces());
    }

    @Test
    public void testExtraInterfacesWithNoInterfaces() {
        try {
            mockSettings.extraInterfaces();
            fail("Expected exception not thrown");
        } catch (Exception e) {
            assertEquals(Reporter.class, e.getClass());
        }
    }

    @Test
    public void testExtraInterfacesWithNullParameter() {
        try {
            mockSettings.extraInterfaces((Class<?>) null);
            fail("Expected exception not thrown");
        } catch (Exception e) {
            assertEquals(Reporter.class, e.getClass());
        }
    }

    @Test
    public void testExtraInterfacesWithNonInterfaceParameter() {
        try {
            mockSettings.extraInterfaces(String.class);
            fail("Expected exception not thrown");
        } catch (Exception e) {
            assertEquals(Reporter.class, e.getClass());
        }
    }

    @Test
    public void testGetMockName() {
        MockName mockName = mock(MockName.class);
        mockSettings.initiateMockName(MockSettingsImpl.class);
        mockSettings.name("testName");
        when(mockName.getMockName()).thenReturn("testName");
        assertEquals(mockName, mockSettings.getMockName());
    }

    @Test
    public void testGetExtraInterfaces() {
        Class<?>[] extraInterfaces = new Class<?>[]{Runnable.class, Comparable.class};
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
    public void testSetName() {
        String name = "testName";
        mockSettings.name(name);
        assertEquals(name, mockSettings.getMockName().getMockName());
    }

    @Test
    public void testSetSpiedInstance() {
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
    public void testIsSerializableWhenExtraInterfacesIsNull() {
        assertFalse(mockSettings.isSerializable());
    }

    @Test
    public void testIsSerializableWhenExtraInterfacesContainsSerializable() {
        mockSettings.extraInterfaces(Serializable.class);
        assertTrue(mockSettings.isSerializable());
    }

    @Test
    public void testInitiateMockName() {
        MockName mockName = mock(MockName.class);
        String name = "testName";
        Class classToMock = MockSettingsImpl.class;
        mockSettings.name(name);
        mockSettings.initiateMockName(classToMock);
        assertEquals(mockName, mockSettings.getMockName());
    }

}