import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.*;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.jsontype.impl.*;
import java.lang.reflect.*;
import java.util.*;

/**
 * Test class for StdSubtypeResolver.
 */
public class StdSubtypeResolverTest {

 private StdSubtypeResolver resolver;

 @Before
 public void setUp() {
 resolver = new StdSubtypeResolver();
 }

 /**
 * Test method for {@link StdSubtypeResolver#registerSubtypes(NamedType...)}.
 */
 @Test
 public void testRegisterSubtypes() {
 NamedType type1 = new NamedType(Integer.class, "INT_TYPE");
 NamedType type2 = new NamedType(Double.class, "DOUBLE_TYPE");
 resolver.registerSubtypes(type1, type2);
 List<NamedType> result = resolver.getRegisteredSubtypes();
 assertEquals(2, result.size());
 assertEquals(type1, result.get(0));
 assertEquals(type2, result.get(1));
 }

 /**
 * Test method for {@link StdSubtypeResolver#registerSubtypes(Class...)}.
 */
 @Test
 public void testRegisterSubtypesClassArray() {
 Class<?> intType = Integer.class;
 Class<?> doubleType = Double.class;
 resolver.registerSubtypes(intType, doubleType);
 List<NamedType> result = resolver.getRegisteredSubtypes();
 assertEquals(2, result.size());
 for (NamedType namedType : result) {
 Class<?> type = namedType.getType();
 if (type.equals(intType)) {
 assertEquals("INT_TYPE", namedType.getName());
 } else if (type.equals(doubleType)) {
 assertEquals("DOUBLE_TYPE", namedType.getName());
 }
 }
 }

 /**
 * Test method for {@link StdSubtypeResolver#collectAndResolveSubtypesByClass(MapperConfig, AnnotatedMember, JavaType)}.
 */
 @Test
 public void testCollectAndResolveSubtypesByClass() {
 AnnotatedMember member = new AnnotatedMember(null, null, null, null, null, null);
 JavaType baseType = null; // for backwards compatibility
 MapperConfig config = new MapperConfig();
 List<NamedType> result = resolver.collectAndResolveSubtypesByClass(config, member, baseType);
 assertTrue(result.isEmpty());

 baseType = Object.class;
 result = resolver.collectAndResolveSubtypesByClass(config, member, baseType);
 assertTrue(result.isEmpty());

 Class<?> intType = Integer.class;
 result = resolver.collectAndResolveSubtypesByClass(config, member, baseType.wrap(intType));
 assertEquals(1, result.size());
 assertEquals(intType, result.get(0).getType());
 }

 /**
 * Test method for {@link StdSubtypeResolver#collectAndResolveSubtypesByClass(MapperConfig, AnnotatedClass)}.
 */
 @Test
 public void testCollectAndResolveSubtypesByClassAnnotatedClass() {
 MapperConfig config = new MapperConfig();
 AnnotatedClass annotatedClass = new AnnotatedClass(null, null, null, null, new TypeIdentifier(Integer.class), null);
 List<NamedType> result = resolver.collectAndResolveSubtypesByClass(config, annotatedClass);
 assertTrue(result.isEmpty());

 annotatedClass = new AnnotatedClass(null, null, null, null, new TypeIdentifier(String.class), null);
 result = resolver.collectAndResolveSubtypesByClass(config, annotatedClass);
 assertTrue(result.isEmpty());

 annotatedClass = new AnnotatedClass(null, null, null, null, new TypeIdentifier(Integer.class), null);
 result = resolver.collectAndResolveSubtypesByClass(config, annotatedClass);
 assertEquals(1, result.size());
 assertEquals(Integer.class, result.get(0).getType());
 }

 /**
 * Test method for {@link StdSubtypeResolver#collectAndResolveSubtypesByTypeId(MapperConfig, AnnotatedMember, JavaType)}.
 */
 @Test
 public void testCollectAndResolveSubtypesByTypeId() {
 AnnotatedMember member = new AnnotatedMember(null, null, null, null, null, null);
 JavaType baseType = null; // for backwards compatibility
 MapperConfig config = new MapperConfig();
 List<NamedType> result = resolver.collectAndResolveSubtypesByTypeId(config, member, baseType);
 assertTrue(result.isEmpty());

 baseType = Object.class;
 result = resolver.collectAndResolveSubtypesByTypeId(config, member, baseType);
 assertTrue(result.isEmpty());

 baseType = Integer.class;
 result = resolver.collectAndResolveSubtypesByTypeId(config, member, baseType);
 assertEquals(1, result.size());
 assertEquals(Integer.class, result.get(0).getType());
 }

 /**
 * Test method for {@link StdSubtypeResolver#collectAndResolveSubtypesByTypeId(MapperConfig, AnnotatedClass)}.
 */
 @Test
 public void testCollectAndResolveSubtypesByTypeIdAnnotatedClass() {
 MapperConfig config = new MapperConfig();
 AnnotatedClass annotatedClass = new AnnotatedClass(null, null, null, null, new TypeIdentifier(Integer.class), null);
 List<NamedType> result = resolver.collectAndResolveSubtypesByTypeId(config, annotatedClass);
 assertTrue(result.isEmpty());

 annotatedClass = new AnnotatedClass(null, null, null, null, new TypeIdentifier(String.class), null);
 result = resolver.collectAndResolveSubtypesByTypeId(config, annotatedClass);
 assertTrue(result.isEmpty());

 annotatedClass = new AnnotatedClass(null, null, null, null, new TypeIdentifier(Integer.class), null);
 result = resolver.collectAndResolveSubtypesByTypeId(config, annotatedClass);
 assertEquals(1, result.size());
 assertEquals(Integer.class, result.get(0).getType());
 }

 /**
 * Test method for handling exceptions in {@link StdSubtypeResolver#collectAndResolveSubtypesByTypeId(MapperConfig, AnnotatedMember, JavaType)}.
 */
 @Test(expected = IllegalArgumentException.class)
 public void testCollectAndResolveSubtypesByTypeIdException() {
 AnnotatedMember member = new AnnotatedMember(null, null, null, null, null, null);
 JavaType baseType = null; // for backwards compatibility
 MapperConfig config = new MapperConfig();
 resolver.collectAndResolveSubtypesByTypeId(config, member, new JavaType());
 }

 /**
 * Test method for handling exceptions in {@link StdSubtypeResolver#collectAndResolveSubtypesByTypeId(MapperConfig, AnnotatedClass)}.
 */
 @Test(expected = IllegalArgumentException.class)
 public void testCollectAndResolveSubtypesByTypeIdAnnotatedClassException() {
 MapperConfig config = new MapperConfig();
 AnnotatedClass annotatedClass = new AnnotatedClass(null, null, null, null, new TypeIdentifier(Integer.class), null);
 resolver.collectAndResolveSubtypesByTypeId(config, null);
 }
}