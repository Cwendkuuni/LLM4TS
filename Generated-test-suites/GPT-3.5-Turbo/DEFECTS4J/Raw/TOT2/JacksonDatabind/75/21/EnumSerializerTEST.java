import static org.junit.Assert.*;
import org.junit.Test;

public class EnumSerializerTest {

    @Test
    public void testConstructor() {
        EnumValues enumValues = new EnumValues(EnumType.class);
        EnumSerializer enumSerializer = new EnumSerializer(enumValues, true);
        
        assertNotNull(enumSerializer);
        assertEquals(enumValues, enumSerializer.getEnumValues());
    }

    @Test
    public void testSerializeAsString() {
        EnumValues enumValues = new EnumValues(EnumType.class);
        EnumSerializer enumSerializer = new EnumSerializer(enumValues, false);
        
        StringWriter writer = new StringWriter();
        JsonGenerator gen = new JsonFactory().createGenerator(writer);
        SerializerProvider provider = new DefaultSerializerProvider.Impl();
        
        EnumType enumValue = EnumType.VALUE1;
        enumSerializer.serialize(enumValue, gen, provider);
        
        gen.flush();
        String jsonString = writer.toString();
        
        assertEquals("\"VALUE1\"", jsonString);
    }

    @Test
    public void testSerializeAsIndex() {
        EnumValues enumValues = new EnumValues(EnumType.class);
        EnumSerializer enumSerializer = new EnumSerializer(enumValues, true);
        
        StringWriter writer = new StringWriter();
        JsonGenerator gen = new JsonFactory().createGenerator(writer);
        SerializerProvider provider = new DefaultSerializerProvider.Impl();
        
        EnumType enumValue = EnumType.VALUE2;
        enumSerializer.serialize(enumValue, gen, provider);
        
        gen.flush();
        String jsonString = writer.toString();
        
        assertEquals("1", jsonString);
    }

    @Test
    public void testGetSchema() {
        EnumValues enumValues = new EnumValues(EnumType.class);
        EnumSerializer enumSerializer = new EnumSerializer(enumValues, true);
        
        SerializerProvider provider = new DefaultSerializerProvider.Impl();
        
        JsonNode schema = enumSerializer.getSchema(provider, null);
        
        assertNotNull(schema);
        assertTrue(schema.isObject());
        
        JsonNode enumNode = schema.get("enum");
        assertNotNull(enumNode);
        assertTrue(enumNode.isArray());
        assertEquals(3, enumNode.size());
    }

    @Test
    public void testAcceptJsonFormatVisitor() {
        EnumValues enumValues = new EnumValues(EnumType.class);
        EnumSerializer enumSerializer = new EnumSerializer(enumValues, false);
        
        SerializerProvider provider = new DefaultSerializerProvider.Impl();
        
        DummyJsonFormatVisitorWrapper visitor = new DummyJsonFormatVisitorWrapper(provider);
        
        enumSerializer.acceptJsonFormatVisitor(visitor, null);
        
        assertNotNull(visitor.getVisitedType());
        assertTrue(visitor.getVisitedType().isString());
        assertTrue(visitor.isEnumTypesVisited());
    }
}

class DummyJsonFormatVisitorWrapper extends JsonFormatVisitorWrapper.Base {
    private JavaType visitedType;
    private boolean enumTypesVisited;

    protected DummyJsonFormatVisitorWrapper(SerializerProvider prov) {
        super(prov);
    }

    @Override
    public JsonStringFormatVisitor expectStringFormat(JavaType convertedType) {
        visitedType = convertedType;
        return new DummyJsonStringFormatVisitor();
    }

    @Override
    public void callVisitor(JavaType type) {
        visitedType = type;
    }

    @Override
    public void enumTypes(Set<String> enums) {
        enumTypesVisited = true;
    }

    public JavaType getVisitedType() {
        return visitedType;
    }

    public boolean isEnumTypesVisited() {
        return enumTypesVisited;
    }
}