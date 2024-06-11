package org.apache.commons.cli2.builder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.validation.ClassValidator;
import org.apache.commons.cli2.validation.DateValidator;
import org.apache.commons.cli2.validation.FileValidator;
import org.apache.commons.cli2.validation.NumberValidator;
import org.apache.commons.cli2.validation.UrlValidator;
import org.junit.Test;

public class PatternBuilderTest {
    @Test
    public void testCreate() {
        PatternBuilder patternBuilder = new PatternBuilder();
        
        Option option1 = patternBuilder.create();
        assertNull(option1);
        
        patternBuilder.reset();
        
        Option option2 = patternBuilder.create();
        assertNull(option2);
        
        patternBuilder.reset();
        
        Option option3 = patternBuilder.create();
        assertNull(option3);
    }
    
    @Test
    public void testReset() {
        PatternBuilder patternBuilder = new PatternBuilder();
        
        patternBuilder.reset();
        
        Option option = patternBuilder.create();
        assertNull(option);
    }
    
    @Test
    public void testWithPattern() {
        PatternBuilder patternBuilder = new PatternBuilder();
        
        patternBuilder.withPattern("!@:");
        Option option1 = patternBuilder.create();
        assertEquals(option1.getShortName(), "!");
        assertEquals(option1.isRequired(), true);
        assertEquals(option1.getArgument().getValidator().getClass(), ClassValidator.class);
        
        patternBuilder.reset();
        
        patternBuilder.withPattern("@%#");
        Option option2 = patternBuilder.create();
        assertEquals(option2.getShortName(), "@");
        assertEquals(option2.isRequired(), false);
        assertEquals(option2.getArgument().getValidator().getClass(), ClassValidator.class);
        
        patternBuilder.reset();
        
        patternBuilder.withPattern("*/?");
        Option option3 = patternBuilder.create();
        assertEquals(option3.getShortName(), "*");
        assertEquals(option3.isRequired(), false);
        assertEquals(option3.getArgument().getValidator().getClass(), FileValidator.class);
    }
}