import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.jsontype.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class StdTypeResolverBuilderTest {

    private ObjectMapper mapper;
    private StdTypeResolverBuilder builder;

    @Before
    public void setup() throws IOException {
        mapper = new ObjectMapper();
        builder = new StdTypeResolverBuilder();
    }

    // Testing constructor and init method

    @Test
    public void testNoArgsConstructor() {
        StdTypeResolverBuilder stdTypeResolverBuilder = new StdTypeResolverBuilder();
        Assert.assertNull(stdTypeResolverBuilder.getDefaultImpl());
        Assert.assertNull(stdTypeResolverBuilder.getTypeProperty());
        Assert.assertFalse(stdTypeResolverBuilder.isTypeIdVisible());
        Assert.assertNull(stdTypeResolverBuilder._idType);
        Assert.assertNull(stdTypeResolverBuilder._includeAs);
        Assert.assertNull(stdTypeResolverBuilder._customIdResolver);
    }

    @Test
    public void testInitMethod() {
        JsonTypeInfo.Id idType = JsonTypeInfo.Id.CLASS;
        TypeIdResolver idRes = new ClassNameIdResolver(Integer.class, null);

        builder.init(idType, idRes);
        Assert.assertEquals(idType, builder._idType);
        Assert.assertEquals(idRes, builder._customIdResolver);
        Assert.assertNull(builder.getTypeProperty());
        Assert.assertFalse(builder.isTypeIdVisible());
        Assert.assertNull(builder.getDefaultImpl());
    }

    // Testing getters

    @Test
    public void testGetters() {
        JsonTypeInfo.Id idType = JsonTypeInfo.Id.CLASS;
        TypeIdResolver idRes = new ClassNameIdResolver(Integer.class, null);
        Class<?> defaultImpl = String.class;
        String typeProperty = "typeIdProp";
        boolean typeIdVisible = true;

        builder.init(idType, idRes).typeProperty(typeProperty).defaultImpl(defaultImpl).typeIdVisibility(typeIdVisible);

        Assert.assertEquals(idType, builder._idType);
        Assert.assertEquals(idRes, builder._customIdResolver);
        Assert.assertEquals(typeProperty, builder.getTypeProperty());
        Assert.assertEquals(defaultImpl, builder.getDefaultImpl());
        Assert.assertEquals(typeIdVisible, builder.isTypeIdVisible());
    }

    // Testing inclusion method

    @Test
    public void testInclusionMethod() {
        builder.inclusion(JsonTypeInfo.As.WRAPPER_ARRAY);
        Assert.assertEquals(JsonTypeInfo.As.WRAPPER_ARRAY, builder._includeAs);
    }

    // Testing typeProperty method

    @Test
    public void testTypePropertyMethod() {
        String typeProperty = "typeIdProp";
        builder.typeProperty(typeProperty);
        Assert.assertEquals(typeProperty, builder.getTypeProperty());
    }

    // Testing defaultImpl method

    @Test
    public void testDefaultImplMethod() {
        Class<?> defaultImpl = String.class;
        builder.defaultImpl(defaultImpl);
        Assert.assertEquals(defaultImpl, builder.getDefaultImpl());
    }

    // Testing typeIdVisibility method

    @Test
    public void testTypeIdVisibilityMethod() {
        builder.typeIdVisibility(true);
        Assert.assertTrue(builder.isTypeIdVisible());
    }

    // Testing buildTypeSerializer method

    @Test
    public void testBuildTypeSerializer() throws IOException {
        JsonTypeInfo.Id idType = JsonTypeInfo.Id.CLASS;
        TypeIdResolver idRes = new ClassNameIdResolver(Integer.class, null);
        JavaType baseType = mapper.getTypeFactory().constructType(List.class);
        Collection<NamedType> subtypes = Arrays.asList(mapper.getTypeFactory().constructType(Integer.class), mapper.getTypeFactory().constructType(String.class));

        TypeSerializer serializer = builder.init(idType, idRes).buildTypeSerializer(null, baseType, subtypes);
        Assert.assertNotNull(serializer);
    }

    // Testing buildTypeDeserializer method

    @Test
    public void testBuildTypeDeserializer() throws IOException {
        JsonTypeInfo.Id idType = JsonTypeInfo.Id.CLASS;
        TypeIdResolver idRes = new ClassNameIdResolver(Integer.class, null);
        JavaType baseType = mapper.getTypeFactory().constructType(List.class);
        Collection<NamedType> subtypes = Arrays.asList(mapper.getTypeFactory().constructType(Integer.class), mapper.getTypeFactory().constructType(String.class));

        TypeDeserializer deserializer = builder.init(idType, idRes).buildTypeDeserializer(null, baseType, subtypes);
        Assert.assertNotNull(deserializer);
    }
}

class TestBean {
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = FromStringDeserializer.class)
    private String typeId;
}

class ToStringSerializer extends StdSerializer<String> {
    public ToStringSerializer() {
        this(null);
    }

    public ToStringSerializer(Class<String> t) {
        this(null);
    }

    public ToStringSerializer(Class<String> t, PropertyName c) {
        super(String.class, c);
    }

    @Override
    public String serialize(String value, com.fasterxml.jackson.core.JsonGenerator jgen) throws IOException {
        jgen.writeString(value);
        return null;
    }
}

class FromStringDeserializer extends StdDeserializer<String> {
    public FromStringDeserializer() {
        this(null);
    }

    public FromStringDeserializer(Class<?> c) {
        super(String.class, c);
    }

    @Override
    public String deserialize(com.fasterxml.jackson.core.JsonParser p, DeserializationContext ctxt) throws IOException {
        return p.getValueAsString();
    }
}