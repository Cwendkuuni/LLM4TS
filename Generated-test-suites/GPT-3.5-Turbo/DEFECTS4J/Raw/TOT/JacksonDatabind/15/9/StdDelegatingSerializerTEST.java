import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdDelegatingSerializerTest {

    private StdDelegatingSerializer serializer;

    @Before
    public void setup() {
        serializer = new StdDelegatingSerializer(new Converter<Object, Object>() {
            @Override
            public Object convert(Object value) {
                return value;
            }
        });
    }

    @Test
    public void testSerialize_NullValue_SerializesNull() {
        JsonGeneratorStub generatorStub = new JsonGeneratorStub();
        JsonSerializerStub delegateSerializerStub = new JsonSerializerStub();

        serializer._delegateSerializer = delegateSerializerStub;

        serializer.serialize(null, generatorStub, null);

        assertTrue(generatorStub.wasDefaultSerializeNullCalled());
        assertFalse(delegateSerializerStub.wasSerializeCalled());
    }

    @Test
    public void testSerialize_NonNullValue_DelegatesToDelegateSerializer() {
        JsonGeneratorStub generatorStub = new JsonGeneratorStub();
        JsonSerializerStub delegateSerializerStub = new JsonSerializerStub();

        serializer._delegateSerializer = delegateSerializerStub;

        serializer.serialize(new Object(), generatorStub, null);

        assertFalse(generatorStub.wasDefaultSerializeNullCalled());
        assertTrue(delegateSerializerStub.wasSerializeCalled());
    }

    @Test
    public void testSerializeWithType_NullValue_SerializesWithTypeNull() {
        JsonGeneratorStub generatorStub = new JsonGeneratorStub();
        JsonSerializerStub delegateSerializerStub = new JsonSerializerStub();
        TypeSerializerStub typeSerializerStub = new TypeSerializerStub();

        serializer._delegateSerializer = delegateSerializerStub;

        serializer.serializeWithType(null, generatorStub, null, typeSerializerStub);

        assertTrue(generatorStub.wasDefaultSerializeNullCalled());
        assertFalse(delegateSerializerStub.wasSerializeWithTypeCalled());
        assertFalse(typeSerializerStub.wasSerializeTypeCalled());
    }

    @Test
    public void testSerializeWithType_NonNullValue_DelegatesToDelegateSerializer() {
        JsonGeneratorStub generatorStub = new JsonGeneratorStub();
        JsonSerializerStub delegateSerializerStub = new JsonSerializerStub();
        TypeSerializerStub typeSerializerStub = new TypeSerializerStub();

        serializer._delegateSerializer = delegateSerializerStub;

        serializer.serializeWithType(new Object(), generatorStub, null, typeSerializerStub);

        assertFalse(generatorStub.wasDefaultSerializeNullCalled());
        assertTrue(delegateSerializerStub.wasSerializeWithTypeCalled());
        assertTrue(typeSerializerStub.wasSerializeTypeCalled());
    }

    @Test
    public void testIsEmpty_NullValue_DelegatesToDelegateSerializer() {
        JsonSerializerStub delegateSerializerStub = new JsonSerializerStub();
        serializer._delegateSerializer = delegateSerializerStub;

        serializer.isEmpty(null, null);

        assertTrue(delegateSerializerStub.isEmptyCalled());
    }

    @Test
    public void testIsEmpty_NonNullValue_DelegatesToDelegateSerializer() {
        JsonSerializerStub delegateSerializerStub = new JsonSerializerStub();
        serializer._delegateSerializer = delegateSerializerStub;

        serializer.isEmpty(null, new Object());

        assertTrue(delegateSerializerStub.isEmptyCalled());
    }

    @Test
    public void testGetSchema_DelegatesToDelegateSerializer() {
        JsonSerializerStub delegateSerializerStub = new JsonSerializerStub();
        serializer._delegateSerializer = delegateSerializerStub;

        serializer.getSchema(null, null);

        assertTrue(delegateSerializerStub.getSchemaCalled());
    }

    @Test
    public void testAcceptJsonFormatVisitor_DelegatesToDelegateSerializer() {
        JsonSerializerStub delegateSerializerStub = new JsonSerializerStub();
        serializer._delegateSerializer = delegateSerializerStub;

        serializer.acceptJsonFormatVisitor(null, null);

        assertTrue(delegateSerializerStub.acceptJsonFormatVisitorCalled());
    }
}