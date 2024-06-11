import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.jsontype.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdTypeResolverBuilderTest {

 private StdTypeResolverBuilder builder;
 private MapperConfig config;

 @Before
 public void setUp() {
 config = new MapperFactory().configure(new ObjectMapper.DefaultSerializerProvider.Base(), new JsonFactory(), null);
 builder = new StdTypeResolverBuilder();
 }

 @Test
 public void testNoTypeInfoBuilder() {
 StdTypeResolverBuilder noTypeInfoBuilder = StdTypeResolverBuilder.noTypeInfoBuilder();
 assertEquals(JsonTypeInfo.Id.NONE, noTypeInfoBuilder.getIdType());
 assertNull(noTypeInfoBuilder.getIdResolver());
 }

 @Test
 public void testInit() {
 JsonTypeInfo.Id idType = JsonTypeInfo.Id.CLASS;
 builder.init(idType, null);
 assertEquals(idType, builder.getIdType());
 assertNull(builder.getIdResolver());

 JsonTypeInfo.As includeAs = JsonTypeInfo.As.PROPERTY;
 builder.inclusion(includeAs);
 assertEquals(includeAs, builder.getIncludeAs());

 String typeProperty = "typeProperty";
 builder.typeProperty(typeProperty);
 assertEquals(typeProperty, builder.getTypeProperty());
 }

 @Test
 public void testBuildTypeSerializer() {
 Class<?> baseType = Integer.class;
 Collection<NamedType> subtypes = null;
 TypeSerializer serializer = builder.buildTypeSerializer(config, baseType, subtypes);
 assertNull(serializer);

 idType = JsonTypeInfo.Id.PROPERTY;
 builder.init(idType, null);
 serializer = builder.buildTypeSerializer(config, baseType, subtypes);
 assertNotNull(serializer);
 }

 @Test(expected = IllegalStateException.class)
 public void testBuildTypeSerializerWithInvalidIdType() {
 builder.init(null, null);
 builder.buildTypeSerializer(config, baseType, subtypes);
 }

 @Test
 public void testBuildTypeDeserializer() {
 Class<?> baseType = Integer.class;
 Collection<NamedType> subtypes = null;
 TypeDeserializer deserializer = builder.buildTypeDeserializer(config, baseType, subtypes);
 assertNull(deserializer);

 idType = JsonTypeInfo.Id.PROPERTY;
 builder.init(idType, null);
 deserializer = builder.buildTypeDeserializer(config, baseType, subtypes);
 assertNotNull(deserializer);
 }

 @Test(expected = IllegalStateException.class)
 public void testBuildTypeDeserializerWithInvalidIdType() {
 builder.init(null, null);
 builder.buildTypeDeserializer(config, baseType, subtypes);
 }

 @Test
 public void testTypeIdVisibility() {
 builder.typeIdVisibility(true);
 assertTrue(builder.isTypeIdVisible());

 builder.typeIdVisibility(false);
 assertFalse(builder.isTypeIdVisible());
 }

 @Test
 public void testDefaultImpl() {
 Class<?> defaultImpl = String.class;
 builder.defaultImpl(defaultImpl);
 assertEquals(defaultImpl, builder.getDefaultImpl());
 }
}