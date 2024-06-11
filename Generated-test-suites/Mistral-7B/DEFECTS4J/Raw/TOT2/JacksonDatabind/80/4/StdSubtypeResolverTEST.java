import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.MapperConfig;
import com.fasterxml.jackson.databind.cfg.MapperConfig.DefaultConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class StdSubtypeResolverTest {

 @InjectMocks
 private StdSubtypeResolver resolver;

 @Test
 public void testRegisterSubtypesByClass() {
 JavaType javaType = Mockito.mock(JavaType.class);
 Class<?> subtype1 = Mockito.mock(Class.class);
 Class<?> subtype2 = Mockito.mock(Class.class);

 resolver.registerSubtypes(subtype1, subtype2);
 resolver.registerSubtypes(javaType);

 AnnotationIntrospector annotationIntrospector = Mockito.mock(AnnotationIntrospector.class);
 MapperConfig<?> config = new DefaultConfig();
 config.setAnnotationIntrospector(annotationIntrospector);

 AnnotatedClass baseType = Mockito.mock(AnnotatedClass.class);
 AnnotatedMember property = Mockito.mock(AnnotatedMember.class);

 Mockito.when(annotationIntrospector.findSubtypes(property)).thenReturn(null);
 Mockito.when(annotationIntrospector.findSubtypes(baseType)).thenReturn(null);

 Collection<NamedType> result = resolver.collectAndResolveSubtypesByClass(config, property, javaType);

 assertTrue(result.size() > 0);
 assertEquals(subtype1, result.iterator().next().getType());
 }

 @Test
 public void testRegisterSubtypesByName() {
 Class<?> superClass = Mockito.mock(Class.class);
 Class<?> subtype1 = Mockito.mock(Class.class);
 Class<?> subtype2 = Mockito.mock(Class.class);

 resolver.registerSubtypes(new NamedType(subtype1, "name1"));
 resolver.registerSubtypes(new NamedType(subtype2, "name2"));
 resolver.registerSubtypes(superClass);

 AnnotationIntrospector annotationIntrospector = Mockito.mock(AnnotationIntrospector.class);
 MapperConfig<?> config = new DefaultConfig();
 config.setAnnotationIntrospector(annotationIntrospector);

 AnnotatedClass baseType = Mockito.mock(AnnotatedClass.class);
 AnnotatedMember property = Mockito.mock(AnnotatedMember.class);
 AnnotatedClass subtype1Class = Mockito.mock(AnnotatedClass.class);
 AnnotatedClass subtype2Class = Mockito.mock(AnnotatedClass.class);

 Mockito.when(annotationIntrospector.findSubtypes(property)).thenReturn(null);
 Mockito.when(annotationIntrospector.findSubtypes(baseType)).thenReturn(null);
 Mockito.when(annotationIntrospector.findSubtypes(subtype1Class)).thenReturn(new NamedType[]{new NamedType(subtype1, "name1")});
 Mockito.when(annotationIntrospector.findSubtypes(subtype2Class)).thenReturn(new NamedType[]{new NamedType(subtype2, "name2")});

 Collection<NamedType> result = resolver.collectAndResolveSubtypesByClass(config, property, javaType);

 assertTrue(result.size() > 0);
 assertTrue(result.contains(new NamedType(subtype1, "name1")));
 assertTrue(result.contains(new NamedType(subtype2, "name2")));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRegisterSubtypesNullTypes() {
 JavaType javaType = Mockito.mock(JavaType.class);

 resolver.registerSubtypes(null);
 resolver.registerSubtypes(javaType);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRegisterSubtypesEmptyTypes() {
 JavaType javaType = Mockito.mock(JavaType.class);

 resolver.registerSubtypes(new NamedType[]{});
 resolver.registerSubtypes(javaType);
 }

 @Test
 public void testCollectAndResolveSubtypesByClassWithNullBaseType() {
 AnnotatedMember property = Mockito.mock(AnnotatedMember.class);
 JavaType baseType = null;

 Collection<NamedType> result = resolver.collectAndResolveSubtypesByClass(Mockito.mock(MapperConfig.class), property, baseType);
 assertEquals(0, result.size());
 }

 @Test
 public void testCollectAndResolveSubtypesByClass() {
 AnnotatedMember property = Mockito.mock(AnnotatedMember.class);
 JavaType baseType = Mockito.mock(JavaType.class);

 Collection<NamedType> result = resolver.collectAndResolveSubtypesByClass(Mockito.mock(MapperConfig.class), property, baseType);
 assertEquals(0, result.size());
 }

 @Test
 public void testCollectAndResolveSubtypesByClassWithRegisteredSubtypes() {
 JavaType baseType = Mockito.mock(JavaType.class);
 Class<?> subtype1 = Mockito.mock(Class.class);
 Class<?> subtype2 = Mockito.mock(Class.class);

 resolver.registerSubtypes(new NamedType[]{new NamedType(subtype1, "name1"), new NamedType(subtype2, "name2")});
 AnnotatedMember property = Mockito.mock(AnnotatedMember.class);
 AnnotatedClass subtype1Class = Mockito.mock(AnnotatedClass.class);
 AnnotatedClass subtype2Class = Mockito.mock(AnnotatedClass.class);

 Mockito.when(annotationIntrospector.findSubtypes(property)).thenReturn(null);
 Mockito.when(annotationIntrospector.findSubtypes(baseType)).thenReturn(null);
 Mockito.when(annotationIntrospector.findSubtypes(subtype1Class)).thenReturn(new NamedType[]{new NamedType(subtype1, "name1")});
 Mockito.when(annotationIntrospector.findSubtypes(subtype2Class)).thenReturn(new NamedType[]{new NamedType(subtype2, "name2")});

 Collection<NamedType> result = resolver.collectAndResolveSubtypesByClass(Mockito.mock(MapperConfig.class), property, baseType);

 assertTrue(result.size() > 0);
 assertTrue(result.contains(new NamedType(subtype1, "name1")));
 assertTrue(result.contains(new NamedType(subtype2, "name2")));
 }

 @Test
 public void testCollectAndResolveSubtypesByClassWithNullAnnotatedClass() {
 JavaType baseType = Mockito.mock(JavaType.class);
 AnnotatedClass annotatedClass = null;

 Collection<NamedType> result = resolver.collectAndResolveSubtypesByClass(Mockito.mock(MapperConfig.class), annotatedClass, baseType);
 assertEquals(0, result.size());
 }

 @Test
 public void testCollectAndResolveSubtypesByClassWithEmptyAnnotatedClass() {
 JavaType baseType = Mockito.mock(JavaType.class);
 AnnotatedClass annotatedClass = Mockito.mock(AnnotatedClass.class);

 Collection<NamedType> result = resolver.collectAndResolveSubtypesByClass(Mockito.mock(MapperConfig.class), annotatedClass, baseType);
 assertEquals(0, result.size());
 }

 @Test
 public void testCollectAndResolveSubtypesByClassWithNoSubtypes() {
 JavaType baseType = Mockito.mock(JavaType.class);
 AnnotatedClass annotatedClass = Mockito.mock(AnnotatedClass.class);

 Collection<NamedType> result = resolver.collectAndResolveSubtypesByClass(Mockito.mock(MapperConfig.class), annotatedClass, baseType);
 assertEquals(0, result.size());
 }

 @Test
 public void testCollectAndResolveSubtypesByClassWithRegisteredAndAnnotatedSubtypes() {
 JavaType baseType = Mockito.mock(JavaType.class);
 Class<?> subtype1 = Mockito.mock(Class.class);
 Class<?> subtype2 = Mockito.mock(Class.class);
 AnnotatedClass subtype1Class = Mockito.mock(AnnotatedClass.class);
 AnnotatedClass subtype2Class = Mockito.mock(AnnotatedClass.class);

 resolver.registerSubtypes(new NamedType[]{new NamedType(subtype1, "name1"), new NamedType(subtype2, "name2")});

 Mockito.when(annotationIntrospector.findSubtypes(property)).thenReturn(null);
 Mockito.when(annotationIntrospector.findSubtypes(baseType)).thenReturn(null);
 Mockito.when(annotationIntrospector.findSubtypes(subtype1Class)).thenReturn(new NamedType[]{new NamedType(subtype1, "name1")});
 Mockito.when(annotationIntrospector.findSubtypes(subtype2Class)).thenReturn(new NamedType[]{new NamedType(subtype2, "name2")});

 Collection<NamedType> result = resolver.collectAndResolveSubtypesByClass(Mockito.mock(MapperConfig.class), property, baseType);

 assertTrue(result.size() > 0);
 assertTrue(result.containsAll(Arrays.asList(new NamedType(subtype1, "name1"), new NamedType(subtype2, "name2"))));
 }

 @Test
 public void testCollectAndResolveSubtypesByTypeId() {
 JavaType baseType = Mockito.mock(JavaType.class);
 AnnotatedMember property = Mockito.mock(AnnotatedMember.class);

 Collection<NamedType> result = resolver.collectAndResolveSubtypesByTypeId(Mockito.mock(MapperConfig.class), property, baseType);
 assertEquals(0, result.size());
 }

 @Test
 public void testCollectAndResolveSubtypesByTypeIdWithNullAnnotatedMember() {
 JavaType baseType = Mockito.mock(JavaType.class);
 AnnotatedMember property = null;

 Collection<NamedType> result = resolver.collectAndResolveSubtypesByTypeId(Mockito.mock(MapperConfig.class), property, baseType);
 assertEquals(0, result.size());
 }

 @Test
 public void testCollectAndResolveSubtypesByTypeIdWithEmptyAnnotatedMember() {
 JavaType baseType = Mockito.mock(JavaType.class);
 AnnotatedMember property = Mockito.mock(AnnotatedMember.class);

 Collection<NamedType> result = resolver.collectAndResolveSubtypesByTypeId(Mockito.mock(MapperConfig.class), property, baseType);
 assertEquals(0, result.size());
 }

 @Test
 public void testCollectAndResolveSubtypesByTypeIdWithNoSubtypes() {
 JavaType baseType = Mockito.mock(JavaType.class);
 AnnotatedMember property = Mockito.mock(AnnotatedMember.class);
 AnnotatedClass annotatedClass = Mockito.mock(AnnotatedClass.class);

 Collection<NamedType> result = resolver.collectAndResolveSubtypesByTypeId(Mockito.mock(MapperConfig.class), property, baseType);
 assertEquals(0, result.size());
 }

 @Test
 public void testCollectAndResolveSubtypesByTypeIdWithRegisteredSubtypes() {
 JavaType baseType = Mockito.mock(JavaType.class);
 Class<?> subtype1 = Mockito.mock(Class.class);
 Class<?> subtype2 = Mockito.mock(Class.class);
 AnnotatedClass subtype1Class = Mockito.mock(AnnotatedClass.class);
 AnnotatedClass subtype2Class = Mockito.mock(AnnotatedClass.class);

 resolver.registerSubtypes(new NamedType[]{new NamedType(subtype1, "name1"), new NamedType(subtype2, "name2")});
 AnnotatedMember property = Mockito.mock(AnnotatedMember.class);
 AnnotatedClass annotatedClass = Mockito.mock(AnnotatedClass.class);

 Mockito.when(annotationIntrospector.findSubtypes(property)).thenReturn(null);
 Mockito.when(annotationIntrospector.findSubtypes(baseType)).thenReturn(null);
 Mockito.when(annotationIntrospector.findSubtypes(subtype1Class)).thenReturn(new NamedType[]{new NamedType(subtype1, "name1")});
 Mockito.when(annotationIntrospector.findSubtypes(subtype2Class)).thenReturn(new NamedType[]{new NamedType(subtype2, "name2")});

 Collection<NamedType> result = resolver.collectAndResolveSubtypesByTypeId(Mockito.mock(MapperConfig.class), property, baseType);

 assertTrue(result.size() > 0);
 assertTrue(result.containsAll(Arrays.asList(new NamedType(subtype1, "name1"), new NamedType(subtype2, "name2"))));
 }

 @Test
 public void testCollectAndResolveSubtypesByTypeIdWithRegisteredSubtypesAndAnnotatedSubtypes() {
 JavaType baseType = Mockito.mock(JavaType.class);
 Class<?> subtype1 = Mockito.mock(Class.class);
 Class<?> subtype2 = Mockito.mock(Class.class);
 AnnotatedClass subtype1Class = Mockito.mock(AnnotatedClass.class);
 AnnotatedClass subtype2Class = Mockito.mock(AnnotatedClass.class);

 resolver.registerSubtypes(new NamedType[]{new NamedType(subtype1, "name1"), new NamedType(subtype2, "name2")});
 AnnotatedMember property = Mockito.mock(AnnotatedMember.class);
 AnnotatedClass annotatedClass = Mockito.mock(AnnotatedClass.class);
 AnnotatedClass subtype1ClassAnnotated = Mockito.mock(AnnotatedClass.class);
 AnnotatedClass subtype2ClassAnnotated = Mockito.mock(AnnotatedClass.class);

 Mockito.when(annotationIntrospector.findSubtypes(property)).thenReturn(null);
 Mockito.when(annotationIntrospector.findSubtypes(baseType)).thenReturn(null);
 Mockito.when(annotationIntrospector.findSubtypes(subtype1Class)).thenReturn(new NamedType[]{new NamedType(subtype1, "name1")});
 Mockito.when(annotationIntrospector.findSubtypes(subtype2Class)).thenReturn(new NamedType[]{new NamedType(subtype2, "name2")});
 Mockito.when(annotationIntrospector.findSubtypes(subtype1ClassAnnotated)).thenReturn(new NamedType[]{new NamedType(subtype1, "name1SubtypeAnnotation")});
 Mockito.when(annotationIntrospector.findSubtypes(subtype2ClassAnnotated)).thenReturn(new NamedType[]{new NamedType(subtype2, "name2SubtypeAnnotation")});

 Collection<NamedType> result = resolver.collectAndResolveSubtypesByTypeId(Mockito.mock(MapperConfig.class), property, baseType);

 assertTrue(result.size() > 0);
 assertTrue(result.containsAll(Arrays.asList(new NamedType(subtype1, "name1"), new NamedType(subtype2, "name2"), new NamedType(subtype1, "name1SubtypeAnnotation"), new NamedType(subtype2, "name2SubtypeAnnotation"))));
 }

}