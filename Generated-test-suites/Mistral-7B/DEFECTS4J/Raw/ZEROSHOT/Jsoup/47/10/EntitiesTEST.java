package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Map<Character, String> full = Entities.fullByVal;
        for (Map.Entry<Character, String> entry : full.entrySet()) {
            Character key = entry.getKey();
            String value = entry.getValue().toString();
            boolean expectedResult = true;
            boolean result = Entities.isNamedEntity(value);
            Assert.assertEquals("Unexpected result for named entity: " + value, expectedResult, result);
        }

        Map<Character, String> base = Entities.baseByVal;
        for (Map.Entry<Character, String> entry : base.entrySet()) {
            Character key = entry.getKey();
            String value = entry.getValue().toString();
            boolean expectedResult = true;
            boolean result = Entities.isBaseNamedEntity(value);
            Assert.assertEquals("Unexpected result for base named entity: " + value, expectedResult, result);
        }

        String unknownEntity = "UnknownEntity";
        boolean result = Entities.isNamedEntity(unknownEntity);
        Assert.assertFalse("Unexpected result for unknown entity: " + unknownEntity, result);
    }

    @Test
    public void testGetCharacterByName() {
        Map<Character, String> full = Entities.fullByVal;
        for (Map.Entry<Character, String> entry : full.entrySet()) {
            Character key = entry.getKey();
            String value = entry.getValue().toString();
            Character expectedResult = key;
            Character result = Entities.getCharacterByName(value);
            Assert.assertEquals("Unexpected result for named entity: " + value, expectedResult, result);
        }
    }

    @Test
    public void testEscape() {
        StringBuilder expected = new StringBuilder();
        StringBuilder input = new StringBuilder("&lt; &gt; &#xA0; &apos; &quot; &amp; < > \" & &");
        Document.OutputSettings settings = new Document.OutputSettings();
        settings.setEscapeMode(Entities.EscapeMode.base);

        Entities.escape(expected, input.toString(), settings, false, false, false);
        StringBuilder actual = new StringBuilder();
        Entities.escape(actual, input.toString(), settings, false, false, false);

        Assert.assertEquals("Unexpected result from escape method", expected.toString(), actual.toString());

        expected.setLength(0);
        input.setLength(0);
        input.append("&lt; &gt; &#xA0; &apos; &quot; &amp; < > \" & & &lt;xml version='1.0' encoding='UTF-8'?&gt;");
        settings.setEscapeMode(Entities.EscapeMode.xhtml);

        Entities.escape(expected, input.toString(), settings, false, false, false);
        actual.setLength(0);
        Entities.escape(actual, input.toString(), settings, false, false, false);

        Assert.assertEquals("Unexpected result from escape method with xhtml mode", expected.toString(), actual.toString());
    }

    @Test
    public void testUnescape() {
        String expected = "&lt; &gt; &apos; &quot; &amp; < > \" & &";
        String input = Entities.unescape(Entities.escape(new StringBuilder(expected).toString(), new Document.OutputSettings(Entities.EscapeMode.base)), false);
        Assert.assertEquals("Unexpected result from unescape method", expected, input);

        expected = "&lt;xml version='1.0' encoding='UTF-8'?&gt; &lt;!DOCTYPE html PUBLIC '-//W3C//DTD XHTML 1.0 Transitional//EN' 'DTD/xhtml1-transitional.dtd'&gt; &lt;html xmlns='http://www.w3.org/1999/xhtml' xml:lang='en'&gt; &lt;head&gt; &lt;meta http-equiv='Content-Type' content='text/html; charset=UTF-8' /&gt; &lt;title&gt;Test&lt;/title&gt; &lt;body&gt; &lt;p&gt;&amp;lt;?xml version='1.0' encoding='UTF-8'?&gt;&lt;/p&gt; &lt;/body&gt; &lt;/html&gt;";
        String inputWithXmlDeclaration = Entities.unescape(Entities.escape(new StringBuilder(expected).toString(), new Document.OutputSettings(Entities.EscapeMode.xhtml)), false);
        Assert.assertEquals("Unexpected result from unescape method with xhtml mode and XML declaration", expected, inputWithXmlDeclaration);
    }

    @Test
    public void testCanEncode() {
        CoreCharset ascii = CoreCharset.ascii;
        CoreCharset utf = CoreCharset.utf;
        CharsetEncoder fallback = Character.encodingForName("UTF-8");

        for (char c = 0; c < 0x100; c++) {
            boolean canEncodeAscii = Entities.canEncode(ascii, c, fallback);
            boolean expectedResultAscii = (c < 0x80);
            Assert.assertEquals("Unexpected result for canEncode with ascii: " + (int) c, expectedResultAscii, canEncodeAscii);

            boolean canEncodeUtf = Entities.canEncode(utf, c, fallback);
            boolean expectedResultUtf = true;
            Assert.assertEquals("Unexpected result for canEncode with utf: " + (int) c, expectedResultUtf, canEncodeUtf);
        }
    }
}