import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonSubTypes;
import com.fasterxml.jackson.databind.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.StdSubtypeResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;

import java.lang.reflect.Modifier;
import java.util.*;

public class StdSubtypeResolverTest {

 private StdSubtypeResolver resolver;
 private MapperConfig config;

 @Before
 public void setup() {
 config = new MapperConfig(new AnnotationIntrospector());
 resolver = new StdSubtypeResolver();
 }

 @Test
 public void testRegistration() {
 // Test registration by class
 resolver.registerSubtypes(MyClass1.class);
 assertEquals(1, resolver.getRegisteredClasses().size());

 // Test registration by type name
 resolver.registerSubtypes(MyClass1.class, "MyType");
 assertEquals(2, resolver.getRegisteredClasses().size());
 }

 @Test
 public void testRegistrationAnnotation() {
 @JsonSubTypes({
 @Type(value = MyClass1.class, name = "MyType"),
 @Type(value = MyClass2.class, name = "OtherType")
 })
 static class MyClassWithAnnotation {
 }

 MyClassWithAnnotation m = new MyClassWithAnnotation();
 resolver.registerSubtypes(m.getClass());
 assertEquals(3, resolver.getRegisteredClasses().size());
 }

 @Test
 public void testCollectAndResolveSubtypesByClass() {
 Class<?> baseType = MyClass1.class;
 JavaType javaBaseType = ObjectMapper.getDefaultInstance().getTypeFactory().constructType(baseType);

 // Test with no registered subtypes
 Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(config, null, javaBaseType);
 assertTrue(subtypes.isEmpty());

 // Test with registered subtypes
 resolver.registerSubtypes(MyClass1.class);
 subtypes = resolver.collectAndResolveSubtypesByClass(config, null, javaBaseType);
 assertEquals(1, subtypes.size());
 assertEquals(MyClass1.class, subtypes.iterator().next().getType().asRawType());

 // Test with registered subtypes by name
 resolver.registerSubtypes(MyClass1.class, "MyType");
 subtypes = resolver.collectAndResolveSubtypesByClass(config, null, javaBaseType);
 assertEquals(1, subtypes.size());
 assertEquals("MyType", subtypes.iterator().next().getName());
 }

 @Test
 public void testCollectAndResolveSubtypesByClassAnnotated() {
 AnnotatedClass ac = AnnotatedClassResolver.resolveWithoutSuperTypes(config, MyClass1.class);
 Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(config, ac, null);
 assertEquals(1, subtypes.size());
 assertEquals(MyClass1.class, subtypes.iterator().next().getType().asRawType());
 }

 @Test
 public void testCollectAndResolveSubtypesByTypeId() {
 JavaType baseType = ObjectMapper.getDefaultInstance().getTypeFactory().constructType(MyClass1.class);

 // Test with no registered subtypes
 Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(config, null, baseType);
 assertTrue(subtypes.isEmpty());

 // Test with registered subtypes by class
 resolver.registerSubtypes(MyClass1.class);
 subtypes = resolver.collectAndResolveSubtypesByTypeId(config, null, baseType);
 assertEquals(1, subtypes.size());
 assertEquals(MyClass1.class, subtypes.iterator().next().getType().asRawType());

 // Test with registered subtypes by type name
 resolver.registerSubtypes(MyClass1.class, "MyType");
 subtypes = resolver.collectAndResolveSubtypesByTypeId(config, null, baseType);
 assertEquals(1, subtypes.size());
 assertEquals("MyType", subtypes.iterator().next().getName());

 // Test with registered subtypes by annotation
 MyClassWithAnnotation m = new MyClassWithAnnotation();
 resolver.registerSubtypes(m.getClass());
 subtypes = resolver.collectAndResolveSubtypesByTypeId(config, null, baseType);
 assertEquals(2, subtypes.size());
 assertEquals(MyClass1.class, subtypes.get(0).getType().asRawType());
 assertEquals("MyType", subtypes.get(1).getName());
 }

 @Test
 public void testCollectAndResolveSubtypesByTypeIdAnnotated() {
 AnnotatedClass baseType = AnnotatedClassResolver.resolveWithoutSuperTypes(config, MyClass1.class);
 Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(config, baseType, null);
 assertEquals(1, subtypes.size());
 assertEquals(MyClass1.class, subtypes.iterator().next().getType().asRawType());
 }

 @Test(expected = RuntimeException.class)
 public void testCollectAndResolveSubtypesByTypeIdInvalidType() {
 // Test exception when typeId is not a valid type
 Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(config, null, Integer.TYPE);
 fail("Expected RuntimeException to be thrown");
 }
}

class MyClass1 {
}

class MyClass2 {
}