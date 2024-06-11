package org.apache.commons.cli2.builder;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.builder.DefaultOptionBuilder;
import org.apache.commons.cli2.builder.GroupBuilder;
import org.apache.commons.cli2.builder.PatternBuilder;
import org.apache.commons.cli2.validation.ClassValidator;
import org.apache.commons.cli2.validation.DateValidator;
import org.apache.commons.cli2.validation.FileValidator;
import org.apache.commons.cli2.validation.NumberValidator;
import org.apache.commons.cli2.validation.UrlValidator;
import org.junit.Test;

public class PatternBuilderTest {

    @Test
    public void testPatternBuilderWithEmptyPattern() {
        PatternBuilder pb = new PatternBuilder();
        Option option = pb.create();
        assertTrue(option instanceof Option);
        assertFalse(option.isSetValueRequired());
        assertNull(option.getValue());
    }

    @Test
    public void testPatternBuilderWithSingleCharacterPattern() {
        PatternBuilder pb = new PatternBuilder();
        pb.withPattern("a");
        Option option = pb.create();
        assertTrue(option instanceof Option);
        assertEquals("a", option.getShortName());
        assertNull(option.getValue());
        assertFalse(option.isSetValueRequired());
    }

    @Test
    public void testPatternBuilderWithRequiredSingleCharacterPattern() {
        PatternBuilder pb = new PatternBuilder();
        pb.withPattern("!a");
        Option option = pb.create();
        assertTrue(option instanceof Option);
        assertEquals("a", option.getShortName());
        assertNull(option.getValue());
        assertTrue(option.isSetValueRequired());
    }

    @Test
    public void testPatternBuilderWithStringPattern() {
        PatternBuilder pb = new PatternBuilder();
        pb.withPattern("ab:c");
        Option option = pb.create();
        assertTrue(option instanceof Option);
        assertEquals("a", option.getOptionParser().getOptions().get(0).getShortName());
        assertEquals("b", option.getOptionParser().getOptions().get(1).getShortName());
        assertEquals("c", option.getValue());
        assertFalse(option.isSetValueRequired());
    }

    @Test
    public void testPatternBuilderWithOptionalStringPattern() {
        PatternBuilder pb = new PatternBuilder();
        pb.withPattern("a:");
        Option option = pb.create();
        assertTrue(option instanceof Option);
        assertEquals("a", option.getOptionParser().getOptions().get(0).getShortName());
        assertNull(option.getValue());
        assertFalse(option.isSetValueRequired());
    }

    @Test
    public void testPatternBuilderWithNumberPattern() {
        PatternBuilder pb = new PatternBuilder();
        pb.withPattern("%d");
        Option option = pb.create();
        assertTrue(option instanceof Option);
        assertEquals("d", option.getOptionParser().getOptions().get(0).getShortName());
        assertFalse(option.isSetValueRequired());
        assertEquals(NumberValidator.class, option.getOptionParser().getOptions().get(0).getValidator().getClass());
    }

    @Test
    public void testPatternBuilderWithDatePattern() {
        PatternBuilder pb = new PatternBuilder();
        pb.withPattern("#{,date,dd/MM/yyyy}");
        Option option = pb.create();
        assertTrue(option instanceof Option);
        assertEquals("#{,date,dd/MM/yyyy}", option.getOptionParser().getOptions().get(0).getLongOpt());
        assertNull(option.getValue());
        assertFalse(option.isSetValueRequired());
        assertEquals(DateValidator.class, option.getOptionParser().getOptions().get(0).getValidator().getClass());
    }

    @Test
    public void testPatternBuilderWithClassPattern() {
        PatternBuilder pb = new PatternBuilder();
        pb.withPattern("@com.example.MyClass");
        Option option = pb.create();
        assertTrue(option instanceof Option);
        assertEquals("com.example.MyClass", option.getValue());
        assertFalse(option.isSetValueRequired());
        assertEquals(ClassValidator.class, option.getOptionParser().getOptions().get(0).getValidator().getClass());
    }

    @Test
    public void testPatternBuilderWithFilePattern() {
        PatternBuilder pb = new PatternBuilder();
        pb.withPattern("<file>");
        Option option = pb.create();
        assertTrue(option instanceof Option);
        assertTrue(option.getOptionParser().getOptions().get(0) instanceof org.apache.commons.cli2.Option);
        Option fileOption = (Option)option.getOptionParser().getOptions().get(0);
        assertTrue(fileOption.getValue() == null);
        assertTrue(fileOption.isSetValueRequired());
        assertEquals(FileValidator.class, option.getOptionParser().getOptions().get(0).getValidator().getClass());
    }

    @Test
    public void testPatternBuilderWithUrlPattern() {
        PatternBuilder pb = new PatternBuilder();
        pb.withPattern("/url");
        Option option = pb.create();
        assertTrue(option instanceof Option);
        assertEquals("/url", option.getOptionParser().getOptions().get(0).getLongOpt());
        assertNull(option.getValue());
        assertFalse(option.isSetValueRequired());
        assertEquals(UrlValidator.class, option.getOptionParser().getOptions().get(0).getValidator().getClass());
    }

    @Test
    public void testPatternBuilderWithGroupPattern() {
        PatternBuilder pb = new PatternBuilder();
        pb.withPattern("g{a,b}");
        Option option = pb.create();
        assertTrue(option instanceof Option);
        Option groupOption = (Option)option.getOptionParser().getOptions().get(0);
        assertEquals("g", groupOption.getShortName());
        assertTrue(groupOption.getValue() instanceof OptionGroup);
        Option aOption = (Option)(((OptionGroup)groupOption.getValue()).getOptions().get(0));
        assertEquals("a", aOption.getShortName());
        assertEquals("b", ((Option)groupOption.getValue()).getOptions().get(1).getShortName());
        assertNull(aOption.getValue());
        assertFalse(aOption.isSetValueRequired());
        assertEquals(PatternBuilder.class, aOption.getOptionParser().getOptions().get(0).getClass());
    }

    @Test
    public void testPatternBuilderWithMultipleArgumentsPattern() {
        PatternBuilder pb = new PatternBuilder();
        pb.withPattern("a:,b:");
        Option option = pb.create();
        assertTrue(option instanceof Option);
        assertEquals("a", option.getOptionParser().getOptions().get(0).getShortName());
        assertNull(option.getOptionParser().getOptions().get(0).getValue());
        assertFalse(option.getOptionParser().getOptions().get(0).isSetValueRequired());

        Argument aArg = ((Option)option.getOptionParser().getOptions().get(0)).getArgument();
        assertTrue(aArg.getName().equals("a"));
        assertTrue(aArg.isSetValueRequired());
        assertTrue(aArg.getValidator() instanceof FileValidator);

        assertEquals("b", option.getOptionParser().getOptions().get(1).getShortName());
        assertNull(option.getOptionParser().getOptions().get(1).getValue());
        assertFalse(option.getOptionParser().getOptions().get(1).isSetValueRequired());

        Argument bArg = ((Option)option.getOptionParser().getOptions().get(1)).getArgument();
        assertTrue(bArg.getName().equals("b"));
        assertTrue(bArg.isSetValueRequired());
        assertTrue(bArg.getValidator() instanceof FileValidator);
    }
}