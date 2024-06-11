import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.creation.CreationValidator;
import org.mockito.internal.util.reflection.LenientCopyTool;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.mock.MockComponent;
import org.powermock.reflect.WhiteboxImpl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

public class MockUtilTest {

 private MockUtil mockUtil;
 private CreationValidator creationValidatorMock;
 private LenientCopyTool lenientCopyToolMock;
 private ClassImposterizer classImposterizerMock;
 private Whitebox whitebox;

 @Before
 public void setUp() {
 creationValidatorMock = Mockito.mock(CreationValidator.class);
 lenientCopyToolMock = Mockito.mock(LenientCopyTool.class);
 classImposterizerMock = Mockito.mock(ClassImposterizer.class);
 whitebox = new WhiteboxImpl();
 mockUtil = new MockUtil(creationValidatorMock);
 }

 @Test
 public void testCreateMock() {
 Class<String> classToMock = String.class;
 MockSettingsImpl settings = new MockSettingsImpl();
 settings.setExtraInterfaces(new Class[0]);
 settings.setSpiedInstance(null);

 mockUtil.createValidator().validateType(classToMock);
 mockUtil.createValidator().validateExtraInterfaces(classToMock, new Class[0]);
 mockUtil.createValidator().validateMockedType(classToMock, null);

 MockHandler<String> mockHandler = Mockito.mock(MockHandler.class);
 MethodInterceptorFilter filter = Mockito.mock(MethodInterceptorFilter.class);
 Class<?>[] ancillaryTypes = new Class[0];

 Whitebox.setInternalState(mockUtil, "classImposterizer", classImposterizerMock);
 Whitebox.setInternalState(mockUtil, "creationValidator", creationValidatorMock);
 Whitebox.setInternalState(mockUtil, "lenientCopyTool", lenientCopyToolMock);

 when(creationValidatorMock.validateType(classToMock)).thenReturn(null);
 when(creationValidatorMock.validateExtraInterfaces(classToMock, ArgumentMatchers.array(Class.class))).thenReturn(null);
 when(creationValidatorMock.validateMockedType(classToMock, null)).thenReturn(null);
 when(classImposterizerMock.imposterise(ArgumentMatchers.any(MethodInterceptorFilter.class), ArgumentMatchers.eq(String.class), ArgumentMatchers.array(Class.class))).thenReturn(Mockito.mock(String.class));
 when(lenientCopyToolMock.copyToMock(ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(null);

 Object mock = mockUtil.createMock(classToMock, settings);
 assertNotNull(mock);
 assertSame(String.class, mock.getClass());
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockWithNullClass() {
 Class<Object> classToMock = null;
 mockUtil.createMock(classToMock, new MockSettingsImpl());
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockWithInvalidClass() {
 Class<Integer> classToMock = int.class;
 mockUtil.createMock(classToMock, new MockSettingsImpl());
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockWithNullExtraInterfaces() {
 Class<String> classToMock = String.class;
 MockSettingsImpl settings = new MockSettingsImpl();
 settings.setExtraInterfaces(null);
 settings.setSpiedInstance(null);

 mockUtil.createValidator().validateType(classToMock);
 mockUtil.createValidator().validateExtraInterfaces(classToMock, new Class[0]);
 mockUtil.createValidator().validateMockedType(classToMock, null);

 mockUtil.createMock(classToMock, settings);
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockWithExtraInterfaces() {
 Class<String> classToMock = String.class;
 Class<StringBuilder> extraInterface = StringBuilder.class;
 MockSettingsImpl settings = new MockSettingsImpl();
 settings.setExtraInterfaces(new Class[]{extraInterface});
 settings.setSpiedInstance(null);

 mockUtil.createValidator().validateType(classToMock);
 mockUtil.createValidator().validateExtraInterfaces(classToMock, new Class[]{extraInterface});
 mockUtil.createValidator().validateMockedType(classToMock, null);

 mockUtil.createMock(classToMock, settings);
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockWithSpiedInstanceOfDifferentType() {
 Class<String> classToMock = String.class;
 Object spiedInstance = new Object();
 MockSettingsImpl settings = new MockSettingsImpl();
 settings.setExtraInterfaces(new Class[0]);
 settings.setSpiedInstance(spiedInstance);

 mockUtil.createValidator().validateType(classToMock);
 mockUtil.createValidator().validateExtraInterfaces(classToMock, new Class[0]);
 mockUtil.createValidator().validateMockedType(classToMock, spiedInstance.getClass());

 mockUtil.createMock(classToMock, settings);
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockWithSpiedInstance() {
 Class<String> classToMock = String.class;
 Object spiedInstance = Mockito.mock(String.class);
 MockSettingsImpl settings = new MockSettingsImpl();
 settings.setExtraInterfaces(new Class[0]);
 settings.setSpiedInstance(spiedInstance);

 mockUtil.createValidator().validateType(classToMock);
 mockUtil.createValidator().validateExtraInterfaces(classToMock, new Class[0]);
 mockUtil.createValidator().validateMockedType(classToMock, String.class);

 mockUtil.createMock(classToMock, settings);
 }

 @Test
 public void testResetMock() {
 Object mock = Mockito.mock(String.class);
 mockUtil.resetMock(mock);

 MockHandlerInterface<String> oldMockHandler = mockUtil.getMockHandler(mock);
 MockHandler<String> newMockHandler = Mockito.mock(MockHandler.class);
 MethodInterceptorFilter newFilter = Mockito.mock(MethodInterceptorFilter.class);

 Whitebox.setInternalState(mockUtil, "creationValidator", creationValidatorMock);
 Whitebox.setInternalState(mockUtil, "lenientCopyTool", lenientCopyToolMock);

 when(creationValidatorMock.validateType(String.class)).thenReturn(null);
 when(creationValidatorMock.validateExtraInterfaces(String.class, ArgumentMatchers.anyArray(Class.class))).thenReturn(null);
 when(creationValidatorMock.validateMockedType(String.class, null)).thenReturn(null);
 when(classImposterizerMock.imposterise(ArgumentMatchers.any(MethodInterceptorFilter.class), ArgumentMatchers.eq(String.class), ArgumentMatchers.array(Class.class))).thenReturn(Mockito.mock(String.class));
 when(lenientCopyToolMock.copyToMock(ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(null);
 when(mockUtil.getMockHandler(mock)).thenReturn(oldMockHandler);

 mockUtil.createMock(String.class, new MockSettingsImpl());

 Whitebox.setInternalState(mock, "callbacks", new Object[]{oldMockHandler});
 Whitebox.setInternalState(mock, "factory", new MockComponent.FactoryMock(oldMockHandler));

 verify(oldMockHandler).getMockSettings();
 assertSame(oldMockHandler, getMockHandler(mock));

 newMockHandler = Mockito.mock(MockHandler.class);
 Whitebox.setInternalState(mockUtil, "creationValidator", creationValidatorMock);
 Whitebox.setInternalState(mockUtil, "lenientCopyTool", lenientCopyToolMock);
 Whitebox.setInternalState(mock, "callbacks", new Object[]{newMockHandler});
 Whitebox.setInternalState(mock, "factory", new MockComponent.FactoryMock(newMockHandler));

 mockUtil.resetMock(mock);

 assertSame(newMockHandler, getMockHandler(mock));
 }

 @Test
 public void testIsMock() {
 Object mock = Mockito.mock(String.class);
 assertSame(mockUtil.isMock(mock), true);
 }

 @Test
 public void testIsMockWithNullMock() {
 assertSame(mockUtil.isMock(null), false);
 }

 @Test
 public void testIsMockWithNonMockitoMock() {
 Object mock = new Object();
 assertSame(mockUtil.isMock(mock), false);
 }

 @Test
 public void testGetMockHandler() {
 Object mock = Mockito.mock(String.class);
 assertSame(mockUtil.getMockHandler(mock), Mockito.mock(MockHandler.class));
 }

 @Test(expected = NotAMockException.class)
 public void testGetMockHandlerWithNullMock() {
 mockUtil.getMockHandler(null);
 }

 @Test(expected = NotAMockException.class)
 public void testGetMockHandlerWithNonMockitoMock() {
 Object mock = new Object();
 mockUtil.getMockHandler(mock);
 }

 @Test
 public void testGetInterceptor() {
 Object mock = Mockito.mock(String.class);
 InvocationHandler invocationHandler = Proxy.getInvocationHandler(mock);
 MethodInterceptorFilter methodInterceptorFilter = mockUtil.getInterceptor(mock);
 assertNotNull(methodInterceptorFilter);
 assertSame(invocationHandler, methodInterceptorFilter.getInterceptor());
 }

 @Test
 public void testGetInterceptorWithNullMock() {
 assertNull(mockUtil.getInterceptor(null));
 }

 @Test
 public void testGetInterceptorWithNonMockitoMock() {
 Object mock = new Object();
 assertNull(mockUtil.getInterceptor(mock));
 }

 @Test
 public void testGetMockName() {
 MockSettingsImpl settings = new MockSettingsImpl();
 settings.setMockName("MockName");
 Object mock = Mockito.mock(String.class);
 assertSame(mockUtil.getMockName(mock), "MockName");
 }

 @Test
 public void testGetMockNameWithNullMock() {
 assertNull(mockUtil.getMockName(null));
 }
}