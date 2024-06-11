import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.MapperConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

@SuppressWarnings("unchecked")
public class StdTypeResolverBuilderTest {

    private StdTypeResolverBuilder builder;
    private MapperConfig config;
    private ObjectMapper mapper;

    @BeforeMethod
    public void setUp() {
        config = mock(MapperConfig.class);
        mapper = new ObjectMapper();
        mapper.setConfig(config);
        builder = new StdTypeResolverBuilder();
    }

    @Test
    public void testEmptyBuilder() {
        assertEquals(builder.buildTypeSerializer(config, null, null), null);
        assertEquals(builder.buildTypeDeserializer(config, null, null), null);
    }

    @Test
    public void testNoTypeInfoBuilder() {
        TypeSerializer serializer = builder.noTypeInfoBuilder().buildTypeSerializer(config, null, null);
        assertNull(serializer);
    }

    @Test
    public void testInitialization() {
        builder.init(JsonTypeInfo.Id.CLASS, null);
        assertEquals(builder.getIdType(), JsonTypeInfo.Id.CLASS);
        assertNull(builder.getCustomIdResolver());
        assertEquals(builder.getTypeProperty(), JsonTypeInfo.defaultPropertyName("type"));
        assertFalse(builder.isTypeIdVisible());
        assertNull(builder.getDefaultImpl());

        builder.init(JsonTypeInfo.Id.MINIMAL_CLASS, new MinimalClassNameIdResolver(JavaType.construct(), mapper.getTypeFactory()));
        assertEquals(builder.getIdType(), JsonTypeInfo.Id.MINIMAL_CLASS);
        assertNotNull(builder.getCustomIdResolver());
        assertEquals(builder.getTypeProperty(), JsonTypeInfo.defaultPropertyName("type"));
        assertFalse(builder.isTypeIdVisible());
        assertNull(builder.getDefaultImpl());

        builder.init(JsonTypeInfo.Id.NAME, TypeNameIdResolver.construct(config, null, null, true, true));
        assertEquals(builder.getIdType(), JsonTypeInfo.Id.NAME);
        assertNotNull(builder.getCustomIdResolver());
        assertEquals(builder.getTypeProperty(), null);
        assertFalse(builder.isTypeIdVisible());
        assertNull(builder.getDefaultImpl());

        builder.init(JsonTypeInfo.Id.CUSTOM, mock(TypeIdResolver.class));
        assertEquals(builder.getIdType(), JsonTypeInfo.Id.CUSTOM);
        assertNotNull(builder.getCustomIdResolver());
        assertEquals(builder.getTypeProperty(), JsonTypeInfo.defaultPropertyName("type"));
        assertFalse(builder.isTypeIdVisible());
        assertNull(builder.getDefaultImpl());
    }

    @Test
    public void testBuildTypeSerializer() {
        builder.init(JsonTypeInfo.Id.PROPERTY, null);
        builder.inclusion(JsonTypeInfo.As.PROPERTY);
        builder.typeProperty("customPropertyName");

        JavaType baseType = JavaType.construct();
        Collection<NamedType> subtypes = mock(Collection.class);

        TypeSerializer serializer = builder.buildTypeSerializer(config, baseType, subtypes);
        assertNotNull(serializer);
        verify(config, times(1)).getTypeFactory();
        verifyNoMoreInteractions(config);

        assertEquals(serializer.getClass(), AsPropertyTypeSerializer.class);
        assertEquals(((AsPropertyTypeSerializer) serializer).getPropertyName(), "customPropertyName");
    }

    @Test
    public void testBuildTypeDeserializer() {
        builder.init(JsonTypeInfo.Id.PROPERTY, null);
        builder.inclusion(JsonTypeInfo.As.PROPERTY);
        builder.typeProperty("customPropertyName");

        JavaType baseType = JavaType.construct();
        Collection<NamedType> subtypes = mock(Collection.class);

        TypeDeserializer deserializer = builder.buildTypeDeserializer(config, baseType, subtypes);
        assertNotNull(deserializer);
        verify(config, times(1)).getTypeFactory();
        verifyNoMoreInteractions(config);

        assertEquals(deserializer.getClass(), AsPropertyTypeDeserializer.class);
        assertEquals(((AsPropertyTypeDeserializer) deserializer).getPropertyName(), "customPropertyName");
    }

    @Test
    public void testBuildTypeDeserializerWithDefaultImpl() {
        builder.init(JsonTypeInfo.Id.PROPERTY, null);
        builder.inclusion(JsonTypeInfo.As.PROPERTY);
        builder.typeProperty("customPropertyName");
        builder.defaultImpl(String.class);

        JavaType baseType = JavaType.construct();
        Collection<NamedType> subtypes = mock(Collection.class);

        TypeDeserializer deserializer = builder.buildTypeDeserializer(config, baseType, subtypes);
        assertNotNull(deserializer);
        verify(config, times(1)).getTypeFactory();
        verifyNoMoreInteractions(config);

        assertEquals(deserializer.getClass(), AsPropertyTypeDeserializer.class);
        assertEquals(((AsPropertyTypeDeserializer) deserializer).getPropertyName(), "customPropertyName");
        assertEquals(((AsPropertyTypeDeserializer) deserializer).getDefaultImpl(), String.class);
    }
}