package org.jdom2.output;

import org.xml.sax.ext.*;
import java.util.*;
import org.jdom2.*;
import org.xml.sax.*;
import org.jdom2.output.support.*;

public class SAXOutputter
{
    private static final SAXOutputProcessor DEFAULT_PROCESSOR;
    private ContentHandler contentHandler;
    private ErrorHandler errorHandler;
    private DTDHandler dtdHandler;
    private EntityResolver entityResolver;
    private LexicalHandler lexicalHandler;
    private DeclHandler declHandler;
    private boolean declareNamespaces;
    private boolean reportDtdEvents;
    private SAXOutputProcessor processor;
    private Format format;
    
    public SAXOutputter() {
        this.declareNamespaces = false;
        this.reportDtdEvents = true;
        this.processor = SAXOutputter.DEFAULT_PROCESSOR;
        this.format = Format.getRawFormat();
    }
    
    public SAXOutputter(final ContentHandler contentHandler) {
        this(contentHandler, null, null, null, null);
    }
    
    public SAXOutputter(final ContentHandler contentHandler, final ErrorHandler errorHandler, final DTDHandler dtdHandler, final EntityResolver entityResolver) {
        this(contentHandler, errorHandler, dtdHandler, entityResolver, null);
    }
    
    public SAXOutputter(final ContentHandler contentHandler, final ErrorHandler errorHandler, final DTDHandler dtdHandler, final EntityResolver entityResolver, final LexicalHandler lexicalHandler) {
        this.declareNamespaces = false;
        this.reportDtdEvents = true;
        this.processor = SAXOutputter.DEFAULT_PROCESSOR;
        this.format = Format.getRawFormat();
        this.contentHandler = contentHandler;
        this.errorHandler = errorHandler;
        this.dtdHandler = dtdHandler;
        this.entityResolver = entityResolver;
        this.lexicalHandler = lexicalHandler;
    }
    
    public SAXOutputter(final SAXOutputProcessor processor, final Format format, final ContentHandler contentHandler, final ErrorHandler errorHandler, final DTDHandler dtdHandler, final EntityResolver entityResolver, final LexicalHandler lexicalHandler) {
        this.declareNamespaces = false;
        this.reportDtdEvents = true;
        this.processor = SAXOutputter.DEFAULT_PROCESSOR;
        this.format = Format.getRawFormat();
        this.processor = ((processor == null) ? SAXOutputter.DEFAULT_PROCESSOR : processor);
        this.format = ((format == null) ? Format.getRawFormat() : format);
        this.contentHandler = contentHandler;
        this.errorHandler = errorHandler;
        this.dtdHandler = dtdHandler;
        this.entityResolver = entityResolver;
        this.lexicalHandler = lexicalHandler;
    }
    
    public void setContentHandler(final ContentHandler contentHandler) {
        this.contentHandler = contentHandler;
    }
    
    public ContentHandler getContentHandler() {
        return this.contentHandler;
    }
    
    public void setErrorHandler(final ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }
    
    public ErrorHandler getErrorHandler() {
        return this.errorHandler;
    }
    
    public void setDTDHandler(final DTDHandler dtdHandler) {
        this.dtdHandler = dtdHandler;
    }
    
    public DTDHandler getDTDHandler() {
        return this.dtdHandler;
    }
    
    public void setEntityResolver(final EntityResolver entityResolver) {
        this.entityResolver = entityResolver;
    }
    
    public EntityResolver getEntityResolver() {
        return this.entityResolver;
    }
    
    public void setLexicalHandler(final LexicalHandler lexicalHandler) {
        this.lexicalHandler = lexicalHandler;
    }
    
    public LexicalHandler getLexicalHandler() {
        return this.lexicalHandler;
    }
    
    public void setDeclHandler(final DeclHandler declHandler) {
        this.declHandler = declHandler;
    }
    
    public DeclHandler getDeclHandler() {
        return this.declHandler;
    }
    
    public boolean getReportNamespaceDeclarations() {
        return this.declareNamespaces;
    }
    
    public void setReportNamespaceDeclarations(final boolean declareNamespaces) {
        this.declareNamespaces = declareNamespaces;
    }
    
    public boolean getReportDTDEvents() {
        return this.reportDtdEvents;
    }
    
    public void setReportDTDEvents(final boolean reportDtdEvents) {
        this.reportDtdEvents = reportDtdEvents;
    }
    
    public void setFeature(final String name, final boolean value) throws SAXNotRecognizedException, SAXNotSupportedException {
        if ("http://xml.org/sax/features/namespace-prefixes".equals(name)) {
            this.setReportNamespaceDeclarations(value);
        }
        else if ("http://xml.org/sax/features/namespaces".equals(name)) {
            if (!value) {
                throw new SAXNotSupportedException(name);
            }
        }
        else {
            if (!"http://xml.org/sax/features/validation".equals(name)) {
                throw new SAXNotRecognizedException(name);
            }
            this.setReportDTDEvents(value);
        }
    }
    
