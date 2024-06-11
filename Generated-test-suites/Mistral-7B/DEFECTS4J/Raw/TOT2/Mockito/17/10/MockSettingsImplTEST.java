package org.mockito.internal.creation;

import org.junit.Test;
import org.mockito.MockSettings;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.util.MockName;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.*;

public class MockSettingsImplTest {

    @Test
    public void testSerializable() {
        MockSettingsImpl mockSettings = new MockSettingsImpl().serializable();
        assertTrue(mockSettings.isSerializable());
    }

    @Test
    public void testEmptyExtraInterfaces() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        mockSettings.extraInterfaces(new Class[0]);
        new Reporter().expectExtraInterfacesRequiresAtLeastOneInterface();
    }

    @Test
    public void testNullExtraInterface() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        mockSettings.extraInterfaces(new Class[]{null});
        new Reporter().expectExtraInterfacesDoesNotAcceptNullParameters();
    }

    @Test
    public void testNonInterfaceExtraInterface() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        mockSettings.extraInterfaces(new Class[]{String.class});
        new Reporter().expectExtraInterfacesAcceptsOnlyInterfaces(String.class);
    }

    @Test
    public void testNullSpiedInstance() {
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        mockSettings.spiedInstance(null);
        new Reporter().spiedInstanceCannotBeNull();
    }

    @Test
    public void testNullName() {
        MockSettingsImpl mockSettings = new MockSettingsImpl().name(null);
        new Reporter().nameCannotBeNull();
    }

    // Add other test cases if necessary
}