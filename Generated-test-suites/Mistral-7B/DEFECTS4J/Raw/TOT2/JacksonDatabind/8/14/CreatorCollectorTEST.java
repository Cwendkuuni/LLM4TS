import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonCreator.Mode;
import com.fasterxml.jackson.databind.annotation.JsonProperty;
import com.fasterxml.jackson.databind.deser.CreatorCollector;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;

public class CreatorCollectorTest {

 @Test
 public void testDefaultCreator() throws Exception {
 CreatorCollector collector = new CreatorCollector(
 new AnnotatedClass(Collection.class), false);
 DeserializationConfig config = new DeserializationConfig();
 ValueInstantiator instantiator = collector.constructValueInstantiator(config);
 ObjectMapper mapper = new ObjectMapper();
 mapper.setConfiguration(config);
 JavaType collectionType = mapper.getTypeFactory().constructType(Collection.class);
 Object collection = instantiator.createValueInstance(collectionType);
 assertTrue(collection instanceof Collection);
 }

 @Test
 public void testStringCreator() throws Exception {
 CreatorCollector collector = new CreatorCollector(
 new AnnotatedClass(StringList.class), false);
 AnnotatedMethod constructor = new AnnotatedMethod(StringList.class.getDeclaredConstructor(String.class), null, null);
 collector.addStringCreator(AnnotatedWithParams.fromMethod(constructor));
 DeserializationConfig config = new DeserializationConfig();
 ValueInstantiator instantiator = collector.constructValueInstantiator(config);
 ObjectMapper mapper = new ObjectMapper();
 mapper.setConfiguration(config);
 JavaType collectionType = mapper.getTypeFactory().constructType(Collection.class);
 Object collection = instantiator.createValueInstance(collectionType, "TestList");
 assertTrue(collection instanceof StringList);
 }

 @Test
 public void testIntCreator() throws Exception {
 CreatorCollector collector = new CreatorCollector(
 new AnnotatedClass(IntArray.class), false);
 AnnotatedMethod constructor = new AnnotatedMethod(IntArray.class.getDeclaredConstructor(int.class), null, null);
 collector.addIntCreator(AnnotatedWithParams.fromMethod(constructor));
 DeserializationConfig config = new DeserializationConfig();
 ValueInstantiator instantiator = collector.constructValueInstantiator(config);
 ObjectMapper mapper = new ObjectMapper();
 mapper.setConfiguration(config);
 JavaType arrayType = mapper.getTypeFactory().constructType(int[].class);
 Object array = instantiator.createValueInstance(arrayType, 10);
 assertTrue(array instanceof int[]);
 assertEquals(10, array.length);
 }

 @Test
 public void testLongCreator() throws Exception {
 CreatorCollector collector = new CreatorCollector(
 new AnnotatedClass(LongArray.class), false);
 AnnotatedMethod constructor = new AnnotatedMethod(LongArray.class.getDeclaredConstructor(long.class), null, null);
 collector.addLongCreator(AnnotatedWithParams.fromMethod(constructor));
 DeserializationConfig config = new DeserializationConfig();
 ValueInstantiator instantiator = collector.constructValueInstantiator(config);
 ObjectMapper mapper = new ObjectMapper();
 mapper.setConfiguration(config);
 JavaType arrayType = mapper.getTypeFactory().constructType(long[].class);
 Object array = instantiator.createValueInstance(arrayType, 20L);
 assertTrue(array instanceof long[]);
 assertEquals(20, array.length);
 }

 @Test
 public void testDoubleCreator() throws Exception {
 CreatorCollector collector = new CreatorCollector(
 new AnnotatedClass(DoubleArray.class), false);
 AnnotatedMethod constructor = new AnnotatedMethod(DoubleArray.class.getDeclaredConstructor(double.class), null, null);
 collector.addDoubleCreator(AnnotatedWithParams.fromMethod(constructor));
 DeserializationConfig config = new DeserializationConfig();
 ValueInstantiator instantiator = collector.constructValueInstantiator(config);
 ObjectMapper mapper = new ObjectMapper();
 mapper.setConfiguration(config);
 JavaType arrayType = mapper.getTypeFactory().constructType(double[].class);
 Object array = instantiator.createValueInstance(arrayType, 3.1415);
 assertTrue(array instanceof double[]);
 assertEquals(3, array.length);
 }

