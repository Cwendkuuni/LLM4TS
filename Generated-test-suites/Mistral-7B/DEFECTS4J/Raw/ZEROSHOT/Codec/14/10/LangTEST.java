import org.apache.commons.codec.language.bm.Lang;
import org.apache.commons.codec.language.bm.LangRule;
import org.apache.commons.codec.language.bm.Languages;
import org.apache.commons.codec.language.bm.NameType;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class LangTest {

    private static Lang englishLang;
    private static Lang germanLang;

    @BeforeClass
    public static void setup() {
        englishLang = Lang.instance(NameType.ENGLISH);
        germanLang = Lang.instance(NameType.GERMAN);
    }

    @Test
    public void testEnglishGuessing() {
        String[] englishTestCases = new String[]{"hello", "world", "apple", "banana", "computer"};

        for (String testCase : englishTestCases) {
            String result = englishLang.guessLanguage(testCase);
            assertEquals(Languages.ENGLISH, result);
        }
    }

    @Test
    public void testGermanGuessing() {
        String[] germanTestCases = new String[]{"hallo", "welt", "apfel", "banane", "computer"};

        for (String testCase : germanTestCases) {
            String result = germanLang.guessLanguage(testCase);
            assertEquals(Languages.GERMAN, result);
        }

        String[] englishTestCasesWithGerman = new String[]{"hallo", "welt", "apfel", "banane", "computer", "Morgen"};

        for (String testCase : englishTestCasesWithGerman) {
            String result = englishLang.guessLanguage(testCase);
            assertEquals(Languages.ANY, result);
        }
    }

    @Test
    public void testMixedGuessing() {
        String[] mixedTestCases = new String[]{"Morgen", "hallo", "welt", "apfel", "banane", "computer", "hello", "world", "apple", "banana"};

        for (String testCase : mixedTestCases) {
            String result = englishLang.guessLanguage(testCase);
            if (result.equals(Languages.GERMAN)) {
                result = germanLang.guessLanguage(testCase);
                assertEquals(testCase.toLowerCase(), result);
            } else {
                assertEquals(Languages.ENGLISH, result);
            }
        }
    }

    @Test
    public void testNoMatch() {
        String input = "test";
        String result = englishLang.guessLanguage(input);
        assertEquals(Languages.ANY, result);
    }

    @Test
    public void testEmptyString() {
        String input = "";
        String result = englishLang.guessLanguage(input);
        assertEquals(Languages.ANY, result);
    }

    @Test
    public void testSingleLanguageRule() {
        NameType testNameType = NameType.TEST;
        Lang testLang = Lang.loadFromResource("org/apache/commons/codec/language/bm/lang_test.txt", Languages.getInstance(testNameType));
        String testString = "test";
        List<LangRule> testRules = testLang.getRules();

        LangRule testRule = testRules.get(0);
        assertEquals("test", testRule.pattern.pattern());
        Set<String> testLanguages = testRule.languages;
        assertEquals(1, testLanguages.size());
        assertEquals("test", testLanguages.iterator().next());
        assertEquals(true, testRule.acceptOnMatch);

        String result = testLang.guessLanguage(testString);
        assertEquals(testNameType, testLang.getNameType());
        assertEquals(testLanguages, result.getLanguages());
    }

    @Test
    public void testMultipleLanguageRules() {
        NameType testNameType = NameType.TEST;
        Lang testLang = Lang.loadFromResource("org/apache/commons/codec/language/bm/lang_test_multiple.txt", Languages.getInstance(testNameType));
        String testString = "test";
        List<LangRule> testRules = testLang.getRules();

        LangRule firstRule = testRules.get(0);
        assertEquals("test", firstRule.pattern.pattern());
        Set<String> firstLanguages = firstRule.languages;
        assertEquals(1, firstLanguages.size());
        assertEquals("test", firstLanguages.iterator().next());
        assertEquals(true, firstRule.acceptOnMatch);

        LangRule secondRule = testRules.get(1);
        assertEquals("test2", secondRule.pattern.pattern());
        Set<String> secondLanguages = secondRule.languages;
        assertEquals(1, secondLanguages.size());
        assertEquals("test2", secondLanguages.iterator().next());
        assertEquals(false, secondRule.acceptOnMatch);

        String result = testLang.guessLanguage(testString);
        assertEquals(testNameType, testLang.getNameType());
        assertEquals(Collections.emptySet(), result.getLanguages());

        testString = "test2";
        result = testLang.guessLanguage(testString);
        assertEquals(testNameType, testLang.getNameType());
        assertEquals(secondLanguages, result.getLanguages());
    }

    @Test
    public void testMultilingualRule() {
        NameType testNameType = NameType.TEST;
        Lang testLang = Lang.loadFromResource("org/apache/commons/codec/language/bm/lang_test_multilingual.txt", Languages.getInstance(testNameType));
        String testString = "test";
        List<LangRule> testRules = testLang.getRules();

        LangRule multiRule = testRules.get(0);
        assertEquals("test", multiRule.pattern.pattern());
        Set<String> multiLanguages = multiRule.languages;
        assertEquals(2, multiLanguages.size());
        assertTrue(multiLanguages.contains("test1"));
        assertTrue(multiLanguages.contains("test2"));
        assertEquals(true, multiRule.acceptOnMatch);

        String result = testLang.guessLanguage(testString);
        assertEquals(testNameType, testLang.getNameType());
        assertEquals(multiLanguages, result.getLanguages());
    }

    @Test
    public void testCommentedRule() {
        NameType testNameType = NameType.TEST;
        Lang testLang = Lang.loadFromResource("org/apache/commons/codec/language/bm/lang_test_commented.txt", Languages.getInstance(testNameType));
        String testString = "test";
        List<LangRule> testRules = testLang.getRules();

        assertEquals(0, testRules.size());
    }
}