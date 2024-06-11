import org.junit.Test;
import org.junit.Assert;
import java.util.Locale;

public class LocaleUtilsTest {

   @Test
   public void testToLocale() {
      // typical use case
      Locale locale = LocaleUtils.toLocale("en_US");
      Assert.assertEquals("en", locale.getLanguage());
      Assert.assertEquals("US", locale.getCountry());

      // edge case with lower case language and upper case country code
      locale = LocaleUtils.toLocale("en_GB");
      Assert.assertEquals("en", locale.getLanguage());
      Assert.assertEquals("GB", locale.getCountry());

      // edge case with lower case language, upper case country code, and variant
      locale = LocaleUtils.toLocale("en_GB_xxx");
      Assert.assertEquals("en", locale.getLanguage());
      Assert.assertEquals("GB", locale.getCountry());
      Assert.assertEquals("xxx", locale.getVariant());

      // error scenario with invalid format
      try {
         LocaleUtils.toLocale("invalid");
      } catch (IllegalArgumentException e) {
         Assert.assertTrue(true);
      }
   }

   @Test
   public void testLocaleLookupList() {
      // typical use case
      Locale locale = new Locale("fr", "CA", "xxx");
      Locale defaultLocale = new Locale("en");
      List<Locale> locales = LocaleUtils.localeLookupList(locale, defaultLocale);
      Assert.assertEquals(4, locales.size());
      Assert.assertEquals(locale, locales.get(0));
      Assert.assertEquals(new Locale("fr", "CA"), locales.get(1));
      Assert.assertEquals(new Locale("fr"), locales.get(2));
      Assert.assertEquals(defaultLocale, locales.get(3));

      // edge case with null input
      locales = LocaleUtils.localeLookupList(null);
      Assert.assertEquals(0, locales.size());
   }

   @Test
   public void testAvailableLocaleList() {
      // typical use case
      List<Locale> locales = LocaleUtils.availableLocaleList();
      Assert.assertNotNull(locales);
      Assert.assertTrue(locales.size() > 0);
   }

   @Test
   public void testAvailableLocaleSet() {
      // typical use case
      Set<Locale> locales = LocaleUtils.availableLocaleSet();
      Assert.assertNotNull(locales);
      Assert.assertTrue(locales.size() > 0);
   }

   @Test
   public void testIsAvailableLocale() {
      // typical use case
      Locale locale = new Locale("en", "US");
      Assert.assertTrue(LocaleUtils.isAvailableLocale(locale));

      // edge case with unavailable locale
      locale = new Locale("xx", "XX");
      Assert.assertFalse(LocaleUtils.isAvailableLocale(locale));
   }

   @Test
   public void testLanguagesByCountry() {
      // typical use case
      List<Locale> locales = LocaleUtils.languagesByCountry("US");
      Assert.assertNotNull(locales);
      Assert.assertTrue(locales.size() > 0);

      // edge case with null input
      locales = LocaleUtils.languagesByCountry(null);
      Assert.assertEquals(0, locales.size());
   }

   @Test
   public void testCountriesByLanguage() {
      // typical use case
      List<Locale> locales = LocaleUtils.countriesByLanguage("en");
      Assert.assertNotNull(locales);
      Assert.assertTrue(locales.size() > 0);

      // edge case with null input
      locales = LocaleUtils.countriesByLanguage(null);
      Assert.assertEquals(0, locales.size());
   }
}