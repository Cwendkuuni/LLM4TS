import org.junit.Test;
import org.joda.time.DurationFieldType;
import org.joda.time.UnsupportedOperationException;

import static org.junit.Assert.*;

/**
 * Tests for {@link UnsupportedDurationField}.
 */
public class UnsupportedDurationFieldTest {

    /**
     * Tests getting an instance of UnsupportedDurationField.
     */
    @Test
    public void testGetInstance() {
        DurationFieldType type = DurationFieldType.SECONDS();
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(type);
        assertEquals(type, unsupported.getType());
    }

    /**
     * Tests that methods which should throw UnsupportedOperationException actually do so.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testUnsupportedMethods() {
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS());
        unsupported.getValue(1L);
        unsupported.getValueAsLong(1L);
        unsupported.getValue(1L, 0L);
        unsupported.getValueAsLong(1L, 0L);
        unsupported.getMillis(1);
        unsupported.getMillis(1L);
        unsupported.getMillis(1, 0L);
        unsupported.getMillis(1L, 0L);
        unsupported.add(0L, 1);
        unsupported.add(0L, 1L);
        unsupported.getDifference(0L, 1L);
        unsupported.getDifferenceAsLong(0L, 1L);
    }

    /**
     * Tests that compareTo returns 1 when compared to a supported field.
     */
    @Test
    public void testCompareToSupported() {
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS());
        DurationField supported = DurationFieldType.MINUTES().withValue(1);
        int result = unsupported.compareTo(supported);
        assertEquals(1, result);
    }

    /**
     * Tests that equals returns true when compared to an instance of the same unsupported field.
     */
    @Test
    public void testEqualsSame() {
        UnsupportedDurationField unsupported1 = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS());
        UnsupportedDurationField unsupported2 = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS());
        assertTrue(unsupported1.equals(unsupported2));
    }

    /**
     * Tests that equals returns false when compared to an instance of a different unsupported field.
     */
    @Test
    public void testEqualsDifferent() {
        UnsupportedDurationField seconds = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS());
        UnsupportedDurationField minutes = UnsupportedDurationField.getInstance(DurationFieldType.MINUTES());
        assertFalse(seconds.equals(minutes));
    }

    /**
     * Tests that equals returns false when compared to an instance of a supported field.
     */
    @Test
    public void testEqualsSupported() {
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS());
        DurationField supported = DurationFieldType.MINUTES().withValue(1);
        assertFalse(unsupported.equals(supported));
    }

    /**
     * Tests that hashCode is consistent.
     */
    @Test
    public void testHashCode() {
        UnsupportedDurationField unsupported = UnsupportedDurationField.getInstance(DurationFieldType.SECONDS());
        String name = unsupported.getName();
        UnsupportedDurationField unsupported2 = UnsupportedDurationField.getInstance(DurationFieldType.valueOf(name));
        int hashCode1 = unsupported.hashCode();
        int hashCode2 = unsupported2.hashCode();
        assertEquals(hashCode1, hashCode2);
    }
}