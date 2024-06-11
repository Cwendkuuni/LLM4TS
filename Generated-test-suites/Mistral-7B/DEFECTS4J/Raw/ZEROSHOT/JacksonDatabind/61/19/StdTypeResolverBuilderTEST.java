import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.type.ClassType;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.cfg.MapperFactory;
import com.fasterxml.jackson.databind.jsontype.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class StdTypeResolverBuilderTest {

 private StdTypeResolverBuilder builder;
 private ObjectMapper mapper;

 @Before
 public void setUp() {
 mapper = new MapperFactory(new AnnotationIntrospector()).findValueMapper(null);
 builder = new StdTypeResolverBuilder();
 }

 @Test
 public void testInit() {
 builder.init(JsonTypeInfo.Id.CLASS, null);
 assertEquals(JsonTypeInfo.Id.CLASS, builder._idType);
 assertNull(builder._customIdResolver);
 assertEquals("", builder._typeProperty);
 }

 @Test
 public void testInitWithCustomIdResolver() {
 TypeIdResolver customIdResolver = new SimpleTypeIdResolver();
 builder.init(JsonTypeInfo.Id.CLASS, customIdResolver);
 assertEquals(JsonTypeInfo.Id.CLASS, builder._idType);
 assertEquals(customIdResolver, builder._customIdResolver);
 assertEquals("", builder._typeProperty);
 }

 @Test
 public void testNoTypeInfoBuilder() {
 StdTypeResolverBuilder noTypeInfoBuilder = StdTypeResolverBuilder.noTypeInfoBuilder();
 assertNull(noTypeInfoBuilder._idType);
 assertNull(noTypeInfoBuilder._customIdResolver);
 assertEquals("", noTypeInfoBuilder._typeProperty);
 }

 @Test
 public void testBuildTypeSerializer() throws JsonMappingException {
 JavaType javaType = mapper.getTypeFactory().constructType(String.class);
 Collection<NamedType> namedTypes = new java.util.ArrayList<>();
 namedTypes.add(new ClassType(Map.class));
 StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
 builder.init(JsonTypeInfo.Id.PROPERTY, null);
 TypeSerializer result = builder.buildTypeSerializer(mapper.getSerializationConfig(), javaType, namedTypes);
 assertNull(result);

 builder = new StdTypeResolverBuilder();
 builder.init(JsonTypeInfo.Id.PROPERTY, null);
 builder.inclusion(JsonTypeInfo.As.PROPERTY);
 result = builder.buildTypeSerializer(mapper.getSerializationConfig(), javaType, namedTypes);
 assertEquals(AsPropertyTypeSerializer.class, result.getClass());
 }

 @Test
 public void testBuildTypeDeserializer() throws JsonMappingException, JsonParseException {
 JavaType javaType = mapper.getTypeFactory().constructType(String.class);
 Collection<NamedType> namedTypes = new java.util.ArrayList<>();
 namedTypes.add(new ClassType(List.class));
 StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
 builder.init(JsonTypeInfo.Id.PROPERTY, null);
 TypeDeserializer result = builder.buildTypeDeserializer(mapper.getDeserializationConfig(), javaType, namedTypes);
 assertNull(result);

 builder = new StdTypeResolverBuilder();
 builder.init(JsonTypeInfo.Id.PROPERTY, null);
 builder.inclusion(JsonTypeInfo.As.PROPERTY);
 result = builder.buildTypeDeserializer(mapper.getDeserializationConfig(), javaType, namedTypes);
 assertEquals(AsPropertyTypeDeserializer.class, result.getClass());
 }

 @Test
 public void testTypeProperty() {
 builder.init(JsonTypeInfo.Id.PROPERTY, null);
 builder.typeProperty("testPropertyName");
 assertEquals("testPropertyName", builder._typeProperty);
 }

 @Test
 public void testDefaultImpl() {
 builder.init(JsonTypeInfo.Id.PROPERTY, null);
 builder.defaultImpl(String.class);
 assertEquals(String.class, builder._defaultImpl);
 }

 @Test
 public void testTypeIdVisibility() {
 builder.init(JsonTypeInfo.Id.PROPERTY, null);
 builder.typeIdVisibility(true);
 assertEquals(true, builder._typeIdVisible);
 }
}

class SimpleTypeIdResolver extends TypeIdResolverBase {
 @Override
 public String idFromValue(Object value) {
 return "testId";
 }
}