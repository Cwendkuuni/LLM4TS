import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Assert;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.creation.CreationValidator;
import org.mockito.internal.util.reflection.LenientCopyTool;
import org.mockito.internal.util.reflection.lenient.LenientMockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stub.Answer;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class MockUtilTest {

 private MockUtil mockUtil;
 private CreationValidator creationValidatorMock;
 private InvocationHandler mockHandlerMock;
 private Object spiedInstance;

 @Before
 public void setup() {
 mockUtil = new MockUtil(new CreationValidator() {
 @Override
 public void validateType(Class<?> clazz) {
 // empty implementation
 }

 @Override
 public void validateExtraInterfaces(Class<?> clazz, Class<?>[] extraInterfaces) {
 // empty implementation
 }

 @Override
 public void validateMockedType(Class<?> clazz, Object spiedInstance) {
 // empty implementation
 }
 });
 creationValidatorMock = mock(CreationValidator.class);
 mockHandlerMock = mock(InvocationHandler.class);
 spiedInstance = spy(new Object() {});
 }

 @Test
 public void testCreateMock() {
 Class<Object> classToMock = Object.class;
 MockSettingsImpl settings = new MockSettingsImpl();
 settings.setMockedType(classToMock);
 settings.setSpiedInstance(spiedInstance);
 Object mock = mockUtil.createMock(classToMock, settings);

 Assert.assertNotNull(mock);
 Assert.assertTrue(MockUtil.isMock(mock));

 ArgumentCaptor<MethodInterceptorFilter> filterCaptor = ArgumentCaptor.forClass(MethodInterceptorFilter.class);
 verify(creationValidatorMock).validateType(classToMock);
 verify(creationValidatorMock).validateExtraInterfaces(classToMock, settings.getExtraInterfaces());
 verify(creationValidatorMock).validateMockedType(classToMock, settings.getSpiedInstance());

 verify(ClassImposterizer.INSTANCE, times(1)).imposterise(eq(filterCaptor.capture()), eq(classToMock), anyArray());
 MethodInterceptorFilter filter = filterCaptor.getValue();
 verify(mockHandlerMock).invoke(any());

 LenientCopyTool lenientCopyTool = new LenientCopyTool();
 lenientCopyTool.copyToMock(spiedInstance, mock);
 verify(mockHandlerMock).handleCall(any(), any());
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockWithNullClass() {
 Class<?> nullClass = null;
 mockUtil.createMock(nullClass, null);
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockWithNullMockedType() {
 Class<Object> nullClass = Object.class;
 mockUtil.createMock(nullClass, null);
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockWithNullSpiedInstance() {
 Class<Object> classToMock = Object.class;
 mockUtil.createMock(classToMock, null);
 }

 @Test
 public void testResetMock() {
 Object mock = mock(Object.class);
 mockUtil.resetMock(mock);

 MethodInterceptorFilter newFilter = new MethodInterceptorFilter(mockHandlerMock, withSettings().defaultAnswer(RETURNS_DEFAULTS));
 verify(mock).setCallback(eq(0), any());
 verify(mock).setCallback(eq(0), eq(newFilter));
 }

 @Test(expected = NotAMockException.class)
 public void testResetMockWithNullMock() {
 Object nullMock = null;
 mockUtil.resetMock(nullMock);
 }

 @Test
 public void testGetMockHandler() {
 Object mock = mock(Object.class);
 Object handler = mockUtil.getMockHandler(mock);
 Assert.assertNotNull(handler);
 Assert.assertTrue(handler instanceof MockHandler);
 }

 @Test(expected = NotAMockException.class)
 public void testGetMockHandlerWithNullMock() {
 Object nullMock = null;
 mockUtil.getMockHandler(nullMock);
 }

 @Test
 public void testIsMockitoMock() {
 Object mock = mock(Object.class);
 Assert.assertTrue(mockUtil.isMockitoMock(mock));
 }

 @Test
 public void testIsMockitoMockWithNullMock() {
 Object nullMock = null;
 Assert.assertFalse(mockUtil.isMockitoMock(nullMock));
 }

 @Test
 public void testGetInterceptor() {
 Object mock = mock(Object.class);
 Object interceptor = mockUtil.getInterceptor(mock);
 Assert.assertNotNull(interceptor);
 }

 @Test
 public void testGetInterceptorWithNullMock() {
 Object nullMock = null;
 Object interceptor = mockUtil.getInterceptor(nullMock);
 Assert.assertNull(interceptor);
 }

 @Test
 public void testGetMockName() {
 Object mock = mock(Object.class);
 String mockName = mockUtil.getMockName(mock);
 Assert.assertNotNull(mockName);
 }

 @Test
 public void testGetMockNameWithNullMock() {
 Object nullMock = null;
 Assert.assertNull(mockUtil.getMockName(nullMock));
 }
}