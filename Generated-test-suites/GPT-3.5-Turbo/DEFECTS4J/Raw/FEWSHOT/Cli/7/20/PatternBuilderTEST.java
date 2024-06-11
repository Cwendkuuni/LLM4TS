package org.apache.commons.cli2.builder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class PatternBuilderTest {
    @Test
    public void testCreate() {
        PatternBuilder patternBuilder = new PatternBuilder();
        Option option = patternBuilder.create();
        assertNull(option);
        
        // Add some options to options set
        Option option1 = new Option();
        Option option2 = new Option();
        patternBuilder.options.add(option1);
        patternBuilder.options.add(option2);
        
        Option createdOption = patternBuilder.create();
        assertEquals(option1, createdOption);
        
        // Add more than 1 options to options set
        Option option3 = new Option();
        patternBuilder.options.add(option3);
        
        createdOption = patternBuilder.create();
        assertTrue(createdOption instanceof Group);
        assertEquals(option1, ((Group) createdOption).getOptions().get(0));
        assertEquals(option2, ((Group) createdOption).getOptions().get(1));
        assertEquals(option3, ((Group) createdOption).getOptions().get(2));
        
        // Reset options set and create option again
        patternBuilder.reset();
        createdOption = patternBuilder.create();
        assertNull(createdOption);
    }
    
    @Test
    public void testReset() {
        PatternBuilder patternBuilder = new PatternBuilder();
        Option option1 = new Option();
        Option option2 = new Option();
        patternBuilder.options.add(option1);
        patternBuilder.options.add(option2);
        
        patternBuilder.reset();
        assertTrue(patternBuilder.options.isEmpty());
    }
    
    @Test
    public void testWithPattern() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("abc");
        assertEquals(1, patternBuilder.options.size());
        Option option = (Option) patternBuilder.options.iterator().next();
        assertEquals('a', option.getShortName());
        
        patternBuilder.reset();
        patternBuilder.withPattern("aaa");
        assertEquals(1, patternBuilder.options.size());
        option = (Option) patternBuilder.options.iterator().next();
        assertEquals('a', option.getShortName());
        assertTrue(option.isRequired());
        
        patternBuilder.reset();
        patternBuilder.withPattern("abc!");
        assertEquals(1, patternBuilder.options.size());
        option = (Option) patternBuilder.options.iterator().next();
        assertEquals('a', option.getShortName());
        assertTrue(option.isRequired());
        
        patternBuilder.reset();
        patternBuilder.withPattern("a:b");
        assertEquals(1, patternBuilder.options.size());
        option = (Option) patternBuilder.options.iterator().next();
        assertEquals('a', option.getShortName());
        Argument argument = option.getArgument();
        assertEquals(1, argument.getMinimum());
        assertEquals(1, argument.getMaximum());
        
        patternBuilder.reset();
        patternBuilder.withPattern("a:");
        assertEquals(1, patternBuilder.options.size());
        option = (Option) patternBuilder.options.iterator().next();
        assertEquals('a', option.getShortName());
        argument = option.getArgument();
        assertEquals(1, argument.getMinimum());
        assertEquals(1, argument.getMaximum());
        
        patternBuilder.reset();
        patternBuilder.withPattern("a:b*");
        assertEquals(1, patternBuilder.options.size());
        option = (Option) patternBuilder.options.iterator().next();
        assertEquals('a', option.getShortName());
        argument = option.getArgument();
        assertEquals(1, argument.getMinimum());
        assertEquals(Integer.MAX_VALUE, argument.getMaximum());
        
        patternBuilder.reset();
        patternBuilder.withPattern("a:");
        assertEquals(1, patternBuilder.options.size());
        option = (Option) patternBuilder.options.iterator().next();
        assertEquals('a', option.getShortName());
        argument = option.getArgument();
        assertEquals(1, argument.getMinimum());
        assertEquals(1, argument.getMaximum());
        
        patternBuilder.reset();
        patternBuilder.withPattern("abc@");
        assertEquals(1, patternBuilder.options.size());
        option = (Option) patternBuilder.options.iterator().next();
        assertEquals('a', option.getShortName());
        argument = option.getArgument();
        assertTrue(argument.getValidator() instanceof ClassValidator);
        assertTrue(((ClassValidator) argument.getValidator()).isInstance());
        
        patternBuilder.reset();
        patternBuilder.withPattern("abc%");
        assertEquals(1, patternBuilder.options.size());
        option = (Option) patternBuilder.options.iterator().next();
        assertEquals('a', option.getShortName());
        argument = option.getArgument();
        assertTrue(argument.getValidator() instanceof NumberValidator);
        
        patternBuilder.reset();
        patternBuilder.withPattern("abc#");
        assertEquals(1, patternBuilder.options.size());
        option = (Option) patternBuilder.options.iterator().next();
        assertEquals('a', option.getShortName());
        argument = option.getArgument();
        assertTrue(argument.getValidator() instanceof DateValidator);
        
        patternBuilder.reset();
        patternBuilder.withPattern("abc<");
        assertEquals(1, patternBuilder.options.size());
        option = (Option) patternBuilder.options.iterator().next();
        assertEquals('a', option.getShortName());
        argument = option.getArgument();
        assertTrue(argument.getValidator() instanceof FileValidator);
        assertTrue(((FileValidator) argument.getValidator()).isExisting());
        assertTrue(((FileValidator) argument.getValidator()).isFile());
        
        patternBuilder.reset();
        patternBuilder.withPattern("abc>");
        assertEquals(1, patternBuilder.options.size());
        option = (Option) patternBuilder.options.iterator().next();
        assertEquals('a', option.getShortName());
        argument = option.getArgument();
        assertTrue(argument.getValidator() instanceof FileValidator);
        
        patternBuilder.reset();
        patternBuilder.withPattern("abc/");
        assertEquals(1, patternBuilder.options.size());
        option = (Option) patternBuilder.options.iterator().next();
        assertEquals('a', option.getShortName());
        argument = option.getArgument();
        assertTrue(argument.getValidator() instanceof UrlValidator);
        
        patternBuilder.reset();
        patternBuilder.withPattern("");
        assertTrue(patternBuilder.options.isEmpty());
    }
    
    @Test
    public void testValidator() {
        PatternBuilder patternBuilder = new PatternBuilder();
        Validator validator = patternBuilder.validator('@');
        assertTrue(validator instanceof ClassValidator);
        assertTrue(((ClassValidator) validator).isInstance());
        
        validator = patternBuilder.validator('+');
        assertTrue(validator instanceof ClassValidator);
        
        validator = patternBuilder.validator(':');
        assertNull(validator);
        
        validator = patternBuilder.validator('%');
        assertTrue(validator instanceof NumberValidator);
        
        validator = patternBuilder.validator('#');
        assertTrue(validator instanceof DateValidator);
        
        validator = patternBuilder.validator('<');
        assertTrue(validator instanceof FileValidator);
        assertTrue(((FileValidator) validator).isExisting());
        assertTrue(((FileValidator) validator).isFile());
        
        validator = patternBuilder.validator('>');
        assertTrue(validator instanceof FileValidator);
        
        validator = patternBuilder.validator('*');
        assertNull(validator);
        
        validator = patternBuilder.validator('/');
        assertTrue(validator instanceof UrlValidator);
        
        validator = patternBuilder.validator('!');
        assertNull(validator);
    }
}