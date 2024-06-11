package org.jsoup.nodes.test;

import org.jsoup.nodes.Entities;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Assert.assertTrue(Entities.isNamedEntity("lt"));
        Assert.assertFalse(Entities.isNamedEntity("gt")); // different from "lt", should be false
    }

    @Test
    public void testIsBaseNamedEntity() {
        Assert.assertTrue(Entities.isBaseNamedEntity("lt"));
        Assert.assertFalse(Entities.isBaseNamedEntity("amp")); // not in base set, should be false
    }

    @Test
    public void testGetCharacterByName() {
        Assert.assertEquals('<', Entities.getCharacterByName("lt"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetCharacterByNameNullName() {
        try {
            Entities.getCharacterByName(null);
        } catch (IllegalArgumentException e) {
            // expected
            Assert.assertEquals("name is mark null", e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetCharacterByNameInvalidName() {
        try {
            Entities.getCharacterByName("invalid");
        } catch (IllegalArgumentException e) {
            // expected
            Assert.assertEquals("Invalid named entity: invalid.", e.getMessage());
        }
    }

    @Test
    public void testEscapeMethodBasic() {
        String input = "This is a test & < > \" string.";
        String expectedOutput = "This is a test &amp; &lt; &gt; &quot; string.";
        String output = Entities.escape(input, null); // null OutputSettings
        Assert.assertEquals(expectedOutput, output);
    }

    @Test
    public void testEscapeMethodWithOutputSettings() {
        String input = "This is a test & < > \" string.";
        String expectedOutput = "This is a test &amp; &lt; &gt; &quot; string.";

        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.setEscapeMode(Entities.EscapeMode.base);

        String output = Entities.escape(input, outputSettings);
        Assert.assertEquals(expectedOutput, output);
    }

    @Test(expected = NullPointerException.class)
    public void testEscapeMethodNullString() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        try {
            Entities.escape(null, outputSettings);
        } catch (NullPointerException e) {
            // expected
            Assert.assertEquals("String should not be null", e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEscapeMethodInvalidOutputSettings() {
        String input = "This is a test & < > \" string.";
        try {
            Document.OutputSettings outputSettings = new Document.OutputSettings();
            outputSettings.setEscapeMode(null);
            Entities.escape(input, outputSettings);
        } catch (IllegalArgumentException e) {
            // expected
            Assert.assertEquals("Invalid escape mode", e.getMessage());
        }
    }

    // Add edge cases and exception handling tests here

}