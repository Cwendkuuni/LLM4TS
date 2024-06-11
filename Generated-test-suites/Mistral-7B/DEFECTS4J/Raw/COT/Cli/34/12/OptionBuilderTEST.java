package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.lang.annotation.ElementType;
import java.lang.reflect.Field;
import java.util.Arrays;

public class OptionBuilderTest {

    

    @Test
    public void testWithLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOpt");
        Assert.assertEquals("longOpt", optionBuilder.longopt);
    }

    @Test(expected = IllegalStateException.class)
    public void testWithLongOpt_WithoutReset() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOpt");
        OptionBuilder.withLongOpt("anotherLongOpt");
    }

    @Test
    public void testHasArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        Assert.assertEquals(1, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testHasArg_WithArgName() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg().withArgName("argName");
        Assert.assertEquals("argName", OptionBuilder.argName);
    }

    @Test
    public void testIsRequired() {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        Assert.assertTrue(OptionBuilder.required);
    }

    @Test
    public void testWithValueSeparator() {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(';');
        Assert.assertEquals(';', OptionBuilder.valuesep);
    }

    @Test
    public void testHasArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs();
        Assert.assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testHasArgs_WithSpecifiedNumber() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs(5);
        Assert.assertEquals(5, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        Assert.assertTrue(OptionBuilder.optionalArg);
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs();
        Assert.assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.numberOfArgs);
        Assert.assertTrue(OptionBuilder.optionalArg);
    }

    @Test
    public void testHasOptionalArgs_WithSpecifiedNumber() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs(3);
        Assert.assertEquals(3, OptionBuilder.numberOfArgs);
        Assert.assertTrue(OptionBuilder.optionalArg);
    }

    @Test
    public void testWithType() {
        Class intType = Integer.TYPE;
        OptionBuilder optionBuilder = OptionBuilder.withType(intType);
        Assert.assertSame(intType, OptionBuilder.type);
    }

    @Test
    public void testWithDescription() {
        OptionBuilder optionBuilder = OptionBuilder.withDescription("test description");
        Assert.assertEquals("test description", OptionBuilder.description);
    }

    @Test
    public void testCreate_WithEmptyLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("");
        try {
            Option option = OptionBuilder.create('A');
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testCreate_WithEmptyLongOpt_AndDescription() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("").withDescription("test description");
        try {
            Option option = OptionBuilder.create('A');
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testCreate_WithNullLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt(null);
        try {
            Option option = OptionBuilder.create('A');
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testCreate_WithEmptyDescription() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("testLongOpt").withDescription("");
        try {
            Option option = OptionBuilder.create('A');
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testCreate_WithNullDescription() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("testLongOpt").withDescription(null);
        try {
            Option option = OptionBuilder.create('A');
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testCreate_WithValidArguments() throws Exception {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("testLongOpt")
                .withDescription("test description")
                .hasArg()
                .withArgName("argName")
                .isRequired()
                .withValueSeparator(';')
                .hasArgs(3)
                .withType(String.class);
        Option option = OptionBuilder.create('A');

        Field longOptField = Option.class.getDeclaredField("longOpt");
        longOptField.setAccessible(true);
        Assert.assertEquals("testLongOpt", longOptField.get(option));

        Field descriptionField = Option.class.getDeclaredField("description");
        descriptionField.setAccessible(true);
        Assert.assertEquals("test description", descriptionField.get(option));

        Field argNameField = Option.class.getDeclaredField("argName");
        argNameField.setAccessible(true);
        Assert.assertEquals("argName", argNameField.get(option));

        Field requiredField = Option.class.getDeclaredField("required");
        requiredField.setAccessible(true);
        Assert.assertTrue(requiredField.get(option));

        Field numberOfArgsField = Option.class.getDeclaredField("args");
        numberOfArgsField.setAccessible(true);
        Object numberOfArgsObj = numberOfArgsField.get(option);
        Assert.assertTrue(numberOfArgsObj instanceof int[]);
        int[] args = (int[]) numberOfArgsObj;
        Assert.assertArrayEquals(new int[]{3}, args);

        Field typeField = Option.class.getDeclaredField("type");
        typeField.setAccessible(true);
        Assert.assertSame(String.class, typeField.get(option));

        Field valueSeparatorField = Option.class.getDeclaredField("valueSeparator");
        valueSeparatorField.setAccessible(true);
        Assert.assertEquals(';', valueSeparatorField.get(option));

        Field optField = Option.class.getDeclaredField("opt");
        optField.setAccessible(true);
        Option parsedOption = (Option) optField.get(option);
        Field valueField = Option.OptionValue.class.getDeclaredField("values");
        valueField.setAccessible(true);
        Object[] values = (Object[]) valueField.get(parsedOption);
        Assert.assertArrayEquals(new String[]{"argName", "argName1", "argName2"}, values);

        Field longOptValuesField = Option.class.getDeclaredField("longOptValues");
        longOptValuesField.setAccessible(true);
        Assert.assertEquals("testLongOpt", longOptValuesField.get(option));
    }

    @Test
    public void testCreate_WithEmptyArgName() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("testLongOpt")
                .hasArg()
                .withArgName("");
        try {
            Option option = OptionBuilder.create('A');
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testCreate_WithEmptyArgName_AndDescription() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("testLongOpt")
                .withDescription("test description")
                .hasArg()
                .withArgName("");
        try {
            Option option = OptionBuilder.create('A');
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testCreate_WithEmptyArgName_AndDescription_AndType() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("testLongOpt")
                .withDescription("test description")
                .hasArg()
                .withArgName("")
                .withType(String[].class);
        try {
            Option option = OptionBuilder.create('A');
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testCreate_WithNullType() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("testLongOpt")
                .withDescription("test description")
                .hasArg()
                .withArgName("argName")
                .withType(null);
        try {
            Option option = OptionBuilder.create('A');
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testCreate_WithEmptyType() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("testLongOpt")
                .withDescription("test description")
                .hasArg()
                .withArgName("argName")
                .withType("");
        try {
            Option option = OptionBuilder.create('A');
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testCreate_WithWrongType() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("testLongOpt")
                .withDescription("test description")
                .hasArg()
                .withArgName("argName")
                .withType(ElementType.class);
        try {
            Option option = OptionBuilder.create('A');
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testCreate_WithDuplicateLongOpt() {
        OptionBuilder optionBuilder1 = OptionBuilder.withLongOpt("testLongOpt1")
                .withDescription("test description1")
                .hasArg()
                .withArgName("argName1")
                .withValueSeparator(';')
                .hasArgs(3)
                .withType(String.class);

        OptionBuilder optionBuilder2 = OptionBuilder.withLongOpt("testLongOpt1")
                .withDescription("test description2")
                .isRequired()
                .withValueSeparator(';')
                .hasArgs(5)
                .withType(Integer.TYPE);

        try {
            OptionBuilder.create('A');
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    
}