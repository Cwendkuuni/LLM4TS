import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class JsonValueSerializerTest {
    private JsonValueSerializer serializer = new JsonValueSerializer(null, null);

    @Test
    public void testSerialize() {
        // Test case 1: Null value
        JsonGeneratorMock generator = new JsonGeneratorMock();
        serializer.serialize(null, generator, null);
        assertNull(generator.getResult());

        // Test case 2: Non-null value
        generator = new JsonGeneratorMock();
        serializer.serialize("test", generator, null);
        assertEquals("test", generator.getResult());
    }

    @Test
    public void testSerializeWithType() {
        // Test case 1: Null value
        JsonGeneratorMock generator = new JsonGeneratorMock();
        TypeSerializerMock typeSerializer = new TypeSerializerMock();
        serializer.serializeWithType(null, generator, null, typeSerializer);
        assertNull(generator.getResult());

        // Test case 2: Non-null value
        generator = new JsonGeneratorMock();
        typeSerializer = new TypeSerializerMock();
        serializer.serializeWithType("test", generator, null, typeSerializer);
        assertEquals("test", generator.getResult());
        assertEquals("test", typeSerializer.getWrittenValue());
    }

    @Test
    public void testGetSchema() {
        // Test case 1: SchemaAware value serializer
        SchemaAwareValueSerializerMock valueSerializer = new SchemaAwareValueSerializerMock();
        serializer = new JsonValueSerializer(null, valueSerializer);
        assertNull(serializer.getSchema(null, null));

        // Test case 2: Non-SchemaAware value serializer
        serializer = new JsonValueSerializer(null, null);
        assertNotNull(serializer.getSchema(null, null));
    }

    @Test
    public void testAcceptJsonFormatVisitor() {
        // Test case 1: Enum type
        JsonFormatVisitorWrapperMock visitor = new JsonFormatVisitorWrapperMock();
        JavaType enumType = new JavaTypeMock(Enum.class);
        serializer.acceptJsonFormatVisitor(visitor, enumType);
        assertTrue(visitor.isStringVisitorCalled());

        // Test case 2: Non-enum type
        visitor = new JsonFormatVisitorWrapperMock();
        JavaType nonEnumType = new JavaTypeMock(String.class);
        serializer.acceptJsonFormatVisitor(visitor, nonEnumType);
        assertFalse(visitor.isStringVisitorCalled());
    }

    @Test
    public void testToString() {
        String className = JsonValueSerializer.class.getName();
        String expected = "(@JsonValue serializer for method " + className + "#" + null + ")";
        assertEquals(expected, serializer.toString());
    }
}