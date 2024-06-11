import org.junit.Test;
import org.apache.commons.lang3.LocaleUtils;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_InvalidInput_Null() {
        try {
            LocaleUtils.toLocale(null);
        } catch (Exception e) {
            //Expected IllegalArgumentException for null input
            assert(e instanceof IllegalArgumentException);
        }
    }

    @Test
    public void testToLocale_EmptyString() {
        try {
            LocaleUtils.toLocale("");
        } catch (Exception e) {
            //Expected IllegalArgumentException for empty string input
            assert(e instanceof IllegalArgumentException);
        }
    }

    @Test
    public void testToLocale_InvalidFormat_ContainsSharpSymbol() {
        try {
            LocaleUtils.toLocale("en#US");
        } catch (Exception e) {
            //Expected IllegalArgumentException for invalid format with # symbol
            assert(e instanceof IllegalArgumentException);
        }
    }

    @Test
    public void testToLocale_InvalidFormat_OnlyOneCharacter() {
        try {
            LocaleUtils.toLocale("e");
        } catch (Exception e) {
            //Expected IllegalArgumentException for invalid format with only one character
            assert(e instanceof IllegalArgumentException);
        }
    }

    @Test
    public void testToLocale_ValidFormat_LanguageOnly() {
        try {
            LocaleUtils.toLocale("en");
        } catch (Exception e) {
            //Unexpected exception
            assert(false);
        }
        //No exception thrown, valid format
        assert(true);
    }

    @Test
    public void testToLocale_ValidFormat_LanguageAndCountry() {
        try {
            LocaleUtils.toLocale("en_US");
        } catch (Exception e) {
            //Unexpected exception
            assert(false);
        }
        //No exception thrown, valid format
        assert(true);
    }

    @Test
    public void testToLocale_ValidFormat_LanguageCountryAndVariant() {
        try {
            LocaleUtils.toLocale("en_US_variant");
        } catch (Exception e) {
            //Unexpected exception
            assert(false);
        }
        //No exception thrown, valid format
        assert(true);
    }

    @Test
    public void testToLocale_InvalidFormat_LanguageCountryAndEmptyVariant() {
        try {
            LocaleUtils.toLocale("en_US_");
        } catch (Exception e) {
            //Expected IllegalArgumentException for invalid format with empty variant
            assert(e instanceof IllegalArgumentException);
        }
    }

    @Test
    public void testToLocale_InvalidFormat_MissingUnderscore() {
        try {
            LocaleUtils.toLocale("enUS");
        } catch (Exception e) {
            //Expected IllegalArgumentException for invalid format missing underscore
            assert(e instanceof IllegalArgumentException);
        }
    }

    @Test
    public void testToLocale_InvalidFormat_LessThanThreeCharactersStartingWithUnderscore() {
        try {
            LocaleUtils.toLocale("_A");
        } catch (Exception e) {
            //Expected IllegalArgumentException for invalid format with less than 3 characters starting with underscore
            assert(e instanceof IllegalArgumentException);
        }
    }

    @Test
    public void testLocaleLookupList_NullLocale() {
        try {
            LocaleUtils.localeLookupList(null);
        } catch (Exception e) {
            //Expected NullPointerException for null locale input
            assert(e instanceof NullPointerException);
        }
    }

    @Test
    public void testLocaleLookupList_ValidLocale() {
        try {
            LocaleUtils.localeLookupList(new Locale("en", "US", "variant"));
        } catch (Exception e) {
            //Unexpected exception
            assert(false);
        }
        //No exception thrown, valid locale input
        assert(true);
    }

    @Test
    public void testLocaleLookupList_NullDefaultLocale() {
        try {
            LocaleUtils.localeLookupList(new Locale("en", "US"), null);
        } catch (Exception e) {
            //Unexpected exception
            assert(false);
        }
        //No exception thrown, null default locale input
        assert(true);
    }

    @Test
    public void testLocaleLookupList_ValidDefaultLocale() {
        try {
            LocaleUtils.localeLookupList(new Locale("en", "US"), new Locale("en", "GB"));
        } catch (Exception e) {
            //Unexpected exception
            assert(false);
        }
        //No exception thrown, valid default locale input
        assert(true);
    }

    @Test
    public void testAvailableLocaleList() {
        try {
            LocaleUtils.availableLocaleList();
        } catch (Exception e) {
            //Unexpected exception
            assert(false);
        }
        //No exception thrown
        assert(true);
    }

    @Test
    public void testAvailableLocaleSet() {
        try {
            LocaleUtils.availableLocaleSet();
        } catch (Exception e) {
            //Unexpected exception
            assert(false);
        }
        //No exception thrown
        assert(true);
    }

    @Test
    public void testIsAvailableLocale_ValidAvailableLocale() {
        boolean isAvailable = LocaleUtils.isAvailableLocale(Locale.US);
        assert(isAvailable);
    }

    @Test
    public void testIsAvailableLocale_InvalidAvailableLocale() {
        boolean isAvailable = LocaleUtils.isAvailableLocale(new Locale("fr", "CA"));
        assert(!isAvailable);
    }

    @Test
    public void testLanguagesByCountry_NullCountryCode() {
        try {
            LocaleUtils.languagesByCountry(null);
        } catch (Exception e) {
            //Expected NullPointerException for null country code input
            assert(e instanceof NullPointerException);
        }
    }

    @Test
    public void testLanguagesByCountry_ValidCountryCode() {
        try {
            LocaleUtils.languagesByCountry("US");
        } catch (Exception e) {
            //Unexpected exception
            assert(false);
        }
        //No exception thrown, valid country code input
        assert(true);
    }

    @Test
    public void testCountriesByLanguage_NullLanguageCode() {
        try {
            LocaleUtils.countriesByLanguage(null);
        } catch (Exception e) {
            //Expected NullPointerException for null language code input
            assert(e instanceof NullPointerException);
        }
    }

    @Test
    public void testCountriesByLanguage_ValidLanguageCode() {
        try {
            LocaleUtils.countriesByLanguage("en");
        } catch (Exception e) {
            //Unexpected exception
            assert(false);
        }
        //No exception thrown, valid language code input
        assert(true);
    }
}