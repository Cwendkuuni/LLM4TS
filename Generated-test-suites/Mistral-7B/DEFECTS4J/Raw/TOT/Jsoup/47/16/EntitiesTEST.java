import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Entities;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Assert.assertTrue("lt should be a named entity", Entities.isNamedEntity("lt"));
        Assert.assertTrue("amp should be a named entity", Entities.isNamedEntity("amp"));
        Assert.assertFalse("not a named entity", Entities.isNamedEntity("xyz"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        Assert.assertTrue("amp is a base named entity", Entities.isBaseNamedEntity("amp"));
        Assert.assertFalse("lt is not a base named entity", Entities.isBaseNamedEntity("lt"));
    }

    @Test
    public void testGetCharacterByName() {
        Character lt = Entities.getCharacterByName("lt");
        Character amp = Entities.getCharacterByName("amp");

        Assert.assertEquals("lt should be '<'", lt, '<');
        Assert.assertEquals("amp should be '&'", amp, '&');
    }

    @Test
    public void testEscape() {
        String input = "&amp;<lt>Hello &quot;World&quot;</lt> >Goodbye&gt;";
        String output = Entities.escape(input, Document.OutputSettings.xhtml());

        String expectedOutput = "&amp;amp;&#x003C;lt&gt;Hello &quot;World&quot;&#x003E; &gt;Goodbye&gt;";
        Assert.assertEquals("Escaped output should match expected", expectedOutput, output);
    }

    @Test
    public void testUnescape() {
        String escapedInput = "&amp;amp;&#x003C;lt&gt;Hello &quot;World&quot;&#x003E; &gt;Goodbye&gt;";
        String unescapedInput = Entities.unescape(escapedInput);

        String input = "&amp;<lt>Hello &quot;World&quot;</lt> >Goodbye&gt;";
        Assert.assertEquals("Unescaped output should match input", input, unescapedInput);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidEscapeMode() {
        Entities.escape("input", new Document.OutputSettings(null));
    }
}