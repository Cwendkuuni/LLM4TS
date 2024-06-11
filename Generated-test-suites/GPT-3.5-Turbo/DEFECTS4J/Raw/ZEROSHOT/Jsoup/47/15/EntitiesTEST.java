package org.jsoup.nodes;

import org.junit.Test;
import java.util.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        // Test the isNamedEntity method with known named entities
        assert Entities.isNamedEntity("lt");
        assert Entities.isNamedEntity("amp");
        assert Entities.isNamedEntity("quote");
        assert Entities.isNamedEntity("apos");
    }

    @Test
    public void testIsBaseNamedEntity() {
        // Test the isBaseNamedEntity method with known named entities in the base set
        assert Entities.isBaseNamedEntity("lt");
        assert Entities.isBaseNamedEntity("amp");
        assert Entities.isBaseNamedEntity("quote");
        assert Entities.isBaseNamedEntity("apos");
    }

    @Test
    public void testGetCharacterByName() {
        // Test the getCharacterByName method to get characters of named entities
        assert Entities.getCharacterByName("lt").equals('<');
        assert Entities.getCharacterByName("amp").equals('&');
        assert Entities.getCharacterByName("quote").equals('"');
        assert Entities.getCharacterByName("apos").equals('\'');
    }

    @Test
    public void testEscape() {
        // Test the escape method
        String escaped = Entities.escape("<tag>");
        assert escaped.equals("&lt;tag&gt;");
    }

    @Test
    public void testUnescape() {
        // Test the unescape method
        String unescaped = Entities.unescape("&lt;tag&gt;");
        assert unescaped.equals("<tag>");
    }
}