package org.mockito.internal.creation;

import org.junit.Test;
import org.junit.Assert;
import org.mockito.MockSettings;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.util.MockName;
import org.mockito.stubbing.Answer;

public class MockSettingsImplTest {
  @Test
  public void testSerializable() {
    MockSettingsImpl mockSettingsImpl = new MockSettingsImpl();
    MockSettings result = mockSettingsImpl.serializable();
    Assert.assertEquals(mockSettingsImpl, result);
  }

  @Test
  public void testExtraInterfacesWithNull() {
    MockSettingsImpl mockSettingsImpl = new MockSettingsImpl();
    try {
      mockSettingsImpl.extraInterfaces(null);
      Assert.fail("Expected Reporter.exceptionWithMessage()");
    } catch (Reporter reporter) {
      // Expected exception
    }
  }

  @Test
  public void testExtraInterfacesWithNonInterface() {
    MockSettingsImpl mockSettingsImpl = new MockSettingsImpl();
    Class<?>[] extraInterfaces = { String.class };
    try {
      mockSettingsImpl.extraInterfaces(extraInterfaces);
      Assert.fail("Expected Reporter.exceptionWithMessage()");
    } catch (Reporter reporter) {
      // Expected exception
    }
  }

  @Test
  public void testExtraInterfaces() {
    MockSettingsImpl mockSettingsImpl = new MockSettingsImpl();
    Class<?>[] extraInterfaces = { Runnable.class };
    MockSettings result = mockSettingsImpl.extraInterfaces(extraInterfaces);
    Assert.assertEquals(mockSettingsImpl, result);
  }

  @Test
  public void testGetMockName() {
    MockSettingsImpl mockSettingsImpl = new MockSettingsImpl();
    MockName result = mockSettingsImpl.getMockName();
    Assert.assertNull(result);
  }

  @Test
  public void testGetExtraInterfaces() {
    MockSettingsImpl mockSettingsImpl = new MockSettingsImpl();
    Class<?>[] result = mockSettingsImpl.getExtraInterfaces();
    Assert.assertNull(result);
  }

  @Test
  public void testGetSpiedInstance() {
    MockSettingsImpl mockSettingsImpl = new MockSettingsImpl();
    Object result = mockSettingsImpl.getSpiedInstance();
    Assert.assertNull(result);
  }

  @Test
  public void testName() {
    MockSettingsImpl mockSettingsImpl = new MockSettingsImpl();
    String name = "test";
    MockSettings result = mockSettingsImpl.name(name);
    Assert.assertEquals(mockSettingsImpl, result);
  }

  @Test
  public void testSpiedInstance() {
    MockSettingsImpl mockSettingsImpl = new MockSettingsImpl();
    Object spiedInstance = new Object();
    MockSettings result = mockSettingsImpl.spiedInstance(spiedInstance);
    Assert.assertEquals(mockSettingsImpl, result);
  }

  @Test
  public void testDefaultAnswer() {
    MockSettingsImpl mockSettingsImpl = new MockSettingsImpl();
    Answer<Object> defaultAnswer = new Answer<Object>() {
      public Object answer(org.mockito.invocation.InvocationOnMock invocation) throws Throwable {
        return null;
      }
    };
    MockSettings result = mockSettingsImpl.defaultAnswer(defaultAnswer);
    Assert.assertEquals(mockSettingsImpl, result);
  }

  @Test
  public void testGetDefaultAnswer() {
    MockSettingsImpl mockSettingsImpl = new MockSettingsImpl();
    Answer<Object> result = mockSettingsImpl.getDefaultAnswer();
    Assert.assertNull(result);
  }

  @Test
  public void testIsSerializableFalse() {
    MockSettingsImpl mockSettingsImpl = new MockSettingsImpl();
    boolean result = mockSettingsImpl.isSerializable();
    Assert.assertFalse(result);
  }

  @Test
  public void testIsSerializableTrue() {
    MockSettingsImpl mockSettingsImpl = new MockSettingsImpl();
    Class<?>[] extraInterfaces = { java.io.Serializable.class };
    mockSettingsImpl.extraInterfaces(extraInterfaces);
    boolean result = mockSettingsImpl.isSerializable();
    Assert.assertTrue(result);
  }

  @Test
  public void testInitiateMockName() {
    MockSettingsImpl mockSettingsImpl = new MockSettingsImpl();
    Class classToMock = String.class;
    mockSettingsImpl.initiateMockName(classToMock);
    MockName result = mockSettingsImpl.getMockName();
    Assert.assertNotNull(result);
  }
}