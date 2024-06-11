package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.*;
import com.fasterxml.jackson.databind.cfg.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.jsontype.impl.*;
import java.lang.reflect.*;
import java.util.*;

import org.junit.*;
import org.junit.experimental.runners.Enclosed;

/**
 * Test class for StdSubtypeResolver
 */
@RunWith(Enclosed.class)
public class StdSubtypeResolverTest {

 @SuppressWarnings("unchecked")
 static class TestCase {
 
     AnnotationIntrospector _ai;
     ObjectMapper _mapper;
     
     @Before
     public void setUp() throws Exception {
         _ai = new JacksonAnnotationIntrospector();
         _mapper = new ObjectMapper();
     }
 
     @Test
     public void testRegisterSubtypes() throws Exception {
         StdSubtypeResolver resolver = new StdSubtypeResolver();
         resolver.registerSubtypes(new NamedType(String.class, "UppercaseString"));
         assertTrue(resolver._registeredSubtypes.contains(new NamedType(String.class, "UppercaseString")));
         assertEquals(1, resolver._registeredSubtypes.size());
     }
 
     @Test
     public void testRegisterSubtypesArray() throws Exception {
         StdSubtypeResolver resolver = new StdSubtypeResolver();
         resolver.registerSubtypes(new Class[] {UppercaseString.class});
         assertTrue(resolver._registeredSubtypes.contains(new NamedType(UppercaseString.class)));
         assertEquals(1, resolver._registeredSubtypes.size());
     }
 
     @Test
     public void testRegisteredSubtypesEmpty() throws Exception {
         StdSubtypeResolver resolver = new StdSubtypeResolver();
         assertNull(resolver._registeredSubtypes);
         resolver.registerSubtypes();
         assertEquals(0, resolver._registeredSubtypes.size());
     }
 
     @Test
     public void testCollectAndResolveSubtypesByClass() throws Exception {
         StdSubtypeResolver resolver = new StdSubtypeResolver();
         resolver.registerSubtypes(new NamedType(Number.class, "IntegerSubtype"));
         resolver.registerSubtypes(new NamedType(String.class, "UppercaseString"));

         MapperConfig<?> config = new MapperConfig(new AnnotationIntrospector());

         JavaType baseType = _mapper.getTypeFactory().constructType(Object.class);

         Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(config, null, baseType);
         assertEquals(3, subtypes.size());
         assertTrue(subtypes.contains(new NamedType(Number.class, "IntegerSubtype")));
         assertTrue(subtypes.contains(new NamedType(String.class, "UppercaseString")));
         assertTrue(subtypes.contains(new NamedType(Object.class, null)));

         subtypes = resolver.collectAndResolveSubtypesByClass(config, null, _mapper.getTypeFactory().constructType(Integer.class));
         assertEquals(2, subtypes.size());
         assertTrue(subtypes.contains(new NamedType(Number.class, "IntegerSubtype")));
         assertTrue(subtypes.contains(new NamedType(Integer.class, null)));
     }
 
     @Test
     public void testCollectAndResolveSubtypesByClassNullBaseType() throws Exception {
         StdSubtypeResolver resolver = new StdSubtypeResolver();
         resolver.registerSubtypes(new NamedType(Number.class, "IntegerSubtype"));
         resolver.registerSubtypes(new NamedType(String.class, "UppercaseString"));

         MapperConfig<?> config = new MapperConfig(new AnnotationIntrospector());

         Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(config, null, null);
         assertEquals(3, subtypes.size());
         assertTrue(subtypes.contains(new NamedType(Object.class, null)));
         assertTrue(subtypes.contains(new NamedType(Number.class, "IntegerSubtype")));
         assertTrue(subtypes.contains(new NamedType(String.class, "UppercaseString")));
     }
 
     @Test
     public void testCollectAndResolveSubtypesByTypeId() throws Exception {
         StdSubtypeResolver resolver = new StdSubtypeResolver();
         resolver.registerSubtypes(new NamedType(Number.class, "IntegerSubtype"));
         resolver.registerSubtypes(new NamedType(String.class, "UppercaseString"));

         MapperConfig<?> config = new MapperConfig(new AnnotationIntrospector());

         AnnotatedMember property = Mockito.mock(AnnotatedMember.class);
         JavaType baseType = _mapper.getTypeFactory().constructType(Object.class);

         Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(config, property, baseType);
         assertEquals(3, subtypes.size());
         assertTrue(subtypes.contains(new NamedType(Object.class, null)));
         assertTrue(subtypes.contains(new NamedType(Number.class, "IntegerSubtype")));
         assertTrue(subtypes.contains(new NamedType(String.class, "UppercaseString")));

         subtypes = resolver.collectAndResolveSubtypesByTypeId(config, property, _mapper.getTypeFactory().constructType(Integer.class));
         assertEquals(2, subtypes.size());
         assertTrue(subtypes.contains(new NamedType(Number.class, "IntegerSubtype")));
         assertTrue(subtypes.contains(new NamedType(Integer.class, null)));
     }
 
