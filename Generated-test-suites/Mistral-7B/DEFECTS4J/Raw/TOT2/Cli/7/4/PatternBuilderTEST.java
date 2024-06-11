
package org.apache.commons.cli2.builder;

import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import org.junit.Assert.assertThrows;
import org.apache.commons.cli2.Option;

public class PatternBuilderTest {

    @Test
    public void testPatternBuilderConstruction() {
        PatternBuilder patternBuilder = new PatternBuilder();
        assertNotNull(patternBuilder);
    }

    @Test
    public void testCreateOption() {
        PatternBuilder patternBuilder = new PatternBuilder();
        Option option = patternBuilder.create();
        assertNotNull(option);
    }

    @Test
    public void testReset() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.create();
        patternBuilder.reset();
        Option option = patternBuilder.create();
        assertNotNull(option);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateMultipleOptionsWithSameShortName() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("a:");
        patternBuilder.withPattern("a:");
    }

    @Test
    public void testCreateOptionWithoutValidator() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.createOption(' ', true, ' ');
        Option option = patternBuilder.create();
        assertNotNull(option.getValidator()); // Assert that an Option was created without a validator
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateOptionWithInvalidValidator() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.createOption(' ', true, ' ', new InvalidValidator());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateOptionWithInvalidCharacter() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("a:x");
        patternBuilder.create();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateOptionWithEmptyPattern() {
        PatternBuilder patternBuilder = new PatternBuilder();
        patternBuilder.withPattern("");
    }

    // ...

}
