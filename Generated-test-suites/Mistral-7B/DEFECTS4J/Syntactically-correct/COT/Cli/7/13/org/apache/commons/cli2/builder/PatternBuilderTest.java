package org.apache.commons.cli2.builder;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.validation.ClassValidator;
import org.apache.commons.cli2.validation.NumberValidator;
import org.apache.commons.cli2.validation.UrlValidator;
import org.junit.Test;

/**
 * Tests for PatternBuilder class
 */
public class PatternBuilderTest {

    @Test
    public void testPatternBuilderInstance() {
        PatternBuilder pb = new PatternBuilder();
        assertNotNull(pb);
    }

    @Test
    public void testPatternBuilderCreateOption() {
        PatternBuilder pb = new PatternBuilder();
        pb.withPattern("ab:c:d@Foo.class");
        Option option = pb.create();
        assertNotNull(option);
        assertTrue(option.getLongOpt().equals("ab"));
        assertTrue(option.getShortName() == 'a');
        assertTrue(option.hasArgument());
        assertTrue(option.isRequiredArgument());
        assertTrue(option.getArgument().getValidator() instanceof ClassValidator);
        assertTrue(option.getArgument().getValueName().equals("c"));
        assertTrue(option.getArgument().hasMinimum(0));
        assertTrue(option.getArgument().hasMaximum(1));
    }

    @Test
    public void testPatternBuilderCreateMultipleOptions() {
        PatternBuilder pb = new PatternBuilder();
        pb.withPattern("ab:c:d@Foo.class e:f g:h i:j");
        Option option1 = pb.create();
        Option option2 = pb.create();
        Option option3 = pb.create();

        assertNotNull(option1);
        assertNotNull(option2);
        assertNotNull(option3);

        assertTrue(option1.getLongOpt().equals("ab"));
        assertTrue(option1.getShortName() == 'a');
        assertTrue(option1.hasArgument());
        assertTrue(option1.isRequiredArgument());
        assertTrue(option1.getArgument().getValidator() instanceof ClassValidator);
        assertTrue(option1.getArgument().getValueName().equals("c"));
        assertTrue(option1.getArgument().hasMinimum(0));
        assertTrue(option1.getArgument().hasMaximum(1));

        assertTrue(option2.getLongOpt().equals("e"));
        assertTrue(option2.getShortName() == 'e');
        assertFalse(option2.hasArgument());
        assertFalse(option2.isRequiredArgument());
        assertNull(option2.getArgument());

        assertTrue(option3.getLongOpt().equals("g"));
        assertTrue(option3.getShortName() == 'g');
        assertTrue(option3.hasArgument());
        assertFalse(option3.isRequiredArgument());
        assertNull(option3.getArgument().getValidator());
        assertNull(option3.getArgument().getValueName());
        assertTrue(option3.getArgument().hasMinimum(0));
        assertTrue(option3.getArgument().hasMaximum(1));
    }

    @Test(expected = IllegalStateException.class)
    public void testPatternBuilderMissingPattern() {
        PatternBuilder pb = new PatternBuilder();
        pb.create();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPatternBuilderInvalidPattern() {
        PatternBuilder pb = new PatternBuilder();
        pb.withPattern("ab:c:d@Foo.class:e");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPatternBuilderMissingShortName() {
        PatternBuilder pb = new PatternBuilder();
        pb.withPattern(":c:d@Foo.class");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPatternBuilderMissingType() {
        PatternBuilder pb = new PatternBuilder();
        pb.withPattern("a b:c:d@Foo.class");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPatternBuilderMissingRequirement() {
        PatternBuilder pb = new PatternBuilder();
        pb.withPattern("a:b:c@Foo.class");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPatternBuilderInvalidType() {
        PatternBuilder pb = new PatternBuilder();
        pb.withPattern("a:b:@Foo.class");
    }

    @Test
    public void testPatternBuilderWithExistingFile() {
        PatternBuilder pb = new PatternBuilder();
        pb.withPattern("a:b:<file.txt>");
        Option option = pb.create();
        assertTrue(option.getLongOpt().equals("a"));
        assertTrue(option.getShortName() == 'a');
        assertTrue(option.hasArgument());
        assertTrue(option.isRequiredArgument());
        assertTrue(option.getArgument().getValidator() instanceof FileValidator);
        assertTrue(option.getArgument().getValueName().equals("b"));
        assertTrue(option.getArgument().hasMinimum(1));
        assertTrue(option.getArgument().hasMaximum(1));
        assertTrue(option.getArgument().isFile());
        assertTrue(option.getArgument().isExisting());
    }

    @Test
    public void testPatternBuilderWithNewFile() {
        PatternBuilder pb = new PatternBuilder();
        pb.withPattern("a:b:<file>");
        Option option = pb.create();
        assertTrue(option.getLongOpt().equals("a"));
        assertTrue(option.getShortName() == 'a');
        assertTrue(option.hasArgument());
        assertTrue(option.isRequiredArgument());
        assertTrue(option.getArgument().getValidator() instanceof FileValidator);
        assertTrue(option.getArgument().getValueName().equals("b"));
        assertTrue(option.getArgument().hasMinimum(1));
        assertTrue(option.getArgument().hasMaximum(1));
        assertFalse(option.getArgument().isFile());
        assertFalse(option.getArgument().isExisting());
    }

    @Test
    public void testPatternBuilderWithClass() {
        PatternBuilder pb = new PatternBuilder();
        pb.withPattern("a:b:@Foo.class");
        Option option = pb.create();
        assertTrue(option.getLongOpt().equals("a"));
        assertTrue(option.getShortName() == 'a');
        assertFalse(option.hasArgument());
        assertFalse(option.isRequiredArgument());
        assertTrue(option.getArgument() == null);
        assertTrue(option.getValidator() instanceof ClassValidator);
        assertTrue(option.getValueName().equals("b"));
    }

    @Test
    public void testPatternBuilderWithNumber() {
        PatternBuilder pb = new PatternBuilder();
        pb.withPattern("a:b:%d");
        Option option = pb.create();
        assertTrue(option.getLongOpt().equals("a"));
        assertTrue(option.getShortName() == 'a');
        assertTrue(option.hasArgument());
        assertFalse(option.isRequiredArgument());
        assertTrue(option.getArgument() == null);
        assertTrue(option.getValidator() instanceof NumberValidator);
        assertTrue(option.getValueName().equals("b"));
    }

    @Test
    public void testPatternBuilderWithDate() {
        PatternBuilder pb = new PatternBuilder();
        pb.withPattern("a:b:/#d");
        Option option = pb.create();
        assertTrue(option.getLongOpt().equals("a"));
        assertTrue(option.getShortName() == 'a');
        assertTrue(option.hasArgument());
        assertFalse(option.isRequiredArgument());
        assertTrue(option.getArgument() == null);
        assertTrue(option.getValidator() instanceof org.apache.commons.cli2.validation.DateValidator);
        assertTrue(option.getValueName().equals("b"));
    }

    @Test
    public void testPatternBuilderWithUrl() {
        PatternBuilder pb = new PatternBuilder();
        pb.withPattern("a:b:/%s");
        Option option = pb.create();
        assertTrue(option.getLongOpt().equals("a"));
        assertTrue(option.getShortName() == 'a');
        assertTrue(option.hasArgument());
        assertFalse(option.isRequiredArgument());
        assertTrue(option.getArgument() == null);
        assertTrue(option.getValidator() instanceof UrlValidator);
        assertTrue(option.getValueName().equals("b"));
    }
}