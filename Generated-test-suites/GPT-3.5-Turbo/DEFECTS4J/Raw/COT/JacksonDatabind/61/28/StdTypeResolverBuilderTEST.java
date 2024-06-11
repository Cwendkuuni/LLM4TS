
import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.impl.*;
import java.util.Collection;

public class StdTypeResolverBuilderTest {

    @Test
    public void testInit() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        try {
            builder.init(null, null);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        builder.init(JsonTypeInfo.Id.CLASS, null);
        assertEquals(JsonTypeInfo.Id.CLASS, builder._idType);
        assertNull(builder._customIdResolver);
        assertEquals("class", builder._typeProperty);
    }

    @Test
    public void testBuildTypeSerializer() {
        try {
            MapperConfig<SerializationConfig> config = null;
            JavaType baseType = null;
            Collection<NamedType> subtypes = null;
            StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
            builder.init(JsonTypeInfo.Id.NONE, null);
            assertNull(builder.buildTypeSerializer(config, baseType, subtypes));

            builder.init(JsonTypeInfo.Id.CLASS, null);
            TypeIdResolver idRes = builder.idResolver(config, baseType, subtypes, true, false);
            builder._includeAs = JsonTypeInfo.As.WRAPPER_ARRAY;
            assertNotNull(builder.buildTypeSerializer(config, baseType, subtypes));

            builder._includeAs = JsonTypeInfo.As.PROPERTY;
            assertNotNull(builder.buildTypeSerializer(config, baseType, subtypes));

            builder._includeAs = JsonTypeInfo.As.WRAPPER_OBJECT;
            assertNotNull(builder.buildTypeSerializer(config, baseType, subtypes));

            builder._includeAs = JsonTypeInfo.As.EXTERNAL_PROPERTY;
            assertNotNull(builder.buildTypeSerializer(config, baseType, subtypes));

            builder._includeAs = JsonTypeInfo.As.EXISTING_PROPERTY;
            assertNotNull(builder.buildTypeSerializer(config, baseType, subtypes));
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testBuildTypeDeserializer() {
        try {
            DeserializationConfig config = null;
            JavaType baseType = null;
            Collection<NamedType> subtypes = null;
            StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
            builder.init(JsonTypeInfo.Id.NONE, null);
            assertNull(builder.buildTypeDeserializer(config, baseType, subtypes));

            builder.init(JsonTypeInfo.Id.CLASS, null);
            TypeIdResolver idRes = builder.idResolver(config, baseType, subtypes, false, true);
            builder._defaultImpl = null;
            builder._includeAs = JsonTypeInfo.As.WRAPPER_ARRAY;
            assertNotNull(builder.buildTypeDeserializer(config, baseType, subtypes));

            builder._includeAs = JsonTypeInfo.As.PROPERTY;
            assertNotNull(builder.buildTypeDeserializer(config, baseType, subtypes));

            builder._includeAs = JsonTypeInfo.As.WRAPPER_OBJECT;
            assertNotNull(builder.buildTypeDeserializer(config, baseType, subtypes));

            builder._includeAs = JsonTypeInfo.As.EXTERNAL_PROPERTY;
            assertNotNull(builder.buildTypeDeserializer(config, baseType, subtypes));

            builder._includeAs = JsonTypeInfo.As.EXISTING_PROPERTY;
            assertNotNull(builder.buildTypeDeserializer(config, baseType, subtypes));
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testInclusion() {
        try {
            StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
            builder.inclusion(null);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        try {
            StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
            builder.inclusion(JsonTypeInfo.As.WRAPPER_ARRAY);
            assertEquals(JsonTypeInfo.As.WRAPPER_ARRAY, builder._includeAs);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testTypeProperty() {
        try {
            StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
            builder.typeProperty(null);
            assertEquals("class", builder._typeProperty);
            builder.typeProperty("");
            assertEquals("class", builder._typeProperty);
            builder.typeProperty("type");
            assertEquals("type", builder._typeProperty);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testDefaultImpl() {
        try {
            StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
            assertNull(builder.getDefaultImpl());

            builder.defaultImpl(null);
            assertNull(builder._defaultImpl);
            assertNull(builder.getDefaultImpl());

            builder.defaultImpl(Void.class);
            assertEquals(Void.class, builder._defaultImpl);
            assertEquals(Void.class, builder.getDefaultImpl());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testTypeIdVisibility() {
        try {
            StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
            assertFalse(builder.isTypeIdVisible());

            builder.typeIdVisibility(false);
            assertFalse(builder._typeIdVisible);
            assertFalse(builder.isTypeIdVisible());

            builder.typeIdVisibility(true);
            assertTrue(builder._typeIdVisible);
            assertTrue(builder.isTypeIdVisible());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testIdResolver() {
        try {
            MapperConfig<?> config = null;
            JavaType baseType = null;
            Collection<NamedType> subtypes = null;
            StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
            builder._customIdResolver = null;
            assertNull(builder.idResolver(config, baseType, subtypes, true, false));

            builder._idType = null;
            builder._customIdResolver = null;
            try {
                builder.idResolver(config, baseType, subtypes, true, false);
                fail("IllegalStateException expected");
            } catch (IllegalStateException e) {
                // Expected
            }

            builder._idType = JsonTypeInfo.Id.CLASS;
            TypeIdResolver idRes = builder.idResolver(config, baseType, subtypes, true, false);
            assertTrue(idRes instanceof ClassNameIdResolver);
//            builder._includeAs = JsonTypeInfo.As.WRAPPER_ARRAY;
//            assertNotNull(builder.buildTypeSerializer(config, baseType, subtypes));
//
//            builder._includeAs = JsonTypeInfo.As.PROPERTY;
//            assertNotNull(builder.buildTypeSerializer(config, baseType, subtypes));
//
//            builder._includeAs = JsonTypeInfo.As.WRAPPER_OBJECT;
//            assertNotNull(builder.buildTypeSerializer(config, baseType, subtypes));
//
//            builder._includeAs = JsonTypeInfo.As.EXTERNAL_PROPERTY;
//            assertNotNull(builder.buildTypeSerializer(config, baseType, subtypes));
//
//            builder._includeAs = JsonTypeInfo.As.EXISTING_PROPERTY;
//            assertNotNull(builder.buildTypeSerializer(config, baseType, subtypes));
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
}
