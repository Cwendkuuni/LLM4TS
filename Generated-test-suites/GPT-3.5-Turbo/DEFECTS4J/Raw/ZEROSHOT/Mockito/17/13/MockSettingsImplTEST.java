import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.util.MockName;
import org.mockito.stubbing.Answer;

import java.io.Serializable;

public class MockSettingsImplTest {

    private MockSettingsImpl mockSettings;

    @Before
    public void setUp() {
        mockSettings = new MockSettingsImpl();
    }

    @Test
    public void testSerializable() {
        MockSettings result = mockSettings.serializable();
        Assert.assertEquals(true, result.isSerializable());
    }

    @Test
    public void testExtraInterfaces() {
        Class<?>[] extraInterfaces = {Serializable.class};
        MockSettings result = mockSettings.extraInterfaces(extraInterfaces);
        Assert.assertArrayEquals(extraInterfaces, result.getExtraInterfaces());
    }

    @Test(expected = NullPointerException.class)
    public void testExtraInterfacesWithNull() {
        Class<?>[] extraInterfaces = null;
        mockSettings.extraInterfaces(extraInterfaces);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExtraInterfacesWithEmpty() {
        Class<?>[] extraInterfaces = {};
        mockSettings.extraInterfaces(extraInterfaces);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExtraInterfacesWithNonInterface() {
        Class<?>[] extraInterfaces = {MockSettingsImpl.class};
        mockSettings.extraInterfaces(extraInterfaces);
    }

    @Test
    public void testGetMockName() {
        MockName mockName = mockSettings.getMockName();
        Assert.assertNull(mockName);
    }

    @Test
    public void testGetName() {
        String name = "mock";
        MockSettings result = mockSettings.name(name);
        Assert.assertEquals(name, result.getMockName().getMockName());
    }

    @Test
    public void testGetExtraInterfaces() {
        Class<?>[] extraInterfaces = {Serializable.class};
        mockSettings.extraInterfaces(extraInterfaces);
        Class<?>[] result = mockSettings.getExtraInterfaces();
        Assert.assertArrayEquals(extraInterfaces, result);
    }

    @Test
    public void testGetSpiedInstance() {
        Object spiedInstance = new Object();
        MockSettings result = mockSettings.spiedInstance(spiedInstance);
        Assert.assertEquals(spiedInstance, result.getSpiedInstance());
    }

    @Test
    public void testDefaultAnswer() {
        Answer<Object> defaultAnswer = Mockito.mock(Answer.class);
        MockSettings result = mockSettings.defaultAnswer(defaultAnswer);
        Assert.assertEquals(defaultAnswer, result.getDefaultAnswer());
    }

    @Test
    public void testIsSerializable() {
        Class<?>[] extraInterfaces = {Serializable.class};
        mockSettings.extraInterfaces(extraInterfaces);
        boolean result = mockSettings.isSerializable();
        Assert.assertEquals(true, result);
    }

    @Test
    public void testInitiateMockName() {
        Class<?> classToMock = Object.class;
        mockSettings.initiateMockName(classToMock);
        MockName mockName = mockSettings.getMockName();
        Assert.assertNotNull(mockName);
        Assert.assertEquals(classToMock, mockName.getMockType());
    }
}