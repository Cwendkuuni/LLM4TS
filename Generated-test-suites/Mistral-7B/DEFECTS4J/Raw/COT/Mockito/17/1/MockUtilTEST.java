import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.CreationValidator;
import org.mockito.internal.creation.MethodInterceptor;
import org.mockito.internal.creation.MockHandler;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.util.reflection.LenientCopyTool;
import org.mockito.exceptions.misusing.NotAMockException;
import java.lang.reflect.Proxy;

@RunWith(MockitoJUnitRunner.class)
public class MockUtilTest {

 private CreationValidator creationValidator = Mockito.spy(new CreationValidator());
 private MockUtil mockUtil;

 @SuppressWarnings("unchecked")
 @Before
 public void setup() {
 mockUtil = new MockUtil(creationValidator);
 }

 @Test(expected = NullPointerException.class)
 public void testCreateMockWithNullClass() {
 mockUtil.createMock(null, new MockSettingsImpl(Mockito.mock(Object.class), new Object[0]));
 }

 @Test
 public void testCreateMockWithValidArguments() {
 Class<String> stringClass = String.class;
 Object spiedInstance = "test";
 MockSettingsImpl settings = new MockSettingsImpl(spiedInstance, new Object[0]);
 String mock = mockUtil.createMock(stringClass, settings);
 MockHandler<String> mockHandler = mockUtil.getMockHandler(mock);
 assert mockHandler != null;
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateMockWithInvalidType() {
 Class<Integer> integerClass = Integer.class;
 Object spiedInstance = 1;
 MockSettingsImpl settings = new MockSettingsImpl(spiedInstance, new Object[0]);
 mockUtil.createMock(String.class, settings);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateMockWithExtraInterfaces() {
 Class<String> stringClass = String.class;
 Object spiedInstance = "test";
 MockSettingsImpl settings = new MockSettingsImpl(spiedInstance, new Object[] { Array.class });
 mockUtil.createMock(stringClass, settings);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateMockWithSpiedInstanceOfDifferentType() {
 Class<String> stringClass = String.class;
 Object spiedInstance = new Object();
 MockSettingsImpl settings = new MockSettingsImpl(spiedInstance, new Object[0]);
 mockUtil.createMock(stringClass, settings);
 }

 @Test
 public void testResetMock() {
 Object mock = Mockito.mock(Object.class);
 mockUtil.resetMock(mock);
 }

 @Test(expected = NullPointerException.class)
 public void testGetMockHandlerWithNullMock() {
 mockUtil.getMockHandler(null);
 }

 @Test
 public void testIsMock() {
 Object mock = Mockito.mock(Object.class);
 assert mockUtil.isMock(mock);
 }

 @Test(expected = NotAMockException.class)
 public void testIsMockWithNonMockitoMock() {
 Object nonMockitoMock = Proxy.newProxyInstance(MockUtil.class.getClassLoader(), new Class[] { String.class }, new MethodInterceptor() {
 @Override
 public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
 throw new RuntimeException();
 }
 });
 assertFalse(mockUtil.isMock(nonMockitoMock));
 }

 @Test(expected = NotAMockException.class)
 public void testGetMockHandlerWithNonMockitoMock() {
 Object nonMockitoMock = Proxy.newProxyInstance(MockUtil.class.getClassLoader(), new Class[] { String.class }, new MethodInterceptor() {
 @Override
 public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
 throw new RuntimeException();
 }
 });
 mockUtil.getMockHandler(nonMockitoMock);
 }
}