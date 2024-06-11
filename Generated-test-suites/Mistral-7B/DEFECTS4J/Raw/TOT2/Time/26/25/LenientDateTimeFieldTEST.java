import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class LenientDateTimeFieldTest {

    @Test
    public void testIsLenient() {
        LenientDateTimeField lenientDateTimeField = new LenientDateTimeField(new DateTimeField() {
            @Override
            public int get(long instant) {
                return 0;
            }

            @Override
            public int getBaseUnit() {
                return 1;
            }

            @Override
            public int getPrecision() {
                return 1;
            }
        }, Chronology.UTC);
        assertTrue(lenientDateTimeField.isLenient());
    }
}