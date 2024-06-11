import org.apache.commons.lang.text.ExtendedMessageFormat;
import org.apache.commons.lang.text.Format;
import org.apache.commons.lang.text.FormatFactory;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExtendedMessageFormatTest {

    

    @Test
    public void testEmptyConstructor() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("");
        assertEquals("", format.toPattern());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPatternWithRegistryNull() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0, date, short}");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPatternWithRegistry() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("DateFormatFactory", new FormatFactoryImpl());
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0, date, short, DateFormatFactory}", Locale.getDefault(), registry);
        format.applyPattern("{0, date, short, DateFormatFactory, invalid}");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidFormatName() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("DateFormatFactory", new FormatFactoryImpl());
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0, date, short, DateFormatFactory}", Locale.getDefault(), registry);
        format.applyPattern("{0, date, short, invalid}");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidFormatStyle() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("DateFormatFactory", new FormatFactoryImpl());
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0, date, short, DateFormatFactory}", Locale.getDefault(), registry);
        format.applyPattern("{0, date, short, DateFormatFactory, short}");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormat() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}", Locale.getDefault());
        format.setFormat(0, new Format() {});
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndex() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}", Locale.getDefault());
        format.setFormatByArgumentIndex(0, new Format() {});
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormats() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}", Locale.getDefault());
        Format[] formats = new Format[1];
        formats[0] = new Format() {};
        format.setFormats(formats);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndex() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}", Locale.getDefault());
        Format[] formats = new Format[1];
        formats[0] = new Format() {};
        format.setFormatsByArgumentIndex(formats);
    }

    @Test
    public void testToString() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}", Locale.getDefault());
        assertEquals("ExtendedMessageFormat: format={}, arguments={}, locale={}", format.toString());
    }

    @Test
    public void testApplyPatternWithRegistryNull() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}");
        format.applyPattern("{0, date, short}");
        assertEquals("{0}", format.toPattern());
    }

    @Test
    public void testApplyPatternWithRegistry() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("DateFormatFactory", new FormatFactoryImpl());
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}", Locale.getDefault(), registry);
        format.applyPattern("{0, date, short, DateFormatFactory}");
        assertEquals("{0}", format.toPattern());
    }

    @Test
    public void testApplyPatternWithCustomFormat() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("CustomDateFormatFactory", new CustomDateFormatFactory());
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}", Locale.getDefault(), registry);
        format.applyPattern("{0, date, short, CustomDateFormatFactory}");
        assertEquals("{0}", format.toPattern());
    }

    @Test
    public void testApplyPatternWithCustomFormatAndCustomStyle() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("CustomDateFormatFactory", new CustomDateFormatFactory());
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0}", Locale.getDefault(), registry);
        format.applyPattern("{0, date, medium, CustomDateFormatFactory}");
        assertEquals("{0}", format.toPattern());
    }

    @Test
    public void testFormatWithRegistryNull() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0, date, short}", Locale.getDefault());
        Object result = format.format(new Object[]{"2022-01-01"}, new ParsePosition(0));
        assertEquals("01-Jan-2022", result);
    }

    @Test
    public void testFormatWithRegistry() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("DateFormatFactory", new FormatFactoryImpl());
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0, date, short, DateFormatFactory}", Locale.getDefault(), registry);
        Object result = format.format(new Object[]{"2022-01-01"}, new ParsePosition(0));
        assertEquals("01-Jan-2022", result);
    }

    @Test
    public void testFormatWithCustomFormat() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("CustomDateFormatFactory", new CustomDateFormatFactory());
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0, date, short, CustomDateFormatFactory}", Locale.getDefault(), registry);
        Object result = format.format(new Object[]{"2022-01-01"}, new ParsePosition(0));
        assertEquals("01-Jan-2022", result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatWithInvalidArgumentIndex() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0, date, short}", Locale.getDefault());
        Object result = format.format(new Object[]{"2022-01-01", "invalid"}, new ParsePosition(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatWithInvalidFormatName() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("DateFormatFactory", new FormatFactoryImpl());
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0, date, short, InvalidFormatFactory}", Locale.getDefault(), registry);
        Object result = format.format(new Object[]{"2022-01-01"}, new ParsePosition(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormatWithInvalidFormatStyle() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("DateFormatFactory", new FormatFactoryImpl());
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0, date, short, DateFormatFactory}", Locale.getDefault(), registry);
        Object result = format.format(new Object[]{"2022-01-01"}, new ParsePosition(0), "invalidStyle");
    }

    @Test
    public void testFormatWithRegistryAndLocaleChange() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("DateFormatFactory", new FormatFactoryImpl());
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0, date, short, DateFormatFactory}", Locale.ENGLISH, registry);
        Object result = format.format(new Object[]{"2022-01-01"}, new ParsePosition(0), Locale.FRENCH);
        assertEquals("01-jan.-2022", result);
    }

    @Test
    public void testFormatWithCustomFormatAndLocaleChange() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("CustomDateFormatFactory", new CustomDateFormatFactory());
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0, date, short, CustomDateFormatFactory}", Locale.ENGLISH, registry);
        Object result = format.format(new Object[]{"2022-01-01"}, new ParsePosition(0), Locale.FRENCH);
        assertEquals("01-jan.-2022", result);
    }

    
}