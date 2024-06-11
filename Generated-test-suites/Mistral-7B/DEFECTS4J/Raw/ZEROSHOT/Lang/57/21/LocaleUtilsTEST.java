import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // test empty string
        assertNull(LocaleUtils.toLocale(""));

        // test invalid locale
        assertNull(LocaleUtils.toLocale("xx"));
        assertNull(LocaleUtils.toLocale("en_XX"));
        assertNull(LocaleUtils.toLocale("en_GB_xx"));

        // test valid locales
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en"));
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en_GB"));
        assertEquals(Locale.ENGLISH, LocaleUtils.toLocale("en_GB_UK"));
        assertEquals(Locale.FRENCH, LocaleUtils.toLocale("fr"));
        assertEquals(Locale.FRENCH, LocaleUtils.toLocale("fr_CA"));
        assertEquals(Locale.FRENCH, LocaleUtils.toLocale("fr_CA_QC"));
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("en", "CA", "ON");
        List list = LocaleUtils.localeLookupList(locale);

        assertEquals(3, list.size());
        assertEquals(locale, list.get(0));
        assertEquals(new Locale("en", "CA"), list.get(1));
        assertEquals(Locale.ENGLISH, list.get(2));

        list = LocaleUtils.localeLookupList(Locale.ENGLISH);
        assertEquals(2, list.size());
        assertEquals(Locale.ENGLISH, list.get(0));
        assertEquals(Locale.ENGLISH, list.get(1));

        list = LocaleUtils.localeLookupList(null);
        assertEquals(0, list.size());
    }

    @Test
    public void testAvailableLocaleListAndSet() {
        List availableLocales = LocaleUtils.availableLocaleList();
        Set availableLocaleSet = LocaleUtils.availableLocaleSet();

        assertEquals(186, availableLocales.size());
        assertEquals(186, availableLocaleSet.size());

        // check lists are equal
        assertTrue(availableLocales.equals(new ArrayList(availableLocaleSet)));

        // test invalid locales
        assertFalse(availableLocaleSet.contains(new Locale("xx", "XX")));
        assertFalse(availableLocaleSet.contains(new Locale("en", "XX")));
        assertFalse(availableLocaleSet.contains(new Locale("en_XX")));
        assertFalse(availableLocaleSet.contains(new Locale("en_GB_xx")));

        // test valid locales
        assertTrue(availableLocaleSet.contains(Locale.ENGLISH));
        assertTrue(availableLocaleSet.contains(new Locale("fr", "CA")));
        assertTrue(availableLocaleSet.contains(new Locale("fr", "FR")));
    }

    @Test
    public void testIsAvailableLocale() {
        List availableLocales = LocaleUtils.availableLocaleList();

        // test unavailable locales
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("xx", "XX")));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("en", "XX")));
        assertFalse(LocaleUtils.isAvailableLocale(Locale.ENGLISH.getVariant()));

        // test available locales
        assertTrue(LocaleUtils.isAvailableLocale(Locale.ENGLISH));
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("fr", "CA")));
        assertTrue(LocaleUtils.isAvailableLocale(new Locale("fr", "FR")));

        // test invalid input
        assertFalse(LocaleUtils.isAvailableLocale(null));
        assertFalse(LocaleUtils.isAvailableLocale(""));
        assertFalse(LocaleUtils.isAvailableLocale("en"));
    }

    @Test
    public void testLanguagesByCountry() {
        List languages = LocaleUtils.languagesByCountry("CA");

        assertEquals(11, languages.size());
        Collection expected = Arrays.asList(
                new Locale("en", "CA"),
                new Locale("fr", "CA"),
                new Locale("cs", "CZ"),
                new Locale("da", "DK"),
                new Locale("de", "DE"),
                new Locale("es", "ES"),
                new Locale("et", "EE"),
                new Locale("fi", "FI"),
                new Locale("it", "IT"),
                new Locale("nl", "NL"),
                new Locale("pl", "PL")
        );
        assertTrue(Collections.disjoint(languages, expected));
    }

    @Test
    public void testCountriesByLanguage() {
        List countries = LocaleUtils.countriesByLanguage("en");

        assertEquals(26, countries.size());
        Collection expected = Arrays.asList(
                new Locale("AG", "AG"),
                new Locale("AI", "AS"),
                new Locale("AN", "AN"),
                new Locale("AO", "AO"),
                new Locale("AR", "AR"),
                new Locale("AT", "AT"),
                new Locale("AU", "AU"),
                new Locale("BE", "BE"),
                new Locale("BM", "BM"),
                new Locale("BR", "BR"),
                new Locale("BW", "BW"),
                new Locale("BZ", "BZ"),
                new Locale("CA", "CA"),
                new Locale("CH", "CH"),
                new Locale("CL", "CL"),
                new Locale("CM", "CM"),
                new Locale("CY", "CY"),
                new Locale("DE", "DE"),
                new Locale("DK", "DK"),
                new Locale("DO", "DO"),
                new Locale("EC", "EC"),
                new Locale("EE", "EE"),
                new Locale("EG", "EG"),
                new Locale("FJ", "FJ"),
                new Locale("FI", "FI"),
                new Locale("FO", "FO"),
                new Locale("FR", "FR"),
                new Locale("GB", "GB"),
                new Locale("GD", "GD"),
                new Locale("GG", "GG"),
                new Locale("GL", "GL"),
                new Locale("GR", "GR"),
                new Locale("GT", "GT"),
                new Locale("GU", "GU"),
                new Locale("HK", "HK"),
                new Locale("IE", "IE"),
                new Locale("IM", "IM"),
                new Locale("IL", "IL"),
                new Locale("IN", "IN"),
                new Locale("IQ", "IQ"),
                new Locale("IR", "IR"),
                new Locale("IS", "IS"),
                new Locale("IT", "IT"),
                new Locale("JE", "JE"),
                new Locale("JM", "JM"),
                new Locale("JO", "JO"),
                new Locale("KE", "KE"),
                new Locale("KI", "KI"),
                new Locale("KM", "KM"),
                new Locale("KW", "KW"),
                new Locale("KY", "KY"),
                new Locale("LA", "LA"),
                new Locale("LB", "LB"),
                new Locale("LC", "LC"),
                new Locale("LI", "LI"),
                new Locale("LK", "LK"),
                new Locale("LS", "LS"),
                new Locale("LT", "LT"),
                new Locale("LU", "LU"),
                new Locale("LV", "LV"),
                new Locale("LY", "LY"),
                new Locale("MA", "MA"),
                new Locale("MC", "MC"),
                new Locale("MD", "MD"),
                new Locale("ME", "ME"),
                new Locale("MG", "MG"),
                new Locale("MH", "MH"),
                new Locale("ML", "ML"),
                new Locale("MM", "MM"),
                new Locale("MO", "MO"),
                new Locale("MP", "MP"),
                new Locale("MQ", "MQ"),
                new Locale("MR", "MR"),
                new Locale("MS", "MS"),
                new Locale("MT", "MT"),
                new Locale("MU", "MU"),
                new Locale("MV", "MV"),
                new Locale("MX", "MX"),
                new Locale("MY", "MY"),
                new Locale("NA", "NA"),
                new Locale("NC", "NC"),
                new Locale("NE", "NE"),
                new Locale("NF", "NF"),
                new Locale("NG", "NG"),
                new Locale("NI", "NI"),
                new Locale("NL", "NL"),
                new Locale("NO", "NO"),
                new Locale("NP", "NP"),
                new Locale("NR", "NR"),
                new Locale("NU", "NU"),
                new Locale("NZ", "NZ"),
                new Locale("OM", "OM"),
                new Locale("PA", "PA"),
                new Locale("PE", "PE"),
                new Locale("PF", "PF"),
                new Locale("PG", "PG"),
                new Locale("PH", "PH"),
                new Locale("PK", "PK"),
                new Locale("PL", "PL"),
                new Locale("PM", "PM"),
                new Locale("PN", "PN"),
                new Locale("PR", "PR"),
                new Locale("PS", "PS"),
                new Locale("PT", "PT"),
                new Locale("PW", "PW"),
                new Locale("PY", "PY"),
                new Locale("QA", "QA"),
                new Locale("RE", "RE"),
                new Locale("RO", "RO"),
                new Locale("RS", "RS"),
                new Locale("RU", "RU"),
                new Locale("SA", "SA"),
                new Locale("SB", "SB"),
                new Locale("SC", "SC"),
                new Locale("SE", "SE"),
                new Locale("SG", "SG"),
                new Locale("SI", "SI"),
                new Locale("SK", "SK"),
                new Locale("SL", "SL"),
                new Locale("SM", "SM"),
                new Locale("SN", "SN"),
                new Locale("SO", "SO"),
                new Locale("SR", "SR"),
                new Locale("SS", "SS"),
                new Locale("ST", "ST"),
                new Locale("SV", "SV"),
                new Locale("SX", "SX"),
                new Locale("SY", "SY"),
                new Locale("SZ", "SZ"),
                new Locale("TC", "TC"),
                new Locale("TD", "TD"),
                new Locale("TG", "TG"),
                new Locale("TH", "TH"),
                new Locale("TJ", "TJ"),
                new Locale("TK", "TK"),
                new Locale("TL", "TL"),
                new Locale("TM", "TM"),
                new Locale("TN", "TN"),
                new Locale("TO", "TO"),
                new Locale("TR", "TR"),
                new Locale("TT", "TT"),
                new Locale("TV", "TV"),
                new Locale("TW", "TW"),
                new Locale("TZ", "TZ"),
                new Locale("UA", "UA"),
                new Locale("UG", "UG"),
                new Locale("US", "US"),
                new Locale("UY", "UY"),
                new Locale("UZ", "UZ"),
                new Locale("VA", "VA"),
                new Locale("VC", "VC"),
                new Locale("VE", "VE"),
                new Locale("VG", "VG"),
                new Locale("VI", "VI"),
                new Locale("VN", "VN"),
                new Locale("WF", "WF"),
                new Locale("WS", "WS"),
                new Locale("YE", "YE"),
                new Locale("ZA", "ZA"),
                new Locale("ZM", "ZM"),
                new Locale("ZW", "ZW")
        );
        assertTrue(Collections.disjoint(countries, expected));
    }
}