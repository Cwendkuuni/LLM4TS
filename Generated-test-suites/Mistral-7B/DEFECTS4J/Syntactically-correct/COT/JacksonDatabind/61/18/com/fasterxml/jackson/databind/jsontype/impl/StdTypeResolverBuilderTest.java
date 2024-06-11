import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.core.type.ClassType;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.MapperFactoryBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.deser.std.AsArrayTypeDeserializer;
import com.fasterxml.jackson.databind.deser.std.AsExistingPropertyTypeDeserializer;
import com.fasterxml.jackson.databind.deser.std.AsPropertyTypeDeserializer;
import com.fasterxml.jackson.databind.deser.std.AsWrapperTypeDeserializer;
import com.fasterxml.jackson.databind.deser.std.AsExternalTypeDeserializer;
import com.fasterxml.jackson.databind.ser.std.AsArrayTypeSerializer;
import com.fasterxml.jackson.databind.ser.std.AsPropertyTypeSerializer;
import com.fasterxml.jackson.databind.ser.std.AsWrapperTypeSerializer;
import com.fasterxml.jackson.databind.ser.std.AsExternalTypeSerializer;
import com.fasterxml.jackson.databind.jsontype.ClassIdResolver;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class StdTypeResolverBuilderTest {

    @Test
    public void testDefaultConstructor() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        assertNull(builder.getIdType());
        assertNull(builder.getIncludeAs());
        assertNull(builder.getTypeProperty());
        assertFalse(builder.isTypeIdVisible());
        assertNull(builder.getDefaultImpl());
        assertNull(builder.getCustomIdResolver());
    }

    @Test
    public void testNoTypeInfoBuilder() {
        StdTypeResolverBuilder builder = StdTypeResolverBuilder.noTypeInfoBuilder();
        assertEquals(Id.NONE, builder.getIdType());
        assertNull(builder.getIncludeAs());
        assertNull(builder.getTypeProperty());
        assertFalse(builder.isTypeIdVisible());
        assertNull(builder.getDefaultImpl());
        assertNull(builder.getCustomIdResolver());
    }

    @Test
    public void testInit() {
        Id idType = Id.CLASS;
        TypeIdResolver idRes = new ClassNameIdResolver(JavaType.construct(), new TypeFactory());
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(idType, idRes);
        assertEquals(idType, builder.getIdType());
        assertEquals(idRes, builder.getCustomIdResolver());
        assertEquals(idType.getDefaultPropertyName(), builder.getTypeProperty());
        assertFalse(builder.isTypeIdVisible());
        assertNull(builder.getDefaultImpl());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitNullIdType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(null, new ClassNameIdResolver(JavaType.construct(), new TypeFactory()));
    }

    @Test
    public void testBuildTypeSerializerNullConfig() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        SerializationConfig config = null;
        JavaType baseType = JavaType.construct();
        Collection<NamedType> subtypes = null;
        TypeIdResolver idRes = new ClassNameIdResolver(JavaType.construct(), new TypeFactory());
        TypeSerializer serializer = builder.buildTypeSerializer(config, baseType, subtypes);
        assertNull(serializer);
    }

    @Test
    public void testBuildTypeSerializerClassType() {
        Id idType = Id.CLASS;
        TypeIdResolver idRes = new ClassNameIdResolver(JavaType.construct(), new TypeFactory());
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(idType, idRes);
        SerializationConfig config = new SerializationConfig();
        JavaType baseType = JavaType.construct();
        Collection<NamedType> subtypes = null;
        TypeSerializer serializer = builder.buildTypeSerializer(config, baseType, subtypes);
        assertNotNull(serializer);
    }

    @Test(expected = IllegalStateException.class)
    public void testBuildTypeSerializerUnknownIncludeAs() {
        Id idType = Id.CLASS;
        TypeIdResolver idRes = new ClassNameIdResolver(JavaType.construct(), new TypeFactory());
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(idType, idRes);
        SerializationConfig config = new SerializationConfig();
        JavaType baseType = JavaType.construct();
        Collection<NamedType> subtypes = null;
        builder.buildTypeSerializer(config, baseType, subtypes);
    }

    @Test
    public void testBuildTypeDeserializerNullConfig() {
        Id idType = Id.CLASS;
        TypeIdResolver idRes = new ClassNameIdResolver(JavaType.construct(), new TypeFactory());
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(idType, idRes);
        DeserializationConfig config = null;
        JavaType baseType = JavaType.construct();
        Collection<NamedType> subtypes = null;
        TypeDeserializer deserializer = builder.buildTypeDeserializer(config, baseType, subtypes);
        assertNull(deserializer);
    }

    @Test
    public void testBuildTypeDeserializerClassType() {
        Id idType = Id.CLASS;
        TypeIdResolver idRes = new ClassNameIdResolver(JavaType.construct(), new TypeFactory());
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(idType, idRes);
        DeserializationConfig config = new DeserializationConfig();
        JavaType baseType = JavaType.construct();
        Collection<NamedType> subtypes = null;
        TypeDeserializer deserializer = builder.buildTypeDeserializer(config, baseType, subtypes);
        assertNotNull(deserializer);
    }

    @Test(expected = IllegalStateException.class)
    public void testBuildTypeDeserializerUnknownIncludeAs() {
        Id idType = Id.CLASS;
        TypeIdResolver idRes = new ClassNameIdResolver(JavaType.construct(), new TypeFactory());
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(idType, idRes);
        DeserializationConfig config = new DeserializationConfig();
        JavaType baseType = JavaType.construct();
        Collection<NamedType> subtypes = null;
        builder.buildTypeDeserializer(config, baseType, subtypes);
    }

    @Test
    public void testInclusion() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.inclusion(As.PROPERTY);
        assertEquals(As.PROPERTY, builder.getIncludeAs());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInclusionNullIncludeAs() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.inclusion(null);
    }

    @Test
    public void testTypeProperty() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.typeProperty("myProperty");
        assertEquals("myProperty", builder.getTypeProperty());
    }

    @Test
    public void testDefaultImpl() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        Class<?> defaultImpl = Object.class;
        builder.defaultImpl(defaultImpl);
        assertEquals(defaultImpl, builder.getDefaultImpl());
    }

    @Test
    public void testTypeIdVisibility() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        boolean isVisible = true;
        builder.typeIdVisibility(isVisible);
        assertEquals(isVisible, builder.isTypeIdVisible());
    }

    @Test
    public void testGetDefaultImpl() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.defaultImpl(Object.class);
        assertEquals(Object.class, builder.getDefaultImpl());
    }

    @Test
    public void testGetTypeProperty() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.typeProperty("myProperty");
        assertEquals("myProperty", builder.getTypeProperty());
    }

    @Test
    public void testIsTypeIdVisible() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.typeIdVisibility(true);
        assertTrue(builder.isTypeIdVisible());
    }

    @Test
    public void testIdResolver() {
        Id idType = Id.CLASS;
        TypeFactory typeFactory = new TypeFactory();
        JavaType baseType = JavaType.construct();
        Collection<NamedType> subtypes = null;
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(idType, null);
        TypeIdResolver idResolver = builder.idResolver(new MapperFactoryBuilder().getMapperConfig(), baseType, subtypes, true, false);
        assertNotNull(idResolver);
    }
}