import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.safety.Whitelist;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class CleanerTest {

    @Test
    public void testClean() {
        Whitelist whitelist = Mockito.mock(Whitelist.class);
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Mockito.mock(Document.class);

        Document clean = cleaner.clean(dirtyDocument);

        assertNotNull(clean);
    }

    @Test
    public void testIsValid_ValidDocument() {
        Whitelist whitelist = Mockito.mock(Whitelist.class);
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Mockito.mock(Document.class);
        Element body = Mockito.mock(Element.class);
        Mockito.when(dirtyDocument.body()).thenReturn(body);
        List<Element> nodeList = new ArrayList<>();
        nodeList.add(Mockito.mock(Element.class));
        Mockito.when(body.childNodes()).thenReturn(nodeList);

        boolean isValid = cleaner.isValid(dirtyDocument);

        assertTrue(isValid);
    }

    @Test
    public void testIsValid_InvalidDocument() {
        Whitelist whitelist = Mockito.mock(Whitelist.class);
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = Mockito.mock(Document.class);
        Element body = Mockito.mock(Element.class);
        Mockito.when(dirtyDocument.body()).thenReturn(body);
        List<Element> nodeList = new ArrayList<>();
        Element invalidElement = Mockito.mock(Element.class);
        nodeList.add(invalidElement);
        Mockito.when(body.childNodes()).thenReturn(nodeList);
        Mockito.when(whitelist.isSafeTag(Mockito.anyString())).thenReturn(false);

        boolean isValid = cleaner.isValid(dirtyDocument);

        assertFalse(isValid);
    }

    @Test
    public void testCopySafeNodes() {
        Whitelist whitelist = Mockito.mock(Whitelist.class);
        Cleaner cleaner = new Cleaner(whitelist);

        Element source = Mockito.mock(Element.class);
        Element dest = Mockito.mock(Element.class);
        List<Element> children = new ArrayList<>();
        children.add(Mockito.mock(Element.class));
        Mockito.when(source.childNodes()).thenReturn(children);

        int numDiscarded = cleaner.copySafeNodes(source, dest);

        assertEquals(1, numDiscarded);
        Mockito.verify(dest, Mockito.times(1)).appendChild(Mockito.any(Element.class));
        Mockito.verify(cleaner, Mockito.times(1)).copySafeNodes(Mockito.any(Element.class), Mockito.any(Element.class));
    }

    @Test
    public void testCreateSafeElement_AllAttributesSafe() {
        Whitelist whitelist = Mockito.mock(Whitelist.class);
        Cleaner cleaner = new Cleaner(whitelist);

        Element sourceEl = Mockito.mock(Element.class);
        Mockito.when(sourceEl.tagName()).thenReturn("test");
        Mockito.when(whitelist.isSafeAttribute(Mockito.anyString(), Mockito.any(Element.class), Mockito.any(Attribute.class))).thenReturn(true);
        Attributes attrs = Mockito.mock(Attributes.class);
        Mockito.when(sourceEl.attributes()).thenReturn(attrs);
        Mockito.when(whitelist.getEnforcedAttributes(Mockito.anyString())).thenReturn(new Attributes());

        Cleaner.ElementMeta meta = cleaner.createSafeElement(sourceEl);

        assertNotNull(meta.el);
        assertEquals(0, meta.numAttribsDiscarded);
    }

    @Test
    public void testCreateSafeElement_SomeAttributesDiscarded() {
        Whitelist whitelist = Mockito.mock(Whitelist.class);
        Cleaner cleaner = new Cleaner(whitelist);

        Element sourceEl = Mockito.mock(Element.class);
        Mockito.when(sourceEl.tagName()).thenReturn("test");
        Mockito.when(whitelist.isSafeAttribute(Mockito.anyString(), Mockito.any(Element.class), Mockito.any(Attribute.class))).thenReturn(false);
        Attributes attrs = Mockito.mock(Attributes.class);
        Mockito.when(sourceEl.attributes()).thenReturn(attrs);
        Mockito.when(attrs.put(Mockito.any(Attribute.class))).thenReturn(Mockito.mock(Attribute.class));
        Attributes enforcedAttrs = new Attributes();
        enforcedAttrs.put("enforcedAttribute", "value");
        Mockito.when(whitelist.getEnforcedAttributes(Mockito.anyString())).thenReturn(enforcedAttrs);

        Cleaner.ElementMeta meta = cleaner.createSafeElement(sourceEl);

        assertNotNull(meta.el);
        assertEquals(1, meta.numAttribsDiscarded);
    }

}