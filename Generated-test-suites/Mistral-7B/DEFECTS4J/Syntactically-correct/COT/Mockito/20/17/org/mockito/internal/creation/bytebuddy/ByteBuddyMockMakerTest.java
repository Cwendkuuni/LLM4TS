import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockHandler;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.creation.instance.ClassInstantiator;
import org.mockito.internal.creation.instance.CachingMockBytecodeGenerator;
import org.mockito.internal.creation.bytebuddy.ByteBuddyMockMaker;
import org.mockito.internal.creation.bytebuddy.MockMethodInterceptor;
import org.mockito.internal.mock.MockCreationSettings;
import org.mockito.plugins.MockMaker;

public class ByteBuddyMockMakerTest {

 private ByteBuddyMockMaker byteBuddyMockMaker;
 private MockHandler mockHandler;

 @Before
 public void setup() {
 byteBuddyMockMaker = new ByteBuddyMockMaker();
 mockHandler = Mockito.mock(MockHandler.class);
 }

 @Test
 public void testCreateMock() {
 MockCreationSettings settings = new MockCreationSettings<Object>()
 .forClass(String.class)
 .withSerializableMode(SerializableMode.SERIAL)
 .withMockHandler(mockHandler);
 try {
 Object mock = byteBuddyMockMaker.createMock(settings, mockHandler);
 Assert.assertNotNull(mock);
 } catch (MockitoException e) {
 Assert.fail("Unexpected exception: " + e.getMessage());
 }
 }

 @Test(expected = MockitoException.class)
 public void testCreateMockWithSerializationAcrossClassLoaders() {
 MockCreationSettings settings = new MockCreationSettings<Object>()
 .forClass(String.class)
 .withSerializableMode(SerializableMode.ACROSS_CLASSLOADERS)
 .withMockHandler(mockHandler);
 byteBuddyMockMaker.createMock(settings, mockHandler);
 }

 @Test(expected = ClassCastException.class)
 public void testCreateMockWithClassCastException() {
 Class<Integer> integerType = Integer.class;
 Class<String> stringType = String.class;
 MockCreationSettings settings = new MockCreationSettings<Object>()
 .forClass(integerType)
 .withMockHandler(mockHandler);
 try {
 Object mock = byteBuddyMockMaker.createMock(settings, mockHandler);
 Assert.fail("Unexpected mock instance of type: " + describeClass(mock));
 } catch (ClassCastException e) {
 // expected
 Assert.assertEquals(stringType, settings.getTypeToMock());
 }
 }

 @Test(expected = InstantiationException.class)
 public void testCreateMockWithInstantiationException() {
 Class<String> stringType = String.class;
 MockCreationSettings settings = new MockCreationSettings<Object>()
 .forClass(stringType)
 .withMockHandler(mockHandler);
 byteBuddyMockMaker.createMock(settings, mockHandler);
 }

 @Test
 public void testGetHandler() {
 Object mock = byteBuddyMockMaker.createMock(new MockCreationSettings<Object>().forClass(String.class).withMockHandler(mockHandler), mockHandler);
 MockHandler handler = byteBuddyMockMaker.getHandler(mock);
 Assert.assertSame(mockHandler, handler);
 }

 @Test
 public void testResetMock() {
 Object mock = byteBuddyMockMaker.createMock(new MockCreationSettings<Object>().forClass(String.class).withMockHandler(mockHandler), mockHandler);
 MockHandler newHandler = Mockito.mock(MockHandler.class);
 byteBuddyMockMaker.resetMock(mock, newHandler, new MockCreationSettings<Object>().forClass(String.class).withMockHandler(newHandler));
 MockHandler resetHandler = byteBuddyMockMaker.getHandler(mock);
 Assert.assertSame(newHandler, resetHandler);
 }

 private String describeClass(Object instance) {
 return byteBuddyMockMaker.describeClass(instance);
 }
}