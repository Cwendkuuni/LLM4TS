
import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.util.MockName;
import org.mockito.stubbing.Answer;

public class MockSettingsImplTest {

    @Test
    public void testSerializable() {
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.serializable();
        Assert.assertTrue(settings.isSerializable());
    }

    @Test
    public void testExtraInterfaces() {
        MockSettingsImpl settings = new MockSettingsImpl();
        Class<?>[] extraInterfaces = {java.io.Serializable.class, Runnable.class};
        settings.extraInterfaces(extraInterfaces);
        Assert.assertArrayEquals(extraInterfaces, settings.getExtraInterfaces());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExtraInterfacesNoInterfaces() {
        MockSettingsImpl settings = new MockSettingsImpl();
        settings.extraInterfaces();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExtraInterfacesNullInterface() {
        MockSettingsImpl settings = new MockSettingsImpl();
        Class<?>[] extraInterfaces = {java.io.Serializable.class, null};
        settings.extraInterfaces(extraInterfaces);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExtraInterfacesNonInterface() {
        MockSettingsImpl settings = new MockSettingsImpl();
        Class<?>[] extraInterfaces = {java.io.Serializable.class, String.class};
        settings.extraInterfaces(extraInterfaces);
    }

    @Test
    public void testGetMockName() {
        MockSettingsImpl settings = new MockSettingsImpl();
        MockName mockName = settings.getMockName();
        Assert.assertNull(mockName);
    }

    @Test
    public void testGetName() {
        MockSettingsImpl settings = new MockSettingsImpl();
        String name = "mockName";
        settings.name(name);
        Assert.assertEquals(name, settings.getMockName().toString());
    }

    @Test
    public void testGetExtraInterfaces() {
        MockSettingsImpl settings = new MockSettingsImpl();
        Class<?>[] extraInterfaces = {java.io.Serializable.class, Runnable.class};
        settings.extraInterfaces(extraInterfaces);
        Assert.assertArrayEquals(extraInterfaces, settings.getExtraInterfaces());
    }

    @Test
    public void testGetSpiedInstance() {
        MockSettingsImpl settings = new MockSettingsImpl();
        Object spiedInstance = new Object();
        settings.spiedInstance(spiedInstance);
        Assert.assertEquals(spiedInstance, settings.getSpiedInstance());
    }

    @Test
    public void testGetDefaultAnswer() {
        MockSettingsImpl settings = new MockSettingsImpl();
        Answer<Object> defaultAnswer = (invocation) -> null;
        settings.defaultAnswer(defaultAnswer);
        Assert.assertEquals(defaultAnswer, settings.getDefaultAnswer());
    }

    @Test
    public void testIsSerializableTrue() {
        MockSettingsImpl settings = new MockSettingsImpl();
        Class<?>[] extraInterfaces = {java.io.Serializable.class};
        settings.extraInterfaces(extraInterfaces);
        Assert.assertTrue(settings.isSerializable());
    }

    @Test
    public void testIsSerializableFalse() {
        MockSettingsImpl settings = new MockSettingsImpl();
        Class<?>[] extraInterfaces = {Runnable.class};
        settings.extraInterfaces(extraInterfaces);
        Assert.assertFalse(settings.isSerializable());
    }

    @Test
    public void testInitiateMockName() {
        MockSettingsImpl settings = new MockSettingsImpl();
        Class<?> classToMock = String.class;
        settings.initiateMockName(classToMock);
        Assert.assertEquals(MockName.DEFAULT.toString(), settings.getMockName().toString());
    }

}
