import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.util.MockName;
import org.mockito.stubbing.Answer;

public class MockSettingsImplTest {

  @Test
  public void testSerializable() {
    MockSettingsImpl mockSettings = new MockSettingsImpl();
    MockSettings result = mockSettings.serializable();
    assertTrue(result.isSerializable());
  }

  @Test(expected = Reporter.class)
  public void testExtraInterfacesWithNull() {
    MockSettingsImpl mockSettings = new MockSettingsImpl();
    mockSettings.extraInterfaces((Class<?>) null);
  }

  @Test(expected = Reporter.class)
  public void testExtraInterfacesWithNoInterfaces() {
    MockSettingsImpl mockSettings = new MockSettingsImpl();
    mockSettings.extraInterfaces();
  }

  @Test(expected = Reporter.class)
  public void testExtraInterfacesWithNonInterface() {
    MockSettingsImpl mockSettings = new MockSettingsImpl();
    mockSettings.extraInterfaces(String.class);
  }

  @Test
  public void testExtraInterfacesWithInterfaces() {
    MockSettingsImpl mockSettings = new MockSettingsImpl();
    Class<?>[] extraInterfaces = {java.io.Serializable.class};
    MockSettings result = mockSettings.extraInterfaces(extraInterfaces);
    assertArrayEquals(extraInterfaces, result.getExtraInterfaces());
  }

  @Test
  public void testGetMockName() {
    MockSettingsImpl mockSettings = new MockSettingsImpl();
    MockName mockName = mockSettings.getMockName();
    assertNull(mockName);
  }

  @Test
  public void testGetName() {
    MockSettingsImpl mockSettings = new MockSettingsImpl();
    String name = "mockName";
    MockSettings result = mockSettings.name(name);
    assertEquals(name, result.getMockName().toString());
  }

  @Test
  public void testGetSpiedInstance() {
    MockSettingsImpl mockSettings = new MockSettingsImpl();
    Object spiedInstance = new Object();
    MockSettings result = mockSettings.spiedInstance(spiedInstance);
    assertEquals(spiedInstance, result.getSpiedInstance());
  }

  @Test
  public void testDefaultAnswer() {
    MockSettingsImpl mockSettings = new MockSettingsImpl();
    Answer<Object> defaultAnswer = null;
    MockSettings result = mockSettings.defaultAnswer(defaultAnswer);
    assertEquals(defaultAnswer, result.getDefaultAnswer());
  }

  @Test
  public void testIsSerializableWithNullExtraInterfaces() {
    MockSettingsImpl mockSettings = new MockSettingsImpl();
    boolean result = mockSettings.isSerializable();
    assertFalse(result);
  }

  @Test
  public void testIsSerializableWithSerializableExtraInterfaces() {
    MockSettingsImpl mockSettings = new MockSettingsImpl();
    mockSettings.extraInterfaces(java.io.Serializable.class);
    boolean result = mockSettings.isSerializable();
    assertTrue(result);
  }

  @Test
  public void testInitiateMockName() {
    MockSettingsImpl mockSettings = new MockSettingsImpl();
    Class classToMock = Object.class;
    mockSettings.initiateMockName(classToMock);
    assertNotNull(mockSettings.getMockName());
  }
}