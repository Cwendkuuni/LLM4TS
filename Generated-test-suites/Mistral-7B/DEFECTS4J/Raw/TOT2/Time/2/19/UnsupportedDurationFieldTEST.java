import org.junit.Test;
import org.joda.time.DurationFieldType;
import static org.junit.Assert.*;

public class UnsupportedDurationFieldTest {

    @Test
    public void testGetInstance() {
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        UnsupportedDurationField sameInstance = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);

        assertSame(unsupported, sameInstance);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetInstanceWithNullType() {
        UnsupportedDurationField.getInstance(null);
    }

    @Test
    public void testGetInstanceWithSameTypeTwice() {
        UnsupportedDurationField firstInstance = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        UnsupportedDurationField secondInstance = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);

        assertSame(firstInstance, secondInstance);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetInstanceWithDifferentTypes() {
        UnsupportedDurationField firstInstance = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        UnsupportedDurationField secondInstance = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS);

        assertNotSame(firstInstance, secondInstance);
    }

    @Test
    public void testGetInstanceWithMultipleInstances() {
        UnsupportedDurationField firstInstance = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        UnsupportedDurationField secondInstance = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);
        UnsupportedDurationField thirdInstance = UnsupportedDurationField.getInstance(DurationFieldType.MILLISECONDS);

        assertSame(firstInstance, secondInstance);
        assertSame(firstInstance, thirdInstance);
    }
}