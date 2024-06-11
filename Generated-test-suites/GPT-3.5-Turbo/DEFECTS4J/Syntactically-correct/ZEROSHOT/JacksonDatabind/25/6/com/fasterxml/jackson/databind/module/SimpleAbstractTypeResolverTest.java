package com.fasterxml.jackson.databind.module;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import com.fasterxml.jackson.databind.AbstractTypeResolver;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ClassKey;
import java.util.*;

public class SimpleAbstractTypeResolverTest {

   private SimpleAbstractTypeResolver resolver;

   @Before
   public void setUp() {
      resolver = new SimpleAbstractTypeResolver();
   }

   @Test
   public void testAddMapping() {
      resolver.addMapping(Collection.class, LinkedList.class);
      resolver.addMapping(List.class, LinkedList.class);

      assertEquals(LinkedList.class, resolver._mappings.get(new ClassKey(Collection.class)));
      assertEquals(LinkedList.class, resolver._mappings.get(new ClassKey(List.class)));
   }

   @Test(expected = IllegalArgumentException.class)
   public void testAddMappingSelf() {
      resolver.addMapping(Collection.class, Collection.class);
   }

   @Test(expected = IllegalArgumentException.class)
   public void testAddMappingSubType() {
      resolver.addMapping(Collection.class, ArrayList.class);
   }

   @Test(expected = IllegalArgumentException.class)
   public void testAddMappingConcreteSuperType() {
      resolver.addMapping(ArrayList.class, LinkedList.class);
   }

   @Test
   public void testFindTypeMapping() {
      resolver.addMapping(Collection.class, LinkedList.class);
      resolver.addMapping(List.class, LinkedList.class);

      DeserializationConfig config = null;
      JavaType type1 = new JavaType(Collection.class);
      JavaType type2 = new JavaType(List.class);
      JavaType type3 = new JavaType(LinkedList.class);
      JavaType type4 = new JavaType(ArrayList.class);

      assertEquals(type3, resolver.findTypeMapping(config, type1));
      assertEquals(type3, resolver.findTypeMapping(config, type2));
      assertNull(resolver.findTypeMapping(config, type3));
      assertNull(resolver.findTypeMapping(config, type4));
   }

   @Test
   public void testResolveAbstractType() {
      DeserializationConfig config = null;
      JavaType type = new JavaType(Object.class);

      assertNull(resolver.resolveAbstractType(config, type));
   }
}