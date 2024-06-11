import org.junit.Test;
import org.apache.commons.lang3.LocaleUtils;
import java.util.Locale;

public class LocaleUtilsTest {
    
    @Test
    public void testToLocale() {
        Locale locale = LocaleUtils.toLocale("en");
        assert(locale.getLanguage().equals("en"));
        assert(locale.getCountry().isEmpty());
    }
    
    @Test
    public void testToLocaleWithCountry() {
        Locale locale = LocaleUtils.toLocale("en_GB");
        assert(locale.getLanguage().equals("en"));
        assert(locale.getCountry().equals("GB"));
    }
    
    @Test
    public void testToLocaleWithVariant() {
        Locale locale = LocaleUtils.toLocale("en_GB_xxx");
        assert(locale.getLanguage().equals("en"));
        assert(locale.getCountry().equals("GB"));
        assert(locale.getVariant().equals("xxx"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testToLocaleWithInvalidFormat() {
        LocaleUtils.toLocale("invalid");
    }
    
    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> lookupList = LocaleUtils.localeLookupList(locale);
        assert(lookupList.get(0).equals(locale));
        assert(lookupList.get(1).equals(new Locale("fr", "CA")));
        assert(lookupList.get(2).equals(new Locale("fr")));
    }
    
    @Test
    public void testLocaleLookupListWithDefaultLocale() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");
        List<Locale> lookupList = LocaleUtils.localeLookupList(locale, defaultLocale);
        assert(lookupList.get(0).equals(locale));
        assert(lookupList.get(1).equals(new Locale("fr", "CA")));
        assert(lookupList.get(2).equals(new Locale("fr")));
        assert(lookupList.get(3).equals(defaultLocale));
    }
    
    @Test
    public void testAvailableLocaleList() {
        List<Locale> availableLocales = LocaleUtils.availableLocaleList();
        assert(availableLocales.size() > 0);
        assert(availableLocales.get(0) instanceof Locale);
    }
    
    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> availableLocales = LocaleUtils.availableLocaleSet();
        assert(availableLocales.size() > 0);
        assert(availableLocales.iterator().next() instanceof Locale);
    }
    
    @Test
    public void testIsAvailableLocale() {
        Locale locale = new Locale("en");
        assert(LocaleUtils.isAvailableLocale(locale));
    }
    
    @Test
    public void testIsAvailableLocaleWithInvalidLocale() {
        Locale locale = new Locale("invalid");
        assert(!LocaleUtils.isAvailableLocale(locale));
    }
    
    @Test
    public void testLanguagesByCountry() {
        List<Locale> languages = LocaleUtils.languagesByCountry("CA");
        assert(languages.size() > 0);
        assert(languages.get(0) instanceof Locale);
    }
    
    @Test
    public void testLanguagesByCountryWithInvalidCountryCode() {
        List<Locale> languages = LocaleUtils.languagesByCountry("invalid");
        assert(languages.isEmpty());
    }
    
    @Test
    public void testCountriesByLanguage() {
        List<Locale> countries = LocaleUtils.countriesByLanguage("fr");
        assert(countries.size() > 0);
        assert(countries.get(0) instanceof Locale);
    }
    
    @Test
    public void testCountriesByLanguageWithInvalidLanguageCode() {
        List<Locale> countries = LocaleUtils.countriesByLanguage("invalid");
        assert(countries.isEmpty());
    }
}