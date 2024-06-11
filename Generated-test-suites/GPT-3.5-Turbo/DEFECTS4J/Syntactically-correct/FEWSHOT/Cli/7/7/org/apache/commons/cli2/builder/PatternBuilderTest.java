package org.apache.commons.cli2.builder;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class PatternBuilderTest {
    private PatternBuilder patternBuilder;

    @Before
    public void setup() {
        patternBuilder = new PatternBuilder();
    }

    @Test
    public void testCreate_SingleOption() {
        patternBuilder.withPattern("a");
        Option option = patternBuilder.create();
        assertEquals("a", option.getShortName());
    }

    @Test
    public void testCreate_MultipleOptions() {
        patternBuilder.withPattern("a b c");
        Option option = patternBuilder.create();
        assertEquals("a", option.getShortName());
        option = option.getChildOption("b");
        assertEquals("b", option.getShortName());
        option = option.getChildOption("c");
        assertEquals("c", option.getShortName());
    }

    @Test
    public void testCreate_RequiredOptionWithArgument() {
        patternBuilder.withPattern("a! b@ c#");
        Option option = patternBuilder.create();
        assertEquals("a", option.getShortName());
        assertEquals(true, option.isRequired());
        assertEquals(1, option.getArgument().getMinimum());
        assertEquals(1, option.getArgument().getMaximum());
        assertEquals(ClassValidator.class, option.getArgument().getValidator().getClass());
        option = option.getChildOption("b");
        assertEquals("b", option.getShortName());
        assertEquals(true, option.isRequired());
        assertEquals(1, option.getArgument().getMinimum());
        assertEquals(1, option.getArgument().getMaximum());
        assertEquals(ClassValidator.class, option.getArgument().getValidator().getClass());
        option = option.getChildOption("c");
        assertEquals("c", option.getShortName());
        assertEquals(true, option.isRequired());
        assertEquals(1, option.getArgument().getMinimum());
        assertEquals(1, option.getArgument().getMaximum());
        assertEquals(DateValidator.class, option.getArgument().getValidator().getClass());
    }

    @Test
    public void testCreate_OptionWithMultipleArguments() {
        patternBuilder.withPattern("a* b+");
        Option option = patternBuilder.create();
        assertEquals("a", option.getShortName());
        assertEquals(0, option.getArgument().getMinimum());
        assertEquals(Integer.MAX_VALUE, option.getArgument().getMaximum());
        assertEquals(null, option.getArgument().getValidator());
        option = option.getChildOption("b");
        assertEquals("b", option.getShortName());
        assertEquals(1, option.getArgument().getMinimum());
        assertEquals(Integer.MAX_VALUE, option.getArgument().getMaximum());
        assertEquals(ClassValidator.class, option.getArgument().getValidator().getClass());
    }
}