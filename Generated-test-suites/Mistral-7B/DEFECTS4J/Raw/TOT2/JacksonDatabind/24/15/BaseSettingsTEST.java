import org.junit.Test;
import static org.junit.Assert.assertEquals;
import com.fasterxml.jackson.databind.cfg.BaseSettings;

public class BaseSettingsTest {
    @Test
    public void testBaseSettingsWithDefaultValues() {
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, Locale.getDefault(),
                TimeZone.getDefault(), Base64Variant.STANDARD);
        assertEquals(Locale.getDefault(), baseSettings.getLocale());
        assertEquals(TimeZone.getDefault(), baseSettings.getTimeZone());
        assertEquals(Base64Variant.STANDARD, baseSettings.getBase64Variant());
    }

    @Test
    public void testBaseSettingsWithCustomValues() {
        DateFormat customDateFormat = new StdDateFormat();
        HandlerInstantiator customHandlerInstantiator = new HandlerInstantiator() {
            // implementation here
        };
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, customDateFormat, customHandlerInstantiator,
                Locale.US, TimeZone.getTimeZone("America/New_York"), Base64Variant.BASE64_URL_SAFE_VARIANT);
        assertEquals(Locale.US, baseSettings.getLocale());
        assertEquals(TimeZone.getTimeZone("America/New_York"), baseSettings.getTimeZone());
        assertEquals(Base64Variant.BASE64_URL_SAFE_VARIANT, baseSettings.getBase64Variant());
    }
}