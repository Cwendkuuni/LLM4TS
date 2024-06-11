import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.CreatorCollector;
import com.fasterxml.jackson.databind.deser.CreatorProperty;
import com.fasterxml.jackson.databind.deser.std.StdCreatorProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.introspect.NestedCreatorProperty;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;

public class CreatorCollectorTest {

 private CreatorCollector collector;
 private DeserializationConfig config;
 private ObjectMapper mapper;

 @Before
 public void setUp() throws Exception {
 collector = new CreatorCollector(null, true);
 config = new DeserializationConfig();
 mapper = new ObjectMapper();
 }

 @Test
 public void testDefaultCreator() throws Exception {
 BeanDescription beanDesc = mapper.getFactory().findDefaultInstance(ArrayList.class);
 collector.setDefaultCreator(beanDesc.getConstructor());
 ValueInstantiator inst = collector.constructValueInstantiator(config);
 assertNotNull(inst.createValueInstance(config, JavaType.constructParametricType(ArrayList.class, String.class)));
 }

 @Test
 public void testStringCreator() throws Exception {
 BeanDescription beanDesc = mapper.getFactory().findDefaultInstance(String[].class);
 AnnotatedMethod constructor = AnnotatedMethod.construct(String[].class, new Class[0]);
 collector.addStringCreator(new AnnotatedWithParams(constructor, null), false);
 ValueInstantiator inst = collector.constructValueInstantiator(config);
 String[] result = (String[]) inst.createValueInstance(config, JavaType.constructParametricType(String[].class, new Class[0]));
 assertNotNull(result);
 assertEquals(new String[0].length, result.length);
 }

 @Test
 public void testIntCreator() throws Exception {
 BeanDescription beanDesc = mapper.getFactory().findDefaultInstance(Integer.class);
 AnnotatedMethod constructor = AnnotatedMethod.construct(Integer.class, new int[0]);
 collector.addIntCreator(new AnnotatedWithParams(constructor, null), false);
 ValueInstantiator inst = collector.constructValueInstantiator(config);
 Integer result = (Integer) inst.createValueInstance(config, JavaType.construct(Integer.class));
 assertEquals(0, result.intValue());
 }

 @Test
 public void testLongCreator() throws Exception {
 BeanDescription beanDesc = mapper.getFactory().findDefaultInstance(Long.class);
 AnnotatedMethod constructor = AnnotatedMethod.construct(Long.class, new long[0]);
 collector.addLongCreator(new AnnotatedWithParams(constructor, null), false);
 ValueInstantiator inst = collector.constructValueInstantiator(config);
 Long result = (Long) inst.createValueInstance(config, JavaType.construct(Long.class));
 assertEquals(0L, result.longValue());
 }

 @Test
 public void testDoubleCreator() throws Exception {
 BeanDescription beanDesc = mapper.getFactory().findDefaultInstance(Double.class);
 AnnotatedMethod constructor = AnnotatedMethod.construct(Double.class, new double[0]);
 collector.addDoubleCreator(new AnnotatedWithParams(constructor, null), false);
 ValueInstantiator inst = collector.constructValueInstantiator(config);
 Double result = (Double) inst.createValueInstance(config, JavaType.construct(Double.class));
 assertEquals(0.0, result.doubleValue(), 0.0);
 }

 @Test
 public void testBooleanCreator() throws Exception {
 BeanDescription beanDesc = mapper.getFactory().findDefaultInstance(Boolean.class);
 AnnotatedMethod constructor = AnnotatedMethod.construct(Boolean.class, new boolean[0]);
 collector.addBooleanCreator(new AnnotatedWithParams(constructor, null), false);
 ValueInstantiator inst = collector.constructValueInstantiator(config);
 Boolean result = (Boolean) inst.createValueInstance(config, JavaType.construct(Boolean.class));
 assertEquals(false, result.booleanValue());
 }

 @Test
 public void testDelegateCreator() throws Exception {
 Class<?> targetClass = MyDelegate.class;
 BeanDescription beanDesc = mapper.getFactory().findDefaultInstance(targetClass);
 AnnotatedMethod constructor = AnnotatedMethod.construct(targetClass, new Class[0]);
 CreatorProperty[] injectables = new CreatorProperty[] {
 new StdCreatorProperty.Instance(MyDependency.class),
 new NestedCreatorProperty.Standard("fieldName", MyDependency.class)
 };
 collector.addDelegatingCreator(new AnnotatedWithParams(constructor, null), false, injectables);
 ValueInstantiator inst = collector.constructValueInstantiator(config);
 MyDelegate result = (MyDelegate) inst.createValueInstance(config, JavaType.construct(targetClass));
 assertNotNull(result);
 assertEquals("expectedValue", result.getDependency().getName());
 assertEquals("expectedValue2", result.getFieldName());
 }

 @Test
 public void testPropertyCreator() throws Exception {
 Class<?> targetClass = MyClass.class;
 BeanDescription beanDesc = mapper.getFactory().findDefaultInstance(targetClass);
 BeanPropertyDefinition field = beanDesc.findBeanPropertyDefinition("fieldName");
 AnnotatedMethod method = findSetter(beanDesc, field);
 CreatorProperty[] properties = new CreatorProperty[] {
 new StdCreatorProperty.Instance(String.class, "expectedValue"),
 new StdCreatorProperty.Instance(MyOtherClass.class, "expectedOtherValue")
 };
 collector.addPropertyCreator(new AnnotatedWithParams(method, null), false, properties);
 ValueInstantiator inst = collector.constructValueInstantiator(config);
 MyClass result = (MyClass) inst.createValueInstance(config, JavaType.construct(targetClass));
 assertNotNull(result);
 assertEquals("expectedValue", result.getFieldName());
 assertEquals("expectedOtherValue", result.getOtherField());
 }

 @Test
 public void testErrorCreator() throws Exception {
 BeanDescription beanDesc = mapper.getFactory().findDefaultInstance(String.class);
 AnnotatedMethod constructor = AnnotatedMethod.construct(int.class, new Class[0]);
 collector.addIntCreator(new AnnotatedWithParams(constructor, null), false);
 try {
 ValueInstantiator inst = collector.constructValueInstantiator(config);
 fail("Should have thrown exception");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testMissingCreator() throws Exception {
 BeanDescription beanDesc = mapper.getFactory().findDefaultInstance(MyClass.class);
 ValueInstantiator inst = collector.constructValueInstantiator(config);
 try {
 inst.createValueInstance(config, JavaType.construct(MyClass.class));
 fail("Should have thrown exception");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 private static AnnotatedMethod findSetter(BeanDescription beanDesc, BeanPropertyDefinition propDef) {
 Method method = propDef.getSetter();
 if (method == null) {
 throw new RuntimeException("No setter for property: " + propDef.getName());
 }
 return AnnotatedMethod.construct(method, new Class[] { propDef.getType() });
 }

 private static class MyClass {
 private String fieldName;
 private MyOtherClass otherField;
 // getters and setters omitted
 }

 private static class MyOtherClass {
 private String name;
 // getters and setters omitted
 }

 private static class MyDelegate {
 private MyDependency dependency;
 private String fieldName;
 // getters and setters omitted
 }

 private static class MyDependency {
 private String name;
 // getters and setters omitted
 }

}