import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.type.ClassType;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.NoClass;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.jsontype.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdTypeResolverBuilderTest {

 private StdTypeResolverBuilder builder;

 @Before
 public void setup() {
 builder = new StdTypeResolverBuilder();
 }

 /**
 * Test method for {@link StdTypeResolverBuilder#StdTypeResolverBuilder()}
 */
 @Test
 public void testConstructor() {
 // Nothing to test here
 }

 /**
 * Test method for {@link StdTypeResolverBuilder#init(JsonTypeInfo.Id, TypeIdResolver)}
 */
 @Test
 public void testInit() {
 // Given
 JsonTypeInfo.Id idType = JsonTypeInfo.Id.CLASS;
 TypeIdResolver idResolver = null;

 // When
 builder.init(idType, idResolver);

 // Then
 assertEquals(idType, builder._idType);
 assertNull(builder._customIdResolver);
 assertEquals("", builder._typeProperty);
 assertFalse(builder._typeIdVisible);
 assertNull(builder._defaultImpl);
 }

 /**
 * Test method for {@link StdTypeResolverBuilder#buildTypeSerializer(SerializationConfig, JavaType, Collection)}
 */
 @Test
 public void testBuildTypeSerializer() {
 // Given
 SerializationConfig config = new SerializationConfig();
 JavaType baseType = new ClassType(String.class);
 Collection<NamedType> subtypes = null;

 // When
 TypeSerializer serializer = builder.buildTypeSerializer(config, baseType, subtypes);

 // Then
 assertNull(serializer);
 }

 /**
 * Test method for {@link StdTypeResolverBuilder#init(JsonTypeInfo.Id, TypeIdResolver)} with null idType
 */
 @Test(expected = IllegalArgumentException.class)
 public void testInitNullIdType() {
 // Given
 TypeIdResolver idResolver = null;

 // When
 builder.init(null, idResolver);

 // Then
 fail("Should throw IllegalArgumentException");
 }

 /**
 * Test method for {@link StdTypeResolverBuilder#buildTypeDeserializer(DeserializationConfig, JavaType, Collection)}
 */
 @Test
 public void testBuildTypeDeserializer() {
 // Given
 DeserializationConfig config = new DeserializationConfig();
 JavaType baseType = new ClassType(String.class);
 Collection<NamedType> subtypes = null;

 // When
 TypeDeserializer deserializer = builder.buildTypeDeserializer(config, baseType, subtypes);

 // Then
 assertNull(deserializer);
 }

 /**
 * Test method for {@link StdTypeResolverBuilder#inclusion(JsonTypeInfo.As)}
 */
 @Test
 public void testInclusion() {
 // Given
 JsonTypeInfo.As includeAs = JsonTypeInfo.As.PROPERTY;

 // When
 builder.inclusion(includeAs);

 // Then
 assertEquals(includeAs, builder._includeAs);
 }

 /**
 * Test method for {@link StdTypeResolverBuilder#typeProperty(String)}
 */
 @Test
 public void testTypeProperty() {
 // Given
 String typeIdPropName = "typeId";

 // When
 builder.typeProperty(typeIdPropName);

 // Then
 assertEquals(typeIdPropName, builder._typeProperty);
 }

 /**
 * Test method for {@link StdTypeResolverBuilder#defaultImpl(Class)}
 */
 @Test
 public void testDefaultImpl() {
 // Given
 Class<?> defaultImpl = String.class;

 // When
 builder.defaultImpl(defaultImpl);

 // Then
 assertEquals(defaultImpl, builder._defaultImpl);
 }

 /**
 * Test method for {@link StdTypeResolverBuilder#typeIdVisibility(boolean)}
 */
 @Test
 public void testTypeIdVisibility() {
 // Given
 boolean isVisible = true;

 // When
 builder.typeIdVisibility(isVisible);

 // Then
 assertEquals(isVisible, builder._typeIdVisible);
 }
}