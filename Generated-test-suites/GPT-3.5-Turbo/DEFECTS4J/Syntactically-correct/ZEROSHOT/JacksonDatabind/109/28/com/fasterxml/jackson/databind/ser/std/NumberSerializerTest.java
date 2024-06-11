import org.junit.Test;

import static org.junit.Assert.*;

public class NumberSerializerTest {

    @Test
    public void testSerialize() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        JsonGenerator g = new JsonGenerator();

        BigDecimal bigDecimal = new BigDecimal(10);
        serializer.serialize(bigDecimal, g, null);
        assertEquals(g.toString(), "10");

        BigInteger bigInteger = new BigInteger("10");
        serializer.serialize(bigInteger, g, null);
        assertEquals(g.toString(), "10");

        Long l = new Long(10);
        serializer.serialize(l, g, null);
        assertEquals(g.toString(), "10");

        Double d = new Double(10);
        serializer.serialize(d, g, null);
        assertEquals(g.toString(), "10");

        Float f = new Float(10);
        serializer.serialize(f, g, null);
        assertEquals(g.toString(), "10");

        Integer i = new Integer(10);
        serializer.serialize(i, g, null);
        assertEquals(g.toString(), "10");

        Byte b = new Byte(10);
        serializer.serialize(b, g, null);
        assertEquals(g.toString(), "10");

        Short s = new Short(10);
        serializer.serialize(s, g, null);
        assertEquals(g.toString(), "10");

        Number n = new Number(10);
        serializer.serialize(n, g, null);
        assertEquals(g.toString(), "10");
    }

    @Test
    public void testGetSchema() {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        SerializerProvider provider = new SerializerProvider();
        Type typeHint = new Type();

        JsonNode schema = serializer.getSchema(provider, typeHint);
        assertEquals(schema.getType(), "number");
    }

    @Test
    public void testAcceptJsonFormatVisitor() {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper();
        JavaType typeHint = new JavaType(Number.class);

        serializer.acceptJsonFormatVisitor(visitor, typeHint);

        // TODO: Assert something
    }
}