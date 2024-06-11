import static org.junit.Assert.*;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.text.ExtendedMessageFormat;
import org.apache.commons.lang.text.FormatFactory;
import org.mockito.Mockito;

import org.junit.Before;
import org.junit.Test;

public class ExtendedMessageFormatTest {

    private FormatFactory formatFactory;
    private Map<String, FormatFactory> registry;

    @Before
    public void setUp() {
        formatFactory = Mockito.mock(FormatFactory.class);
        registry = new HashMap<>();
        registry.put("CustomFormat", formatFactory);
    }

    @Test
    public void testConstructorWithPattern() {
        String pattern = "{} {0} {}";
        ExtendedMessageFormat testInstance = new ExtendedMessageFormat(pattern, registry);
        assertEquals(pattern, testInstance.toPattern());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithInvalidPattern() {
        String pattern = "{} {}";
        new ExtendedMessageFormat(pattern, registry);
    }

    @Test
    public void testSetLocale() {
        String pattern = "{} {0} {}";
        ExtendedMessageFormat testInstance = new ExtendedMessageFormat(pattern, registry, null);
        testInstance.setLocale(Locale.US);
        assertEquals(Locale.US, testInstance.getLocale());
    }

    @Test
    public void testApplyPattern() {
        String pattern = "{} {0} {}";
        ExtendedMessageFormat testInstance = new ExtendedMessageFormat(pattern, registry, null);
        testInstance.applyPattern(pattern);
        assertEquals(pattern, testInstance.toPattern());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testApplyPatternWithInvalidPattern() {
        String pattern = "{} {}";
        ExtendedMessageFormat testInstance = new ExtendedMessageFormat("", registry);
        testInstance.applyPattern(pattern);
    }

    @Test
    public void testGetFormat() {
        String formatName = "CustomFormat";
        Format expectedFormat = Mockito.mock(Format.class);
        Mockito.when(formatFactory.getFormat(formatName, null, Locale.US))
                .thenReturn(expectedFormat);

        ExtendedMessageFormat testInstance = new ExtendedMessageFormat("", registry);
        testInstance.applyPattern("{0 {}}");
        testInstance.setLocale(Locale.US);

        Format actualFormat = testInstance.getFormat(0);
        assertEquals(expectedFormat, actualFormat);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormat() {
        ExtendedMessageFormat testInstance = new ExtendedMessageFormat("", registry);
        testInstance.setFormat(0, Mockito.mock(Format.class));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndex() {
        ExtendedMessageFormat testInstance = new ExtendedMessageFormat("", registry);
        testInstance.setFormatsByArgumentIndex(new Format[1]);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormats() {
        ExtendedMessageFormat testInstance = new ExtendedMessageFormat("", registry);
        testInstance.setFormats(new Format[1]);
    }
}