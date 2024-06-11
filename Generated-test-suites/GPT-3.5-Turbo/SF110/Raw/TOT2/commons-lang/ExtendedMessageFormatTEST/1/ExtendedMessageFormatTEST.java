import static org.junit.Assert.*;

import java.text.Format;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.junit.Test;

public class ExtendedMessageFormatTest {

    @Test
    public void testConstructor1() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test");
        assertNotNull(emf);
    }

    @Test
    public void testConstructor2() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test", Locale.US);
        assertNotNull(emf);
    }

    @Test
    public void testConstructor3() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("number", NumberFormat::getInstance);
        
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test", registry);
        assertNotNull(emf);
    }

    @Test
    public void testConstructor4() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("date", SimpleDateFormat::new);
        
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test", Locale.US, registry);
        assertNotNull(emf);
    }

    @Test
    public void testToPattern() {
        ExtendedMessageFormat emf = new ExtendedMessageFormat("Test");
        assertEquals("Test", emf.toPattern());
    }

    // Add more test cases for other methods

    @Test
    public void testEquals() {
        ExtendedMessageFormat emf1 = new ExtendedMessageFormat("Test");
        ExtendedMessageFormat emf2 = new ExtendedMessageFormat("Test");
        
        assertEquals(emf1, emf2);
        
        emf2.setFormat(0, NumberFormat.getCurrencyInstance());
        assertNotEquals(emf1, emf2);
    }
    
    @Test
    public void testHashCode() {
        ExtendedMessageFormat emf1 = new ExtendedMessageFormat("Test");
        ExtendedMessageFormat emf2 = new ExtendedMessageFormat("Test");
        
        assertEquals(emf1.hashCode(), emf2.hashCode());
        
        emf2.setFormat(0, NumberFormat.getCurrencyInstance());
        assertNotEquals(emf1.hashCode(), emf2.hashCode());
    }

    // Add more test cases for other methods

}