import org.junit.Test;
import org.mockito.exceptions.Reporter;

public class MockSettingsImplTest {
    MockSettingsImpl mockSettings;

    @Test
    public void testSerializable() {
        mockSettings = new MockSettingsImpl();

        MockSettings mockSettingsResult = mockSettings.serializable();

        assert mockSettingsResult.equals(mockSettings);
        assert mockSettingsResult.isSerializable();
    }

    @Test
    public void testExtraInterfaces() {
        mockSettings = new MockSettingsImpl();

        Class<?>[] extraInterfaces = new Class<?>[]{java.io.Serializable.class, java.lang.Cloneable.class};
        MockSettings mockSettingsResult = mockSettings.extraInterfaces(extraInterfaces);

        assert mockSettingsResult.equals(mockSettings);
        assert mockSettingsResult.getExtraInterfaces().length == 2;
        assert mockSettingsResult.getExtraInterfaces()[0].equals(java.io.Serializable.class);
        assert mockSettingsResult.getExtraInterfaces()[1].equals(java.lang.Cloneable.class);
    }

    @Test(expected = Reporter.class)
    public void testExtraInterfacesWithNull() {
        mockSettings = new MockSettingsImpl();

        Class<?>[] extraInterfaces = new Class<?>[]{null};
        mockSettings.extraInterfaces(extraInterfaces);
    }

    @Test(expected = Reporter.class)
    public void testExtraInterfacesWithNonInterfaceClass() {
        mockSettings = new MockSettingsImpl();

        Class<?>[] extraInterfaces = new Class<?>[]{java.lang.String.class};
        mockSettings.extraInterfaces(extraInterfaces);
    }

    @Test
    public void testGetName() {
        mockSettings = new MockSettingsImpl();

        String name = "testName";
        MockSettings mockSettingsResult = mockSettings.name(name);

        assert mockSettingsResult.equals(mockSettings);
        assert mockSettingsResult.getMockName().getMockName().equals(name);
    }

    @Test
    public void testGetSpiedInstance() {
        mockSettings = new MockSettingsImpl();

        Object spiedInstance = new Object();
        MockSettings mockSettingsResult = mockSettings.spiedInstance(spiedInstance);

        assert mockSettingsResult.equals(mockSettings);
        assert mockSettingsResult.getSpiedInstance().equals(spiedInstance);
    }

    @Test
    public void testDefaultAnswer() {
        mockSettings = new MockSettingsImpl();
        Answer<Object> defaultAnswer = new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return null;
            }
        };

        MockSettings mockSettingsResult = mockSettings.defaultAnswer(defaultAnswer);

        assert mockSettingsResult.equals(mockSettings);
        assert mockSettingsResult.getDefaultAnswer().equals(defaultAnswer);
    }

    @Test
    public void testIsSerializable() {
        mockSettings = new MockSettingsImpl();

        assert !mockSettings.isSerializable();

        Class<?>[] extraInterfaces = new Class<?>[]{java.io.Serializable.class};
        mockSettings.extraInterfaces(extraInterfaces);

        assert mockSettings.isSerializable();
    }

    @Test
    public void testInitiateMockName() {
        mockSettings = new MockSettingsImpl();

        String name = "testName";
        Class classToMock = java.lang.Object.class;
        mockSettings.name(name);
        mockSettings.initiateMockName(classToMock);

        assert mockSettings.getMockName().getMockName().equals(name);
        assert mockSettings.getMockName().getTypeToMock().equals(classToMock);
    }
}