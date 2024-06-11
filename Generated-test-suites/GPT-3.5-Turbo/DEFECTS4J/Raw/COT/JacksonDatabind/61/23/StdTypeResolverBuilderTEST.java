
import org.junit.Test;
import java.util.Collection;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.NoClass;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.jsontype.impl.*;
import java.util.ArrayList;

public class StdTypeResolverBuilderTest {

    @Test
    public void testInit() {
        StdTypeResolverBuilder resolverBuilder = new StdTypeResolverBuilder();
        resolverBuilder.init(JsonTypeInfo.Id.CLASS, null);
        assert resolverBuilder._idType == JsonTypeInfo.Id.CLASS;
        assert resolverBuilder._customIdResolver == null;
        assert resolverBuilder._typeProperty == "class";
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitNullIdType() {
        StdTypeResolverBuilder resolverBuilder = new StdTypeResolverBuilder();
        resolverBuilder.init(null, null);
    }

    @Test
    public void testNoTypeInfoBuilder() {
        StdTypeResolverBuilder resolverBuilder = StdTypeResolverBuilder.noTypeInfoBuilder();
        assert resolverBuilder._idType == JsonTypeInfo.Id.NONE;
        assert resolverBuilder._customIdResolver == null;
        assert resolverBuilder._typeProperty == null;
    }

    @Test
    public void testBuildTypeSerializerNoneIdType() {
        StdTypeResolverBuilder resolverBuilder = new StdTypeResolverBuilder();
        TypeSerializer typeSerializer = resolverBuilder.buildTypeSerializer(null, null, null);
        assert typeSerializer == null;
    }

    @Test
    public void testBuildTypeSerializerWrapperArrayType() {
        StdTypeResolverBuilder resolverBuilder = new StdTypeResolverBuilder();
        resolverBuilder.init(JsonTypeInfo.Id.CLASS, new ClassNameIdResolver(null, null));
        resolverBuilder.inclusion(JsonTypeInfo.As.WRAPPER_ARRAY);
        TypeSerializer typeSerializer = resolverBuilder.buildTypeSerializer(null, null, null);
        assert typeSerializer instanceof AsArrayTypeSerializer;
    }

    @Test
    public void testBuildTypeSerializerPropertyType() {
        StdTypeResolverBuilder resolverBuilder = new StdTypeResolverBuilder();
        resolverBuilder.init(JsonTypeInfo.Id.CLASS, new ClassNameIdResolver(null, null));
        resolverBuilder.inclusion(JsonTypeInfo.As.PROPERTY);
        TypeSerializer typeSerializer = resolverBuilder.buildTypeSerializer(null, null, null);
        assert typeSerializer instanceof AsPropertyTypeSerializer;
    }

    @Test
    public void testBuildTypeSerializerWrapperObjectType() {
        StdTypeResolverBuilder resolverBuilder = new StdTypeResolverBuilder();
        resolverBuilder.init(JsonTypeInfo.Id.CLASS, new ClassNameIdResolver(null, null));
        resolverBuilder.inclusion(JsonTypeInfo.As.WRAPPER_OBJECT);
        TypeSerializer typeSerializer = resolverBuilder.buildTypeSerializer(null, null, null);
        assert typeSerializer instanceof AsWrapperTypeSerializer;
    }

    @Test
    public void testBuildTypeSerializerExternalPropertyType() {
        StdTypeResolverBuilder resolverBuilder = new StdTypeResolverBuilder();
        resolverBuilder.init(JsonTypeInfo.Id.CLASS, new ClassNameIdResolver(null, null));
        resolverBuilder.inclusion(JsonTypeInfo.As.EXTERNAL_PROPERTY);
        TypeSerializer typeSerializer = resolverBuilder.buildTypeSerializer(null, null, null);
        assert typeSerializer instanceof AsExternalTypeSerializer;
    }

    @Test
    public void testBuildTypeSerializerExistingPropertyType() {
        StdTypeResolverBuilder resolverBuilder = new StdTypeResolverBuilder();
        resolverBuilder.init(JsonTypeInfo.Id.CLASS, new ClassNameIdResolver(null, null));
        resolverBuilder.inclusion(JsonTypeInfo.As.EXISTING_PROPERTY);
        TypeSerializer typeSerializer = resolverBuilder.buildTypeSerializer(null, null, null);
        assert typeSerializer instanceof AsExistingPropertyTypeSerializer;
    }

    @Test(expected = IllegalStateException.class)
    public void testBuildTypeSerializerUnknownInclusionType() {
        StdTypeResolverBuilder resolverBuilder = new StdTypeResolverBuilder();
        resolverBuilder.init(JsonTypeInfo.Id.CLASS, new ClassNameIdResolver(null, null));
        resolverBuilder.inclusion(null);
        resolverBuilder.buildTypeSerializer(null, null, null);
    }

    @Test
    public void testBuildTypeDeserializerNoneIdType() {
        StdTypeResolverBuilder resolverBuilder = new StdTypeResolverBuilder();
        TypeDeserializer typeDeserializer = resolverBuilder.buildTypeDeserializer(null, null, null);
        assert typeDeserializer == null;
    }

    @Test
    public void testBuildTypeDeserializerWrapperArrayType() {
        StdTypeResolverBuilder resolverBuilder = new StdTypeResolverBuilder();
        resolverBuilder.init(JsonTypeInfo.Id.CLASS, new ClassNameIdResolver(null, null));
        resolverBuilder.inclusion(JsonTypeInfo.As.WRAPPER_ARRAY);
        TypeDeserializer typeDeserializer = resolverBuilder.buildTypeDeserializer(null, null, null);
        assert typeDeserializer instanceof AsArrayTypeDeserializer;
    }

    @Test
    public void testBuildTypeDeserializerPropertyType() {
        StdTypeResolverBuilder resolverBuilder = new StdTypeResolverBuilder();
        resolverBuilder.init(JsonTypeInfo.Id.CLASS, new ClassNameIdResolver(null, null));
        resolverBuilder.inclusion(JsonTypeInfo.As.PROPERTY);
        TypeDeserializer typeDeserializer = resolverBuilder.buildTypeDeserializer(null, null, null);
        assert typeDeserializer instanceof AsPropertyTypeDeserializer;
    }

    @Test
    public void testBuildTypeDeserializerWrapperObjectType() {
        StdTypeResolverBuilder resolverBuilder = new StdTypeResolverBuilder();
        resolverBuilder.init(JsonTypeInfo.Id.CLASS, new ClassNameIdResolver(null, null));
        resolverBuilder.inclusion(JsonTypeInfo.As.WRAPPER_OBJECT);
        TypeDeserializer typeDeserializer = resolverBuilder.buildTypeDeserializer(null, null, null);
        assert typeDeserializer instanceof AsWrapperTypeDeserializer;
    }

    @Test
    public void testBuildTypeDeserializerExternalPropertyType() {
        StdTypeResolverBuilder resolverBuilder = new StdTypeResolverBuilder();
        resolverBuilder.init(JsonTypeInfo.Id.CLASS, new ClassNameIdResolver(null, null));
        resolverBuilder.inclusion(JsonTypeInfo.As.EXTERNAL_PROPERTY);
        TypeDeserializer typeDeserializer = resolverBuilder.buildTypeDeserializer(null, null, null);
        assert typeDeserializer instanceof AsExternalTypeDeserializer;
    }

    @Test
    public void testTypeIdVisibility() {
        StdTypeResolverBuilder resolverBuilder = new StdTypeResolverBuilder();
        assert !resolverBuilder.isTypeIdVisible();
        resolverBuilder.typeIdVisibility(true);
        assert resolverBuilder.isTypeIdVisible();
    }
}
