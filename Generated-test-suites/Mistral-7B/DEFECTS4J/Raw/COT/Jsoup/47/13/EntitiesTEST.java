package org.jsoup.nodes.test;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Entities;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Document.OutputSettings;

import java.util.HashMap;
import java.util.Map;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Entities entities = new Entities();
        String namedEntity = "lt";
        boolean result = entities.isNamedEntity(namedEntity);
        boolean expected = true;
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testIsNamedEntityWithInvalidName() {
        Entities entities = new Entities();
        String namedEntity = "invalid";
        boolean result = entities.isNamedEntity(namedEntity);
        boolean expected = false;
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testIsBaseNamedEntity() {
        Entities entities = new Entities();
        String baseNamedEntity = "amp";
        boolean result = entities.isBaseNamedEntity(baseNamedEntity);
        boolean expected = true;
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testIsBaseNamedEntityWithInvalidName() {
        Entities entities = new Entities();
        String baseNamedEntity = "invalid";
        boolean result = entities.isBaseNamedEntity(baseNamedEntity);
        boolean expected = false;
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testGetCharacterByName() {
        Entities entities = new Entities();
        String namedEntity = "lt";
        Character result = entities.getCharacterByName(namedEntity);
        Character expected = '<';
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testGetCharacterByNameWithInvalidName() {
        Entities entities = new Entities();
        String namedEntity = "invalid";
        Character result = entities.getCharacterByName(namedEntity);
        Assert.assertNull(result);
    }

    @Test
    public void testEscapeMethodWithDefaultSettings() {
        Entities entities = new Entities();
        String input = "<html>&amp;<lt;xml&gt;</html>";
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        String expected = "&amp;lt;xml&gt;";
        String result = entities.escape(input, outputSettings);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testEscapeMethodWithXHTMLSettings() {
        Entities entities = new Entities();
        String input = "<html>&amp;<lt;xml&gt;</html>";
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.escapeMode(Entities.EscapeMode.xhtml);
        String expected = "&lt;xml&gt;";
        String result = entities.escape(input, outputSettings);
        Assert.assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEscapeMethodWithInvalidOutputSettings() {
        Entities entities = new Entities();
        String input = "<html>&amp;<lt;xml&gt;</html>";
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.escapeMode("invalid");
        entities.escape(input, outputSettings);
    }

    @Test
    public void testCanEncodeMethod() {
        Entities entities = new Entities();
        char c = 'é';
        CoreCharset coreCharset = Entities.CoreCharset.utf;
        CharsetEncoder encoder = null; // using the default encoder
        boolean expected = true;
        boolean result = entities.canEncode(coreCharset, c, encoder);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testCanEncodeMethodWithInvalidCoreCharset() {
        Entities entities = new Entities();
        char c = 'é';
        CoreCharset coreCharset = Entities.CoreCharset.fallback;
        CharsetEncoder encoder = null;
        boolean expected = false;
        boolean result = entities.canEncode(coreCharset, c, encoder);
        Assert.assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCanEncodeMethodWithInvalidCharsetEncoder() {
        Entities entities = new Entities();
        char c = 'é';
        CoreCharset coreCharset = Entities.CoreCharset.utf;
        CharsetEncoder encoder = null;
        entities.canEncode(coreCharset, c, encoder);
    }

}