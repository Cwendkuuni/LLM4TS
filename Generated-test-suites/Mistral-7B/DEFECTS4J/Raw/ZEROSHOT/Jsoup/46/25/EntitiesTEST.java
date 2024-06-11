package org.jsoup.nodes;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class EntitiesTest {

    private static Entities entities;
    private static Map<String, Character> fullEntities;
    private static Map<Character, String> xhtmlEntities;
    private static Map<Character, String> baseEntities;

    @BeforeClass
    public static void setup() {
        entities = new Entities();
        fullEntities = Entities.full;
        xhtmlEntities = Entities.xhtmlByVal;
        baseEntities = Entities.baseByVal;
    }

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {true, "isNamedEntity", "lt", true},
                {true, "isNamedEntity", "lt", false},
                {true, "isNamedEntity", "amp", true},
                {true, "isNamedEntity", "amp", false},
                {true, "isNamedEntity", "quot", true},
                {true, "isNamedEntity", "quot", false},
                {true, "isNamedEntity", "gt", true},
                {true, "isNamedEntity", "gt", false},

                {true, "isBaseNamedEntity", "amp", true},
                {true, "isBaseNamedEntity", "amp", false},
                {true, "isBaseNamedEntity", "lt", true},
                {true, "isBaseNamedEntity", "lt", false},
                {true, "isBaseNamedEntity", "gt", true},
                {true, "isBaseNamedEntity", "gt", false},
                {true, "isBaseNamedEntity", "quot", true},
                {true, "isBaseNamedEntity", "quot", false},

                {true, "getCharacterByName", "lt", '<'},
                {true, "getCharacterByName", "amp", '&'},
                {true, "getCharacterByName", "quot", '"'},
                {true, "getCharacterByName", "gt", '>'},

                {false, "isNamedEntity", "invalid", false},
                {false, "isBaseNamedEntity", "invalid", false},

                {true, "escape", "&", "&amp;"},
                {true, "escape", "<", "&lt;"},
                {true, "escape", ">", "&gt;"},
                {true, "escape", "\"", "&quot;"},
                {true, "escape", "&amp;", "&amp;amp;"},
                {true, "escape", "&lt;lt;", "&amp;lt;lt;"},
                {true, "escape", "&gt;gt;", "&amp;gt;gt;"},
                {true, "escape", "abcdef", "abcdef"},
                {true, "escape", "你好，世界！", "&#x4f60;&#x597d;&#x5b8c;&#x4f8b;&#x7231;&#x56fd;&#x597d;"},

                {false, "escape", "", ""},
                {false, "escape", null, ""},

                {true, "unescape", "&amp;", "&"},
                {true, "unescape", "&lt;", "<"},
                {true, "unescape", "&gt;", ">"},
                {true, "unescape", "&quot;", "\""},
                {true, "unescape", "&amp;amp;", "&"},
                {true, "unescape", "&lt;lt;", "<"},
                {true, "unescape", "&gt;gt;", ">"},
                {true, "unescape", "&#x4f60;&#x597d;&#x5b8c;&#x4f8b;&#x7231;&#x56fd;&#x597d;", "你好，世界！"},

                {false, "unescape", "", ""},
                {false, "unescape", null, ""}
        });
    }

    private boolean isEntitySet;
    private String testMethodName;
    private String entityName;
    private Character expectedCharacter;
    private boolean testIsNamedEntity;

    public EntitiesTest(boolean isEntitySet, String testMethodName, String entityName, Character expectedCharacter, boolean testIsNamedEntity) {
        this.isEntitySet = isEntitySet;
        this.testMethodName = testMethodName;
        this.entityName = entityName;
        this.expectedCharacter = expectedCharacter;
        this.testIsNamedEntity = testIsNamedEntity;
    }

    @Test
    public void testEntities() {
        if (isEntitySet) {
            if (testIsNamedEntity) {
                boolean result = Entities.isNamedEntity(entityName);
                assertEquals(testMethodName + " returned expected result for named entity: ", expectedCharacter == null ? true : expectedCharacter == Entities.getCharacterByName(entityName), result);
            } else {
                boolean result = Entities.isBaseNamedEntity(entityName);
                assertEquals(testMethodName + " returned expected result for base named entity: ", expectedCharacter == null ? baseEntities.containsKey(entityName.charAt(0)) : baseEntities.get(entityName.charAt(0)) == expectedCharacter, result);
            }
        } else {
            if (testIsNamedEntity) {
                boolean result = Entities.isNamedEntity(entityName);
                assertEquals(testMethodName + " returned expected result for invalid entity name: ", false, result);
            } else {
                boolean result = Entities.isBaseNamedEntity(entityName);
                assertEquals(testMethodName + " returned expected result for invalid entity name: ", false, result);
            }
        }
    }

    @Test
    public void testGetCharacterByName() {
        Character result;
        if (isEntitySet) {
            result = Entities.getCharacterByName(entityName);
            if (expectedCharacter != null) {
                assertEquals(testMethodName + " returned expected character for given entity name: ", expectedCharacter, result);
            } else {
                assertTrue(testMethodName + " returned expected result for unknown entity name: ", Entities.isNamedEntity(entityName));
            }
        } else {
            result = Entities.getCharacterByName(entityName);
            assertEquals(testMethodName + " returned expected result for invalid entity name: ", false, result != null);
        }
    }

    @Test
    public void testEscape() {
        String stringToEscape;
        String expectedEncodedString;
        if (isEntitySet) {
            stringToEscape = "test";
            expectedEncodedString = testIsNamedEntity ? xhtmlEntities.get(Entities.getCharacterByName(entityName)) : baseEntities.get(expectedCharacter);
            Entities.escape(new StringBuilder(), stringToEscape, null, false, false, false);
            String accum = StringBuilder.class.cast(Entities.escape(stringToEscape, null)).toString();
            assertEquals(testMethodName + " returned expected encoded string for given input: ", expectedEncodedString, accum);
        } else {
            stringToEscape = entityName;
            String accum = Entities.escape(entityName, null);
            assertEquals(testMethodName + " returned expected result for invalid entity name: ", false, accum != null);
        }
    }

    @Test
    public void testUnescape() {
        String stringToUnescape;
        String expectedDecodedString;
        if (isEntitySet) {
            stringToUnescape = testIsNamedEntity ? xhtmlEntities.get(expectedCharacter) : baseEntities.get(expectedCharacter.toString());
            expectedDecodedString = String.valueOf(expectedCharacter);
            String decodedString = Entities.unescape(stringToUnescape);
            assertEquals(testMethodName + " returned expected decoded string for given input: ", expectedDecodedString, decodedString);
        } else {
            stringToUnescape = entityName;
            String decodedString = Entities.unescape(entityName, false);
            assertEquals(testMethodName + " returned expected result for invalid entity name: ", false, decodedString != null);
        }
    }
}