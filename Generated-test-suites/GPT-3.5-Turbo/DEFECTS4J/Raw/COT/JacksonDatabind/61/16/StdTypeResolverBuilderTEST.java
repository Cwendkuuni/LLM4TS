import org.junit.Test;

import static org.junit.Assert.*;

public class StdTypeResolverBuilderTest {

    @Test
    public void testInit() {
        StdTypeResolverBuilder resolverBuilder = new StdTypeResolverBuilder();
        assertEquals(JsonTypeInfo.Id.NONE, resolverBuilder._idType);
        assertNull(resolverBuilder._customIdResolver);
        assertNull(resolverBuilder._typeProperty);

        resolverBuilder.init(JsonTypeInfo.Id.CLASS, null);
        assertEquals(JsonTypeInfo.Id.CLASS, resolverBuilder._idType);
        assertNull(resolverBuilder._customIdResolver);
        assertEquals("@class", resolverBuilder._typeProperty);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitNullIdType() {
        StdTypeResolverBuilder resolverBuilder = new StdTypeResolverBuilder();
        resolverBuilder.init(null, null);
    }

    @Test
    public void testNoTypeInfoBuilder() {
        StdTypeResolverBuilder resolverBuilder = StdTypeResolverBuilder.noTypeInfoBuilder();
        assertEquals(JsonTypeInfo.Id.NONE, resolverBuilder._idType);
        assertNull(resolverBuilder._customIdResolver);
        assertNull(resolverBuilder._typeProperty);
    }

    @Test
    public void testBuildTypeSerializer() {
        StdTypeResolverBuilder resolverBuilder = new StdTypeResolverBuilder();
        resolverBuilder.init(JsonTypeInfo.Id.CLASS, null);

        SerializationConfig config = null;
        JavaType baseType = null;
        Collection<NamedType> subtypes = null;

        TypeSerializer typeSerializer = resolverBuilder.buildTypeSerializer(config, baseType, subtypes);
        assertNull(typeSerializer);

        resolverBuilder._includeAs = JsonTypeInfo.As.WRAPPER_ARRAY;
        typeSerializer = resolverBuilder.buildTypeSerializer(config, baseType, subtypes);
        assertNotNull(typeSerializer);
        assertTrue(typeSerializer instanceof AsArrayTypeSerializer);

        resolverBuilder._includeAs = JsonTypeInfo.As.PROPERTY;
        typeSerializer = resolverBuilder.buildTypeSerializer(config, baseType, subtypes);
        assertNotNull(typeSerializer);
        assertTrue(typeSerializer instanceof AsPropertyTypeSerializer);

        resolverBuilder._includeAs = JsonTypeInfo.As.WRAPPER_OBJECT;
        typeSerializer = resolverBuilder.buildTypeSerializer(config, baseType, subtypes);
        assertNotNull(typeSerializer);
        assertTrue(typeSerializer instanceof AsWrapperTypeSerializer);

        resolverBuilder._includeAs = JsonTypeInfo.As.EXTERNAL_PROPERTY;
        typeSerializer = resolverBuilder.buildTypeSerializer(config, baseType, subtypes);
        assertNotNull(typeSerializer);
        assertTrue(typeSerializer instanceof AsExternalTypeSerializer);

        resolverBuilder._includeAs = JsonTypeInfo.As.EXISTING_PROPERTY;
        typeSerializer = resolverBuilder.buildTypeSerializer(config, baseType, subtypes);
        assertNotNull(typeSerializer);
        assertTrue(typeSerializer instanceof AsExistingPropertyTypeSerializer);
    }

    @Test
    public void testBuildTypeDeserializer() {
        StdTypeResolverBuilder resolverBuilder = new StdTypeResolverBuilder();
        resolverBuilder.init(JsonTypeInfo.Id.CLASS, null);

        DeserializationConfig config = null;
        JavaType baseType = null;
        Collection<NamedType> subtypes = null;

        TypeDeserializer typeDeserializer = resolverBuilder.buildTypeDeserializer(config, baseType, subtypes);
        assertNull(typeDeserializer);

        resolverBuilder._includeAs = JsonTypeInfo.As.WRAPPER_ARRAY;
        typeDeserializer = resolverBuilder.buildTypeDeserializer(config, baseType, subtypes);
        assertNotNull(typeDeserializer);
        assertTrue(typeDeserializer instanceof AsArrayTypeDeserializer);

        resolverBuilder._includeAs = JsonTypeInfo.As.PROPERTY;
        typeDeserializer = resolverBuilder.buildTypeDeserializer(config, baseType, subtypes);
        assertNotNull(typeDeserializer);
        assertTrue(typeDeserializer instanceof AsPropertyTypeDeserializer);

        resolverBuilder._includeAs = JsonTypeInfo.As.WRAPPER_OBJECT;
        typeDeserializer = resolverBuilder.buildTypeDeserializer(config, baseType, subtypes);
        assertNotNull(typeDeserializer);
        assertTrue(typeDeserializer instanceof AsWrapperTypeDeserializer);

        resolverBuilder._includeAs = JsonTypeInfo.As.EXTERNAL_PROPERTY;
        typeDeserializer = resolverBuilder.buildTypeDeserializer(config, baseType, subtypes);
        assertNotNull(typeDeserializer);
        assertTrue(typeDeserializer instanceof AsExternalTypeDeserializer);
    }

    @Test
    public void testInclusion() {
        StdTypeResolverBuilder resolverBuilder = new StdTypeResolverBuilder();

        resolverBuilder.inclusion(JsonTypeInfo.As.WRAPPER_ARRAY);
        assertEquals(JsonTypeInfo.As.WRAPPER_ARRAY, resolverBuilder._includeAs);

        resolverBuilder.inclusion(JsonTypeInfo.As.PROPERTY);
        assertEquals(JsonTypeInfo.As.PROPERTY, resolverBuilder._includeAs);

        resolverBuilder.inclusion(JsonTypeInfo.As.WRAPPER_OBJECT);
        assertEquals(JsonTypeInfo.As.WRAPPER_OBJECT, resolverBuilder._includeAs);

        resolverBuilder.inclusion(JsonTypeInfo.As.EXTERNAL_PROPERTY);
        assertEquals(JsonTypeInfo.As.EXTERNAL_PROPERTY, resolverBuilder._includeAs);

        resolverBuilder.inclusion(JsonTypeInfo.As.EXISTING_PROPERTY);
        assertEquals(JsonTypeInfo.As.EXISTING_PROPERTY, resolverBuilder._includeAs);

        try {
            resolverBuilder.inclusion(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testTypeProperty() {
        StdTypeResolverBuilder resolverBuilder = new StdTypeResolverBuilder();

        resolverBuilder.typeProperty("typeName");
        assertEquals("typeName", resolverBuilder._typeProperty);

        resolverBuilder.typeProperty(null);
        assertEquals("@class", resolverBuilder._typeProperty);

        resolverBuilder.typeProperty("");
        assertEquals("@class", resolverBuilder._typeProperty);
    }

    @Test
    public void testDefaultImpl() {
        StdTypeResolverBuilder resolverBuilder = new StdTypeResolverBuilder();

        assertNull(resolverBuilder._defaultImpl);

        Class<?> defaultImpl = Integer.class;
        resolverBuilder.defaultImpl(defaultImpl);
        assertEquals(defaultImpl, resolverBuilder._defaultImpl);
    }

    @Test
    public void testTypeIdVisibility() {
        StdTypeResolverBuilder resolverBuilder = new StdTypeResolverBuilder();

        assertFalse(resolverBuilder._typeIdVisible);

        resolverBuilder.typeIdVisibility(true);
        assertTrue(resolverBuilder._typeIdVisible);

        resolverBuilder.typeIdVisibility(false);
        assertFalse(resolverBuilder._typeIdVisible);
    }

    @Test
    public void testGetDefaultImpl() {
        StdTypeResolverBuilder resolverBuilder = new StdTypeResolverBuilder();

        assertNull(resolverBuilder.getDefaultImpl());

        Class<?> defaultImpl = Integer.class;
        resolverBuilder.defaultImpl(defaultImpl);
        assertEquals(defaultImpl, resolverBuilder.getDefaultImpl());
    }

    @Test
    public void testGetTypeProperty() {
        StdTypeResolverBuilder resolverBuilder = new StdTypeResolverBuilder();

        assertNull(resolverBuilder.getTypeProperty());

        resolverBuilder.typeProperty("typeName");
        assertEquals("typeName", resolverBuilder.getTypeProperty());
    }

    @Test
    public void testIsTypeIdVisible() {
        StdTypeResolverBuilder resolverBuilder = new StdTypeResolverBuilder();

        assertFalse(resolverBuilder.isTypeIdVisible());

        resolverBuilder.typeIdVisibility(true);
        assertTrue(resolverBuilder.isTypeIdVisible());
    }
}