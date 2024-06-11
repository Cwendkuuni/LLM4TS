
import org.junit.Test;
import org.junit.Assert;

public class MockSettingsImplTest {

    @Test
    public void testSerializable() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();

        // Verify that isSerializable returns false when no extra interfaces are set
        Assert.assertFalse(mockSettings.isSerializable());

        // Call serializable to set extra interfaces
        MockSettingsImpl serializableMockSettings = mockSettings.serializable();

        // Verify that isSerializable returns true when extra interfaces are set
        Assert.assertTrue(serializableMockSettings.isSerializable());
    }

    @Test
    public void testExtraInterfaces() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();

        // Verify that extraInterfaces method throws an exception when no interfaces are provided
        try {
            mockSettings.extraInterfaces();
            Assert.fail("Expected exception not thrown");
        } catch (Exception e) {
            // Exception expected
        }

        // Verify that extraInterfaces method throws an exception when null parameter is provided
        try {
            mockSettings.extraInterfaces(null);
            Assert.fail("Expected exception not thrown");
        } catch (Exception e) {
            // Exception expected
        }

        // Verify that extraInterfaces method throws an exception when a non-interface class is provided
        try {
            mockSettings.extraInterfaces(String.class);
            Assert.fail("Expected exception not thrown");
        } catch (Exception e) {
            // Exception expected
        }

        // Verify that extraInterfaces method sets the extra interfaces correctly
        Class<?>[] interfaces = new Class<?>[] {Comparable.class, Cloneable.class};
        MockSettingsImpl mockSettingsWithInterfaces = mockSettings.extraInterfaces(interfaces);
        Assert.assertArrayEquals(interfaces, mockSettingsWithInterfaces.getExtraInterfaces());
    }

    @Test
    public void testGetName() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();

        // Verify that getName returns null when no name is set
        Assert.assertNull(mockSettings.getMockName());

        // Set a name and verify that it is returned correctly
        String name = "mockName";
        MockSettingsImpl mockSettingsWithName = mockSettings.name(name);
        Assert.assertEquals(name, mockSettingsWithName.getMockName());
    }

    @Test
    public void testSpiedInstance() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();

        // Verify that getSpiedInstance returns null when no spied instance is set
        Assert.assertNull(mockSettings.getSpiedInstance());

        // Set a spied instance and verify that it is returned correctly
        Object spiedInstance = new Object();
        MockSettingsImpl mockSettingsWithInstance = mockSettings.spiedInstance(spiedInstance);
        Assert.assertEquals(spiedInstance, mockSettingsWithInstance.getSpiedInstance());
    }

    @Test
    public void testDefaultAnswer() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();

        // Verify that getDefaultAnswer returns null when no default answer is set
        Assert.assertNull(mockSettings.getDefaultAnswer());

        // Set a default answer and verify that it is returned correctly
        Answer<Object> defaultAnswer = new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return null;
            }
        };
        MockSettingsImpl mockSettingsWithAnswer = mockSettings.defaultAnswer(defaultAnswer);
        Assert.assertEquals(defaultAnswer, mockSettingsWithAnswer.getDefaultAnswer());
    }

    @Test
    public void testInitiateMockName() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();

        // Set a name and verify that it is used in the mock name
        String name = "mockName";
        Class<?> classToMock = String.class;
        mockSettings.name(name);
        mockSettings.initiateMockName(classToMock);
        Assert.assertEquals(name, mockSettings.getMockName());

        // Verify that the class to mock is used in the mock name
        Assert.assertEquals(classToMock, mockSettings.getMockName().getMockedType());
    }

}
