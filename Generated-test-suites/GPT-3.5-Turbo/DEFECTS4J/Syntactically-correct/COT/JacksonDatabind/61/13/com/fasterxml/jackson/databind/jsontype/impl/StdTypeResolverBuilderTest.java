import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.Configuration;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.*;

import org.junit.Assert;
import org.junit.Test;

public class StdTypeResolverBuilderTest {

    @Test
    public void testInit() {
        StdTypeResolverBuilder resolverBuilder = new StdTypeResolverBuilder();
        resolverBuilder.init(JsonTypeInfo.Id.CLASS, null);
        Assert.assertEquals(JsonTypeInfo.Id.CLASS, resolverBuilder._idType);
        Assert.assertNull(resolverBuilder._customIdResolver);
        Assert.assertEquals(JsonTypeInfo.Id.CLASS.getDefaultPropertyName(), resolverBuilder._typeProperty);
    }

    @Test
    public void testBuildTypeSerializer() {
        StdTypeResolverBuilder resolverBuilder = new StdTypeResolverBuilder();
        resolverBuilder.init(JsonTypeInfo.Id.CLASS, null);
        Configuration config = new ObjectMapper().getDeserializationConfig();
        JavaType baseType = config.constructType(String.class);
        Assert.assertNull(resolverBuilder.buildTypeSerializer(config, baseType, null));
    }

    @Test
    public void testBuildTypeDeserializer() {
        StdTypeResolverBuilder resolverBuilder = new StdTypeResolverBuilder();
        resolverBuilder.init(JsonTypeInfo.Id.CLASS, null);
        Configuration config = new ObjectMapper().getDeserializationConfig();
        JavaType baseType = config.constructType(String.class);
        Assert.assertNull(resolverBuilder.buildTypeDeserializer(config, baseType, null));
    }

    @Test
    public void testInclusion() {
        StdTypeResolverBuilder resolverBuilder = new StdTypeResolverBuilder();
        resolverBuilder.inclusion(JsonTypeInfo.As.WRAPPER_ARRAY);
        Assert.assertEquals(JsonTypeInfo.As.WRAPPER_ARRAY, resolverBuilder._includeAs);
    }

    @Test
    public void testTypeProperty() {
        StdTypeResolverBuilder resolverBuilder = new StdTypeResolverBuilder();
        resolverBuilder.typeProperty("type");
        Assert.assertEquals("type", resolverBuilder._typeProperty);
    }

    @Test
    public void testDefaultImpl() {
        StdTypeResolverBuilder resolverBuilder = new StdTypeResolverBuilder();
        resolverBuilder.defaultImpl(Object.class);
        Assert.assertEquals(Object.class, resolverBuilder._defaultImpl);
    }

    @Test
    public void testTypeIdVisibility() {
        StdTypeResolverBuilder resolverBuilder = new StdTypeResolverBuilder();
        resolverBuilder.typeIdVisibility(true);
        Assert.assertTrue(resolverBuilder._typeIdVisible);
    }
}