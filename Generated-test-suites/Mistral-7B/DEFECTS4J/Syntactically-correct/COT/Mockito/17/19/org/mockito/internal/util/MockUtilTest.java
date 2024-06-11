import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.creation.CreationValidator;
import org.mockito.internal.util.reflection.LenientCopyTool;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.util.MockSettingsImpl;
import org.mockito.internal.creation.MethodInterceptorFilter;
import org.mockito.internal.creation.MockHandler;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.util.MockHandlerInterface;
import org.mockito.internal.util.reflection.Whitebox2;
import org.mockito.invocation.MethodInterceptor;
import org.mockito.internal.creation.Factory;
import org.mockito.internal.util.MockName;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
public class MockUtilTest {

 private CreationValidator creationValidator;
 private LenientCopyTool lenientCopyTool;
 private MockUtil mockUtil;

 @Before
 public void setUp() {
 creationValidator = mock(CreationValidator.class);
 lenientCopyTool = mock(LenientCopyTool.class);
 mockUtil = new MockUtil(creationValidator);
 }

 @Test
 public void testCreateMockWithValidArguments() {
 Class<String> stringClass = String.class;
 MockSettingsImpl settings = new MockSettingsImpl();
 String spiedInstance = "mock instance";

 when(creationValidator.validateType(eq(stringClass))).thenReturn(null);
 when(creationValidator.validateExtraInterfaces(eq(stringClass), anyCollection())).thenReturn(new Class[0]);
 when(creationValidator.validateMockedType(eq(stringClass), eq(spiedInstance))).thenReturn(null);

 MockHandler<String> mockHandler = mock(MockHandler.class);
 MethodInterceptorFilter filter = mock(MethodInterceptorFilter.class);
 MockSettingsImpl mockSettings = new MockSettingsImpl();
 mockSettings.initiateMockName(stringClass);

 when(ClassImposterizer.INSTANCE.imposterise(eq(filter), eq(String.class), anyArray())).thenReturn(mockHandler);
 new LenientCopyTool().copyToMock(spiedInstance, mockHandler.getMock());

 assertEquals(mockHandler.getMock(), mockUtil.createMock(stringClass, settings));
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockWithNullClassArgument() {
 Class<Object> nullClass = null;
 MockSettingsImpl settings = new MockSettingsImpl();
 assertNull(mockUtil.createMock(nullClass, settings));
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockWithInvalidClassArgument() {
 Class<String> stringClass = String.class;
 MockSettingsImpl settings = new MockSettingsImpl();
 assertNull(mockUtil.createMock(Object.class, settings));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateMockWithInvalidSettingsArgument() {
 Class<String> stringClass = String.class;
 MockSettingsImpl settings = null;
 assertNull(mockUtil.createMock(stringClass, settings));
 }

 @Test
 public void testCreateMockWithNullSpiedInstance() {
 Class<String> stringClass = String.class;
 MockSettingsImpl settings = new MockSettingsImpl();
 String spiedInstance = null;
 assertEquals(null, mockUtil.createMock(stringClass, settings));
 }

 @Test
 public void testCreateMockWithSpiedInstance() {
 Class<String> stringClass = String.class;
 MockSettingsImpl settings = new MockSettingsImpl();
 String spiedInstance = "mock instance";
 assertEquals(spiedInstance, mockUtil.createMock(stringClass, settings).getValue());
 }

 @Test
 public void testResetMock() {
 String mock = mock(String.class);
 mockUtil.resetMock(mock);

 MethodInterceptor methodInterceptor = PowerMockito.mock(MethodInterceptor.class);
 MethodInterceptorFilter filter = new MethodInterceptorFilter(methodInterceptor, new MockSettingsImpl());
 Factory factory = (Factory) mock;
 factory.setCallback(0, filter);

 InvocationOnMock invocationOnMock = mock(InvocationOnMock.class);
 Whitebox.setInternalValue(methodInterceptor, "interceptor", invocationOnMock);
 Whitebox.setInternalValue(filter, "filter", invocationOnMock);
 Whitebox2.setInternalValue(factory, "callback", filter);

 verify(methodInterceptor).intercept(any(), any(), anyArray(), anyInt(), any());
 }

 @Test(expected = NotAMockException.class)
 public void testGetMockHandlerWithNullMockArgument() {
 Object mock = null;
 assertNull(mockUtil.getMockHandler(mock));
 }

 @Test(expected = NotAMockException.class)
 public void testGetMockHandlerWithNonMockitoMockArgument() {
 Object mock = new Object();
 assertNull(mockUtil.getMockHandler(mock));
 }

 @Test
 public void testIsMockWithNullMockArgument() {
 Object mock = null;
 assertFalse(mockUtil.isMock(mock));
 }

 @Test
 public void testIsMockWithNonMockitoMockArgument() {
 Object mock = new Object();
 assertFalse(mockUtil.isMock(mock));
 }

 @Test
 public void testIsMockWithMockitoMockArgument() {
 String mock = mock(String.class);
 assertTrue(mockUtil.isMock(mock));
 }

 @Test
 public void testGetInterceptorWithNullMockArgument() {
 Object mock = null;
 assertNull(mockUtil.getInterceptor(mock));
 }

 @Test
 public void testGetInterceptorWithMockitoMockArgument() {
 String mock = mock(String.class);
 MethodInterceptor interceptor = mockUtil.getInterceptor(mock);
 assertNotNull(interceptor);
 }

 @Test
 public void testGetMockNameWithNullMockArgument() {
 Object mock = null;
 assertNull(mockUtil.getMockName(mock));
 }

 @Test
 public void testGetMockNameWithMockitoMockArgument() {
 String mock = mock(String.class);
 MockName mockName = mockUtil.getMockName(mock);
 assertNotNull(mockName);
 }
}