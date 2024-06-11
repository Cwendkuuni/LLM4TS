import org.junit.Assert;
import org.junit.Test;
import org.mockito.MockHandler;
import org.mockito.Mockito;
import org.mockito.internal.creation.bytebuddy.ByteBuddyMockMaker;
import org.mockito.internal.creation.bytebuddy.CachingMockBytecodeGenerator;
import org.mockito.internal.creation.bytebuddy.ClassInstantiator;
import org.mockito.internal.creation.bytebuddy.GlobalConfiguration;
import org.mockito.internal.creation.bytebuddy.MockMethodInterceptor;
import org.mockito.internal.configuration.MockCreationSettings;
import org.mockito.plugins.MockMaker;

public class ByteBuddyMockMakerTest {

 private ByteBuddyMockMaker byteBuddyMockMaker;

 @Before
 public void setup() {
 byteBuddyMockMaker = new ByteBuddyMockMaker();
 CachingMockBytecodeGenerator cachingMockBytecodeGenerator = byteBuddyMockMaker.getCachingMockBytecodeGenerator();
 cachingMockBytecodeGenerator.addMockCreator(new MyMockCreator());
 }

 @Test
 public void testCreateMock() {
 MockHandler mockHandler = Mockito.mock(MockHandler.class);
 Class<Foo> fooClass = Foo.class;
 MockCreationSettings settings = new MockCreationSettings<Foo>(fooClass);
 Foo fooMock = byteBuddyMockMaker.createMock(settings, mockHandler);
 Assert.assertNotNull(fooMock);
 }

 @Test(expected = MockitoException.class)
 public void testCreateMockWithSerializationAcrossClassLoaders() {
 MockHandler mockHandler = Mockito.mock(MockHandler.class);
 Class<Foo> fooClass = Foo.class;
 MockCreationSettings settings = new MockCreationSettings<Foo>(fooClass).serializable(SerializableMode.ACROSS_CLASSLOADERS);
 byteBuddyMockMaker.createMock(settings, mockHandler);
 }

 @Test
 public void testGetHandler() {
 MockHandler mockHandler = Mockito.mock(MockHandler.class);
 Foo fooMock = byteBuddyMockMaker.createMock(MockCreationSettings.of(Foo.class), mockHandler);
 Assert.assertNotNull(byteBuddyMockMaker.getHandler(fooMock));
 }

 @Test
 public void testResetMock() {
 MockHandler mockHandler = Mockito.mock(MockHandler.class);
 Foo fooMock = byteBuddyMockMaker.createMock(MockCreationSettings.of(Foo.class), mockHandler);
 MockHandler newMockHandler = Mockito.mock(MockHandler.class);
 byteBuddyMockMaker.resetMock(fooMock, newMockHandler, new MockCreationSettings<Foo>(Foo.class));
 Assert.assertEquals(newMockHandler, byteBuddyMockMaker.getHandler(fooMock));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testResetMockWithNullMock() {
 Foo fooMock = null;
 byteBuddyMockMaker.resetMock(fooMock, Mockito.mock(MockHandler.class), new MockCreationSettings<Foo>(Foo.class));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testResetMockWithNullMockHandler() {
 Foo fooMock = byteBuddyMockMaker.createMock(MockCreationSettings.of(Foo.class), Mockito.mock(MockHandler.class));
 byteBuddyMockMaker.resetMock(fooMock, null, new MockCreationSettings<Foo>(Foo.class));
 }

 @Test(expected = MockitoException.class)
 public void testCreateMockWithClassCastException() {
 Class<Bar> barClass = Bar.class;
 MockHandler mockHandler = Mockito.mock(MockHandler.class);
 MockCreationSettings settings = new MockCreationSettings<Bar>(barClass);
 byteBuddyMockMaker.createMock(settings, mockHandler);
 }

 @Test(expected = InstantiationException.class)
 public void testCreateMockWithInstantiationException() {
 Class<Baz> bazClass = Baz.class;
 MockHandler mockHandler = Mockito.mock(MockHandler.class);
 MockCreationSettings settings = new MockCreationSettings<Baz>(bazClass);
 byteBuddyMockMaker.createMock(settings, mockHandler);
 }

 @Test
 public void testDescribeClass() {
 Class<Foo> fooClass = Foo.class;
 Assert.assertEquals("class org.mockitotesting.Foo, loaded by sun.misc.Launcher$AppClassLoader@18b4aac2", byteBuddyMockMaker.describeClass(fooClass));
 }

 @Test
 public void testDescribeClassWithNull() {
 Assert.assertEquals("null", byteBuddyMockMaker.describeClass(null));
 }

 @Test
 public void testInitializeClassInstantiator() {
 Class<?> objenesisClassLoader = null;
 try {
 objenesisClassLoader = Class.forName("org.mockito.internal.creation.bytebuddy.ClassInstantiator$UsingObjenesis");
 } catch (ClassNotFoundException e) {
 // Expected
 }
 Assert.assertNotNull(byteBuddyMockMaker.initializeClassInstantiator());
 }

 @Test(expected = IllegalStateException.class)
 public void testInitializeClassInstantiatorWithObjenesisMissing() {
 byteBuddyMockMaker.initializeClassInstantiator();
 }

 @Test
 public void testAsInternalMockHandler() {
 MockHandler mockHandler = Mockito.mock(MockHandler.class);
 InternalMockHandler internalMockHandler = byteBuddyMockMaker.asInternalMockHandler(mockHandler);
 Assert.assertNotNull(internalMockHandler);
 }

 @Test(expected = MockitoException.class)
 public void testAsInternalMockHandlerWithNullMockHandler() {
 byteBuddyMockMaker.asInternalMockHandler(null);
 }
}

class MyMockCreator implements ClassMockCreator<Object> {
 @Override
 public <T> Class<T> createMockType(Class<T> type) {
 return type;
 }

 @Override
 public T createMockInstance(Class<T> type) {
 return null;
 }
}