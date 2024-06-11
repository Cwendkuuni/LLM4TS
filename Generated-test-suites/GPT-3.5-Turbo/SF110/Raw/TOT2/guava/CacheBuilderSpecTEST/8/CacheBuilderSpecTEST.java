import org.junit.Test;
import static org.junit.Assert.*;

public class CacheBuilderSpecTest {

    @Test
    public void testParse_validSpecification_returnsCacheBuilderSpec() {
        // Arrange
        String specification = "initialCapacity=10,maximumSize=100";
        
        // Act
        CacheBuilderSpec spec = CacheBuilderSpec.parse(specification);
        
        // Assert
        assertNotNull(spec);
        assertEquals(10, (int)spec.initialCapacity);
        assertEquals(100, (long)spec.maximumSize);
    }
    
    @Test
    public void testDisableCaching_returnsCacheBuilderSpecWithMaximumSizeZero() {
        // Act
        CacheBuilderSpec spec = CacheBuilderSpec.disableCaching();
        
        // Assert
        assertNotNull(spec);
        assertEquals(0, (long)spec.maximumSize);
    }
    
    // Add additional test cases for all methods in CacheBuilderSpec class
    
}