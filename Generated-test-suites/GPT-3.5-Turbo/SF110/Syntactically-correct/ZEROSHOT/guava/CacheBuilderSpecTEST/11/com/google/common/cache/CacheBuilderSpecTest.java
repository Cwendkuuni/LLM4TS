import org.junit.*;
import static org.junit.Assert.*;

public class CacheBuilderSpecTest {

    @Test
    public void testParse() {
        String cacheBuilderSpecification = "initialCapacity=10,maximumSize=100,concurrencyLevel=5";
        CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpecification);
        
        assertEquals(10, spec.initialCapacity.intValue());
        assertEquals(100, spec.maximumSize.longValue());
        assertEquals(5, spec.concurrencyLevel.intValue());
    }
    
    @Test
    public void testDisableCaching() {
        CacheBuilderSpec spec = CacheBuilderSpec.disableCaching();
        
        assertEquals(0, spec.maximumSize.longValue());
    }
    
    @Test
    public void testToCacheBuilder() {
        String cacheBuilderSpecification = "initialCapacity=10,maximumSize=100,concurrencyLevel=5";
        CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpecification);
        
        CacheBuilder<Object, Object> builder = spec.toCacheBuilder();
        
        assertEquals(10, builder.initialCapacity());
        assertEquals(100, builder.maximumSize());
        assertEquals(5, builder.concurrencyLevel());
    }
    
    @Test
    public void testToParsableString() {
        String cacheBuilderSpecification = "initialCapacity=10,maximumSize=100,concurrencyLevel=5";
        CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpecification);
        
        String parsableString = spec.toParsableString();
        
        assertEquals(cacheBuilderSpecification, parsableString);
    }
    
    @Test
    public void testHashCode() {
        String cacheBuilderSpecification1 = "initialCapacity=10,maximumSize=100,concurrencyLevel=5";
        String cacheBuilderSpecification2 = "initialCapacity=20,maximumSize=200,concurrencyLevel=10";
        CacheBuilderSpec spec1 = CacheBuilderSpec.parse(cacheBuilderSpecification1);
        CacheBuilderSpec spec2 = CacheBuilderSpec.parse(cacheBuilderSpecification2);
        
        int hashCode1 = spec1.hashCode();
        int hashCode2 = spec2.hashCode();
        
        assertNotEquals(hashCode1, hashCode2);
    }
    
    @Test
    public void testEquals() {
        String cacheBuilderSpecification1 = "initialCapacity=10,maximumSize=100,concurrencyLevel=5";
        String cacheBuilderSpecification2 = "initialCapacity=10,maximumSize=100,concurrencyLevel=5";
        String cacheBuilderSpecification3 = "initialCapacity=20,maximumSize=200,concurrencyLevel=10";
        CacheBuilderSpec spec1 = CacheBuilderSpec.parse(cacheBuilderSpecification1);
        CacheBuilderSpec spec2 = CacheBuilderSpec.parse(cacheBuilderSpecification2);
        CacheBuilderSpec spec3 = CacheBuilderSpec.parse(cacheBuilderSpecification3);
        
        assertTrue(spec1.equals(spec2));
        assertFalse(spec1.equals(spec3));
    }
}