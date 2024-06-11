import org.junit.Test;
import java.util.*;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_ValidString() {
        Locale locale = LocaleUtils.toLocale("en");
        assert(locale.getLanguage().equals("en"));
        assert(locale.getCountry().equals(""));
        assert(locale.getVariant().equals(""));
    }

    @Test
    public void testToLocale_ValidString_CountryVariant() {
        Locale locale = LocaleUtils.toLocale("en_GB_xxx");
        assert(locale.getLanguage().equals("en"));
        assert(locale.getCountry().equals("GB"));
        assert(locale.getVariant().equals("xxx"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidString() {
        Locale locale = LocaleUtils.toLocale("foo");
    }

    @Test
    public void testLocaleLookupList_Locale() {
        Locale locale = LocaleUtils.toLocale("fr_CA_xxx");
        List<Locale> list = LocaleUtils.localeLookupList(locale);
        assert(list.size() == 3);
        assert(list.get(0).equals(locale));
        assert(list.get(1).equals(LocaleUtils.toLocale("fr_CA")));
        assert(list.get(2).equals(LocaleUtils.toLocale("fr")));
    }

    @Test
    public void testLocaleLookupList_Locale_Default() {
        Locale locale = LocaleUtils.toLocale("fr_CA_xxx");
        Locale defaultLocale = LocaleUtils.toLocale("en");
        List<Locale> list = LocaleUtils.localeLookupList(locale, defaultLocale);
        assert(list.size() == 4);
        assert(list.get(0).equals(locale));
        assert(list.get(1).equals(LocaleUtils.toLocale("fr_CA")));
        assert(list.get(2).equals(LocaleUtils.toLocale("fr")));
        assert(list.get(3).equals(defaultLocale));
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> list = LocaleUtils.availableLocaleList();
        assert(list.size() > 0);
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> set = LocaleUtils.availableLocaleSet();
        assert(set.size() > 0);
    }

    @Test
    public void testIsAvailableLocale_True() {
        Locale locale = LocaleUtils.toLocale("en_GB");
        boolean result = LocaleUtils.isAvailableLocale(locale);
        assert(result);
    }

    @Test
    public void testIsAvailableLocale_False() {
        Locale locale = LocaleUtils.toLocale("xx_YY");
        boolean result = LocaleUtils.isAvailableLocale(locale);
        assert(!result);
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> list = LocaleUtils.languagesByCountry("CA");
        assert(list.size() > 0);
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> list = LocaleUtils.countriesByLanguage("fr");
        assert(list.size() > 0);
    }
}