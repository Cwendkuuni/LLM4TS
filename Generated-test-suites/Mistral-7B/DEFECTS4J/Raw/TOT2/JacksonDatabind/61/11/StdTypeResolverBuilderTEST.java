import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import static org.junit.Assert.*;

public class StdTypeResolverBuilderTest {

    private StdTypeResolverBuilder builder;

    @Before
    public void setup() {
        builder = new StdTypeResolverBuilder();
    }

    @Test
    public void testInit() {
        TypeReference typeRef = new TypeReference<Collection<String>>() {};
        ObjectMapper mapper = new ObjectMapper();
        builder.init(JsonTypeInfo.Id.CLASS, mapper.getTypeIdResolver());
        StdTypeResolver resolver = builder.build();
        TypeSerializer serializer = resolver.buildTypeSerializer(mapper.getSerializationConfig(), typeRef, null);
        assertNotNull(serializer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitNullIdType() {
        TypeReference typeRef = new TypeReference<Collection<String>>() {};
        ObjectMapper mapper = new ObjectMapper();
        builder.init(null, mapper.getTypeIdResolver());
    }

    @Test
    public void testBuildTypeSerializer() {
        TypeReference typeRef = new TypeReference<Collection<String>>() {};
        ObjectMapper mapper = new ObjectMapper();
        builder.init(JsonTypeInfo.Id.CLASS, mapper.getTypeIdResolver());
        TypeSerializer serializer = builder.buildTypeSerializer(mapper.getSerializationConfig(), typeRef, null);
        assertNotNull(serializer);
        serializer.serialize(Collections.emptyList(), mapper.getSerializationConfig(), JsonWriterFactory.getDefaultInstance());
    }

    @Test(expected = NullPointerException.class)
    public void testBuildTypeSerializerNullBaseType() {
        ObjectMapper mapper = new ObjectMapper();
        builder.init(JsonTypeInfo.Id.CLASS, mapper.getTypeIdResolver());
        builder.buildTypeSerializer(mapper.getSerializationConfig(), null, null);
    }

    @Test(expected = NullPointerException.class)
    public void testBuildTypeSerializerNullCollection() {
        ObjectMapper mapper = new ObjectMapper();
        builder.init(JsonTypeInfo.Id.CLASS, mapper.getTypeIdResolver());
        TypeSerializer serializer = builder.buildTypeSerializer(mapper.getSerializationConfig(), null);
        serializer.serialize(null, mapper.getSerializationConfig(), JsonWriterFactory.getDefaultInstance());
    }

    @Test
    public void testBuildTypeDeserializer() {
        TypeReference typeRef = new TypeReference<Collection<String>>() {};
        ObjectMapper mapper = new ObjectMapper();
        builder.init(JsonTypeInfo.Id.CLASS, mapper.getTypeIdResolver());
        TypeDeserializer deserializer = builder.buildTypeDeserializer(mapper.getDeserializationConfig(), typeRef, null);
        assertNotNull(deserializer);
        deserializer.deserialize(mapper.getFactory().createParser("[]"), typeRef);
    }

    @Test(expected = NullPointerException.class)
    public void testBuildTypeDeserializerNullBaseType() {
        ObjectMapper mapper = new ObjectMapper();
        builder.init(JsonTypeInfo.Id.CLASS, mapper.getTypeIdResolver());
        builder.buildTypeDeserializer(mapper.getDeserializationConfig(), null);
    }

    @Test(expected = NullPointerException.class)
    public void testBuildTypeDeserializerNullCollection() {
        ObjectMapper mapper = new ObjectMapper();
        builder.init(JsonTypeInfo.Id.CLASS, mapper.getTypeIdResolver());
        TypeDeserializer deserializer = builder.buildTypeDeserializer(mapper.getDeserializationConfig(), null);
        deserializer.deserialize(mapper.getFactory().createParser("[]"), null);
    }
}