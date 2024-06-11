import org.junit.Test;
import org.mockito.exceptions.Reporter;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MockSettingsImplTest {

    @Test
    public void testSerializable() {
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.serializable();

        assertTrue(settings.isSerializable());
    }

    @Test(expected = Reporter.class)
    public void testExtraInterfaces_NullParameters() {
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.extraInterfaces(null);
    }

    @Test(expected = Reporter.class)
    public void testExtraInterfaces_NoInterfaces() {
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.extraInterfaces();
    }

    @Test(expected = Reporter.class)
    public void testExtraInterfaces_NotInterface() {
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.extraInterfaces(String.class);
    }

    @Test
    public void testExtraInterfaces_ValidInterfaces() {
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.extraInterfaces(Runnable.class, Comparable.class);

        assertArrayEquals(new Class<?>[]{Runnable.class, Comparable.class}, settings.getExtraInterfaces());
    }

    @Test
    public void testGetMockName_NoNameSet() {
        MockSettingsImpl settings = new MockSettingsImpl();

        assertNull(settings.getMockName());
    }

    @Test
    public void testGetMockName_NameSet() {
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.name("mockName");

        assertNotNull(settings.getMockName());
        assertEquals("mockName", settings.getMockName().toString());
    }

    @Test
    public void testGetExtraInterfaces() {
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.extraInterfaces(Runnable.class);

        assertArrayEquals(new Class<?>[]{Runnable.class}, settings.getExtraInterfaces());
    }

    @Test
    public void testGetSpiedInstance() {
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.spiedInstance(new Object());

        assertNotNull(settings.getSpiedInstance());
    }

    @Test
    public void testDefaultAnswer() {
        Answer<Object> answer = mock(Answer.class);

        MockSettingsImpl settings = new MockSettingsImpl();
        settings.defaultAnswer(answer);

        assertEquals(answer, settings.getDefaultAnswer());
    }

    @Test
    public void testIsSerializable_ExtraInterfacesNull() {
        MockSettingsImpl settings = new MockSettingsImpl();

        assertFalse(settings.isSerializable());
    }

    @Test
    public void testIsSerializable_ExtraInterfacesNotNull() {
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.extraInterfaces(Runnable.class);

        assertFalse(settings.isSerializable());
    }

    @Test
    public void testInitiateMockName() {
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.initiateMockName(MockSettingsImpl.class);

        assertNotNull(settings.getMockName());
        assertEquals("MockSettingsImpl", settings.getMockName().toString());
    }

}