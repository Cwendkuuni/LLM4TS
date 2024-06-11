package com.google.common.cache;

import org.junit.Test;
import static org.junit.Assert.*;

public class CacheBuilderSpecTest {

    @Test
    public void testParse_EmptySpecification_ReturnsEmptySpec() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("");
        
        assertEquals("", spec.toParsableString());
    }
    
    @Test
    public void testParse_InitialCapacitySpecification_ReturnsSpecWithInitialCapacity() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=100");
        
        assertEquals("initialCapacity=100", spec.toParsableString());
    }
    
    @Test
    public void testParse_MaximumSizeSpecification_ReturnsSpecWithMaximumSize() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("maximumSize=1000");
        
        assertEquals("maximumSize=1000", spec.toParsableString());
    }
    
    @Test
    public void testParse_MaximumWeightSpecification_ReturnsSpecWithMaximumWeight() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("maximumWeight=10000");
        
        assertEquals("maximumWeight=10000", spec.toParsableString());
    }
    
    @Test
    public void testParse_ConcurrencyLevelSpecification_ReturnsSpecWithConcurrencyLevel() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("concurrencyLevel=2");
        
        assertEquals("concurrencyLevel=2", spec.toParsableString());
    }
    
    @Test
    public void testParse_KeyStrengthSpecification_ReturnsSpecWithKeyStrength() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("weakKeys");
        
        assertEquals("weakKeys", spec.toParsableString());
    }
    
    @Test
    public void testParse_ValueStrengthSpecification_ReturnsSpecWithValueStrength() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("softValues");
        
        assertEquals("softValues", spec.toParsableString());
    }
    
    @Test
    public void testParse_RecordStatsSpecification_ReturnsSpecWithRecordStats() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("recordStats");
        
        assertEquals("recordStats", spec.toParsableString());
    }
    
    @Test
    public void testParse_WriteExpirationSpecification_ReturnsSpecWithWriteExpiration() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("expireAfterWrite=5m");
        
        assertEquals("expireAfterWrite=5m", spec.toParsableString());
    }
    
    @Test
    public void testParse_AccessExpirationSpecification_ReturnsSpecWithAccessExpiration() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("expireAfterAccess=1h");
        
        assertEquals("expireAfterAccess=1h", spec.toParsableString());
    }
    
    @Test
    public void testParse_RefreshExpirationSpecification_ReturnsSpecWithRefreshExpiration() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("refreshAfterWrite=10m");
        
        assertEquals("refreshAfterWrite=10m", spec.toParsableString());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParse_InvalidSpecification_ThrowsIllegalArgumentException() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("invalidSpec");
    }
    
    @Test
    public void testDisableCaching_ReturnsSpecWithMaximumSizeZero() {
        CacheBuilderSpec spec = CacheBuilderSpec.disableCaching();
        
        assertEquals("maximumSize=0", spec.toParsableString());
    }
    
    @Test
    public void testToCacheBuilder_WithAllSpecs_ReturnsCacheBuilderWithSpecs() {
        CacheBuilderSpec spec = CacheBuilderSpec.parse("initialCapacity=100,maximumSize=1000,concurrencyLevel=2,weakKeys,expireAfterWrite=5m");
        
        CacheBuilder<Object, Object> builder = spec.toCacheBuilder();
        
        assertNotNull(builder);
        assertEquals(100, builder.initialCapacity());
        assertEquals(1000, builder.maximumSize());
        assertEquals(2, builder.concurrencyLevel());
        assertTrue(builder.toString().contains("weakKeys"));
        assertTrue(builder.toString().contains("expireAfterWrite=5m"));
    }

}