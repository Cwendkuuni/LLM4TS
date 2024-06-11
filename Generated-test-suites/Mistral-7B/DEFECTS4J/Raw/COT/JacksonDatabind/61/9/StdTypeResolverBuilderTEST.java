import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.type.ClassType;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.NoClass;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.jsontype.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.*;

@RunWith(PowerMockRunner.class)
public class StdTypeResolverBuilderTest {

    @Test
    public void testConstructor() {
        new StdTypeResolverBuilder();
        new StdTypeResolverBuilder().init(JsonTypeInfo.Id.CLASS, null);
    }

    @Test
    public void testInitWithNullIdType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        try {
            builder.init(null, null);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("idType can not be null"));
        }
    }

    @Test
    public void testBuildTypeSerializerWithNullBaseType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder()
                .init(JsonTypeInfo.Id.CLASS, null);
        TypeDeserializer deserializer = Mockito.mock(TypeDeserializer.class);
        JavaType javaType = Mockito.mock(JavaType.class);
        Collection<NamedType> subtypes = Mockito.mock(Collection.class);
        TypeSerializer result = builder.buildTypeSerializer(null, javaType, subtypes);
        assertEquals(null, result);
    }

    @Test
    public void testBuildTypeSerializerWithValidArguments() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder()
                .init(JsonTypeInfo.Id.CLASS, null);
        TypeDeserializer deserializer = Mockito.mock(TypeDeserializer.class);
        TypeIdResolver idResolver = Mockito.mock(TypeIdResolver.class);
        JavaType javaType = Mockito.mock(JavaType.class);
        Collection<NamedType> subtypes = Mockito.mock(Collection.class);
        PowerMockito.when(idResolver.getIdResolverName()).thenReturn("idResolverName");
        PowerMockito.when(idResolver.getIdValueSerializer()).thenReturn(deserializer);
        PowerMockito.when(javaType.getRawClass()).thenReturn(String.class);
        PowerMockito.when(subtypes.iterator().next()).thenReturn(Mockito.mock(NamedType.class));
        PowerMockito.when(subtypes.size()).thenReturn(1);

        TypeSerializer result = builder.buildTypeSerializer(Mockito.mock(SerializationConfig.class), javaType, subtypes);
        assertNotNull(result);
        assertEquals("idResolverName", result.getIdResolverName());
        assertEquals(deserializer, result.getIdValueSerializer());
    }

    @Test
    public void testBuildTypeDeserializerWithNullBaseType() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder()
                .init(JsonTypeInfo.Id.CLASS, null);
        DeserializationConfig config = Mockito.mock(DeserializationConfig.class);
        JavaType javaType = Mockito.mock(JavaType.class);
        Collection<NamedType> subtypes = Mockito.mock(Collection.class);
        TypeDeserializer result = builder.buildTypeDeserializer(config, javaType, subtypes);
        assertEquals(null, result);
    }

    @Test
    public void testBuildTypeDeserializerWithValidArguments() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder()
                .init(JsonTypeInfo.Id.CLASS, null);
        DeserializationConfig config = Mockito.mock(DeserializationConfig.class);
        TypeIdResolver idResolver = Mockito.mock(TypeIdResolver.class);
        JavaType javaType = Mockito.mock(JavaType.class);
        Collection<NamedType> subtypes = Mockito.mock(Collection.class);
        PowerMockito.when(idResolver.getIdResolverName()).thenReturn("idResolverName");
        PowerMockito.when(idResolver.getIdValueDeserializer()).thenReturn(Mockito.mock(TypeDeserializer.class));
        PowerMockito.when(javaType.getRawClass()).thenReturn(String.class);
        PowerMockito.when(subtypes.iterator().next()).thenReturn(Mockito.mock(NamedType.class));
        PowerMockito.when(subtypes.size()).thenReturn(1);

        TypeDeserializer result = builder.buildTypeDeserializer(config, javaType, subtypes);
        assertNotNull(result);
        assertEquals("idResolverName", result.getIdResolverName());
        assertEquals(Mockito.mock(TypeDeserializer.class), result.getIdValueDeserializer());
    }

    @Test
    public void testTypePropertyWithNullArgument() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.typeProperty(null);
        assertEquals("", builder.getTypeProperty());
    }

    @Test
    public void testTypePropertyWithEmptyStringArgument() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.typeProperty("");
        assertEquals("", builder.getTypeProperty());
    }

    @Test
    public void testTypePropertyWithValidStringArgument() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.typeProperty("typeIdPropertyName");
        assertEquals("typeIdPropertyName", builder.getTypeProperty());
    }

    @Test
    public void testDefaultImplWithNullArgument() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.defaultImpl(null);
        assertNull(builder.getDefaultImpl());
    }

    @Test
    public void testDefaultImplWithStringArgument() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.defaultImpl("DefaultImplClass");
        assertEquals("DefaultImplClass", builder.getDefaultImpl().getName());
    }

    @Test
    public void testDefaultImplWithClassArgument() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        Class<?> defaultImpl = new ClassType(String.class).getRawClass();
        builder.defaultImpl(defaultImpl);
        assertEquals(defaultImpl, builder.getDefaultImpl());
    }

    @Test
    public void testTypeIdVisibilityWithBooleanArgument() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.typeIdVisibility(true);
        assertTrue(builder.isTypeIdVisible());

        builder.typeIdVisibility(false);
        assertFalse(builder.isTypeIdVisible());
    }

    @Test
    public void testIdResolverWithNullArguments() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        MapperConfig<?> config = Mockito.mock(MapperConfig.class);
        JavaType baseType = Mockito.mock(JavaType.class);
        Collection<NamedType> subtypes = Mockito.mock(Collection.class);
        TypeIdResolver result = builder.idResolver(config, baseType, subtypes, true, false);
        assertNull(result);
    }

    @Test
    public void testIdResolverWithValidArguments() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        MapperConfig<?> config = Mockito.mock(MapperConfig.class);
        JavaType baseType = Mockito.mock(JavaType.class);
        Collection<NamedType> subtypes = Mockito.mock(Collection.class);
        PowerMockito.when(config.getTypeFactory().constructType(Mockito.any(Class.class))).thenReturn(Mockito.mock(JavaType.class));
        TypeIdResolver result = builder.idResolver(config, baseType, subtypes, true, false);
        assertNotNull(result);
    }
}