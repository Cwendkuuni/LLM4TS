package org.apache.commons.codec.language;

import org.junit.Assert;
import org.junit.Test;

public class MetaphoneTest {

    @Test
    public void testMetaphoneEncodings() {
        Metaphone encoder = new Metaphone();

        String[] words = { "apple", "appel", "apples", "applesauce", "banana", "bananam", "bananana", "cat", "catnip", "chair", "cherry", "cherries", "cherrybomb", "dog", "doghouse", "egg", "eggs", "elephant", "elephants", "grape", "grapes", "hello", "helloWorld", "how", "hownowbrowncow", "i", "iPhone", "kangaroo", "kangaroos", "lemon", "lemons", "mango", "mangoose", "monkey", "monkeys", "orange", "oranges", "pineapple", "pineapples", "potato", "potatoes", "quince", "quincey", "queen", "queens", "strawberry", "strawberries", "tiger", "tigers", "tomato", "tomatoes", "watermelon", "watermelons", "yellow", "yellowstone" };

        for (String word : words) {
            String code = encoder.metaphone(word);
            String expectedCode = expectedMetaphoneCodes.get(word);
            Assert.assertEquals("Incorrect metaphone encoding for word: " + word, expectedCode, code);
        }
    }

    @Test(expected = EncoderException.class)
    public void testEncodesNull() {
        Metaphone encoder = new Metaphone();
        encoder.encode(null);
    }

    @Test(expected = EncoderException.class)
    public void testEncodesEmptyString() {
        Metaphone encoder = new Metaphone();
        encoder.encode("");
    }

    @Test(expected = EncoderException.class)
    public void testEncodesNonString() {
        Metaphone encoder = new Metaphone();
        encoder.encode(new Object());
    }

    private Map<String, String> expectedMetaphoneCodes = new HashMap<>();

    static {
        expectedMetaphoneCodes.put("apple", "APEL");
        expectedMetaphoneCodes.put("appel", "APEL");
        expectedMetaphoneCodes.put("apples", "APLS");
        expectedMetaphoneCodes.put("applesauce", "APSLS");
        expectedMetaphoneCodes.put("banana", "BANA");
        expectedMetaphoneCodes.put("bananam", "BANM");
        expectedMetaphoneCodes.put("bananana", "BANN");
        expectedMetaphoneCodes.put("cat", "KAT");
        expectedMetaphoneCodes.put("catnip", "KPNP");
        expectedMetaphoneCodes.put("chair", "KR");
        expectedMetaphoneCodes.put("cherry", "CHRY");
        expectedMetaphoneCodes.put("cherries", "CHRYS");
        expectedMetaphoneCodes.put("cherrybomb", "CHRBMB");
        expectedMetaphoneCodes.put("dog", "DOK");
        expectedMetaphoneCodes.put("doghouse", "DOKHUS");
        expectedMetaphoneCodes.put("egg", "EGG");
        expectedMetaphoneCodes.put("eggs", "EGS");
        expectedMetaphoneCodes.put("elephant", "ELPHT");
        expectedMetaphoneCodes.put("elephants", "ELPHTS");
        expectedMetaphoneCodes.put("grape", "GRAPE");
        expectedMetaphoneCodes.put("grapes", "GRAPES");
        expectedMetaphoneCodes.put("hello", "HHLL");
        expectedMetaphoneCodes.put("helloWorld", "HHLLWRLD");
        expectedMetaphoneCodes.put("how", "HUW");
        expectedMetaphoneCodes.put("hownowbrowncow", "HHNBRNCOW");
        expectedMetaphoneCodes.put("i", "I");
        expectedMetaphoneCodes.put("iPhone", "IPHN");
        expectedMetaphoneCodes.put("kangaroo", "KNGARU");
        expectedMetaphoneCodes.put("kangaroos", "KNGAROS");
        expectedMetaphoneCodes.put("lemon", "LEMON");
        expectedMetaphoneCodes.put("lemons", "LEMNS");
        expectedMetaphoneCodes.put("mango", "MNG");
        expectedMetaphoneCodes.put("mangoose", "MNGUZ");
        expectedMetaphoneCodes.put("monkey", "MNKI");
        expectedMetaphoneCodes.put("monkeys", "MNKS");
        expectedMetaphoneCodes.put("orange", "ORNJ");
        expectedMetaphoneCodes.put("oranges", "ORNJS");
        expectedMetaphoneCodes.put("pineapple", "PNPPL");
        expectedMetaphoneCodes.put("pineapples", "PNPPLS");
        expectedMetaphoneCodes.put("potato", "PTTO");
        expectedMetaphoneCodes.put("potatoes", "PTTS");
        expectedMetaphoneCodes.put("quince", " KNTS");
        expectedMetaphoneCodes.put("quincey", " KNTS");
        expectedMetaphoneCodes.put("queen", "QN");
        expectedMetaphoneCodes.put("queens", "QNS");
        expectedMetaphoneCodes.put("strawberry", "STRWRY");
        expectedMetaphoneCodes.put("strawberries", "STRWRBS");
        expectedMetaphoneCodes.put("tiger", "TG");
        expectedMetaphoneCodes.put("tigers", "TGS");
        expectedMetaphoneCodes.put("tomato", "TMTO");
        expectedMetaphoneCodes.put("tomatoes", "TMTS");
        expectedMetaphoneCodes.put("watermelon", "WTRMLN");
        expectedMetaphoneCodes.put("watermelons", "WTRMLNS");
        expectedMetaphoneCodes.put("yellow", "YLOW");
        expectedMetaphoneCodes.put("yellowstone", "YLOWSTN");
    }
}