import org.junit.Test;
import static org.junit.Assert.*;

public class ExtendedMessageFormatTest {

    @Test
    public void testConstructorWithPatternAndLocale() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("{0,number,#.##}", Locale.ENGLISH);
        assertNotNull(emf);
    }

    @Test
    public void testConstructorWithPatternAndRegistry() {
        Map<String, FormatFactory> registry = new HashMap<>();
        ExtendedMessageFormat emf = new ExtendedMessageFormat("{0,date,yyyy-MM-dd}", registry);
        assertNotNull(emf);
    }

    @Test
    public void testToPattern() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("{0,number,#}");
        assertEquals("{0,number,#}", emf.toPattern());
    }

    @Test
    public void testApplyPattern() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("{0,number,#.##}");
        emf.applyPattern("{0,number,#.####}");
        assertEquals("{0,number,#.####}", emf.toPattern());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormat() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("{0,number,#}");
        emf.setFormat(0, NumberFormat.getCurrencyInstance());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndex() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("{0,number,#}");
        emf.setFormatByArgumentIndex(0, NumberFormat.getCurrencyInstance());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormats() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("{0,number,#}");
        Format[] formats = {NumberFormat.getInstance()};
        emf.setFormats(formats);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void setFormatsByArgumentIndex() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("{0,number,#}");
        Format[] formats = {NumberFormat.getInstance()};
        emf.setFormatsByArgumentIndex(formats);
    }

}