     @Test
     public void testCollectAndResolveSubtypesByTypeIdNullBaseType() throws Exception {
         StdSubtypeResolver resolver = new StdSubtypeResolver();
         resolver.registerSubtypes(new NamedType(Number.class, "IntegerSubtype"));
         resolver.registerSubtypes(new NamedType(String.class, "UppercaseString"));

         MapperConfig<?> config = new MapperConfig(new AnnotationIntrospector());

         AnnotatedMember property = Mockito.mock(AnnotatedMember.class);

         Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(config, property, null);
         assertEquals(3, subtypes.size());
         assertTrue(subtypes.contains(new NamedType(Object.class, null)));
         assertTrue(subtypes.contains(new NamedType(Number.class, "IntegerSubtype")));
         assertTrue(subtypes.contains(new NamedType(String.class, "UppercaseString")));
     }
 
     @Test(expected = IllegalArgumentException.class)
     public void testCollectAndResolveSubtypesByTypeIdNullProperty() throws Exception {
         StdSubtypeResolver resolver = new StdSubtypeResolver();

         MapperConfig<?> config = new MapperConfig(new AnnotationIntrospector());

         JavaType baseType = _mapper.getTypeFactory().constructType(Object.class);

         resolver.collectAndResolveSubtypesByTypeId(config, null, baseType);
     }
 
     @Test(expected = IllegalArgumentException.class)
     public void testCollectAndResolveSubtypesByTypeIdNullBaseType() throws Exception {
         StdSubtypeResolver resolver = new StdSubtypeResolver();

         MapperConfig<?> config = new MapperConfig(new AnnotationIntrospector());

         AnnotatedMember property = Mockito.mock(AnnotatedMember.class);

         resolver.collectAndResolveSubtypesByTypeId(config, property, null);
     }
 
     @Test(expected = IllegalArgumentException.class)
     public void testCollectAndResolveSubtypesByClassNullBaseType() throws Exception {
         StdSubtypeResolver resolver = new StdSubtypeResolver();

         MapperConfig<?> config = new MapperConfig(new AnnotationIntrospector());

         resolver.collectAndResolveSubtypesByClass(config, null, null);
     }
 
     @Test
     public void testCollectAndResolveSubtypesByClassNullProperty() throws Exception {
         StdSubtypeResolver resolver = new StdSubtypeResolver();

         MapperConfig<?> config = new MapperConfig(new AnnotationIntrospector());

         JavaType baseType = _mapper.getTypeFactory().constructType(Object.class);

         Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByClass(config, null, baseType);
         assertEquals(1, subtypes.size());
         assertTrue(subtypes.contains(new NamedType(Object.class, null)));
     }
 
     @Test
     public void testCollectAndResolveSubtypesByTypeIdNullProperty() throws Exception {
         StdSubtypeResolver resolver = new StdSubtypeResolver();

         MapperConfig<?> config = new MapperConfig(new AnnotationIntrospector());

         JavaType baseType = _mapper.getTypeFactory().constructType(Object.class);

         Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(config, null, baseType);
         assertEquals(1, subtypes.size());
         assertTrue(subtypes.contains(new NamedType(Object.class, null)));
     }
 
     @Test
     public void testCollectAndResolveSubtypesByTypeIdEmpty() throws Exception {
         StdSubtypeResolver resolver = new StdSubtypeResolver();

         MapperConfig<?> config = new MapperConfig(new AnnotationIntrospector());

         AnnotatedMember property = Mockito.mock(AnnotatedMember.class);

         JavaType baseType = _mapper.getTypeFactory().constructType(Object.class);

         Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(config, property, baseType);
         assertEquals(1, subtypes.size());
         assertTrue(subtypes.contains(new NamedType(Object.class, null)));
     }
 
     @Test
     public void testCombineNamedAndUnnamed() throws Exception {
         StdSubtypeResolver resolver = new StdSubtypeResolver();
         resolver.registerSubtypes(new NamedType(Number.class, "IntegerSubtype"));
         resolver.registerSubtypes(new NamedType(String.class, "UppercaseString"));
         resolver.registerSubtypes(new NamedType(Integer.class));

         MapperConfig<?> config = new MapperConfig(new AnnotationIntrospector());

         Collection<NamedType> subtypes = resolver.collectAndResolveSubtypesByTypeId(config, null, _mapper.getTypeFactory().constructType(Object.class));
         assertEquals(5, subtypes.size());
         assertTrue(subtypes.containsAll(Arrays.asList(new NamedType(Object.class, null),
                 new NamedType(Number.class, "IntegerSubtype"),
                 new NamedType(String.class, "UppercaseString"),
                 new NamedType(Integer.class),
                 new NamedType(Number.class, "IntegerSubtype"))));
     }
 }

 static class UppercaseString {
 }
}