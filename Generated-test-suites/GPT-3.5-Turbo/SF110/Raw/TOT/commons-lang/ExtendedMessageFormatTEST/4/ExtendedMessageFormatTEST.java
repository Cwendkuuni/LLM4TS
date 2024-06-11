
import org.junit.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.text.*;
import java.util.*;

public class ExtendedMessageFormatTest {

    @Mock
    Map<String, ? extends FormatFactory> registryMock = mock(Map.class);

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testConstructorWithPatternAndLocale() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello, {0}!", Locale.US);
        assertEquals("Hello, {0}!", format.toPattern());
    }

    @Test
    public void testConstructorWithPatternAndLocaleAndRegistry() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello, {0}!", Locale.US, registryMock);
        assertEquals("Hello, {0}!", format.toPattern());
    }

    @Test
    public void testConstructorWithPatternAndRegistry() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello, {0}!", registryMock);
        assertEquals("Hello, {0}!", format.toPattern());
    }

    @Test
    public void testApplyPatternWithoutRegistry() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello, {0}!");
        format.applyPattern("Goodbye, {0}!");
        assertEquals("Goodbye, {0}!", format.toPattern());
    }

    @Test
    public void testApplyPatternWithRegistry() {
        when(registryMock.get(eq("date"))).thenReturn(new DateFormatFactory().getFormat("date", null, Locale.US));
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello, {0,date}!", registryMock);
        format.applyPattern("Goodbye, {0,date}!");
        assertEquals("Goodbye, {0,date}!", format.toPattern());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormat() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello, {0}!");
        format.setFormat(0, new DecimalFormat());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndex() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello, {0}!");
        format.setFormatByArgumentIndex(0, new DecimalFormat());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormats() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello, {0}!");
        format.setFormats(new Format[]{new DecimalFormat()});
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndex() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello, {0}!");
        format.setFormatsByArgumentIndex(new Format[]{new DecimalFormat()});
    }

    @Test
    public void testEquals() {
        ExtendedMessageFormat format1 = new ExtendedMessageFormat("Hello, {0}!");
        ExtendedMessageFormat format2 = new ExtendedMessageFormat("Hello, {0}!");
        ExtendedMessageFormat format3 = new ExtendedMessageFormat("Goodbye, {0}!");

        assertEquals(format1, format1);
        assertEquals(format1, format2);
        assertNotEquals(format1, format3);
    }

    @Test
    public void testHashCode() {
        ExtendedMessageFormat format1 = new ExtendedMessageFormat("Hello, {0}!");
        ExtendedMessageFormat format2 = new ExtendedMessageFormat("Hello, {0}!");
        ExtendedMessageFormat format3 = new ExtendedMessageFormat("Goodbye, {0}!");

        assertEquals(format1.hashCode(), format1.hashCode());
        assertEquals(format1.hashCode(), format2.hashCode());
        assertNotEquals(format1.hashCode(), format3.hashCode());
    }

    @Test
    public void testGetFormatWithExistingFactory() {
        when(registryMock.get(eq("date"))).thenReturn(new DateFormatFactory().getFormat("date", null, Locale.US));
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello, {0,date}!", registryMock);
        Format result = format.getFormat("date,YMD");
        assertNotNull(result);
    }

    @Test
    public void testGetFormatWithNonExistingFactory() {
        when(registryMock.get(eq("invalid"))).thenReturn(null);
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello, {0,invalid}!", registryMock);
        Format result = format.getFormat("invalid,args");
        assertNull(result);
    }

}
