import org.junit.Test;
import static org.junit.Assert.*;

public class CacheBuilderSpecTest {
    
    @Test
    public void testParse() {
        String cacheBuilderSpecification = "initialCapacity=10,maximumSize=100,concurrencyLevel=4";
        CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpecification);
        
        assertEquals((Integer)10, spec.initialCapacity);
        assertEquals((Long)100L, spec.maximumSize);
        assertEquals((Integer)4, spec.concurrencyLevel);
    }
    
    @Test
    public void testDisableCaching() {
        CacheBuilderSpec spec = CacheBuilderSpec.disableCaching();
        
        assertEquals((Long)0L, spec.maximumSize);
    }
    
    @Test
    public void testToCacheBuilder() {
        CacheBuilderSpec spec = new CacheBuilderSpec("initialCapacity=10,maximumSize=100,concurrencyLevel=4");
        
        CacheBuilder<Object, Object> builder = spec.toCacheBuilder();
        
        assertEquals(10, builder.initialCapacity());
        assertEquals(100, builder.maximumSize());
        assertEquals(4, builder.concurrencyLevel());
    }
    
    @Test
    public void testToParsableString() {
        CacheBuilderSpec spec = new CacheBuilderSpec("initialCapacity=10,maximumSize=100,concurrencyLevel=4");
        
        String parsableString = spec.toParsableString();
        
        assertEquals("initialCapacity=10,maximumSize=100,concurrencyLevel=4", parsableString);
    }
    
    @Test
    public void testEquals() {
        CacheBuilderSpec spec1 = new CacheBuilderSpec("initialCapacity=10,maximumSize=100,concurrencyLevel=4");
        CacheBuilderSpec spec2 = new CacheBuilderSpec("initialCapacity=10,maximumSize=100,concurrencyLevel=4");
        CacheBuilderSpec spec3 = new CacheBuilderSpec("initialCapacity=5,maximumSize=50,concurrencyLevel=2");
        
        assertTrue(spec1.equals(spec2));
        assertFalse(spec1.equals(spec3));
    }
    
    @Test
    public void testHashCode() {
        CacheBuilderSpec spec1 = new CacheBuilderSpec("initialCapacity=10,maximumSize=100,concurrencyLevel=4");
        CacheBuilderSpec spec2 = new CacheBuilderSpec("initialCapacity=10,maximumSize=100,concurrencyLevel=4");
        
        assertEquals(spec1.hashCode(), spec2.hashCode());
    }
    
    // Test for edge cases and exception handling
    
    @Test(expected = IllegalArgumentException.class)
    public void testParse_WithInvalidKey() {
        String cacheBuilderSpecification = "invalidKey=10";
        CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpecification);
    }
    
    @Test(expected = NumberFormatException.class)
    public void testParse_WithInvalidValue() {
        String cacheBuilderSpecification = "initialCapacity=ten";
        CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpecification);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testDisableCaching_WithNonZeroMaximumSize() {
        String cacheBuilderSpecification = "maximumSize=10";
        CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpecification);
        CacheBuilderSpec.disableCaching();
    }
    
    @Test
    public void testToCacheBuilder_WithNullValues() {
        CacheBuilderSpec spec = new CacheBuilderSpec("initialCapacity=10,maximumSize=null,concurrencyLevel=null");
        
        CacheBuilder<Object, Object> builder = spec.toCacheBuilder();
        
        assertEquals(10, builder.initialCapacity());
        assertEquals(0, builder.maximumSize());
        assertEquals(16, builder.concurrencyLevel());
    }
    
}