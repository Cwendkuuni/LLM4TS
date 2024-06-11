package org.mockito.internal.creation;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.mockito.Answer;
import org.mockito.MockSettings;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.util.MockName;

public class MockSettingsImplTest {

	@Test
	public void testSerializable() {
		MockSettings settings = new MockSettingsImpl().serializable();
		assertTrue(settings.isSerializable());
	}

	@Test(expected = Reporter.class)
	public void testExtraInterfacesRequiresAtLeastOneInterface() {
		new MockSettingsImpl().extraInterfaces(new Object[0]);
	}

	@Test(expected = Reporter.class)
	public void testExtraInterfacesAcceptsOnlyInterfaces() {
		new MockSettingsImpl().extraInterfaces(Object.class);
	}

	@Test
	public void testExtraInterfaces() {
		Class<?>[] expectedInterfaces = { java.io.Serializable.class, String.class };
		MockSettings settings = new MockSettingsImpl().extraInterfaces(expectedInterfaces);
		assertArrayEquals(expectedInterfaces, settings.getExtraInterfaces());
	}

	@Test
	public void testGetMockName() {
		MockSettings settings = new MockSettingsImpl();
		settings.initiateMockName(String.class);
		MockName mockName = settings.getMockName();
		assertNotNull(mockName);
	}

	@Test
	public void testGetExtraInterfaces() {
		Class<?>[] expectedInterfaces = { java.io.Serializable.class, String.class };
		MockSettings settings = new MockSettingsImpl().extraInterfaces(expectedInterfaces);
		assertArrayEquals(expectedInterfaces, settings.getExtraInterfaces());
	}

	@Test
	public void testGetSpiedInstance() {
		Object spiedInstance = new Object();
		MockSettings settings = new MockSettingsImpl().spiedInstance(spiedInstance);
		assertSame(spiedInstance, settings.getSpiedInstance());
	}

	@Test
	public void testName() {
		MockSettings settings = new MockSettingsImpl().name("MockitoTest");
		assertEquals("MockitoTest", settings.getName());
	}

	@Test
	public void testDefaultAnswer() {
		Answer<Object> answer = args -> args[0];
		MockSettings settings = new MockSettingsImpl().defaultAnswer(answer);
		assertEquals(answer, settings.getDefaultAnswer());
	}

	@Test
	public void testIsSerializable() {
		Class<?>[] extraInterfaces = { java.io.Serializable.class };
		MockSettings settings = new MockSettingsImpl().extraInterfaces(extraInterfaces);
		assertTrue(settings.isSerializable());
	}
}