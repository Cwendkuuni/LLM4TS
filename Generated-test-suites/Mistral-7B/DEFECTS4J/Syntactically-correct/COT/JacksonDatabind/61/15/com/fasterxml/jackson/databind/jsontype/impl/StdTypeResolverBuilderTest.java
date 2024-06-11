import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.type.ClassType;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.NoClass;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.jsontype.ClassIdResolver;
import com.fasterxml.jackson.databind.jsontype.IdResolver;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class StdTypeResolverBuilderTest {

    private StdTypeResolverBuilder builder;
    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        builder = new StdTypeResolverBuilder();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testConstructor() {
        StdTypeResolverBuilder actual = new StdTypeResolverBuilder();
        Assert.assertNotNull(actual);
    }

    @Test
    public void testNoTypeInfoBuilder() {
        StdTypeResolverBuilder actual = StdTypeResolverBuilder.noTypeInfoBuilder();
        Assert.assertNotNull(actual);
    }

    @Test
    public void testInit() {
        JsonTypeInfo.Id idType = JsonTypeInfo.Id.CLASS;
        TypeIdResolver idRes = new ClassNameIdResolver(Object.class, null);
        StdTypeResolverBuilder expected = new StdTypeResolverBuilder()
                .init(idType, idRes);
        StdTypeResolverBuilder actual = builder.init(idType, idRes);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testBuildTypeSerializerNullIdType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        Assert.assertNull(builder.buildTypeSerializer(null, null, null));
    }

    @Test
    public void testBuildTypeSerializerWrongIncludeAs() {
        JsonTypeInfo idType = JsonTypeInfo.Id.WRAPPER_OBJECT;
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder()
                .init(idType, null);

        Assert.assertNull(builder.buildTypeSerializer(null, null, null));
    }

    @Test
    public void testBuildTypeSerializerProperty() {
        JsonTypeInfo idType = JsonTypeInfo.Id.PROPERTY;
        JavaType baseType = ObjectMapper.TYPE_MAP.constructType(String.class);
        Collection<NamedType> subtypes = Arrays.asList(new NamedType(String.class, "str"));
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder()
                .init(idType, null);

        TypeSerializer actual = builder.buildTypeSerializer(null, baseType, subtypes);
        TypeSerializer expected = new AsPropertyTypeSerializer(new ClassNameIdResolver(baseType.getRawClass(), null), null, "_type");
        Assert.assertEquals(expected.getClass(), actual.getClass());
    }

    @Test
    public void testBuildTypeSerializerWrapperArray() {
        JsonTypeInfo idType = JsonTypeInfo.Id.WRAPPER_ARRAY;
        JavaType baseType = ObjectMapper.TYPE_MAP.constructType(List.class);
        Collection<NamedType> subtypes = Arrays.asList(new NamedType(Integer.class, "int"));
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder()
                .init(idType, null);

        TypeSerializer actual = builder.buildTypeSerializer(null, baseType, subtypes);
        TypeSerializer expected = new AsArrayTypeSerializer(new ClassNameIdResolver(baseType.getRawClass(), null), null);
        Assert.assertEquals(expected.getClass(), actual.getClass());
    }

    @Test
    public void testBuildTypeSerializerWrapperObject() {
        JsonTypeInfo idType = JsonTypeInfo.Id.WRAPPER_OBJECT;
        JavaType baseType = ObjectMapper.TYPE_MAP.constructType(Map.class);
        Collection<NamedType> subtypes = Arrays.asList(new NamedType(String.class, "str"));
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder()
                .init(idType, null);

        TypeSerializer actual = builder.buildTypeSerializer(null, baseType, subtypes);
        TypeSerializer expected = new AsWrapperTypeSerializer(new ClassNameIdResolver(baseType.getRawClass(), null), null);
        Assert.assertEquals(expected.getClass(), actual.getClass());
    }

    @Test
    public void testBuildTypeSerializerExternalProperty() {
        JsonTypeInfo idType = JsonTypeInfo.Id.EXTERNAL_PROPERTY;
        JavaType baseType = ObjectMapper.TYPE_MAP.constructType(String.class);
        Collection<NamedType> subtypes = Arrays.asList(new NamedType(Integer.class, "int"));
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder()
                .init(idType, null);

        TypeSerializer actual = builder.buildTypeSerializer(null, baseType, subtypes);
        TypeSerializer expected = new AsExternalTypeSerializer(new ClassNameIdResolver(baseType.getRawClass(), null), null, "_type");
        Assert.assertEquals(expected.getClass(), actual.getClass());
    }

    @Test(expected = IllegalStateException.class)
    public void testBuildTypeSerializerExistingProperty() {
        JsonTypeInfo idType = JsonTypeInfo.Id.EXISTING_PROPERTY;
        JavaType baseType = ObjectMapper.TYPE_MAP.constructType(String.class);
        Collection<NamedType> subtypes = Arrays.asList(new NamedType(Integer.class, "int"));
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder()
                .init(idType, null);

        builder.buildTypeSerializer(null, baseType, subtypes);
    }

    @Test
    public void testBuildTypeDeserializerNullIdType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder()
                .init(JsonTypeInfo.Id.NONE, null);
        Assert.assertNull(builder.buildTypeDeserializer(null, null, null));
    }

    @Test
    public void testBuildTypeDeserializerWrongIncludeAs() {
        JsonTypeInfo idType = JsonTypeInfo.Id.WRAPPER_OBJECT;
        JavaType baseType = ObjectMapper.TYPE_MAP.constructType(Map.class);
        Collection<NamedType> subtypes = Arrays.asList(new NamedType(String.class, "str"));
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder()
                .init(idType, null);

        Assert.assertNull(builder.buildTypeDeserializer(null, baseType, subtypes));
    }

    @Test
    public void testBuildTypeDeserializerProperty() {
        JsonTypeInfo idType = JsonTypeInfo.Id.PROPERTY;
        JavaType baseType = ObjectMapper.TYPE_MAP.constructType(String.class);
        Collection<NamedType> subtypes = Arrays.asList(new NamedType(Integer.class, "int"));
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder()
                .init(idType, null);

        TypeDeserializer actual = builder.buildTypeDeserializer(null, baseType, subtypes);
        TypeDeserializer expected = new AsPropertyTypeDeserializer(baseType, null, "_type", false, null);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testBuildTypeDeserializerWrapperArray() {
        JsonTypeInfo idType = JsonTypeInfo.Id.WRAPPER_ARRAY;
        JavaType baseType = ObjectMapper.TYPE_MAP.constructType(List.class);
        Collection<NamedType> subtypes = Arrays.asList(new NamedType(Integer.class, "int"));
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder()
                .init(idType, null);

        TypeDeserializer actual = builder.buildTypeDeserializer(null, baseType, subtypes);
        TypeDeserializer expected = new AsArrayTypeDeserializer(baseType, null, "_type", false, null);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testBuildTypeDeserializerWrapperObject() {
        JsonTypeInfo idType = JsonTypeInfo.Id.WRAPPER_OBJECT;
        JavaType baseType = ObjectMapper.TYPE_MAP.constructType(Map.class);
        Collection<NamedType> subtypes = Arrays.asList(new NamedType(String.class, "str"));
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder()
                .init(idType, null);

        TypeDeserializer actual = builder.buildTypeDeserializer(null, baseType, subtypes);
        TypeDeserializer expected = new AsWrapperTypeDeserializer(baseType, null, "_type", false, null);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testBuildTypeDeserializerExternalProperty() {
        JsonTypeInfo idType = JsonTypeInfo.Id.EXTERNAL_PROPERTY;
        JavaType baseType = ObjectMapper.TYPE_MAP.constructType(String.class);
        Collection<NamedType> subtypes = Arrays.asList(new NamedType(Integer.class, "int"));
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder()
                .init(idType, null);

        TypeDeserializer actual = builder.buildTypeDeserializer(null, baseType, subtypes);
        TypeDeserializer expected = new AsExternalTypeDeserializer(baseType, null, "_type", false, null);
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = IllegalStateException.class)
    public void testBuildTypeDeserializerExistingProperty() {
        JsonTypeInfo idType = JsonTypeInfo.Id.EXISTING_PROPERTY;
        JavaType baseType = ObjectMapper.TYPE_MAP.constructType(String.class);
        Collection<NamedType> subtypes = Arrays.asList(new NamedType(Integer.class, "int"));
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder()
                .init(idType, null);

        builder.buildTypeDeserializer(null, baseType, subtypes);
    }

    @Test
    public void testInclusion() {
        StdTypeResolverBuilder actual = builder.inclusion(JsonTypeInfo.As.PROPERTY);
        StdTypeResolverBuilder expected = new StdTypeResolverBuilder()
                .inclusion(JsonTypeInfo.As.PROPERTY);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testTypeProperty() {
        String typeIdPropName = "customTypeIdPropName";
        StdTypeResolverBuilder actual = builder.typeProperty(typeIdPropName);
        StdTypeResolverBuilder expected = new StdTypeResolverBuilder()
                .typeProperty(typeIdPropName);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testDefaultImpl() {
        Class<String> defaultImpl = String.class;
        StdTypeResolverBuilder actual = builder.defaultImpl(defaultImpl);
        StdTypeResolverBuilder expected = new StdTypeResolverBuilder()
                .defaultImpl(defaultImpl);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testTypeIdVisibility() {
        boolean isVisible = true;
        StdTypeResolverBuilder actual = builder.typeIdVisibility(isVisible);
        StdTypeResolverBuilder expected = new StdTypeResolverBuilder()
                .typeIdVisibility(isVisible);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIdResolver() {
        MapperConfig<?> config = new MapperConfig(ObjectMapper.getDefaultConfig());
        JavaType baseType = ObjectMapper.TYPE_MAP.constructType(String.class);
        Collection<NamedType> subtypes = Arrays.asList(new NamedType(String.class, "str"));

        // For serialization
        TypeIdResolver idRes1 = builder.idResolver(config, baseType, subtypes, true, false);
        Assert.instanceOf(ClassNameIdResolver.class, idRes1);

        // For deserialization
        TypeIdResolver idRes2 = builder.idResolver(config, baseType, subtypes, false, true);
        Assert.instanceOf(ClassNameIdResolver.class, idRes2);

        // Custom Id Resolver
        IdResolver customIdResolver = new CustomIdResolver();
        TypeIdResolver idRes3 = builder.init(JsonTypeInfo.Id.CUSTOM, customIdResolver)
                .buildTypeSerializer(config, baseType, subtypes);
        Assert.assertEquals(customIdResolver, idRes3);
    }
}

class CustomIdResolver implements IdResolver {
    @Override
    public TypeId resolveId(SerializerProvider serializers, TypeSerializer serializer, JavaType type, Boolean unquoted) {
        return null;
    }

    @Override
    public String idFromValue(DeserializationContext ctxt, Object value) {
        return null;
    }
}