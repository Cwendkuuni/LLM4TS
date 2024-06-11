import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.ArrayList;
import java.util.List;

public class DocumentTest {

    private Document document;

    @Test
    public void testDocument() {
        document = new Document("http://example.com");
        assert(document.baseUri().equals("http://example.com"));
    }

    @Test
    public void testCreateShell() {
        document = Document.createShell("http://example.com");
        assert(document.baseUri().equals("http://example.com"));
        assert(document.getElementsByTag("html").size() == 1);
        assert(document.getElementsByTag("head").size() == 1);
        assert(document.getElementsByTag("body").size() == 1);
    }

    @Test
    public void testHead() {
        document = Document.createShell("http://example.com");
        Element headElement = document.head();
        assert(headElement != null);
        assert(headElement.tagName().equals("head"));
    }

    @Test
    public void testBody() {
        document = Document.createShell("http://example.com");
        Element bodyElement = document.body();
        assert(bodyElement != null);
        assert(bodyElement.tagName().equals("body"));
    }

    @Test
    public void testTitle() {
        document = Document.createShell("http://example.com");
        document.title("Test Title");
        assert(document.title().equals("Test Title"));
    }

    @Test
    public void testcreateElement() {
        document = Document.createShell("http://example.com");
        Element element = document.createElement("div");
        assert(element != null);
        assert(element.tagName().equals("div"));
        assert(element.baseUri().equals("http://example.com"));
    }

    @Test
    public void testnormalise() {
        document = Document.createShell("http://example.com");
        document.normalise();
        assert(document.getElementsByTag("html").size() == 1);
        assert(document.getElementsByTag("head").size() == 1);
        assert(document.getElementsByTag("body").size() == 1);
    }

    @Test
    public void testouterHtml() {
        document = Document.createShell("http://example.com");
        String outerHtml = document.outerHtml();
        assert(outerHtml.equals("<html>\n <head></head>\n <body></body>\n</html>"));
    }

    @Test
    public void testtext() {
        document = Document.createShell("http://example.com");
        document.text("This is a test");
        assert(document.body().text().equals("This is a test"));
    }

    @Test
    public void testnodeName() {
        document = Document.createShell("http://example.com");
        String nodeName = document.nodeName();
        assert(nodeName.equals("#document"));
    }

    @Test
    public void testclone() {
        document = Document.createShell("http://example.com");
        Document clone = document.clone();
        assert(clone != null);
        assert(clone != document);
        assert(clone.baseUri().equals(document.baseUri()));
        assert(clone.outputSettings().equals(document.outputSettings()));
    }

    @Test
    public void testoutputSettings() {
        document = Document.createShell("http://example.com");
        Document.OutputSettings outputSettings = document.outputSettings();
        assert(outputSettings != null);
    }

    @Test
    public void testquirksMode() {
        document = Document.createShell("http://example.com");
        Document.QuirksMode quirksMode = document.quirksMode();
        assert(quirksMode.equals(Document.QuirksMode.noQuirks));
    }

    @Test
    public void testOutputSettings_escapeMode() {
        document = Document.createShell("http://example.com");
        Document.OutputSettings outputSettings = document.outputSettings();
        outputSettings.escapeMode(Entities.EscapeMode.extended);
        assert(outputSettings.escapeMode().equals(Entities.EscapeMode.extended));
    }

    @Test
    public void testOutputSettings_charset() {
        document = Document.createShell("http://example.com");
        Document.OutputSettings outputSettings = document.outputSettings();
        outputSettings.charset(Charset.forName("UTF-16"));
        assert(outputSettings.charset().equals(Charset.forName("UTF-16")));
    }

    @Test
    public void testOutputSettings_prettyPrint() {
        document = Document.createShell("http://example.com");
        Document.OutputSettings outputSettings = document.outputSettings();
        outputSettings.prettyPrint(false);
        assert(!outputSettings.prettyPrint());
    }

    @Test
    public void testOutputSettings_indentAmount() {
        document = Document.createShell("http://example.com");
        Document.OutputSettings outputSettings = document.outputSettings();
        outputSettings.indentAmount(2);
        assert(outputSettings.indentAmount() == 2);
    }
}