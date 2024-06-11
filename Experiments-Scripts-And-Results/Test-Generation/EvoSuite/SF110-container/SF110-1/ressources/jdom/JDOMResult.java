package org.jdom2.transform;

import javax.xml.transform.sax.*;
import org.xml.sax.ext.*;
import org.jdom2.*;
import java.util.*;
import org.jdom2.input.sax.*;
import org.xml.sax.helpers.*;
import org.xml.sax.*;

public class JDOMResult extends SAXResult
{
    public static final String JDOM_FEATURE = "http://jdom.org/jdom2/transform/JDOMResult/feature";
    private List<Content> resultlist;
    private Document resultdoc;
    private boolean queried;
    private JDOMFactory factory;
    
    public JDOMResult() {
        this.resultlist = null;
        this.resultdoc = null;
        this.queried = false;
        this.factory = null;
        final DocumentBuilder builder = new DocumentBuilder();
        super.setHandler(builder);
        super.setLexicalHandler(builder);
    }
    
    public void setResult(final List<Content> result) {
        this.resultlist = result;
        this.queried = false;
    }
    
    public List<Content> getResult() {
        List<Content> nodes = Collections.emptyList();
        this.retrieveResult();
        if (this.resultlist != null) {
            nodes = this.resultlist;
        }
        else if (this.resultdoc != null && !this.queried) {
            final List<Content> content = this.resultdoc.getContent();
            nodes = new ArrayList<Content>(content.size());
            while (content.size() != 0) {
                final Content o = content.remove(0);
                nodes.add(o);
            }
            this.resultlist = nodes;
            this.resultdoc = null;
        }
        this.queried = true;
        return nodes;
    }
    
    public void setDocument(final Document document) {
        this.resultdoc = document;
        this.resultlist = null;
        this.queried = false;
    }
    
    public Document getDocument() {
        Document doc = null;
        this.retrieveResult();
        if (this.resultdoc != null) {
            doc = this.resultdoc;
        }
        else if (this.resultlist != null && !this.queried) {
            try {
                JDOMFactory f = this.getFactory();
                if (f == null) {
                    f = new DefaultJDOMFactory();
                }
                doc = f.document(null);
                doc.setContent(this.resultlist);
                this.resultdoc = doc;
                this.resultlist = null;
            }
            catch (RuntimeException ex1) {
                return null;
            }
        }
        this.queried = true;
        return doc;
    }
    
    public void setFactory(final JDOMFactory factory) {
        this.factory = factory;
    }
    
    public JDOMFactory getFactory() {
        return this.factory;
    }
    
    private void retrieveResult() {
        if (this.resultlist == null && this.resultdoc == null) {
            this.setResult(((DocumentBuilder)this.getHandler()).getResult());
        }
    }
    
    public void setHandler(final ContentHandler handler) {
    }
    
    public void setLexicalHandler(final LexicalHandler handler) {
    }
    
    private static class FragmentHandler extends SAXHandler
    {
        private Element dummyRoot;
        
        public FragmentHandler(final JDOMFactory factory) {
            super(factory);
            this.pushElement(this.dummyRoot = new Element("root", null, null));
        }
        
        public List<Content> getResult() {
            try {
                this.flushCharacters();
            }
            catch (SAXException ex) {}
            return this.getDetachedContent(this.dummyRoot);
        }
        
        private List<Content> getDetachedContent(final Element elt) {
            final List<Content> content = elt.getContent();
            final List<Content> nodes = new ArrayList<Content>(content.size());
            while (content.size() != 0) {
                final Content o = content.remove(0);
                nodes.add(o);
            }
            return nodes;
        }
    }
    
    private class DocumentBuilder extends XMLFilterImpl implements LexicalHandler
    {
        private FragmentHandler saxHandler;
        private boolean startDocumentReceived;
        
        public DocumentBuilder() {
            this.saxHandler = null;
            this.startDocumentReceived = false;
        }
        
        public List<Content> getResult() {
            List<Content> mresult = null;
            if (this.saxHandler != null) {
                mresult = this.saxHandler.getResult();
                this.saxHandler = null;
                this.startDocumentReceived = false;
            }
            return mresult;
        }
        
        private void ensureInitialization() throws SAXException {
            if (!this.startDocumentReceived) {
                this.startDocument();
            }
        }
        
        public void startDocument() throws SAXException {
            this.startDocumentReceived = true;
            JDOMResult.this.setResult(null);
            super.setContentHandler(this.saxHandler = new FragmentHandler(JDOMResult.this.getFactory()));
            super.startDocument();
        }
        
        public void startElement(final String nsURI, final String localName, final String qName, final Attributes atts) throws SAXException {
            this.ensureInitialization();
            super.startElement(nsURI, localName, qName, atts);
        }
        
        public void startPrefixMapping(final String prefix, final String uri) throws SAXException {
            this.ensureInitialization();
            super.startPrefixMapping(prefix, uri);
        }
        
        public void characters(final char[] ch, final int start, final int length) throws SAXException {
            this.ensureInitialization();
            super.characters(ch, start, length);
        }
        
        public void ignorableWhitespace(final char[] ch, final int start, final int length) throws SAXException {
            this.ensureInitialization();
            super.ignorableWhitespace(ch, start, length);
        }
        
        public void processingInstruction(final String target, final String data) throws SAXException {
            this.ensureInitialization();
            super.processingInstruction(target, data);
        }
        
        public void skippedEntity(final String name) throws SAXException {
            this.ensureInitialization();
            super.skippedEntity(name);
        }
        
        public void startDTD(final String name, final String publicId, final String systemId) throws SAXException {
            this.ensureInitialization();
            this.saxHandler.startDTD(name, publicId, systemId);
        }
        
        public void endDTD() throws SAXException {
            this.saxHandler.endDTD();
        }
        
        public void startEntity(final String name) throws SAXException {
            this.ensureInitialization();
            this.saxHandler.startEntity(name);
        }
        
        public void endEntity(final String name) throws SAXException {
            this.saxHandler.endEntity(name);
        }
        
        public void startCDATA() throws SAXException {
            this.ensureInitialization();
            this.saxHandler.startCDATA();
        }
        
        public void endCDATA() throws SAXException {
            this.saxHandler.endCDATA();
        }
        
        public void comment(final char[] ch, final int start, final int length) throws SAXException {
            this.ensureInitialization();
            this.saxHandler.comment(ch, start, length);
        }
    }
}
