package org.apache.commons.cli2.builder;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Group;
import org.apache.commons.cli2.Option;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class PatternBuilderTest {

    private PatternBuilder patternBuilder;

    @Before
    public void setUp() {
        patternBuilder = new PatternBuilder();
    }

    @Test
    public void testCreate() {
        Option option1 = new DefaultOptionBuilder()
                .withShortName("a")
                .withRequired(true)
                .create();

        patternBuilder.reset();
        patternBuilder.options.add(option1);

        Option createdOption = patternBuilder.create();
        Assert.assertEquals(option1, createdOption);
    }

    @Test
    public void testReset() {
        Option option1 = new DefaultOptionBuilder()
                .withShortName("a")
                .withRequired(true)
                .create();

        patternBuilder.options.add(option1);

        patternBuilder.reset();
        Assert.assertTrue(patternBuilder.options.isEmpty());
    }

    @Test
    public void testWithPattern() {
        Option option1 = new DefaultOptionBuilder()
                .withShortName("a")
                .withRequired(true)
                .create();
        Option option2 = new DefaultOptionBuilder()
                .withShortName("b")
                .withRequired(false)
                .create();

        patternBuilder.withPattern("a:b");

        Set<Option> expectedOptions = new HashSet<>();
        expectedOptions.add(option1);
        expectedOptions.add(option2);

        Assert.assertEquals(expectedOptions, patternBuilder.options);
    }

    @Test
    public void testValidator() {
        Assert.assertNull(patternBuilder.validator(':'));

        Argument argument = new ArgumentBuilder().create();
        Option option1 = new DefaultOptionBuilder()
                .withArgument(argument)
                .withShortName("a")
                .withRequired(true)
                .create();

        patternBuilder.options.add(option1);

        Option createdOption = patternBuilder.create();
        Assert.assertEquals(argument, createdOption.argument);
    }

}