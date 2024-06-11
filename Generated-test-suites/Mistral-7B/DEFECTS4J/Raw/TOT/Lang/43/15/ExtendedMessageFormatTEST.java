package org.apache.commons.lang.text;

import org.apache.commons.lang.Validate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.MissingResourceException;

/**
 * Tests for {@link ExtendedMessageFormat}.
 */
public class ExtendedMessageFormatTest {

    /**
     * Test default constructor.
     */
    @Test
    public void testDefaultConstructor() {
        String pattern = "Hello, {0}!";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern);
        String result = format.format(new Object[]{"World"});
        Assertions.assertEquals("Hello, World!", result);
    }

    /**
     * Test constructor with locale.
     */
    @Test
    public void testConstructorWithLocale() {
        String pattern = "{0,choice,0#Hello|1#Hi} {1,choice,0#friend|1#enemy}.";
        Locale locale = Locale.US;
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, locale);
        Object[] args = {1, "friend"};
        String result = format.format(args);
        Assertions.assertEquals("Hi friend.", result);

        args = new Object[]{2, "enemy"};
        result = format.format(args);
        Assertions.assertEquals("Hello enemy.", result);
    }

    /**
     * Test constructor with registry.
     */
    @Test
    public void testConstructorWithRegistry() {
        String pattern = "{0,date,short,dd.MM.yyyy}";
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date.MED", new DateFormatFactory());

        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, registry);
        Object[] args = {"2022-03-31"};
        String result = format.format(args);
        Assertions.assertEquals("31.03.2022", result);
    }

    /**
     * Test toPattern.
     */
    @Test
    public void testToPattern() {
        String pattern = "{0,choice,0#Hello|1#Hi} {1,choice,0#friend|1#enemy}.";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, Locale.US);
        String expectedPattern = "{0,choice,0#Hello|1#Hi} {1,choice,0#friend|1#enemy}. ";
        String resultPattern = format.toPattern();
        Assertions.assertEquals(expectedPattern, resultPattern);
    }

    /**
     * Test applying pattern.
     */
    @Test
    public void testApplyingPattern() {
        String pattern = "{0,choice,0#Hello|1#Hi} {1,choice,0#friend|1#enemy}. ";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, Locale.US);
        Object[] args = {1, "friend"};
        format.applyPattern("{1,choice,0#foe|1#enemy}");
        String result = format.format(args);
        Assertions.assertEquals("Hi friend.", result);

        args = new Object[]{2, "enemy"};
        result = format.format(args);
        Assertions.assertEquals("Hello enemy.", result);

        String expectedPattern = "{0,choice,0#Hello|1#Hi} {1,choice,0#foe|1#enemy}. ";
        String resultPattern = format.toPattern();
        Assertions.assertEquals(expectedPattern, resultPattern);
    }

    /**
     * Test invalid argument index.
     */
    @Test
    public void testInvalidArgumentIndex() {
        String pattern = "{invalidIndex}";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, Locale.US);
        Object[] args = new Object[0];
        Assertions.assertThrows(IllegalArgumentException.class, () -> format.format(args));
    }

    /**
     * Test invalid format name.
     */
    @Test
    public void testInvalidFormatName() {
        String pattern = "{0,invalidFormat}";
        ExtendedMessageFormat format = new ExtendedMessageFormat(pattern, Locale.US, new HashMap<>());
        Object[] args = new Object[]{1};
        String result = "";
        try {
            result = format.format(args);
        } catch (MissingResourceException e) {
            // expected
        }
        Assertions.assertEquals("", result);
    }
}

class DateFormatFactory implements FormatFactory {

    @Override
    public Format getFormat(String name, String arguments, Locale locale) {
        if ("date.MED".equals(name)) {
            return new SimpleDateFormat("dd.MM.yyyy", locale);
        }
        throw new IllegalArgumentException("Invalid format name: " + name);
    }
}