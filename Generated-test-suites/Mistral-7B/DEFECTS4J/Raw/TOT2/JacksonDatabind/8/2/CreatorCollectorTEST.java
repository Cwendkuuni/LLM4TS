import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class CreatorCollectorTest {

 private static ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testCreatorCollectorDefaultConstructor() throws Exception {
 BeanDescription beanDesc = mapper.getFactory().findBeanDescription(String[].class, null);
 CreatorCollector collector = new CreatorCollector(beanDesc, true);
 ValueInstantiator instantiator = collector.constructValueInstantiator(mapper.getConfig());
 Object result = instantiator.createValue(mapper.getConfig(), null);
 assertEquals(ArrayList.class, result.getClass());
 }

 @Test
 public void testCreatorCollectorCustomConstructor() throws Exception {
 AnnotatedClass collectionClass = mapper.getFactory().findClassDescForClass(ArrayList.class);
 AnnotatedMember constructor = collectionClass.findMember("ArrayList");
 AnnotatedWithParams constructorAnnotated = AnnotationIntrospector.findCreatorFromConstructor(constructor, mapper.getConfig());
 BeanDescription beanDesc = mapper.getFactory().findBeanDescription(ArrayList.class, null);
 CreatorCollector collector = new CreatorCollector(beanDesc, true);
 collector.setDefaultCreator(constructorAnnotated);
 ValueInstantiator instantiator = collector.constructValueInstantiator(mapper.getConfig());
 Object result = instantiator.createValue(mapper.getConfig(), null);
 assertEquals(ArrayList.class, result.getClass());
 }

 @Test
 public void testCreatorCollectorStringCreator() throws Exception {
 BeanDescription beanDesc = mapper.getFactory().findBeanDescription(String.class, null);
 AnnotatedWithParams creator = mapper.getFactory().findValueMethodAnnotation(beanDesc, String.class, "valueOf");
 CreatorCollector collector = new CreatorCollector(beanDesc, true);
 collector.addStringCreator(creator, true);
 ValueInstantiator instantiator = collector.constructValueInstantiator(mapper.getConfig());
 Object result = instantiator.createValue(mapper.getConfig(), null);
 assertEquals("", result.toString());
 }

 @Test
 public void testCreatorCollectorIntCreator() throws Exception {
 BeanDescription beanDesc = mapper.getFactory().findBeanDescription(int[].class, null);
 AnnotatedWithParams creator = mapper.getFactory().findValueMethodAnnotation(beanDesc, int[].class, "valueOf");
 CreatorCollector collector = new CreatorCollector(beanDesc, true);
 collector.addIntCreator(creator, true);
 ValueInstantiator instantiator = collector.constructValueInstantiator(mapper.getConfig());
 Object result = instantiator.createValue(mapper.getConfig(), null);
 assertArrayEquals(new int[0], (int[]) result);
 }

 @Test
 public void testCreatorCollectorLongCreator() throws Exception {
 BeanDescription beanDesc = mapper.getFactory().findBeanDescription(long[].class, null);
 AnnotatedWithParams creator = mapper.getFactory().findValueMethodAnnotation(beanDesc, long[].class, "valueOf");
 CreatorCollector collector = new CreatorCollector(beanDesc, true);
 collector.addLongCreator(creator, true);
 ValueInstantiator instantiator = collector.constructValueInstantiator(mapper.getConfig());
 Object result = instantiator.createValue(mapper.getConfig(), null);
 assertArrayEquals(new long[0], (long[]) result);
 }

 @Test
 public void testCreatorCollectorDoubleCreator() throws Exception {
 BeanDescription beanDesc = mapper.getFactory().findBeanDescription(double[].class, null);
 AnnotatedWithParams creator = mapper.getFactory().findValueMethodAnnotation(beanDesc, double[].class, "valueOf");
 CreatorCollector collector = new CreatorCollector(beanDesc, true);
 collector.addDoubleCreator(creator, true);
 ValueInstantiator instantiator = collector.constructValueInstantiator(mapper.getConfig());
 Object result = instantiator.createValue(mapper.getConfig(), null);
 assertArrayEquals(new double[0], (double[]) result);
 }

 @Test
 public void testCreatorCollectorBooleanCreator() throws Exception {
 BeanDescription beanDesc = mapper.getFactory().findBeanDescription(boolean[].class, null);
 AnnotatedWithParams creator = mapper.getFactory().findValueMethodAnnotation(beanDesc, boolean[].class, "valueOf");
 CreatorCollector collector = new CreatorCollector(beanDesc, true);
 collector.addBooleanCreator(creator, true);
 ValueInstantiator instantiator = collector.constructValueInstantiator(mapper.getConfig());
 Object result = instantiator.createValue(mapper.getConfig(), null);
 assertArrayEquals(new boolean[0], (boolean[]) result);
 }

 @Test
 public void testCreatorCollectorDelegateCreator() throws Exception {
 BeanDescription beanDesc = mapper.getFactory().findBeanDescription(String.class, null);
 AnnotatedWithParams creator = mapper.getFactory().findMethodAnnotation(beanDesc, createStringFromAnotherStringMethod());
 CreatorCollector collector = new CreatorCollector(beanDesc, true);
 CreatorProperty[] injectables = new CreatorProperty[1];
 injectables[0] = mapper.findInjectableValue("anotherString", String.class);
 collector.addDelegatingCreator(creator, true, injectables);
 ValueInstantiator instantiator = collector.constructValueInstantiator(mapper.getConfig());
 Object result = instantiator.createValue(mapper.getConfig(), null);
 assertEquals("", result.toString());
 }

 @Test
 public void testCreatorCollectorPropertyBasedCreator() throws Exception {
 BeanDescription beanDesc = mapper.getFactory().findBeanDescription(Person.class, null);
 AnnotatedWithParams creator = mapper.getFactory().findMethodAnnotation(beanDesc, createPersonWithNameAndAgeMethod());
 CreatorProperty[] properties = new CreatorProperty[2];
 properties[0] = mapper.findInjectableValue("name", String.class);
 properties[1] = mapper.findInjectableValue("age", int.class);
 CreatorCollector collector = new CreatorCollector(beanDesc, true);
 collector.addPropertyCreator(creator, true, properties);
 ValueInstantiator instantiator = collector.constructValueInstantiator(mapper.getConfig());
 Object result = instantiator.createValue(mapper.getConfig(), null);
 Person person = (Person) result;
 assertEquals("John Doe", person.getName());
 assertEquals(30, person.getAge());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreatorCollectorDuplicateCreator() throws Exception {
 BeanDescription beanDesc = mapper.getFactory().findBeanDescription(String.class, null);
 AnnotatedWithParams creator = mapper.getFactory().findValueMethodAnnotation(beanDesc, String.class, "valueOf");
 CreatorCollector collector = new CreatorCollector(beanDesc, true);
 collector.addStringCreator(creator, true);
 collector.addStringCreator(creator, true);
 try {
 ValueInstantiator instantiator = collector.constructValueInstantiator(mapper.getConfig());
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testCreatorCollectorIncompleteParameter() throws Exception {
 BeanDescription beanDesc = mapper.getFactory().findBeanDescription(Person.class, null);
 AnnotatedParameter incompleteParameter = mapper.getFactory().findParameterAnnotation(beanDesc, "id");
 CreatorCollector collector = new CreatorCollector(beanDesc, true);
 collector.addIncompleteParameter(incompleteParameter);
 ValueInstantiator instantiator = collector.constructValueInstantiator(mapper.getConfig());
 assertNull(instantiator.createValue(mapper.getConfig(), null));
 }

 private static AnnotatedWithParams createStringFromAnotherStringMethod() {
 return mapper.getFactory().findMethodAnnotation(Object.class, "valueOf", String.class, new Class[0]);
 }

 private static AnnotatedWithParams createPersonWithNameAndAgeMethod() {
 JavaType personType = mapper.getTypeFactory().constructType(Person.class);
 TypeDeserializer idDeserializer = mapper.getFactory().findValueDeserializer(null, String.class);
 AnnotatedClass personClass = mapper.getFactory().findClassDescForClass(Person.class);
 BeanPropertyDefinition idProperty = personClass.findPropertyDefinition("id");
 AnnotatedWithParams creator = mapper.getFactory().createMethodParameterAnnotation(
 Person.class, "createPersonWithNameAndAge", new Class[] { String.class, int.class }, new Class[] { String.class, int.class }, idProperty.getAnnotations(), idDeserializer);
 return creator;
 }

 static class Person {
 String name;
 int age;
 }
}