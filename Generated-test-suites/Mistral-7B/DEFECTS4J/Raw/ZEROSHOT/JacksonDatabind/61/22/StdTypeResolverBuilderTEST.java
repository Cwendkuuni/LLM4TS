import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.type.ClassIdType;
import com.fasterxml.jackson.core.type.Types;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.databind.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.databind.jsontype.*;
import junit.framework.TestCase;

public class StdTypeResolverBuilderTest {

    @SuppressWarnings("unchecked")
    private static ObjectMapper createObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper;
    }

    @SuppressWarnings("unchecked")
    private static JavaType createJavaType(Class<?> clazz) {
        return Types.constructType(clazz);
    }

    @SuppressWarnings("unchecked")
    private static Collection<NamedType> createNamedTypes(Class<?>... classes) {
        JavaType javaType = Types.constructMapType(Map.class, String.class, Types.collector().subTypeOf(Object.class).withoutAny());
        ObjectMapper objectMapper = createObjectMapper();
        return objectMapper.getTypeFactory().constructCollectionType(LinkedHashSet.class, Types.parameterizedType(NamedType.class, Class.class));

        // Alternatively, you can use this method for creating NamedTypes:
        // List<NamedType> namedTypes = new ArrayList<>();
        // for (Class<?> clazz : classes) {
        //     namedTypes.add(new NamedType(clazz, clazz.getSimpleName()));
        // }
        // return namedTypes;
    }

    @Test
    public void testEmptyBuilder() {
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        assertNull(builder.getCustomIdResolver());
        assertNull(builder.getIdType());
        assertNull(builder.getIncludeAs());
        assertNull(builder.getTypeProperty());
        assertNull(builder.getDefaultImpl());
        assertFalse(builder.isTypeIdVisible());
    }

    @Test
    public void testInitBuilder() {
        Id idType = JsonTypeInfo.Id.CLASS;
        TypeIdResolver customIdResolver = new ClassNameIdResolver(createJavaType(String.class), null);
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder();
        builder.init(idType, customIdResolver);

        assertEquals(idType, builder.getIdType());
        assertEquals(customIdResolver, builder.getCustomIdResolver());
        assertNull(builder.getIncludeAs());
        assertNull(builder.getTypeProperty());
        assertNull(builder.getDefaultImpl());
        assertFalse(builder.isTypeIdVisible());
    }

    @Test
    public void testBuildTypeSerializer() {
        Id idType = JsonTypeInfo.Id.CLASS;
        TypeIdResolver idResolver = new ClassNameIdResolver(createJavaType(String.class), null);
        As includeAs = As.PROPERTY;
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder()
                .init(idType, idResolver)
                .inclusion(includeAs);

        JsonSerializer<?> serializer = builder.buildTypeSerializer(null, createJavaType(String.class), createNamedTypes(String[].class));
        assertNotNull(serializer);

        // TODO: Add more test cases for different inclusion types and settings
    }

    @Test
    public void testBuildTypeDeserializer() {
        Id idType = JsonTypeInfo.Id.CLASS;
        TypeIdResolver idResolver = new ClassNameIdResolver(createJavaType(String.class), null);
        As includeAs = As.PROPERTY;
        StdTypeResolverBuilder builder = new StdTypeResolverBuilder()
                .init(idType, idResolver)
                .inclusion(includeAs)
                .typeProperty("typeProperty");

        JsonDeserializer<?> deserializer = builder.buildTypeDeserializer(null, createJavaType(String.class), createNamedTypes(String[].class));
        assertNotNull(deserializer);

        // TODO: Add more test cases for different inclusion types and settings
    }
}