package org.apache.commons.cli2.builder;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
    public void testPatternBuilder() {
        PatternBuilder pb = new PatternBuilder();
        Option option = pb.withPattern("a:").create();
        assertTrue(option.getShortName().equals("a"));
        assertTrue(option.isRequired());
        assertTrue(option.getArgument().isPresent());
        reset(pb);

        option = pb.withPattern("b+").create();
        assertTrue(option.getShortName().equals("b"));
        assertTrue(option.isRequired());
        assertFalse(option.getArgument().isPresent());
        reset(pb);

        option = pb.withPattern("c-%").create();
        assertTrue(option.getShortName().equals("c"));
        assertFalse(option.isRequired());
        assertTrue(option.getArgument().isPresent());
        reset(pb);

        option = pb.withPattern("d:/").create();
        assertTrue(option.getShortName().equals("d"));
        assertFalse(option.isRequired());
        assertTrue(option.getArgument().map(Argument::getValidator).isPresent());
        UrlValidator urlValidator = (UrlValidator) option.getArgument().getValidator().get();
        assertTrue(urlValidator.isUrlRequired());
        reset(pb);

        option = pb.withPattern("e@").create();
        assertTrue(option.getShortName().equals("e"));
        assertFalse(option.isRequired());
        assertTrue(option.getArgument().isPresent());
        ClassValidator classValidator = (ClassValidator) option.getArgument().getValidator().get();
        assertTrue(classValidator.getInstanceRequired());
        reset(pb);

        Option groupOption = pb.withPattern("g:").create();
        GroupBuilder groupBuilder = pb.getGroupBuilder();
        Option childOption1 = groupBuilder.withOption(new OptionBuilder().withShortNames("g1").withLongNames("group-long").create());
        Option childOption2 = groupBuilder.withOption(new OptionBuilder().withShortNames("g2").withLongNames("group-long2").create());
        Option resultOption = groupOption.getOption();
        assertTrue(resultOption.getShortName().equals("g"));
        assertTrue(resultOption.isRequired());
        assertTrue(resultOption.getArguments().size() == 2);
        assertTrue(resultOption.getArguments().get(0).getValue().equals("g1"));
        assertTrue(resultOption.getArguments().get(1).getValue().equals("g2"));
        reset(pb);
    }

    private void reset(PatternBuilder pb) {
        pb.reset();
    }
}