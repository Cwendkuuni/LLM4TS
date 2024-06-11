import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.quality.Strictness;
import org.mockito.stubbing.Answer;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.CreationValidator;
import org.mockito.internal.creation.MethodInterceptorFilter;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.creation.ClassImposterizer;
import org.mockito.internal.util.reflection.LenientCopyTool;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.util.MockHandler;
import org.mockito.internal.util.MockHandlerInterface;
import org.mockito.internal.util.MockName;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.internal.creation.Factory;

import static org.mockito.Mockito.*;
import static org.mockito.quality.Strictness.LENIENT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

@RunWith(MockitoJUnitRunner.class)
public class MockUtilTest {

 private MockUtil mockUtil;
 private CreationValidator creationValidatorMock;
 private MockSettingsImpl mockSettingsMock;
 private ClassImposterizer classImposterizerMock;
 private LenientCopyTool lenientCopyToolMock;
 private Factory factoryMock;
 private MethodInterceptorFilter methodInterceptorFilterMock;
 private Object mock;
 private Object spiedInstance;

 @Before
 public void setup() {
 creationValidatorMock = mock(CreationValidator.class);
 mockSettingsMock = mock(MockSettingsImpl.class);
 classImposterizerMock = mock(ClassImposterizer.class);
 lenientCopyToolMock = mock(LenientCopyTool.class);
 mockUtil = new MockUtil(creationValidatorMock);
 factoryMock = mock(Factory.class);
 }

 @Test
 public void testCreateMock() {
 // Test creating a mock with no extra interfaces or spied instance
 Class<String> stringClass = String.class;
 mockUtil.createMock(stringClass, mockSettingsMock);
 verify(creationValidatorMock).validateType(stringClass);
 verify(creationValidatorMock).validateExtraInterfaces(eq(stringClass), isNull());
 verifyNoMoreInteractions(creationValidatorMock);

 // Test creating a mock with extra interfaces
 Class<?> extraInterface1 = Mockito.class;
 Class<?> extraInterface2 = Mockito.mock(String.class);
 mockSettingsMock.setExtraInterfaces(new Class<?>[]{extraInterface1, extraInterface2});
 verify(creationValidatorMock).validateType(stringClass);
 verify(creationValidatorMock).validateExtraInterfaces(eq(stringClass), isArray(instanceOf(Class.class)));

 // Test creating a mock with a spied instance
 spiedInstance = "test";
 mockSettingsMock.setSpiedInstance(spiedInstance);
 verify(classImposterizerMock).imposterise(eq(methodInterceptorFilterMock), eq(stringClass), isArray(eq(extraInterface1)));
 new LenientCopyTool().copyToMock(spiedInstance, eq(mock));
 verify(lenientCopyToolMock).copyToMock(eq(spiedInstance), eq(mock));

 // Test exception when creating a mock with null class
 assertThrows(NullPointerException.class, () -> mockUtil.createMock(null, mockSettingsMock));
 }

 @Test
 public void testResetMock() {
 mock = mock(String.class);
 factoryMock.setCallback(0, methodInterceptorFilterMock);
 mockUtil.resetMock(mock);
 verify(methodInterceptorFilterMock).setHandler(any());
 }

 @Test
 public void testGetMockHandler() {
 mock = mock(String.class);
 when(getInterceptor(mock)).thenReturn(methodInterceptorFilterMock);
 assertSame(methodInterceptorFilterMock, mockUtil.getMockHandler(mock));
 }

 @Test(expected = NotAMockException.class)
 public void testGetMockHandlerNullMock() {
 assertNull(mockUtil.getMockHandler(null));
 }

 @Test(expected = NotAMockException.class)
 public void testGetMockHandlerNonMockitoMock() {
 Object nonMockitoMock = new Object();
 assertNull(mockUtil.getMockHandler(nonMockitoMock));
 }

 @Test
 public void testIsMockitoMock() {
 mock = mock(String.class);
 assertFalse(mockUtil.isMock(mock));

 mock = mockitoMock(String.class);
 assertTrue(mockUtil.isMock(mock));
 }

 @Test
 public void testIsMock() {
 mock = mock(String.class);
 assertFalse(mockUtil.isMock(mock));

 mockitoMock = mockitoMock(String.class);
 assertTrue(mockUtil.isMock(mockitoMock));
 }

 @Test
 public void testGetInterceptor() {
 mock = mockitoMock(String.class);
 Factory factory = spy(factoryMock);
 when(factory.getCallback(0)).thenReturn(methodInterceptorFilterMock);
 assertSame(methodInterceptorFilterMock, mockUtil.getInterceptor(mock));
 }

 @Test
 public void testGetMockName() {
 mock = mockitoMock(String.class);
 MockName mockName = mockitoMock(MockName.class);
 when(mockHandler(mock).getMockSettings()).thenReturn(mockSettingsMock);
 when(mockSettingsMock.getMockName()).thenReturn(mockName);
 assertEquals(mockName, mockUtil.getMockName(mock));
 }

 @Test
 public void testValidateType() {
 creationValidatorMock.validateType(String.class);
 verify(creationValidatorMock).validateType(eq(String.class));
 }

 @Test
 public void testValidateExtraInterfaces() {
 Class<?>[] extraInterfaces = new Class<?>[0];
 creationValidatorMock.validateExtraInterfaces(String.class, extraInterfaces);
 verify(creationValidatorMock).validateExtraInterfaces(eq(String.class), isArray(instanceOf(Class.class)));

 Class<?> extraInterface = Mockito.class;
 creationValidatorMock.validateExtraInterfaces(String.class, new Class<?>[]{extraInterface});
 verify(creationValidatorMock).validateExtraInterfaces(eq(String.class), isArray(instanceOf(Class.class)));
 }

 @Test
 public void testValidateMockedType() {
 Class<String> stringClass = String.class;
 creationValidatorMock.validateMockedType(stringClass, null);
 verify(creationValidatorMock).validateMockedType(eq(stringClass), isNull());

 Object spiedInstance = new Object();
 creationValidatorMock.validateMockedType(stringClass, spiedInstance);
 verify(creationValidatorMock).validateMockedType(eq(stringClass), eq(spiedInstance));
 }

 @Test
 public void testInitiateMockName() {
 Class<String> stringClass = String.class;
 mockSettingsMock.initiateMockName(stringClass);
 verify(mockSettingsMock).initiateMockName(eq(stringClass));
 }

 @Test
 public void testImposterise() {
 MethodInterceptorFilter methodInterceptorFilter = mock(MethodInterceptorFilter.class);
 Class<String> stringClass = String.class;
 Class<?>[] extraInterfaces = new Class<?>[0];
 Object spiedInstance = new Object();
 verify(classImposterizerMock).imposterise(eq(methodInterceptorFilter), eq(stringClass), isArray(eq(extraInterfaces)));
 }

 @Test
 public void testCopyToMock() {
 Class<String> stringClass = String.class;
 String stringValue = "test";
 Object spiedInstance = new Object();
 Whitebox.setInternalState(spiedInstance, "value", stringValue);
 Object mock = mockitoMock(String.class);
 lenientCopyToolMock.copyToMock(spiedInstance, mock);
 assertEquals(stringValue, Whitebox.getInternalState(mock, "value"));
 }
}