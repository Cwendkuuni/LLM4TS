import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;
import org.mockito.stub.Answer2;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

@PrepareForTest(MockUtil.class)
public class MockUtilTest {

 private MockUtil mockUtil;
 private ClassImposterizer classImposterizer;
 private MethodInterceptorFilter filter;
 private Factory mock;
 private MockSettingsImpl settings;

 @Before
 public void setup() {
 mockUtil = new MockUtil(new CreationValidator());
 classImposterizer = Mockito.mock(ClassImposterizer.class);
 filter = Mockito.mock(MethodInterceptorFilter.class);
 settings = new MockSettingsImpl();
 mock = Mockito.mock(Factory.class);
 }

 @Test
 public void testCreateMock() throws Exception {
 // Test creating a mock with no extra interfaces or spied instance
 Class<?> mockClass = Mockito.mock(String.class);
 T mock = mockUtil.createMock(mockClass.getClass(), settings);
 assertNotNull(mock);
 assertEquals(mockClass, mock.getClass());

 // Test creating a mock with extra interfaces
 Class<?>[] extraInterfaces = new Class<?>[]{String[].class};
 settings.setExtraInterfaces(extraInterfaces);
 T extraInterfaceMock = mockUtil.createMock(String[].class, settings);
 assertNotNull(extraInterfaceMock);
 assertArrayEquals(new String[]{"foo", "bar"}, (String[]) extraInterfaceMock);
 // Test creating a mock with a spied instance
 Object spiedInstance = new Object(){};
 settings.setSpiedInstance(spiedInstance);
 T spiedMock = mockUtil.createMock(String.class, settings);
 assertNotNull(spiedMock);
 assertEquals(spiedInstance, spiedMock);

 // Test throwing exceptions when invalid arguments are provided
 assertThrows(NullPointerException.class, () -> mockUtil.createMock(null, settings));
 assertThrows(NullPointerException.class, () -> mockUtil.createMock(String.class, null));
 }

 @Test
 public void testResetMock() throws Exception {
 // Test resetting a mock with no extra interfaces or spied instance
 when(filter.intercept(any(), eq(0), any(), any()))
 .thenAnswer(new Answer<Object>() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 throw new RuntimeException("Previous mock behavior");
 }
 });
 mockUtil.resetMock(mock);
 verify(filter).intercept(eq(mock), eq(0), any(), any());

 // Test resetting a mock with extra interfaces
 settings.setExtraInterfaces(new Class<?>[]{String[].class});
 when(filter.intercept(any(), eq(0), any(), any()))
 .thenAnswer(new Answer2<Object, Object>() {
 @Override
 public Object answer(InvocationOnMock invocation, Object extraArgument) throws Throwable {
 return "mocked string";
 }
 });
 mockUtil.resetMock(mock);
 verify(filter).intercept(eq(mock), eq(0), any(), any());
 assertEquals("mocked string", mock.getString());
 }

 @Test
 public void testGetMockHandler() throws Exception {
 // Test getting a mock handler for a valid mock
 T mock = mockUtil.createMock(String.class, settings);
 MockHandlerInterface<T> handler = mockUtil.getMockHandler(mock);
 assertNotNull(handler);
 assertEquals(mock, handler.getMock());

 // Test getting a mock handler for a null mock
 assertNull(mockUtil.getMockHandler(null));
 }

 @Test
 public void testIsMockitoMock() throws Exception {
 // Test isMockitoMock returns true for valid mocks
 T mock = mockUtil.createMock(String.class, settings);
 assertTrue(mockUtil.isMockitoMock(mock));

 // Test isMockitoMock returns false for non-mockito mocks
 Object nonMockitoMock = Mockito.mock(String.class);
 assertFalse(mockUtil.isMockitoMock(nonMockitoMock));
 }

 @Test
 public void testIsMock() throws Exception {
 // Test isMock returns true for valid mocks
 T mock = mockUtil.createMock(String.class, settings);
 assertTrue(mockUtil.isMock(mock));

 // Test isMock returns false for null mocks and non-mockito mocks
 assertFalse(mockUtil.isMock(null));
 assertFalse(mockUtil.isMock(new Object()));
 }

 @Test
 public void testGetInterceptor() throws Exception {
 // Test getting a valid interceptor
 T mock = mockUtil.createMock(String.class, settings);
 MethodInterceptorFilter interceptor = mockUtil.getInterceptor(mock);
 assertNotNull(interceptor);

 // Test getting a null interceptor for a null mock
 assertNull(mockUtil.getInterceptor(null));
 }

 @Test
 public void testGetMockName() throws Exception {
 // Test getting a mock name for a mock with no name set
 T mock = mockUtil.createMock(String.class, settings);
 MockName name = mockUtil.getMockName(mock);
 assertNotNull(name);
 assertEquals("MockUtil_mock_<class String>", name.getName());

 // Test getting a mock name for a mock with a custom name set
 settings.setMockName("MyMockName");
 T customNameMock = mockUtil.createMock(String.class, settings);
 MockName customName = mockUtil.getMockName(customNameMock);
 assertNotNull(customName);
 assertEquals("MyMockName", customName.getName());
 }

 @Test
 public void testCreateMockWithCustomName() throws Exception {
 // Test creating a mock with a custom name
 String customName = "MyMockName";
 T customNameMock = mockUtil.createMock(String.class, new MockSettingsImpl().setMockName(customName));
 MockName name = mockUtil.getMockName(customNameMock);
 assertNotNull(name);
 assertEquals(customName, name.getName());
 }
}