import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.creation.CreationValidator;
import org.mockito.internal.util.reflection.LenientCopyTool;
import org.mockito.junit.runners.MockitoJUnitRunner;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;

import static org.mockito.Mockito.*;
import static org.mockito.internal.util.MockUtil.getMockName;

public class MockUtilTest {

 private CreationValidator creationValidator;
 private MockUtil mockUtil;
 private MockHandler<Object> mockHandler;
 private Object spiedInstance;

 @Before
 public void setup() {
 creationValidator = mock(CreationValidator.class);
 mockUtil = new MockUtil(creationValidator);
 mockHandler = mock(MockHandler.class);
 spiedInstance = new Object() {};
 }

 @Test
 public void testCreateMockWithValidArguments() {
 Class<Object> classToMock = Object.class;
 MockSettingsImpl settings = new MockSettingsImpl(classToMock, null, spiedInstance);
 Object mock = mockUtil.createMock(classToMock, settings);

 verify(creationValidator, times(1)).validateType(classToMock);
 verify(creationValidator, times(1)).validateExtraInterfaces(classToMock, (Class[]) eq(settings.getExtraInterfaces()));
 verify(creationValidator, times(1)).validateMockedType(classToMock, eq(spiedInstance));

 ArgumentCaptor<MethodInterceptorFilter> filterArgumentCaptor = ArgumentCaptor.forClass(MethodInterceptorFilter.class);
 verify(ClassImposterizer.INSTANCE, times(1)).imposterise(eq(filterArgumentCaptor.capture()), classToMock, (Class[]) eq(settings.getExtraInterfaces()));
 MethodInterceptorFilter filter = filterArgumentCaptor.getValue();
 assertEquals(mockHandler, filter.getMockHandler());

 ArgumentCaptor<MockSettingsImpl> settingsArgumentCaptor = ArgumentCaptor.forClass(MockSettingsImpl.class);
 assertEquals(settings, filter.getMockSettings());

 assertEquals(mock, filter.getMock());
 assertEquals(getMockName(mock), Settings.DEFAULT_MOCK_NAME);
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockWithNullClassToMock() {
 Class<Object> classToMock = null;
 MockSettingsImpl settings = new MockSettingsImpl(classToMock, null, spiedInstance);
 mockUtil.createMock(classToMock, settings);
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockWithNullExtraInterfaces() {
 Class<Object> classToMock = Object.class;
 MockSettingsImpl settings = new MockSettingsImpl(classToMock, null, spiedInstance);
 settings.setExtraInterfaces(null);
 mockUtil.createMock(classToMock, settings);
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockWithInvalidSpiedInstance() {
 Class<Object> classToMock = Object.class;
 MockSettingsImpl settings = new MockSettingsImpl(classToMock, null, null);
 mockUtil.createMock(classToMock, settings);
 }

 @Test
 public void testResetMock() throws InvocationTargetException, IllegalAccessException {
 Object mock = Proxy.newProxyInstance(Object.class.getClassLoader(), new Class[]{Object.class}, new MockHandler<Object>(new MockSettingsImpl(Object.class, null, spiedInstance)));
 mockUtil.resetMock(mock);
 MethodInterceptorFilter newFilter = (MethodInterceptorFilter) Proxy.getInvocationHandler(mock);
 assertEquals(RETURNS_DEFAULTS, newFilter.getMockSettings().getDefaultAnswer());
 }

 @Test
 public void testIsMock() {
 Object mock = Proxy.newProxyInstance(Object.class.getClassLoader(), new Class[]{Object.class}, new MockHandler<Object>(new MockSettingsImpl(Object.class, null, spiedInstance)));
 assertTrue(mockUtil.isMock(mock));
 }

 @Test
 public void testIsNotAMock() {
 Object notAMock = new Object() {};
 assertFalse(mockUtil.isMock(notAMock));
 }
}