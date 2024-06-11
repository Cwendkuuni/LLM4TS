package org.apache.commons.cli2.builder;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.validation.ClassValidator;
import org.apache.commons.cli2.validation.DateValidator;
import org.apache.commons.cli2.validation.FileValidator;
import org.apache.commons.cli2.validation.NumberValidator;
import org.apache.commons.cli2.validation.UrlValidator;
import org.junit.Test;

public class PatternBuilderTest {

    @Test
    public void testPatternBuilderCreateOption() {
        PatternBuilder pb = new PatternBuilder();
        pb.withPattern("a:");
        Option option = pb.create();
        assertNotNull(option);
        assertTrue(option.isSettable());
        assertTrue(option.isRequired());
        assertTrue(option.getLongOpt().equals("a"));
        assertTrue(option.getValue().isEmpty());

        pb.reset();
        pb.withPattern("b:@java.lang.String");
        option = pb.create();
        assertNotNull(option);
        assertTrue(option.isSettable());
        assertTrue(option.isRequired());
        assertTrue(option.getValue().isEmpty());
        assertTrue(option.getValidator() instanceof ClassValidator);
        assertEquals(String.class, ((ClassValidator) option.getValidator()).getClazz());

        pb.reset();
        pb.withPattern("c:%");
        option = pb.create();
        assertNotNull(option);
        assertTrue(option.isSettable());
        assertFalse(option.isRequired());
        assertTrue(option.getLongOpt().equals("c"));
        assertTrue(option.getValue().isEmpty());
        assertTrue(option.getValidator() instanceof NumberValidator);

        pb.reset();
        pb.withPattern("d:<");
        option = pb.create();
        assertNotNull(option);
        assertTrue(option.isSettable());
        assertFalse(option.isRequired());
        assertTrue(option.getLongOpt().equals("d"));
        assertTrue(option.getValue().isEmpty());
        assertTrue(option.getValidator() instanceof FileValidator);
        FileValidator fileValidator = (FileValidator) option.getValidator();
        assertTrue(fileValidator.isFile());
        assertTrue(fileValidator.isExisting());

        pb.reset();
        pb.withPattern("e:*");
        option = pb.create();
        assertNotNull(option);
        assertTrue(option.isSettable());
        assertFalse(option.isRequired());
        assertTrue(option.getLongOpt().equals("e"));
        assertTrue(option.getValue().isEmpty());
        assertTrue(option.getValidator() != null);
        assertTrue(option.getValidator() instanceof FileValidator);
        FileValidator fileValidator2 = (FileValidator) option.getValidator();
        assertFalse(fileValidator2.isFile());
        assertFalse(fileValidator2.isExisting());

        pb.reset();
        pb.withPattern("/f:");
        option = pb.create();
        assertNotNull(option);
        assertTrue(option.isSettable());
        assertFalse(option.isRequired());
        assertTrue(option.getLongOpt().equals("f"));
        assertTrue(option.getValue().isEmpty());
        assertTrue(option.getValidator() instanceof UrlValidator);
    }

    @Test
    public void testPatternBuilderCreateOptions() {
        PatternBuilder pb = new PatternBuilder();
        pb.withPattern("a: b: c:");
        Option option1 = pb.create();
        Option option2 = pb.create();
        Option option3 = pb.create();

        assertNotNull(option1);
        assertNotNull(option2);
        assertNotNull(option3);

        pb.reset();
        pb.withPattern("a: b:@java.lang.String c:% d:< e:/ f:");
        option1 = pb.create();
        option2 = pb.create();
        option3 = pb.create();
        option4 = pb.create();
        option5 = pb.create();

        assertNotNull(option1);
        assertNotNull(option2);
        assertNotNull(option3);
        assertNotNull(option4);
        assertNotNull(option5);

        pb.reset();
        pb.withPattern("a: b:c: d:e:f:");
        option1 = pb.create();
        option2 = pb.create();
        option3 = pb.create();
        option4 = pb.create();
        option5 = pb.create();
        option6 = pb.create();

        assertNotNull(option1);
        assertNotNull(option2);
        assertNotNull(option3);
        assertNotNull(option4);
        assertNotNull(option5);
        assertNotNull(option6);
    }
}