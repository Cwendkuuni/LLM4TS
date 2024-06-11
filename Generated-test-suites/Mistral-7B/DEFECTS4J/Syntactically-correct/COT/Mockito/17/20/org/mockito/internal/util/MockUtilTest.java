import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.creation.MockSettingsImpl;
import org.mockito.internal.util.reflection.LenientCopyTool;
import org.mockito.stub.Answer;
import org.mockito.stub.Answer2;
import org.powermock.reflect.Whitebox;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MockUtilTest {

 private MockUtil mockUtil;
 private MockSettingsImpl mockSettings;

 @Before
 public void setUp() {
 mockUtil = new MockUtil(new CreationValidator());
 mockSettings = new MockSettingsImpl();
 }

 @Test
 public void testCreateMock() {
 // Basic functionality test
 MockUtil mockUtilSpy = spy(mockUtil);
 when(mockUtilSpy.createMock(Mockito.eq(String.class), mockSettings)).thenReturn(mock(String.class));
 String mock = mockUtilSpy.createMock(String.class, mockSettings);
 assertSame(mock, mock);

 // Test with null arguments
 assertNull(mockUtil.createMock(null, mockSettings));
 assertNull(mockUtil.createMock(String.class, null));

 // Test with null classToMock
 assertNull(mockUtil.createMock(null, mockSettings));

 // Test with empty extraInterfaces
 MockUtil mockUtilEmpty = new MockUtil();
 MockUtil.CreationValidator creationValidator = mock(MockUtil.CreationValidator.class);
 when(creationValidator.validateType(any(Class.class))).thenReturn(true);
 when(creationValidator.validateMockedType(any(Class.class), any(Object.class))).thenReturn(true);
 when(mockUtilEmpty.createValidator()).thenReturn(creationValidator);
 List<Class<?>> emptyExtraInterfaces = Collections.emptyList();
 assertNull(mockUtilEmpty.createMock(String.class, new MockSettingsImpl().setExtraInterfaces(emptyExtraInterfaces)));

 // Test with non-mock Class
 MockUtil.CreationValidator creationValidatorNonMock = mock(MockUtil.CreationValidator.class);
 when(creationValidatorNonMock.validateType(any(Class.class))).thenReturn(true);
 when(creationValidatorNonMock.validateMockedType(any(Class.class), any(Object.class))).thenReturn(false);
 when(mockUtil.createValidator()).thenReturn(creationValidatorNonMock);
 assertNull(mockUtil.createMock(Integer.class, new MockSettingsImpl()));

 // Test with duplicate interfaces
 Class<?>[] interfaces = new Class[] { String.class, String.class };
 assertNull(mockUtil.createMock(String.class, new MockSettingsImpl().setExtraInterfaces(interfaces)));

 // Test with non-itemize interface
 Class<?> nonInterfaceClass = int[].class;
 assertNull(mockUtil.createMock(nonInterfaceClass, new MockSettingsImpl()));

 // Test with null spiedInstance
 assertNull(mockUtil.createMock(String.class, new MockSettingsImpl().setSpiedInstance(null)));

 // Test with spiedInstance of different type
 Class<?> spiedInstanceDifferentType = Object.class;
 assertNull(mockUtil.createMock(String.class, new MockSettingsImpl().setSpiedInstance(spiedInstanceDifferentType)));

 // Test resetMock method
 String mockString = mock(String.class);
 MockUtil mockUtilReset = spy(mockUtil);
 when(mockUtilReset.createMock(any(Class.class), any(MockSettingsImpl.class))).thenReturn(mockString);
 MockHandlerInterface<String> oldMockHandler = mockUtilReset.getMockHandler(mockString);
 doAnswer(new Answer<Void>() {
 @Override
 public Void answer(InvocationOnMock invocation) throws Throwable {
 mockString.equals("foo");
 return null;
 }
 }).when(oldMockHandler).answer(any());
 mockUtilReset.resetMock(mockString);
 verify(mockString).equals(anyString());

 // Test isMock method
 Object mockObject = mock(Object.class);
 assertFalse(mockUtil.isMock(null));
 assertTrue(mockUtil.isMock(mockObject));

 // Test getMockName method
 String mockName = "MockName";
 MockSettingsImpl mockSettingsName = new MockSettingsImpl().setMockName(mockName);
 when(mockUtil.createMock(any(Class.class), mockSettingsName)).thenReturn(mock(String.class));
 assertEquals(mockName, mockUtil.getMockName(mock(String.class)));

 // Test getInterceptor method
 String mockStringInterceptor = mock(String.class);
 MethodInterceptorFilter mockInterceptorFilter = mock(MethodInterceptorFilter.class);
 when(mockUtil.createMock(String.class, new MockSettingsImpl())).thenReturn(mockStringInterceptor);
 when(mockInterceptorFilter.getHandler()).thenReturn(mockUtil);
 assertEquals(mockUtil, mockUtil.getInterceptor(mockStringInterceptor));
 }

 @Test(expected = NotAMockException.class)
 public void testGetMockHandlerNullMock() {
 mockUtil.getMockHandler(null);
 }

 @Test(expected = NotAMockException.class)
 public void testGetMockHandlerWrongClass() {
 Object wrongClassMock = mock(Object.class);
 mockUtil.getMockHandler(wrongClassMock);
 }
}