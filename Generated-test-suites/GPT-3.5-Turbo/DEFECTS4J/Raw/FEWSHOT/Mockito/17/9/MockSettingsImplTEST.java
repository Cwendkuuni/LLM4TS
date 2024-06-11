import static org.mockito.Mockito.*;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.util.MockName;
import org.mockito.stubbing.Answer;

public class MockSettingsImplTest {
    @Test
    public void testSerializable() {
        MockSettingsImpl mockSettingsImpl = new MockSettingsImpl();
        MockSettings result = mockSettingsImpl.serializable();
        Assert.assertEquals(mockSettingsImpl, result);
    }

    @Test
    public void testExtraInterfaces() {
        // Test with null extraInterfaces
        MockSettingsImpl mockSettingsImpl1 = new MockSettingsImpl();
        Reporter reporter = mock(Reporter.class);
        when(reporter.extraInterfacesRequiresAtLeastOneInterface()).thenReturn(reporter);
        mockSettingsImpl1.extraInterfaces(null);
        verify(reporter).extraInterfacesRequiresAtLeastOneInterface();

        // Test with empty extraInterfaces
        MockSettingsImpl mockSettingsImpl2 = new MockSettingsImpl();
        reporter = mock(Reporter.class);
        when(reporter.extraInterfacesRequiresAtLeastOneInterface()).thenReturn(reporter);
        mockSettingsImpl2.extraInterfaces(new Class<?>[]{});
        verify(reporter).extraInterfacesRequiresAtLeastOneInterface();

        // Test with valid extraInterfaces
        MockSettingsImpl mockSettingsImpl3 = new MockSettingsImpl();
        Class<?>[] extraInterfaces = {MockName.class};
        mockSettingsImpl3.extraInterfaces(extraInterfaces);
        Assert.assertArrayEquals(extraInterfaces, mockSettingsImpl3.getExtraInterfaces());

        // Test with null extraInterfaces element
        MockSettingsImpl mockSettingsImpl4 = new MockSettingsImpl();
        reporter = mock(Reporter.class);
        when(reporter.extraInterfacesDoesNotAcceptNullParameters()).thenReturn(reporter);
        mockSettingsImpl4.extraInterfaces(new Class<?>[]{null});
        verify(reporter).extraInterfacesDoesNotAcceptNullParameters();

        // Test with non-interface extraInterfaces element
        MockSettingsImpl mockSettingsImpl5 = new MockSettingsImpl();
        reporter = mock(Reporter.class);
        when(reporter.extraInterfacesAcceptsOnlyInterfaces(Mockito.any(Class.class))).thenReturn(reporter);
        mockSettingsImpl5.extraInterfaces(new Class<?>[]{String.class});
        verify(reporter).extraInterfacesAcceptsOnlyInterfaces(String.class);
    }

    @Test
    public void testGetMockName() {
        MockSettingsImpl mockSettingsImpl = new MockSettingsImpl();
        MockName mockName = mock(MockName.class);
        mockSettingsImpl.mockName = mockName;
        Assert.assertEquals(mockName, mockSettingsImpl.getMockName());
    }

    @Test
    public void testGetExtraInterfaces() {
        MockSettingsImpl mockSettingsImpl = new MockSettingsImpl();
        Class<?>[] extraInterfaces = {MockName.class};
        mockSettingsImpl.extraInterfaces(extraInterfaces);
        Assert.assertArrayEquals(extraInterfaces, mockSettingsImpl.getExtraInterfaces());
    }

    @Test
    public void testGetSpiedInstance() {
        MockSettingsImpl mockSettingsImpl = new MockSettingsImpl();
        Object spiedInstance = new Object();
        mockSettingsImpl.spiedInstance(spiedInstance);
        Assert.assertEquals(spiedInstance, mockSettingsImpl.getSpiedInstance());
    }

    @Test
    public void testName() {
        MockSettingsImpl mockSettingsImpl = new MockSettingsImpl();
        String name = "test";
        MockSettings result = mockSettingsImpl.name(name);
        Assert.assertEquals(mockSettingsImpl, result);
        Assert.assertEquals(name, mockSettingsImpl.name);
    }

    @Test
    public void testSpiedInstance() {
        MockSettingsImpl mockSettingsImpl = new MockSettingsImpl();
        Object spiedInstance = new Object();
        MockSettings result = mockSettingsImpl.spiedInstance(spiedInstance);
        Assert.assertEquals(mockSettingsImpl, result);
        Assert.assertEquals(spiedInstance, mockSettingsImpl.spiedInstance);
    }

    @Test
    public void testDefaultAnswer() {
        MockSettingsImpl mockSettingsImpl = new MockSettingsImpl();
        Answer<Object> defaultAnswer = mock(Answer.class);
        MockSettings result = mockSettingsImpl.defaultAnswer(defaultAnswer);
        Assert.assertEquals(mockSettingsImpl, result);
        Assert.assertEquals(defaultAnswer, mockSettingsImpl.defaultAnswer);
    }

    @Test
    public void testGetDefaultAnswer() {
        MockSettingsImpl mockSettingsImpl = new MockSettingsImpl();
        Answer<Object> defaultAnswer = mock(Answer.class);
        mockSettingsImpl.defaultAnswer = defaultAnswer;
        Assert.assertEquals(defaultAnswer, mockSettingsImpl.getDefaultAnswer());
    }

    @Test
    public void testIsSerializable() {
        MockSettingsImpl mockSettingsImpl = new MockSettingsImpl();
        mockSettingsImpl.extraInterfaces = new Class<?>[]{java.io.Serializable.class};
        Assert.assertTrue(mockSettingsImpl.isSerializable());

        mockSettingsImpl.extraInterfaces = new Class<?>[]{String.class};
        Assert.assertFalse(mockSettingsImpl.isSerializable());
    }

    @Test
    public void testInitiateMockName() {
        MockSettingsImpl mockSettingsImpl = new MockSettingsImpl();
        String name = "test";
        Class classToMock = String.class;
        mockSettingsImpl.initiateMockName(classToMock);
        Assert.assertEquals(mockSettingsImpl.name, name);
    }
}