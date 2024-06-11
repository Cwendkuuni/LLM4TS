import org.junit.*;
import org.mockito.*;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.util.MockName;
import org.mockito.stubbing.Answer;
import java.io.Serializable;
import java.util.Arrays;

public class MockSettingsImplTest {
  
  @Test
  public void testSerializable() {
    MockSettingsImpl mockSettings = new MockSettingsImpl();
    MockSettingsImpl result = mockSettings.serializable();
    assertTrue(result.isSerializable());
  }
  
  @Test(expected = Reporter.class)
  public void testExtraInterfacesWithNullInput() {
    MockSettingsImpl mockSettings = new MockSettingsImpl();
    mockSettings.extraInterfaces(null);
  }
  
  @Test(expected = Reporter.class)
  public void testExtraInterfacesWithEmptyInput() {
    MockSettingsImpl mockSettings = new MockSettingsImpl();
    mockSettings.extraInterfaces();
  }
  
  @Test(expected = Reporter.class)
  public void testExtraInterfacesWithNonInterfaceInput() {
    MockSettingsImpl mockSettings = new MockSettingsImpl();
    mockSettings.extraInterfaces(String.class);
  }
  
  @Test
  public void testExtraInterfaces() {
    MockSettingsImpl mockSettings = new MockSettingsImpl();
    Class<?>[] extraInterfaces = {Serializable.class, Cloneable.class};
    MockSettingsImpl result = mockSettings.extraInterfaces(extraInterfaces);
    assertArrayEquals(extraInterfaces, result.getExtraInterfaces());
  }
  
  @Test
  public void testGetMockName() {
    MockSettingsImpl mockSettings = new MockSettingsImpl();
    assertNull(mockSettings.getMockName());
  }
  
  @Test
  public void testGetName() {
    MockSettingsImpl mockSettings = new MockSettingsImpl();
    String name = "mockName";
    MockSettingsImpl result = mockSettings.name(name);
    assertEquals(name, result.getMockName().getMockName());
  }
  
  @Test
  public void testGetExtraInterfaces() {
    MockSettingsImpl mockSettings = new MockSettingsImpl();
    Class<?>[] extraInterfaces = {Serializable.class, Cloneable.class};
    mockSettings.extraInterfaces(extraInterfaces);
    assertArrayEquals(extraInterfaces, mockSettings.getExtraInterfaces());
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
    MockSettingsImpl result = mockSettings.spiedInstance(spiedInstance);
    assertEquals(spiedInstance, result.getSpiedInstance());
  }
  
  @Test
  public void testDefaultAnswer() {
    MockSettingsImpl mockSettings = new MockSettingsImpl();
    Answer<Object> defaultAnswer = Mockito.mock(Answer.class);
    MockSettingsImpl result = mockSettings.defaultAnswer(defaultAnswer);
    assertEquals(defaultAnswer, result.getDefaultAnswer());
  }
  
  @Test
  public void testGetDefaultAnswer() {
    MockSettingsImpl mockSettings = new MockSettingsImpl();
    assertNull(mockSettings.getDefaultAnswer());
  }
  
  @Test
  public void testIsSerializableWithoutExtraInterfaces() {
    MockSettingsImpl mockSettings = new MockSettingsImpl();
    assertFalse(mockSettings.isSerializable());
  }
  
  @Test
  public void testIsSerializableWithSerializableExtraInterface() {
    MockSettingsImpl mockSettings = new MockSettingsImpl();
    Class<?>[] extraInterfaces = {Serializable.class};
    mockSettings.extraInterfaces(extraInterfaces);
    assertTrue(mockSettings.isSerializable());
  }
  
  @Test
  public void testInitiateMockName() {
    MockSettingsImpl mockSettings = new MockSettingsImpl();
    Class<?> classToMock = String.class;
    mockSettings.initiateMockName(classToMock);
    MockName mockName = mockSettings.getMockName();
    assertEquals(classToMock, mockName.getMockType());
  }
  
}