import org.junit.Test;
import org.junit.Assert.*;
import java.util.Locale;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class LocaleUtilsTest {

 private static final LocaleUtils LOCALE_UTILS = new LocaleUtils();

 @Test
 public void testToLocale() {
     String[] testCases = new String[]{"en", "en_GB", "en_GB_XXX"};
     Locale[] expectedLocales = new Locale[]{new Locale("en", ""), new Locale("en", "GB"), new Locale("en", "GB", "XXX")};

     for (int i = 0; i < testCases.length; i++) {
         Locale actualLocale = LOCALE_UTILS.toLocale(testCases[i]);
         assertEquals(expectedLocales[i].getDisplayName(), actualLocale.getDisplayName());
     }
 }

 @Test
 public void testLocaleLookupList() {
     Locale baseLocale = new Locale("fr", "CA", "xxx");
     Locale defaultLocale = new Locale("en");

     List<Locale> expectedLocaleList = Arrays.asList(
         new Locale("fr", "CA", "xxx"), new Locale("fr", "CA"), new Locale("fr"), new Locale("en")
     );

     List<Locale> localeLookupList = LOCALE_UTILS.localeLookupList(baseLocale);
     assertEquals(expectedLocaleList, localeLookupList);

     localeLookupList = LOCALE_UTILS.localeLookupList(baseLocale, defaultLocale);
     assertEquals(expectedLocaleList, localeLookupList);

     localeLookupList = LOCALE_UTILS.localeLookupList(null);
     assertNotNull(localeLookupList);
     assertTrue(Collections.isEmpty(localeLookupList));
 }

 @Test
 public void testAvailableLocaleList() {
     List<Locale> availableLocaleList = LOCALE_UTILS.availableLocaleList();
     List<Locale> expectedAvailableLocaleList = Arrays.asList(Locale.getAvailableLocales());

     assertEquals(expectedAvailableLocaleList.size(), availableLocaleList.size());

     for (int i = 0; i < expectedAvailableLocaleList.size(); i++) {
         Locale expectedLocale = expectedAvailableLocaleList.get(i);
         Locale actualLocale = availableLocaleList.get(i);

         assertEquals(expectedLocale.getDisplayName(), actualLocale.getDisplayName());
     }
 }

 @Test
 public void testAvailableLocaleSet() {
     Set<Locale> availableLocaleSet = LOCALE_UTILS.availableLocaleSet();
     Set<Locale> expectedAvailableLocaleSet = Collections.unmodifiableSet(new HashSet<>(Locale.getAvailableLocales()));

     assertEquals(expectedAvailableLocaleSet.size(), availableLocaleSet.size());

     for (Locale locale : expectedAvailableLocaleSet) {
         assertTrue(availableLocaleSet.contains(locale));
     }
 }

 @Test
 public void testIsAvailableLocale() {
     Locale locale = new Locale("fr", "CA");
     Locale unavailableLocale = new Locale("xyz", "xyz");

     assertTrue(LOCALE_UTILS.isAvailableLocale(locale));
     assertFalse(LOCALE_UTILS.isAvailableLocale(unavailableLocale));
 }

 @Test
 public void testLanguagesByCountry() {
     List<Locale> expectedLocaleList = Arrays.asList(new Locale("fr", "CA"), new Locale("en", "CA"));

     List<Locale> languagesByCountry = LOCALE_UTILS.languagesByCountry("CA");

     assertEquals(expectedLocaleList, languagesByCountry);
 }

 @Test
 public void testCountriesByLanguage() {
     List<Locale> expectedLocaleList = Arrays.asList(new Locale("CA", "fr"), new Locale("CA", "en"));

     List<Locale> countriesByLanguage = LOCALE_UTILS.countriesByLanguage("fr");

     assertEquals(expectedLocaleList, countriesByLanguage);
 }
}