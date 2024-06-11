import org.junit.Test;
import static org.mockito.Mockito.*;
import org.mockito.exceptions.misusing.MockCreationException;
import org.mockito.internal.util.MockName;
import org.mockito.stubbing.Answer;

public class MockSettingsImplTest {

    @Test
    public void testMockSettingsSerializable() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        mockSettings.serializable();
        assertTrue(mockSettings.isSerializable());
    }

    @Test
    public void testMockSettingsExtraInterfaces() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Class<?>[] extraInterfaces = {Runnable.class};
        mockSettings.extraInterfaces(extraInterfaces);
        assertArrayEquals(extraInterfaces, mockSettings.getExtraInterfaces());
    }

    @Test
    public void testMockSettingsExtraInterfacesWithNull() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Class<?>[] extraInterfaces = {Runnable.class, null};
        try {
            mockSettings.extraInterfaces(extraInterfaces);
            fail("Expected MockCreationException");
        } catch (MockCreationException e) {
            assertEquals("Null extra interface", e.getMessage());
        }
    }

    @Test
    public void testMockSettingsExtraInterfacesWithNonInterfaceClass() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Class<?>[] extraInterfaces = {Runnable.class, Object.class};
        try {
            mockSettings.extraInterfaces(extraInterfaces);
            fail("Expected MockCreationException");
        } catch (MockCreationException e) {
            assertEquals("Class is not an interface", e.getMessage());
            assertTrue(e.getInvolvedClass() == Object.class);
        }
    }

    @Test
    public void testMockSettingsGetName() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        String name = "mockName";
        mockSettings.name(name);
        assertEquals(name, mockSettings.getMockName().getMockName());
    }

    @Test
    public void testMockSettingsSpiedInstance() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Object spiedInstance = new Object();
        mockSettings.spiedInstance(spiedInstance);
        assertEquals(spiedInstance, mockSettings.getSpiedInstance());
    }

    @Test
    public void testMockSettingsDefaultAnswer() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Answer<Object> defaultAnswer = new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) throws Throwable {
                 return null;
            }
        };
        mockSettings.defaultAnswer(defaultAnswer);
        assertEquals(defaultAnswer, mockSettings.getDefaultAnswer());
    }

    @Test
    public void testMockSettingsIsSerializable() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Class<?>[] extraInterfaces = {Serializable.class};
        mockSettings.extraInterfaces(extraInterfaces);
        assertTrue(mockSettings.isSerializable());
    }

    @Test
    public void testMockSettingsInitiateMockName() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        String name = "mockName";
        Class<?> classToMock = Object.class;
        mockSettings.name(name);
        mockSettings.initiateMockName(classToMock);
        assertEquals(name, mockSettings.getMockName().getMockName());
        assertEquals(classToMock, mockSettings.getMockName().getMockedType());
    }
}