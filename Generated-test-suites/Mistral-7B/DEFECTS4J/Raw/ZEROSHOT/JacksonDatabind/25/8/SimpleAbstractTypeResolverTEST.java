import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.core.type.ClassIdType;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.type.TypeKey;
import com.fasterxml.jackson.databind.module.SimpleAbstractTypeResolver;

public class SimpleAbstractTypeResolverTest {

 private SimpleAbstractTypeResolver resolver;

 @Before
 public void setup() {
 resolver = new SimpleAbstractTypeResolver();
 resolver.addMapping(Collection.class, java.util.LinkedList.class);
 resolver.addMapping(List.class, java.util.LinkedList.class);
 }

 @Test
 public void testFindTypeMapping() {
 DeserializationConfig config = new DeserializationConfig();
 TypeFactory typeFactory = config.getTypeFactory();

 JavaType javaType1 = typeFactory.constructType(Collection.class);
 JavaType javaType2 = typeFactory.constructType(List.class);
 JavaType javaType3 = typeFactory.constructType(LinkedList.class);
 JavaType javaType4 = typeFactory.constructType(ArrayList.class);

 JavaType resolvedType1 = resolver.findTypeMapping(config, javaType1);
 JavaType resolvedType2 = resolver.findTypeMapping(config, javaType2);
 JavaType resolvedType3 = resolver.findTypeMapping(config, javaType3);
 JavaType resolvedType4 = resolver.findTypeMapping(config, javaType4);

 assertNull(resolvedType4);
 assertEquals(javaType3.getRawClass(), resolvedType1.getRawClass());
 assertEquals(javaType3.getRawClass(), resolvedType2.getRawClass());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindTypeMapping_InvalidMapping() {
 DeserializationConfig config = new DeserializationConfig();
 TypeFactory typeFactory = config.getTypeFactory();

 JavaType javaType1 = typeFactory.constructType(Collection.class);
 JavaType javaType2 = typeFactory.constructType(String.class);

 resolver.findTypeMapping(config, javaType1);
 resolver.findTypeMapping(config, javaType2);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddMapping_InvalidSuperType() {
 resolver.addMapping(Integer.class, String.class);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddMapping_InvalidSubType() {
 resolver.addMapping(Collection.class, Integer.class);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddMapping_SameType() {
 resolver.addMapping(Collection.class, Collection.class);
 }

 @Test
 public void testResolveAbstractType() {
 DeserializationConfig config = new DeserializationConfig();
 JavaType javaType = typeFactory.constructType(AbstractList.class);
 JavaType resolvedType = resolver.resolveAbstractType(config, javaType);
 assertNull(resolvedType);
 }
}