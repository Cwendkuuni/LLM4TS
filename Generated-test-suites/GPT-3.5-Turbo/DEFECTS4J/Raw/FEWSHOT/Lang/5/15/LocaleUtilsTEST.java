import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class LocaleUtilsTest {
    @Test
    public void testToLocale() {
        assertNull(LocaleUtils.toLocale(null));
        assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));
        assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("EN_GB"));
        assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_gb"));
        assertEquals(new Locale("en", ""), LocaleUtils.toLocale("EN"));
        assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("EN_GB_XXX"));
        assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en_"));
        assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB_"));
    }

    @Test
    public void testLocaleLookupList() {
        assertEquals(Arrays.asList(new Locale("fr","CA","xxx"), new Locale("fr","CA"), new Locale("fr"), new Locale("en")),
                          LocaleUtils.localeLookupList(new Locale("fr", "CA", "xxx"), new Locale("en")));

        assertEquals(Arrays.asList(new Locale("fr","CA","xxx"), new Locale("fr","CA"), new Locale("fr")),
                          LocaleUtils.localeLookupList(new Locale("fr", "CA", "xxx")));
        
        assertEquals(Arrays.asList(new Locale("en")),
                          LocaleUtils.localeLookupList(new Locale("en")));
        
        assertEquals(Arrays.asList(new Locale("en"), new Locale("es"), new Locale("fr")),
                          LocaleUtils.localeLookupList(new Locale("en"), new Locale("fr")));
              
        assertEquals(Arrays.asList(new Locale("es"), new Locale("fr")),
                          LocaleUtils.localeLookupList(new Locale("es"), new Locale("fr")));
        
        assertEquals(Collections.singletonList(new Locale("fr")),
                          LocaleUtils.localeLookupList(new Locale("fr")));
        
        assertEquals(Collections.singletonList(new Locale("en")),
                          LocaleUtils.localeLookupList(new Locale("en"), new Locale("en")));
    }

    @Test
    public void testAvailableLocaleList() {
        assertEquals(Arrays.asList(Locale.getAvailableLocales()), LocaleUtils.availableLocaleList());
    }

    @Test
    public void testAvailableLocaleSet() {
        assertEquals(new HashSet<>(Arrays.asList(Locale.getAvailableLocales())), LocaleUtils.availableLocaleSet());
    }

    @Test
    public void testIsAvailableLocale() {
        assertEquals(true, LocaleUtils.isAvailableLocale(Locale.ENGLISH));
        assertEquals(false, LocaleUtils.isAvailableLocale(new Locale("xx")));
    }

    @Test
    public void testLanguagesByCountry() {
        assertEquals(Collections.emptyList(), LocaleUtils.languagesByCountry(null));
        assertEquals(Collections.emptyList(), LocaleUtils.languagesByCountry("xx"));
        assertEquals(Arrays.asList(Locale.ENGLISH, Locale.GERMAN), LocaleUtils.languagesByCountry("DE"));
        assertEquals(Arrays.asList(Locale.FRENCH), LocaleUtils.languagesByCountry("FR"));
    }

    @Test
    public void testCountriesByLanguage() {
        assertEquals(Collections.emptyList(), LocaleUtils.countriesByLanguage(null));
        assertEquals(Collections.emptyList(), LocaleUtils.countriesByLanguage("xx"));
        assertEquals(Arrays.asList(Locale.GERMANY), LocaleUtils.countriesByLanguage("DE"));
        assertEquals(Arrays.asList(new Locale("en", "GB")), LocaleUtils.countriesByLanguage("EN"));
    }
}