import org.junit.Assert;
import org.junit.Test;

public class NumberSerializersTest {

    @Test
    public void testShortSerializer() {
        ShortSerializer shortSerializer = new ShortSerializer();
        
        // typical use case
        Short value = 10;
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        shortSerializer.serialize(value, gen, provider);
        // validate that gen.writeNumber() is called with correct value
        verify(gen, times(1)).writeNumber(value.shortValue());
        
        // edge case
        value = Short.MAX_VALUE;
        shortSerializer.serialize(value, gen, provider);
        // validate that gen.writeNumber() is called with correct value      
        verify(gen, times(1)).writeNumber(value.shortValue());
        
        // error scenario
        value = null;
        try {
            shortSerializer.serialize(value, gen, provider);
            Assert.fail("NullPointerException expected");
        } catch (IOException ex) {
            // validate that NullPointerException is thrown
            Assert.assertTrue(ex instanceof NullPointerException);
        }
    }

    @Test
    public void testIntegerSerializer() {
        IntegerSerializer integerSerializer = new IntegerSerializer();
        
        // typical use case
        Integer value = 10;
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        integerSerializer.serialize(value, gen, provider);
        // validate that gen.writeNumber() is called with correct value
        verify(gen, times(1)).writeNumber(value.intValue());
        
        // edge case
        value = Integer.MAX_VALUE;
        integerSerializer.serialize(value, gen, provider);
        // validate that gen.writeNumber() is called with correct value
        verify(gen, times(1)).writeNumber(value.intValue());
        
        // error scenario
        value = null;
        try {
            integerSerializer.serialize(value, gen, provider);
            Assert.fail("NullPointerException expected");
        } catch (IOException ex) {
            // validate that NullPointerException is thrown
            Assert.assertTrue(ex instanceof NullPointerException);
        }
    }

    @Test
    public void testIntLikeSerializer() {
        IntLikeSerializer intLikeSerializer = new IntLikeSerializer();
        
        // typical use case
        Integer value = 10;
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        intLikeSerializer.serialize(value, gen, provider);
        // validate that gen.writeNumber() is called with correct value
        verify(gen, times(1)).writeNumber(value.intValue());
        
        // edge case
        value = Integer.MAX_VALUE;
        intLikeSerializer.serialize(value, gen, provider);
        // validate that gen.writeNumber() is called with correct value
        verify(gen, times(1)).writeNumber(value.intValue());
        
        // error scenario
        value = null;
        try {
            intLikeSerializer.serialize(value, gen, provider);
            Assert.fail("NullPointerException expected");
        } catch (IOException ex) {
            // validate that NullPointerException is thrown
            Assert.assertTrue(ex instanceof NullPointerException);
        }
    }

    @Test
    public void testLongSerializer() {
        LongSerializer longSerializer = new LongSerializer();
        
        // typical use case
        Long value = 10L;
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        longSerializer.serialize(value, gen, provider);
        // validate that gen.writeNumber() is called with correct value
        verify(gen, times(1)).writeNumber(value.longValue());
        
        // edge case
        value = Long.MAX_VALUE;
        longSerializer.serialize(value, gen, provider);
        // validate that gen.writeNumber() is called with correct value
        verify(gen, times(1)).writeNumber(value.longValue());
        
        // error scenario
        value = null;
        try {
            longSerializer.serialize(value, gen, provider);
            Assert.fail("NullPointerException expected");
        } catch (IOException ex) {
            // validate that NullPointerException is thrown
            Assert.assertTrue(ex instanceof NullPointerException);
        }
    }

    @Test
    public void testFloatSerializer() {
        FloatSerializer floatSerializer = new FloatSerializer();
        
        // typical use case
        Float value = 10.5f;
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        floatSerializer.serialize(value, gen, provider);
        // validate that gen.writeNumber() is called with correct value
        verify(gen, times(1)).writeNumber(value.floatValue());
        
        // edge case
        value = Float.MAX_VALUE;
        floatSerializer.serialize(value, gen, provider);
        // validate that gen.writeNumber() is called with correct value
        verify(gen, times(1)).writeNumber(value.floatValue());
        
        // error scenario
        value = null;
        try {
            floatSerializer.serialize(value, gen, provider);
            Assert.fail("NullPointerException expected");
        } catch (IOException ex) {
            // validate that NullPointerException is thrown
            Assert.assertTrue(ex instanceof NullPointerException);
        }
    }

    @Test
    public void testDoubleSerializer() {
        DoubleSerializer doubleSerializer = new DoubleSerializer();
        
        // typical use case
        Double value = 10.5;
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        doubleSerializer.serialize(value, gen, provider);
        // validate that gen.writeNumber() is called with correct value
        verify(gen, times(1)).writeNumber(value.doubleValue());
        
        // edge case
        value = Double.MAX_VALUE;
        doubleSerializer.serialize(value, gen, provider);
        // validate that gen.writeNumber() is called with correct value
        verify(gen, times(1)).writeNumber(value.doubleValue());
        
        // error scenario
        value = null;
        try {
            doubleSerializer.serialize(value, gen, provider);
            Assert.fail("NullPointerException expected");
        } catch (IOException ex) {
            // validate that NullPointerException is thrown
            Assert.assertTrue(ex instanceof NullPointerException);
        }
    }
}