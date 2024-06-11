import org.junit.Before;
import org.junit.Test;
import java.text.Format;
import java.util.HashMap;
import static org.mockito.Mockito.*;

public class ExtendedMessageFormatTest {
    
    private static final String PATTERN = "This is a test {0} format {1}";
    
    private ExtendedMessageFormat extendedMessageFormat;
    private HashMap<String, FormatFactory> registry;
    
    @Before
    public void setup() {
        registry = new HashMap<>();
        registry.put("first", mock(FormatFactory.class));
        registry.put("second", mock(FormatFactory.class));
        extendedMessageFormat = new ExtendedMessageFormat(PATTERN, registry);
    }
    
    @Test
    public void testConstructor() {
        ExtendedMessageFormat extendedMessageFormat = new ExtendedMessageFormat(PATTERN);
        ExtendedMessageFormat extendedMessageFormatWithLocale = new ExtendedMessageFormat(PATTERN, Locale.getDefault());
        ExtendedMessageFormat extendedMessageFormatWithRegistry = new ExtendedMessageFormat(PATTERN, registry);
        ExtendedMessageFormat extendedMessageFormatWithLocaleAndRegistry = new ExtendedMessageFormat(PATTERN, Locale.getDefault(), registry);
    }
    
    @Test
    public void testToPattern() {
        String toPattern = extendedMessageFormat.toPattern();
    }
    
    @Test
    public void testApplyPattern() {
        extendedMessageFormat.applyPattern(PATTERN);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormat() {
        extendedMessageFormat.setFormat(0, mock(Format.class));
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatByArgumentIndex() {
        extendedMessageFormat.setFormatByArgumentIndex(0, mock(Format.class));
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormats() {
        Format[] formats = new Format[2];
        formats[0] = mock(Format.class);
        formats[1] = mock(Format.class);
        extendedMessageFormat.setFormats(formats);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testSetFormatsByArgumentIndex() {
        Format[] formats = new Format[2];
        formats[0] = mock(Format.class);
        formats[1] = mock(Format.class);
        extendedMessageFormat.setFormatsByArgumentIndex(formats);
    }
}