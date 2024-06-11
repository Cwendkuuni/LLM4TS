
import org.junit.Test;
import static org.junit.Assert.*;

public class CacheBuilderSpecTest {
    
    @Test
    public void testParse() {
        String cacheBuilderSpecification = "initialCapacity=10, maximumSize=100";
        CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpecification);
        
        assertEquals(10, (int)spec.initialCapacity);
        assertEquals(100L, (long)spec.maximumSize);
    }
    
    @Test
    public void testDisableCaching() {
        CacheBuilderSpec spec = CacheBuilderSpec.disableCaching();
        
        assertEquals(0L, (long)spec.maximumSize);
    }
    
    @Test
    public void testToCacheBuilder() {
        String cacheBuilderSpecification = "initialCapacity=10, maximumSize=100";
        CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpecification);
        
        CacheBuilder<Object, Object> builder = spec.toCacheBuilder();
        
        assertNotNull(builder);
        assertEquals(10, builder.initialCapacity());
        assertEquals(100L, builder.maximumSize());
    }
    
    @Test
    public void testToParsableString() {
        String cacheBuilderSpecification = "initialCapacity=10, maximumSize=100";
        CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpecification);
        
        String parsableString = spec.toParsableString();
        
        assertEquals(cacheBuilderSpecification, parsableString);
    }
    
    @Test
    public void testEquals() {
        String cacheBuilderSpecification1 = "initialCapacity=10, maximumSize=100";
        String cacheBuilderSpecification2 = "initialCapacity=10, maximumSize=100";
        String cacheBuilderSpecification3 = "initialCapacity=10, maximumSize=200";
        
        CacheBuilderSpec spec1 = CacheBuilderSpec.parse(cacheBuilderSpecification1);
        CacheBuilderSpec spec2 = CacheBuilderSpec.parse(cacheBuilderSpecification2);
        CacheBuilderSpec spec3 = CacheBuilderSpec.parse(cacheBuilderSpecification3);
        
        assertEquals(spec1, spec2);
        assertNotEquals(spec1, spec3);
    }
}
