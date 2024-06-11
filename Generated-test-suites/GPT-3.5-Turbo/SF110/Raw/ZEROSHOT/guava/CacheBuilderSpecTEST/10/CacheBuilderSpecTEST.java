import org.junit.Test;
import static org.junit.Assert.*;

public class CacheBuilderSpecTest {

    @Test
    public void testParse_emptyString() {
        String cacheBuilderSpecification = "";
        CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpecification);

        assertTrue(spec.initialCapacity == null);
        assertTrue(spec.maximumSize == null);
        assertTrue(spec.maximumWeight == null);
        assertTrue(spec.concurrencyLevel == null);
        assertTrue(spec.keyStrength == null);
        assertTrue(spec.valueStrength == null);
        assertTrue(spec.recordStats == null);
        assertTrue(spec.writeExpirationDuration == 0);
        assertTrue(spec.writeExpirationTimeUnit == null);
        assertTrue(spec.accessExpirationDuration == 0);
        assertTrue(spec.accessExpirationTimeUnit == null);
        assertTrue(spec.refreshDuration == 0);
        assertTrue(spec.refreshTimeUnit == null);
    }

    @Test
    public void testDisableCaching() {
        CacheBuilderSpec spec = CacheBuilderSpec.disableCaching();

        assertTrue(spec.maximumSize == 0);
    }

    @Test
    public void testToCacheBuilder() {
        CacheBuilderSpec spec = new CacheBuilderSpec("initialCapacity=100, maximumSize=1000, concurrencyLevel=4");
        CacheBuilder<Object, Object> builder = spec.toCacheBuilder();

        assertTrue(builder.initialCapacity() == 100);
        assertTrue(builder.maximumSize() == 1000);
        assertTrue(builder.concurrencyLevel() == 4);
    }

    @Test
    public void testToParsableString() {
        CacheBuilderSpec spec = new CacheBuilderSpec("initialCapacity=100, maximumSize=1000, concurrencyLevel=4");
        String parsableString = spec.toParsableString();

        assertEquals(parsableString, "initialCapacity=100, maximumSize=1000, concurrencyLevel=4");
    }

    @Test
    public void testHashCode_equals() {
        CacheBuilderSpec spec1 = CacheBuilderSpec.parse("initialCapacity=100, maximumSize=1000");
        CacheBuilderSpec spec2 = CacheBuilderSpec.parse("initialCapacity=100, maximumSize=1000");

        assertTrue(spec1.hashCode() == spec2.hashCode());
    }

    @Test
    public void testEquals_notEqual() {
        CacheBuilderSpec spec1 = CacheBuilderSpec.parse("initialCapacity=100, maximumSize=1000");
        CacheBuilderSpec spec2 = CacheBuilderSpec.parse("initialCapacity=100, maximumSize=2000");

        assertFalse(spec1.equals(spec2));
    }

    @Test
    public void testEquals_sameObject() {
        CacheBuilderSpec spec1 = CacheBuilderSpec.parse("initialCapacity=100, maximumSize=1000");

        assertTrue(spec1.equals(spec1));
    }

    @Test
    public void testEquals_sameValues() {
        CacheBuilderSpec spec1 = CacheBuilderSpec.parse("initialCapacity=100, maximumSize=1000");
        CacheBuilderSpec spec2 = CacheBuilderSpec.parse("initialCapacity=100, maximumSize=1000");

        assertTrue(spec1.equals(spec2));
    }
}