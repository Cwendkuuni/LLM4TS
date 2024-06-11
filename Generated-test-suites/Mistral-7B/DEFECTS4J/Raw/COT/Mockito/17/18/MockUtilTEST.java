import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.LenientCopyTool;
import org.mockito.invocation.MethodInterceptor;
import org.mockito.stub.Answer;
import org.mockito.stub.MockitoStub;

import java.lang.reflect.Proxy;

public class MockUtilTest {

 private MockUtil mockUtil;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 mockUtil = new MockUtil();
 }

 @Test
 public void testCreateMock_ValidArguments() {
 // Given
 Class<MockitoStub> mockitoStubClass = MockitoStub.class;
 MockSettingsImpl settings = new MockSettingsImpl();

 // When
 MockitoStub mock = (MockitoStub) mockUtil.createMock(mockitoStubClass, settings);

 // Then
 Assert.assertNotNull(mock);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateMock_NullClassToMock() {
 // Given
 MockSettingsImpl settings = new MockSettingsImpl();

 // When & Then
 mockUtil.createMock(null, settings);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateMock_InvalidClassToMock() {
 // Given
 Class<String> stringClass = String.class;
 MockSettingsImpl settings = new MockSettingsImpl();

 // When & Then
 mockUtil.createMock(stringClass, settings);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateMock_NullSettings() {
 // Given
 Class<MockitoStub> mockitoStubClass = MockitoStub.class;

 // When & Then
 mockUtil.createMock(mockitoStubClass, null);
 }

 @Test
 public void testCreateMock_ValidSpiedInstance() {
 // Given
 Class<MockitoStub> mockitoStubClass = MockitoStub.class;
 MockSettingsImpl settings = new MockSettingsImpl();
 Object spiedInstance = new MockitoStub();

 // When
 MockitoStub mock = (MockitoStub) mockUtil.createMock(mockitoStubClass, settings, spiedInstance);

 // Then
 Assert.assertNotNull(mock);
 // Assert.assertSame(spiedInstance, mock); // Uncomment if you want to test deep equality
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateMock_InvalidSpiedInstance() {
 // Given
 Class<MockitoStub> mockitoStubClass = MockitoStub.class;
 MockSettingsImpl settings = new MockSettingsImpl();
 Object spiedInstance = new Object();

 // When & Then
 mockUtil.createMock(mockitoStubClass, settings, spiedInstance);
 }

 @Test
 public void testResetMock() {
 // Given
 Class<MockitoStub> mockitoStubClass = MockitoStub.class;
 MockSettingsImpl settings = new MockSettingsImpl();
 MockitoStub mock = (MockitoStub) mockUtil.createMock(mockitoStubClass, settings);

 // When
 mockUtil.resetMock(mock);

 // Then
 MethodInterceptor filter = (MethodInterceptor) Proxy.getInvocationHandler(mock);
 Answer answer = filter.answer();
 Assert.assertNotNull(answer);
 Assert.assertEquals(Answer.RETURN_DEFAULTS, answer.getValue());
 }

 @Test(expected = NullPointerException.class)
 public void testGetMockHandler_NullMock() {
 // Given
 Object nullMock = null;

 // When & Then
 mockUtil.getMockHandler(nullMock);
 }

 @Test(expected = NotAMockException.class)
 public void testGetMockHandler_MockNotCreatedByMockito() {
 // Given
 Object notAMock = new Object();

 // When & Then
 mockUtil.getMockHandler(notAMock);
 }

 @Test
 public void testIsMock() {
 // Given
 Object mockitoMock = Mockito.mock(String.class);
 Object notAMock = new Object();

 // When
 boolean isMockitoMock = mockUtil.isMock(mockitoMock);
 boolean isNotAMock = mockUtil.isMock(notAMock);

 // Then
 Assert.assertTrue(isMockitoMock);
 Assert.assertFalse(isNotAMock);
 }

 @Test
 public void testGetInterceptor_MockitoMock() {
 // Given
 Object mockitoMock = Mockito.mock(String.class);

 // When
 MethodInterceptor interceptor = mockUtil.getInterceptor(mockitoMock);

 // Then
 Assert.assertNotNull(interceptor);
 }

 @Test(expected = NotAMockException.class)
 public void testGetInterceptor_NotAMockitoMock() {
 // Given
 Object notAMockitoMock = new Object();

 // When & Then
 mockUtil.getInterceptor(notAMockitoMock);
 }

 @Test
 public void testGetMockName() {
 // Given
 Object mockitoMock = Mockito.mock(String.class);

 // When
 MockName mockName = mockUtil.getMockName(mockitoMock);

 // Then
 Assert.assertNotNull(mockName);
 Assert.assertEquals("Mockito stub for: class org.mockito.stub.MockitoStub", mockName.getMockName());
 }

 @Test(expected = NullPointerException.class)
 public void testGetMockName_NullMock() {
 // Given
 Object nullMock = null;

 // When & Then
 mockUtil.getMockName(nullMock);
 }
}