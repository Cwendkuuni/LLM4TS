import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.util.MockName;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MockSettingsImplTest {

    private MockSettingsImpl mockSettings;

    @Before
    public void setUp() {
        mockSettings = new MockSettingsImpl();
    }

    @Test
    public void testSerializable() {
        MockSettings result = mockSettings.serializable();
        assertTrue(result.isSerializable());
    }

    @Test
    public void testExtraInterfaces() {
        Class<?>[] extraInterfaces = {Runnable.class, Comparable.class};
        MockSettings result = mockSettings.extraInterfaces(extraInterfaces);
        assertArrayEquals(extraInterfaces, result.getExtraInterfaces());
    }

    @Test
    public void testExtraInterfacesThrowsExceptionWhenNull() {
        try {
            mockSettings.extraInterfaces(null);
            fail("Should have thrown an exception");
        } catch (Reporter e) {
            // Exception expected
        }
    }

    @Test
    public void testExtraInterfacesThrowsExceptionWhenEmpty() {
        try {
            mockSettings.extraInterfaces();
            fail("Should have thrown an exception");
        } catch (Reporter e) {
            // Exception expected
        }
    }

    @Test
    public void testExtraInterfacesThrowsExceptionWhenNonInterface() {
        try {
            Class<?>[] extraInterfaces = {String.class};
            mockSettings.extraInterfaces(extraInterfaces);
            fail("Should have thrown an exception");
        } catch (Reporter e) {
            // Exception expected
        }
    }

    @Test
    public void testGetMockName() {
        MockName mockName = mock(MockName.class);
        mockSettings.initiateMockName(String.class);
        when(mockName.get()).thenReturn("mockName");
        assertEquals(mockName, mockSettings.getMockName());
    }

    @Test
    public void testGetExtraInterfaces() {
        Class<?>[] extraInterfaces = {Runnable.class, Comparable.class};
        mockSettings.extraInterfaces(extraInterfaces);
        assertArrayEquals(extraInterfaces, mockSettings.getExtraInterfaces());
    }

    @Test
    public void testGetSpiedInstance() {
        Object spiedInstance = new Object();
        mockSettings.spiedInstance(spiedInstance);
        assertEquals(spiedInstance, mockSettings.getSpiedInstance());
    }

    @Test
    public void testName() {
        String name = "mockName";
        MockSettings result = mockSettings.name(name);
        assertEquals(name, result.getMockName().toString());
    }

    @Test
    public void testSpiedInstance() {
        Object spiedInstance = new Object();
        MockSettings result = mockSettings.spiedInstance(spiedInstance);
        assertEquals(spiedInstance, result.getSpiedInstance());
    }

    @Test
    public void testDefaultAnswer() {
        Answer<Object> answer = mock(Answer.class);
        MockSettings result = mockSettings.defaultAnswer(answer);
        assertEquals(answer, result.getDefaultAnswer());
    }
}