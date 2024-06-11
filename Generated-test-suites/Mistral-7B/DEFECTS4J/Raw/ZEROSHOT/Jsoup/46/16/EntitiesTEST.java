package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@RunWith(JUnit4.class)
public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Entities entities = new Entities();
        Assert.assertTrue(entities.isNamedEntity("lt"));
        Assert.assertTrue(entities.isNamedEntity("amp"));
        Assert.assertTrue(entities.isNamedEntity("quot"));
        Assert.assertFalse(entities.isNamedEntity(""));
        Assert.assertFalse(entities.isNamedEntity("foo"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        Entities entities = new Entities();
        Assert.assertTrue(entities.isBaseNamedEntity("lt"));
        Assert.assertTrue(entities.isBaseNamedEntity("amp"));
        Assert.assertFalse(entities.isBaseNamedEntity("quot"));
        Assert.assertFalse(entities.isBaseNamedEntity(""));
        Assert.assertFalse(entities.isBaseNamedEntity("foo"));
    }

    @Test
    public void testGetCharacterByName() {
        Entities entities = new Entities();
        Assert.assertEquals('<', entities.getCharacterByName("lt"));
        Assert.assertEquals('&', entities.getCharacterByName("amp"));
        Assert.assertEquals('"', entities.getCharacterByName("quot"));
        Assert.assertEquals(0, entities.getCharacterByName(null));
        Assert.assertEquals(0, entities.getCharacterByName(""));
        Assert.assertEquals(0, entities.getCharacterByName("foo"));
    }

    @Test
    public void testEscape() {
        Entities entities = new Entities();
        Document.OutputSettings out = new Document.OutputSettings();
        StringBuilder accum = new StringBuilder();
        String input = "&lt;foo&gt;<bar>Quot: &quot; Quot: \"&amp; Quot: \" &quot; &amp; Quot: \" &amp;Quot: \"";

        entities.escape(accum, input, out, false, false, false);
        String output = accum.toString();
        Assert.assertEquals("&amp;lt;foo&amp;gt;&amp;lt;bar&amp;&gt;Quot: &amp;#x22; Quot: &#34; Quot: &amp;#x22; Quot: &#34; &amp; Quot: &#34; &amp;Quot: &#34;", output);

        accum.setLength(0);
        entities.escape(accum, input, out, true, false, false);
        output = accum.toString();
       Assert.assertEquals("&lt;foo&gt;&lt;bar&gt;Quot: &quot; Quot: &quot; Quot: &quot; &quot; &quot; &quot; &quot;", output);

        accum.setLength(0);
        out.escapeMode(Entities.EscapeMode.xhtml);
        entities.escape(accum, input, out, false, false, false);
        output = accum.toString();
        Assert.assertEquals("&lt;foo&gt;&lt;bar&gt;Quot: &quot;", output);
    }

    @Test
    public void testUnescape() {
        Entities entities = new Entities();
        String input = "&amp;lt;foo&amp;gt;&amp;lt;bar&amp;&gt;Quot: &amp;#x22; Quot: &#34; Quot: &amp;#x22; Quot: &#34; &quot; &amp;Quot: &#34;";
        String output = entities.unescape(input, false);
        Assert.assertEquals("&lt;foo&gt;&lt;bar&gt;Quot: \" Quot: \" Quot: \" Quot: \" Quot: \"", output);

        output = entities.unescape(input, true);
        Assert.assertEquals("&lt;foo&gt;&lt;bar&gt;Quot: &quot;", output);
    }

    @Test
    public void testCanEncode() {
        Entities entities = new Entities();
        CoreCharset charset = CoreCharset.utf;
        CharsetEncoder encoder = StandardCharsets.UTF_8.newEncoder();
        Assert.assertTrue(entities.canEncode(charset, 'a', encoder));
        Assert.assertTrue(entities.canEncode(charset, 'A', encoder));
        Assert.assertTrue(entities.canEncode(charset, '&', encoder));
        Assert.assertTrue(entities.canEncode(charset, '>', encoder));
        Assert.assertFalse(entities.canEncode(charset, '\u00FC', encoder));
    }
}