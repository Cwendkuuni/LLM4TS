package org.apache.commons.lang.test; // replace with your test package if needed

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.test.formatter.FormatFactory;
import org.apache.commons.lang3.test.formatter.FormatFactoryImpl;
import org.apache.commons.text.ExtendedMessageFormat;
import org.apache.commons.text.SimpleFormat;
import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for ExtendedMessageFormat class.
 */
public class ExtendedMessageFormatTest {

    // Test data
    private static final String PATTERN = "Hello {0,choice,0#World|1#Hello}{1,choice,0#Foo|1#Bar}";
    private static final Object[] ARGS = new Object[] {"Foo", "Hello"};
    private static final Map<String, FormatFactory> REGISTRY = new HashMap<>();
    static {
        REGISTRY.put("SimpleFormat", new FormatFactoryImpl(SimpleFormat.class));
    }

    private ExtendedMessageFormat formatter;

    @Before
    public void setUp() {
        formatter = new ExtendedMessageFormat(PATTERN, REGISTRY);
    }

    /**
     * Test constructor with null pattern.
     */
    @Test
    public void testEmptyConstructor() {
        assertThrows(IllegalArgumentException.class, () -> {
            new ExtendedMessageFormat(null);
        });
    }

    /**
     * Test constructor with empty pattern.
     */
    @Test
    public void testEmptyPatternConstructor() {
        assertThrows(IllegalArgumentException.class, () -> {
            new ExtendedMessageFormat("");
        });
    }

    /**
     * Test constructor with valid pattern.
     */
    @Test
    public void testValidConstructor() {
        assertEquals(PATTERN, formatter.toPattern());
    }

    /**
     * Test applyPattern() method with empty pattern.
     */
    @Test
    public void testApplyEmptyPattern() {
        formatter = new ExtendedMessageFormat(DUMMY_PATTERN, REGISTRY);
        assertNull(formatter.toPattern());
    }

    /**
     * Test applyPattern() method with a pattern containing no format elements.
     */
    @Test
    public void testApplySimplePattern() {
        formatter = new ExtendedMessageFormat("Hello {0}", REGISTRY);
        assertEquals("Hello {0}", formatter.toPattern());
    }

    /**
     * Test applyPattern() method with a pattern containing one format element.
     */
    @Test
    public void testApplySingleFormatPattern() {
        formatter = new ExtendedMessageFormat("Hello {0}", REGISTRY);
        formatter.applyPattern(PATTERN);
        assertEquals(PATTERN, formatter.toPattern());
        assertArrayEquals(ARGS, formatter.format(ARGS));
    }

    /**
     * Test applyPattern() method with a pattern containing multiple format elements.
     */
    @Test
    public void testApplyComplexPattern() {
        formatter = new ExtendedMessageFormat("Hello {0}", REGISTRY);
        formatter.applyPattern(PATTERN);
        assertEquals(PATTERN, formatter.toPattern());
        assertArrayEquals(new Object[] {"Hello Foo", "Hello Hello"},
                formatter.format(new Object[] {"Foo", "Hello"}));
    }

    /**
     * Test applyPattern() method with a pattern containing invalid format elements.
     */
    @Test
    public void testApplyInvalidPattern() {
        formatter = new ExtendedMessageFormat(PATTERN, REGISTRY);
        formatter.applyPattern(DUMMY_PATTERN);
        assertEquals(PATTERN, formatter.toPattern());
    }

    /**
     * Test invalid format() method calls.
     */
    @Test
    public void testInvalidFormatMethods() {
        formatter = new ExtendedMessageFormat(PATTERN, REGISTRY);
        assertThrows(UnsupportedOperationException.class,
                () -> formatter.setFormat(0, new SimpleFormat("test")));
        assertThrows(UnsupportedOperationException.class,
                () -> formatter.setFormats(new SimpleFormat[] { new SimpleFormat("test") }));
        assertThrows(UnsupportedOperationException.class,
                () -> formatter.setFormatsByArgumentIndex(new SimpleFormat[] { new SimpleFormat("test") }));
    }
}