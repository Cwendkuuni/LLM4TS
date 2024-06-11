import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.MockHandler;
import org.mockito.MockitoAnnotation;
import org.mockito.plugins.MockMaker;
import org.mockito.internal.creation.bytebuddy.ByteBuddyMockMaker;
import org.mockito.internal.creation.instance.ClassInstantiator;
import org.mockito.internal.util.StringJoiner;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.plugins.MockMaker.MockMakerProvider;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

@RunWith(MockMakerJUnitRunner.class)
public class ByteBuddyMockMakerTest {

 private ByteBuddyMockMaker byteBuddyMockMaker;

 @Before
 public void setup() {
 byteBuddyMockMaker = new ByteBuddyMockMaker();
 }

 @Test
 public void testCreateMock() {
 Class<MyClass> typeToMock = MyClass.class;
 MockHandler handler = mock(MockHandler.class);
 MyClass mockedInstance = byteBuddyMockMaker.createMock(MockCreationSettings.forClass(typeToMock), handler);
 assertEquals(typeToMock, mockedInstance.getClass());
 verify(handler).mockingDetails(eq(typeToMock), any());
 }

 @Test(expected = MockitoException.class)
 public void testCreateMockWithSerializableMode() {
 Class<MyClass> typeToMock = MyClass.class;
 MockHandler handler = mock(MockHandler.class);
 MockCreationSettings settings = MockCreationSettings.builder().serializableMode(SerializableMode.ACROSS_CLASSLOADERS).build();
 byteBuddyMockMaker.createMock(settings, handler);
 fail("Expected MockitoException with message: Serialization across classloaders not yet supported with ByteBuddyMockMaker");
 }

 @Test
 public void testGetHandler() {
 MyClass mock = mock(MyClass.class);
 MockHandler handler = byteBuddyMockMaker.getHandler(mock);
 assertNotNull(handler);
 }

 @Test
 public void testResetMock() {
 MyClass mock = mock(MyClass.class);
 MockHandler newHandler = mock(MockHandler.class);
 byteBuddyMockMaker.resetMock(mock, newHandler, new MockCreationSettings.MockCreationSettingsBuilder().build());
 assertEquals(newHandler, byteBuddyMockMaker.getHandler(mock));
 }

 @Test(expected = MockitoException.class)
 public void testCreateMockWithClassCastException() {
 Class<MyClass> mockedProxyType = Mockito.mock(Class.class);
 Class<MyClass> typeToMock = MyClass.class;
 MockHandler handler = mock(MockHandler.class);
 byteBuddyMockMaker.createMock(new MockCreationSettings<MyClass>() {{
 setTypeToMock(typeToMock);
 setExtraInterfaces(new Class[] { mockedProxyType });
 }}, handler);
 fail("Expected ClassCastException with a specific message");
 }

 @Test(expected = MockitoException.class)
 public void testCreateMockWithInstantiationException() {
 Class<MyClass> mockedProxyType = Mockito.mock(Class.class);
 Class<MyClass> typeToMock = MyClass.class;
 MockHandler handler = mock(MockHandler.class);
 byteBuddyMockMaker = new ByteBuddyMockMaker();
 byteBuddyMockMaker.createMock(new MockCreationSettings<MyClass>() {{
 setTypeToMock(typeToMock);
 }}, handler);
 fail("Expected MockitoException with a specific message");
 }

 @Test
 public void testDescribeClass() {
 Class<MyClass> typeToMock = MyClass.class;
 assertEquals("'MyClass', loaded by classloader : 'org.mockito.internal.creation.bytebuddy.ByteBuddyMockMaker$$EnhancerByMockitoWithCGLIB$$1'", describeClass(typeToMock));
 }

 @Test
 public void testDescribeClassForNull() {
 assertNull(describeClass(null));
 }

 @Test
 public void testDescribeClassForObject() {
 Object instance = new Object();
 assertEquals("'class java.lang.Object', loaded by classloader : 'sun.misc.Launcher$AppClassLoader@18b4aac2'", describeClass(instance));
 }

 private static String describeClass(Object instance) {
 return byteBuddyMockMaker.describeClass(instance);
 }
}