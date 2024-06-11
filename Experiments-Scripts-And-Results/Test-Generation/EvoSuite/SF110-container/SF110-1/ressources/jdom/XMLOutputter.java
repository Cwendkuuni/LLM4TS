package org.jdom2.output;

import java.util.*;
import org.jdom2.*;
import java.io.*;
import org.jdom2.output.support.*;

public final class XMLOutputter implements Cloneable
{
    private static final DefaultXMLProcessor DEFAULTPROCESSOR;
    private Format myFormat;
    private XMLOutputProcessor myProcessor;
    
    private static final Writer makeWriter(final OutputStream out, final Format format) throws UnsupportedEncodingException {
        return new BufferedWriter(new OutputStreamWriter(new BufferedOutputStream(out), format.getEncoding()));
    }
    
    public XMLOutputter(final Format format, final XMLOutputProcessor processor) {
        this.myFormat = null;
        this.myProcessor = null;
        this.myFormat = ((format == null) ? Format.getRawFormat() : format.clone());
        this.myProcessor = ((processor == null) ? XMLOutputter.DEFAULTPROCESSOR : processor);
    }
    
    public XMLOutputter() {
        this(null, null);
    }
    
    public XMLOutputter(final XMLOutputter that) {
        this(that.myFormat, null);
    }
    
    public XMLOutputter(final Format format) {
        this(format, null);
    }
    
    public XMLOutputter(final XMLOutputProcessor processor) {
        this(null, processor);
    }
    
    public void setFormat(final Format newFormat) {
        this.myFormat = newFormat.clone();
    }
    
    public Format getFormat() {
        return this.myFormat;
    }
    
    public XMLOutputProcessor getXMLOutputProcessor() {
        return this.myProcessor;
    }
    
    public void setXMLOutputProcessor(final XMLOutputProcessor processor) {
        this.myProcessor = processor;
    }
    
    public final void output(final Document doc, final OutputStream out) throws IOException {
        this.output(doc, makeWriter(out, this.myFormat));
    }
    
    public final void output(final DocType doctype, final OutputStream out) throws IOException {
        this.output(doctype, makeWriter(out, this.myFormat));
    }
    
    public final void output(final Element element, final OutputStream out) throws IOException {
        this.output(element, makeWriter(out, this.myFormat));
    }
    
    public final void outputElementContent(final Element element, final OutputStream out) throws IOException {
        this.outputElementContent(element, makeWriter(out, this.myFormat));
    }
    
    public final void output(final List<? extends Content> list, final OutputStream out) throws IOException {
        this.output(list, makeWriter(out, this.myFormat));
    }
    
    public final void output(final CDATA cdata, final OutputStream out) throws IOException {
        this.output(cdata, makeWriter(out, this.myFormat));
    }
    
    public final void output(final Text text, final OutputStream out) throws IOException {
        this.output(text, makeWriter(out, this.myFormat));
    }
    
    public final void output(final Comment comment, final OutputStream out) throws IOException {
        this.output(comment, makeWriter(out, this.myFormat));
    }
    
    public final void output(final ProcessingInstruction pi, final OutputStream out) throws IOException {
        this.output(pi, makeWriter(out, this.myFormat));
    }
    
    public void output(final EntityRef entity, final OutputStream out) throws IOException {
        this.output(entity, makeWriter(out, this.myFormat));
    }
    
    public final String outputString(final Document doc) {
        final StringWriter out = new StringWriter();
        try {
            this.output(doc, out);
        }
        catch (IOException ex) {}
        return out.toString();
    }
    
    public final String outputString(final DocType doctype) {
        final StringWriter out = new StringWriter();
        try {
            this.output(doctype, out);
        }
        catch (IOException ex) {}
        return out.toString();
    }
    
    public final String outputString(final Element element) {
        final StringWriter out = new StringWriter();
        try {
            this.output(element, out);
        }
        catch (IOException ex) {}
        return out.toString();
    }
    
    public final String outputString(final List<? extends Content> list) {
        final StringWriter out = new StringWriter();
        try {
            this.output(list, out);
        }
        catch (IOException ex) {}
        return out.toString();
    }
    
    public final String outputString(final CDATA cdata) {
        final StringWriter out = new StringWriter();
        try {
            this.output(cdata, out);
        }
        catch (IOException ex) {}
        return out.toString();
    }
    
    public final String outputString(final Text text) {
        final StringWriter out = new StringWriter();
        try {
            this.output(text, out);
        }
        catch (IOException ex) {}
        return out.toString();
    }
    
    public final String outputString(final Comment comment) {
        final StringWriter out = new StringWriter();
        try {
            this.output(comment, out);
        }
        catch (IOException ex) {}
        return out.toString();
    }
    
