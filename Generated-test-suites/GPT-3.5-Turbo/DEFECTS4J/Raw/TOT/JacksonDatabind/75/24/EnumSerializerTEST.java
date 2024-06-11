import org.junit.Test;
import static org.junit.Assert.*;
import java.util.HashMap;
import java.util.Map;

public class EnumSerializerTest {

    @Test
    public void testSerializeAsIndex() {
        EnumSerializer serializer = new EnumSerializer(EnumValues.constructFromName(config, (Class<Enum<?>>) enumClass), true);

        // Typical use case
        MyEnum myEnum1 = MyEnum.VALUE1;
        int expectedIndex1 = 0;
        JsonGenerator generator1 = new JsonFactory().createGenerator(new ByteArrayOutputStream(), JsonEncoding.UTF8);
        serializer.serialize(myEnum1, generator1, new DefaultSerializerProvider());
        assertEquals(expectedIndex1, generator1.getIntegerValue());

        // Edge case
        MyEnum myEnum2 = MyEnum.VALUE2;
        int expectedIndex2 = 1;
        JsonGenerator generator2 = new JsonFactory().createGenerator(new ByteArrayOutputStream(), JsonEncoding.UTF8);
        serializer.serialize(myEnum2, generator2, new DefaultSerializerProvider());
        assertEquals(expectedIndex2, generator2.getIntegerValue());
    }

    @Test
    public void testSerializeAsString() {
        EnumValues values = EnumValues.constructFromName(config, (Class<Enum<?>>) enumClass);
        EnumSerializer serializer = new EnumSerializer(values, false);

        // Typical use case
        MyEnum myEnum1 = MyEnum.VALUE1;
        String expectedString1 = "Value 1";
        JsonGenerator generator1 = new JsonFactory().createGenerator(new ByteArrayOutputStream(), JsonEncoding.UTF8);
        serializer.serialize(myEnum1, generator1, new DefaultSerializerProvider());
        assertEquals(expectedString1, generator1.getText());

        // Edge case
        MyEnum myEnum2 = MyEnum.VALUE2;
        String expectedString2 = "Value 2";
        JsonGenerator generator2 = new JsonFactory().createGenerator(new ByteArrayOutputStream(), JsonEncoding.UTF8);
        serializer.serialize(myEnum2, generator2, new DefaultSerializerProvider());
        assertEquals(expectedString2, generator2.getText());
    }

    @Test
    public void testSerializeUsingToString() {
        EnumValues values = EnumValues.constructFromName(config, (Class<Enum<?>>) enumClass);
        EnumSerializer serializer = new EnumSerializer(values, null);

        MyEnum myEnum = MyEnum.VALUE1;
        String expectedString = myEnum.toString();
        JsonGenerator generator = new JsonFactory().createGenerator(new ByteArrayOutputStream(), JsonEncoding.UTF8);
        serializer.serialize(myEnum, generator, new DefaultSerializerProvider());
        assertEquals(expectedString, generator.getText());
    }

    @Test
    public void testSchemaWhenSerializeAsIndex() {
        EnumValues values = EnumValues.constructFromName(config, (Class<Enum<?>>) enumClass);
        EnumSerializer serializer = new EnumSerializer(values, true);

        MyEnum myEnum = MyEnum.VALUE1;
        int expectedSchemaType = JsonToken.VALUE_NUMBER_INT;
        SerializerProvider provider = new DefaultSerializerProvider();
        JsonNode schema = serializer.getSchema(provider, MyEnum.class);
        assertEquals(expectedSchemaType, schema.get("$schemaValue").intValue());
    }

    @Test
    public void testSchemaWhenSerializeAsString() {
        EnumValues values = EnumValues.constructFromName(config, (Class<Enum<?>>) enumClass);
        EnumSerializer serializer = new EnumSerializer(values, false);

        MyEnum myEnum = MyEnum.VALUE1;
        int expectedSchemaType = JsonToken.VALUE_STRING;
        SerializerProvider provider = new DefaultSerializerProvider();
        JsonNode schema = serializer.getSchema(provider, MyEnum.class);
        assertEquals(expectedSchemaType, schema.get("$schemaValue").intValue());
        assertTrue(schema.get("enum").isArray());
        assertEquals(2, schema.get("enum").size());
        assertEquals("Value 1", schema.get("enum").get(0).textValue());
        assertEquals("Value 2", schema.get("enum").get(1).textValue());
    }

    @Test
    public void testVisitIntFormat() {
        EnumValues values = EnumValues.constructFromName(config, (Class<Enum<?>>) enumClass);
        EnumSerializer serializer = new EnumSerializer(values, true);
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper();

        // Typical use case
        JavaType typeHint1 = new SimpleType(Integer.class);
        JsonParser.NumberType expectedNumberType1 = INT;
        serializer.visitIntFormat(visitor, typeHint1, expectedNumberType1);
        assertEquals(expectedNumberType1, visitor.getJsonNumberType());

        // Edge case
        JavaType typeHint2 = new SimpleType(Long.class);
        JsonParser.NumberType expectedNumberType2 = INT;
        serializer.visitIntFormat(visitor, typeHint2, expectedNumberType2);
        assertEquals(expectedNumberType2, visitor.getJsonNumberType());
    }

    @Test
    public void testAcceptStringFormatVisitor() {
        EnumValues values = EnumValues.constructFromName(config, (Class<Enum<?>>) enumClass);
        EnumSerializer serializer = new EnumSerializer(values, false);
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper();
        JavaType typeHint = new SimpleType(String.class);

        // Typical use case
        boolean writeEnumsUsingToString = true;
        boolean writeEnumsUsingToStringEnabled = true;
        boolean writeEnumsUsingToStringDisabled = false;

        // Case 1: writeEnumsUsingToString enabled
        serializer.acceptJsonFormatVisitor(visitor, typeHint);
        assertTrue(visitor.isStringFormatVisitor());
        assertTrue(visitor.getStringEnums().contains("Value 1"));
        assertTrue(visitor.getStringEnums().contains("Value 2"));

        // Case 2: writeEnumsUsingToString disabled
        writeEnumsUsingToString = false;
        writeEnumsUsingToStringEnabled = false;
        writeEnumsUsingToStringDisabled = true;

        serializer.acceptJsonFormatVisitor(visitor, typeHint);
        assertTrue(visitor.isStringFormatVisitor());
        assertTrue(visitor.getStringEnums().contains("Value1"));
        assertTrue(visitor.getStringEnums().contains("Value2"));
    }
}