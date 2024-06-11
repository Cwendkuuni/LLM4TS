import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.util.MockName;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;



public class MockSettingsImplTest {

    @Test
    public void testSerializable() {
        MockSettingsImpl settings = new MockSettingsImpl();
        MockSettingsImpl result = settings.serializable();
        assertNotNull(result);
        assertArrayEquals(new Class[]{java.io.Serializable.class}, result.getExtraInterfaces());
    }

    @Test
    public void testExtraInterfaces() {
        MockSettingsImpl settings = new MockSettingsImpl();
        Class<?>[] extraInterfaces = {Runnable.class, Comparable.class};
        MockSettingsImpl result = settings.extraInterfaces(extraInterfaces);
        assertNotNull(result);
        assertArrayEquals(extraInterfaces, result.getExtraInterfaces());
    }

    @Test
    public void testExtraInterfacesNull() {
        MockSettingsImpl settings = new MockSettingsImpl();
        Reporter reporter = Mockito.mock(Reporter.class);

        settings.extraInterfaces(null);

        Mockito.verify(reporter).extraInterfacesRequiresAtLeastOneInterface();
        assertNull(settings.getExtraInterfaces());
    }

    @Test
    public void testExtraInterfacesContainsNull() {
        MockSettingsImpl settings = new MockSettingsImpl();
        Reporter reporter = Mockito.mock(Reporter.class);

        settings.extraInterfaces(Runnable.class, null);

        Mockito.verify(reporter).extraInterfacesDoesNotAcceptNullParameters();
        assertNull(settings.getExtraInterfaces());
    }

    @Test
    public void testExtraInterfacesAcceptsOnlyInterfaces() {
        MockSettingsImpl settings = new MockSettingsImpl();
        Reporter reporter = Mockito.mock(Reporter.class);
        Class<?> nonInterface = String.class;

        settings.extraInterfaces(nonInterface);

        Mockito.verify(reporter).extraInterfacesAcceptsOnlyInterfaces(nonInterface);
        assertNull(settings.getExtraInterfaces());
    }

    @Test
    public void testGetMockName() {
        MockSettingsImpl settings = new MockSettingsImpl();
        MockName mockName = new MockName("test", Object.class);
        settings.initiateMockName(Object.class);

        MockName result = settings.getMockName();

        assertEquals(mockName, result);
    }

    @Test
    public void testGetName() {
        MockSettingsImpl settings = new MockSettingsImpl();
        String name = "test";
        MockSettingsImpl result = settings.name(name);

        assertNotNull(result);
        assertEquals(name, result.getMockName().toString());
    }

    @Test
    public void testGetSpiedInstance() {
        MockSettingsImpl settings = new MockSettingsImpl();
        Object spiedInstance = new Object();
        MockSettingsImpl result = settings.spiedInstance(spiedInstance);

        assertNotNull(result);
        assertEquals(spiedInstance, result.getSpiedInstance());
    }

    @Test
    public void testDefaultAnswer() {
        MockSettingsImpl settings = new MockSettingsImpl();
        Answer<Object> defaultAnswer = Mockito.mock(Answer.class);
        MockSettingsImpl result = settings.defaultAnswer(defaultAnswer);

        assertNotNull(result);
        assertEquals(defaultAnswer, result.getDefaultAnswer());
    }

    @Test
    public void testIsSerializable() {
        MockSettingsImpl settings = new MockSettingsImpl();
        Class<?>[] extraInterfaces = {Runnable.class, java.io.Serializable.class};
        settings.extraInterfaces(extraInterfaces);

        boolean result = settings.isSerializable();

        assertEquals(true, result);
    }

    @Test
    public void testIsNotSerializable() {
        MockSettingsImpl settings = new MockSettingsImpl();
        Class<?>[] extraInterfaces = {Runnable.class};
        settings.extraInterfaces(extraInterfaces);

        boolean result = settings.isSerializable();

        assertEquals(false, result);
    }
}

