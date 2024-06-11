import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.internal.MockHandler;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.MockSettingsImpl;
import org.mockito.internal.util.MockCreationValidator;
import org.mockito.internal.util.reflection.LenientCopyTool;
import org.mockito.internal.util.reflection.MethodInterceptorFilter;
import org.mockito.internal.util.reflection.MethodInterceptor;
import org.mockito.internal.util.reflection.ProxyFactory;
import org.mockito.exceptions.misusing.NotAMockException;
import java.io.Serializable;
import static org.junit.Assert.*;

public class MockUtilTest {

 private MockUtil mockUtil;
 private MockCreationValidator mockCreationValidator;

 @Before
 public void setUp() {
 mockCreationValidator = MockCreationValidator.create();
 mockUtil = new MockUtil(mockCreationValidator);
 }

 @Test
 public void testCreateMockWithValidArguments() {
 Class<String> classToMock = String.class;
 Object spiedInstance = "Test String";
 MockSettingsImpl settings = new MockSettingsImpl();
 settings.setSpiedInstance(spiedInstance);

 String mock = mockUtil.create(classToMock, settings);
 assertNotNull(mock);
 assertTrue(isMockitoMock(mock));
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockWithNullClassToMock() {
 Class<Object> classToMock = null;
 Object spiedInstance = new Object();
 MockSettingsImpl settings = new MockSettingsImpl();
 settings.setSpiedInstance(spiedInstance);

 mockUtil.create(classToMock, settings);
 fail("Expected NotAMockException but got none");
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockWithInvalidClassToMock() {
 Class<Object> classToMock = Object[].class;
 Object spiedInstance = new Object();
 MockSettingsImpl settings = new MockSettingsImpl();
 settings.setSpiedInstance(spiedInstance);

 mockUtil.create(classToMock, settings);
 fail("Expected NotAMockException but got none");
 }

 @Test
 public void testCreateMockWithExtraInterfaces() {
 Class<String> classToMock = String.class;
 Object spiedInstance = "Test String";
 MockSettingsImpl settings = new MockSettingsImpl();
 settings.setSpiedInstance(spiedInstance);
 settings.setExtraInterfaces(new Class[] {Serializable.class});

 String mock = mockUtil.create(classToMock, settings);
 assertNotNull(mock);
 assertTrue(isMockitoMock(mock));
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockWithInvalidExtraInterface() {
 Class<String> classToMock = String.class;
 Object spiedInstance = "Test String";
 MockSettingsImpl settings = new MockSettingsImpl();
 settings.setSpiedInstance(spiedInstance);
 settings.setExtraInterfaces(new Class[] {Integer.class});

 mockUtil.create(classToMock, settings);
 fail("Expected NotAMockException but got none");
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockWithNullSpiedInstance() {
 Class<String> classToMock = String.class;
 MockSettingsImpl settings = new MockSettingsImpl();

 mockUtil.create(classToMock, settings);
 fail("Expected NotAMockException but got none");
 }

 @Test
 public void testResetMock() {
 String mock = Mockito.mock(String.class);
 mockUtil.resetMock(mock);
 assertNull(mockUtil.getInterceptor(mock));
 }

 @Test(expected = NotAMockException.class)
 public void testResetMockWithNullMock() {
 Object mock = null;
 mockUtil.resetMock(mock);
 fail("Expected NotAMockException but got none");
 }

 @Test(expected = NotAMockException.class)
 public void testResetMockWithNonMockitoMock() {
 Object mock = new Object();
 mockUtil.resetMock(mock);
 fail("Expected NotAMockException but got none");
 }

 @Test
 public void testIsMock() {
 String mock = Mockito.mock(String.class);
 assertTrue(mockUtil.isMock(mock));
 }

 @Test
 public void testIsMockWithNullMock() {
 Object mock = null;
 assertFalse(mockUtil.isMock(mock));
 }

 @Test
 public void testIsMockWithNonMockitoMock() {
 Object mock = new Object();
 assertFalse(mockUtil.isMock(mock));
 }

 @Test
 public void testGetInterceptorWithMockitoMock() {
 String mock = Mockito.mock(String.class);
 MethodInterceptor interceptor = mockUtil.getInterceptor(mock);
 assertNotNull(interceptor);
 assertTrue(interceptor instanceof MethodInterceptorFilter);
 }

 @Test(expected = NotAMockException.class)
 public void testGetInterceptorWithNonMockitoMock() {
 Object mock = new Object();
 mockUtil.getInterceptor(mock);
 fail("Expected NotAMockException but got none");
 }

 @Test
 public void testGetMockNameWithMockitoMock() {
 String mock = Mockito.mock(String.class);
 String mockName = mockUtil.getMockName(mock);
 assertNotNull(mockName);
 assertEquals("MockitoMock_" + hashCode(mock), mockName);
 }

 @Test(expected = NotAMockException.class)
 public void testGetMockNameWithNonMockitoMock() {
 Object mock = new Object();
 mockUtil.getMockName(mock);
 fail("Expected NotAMockException but got none");
 }

 private int hashCode(Object object) {
 return object != null ? object.hashCode() : 0;
}
}