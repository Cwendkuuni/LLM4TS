import org.junit.Test;
import static org.junit.Assert.*;

public class MockSettingsImplTest {

  @Test
  public void testSerializable() {
    MockSettingsImpl mockSettings = new MockSettingsImpl();
    MockSettings result = mockSettings.serializable();
    assertTrue(result.isSerializable());
  }

  @Test
  public void testExtraInterfaces() {
    MockSettingsImpl mockSettings = new MockSettingsImpl();

    // Test when extraInterfaces is null
    MockSettings result = mockSettings.extraInterfaces(null);
    assertNull(result.getExtraInterfaces());

    // Test when extraInterfaces is empty
    result = mockSettings.extraInterfaces();
    assertNull(result.getExtraInterfaces());

    // Test when extraInterface contains null
    result = mockSettings.extraInterfaces(null, null);
    assertNull(result.getExtraInterfaces());

    // Test when extraInterface is not an interface
    result = mockSettings.extraInterfaces(MockSettingsImpl.class);
    assertNull(result.getExtraInterfaces());

    // Test when extraInterface is valid
    Class<?>[] extraInterfaces = { Runnable.class };
    result = mockSettings.extraInterfaces(extraInterfaces);
    assertArrayEquals(extraInterfaces, result.getExtraInterfaces());
  }

  @Test
  public void testGetMockName() {
    MockSettingsImpl mockSettings = new MockSettingsImpl();
    assertNull(mockSettings.getMockName());
  }

  @Test
  public void testGetExtraInterfaces() {
    MockSettingsImpl mockSettings = new MockSettingsImpl();
    assertNull(mockSettings.getExtraInterfaces());
  }

  @Test
  public void testGetSpiedInstance() {
    MockSettingsImpl mockSettings = new MockSettingsImpl();
    assertNull(mockSettings.getSpiedInstance());
  }

  @Test
  public void testName() {
    MockSettingsImpl mockSettings = new MockSettingsImpl();
    MockSettings result = mockSettings.name("testName");
    assertEquals("testName", result.getMockName().toString());
  }

  @Test
  public void testSpiedInstance() {
    MockSettingsImpl mockSettings = new MockSettingsImpl();
    Object spiedInstance = new Object();
    MockSettings result = mockSettings.spiedInstance(spiedInstance);
    assertEquals(spiedInstance, result.getSpiedInstance());
  }

  @Test
  public void testDefaultAnswer() {
    MockSettingsImpl mockSettings = new MockSettingsImpl();
    Answer<Object> defaultAnswer = () -> null;
    MockSettings result = mockSettings.defaultAnswer(defaultAnswer);
    assertEquals(defaultAnswer, result.getDefaultAnswer());
  }

  @Test
  public void testIsSerializable() {
    MockSettingsImpl mockSettings = new MockSettingsImpl();
    assertFalse(mockSettings.isSerializable());

    Class<?>[] extraInterfaces = { java.io.Serializable.class };
    mockSettings.extraInterfaces(extraInterfaces);
    assertTrue(mockSettings.isSerializable());
  }

  @Test
  public void testInitiateMockName() {
    MockSettingsImpl mockSettings = new MockSettingsImpl();
    mockSettings.initiateMockName(MockSettingsImplTest.class);
    assertNotNull(mockSettings.getMockName());
  }
}