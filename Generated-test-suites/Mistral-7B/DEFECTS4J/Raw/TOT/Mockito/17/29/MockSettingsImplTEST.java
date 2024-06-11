
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import static org.junit.Assert.*;;

public class MockSettingsImplTest {

    private MockSettings settings;

    @Before
    public void setUp() {
        settings = new MockSettingsImpl();
    }

    @Test
    public void testSerializable() {
        settings.serializable();
        assertTrue(settings.isSerializable());
    }

    @Test
    public void testExtraInterfacesNull() {
        assertThrows(IllegalArgumentException.class, () -> settings.extraInterfaces(null));
    }

    @Test
    public void testExtraInterfacesEmptyArray() {
        assertThrows(IllegalArgumentException.class, () -> settings.extraInterfaces(new Class[0]));
    }

    @Test
    public void testExtraInterfacesNullClass() {
        Class<?>[] extraInterfaces = new Class[] { null };
        assertThrows(IllegalArgumentException.class, () -> settings.extraInterfaces(extraInterfaces));
    }

    @Test
    public void testExtraInterfacesNonInterface() {
        Class<?>[] extraInterfaces = new Class[] { String.class };
        assertThrows(IllegalArgumentException.class, () -> settings.extraInterfaces(extraInterfaces));
    }

    @Test
    public void testDefaultAnswer() {
        Answer<Object> answer = Mockito.mock(Answer.class);
        settings.defaultAnswer(answer);
        assertEquals(answer, settings.getDefaultAnswer());
    }

    @Test
    public void testInitiateMockNameNullClass() {
        assertThrows(NullPointerException.class, () -> settings.initiateMockName(null));
    }

    @Test
    public void testInitiateMockNameEmptyString() {
        assertThrows(IllegalArgumentException.class, () -> settings.initiateMockName(""));
    }

    @Test
    public void testInitiateMockNameClassIsSerializable() {
        settings.initiateMockName(Serializable.class);
        assertNotNull(settings.getMockName());
    }

    @Test
    public void testGetExtraInterfaces() {
        settings.extraInterfaces(new Class[] { java.io.Serializable.class, String.class });
        assertArrayEquals(new Class[] { java.io.Serializable.class, String.class }, settings.getExtraInterfaces());
    }

    @Test
    public void testGetExtraInterfacesEmptyArray() {
        settings.extraInterfaces(new Class[0]);
        assertArrayEquals(new Class[0], settings.getExtraInterfaces());
    }

    @Test
    public void testIsSerializable() {
        settings.extraInterfaces(new Class[] { java.io.Serializable.class });
        assertTrue(settings.isSerializable());
    }

    @Test
    public void testIsSerializableEmptyArray() {
        settings.extraInterfaces(new Class[0]);
        assertFalse(settings.isSerializable());
    }
}