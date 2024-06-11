import org.junit.Test;
import org.junit.Assert;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.util.MockName;
import org.mockito.stubbing.Answer;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Arrays;

public class MockSettingsImplTest {

    @Test
    public void testSerializable() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Assert.assertEquals(mockSettings, mockSettings.serializable());

        Class<?>[] extraInterfaces = mockSettings.getExtraInterfaces();
        Assert.assertTrue(extraInterfaces != null && Arrays.asList(extraInterfaces).contains(Serializable.class));
    }

    @Test
    public void testExtraInterfaces() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();

        // Test with null extraInterfaces
        Reporter reporter = new Reporter();
        try {
            mockSettings.extraInterfaces(null);
            Assert.fail("Expected exception not thrown.");
        } catch (NullPointerException e) {
            // Expected
            Assert.assertEquals("extraInterfacesDoesNotAcceptNullParameters", reporter.getMethodName(e));
        }

         // Test with no extraInterfaces
        reporter = new Reporter();
        try {
            mockSettings.extraInterfaces();
            Assert.fail("Expected exception not thrown.");
        } catch (NullPointerException e) {
             // Expected
            Assert.assertEquals("extraInterfacesRequiresAtLeastOneInterface", reporter.getMethodName(e));
        }

         // Test with invalid extraInterfaces
         reporter = new Reporter();
         try {
            mockSettings.extraInterfaces(String.class);
            Assert.fail("Expected exception not thrown.");
        } catch (IllegalArgumentException e) {
            // Expected
            Assert.assertEquals("extraInterfacesAcceptsOnlyInterfaces", reporter.getMethodName(e));
        }

         // Test with valid extraInterfaces
         mockSettings.extraInterfaces(Serializable.class);
         Class<?>[] extraInterfaces = mockSettings.getExtraInterfaces();
         Assert.assertTrue(extraInterfaces != null && extraInterfaces.length == 1 && extraInterfaces[0] == Serializable.class);
    }

    @Test
    public void testGetMockName() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        mockSettings.initiateMockName(Object.class);
        MockName mockName = mockSettings.getMockName();
        Assert.assertEquals(mockName, mockSettings.getMockName());
    }

    @Test
    public void testGetName() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        String name = "mockName";
        mockSettings.name(name);
        Assert.assertEquals(name, mockSettings.getMockName().toString());
    }

    @Test
    public void testGetSpiedInstance() {
        Object spiedInstance = new Object();
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        mockSettings.spiedInstance(spiedInstance);
        Assert.assertEquals(spiedInstance, mockSettings.getSpiedInstance());
    }

    @Test
    public void testDefaultAnswer() {
        Answer<Object> defaultAnswer = new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return null;
            }
        };
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        mockSettings.defaultAnswer(defaultAnswer);
        Assert.assertEquals(defaultAnswer, mockSettings.getDefaultAnswer());
    }

    @Test
    public void testIsSerializable() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Assert.assertFalse(mockSettings.isSerializable());

        Class<?>[] extraInterfaces = new Class[]{Serializable.class};
        mockSettings.extraInterfaces(extraInterfaces);
        Assert.assertTrue(mockSettings.isSerializable());
    }

    @Test
    public void testInitiateMockName() throws NoSuchFieldException, IllegalAccessException {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        mockSettings.initiateMockName(MockSettingsImpl.class);

        Field mockNameField = MockSettingsImpl.class.getDeclaredField("mockName");
        mockNameField.setAccessible(true);
        MockName mockName = (MockName) mockNameField.get(mockSettings);

        Assert.assertEquals(MockSettingsImpl.class, mockName.getMockType());
    }

}