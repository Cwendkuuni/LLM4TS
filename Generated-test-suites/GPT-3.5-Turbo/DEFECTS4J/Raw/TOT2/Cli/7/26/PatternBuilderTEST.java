package org.apache.commons.cli2.builder;

import org.junit.Test;
import static org.junit.Assert.*;

public class PatternBuilderTest {

    @Test
    public void testCreateWithOptions() {
        PatternBuilder patternBuilder = new PatternBuilder();
        
        patternBuilder.withPattern("-i -f @ -d <:path -l #");
        Option option = patternBuilder.create();
        
        assertNotNull(option);
        assertEquals(2, option.getOptions().size());
        // Add more assertions as needed
    }

    @Test
    public void testCreateWithoutOptions() {
        PatternBuilder patternBuilder = new PatternBuilder();
        
        patternBuilder.withPattern("");
        Option option = patternBuilder.create();
        
        assertNotNull(option);
        assertTrue(option.getOptions().isEmpty());
        // Add more assertions as needed
    }

    @Test
    public void testWithPattern() {
        PatternBuilder patternBuilder = new PatternBuilder();
        
        patternBuilder.withPattern("-i -f @ -d <:path -l #");
        Option option = patternBuilder.create();
        
        assertNotNull(option);
        // Add more assertions as needed
    }

    @Test
    public void testReset() {
        PatternBuilder patternBuilder = new PatternBuilder();
        
        patternBuilder.withPattern("-i -f @ -d <:path -l #");
        Option option = patternBuilder.create();
        
        assertNotNull(option);
        
        patternBuilder.reset();
        Option resetOption = patternBuilder.create();
        
        assertNotNull(resetOption);
        assertTrue(resetOption.getOptions().isEmpty());
        // Add more assertions as needed
    }

    // Add more test methods for edge cases and exceptions

}