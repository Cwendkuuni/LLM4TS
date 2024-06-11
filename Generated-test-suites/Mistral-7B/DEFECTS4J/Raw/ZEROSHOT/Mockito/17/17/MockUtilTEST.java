import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;
import org.mockito.utils.ClassImposterizer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.powermock.reflect.Whitebox;

import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@PrepareForTest(MockUtil.class)
public class MockUtilTest {

 private MockUtil mockUtil;
 private MockHandler<Object> mockHandler;
 private Object mock;

 @Rule
 public PowerMockRule powerMockRule = new PowerMockRule();

 @Before
 public void setUp() {
 mockUtil = new MockUtil();
 mockHandler = mock(MockHandler.class);
 mock = mock(Object.class);
 }

 @Test
 public void testCreateMock() throws Exception {
 // Test creating a mock with no extra interfaces or spied instance
 Class<Object> classToMock = Object.class;
 MockSettingsImpl settings = new MockSettingsImpl(classToMock);
 Object createdMock = mockUtil.createMock(classToMock, settings);
 assertEquals(mockHandler, getInterceptor(createdMock).getHandler());

 // Test creating a mock with extra interfaces
 Class<Object> extraInterface = Mockito.class;
 settings.setExtraInterfaces(new Class<?>[]{extraInterface});
 Object createdMockWithExtraInterface = mockUtil.createMock(classToMock, settings);
 verify(mockHandler).interceptCallsTo(eq(extraInterface), any());

 // Test creating a mock with a spied instance
 Object spiedInstance = new Object();
 settings.setSpiedInstance(spiedInstance);
 Object createdMockWithSpiedInstance = mockUtil.createMock(classToMock, settings);
 assertEquals(spiedInstance, Whitebox.getInternalState(createdMockWithSpiedInstance, "instance"));

 // Test validation errors
 Class<NullPointerException> nullPointerExceptionClass = NullPointerException.class;
 try {
 mockUtil.createMock(null, settings);
 fail("Expected NotAMockException but got nothing.");
 } catch (NotAMockException e) {
 // Expected
 }
 try {
 mockUtil.createMock(Object.class, null);
 fail("Expected NotAMockException but got nothing.");
 } catch (NotAMockException e) {
 // Expected
 }
 try {
 mockUtil.createMock(Object.class, settings);
 settings.setExtraInterfaces(null);
 fail("Expected NotAMockException but got nothing.");
 } catch (NotAMockException e) {
 // Expected
 }
 try {
 mockUtil.createMock(Object.class, settings);
 settings.setSpiedInstance(new NullPointerException());
 fail("Expected NotAMockException but got nothing.");
 } catch (NotAMockException e) {
 // Expected
 }
 }

 @Test
 public void testResetMock() throws Exception {
 // Test resetting a mock with no intercepted methods
 resetMock(mock);
 verifyNoMoreInteractions(mock);

 // Test resetting a mock with intercepted methods
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 when(mock).mockMethod(any(), any());
 invokeMethod(mock, "mockMethod", new Object[]{invocation, "arg1"}, new Object[]{1L});
 resetMock(mock);
 verifyZeroInteractions(mock);

 // Test resetting a mock with a custom answer
 Answer<Object> answer = arg -> "answer";
 when(mock).thenAnswer(answer);
 invokeMethod(mock, "mockMethod", new Object[]{invocation, "arg1"}, new Object[]{1L});
 assertEquals("answer", invokeMethod(mock, "mockMethod", new Object[]{invocation, "arg1"}, new Object[]{1L}));
 resetMock(mock);
 invokeMethod(mock, "mockMethod", new Object[]{invocation, "arg1"}, new Object[]{1L});
 verifyZeroInteractions(mock);
 }

 @Test
 public void testGetMockHandler() throws Exception {
 Object mockWithMockitoMockHandler = PowerMockito.mock(Object.class, new Class[]{MockHandler.class});
 PowerMockito.whenNew(MockHandler.class).withArguments(any(MockSettingsImpl.class)).thenReturn(mockHandler);
 setInterceptor(mockWithMockitoMockHandler, new Interceptor(mockHandler));
 assertEquals(mockHandler, mockUtil.getMockHandler(mockWithMockitoMockHandler));
 }

 @Test
 public void testIsMockitoMock() throws Exception {
 assertTrue(mockUtil.isMockitoMock(mock));
 assertFalse(mockUtil.isMockitoMock(new Object()));
 }

 @Test
 public void testIsMock() throws Exception {
 assertTrue(mockUtil.isMock(mock));
 assertFalse(mockUtil.isMock(new Object()));
 }

 @Test
 public void testGetInterceptor() throws Exception {
 MethodInterceptorFilter interceptorFilter = mock(MethodInterceptorFilter.class);
 Factory factory = mock(Factory.class);
 when(factory.getCallback(0)).thenReturn(interceptorFilter);
 assertEquals(interceptorFilter, mockUtil.getInterceptor(mock));
 }

 @Test
 public void testGetMockName() throws Exception {
 MockName mockName = mock(MockName.class);
 when(mockHandler.getMockSettings()).thenReturn(settings);
 when(settings.getMockName()).thenReturn(mockName);
 assertEquals(mockName, mockUtil.getMockName(mock));
 }

 private void setInterceptor(Object mock, Interceptor interceptor) throws Exception {
 ClassImposterizer imposterizer = ClassImposterizer.INSTANCE;
 Method method = Factory.class.getDeclaredMethod("setCallback", int.class, Callback.class);
 method.setAccessible(true);
 method.invoke(imposterizer.getInstance(mock), 0, interceptor);
 }

 private Object invokeMethod(Object object, String methodName, Object[] arguments, Object[] expected) throws Exception {
 Method method = object.getClass().getMethod(methodName, arguments.getClass());
 method.invoke(object, arguments);
 assertEquals(expected, arguments);
 }
}