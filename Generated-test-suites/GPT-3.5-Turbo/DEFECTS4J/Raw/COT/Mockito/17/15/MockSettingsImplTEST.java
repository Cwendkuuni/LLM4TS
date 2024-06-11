import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;

public class MockSettingsImplTest {

    private MockSettingsImpl mockSettings;

    @Before
    public void setUp() {
        mockSettings = new MockSettingsImpl();
    }

    @Test
    public void testSerializable() {
        MockSettingsImpl result = mockSettings.serializable();
        Assert.assertSame(mockSettings, result);
    }

    @Test
    public void testExtraInterfaces() {
        Class<?>[] extraInterfaces = {MockSettings.class};
        MockSettingsImpl result = mockSettings.extraInterfaces(extraInterfaces);
        Assert.assertSame(mockSettings, result);
        Assert.assertArrayEquals(extraInterfaces, mockSettings.getExtraInterfaces());
    }

    @Test
    public void testExtraInterfacesWithNull() {
        try {
            mockSettings.extraInterfaces(null);
        } catch (Exception e) {
            Assert.assertTrue(e instanceof Reporter);
        }
    }

    @Test
    public void testExtraInterfacesWithNonInterface() {
        try {
            Class<?>[] extraInterfaces = {String.class};
            mockSettings.extraInterfaces(extraInterfaces);
        } catch (Exception e) {
            Assert.assertTrue(e instanceof Reporter);
        }
    }

    @Test
    public void testGetMockName() {
        Assert.assertNull(mockSettings.getMockName());
    }

    @Test
    public void testGetName() {
        String name = "mockName";
        MockSettingsImpl result = mockSettings.name(name);
        Assert.assertSame(mockSettings, result);
        Assert.assertEquals(name, mockSettings.getMockName());
    }

    @Test
    public void testGetSpiedInstance() {
        Object spiedInstance = new Object();
        MockSettingsImpl result = mockSettings.spiedInstance(spiedInstance);
        Assert.assertSame(mockSettings, result);
        Assert.assertEquals(spiedInstance, mockSettings.getSpiedInstance());
    }

    @Test
    public void testDefaultAnswer() {
        Answer<Object> defaultAnswer = null;
        MockSettingsImpl result = mockSettings.defaultAnswer(defaultAnswer);
        Assert.assertSame(mockSettings, result);
        Assert.assertEquals(defaultAnswer, mockSettings.getDefaultAnswer());
    }

    @Test
    public void testIsSerializable() {
        Assert.assertFalse(mockSettings.isSerializable());
        Class<?>[] extraInterfaces = {java.io.Serializable.class};
        mockSettings.extraInterfaces(extraInterfaces);
        Assert.assertTrue(mockSettings.isSerializable());
    }

    @Test
    public void testInitiateMockName() {
        String name = "mockName";
        Class<?> classToMock = MockSettingsImpl.class;
        MockSettingsImpl result = mockSettings.initiateMockName(classToMock);
        Assert.assertSame(mockSettings, result);
        Assert.assertNotNull(mockSettings.getMockName());
    }

}