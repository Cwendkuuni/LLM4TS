import org.junit.*;
import static org.junit.Assert.*;

public class CacheBuilderSpecTest {

    @Test
    public void testParse_initialCapacity() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=100");
        assertEquals(new Integer(100), spec.initialCapacity);
    }
    
    @Test
    public void testParse_maximumSize() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("maximumSize=1000");
        assertEquals(new Long(1000), spec.maximumSize);
    }
    
    @Test
    public void testParse_maximumWeight() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("maximumWeight=10000");
        assertEquals(new Long(10000), spec.maximumWeight);
    }

    @Test
    public void testParse_concurrencyLevel() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("concurrencyLevel=4");
        assertEquals(new Integer(4), spec.concurrencyLevel);
    }
    
    @Test
    public void testParse_keyStrength_weakKeys() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("weakKeys");
        assertEquals(LocalCache.Strength.WEAK, spec.keyStrength);
    }
    
    @Test
    public void testParse_valueStrength_softValues() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("softValues");
        assertEquals(LocalCache.Strength.SOFT, spec.valueStrength);
    }
    
    @Test
    public void testParse_valueStrength_weakValues() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("weakValues");
        assertEquals(LocalCache.Strength.WEAK, spec.valueStrength);
    }
    
    @Test
    public void testParse_recordStats() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("recordStats");
        assertEquals(true, spec.recordStats);
    }
    
    @Test
    public void testParse_expireAfterWrite() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("expireAfterWrite=10s");
        assertEquals(new Long(10), spec.writeExpirationDuration);
        assertEquals(TimeUnit.SECONDS, spec.writeExpirationTimeUnit);
    }
    
    @Test
    public void testParse_expireAfterAccess() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("expireAfterAccess=2h");
        assertEquals(new Long(2), spec.accessExpirationDuration);
        assertEquals(TimeUnit.HOURS, spec.accessExpirationTimeUnit);
    }
    
    @Test
    public void testParse_refreshAfterWrite() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("refreshAfterWrite=5m");
        assertEquals(new Long(5), spec.refreshDuration);
        assertEquals(TimeUnit.MINUTES, spec.refreshTimeUnit);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParse_unknownKey() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("invalidKey=10");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParse_blankKeyValuePair() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("maximumSize=100, ");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParse_keyValuePairWithMoreThanOneEqualsSign() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("maximumSize=100=200");
    }
    
    @Test
    public void testDisableCaching() {
        CacheBuilderSpec spec = CacheBuilderSpec.disableCaching();
        assertEquals(new Long(0), spec.maximumSize);
    }
    
    @Test
    public void testToParsableString() {
        CacheBuilderSpec spec = new CacheBuilderSpec("maximumSize=100, initialCapacity=50");
        String parsableString = spec.toParsableString();
        assertEquals("maximumSize=100, initialCapacity=50", parsableString);
    }
    
    @Test
    public void testToString() {
        CacheBuilderSpec spec = new CacheBuilderSpec("maximumSize=100, initialCapacity=50");
        String toString = spec.toString();
        assertEquals("CacheBuilderSpec{maximumSize=100, initialCapacity=50}", toString);
    }
    
    @Test
    public void testHashCode() {
        CacheBuilderSpec spec1 = new CacheBuilderSpec("maximumSize=100");
        CacheBuilderSpec spec2 = new CacheBuilderSpec("maximumSize=100");
        assertEquals(spec1.hashCode(), spec2.hashCode());
    }
    
    @Test
    public void testEquals() {
        CacheBuilderSpec spec1 = new CacheBuilderSpec("maximumSize=100");
        CacheBuilderSpec spec2 = new CacheBuilderSpec("maximumSize=100");
        assertTrue(spec1.equals(spec2));
    }
}