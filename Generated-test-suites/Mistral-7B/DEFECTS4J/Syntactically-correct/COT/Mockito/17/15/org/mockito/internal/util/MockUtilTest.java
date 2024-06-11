import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.CreationValidator;
import org.mockito.internal.creation.MethodInterceptor;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.util.reflection.LenientCopyTool;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.exceptions.misusing.NotAMockException;

import java.lang.reflect.Proxy;
import java.util.Arrays;

public class MockUtilTest {

 private CreationValidator creationValidator;
 private MockUtil mockUtil;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 creationValidator = Mockito.spy(CreationValidator.class);
 mockUtil = new MockUtil(creationValidator);
 }

 @Test
 public void testCreateMock_ValidArguments() {
 Class<String> classToMock = String.class;
 MockSettingsImpl settings = Mockito.mock(MockSettingsImpl.class);
 Object spiedInstance = new Object();
 T mock = (T) mockUtil.createMock(classToMock, settings);
 Assert.assertNotNull(mock);
 }

 @Test(expected = NullPointerException.class)
 public void testCreateMock_NullClassToMock() {
 Class<String> nullClassToMock = null;
 MockSettingsImpl settings = Mockito.mock(MockSettingsImpl.class);
 mockUtil.createMock(nullClassToMock, settings);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateMock_InvalidClassToMock() {
 Class<String> invalidClassToMock = int.class;
 MockSettingsImpl settings = Mockito.mock(MockSettingsImpl.class);
 mockUtil.createMock(invalidClassToMock, settings);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateMock_NullSettings() {
 Class<String> classToMock = String.class;
 MockSettingsImpl nullSettings = null;
 mockUtil.createMock(classToMock, nullSettings);
 }

 @Test
 public void testCreateMock_ValidArgumentsWithExtraInterfaces() {
 Class<String> classToMock = String.class;
 Class<?>[] extraInterfaces = {SomeInterface.class};
 MockSettingsImpl settings = Mockito.mock(MockSettingsImpl.class);
 settings.setExtraInterfaces(extraInterfaces);
 Object spiedInstance = new Object();
 T mock = (T) mockUtil.createMock(classToMock, settings);
 Assert.assertNotNull(mock);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateMock_InvalidExtraInterfaces() {
 Class<String> classToMock = String.class;
 Class<?>[] invalidExtraInterfaces = {int.class};
 MockSettingsImpl settings = Mockito.mock(MockSettingsImpl.class);
 settings.setExtraInterfaces(invalidExtraInterfaces);
 mockUtil.createMock(classToMock, settings);
 }

 @Test
 public void testCreateMock_SpiedInstanceIsNotNull() {
 Class<String> classToMock = String.class;
 MockSettingsImpl settings = Mockito.mock(MockSettingsImpl.class);
 Object spiedInstance = new Object();
 T mock = mockUtil.createMock(classToMock, settings);
 new LenientCopyTool().copyToMock(spiedInstance, mock);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateMock_SpiedInstanceIsNull() {
 Class<String> classToMock = String.class;
 MockSettingsImpl settings = Mockito.mock(MockSettingsImpl.class);
 Object nullSpiedInstance = null;
 mockUtil.createMock(classToMock, settings, nullSpiedInstance);
 }

 @Test
 public void testResetMock() {
 T mock = Mockito.mock(String.class);
 mockUtil.resetMock(mock);
 MethodInterceptorFilter filter = (MethodInterceptorFilter) Whitebox.getInternalState(mock, "callbacks")[0];
 Assert.assertEquals(Filter.RETURNS_DEFAULTS, filter.getMockSettings().getDefaultAnswer());
 }

 @Test(expected = NullPointerException.class)
 public void testGetMockHandler_NullMock() {
 Object nullMock = null;
 mockUtil.getMockHandler(nullMock);
 }

 @Test(expected = NotAMockException.class)
 public void testGetMockHandler_NotAMockitoMock() {
 Object notAMockitoMock = new Object();
 mockUtil.getMockHandler(notAMockitoMock);
 }

 @Test
 public void testIsMock_ValidMockitoMock() {
 Object mockitoMock = Mockito.mock(String.class);
 assertTrue(mockUtil.isMock(mockitoMock));
 }

 @Test
 public void testIsMock_ValidOtherObject() {
 Object otherObject = new Object();
 assertFalse(mockUtil.isMock(otherObject));
 }

 @Test(expected = NullPointerException.class)
 public void testGetInterceptor_NullMock() {
 Object nullMock = null;
 mockUtil.getInterceptor(nullMock);
 }

 @Test(expected = NotAMockException.class)
 public void testGetInterceptor_NotAMockitoMock() {
 Object notAMockitoMock = new Object();
 mockUtil.getInterceptor(notAMockitoMock);
 }

 @Test
 public void testGetMockName_ValidMockitoMock() {
 Object mockitoMock = Mockito.mock(String.class);
 MockName mockName = mockUtil.getMockName(mockitoMock);
 Assert.assertNotNull(mockName);
 }

 @Test(expected = NullPointerException.class)
 public void testGetMockName_NullMock() {
 Object nullMock = null;
 mockUtil.getMockName(nullMock);
 }

 @Test(expected = NotAMockException.class)
 public void testGetMockName_NotAMockitoMock() {
 Object notAMockitoMock = new Object();
 mockUtil.getMockName(notAMockitoMock);
 }
}