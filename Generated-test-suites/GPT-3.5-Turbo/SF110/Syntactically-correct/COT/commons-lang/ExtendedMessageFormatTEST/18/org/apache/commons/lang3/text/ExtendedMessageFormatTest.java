import org.junit.Test;

public class ExtendedMessageFormatTest {
    
    @Test
    public void testExtendedMessageFormat() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello, {0}!");
        String result = format.format(new Object[] { "John" });
        assert(result.equals("Hello, John!"));
    }
    
    @Test
    public void testExtendedMessageFormatWithLocale() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello, {0}!", Locale.US);
        String result = format.format(new Object[] { "John" });
        assert(result.equals("Hello, John!"));
    }
    
    @Test
    public void testExtendedMessageFormatWithRegistry() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("custom", new CustomFormatFactory());
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello, {0,custom}!", registry);
        String result = format.format(new Object[] { "John" });
        assert(result.equals("Hello, John!"));
    }
    
    @Test
    public void testExtendedMessageFormatWithLocaleAndRegistry() {
        Map<String, FormatFactory> registry = new HashMap<>();
        registry.put("custom", new CustomFormatFactory());
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello, {0,custom}!", Locale.US, registry);
        String result = format.format(new Object[] { "John" });
        assert(result.equals("Hello, John!"));
    }
    
    @Test
    public void testToPattern() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello, {0}!");
        String pattern = format.toPattern();
        assert(pattern.equals("Hello, {0}!"));
    }
    
    @Test
    public void testApplyPattern() {
        ExtendedMessageFormat format = new ExtendedMessageFormat("Hello, {0}!");
        format.applyPattern("Goodbye, {0}!");
        String pattern = format.toPattern();
        assert(pattern.equals("Goodbye, {0}!"));
    }
}