package org.jsoup.nodes;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.CharsetEncoder;

public class EntitiesTest {
    
    @Test
    public void testIsNamedEntity(){
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("gt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertTrue(Entities.isNamedEntity("apos"));
        assertTrue(Entities.isNamedEntity("quot"));
        assertFalse(Entities.isNamedEntity("test"));
    }
    
    @Test
    public void testGetCharacterByName(){
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertEquals(Character.valueOf('>'), Entities.getCharacterByName("gt"));
        assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
        assertEquals(Character.valueOf('\''), Entities.getCharacterByName("apos"));
        assertEquals(Character.valueOf('"'), Entities.getCharacterByName("quot"));
        assertNull(Entities.getCharacterByName("test"));
    }
    
    @Test
    public void testEscape(){
        Document.OutputSettings out = new Document.OutputSettings();
        out.charset(CharsetEncoder.getDefaultCharset().name());
        assertEquals("&lt;test&gt;&amp;", Entities.escape("<test>&", out));
    }
    
    @Test
    public void testUnescape(){
        assertEquals("<test>&", Entities.unescape("&lt;test&gt;&amp;"));
    }
    
    @Test
    public void testLoadEntities(){
        assertNotNull(Entities.loadEntities("entities-base.properties"));
        assertNotNull(Entities.loadEntities("entities-full.properties"));
    }
    
    @Test
    public void testToCharacterKey(){
        Map<String, Character> inMap = new HashMap<>();
        inMap.put("lt", '<');
        inMap.put("gt", '>');
        inMap.put("amp", '&');
        inMap.put("apos", '\'');
        inMap.put("quot", '"');
        Map<Character, String> outMap = Entities.toCharacterKey(inMap);
        assertTrue(outMap.containsKey('<'));
        assertTrue(outMap.containsKey('>'));
        assertTrue(outMap.containsKey('&'));
        assertTrue(outMap.containsKey('\''));
        assertTrue(outMap.containsKey('"'));
    }
}