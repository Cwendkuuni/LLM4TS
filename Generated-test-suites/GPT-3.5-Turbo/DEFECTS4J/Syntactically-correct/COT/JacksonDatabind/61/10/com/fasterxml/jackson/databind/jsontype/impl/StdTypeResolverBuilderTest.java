import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import org.junit.Test;

import java.util.Collection;

import static org.junit.Assert.*;

public class StdTypeResolverBuilderTest {

    @Test
    public void testInit() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        try {
            builder.init(null, null);
            fail("IllegalArgumentException should be thrown when idType is null");
        } catch (IllegalArgumentException e) {
            // expected
        }

        TypeResolverBuilder<StdTypeResolverBuilder> resolverBuilder =
                builder.init(JsonTypeInfo.Id.CLASS, null);
        assertSame(builder, resolverBuilder);
        assertEquals(JsonTypeInfo.Id.CLASS, builder._idType);
        assertNull(builder._customIdResolver);
        assertEquals(JsonTypeInfo.Id.CLASS.getDefaultPropertyName(), builder._typeProperty);
    }

    @Test
    public void testBuildTypeSerializer() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.NONE, null);
        assertNull(builder.buildTypeSerializer(null, null, null));

        builder.inclusion(JsonTypeInfo.As.WRAPPER_ARRAY);
        assertNotNull(builder.buildTypeSerializer(null, null, null));

        builder.inclusion(JsonTypeInfo.As.PROPERTY).typeProperty("type");
        assertNotNull(builder.buildTypeSerializer(null, null, null));

        builder.inclusion(JsonTypeInfo.As.WRAPPER_OBJECT);
        assertNotNull(builder.buildTypeSerializer(null, null, null));

        builder.inclusion(JsonTypeInfo.As.EXTERNAL_PROPERTY);
        assertNotNull(builder.buildTypeSerializer(null, null, null));

        builder.inclusion(JsonTypeInfo.As.EXISTING_PROPERTY);
        assertNotNull(builder.buildTypeSerializer(null, null, null));
    }

    @Test
    public void testBuildTypeDeserializer() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(JsonTypeInfo.Id.NONE, null);
        assertNull(builder.buildTypeDeserializer(null, null, null));

        builder.inclusion(JsonTypeInfo.As.WRAPPER_ARRAY);
        assertNotNull(builder.buildTypeDeserializer(null, null, null));

        builder.inclusion(JsonTypeInfo.As.PROPERTY).typeProperty("type");
        assertNotNull(builder.buildTypeDeserializer(null, null, null));

        builder.inclusion(JsonTypeInfo.As.WRAPPER_OBJECT);
        assertNotNull(builder.buildTypeDeserializer(null, null, null));

        builder.inclusion(JsonTypeInfo.As.EXTERNAL_PROPERTY);
        assertNotNull(builder.buildTypeDeserializer(null, null, null));

        builder.inclusion(JsonTypeInfo.As.EXISTING_PROPERTY);
        assertNotNull(builder.buildTypeDeserializer(null, null, null));
    }

    @Test
    public void testInclusion() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        try {
            builder.inclusion(null);
            fail("IllegalArgumentException should be thrown when includeAs is null");
        } catch (IllegalArgumentException e) {
            // expected
        }

        TypeResolverBuilder<StdTypeResolverBuilder> resolverBuilder =
                builder.inclusion(JsonTypeInfo.As.PROPERTY);
        assertEquals(JsonTypeInfo.As.PROPERTY, builder._includeAs);
        assertSame(builder, resolverBuilder);
    }

    @Test
    public void testTypeProperty() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        String typeIdPropName = "typeId";
        TypeResolverBuilder<StdTypeResolverBuilder> resolverBuilder =
                builder.typeProperty(typeIdPropName);
        assertEquals(typeIdPropName, builder._typeProperty);
        assertSame(builder, resolverBuilder);
    }

    @Test
    public void testDefaultImpl() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        Class<?> defaultImpl = Object.class;
        TypeResolverBuilder<StdTypeResolverBuilder> resolverBuilder =
                builder.defaultImpl(defaultImpl);
        assertEquals(defaultImpl, builder._defaultImpl);
        assertSame(builder, resolverBuilder);
    }

    @Test
    public void testTypeIdVisibility() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        boolean isVisible = true;
        TypeResolverBuilder<StdTypeResolverBuilder> resolverBuilder =
                builder.typeIdVisibility(isVisible);
        assertEquals(isVisible, builder._typeIdVisible);
        assertSame(builder, resolverBuilder);
    }

    @Test
    public void testIdResolver() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        MapperConfig<?> config = null;
        JavaType baseType = null;
        Collection<NamedType> subtypes = null;

        try {
            builder.idResolver(config, baseType, subtypes, true, false);
            fail("IllegalStateException should be thrown when _idType is null");
        } catch (IllegalStateException e) {
            // expected
        }

        builder.init(JsonTypeInfo.Id.NAME, null);
        assertNotNull(builder.idResolver(config, baseType, subtypes, true, false));

        builder.init(JsonTypeInfo.Id.MINIMAL_CLASS, null);
        assertNotNull(builder.idResolver(config, baseType, subtypes, true, false));

        builder.init(JsonTypeInfo.Id.CLASS, null);
        assertNotNull(builder.idResolver(config, baseType, subtypes, true, false));

        builder.init(JsonTypeInfo.Id.NONE, null);
        assertNull(builder.idResolver(config, baseType, subtypes, true, false));

        builder.init(JsonTypeInfo.Id.CUSTOM, null);
        try {
            builder.idResolver(config, baseType, subtypes, true, false);
            fail("IllegalStateException should be thrown when _idType is CUSTOM");
        } catch (IllegalStateException e) {
            // expected
        }
    }
}