import static org.junit.Assert.*;
import org.junit.Test;

public class CacheBuilderSpecTest {
    
    @Test
    public void testParse_shouldParseCacheBuilderSpecification() {
        // Arrange
        String cacheBuilderSpecification = "initialCapacity=10, maximumSize=100, concurrencyLevel=4";
        
        // Act
        CacheBuilderSpec spec = CacheBuilderSpec.parse(cacheBuilderSpecification);
        
        // Assert
        assertEquals(10, (int)spec.initialCapacity);
        assertEquals(100L, (long)spec.maximumSize);
        assertEquals(4, (int)spec.concurrencyLevel);
    }
    
    @Test
    public void testDisableCaching_shouldCreateCacheBuilderSpecWithMaximumSizeZero() {
        // Act
        CacheBuilderSpec spec = CacheBuilderSpec.disableCaching();
        
        // Assert
        assertEquals(0L, (long)spec.maximumSize);
    }
    
    @Test
    public void testToCacheBuilder_shouldConvertCacheBuilderSpecToCacheBuilder() {
        // Arrange
        CacheBuilderSpec spec = new CacheBuilderSpec("initialCapacity=10");
        
        // Act
        CacheBuilder<Object, Object> builder = spec.toCacheBuilder();
        
        // Assert
        assertNotNull(builder);
    }
    
    @Test
    public void testToParsableString_shouldReturnParsableStringRepresentationOfCacheBuilderSpec() {
        // Arrange
        CacheBuilderSpec spec = new CacheBuilderSpec("initialCapacity=10");
        
        // Act
        String parsableString = spec.toParsableString();
        
        // Assert
        assertEquals("initialCapacity=10", parsableString);
    }
    
    @Test
    public void testEquals_shouldReturnTrueForEqualCacheBuilderSpecs() {
        // Arrange
        CacheBuilderSpec spec1 = new CacheBuilderSpec("maximumSize=100");
        CacheBuilderSpec spec2 = new CacheBuilderSpec("maximumSize=100");
        
        // Act
        boolean result = spec1.equals(spec2);
        
        // Assert
        assertTrue(result);
    }
    
    @Test
    public void testHashCode_shouldReturnSameHashCodeForEqualCacheBuilderSpecs() {
        // Arrange
        CacheBuilderSpec spec1 = new CacheBuilderSpec("maximumSize=100");
        CacheBuilderSpec spec2 = new CacheBuilderSpec("maximumSize=100");
        
        // Act
        int hashCode1 = spec1.hashCode();
        int hashCode2 = spec2.hashCode();
        
        // Assert
        assertEquals(hashCode1, hashCode2);
    }
}