
import org.junit.Test;
import static org.junit.Assert.*;

public class StdDelegatingSerializerTest {

    @Test
    public void testSerialize() {

        StdDelegatingSerializer serializer = new StdDelegatingSerializer(null);

        // Test with null value
        JsonGeneratorStub jsonGenerator = new JsonGeneratorStub();
        SerializerProviderStub serializerProvider = new SerializerProviderStub();
        try {
            serializer.serialize(null, jsonGenerator, serializerProvider);
            assertTrue(jsonGenerator.isNullSerialized());
            assertTrue(serializerProvider.isDefaultSerializeNullCalled());
        } catch (IOException e) {
            fail("Unexpected exception");
        }
    }

    @Test
    public void testSerializeWithType() {

        StdDelegatingSerializer serializer = new StdDelegatingSerializer(null);

        // Test with null value
        JsonGeneratorStub jsonGenerator = new JsonGeneratorStub();
        SerializerProviderStub serializerProvider = new SerializerProviderStub();
        TypeSerializerStub typeSerializer = new TypeSerializerStub();
        try {
            serializer.serializeWithType(null, jsonGenerator, serializerProvider, typeSerializer);
            assertTrue(typeSerializer.isSerializeWithTypeCalled());
        } catch (IOException e) {
            fail("Unexpected exception");
        }
    }

    @Test
    public void testIsEmpty() {

        StdDelegatingSerializer serializer = new StdDelegatingSerializer(null);

        // Test with null value
        Object value = null;
        assertTrue(serializer.isEmpty(value));

        // Test with non-null value
        value = new Object();
        assertFalse(serializer.isEmpty(value));
    }

    @Test
    public void testIsEmptyWithSerializerProvider() {

        StdDelegatingSerializer serializer = new StdDelegatingSerializer(null);

        // Test with null value
        Object value = null;
        SerializerProviderStub serializerProvider = new SerializerProviderStub();
        assertTrue(serializer.isEmpty(serializerProvider, value));

        // Test with non-null value
        value = new Object();
        assertFalse(serializer.isEmpty(serializerProvider, value));
    }

    @Test
    public void testGetSchema() {

        StdDelegatingSerializer serializer = new StdDelegatingSerializer(null);

        // Test with SchemaAware delegate serializer
        MockSchemaAwareSerializer delegateSerializer = new MockSchemaAwareSerializer();
        serializer._delegateSerializer = delegateSerializer;
        SerializerProviderStub serializerProvider = new SerializerProviderStub();
        Type typeHint = Object.class;
        try {
            serializer.getSchema(serializerProvider, typeHint);
            assertTrue(delegateSerializer.isGetSchemaCalled());
        } catch (JsonMappingException e) {
            fail("Unexpected exception");
        }

        // Test with non-SchemaAware delegate serializer
        delegateSerializer = new MockSchemaAwareSerializer();
        serializer._delegateSerializer = delegateSerializer;
        serializerProvider = new SerializerProviderStub();
        try {
            serializer.getSchema(serializerProvider, typeHint);
            assertFalse(delegateSerializer.isGetSchemaCalled());
        } catch (JsonMappingException e) {
            fail("Unexpected exception");
        }
    }

    @Test
    public void testGetSchemaWithIsOptional() {

        StdDelegatingSerializer serializer = new StdDelegatingSerializer(null);

        // Test with SchemaAware delegate serializer
        MockSchemaAwareSerializer delegateSerializer = new MockSchemaAwareSerializer();
        serializer._delegateSerializer = delegateSerializer;
        SerializerProviderStub serializerProvider = new SerializerProviderStub();
        Type typeHint = Object.class;
        try {
            serializer.getSchema(serializerProvider, typeHint, true);
            assertTrue(delegateSerializer.isGetSchemaCalled());
        } catch (JsonMappingException e) {
            fail("Unexpected exception");
        }

        // Test with non-SchemaAware delegate serializer
        delegateSerializer = new MockSchemaAwareSerializer();
        serializer._delegateSerializer = delegateSerializer;
        serializerProvider = new SerializerProviderStub();
        try {
            serializer.getSchema(serializerProvider, typeHint, true);
            assertFalse(delegateSerializer.isGetSchemaCalled());
        } catch (JsonMappingException e) {
            fail("Unexpected exception");
        }
    }

    @Test
    public void testAcceptJsonFormatVisitor() {

        StdDelegatingSerializer serializer = new StdDelegatingSerializer(null);

        // Test with JsonFormatVisitorWrapper and JavaType
        MockJsonFormatVisitorWrapper visitorWrapper = new MockJsonFormatVisitorWrapper();
        JavaType typeHint = mock(JavaType.class);
        try {
            serializer.acceptJsonFormatVisitor(visitorWrapper, typeHint);
            assertTrue(visitorWrapper.isAcceptJsonFormatVisitorCalled());
        } catch (JsonMappingException e) {
            fail("Unexpected exception");
        }

        // Test with JsonFormatVisitorWrapper and JavaType
        visitorWrapper = new MockJsonFormatVisitorWrapper();
        try {
            serializer.acceptJsonFormatVisitor(visitorWrapper, typeHint);
            assertFalse(visitorWrapper.isAcceptJsonFormatVisitorCalled());
        } catch (JsonMappingException e) {
            fail("Unexpected exception");
        }
    }
}
