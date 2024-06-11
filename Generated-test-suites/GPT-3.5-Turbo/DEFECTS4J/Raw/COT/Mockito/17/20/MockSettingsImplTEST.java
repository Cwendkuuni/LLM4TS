import static org.junit.Assert.*;
import org.junit.Test;

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
    Class<?>[] extraInterfaces = new Class<?>[]{SomeInterface.class};
    MockSettings result = mockSettings.extraInterfaces(extraInterfaces);
    assertArrayEquals(extraInterfaces, result.getExtraInterfaces());
  }

  @Test
  public void testGetMockName() {
    MockSettingsImpl mockSettings = new MockSettingsImpl();
    assertNull(mockSettings.getMockName());
  }

  @Test
  public void testGetName() {
    String name = "mockName";
    MockSettingsImpl mockSettings = new MockSettingsImpl();
    MockSettings result = mockSettings.name(name);
    assertEquals(name, result.getMockName().toString());
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
  public void testSpiedInstance() {
    MockSettingsImpl mockSettings = new MockSettingsImpl();
    Object spiedInstance = new Object();
    MockSettings result = mockSettings.spiedInstance(spiedInstance);
    assertEquals(spiedInstance, result.getSpiedInstance());
  }

  @Test
  public void testDefaultAnswer() {
    MockSettingsImpl mockSettings = new MockSettingsImpl();
    Answer defaultAnswer = new SomeAnswer();
    MockSettings result = mockSettings.defaultAnswer(defaultAnswer);
    assertEquals(defaultAnswer, result.getDefaultAnswer());
  }

  @Test
  public void testIsSerializable() {
    MockSettingsImpl mockSettings = new MockSettingsImpl();
    assertFalse(mockSettings.isSerializable());
  }

  @Test
  public void testInitiateMockName() {
    String name = "mockName";
    Class classToMock = SomeClass.class;
    MockSettingsImpl mockSettings = new MockSettingsImpl();
    mockSettings.initiateMockName(classToMock);
    assertEquals(name, mockSettings.getMockName().toString());
  }
  
  private interface SomeInterface {}

  private class SomeAnswer implements Answer<Object> {
      public Object answer(InvocationOnMock invocation) {
          return null;
      }
  }
  
  private class SomeClass {}
}