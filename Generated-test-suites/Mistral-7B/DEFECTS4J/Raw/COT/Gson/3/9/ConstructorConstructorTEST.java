import org.junit.Assert;
import org.junit.Test;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import com.google.gson.InstanceCreator;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.UnsafeAllocator;

public class ConstructorConstructorTest {

 private ConstructorConstructor constructorConstructor;

 @SuppressWarnings("unchecked")
 @Before
 public void setup() {
 Map<Type, InstanceCreator<?>> instanceCreators = new HashMap<>();
 constructorConstructor = new ConstructorConstructor(instanceCreators);
 }

 @Test
 public void testGet_TypeTokenWithInstanceCreator_ReturnsInstance() {
 TypeToken<String> typeToken = new TypeToken<String>() {};
 ObjectConstructor<String> objectConstructor = constructorConstructor.get(typeToken);
 String result = objectConstructor.construct();
 Assert.assertNotNull(result);
 Assert.assertEquals("", result);
 }

 @Test
 public void testGet_TypeTokenWithoutInstanceCreator_TriesRawType() {
 TypeToken<String> typeToken = new TypeToken<String>() {};
 ObjectConstructor<String> objectConstructor = constructorConstructor.get(typeToken);
 if (objectConstructor == null) {
 Type rawType = typeToken.getType().getComponentType();
 objectConstructor = constructorConstructor.get(new TypeToken<String>() {}.where(new TypeNameFilter() {
 @Override public boolean accept(Type type) {
 return Arrays.equals(type.getTypeName().split("\\."), rawType.getTypeName().split("\\."));
 }
 })).get(new TypeToken<String>() {});
 }
 String result = objectConstructor.construct();
 Assert.assertNotNull(result);
 Assert.assertEquals("", result);
 }

 @Test
 public void testGet_TypeTokenWithNoMatchingConstructor_ReturnsNull() {
 TypeToken<String> typeToken = new TypeToken<String>() {}.where(new TypeNameFilter() {
 @Override public boolean accept(Type type) {
 return type.getTypeName().equals("java.util.Date");
 }
 });
 ObjectConstructor<String> objectConstructor = constructorConstructor.get(typeToken);
 Assert.assertNull(objectConstructor);
 }

 @Test(expected = RuntimeException.class)
 public void testGet_TypeTokenWithThrowingConstructor_ThrowsException() {
 TypeToken<String> typeToken = new TypeToken<String>() {}.where(new TypeNameFilter() {
 @Override public boolean accept(Type type) {
 return type.getTypeName().equals("java.io.FileDescriptor");
 }
 });
 ObjectConstructor<String> objectConstructor = constructorConstructor.get(typeToken);
 objectConstructor.construct();
 }

 @Test(expected = RuntimeException.class)
 public void testGet_TypeTokenWithNoDefaultConstructor_ThrowsException() {
 Type type = Object[].class;
 ObjectConstructor<Object[]> objectConstructor = constructorConstructor.get(new TypeToken<Object[]>() {});
 Assert.assertNull(objectConstructor);
 }

 @Test
 public void testGet_TypeTokenWithCustomDefaultConstructor_ReturnsInstance() {
 TypeToken<LinkedTreeMap<String, Object>> typeToken = new TypeToken<LinkedTreeMap<String, Object>>() {};
 ObjectConstructor<LinkedTreeMap<String, Object>> objectConstructor = constructorConstructor.get(typeToken);
 LinkedTreeMap<String, Object> result = objectConstructor.construct();
 Assert.assertNotNull(result);
 Assert.assertTrue(result instanceof LinkedTreeMap);
 }

 @Test(expected = JsonIOException.class)
 public void testGet_TypeTokenWithInvalidType_ThrowsJsonIOException() {
 TypeToken<Object> typeToken = new TypeToken<Object>() {};
 ObjectConstructor<Object> objectConstructor = constructorConstructor.get(typeToken);
 objectConstructor.construct();
 }

 @Test(expected = IllegalStateException.class)
 @SuppressWarnings("tryNotFoundException")
 public void testGet_TypeTokenWithUnsafeAllocator_ThrowsIllegalStateException() {
 Type type = SunMicrosystemsInc.class.getTypeName().asSubType(Class.class);
 ObjectConstructor<SunMicrosystemsInc> objectConstructor = constructorConstructor.get(new TypeToken<SunMicrosystemsInc>() {});
 try {
 objectConstructor.construct();
 } catch (InvocationTargetException e) {
 throw new AssertionError("Unexpected exception: " + e.getCause().getMessage());
 } catch (UndeclaredThrowableException e) {
 throw e.getException();
 }
 }

 @Test
 public void testToString() {
 String result = constructorConstructor.toString();
 Assert.assertTrue(result.startsWith("com.google.gson.internal.ConstructorConstructor@"));
 }
}

 class TypeNameFilter {
 private final String typeName;

 TypeNameFilter(String typeName) {
 this.typeName = typeName;
 }

 @Override
 public boolean accept(Type type) {
 return type.getTypeName().equals(typeName);
 }
}

 class SunMicrosystemsInc {
}