    public final String outputString(final ProcessingInstruction pi) {
        final StringWriter out = new StringWriter();
        try {
            this.output(pi, out);
        }
        catch (IOException ex) {}
        return out.toString();
    }
    
    public final String outputString(final EntityRef entity) {
        final StringWriter out = new StringWriter();
        try {
            this.output(entity, out);
        }
        catch (IOException ex) {}
        return out.toString();
    }
    
    public final String outputElementContentString(final Element element) {
        final StringWriter out = new StringWriter();
        try {
            this.outputElementContent(element, out);
        }
        catch (IOException ex) {}
        return out.toString();
    }
    
    public final void output(final Document doc, final Writer out) throws IOException {
        this.myProcessor.process(out, this.myFormat, doc);
        out.flush();
    }
    
    public final void output(final DocType doctype, final Writer out) throws IOException {
        this.myProcessor.process(out, this.myFormat, doctype);
        out.flush();
    }
    
    public final void output(final Element element, final Writer out) throws IOException {
        this.myProcessor.process(out, this.myFormat, element);
        out.flush();
    }
    
    public final void outputElementContent(final Element element, final Writer out) throws IOException {
        this.myProcessor.process(out, this.myFormat, element.getContent());
        out.flush();
    }
    
    public final void output(final List<? extends Content> list, final Writer out) throws IOException {
        this.myProcessor.process(out, this.myFormat, list);
        out.flush();
    }
    
    public final void output(final CDATA cdata, final Writer out) throws IOException {
        this.myProcessor.process(out, this.myFormat, cdata);
        out.flush();
    }
    
    public final void output(final Text text, final Writer out) throws IOException {
        this.myProcessor.process(out, this.myFormat, text);
        out.flush();
    }
    
    public final void output(final Comment comment, final Writer out) throws IOException {
        this.myProcessor.process(out, this.myFormat, comment);
        out.flush();
    }
    
    public final void output(final ProcessingInstruction pi, final Writer out) throws IOException {
        this.myProcessor.process(out, this.myFormat, pi);
        out.flush();
    }
    
    public final void output(final EntityRef entity, final Writer out) throws IOException {
        this.myProcessor.process(out, this.myFormat, entity);
        out.flush();
    }
    
    public String escapeAttributeEntities(final String str) {
        return XMLOutputter.DEFAULTPROCESSOR.escapeAttributeEntities(str, this.myFormat);
    }
    
    public String escapeElementEntities(final String str) {
        return XMLOutputter.DEFAULTPROCESSOR.escapeElementEntities(str, this.myFormat);
    }
    
    public XMLOutputter clone() {
        try {
            return (XMLOutputter)super.clone();
        }
        catch (CloneNotSupportedException e) {
            throw new RuntimeException(e.toString());
        }
    }
    
    public String toString() {
        final StringBuilder buffer = new StringBuilder();
        buffer.append("XMLOutputter[omitDeclaration = ");
        buffer.append(this.myFormat.omitDeclaration);
        buffer.append(", ");
        buffer.append("encoding = ");
        buffer.append(this.myFormat.encoding);
        buffer.append(", ");
        buffer.append("omitEncoding = ");
        buffer.append(this.myFormat.omitEncoding);
        buffer.append(", ");
        buffer.append("indent = '");
        buffer.append(this.myFormat.indent);
        buffer.append("'");
        buffer.append(", ");
        buffer.append("expandEmptyElements = ");
        buffer.append(this.myFormat.expandEmptyElements);
        buffer.append(", ");
        buffer.append("lineSeparator = '");
        for (final char ch : this.myFormat.lineSeparator.toCharArray()) {
            switch (ch) {
                case '\r': {
                    buffer.append("\\r");
                    break;
                }
                case '\n': {
                    buffer.append("\\n");
                    break;
                }
                case '\t': {
                    buffer.append("\\t");
                    break;
                }
                default: {
                    buffer.append("[" + (int)ch + "]");
                    break;
                }
            }
        }
        buffer.append("', ");
        buffer.append("textMode = ");
        buffer.append(this.myFormat.mode + "]");
        return buffer.toString();
    }
    
    static {
        DEFAULTPROCESSOR = new DefaultXMLProcessor();
    }
    
    private static final class DefaultXMLProcessor extends AbstractXMLOutputProcessor
    {
        public String escapeAttributeEntities(final String str, final Format format) {
            final StringWriter sw = new StringWriter();
            try {
                super.attributeEscapedEntitiesFilter(sw, new FormatStack(format), str);
            }
            catch (IOException ex) {}
            return sw.toString();
        }
        
        public final String escapeElementEntities(final String str, final Format format) {
            return Format.escapeText(format.getEscapeStrategy(), format.getLineSeparator(), str);
        }
    }
}
