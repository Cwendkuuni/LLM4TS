
package org.jdom2.transform;

import static org.junit.Assert.*;
import org.junit.Test;
import org.jdom2.*;
import java.util.*;

public class JDOMResultTest {
    @Test
    public void testSetResult() {
        JDOMResult result = new JDOMResult();
        List<Content> content = new ArrayList<>();
        content.add(new Element("element"));
        result.setResult(content);
        assertEquals(content, result.getResult());
    }

    @Test
    public void testSetDocument() {
        JDOMResult result = new JDOMResult();
        Document doc = new Document();
        result.setDocument(doc);
        assertEquals(doc, result.getDocument());
    }

    @Test
    public void testSetFactory() {
        JDOMResult result = new JDOMResult();
        JDOMFactory factory = new DefaultJDOMFactory();
        result.setFactory(factory);
        assertEquals(factory, result.getFactory());
    }
}
