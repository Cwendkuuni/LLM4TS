package org.mockito.internal.creation;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.MockSettings;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.util.MockName;
import org.mockito.stubbing.Answer;

public class MockSettingsImplTest {
    
    // Helper method to create a MockSettingsImpl instance with default values
    private MockSettingsImpl createMockSettingsImpl() {
        return new MockSettingsImpl();
    }
    
    @Test
    public void testSerializable() {
        MockSettingsImpl mockSettingsImpl = createMockSettingsImpl();

        MockSettings result = mockSettingsImpl.serializable();

        assertTrue(result.isSerializable());
    }
    
    @Test
    public void testExtraInterfaces_whenExtraInterfacesIsNull_thenThrowsReporterException() {
        MockSettingsImpl mockSettingsImpl = createMockSettingsImpl();
        Reporter reporter = mock(Reporter.class);
        mockSettingsImpl.setReporter(reporter);

        try {
            mockSettingsImpl.extraInterfaces(null);
            fail("Should throw Reporter exception");
        } catch (Exception e) {
            verify(reporter).extraInterfacesRequiresAtLeastOneInterface();
        }
    }

    @Test
    public void testExtraInterfaces_whenExtraInterfacesIsEmpty_thenThrowsReporterException() {
        MockSettingsImpl mockSettingsImpl = createMockSettingsImpl();
        Reporter reporter = mock(Reporter.class);
        mockSettingsImpl.setReporter(reporter);

        try {
            mockSettingsImpl.extraInterfaces(new Class<?>[]{});
            fail("Should throw Reporter exception");
        } catch (Exception e) {
            verify(reporter).extraInterfacesRequiresAtLeastOneInterface();
        }
    }

    @Test
    public void testExtraInterfaces_whenExtraInterfacesContainsNull_thenThrowsReporterException() {
        MockSettingsImpl mockSettingsImpl = createMockSettingsImpl();
        Reporter reporter = mock(Reporter.class);
        mockSettingsImpl.setReporter(reporter);

        try {
            mockSettingsImpl.extraInterfaces(new Class<?>[]{null});
            fail("Should throw Reporter exception");
        } catch (Exception e) {
            verify(reporter).extraInterfacesDoesNotAcceptNullParameters();
        }
    }

    @Test
    public void testExtraInterfaces_whenExtraInterfacesContainsNonInterfaceClass_thenThrowsReporterException() {
        MockSettingsImpl mockSettingsImpl = createMockSettingsImpl();
        Reporter reporter = mock(Reporter.class);
        mockSettingsImpl.setReporter(reporter);

        try {
            mockSettingsImpl.extraInterfaces(new Class<?>[]{String.class});
            fail("Should throw Reporter exception");
        } catch (Exception e) {
            verify(reporter).extraInterfacesAcceptsOnlyInterfaces(String.class);
        }
    }

    @Test
    public void testExtraInterfaces_whenExtraInterfacesContainsInterfaceClass_thenReturnMockSettingsWithExtraInterfaces() {
        MockSettingsImpl mockSettingsImpl = createMockSettingsImpl();
        Class<?>[] extraInterfaces = new Class<?>[]{Serializable.class};

        MockSettings result = mockSettingsImpl.extraInterfaces(extraInterfaces);

        assertArrayEquals(extraInterfaces, result.getExtraInterfaces());
    }
    
    @Test
    public void testGetMockName() {
        MockSettingsImpl mockSettingsImpl = createMockSettingsImpl();
        MockName mockName = new MockName();
        mockSettingsImpl.setMockName(mockName);

        MockName result = mockSettingsImpl.getMockName();

        assertEquals(mockName, result);
    }
    
    @Test
    public void testGetExtraInterfaces() {
        MockSettingsImpl mockSettingsImpl = createMockSettingsImpl();
        Class<?>[] extraInterfaces = new Class<?>[]{Cloneable.class};
        mockSettingsImpl.setExtraInterfaces(extraInterfaces);

        Class<?>[] result = mockSettingsImpl.getExtraInterfaces();

        assertArrayEquals(extraInterfaces, result);
    }
    
    @Test
    public void testGetSpiedInstance() {
        MockSettingsImpl mockSettingsImpl = createMockSettingsImpl();
        Object spiedInstance = new Object();
        mockSettingsImpl.setSpiedInstance(spiedInstance);

        Object result = mockSettingsImpl.getSpiedInstance();

        assertEquals(spiedInstance, result);
    }
    
    @Test
    public void testName() {
        MockSettingsImpl mockSettingsImpl = createMockSettingsImpl();
        String name = "mockName";

        MockSettings result = mockSettingsImpl.name(name);

        assertEquals(name, result.getMockName().toString());
    }
    
    @Test
    public void testSpiedInstance() {
        MockSettingsImpl mockSettingsImpl = createMockSettingsImpl();
        Object spiedInstance = new Object();

        MockSettings result = mockSettingsImpl.spiedInstance(spiedInstance);

        assertEquals(spiedInstance, result.getSpiedInstance());
    }
    
    @Test
    public void testDefaultAnswer() {
        MockSettingsImpl mockSettingsImpl = createMockSettingsImpl();
        Answer<Object> defaultAnswer = mock(Answer.class);

        MockSettings result = mockSettingsImpl.defaultAnswer(defaultAnswer);

        assertEquals(defaultAnswer, result.getDefaultAnswer());
    }
    
    @Test
    public void testIsSerializable_whenExtraInterfacesIsNull_thenReturnFalse() {
        MockSettingsImpl mockSettingsImpl = createMockSettingsImpl();

        boolean result = mockSettingsImpl.isSerializable();

        assertFalse(result);
    }
    
    @Test
    public void testIsSerializable_whenExtraInterfacesContainsSerializableInterface_thenReturnTrue() {
        MockSettingsImpl mockSettingsImpl = createMockSettingsImpl();
        Class<?>[] extraInterfaces = new Class<?>[]{Serializable.class};
        mockSettingsImpl.setExtraInterfaces(extraInterfaces);

        boolean result = mockSettingsImpl.isSerializable();

        assertTrue(result);
    }
    
    @Test
    public void testInitiateMockName() {
        MockSettingsImpl mockSettingsImpl = createMockSettingsImpl();
        Class<?> classToMock = Object.class;

        mockSettingsImpl.initiateMockName(classToMock);

        assertEquals(classToMock, mockSettingsImpl.getMockName().getMockedType());
    }
}