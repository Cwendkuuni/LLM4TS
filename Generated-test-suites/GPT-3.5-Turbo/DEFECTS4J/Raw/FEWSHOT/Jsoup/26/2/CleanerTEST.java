import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CleanerTest {

    @Mock
    private Whitelist whitelistMock;

    @Mock
    private Document dirtyDocumentMock;

    @Test
    public void testClean() {
        Cleaner cleaner = new Cleaner(whitelistMock);

        when(dirtyDocumentMock.baseUri()).thenReturn("http://example.com");

        Document cleanDocument = cleaner.clean(dirtyDocumentMock);

        assertTrue(cleanDocument.html().isEmpty());
        verify(dirtyDocumentMock, times(1)).baseUri();
        verify(dirtyDocumentMock, times(1)).body();
    }

    @Test
    public void testIsValid() {
        Cleaner cleaner = new Cleaner(whitelistMock);

        boolean isValid = cleaner.isValid(dirtyDocumentMock);

        assertTrue(isValid);
        verify(dirtyDocumentMock, times(1)).baseUri();
        verify(dirtyDocumentMock, times(1)).body();
    }

    @Test
    public void testCopySafeNodes() {
        Cleaner cleaner = new Cleaner(whitelistMock);

        Element sourceElementMock = mock(Element.class);
        Element destElementMock = mock(Element.class);
        NodeList nodeListMock = mock(NodeList.class);
        Element childElementMock = mock(Element.class);
        TextNode textNodeMock = mock(TextNode.class);

        List<Node> sourceChildren = new ArrayList<>();
        sourceChildren.add(sourceElementMock);

        when(sourceElementMock.childNodes()).thenReturn(nodeListMock);
        when(nodeListMock.iterator()).thenReturn(sourceChildren.iterator());
        when(sourceElementMock instanceof Element).thenReturn(true);
        when(whitelistMock.isSafeTag(sourceElementMock.tagName())).thenReturn(true);
        when(cleaner.createSafeElement(sourceElementMock)).thenReturn(cleaner.new ElementMeta(destElementMock, 0));
        when(sourceElementMock.attributes()).thenReturn(mock(Attributes.class));
        when(sourceElementMock.attributes()).thenReturn(mock(Attributes.class));
        when(whitelistMock.getEnforcedAttributes(sourceElementMock.tagName())).thenReturn(mock(Attributes.class));

        int numDiscarded = cleaner.copySafeNodes(sourceElementMock, destElementMock);

        assertEquals(0, numDiscarded);
        verify(sourceElementMock, times(1)).childNodes();
        verify(nodeListMock, times(1)).iterator();
        verify(sourceElementMock, times(1)).tagName();
        verify(whitelistMock, times(1)).isSafeTag(sourceElementMock.tagName());
        verify(cleaner, times(1)).createSafeElement(sourceElementMock);
        verify(destElementMock, times(1)).appendChild(any(Element.class));
        verify(sourceElementMock, times(1)).attributes();
        verify(sourceElementMock, times(1)).attributes();
        verify(whitelistMock, times(1)).getEnforcedAttributes(sourceElementMock.tagName());
    }

    @Test
    public void testCreateSafeElement() {
        Cleaner cleaner = new Cleaner(whitelistMock);

        Element sourceElementMock = mock(Element.class);
        Attributes destAttributesMock = mock(Attributes.class);

        when(sourceElementMock.tagName()).thenReturn("div");
        when(sourceElementMock.baseUri()).thenReturn("http://example.com");
        when(sourceElementMock.attributes()).thenReturn(mock(Attributes.class));
        when(whitelistMock.isSafeAttribute(sourceElementMock.tagName(), sourceElementMock, sourceAttr)).thenReturn(true);
        when(whitelistMock.getEnforcedAttributes(sourceElementMock.tagName())).thenReturn(new Attributes());

        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(sourceElementMock);

        assertEquals(destElementMock, elementMeta.el);
        assertEquals(0, elementMeta.numAttribsDiscarded);
        verify(sourceElementMock, times(1)).tagName();
        verify(sourceElementMock, times(1)).baseUri();
        verify(sourceElementMock, times(1)).attributes();
        verify(whitelistMock, times(1)).isSafeAttribute(sourceElementMock.tagName(), sourceElementMock, sourceAttr);
        verify(whitelistMock, times(1)).getEnforcedAttributes(sourceElementMock.tagName());
    }
}