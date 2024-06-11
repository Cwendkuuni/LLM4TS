import org.junit.Test;
import static org.junit.Assert.assertEquals;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import java.util.Collection;

public class StdTypeResolverBuilderTest {

  @Test
  public void testInit() {
    StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
    builder.init(JsonTypeInfo.Id.CLASS, null);
    assertEquals(JsonTypeInfo.Id.CLASS, builder.getIdType());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInitWithNullIdType() {
    StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
    builder.init(null, null);
  }

  @Test
  public void testNoTypeInfoBuilder() {
    StdTypeResolverBuilder builder = StdTypeResolverBuilder.noTypeInfoBuilder();
    assertEquals(JsonTypeInfo.Id.NONE, builder.getIdType());
    assertEquals(null, builder.getCustomIdResolver());
  }

  @Test
  public void testBuildTypeSerializer() {
    StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
    builder.init(JsonTypeInfo.Id.CLASS, null);
    TypeSerializer serializer = builder.buildTypeSerializer(null, null, null);
    assertEquals(serializer.getClass(), AsArrayTypeSerializer.class);
  }

  @Test
  public void testBuildTypeSerializerWithNoTypeInfo() {
    StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
    builder.init(JsonTypeInfo.Id.NONE, null);
    TypeSerializer serializer = builder.buildTypeSerializer(null, null, null);
    assertEquals(null, serializer);
  }

  @Test
  public void testBuildTypeDeserializer() {
    StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
    builder.init(JsonTypeInfo.Id.CLASS, null);
    TypeDeserializer deserializer = builder.buildTypeDeserializer(null, null, null);
    assertEquals(deserializer.getClass(), AsArrayTypeDeserializer.class);
  }

  @Test
  public void testBuildTypeDeserializerWithNoTypeInfo() {
    StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
    builder.init(JsonTypeInfo.Id.NONE, null);
    TypeDeserializer deserializer = builder.buildTypeDeserializer(null, null, null);
    assertEquals(null, deserializer);
  }

  @Test
  public void testInclusion() {
    StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
    builder.inclusion(JsonTypeInfo.As.WRAPPER_ARRAY);
    assertEquals(JsonTypeInfo.As.WRAPPER_ARRAY, builder.getIncludeAs());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInclusionWithNullValue() {
    StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
    builder.inclusion(null);
  }

  @Test
  public void testTypeProperty() {
    StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
    builder.typeProperty("typeId");
    assertEquals("typeId", builder.getTypeProperty());
  }

  @Test
  public void testTypePropertyWithEmptyValue() {
    StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
    builder.typeProperty("");
    assertEquals("type", builder.getTypeProperty());
  }

  @Test
  public void testDefaultImpl() {
    StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
    builder.defaultImpl(String.class);
    assertEquals(String.class, builder.getDefaultImpl());
  }

  @Test
  public void testTypeIdVisibility() {
    StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
    builder.typeIdVisibility(true);
    assertEquals(true, builder.isTypeIdVisible());
  }
}