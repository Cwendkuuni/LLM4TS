package org.apache.commons.cli2.builder;

import static org.junit.Assert.*;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.validation.ClassValidator;
import org.apache.commons.cli2.validation.DateValidator;
import org.apache.commons.cli2.validation.FileValidator;
import org.apache.commons.cli2.validation.NumberValidator;
import org.apache.commons.cli2.validation.UrlValidator;
import org.apache.commons.cli2.builder.PatternBuilder;

/**
 * Test class for PatternBuilder
 */
public class PatternBuilderTest {

    @org.junit.Test
    public void testPatternBuilder() {
        PatternBuilder patternBuilder = new PatternBuilder();

        Option option = patternBuilder.withPattern("a:@java.lang.String,b:@java.lang.Integer,c:#dd-MM-yyyy,d:+java.util.List<java.lang.String>,e:<myFile.txt>")
                .create();

        assertNotNull("Option should not be null", option);

        Argument argumentA = option.getArgument();
        assertNotNull("Argument A should not be null", argumentA);
        assertEquals("java.lang.String", argumentA.getClass().getName());

        Argument argumentB = option.getArgument(1);
        assertNotNull("Argument B should not be null", argumentB);
        assertEquals("java.lang.Integer", argumentB.getClass().getName());

        Option optionDate = (Option) option.getOption("d");
        assertNotNull("Option date should not be null", optionDate);

        Argument argumentD = optionDate.getArgument();
        assertNotNull("Argument date should not be null", argumentD);
        assertEquals("java.util.List", argumentD.getClass().getName());
        assertEquals(java.util.List.class, argumentD.getType());

        Option optionFile = (Option) option.getOption("e");
        assertNotNull("Option file should not be null", optionFile);

        Argument argumentFile = optionFile.getArgument();
        assertNotNull("Argument file should not be null", argumentFile);
        assertEquals("java.io.File", argumentFile.getClass().getName());

        Option optionUrl = (Option) option.getOption("e");
        assertSame("Option url should be the same as file option", optionFile, optionUrl);

        Option optionEmpty = patternBuilder.withPattern(":").create();
        assertNull("Empty option should be null", optionEmpty);

        Option optionError = patternBuilder.withPattern("a:@java.lang.String,x:@java.lang.Integer");
        assertNull("Option with invalid character should be null", optionError);
    }
}