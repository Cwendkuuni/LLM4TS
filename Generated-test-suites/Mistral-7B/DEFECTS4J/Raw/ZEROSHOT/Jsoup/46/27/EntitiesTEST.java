package org.jsoup.nodes;

import org.junit.Test;
import org.junit.Assert.*;

import java.util.Map;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Entities entities = new Entities();
        String[] namedEntities = {"lt", "gt", "amp", "quot", "nbsp", "iexcl", "cent", "pound", "curren", "yen", "euro", "copy", "agrave", "acute", "acirc", "atilde", "uml", "aring", "aelig", "ccedil", "egrave", "laquo", "not", "shy", "reg", "macr", "deg", "plusmn", "sup2", "sup3", "acutegrave", "micro", "para", "middot", "cedil", "sup1", "ordf", "ordm", "raquo", "frac14", "frac12", "frac34", "iquest", "Agrave", "Aacute", "Acirc", "Atilde", "Auml", "Aring", "Aelig", "Ccedil", "Egrave", "Laquo", "Not", "Shy", "Reg", "Macr", "Deg", "Plusmn", "Sup2", "Sup3", "Acutegrave", "Micro", "Parag", "Middot", "Cedil", "Sup1", "OrdF", "OrdM", "Raquo", "frac14", "frac12", "frac34", "Iquest"};

        for (String namedEntity : namedEntities) {
            assertTrue(entities.isNamedEntity(namedEntity));
        }
    }

    @Test
    public void testIsBaseNamedEntity() {
        Entities entities = new Entities();
        String[] baseNamedEntities = {"lt", "gt", "amp", "quot"};

        for (String namedEntity : baseNamedEntities) {
            assertTrue(entities.isBaseNamedEntity(namedEntity));
        }

        String[] nonBaseNamedEntities = {"nbsp", "iexcl", "cent", "pound", "curren", "yen", "euro", "copy", "agrave", "acute", "acirc", "atilde", "uml", "aring", "aelig", "ccedil", "egrave", "laquo", "not", "shy", "reg", "macr", "deg", "plusmn", "sup2", "sup3", "acutegrave", "micro", "para", "middot", "cedil", "sup1", "ordf", "ordm", "raquo", "frac14", "frac12", "frac34", "iquest"};

        for (String namedEntity : nonBaseNamedEntities) {
            assertFalse(entities.isBaseNamedEntity(namedEntity));
        }
    }

    @Test
    public void testGetCharacterByName() {
        Entities entities = new Entities();
        String[] namedEntities = {"lt", "gt", "amp", "quot", "nbsp", "iexcl", "cent", "pound", "curren", "yen", "euro", "copy", "agrave", "acute", "acirc", "atilde", "uml", "aring", "aelig", "ccedil", "egrave", "laquo", "not", "shy", "reg", "macr", "deg", "plusmn", "sup2", "sup3", "acutegrave", "micro", "para", "middot", "cedil", "sup1", "ordf", "ordm", "raquo", "frac14", "frac12", "frac34", "iquest", "Agrave", "Aacute", "Acirc", "Atilde", "Auml", "Aring", "Aelig", "Ccedil", "Egrave", "Laquo", "Not", "Shy", "Reg", "Macr", "Deg", "Plusmn", "Sup2", "Sup3", "Acutegrave", "Micro", "Parag", "Middot", "Cedil", "Sup1", "OrdF", "OrdM", "Raquo", "frac14", "frac12", "frac34", "Iquest"};
        Character expectedCharacter;

        for (String namedEntity : namedEntities) {
            Character character = entities.getCharacterByName(namedEntity);
            switch (namedEntity) {
                case "lt":
                    expectedCharacter = '<';
                    break;
                case "gt":
                    expectedCharacter = '>';
                    break;
                case "amp":
                    expectedCharacter = '&';
                    break;
                case "quot":
                    expectedCharacter = '"';
                    break;
                default:
                    expectedCharacter = Character.valueOf((int) namedEntity.charAt(0));
            }
            assertEquals(expectedCharacter, character);
        }
    }

    @Test
    public void testEscape() {
        Entities entities = new Entities();
        String input = "Hello & World! <test> <> & 'single' & 'double' & &#123; & & & & & & & &";
        StringBuilder expectedOutput = new StringBuilder();
        expectedOutput.append("Hello &amp; World! &lt;test&gt; &lt;&gt; &amp; 'single' &amp; &quot; &amp; &amp; &amp; &amp; &amp; &amp;");
        StringBuilder output = new StringBuilder();
        entities.escape(output, input, Document.OutputSettings.base(), false, false, false);
        assertEquals(expectedOutput.toString(), output.toString());
    }

    @Test
    public void testEscapeWithXHTML() {
        Entities entities = new Entities();
        String input = "Hello & World! <test> <> & 'single' & 'double' & &#123; & & & & & & & &";
        StringBuilder expectedOutput = new StringBuilder();
        expectedOutput.append("Hello &amp;amp; World! &lt;test&gt; &lt;!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\"> <!xml version=\"1.0\" encoding=\"UTF-8\"?> <test>&lt;</test> &amp; 'single' &quot;double&quot; &amp; &amp; &amp; &amp; &amp; &amp;");
        StringBuilder output = new StringBuilder();
        entities.escape(output, input, Document.OutputSettings.xhtml(), false, false, false);
        assertEquals(expectedOutput.toString(), output.toString());
    }

    @Test
    public void testUnescape() {
        Entities entities = new Entities();
        String input = "Hello &amp;amp; World! &lt;test&gt; &lt;!DOCTYPE html PUBLIC &#x26;-//W3C//DTD XHTML 1.0 Strict//EN&#x26; &quot;http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd&#x26;&gt; <!xml version=\"1.0\" encoding=\"UTF-8\"?> <test>&lt;test&gt;</test> &amp; 'single' &quot;double&quot; &amp; &amp; &amp; &amp; &amp; &amp;";
        String expectedOutput = "Hello & World! <test> <test></test> & 'single' \"double\" & & & & & & & &";
        String output = entities.unescape(input, false);
        assertEquals(expectedOutput, output);

        String strictOutput = entities.unescape(input, true);
        assertEquals(expectedOutput, strictOutput);
    }
}