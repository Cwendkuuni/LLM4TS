package org.jsoup.parser;

import org.apache.commons.cli.Util;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class ParserTest {

    @InjectMocks
    private Parser parser;

    @Mock
    private String html;

    @Mock
    private String baseUri;

    @Mock
    private boolean isBodyFragment;

    @Mock
    private LinkedList<Element> stack;

    @Mock
    private TokenQueue tq;

    @Mock
    private Document doc;

    @Mock
    private String SQ = "'";

    @Mock
    private String DQ = "\"";

    @Mock
    private Tag htmlTag;

    @Mock
    private Tag headTag;

    @Mock
    private Tag bodyTag;

    @Mock
    private Tag titleTag;

    @Mock
    private Tag textareaTag;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        parser = new Parser(html, baseUri, isBodyFragment);
    }

    @Test
    public void testParse() {
        when(tq.isEmpty()).thenReturn(true);
        when(tq.matches("<!--")).thenReturn(true);
        when(tq.matches("<![CDATA[")).thenReturn(false);
        when(tq.matches("<?")).thenReturn(false);
        when(tq.matches("<!")).thenReturn(false);
        when(tq.matches("</")).thenReturn(false);
        when(tq.matches("<")).thenReturn(false);
        parser.parse();
        verify(tq, times(1)).isEmpty();
        verify(tq, times(1)).matches("<!--");
        verify(tq, never()).matches("<![CDATA[");
        verify(tq, never()).matches("<?");
        verify(tq, never()).matches("<!");
        verify(tq, never()).matches("</");
        verify(tq, never()).matches("<");
    }

    @Test
    public void testParseComment() {
        parser.parseComment();
    }

    @Test
    public void testParseXmlDecl() {
        parser.parseXmlDecl();
    }

    @Test
    public void testParseEndTag() {
        parser.parseEndTag();
    }

    @Test
    public void testParseStartTag() {
        parser.parseStartTag();
    }

    @Test
    public void testParseAttribute() {
        parser.parseAttribute();
    }

    @Test
    public void testParseTextNode() {
        parser.parseTextNode();
    }

    @Test
    public void testParseCdata() {
        parser.parseCdata();
    }

    @Test
    public void testAddChildToParent() {
        Element child = mock(Element.class);
        boolean isEmptyElement = true;
        parser.addChildToParent(child, isEmptyElement);
        verify(stack, times(1)).addLast(any(Element.class));
    }

    @Test
    public void testStackHasValidParent() {
        Tag childTag = mock(Tag.class);
        when(stack.size()).thenReturn(1);
        when(stack.get(0)).thenReturn(mock(Element.class));
        when(last().tag()).thenReturn(mock(Tag.class));
        assertFalse(parser.stackHasValidParent(childTag));
    }

    @Test
    public void testPopStackToSuitableContainer() {
        Tag tag = mock(Tag.class);
        parser.popStackToSuitableContainer(tag);
        verify(stack, times(1)).removeLast();
    }

    @Test
    public void testPopStackToClose() {
        Tag tag = mock(Tag.class);
        parser.popStackToClose(tag);
        verify(stack, times(1)).removeLast();
    }

    @Test
    public void testLast() {
        Element element = mock(Element.class);
        when(stack.getLast()).thenReturn(element);
        assertEquals(element, parser.last());
    }
}