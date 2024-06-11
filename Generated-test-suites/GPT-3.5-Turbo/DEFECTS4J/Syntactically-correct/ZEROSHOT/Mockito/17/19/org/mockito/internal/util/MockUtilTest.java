package org.mockito.internal.util;
import static org.mockito.Mockito.*;
import org.mockito.internal.creation.MockSettingsImpl;
import org.junit.*;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.MockHandler;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.util.reflection.LenientCopyTool;

@SuppressWarnings("unchecked")
public class MockUtilTest {

  @Test
  public void testCreateMock() {
    CreationValidator creationValidator = mock(CreationValidator.class);
    MockUtil mockUtil = new MockUtil(creationValidator);
    Class<TestClass> classToMock = TestClass.class;
    MockSettingsImpl settings = mock(MockSettingsImpl.class);
    TestClass mock = mockUtil.createMock(classToMock, settings);
    // assertions or verifications
  }

  @Test
  public void testResetMock() {
    TestClass mock = mock(TestClass.class);
    MockUtil mockUtil = new MockUtil();
    mockUtil.resetMock(mock);
    // assertions or verifications
  }

  @Test
  public void testGetMockHandler() {
    TestClass mock = mock(TestClass.class);
    MockUtil mockUtil = new MockUtil();
    MockHandlerInterface<TestClass> result = mockUtil.getMockHandler(mock);
    // assertions or verifications
  }

  @Test(expected = NotAMockException.class)
  public void testGetMockHandlerWithNullMock() {
    TestClass mock = null;
    MockUtil mockUtil = new MockUtil();
    MockHandlerInterface<TestClass> result = mockUtil.getMockHandler(mock);
    // assertions or verifications
  }

  @Test(expected = NotAMockException.class)
  public void testGetMockHandlerWithNonMockInstance() {
    TestClass mock = new TestClass();
    MockUtil mockUtil = new MockUtil();
    MockHandlerInterface<TestClass> result = mockUtil.getMockHandler(mock);
    // assertions or verifications
  }

  @Test
  public void testIsMock() {
    TestClass mock = mock(TestClass.class);
    MockUtil mockUtil = new MockUtil();
    boolean result = mockUtil.isMock(mock);
    // assertions or verifications
  }

  @Test
  public void testIsMockWithNonMockInstance() {
    TestClass mock = new TestClass();
    MockUtil mockUtil = new MockUtil();
    boolean result = mockUtil.isMock(mock);
    // assertions or verifications
  }

  @Test
  public void testGetMockName() {
    TestClass mock = mock(TestClass.class);
    MockUtil mockUtil = new MockUtil();
    MockName result = mockUtil.getMockName(mock);
    // assertions or verifications
  }

}