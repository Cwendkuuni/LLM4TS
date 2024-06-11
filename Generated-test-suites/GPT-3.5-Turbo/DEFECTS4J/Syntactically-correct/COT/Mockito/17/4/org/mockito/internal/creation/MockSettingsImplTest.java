import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.util.MockName;
import org.mockito.stubbing.Answer;

public class MockSettingsImplTest {
    private MockSettingsImpl mockSettings;

    @Before
    public void setUp() {
        mockSettings = new MockSettingsImpl();
    }

    @Test
    public void testSerializable() {
        mockSettings = mockSettings.serializable();
        Assert.assertTrue(mockSettings.isSerializable());
    }

    @Test
    public void testExtraInterfaces() {
        Class<?>[] extraInterfaces = new Class<?>[]{Comparable.class, Cloneable.class};
        mockSettings = mockSettings.extraInterfaces(extraInterfaces);
        Assert.assertArrayEquals(extraInterfaces, mockSettings.getExtraInterfaces());
    }

    @Test
    public void testExtraInterfacesWithNull() {
        try {
            mockSettings.extraInterfaces((Class<?>[]) null);
            Assert.fail("Expected an exception to be thrown");
        } catch (Exception e) {
            Assert.assertTrue(e instanceof IllegalArgumentException);
        }
    }

    @Test
    public void testExtraInterfacesWithNonInterface() {
        try {
            mockSettings.extraInterfaces(String.class);
            Assert.fail("Expected an exception to be thrown");
        } catch (Exception e) {
            Assert.assertTrue(e instanceof IllegalArgumentException);
        }
    }

    @Test
    public void testGetMockName() {
        MockName mockName = new MockName("mockName", MockSettingsImpl.class);
        mockSettings.name("mockName");
        Assert.assertEquals(mockName, mockSettings.getMockName());
    }

    @Test
    public void testGetSpiedInstance() {
        Object spiedInstance = new Object();
        mockSettings.spiedInstance(spiedInstance);
        Assert.assertEquals(spiedInstance, mockSettings.getSpiedInstance());
    }

    @Test
    public void testGetName() {
        String name = "mockName";
        mockSettings.name(name);
        Assert.assertEquals(name, mockSettings.getMockName().toString());
    }

    @Test
    public void testDefaultAnswer() {
        Answer<Object> defaultAnswer = invocation -> null;
        mockSettings.defaultAnswer(defaultAnswer);
        Assert.assertEquals(defaultAnswer, mockSettings.getDefaultAnswer());
    }

    @Test
    public void testIsSerializable() {
        Assert.assertFalse(mockSettings.isSerializable());
        mockSettings.extraInterfaces(java.io.Serializable.class);
        Assert.assertTrue(mockSettings.isSerializable());
    }

    @Test
    public void testInitiateMockName() {
        Class<?> classToMock = MockSettingsImplTest.class;
        mockSettings.initiateMockName(classToMock);
        Assert.assertEquals(classToMock.getSimpleName(), mockSettings.getMockName().toString());
    }
}