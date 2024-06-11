import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.type.ClassType;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.jsontype.impl.AsArrayTypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.impl.AsArrayTypeSerializer;
import com.fasterxml.jackson.databind.jsontype.impl.AsExistingPropertyTypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.impl.AsExistingPropertyTypeSerializer;
import com.fasterxml.jackson.databind.jsontype.impl.AsExternalTypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.impl.AsExternalTypeSerializer;
import com.fasterxml.jackson.databind.jsontype.impl.AsPropertyTypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.impl.AsPropertyTypeSerializer;
import com.fasterxml.jackson.databind.jsontype.impl.AsWrapperTypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.impl.AsWrapperTypeSerializer;
import com.fasterxml.jackson.databind.jsontype.impl.ClassNameIdResolver;
import com.fasterxml.jackson.databind.jsontype.impl.MinimalClassNameIdResolver;
import com.fasterxml.jackson.databind.jsontype.impl.TypeIdResolver;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdTypeResolverBuilderTest {

 private StdTypeResolverBuilder builder;

 @Before
 public void setUp() {
 builder = new StdTypeResolverBuilder();
 }

 @Test
 public void testDefaultInitialization() {
 StdTypeResolverBuilder defaultBuilder = new StdTypeResolverBuilder();
 assertEquals(JsonTypeInfo.Id.NONE, defaultBuilder.getIdType());
 assertNull(defaultBuilder.getTypeProperty());
 assertFalse(defaultBuilder.isTypeIdVisible());
 assertNull(defaultBuilder.getDefaultImpl());
 assertNull(defaultBuilder._customIdResolver);
 }

 @Test
 public void testNoTypeInfoBuilderInitialization() {
 StdTypeResolverBuilder noTypeInfoBuilder = StdTypeResolverBuilder.noTypeInfoBuilder();
 assertEquals(JsonTypeInfo.Id.NONE, noTypeInfoBuilder.getIdType());
 assertNull(noTypeInfoBuilder.getTypeProperty());
 assertFalse(noTypeInfoBuilder.isTypeIdVisible());
 assertNull(noTypeInfoBuilder.getDefaultImpl());
 assertNull(noTypeInfoBuilder._customIdResolver);
 }

 @Test
 public void testInitialization() {
 JsonTypeInfo.Id idType = JsonTypeInfo.Id.CLASS;
 TypeIdResolver idRes = new MinimalClassNameIdResolver(new ClassType(String.class), null);
 builder.init(idType, idRes);
 assertEquals(idType, builder.getIdType());
 assertEquals(idRes, builder._customIdResolver);
 assertEquals(idType.getDefaultPropertyName(), builder.getTypeProperty());
 assertFalse(builder.isTypeIdVisible());
 assertNull(builder.getDefaultImpl());
 }

 @Test
 public void testBuildTypeSerializer() {
 MapperConfig<Object> config = new MapperConfig<Object>();
 JavaType baseType = new ClassType(String.class);
 Collection<NamedType> subtypes = null;
 TypeSerializer serializer = builder.buildTypeSerializer(config, baseType, subtypes);
 assertNull(serializer);

 baseType = new ClassType(List.class);
 subtypes = null;
 TypeIdResolver idResolver = new MinimalClassNameIdResolver(baseType, null);
 config.setTypeResolverBuilder(builder);
 serializer = builder.buildTypeSerializer(config, baseType, subtypes);
 assertNotNull(serializer);
 serializer.close();

 baseType = new ClassType(Map.class);
 subtypes = null;
 config.setTypeResolverBuilder(builder);
 serializer = builder.buildTypeSerializer(config, baseType, subtypes);
 assertNotNull(serializer);
 serializer.close();

 idType = JsonTypeInfo.Id.PROPERTY;
 config.setTypeResolverBuilder(builder);
 builder.init(idType, null);
 serializer = builder.buildTypeSerializer(config, baseType, subtypes);
 assertNull(serializer);

 idType = JsonTypeInfo.Id.WRAPPER_ARRAY;
 config.setTypeResolverBuilder(builder);
 builder.init(idType, null);
 serializer = builder.buildTypeSerializer(config, baseType, subtypes);
 assertInstanceOf(AsArrayTypeSerializer.class, serializer);
 serializer.close();

 idType = JsonTypeInfo.Id.WRAPPER_OBJECT;
 config.setTypeResolverBuilder(builder);
 builder.init(idType, null);
 serializer = builder.buildTypeSerializer(config, baseType, subtypes);
 assertInstanceOf(AsWrapperTypeSerializer.class, serializer);
 serializer.close();

 idType = JsonTypeInfo.Id.EXTERNAL_PROPERTY;
 config.setTypeResolverBuilder(builder);
 builder.init(idType, null);
 serializer = builder.buildTypeSerializer(config, baseType, subtypes);
 assertInstanceOf(AsExternalTypeSerializer.class, serializer);
 serializer.close();

 idType = JsonTypeInfo.Id.EXISTING_PROPERTY;
 config.setTypeResolverBuilder(builder);
 builder.init(idType, null);
 serializer = builder.buildTypeSerializer(config, baseType, subtypes);
 assertInstanceOf(AsExistingPropertyTypeSerializer.class, serializer);
 serializer.close();

 idType = JsonTypeInfo.Id.CUSTOM;
 config.setTypeResolverBuilder(builder);
 builder.init(idType, idResolver);
 serializer = builder.buildTypeSerializer(config, baseType, subtypes);
 assertInstanceOf(AsPropertyTypeSerializer.class, serializer);
 serializer.close();
}

 @Test
 public void testBuildTypeDeserializer() {
 MapperConfig<Object> config = new MapperConfig<Object>();
 JavaType baseType = new ClassType(String.class);
 Collection<NamedType> subtypes = null;
 TypeDeserializer deserializer = builder.buildTypeDeserializer(config, baseType, subtypes);
 assertNull(deserializer);

 baseType = new ClassType(List.class);
 config.setTypeResolverBuilder(builder);
 deserializer = builder.buildTypeDeserializer(config, baseType, subtypes);
 assertNotNull(deserializer);
 deserializer.close();

 baseType = new ClassType(Map.class);
 config.setTypeResolverBuilder(builder);
 deserializer = builder.buildTypeDeserializer(config, baseType, subtypes);
 assertNotNull(deserializer);
 deserializer.close();

 JsonTypeInfo.Id idType = JsonTypeInfo.Id.PROPERTY;
 config.setTypeResolverBuilder(builder);
 builder.init(idType, null);
 deserializer = builder.buildTypeDeserializer(config, baseType, subtypes);
 assertNull(deserializer);

 idType = JsonTypeInfo.Id.WRAPPER_ARRAY;
 config.setTypeResolverBuilder(builder);
 builder.init(idType, null);
 deserializer = builder.buildTypeDeserializer(config, baseType, subtypes);
 assertInstanceOf(AsArrayTypeDeserializer.class, deserializer);
 deserializer.close();

 idType = JsonTypeInfo.Id.WRAPPER_OBJECT;
 config.setTypeResolverBuilder(builder);
 builder.init(idType, null);
 deserializer = builder.buildTypeDeserializer(config, baseType, subtypes);
 assertInstanceOf(AsWrapperTypeDeserializer.class, deserializer);
 deserializer.close();

 idType = JsonTypeInfo.Id.EXTERNAL_PROPERTY;
 config.setTypeResolverBuilder(builder);
 builder.init(idType, null);
 deserializer = builder.buildTypeDeserializer(config, baseType, subtypes);
 assertInstanceOf(AsExternalTypeDeserializer.class, deserializer);
 deserializer.close();

 idType = JsonTypeInfo.Id.EXISTING_PROPERTY;
 config.setTypeResolverBuilder(builder);
 builder.init(idType, null);
 deserializer = builder.buildTypeDeserializer(config, baseType, subtypes);
 assertInstanceOf(AsExistingPropertyTypeDeserializer.class, deserializer);
 deserializer.close();

 idType = JsonTypeInfo.Id.CUSTOM;
 config.setTypeResolverBuilder(builder);
 builder.init(idType, idResolver);
 deserializer = builder.buildTypeDeserializer(config, baseType, subtypes);
 assertInstanceOf(AsPropertyTypeDeserializer.class, deserializer);
 deserializer.close();
}

 @Test
 public void testInclude() {
 builder.inclusion(JsonTypeInfo.As.PROPERTY);
 assertEquals(JsonTypeInfo.As.PROPERTY, builder.getIncludeAs());

 builder.inclusion(JsonTypeInfo.As.WRAPPER_OBJECT);
 assertEquals(JsonTypeInfo.As.WRAPPER_OBJECT, builder.getIncludeAs());

 builder.inclusion(JsonTypeInfo.As.WRAPPER_ARRAY);
 assertEquals(JsonTypeInfo.As.WRAPPER_ARRAY, builder.getIncludeAs());

 builder.inclusion(JsonTypeInfo.As.EXTERNAL_PROPERTY);
 assertEquals(JsonTypeInfo.As.EXTERNAL_PROPERTY, builder.getIncludeAs());

 builder.inclusion(JsonTypeInfo.As.EXISTING_PROPERTY);
 assertEquals(JsonTypeInfo.As.EXISTING_PROPERTY, builder.getIncludeAs());
}

 @Test
 public void testTypeProperty() {
 String typeProperty = "typeProperty";
 builder.typeProperty(typeProperty);
 assertEquals(typeProperty, builder.getTypeProperty());
}

 @Test
 public void testDefaultImpl() {
 String className = "DefaultImpl";
 try {
 builder.defaultImpl(className);
 Class<?> defaultImpl = Class.forName(className);
 assertEquals(defaultImpl, builder.getDefaultImpl());
 } catch (ClassNotFoundException e) {
 fail("Could not find class: " + className);
 }
}

 @Test
 public void testTypeIdVisibility() {
 builder.typeIdVisibility(true);
 assertTrue(builder.isTypeIdVisible());

 builder.typeIdVisibility(false);
 assertFalse(builder.isTypeIdVisible());
}

 @Test
 public void testCustomIdResolver() {
 TypeIdResolver customIdResolver = new MinimalClassNameIdResolver(new ClassType(String.class), null);
 builder.setIdResolver(customIdResolver);
 assertEquals(customIdResolver, builder._customIdResolver);
}
}