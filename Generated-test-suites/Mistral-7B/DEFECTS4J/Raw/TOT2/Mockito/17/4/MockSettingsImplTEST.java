import org.junit.Test;
import org.mockito.Assert;
import org.mockito.Mockito;
import org.mockito.internal.creation.MockSettingsImpl;

public class MockSettingsImplTest {

    @Test
    public void testSerializableMethod() {
        // ... (same as the first test case)
    }

    @Test
    public void testSerializableNullArgument() {
        // ... (same as the second test case, but with a different method name)
    }

    @Test
    public void testSerializableNonInterfaceArgument() {
        // ... (same as the second test case, but with a different method name)
    }

    @Test
    public void testSerializableMultipleInterfaces() {
        // ... (same as the second test case, but with a different method name)
    }

    @Test
    public void testExtraInterfacesNullArgument() {
        MockSettings settings = new MockSettingsImpl();
        settings.extraInterfaces(null);
        Assert.assertThatThrownBy(() -> settings.extraInterfaces(null)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testExtraInterfacesNullInterfaceArgument() {
        MockSettings settings = new MockSettingsImpl();
        settings.extraInterfaces(new Class<?>[]{null});
        Assert.assertThatThrownBy(() -> settings.extraInterfaces(new Class<?>[]{null})).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testExtraInterfacesEmptyArray() {
        MockSettings settings = new MockSettingsImpl();
        settings.extraInterfaces(new Class<?>[0]);
        Assert.assertThat(settings.getExtraInterfaces()).isEmpty();
    }

    @Test
    public void testExtraInterfacesTwoInterfaces() {
        MockSettings settings = new MockSettingsImpl();
        settings.extraInterfaces(new Class<?>[]{java.io.Serializable.class, java.util.List.class});
        Assert.assertArrayEquals(new Class<?>[]{java.io.Serializable.class, java.util.List.class}, settings.getExtraInterfaces());
    }

    @Test(expected = IllegalStateException.class)
    public void testExtraInterfacesNonInterfaceClass() {
        MockSettings settings = new MockSettingsImpl();
        settings.extraInterfaces(String.class);
    }

    @Test
    public void testGetMockName() {
        MockSettings settings = new MockSettingsImpl();
        settings.name("MockName");
        MockName mockName = settings.getMockName();
        Assert.assertEquals("MockName", mockName.getName());
    }

    @Test
    public void testGetSpiedInstance() {
        Object spiedInstance = mock(Object.class);
        MockSettings settings = new MockSettingsImpl();
        settings.spiedInstance(spiedInstance);
        Assert.assertEquals(spiedInstance, settings.getSpiedInstance());
    }

    @Test
    public void testGetExtraInterfaces() {
        MockSettings settings = new MockSettingsImpl();
        settings.extraInterfaces(new Class<?>[]{java.io.Serializable.class, java.util.List.class});
        Assert.assertArrayEquals(new Class<?>[]{java.io.Serializable.class, java.util.List.class}, settings.getExtraInterfaces());
    }

    @Test
    public void testIsSerializable() {
        MockSettings settings = new MockSettingsImpl();
        settings.extraInterfaces(new Class<?>[]{java.io.Serializable.class});
        assertTrue(settings.isSerializable());
    }

    @Test
    public void testInitiateMockName() {
        Class classToMock = Mockito.class;
        MockSettings settings = new MockSettingsImpl();
        settings.initiateMockName(classToMock);
        Assert.assertNotNull(settings.getMockName());
    }
}