    public boolean getFeature(final String name) throws SAXNotRecognizedException, SAXNotSupportedException {
        if ("http://xml.org/sax/features/namespace-prefixes".equals(name)) {
            return this.declareNamespaces;
        }
        if ("http://xml.org/sax/features/namespaces".equals(name)) {
            return true;
        }
        if ("http://xml.org/sax/features/validation".equals(name)) {
            return this.reportDtdEvents;
        }
        throw new SAXNotRecognizedException(name);
    }
    
    public void setProperty(final String name, final Object value) throws SAXNotRecognizedException, SAXNotSupportedException {
        if ("http://xml.org/sax/properties/lexical-handler".equals(name) || "http://xml.org/sax/handlers/LexicalHandler".equals(name)) {
            this.setLexicalHandler((LexicalHandler)value);
        }
        else {
            if (!"http://xml.org/sax/properties/declaration-handler".equals(name) && !"http://xml.org/sax/handlers/DeclHandler".equals(name)) {
                throw new SAXNotRecognizedException(name);
            }
            this.setDeclHandler((DeclHandler)value);
        }
    }
    
    public Object getProperty(final String name) throws SAXNotRecognizedException, SAXNotSupportedException {
        if ("http://xml.org/sax/properties/lexical-handler".equals(name) || "http://xml.org/sax/handlers/LexicalHandler".equals(name)) {
            return this.getLexicalHandler();
        }
        if ("http://xml.org/sax/properties/declaration-handler".equals(name) || "http://xml.org/sax/handlers/DeclHandler".equals(name)) {
            return this.getDeclHandler();
        }
        throw new SAXNotRecognizedException(name);
    }
    
    public SAXOutputProcessor getSAXOutputProcessor() {
        return this.processor;
    }
    
    public void setSAXOutputProcessor(final SAXOutputProcessor processor) {
        this.processor = ((processor == null) ? SAXOutputter.DEFAULT_PROCESSOR : processor);
    }
    
    public Format getFormat() {
        return this.format;
    }
    
    public void setFormat(final Format format) {
        this.format = ((format == null) ? Format.getRawFormat() : format);
    }
    
    private final SAXTarget buildTarget(final Document doc) {
        String publicID = null;
        String systemID = null;
        if (doc != null) {
            final DocType dt = doc.getDocType();
            if (dt != null) {
                publicID = dt.getPublicID();
                systemID = dt.getSystemID();
            }
        }
        return new SAXTarget(this.contentHandler, this.errorHandler, this.dtdHandler, this.entityResolver, this.lexicalHandler, this.declHandler, this.declareNamespaces, this.reportDtdEvents, publicID, systemID);
    }
    
    public void output(final Document document) throws JDOMException {
        this.processor.process(this.buildTarget(document), this.format, document);
    }
    
    public void output(final List<? extends Content> nodes) throws JDOMException {
        this.processor.processAsDocument(this.buildTarget(null), this.format, nodes);
    }
    
    public void output(final Element node) throws JDOMException {
        this.processor.processAsDocument(this.buildTarget(null), this.format, node);
    }
    
    public void outputFragment(final List<? extends Content> nodes) throws JDOMException {
        if (nodes == null) {
            return;
        }
        this.processor.process(this.buildTarget(null), this.format, nodes);
    }
    
    public void outputFragment(final Content node) throws JDOMException {
        if (node == null) {
            return;
        }
        final SAXTarget out = this.buildTarget(null);
        switch (node.getCType()) {
            case CDATA: {
                this.processor.process(out, this.format, (CDATA)node);
                break;
            }
            case Comment: {
                this.processor.process(out, this.format, (Comment)node);
                break;
            }
            case Element: {
                this.processor.process(out, this.format, (Element)node);
                break;
            }
            case EntityRef: {
                this.processor.process(out, this.format, (EntityRef)node);
                break;
            }
            case ProcessingInstruction: {
                this.processor.process(out, this.format, (ProcessingInstruction)node);
                break;
            }
            case Text: {
                this.processor.process(out, this.format, (Text)node);
                break;
            }
            default: {
                this.handleError(new JDOMException("Invalid element content: " + node));
                break;
            }
        }
    }
    
    private void handleError(final JDOMException exception) throws JDOMException {
        if (this.errorHandler != null) {
            try {
                this.errorHandler.error(new SAXParseException(exception.getMessage(), null, exception));
                return;
            }
            catch (SAXException se) {
                if (se.getException() instanceof JDOMException) {
                    throw (JDOMException)se.getException();
                }
                throw new JDOMException(se.getMessage(), se);
            }
            throw exception;
        }
        throw exception;
    }
    
    @Deprecated
    public JDOMLocator getLocator() {
        return null;
    }
    
    static {
        DEFAULT_PROCESSOR = new DefaultSAXOutputProcessor();
    }
    
    private static final class DefaultSAXOutputProcessor extends AbstractSAXOutputProcessor
    {
    }
}
