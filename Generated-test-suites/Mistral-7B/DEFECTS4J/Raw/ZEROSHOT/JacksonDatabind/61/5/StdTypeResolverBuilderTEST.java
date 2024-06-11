import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.MapperFactory;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.type.Types;

public class StdTypeResolverBuilderTest {

    @Test
    public void testStdTypeResolverBuilder() {
        // Test constructor
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();

        // Test init method with null arguments
        try {
            builder.init(null, null);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test init method with valid arguments
        JsonTypeInfo.Id idType = JsonTypeInfo.Id.CLASS;
        TypeIdResolver idResolver = TypeNameIdResolver.construct(new MapperFactory(), Types.JavaType.INT_CLASS, null, false, false);
        builder.init(idType, idResolver);

        // Test building TypeSerializer
        JsonTypeInfo jsonTypeInfo = new JsonTypeInfo.Builder()
                .use(builder)
                .build();
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationConfig(mapper.getSerializationConfig().with(jsonTypeInfo));
        JavaType javaType = Types.constructType(String[].class);
        TypeSerializer typeSerializer = builder.buildTypeSerializer(mapper.getSerializationConfig(), javaType, null);
        Assert.assertNotNull(typeSerializer);

        // Test building TypeDeserializer
        DeserializationConfig deserializationConfig = mapper.getDeserializationConfig()
                .with(jsonTypeInfo);
        JavaType baseType = Types.constructType(String.class);
        TypeDeserializer typeDeserializer = builder.buildTypeDeserializer(deserializationConfig, baseType, null);
        Assert.assertNotNull(typeDeserializer);

        // Test methods for setting properties and inclusion
        builder = new StdTypeResolverBuilder();
        builder.inclusion(JsonTypeInfo.As.PROPERTY);
        builder.typeProperty("typePropertyName");
        builder.defaultImpl(String.class);
        builder.typeIdVisibility(true);
        Assert.assertEquals(JsonTypeInfo.As.PROPERTY, builder.getIncludeAs());
        Assert.assertEquals("typePropertyName", builder.getTypeProperty());
        Assert.assertEquals(String.class, builder.getDefaultImpl());
        Assert.assertTrue(builder.isTypeIdVisible());

        // Test getters
        Assert.assertNull(builder.getDefaultImpl());
        Assert.assertEquals("", builder.getTypeProperty());
        Assert.assertFalse(builder.isTypeIdVisible());
    }
}