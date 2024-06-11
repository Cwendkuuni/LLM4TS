import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdTypeResolverBuilderTest {

    private StdTypeResolverBuilder stdTypeResolverBuilder;

    @Before
    public void setup() {
        stdTypeResolverBuilder = new StdTypeResolverBuilder();
    }

    @Test
    public void testInit() {
        JsonTypeInfo.Id idType = JsonTypeInfo.Id.CLASS;
        TypeIdResolver typeIdResolver = new ClassNameIdResolver();
        stdTypeResolverBuilder.init(idType, typeIdResolver);
        
        assertEquals(idType, stdTypeResolverBuilder._idType);
        assertEquals(typeIdResolver, stdTypeResolverBuilder._customIdResolver);
        assertEquals(idType.getDefaultPropertyName(), stdTypeResolverBuilder._typeProperty);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitWithNullIdType() {
        stdTypeResolverBuilder.init(null, new ClassNameIdResolver());
    }

    @Test
    public void testBuildTypeSerializer() {
        SerializationConfig config = new SerializationConfig();
        JavaType baseType = new JavaType();
        Collection<NamedType> subtypes = new ArrayList<>();
        stdTypeResolverBuilder.init(JsonTypeInfo.Id.NAME, new TypeNameIdResolver());
        stdTypeResolverBuilder.inclusion(JsonTypeInfo.As.WRAPPER_ARRAY);

        TypeSerializer typeSerializer = stdTypeResolverBuilder.buildTypeSerializer(config, baseType, subtypes);

        assertTrue(typeSerializer instanceof AsArrayTypeSerializer);
        assertEquals(JsonTypeInfo.As.WRAPPER_ARRAY, stdTypeResolverBuilder._includeAs);
    }

    @Test(expected = IllegalStateException.class)
    public void testBuildTypeSerializerWithInvalidInclusionType() {
        SerializationConfig config = new SerializationConfig();
        JavaType baseType = new JavaType();
        Collection<NamedType> subtypes = new ArrayList<>();
        stdTypeResolverBuilder.init(JsonTypeInfo.Id.NAME, new TypeNameIdResolver());
        stdTypeResolverBuilder.inclusion(null);

        stdTypeResolverBuilder.buildTypeSerializer(config, baseType, subtypes);
    }

    @Test
    public void testBuildTypeDeserializer() {
        DeserializationConfig config = new DeserializationConfig();
        JavaType baseType = new JavaType();
        Collection<NamedType> subtypes = new ArrayList<>();
        stdTypeResolverBuilder.init(JsonTypeInfo.Id.NAME, new TypeNameIdResolver());
        stdTypeResolverBuilder.inclusion(JsonTypeInfo.As.WRAPPER_OBJECT);
        stdTypeResolverBuilder.defaultImpl(String.class);

        TypeDeserializer typeDeserializer = stdTypeResolverBuilder.buildTypeDeserializer(config, baseType, subtypes);

        assertTrue(typeDeserializer instanceof AsWrapperTypeDeserializer);
        assertEquals(JsonTypeInfo.As.WRAPPER_OBJECT, stdTypeResolverBuilder._includeAs);
        assertEquals(String.class, stdTypeResolverBuilder._defaultImpl);
        assertTrue(stdTypeResolverBuilder._typeIdVisible);
    }

    @Test(expected = IllegalStateException.class)
    public void testBuildTypeDeserializerWithInvalidInclusionType() {
        DeserializationConfig config = new DeserializationConfig();
        JavaType baseType = new JavaType();
        Collection<NamedType> subtypes = new ArrayList<>();
        stdTypeResolverBuilder.init(JsonTypeInfo.Id.NAME, new TypeNameIdResolver());
        stdTypeResolverBuilder.inclusion(null);

        stdTypeResolverBuilder.buildTypeDeserializer(config, baseType, subtypes);
    }

    @Test
    public void testInclusion() {
        JsonTypeInfo.As includeAs = JsonTypeInfo.As.WRAPPER_ARRAY;

        stdTypeResolverBuilder.inclusion(includeAs);

        assertEquals(includeAs, stdTypeResolverBuilder._includeAs);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInclusionWithNullIncludeAs() {
        stdTypeResolverBuilder.inclusion(null);
    }

    @Test
    public void testTypeProperty() {
        String typeIdPropName = "type";

        stdTypeResolverBuilder.typeProperty(typeIdPropName);

        assertEquals(typeIdPropName, stdTypeResolverBuilder._typeProperty);
    }

    @Test
    public void testTypePropertyWithNullTypeIdPropName() {
        stdTypeResolverBuilder.typeProperty(null);
        assertEquals(stdTypeResolverBuilder._idType.getDefaultPropertyName(), stdTypeResolverBuilder._typeProperty);
    }

    @Test
    public void testTypePropertyWithEmptyTypeIdPropName() {
        stdTypeResolverBuilder.typeProperty("");
        assertEquals(stdTypeResolverBuilder._idType.getDefaultPropertyName(), stdTypeResolverBuilder._typeProperty);
    }

    @Test
    public void testDefaultImpl() {
        Class<?> defaultImpl = String.class;

        stdTypeResolverBuilder.defaultImpl(defaultImpl);

        assertEquals(defaultImpl, stdTypeResolverBuilder._defaultImpl);
    }

    @Test
    public void testTypeIdVisibility() {
        boolean isVisible = true;

        stdTypeResolverBuilder.typeIdVisibility(isVisible);

        assertEquals(isVisible, stdTypeResolverBuilder._typeIdVisible);
    }

    @Test
    public void testGetDefaultImpl() {
        Class<?> defaultImpl = stdTypeResolverBuilder.getDefaultImpl();

        assertNull(defaultImpl);
    }

    @Test
    public void testGetTypeProperty() {
        String typeProperty = stdTypeResolverBuilder.getTypeProperty();

        assertEquals(stdTypeResolverBuilder._idType.getDefaultPropertyName(), typeProperty);
    }

    @Test
    public void testIsTypeIdVisible() {
        boolean typeIdVisible = stdTypeResolverBuilder.isTypeIdVisible();

        assertFalse(typeIdVisible);
    }
}