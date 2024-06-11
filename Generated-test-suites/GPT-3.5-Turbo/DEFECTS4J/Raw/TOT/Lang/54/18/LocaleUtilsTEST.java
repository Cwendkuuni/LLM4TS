import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleUtilsTest {

   // Test cases for toLocale(String str)
   @Test
   public void testToLocale_validFormat() {
      Locale locale = LocaleUtils.toLocale("en_US");
      assertEquals("en", locale.getLanguage());
      assertEquals("US", locale.getCountry());
   }
   
   @Test
   public void testToLocale_nullInput() {
      Locale locale = LocaleUtils.toLocale(null);
      assertNull(locale);
   }
   
   @Test(expected = IllegalArgumentException.class)
   public void testToLocale_invalidFormat() {
      Locale locale = LocaleUtils.toLocale("enUS");
   }
   
   // Test cases for localeLookupList(Locale locale)
   @Test
   public void testLocaleLookupList_validLocale() {
      Locale locale = new Locale("fr", "CA");
      List<Locale> localeList = LocaleUtils.localeLookupList(locale);
      assertEquals(3, localeList.size());
      assertEquals(locale, localeList.get(0));
      assertEquals(new Locale("fr", "CA"), localeList.get(1));
      assertEquals(new Locale("fr"), localeList.get(2));
   }
   
   @Test
   public void testLocaleLookupList_nullLocale() {
      List<Locale> localeList = LocaleUtils.localeLookupList(null);
      assertEquals(0, localeList.size());
   }
   
   // Test cases for localeLookupList(Locale locale, Locale defaultLocale)
   @Test
   public void testLocaleLookupList_validLocaleAndDefaultLocale() {
      Locale locale = new Locale("fr", "CA");
      Locale defaultLocale = new Locale("en");
      List<Locale> localeList = LocaleUtils.localeLookupList(locale, defaultLocale);
      assertEquals(4, localeList.size());
      assertEquals(locale, localeList.get(0));
      assertEquals(new Locale("fr", "CA"), localeList.get(1));
      assertEquals(new Locale("fr"), localeList.get(2));
      assertEquals(defaultLocale, localeList.get(3));
   }
   
   @Test
   public void testLocaleLookupList_nullLocaleAndDefaultLocale() {
      List<Locale> localeList = LocaleUtils.localeLookupList(null, null);
      assertEquals(0, localeList.size());
   }
   
   // Test cases for availableLocaleList()
   @Test
   public void testAvailableLocaleList() {
      List<Locale> localeList = LocaleUtils.availableLocaleList();
      assertNotNull(localeList);
      assertTrue(localeList.size() > 0);
   }
   
   // Test cases for availableLocaleSet()
   @Test
   public void testAvailableLocaleSet() {
      Set<Locale> localeSet = LocaleUtils.availableLocaleSet();
      assertNotNull(localeSet);
      assertTrue(localeSet.size() > 0);
   }
   
   // Test cases for isAvailableLocale(Locale locale)
   @Test
   public void testIsAvailableLocale_availableLocale() {
      Locale locale = new Locale("en");
      boolean result = LocaleUtils.isAvailableLocale(locale);
      assertTrue(result);
   }
   
   @Test
   public void testIsAvailableLocale_unavailableLocale() {
      Locale locale = new Locale("xx");
      boolean result = LocaleUtils.isAvailableLocale(locale);
      assertFalse(result);
   }
   
   // Test cases for languagesByCountry(String countryCode)
   @Test
   public void testLanguagesByCountry_validCountryCode() {
      List<Locale> languages = LocaleUtils.languagesByCountry("US");
      assertNotNull(languages);
      assertTrue(languages.size() > 0);
   }
   
   @Test
   public void testLanguagesByCountry_nullCountryCode() {
      List<Locale> languages = LocaleUtils.languagesByCountry(null);
      assertNotNull(languages);
      assertEquals(0, languages.size());
   }
   
   // Test cases for countriesByLanguage(String languageCode)
   @Test
   public void testCountriesByLanguage_validLanguageCode() {
      List<Locale> countries = LocaleUtils.countriesByLanguage("en");
      assertNotNull(countries);
      assertTrue(countries.size() > 0);
   }
   
   @Test
   public void testCountriesByLanguage_nullLanguageCode() {
      List<Locale> countries = LocaleUtils.countriesByLanguage(null);
      assertNotNull(countries);
      assertEquals(0, countries.size());
   }

}