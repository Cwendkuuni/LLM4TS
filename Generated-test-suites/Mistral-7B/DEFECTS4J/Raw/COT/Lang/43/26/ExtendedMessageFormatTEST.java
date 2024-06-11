import org.apache.commons.lang.text.ExtendedMessageFormat;
import org.apache.commons.lang.text.FormatFactory;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExtendedMessageFormatTest {

    

    @Test
    public void testExtendedMessageFormatConstructor() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0,number,currency} {1}");
        assertNotNull(format);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExtendedMessageFormatConstructorWithInvalidPattern() {
        new ExtendedMessageFormat("{0,number,currency} {1} {2}"); // extra argument index
    }

    @Test
    public void testExtendedMessageFormatConstructorWithNullPattern() {
        try {
            new ExtendedMessageFormat(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testExtendedMessageFormatConstructorWithEmptyRegistry() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0,myFormat}", null);
        assertEquals("", format.toPattern());
    }

    @Test
    public void testExtendedMessageFormatConstructorWithRegistry() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0,myFormat}", Map.of("myFormat", new MyFormatFactory()));
        assertEquals("{0,myFormat}", format.toPattern());
    }

    @Test
    public void testApplyPattern() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0,number,currency} {1,date,long,EN}");
        format.applyPattern("{0,number,currency} {1,date,long,EN}");
        assertEquals("{0,number,currency} {1,date,long,EN}", format.toPattern());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testApplyPatternWithInvalidFormatName() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0,myFormat} {1,date,long,EN}");
        format.applyPattern("{0,myFormat} {1,date,long,EN}"); // invalid format name "myFormat"
    }

    @Test(expected = IllegalArgumentException.class)
    public void testApplyPatternWithMissingFormatFactory() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0,myFormat} {1,date,long,EN}", Map.of());
        format.applyPattern("{0,myFormat} {1,date,long,EN}"); // missing format factory for "myFormat"
    }

    @Test(expected = IllegalArgumentException.class)
    public void testApplyPatternWithInvalidFormatStyle() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0,myFormat,short} {1,date,long,EN}", Map.of("myFormat", new MyFormatFactory()));
        format.applyPattern("{0,myFormat,short} {1,date,long,EN}"); // invalid format style "short" for "myFormat"
    }

    @Test
    public void testToPattern() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0,number,currency} {1,date,long,EN}");
        assertEquals("{0,number,currency} {1,date,long,EN}", format.toPattern());
    }

    @Test
    public void testGetFormat() {
        FormatFactory formatFactory = new FormatFactory() {
            @Override
            public Format getFormat(String formatName, String formatStyle, Locale locale) {
                return new SimpleDateFormat(formatStyle, locale);
            }
        };
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0,date,long,EN} {1,date,short,EN}", Map.of("date", formatFactory));
        Format dateLongFormat = format.getFormat("date", "long", Locale.ENGLISH);
        assertEquals(SimpleDateFormat.class, dateLongFormat.getClass());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormat() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0,number,currency} {1,date,long,EN}");
        format.setFormat(0, new SimpleDateFormat("dd/MM/yyyy")); // UnsupportedOperationException
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndex() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0,number,currency} {1,date,long,EN}");
        format.setFormatByArgumentIndex(1, new SimpleDateFormat("dd/MM/yyyy")); // UnsupportedOperationException
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormats() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0,number,currency} {1,date,long,EN}");
        Format[] formats = {new SimpleDateFormat("dd/MM/yyyy"), new SimpleDateFormat("dd-MM-yy")};
        format.setFormats(formats); // UnsupportedOperationException
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndex() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("{0,number,currency} {1,date,long,EN}");
        Format[] formats = {new SimpleDateFormat("dd/MM/yyyy"), new SimpleDateFormat("dd-MM-yy")};
        format.setFormatsByArgumentIndex(new Format[]{formats[0], formats[1]}); // UnsupportedOperationException
    }

    
}

// Custom FormatFactory and SimpleDateFormat for testing
interface FormatFactory {
    Format getFormat(String formatName, String formatStyle, Locale locale);
}

class MyFormatFactory implements FormatFactory {
    @Override
    public Format getFormat(String formatName, String formatStyle, Locale locale) {
        return new SimpleFormat();
    }
}

class SimpleFormat extends Format {
    // empty implementation for testing
}

class SimpleDateFormat extends Format {
    public SimpleDateFormat(String format, Locale locale) {
        // empty constructor for testing
    }
}