 @Test
 public void testBooleanCreator() throws Exception {
 CreatorCollector collector = new CreatorCollector(
 new AnnotatedClass(BooleanArray.class), false);
 AnnotatedMethod constructor = new AnnotatedMethod(BooleanArray.class.getDeclaredConstructor(boolean.class), null, null);
 collector.addBooleanCreator(AnnotatedWithParams.fromMethod(constructor));
 DeserializationConfig config = new DeserializationConfig();
 ValueInstantiator instantiator = collector.constructValueInstantiator(config);
 ObjectMapper mapper = new ObjectMapper();
 mapper.setConfiguration(config);
 JavaType arrayType = mapper.getTypeFactory().constructType(boolean[].class);
 Object array = instantiator.createValueInstance(arrayType, true);
 assertTrue(array instanceof boolean[]);
 assertEquals(1, array.length);
 assertTrue((boolean) array[0]);
 }

 @Test
 public void testDelegateCreator() throws Exception {
 CreatorCollector collector = new CreatorCollector(
 new AnnotatedClass(DelegateArray.class), false);
 AnnotatedMethod constructor = new AnnotatedMethod(DelegateArray.class.getDeclaredConstructor(Delegate.class), null, null);
 AnnotatedMethod delegateConstructor = new AnnotatedMethod(Delegate.class.getDeclaredConstructor(), null, null);
 CreatorProperty[] injectables = new CreatorProperty[] {
 new CreatorProperty(delegateConstructor, null, null, null)
 };
 collector.addDelegatingCreator(AnnotatedWithParams.fromMethod(constructor), true, injectables);
 DeserializationConfig config = new DeserializationConfig();
 ValueInstantiator instantiator = collector.constructValueInstantiator(config);
 ObjectMapper mapper = new ObjectMapper();
 mapper.setConfiguration(config);
 JavaType arrayType = mapper.getTypeFactory().constructType(DelegateArray.class);
 Object array = instantiator.createValueInstance(arrayType, new Delegate());
 assertTrue(array instanceof DelegateArray);
 }

