package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class OptionBuilderTest {
    

    @Test
    public void testWithLongOpt() {
        OptionBuilder optionBuilder = new OptionBuilder();
        OptionBuilder instance = optionBuilder.withLongOpt("opt");
        String longOpt = instance.longopt;
        Assert.assertEquals("opt", longOpt);
    }

    @Test
    public void testHasArg() {
        OptionBuilder optionBuilder = new OptionBuilder();
        OptionBuilder instance = optionBuilder.hasArg();
        int numberOfArgs = instance.numberOfArgs;
        Assert.assertEquals(1, numberOfArgs);
    }

    @Test
    public void testHasArgWithArgumentName() {
        OptionBuilder optionBuilder = new OptionBuilder();
        OptionBuilder instance = optionBuilder.hasArg().withArgName("argName");
        String argName = instance.argName;
        Assert.assertEquals("argName", argName);
    }

    @Test
    public void testIsRequired() {
        OptionBuilder optionBuilder = new OptionBuilder();
        OptionBuilder instance = optionBuilder.isRequired();
        boolean required = instance.required;
        Assert.assertTrue(required);
    }

    @Test
    public void testWithValueSeparator() {
        OptionBuilder optionBuilder = new OptionBuilder();
        OptionBuilder instance = optionBuilder.withValueSeparator(':');
        char valuesep = instance.valuesep;
        Assert.assertEquals(':', valuesep);
    }

    @Test
    public void testWithValueSeparatorEmpty() {
        OptionBuilder optionBuilder = new OptionBuilder();
        OptionBuilder instance = optionBuilder.withValueSeparator();
        char valuesep = instance.valuesep;
        Assert.assertEquals('=', valuesep);
    }

    @Test
    public void testIsRequiredFalse() {
        OptionBuilder optionBuilder = new OptionBuilder();
        OptionBuilder instance = optionBuilder.isRequired(false);
        boolean required = instance.required;
        Assert.assertFalse(required);
    }

    @Test
    public void testHasArgs() {
        OptionBuilder optionBuilder = new OptionBuilder();
        OptionBuilder instance = optionBuilder.hasArgs();
        int numberOfArgs = instance.numberOfArgs;
        Assert.assertEquals(Option.UNLIMITED_VALUES, numberOfArgs);
    }

    @Test
    public void testHasArgsWithArgumentNumber() {
        OptionBuilder optionBuilder = new OptionBuilder();
        OptionBuilder instance = optionBuilder.hasArgs(2);
        int numberOfArgs = instance.numberOfArgs;
        Assert.assertEquals(2, numberOfArgs);
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder optionBuilder = new OptionBuilder();
        OptionBuilder instance = optionBuilder.hasOptionalArg();
        boolean optionalArg = instance.optionalArg;
        Assert.assertTrue(optionalArg);
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder optionBuilder = new OptionBuilder();
        OptionBuilder instance = optionBuilder.hasOptionalArgs();
        int numberOfArgs = instance.numberOfArgs;
        Assert.assertEquals(Option.UNLIMITED_VALUES, numberOfArgs);
    }

    @Test
    public void testHasOptionalArgsWithArgumentNumber() {
        OptionBuilder optionBuilder = new OptionBuilder();
        OptionBuilder instance = optionBuilder.hasOptionalArgs(3);
        int numberOfArgs = instance.numberOfArgs;
        Assert.assertEquals(3, numberOfArgs);
    }

    @Test
    public void testWithType() {
        OptionBuilder optionBuilder = new OptionBuilder();
        Class stringClass = String.class;
        OptionBuilder instance = optionBuilder.withType(stringClass);
        Class type = instance.type;
        Assert.assertEquals(String.class, type);
    }

    @Test
    public void testWithDescription() {
        OptionBuilder optionBuilder = new OptionBuilder();
        String description = "description";
        OptionBuilder instance = optionBuilder.withDescription(description);
        String instanceDescription = instance.description;
        Assert.assertEquals("description", instanceDescription);
    }

    @Test
    public void testCreate() throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        OptionBuilder optionBuilder = new OptionBuilder();
        String longOpt = "longOpt";
        String description = "description";
        OptionBuilder instance = optionBuilder.withLongOpt(longOpt).withDescription(description);
        Method createMethod = OptionBuilder.class.getDeclaredMethod("create", (Class[]) null);
        createMethod.setAccessible(true);
        Option option = (Option) createMethod.invoke(instance, (Object[]) null);
        Assert.assertEquals(longOpt, option.getLongOpt());
        Assert.assertEquals(description, option.getDescription());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithoutLongOpt() throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        OptionBuilder optionBuilder = new OptionBuilder();
        Method createMethod = OptionBuilder.class.getDeclaredMethod("create", (Class[]) null);
        createMethod.setAccessible(true);
        Option option = (Option) createMethod.invoke(optionBuilder, (Object[]) null);
        optionBuilder.reset();
    }

    @Test
    public void testCreateWithEmptyLongOpt() throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        OptionBuilder optionBuilder = new OptionBuilder();
        OptionBuilder instance = optionBuilder.withLongOpt("");
        Method createMethod = OptionBuilder.class.getDeclaredMethod("create", String.class);
        createMethod.setAccessible(true);
        Option option = (Option) createMethod.invoke(instance, (Object[]) null);
        Assert.assertEquals("", option.getLongOpt());
    }

    @Test
    public void testCreateWithNullLongOpt() throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        OptionBuilder optionBuilder = new OptionBuilder();
        OptionBuilder instance = optionBuilder.withLongOpt(null);
        Method createMethod = OptionBuilder.class.getDeclaredMethod("create", String.class);
        createMethod.setAccessible(true);
        Option option = (Option) createMethod.invoke(instance, (Object[]) null);
        Assert.assertNull(option.getLongOpt());
    }

    @Test
    public void testCreateWithEmptyDescription() throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        OptionBuilder optionBuilder = new OptionBuilder();
        OptionBuilder instance = optionBuilder.withDescription("");
        Method createMethod = OptionBuilder.class.getDeclaredMethod("create", String.class, String.class);
        createMethod.setAccessible(true);
        Option option = (Option) createMethod.invoke(instance, new String[]{"shortOpt"}, new String[]{"emptyDescription"});
        Assert.assertEquals("emptyDescription", option.getDescription());
    }

    @Test
    public void testCreateWithNullDescription() throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        OptionBuilder optionBuilder = new OptionBuilder();
        OptionBuilder instance = optionBuilder.withDescription(null);
        Method createMethod = OptionBuilder.class.getDeclaredMethod("create", String.class, String.class);
        createMethod.setAccessible(true);
        Option option = (Option) createMethod.invoke(instance, new String[]{"shortOpt"}, new String[]{"description"});
        Assert.assertNull(option.getDescription());
    }

    @Test
    public void testCreateWithEmptyArgName() throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        OptionBuilder optionBuilder = new OptionBuilder();
        OptionBuilder instance = optionBuilder.withArgName("");
        Method createMethod = OptionBuilder.class.getDeclaredMethod("create", int.class, String.class, char.class, Class.class, boolean.class, boolean.class, char.class, String.class);
        createMethod.setAccessible(true);
        Option option = (Option) createMethod.invoke(instance, new Object[]{1, "", null, ' ', null, false, false, '\u0000', null});
        Assert.assertEquals("", option.getArgName());
    }

    @Test
    public void testCreateWithNullArgName() throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        OptionBuilder optionBuilder = new OptionBuilder();
        OptionBuilder instance = optionBuilder.withArgName(null);
        Method createMethod = OptionBuilder.class.getDeclaredMethod("create", int.class, String.class, char.class, Class.class, boolean.class, boolean.class, char.class, String.class);
        createMethod.setAccessible(true);
        Option option = (Option) createMethod.invoke(instance, new Object[]{1, null, null, ' ', null, false, false, '\u0000', null});
        Assert.assertNull(option.getArgName());
    }

    @Test
    public void testCreateWithEmptyLongOptValueSeparator() throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        OptionBuilder optionBuilder = new OptionBuilder();
        OptionBuilder instance = optionBuilder.withValueSeparator('');
        Method createMethod = OptionBuilder.class.getDeclaredMethod("create", int.class, String.class, char.class, Class.class, boolean.class, boolean.class, char.class, String.class);
        createMethod.setAccessible(true);
        Option option = (Option) createMethod.invoke(instance, new Object[]{1, "longOpt", null, ' ', null, false, false, '\u0000', null});
        Assert.assertEquals("", option.getValueSeparator());
    }

    @Test
    public void testCreateWithNullValueSeparator() throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        OptionBuilder optionBuilder = new OptionBuilder();
        OptionBuilder instance = optionBuilder.withValueSeparator(null);
        Method createMethod = OptionBuilder.class.getDeclaredMethod("create", int.class, String.class, char.class, Class.class, boolean.class, boolean.class, char.class, String.class);
        createMethod.setAccessible(true);
        Option option = (Option) createMethod.invoke(instance, new Object[]{1, "longOpt", null, ' ', null, false, false, '\u0000', null});
        Assert.assertNull(option.getValueSeparator());
    }

    @Test
    public void testCreateWithEmptyArgNameValueSeparator() throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        OptionBuilder optionBuilder = new OptionBuilder();
        OptionBuilder instance = optionBuilder.withValueSeparator(' ').withArgName("argName");
        Method createMethod = OptionBuilder.class.getDeclaredMethod("create", int.class, String.class, char.class, Class.class, boolean.class, boolean.class, char.class, String.class);
        createMethod.setAccessible(true);
        Option option = (Option) createMethod.invoke(instance, new Object[]{1, "longOpt", ' ', String.class, false, false, false, ' ', "argName"});
        Assert.assertEquals(" ", option.getValueSeparator());
    }

    @Test
    public void testCreateWithNullArgNameValueSeparator() throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        OptionBuilder optionBuilder = new OptionBuilder();
        OptionBuilder instance = optionBuilder.withValueSeparator(' ').withArgName(null);
        Method createMethod = OptionBuilder.class.getDeclaredMethod("create", int.class, String.class, char.class, Class.class, boolean.class, boolean.class, char.class, String.class);
        createMethod.setAccessible(true);
        Option option = (Option) createMethod.invoke(instance, new Object[]{1, "longOpt", ' ', String.class, false, false, false, ' ', null});
        Assert.assertEquals(" ", option.getValueSeparator());
    }

    @Test
    public void testCreateWithEmptyOptionalArg() throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        OptionBuilder optionBuilder = new OptionBuilder();
        OptionBuilder instance = optionBuilder.hasOptionalArg().withArgName("argName");
        Method createMethod = OptionBuilder.class.getDeclaredMethod("create", int.class, String.class, char.class, Class.class, boolean.class, boolean.class, char.class, String.class);
        createMethod.setAccessible(true);
        Option option = (Option) createMethod.invoke(instance, new Object[]{1, "longOpt", null, ' ', null, false, true, '\u0000', "argName"});
        Assert.assertTrue(option.isOptional());
    }

    @Test
    public void testCreateWithEmptyOptionalArgs() throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        OptionBuilder optionBuilder = new OptionBuilder();
        OptionBuilder instance = optionBuilder.hasOptionalArgs().withArgName("argName");
        Method createMethod = OptionBuilder.class.getDeclaredMethod("create", int.class, String.class, char.class, Class.class, boolean.class, boolean.class, char.class, String.class);
        createMethod.setAccessible(true);
        Option option = (Option) createMethod.invoke(instance, new Object[]{1, "longOpt", null, ' ', null, false, true, '\u0000', "argName"});
        Assert.assertTrue(option.isOptional());
    }

    @Test
    public void testCreateWithArgNumber() throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        OptionBuilder optionBuilder = new OptionBuilder();
        OptionBuilder instance = optionBuilder.hasArgs(2).withArgName("argName");
        Method createMethod = OptionBuilder.class.getDeclaredMethod("create", int.class, String.class, char.class, Class.class, boolean.class, boolean.class, char.class, String.class);
        createMethod.setAccessible(true);
        Option option = (Option) createMethod.invoke(instance, new Object[]{1, "longOpt", null, ' ', null, false, false, '\u0000', "argName"});
        String[] values = option.getArgs();
        Assert.assertEquals(2, values.length);
    }

    @Test
    public void testCreateWithArgNumberAndValueSeparator() throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        OptionBuilder optionBuilder = new OptionBuilder();
        OptionBuilder instance = optionBuilder.hasArgs(2).withValueSeparator(':').withArgName("argName");
        Method createMethod = OptionBuilder.class.getDeclaredMethod("create", int.class, String.class, char.class, Class.class, boolean.class, boolean.class, char.class, String.class);
        createMethod.setAccessible(true);
        Option option = (Option) createMethod.invoke(instance, new Object[]{1, "longOpt", null, ' ', String[].class, false, false, ':', "argName"});
        String[] values = option.getArgs();
        Assert.assertEquals(2, values.length);
        Assert.assertEquals(":", option.getValueSeparator());
    }

    @Test
    public void testCreateWithType() throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        OptionBuilder optionBuilder = new OptionBuilder();
        Class integerClass = Integer.class;
        OptionBuilder instance = optionBuilder.withType(integerClass).withArgName("argName");
        Method createMethod = OptionBuilder.class.getDeclaredMethod("create", int.class, String.class, char.class, Class.class, boolean.class, boolean.class, char.class, String.class);
        createMethod.setAccessible(true);
        Option option = (Option) createMethod.invoke(instance, new Object[]{1, "longOpt", null, ' ', integerClass, false, false, '\u0000', "argName"});
        Assert.assertEquals(integerClass, option.getType());
    }

    
}