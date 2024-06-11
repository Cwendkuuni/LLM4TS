import org.junit.*;
import static org.junit.Assert.*;

public class NumberSerializersTest {

    @Test
    public void testShortSerializerSerialize() {
        // Test ShortSerializer's serialize method
        ShortSerializer shortSerializer = new ShortSerializer();
        Short value = 10;
        try {
            shortSerializer.serialize(value, null, null);
            // Assert some condition here if needed
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testIntegerSerializerSerialize() {
        // Test IntegerSerializer's serialize method
        IntegerSerializer integerSerializer = new IntegerSerializer();
        Integer value = 20;
        try {
            integerSerializer.serialize(value, null, null);
            // Assert some condition here if needed
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    // Add more test methods for the remaining serializers, e.g. Long, Float, Double, and IntLike

}