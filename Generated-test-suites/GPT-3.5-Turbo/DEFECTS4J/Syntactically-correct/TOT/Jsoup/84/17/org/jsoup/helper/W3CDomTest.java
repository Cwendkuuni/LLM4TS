import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class W3CDomTest {

    private W3CDom w3cDom;

    @Mock
    private org.jsoup.nodes.Document jsoupDoc;

    @Mock
    private org.jsoup.nodes.Element jsoupElement;

    @Mock
    private org.jsoup.nodes.TextNode jsoupTextNode;

    @Mock
    private org.jsoup.nodes.Comment jsoupComment;

    @Mock
    private org.jsoup.nodes.DataNode jsoupDataNode;

    @Mock
    private DocumentBuilderFactory docFactory;

    @Mock
    private DocumentBuilder docBuilder;

    @Mock
    private Document w3cDoc;

    @Before
    public void setup() throws ParserConfigurationException {
        MockitoAnnotations.initMocks(this);
        w3cDom = new W3CDom();
    }

    @Test
    public void testFromJsoup_withValidInput_shouldReturnW3CDocument() {
        // Arrange
        when(jsoupDoc.location()).thenReturn("http://example.com");
        when(jsoupDoc.child(0)).thenReturn(jsoupElement);
        when(docFactory.newInstance()).thenReturn(docFactory);
        when(docFactory.newDocumentBuilder()).thenReturn(docBuilder);
        when(docBuilder.newDocument()).thenReturn(w3cDoc);

        // Act
        Document result = w3cDom.fromJsoup(jsoupDoc);

        // Assert
        assertNotNull(result);
        assertEquals(w3cDoc, result);
        verify(docFactory).setNamespaceAware(true);
        verify(docFactory).newInstance();
        verify(docFactory).newDocumentBuilder();
        verify(docBuilder).newDocument();
        verify(jsoupDoc).location();
        verify(jsoupDoc).child(0);
    }

    @Test(expected = NullPointerException.class)
    public void testFromJsoup_withNullInput_shouldThrowNullPointerException() {
        // Act
        Document result = w3cDom.fromJsoup(null);

        // Assert
        assertNull(result); // Will not run, expecting NullPointerException
    }

    @Test
    public void testConvert_withValidInputs_shouldTransformJsoupToW3CDocument() {
        // Arrange
        ArgumentCaptor<W3CDom.W3CBuilder> w3cBuilderCaptor = ArgumentCaptor.forClass(W3CDom.W3CBuilder.class);
        when(jsoupDoc.location()).thenReturn("http://example.com");
        when(jsoupDoc.child(0)).thenReturn(jsoupElement);
        when(w3cBuilderCaptor.getValue().head(jsoupElement, 0)).thenReturn(null);
        when(w3cBuilderCaptor.getValue().tail(jsoupElement, 0)).thenReturn(null);

        // Act
        w3cDom.convert(jsoupDoc, w3cDoc);

        // Assert
        verify(jsoupDoc).location();
        verify(jsoupDoc).child(0);
        verifyNoMoreInteractions(jsoupDoc);
        verify(w3cBuilderCaptor.getValue(), times(2)).head(any(org.jsoup.nodes.Node.class), anyInt());
        verify(w3cBuilderCaptor.getValue(), times(2)).tail(any(org.jsoup.nodes.Node.class), anyInt());
    }

    @Test(expected = NullPointerException.class)
    public void testConvert_withNullJsoupDoc_shouldThrowNullPointerException() {
        // Act
        w3cDom.convert(null, w3cDoc);

        // Assert
        assertNull(w3cDoc); // Will not run, expecting NullPointerException
    }

    @Test(expected = NullPointerException.class)
    public void testConvert_withNullW3CDoc_shouldThrowNullPointerException() {
        // Act
        w3cDom.convert(jsoupDoc, null);

        // Assert
        assertNull(w3cDoc); // Will not run, expecting NullPointerException
    }

    @Test
    public void testAsString_withValidInput_shouldReturnStringRepresentationOfW3CDocument() {
        // Arrange
        ArgumentCaptor<DOMSource> domSourceCaptor = ArgumentCaptor.forClass(DOMSource.class);
        ArgumentCaptor<StreamResult> streamResultCaptor = ArgumentCaptor.forClass(StreamResult.class);
        when(docFactory.newInstance()).thenReturn(docFactory);
        when(docFactory.newDocumentBuilder()).thenReturn(docBuilder);
        when(docBuilder.newDocument()).thenReturn(w3cDoc);

        // Act
        String result = w3cDom.asString(w3cDoc);

        // Assert
        assertNotNull(result);
        verify(docFactory).newInstance();
        verify(docFactory).newDocumentBuilder();
        verify(docBuilder).newDocument();
        verify(domSourceCaptor.getValue()).setNode(w3cDoc);
        verify(streamResultCaptor.getValue()).setWriter(any(StringWriter.class));
    }

    @Test(expected = NullPointerException.class)
    public void testAsString_withNullInput_shouldThrowNullPointerException() {
        // Act
        String result = w3cDom.asString(null);

        // Assert
        assertNull(result); // Will not run, expecting NullPointerException
    }
}