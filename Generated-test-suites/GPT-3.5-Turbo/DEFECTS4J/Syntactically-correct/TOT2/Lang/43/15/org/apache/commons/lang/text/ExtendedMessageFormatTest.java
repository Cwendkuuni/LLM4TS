import static org.junit.Assert.*;
import org.junit.Test;
import java.util.HashMap;
import java.util.Locale;

public class ExtendedMessageFormatTest {

    @Test
    public void testConstructorDefaultLocale() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test {0}");
        assertNotNull(emf);
    }

    @Test
    public void testConstructorWithLocale() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test {0}", Locale.US);
        assertNotNull(emf);
    }

    @Test
    public void testConstructorWithRegistry() {
        HashMap<String, Object> registry = new HashMap<>();
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test {0}", registry);
        assertNotNull(emf);
    }

    @Test
    public void testConstructorWithLocaleAndRegistry() {
        HashMap<String, Object> registry = new HashMap<>();
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test {0}", Locale.US, registry);
        assertNotNull(emf);
    }

    @Test
    public void testApplyPattern() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test {0}");
        emf.applyPattern("Test {0,number,#.##}");
        assertNotNull(emf.toPattern());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormat() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test {0}");
        emf.setFormat(0, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndex() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test {0}");
        emf.setFormatByArgumentIndex(0, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormats() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test {0}");
        emf.setFormats(new Format[1]);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndex() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test {0}");
        emf.setFormatsByArgumentIndex(new Format[1]);
    }

}