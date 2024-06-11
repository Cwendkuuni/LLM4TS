import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

public class CacheBuilderSpecTest {

    private CacheBuilderSpec cacheBuilderSpec;
    
    @Before
    public void setup() {
        cacheBuilderSpec = CacheBuilderSpec.parse("initialCapacity=10,maximumSize=100,expireAfterAccess=5h,expireAfterWrite=10m,recordStats");
    }
    
    @Test
    public void testParse() {
        assertNotNull(cacheBuilderSpec);
        assertEquals("initialCapacity=10,maximumSize=100,expireAfterAccess=5h,expireAfterWrite=10m,recordStats", cacheBuilderSpec.toParsableString());
    }
    
    @Test
    public void testDisableCaching() {
        CacheBuilderSpec disabledCache = CacheBuilderSpec.disableCaching();
        assertEquals("maximumSize=0", disabledCache.toParsableString());
    }
    
    @Test
    public void testToCacheBuilder() {
        assertNotNull(cacheBuilderSpec.toCacheBuilder());
    }
    
    @Test
    public void testToString() {
        assertEquals("CacheBuilderSpec{initialCapacity=10,maximumSize=100,expireAfterAccess=5h,expireAfterWrite=10m,recordStats}", cacheBuilderSpec.toString());
    }
    
    @Test
    public void testHashCode() {
        assertNotNull(cacheBuilderSpec.hashCode());
    }
    
    @Test
    public void testEquals() {
        CacheBuilderSpec sameSpec = CacheBuilderSpec.parse("initialCapacity=10,maximumSize=100,expireAfterAccess=5h,expireAfterWrite=10m,recordStats");
        assertTrue(cacheBuilderSpec.equals(sameSpec));
        
        CacheBuilderSpec diffSpec = CacheBuilderSpec.parse("initialCapacity=20,maximumSize=200,expireAfterAccess=10h,expireAfterWrite=20m,recordStats");
        assertFalse(cacheBuilderSpec.equals(diffSpec));
    }
}