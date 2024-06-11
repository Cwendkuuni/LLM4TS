import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.CreatorCollector;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import java.lang.reflect.ParameterizedType;
import java.util.*;
import static org.testng.Assert.*;

public class CreatorCollectorTest {

 private ObjectMapper objectMapper;
 private CreatorCollector creatorCollector;

 @BeforeMethod
 public void setUp() {
 objectMapper = new ObjectMapper();
 creatorCollector = new CreatorCollector(new AnnotatedClass(ArrayList.class), objectMapper.getDeserializationConfig());
 }

 @Test
 public void testDefaultCreator() throws Exception {
 // Test default creator (no-args constructor)
 Object list = creatorCollector.constructValueInstantiator(objectMapper.getDeserializationConfig()).createValueInstance(null);
 assertTrue(list instanceof ArrayList);
 }

 @Test
 public void testStringCreator() throws Exception {
 // Test String creator
 AnnotatedConstructor constructor = new AnnotatedClass(ArrayList.class).getConstructors()[0];
 creatorCollector.setStringCreator(new AnnotatedWithParams(constructor), true);
 Object list = creatorCollector.constructValueInstantiator(objectMapper.getDeserializationConfig()).createValueInstance(new Object());
 assertEquals(list.getClass(), ArrayList.class);
 assertEquals(list.toString(), "[]");

 // Test String creator with arguments
 constructor = new AnnotatedClass(ArrayList.class).getConstructors()[1];
 creatorCollector.setStringCreator(new AnnotatedWithParams(constructor, new AnnotatedParameter(constructor.getParameters()[0], Object.class)), true);
 list = creatorCollector.constructValueInstantiator(objectMapper.getDeserializationConfig()).createValueInstance(new Object[]{"one", "two", "three"});
 assertEquals(list.getClass(), ArrayList.class);
 assertEquals(list.toString(), "[one, two, three]");
 }

 @Test
 public void testIntCreator() throws Exception {
 // Test int creator
 AnnotatedConstructor constructor = new AnnotatedClass(ArrayList.class).getConstructors()[2];
 creatorCollector.setIntCreator(new AnnotatedWithParams(constructor), true);
 Object list = creatorCollector.constructValueInstantiator(objectMapper.getDeserializationConfig()).createValueInstance(new Object(), 3);
 assertEquals(list.getClass(), ArrayList.class);
 assertEquals(list.size(), 3);
 }

 @Test
 public void testLongCreator() throws Exception {
 // Test long creator
 AnnotatedConstructor constructor = new AnnotatedClass(ArrayList.class).getConstructors()[3];
 creatorCollector.setLongCreator(new AnnotatedWithParams(constructor), true);
 Object list = creatorCollector.constructValueInstantiator(objectMapper.getDeserializationConfig()).createValueInstance(new Object(), 5L);
 assertEquals(list.getClass(), ArrayList.class);
 assertEquals(list.size(), 5);
 }

 @Test
 public void testDoubleCreator() throws Exception {
 // Test double creator
 AnnotatedConstructor constructor = new AnnotatedClass(ArrayList.class).getConstructors()[4];
 creatorCollector.setDoubleCreator(new AnnotatedWithParams(constructor), true);
 Object list = creatorCollector.constructValueInstantiator(objectMapper.getDeserializationConfig()).createValueInstance(new Object(), 3.14);
 assertEquals(list.getClass(), ArrayList.class);
 assertEquals(list.size(), 1);
 assertEquals(list.get(0), 3.14);
 }

 @Test
 public void testBooleanCreator() throws Exception {
 // Test boolean creator
 AnnotatedConstructor constructor = new AnnotatedClass(ArrayList.class).getConstructors()[5];
 creatorCollector.setBooleanCreator(new AnnotatedWithParams(constructor), true);
 Object list = creatorCollector.constructValueInstantiator(objectMapper.getDeserializationConfig()).createValueInstance(new Object(), false);
 assertEquals(list.getClass(), ArrayList.class);
 assertEquals(list.isEmpty(), true);
 }

