import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.internal.creation.CreationValidator;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.LenientCopyTool;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.powermock.reflect.Whitebox;

import java.lang.reflect.Method;
import java.util.Arrays;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MockUtilTest {

 private CreationValidator creationValidator;
 private MockUtil mockUtil;
 private Object spiedInstance;

 @Before
 public void setup() {
 creationValidator = mock(CreationValidator.class);
 mockUtil = new MockUtil();
 spiedInstance = Mockito.mock(Object.class);
 }

 @Test
 public void testCreateMock() {
 Class<Object> classToMock = Object.class;
 MockSettingsImpl settings = new MockSettingsImpl(RETURNS_DEFAULTS);
 settings.setSpiedInstance(spiedInstance);
 Object mock = mockUtil.createMock(classToMock, settings);
 assertNotNull(mock);
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockWithNullClass() {
 mockUtil.createMock(null, new MockSettingsImpl(RETURNS_DEFAULTS));
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockWithInvalidClass() {
 Class<String> invalidClass = (Class<String>) Class.forName("java.lang.String$$MockitoHelper");
 mockUtil.createMock(invalidClass, new MockSettingsImpl(RETURNS_DEFAULTS));
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockWithNullSettings() {
 mockUtil.createMock(Object.class, null);
 }

 @Test
 public void testCreateMockWithExtraInterfaces() {
 Class<Object> classToMock = Object.class;
 Class<?>[] extraInterfaces = {String.class};
 MockSettingsImpl settings = new MockSettingsImpl(RETURNS_DEFAULTS);
 settings.setExtraInterfaces(extraInterfaces);
 settings.setSpiedInstance(spiedInstance);
 Object mock = mockUtil.createMock(classToMock, settings);
 verify(settings).setExtraInterfaces(any(Class[].class));
 }

 @Test
 public void testCreateMockWithSpiedInstance() {
 Class<Object> classToMock = Object.class;
 MockSettingsImpl settings = new MockSettingsImpl(RETURNS_DEFAULTS);
 settings.setSpiedInstance(spiedInstance);
 Object mock = mockUtil.createMock(classToMock, settings);
 assertNotNull(mock);
 LenientCopyTool.copyToMock(spiedInstance, mock);
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockWithNonMockedType() {
 Class<String> classToMock = String.class;
 mockUtil.createMock(classToMock, new MockSettingsImpl(RETURNS_DEFAULTS));
 }

 @Test
 public void testResetMock() {
 Object mock = mock(Object.class);
 mockUtil.resetMock(mock);
 MockHandlerInterface mockHandler = mockUtil.getMockHandler(mock);
 verify(mockHandler).getMockSettings().getAnswer();
 Answer answer = mock(Answer.class);
 Whitebox.setInternalState(mockHandler, "answer", answer);
 mockUtil.resetMock(mock);
 verify(mockHandler, never()).getMockSettings().getAnswer();
 }

 @Test(expected = NotAMockException.class)
 public void testResetMockWithNullMock() {
 mockUtil.resetMock(null);
 }

 @Test
 public void testIsMockitoMock() {
 Object mock = mock(Object.class);
 assertTrue(mockUtil.isMockitoMock(mock));
 }

 @Test
 public void testIsMockitoMockWithNullMock() {
 assertFalse(mockUtil.isMockitoMock(null));
 }

 @Test
 public void testIsMockitoMockWithNonMockitoMock() {
 Object nonMockitoMock = new Object() {};
 assertFalse(mockUtil.isMockitoMock(nonMockitoMock));
 }

 @Test
 public void testGetMockHandler() {
 Object mock = mock(Object.class);
 Object handler = mockUtil.getMockHandler(mock);
 assertNotNull(handler);
 }

 @Test(expected = NotAMockException.class)
 public void testGetMockHandlerWithNullMock() {
 mockUtil.getMockHandler(null);
 }

 @Test(expected = NotAMockException.class)
 public void testGetMockHandlerWithNonMockitoMock() {
 Object nonMockitoMock = new Object() {};
 mockUtil.getMockHandler(nonMockitoMock);
 }

 @Test
 public void testGetInterceptor() {
 Object mock = mock(Object.class);
 Object interceptor = mockUtil.getInterceptor(mock);
 assertNotNull(interceptor);
 }

 @Test(expected = NotAMockException.class)
 public void testGetInterceptorWithNullMock() {
 mockUtil.getInterceptor(null);
 }

 @Test(expected = NotAMockException.class)
 public void testGetInterceptorWithNonMockitoMock() {
 Object nonMockitoMock = new Object() {};
 mockUtil.getInterceptor(nonMockitoMock);
 }

 @Test
 public void testGetMockName() {
 Object mock = mock(Object.class);
 String mockName = mockUtil.getMockName(mock);
 assertNotNull(mockName);
 }

 @Test(expected = NotAMockException.class)
 public void testGetMockNameWithNullMock() {
 mockUtil.getMockName(null);
 }

 @Test(expected = NotAMockException.class)
 public void testGetMockNameWithNonMockitoMock() {
 Object nonMockitoMock = new Object() {};
 mockUtil.getMockName(nonMockitoMock);
 }
}