import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.type.ClassIdType;
import com.fasterxml.jackson.core.type.TypeFactory;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.jsontype.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdTypeResolverBuilderTest {

 private StdTypeResolverBuilder builder;

 @Test
 public void testEmptyConstructor() {
 builder = new StdTypeResolverBuilder();
 assertNull(builder.getIdType());
 assertNull(builder.getIncludeAs());
 assertNull(builder.getTypeProperty());
 assertNull(builder.getDefaultImpl());
 assertNull(builder.getTypeIdResolver());
 }

 @Test
 public void testNoTypeInfoBuilder() {
 builder = StdTypeResolverBuilder.noTypeInfoBuilder();
 assertNull(builder.getIdType());
 assertNull(builder.getIncludeAs());
 assertNull(builder.getTypeProperty());
 assertNull(builder.getDefaultImpl());
 assertNull(builder.getTypeIdResolver());
 }

 @Test
 public void testInitWithNullIdType() {
 builder = new StdTypeResolverBuilder();
 try {
 builder.init(null, null);
 fail("IllegalArgumentException expected");
 } catch (IllegalArgumentException e) {
 // expected
 }
 assertNull(builder.getIdType());
 assertNull(builder.getIncludeAs());
 assertNull(builder.getTypeProperty());
 assertNull(builder.getDefaultImpl());
 assertNull(builder.getTypeIdResolver());
 }

 @Test
 public void testInitWithNullIdRes() {
 builder = new StdTypeResolverBuilder();
 builder.init(ClassIdType.ID_CLASS, null);
 assertNull(builder.getIdType());
 assertNull(builder.getIncludeAs());
 assertNull(builder.getTypeProperty());
 assertNull(builder.getDefaultImpl());
 assertNull(builder.getTypeIdResolver());
 }

 @Test
 public void testInit() {
 builder = new StdTypeResolverBuilder();
 ClassIdType idType = ClassIdType.ID_CLASS;
 TypeIdResolver idRes = new ClassNameIdResolver(null, new TypeFactory());
 builder.init(idType, idRes);
 assertEquals(idType, builder.getIdType());
 assertEquals(idRes, builder.getTypeIdResolver());
 assertNull(builder.getIncludeAs());
 assertNull(builder.getTypeProperty());
 assertNull(builder.getDefaultImpl());
 }

 @Test
 public void testBuildTypeSerializerWithNullConfig() {
 builder = new StdTypeResolverBuilder();
 builder.init(ClassIdType.ID_CLASS, null);
 JavaType baseType = TypeFactory.defaultInstance().constructType(Integer[].class);
 Collection<NamedType> subtypes = TypeFactory.defaultInstance().constructValueType(int[].class);
 try {
 builder.buildTypeSerializer(null, baseType, subtypes);
 fail("IllegalArgumentException expected");
 } catch (IllegalArgumentException e) {
 // expected
 }
 assertNull(builder.getTypeSerializer());
 }

 @Test
 public void testBuildTypeSerializerWithValidArgs() {
 builder = new StdTypeResolverBuilder();
 builder.init(ClassIdType.ID_CLASS, null);
 JavaType baseType = TypeFactory.defaultInstance().constructType(Integer[].class);
 Collection<NamedType> subtypes = TypeFactory.defaultInstance().constructValueType(int[].class);
 builder.buildTypeSerializer(new MapperConfig(new AnnotationIntrospector(), new StandardSerializerFactory()), baseType, subtypes);
 assertNotNull(builder.getTypeSerializer());
 }

 @Test
 public void testBuildTypeDeserializerWithNullConfig() {
 builder = new StdTypeResolverBuilder();
 builder.init(ClassIdType.ID_CLASS, null);
 JavaType baseType = TypeFactory.defaultInstance().constructType(Integer[].class);
 Collection<NamedType> subtypes = TypeFactory.defaultInstance().constructValueType(int[].class);
 try {
 builder.buildTypeDeserializer(null, baseType, subtypes);
 fail("IllegalArgumentException expected");
 } catch (IllegalArgumentException e) {
 // expected
 }
 assertNull(builder.getTypeDeserializer());
 }

 @Test
 public void testBuildTypeDeserializerWithValidArgs() {
 builder = new StdTypeResolverBuilder();
 builder.init(ClassIdType.ID_CLASS, null);
 JavaType baseType = TypeFactory.defaultInstance().constructType(Integer[].class);
 Collection<NamedType> subtypes = TypeFactory.defaultInstance().constructValueType(int[].class);
 builder.buildTypeDeserializer(new DeserializationConfig(new AnnotationIntrospector(), new StandardDeserializerFactory()), baseType, subtypes);
 assertNotNull(builder.getTypeDeserializer());
 }

 @Test(expected = IllegalStateException.class)
 public void testUnknownInclusionType() {
 builder = new StdTypeResolverBuilder();
 builder.init(ClassIdType.ID_CLASS, null);
 builder.buildTypeSerializer(new MapperConfig(new AnnotationIntrospector(), new StandardSerializerFactory()), Integer[].class, null);
 }
}