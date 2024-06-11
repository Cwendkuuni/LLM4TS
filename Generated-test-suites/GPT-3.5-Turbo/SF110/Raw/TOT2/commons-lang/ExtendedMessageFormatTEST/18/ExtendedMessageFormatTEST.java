import static org.junit.Assert.*;
import org.junit.Test;
import java.text.Format;
import java.util.Locale;
import java.util.HashMap;
import org.apache.commons.lang3.text.ExtendedMessageFormat;

public class ExtendedMessageFormatTest {

    @Test
    public void testConstructorWithPatternAndLocale() {
        String pattern = "Hello, {0}!";
        Locale locale = Locale.US;
        ExtendedMessageFormat emf = new ExtendedMessageFormat(pattern, locale);
        assertNotNull(emf);
    }

    @Test
    public void testApplyPatternWithRegistry() {
        String pattern = "{0,number}, {1,date}";
        HashMap<String, Format> registry = new HashMap<>();
        registry.put("number", NumberFormat.getInstance());
        registry.put("date", DateFormat.getDateInstance());
        ExtendedMessageFormat emf = new ExtendedMessageFormat(pattern, registry);
        assertNotNull(emf.toPattern());
    }

    @Test
    public void testEquals() {
        ExtendedMessageFormat emf1 = new ExtendedMessageFormat("Test");
        ExtendedMessageFormat emf2 = new ExtendedMessageFormat("Test");
        assertEquals(emf1, emf2);
    }

    @Test
    public void testHashCode() {
        ExtendedMessageFormat emf1 = new ExtendedMessageFormat("Test");
        ExtendedMessageFormat emf2 = new ExtendedMessageFormat("Test");
        assertEquals(emf1.hashCode(), emf2.hashCode());
    }

    // Add more test cases for each method to cover edge cases and error scenarios

}