 @Test
 public void testDelegatingCreator() throws Exception {
 // Test delegating creator
 AnnotatedConstructor constructor = new AnnotatedClass(LinkedList.class).getConstructors()[0];
 SettableBeanProperty[] injectables = new SettableBeanProperty[1];
 injectables[0] = new SettableBeanProperty("delegate", Object.class);
 creatorCollector.addDelegatingCreator(new AnnotatedWithParams(constructor), true, injectables);
 Object list = creatorCollector.constructValueInstantiator(objectMapper.getDeserializationConfig()).createValueInstance(new Object());
 assertEquals(list.getClass(), LinkedList.class);
 assertNotNull(list);
 assertNotNull(list.get("delegate"));
 }

 @Test
 public void testPropertyBasedCreator() throws Exception {
 // Test property-based creator
 AnnotatedConstructor constructor = new AnnotatedClass(ArrayList.class).getConstructors()[0];
 SettableBeanProperty[] properties = new SettableBeanProperty[2];
 properties[0] = new SettableBeanProperty("size", Integer.class);
 properties[1] = new SettableBeanProperty("list", new TypeReference<List<Object>>(){}.getType());
 creatorCollector.addPropertyCreator(new AnnotatedWithParams(constructor), true, properties);
 Object list = creatorCollector.constructValueInstantiator(objectMapper.getDeserializationConfig()).createValueInstance(new Object(), 5, new ArrayList<Object>());
 assertEquals(list.getClass(), ArrayList.class);
 assertEquals(list.size(), 5);
 }

 @Test
 public void testCustomCreator() throws Exception {
 // Test custom creator
 AnnotatedConstructor constructor = new AnnotatedClass(CustomList.class).getConstructors()[0];
 creatorCollector.addStringCreator(new AnnotatedWithParams(constructor), false);
 Object list = creatorCollector.constructValueInstantiator(objectMapper.getDeserializationConfig()).createValueInstance(new Object());
 assertEquals(list.getClass(), CustomList.class);
 assertEquals(list.size(), 0);
 }

 @Test
 public void testInvalidCreator() throws Exception {
 // Test invalid creator
 AnnotatedConstructor constructor = new AnnotatedClass(CustomList.class).getConstructors()[1];
 creatorCollector.addStringCreator(new AnnotatedWithParams(constructor), false);
 try {
 Object list = creatorCollector.constructValueInstantiator(objectMapper.getDeserializationConfig()).createValueInstance(new Object());
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testMissingCreator() throws Exception {
 // Test missing creator
 ObjectMapper mapper = new ObjectMapper();
 CreatorCollector creatorCollector = new CreatorCollector(new AnnotatedClass(ArrayList.class), mapper.getDeserializationConfig());
 try {
 creatorCollector.constructValueInstantiator(mapper.getDeserializationConfig());
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }
}

class CustomList extends ArrayList<Object> {
 // Empty
}

class AnnotatedWithParams {
 private final AnnotatedMember member;
 private final Object[] arguments;

 public AnnotatedWithParams(AnnotatedMember member) {
 this(member, null);
 }

 public AnnotatedWithParams(AnnotatedMember member, Object[] arguments) {
 this.member = member;
 this.arguments = arguments;
 }

 public AnnotatedMember getAnnotated() {
 return member;
 }

 public Object[] getRawParameterTypes() {
 return member.getParameterTypes();
 }

 public AnnotatedParameter getRawParameter(int index) {
 return new AnnotatedParameter(member.getParameters()[index]);
 }

 public Class<?> getRawParameterType(int index) {
 return member.getParameterTypes()[index].getType();
 }
}

class AnnotatedParameter {
 private final AnnotatedMethod method;

 public AnnotatedParameter(AnnotatedMethod method) {
 this.method = method;
 }

 public String getName() {
 return method.getName();
 }

 public Type getType() {
 return method.getTypeParameters()[0].getType();
 }
}