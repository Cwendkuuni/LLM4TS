import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.NoClass;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.jsontype.*;

public class StdTypeResolverBuilderTest {

 private StdTypeResolverBuilder builder;

 @Test
 public void testConstructor() {
 builder = new StdTypeResolverBuilder();
 assertNull(builder.getDefaultImpl());
 assertNull(builder.getTypeProperty());
 assertFalse(builder.isTypeIdVisible());
 }

 @Test
 public void testNoTypeInfoBuilder() {
 builder = StdTypeResolverBuilder.noTypeInfoBuilder();
 assertNull(builder.getDefaultImpl());
 assertNull(builder.getTypeProperty());
 assertFalse(builder.isTypeIdVisible());
 assertNull(builder.getCustomIdResolver());
 }

 @Test
 public void testInit() {
 builder = new StdTypeResolverBuilder();
 JsonTypeInfo.Id idType = JsonTypeInfo.Id.CLASS;
 builder.init(idType, null);
 assertEquals(idType, builder.getIdType());
 assertNull(builder.getDefaultImpl());
 assertNull(builder.getTypeProperty());
 assertFalse(builder.isTypeIdVisible());
 assertNull(builder.getCustomIdResolver());

 builder = new StdTypeResolverBuilder();
 builder.init(idType, null);
 builder.typeProperty("testProperty");
 builder.defaultImpl(String.class);
 builder.typeIdVisibility(true);
 assertEquals(idType, builder.getIdType());
 assertEquals(String.class, builder.getDefaultImpl());
 assertEquals("testProperty", builder.getTypeProperty());
 assertTrue(builder.isTypeIdVisible());
 assertNull(builder.getCustomIdResolver());
}

 @Test
 public void testBuildTypeSerializer() {
 // TODO: Implement test for buildTypeSerializer
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBuildTypeSerializerWithNullIdType() {
 builder = new StdTypeResolverBuilder();
 builder.init(null, null);
 builder.buildTypeSerializer(null, null, null);
 }

 @Test
 public void testBuildTypeDeserializer() {
 // TODO: Implement test for buildTypeDeserializer
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBuildTypeDeserializerWithNullIdType() {
 builder = new StdTypeResolverBuilder();
 builder.init(null, null);
 builder.buildTypeDeserializer(null, null, null);
 }

 @Test
 public void testInclusion() {
 builder = new StdTypeResolverBuilder();
 builder = builder.inclusion(JsonTypeInfo.As.PROPERTY);
 assertEquals(JsonTypeInfo.As.PROPERTY, builder.getIncludeAs());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInclusionWithNullIncludeAs() {
 builder = new StdTypeResolverBuilder();
 builder = builder.inclusion(null);
 }

 @Test
 public void testTypeProperty() {
 builder = new StdTypeResolverBuilder();
 builder.typeProperty("testProperty");
 assertEquals("testProperty", builder.getTypeProperty());
 }

 @Test
 public void testDefaultImpl() {
 builder = new StdTypeResolverBuilder();
 builder = builder.defaultImpl(String[].class);
 assertEquals(String[].class, builder.getDefaultImpl());
 }

 @Test
 public void testTypeIdVisibility() {
 builder = new StdTypeResolverBuilder();
 builder = builder.typeIdVisibility(true);
 assertTrue(builder.isTypeIdVisible());
 }

 @Test
 public void testGetDefaultImpl() {
 builder = new StdTypeResolverBuilder();
 builder = builder.defaultImpl(String.class);
 assertEquals(String.class, builder.getDefaultImpl());
 }

 @Test
 public void testGetTypeProperty() {
 builder = new StdTypeResolverBuilder();
 builder.typeProperty("testProperty");
 assertEquals("testProperty", builder.getTypeProperty());
 }

 @Test
 public void testIsTypeIdVisible() {
 builder = new StdTypeResolverBuilder();
 builder = builder.typeIdVisibility(true);
 assertTrue(builder.isTypeIdVisible());
 }

 @Test
 public void testGetCustomIdResolver() {
 TypeIdResolver idResolver = new MinimalClassNameIdResolver(JavaType.construct(Integer.class),
 new TypeFactory());
 builder = new StdTypeResolverBuilder();
 builder.init(JsonTypeInfo.Id.MINIMAL_CLASS, idResolver);
 assertEquals(idResolver, builder.getCustomIdResolver());
 }
}