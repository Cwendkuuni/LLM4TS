package org.apache.commons.cli2.builder;

import static org.junit.Assert.assertNotNull;
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
    public void testWithPattern() {
        PatternBuilder pb = new PatternBuilder();

        pb.withPattern("a:");
        Option optionA = pb.create();
        assertNotNull(optionA);
        assertTrue(optionA.hasArg());

        pb.reset();

        pb.withPattern("b@");
        Option optionB = pb.create();
        assertNotNull(optionB);
        assertTrue(optionB.getClass() == ClassValidator.class);

        pb.reset();

        pb.withPattern("c%:");
        Option optionC = pb.create();
        assertNotNull(optionC);
        assertTrue(optionC.hasArg());
        assertTrue(optionC.getValidator() instanceof NumberValidator);

        pb.reset();

        pb.withPattern("d#:");
        Option optionD = pb.create();
        assertNotNull(optionD);
        assertTrue(optionD.hasArg());
        assertTrue(optionD.getValidator() instanceof DateValidator);

        pb.reset();

        pb.withPattern("e<:");
        Option optionE = pb.create();
        assertNotNull(optionE);
        assertTrue(optionE.hasArg());
        assertTrue(optionE.getValidator() instanceof FileValidator);
        assertTrue(optionE.isRequiredFile());

        pb.reset();

        pb.withPattern("f>:");
        Option optionF = pb.create();
        assertNotNull(optionF);
        assertTrue(optionF.hasArg());
        assertTrue(optionF.getValidator() instanceof FileValidator);
        assertFalse(optionF.isRequiredFile());

        pb.reset();

        pb.withPattern("g*/");
        Option optionG = pb.create();
        assertNotNull(optionG);
        assertTrue(optionG.hasArg());
        assertTrue(optionG.isMultiValued());

        pb.reset();

        pb.withPattern("h+:");
        Option optionH = pb.create();
        assertNotNull(optionH);
        assertTrue(optionH.hasArg());
        assertTrue(optionH.isRepeatable());

        pb.reset();

        pb.withPattern("i:");
        Option optionI = pb.create();
        assertNotNull(optionI);
        assertTrue(optionI.hasArg());

        pb.reset();

        pb.withPattern("j:/");
        Option optionJ = pb.create();
        assertNotNull(optionJ);
        assertTrue(optionJ.hasArg());
        assertTrue(optionJ.getValidator() instanceof UrlValidator);

        pb.reset();

        pb.withPattern("k:!");
        Option optionK = pb.create();
        assertNotNull(optionK);
        assertTrue(optionK.isRequired());
    }
}