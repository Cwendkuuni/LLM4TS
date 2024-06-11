import org.junit.Test;
import static org.junit.Assert.*;

public class CacheBuilderSpecTest {

    @Test
    public void parse_ValidSpec_ReturnsCacheBuilderSpecObject() {
        // Arrange
        String spec = "maximumSize=100, concurrencyLevel=8";
        
        // Act
        CacheBuilderSpec cacheBuilderSpec = CacheBuilderSpec.parse(spec);
        
        // Assert
        assertNotNull(cacheBuilderSpec);
        assertEquals(100, cacheBuilderSpec.maximumSize.intValue());
        assertEquals(8, cacheBuilderSpec.concurrencyLevel.intValue());
    }
    
    @Test
    public void parse_EmptySpec_ReturnsEmptyCacheBuilderSpecObject() {
        // Arrange
        String spec = "";
        
        // Act
        CacheBuilderSpec cacheBuilderSpec = CacheBuilderSpec.parse(spec);
        
        // Assert
        assertNotNull(cacheBuilderSpec);
        assertNull(cacheBuilderSpec.maximumSize);
        assertNull(cacheBuilderSpec.concurrencyLevel);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void parse_InvalidSpec_ThrowsIllegalArgumentException() {
        // Arrange
        String spec = "invalidSpec";
        
        // Act
        CacheBuilderSpec.parse(spec);
    }
    
    @Test
    public void disableCaching_WhenCalled_ReturnsCacheBuilderSpecObjectWithMaximumSizeZero() {
        // Arrange
        CacheBuilderSpec expectedSpec = CacheBuilderSpec.parse("maximumSize=0");
        
        // Act
        CacheBuilderSpec cacheBuilderSpec = CacheBuilderSpec.disableCaching();
        
        // Assert
        assertNotNull(cacheBuilderSpec);
        assertEquals(expectedSpec.maximumSize, cacheBuilderSpec.maximumSize);
    }
    
    @Test
    public void toCacheBuilder_WithAllSpecsSet_ReturnsCacheBuilderObject() {
        // Arrange
        CacheBuilderSpec cacheBuilderSpec = new CacheBuilderSpec("");
        cacheBuilderSpec.maximumSize = 100;
        cacheBuilderSpec.concurrencyLevel = 8;
        
        // Act
        CacheBuilder<Object, Object> cacheBuilder = cacheBuilderSpec.toCacheBuilder();
        
        // Assert
        assertNotNull(cacheBuilder);
        assertEquals(100, cacheBuilder.maximumSize());
        assertEquals(8, cacheBuilder.concurrencyLevel());
    }
    
    @Test
    public void toParsableString_WithSpecsSet_ReturnsParsableString() {
        // Arrange
        CacheBuilderSpec cacheBuilderSpec = new CacheBuilderSpec("");
        cacheBuilderSpec.maximumSize = 100;
        cacheBuilderSpec.concurrencyLevel = 8;
        String expectedString = "maximumSize=100, concurrencyLevel=8";
        
        // Act
        String actualString = cacheBuilderSpec.toParsableString();
        
        // Assert
        assertEquals(expectedString, actualString);
    }
    
    @Test
    public void equals_TwoCacheBuilderSpecObjectsWithSameProperties_ReturnsTrue() {
        // Arrange
        CacheBuilderSpec spec1 = new CacheBuilderSpec("");
        spec1.maximumSize = 100;
        spec1.concurrencyLevel = 8;
        
        CacheBuilderSpec spec2 = new CacheBuilderSpec("");
        spec2.maximumSize = 100;
        spec2.concurrencyLevel = 8;
        
        // Act
        boolean result = spec1.equals(spec2);
        
        // Assert
        assertTrue(result);
    }
    
    @Test
    public void hashCode_TwoCacheBuilderSpecObjectsWithSameProperties_ReturnsSameHashCode() {
        // Arrange
        CacheBuilderSpec spec1 = new CacheBuilderSpec("");
        spec1.maximumSize = 100;
        spec1.concurrencyLevel = 8;
        
        CacheBuilderSpec spec2 = new CacheBuilderSpec("");
        spec2.maximumSize = 100;
        spec2.concurrencyLevel = 8;
        
        // Act
        int hashCode1 = spec1.hashCode();
        int hashCode2 = spec2.hashCode();
        
        // Assert
        assertEquals(hashCode1, hashCode2);
    }
}