package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import java.lang.reflect.Modifier;
import java.util.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.core.type.ClassType;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.*;
import com.fasterxml.jackson.databind.cfg.MapperBuilder;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class StdSubtypeResolverTest {

 private AnnotatedClassResolver annotatedClassResolver;
 private AnnotationIntrospector annotationIntrospector;
 private JavaTypeFactory typeFactory;
 private MapperBuilder mapperBuilder;
 private ObjectMapper objectMapper;
 private StdSubtypeResolver stdSubtypeResolver;

 @Before
 public void setUp() throws Exception {
 ObjectMapper baseMapper = new ObjectMapper();
 mapperBuilder = baseMapper.builder();
 annotationIntrospector = mapperBuilder.getAnnotationIntrospector();
 typeFactory = mapperBuilder.getTypeFactory();
 stdSubtypeResolver = new StdSubtypeResolver();
 annotatedClassResolver = Mockito.mock(AnnotatedClassResolver.class);
 }

 @Test
 public void testRegisterSubtypes() throws Exception {
 Class<SubclassA> subclassA = SubclassA.class;
 Class<SubclassB> subclassB = SubclassB.class;
 Class<BaseClass> baseClass = BaseClass.class;
 stdSubtypeResolver.registerSubtypes(new NamedType(subclassA));
 stdSubtypeResolver.registerSubtypes(new NamedType(subclassB));
 List<NamedType> registeredTypes = stdSubtypeResolver.getRegisteredSubtypes();
 assertTrue(registeredTypes.size() == 2);
 assertTrue(registeredTypes.contains(new NamedType(subclassA)));
 assertTrue(registeredTypes.contains(new NamedType(subclassB)));
 }

 @Test
 public void testRegisterSubtypesWithClass() throws Exception {
 Class<SubclassA> subclassA = SubclassA.class;
 Class<SubclassB> subclassB = SubclassB.class;
 Class<BaseClass> baseClass = BaseClass.class;
 stdSubtypeResolver.registerSubtypes(new Class[] { subclassA, subclassB });
 List<NamedType> registeredTypes = stdSubtypeResolver.getRegisteredSubtypes();
 assertTrue(registeredTypes.size() == 2);
 assertTrue(registeredTypes.contains(new NamedType(subclassA)));
 assertTrue(registeredTypes.contains(new NamedType(subclassB)));
 }

 @Test
 public void testCollectAndResolveSubtypesByClass() throws Exception {
 BaseClass baseClass = new BaseClass();
 SubclassA subclassA = new SubclassA();
 SubclassB subclassB = new SubclassB();
 MapperConfig<?> config = mapperBuilder.build();
 JavaType baseType = typeFactory.constructType(BaseClass.class);
 ArrayList<NamedType> expectedTypes = new ArrayList<>();
 expectedTypes.add(new NamedType(BaseClass.class, null));
 expectedTypes.add(new NamedType(SubclassA.class, "SubclassA"));
 expectedTypes.add(new NamedType(SubclassB.class, "SubclassB"));
 AnnotatedMember property = Mockito.mock(AnnotatedMember.class);
 AnnotatedClass annotatedClass = Mockito.mock(AnnotatedClass.class);
 when(annotatedClass.getAnnotations()).thenReturn(new JsonTypeInfo.Id(JsonTypeInfo.As.PROPERTY, "type"));
 when(annotatedClass.getRawType()).thenReturn(baseClass.getSuperclass());
 when(annotationIntrospector.findSubtypes(property)).thenReturn(null);
 when(annotationIntrospector.findSubtypes(annotatedClass)).thenReturn(null);
 when(annotatedClassResolver.resolveWithoutSuperTypes(config, baseClass)).thenReturn(annotatedClass);
 AnnotatedClass subclassAAnnotatedClass = Mockito.mock(AnnotatedClass.class);
 when(annotatedClassResolver.resolveWithoutSuperTypes(config, SubclassA.class)).thenReturn(subclassAAnnotatedClass);
 AnnotatedClass subclassBAnnotatedClass = Mockito.mock(AnnotatedClass.class);
 when(annotatedClassResolver.resolveWithoutSuperTypes(config, SubclassB.class)).thenReturn(subclassBAnnotatedClass);
 AnnotatedMember subclassAProperty = Mockito.mock(AnnotatedMember.class);
 AnnotatedMember subclassBProperty = Mockito.mock(AnnotatedMember.class);
 when(subclassAProperty.getAnnotations()).thenReturn(new JsonTypeName("SubclassA"));
 when(subclassBProperty.getAnnotations()).thenReturn(new JsonTypeName("SubclassB"));
 when(property.getDeclaringClass()).thenReturn(baseClass);
 when(property.getName()).thenReturn("testProperty");
 when(objectMapper.findClassForType(ClassType.construct(baseClass.getSuperclass()))).thenReturn(baseClass);
 when(objectMapper.findClassForType(ClassType.construct(SubclassA.class))).thenReturn(subclassA);
 when(objectMapper.findClassForType(ClassType.construct(SubclassB.class))).thenReturn(subclassB);
 ArrayList<NamedType> actualTypes = stdSubtypeResolver.collectAndResolveSubtypesByClass(config, property, baseType);
 assertEquals(expectedTypes, actualTypes);
 }

 @Test
 public void testCollectAndResolveSubtypesByClassEmpty() throws Exception {
 BaseClass baseClass = new BaseClass();
 MapperConfig<?> config = mapperBuilder.build();
 JavaType baseType = typeFactory.constructType(BaseClass.class);
 ArrayList<NamedType> expectedTypes = new ArrayList<>();
 expectedTypes.add(new NamedType(BaseClass.class, null));
 ArrayList<NamedType> actualTypes = stdSubtypeResolver.collectAndResolveSubtypesByClass(config, null, baseType);
 assertEquals(expectedTypes, actualTypes);
 }

 @Test
 public void testCollectAndResolveSubtypesByTypeId() throws Exception {
 BaseClass baseClass = new BaseClass();
 SubclassA subclassA = new SubclassA();
 SubclassB subclassB = new SubclassB();
 MapperConfig<?> config = mapperBuilder.build();
 ObjectMapper objectMapper = new ObjectMapper();
 JsonFactory jsonFactory = objectMapper.getFactory();
 JsonParser jsonParser = jsonFactory.createParser("[\"BaseClass\", {\"type\":\"SubclassA\"}]");
 ObjectReader objectReader = jsonParser.readerForUpdating(baseClass);
 JsonNode node = jsonParser.readValueAsTree();
 JsonNode subclassAJsonNode = node.get(0);
 JsonNode subclassBJsonNode = node.get(1);
 Class<?> actualSubclassA = objectReader.readValueAs(Class.class);
 Class<?> actualSubclassB = objectReader.readValueAs(Class.class);
 assertEquals(SubclassA.class, actualSubclassA);
 assertEquals(SubclassB.class, actualSubclassB);
 ArrayList<NamedType> expectedTypes = new ArrayList<>();
 expectedTypes.add(new NamedType(BaseClass.class, null));
 expectedTypes.add(new NamedType(SubclassA.class, "SubclassA"));
 expectedTypes.add(new NamedType(SubclassB.class, "SubclassB"));
 ArrayList<NamedType> actualTypes = stdSubtypeResolver.collectAndResolveSubtypesByTypeId(config, null, baseType);
 assertEquals(expectedTypes, actualTypes);
 }

 @Test
 public void testCollectAndResolveSubtypesByTypeIdEmpty() throws Exception {
 BaseClass baseClass = new BaseClass();
 MapperConfig<?> config = mapperBuilder.build();
 JavaType baseType = typeFactory.constructType(BaseClass.class);
 ArrayList<NamedType> expectedTypes = new ArrayList<>();
 expectedTypes.add(new NamedType(BaseClass.class, null));
 ArrayList<NamedType> actualTypes = stdSubtypeResolver.collectAndResolveSubtypesByTypeId(config, null, baseType);
 assertEquals(expectedTypes, actualTypes);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCollectAndResolveSubtypesByTypeIdNullBaseType() throws Exception {
 BaseClass baseClass = null;
 MapperConfig<?> config = mapperBuilder.build();
 JavaType baseType = typeFactory.constructType(BaseClass.class);
 stdSubtypeResolver.collectAndResolveSubtypesByTypeId(config, null, baseType);
 fail("Should have thrown IllegalArgumentException for null baseType");
 }

 @Test
 public void testGetRegisteredSubtypesEmpty() throws Exception {
 StdSubtypeResolver subtypeResolver = new StdSubtypeResolver();
 List<NamedType> registeredTypes = subtypeResolver.getRegisteredSubtypes();
 assertTrue(registeredTypes.isEmpty());
 }

 @Test
 public void testGetRegisteredSubtypesNonEmpty() throws Exception {
 Class<SubclassA> subclassA = SubclassA.class;
 Class<SubclassB> subclassB = SubclassB.class;
 Class<BaseClass> baseClass = BaseClass.class;
 StdSubtypeResolver subtypeResolver = new StdSubtypeResolver();
 subtypeResolver.registerSubtypes(new NamedType(subclassA));
 subtypeResolver.registerSubtypes(new NamedType(subclassB));
 List<NamedType> registeredTypes = subtypeResolver.getRegisteredSubtypes();
 assertTrue(registeredTypes.size() == 2);
 assertTrue(registeredTypes.contains(new NamedType(subclassA)));
 assertTrue(registeredTypes.contains(new NamedType(subclassB)));
 }
}

class BaseClass {
}

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type")
class SubclassA {
 @JsonTypeName("SubclassA")
 private String type;
}

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type")
class SubclassB {
 @JsonTypeName("SubclassB")
 private String type;
}