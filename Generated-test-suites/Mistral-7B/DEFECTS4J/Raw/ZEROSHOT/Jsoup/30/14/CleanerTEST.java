package org.jsoup.safety;

import static org.junit.Assert.*;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Test class for Cleaner.java
 */
public class CleanerTest {

    @Test
    public void testCleanerWithValidWhitelist() {
        Whitelist whitelist = Whitelist.relaxed();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Document.fromUrl("http://example.com/dirty.html");
        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertEquals("Expected clean document to have the same number of nodes as the dirty document.",
                dirtyDocument.numChildren(), cleanDocument.numChildren());

        Element dirtyBody = dirtyDocument.body();
        Element cleanBody = cleanDocument.body();

        List<Node> dirtyChildren = dirtyBody.childNodes();
        List<Node> cleanChildren = cleanBody.childNodes();

        for (int i = 0; i < dirtyChildren.size(); i++) {
            Node dirtyNode = dirtyChildren.get(i);
            Node cleanNode = cleanChildren.get(i);

            if (dirtyNode instanceof TextNode) {
                TextNode dirtyText = (TextTextNode) dirtyNode;
                TextNode cleanText = (TextNode) cleanNode;

                assertEquals("Expected text nodes to have the same text.", dirtyText.getWholeText(), cleanText.getWholeText());
            } else if (dirtyNode instanceof Element) {
                Element dirtyElement = (Element) dirtyNode;
                Element cleanElement = (Element) cleanNode;

                String dirtyTag = dirtyElement.tagName();
                String cleanTag = cleanElement.tagName();

                assertTrue("Expected clean tag to match dirty tag.", cleanTag.equals(dirtyTag));

                List<Attribute> dirtyAttributes = dirtyElement.attributes();
                List<Attribute> cleanAttributes = cleanElement.attributes();

                for (Attribute dirtyAttribute : dirtyAttributes) {
                    String dirtyName = dirtyAttribute.getKey();
                    String dirtyValue = dirtyAttribute.getValue();

                    Attribute cleanAttribute = cleanAttributes.get(dirtyName);
                    String cleanValue = cleanAttribute != null ? cleanAttribute.getValue() : null;

                    if (cleanAttribute == null) {
                        fail("Expected clean document to have attribute: " + dirtyName + "=" + dirtyValue);
                    } else {
                        assertEquals("Expected clean attribute value to match dirty attribute value.", dirtyValue, cleanValue);
                    }
                }
            }
        }
    }

    @Test
    public void testCleanerWithInvalidWhitelist() {
        Whitelist whitelist = new Whitelist.SimpleWhitelist("p", "a");
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Document.fromUrl("http://example.com/dirty.html");

        // Validate that the document is considered invalid
        assertFalse("Expected document to be invalid.", cleaner.isValid(dirtyDocument));

        Document cleanDocument = cleaner.clean(dirtyDocument);

        // Validate that the document has been cleaned
        assertTrue("Expected clean document to have fewer nodes than the dirty document.", cleanDocument.numChildren() < dirtyDocument.numChildren());

        Element dirtyBody = dirtyDocument.body();
        Element cleanBody = cleanDocument.body();

        List<Node> dirtyChildren = dirtyBody.childNodes();
        List<Node> cleanChildren = cleanBody.childNodes();

        // Validate that all non-allowed tags and attributes have been removed
        for (int i = 0; i < dirtyChildren.size(); i++) {
            Node dirtyNode = dirtyChildren.get(i);
            Node cleanNode = cleanChildren.get(i);

            if (dirtyNode instanceof TextNode) {
                TextNode dirtyText = (TextTextNode) dirtyNode;
                TextNode cleanText = (TextNode) cleanNode;

                assertEquals("Expected text nodes to have the same text.", dirtyText.getWholeText(), cleanText.getWholeText());
            } else if (dirtyNode instanceof Element) {
                Element dirtyElement = (Element) dirtyNode;
                Element cleanElement = (Element) cleanNode;

                String dirtyTag = dirtyElement.tagName();

                // Tags not in the whitelist should have been removed
                if (!Arrays.asList(whitelist.getTags()).contains(Tag.valueOf(dirtyTag))) {
                    assertTrue("Expected clean document to not have tag: " + dirtyTag, cleanElement == null);
                } else {
                    List<Attribute> dirtyAttributes = dirtyElement.attributes();

                    // Attributes not in the whitelist should have been removed
                    for (Attribute dirtyAttribute : dirtyAttributes) {
                        String dirtyName = dirtyAttribute.getKey();
                        String dirtyValue = dirtyAttribute.getValue();

                        Attribute cleanAttribute = cleanElement.attribute(dirtyName);

                        // Attributes not in the whitelist should not be present
                        assertTrue("Expected clean document to not have attribute: " + dirtyName + "=" + dirtyValue, cleanAttribute == null);
                    }

                    // Enforced attributes should be present
                    List<Attribute> enforcedAttributes = whitelist.getEnforcedAttributes(dirtyTag);
                    for (Attribute enforcedAttribute : enforcedAttributes) {
                        String enforcedName = enforcedAttribute.getKey();
                        String enforcedValue = enforcedAttribute.getValue();

                        Attribute cleanAttribute = cleanElement.attribute(enforcedName);

                        assertEquals("Expected clean document to have enforced attribute: " + enforcedName + "=" + enforcedValue, enforcedValue, cleanAttribute.getValue());
                    }
                }
            }
        }
    }
}