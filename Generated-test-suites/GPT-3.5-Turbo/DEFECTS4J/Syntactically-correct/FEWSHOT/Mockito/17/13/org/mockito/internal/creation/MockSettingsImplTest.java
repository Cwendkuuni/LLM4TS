
package org.mockito.internal.creation;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

public class MockSettingsImplTest {
    
    @Test
    public void testSerializable() {
        MockSettingsImpl mockSettingsImpl = new MockSettingsImpl();
        MockSettingsImpl result = mockSettingsImpl.serializable();
        assertEquals(mockSettingsImpl, result);
    }

    @Test
    public void testExtraInterfaces() {
        MockSettingsImpl mockSettingsImpl = new MockSettingsImpl();
        Class<?>[] extraInterfaces = new Class[] { java.io.Serializable.class };
        MockSettingsImpl result = mockSettingsImpl.extraInterfaces(extraInterfaces);
        assertEquals(mockSettingsImpl, result);
    }

    @Test(expected = RuntimeException.class)
    public void testExtraInterfacesNull() {
        MockSettingsImpl mockSettingsImpl = new MockSettingsImpl();
        mockSettingsImpl.extraInterfaces(null);
    }

    @Test(expected = RuntimeException.class)
    public void testExtraInterfacesEmpty() {
        MockSettingsImpl mockSettingsImpl = new MockSettingsImpl();
        mockSettingsImpl.extraInterfaces(new Class[0]);
    }

    @Test(expected = RuntimeException.class)
    public void testExtraInterfacesInvalid() {
        MockSettingsImpl mockSettingsImpl = new MockSettingsImpl();
        mockSettingsImpl.extraInterfaces(String.class);
    }

    @Test
    public void testGetMockName() {
        MockSettingsImpl mockSettingsImpl = new MockSettingsImpl();
        assertNull(mockSettingsImpl.getMockName());
    }

    @Test
    public void testGetExtraInterfaces() {
        MockSettingsImpl mockSettingsImpl = new MockSettingsImpl();
        assertNull(mockSettingsImpl.getExtraInterfaces());
    }

    @Test
    public void testGetSpiedInstance() {
        MockSettingsImpl mockSettingsImpl = new MockSettingsImpl();
        assertNull(mockSettingsImpl.getSpiedInstance());
    }

    @Test
    public void testName() {
        MockSettingsImpl mockSettingsImpl = new MockSettingsImpl();
        MockSettingsImpl result = mockSettingsImpl.name("mockName");
        assertEquals(mockSettingsImpl, result);
    }

    @Test
    public void testSpiedInstance() {
        MockSettingsImpl mockSettingsImpl = new MockSettingsImpl();
        Object spiedInstance = new Object();
        MockSettingsImpl result = mockSettingsImpl.spiedInstance(spiedInstance);
        assertEquals(mockSettingsImpl, result);
    }

    @Test
    public void testDefaultAnswer() {
        MockSettingsImpl mockSettingsImpl = new MockSettingsImpl();
        Answer defaultAnswer = mock(Answer.class);
        MockSettingsImpl result = mockSettingsImpl.defaultAnswer(defaultAnswer);
        assertEquals(mockSettingsImpl, result);
    }

    @Test
    public void testGetDefaultAnswer() {
        MockSettingsImpl mockSettingsImpl = new MockSettingsImpl();
        assertNull(mockSettingsImpl.getDefaultAnswer());
    }

    @Test
    public void testIsSerializableWithExtraInterfacesNull() {
        MockSettingsImpl mockSettingsImpl = new MockSettingsImpl();
        boolean result = mockSettingsImpl.isSerializable();
        assertEquals(false, result);
    }

    @Test
    public void testIsSerializableWithExtraInterfacesNotContainingSerializable() {
        MockSettingsImpl mockSettingsImpl = new MockSettingsImpl();
        mockSettingsImpl.extraInterfaces(String.class);
        boolean result = mockSettingsImpl.isSerializable();
        assertEquals(false, result);
    }

    @Test
    public void testIsSerializableWithExtraInterfacesContainingSerializable() {
        MockSettingsImpl mockSettingsImpl = new MockSettingsImpl();
        mockSettingsImpl.extraInterfaces(java.io.Serializable.class);
        boolean result = mockSettingsImpl.isSerializable();
        assertEquals(true, result);
    }

    @Test
    public void testInitiateMockName() {
        MockSettingsImpl mockSettingsImpl = new MockSettingsImpl();
        mockSettingsImpl.initiateMockName(Object.class);
        assertEquals(Object.class, mockSettingsImpl.getMockName().getMockType());
        assertEquals(null, mockSettingsImpl.getMockName().getDeclaredName());
    }
}
