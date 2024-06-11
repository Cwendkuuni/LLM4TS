
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ExtendedMessageFormatTest {

    @Test
    public void testConstructorWithPatternAndLocale() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0,date} {0,time}", Locale.US);
        String pattern = format.toPattern();
        assertEquals("{0,date} {0,time}", pattern);
    }

    @Test
    public void testConstructorWithPatternAndRegistry() {
        FormatFactory factory = new CustomFormatFactory();
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("custom", factory);
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0,custom}", registry);
        String pattern = format.toPattern();
        assertEquals("{0,custom}", pattern);
    }

    @Test
    public void testConstructorWithPatternLocaleAndRegistry() {
        FormatFactory factory = new CustomFormatFactory();
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("custom", factory);
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0,custom}", Locale.US, registry);
        String pattern = format.toPattern();
        assertEquals("{0,custom}", pattern);
    }

    @Test
    public void testApplyPatternWithNoRegistry() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0,number,#.##}");
        format.applyPattern("{0,number,###.##}");
        String pattern = format.toPattern();
        assertEquals("{0,number,###.##}", pattern);
    }

    @Test
    public void testApplyPatternWithRegistry() {
        FormatFactory factory = new CustomFormatFactory();
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("custom", factory);
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0,custom}", registry);
        format.applyPattern("{0,number,###.##}");
        String pattern = format.toPattern();
        assertEquals("{0,number,###.##}", pattern);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormat() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("");
        format.setFormat(0, new DecimalFormat());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndex() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("");
        format.setFormatByArgumentIndex(0, new SimpleDateFormat());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormats() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("");
        format.setFormats(new Format[]{new DecimalFormat()});
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndex() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("");
        format.setFormatsByArgumentIndex(new Format[]{new SimpleDateFormat()});
    }

    @Test
    public void testEqualsAndHashCode() {
        ExtendedMessageFormat format1 = new ExtendedMessageFormat("{0,number,#}");
        ExtendedMessageFormat format2 = new ExtendedMessageFormat("{0,number,#}");
        ExtendedMessageFormat format3 = new ExtendedMessageFormat("{0,date}");
        assertEquals(format1, format2);
        assertEquals(format1.hashCode(), format2.hashCode());
        assertNotEquals(format1, format3);
        assertNotEquals(format1.hashCode(), format3.hashCode());
    }

    private class CustomFormatFactory implements FormatFactory {
        @Override
        public Format getFormat(String name, String arguments, Locale locale) {
            return new DecimalFormat();
        }
    }
}

