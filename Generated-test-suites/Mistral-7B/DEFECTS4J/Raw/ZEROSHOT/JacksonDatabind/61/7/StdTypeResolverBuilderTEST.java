import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.type.ClassType;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.jsontype.ClassIdResolverBase;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.AsArraySerializerBase;
import com.fasterxml.jackson.databind.ser.std.AsExternalTypeSerializer;
import com.fasterxml.jackson.databind.ser.std.AsPropertySerializer;
import com.fasterxml.jackson.databind.ser.std.AsWrapperSerializer;
import com.fasterxml.jackson.databind.deser.std.AsArrayDeserializer;
import com.fasterxml.jackson.databind.deser.std.AsExternalTypeDeserializer;
import com.fasterxml.jackson.databind.deser.std.AsPropertyDeserializer;
import com.fasterxml.jackson.databind.deser.std.AsWrapperDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class StdTypeResolverBuilderTest {

 @Before
 public void setup() {
 ObjectMapper mapper = new ObjectMapper();
 }

 @After
 public void teardown() {
 // no cleanup needed
 }

 @Test
 public void testStdTypeResolverBuilderDefaultConfiguration() {
 StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
 TypeResolverBuilder<StdTypeResolverBuilder> resolverBuilder = builder;
 ObjectMapper mapper = new ObjectMapper();
 mapper.configure(MapperConfig.Feature.AUTO_DETECT_TYPES, true);

 TypeResolver<?> typeResolver = resolverBuilder.build(mapper.getConfig());
 assertNotNull(typeResolver);
 }

 @Test
 public void testStdTypeResolverBuilderBasicConfiguration() {
 StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
 TypeResolverBuilder<StdTypeResolverBuilder> resolverBuilder = builder;
 ObjectMapper mapper = new ObjectMapper();
 mapper.configure(MapperConfig.Feature.AUTO_DETECT_TYPES, true);

 // Set custom idType
 JsonTypeInfo.Id idType = JsonTypeInfo.Id.CLASS;
 resolverBuilder.init(idType, null);

 TypeResolver<?> typeResolver = resolverBuilder.build(mapper.getConfig());
 assertNotNull(typeResolver);
 assertEquals(idType, typeResolver.getIdType());
 }

 @Test
 public void testStdTypeResolverBuilderIncludeAsWrapperArray() {
 StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
 TypeResolverBuilder<StdTypeResolverBuilder> resolverBuilder = builder;
 ObjectMapper mapper = new ObjectMapper();
 mapper.configure(MapperConfig.Feature.AUTO_DETECT_TYPES, true);

 // Set includeAs and custom idType
 JsonTypeInfo.Id idType = JsonTypeInfo.Id.CLASS;
 JsonTypeInfo.As includeAs = JsonTypeInfo.As.WRAPPER_ARRAY;
 resolverBuilder.init(idType, null);
 resolverBuilder.inclusion(includeAs);

 TypeSerializer typeSerializer = resolverBuilder.buildTypeSerializer(mapper.getConfig(), Object.class, null);
 assertNotNull(typeSerializer);
 assertInstanceOf(AsArrayTypeSerializer.class, typeSerializer);
 }

 @Test
 public void testStdTypeResolverBuilderIncludeAsProperty() {
 StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
 TypeResolverBuilder<StdTypeResolverBuilder> resolverBuilder = builder;
 ObjectMapper mapper = new ObjectMapper();
 mapper.configure(MapperConfig.Feature.AUTO_DETECT_TYPES, true);

 // Set includeAs and custom idType
 JsonTypeInfo.Id idType = JsonTypeInfo.Id.CLASS;
 JsonTypeInfo.As includeAs = JsonTypeInfo.As.PROPERTY;
 String typeProperty = "type";
 resolverBuilder.init(idType, null);
 resolverBuilder.inclusion(includeAs);
 resolverBuilder.typeProperty(typeProperty);

 TypeDeserializer typeDeserializer = resolverBuilder.buildTypeDeserializer(mapper.getConfig(), Object.class, null);
 assertNotNull(typeDeserializer);
 assertInstanceOf(AsPropertyTypeDeserializer.class, typeDeserializer);
 }

 @Test
 public void testStdTypeResolverBuilderIncludeAsWrapperObject() {
 StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
 TypeResolverBuilder<StdTypeResolverBuilder> resolverBuilder = builder;
 ObjectMapper mapper = new ObjectMapper();
 mapper.configure(MapperConfig.Feature.AUTO_DETECT_TYPES, true);

 // Set includeAs and custom idType
 JsonTypeInfo.Id idType = JsonTypeInfo.Id.CLASS;
 JsonTypeInfo.As includeAs = JsonTypeInfo.As.WRAPPER_OBJECT;
 resolverBuilder.init(idType, null);
 resolverBuilder.inclusion(includeAs);

 TypeSerializer typeSerializer = resolverBuilder.buildTypeSerializer(mapper.getConfig(), Object.class, null);
 assertNotNull(typeSerializer);
 assertInstanceOf(AsWrapperTypeSerializer.class, typeSerializer);
 }

 @Test
 public void testStdTypeResolverBuilderIncludeAsExternalProperty() {
 StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
 TypeResolverBuilder<StdTypeResolverBuilder> resolverBuilder = builder;
 ObjectMapper mapper = new ObjectMapper();
 mapper.configure(MapperConfig.Feature.AUTO_DETECT_TYPES, true);

 // Set includeAs and custom idType
 JsonTypeInfo.Id idType = JsonTypeInfo.Id.CLASS;
 JsonTypeInfo.As includeAs = JsonTypeInfo.As.EXTERNAL_PROPERTY;
 String typeProperty = "type";
 resolverBuilder.init(idType, null);
 resolverBuilder.inclusion(includeAs);
 resolverBuilder.typeProperty(typeProperty);

 TypeDeserializer typeDeserializer = resolverBuilder.buildTypeDeserializer(mapper.getConfig(), Object.class, null);
 assertNotNull(typeDeserializer);
 assertInstanceOf(AsExternalTypeDeserializer.class, typeDeserializer);
 }

 @Test
 public void testStdTypeResolverBuilderIncludeAsExistingProperty() {
 StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
 TypeResolverBuilder<StdTypeResolverBuilder> resolverBuilder = builder;
 ObjectMapper mapper = new ObjectMapper();
 mapper.configure(MapperConfig.Feature.AUTO_DETECT_TYPES, true);

 // Set includeAs and custom idType
 JsonTypeInfo.Id idType = JsonTypeInfo.Id.CLASS;
 JsonTypeInfo.As includeAs = JsonTypeInfo.As.EXTERNAL_PROPERTY;
 String typeProperty = "type";
 resolverBuilder.init(idType, null);
 resolverBuilder.inclusion(includeAs);
 resolverBuilder.typeProperty(typeProperty);

 TypeDeserializer typeDeserializer = resolverBuilder.buildTypeDeserializer(mapper.getConfig(), Object.class, null);
 assertNotNull(typeDeserializer);
 assertInstanceOf(AsExistingPropertyTypeDeserializer.class, typeDeserializer);
 }

 @Test
 public void testStdTypeResolverBuilderDefaultImpl() {
 StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
 TypeResolverBuilder<StdTypeResolverBuilder> resolverBuilder = builder;
 ObjectMapper mapper = new ObjectMapper();
 mapper.configure(MapperConfig.Feature.AUTO_DETECT_TYPES, true);

 // Set defaultImpl and custom idType
 JsonTypeInfo.Id idType = JsonTypeInfo.Id.CLASS;
 Class<String> defaultImpl = String.class;
 resolverBuilder.init(idType, null);
 resolverBuilder.defaultImpl(defaultImpl);

 TypeSerializer typeSerializer = resolverBuilder.buildTypeSerializer(mapper.getConfig(), Object.class, null);
 assertNotNull(typeSerializer);
 assertNull(typeSerializer.getDefaultImpl());

 TypeDeserializer typeDeserializer = resolverBuilder.buildTypeDeserializer(mapper.getConfig(), Object.class, null);
 assertNotNull(typeDeserializer);
 assertNull(typeDeserializer.getDefaultImpl());
 }

 @Test
 public void testStdTypeResolverBuilderTypeIdVisible() {
 StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
 TypeResolverBuilder<StdTypeResolverBuilder> resolverBuilder = builder;
 ObjectMapper mapper = new ObjectMapper();
 mapper.configure(MapperConfig.Feature.AUTO_DETECT_TYPES, true);

 // Set typeIdVisible and custom idType
 JsonTypeInfo.Id idType = JsonTypeInfo.Id.CLASS;
 resolverBuilder.init(idType, null);
 resolverBuilder.typeIdVisibility(true);

 TypeSerializer typeSerializer = resolverBuilder.buildTypeSerializer(mapper.getConfig(), Object.class, null);
 assertNotNull(typeSerializer);
 assertTrue(typeSerializer.isTypeIdVisible());

 TypeDeserializer typeDeserializer = resolverBuilder.buildTypeDeserializer(mapper.getConfig(), Object.class, null);
 assertNotNull(typeDeserializer);
 assertTrue(typeDeserializer.isTypeIdVisible());
 }

 @Test
 public void testStdTypeResolverBuilderCustomIdResolver() {
 StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
 TypeResolverBuilder<StdTypeResolverBuilder> resolverBuilder = builder;
 ObjectMapper mapper = new ObjectMapper();
 mapper.configure(MapperConfig.Feature.AUTO_DETECT_TYPES, true);

 // Set customIdResolver and custom idType
 JsonTypeInfo.Id idType = JsonTypeInfo.Id.CLASS;
 ClassIdResolverBase customIdResolver = new ClassNameIdResolver(String.class, mapper.getTypeFactory());
 resolverBuilder.init(idType, customIdResolver);

 TypeSerializer typeSerializer = resolverBuilder.buildTypeSerializer(mapper.getConfig(), Object.class, null);
 assertNotNull(typeSerializer);
 assertSame(customIdResolver, typeSerializer.getIdResolver());

 TypeDeserializer typeDeserializer = resolverBuilder.buildTypeDeserializer(mapper.getConfig(), Object.class, null);
 assertNotNull(typeDeserializer);
 assertSame(customIdResolver, typeDeserializer.getIdResolver());
 }
}