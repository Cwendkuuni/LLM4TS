import org.junit.Test;
import org.junit.Assert;
import org.mockito.Mockito;
import org.mockito.internal.creation.bytebuddy.ByteBuddyMockMaker;
import org.mockito.internal.creation.bytebuddy.CachingMockBytecodeGenerator;
import org.mockito.internal.creation.bytebuddy.ClassInstantiator;
import org.mockito.internal.creation.instance.InstantiationException;
import org.mockito.invocation.MockHandler;
import org.mockito.mock.MockCreationSettings;
import org.mockito.plugins.MockMaker;

public class ByteBuddyMockMakerTest {

 private ByteBuddyMockMaker byteBuddyMockMaker;
 private CachingMockBytecodeGenerator cachingMockBytecodeGenerator;
 private ClassInstantiator classInstantiator;

 @SuppressWarnings("unchecked")
 @Before
 public void setup() {
 byteBuddyMockMaker = new ByteBuddyMockMaker();
 cachingMockBytecodeGenerator = byteBuddyMockMaker.new CachingMockBytecodeGenerator();
 classInstantiator = byteBuddyMockMaker.new ClassInstantiator();
 }

 @Test
 public void testCreateMock() {
 Class<String> stringClass = String.class;
 MockHandler mockHandler = Mockito.mock(MockHandler.class);
 MockCreationSettings<String> settings = new MockCreationSettings.Builder().withType(String.class).build();
 String mock = byteBuddyMockMaker.createMock(settings, mockHandler);
 Assert.assertNotNull(mock);
 }

 @Test(expected = MockitoException.class)
 public void testCreateMockWithSerializationAcrossClassloaders() {
 Class<String> stringClass = String.class;
 MockHandler mockHandler = Mockito.mock(MockHandler.class);
 MockCreationSettings<String> settings = new MockCreationSettings.Builder()
 .withType(String.class)
 .withSerializableMode(SerializableMode.ACROSS_CLASSLOADERS)
 .build();
 byteBuddyMockMaker.createMock(settings, mockHandler);
 }

 @Test
 public void testEnsureMockIsAssignableToMockedType() {
 Class<String> stringClass = String.class;
 MockHandler mockHandler = Mockito.mock(MockHandler.class);
 MockCreationSettings<String> settings = new MockCreationSettings.Builder().withType(String.class).build();
 Object mock = byteBuddyMockMaker.createMock(settings, mockHandler);
 Assert.assertEquals(String.class, mock.getClass());
 }

 @Test(expected = ClassCastException.class)
 public void testCreateMockWithInvalidInstanceType() {
 Class<String> stringClass = String.class;
 MockHandler mockHandler = Mockito.mock(MockHandler.class);
 MockCreationSettings<Object> settings = new MockCreationSettings.Builder().withType(Object.class).build();
 Object mock = byteBuddyMockMaker.createMock(settings, mockHandler);
 Assert.assertNotNull(mock);
 Assert.assertFalse(String.class.isAssignableFrom(mock.getClass()));
 }

 @Test(expected = InstantiationException.class)
 public void testCreateMockWithInstantiationException() {
 Class<String> stringClass = String.class;
 MockHandler mockHandler = Mockito.mock(MockHandler.class);
 MockCreationSettings<String> settings = new MockCreationSettings.Builder().withType(String.class).build();
 byteBuddyMockMaker = new ByteBuddyMockMaker();
 byteBuddyMockMaker.createMock(settings, mockHandler);
 }

 @Test
 public void testGetHandler() {
 Object mock = Mockito.mock(Object.class);
 Assert.assertNotNull(byteBuddyMockMaker.getHandler(mock));
 }

 @Test
 public void testResetMock() {
 Object mock = Mockito.mock(Object.class);
 MockHandler newHandler = Mockito.mock(MockHandler.class);
 byteBuddyMockMaker.resetMock(mock, newHandler, new MockCreationSettings.Builder().build());
 }

 @Test
 public void testDescribeClass() {
 Class<Object> objectClass = Object.class;
 Assert.assertEquals("class 'java.lang.Object', loaded by classloader : 'sun.misc.Launcher$AppClassLoader@18b4aac2'", byteBuddyMockMaker.describeClass(objectClass));
 }

 @Test
 public void testDescribeClassNull() {
 Assert.assertEquals("null", byteBuddyMockMaker.describeClass(null));
 }
}