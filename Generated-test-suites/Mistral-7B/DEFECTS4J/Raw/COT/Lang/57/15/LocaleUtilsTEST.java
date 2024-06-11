import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Test valid inputs
        assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));

        // Test invalid inputs
        assertNull(LocaleUtils.toLocale(null));
        assertNull(LocaleUtils.toLocale(""));
        assertNull(LocaleUtils.toLocale("abcd"));
        assertNull(LocaleUtils.toLocale("en_"));
        assertNull(LocaleUtils.toLocale("en_AB"));
        assertNull(LocaleUtils.toLocale("en_AB_"));
        assertNull(LocaleUtils.toLocale("en_AB_cd"));
        assertNull(LocaleUtils.toLocale("en_AB_cd_"));
        assertNull(LocaleUtils.toLocale("en_AB_cd_ef"));

        // Test empty country and language codes
        assertEquals(Locale.getDefault(), LocaleUtils.toLocale(""));
        assertEquals(Locale.getDefault(), LocaleUtils.toLocale("_"));
        assertEquals(Locale.getDefault(), LocaleUtils.toLocale("xx"));
        assertEquals(Locale.getDefault(), LocaleUtils.toLocale("xx_"));
        assertEquals(Locale.getDefault(), LocaleUtils.toLocale("xx_yy"));
        assertEquals(Locale.getDefault(), LocaleUtils.toLocale("xx_yy_"));
        assertEquals(Locale.getDefault(), LocaleUtils.toLocale("xx_yy_zz"));
        assertEquals(Locale.getDefault(), LocaleUtils.toLocale("xx_yy_zz_"));

        // Test invalid country and language codes
        assertNull(LocaleUtils.toLocale("xx_ab"));
        assertNull(LocaleUtils.toLocale("ab_xx"));
        assertNull(LocaleUtils.toLocale("ab_xx_"));
        assertNull(LocaleUtils.toLocale("ab_xx_cd"));
        assertNull(LocaleUtils.toLocale("ab_xx_cd_"));
        assertNull(LocaleUtils.toLocale("ab_xx_cd_ef"));

        // Test invalid separator
        assertNull(LocaleUtils.toLocale("en_GB_cd"));
        assertNull(LocaleUtils.toLocale("en_GB_cd_"));
        assertNull(LocaleUtils.toLocale("en_GB_cd_ef"));
        assertNull(LocaleUtils.toLocale("en_GB_cd_ef_"));
        assertNull(LocaleUtils.toLocale("en_GB_cd_ef_g"));
        assertNull(LocaleUtils.toLocale("en_GB_cd_ef_g_"));
    }

    @Test
    public void testLocaleLookupList() {
        // Test empty input
        List<Locale> result = LocaleUtils.localeLookupList(null);
        assertTrue(result.isEmpty());

        // Test valid input
        Locale input = new Locale("en", "CA", "xxx");
        List<Locale> expected = Arrays.asList(input, new Locale("en", "CA"), new Locale("en"), Locale.getDefault());
        List<Locale> actual = LocaleUtils.localeLookupList(input);
        assertEquals(expected, actual);

        // Test invalid input
        assertSame(Collections.emptyList(), LocaleUtils.localeLookupList(Locale.getDefault()));
    }

    @Test
    public void testIsAvailableLocale() {
        // Test available locale
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("en", "US")));

        // Test invalid input
        assertFalse(LocaleUtils.isAvailableLocale(null));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("ab", "cd")));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLanguagesByCountry() {
        LocaleUtils.languagesByCountry(null);
    }

    @Test
    public void testCountriesByLanguage() {
        // Test empty input
        List<Locale> result = LocaleUtils.countriesByLanguage(null);
        assertTrue(result.isEmpty());

        // Test valid inputs
        List<Locale> result1 = LocaleUtils.countriesByLanguage("en");
        List<Locale> expected1 = Arrays.asList(new Locale("CA"), new Locale("GB"), new Locale("US"));
        assertEquals(expected1, result1);

        List<Locale> result2 = LocaleUtils.countriesByLanguage("fr");
        List<Locale> expected2 = Arrays.asList(new Locale("CA"), new Locale("BE"), new Locale("CH"));
        assertEquals(expected2, result2);
    }
}