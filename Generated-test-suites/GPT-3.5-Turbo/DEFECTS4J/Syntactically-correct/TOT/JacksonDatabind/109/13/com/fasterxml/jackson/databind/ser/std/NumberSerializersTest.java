import com.fasterxml.jackson.databind.JsonSerializer;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class NumberSerializersTest {

    @Test
    public void testAddAll() {
        Map<String, JsonSerializer<?>> allDeserializers = new HashMap<>();
        NumberSerializers.addAll(allDeserializers);
        Assert.assertEquals(12, allDeserializers.size());
    }

    @Test
    public void testShortSerializer() {
        NumberSerializers.ShortSerializer shortSerializer = new NumberSerializers.ShortSerializer();
        Assert.assertEquals(Short.class, shortSerializer.handledType());
        // typical use case
        Assert.assertEquals("2", shortSerializer.serialize(new Short("2"), null, null).getValue());
        // edge case
        Assert.assertEquals("0", shortSerializer.serialize(null, null, null).getValue());
        // error scenario
        try {
            shortSerializer.serialize("string", null, null);
            Assert.fail("Should throw ClassCastException");
        } catch (ClassCastException e) {
            // expected exception
        }
    }

    @Test
    public void testIntegerSerializer() {
        NumberSerializers.IntegerSerializer integerSerializer = new NumberSerializers.IntegerSerializer(Integer.class);
        Assert.assertEquals(Integer.class, integerSerializer.handledType());
        // typical use case
        Assert.assertEquals("3", integerSerializer.serialize(3, null, null).getValue());
        // edge case
        Assert.assertEquals("0", integerSerializer.serialize(null, null, null).getValue());
        // error scenario
        try {
            integerSerializer.serialize("string", null, null);
            Assert.fail("Should throw ClassCastException");
        } catch (ClassCastException e) {
            // expected exception
        }
    }

    @Test
    public void testIntLikeSerializer() {
        NumberSerializers.IntLikeSerializer intLikeSerializer = new NumberSerializers.IntLikeSerializer();
        Assert.assertEquals(Number.class, intLikeSerializer.handledType());
        // typical use case
        Assert.assertEquals("4", intLikeSerializer.serialize(4, null, null).getValue());
        // edge case
        Assert.assertEquals("0", intLikeSerializer.serialize(null, null, null).getValue());
        // error scenario
        try {
            intLikeSerializer.serialize("string", null, null);
            Assert.fail("Should throw ClassCastException");
        } catch (ClassCastException e) {
            // expected exception
        }
    }

    @Test
    public void testLongSerializer() {
        NumberSerializers.LongSerializer longSerializer = new NumberSerializers.LongSerializer(Long.class);
        Assert.assertEquals(Long.class, longSerializer.handledType());
        // typical use case
        Assert.assertEquals("5", longSerializer.serialize(5L, null, null).getValue());
        // edge case
        Assert.assertEquals("0", longSerializer.serialize(null, null, null).getValue());
        // error scenario
        try {
            longSerializer.serialize("string", null, null);
            Assert.fail("Should throw ClassCastException");
        } catch (ClassCastException e) {
            // expected exception
        }
    }

    @Test
    public void testFloatSerializer() {
        NumberSerializers.FloatSerializer floatSerializer = new NumberSerializers.FloatSerializer();
        Assert.assertEquals(Float.class, floatSerializer.handledType());
        // typical use case
        Assert.assertEquals("6.0", floatSerializer.serialize(6.0f, null, null).getValue());
        // edge case
        Assert.assertEquals("0.0", floatSerializer.serialize(null, null, null).getValue());
        // error scenario
        try {
            floatSerializer.serialize("string", null, null);
            Assert.fail("Should throw ClassCastException");
        } catch (ClassCastException e) {
            // expected exception
        }
    }

    @Test
    public void testDoubleSerializer() {
        NumberSerializers.DoubleSerializer doubleSerializer = new NumberSerializers.DoubleSerializer(Double.class);
        Assert.assertEquals(Double.class, doubleSerializer.handledType());
        // typical use case
        Assert.assertEquals("7.0", doubleSerializer.serialize(7.0, null, null).getValue());
        // edge case
        Assert.assertEquals("0.0", doubleSerializer.serialize(null, null, null).getValue());
        // error scenario
        try {
            doubleSerializer.serialize("string", null, null);
            Assert.fail("Should throw ClassCastException");
        } catch (ClassCastException e) {
            // expected exception
        }
    }
}