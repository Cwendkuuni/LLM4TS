import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.NoClass;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.jsontype.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdTypeResolverBuilderTest {

    @Test
    public void testInit() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        assertNull(builder.getIdType());
        assertNull(builder.getIncludeAs());
        assertNull(builder.getTypeProperty());
        assertFalse(builder.isTypeIdVisible());
        assertNull(builder.getDefaultImpl());
        assertNull(builder.getCustomIdResolver());

        builder.init(JsonTypeInfo.Id.CLASS, null);
        assertSame(JsonTypeInfo.Id.CLASS, builder.getIdType());
        assertNull(builder.getIncludeAs());
        assertNull(builder.getTypeProperty());
        assertFalse(builder.isTypeIdVisible());
        assertNull(builder.getDefaultImpl());
        assertNull(builder.getCustomIdResolver());

        builder.init(JsonTypeInfo.Id.NAME, new MinimalClassNameIdResolver(null, null));
        assertSame(JsonTypeInfo.Id.NAME, builder.getIdType());
        assertNull(builder.getIncludeAs());
        assertNull(builder.getTypeProperty());
        assertFalse(builder.isTypeIdVisible());
        assertNotNull(builder.getDefaultImpl());
        assertSame(MinimalClassNameIdResolver.class, builder.getCustomIdResolver().getClass());
    }

    @Test
    public void testBuildTypeSerializer() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();

        MapperConfig<?> config = new MapperConfig<>();
        JavaType baseType = config.constructType(String.class);
        Collection<NamedType> subtypes = null;

        try {
            builder.buildTypeSerializer(config, baseType, subtypes);
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            assertEquals("Do not know how to construct standard type serializer for inclusion type: null", e.getMessage());
        }

        builder.init(JsonTypeInfo.Id.WRAPPER_ARRAY, null);
        TypeSerializer serializer = builder.buildTypeSerializer(config, baseType, subtypes);
        assertNull(serializer);

        builder.init(JsonTypeInfo.Id.PROPERTY, null);
        serializer = builder.buildTypeSerializer(config, baseType, subtypes);
        assertNotNull(serializer);

        builder.init(JsonTypeInfo.Id.WRAPPER_OBJECT, null);
        serializer = builder.buildTypeSerializer(config, baseType, subtypes);
        assertNotNull(serializer);

        builder.init(JsonTypeInfo.Id.EXTERNAL_PROPERTY, null);
        serializer = builder.buildTypeSerializer(config, baseType, subtypes);
        assertNotNull(serializer);

        builder.init(JsonTypeInfo.Id.EXISTING_PROPERTY, null);
        serializer = builder.buildTypeSerializer(config, baseType, subtypes);
        assertNotNull(serializer);
    }

    @Test
    public void testBuildTypeDeserializer() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();

        DeserializationConfig config = new DeserializationConfig();
        JavaType baseType = config.constructType(String.class);
        Collection<NamedType> subtypes = null;

        try {
            builder.buildTypeDeserializer(config, baseType, subtypes);
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            assertEquals("Do not know how to construct standard type deserializer for inclusion type: null", e.getMessage());
        }

        builder.init(JsonTypeInfo.Id.WRAPPER_ARRAY, null);
        TypeDeserializer deserializer = builder.buildTypeDeserializer(config, baseType, subtypes);
        assertNull(deserializer);

        builder.init(JsonTypeInfo.Id.PROPERTY, null);
        deserializer = builder.buildTypeDeserializer(config, baseType, subtypes);
        assertNotNull(deserializer);

        builder.init(JsonTypeInfo.Id.WRAPPER_OBJECT, null);
        deserializer = builder.buildTypeDeserializer(config, baseType, subtypes);
        assertNotNull(deserializer);

        builder.init(JsonTypeInfo.Id.EXTERNAL_PROPERTY, null);
        deserializer = builder.buildTypeDeserializer(config, baseType, subtypes);
        assertNotNull(deserializer);

        builder.init(JsonTypeInfo.Id.EXISTING_PROPERTY, null);
        deserializer = builder.buildTypeDeserializer(config, baseType, subtypes);
        assertNotNull(deserializer);
    }

    @Test
    public void testInclusion() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();

        builder.init(JsonTypeInfo.Id.NONE, null);
        assertNull(builder.getIncludeAs());
        builder.inclusion(JsonTypeInfo.As.WRAPPER_ARRAY);
        assertEquals(JsonTypeInfo.As.WRAPPER_ARRAY, builder.getIncludeAs());
    }

    @Test
    public void testTypeProperty() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();

        builder.init(JsonTypeInfo.Id.NONE, null);
        assertNull(builder.getTypeProperty());
        builder.typeProperty("myProperty");
        assertEquals("myProperty", builder.getTypeProperty());
    }

    @Test
    public void testDefaultImpl() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();

        builder.init(JsonTypeInfo.Id.NONE, null);
        assertNull(builder.getDefaultImpl());
        builder.defaultImpl(String.class);
        assertSame(String.class, builder.getDefaultImpl());
    }

    @Test
    public void testTypeIdVisibility() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();

        builder.init(JsonTypeInfo.Id.NONE, null);
        assertFalse(builder.isTypeIdVisible());
        builder.typeIdVisibility(true);
        assertTrue(builder.isTypeIdVisible());
    }
}