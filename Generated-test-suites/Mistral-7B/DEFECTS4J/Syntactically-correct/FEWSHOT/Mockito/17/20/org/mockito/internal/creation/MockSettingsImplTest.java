package org.mockito.internal.creation;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.exceptions.base.MockitoException.classIsNotAnInterface;
import static org.mockito.exceptions.base.MockitoException.nullParameter;

import org.junit.Test;
import org.mockito.MockSettings;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.util.MockName;
import org.mockito.stubbing.Answer;

public class MockSettingsImplTest {

    @Test
    public void testSerializable() {
        MockSettings serializableSettings = new MockSettingsImpl().serializable();
        assertSame(serializableSettings, new MockSettingsImpl().serializable());
        assertArrayEquals(new Class<?>[]{java.io.Serializable.class}, serializableSettings.getExtraInterfaces());
    }

    @Test(expected = MockitoException.class)
    public void testExtraInterfacesRequiresAtLeastOneInterface() {
        new MockSettingsImpl().extraInterfaces();
    }

    @Test
    public void testExtraInterfacesWithNull() {
        try {
            new MockSettingsImpl().extraInterfaces(null);
            new Reporter().extraInterfacesDoesNotAcceptNullParameters();
        } catch (MockitoException e) {
            // expected
        }
    }

    @Test(expected = MockitoException.class)
    public void testExtraInterfacesAcceptsOnlyInterfaces() {
        new MockSettingsImpl().extraInterfaces(String.class);
        new Reporter().extraInterfacesAcceptsOnlyInterfaces(String.class);
    }

    @Test
    public void testExtraInterfacesWithMultipleInterfaces() {
        MockSettings settings = new MockSettingsImpl()
                .extraInterfaces(java.io.Serializable.class, java.util.List.class);
        assertArrayEquals(new Class<?>[]{java.io.Serializable.class, java.util.List.class}, settings.getExtraInterfaces());
    }

    @Test(expected = MockitoException.class)
    public void testExtraInterfacesWithNonInterface() {
        new MockSettingsImpl().extraInterfaces(String.class);
        new Reporter().extraInterfacesAcceptsOnlyInterfaces(String.class);
    }

    @Test
    public void testGetMockName() {
        MockSettings settings = new MockSettingsImpl().name("MockitoTest").initiateMockName(String.class);
        assertSame(settings.getMockName(), new MockName("MockitoTest", String.class));
    }

    @Test
    public void testGetSpiedInstance() {
        Object spiedInstance = new Object();
        MockSettings settings = new MockSettingsImpl().spiedInstance(spiedInstance);
        assertSame(settings.getSpiedInstance(), spiedInstance);
    }

    @Test
    public void testgetDefaultAnswer() {
        Answer answer = mock(Answer.class);
        MockSettings settings = new MockSettingsImpl().defaultAnswer(answer);
        assertSame(settings.getDefaultAnswer(), answer);
    }

    @Test
    public void testIsSerializable() {
        MockSettings settings = new MockSettingsImpl().extraInterfaces(java.io.Serializable.class);
        assertTrue(settings.isSerializable());

        MockSettings settingsWithoutSerializable = new MockSettingsImpl();
        assertFalse(settingsWithoutSerializable.isSerializable());
    }
}