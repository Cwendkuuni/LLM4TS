package org.mockito.internal.creation;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.util.MockName;
import org.mockito.stubbing.Answer;

import java.io.Serializable;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

public class MockSettingsImplTest {
    
    @Test
    public void testSerializable() {
        MockSettingsImpl settings = new MockSettingsImpl();
        MockSettingsImpl returnedSettings = settings.serializable();
        
        assertTrue(returnedSettings.isSerializable());
    }
    
    @Test
    public void testExtraInterfaces() {
        MockSettingsImpl settings = new MockSettingsImpl();
        Class<?>[] extraInterfaces = {Serializable.class};
        MockSettingsImpl returnedSettings = settings.extraInterfaces(extraInterfaces);
        
        assertArrayEquals(extraInterfaces, returnedSettings.getExtraInterfaces());
    }
    
    @Test
    public void testExtraInterfacesNullCheck() {
        MockSettingsImpl settings = new MockSettingsImpl();
        Reporter reporter = Mockito.mock(Reporter.class);
        
        settings.extraInterfaces((Class<?>[]) null);
        
        Mockito.verify(reporter).extraInterfacesRequiresAtLeastOneInterface();
    }
    
    @Test
    public void testExtraInterfacesNullParameterCheck() {
        MockSettingsImpl settings = new MockSettingsImpl();
        Reporter reporter = Mockito.mock(Reporter.class);
        
        settings.extraInterfaces((Class<?>) null);
        
        Mockito.verify(reporter).extraInterfacesDoesNotAcceptNullParameters();
    }
    
    @Test
    public void testExtraInterfacesOnlyInterfacesCheck() {
        MockSettingsImpl settings = new MockSettingsImpl();
        Reporter reporter = Mockito.mock(Reporter.class);
        
        settings.extraInterfaces(Integer.class);
        
        Mockito.verify(reporter).extraInterfacesAcceptsOnlyInterfaces(eq(Integer.class));
    }
    
    @Test
    public void testGetMockName() {
        MockSettingsImpl settings = new MockSettingsImpl();
        MockName mockName = Mockito.mock(MockName.class);
        settings.initiateMockName(MockSettingsImpl.class);
        
        Mockito.when(mockName.getName()).thenReturn("mockName");
        Mockito.when(mockName.getTestClass()).thenReturn(MockSettingsImplTest.class);
        
        settings.mockName = mockName;
        
        assertEquals(mockName, settings.getMockName());
    }
    
    @Test
    public void testGetName() {
        MockSettingsImpl settings = new MockSettingsImpl();
        String name = "testName";
        settings.name(name);
        
        assertEquals(name, settings.getMockName().toString());
    }
    
    @Test
    public void testGetExtraInterfaces() {
        MockSettingsImpl settings = new MockSettingsImpl();
        Class<?>[] extraInterfaces = {Serializable.class};
        settings.extraInterfaces(extraInterfaces);
        
        assertArrayEquals(extraInterfaces, settings.getExtraInterfaces());
    }
    
    @Test
    public void testGetSpiedInstance() {
        MockSettingsImpl settings = new MockSettingsImpl();
        Object spiedInstance = new Object();
        settings.spiedInstance(spiedInstance);
        
        assertEquals(spiedInstance, settings.getSpiedInstance());
    }
    
    @Test
    public void testDefaultAnswer() {
        MockSettingsImpl settings = new MockSettingsImpl();
        Answer<Object> defaultAnswer = (invocation) -> null;
        MockSettingsImpl returnedSettings = settings.defaultAnswer(defaultAnswer);
        
        assertEquals(defaultAnswer, returnedSettings.getDefaultAnswer());
    }
    
    @Test
    public void testIsSerializable() {
        MockSettingsImpl settings = new MockSettingsImpl();
        Class<?>[] extraInterfaces = {Serializable.class};
        settings.extraInterfaces(extraInterfaces);
        
        assertTrue(settings.isSerializable());
    }
    
    @Test
    public void testInitiateMockName() {
        MockSettingsImpl settings = new MockSettingsImpl();
        MockName mockName = Mockito.mock(MockName.class);
        settings.mockName = mockName;
        
        assertEquals(mockName, settings.getMockName());
        Mockito.verify(mockName, Mockito.never()).getName();
        Mockito.verify(mockName, Mockito.never()).getTestClass();
    }
}