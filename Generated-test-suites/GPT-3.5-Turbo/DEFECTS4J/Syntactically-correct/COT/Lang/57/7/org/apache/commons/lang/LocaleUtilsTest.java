import org.junit.Test;

public class LocaleUtilsTest {

    @Test
    public void testToLocale() {
        // Test valid input
        Locale locale1 = LocaleUtils.toLocale("en");
        assertEquals(new Locale("en", ""), locale1);

        Locale locale2 = LocaleUtils.toLocale("en_GB");
        assertEquals(new Locale("en", "GB"), locale2);

        Locale locale3 = LocaleUtils.toLocale("en_GB_xxx");
        assertEquals(new Locale("en", "GB", "xxx"), locale3);

        // Test invalid input
        try {
            LocaleUtils.toLocale(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected IllegalArgumentException
        }

        try {
            LocaleUtils.toLocale("eng");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected IllegalArgumentException
        }

        try {
            LocaleUtils.toLocale("ENG_GB");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected IllegalArgumentException
        }

        try {
            LocaleUtils.toLocale("en_GB_");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected IllegalArgumentException
        }
    }

    @Test
    public void testLocaleLookupList() {
        // Test with non-null locales
        List<Locale> list1 = LocaleUtils.localeLookupList(new Locale("fr", "CA", "xxx"));
        List<Locale> expectedList1 = new ArrayList<>();
        expectedList1.add(new Locale("fr", "CA", "xxx"));
        expectedList1.add(new Locale("fr", "CA"));
        expectedList1.add(new Locale("fr"));
        assertEquals(expectedList1, list1);

        List<Locale> list2 = LocaleUtils.localeLookupList(new Locale("fr", "CA", "xxx"), new Locale("en"));
        List<Locale> expectedList2 = new ArrayList<>();
        expectedList2.add(new Locale("fr", "CA", "xxx"));
        expectedList2.add(new Locale("fr", "CA"));
        expectedList2.add(new Locale("fr"));
        expectedList2.add(new Locale("en"));
        assertEquals(expectedList2, list2);

        List<Locale> list3 = LocaleUtils.localeLookupList(Locale.getDefault());
        List<Locale> expectedList3 = new ArrayList<>();
        expectedList3.add(Locale.getDefault());
        expectedList3.add(new Locale(Locale.getDefault().getLanguage(), ""));
        expectedList3.add(new Locale(Locale.getDefault().getLanguage()));
        assertEquals(expectedList3, list3);

        // Test with null locales
        List<Locale> list4 = LocaleUtils.localeLookupList(null);
        List<Locale> expectedList4 = new ArrayList<>();
        assertEquals(expectedList4, list4);

        List<Locale> list5 = LocaleUtils.localeLookupList(null, Locale.getDefault());
        List<Locale> expectedList5 = new ArrayList<>();
        expectedList5.add(Locale.getDefault());
        assertEquals(expectedList5, list5);
    }

    @Test
    public void testAvailableLocaleList() {
        List<Locale> list = LocaleUtils.availableLocaleList();
        assertTrue(list.contains(Locale.US));
        assertTrue(list.contains(Locale.FRANCE));
        assertTrue(list.contains(Locale.GERMANY));
        assertTrue(list.contains(Locale.ITALY));
    }

    @Test
    public void testAvailableLocaleSet() {
        Set<Locale> set = LocaleUtils.availableLocaleSet();
        assertTrue(set.contains(Locale.US));
        assertTrue(set.contains(Locale.FRANCE));
        assertTrue(set.contains(Locale.GERMANY));
        assertTrue(set.contains(Locale.ITALY));
    }

    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.US));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.FRANCE));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.GERMANY));
        assertTrue(LocaleUtils.isAvailableLocale(Locale.ITALY));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("xx", "XX")));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("xx")));
    }

    @Test
    public void testLanguagesByCountry() {
        List<Locale> list1 = LocaleUtils.languagesByCountry("CA");
        List<Locale> expectedList1 = new ArrayList<>();
        expectedList1.add(new Locale("en", "CA", ""));
        expectedList1.add(new Locale("fr", "CA", ""));
        assertEquals(expectedList1, list1);

        List<Locale> list2 = LocaleUtils.languagesByCountry("FR");
        List<Locale> expectedList2 = new ArrayList<>();
        expectedList2.add(new Locale("fr", "FR", ""));
        assertEquals(expectedList2, list2);

        List<Locale> list3 = LocaleUtils.languagesByCountry(null);
        List<Locale> expectedList3 = new ArrayList<>();
        assertEquals(expectedList3, list3);
    }

    @Test
    public void testCountriesByLanguage() {
        List<Locale> list1 = LocaleUtils.countriesByLanguage("en");
        List<Locale> expectedList1 = new ArrayList<>();
        expectedList1.add(new Locale("en", "CA", ""));
        expectedList1.add(new Locale("en", "GB", ""));
        expectedList1.add(new Locale("en", "", ""));
        assertEquals(expectedList1, list1);

        List<Locale> list2 = LocaleUtils.countriesByLanguage("fr");
        List<Locale> expectedList2 = new ArrayList<>();
        expectedList2.add(new Locale("fr", "CA", ""));
        expectedList2.add(new Locale("fr", "FR", ""));
        assertEquals(expectedList2, list2);

        List<Locale> list3 = LocaleUtils.countriesByLanguage(null);
        List<Locale> expectedList3 = new ArrayList<>();
        assertEquals(expectedList3, list3);
    }
}