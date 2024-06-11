import org.junit.Assert.*;
import org.junit.Test;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.CreationValidator;
import org.mockito.internal.creation.MethodInterceptor;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.internal.util.reflection.LenientCopyTool;
import org.mockito.Mockito;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.util.MockHandler;
import org.mockito.internal.util.MockHandlerInterface;
import org.mockito.internal.creation.Factory;

public class MockUtilTest {

 private CreationValidator creationValidator;
 private MockUtil mockUtil;

 @SuppressWarnings("unchecked")
 @Before
 public void setup() {
 creationValidator = new CreationValidator();
 mockUtil = new MockUtil(creationValidator);
 }

 @Test
 public void testCreateMockNullCreationValidator() {
 assertThrows(IllegalArgumentException.class, () -> new MockUtil(null));
 }

 @Test
 public void testCreateMockNullClassToMock() {
 assertThrows(NullPointerException.class, () -> mockUtil.createMock(null, new MockSettingsImpl()));
 }

 @Test
 public void testCreateMockNullSettings() {
 assertThrows(NullPointerException.class, () -> mockUtil.createMock(Mockito.mock(String.class), null));
 }

 @Test
 public void testCreateMock() throws Exception {
 Object mock = mockUtil.createMock(String.class, new MockSettingsImpl());
 assertTrue(Mockito.isMock(mock));
 }

 @Test
 public void testCreateMockWithExtraInterfaces() throws Exception {
 Class<MyClass> clazz = Mockito.mock(MyClass.class);
 MockSettingsImpl settings = new MockSettingsImpl();
 settings.setExtraInterfaces(new Class[] {MyExtraInterface.class});
 Object mock = mockUtil.createMock(clazz, settings);
 Mockito.verify(mock).myMethod(); // MyExtraInterface method
 }

 @Test
 public void testCreateMockWithSpiedInstance() throws Exception {
 Object spiedInstance = new Object();
 Object mock = mockUtil.createMock(String.class, new MockSettingsImpl().spiedInstance(spiedInstance));
 assertEquals(spiedInstance, mockUtil.getMockHandler(mock).getMockSettings().getSpiedInstance());
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockInvalidType() throws Exception {
 mockUtil.createMock(int.class, new MockSettingsImpl());
 }

 @Test(expected = NotAMockException.class)
 public void testCreateMockInvalidTypeWithInterfaces() throws Exception {
 Class<MyClass> clazz = Mockito.mock(MyClass.class);
 mockUtil.createMock(clazz.getClass(), new MockSettingsImpl().extraInterfaces(new Class[]{MyExtraInterface.class}));
 }

 @Test
 public void testResetMockNullMock() {
 assertThrows(NullPointerException.class, () -> mockUtil.resetMock(null));
 }

 @Test
 public void testResetMock() throws Exception {
 Object mock = mockUtil.createMock(String.class, new MockSettingsImpl());
 mockUtil.resetMock(mock);
 assertNull(mockUtil.getMockHandler(mock).getMockSettings().getMockName());
 }

 @Test(expected = NotAMockException.class)
 public void testGetMockHandlerNullMock() {
 mockUtil.getMockHandler(null);
 }

 @Test(expected = NotAMockException.class)
 public void testGetMockHandlerNonMockitoMock() {
 Object nonMockitoMock = new Object();
 mockUtil.getMockHandler(nonMockitoMock);
 }

 @Test
 public void testIsMockNullObject() {
 assertFalse(mockUtil.isMock(null));
 }

 @Test
 public void testIsMockTrueMockitoMock() throws Exception {
 Object mock = mockUtil.createMock(String.class, new MockSettingsImpl());
 assertTrue(mockUtil.isMock(mock));
 }

 @Test
 public void testIsMockFalseNonMockitoMock() {
 assertFalse(mockUtil.isMock(new Object()));
 }

 @Test
 public void testGetInterceptorNullMock() {
 assertNull(mockUtil.getInterceptor(null));
 }

 @Test
 public void testGetInterceptorMockitoMock() throws Exception {
 Object mock = mockUtil.createMock(String.class, new MockSettingsImpl());
 assertNotNull(mockUtil.getInterceptor(mock));
 }

 @Test
 public void testGetMockNameNullMock() {
 assertNull(mockUtil.getMockName(null));
 }

 @Test
 public void testGetMockNameMockitoMock() throws Exception {
 Object mock = mockUtil.createMock(String.class, new MockSettingsImpl());
 assertNotNull(mockUtil.getMockName(mock));
 }
}