 @Test
 public void testPropertyBasedCreator() throws Exception {
 CreatorCollector collector = new CreatorCollector(
 new AnnotatedClass(PropertyBasedArray.class), false);
 AnnotatedMethod constructor = new AnnotatedMethod(PropertyBasedArray.class.getDeclaredConstructor(Map.class), null, null);
 AnnotatedMethod mapConstructor = new AnnotatedMethod(Map.class.getDeclaredConstructor(Class.class, int.class), null, null);
 CreatorProperty[] properties = new CreatorProperty[] {
 new CreatorProperty(mapConstructor, String.class, "key", null),
 new CreatorProperty(mapConstructor, Integer.class, "value", null)
 };
 collector.addPropertyCreator(AnnotatedWithParams.fromMethod(constructor), true, properties);
 DeserializationConfig config = new DeserializationConfig();
 ValueInstantiator instantiator = collector.constructValueInstantiator(config);
 ObjectMapper mapper = new ObjectMapper();
 mapper.setConfiguration(config);
 mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 ObjectMapper.DefaultTyping defaultTyping = mapper.getDefaultTyping();
 JavaType arrayType = mapper.getTypeFactory().constructType(PropertyBasedArray.class, Map.class);
 Object array = instantiator.createValueInstance(arrayType, new HashMap<>());
 assertTrue(array instanceof PropertyBasedArray);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDuplicateCreator() throws Exception {
 CreatorCollector collector = new CreatorCollector(
 new AnnotatedClass(DuplicateArray.class), false);
 AnnotatedMethod constructor = new AnnotatedMethod(DuplicateArray.class.getDeclaredConstructor(), null, null);
 collector.addIntCreator(AnnotatedWithParams.fromMethod(constructor));
 collector.addIntCreator(AnnotatedWithParams.fromMethod(constructor));
 DeserializationConfig config = new DeserializationConfig();
 ValueInstantiator instantiator = collector.constructValueInstantiator(config);
 ObjectMapper mapper = new ObjectMapper();
 mapper.setConfiguration(config);
 JavaType arrayType = mapper.getTypeFactory().constructType(int[].class);
 try {
 instantiator.createValueInstance(arrayType, null);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 assertEquals("Conflicting int creators: already had explicitly marked "+constructor+" encountered "+constructor, e.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDuplicatePropertyBasedCreator() throws Exception {
 CreatorCollector collector = new CreatorCollector(
 new AnnotatedClass(DuplicateProperties.class), false);
 AnnotatedMethod constructor = new AnnotatedMethod(DuplicateProperties.class.getDeclaredConstructor(Map.class), null, null);
 AnnotatedMethod mapConstructor = new AnnotatedMethod(Map.class.getDeclaredConstructor(Class.class, int.class), null, null);
 CreatorProperty[] properties = new CreatorProperty[] {
 new CreatorProperty(mapConstructor, String.class, "key", null),
 new CreatorProperty(mapConstructor, Integer.class, "key", null)
 };
 collector.addPropertyCreator(AnnotatedWithParams.fromMethod(constructor), true, properties);
 DeserializationConfig config = new DeserializationConfig();
 ValueInstantiator instantiator = collector.constructValueInstantiator(config);
 ObjectMapper mapper = new ObjectMapper();
 mapper.setConfiguration(config);
 mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 ObjectMapper.DefaultTyping defaultTyping = mapper.getDefaultTyping();
 JavaType arrayType = mapper.getTypeFactory().constructType(DuplicateProperties.class, Map.class);
 try {
 instantiator.createValueInstance(arrayType, new HashMap<>());
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 assertEquals("Duplicate creator property \"key\"", e.getMessage());
 }
 }

 @Test
 public void testCanFixAccess() throws Exception {
 CreatorCollector collector = new CreatorCollector(
 new AnnotatedClass(PrivateCreator.class), true);
 AnnotatedMethod constructor = new AnnotatedMethod(PrivateCreator.class.getDeclaredConstructor(), null, null);
 collector.addIntCreator(AnnotatedWithParams.fromMethod(constructor));
 DeserializationConfig config = new DeserializationConfig();
 ValueInstantiator instantiator = collector.constructValueInstantiator(config);
 ObjectMapper mapper = new ObjectMapper();
 mapper.setConfiguration(config);
 JavaType arrayType = mapper.getTypeFactory().constructType(int[].class);
 Object array = instantiator.createValueInstance(arrayType, null);
 assertTrue(array instanceof int[]);
 }

 @Test(expected = IllegalAccessException.class)
 public void testCannotFixAccess() throws Exception {
 CreatorCollector collector = new CreatorCollector(
 new AnnotatedClass(PrivateCreator.class), false);
 AnnotatedMethod constructor = new AnnotatedMethod(PrivateCreator.class.getDeclaredConstructor(), null, null);
 collector.addIntCreator(AnnotatedWithParams.fromMethod(constructor));
 DeserializationConfig config = new DeserializationConfig();
 ValueInstantiator instantiator = collector.constructValueInstantiator(config);
 ObjectMapper mapper = new ObjectMapper();
 mapper.setConfiguration(config);
 JavaType arrayType = mapper.getTypeFactory().constructType(int[].class);
 try {
 instantiator.createValueInstance(arrayType, null);
 fail("Expected IllegalAccessException");
 } catch (IllegalAccessException e) {
 // expected
 }
 }
}