import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.type.ClassType;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.databind.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.databind.cfg.MapperFactory;
import com.fasterxml.jackson.databind.jsontype.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdTypeResolverBuilderTest {

 private StdTypeResolverBuilder builder;
 private ObjectMapper mapper;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 builder = new StdTypeResolverBuilder();
 }

 @Test
 public void testDefaultConstruction() {
 StdTypeResolverBuilder stdTypeResolverBuilder = new StdTypeResolverBuilder();
 assertNull(stdTypeResolverBuilder._idType);
 assertNull(stdTypeResolverBuilder._includeAs);
 assertNull(stdTypeResolverBuilder._typeProperty);
 assertFalse(stdTypeResolverBuilder._typeIdVisible);
 assertNull(stdTypeResolverBuilder._defaultImpl);
 assertNull(stdTypeResolverBuilder._customIdResolver);
 }

 @Test
 public void testNoTypeInfoBuilder() {
 StdTypeResolverBuilder stdTypeResolverBuilder = StdTypeResolverBuilder.noTypeInfoBuilder();
 assertNull(stdTypeResolverBuilder._idType);
 assertNull(stdTypeResolverBuilder._includeAs);
 assertNull(stdTypeResolverBuilder._typeProperty);
 assertFalse(stdTypeResolverBuilder._typeIdVisible);
 assertNull(stdTypeResolverBuilder._defaultImpl);
 assertNull(stdTypeResolverBuilder._customIdResolver);
 }

 @Test
 public void testInit() {
 builder.init(Id.CLASS, null);
 assertEquals(Id.CLASS, builder._idType);
 assertNull(builder._includeAs);
 assertNull(builder._typeProperty);
 assertFalse(builder._typeIdVisible);
 assertNull(builder._defaultImpl);
 assertNull(builder._customIdResolver);

 builder.init(Id.MINIMAL_CLASS, null);
 assertEquals(Id.MINIMAL_CLASS, builder._idType);
 assertNull(builder._includeAs);
 assertNull(builder._typeProperty);
 assertFalse(builder._typeIdVisible);
 assertNull(builder._defaultImpl);
 assertNull(builder._customIdResolver);

 builder.init(Id.NAME, null);
 assertEquals(Id.NAME, builder._idType);
 assertNull(builder._includeAs);
 assertNull(builder._typeProperty);
 assertFalse(builder._typeIdVisible);
 assertNull(builder._defaultImpl);
 assertNull(builder._customIdResolver);

 builder.init(Id.CUSTOM, new MyTypeIdResolver());
 assertEquals(Id.CUSTOM, builder._idType);
 assertNull(builder._includeAs);
 assertNull(builder._typeProperty);
 assertFalse(builder._typeIdVisible);
 assertNotNull(builder._customIdResolver);
 }

 @Test
 public void testTypeSerializer() throws Exception {
 builder.init(Id.CLASS, null);
 JavaType javaType = mapper.getTypeFactory().constructType(String.class);
 Collection<NamedType> namedTypes = mapper.getTypeFactory().constructValueType(List.class, String.class);
 TypeSerializer typeSerializer = builder.buildTypeSerializer(mapper.getSerializationConfig(), javaType, namedTypes);
 assertNotNull(typeSerializer);
 }

 @Test(expected = IllegalStateException.class)
 public void testBuildTypeSerializerWithNullIdType() throws Exception {
 builder.init(null, null);
 builder.buildTypeSerializer(mapper.getSerializationConfig(), null, null);
 fail("Should not reach this point.");
 }

 @Test(expected = IllegalStateException.class)
 public void testBuildTypeSerializerWithNullBaseType() throws Exception {
 builder.init(Id.CLASS, null);
 builder.buildTypeSerializer(mapper.getSerializationConfig(), null, null);
 fail("Should not reach this point.");
 }

 @Test(expected = IllegalStateException.class)
 public void testBuildTypeSerializerWithNullSubtypes() throws Exception {
 builder.init(Id.CLASS, null);
 builder.buildTypeSerializer(mapper.getSerializationConfig(), null, null);
 fail("Should not reach this point.");
 }

 @Test(expected = IllegalStateException.class)
 public void testBuildTypeSerializerWithNullIdResolver() throws Exception {
 builder.init(Id.CLASS, null);
 builder.buildTypeSerializer(mapper.getSerializationConfig(), null, null, true, false);
 fail("Should not reach this point.");
 }

 @Test
 public void testTypeDeserializer() throws Exception {
 builder.init(Id.CLASS, null);
 JavaType javaType = mapper.getTypeFactory().constructType(String.class);
 Collection<NamedType> namedTypes = mapper.getTypeFactory().constructValueType(List.class, String.class);
 TypeDeserializer typeDeserializer = builder.buildTypeDeserializer(mapper.getDeserializationConfig(), javaType, namedTypes);
 assertNotNull(typeDeserializer);
 }

 @Test(expected = IllegalStateException.class)
 public void testBuildTypeDeserializerWithNullIdType() throws Exception {
 builder.init(null, null);
 builder.buildTypeDeserializer(mapper.getDeserializationConfig(), null, null);
 fail("Should not reach this point.");
 }

 @Test
 public void testBuildTypeDeserializerWithNullBaseType() throws Exception {
 builder.init(Id.CLASS, null);
 builder.buildTypeDeserializer(mapper.getDeserializationConfig(), null, null);
 fail("Should not reach this point.");
 }

 @Test
 public void testBuildTypeDeserializerWithNullSubtypes() throws Exception {
 builder.init(Id.CLASS, null);
 builder.buildTypeDeserializer(mapper.getDeserializationConfig(), null, null);
 fail("Should not reach this point.");
 }

 @Test
 public void testBuildTypeDeserializerWithNullIdResolver() throws Exception {
 builder.init(Id.CLASS, null);
 builder.buildTypeDeserializer(mapper.getDeserializationConfig(), null, null, false, true);
 fail("Should not reach this point.");
 }

 @Test
 public void testInclusion() {
 builder.inclusion(JsonTypeInfo.As.WRAPPER_ARRAY);
 assertEquals(JsonTypeInfo.As.WRAPPER_ARRAY, builder._includeAs);
 }

 @Test
 public void testTypeProperty() {
 builder.typeProperty("myProperty");
 assertEquals("myProperty", builder.getTypeProperty());
 }

 @Test
 public void testDefaultImpl() {
 builder.defaultImpl(String.class);
 assertEquals(String.class, builder.getDefaultImpl());
 }

 @Test
 public void testTypeIdVisibility() {
 builder.typeIdVisibility(true);
 assertTrue(builder.isTypeIdVisible());
 }

 @Test
 public void testGetDefaultImpl() {
 builder.defaultImpl(String.class);
 assertEquals(String.class, builder.getDefaultImpl());
 }

 @Test
 public void testIsTypeIdVisible() {
 builder.typeIdVisibility(true);
 assertTrue(builder.isTypeIdVisible());
 }

 @Test
 public void testIdResolver() throws Exception {
 MapperFactory mapperFactory = mapper.getFactory();
 ObjectReader reader = mapper.readerForMap();
 TypeIdResolver idResolver = builder.idResolver(mapperFactory, new ClassType(String[].class), null, true, false);
 assertEquals(TypeIdResolver.ClassIdResolver.instance, idResolver);
 }
}

class MyTypeIdResolver implements TypeIdResolver {
 @Override
 public String idFromValue(Object value) {
 return "customId";
 }
}