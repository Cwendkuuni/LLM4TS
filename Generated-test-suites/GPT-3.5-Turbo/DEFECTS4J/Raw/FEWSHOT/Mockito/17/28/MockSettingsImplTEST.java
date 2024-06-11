package org.mockito.internal.creation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

public class MockSettingsImplTest {

    private MockSettingsImpl mockSettings;

    @Before
    public void setUp() {
        mockSettings = new MockSettingsImpl();
    }

    @Test
    public void testSerializable() {
        MockSettingsImpl serializableMockSettings = mockSettings.serializable();

        assertTrue(serializableMockSettings.isSerializable());
    }

    @Test
    public void testExtraInterfaces_withNullArray() {
        MockSettingsImpl mockSettingsWithNullArray = mockSettings.extraInterfaces((Class<?>[]) null);

        assertNotNull(mockSettingsWithNullArray.getExtraInterfaces());
        assertEquals(0, mockSettingsWithNullArray.getExtraInterfaces().length);
    }

    @Test
    public void testExtraInterfaces_withEmptyArray() {
        MockSettingsImpl mockSettingsWithEmptyArray = mockSettings.extraInterfaces();

        assertNotNull(mockSettingsWithEmptyArray.getExtraInterfaces());
        assertEquals(0, mockSettingsWithEmptyArray.getExtraInterfaces().length);
    }

    @Test
    public void testExtraInterfaces_withValidInterfaces() {
        Class<?>[] extraInterfaces = new Class<?>[]{Comparable.class, AutoCloseable.class};
        MockSettingsImpl mockSettingsWithValidInterfaces = mockSettings.extraInterfaces(extraInterfaces);

        assertEquals(extraInterfaces.length, mockSettingsWithValidInterfaces.getExtraInterfaces().length);
        for (Class<?> i : extraInterfaces) {
            assertTrue(Arrays.asList(mockSettingsWithValidInterfaces.getExtraInterfaces()).contains(i));
        }
    }

    @Test(expected = Reporter.extraInterfacesRequiresAtLeastOneInterfaceException.class)
    public void testExtraInterfaces_withNoInterfaces() {
        mockSettings.extraInterfaces(new Class<?>[]{});
    }

    @Test(expected = Reporter.extraInterfacesDoesNotAcceptNullException.class)
    public void testExtraInterfaces_withNullInterface() {
        mockSettings.extraInterfaces((Class<?>) null);
    }

    @Test(expected = Reporter.extraInterfacesAcceptsOnlyInterfacesException.class)
    public void testExtraInterfaces_withNonInterface() {
        mockSettings.extraInterfaces(Object.class);
    }

    @Test
    public void testGetMockName() {
        MockName mockName = Mockito.mock(MockName.class);
        mockSettings.initiateMockName(String.class);
        Mockito.when(mockSettings.getMockName()).thenReturn(mockName);

        assertEquals(mockName, mockSettings.getMockName());
    }

    @Test
    public void testGetSpiedInstance() {
        Object spiedInstance = mock(Object.class);
        mockSettings.spiedInstance(spiedInstance);

        assertEquals(spiedInstance, mockSettings.getSpiedInstance());
    }

    @Test
    public void testGetName() {
        String name = "mockName";
        mockSettings.name(name);

        assertEquals(name, mockSettings.getMockName().toString());
    }

    @Test
    public void testDefaultAnswer() {
        Answer<Object> defaultAnswer = mock(Answer.class);
        mockSettings.defaultAnswer(defaultAnswer);

        assertEquals(defaultAnswer, mockSettings.getDefaultAnswer());
    }

    @Test
    public void testIsSerializable_withSerializableInterface() {
        Class<?>[] extraInterfaces = new Class<?>[]{Comparable.class, java.io.Serializable.class};
        MockSettingsImpl mockSettingsWithSerializableInterface = mockSettings.extraInterfaces(extraInterfaces);

        assertTrue(mockSettingsWithSerializableInterface.isSerializable());
    }

    @Test
    public void testIsSerializable_withoutSerializableInterface() {
        Class<?>[] extraInterfaces = new Class<?>[]{Comparable.class};
        MockSettingsImpl mockSettingsWithoutSerializableInterface = mockSettings.extraInterfaces(extraInterfaces);

        assertTrue(!mockSettingsWithoutSerializableInterface.isSerializable());
    }
}