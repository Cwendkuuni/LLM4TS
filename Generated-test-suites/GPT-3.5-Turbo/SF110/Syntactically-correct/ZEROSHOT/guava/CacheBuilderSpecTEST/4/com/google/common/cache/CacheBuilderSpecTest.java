import org.junit.Test;

public class CacheBuilderSpecTest {
    
    @Test
    public void testParse() {
        String cacheBuilderSpecification = "maximumSize=100, concurrencyLevel=4";
        CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpecification);
        
        // perform assertions on spec object
    }
    
    @Test
    public void testDisableCaching() {
        CacheBuilderSpec spec = CacheBuilderSpec.disableCaching();
        
        // perform assertions on spec object
    }
    
    @Test
    public void testToCacheBuilder() {
        CacheBuilderSpec spec = new CacheBuilderSpec("maximumSize=100, initialCapacity=10");
        CacheBuilder<Object, Object> builder = spec.toCacheBuilder();
        
        // perform assertions on builder object
    }
    
    @Test
    public void testToParsableString() {
        CacheBuilderSpec spec = new CacheBuilderSpec("maximumSize=100, concurrencyLevel=4");
        String parsableString = spec.toParsableString();
        
        // perform assertions on parsableString
    }
    
    @Test
    public void testHashCode() {
        CacheBuilderSpec spec1 = new CacheBuilderSpec("maximumSize=100, concurrencyLevel=4");
        CacheBuilderSpec spec2 = new CacheBuilderSpec("maximumSize=200, concurrencyLevel=8");
        
        int hashCode1 = spec1.hashCode();
        int hashCode2 = spec2.hashCode();
        
        // perform assertions on hashCode1 and hashCode2
    }
    
    @Test
    public void testEquals() {
        CacheBuilderSpec spec1 = new CacheBuilderSpec("maximumSize=100, concurrencyLevel=4");
        CacheBuilderSpec spec2 = new CacheBuilderSpec("maximumSize=100, concurrencyLevel=4");
        CacheBuilderSpec spec3 = new CacheBuilderSpec("maximumSize=200, concurrencyLevel=8");
        
        boolean equals1 = spec1.equals(spec2);
        boolean equals2 = spec1.equals(spec3);
        
        // perform assertions on equals1 and equals2
    }
    
    @Test
    public void testDurationInNanos() {
        CacheBuilderSpec spec = new CacheBuilderSpec("maximumSize=100, concurrencyLevel=4");
        Long duration1 = spec.durationInNanos(10, TimeUnit.SECONDS);
        Long duration2 = spec.durationInNanos(1, TimeUnit.HOURS);
        
        // perform assertions on duration1 and duration2
    }
}