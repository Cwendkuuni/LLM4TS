
package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Assert;
import org.junit.Test;

public class OptionBuilderTest {
    // Test cases for all methods, edge cases, and exceptions

    // Test creating an Option with valid settings
    @Test
    public void testCreateWithValidLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        Option option = null;

        try {
            option = optionBuilder.create('t');
        } finally {
            OptionBuilder.reset();
        }

        assertNotNull(option);
    }

    // Test creating an Option without setting long opt
    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithoutSettingLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        optionBuilder.create('t');
    }

    // Test creating an Option with an invalid type
    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithInvalidType() {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        optionBuilder.withType(new Object());
        try {
            optionBuilder.create('t');
        } finally {
            OptionBuilder.reset();
        }
    }

    // Test creating an Option with an empty long opt
    @Test
    public void testCreateWithEmptyLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        Option option = null;

        try {
            option = optionBuilder.create('').withDescription("Option with empty long opt");
        } finally {
            OptionBuilder.reset();
        }

        assertNotNull(option);
    }

    // Test creating an Option with a required arg but no args provided
    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithRequiredArgButNoArgs() {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        optionBuilder.isRequired();
        try {
            optionBuilder.create('t');
        } finally {
            OptionBuilder.reset();
        }
    }

    // Test creating an Option with an optional arg and no args provided
    @Test
    public void testCreateWithOptionalArgButNoArgs() {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        optionBuilder.hasOptionalArg();
        Option option = null;

        try {
            option = optionBuilder.create('t');
        } finally {
            OptionBuilder.reset();
        }

        assertNotNull(option);
    }

    // Test creating an Option with an optional arg and multiple args provided
    @Test
    public void testCreateWithOptionalArgAndMultipleArgs() {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        optionBuilder.hasOptionalArgs(2);
        Option option = null;

        try {
            option = optionBuilder.create('t');
            option.setValue("arg1 arg2");
        } finally {
            OptionBuilder.reset();
        }

        assertNotNull(option);
        assertEquals("arg1 arg2", option.getValue());
    }

    // Test creating an Option with an invalid value separator
    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithInvalidValueSeparator() {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        optionBuilder.hasArgs().withValueSeparator('%');
        try {
            optionBuilder.create('t');
        } finally {
            OptionBuilder.reset();
        }
    }

    // Test creating an Option with an empty description
    @Test
    public void testCreateWithEmptyDescription() {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        Option option = null;

        try {
            option = optionBuilder.create('t').withDescription("");
        } finally {
            OptionBuilder.reset();
        }

        assertNotNull(option);
    }

    // Test creating an Option with an invalid long opt character
    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithInvalidLongOptCharacter() {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        try {
            optionBuilder.create((char) 0);
        } finally {
            OptionBuilder.reset();
        }
    }

    // Test creating an Option with an invalid arg name
    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithInvalidArgName() {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        try {
            optionBuilder.create('t').withArgName("\\0");
        } finally {
            OptionBuilder.reset();
        }
    }

    // Test creating an Option with an invalid number of args
    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithInvalidNumberOfArgs() {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        try {
            optionBuilder.create('t').hasArgs(-1);
        } finally {
            OptionBuilder.reset();
        }
    }

    // Test creating an Option with an invalid type
    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithInvalidTypeInstance() {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        try {
            optionBuilder.create('t').withType(new String("invalidType"));
        } finally {
            OptionBuilder.reset();
        }
    }
}
