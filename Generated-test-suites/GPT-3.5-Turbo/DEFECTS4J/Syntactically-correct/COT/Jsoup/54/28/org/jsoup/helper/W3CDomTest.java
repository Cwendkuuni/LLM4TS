package org.jsoup.helper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Comment;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerFactory;
import java.io.StringWriter;
import java.util.HashMap;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        org.jsoup.nodes.Document in = Jsoup.parse("<html><body><div>Test</div></body></html>");
        W3CDom w3cDom = new W3CDom();
        Document out = w3cDom.fromJsoup(in);
        Assert.assertNotNull(out);
    }

    @Test
    public void testConvert() {
        org.jsoup.nodes.Document in = Jsoup.parse("<html><body><div>Test</div></body></html>");
        Document out = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        W3CDom w3cDom = new W3CDom();
        w3cDom.convert(in, out);
        Assert.assertNotNull(out);
    }

    @Test
    public void testAsString() {
        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        Element rootElement = doc.createElement("root");
        doc.appendChild(rootElement);
        W3CDom w3cDom = new W3CDom();
        String result = w3cDom.asString(doc);
        Assert.assertNotNull(result);
    }

    // Additional test cases for edge cases and exceptions

    @Test(expected = IllegalArgumentException.class)
    public void testFromJsoupNullInput() {
        W3CDom w3cDom = new W3CDom();
        w3cDom.fromJsoup(null);
    }

    @Test(expected = IllegalStateException.class)
    public void testFromJsoupParserConfigurationException() {
        org.jsoup.nodes.Document in = Jsoup.parse("<html><body><div>Test</div></body></html>");
        W3CDom w3cDom = new W3CDom();
        // Force ParserConfigurationException by setting a non-namespace aware factory
        w3cDom.factory = DocumentBuilderFactory.newInstance();
        w3cDom.fromJsoup(in);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConvertNullInput() {
        Document out = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        W3CDom w3cDom = new W3CDom();
        w3cDom.convert(null, out);
    }

    @Test(expected = IllegalStateException.class)
    public void testConvertParserConfigurationException() {
        org.jsoup.nodes.Document in = Jsoup.parse("<html><body><div>Test</div></body></html>");
        Document out = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        W3CDom w3cDom = new W3CDom();
        // Force ParserConfigurationException by setting a non-namespace aware factory
        w3cDom.factory = DocumentBuilderFactory.newInstance();
        w3cDom.convert(in, out);
    }

    @Test(expected = IllegalStateException.class)
    public void testConvertIllegalStateException() {
        org.jsoup.nodes.Document in = Jsoup.parse("<html><body><div>Test</div></body></html>");
        Document out = DocumentBuilderMock.createDocumentBuilderWithException();
        W3CDom w3cDom = new W3CDom();
        w3cDom.convert(in, out);
    }

    @Test(expected = IllegalStateException.class)
    public void testConvertIllegalStateException2() {
        org.jsoup.nodes.Document in = Jsoup.parse("<html><body><div>Test</div></body></html>");
        Document out = DocumentBuilderMock.createDocumentBuilderWithIllegalStateException();
        W3CDom w3cDom = new W3CDom();
        w3cDom.convert(in, out);
    }

    @Test(expected = IllegalStateException.class)
    public void testConvertIllegalStateException3() {
        org.jsoup.nodes.Document in = Jsoup.parse("<html><body><div>Test</div></body></html>");
        Document out = DocumentBuilderMock.createDocumentBuilderWithTransformException();
        W3CDom w3cDom = new W3CDom();
        w3cDom.convert(in, out);
    }

    @Test(expected = IllegalStateException.class)
    public void testAsStringIllegalStateException() {
        Document doc = DocumentBuilderMock.createDocumentWithIllegalStateException();
        W3CDom w3cDom = new W3CDom();
        w3cDom.asString(doc);
    }

    @Test(expected = IllegalStateException.class)
    public void testAsStringIllegalStateException2() {
        Document doc = DocumentBuilderMock.createDocumentWithIllegalStateException2();
        W3CDom w3cDom = new W3CDom();
        w3cDom.asString(doc);
    }

    @Test(expected = IllegalStateException.class)
    public void testAsStringIllegalStateException3() {
        Document doc = DocumentBuilderMock.createDocumentWithTransformerException();
        W3CDom w3cDom = new W3CDom();
        w3cDom.asString(doc);
    }

    // Helper class for mocking document builder exceptions
    static class DocumentBuilderMock extends DocumentBuilderFactory {
        static DocumentBuilder createDocumentBuilderWithException() {
            return new DocumentBuilderMock().new DocumentBuilderWithException();
        }

        static DocumentBuilder createDocumentBuilderWithIllegalStateException() {
            return new DocumentBuilderMock().new DocumentBuilderWithIllegalStateException();
        }

        static DocumentBuilder createDocumentBuilderWithTransformException() {
            return new DocumentBuilderMock().new DocumentBuilderWithTransformException();
        }

        static Document createDocumentWithIllegalStateException() {
            return new DocumentBuilderMock().new DocumentWithIllegalStateException();
        }

        static Document createDocumentWithIllegalStateException2() {
            return new DocumentBuilderMock().new DocumentWithIllegalStateException2();
        }

        static Document createDocumentWithTransformerException() {
            return new DocumentBuilderMock().new DocumentWithTransformerException();
        }

        class DocumentBuilderWithException extends DocumentBuilder {
            @Override
            public Document newDocument() {
                throw new ParserConfigurationException("ParserConfigurationException");
            }
        }

        class DocumentBuilderWithIllegalStateException extends DocumentBuilder {
            @Override
            public Document newDocument() {
                throw new IllegalStateException("IllegalStateException");
            }
        }

        class DocumentBuilderWithTransformException extends DocumentBuilder {
            @Override
            public Document newDocument() {
                throw new TransformerFactoryConfigurationError(new TransformerException("TransformerException"));
            }
        }

        class DocumentWithIllegalStateException extends org.w3c.dom.Document {
            @Override
            public org.w3c.dom.DocumentType getDoctype() {
                throw new IllegalStateException("IllegalStateException");
            }

            // Other required methods are unimplemented
        }

        class DocumentWithIllegalStateException2 extends org.w3c.dom.Document {
            @Override
            public org.w3c.dom.Element getDocumentElement() {
                throw new IllegalStateException("IllegalStateException");
            }

            // Other required methods are unimplemented
        }

        class DocumentWithTransformerException extends org.w3c.dom.Document {
            @Override
            public org.w3c.dom.DocumentType getDoctype() {
                throw new TransformerException("TransformerException");
            }

            // Other required methods are unimplemented
        }
    }

}