package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedClassResolver;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;

import org.junit.Before;
import org.junit.Test;
import java.lang.reflect.Modifier;
import java.util.*;

import static org.junit.Assert.*;

public class StdSubtypeResolverTest {

  private StdSubtypeResolver subtypeResolver;

  @Before
  public void setUp() {
    subtypeResolver = new StdSubtypeResolver();
  }

  @Test
  public void registerSubtypes() {
    NamedType namedType1 = new NamedType(Class1.class);
    NamedType namedType2 = new NamedType(Class2.class);
    subtypeResolver.registerSubtypes(namedType1, namedType2);

    Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByClass(null, null, null);
    assertNotNull(subtypes);
    assertEquals(2, subtypes.size());
    assertTrue(subtypes.contains(namedType1));
    assertTrue(subtypes.contains(namedType2));
  }

  @Test
  public void registerSubtypesWithClasses() {
    subtypeResolver.registerSubtypes(Class1.class, Class2.class);

    Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByClass(null, null, null);
    assertNotNull(subtypes);
    assertEquals(2, subtypes.size());
    assertTrue(subtypes.contains(new NamedType(Class1.class)));
    assertTrue(subtypes.contains(new NamedType(Class2.class)));
  }

  @Test
  public void collectAndResolveSubtypesByClassWhenNoSubtypeIsRegistered() {
    Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByClass(null, null, null);

    assertNotNull(subtypes);
    assertTrue(subtypes.isEmpty());
  }

  @Test
  public void collectAndResolveSubtypesByClass() {
    subtypeResolver.registerSubtypes(Class1.class, Class3.class);

    Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByClass(null, null, null);

    assertNotNull(subtypes);
    assertEquals(2, subtypes.size());
    assertTrue(subtypes.contains(new NamedType(Class1.class)));
    assertTrue(subtypes.contains(new NamedType(Class3.class)));
  }

  @Test
  public void collectAndResolveSubtypesByClassWithProperty() {
    NamedType namedType = new NamedType(Class2.class);
    AnnotationIntrospector annotationIntrospector = new AnnotationIntrospector() {
        @Override
        public Collection<NamedType> findSubtypes(AnnotatedClass ac) {
            return Arrays.asList(namedType);
        }
    };

    MapperConfig<?> config = new MapperConfig<Object>() {
        @Override
        public AnnotationIntrospector getAnnotationIntrospector() {
            return annotationIntrospector;
        }
    };

    Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByClass(config, null, null);

    assertNotNull(subtypes);
    assertEquals(1, subtypes.size());
    assertTrue(subtypes.contains(namedType));
  }

  @Test
  public void collectAndResolveSubtypesByClassWithRootType() {
    subtypeResolver.registerSubtypes(Class1.class);

    Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByClass(null, null, JavaType.empty());

    assertNotNull(subtypes);
    assertEquals(1, subtypes.size());
    assertTrue(subtypes.contains(new NamedType(Class1.class)));
  }

  @Test
  public void collectAndResolveSubtypesByTypeIdWhenNoSubtypeIsRegistered() {
    Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByTypeId(null, null, null);

    assertNotNull(subtypes);
    assertTrue(subtypes.isEmpty());
  }

  @Test
  public void collectAndResolveSubtypesByTypeId() {
    subtypeResolver.registerSubtypes(Class1.class, Class2.class, Class3.class);

    Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByTypeId(null, null, JavaType.empty());

    assertNotNull(subtypes);
    assertEquals(3, subtypes.size());
    assertTrue(subtypes.contains(new NamedType(Class1.class)));
    assertTrue(subtypes.contains(new NamedType(Class2.class)));
    assertTrue(subtypes.contains(new NamedType(Class3.class)));
  }

  @Test
  public void collectAndResolveSubtypesByTypeIdWithProperty() {
    NamedType namedType = new NamedType(Class2.class);
    AnnotationIntrospector annotationIntrospector = new AnnotationIntrospector() {
        @Override
        public Collection<NamedType> findSubtypes(AnnotatedClass ac) {
            return Arrays.asList(namedType);
        }
    };

    MapperConfig<?> config = new MapperConfig<Object>() {
        @Override
        public AnnotationIntrospector getAnnotationIntrospector() {
            return annotationIntrospector;
        }
    };

    Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByTypeId(config, null, null);

    assertNotNull(subtypes);
    assertEquals(1, subtypes.size());
    assertTrue(subtypes.contains(namedType));
  }

  @Test
  public void collectAndResolveSubtypesByTypeIdWithRootType() {
    subtypeResolver.registerSubtypes(Class1.class);

    Collection<NamedType> subtypes = subtypeResolver.collectAndResolveSubtypesByTypeId(null, null, null);

    assertNotNull(subtypes);
    assertEquals(1, subtypes.size());
    assertTrue(subtypes.contains(new NamedType(Class1.class)));
  }

  private class Class1 {}
  private class Class2 {}
  private class Class3 {}

}