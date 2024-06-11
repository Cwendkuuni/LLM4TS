import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.Mockito;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.CreationValidator;
import org.mockito.internal.util.reflection.LenientCopyTool;
import org.mockito.internal.creation.MethodInterceptorFilter;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.util.MockHandler;
import org.mockito.internal.util.MockHandlerInterface;
import org.mockito.internal.util.MockName;
import org.mockito.internal.util.Factory;

import java.lang.reflect.Method;
import java.util.Arrays;

public class MockUtilTest {

 private CreationValidator creationValidator = new CreationValidator();
 private MockUtil mockUtil = new MockUtil(creationValidator);

 @Test
 public void testCreateMock_ValidArguments() {
 Class<String> classToMock = String.class;
 Object spiedInstance = "Mockito";
 MockSettingsImpl settings = new MockSettingsImpl();
 T mock = mockUtil.createMock(classToMock, settings);
 assertNotNull(mock);
 assertTrue(mockUtil.isMock(mock));
 }

 @Test
 public void testCreateMock_NullClassToMock() {
 Class<String> classToMock = null;
 assertThrows(IllegalArgumentException.class, () -> mockUtil.createMock(classToMock, null));
 }

 @Test
 public void testCreateMock_NullSettings() {
 MockSettingsImpl settings = null;
 MockHandler<String> mockHandler = Mockito.mock(String.class);
 ClassImposterizer imposterizer = Mockito.mock(ClassImposterizer.class);
 when(imposterizer.imposterise(Mockito.any(MethodInterceptorFilter.class), Mockito.eq(String.class), Mockito.anyClasses())).thenReturn(mockHandler);
 doThrow(new RuntimeException()).when(mockHandler).answerInterceptions();

 Class<?>[] ancillaryTypes = new Class[0];
 Object spiedInstance = "Mockito";
 T mock = mockUtil.createMock(null, settings);
 assertNull(mock);
 verify(mockHandler).answerInterceptions();
 }

 @Test
 public void testCreateMock_NullClassToMockWithExtraInterfaces() {
 Class<String> classToMock = null;
 Class<?>[] extraInterfaces = {Integer.class};
 T mock = mockUtil.createMock(classToMock, new MockSettingsImpl().extraInterfaces(extraInterfaces));
 assertNull(mock);
 }

 @Test
 public void testCreateMock_ClassThatCannotBeMocked() {
 Class<Integer> classToMock = Integer.class;
 assertThrows(NotAMockException.class, () -> mockUtil.createMock(classToMock, null));
 }

 @Test
 public void testCreateMock_SpiedInstanceAndExtraInterfaces() {
 Class<String> classToMock = String.class;
 Object spiedInstance = "Mockito";
 Class<?>[] extraInterfaces = {Integer.class};
 MockSettingsImpl settings = new MockSettingsImpl().extraInterfaces(extraInterfaces);
 T mock = mockUtil.createMock(classToMock, settings);
 assertNotNull(mock);
 assertTrue(mockUtil.isMock(mock));
 }

 @Test
 public void testResetMock() {
 T mock = Mockito.mock(String.class);
 mockUtil.resetMock(mock);
 MethodInterceptorFilter filter = new MethodInterceptorFilter(Mockito.mock(MockHandler.class), new MockSettingsImpl().defaultAnswer(Mockito.RETURNS_DEFAULTS));
 assertEquals(filter, Mockito.mock(MethodInterceptorFilter.class));
 }

 @Test
 public void testResetMock_NullMock() {
 T mock = null;
 assertThrows(IllegalArgumentException.class, () -> mockUtil.resetMock(mock));
 }

 @Test
 public void testGetMockHandler_NullMock() {
 T mock = null;
 assertThrows(NullPointerException.class, () -> mockUtil.getMockHandler(mock));
 }

 @Test
 public void testGetMockHandler_ValidMock() {
 T mock = Mockito.mock(String.class);
 MockHandlerInterface<String> mockHandler = mockUtil.getMockHandler(mock);
 assertNotNull(mockHandler);
 }

 @Test
 public void testIsMockitoMock_NullMock() {
 assertFalse(mockUtil.isMockitoMock(null));
 }

 @Test
 public void testIsMockitoMock_Mock() {
 T mock = Mockito.mock(String.class);
 assertTrue(mockUtil.isMockitoMock(mock));
 }

 @Test
 public void testIsMock_Null() {
 assertFalse(mockUtil.isMock(null));
 }

 @Test
 public void testIsMock_Mock() {
 T mock = Mockito.mock(String.class);
 assertTrue(mockUtil.isMock(mock));
 }

 @Test
 public void testGetInterceptor_NullMock() {
 T mock = null;
 assertThrows(NullPointerException.class, () -> mockUtil.getInterceptor(mock));
 }

 @Test
 public void testGetInterceptor_Mock() {
 T mock = Mockito.mock(String.class);
 MethodInterceptorFilter interceptor = mockUtil.getInterceptor(mock);
 assertNotNull(interceptor);
 }

 @Test
 public void testGetMockName_NullMock() {
 T mock = null;
 assertNull(mockUtil.getMockName(mock));
 }

 @Test
 public void testGetMockName_Mock() {
 T mock = Mockito.mock(String.class);
 MockName mockName = mockUtil.getMockName(mock);
 assertNotNull(mockName);
 assertEquals("Mockito Mock", mockName.getMockName());
 }
}